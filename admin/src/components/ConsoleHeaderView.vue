<template>
  <a-layout-header class="header">
    <div class="logo">
      <!--使用 router-link标签+to来跳转页面，相当于a标签+href-->
      <router-link to="/welcome" style="color: white; font-size: 18px">
        12306铁路售票系统
      </router-link>
    </div>
    <div style="float: right; color: white;">
      欢迎使用控制台管理
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
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined/> &nbsp; 关于
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script>
import {defineComponent, ref, watch} from 'vue';
import router from "@/router";

export default defineComponent({
  name: "ConsoleHeaderView",
  setup() {
    // ref和reactive都用于定义响应式变量
    // ref用来声明基本的数据类型，reactive用来声明对象或对象数组
    const selectedKeys = ref([])
    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    return {
      selectedKeys,
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
