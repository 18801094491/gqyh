import { UserLayout, TabLayout, BlankLayout, screenMap, ScreenMonitor, ScreenMonitorThree, ScreenDateTime } from '@/components/layouts'
import Video from '../views/video/video.vue'
import bigScreenPages from '../views/bigScreenPages/index'

//登录后首页路由；动态获取菜单
export const asyncRouterMap = [
  {
    path: '/',
    name: 'dashboard',
    component: TabLayout,
    meta: { title: '信息中心' },
    redirect: '/dashboard/workplace',
    children: [

    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

//用户登录、错误等固定基础由
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/Login')
      },
    ]
  },
  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('@/views/Home')
      }
    ]
  },

  {
    path: '/bigScreenPages',
    component: bigScreenPages,
    redirect: '/bigScreenPages/index',
    hidden: true,
    children: [
        {
            path: 'index',
            name: 'bigScreenPages',
            component: () => import(/* webpackChunkName: "user" */ '@/views/bigScreenPages/index')
        },
    ]
  },
  {
    path: '/screen',
    component: screenMap,
    redirect: '/screen/map',
    hidden: true,
    children: [
        {
            path: 'map',
            name: 'screenMap',
            component: () => import(/* webpackChunkName: "user" */ '@/components/bigScreenPages/Map')
        },
    ]
  },
  {
    path: '/screen',
    component: ScreenMonitor,
    redirect: '/screen/monitor',
    hidden: true,
    children: [
        {
            path: 'Monitor',
            name: 'ScreenMonitor',
            component: () => import(/* webpackChunkName: "user" */ '@/components/bigScreenPages/Monitor')
        }
    ]
  },
  {
    path: '/screen',
    component: ScreenMonitorThree,
    redirect: '/screen/MonitorThree',
    hidden: true,
    children: [
        {
            path: 'MonitorThree',
            name: 'ScreenMonitorThree',
            component: () => import(/* webpackChunkName: "user" */ '@/components/bigScreenPages/MonitorThree')
      }
    ]
  },
  {
    path: '/screen',
    component: ScreenDateTime,
    redirect: '/screen/dateTime',
    hidden: true,
    children: [
        {
            path: 'DateTime',
            name: 'ScreenDateTime',
            component: () => import(/* webpackChunkName: "user" */ '@/components/bigScreenPages/DateTime')
        }
    ]
  },
  {
    path:'/video',
    component:Video
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }

]
