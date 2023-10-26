<!--每个vue页面都由三部分组成：template,script,style-->
<template>
  <!-- vue 两种绑定：1.：属性绑定 2.@事件绑定-->
  <a-row class="login">
    <a-col :span="8" :offset="8" class="login-main">
      <h1 style="text-align: center">
        <rocket-two-tone/>&nbsp;12306铁路售票系统-登录
      </h1>
      <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
      >
        <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号!' }]"
        >
          <a-input v-model:value="loginForm.mobile" placeholder="手机号"/>
        </a-form-item>

        <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码!' }]"
        >
          <a-input v-model:value="loginForm.code" placeholder="短信验证码（测试：8888）">
            <template #addonAfter>
              <a @click="sendCode">获取验证码</a>
            </template>
          </a-input>
          <!--<a-input v-model:value="loginForm.code" placeholder="验证码"/>-->
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>

      </a-form>
    </a-col>
  </a-row>
</template>
<script>
import {defineComponent, reactive} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import {useRouter} from "vue-router";
// @指src根目录
import store from "@/store";

export default defineComponent({
  setup() {
    // 声明reactive响应式变量
    const router = useRouter()
    const loginForm = reactive({
      mobile: '18000000000',
      code: '',
    });
    // 声明事件
    const sendCode = () => {
      axios.post("/member/member/sendCode", {
        mobile: loginForm.mobile
      }).then(response => {
        let data = response.data;
        if (data.success) {
          notification.success({description: '短信验证码发送成功'});
          loginForm.code = "8888";
        } else {
          notification.error({description: data.message});
        }
      });
    };
    const login = () => {
      axios.post("/member/member/login", loginForm).then((response) => {
        let data = response.data;
        if (data.success) {
          notification.success({description: '登录成功'});
          // 登录成功，跳到控制台主页
          router.push("/welcome");
          // 必须通过commit()调用store内的mutations方法以提交数据修改
          // setMember()中第一个参数state类似于Python函数中的self，不用理会
          store.commit("setMember", data.content)
        } else {
          notification.error({description: data.message});
        }
      })
    };
    // return可将变量、事件暴露给html使用
    return {
      loginForm,
      sendCode,
      login,
    };
  },
});
</script>
<style>
.login-main h1 {
  font-family: "Microsoft YaHei UI Light", system-ui;
  font-size: 25px;
  font-weight: bold;
}

.login-main {
  margin-top: 10%;
  padding: 30px 30px 20px;
  border: 1px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}
</style>