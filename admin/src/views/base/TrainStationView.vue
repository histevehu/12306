<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainStations"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
    </template>
  </a-table>
  <a-modal v-model:visible="visible" title="火车车站" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="trainStation" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="车次编号">
        <!--可向组件传入自定义参数width，也可以写作 :width="'100%'"-->
        <!--可向组件传入自定义事件，例如 @change="XXX"-->
        <!--注意：v-model:value这个是给value这个属性赋值，你可以把value看成是一个普通的属性，再换成另一个普通的属性，写法是一样的，比如我们自定义组件有加v-model:width，就是给width属性赋值。
        而我们自定义的组件里没有value属性，所以不写成v-model:value。antdv的组件都用了v-model:value，因为这些组件都绑定了value属性-->
        <TrainSelectView v-model="trainStation.trainCode" width="100%" @change=""></TrainSelectView>
      </a-form-item>
      <a-form-item label="站序">
        <a-input v-model:value="trainStation.index"/>
      </a-form-item>
      <a-form-item label="站名">
        <StationSelectView v-model="trainStation.name"/>
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="trainStation.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="进站时间">
        <a-time-picker v-model:value="trainStation.inTime" valueFormat="HH:mm:ss"
                       placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="出站时间">
        <a-time-picker v-model:value="trainStation.outTime" valueFormat="HH:mm:ss"
                       placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="停站时长">
        <a-time-picker v-model:value="trainStation.stopTime" valueFormat="HH:mm:ss"
                       placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="里程（公里）">
        <a-input v-model:value="trainStation.km"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, ref, watch} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import {pinyin} from "pinyin-pro";
import TrainSelectView from "@/components/TrainSelect.vue";
import StationSelectView from "@/components/StationSelect.vue";
import dayjs from "dayjs";

export default defineComponent({
  name: "TrainStationView",
  components: {StationSelectView, TrainSelectView},
  setup() {
    const visible = ref(false);
    let trainStation = ref({
      id: undefined,
      trainCode: undefined,
      index: undefined,
      name: undefined,
      namePinyin: undefined,
      inTime: undefined,
      outTime: undefined,
      stopTime: undefined,
      km: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const trainStations = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params = ref({
      trainCode: null
    });
    const columns = [
      {
        title: '车次编号',
        dataIndex: 'trainCode',
        key: 'trainCode',
      },
      {
        title: '站序',
        dataIndex: 'index',
        key: 'index',
      },
      {
        title: '站名',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '站名拼音',
        dataIndex: 'namePinyin',
        key: 'namePinyin',
      },
      {
        title: '进站时间',
        dataIndex: 'inTime',
        key: 'inTime',
      },
      {
        title: '出站时间',
        dataIndex: 'outTime',
        key: 'outTime',
      },
      {
        title: '停站时长',
        dataIndex: 'stopTime',
        key: 'stopTime',
      },
      {
        title: '里程（公里）',
        dataIndex: 'km',
        key: 'km',
      },
      {
        title: '操作',
        dataIndex: 'operation'
      }
    ];
    watch(() => trainStation.value.name, () => {
      if (Tool.isNotEmpty(trainStation.value.name)) {
        trainStation.value.namePinyin = pinyin(trainStation.value.name, {toneType: 'none'}).replaceAll(" ", "");
      } else {
        trainStation.value.namePinyin = "";
      }
    }, {immediate: true});

    // 自动计算停车时长
    watch(() => trainStation.value.inTime, () => {
      // 停车时间=出站时间-入站时间，单位：秒
      let diff = dayjs(trainStation.value.outTime, 'HH:mm:ss').diff(dayjs(trainStation.value.inTime, 'HH:mm:ss'), 'seconds');
      // stopTime=0:0:0+停车时间
      trainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    }, {immediate: true});
    watch(() => trainStation.value.outTime, () => {
      // 停车时间=出站时间-入站时间
      let diff = dayjs(trainStation.value.outTime, 'HH:mm:ss').diff(dayjs(trainStation.value.inTime, 'HH:mm:ss'), 'seconds');
      trainStation.value.stopTime = dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    }, {immediate: true});

    const onAdd = () => {
      trainStation.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      trainStation.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/trainStation/delete/" + record.id).then((response) => {
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
      axios.post("/business/admin/trainStation/save", trainStation.value).then((response) => {
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
      axios.get("/business/admin/trainStation/queryList", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode,
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          trainStations.value = data.content.list;
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
      trainStation,
      visible,
      trainStations,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      params
    };
  },
});
</script>
