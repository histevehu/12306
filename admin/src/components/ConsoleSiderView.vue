<template>
  <a-layout-sider width="200" style="background: #fff">
    <!--open-keys为默认展开的多级菜单key-->
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
        :open-keys="['base','business','batch','member']"
    >
      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedListOutlined/>
            基础信息
          </span>
        </template>
        <a-menu-item key="/base/station">
          <router-link to="/base/station">
            <MenuUnfoldOutlined/>
            车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train">
          <router-link to="/base/train">
            <MenuUnfoldOutlined/>
            火车管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/trainStation">
          <router-link to="/base/trainStation">
            <MenuUnfoldOutlined/>
            火车车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/trainCarriage">
          <router-link to="/base/trainCarriage">
            <MenuUnfoldOutlined/>
            火车车厢管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/trainSeat">
          <router-link to="/base/trainSeat">
            <MenuUnfoldOutlined/>
            座位管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="member">
        <template #title>
          <span>
            <UnorderedListOutlined/>
            会员管理
          </span>
        </template>
        <a-menu-item key="/member/ticket">
          <router-link to="/member/ticket">
            <MenuUnfoldOutlined/>
            会员车票
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="business">
        <template #title>
          <span>
            <UnorderedListOutlined/>
            业务管理
          </span>
        </template>
        <a-menu-item key="/business/confirmOrder">
          <router-link to="/business/confirmOrder">
            <MenuUnfoldOutlined/>
            订单信息
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/dailyTrainTicket">
          <router-link to="/business/dailyTrainTicket">
            <MenuUnfoldOutlined/>
            余票信息
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/dailyTrain">
          <router-link to="/business/dailyTrain">
            <MenuUnfoldOutlined/>
            每日车次管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/dailyTrainStation">
          <router-link to="/business/dailyTrainStation">
            <MenuUnfoldOutlined/>
            每日车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/dailyTrainCarriage">
          <router-link to="/business/dailyTrainCarriage">
            <MenuUnfoldOutlined/>
            每日车厢管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/dailyTrainSeat">
          <router-link to="/business/dailyTrainSeat">
            <MenuUnfoldOutlined/>
            每日座位管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>

      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined/>
            跑批管理
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <MenuUnfoldOutlined/>
            任务管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>

    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import router from "@/router";

export default defineComponent({
  name: "ConsoleSiderView",
  setup() {
    const selectedKeys = ref([]);
    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    return {
      selectedKeys
    };
  },
});
</script>

<!-- 使用scoped属性使css样式限制在当前组件-->
<style scoped>

</style>
