import Vue from 'vue'
import MessageTips from './messageTips.vue'
Vue.prototype.$messageTips = (options) => {
  //options为调用时的传入的参数
  if (options === undefined) {
    throw new Error('messageTips未传入提示信息')
  } else if (typeof options === 'string' || typeof options === 'number') {
    options = {
      message: options
    }
  } else if (typeof options === 'object') {
    options = options
  }
  let message = Vue.extend(MessageTips)
  let component = new message({
    data: options
  }).$mount()
  document.body.appendChild(component.$el)
  Vue.nextTick(() => {
    component.show = true
  })
}
MessageTips.install = (vue) => {
  vue.component(MessageTips.name, MessageTips)
}
export default MessageTips
