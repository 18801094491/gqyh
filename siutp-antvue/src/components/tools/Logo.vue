<template>
  <div class="logo">
    <router-link :to="{name:'operationCenter-mapIndex'}">

      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->
      <!-- <img v-if="navTheme === 'dark'" src="~@/assets/logo-white.png" alt="logo"> -->
      <img v-if="logoBok" :src="horizontalLogo" alt="logo" class="horizontalLogo">
      <img v-if="!logoBok" :src="simpleLogo" alt="logo" class="simpleLogo">
      <!-- <img v-else src="~@/assets/logo.svg" alt="logo"> -->
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->

      <!-- <h1 v-if="showTitle">{{ title }}</h1> -->
    </router-link>
  </div>
</template>

<script>
import index from '@assets/js/system/Logo.js';
export default {
  ...index
}
</script>
<style lang="scss" scoped>
  /*缩小首页布 局顶部的高度*/
  $height: 50px;
    .horizontalLogo, .simpleLogo{
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%,-50%);
      width: auto !important;
      height: 47px !important;
    }
    .simpleLogo {
      height: 25px !important;
    }
  .sider {
    box-shadow: none !important;
    .logo {
      height: $height !important;
      line-height: $height !important;
      box-shadow: none !important;
      transition: background 300ms;
      padding-left: 0;
      text-align: center;
      position: relative;
      a {
        color: white;

        &:hover {
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }

    &.light .logo {
      background-color: #1890ff;
      background: url('~@/assets/logobg.png') repeat-x !important;
    }
  }
</style>