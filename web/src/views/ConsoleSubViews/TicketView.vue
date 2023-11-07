<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" :disabled-date="disabledDate"
                     placeholder="请选择日期"></a-date-picker>
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
        <a-space>
          <a-button type="primary" @click="toOrder(record)" :disabled="isExpire(record)">
            {{ isExpire(record) ? "过期" : "预订" }}
          </a-button>
          <router-link :to="{
            path: '/seat',
            query: {
              date: record.date,
              trainCode: record.trainCode,
              start: record.start,
              startIndex: record.startIndex,
              end: record.end,
              endIndex: record.endIndex
            }
          }">
            <a-button type="primary">座位销售图</a-button>
          </router-link>
          <a-button type="primary" @click="showStation(record)">途经车站</a-button>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'station'">
        {{ record.start }}<br/>
        {{ record.end }}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        {{ record.startTime }}<br/>
        {{ record.endTime }}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{ calDuration(record.startTime, record.endTime) }}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <div v-if="record.ydz >= 0">
          {{ record.ydz }}<br/>
          {{ record.ydzPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          {{ record.edz }}<br/>
          {{ record.edzPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          {{ record.rw }}<br/>
          {{ record.rwPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          {{ record.yw }}<br/>
          {{ record.ywPrice }}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>

  <!-- 途经车站 -->
  <a-modal style="top: 30px" v-model:visible="visible" :title="null" :footer="null" :closable="false">
    <a-table :data-source="stations" :pagination="false">
      <a-table-column key="index" title="站序" data-index="index"/>
      <a-table-column key="name" title="站名" data-index="name"/>
      <a-table-column key="inTime" title="进站时间" data-index="inTime">
        <template #default="{ record }">
          {{ record.index === 0 ? '-' : record.inTime }}
        </template>
      </a-table-column>
      <a-table-column key="outTime" title="出站时间" data-index="outTime">
        <template #default="{ record }">
          {{ record.index === (stations.length - 1) ? '-' : record.outTime }}
        </template>
      </a-table-column>
      <a-table-column key="stopTime" title="停站时长" data-index="stopTime">
        <template #default="{ record }">
          {{ record.index === 0 || record.index === (stations.length - 1) ? '-' : record.stopTime }}
        </template>
      </a-table-column>
    </a-table>
  </a-modal>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import StationSelectView from "@/components/StationSelect";
import dayjs from "dayjs";
import router from "@/router";

export default defineComponent({
  name: "ticket-view",
  components: {StationSelectView},
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
      {
        title: '一等座',
        dataIndex: 'ydz',
        key: 'ydz',
      },
      {
        title: '二等座',
        dataIndex: 'edz',
        key: 'edz',
      },
      {
        title: '软卧',
        dataIndex: 'rw',
        key: 'rw',
      },
      {
        title: '硬卧',
        dataIndex: 'yw',
        key: 'yw',
      },
      {
        title: '操作',
        dataIndex: 'operation',
      },
    ];


    const handleQuery = (param) => {
      if (Tool.isEmpty(params.value.date)) {
        notification.error({description: "请输入日期"});
        return;
      }
      if (Tool.isEmpty(params.value.start)) {
        notification.error({description: "请输入出发地"});
        return;
      }
      if (Tool.isEmpty(params.value.end)) {
        notification.error({description: "请输入目的地"});
        return;
      }
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }

      // 保存查询参数，这样刷新以后查询参数仍然保留
      SessionStorage.set(SESSION_TICKET_PARAMS, params.value);

      loading.value = true;
      axios.get("/business/dailyTrainTicket/queryList", {
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

    const toOrder = (record) => {
      dailyTrainTicket.value = Tool.copy(record);
      SessionStorage.set(SESSION_ORDER, dailyTrainTicket.value);
      router.push("/order")
    };

    // ---------------------- 途经车站 ----------------------
    const stations = ref([]);
    const showStation = record => {
      visible.value = true;
      axios.get("/business/dailyTrainStation/queryByTrainCode", {
        params: {
          date: record.date,
          trainCode: record.trainCode
        }
      }).then((response) => {
        let data = response.data;
        if (data.success) {
          stations.value = data.content;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    // 不能选择今天以前及两周以后的日期
    const disabledDate = current => {
      return current && (current <= dayjs().add(-1, 'day') || current > dayjs().add(14, 'day'));
    };

    // 判断是否过期
    const isExpire = (record) => {
      // 标准时间：2000/01/01 00:00:00
      let startDateTimeString = record.date.replace(/-/g, "/") + " " + record.startTime;
      let startDateTime = new Date(startDateTimeString);

      //当前时间
      let now = new Date();

      console.log(startDateTime)
      return now.valueOf() >= startDateTime.valueOf();
    };

    onMounted(() => {
      // 加载查询参数
      //  "|| {}"是常用技巧，可以避免空指针异常
      params.value = SessionStorage.get(SESSION_TICKET_PARAMS) || {};
      if (Tool.isNotEmpty(params.value)) {
        handleQuery({
          page: 1,
          size: pagination.value.pageSize
        });
      }
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
      calDuration,
      toOrder,
      showStation,
      stations,
      disabledDate,
      isExpire
    };
  },
});
</script>