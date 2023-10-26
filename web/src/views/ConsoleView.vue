<template>
  <a-layout id="components-layout-demo-top-side-2">
    <consoleHeaderView></consoleHeaderView>
    <a-layout>
      <consoleSiderView></consoleSiderView>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <!--<router-view>为动态部分，显示子路由内容-->
        <router-view></router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import {defineComponent} from 'vue';
import ConsoleHeaderView from "@/components/ConsoleHeaderView.vue";
import ConsoleSiderView from "@/components/ConsoleSiderView.vue";

export default defineComponent({
  components: {
    ConsoleSiderView,
    ConsoleHeaderView,
  },
  setup() {
    return {};
  },
});
</script>
<style>

</style>