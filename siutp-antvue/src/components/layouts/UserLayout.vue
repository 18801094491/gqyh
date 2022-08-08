<!--路由跳转的用户登录页面-->
<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
    <route-view></route-view>
  </div>
</template>

<script>
import RouteView from "@/components/layouts/RouteView";
import { mixinDevice } from "@/utils/mixin.js";

export default {
  name: "UserLayout",
  components: { RouteView },
  mixins: [mixinDevice],
  data() {
    return {};
  },
  mounted() {
    document.body.classList.add("userLayout");
  },
  beforeDestroy() {
    document.body.classList.remove("userLayout");
    document.body.style.background='none'
  },
};
</script>

<style lang="scss" scoped>
#userLayout.user-layout-wrapper {
  height: 100%;

  &.mobile {
    .container {
      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .container {
    width: 100%;
    height: 100%;
    margin: 0 auto;
    overflow: hidden;
    min-height: 100%;

    background: url("~@/assets/bg.png") no-repeat center center;
    background-size: 880px 641px;

    position: relative;
    #containerBg {
      position: absolute;
      cursor: default;
      opacity: 0.6;
      width: 100%;
      height: 100%;
      left: 0;
      top: 0;
      background: -webkit-linear-gradient(top, #005bea, #00c6fb);
    }
    a {
      text-decoration: none;
    }

    .top {
      text-align: center;

      .header {
        height: 44px;
        line-height: 44px;

        .badge {
          position: absolute;
          display: inline-block;
          line-height: 1;
          vertical-align: middle;
          margin-left: -12px;
          margin-top: -10px;
          opacity: 0.8;
        }

        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
          border-style: none;
        }

        .title {
          font-size: 33px;
          color: rgba(0, 0, 0, 0.85);
          font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont,
            "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei",
            "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji",
            "Segoe UI Emoji", "Segoe UI Symbol";
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }
      .desc {
        font-size: 30px;
        color: #000;
        margin-top: 12px;
        margin-bottom: 20px;
        font-weight: 900;
      }
    }
    // 登录页修改
    .main {
      min-width: 260px;
      width: 450px; // 登录页修改 368
      margin: 0 auto;
    }
    // 登录页修改
    .main#login {
      width: 450px;
    }
    .footer {
      position: absolute;
      width: 100%;
      bottom: 0;
      padding: 0 16px;
      margin: 48px 0 24px;
      text-align: center;

      .links {
        margin-bottom: 8px;
        font-size: 14px;
        a {
          color: rgba(0, 0, 0, 0.45);
          transition: all 0.3s;
          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }
      .copyright {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
      }
    }
  }
}
.loginBox {
  position: absolute;
  left: 50%;
  top: 42%;
  transform: translate(-50%, -50%);
}
</style>
