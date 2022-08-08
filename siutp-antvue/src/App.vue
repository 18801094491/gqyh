<template>
  <a-locale-provider :locale="locale">
    <div id="app">
      <router-view/>
    </div>
  </a-locale-provider>
</template>
<script>
  import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
  import enquireScreen from '@/utils/device'

  export default {
    data () {
      return {
        locale: zhCN,//中文环境
      }
    },
    created () {
      let that = this
      //设备类型
      enquireScreen(deviceType => {
        // tablet 电脑
        if (deviceType === 0) {
          that.$store.commit('TOGGLE_DEVICE', 'mobile')
          that.$store.dispatch('setSidebar', false)
        }
        // mobile 移动端
        else if (deviceType === 1) {
          that.$store.commit('TOGGLE_DEVICE', 'mobile')
          that.$store.dispatch('setSidebar', false)
        }
        else {
          that.$store.commit('TOGGLE_DEVICE', 'desktop')
          that.$store.dispatch('setSidebar', true)
        }

      })
    }
  }
</script>
<style>
  #app {
    height: 100%;
  }
  .el-input__inner{
    height: 32px!important;
    line-height: 32px!important;
    border: 1px solid #d9d9d9!important;
  }
  .el-date-editor.el-input{
    width: 200px!important;
    
  }
  .el-input__icon{
    line-height: 32px!important;
  }
  .el-date-editor .el-range__icon, .el-date-editor .el-range-separator{
    line-height: 25px!important;
  }
  .el-input__inner::placeholder{
    color:#bfbfbf!important;
  }
</style>