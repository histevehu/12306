import { createStore } from 'vuex'

export default createStore({
  // state内声明了数据对象，相当于Java的实体类
  state: {
    member:{}
  },
  // 除非存在数值类型等转换，否则直接通过state.xxx访问即可
  getters: {
  },
  // mutations内说明了对数据对象的赋值操作，相当于Java的setter()
  mutations: {
    setMember(state, _member){
      state.member=_member
    }
  },
  // actions内声明异步任务
  actions: {
  },
  // 若项目较大，可将上述内容模块化
  modules: {
  }
})
