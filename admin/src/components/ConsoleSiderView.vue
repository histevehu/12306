<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
    <!--open-keys为默认展开的多级菜单key-->
    open-keys="['batch']"
    >
    <a-menu-item key="/station">
      <router-link to="/station">
        <MenuUnfoldOutlined/>
        车站管理
      </router-link>
    </a-menu-item>
    <a-menu-item key="/train">
      <router-link to="/train">
        <MenuUnfoldOutlined/>
        火车管理
      </router-link>
    </a-menu-item>
    <a-menu-item key="/trainStation">
      <router-link to="/trainStation">
        <MenuUnfoldOutlined/>
        火车车站管理
      </router-link>
    </a-menu-item>
    <a-menu-item key="/trainCarriage">
      <router-link to="/trainCarriage">
        <MenuUnfoldOutlined/>
        火车车厢管理
      </router-link>
    </a-menu-item>
    <a-menu-item key="/trainSeat">
      <router-link to="/trainSeat">
        <MenuUnfoldOutlined/>
        座位管理
      </router-link>
    </a-menu-item>
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
