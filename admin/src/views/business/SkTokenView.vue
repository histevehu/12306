<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
    </a-space>
  </p>
  <a-table :dataSource="skTokens"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a @click="onEdit(record)">修改令牌余量</a>
        </a-space>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="秒杀令牌" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="skToken" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="skToken.date" valueFormat="YYYY-MM-DD"
                       placeholder="请选择日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <a-input v-model:value="skToken.trainCode"/>
      </a-form-item>
      <a-form-item label="令牌余量">
        <a-input v-model:value="skToken.count"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";

export default defineComponent({
  name: "SkTokenView",
  setup() {
    const visible = ref(false);
    let skToken = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      count: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const skTokens = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
      {
        title: '日期',
        dataIndex: 'date',
        key: 'date',
      },
      {
        title: '车次编号',
        dataIndex: 'trainCode',
        key: 'trainCode',
      },
      {
        title: '令牌余量',
        dataIndex: 'count',
        key: 'count',
      },
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ];

    const onAdd = () => {
      skToken.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      skToken.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/skToken/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.success) {
          notification.success({description: "删除成功"});
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleOk = () => {
      axios.post("/business/admin/skToken/save", skToken.value).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({description: "保存成功"});
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/skToken/queryList", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          skTokens.value = data.content.list;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (page) => {
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      skToken,
      visible,
      skTokens,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete
    };
  },
});
</script>
