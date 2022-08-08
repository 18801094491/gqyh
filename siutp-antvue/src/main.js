import Vue from 'vue'
import App from './App.vue'
import Storage from 'vue-ls'//导入插件vue-ls用于本地储存
import router from './router'//导入插件vue-Router 用于构建单页应用
import store from './store/'//导入插件vuex集中式储存管理应用的所有组件的状态。
import './assets/less/base.less'//导入less文件
import { VueAxios } from "@/utils/request"//导入插件axios用于向后台发起请求

import Antd from 'ant-design-vue'//导入插件Ant Design of Vue这个ui框架搭建布局
import Viser from 'viser-vue'//导入插件viser-vue用于ui布局
import 'ant-design-vue/dist/antd.less';  //导入Ant Design of Vue less可以自定义主题
import Moment from 'moment'//导入插件moment用于日期格式化变化
import '@/permission' // 导入权限控制的组件
import '@/utils/filter' //导入基本过滤组件
import Print from 'vue-print-nb-jeecg'//导入插件vue-print-nb-jeecg用于打印
import VueApexCharts from 'vue-apexcharts'//导入插件ApexCharts 是一个现代的图表库
import preview from 'vue-photo-preview'//导入插件vue-photo-preview 用于图片的预览，放大
import 'vue-photo-preview/dist/skin.css'
import "@jeecg/antd-online"
import '@jeecg/antd-online/dist/OnlineForm.css'
import { DatePicker, Dialog } from 'element-ui';//导入插件element-ui 这个ui框架
import 'element-ui/lib/theme-chalk/index.css';

import config from '@/defaultSettings'//导入默认值设置
import {
  ACCESS_TOKEN,
  DEFAULT_COLOR,
  DEFAULT_THEME,
  DEFAULT_LAYOUT_MODE,
  DEFAULT_COLOR_WEAK,
  SIDEBAR_TYPE,
  DEFAULT_FIXED_HEADER,
  DEFAULT_FIXED_HEADER_HIDDEN,
  DEFAULT_FIXED_SIDEMENU,
  DEFAULT_CONTENT_WIDTH_TYPE,
  DEFAULT_MULTI_PAGE
} from "@/store/mutation-types"//导入mutation-types 定义的常量可以替代Mutation事件类型

import hasPermission from '@/directive/hasPermission'//自定义的插件里面是自定义的权限指令
import hasRole from '@/directive/hasRole'//自定义的插件里面是自定义的角色指令
import vueBus from '@/utils/vueBus';//自定义插件中央事件总线
import JDictSelectTag from './components/dict/index.js'//自定义插件里面自定义的元素
import JeecgComponents from '@/components/jeecg/index'//自定义插件表单
import MessageTips from '@/components/messageTips'//自定义的插件 信息提示组件

Vue.config.productionTip = false
/**
 * 实例化调用插件，全局调用
 */
Vue.prototype.moment = Moment
Vue.use(Storage, config.storageOptions)
Vue.use(Antd)
Vue.use(VueAxios, router)
Vue.use(Viser)
Vue.use(hasPermission)
Vue.use(hasRole)
Vue.use(JDictSelectTag)
Vue.use(Print)
Vue.use(VueApexCharts)
Vue.component('apexchart', VueApexCharts)
Vue.use(preview)
Vue.use(vueBus);
Vue.use(JeecgComponents);
Vue.use(DatePicker);
Vue.use(Dialog);
Vue.use(MessageTips)

new Vue({
  router,
  store,
  mounted() {
    //系统初始化，读取localstorage的SIDEBAR_TYPE值调用SET_SIDEBAR_TYPE
    store.commit('SET_SIDEBAR_TYPE', Vue.ls.get(SIDEBAR_TYPE, true))
    store.commit('TOGGLE_THEME', Vue.ls.get(DEFAULT_THEME, config.navTheme))
    store.commit('TOGGLE_LAYOUT_MODE', Vue.ls.get(DEFAULT_LAYOUT_MODE, config.layout))
    store.commit('TOGGLE_FIXED_HEADER', Vue.ls.get(DEFAULT_FIXED_HEADER, config.fixedHeader))
    store.commit('TOGGLE_FIXED_SIDERBAR', Vue.ls.get(DEFAULT_FIXED_SIDEMENU, config.fixSiderbar))
    store.commit('TOGGLE_CONTENT_WIDTH', Vue.ls.get(DEFAULT_CONTENT_WIDTH_TYPE, config.contentWidth))
    store.commit('TOGGLE_FIXED_HEADER_HIDDEN', Vue.ls.get(DEFAULT_FIXED_HEADER_HIDDEN, config.autoHideHeader))
    store.commit('TOGGLE_WEAK', Vue.ls.get(DEFAULT_COLOR_WEAK, config.colorWeak))
    store.commit('TOGGLE_COLOR', Vue.ls.get(DEFAULT_COLOR, config.primaryColor))
    store.commit('SET_TOKEN', Vue.ls.get(ACCESS_TOKEN))
    store.commit('SET_MULTI_PAGE', Vue.ls.get(DEFAULT_MULTI_PAGE, true))
  },
  render: h => h(App)
}).$mount('#app')
