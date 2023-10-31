<template>
  <div class="job">
    <p>
      <a-button type="primary" @click="handleAdd()">
        新增
      </a-button>&nbsp;
      <a-button type="primary" @click="handleQuery()">
        刷新
      </a-button>
    </p>
    <a-table :dataSource="jobs"
             :columns="columns"
             :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-popconfirm
                title="手动执行会立即执行一次，确定执行？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleRun(record)"
            >
              <a-button type="primary" size="small">
                手动执行
              </a-button>
            </a-popconfirm>
            <a-popconfirm
                title="确定重启？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleResume(record)"
            >
              <a-button v-show="record.state === 'PAUSED' || record.state === 'ERROR'" type="primary" size="small">
                重启
              </a-button>
            </a-popconfirm>
            <a-popconfirm
                title="确定暂停？"
                ok-text="是"
                cancel-text="否"
                @confirm="handlePause(record)"
            >
              <a-button v-show="record.state === 'NORMAL' || record.state === 'BLOCKED'" type="primary" size="small">
                暂停
              </a-button>
            </a-popconfirm>
            <a-button type="primary" @click="handleEdit(record)" size="small">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record)"
            >
              <a-button type="danger" size="small">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal
        title="用户"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
      <a-form :model="job" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="类名">
          <a-input v-model:value="job.name"/>
        </a-form-item>
        <a-form-item label="描述">
          <a-input v-model:value="job.description"/>
        </a-form-item>
        <a-form-item label="分组">
          <a-input v-model:value="job.group" :disabled="!!job.state"/>
        </a-form-item>
        <a-form-item label="表达式">
          <a-input v-model:value="job.cronExpression"/>
          <div class="ant-alert ant-alert-success">
            每5秒执行一次：0/5 * * * * ?
            <br>
            每5分钟执行一次：* 0/5 * * * ?
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {notification} from 'ant-design-vue';

export default defineComponent({
  name: 'batch-job-view',
  setup() {
    const jobs = ref();
    const loading = ref();

    const columns = [{
      title: '分组',
      dataIndex: 'group',
    }, {
      title: '类名',
      dataIndex: 'name',
    }, {
      title: '描述',
      dataIndex: 'description',
    }, {
      title: '状态',
      dataIndex: 'state',
    }, {
      title: '表达式',
      dataIndex: 'cronExpression',
    }, {
      title: '上次时间',
      dataIndex: 'preFireTime',
    }, {
      title: '下次时间',
      dataIndex: 'nextFireTime',
    }, {
      title: '操作',
      dataIndex: 'operation'
    }];

    const handleQuery = () => {
      loading.value = true;
      jobs.value = [];
      axios.get('/batch/admin/job/query').then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          jobs.value = data.content;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    // -------- 表单 ---------
    const job = ref();
    job.value = {};
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      let url = "add";
      if (job.value.state) {
        url = "reschedule";
      }
      axios.post('/batch/admin/job/' + url, job.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success) {
          modalVisible.value = false;
          notification.success({description: "保存成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    /**
     * 新增
     */
    const handleAdd = () => {
      modalVisible.value = true;
      job.value = {
        group: 'DEFAULT'
      };
    };

    /**
     * 编辑
     */
    const handleEdit = (record) => {
      modalVisible.value = true;
      job.value = Tool.copy(record);
    };

    /**
     * 删除
     */
    const handleDelete = (record) => {
      axios.post('/batch/admin/job/delete', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "删除成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    /**
     * 暂停
     */
    const handlePause = (record) => {
      axios.post('/batch/admin/job/pause', {
        name: record.name,
        group: record.group
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "暂停成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    /**
     * 重启
     */
    const handleResume = (record) => {
      axios.post('/batch/admin/job/reschedule', record).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success) {
          modalVisible.value = false;
          notification.success({description: "重启成功！"});
          handleQuery();
        } else {
          notification.error({description: data.message});
        }
      });
    };

    /**
     * 手动执行
     */
    const handleRun = (record) => {
      axios.post('/batch/admin/job/run', record).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "手动执行成功！"});
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const getEnumValue = (key, obj) => {
      return Tool.getEnumValue(key, obj);
    };

    onMounted(() => {
      console.log('index mounted!');
      handleQuery();
    });

    return {
      columns,
      jobs,
      loading,
      handleQuery,

      handleAdd,
      handleEdit,
      handleDelete,
      handleResume,
      handlePause,
      job,
      modalVisible,
      modalLoading,
      handleModalOk,
      getEnumValue,
      handleRun
    };
  }
})
</script>

<style scoped>
</style>