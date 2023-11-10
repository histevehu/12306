<template>
  <a-select v-model:value="name" show-search allowClear
            :filterOption="filterNameOption"
            @change="onChange" placeholder="请选择车站"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :label="item.name + item.namePinyin + item.namePy">
      {{item.name}} {{item.namePinyin}} ~ {{item.namePy}}
    </a-select-option>
  </a-select>
</template>

<script>

import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "StationSelectView",
  props: ["modelValue", "width"],
  emits: ['update:modelValue', 'change'],
  setup(props, {emit}) {
    const name = ref();
    const stations = ref([]);
    const localWidth = ref(props.width);
    if (Tool.isEmpty(props.width)) {
      localWidth.value = "100%";
    }

    // 利用watch，动态获取父组件的值，如果放在onMounted或其它方法里，则只有第一次有效
    watch(() => props.modelValue, ()=>{
      console.log("props.modelValue", props.modelValue);
      name.value = props.modelValue;
    }, {immediate: true});

    /**
     * 查询所有的车站，用于车站下拉框
     */
    const queryAllStation = () => {
      // 引入前段缓存，使一些数据相对固定的界面（比如车站车次查询）每次加载后读取本地缓存而不必发起查询请求
      let list = SessionStorage.get(SESSION_ALL_STATIONS);
      // 若缓存中有数据，使用缓存数据
      if (Tool.isNotEmpty(list)) {
        console.log("queryAllStation 读取缓存");
        stations.value = list;
      } else {
        axios.get("/business/admin/station/queryAll").then((response) => {
          let data = response.data;
          if (data.success) {
            stations.value = data.content;
            console.log("queryAllStation 保存缓存");
            SessionStorage.set(SESSION_ALL_STATIONS, stations.value);
          } else {
            notification.error({description: data.message});
          }
        });
      }
    };

    /**
     * 车站下拉框筛选
     */
    const filterNameOption = (input, option) => {
      console.log(input, option);
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    /**
     * 将当前组件的值响应给父组件
     * @param value
     */
    const onChange = (value) => {
      emit('update:modelValue', value);
      let station = stations.value.filter(item => item.code === value)[0];
      if (Tool.isEmpty(station)) {
        station = {};
      }
      emit('change', station);
    };

    onMounted(() => {
      queryAllStation();
    });

    return {
      name,
      stations,
      filterNameOption,
      onChange,
      localWidth
    };
  },
});
</script>
