<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>

    </a-space>
  </p>
  <a-table :dataSource="confirmOrders"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex === 'status'">
        <span v-for="item in CONFIRM_ORDER_STATUS_ARRAY" :key="item.code">
          <span v-if="item.code === record.status">
            {{ item.desc }}
          </span>
        </span>
      </template>
    </template>
  </a-table>
</template>

<script>
import {defineComponent, onMounted, ref} from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";

export default defineComponent({
  name: "confirmOrderView",
  setup() {
    const CONFIRM_ORDER_STATUS_ARRAY = window.CONFIRM_ORDER_STATUS_ARRAY;
    const visible = ref(false);
    let confirmOrder = ref({
      id: undefined,
      memberId: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      end: undefined,
      dailyTrainTicketId: undefined,
      tickets: undefined,
      status: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const confirmOrders = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    const columns = [
      {
        title: '会员id',
        dataIndex: 'memberId',
        key: 'memberId',
      },
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
        title: '出发站',
        dataIndex: 'start',
        key: 'start',
      },
      {
        title: '到达站',
        dataIndex: 'end',
        key: 'end',
      },
      {
        title: '余票ID',
        dataIndex: 'dailyTrainTicketId',
        key: 'dailyTrainTicketId',
      },
      {
        title: '车票',
        dataIndex: 'tickets',
        key: 'tickets',
      },
      {
        title: '订单状态',
        dataIndex: 'status',
        key: 'status',
      },
    ];


    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/confirmOrder/queryList", {
        params: {
          page: param.page,
          size: param.size
        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.success) {
          confirmOrders.value = data.content.list;
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
      CONFIRM_ORDER_STATUS_ARRAY,
      confirmOrder,
      visible,
      confirmOrders,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
    };
  },
});
</script>
