<template>
  <a-layout-header class="header">
    <div class="logo">
      <!--使用 router-link标签+to来跳转页面，相当于a标签+href-->
      <router-link to="/welcome" style="color: white; font-size: 18px">
        12306铁路售票系统
      </router-link>
    </div>
    <div style="float: right; color: white;">
      您好：{{ member.mobile }} &nbsp;&nbsp;
      <a-button type="primary" @click="logout">退出登录</a-button>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined/> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined/> &nbsp; 乘客管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <border-outer-outlined/> &nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/myticket">
        <router-link to="/myticket">
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
          <desktop-outlined/> &nbsp; 控制台管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import store from "@/store";
import router from "@/router";

export default defineComponent({
  name: "ConsoleHeaderView",
  setup() {
    let member = store.state.member;
    // ref和reactive都用于定义响应式变量
    // ref用来声明基本的数据类型，reactive用来声明对象或对象数组
    const selectedKeys = ref([])
    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    const logout = () => {
      store.commit("setMember", {})
      router.push("/login");
    };
    return {
      member,
      selectedKeys,
      logout
    }
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.logo {
  float: left;
  height: 31px;
  width: 200px;
  color: white;
  font-size: 20px;
}
</style>
