<template>
  <transition name="el-fade-in" >
    <!--<div v-if="show">-->
    <div class="mask" v-if="show">
      <div class="message">
        <div class="message-box">
          <i class="message-icon" :class="'icon-'+type" ></i>
          <span>{{message}}</span>
        </div>
        <a class="close" href="javascript:;" @click="close" >
        </a>
      </div>
    </div>
  </transition>
</template>
<script>
export default {
  name: "MessageTips",
  data() {
    return {
      message: "",
      duration: 3000,
      show: false,
      type: "error",
      timer: null
    };
  },
  methods: {
    close() {
      this.timer = null;
      this.show = false;
    }
  },
  mounted() {
    this.timer = setTimeout(() => {
      this.show = false;
    }, this.duration);
  },
  destroyed() {
    this.timer = null;
  }
};
</script>
 
<style lang="scss" scoped>
$success: #51d163;
$wt: #ffff;
$error: #e27878;
.mask {
  position: fixed;
  width: 100vw;
  height: 100vh;
  top: 0;
  right: 0;
  /*background: rgba(0, 0, 0, 0.3);*/
  z-index: 9999;
  .message {
    /*width: 396px;
    height: 160px;*/
    position: absolute;
    top: 0vh;
    left: 0;
    right: 0;
    margin: auto;
    box-sizing: border-box;
    padding: 20px 0 0 0;
    background: $wt;
    &-box {
    	width: 300px;
    	border: 1px solid $error;
    	/*background: $error;*/
      height: 40px;
      margin: auto;
      text-align: center;
		border-radius: 5px;
      .message-icon {
        display: inline-block;
        width: 40px;
        height: 40px;
        box-sizing: border-box;
        vertical-align: top;
        border-radius: 50%;
        margin-left: -30px;
        position: absolute;
        left: 44%;
      }
      .icon-success {
        border: 1px solid $success;
      }
      .icon-error {
        background: url("./error_icon.png") no-repeat left;
      }
      span {
        font: 400 14px/40px "";
        padding-left: 12px;
      }
    }
    .close {
      width: 12px;
      height: 12px;
      position: absolute;
      top: 35px;
      right: 42%;
      background: url("./close_icon.png") no-repeat center;
      color: red;
    }
  }
}
</style>
