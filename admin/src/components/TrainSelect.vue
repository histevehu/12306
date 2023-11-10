<template>
  <!--v-model:value和外部的父组件关联-->
  <a-select v-model:value="trainCode" show-search allowClear
            :filterOption="filterTrainCodeOption"
            @change="onChange" placeholder="请选择车次"
            :style="'width: ' + localWidth">
    <!--:label属性为自定义属性-->
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code"
                     :label="item.code + item.start + item.end">
      {{ item.code }} {{ item.start }} ~ {{ item.end }}
    </a-select-option>
  </a-select>
</template>

<script>

import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "TrainSelectView",
  // props区域用于定义父子组件参数传递的属性：
  // modelValue为Vue内置变量
  props: ["modelValue", "width"],
  // emits内定义触发事件：
  // "change"为自定义事件，可暴露给父组件来使用
  emits: ['update:modelValue', 'change'],
  setup(props, {emit}) {
    const trainCode = ref();
    const trains = ref([]);
    // 读取父组件传入的组件宽度
    const localWidth = ref(props.width);
    if (Tool.isEmpty(props.width)) {
      localWidth.value = "100%";
    }

    // 利用watch，动态获取父组件的值，如果放在onMounted或其它方法里，则只有第一次有效
    // 将车次编号通过modelValue传递到子组件，再在子组件中，通过watch为组件内的响应式变量traincode赋值，从而让下拉框选择车次
    watch(() => props.modelValue, () => {
      console.log("props.modelValue", props.modelValue);
      trainCode.value = props.modelValue;
    }, {immediate: true});

    /**
     * 查询所有的车次，用于车次下拉框
     */
    const queryAllTrain = () => {
      // 引入前段缓存，使一些数据相对固定的界面（比如车站车次查询）每次加载后读取本地缓存而不必发起查询请求
      let list = SessionStorage.get(SESSION_ALL_TRAIN);
      // 若缓存中有数据，使用缓存数据
      if (Tool.isNotEmpty(list)) {
        console.log("queryAllTrain 读取缓存");
        trains.value = list;
      } else {
        axios.get("/business/admin/train/queryAll").then((response) => {
          let data = response.data;
          if (data.success) {
            trains.value = data.content;
            console.log("queryAllTrain 保存缓存");
            SessionStorage.set(SESSION_ALL_TRAIN, trains.value);
          } else {
            notification.error({description: data.message});
          }
        });
      }
    };

    /**
     * 车次下拉框筛选
     */
    const filterTrainCodeOption = (input, option) => {
      console.log(input, option);
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    /**
     * 将当前组件的值响应给父组件
     * @param value
     */
    const onChange = (value) => {
      // emit(触发事件名，属性值)
      emit('update:modelValue', value);
      // 功能扩展，可以将子组件的事件暴露给父组件来使用
      let train = trains.value.filter(item => item.code === value)[0];
      if (Tool.isEmpty(train)) {
        train = {};
      }
      emit('change', train);
    };

    onMounted(() => {
      queryAllTrain();
    });

    return {
      trainCode,
      trains,
      filterTrainCodeOption,
      onChange,
      localWidth
    };
  },
});
</script>
