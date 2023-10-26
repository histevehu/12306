<template>
  <a-layout id="components-layout-demo-top-side-2">
    <console-header-view></console-header-view>
    <a-layout>
      <console-sider-view></console-sider-view>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <router-view></router-view>
        平台用户总数: {{count}}
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import {defineComponent, ref} from 'vue';
import ConsoleHeaderView from "@/components/ConsoleHeaderView.vue";
import ConsoleSiderView from "@/components/ConsoleSiderView.vue";
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  components: {
    ConsoleSiderView,
    ConsoleHeaderView,
  },
  setup() {
    // ref和reactive都用于定义响应式变量
    // ref用来声明基本的数据类型，reactive用来声明对象或对象数组
    const count = ref(0)
    axios.get("/member/member/count").then((response) => {
      let data = response.data;
      if (data.success) {
        // ref类型变量取值、赋值必须要加.value
        count.value = data.content
      } else {
        notification.error({description: data.message});
      }
    })
    return {
      count
    };
  },
});
</script>
<style>

</style>
