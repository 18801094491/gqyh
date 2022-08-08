import enquireJs from 'enquire.js'//引入CSS插件媒体查询
//自定义查询判断为mobile还是tablet
const enquireScreen = function (call) {
  // tablet
  const handler = {
    match: function () {
      call && call(0)
    },
    unmatch: function () {
      call && call(-1)
    }
  }
  // mobile
  const handler2 = {
    match: () => {
      call && call(1)
    }
  }
  enquireJs.register('screen and (max-width: 1087.99px)', handler)//宽度大于1300px时判断为pc电脑
  enquireJs.register('screen and (max-width: 767.99px)', handler2)// mobile: 宽度大于767.99px时判断为mobile
}

export default enquireScreen