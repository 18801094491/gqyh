import Vue from 'vue'
import router from './router'
import store from './store'
import NProgress from 'nprogress' //进度条
import 'nprogress/nprogress.css' // 进度条风格css
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { generateIndexRouter } from "@/utils/util"
NProgress.configure({ showSpinner: false }) // NProgress配置  

//路由的白名单；跟axios的白名单两码事；
const whiteList = ['/user/login', '/user/register', '/user/register-result', '/user/alteration', '/video', '/screen/map', '/screen/Monitor', '/screen/MonitorThree'] // 没有重定向的白名单

//在路由导航守卫中处理获取用户动态菜单权限，加到路由表里
router.beforeEach((to, from, next) => {
  NProgress.start() // 开始进度条

  if (Vue.ls.get(ACCESS_TOKEN)) {//如果有token，说明是登录状态
    /* has token */
    if (to.path === '/user/login') {//路由是/login页
      next({ path: '/dashboard/workplace' })///那么直接跳转到首页
      NProgress.done()// 完成进度条
    } else {
      if (store.getters.permissionList.length === 0) {
        store.dispatch('GetPermissionList').then(res => {
          const menuData = res.result.menu;
          if (menuData === null || menuData === "" || menuData === undefined) {
            return;
          }
          let constRoutes = [];

          constRoutes = generateIndexRouter(menuData);
          // 添加主界面路由
          store.dispatch('UpdateAppRouter', { constRoutes }).then(() => {
            // 根据roles权限生成可访问的路由表
            // 动态添加可访问路由表
            router.addRoutes(store.getters.addRouters)
            const redirect = decodeURIComponent(from.query.redirect || to.path)
            if (to.path === redirect) {
              // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
              next({ ...to, replace: true })
            } else {
              // 跳转到目的路由
              next({ path: redirect })
            }
          })
        })
          .catch(() => {

            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } })
            })
          })
      } else {
        next()
      }
    }
  } else {//如果没有token
    if (whiteList.indexOf(to.path) !== -1) {//白名单中有的路由，可以继续访问
      // 在免登录白名单，直接进入
      next()
    } else {//否则，白名单中没有的路由
      next({ path: '/user/login', query: { redirect: to.fullPath } })//跳回首页
      NProgress.done() //进度条完成
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 完成进度条
})
