<template>
  <div class="order-train">
    <span class="order-train-main">{{ dailyTrainTicket.date }}</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.trainCode }}次</span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.start }}站</span>
    <span class="order-train-main">({{ dailyTrainTicket.startTime }})</span>&nbsp;
    <span class="order-train-main">-></span>&nbsp;
    <span class="order-train-main">{{ dailyTrainTicket.end }}站</span>
    <span class="order-train-main">({{ dailyTrainTicket.endTime }})</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{ item.desc }}</span>：
        <span class="order-train-ticket-main">{{ item.price }}￥</span>&nbsp;
        <span class="order-train-ticket-main">{{ item.count }}</span>&nbsp;张票&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
    </div>
  </div>
  <a-divider></a-divider>
  <b>勾选要购票的乘客：</b>&nbsp;
  <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions"/>

  <div class="order-tickets">
    <a-row class="order-tickets-header" v-if="tickets.length > 0">
      <a-col :span="2">乘客</a-col>
      <a-col :span="6">身份证</a-col>
      <a-col :span="4">票种</a-col>
      <a-col :span="4">座位类型</a-col>
    </a-row>
    <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
      <a-col :span="2">{{ ticket.passengerName }}</a-col>
      <a-col :span="6">{{ ticket.passengerIdCard }}</a-col>
      <a-col :span="4">
        <a-select v-model:value="ticket.passengerType" style="width: 100%">
          <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-col>
      <a-col :span="4">
        <a-select v-model:value="ticket.seatTypeCode" style="width: 100%">
          <a-select-option v-for="item in seatTypes" :key="item.code" :value="item.code">
            {{ item.desc }}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
  </div>
  <div v-if="tickets.length > 0">
    <a-button type="primary" size="large" @click="finishCheckPassenger">提交订单</a-button>
  </div>

  <a-modal v-model:visible="visible" title="请核对以下信息"
           style="top: 50px; width: 800px"
           ok-text="确认" cancel-text="取消"
           @ok="showFirstImageCodeModal">
    <div class="order-tickets">
      <a-row class="order-tickets-header" v-if="tickets.length > 0">
        <a-col :span="3">乘客</a-col>
        <a-col :span="15">身份证</a-col>
        <a-col :span="3">票种</a-col>
        <a-col :span="3">座位类型</a-col>
      </a-row>
      <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
        <a-col :span="3">{{ ticket.passengerName }}</a-col>
        <a-col :span="15">{{ ticket.passengerIdCard }}</a-col>
        <a-col :span="3">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === ticket.passengerType">
              {{ item.desc }}
            </span>
          </span>
        </a-col>
        <a-col :span="3">
          <span v-for="item in seatTypes" :key="item.code">
            <span v-if="item.code === ticket.seatTypeCode">
              {{ item.desc }}
            </span>
          </span>
        </a-col>
      </a-row>
      <br/>
      <div v-if="chooseSeatType === 0" style="color: red;">
        您购买的车票不支持选座
        <div>12306规则：只有全部是一等座或全部是二等座才支持选座</div>
        <div>12306规则：余票小于一定数量时，不允许选座（本项目以20为例）</div>
      </div>
      <div v-else style="text-align: center">
        <a-switch class="choose-seat-item" v-for="item in SEAT_COL_ARRAY" :key="item.code"
                  v-model:checked="chooseSeatObj[item.code + '1']" :checked-children="item.desc"
                  :un-checked-children="item.desc"/>
        <!--只有当够票数>1时才显示两排选座-->
        <div v-if="tickets.length > 1">
          <a-switch class="choose-seat-item" v-for="item in SEAT_COL_ARRAY" :key="item.code"
                    v-model:checked="chooseSeatObj[item.code + '2']" :checked-children="item.desc"
                    :un-checked-children="item.desc"/>
        </div>
        <div style="color: #999999">提示：您可以选择{{ tickets.length }}个座位</div>
      </div>
      <br>
      <div style="color: red">
        体验排队购票，加入多人一起排队购票：
        <a-input-number v-model:value="lineNumber" :min="0" :max="20"/>
      </div>
      <!--<br/>-->
      <!--最终购票：{{tickets}}-->
      <!--最终选座：{{chooseSeatObj}}-->
    </div>
  </a-modal>

  <!-- 第二层验证码 后端 -->
  <a-modal v-model:visible="imageCodeModalVisible" :title="null" :footer="null" :closable="false"
           style="top: 50px; width: 400px">
    <p style="text-align: center; font-weight: bold; font-size: 18px">
      使用服务端验证码削弱瞬时高峰<br/>
      防止机器人刷票
    </p>
    <p>
      <a-input v-model:value="imageCode" placeholder="图片验证码">
        <template #suffix>
          <img v-show="!!imageCodeSrc" :src="imageCodeSrc" alt="验证码" v-on:click="loadImageCode()"/>
        </template>
      </a-input>
    </p>
    <a-button type="danger" block :loading="btn_buying" @click="handleOk">输入验证码后开始购票</a-button>
  </a-modal>

  <!-- 第一层验证码 纯前端 -->
  <a-modal v-model:visible="firstImageCodeModalVisible" :title="null" :footer="null" :closable="false"
           style="top: 50px; width: 400px">
    <p style="text-align: center; font-weight: bold; font-size: 18px">
      使用纯前端验证码削弱瞬时高峰<br/>
      减小后端验证码接口的压力
    </p>
    <p>
      <a-input v-model:value="firstImageCodeTarget" placeholder="验证码">
        <template #suffix>
          {{ firstImageCodeSourceA }} + {{ firstImageCodeSourceB }}
        </template>
      </a-input>
    </p>
    <a-button type="danger" block @click="validFirstImageCode">提交验证码</a-button>
  </a-modal>

  <a-modal v-model:visible="lineModalVisible" title="排队购票" :footer="null" :maskClosable="false" :closable="false"
           style="top: 50px; width: 400px">
    <div class="book-line">
      <div v-show="confirmOrderLineCount < 0">
        <loading-outlined/>
        系统正在处理中...
      </div>
      <div v-show="confirmOrderLineCount >= 0">
        <loading-outlined/>
        您前面还有{{ confirmOrderLineCount }}位用户在购票，排队中，请稍候
      </div>
    </div>
    <br/>
    <a-button type="danger" @click="onCancelOrder">取消购票</a-button>
  </a-modal>
  <a-modal v-model:visible="lineModalVisible" title="系统正在处理中" :footer="null" :maskClosable="false" :closable="false"
           style="top: 50px; width: 400px">
    <div class="book-line">
      <div v-show="confirmOrderLineCount < 0">
        <loading-outlined/>
        请稍后...
      </div>
      <div v-show="confirmOrderLineCount >= 0">
        <loading-outlined/>
        您前面还有{{ confirmOrderLineCount }}位用户在购票，排队中，请稍候
      </div>
    </div>
    <br/>
    <a-button type="danger" @click="onCancelOrder">取消购票</a-button>
  </a-modal>
</template>

<script>

import {computed, defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

export default defineComponent({
  name: "OrderView",
  setup() {
    const passengers = ref([]);
    const passengerOptions = ref([]);
    const passengerChecks = ref([]);
    const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
    console.log("下单的车次信息", dailyTrainTicket);

    const SEAT_TYPE = window.SEAT_TYPE;
    console.log(SEAT_TYPE)
    // 本车次提供的座位类型seatTypes，含票价，余票等信息，例：
    // {
    //   type: "YDZ",
    //   code: "1",
    //   desc: "一等座",
    //   count: "100",
    //   price: "50",
    // }
    // 关于SEAT_TYPE[KEY]：当知道某个具体的属性xxx时，可以用obj.xxx，当属性名是个变量时，可以使用obj[xxx]
    const seatTypes = [];
    for (let KEY in SEAT_TYPE) {
      let key = KEY.toLowerCase();
      if (dailyTrainTicket[key] >= 0) {
        seatTypes.push({
          type: KEY,
          code: SEAT_TYPE[KEY]["code"],
          desc: SEAT_TYPE[KEY]["desc"],
          count: dailyTrainTicket[key],
          price: dailyTrainTicket[key + 'Price'],
        })
      }
    }
    console.log("本车次提供的座位：", seatTypes)
    // 购票列表，用于界面展示，并传递到后端接口，用来描述：哪个乘客购买什么座位的票
    // {
    //   passengerId: 123,
    //   passengerType: "1",
    //   passengerName: "张三",
    //   passengerIdCard: "12323132132",
    //   seatTypeCode: "1",
    //   seat: "C1"
    // }
    const tickets = ref([]);
    const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
    const visible = ref(false);
    const lineModalVisible = ref(false);
    const confirmOrderId = ref();
    const confirmOrderLineCount = ref(-1);
    const lineNumber = ref(5);
    const chooseSeatObj = ref({});

    // 勾选或去掉某个乘客时，在购票列表中加上或去掉一张表
    watch(() => passengerChecks.value, (newVal, oldVal) => {
      console.log("勾选乘客发生变化", newVal, oldVal, passengerChecks.value)
      // 每次有变化时，把购票列表清空，重新构造列表
      tickets.value = [];
      passengerChecks.value.forEach((item) => tickets.value.push({
        passengerId: item.id,
        passengerType: item.type,
        seatTypeCode: seatTypes[0].code,
        passengerName: item.name,
        passengerIdCard: item.idCard
      }));
      chooseSeatObj.value = {}
    }, {immediate: true});

    // 0：不支持选座；1：选一等座；2：选二等座
    const chooseSeatType = ref(0);
    // 根据选择的座位类型，计算出对应的列，比如要选的是一等座，就筛选出ACDF，要选的是二等座，就筛选出ABCDF
    // computed 和 watch 类似，都可以用来响应式的计算一个值，compute 可以像这里这样写，直接声明出一个响应式变量
    const SEAT_COL_ARRAY = computed(() => {
      return window.SEAT_COL_ARRAY.filter(item => item.type === chooseSeatType.value);
    });
    // 选择的座位
    // {
    //   A1: false, C1: true，D1: false, F1: false，
    //   A2: false, C2: false，D2: true, F2: false
    // }
    watch(() => SEAT_COL_ARRAY.value, () => {
      chooseSeatObj.value = {};
      // 初始化所有座位，设为false
      for (let i = 1; i <= 2; i++) {
        SEAT_COL_ARRAY.value.forEach((item) => {
          // e.g: item.code+i = "A1"
          chooseSeatObj.value[item.code + i] = false;
        })
      }
      console.log("初始化两排座位，都是未选中：", chooseSeatObj.value);
    }, {immediate: true});

    const handleQueryPassenger = () => {
      axios.get("/member/passenger/queryMine").then((response) => {
        let data = response.data;
        if (data.success) {
          passengers.value = data.content;
          passengers.value.forEach((item) => passengerOptions.value.push({
            label: item.name,
            value: item
          }))
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const finishCheckPassenger = () => {
      console.log("购票列表：", tickets.value);

      if (tickets.value.length > 5) {
        notification.error({description: '最多只能购买5张车票'});
        return;
      }

      // 校验余票是否充足，购票列表中的每个座位类型，都去车次座位余票信息中，看余票是否充足
      // 前端校验不一定准，但前端校验可以减轻后端很多压力
      // 注意：这段只是校验，必须copy出seatTypesTemp变量来扣减，用原始的seatTypes去扣减，会影响真实的库存
      let seatTypesTemp = Tool.copy(seatTypes);
      for (let i = 0; i < tickets.value.length; i++) {
        let ticket = tickets.value[i];
        for (let j = 0; j < seatTypesTemp.length; j++) {
          let seatType = seatTypesTemp[j];
          // 同类型座位余票-1，这里扣减的是临时copy出来的库存，不是真正的库存，只是为了校验
          if (ticket.seatTypeCode === seatType.code) {
            seatType.count--;
            if (seatType.count < 0) {
              notification.error({description: seatType.desc + '余票不足'});
              return;
            }
          }
        }
      }
      console.log("前端余票校验通过");

      // 判断是否支持选座，只有纯一等座和纯二等座支持选座
      // 先筛选出购票列表中的所有座位类型，比如四张表：[1, 1, 2, 2]
      let ticketSeatTypeCodes = [];
      for (let i = 0; i < tickets.value.length; i++) {
        let ticket = tickets.value[i];
        ticketSeatTypeCodes.push(ticket.seatTypeCode);
      }
      // 为购票列表中的所有座位类型去重：[1, 2]
      const ticketSeatTypeCodesSet = Array.from(new Set(ticketSeatTypeCodes));
      console.log("选好的座位类型：", ticketSeatTypeCodesSet);
      // 12306规则：只有全部是一等座或全部是二等座才支持选座
      if (ticketSeatTypeCodesSet.length !== 1) {
        console.log("选了多种座位，不支持选座");
        chooseSeatType.value = 0;
      } else {
        // ticketSeatTypeCodesSet.length === 1，即只选择了一种座位（不是一个座位，是一种座位）
        if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.YDZ.code) {
          console.log("一等座选座");
          chooseSeatType.value = SEAT_TYPE.YDZ.code;
        } else if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.EDZ.code) {
          console.log("二等座选座");
          chooseSeatType.value = SEAT_TYPE.EDZ.code;
        } else {
          console.log("不是一等座或二等座，不支持选座");
          chooseSeatType.value = 0;
        }

        // 12306规则：余票小于20张时，不允许选座，否则选座成功率不高，影响出票
        if (chooseSeatType.value !== 0) {
          for (let i = 0; i < seatTypes.length; i++) {
            let seatType = seatTypes[i];
            // 找到同类型座位
            if (ticketSeatTypeCodesSet[0] === seatType.code) {
              // 判断余票，小于20张就不支持选座
              if (seatType.count < 20) {
                console.log("余票小于20张就不支持选座")
                chooseSeatType.value = 0;
                break;
              }
            }
          }
        }
      }
      // 弹出确认界面
      visible.value = true;
    };

    const btn_buying = ref(false);
    const handleOk = () => {
      btn_buying.value = true;
      if (Tool.isEmpty(imageCode.value)) {
        notification.error({description: '验证码不能为空'});
        return;
      }

      console.log("选好的座位：", chooseSeatObj.value);

      // 设置每张票的座位
      // 先清空购票列表的座位，有可能之前选了并设置座位了，但选座数不对被拦截了，又重新选一遍
      for (let i = 0; i < tickets.value.length; i++) {
        tickets.value[i].seat = null;
      }
      let i = -1;
      // 要么不选座位，要么所选座位应该等于购票数，即i === (tickets.value.length - 1)
      for (let key in chooseSeatObj.value) {
        if (chooseSeatObj.value[key]) {
          i++;
          if (i > tickets.value.length - 1) {
            notification.error({description: '所选座位数大于购票数'});
            return;
          }
          tickets.value[i].seat = key;
        }
      }
      if (i > -1 && i < (tickets.value.length - 1)) {
        notification.error({description: '所选座位数小于购票数'});
        return;
      }

      console.log("最终购票：", tickets.value);

      axios.post("/business/confirmOrder/do", {
        dailyTrainTicketId: dailyTrainTicket.id,
        date: dailyTrainTicket.date,
        trainCode: dailyTrainTicket.trainCode,
        start: dailyTrainTicket.start,
        end: dailyTrainTicket.end,
        tickets: tickets.value,
        imageCodeToken: imageCodeToken.value,
        imageCode: imageCode.value,
        lineNumber: lineNumber.value
      }).then((response) => {
        btn_buying.value = false;
        let data = response.data;
        if (data.success) {
          notification.success({description: "下单成功！"});
          visible.value = false;
          imageCodeModalVisible.value = false;
          lineModalVisible.value = true;
          confirmOrderId.value = data.content;
          queryLineCount();
        } else {
          notification.error({description: data.message});
        }
      });
    }

    /* ------------------- 定时查询订单状态 --------------------- */
    // 确认订单后定时查询
    let queryLineCountInterval;

    // 定时查询订单结果/排队数量
    const queryLineCount = () => {
      confirmOrderLineCount.value = -1;
      queryLineCountInterval = setInterval(function () {
        axios.get("/business/confirmOrder/queryLineCount/" + confirmOrderId.value).then((response) => {
          let data = response.data;
          if (data.success) {
            let result = data.content;
            switch (result) {
              case -1 :
                notification.success({description: "购票成功"});
                lineModalVisible.value = false;
                clearInterval(queryLineCountInterval);
                break;
              case -2:
                notification.error({description: "购票失败"});
                lineModalVisible.value = false;
                clearInterval(queryLineCountInterval);
                break;
              case -3:
                notification.error({description: "当日车次车票已售罄"});
                lineModalVisible.value = false;
                clearInterval(queryLineCountInterval);
                break;
              default:
                confirmOrderLineCount.value = result;
            }
          } else {
            notification.error({description: data.message});
          }
        });
      }, 500);
    };

    /* ------------------- 第二层验证码 --------------------- */
    const imageCodeModalVisible = ref();
    const imageCodeToken = ref();
    const imageCodeSrc = ref();
    const imageCode = ref();
    /**
     * 初始化后端验证码请求参数
     */
    const loadImageCode = () => {
      imageCodeToken.value = Tool.uuid(8);
      imageCodeSrc.value = process.env.VUE_APP_SERVER + '/business/kaptcha/image-code/' + imageCodeToken.value;
    };

    const showImageCodeModal = () => {
      loadImageCode();
      imageCodeModalVisible.value = true;
    };

    /* ------------------- 第一层验证码 --------------------- */
    const firstImageCodeSourceA = ref();
    const firstImageCodeSourceB = ref();
    const firstImageCodeTarget = ref();
    const firstImageCodeModalVisible = ref();

    /**
     * 加载第一层验证码
     */
    const loadFirstImageCode = () => {
      // 获取1~10的数：Math.floor(Math.random()*10 + 1)
      firstImageCodeSourceA.value = Math.floor(Math.random() * 10 + 1) + 10;
      firstImageCodeSourceB.value = Math.floor(Math.random() * 10 + 1) + 20;
    };

    /**
     * 显示第一层验证码弹出框
     */
    const showFirstImageCodeModal = () => {
      loadFirstImageCode();
      firstImageCodeModalVisible.value = true;
    };

    /**
     * 校验第一层验证码
     */
    const validFirstImageCode = () => {
      if (parseInt(firstImageCodeTarget.value) === parseInt(firstImageCodeSourceA.value + firstImageCodeSourceB.value)) {
        // 第一层验证通过
        firstImageCodeModalVisible.value = false;
        // 进入第二层后端验证码验证
        showImageCodeModal();
      } else {
        notification.error({description: '验证码错误'});
      }
    };

    /**
     * 取消排队
     */
    const onCancelOrder = () => {
      axios.get("/business/confirmOrder/cancel/" + confirmOrderId.value).then((response) => {
        let data = response.data;
        if (data.success) {
          let result = data.content;
          if (result === 1) {
            notification.success({description: "取消成功！"});
            // 取消成功时，不用再轮询排队结果
            clearInterval(queryLineCountInterval);
            lineModalVisible.value = false;
          } else {
            notification.error({description: "取消失败！"});
          }
        } else {
          notification.error({description: data.message});
        }
      });
    };

    onMounted(() => {
      handleQueryPassenger();
    });

    return {
      passengers,
      dailyTrainTicket,
      seatTypes,
      passengerOptions,
      passengerChecks,
      tickets,
      PASSENGER_TYPE_ARRAY,
      visible,
      finishCheckPassenger,
      chooseSeatType,
      chooseSeatObj,
      SEAT_COL_ARRAY,
      handleOk,
      imageCodeToken,
      imageCodeSrc,
      imageCode,
      showImageCodeModal,
      imageCodeModalVisible,
      loadImageCode,
      firstImageCodeSourceA,
      firstImageCodeSourceB,
      firstImageCodeTarget,
      firstImageCodeModalVisible,
      showFirstImageCodeModal,
      validFirstImageCode,
      lineModalVisible,
      confirmOrderId,
      confirmOrderLineCount,
      onCancelOrder,
      lineNumber,
      btn_buying
    };
  },
});
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}

.order-train .order-train-ticket {
  margin-top: 15px;
}

.order-train .order-train-ticket .order-train-ticket-main {
  color: red;
  font-size: 18px;
}

.order-tickets {
  margin: 10px 0;
}

.order-tickets .ant-col {
  padding: 5px 10px;
}

.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: white;
  font-size: 16px;
  padding: 5px 0;
}

.order-tickets .order-tickets-row {
  border: solid 1px cornflowerblue;
  border-top: none;
  vertical-align: middle;
  line-height: 30px;
}

.order-tickets .choose-seat-item {
  margin: 5px 5px;
}
</style>
