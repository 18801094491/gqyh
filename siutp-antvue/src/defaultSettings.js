/**
 * 项目默认配置项
 * primaryColor - 默认主题色
 * navTheme - sidebar theme ['dark', 'light'] 两种主题
 * colorWeak - 色盲模式
 * layout - 整体布局方式 ['sidemenu', 'topmenu'] 两种布局
 * fixedHeader - 固定 Header : boolean
 * fixSiderbar - 固定左侧菜单栏 ： boolean
 * autoHideHeader - 向下滚动时，隐藏 Header : boolean
 * contentWidth - 内容区布局： 流式 |  固定
 *
 * storageOptions: {} - Vue-ls 插件配置项 (localStorage/sessionStorage)
 *
 */

export default {
  primaryColor: '#448cda', // 默认主题色
  navTheme: 'light', // sidebar theme'light' 主题
  layout: 'sidemenu', // 整体布局方式 sidemenu
  contentWidth: 'Fixed', // 内容区布局： 固定
  fixedHeader: false, // 固定 Header : boolean 禁用
  fixSiderbar: false, // 固定左侧菜单栏 ： boolean  禁用
  autoHideHeader: false, // 向下滚动时，隐藏 Header : boolean 禁用
  colorWeak: false, //色盲模式  禁用
  multipage: false, //默认多页签模式

  // vue-ls options
  storageOptions: {
    namespace: 'pro__',  // key键前缀
    name: 'ls', // 命名Vue变量.[ls]或this.[$ls],
    storage: 'local', // 存储名称: session, local, memory
  }
}