<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"></train-select-view>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"></a-date-picker>
      <station-select-view v-model="params.start" width="200px"></station-select-view>
      <station-select-view v-model="params.end" width="200px"></station-select-view>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <!--复杂的列使用自定义的template, 如列的计算、加单位、多列组件显示等-->
      <template v-else-if="column.dataIndex === 'station'">
        {{ record.start }}->{{ record.end }}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        {{ record.startTime }}~{{ record.endTime }}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{ calDuration(record.startTime, record.endTime) }}<br/>
<!--只是根据-->
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <!--若余票数量为-1，则证明该车次没有该类型的座位，显示"--"，与车票售完的情况相区别-->
        <div v-if="record.ydz >= 0">
          {{ record.ydz }}张<br/>
          {{ record.ydzPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          {{ record.edz }}张<br/>
          {{ record.edzPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          {{ record.rw }}张<br/>
          {{ record.rwPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          {{ record.yw }}张<br/>
          {{ record.ywPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/TrainSelect.vue";
import StationSelectView from "@/components/StationSelect.vue";
import dayjs from "dayjs";

export default defineComponent({
  name: "DailyTrainTicketView",
  components: {StationSelectView, TrainSelectView},
  setup() {
    const visible = ref(false);
    let dailyTrainTicket = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      startIndex: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      endIndex: undefined,
      ydz: undefined,
      ydzPrice: undefined,
      edz: undefined,
      edzPrice: undefined,
      rw: undefined,
      rwPrice: undefined,
      yw: undefined,
      ywPrice: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainTickets = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const params = ref({});
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
        title: '车站',
        dataIndex: 'station',
      },
      {
        title: '时间',
        dataIndex: 'time',
      },
      {
        title: '历时',
        dataIndex: 'duration',
      },
      // {
      //   title: '出发站',
      //   dataIndex: 'start',
      //   key: 'start',
      // },
      // {
      //   title: '出发站拼音',
      //   dataIndex: 'startPinyin',
      //   key: 'startPinyin',
      // },
      // {
      //   title: '出发时间',
      //   dataIndex: 'startTime',
      //   key: 'startTime',
      // },
      // {
      //   title: '出发站序',
      //   dataIndex: 'startIndex',
      //   key: 'startIndex',
      // },
      // {
      //   title: '到达站',
      //   dataIndex: 'end',
      //   key: 'end',
      // },
      // {
      //   title: '到达站拼音',
      //   dataIndex: 'endPinyin',
      //   key: 'endPinyin',
      // },
      // {
      //   title: '到站时间',
      //   dataIndex: 'endTime',
      //   key: 'endTime',
      // },
      // {
      //   title: '到站站序',
      //   dataIndex: 'endIndex',
      //   key: 'endIndex',
      // },
      {
        title: '一等座',
        dataIndex: 'ydz',
        key: 'ydz',
      },
      // {
      //   title: '一等座票价',
      //   dataIndex: 'ydzPrice',
      //   key: 'ydzPrice',
      // },
      {
        title: '二等座',
        dataIndex: 'edz',
        key: 'edz',
      },
      // {
      //   title: '二等座票价',
      //   dataIndex: 'edzPrice',
      //   key: 'edzPrice',
      // },
      {
        title: '软卧',
        dataIndex: 'rw',
        key: 'rw',
      },
      // {
      //   title: '软卧票价',
      //   dataIndex: 'rwPrice',
      //   key: 'rwPrice',
      // },
      {
        title: '硬卧',
        dataIndex: 'yw',
        key: 'yw',
      },
      // {
      //   title: '硬卧票价',
      //   dataIndex: 'ywPrice',
      //   key: 'ywPrice',
      // },
    ];


    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/dailyTrainTicket/queryList", {
        params: {
          page: param.page,
          size: param.size,
          trainCode: params.value.trainCode,
          date: params.value.date,
          start: params.value.start,
          end: params.value.end
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          dailyTrainTickets.value = data.content.list;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = data.content.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (page) => {
      // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };

    const calDuration = (startTime, endTime) => {
      let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds');
      return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      dailyTrainTicket,
      visible,
      dailyTrainTickets,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      params,
      calDuration
    };
  },
});
</script>
