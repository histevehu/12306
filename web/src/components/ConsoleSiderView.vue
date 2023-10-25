<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    >
      <a-menu-item key="/">
        <router-link to="/">
          <coffee-outlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined /> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <border-outer-outlined /> &nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/my-ticket">
        <router-link to="/my-ticket">
          <idcard-outlined /> &nbsp; 我的车票
        </router-link>
      </a-menu-item>
      <a-menu-item key="/seat">
        <router-link to="/seat">
          <usergroup-add-outlined /> &nbsp; 座位销售图
        </router-link>
      </a-menu-item>
      <a-menu-item key="/admin">
        <router-link to="/admin">
          <desktop-outlined /> &nbsp; 关于控台管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import {defineComponent} from 'vue';

export default defineComponent({
  name: "ConsoleSiderView",
  setup() {

  },
});
</script>

<!-- 使用scoped属性使css样式限制在当前组件-->
<style scoped>

</style>
