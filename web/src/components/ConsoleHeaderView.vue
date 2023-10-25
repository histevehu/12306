<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/" style="color: white; font-size: 18px">
        12306铁路售票系统
      </router-link>
    </div>
    <div style="float: right; color: white;">
      您好
      <router-link to="/login" style="color: white;">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">
          <coffee-outlined/> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined/> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <border-outer-outlined/> &nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/my-ticket">
        <router-link to="/my-ticket">
          <idcard-outlined/> &nbsp; 我的车票
        </router-link>
      </a-menu-item>
      <a-menu-item key="/seat">
        <router-link to="/seat">
          <usergroup-add-outlined/> &nbsp; 座位销售图
        </router-link>
      </a-menu-item>
      <a-menu-item key="/admin">
        <router-link to="/admin">
          <desktop-outlined/> &nbsp; 关于控台管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script>
import {defineComponent} from 'vue';

export default defineComponent({
  name: "ConsoleHeaderView",
  setup() {

  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.logo {
  float: left;
  height: 31px;
  width: 150px;
  color: white;
  font-size: 20px;
}
</style>
