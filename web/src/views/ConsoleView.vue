<template>
  <a-layout id="components-layout-demo-top-side-2">
    <console-header-view></console-header-view>
    <a-layout>
      <console-sider-view></console-sider-view>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <router-view></router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script>
import { defineComponent } from 'vue';
import ConsoleHeaderView from "@/components/ConsoleHeaderView.vue";
import ConsoleSiderView from "@/components/ConsoleSiderView.vue";

export default defineComponent({
  components: {
    ConsoleSiderView,
    ConsoleHeaderView,
  },
  setup() {
    return {
    };
  },
});
</script>
<style>

</style>
