<!-- 安全中心-视频监控 -->
<template>
  <div class="hello-ezuikit-js">
    <input type="text" v-model="token" style="width: 800px" v-if="false" />
    <button name="el-button" type="primary" @click="getToken" class="button">
      刷新
    </button>
    <div>
      <div id="video-container1" class="videos"></div>
      <div id="video-container2" class="videos"></div>
    </div>
    <div>
      <div id="video-container3" class="videos"></div>
      <div id="video-container4" class="videos"></div>
    </div>
  </div>
</template>

<script>
import EZUIKit from "ezuikit-js";
import axios from "axios";
const new_token = "";
var token = "";
export default {
  name: "HelloWorld",
  props: {
    msg: String,
  },
  data() {
    return {
      token: null,
      new_token: null,
    };
  },
  created() {},
  methods: {
    getToken: function () {
      location.reload();
    },
  },
  beforeMount() {},
  beforeCreate: function () {
    axios({
      method: "get",
      url: "http://125.35.94.110:9000/siutp/video/getToken",
    })
      .then((response) => {
        // 请求成功
        this.new_token = response.data.result.token;
        token = this.new_token;
        this.token = token;
      })
      .catch((error) => {
        // 请求失败，
        console.log(error);
      });
  },
  mounted: function () {
    var token = "";
    axios({
      method: "get",
      url: "http://125.35.94.110:9000/siutp/video/getToken",
    })
      .then((response) => {
        // 请求成功
        this.new_token = response.data.result.token;
        token = this.new_token;
        this.token = token;
        console.log(this.token);
        var player = new EZUIKit.EZUIKitPlayer({
          autoplay: true,
          id: "video-container1",
          accessToken: this.token,
          url: "ezopen://open.ys7.com/E53844575/1.hd.live",
          template: "standard", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
        });
        var player = new EZUIKit.EZUIKitPlayer({
          autoplay: true,
          id: "video-container2",
          accessToken: this.token,
          url: "ezopen://open.ys7.com/E53844575/2.hd.live",
          template: "standard", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
        });
        var player = new EZUIKit.EZUIKitPlayer({
          autoplay: true,
          id: "video-container3",
          accessToken: this.token,
          url: "ezopen://open.ys7.com/E53844575/3.hd.live",
          template: "standard", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
        });
        var player = new EZUIKit.EZUIKitPlayer({
          autoplay: true,
          id: "video-container4",
          accessToken: this.token,
          url: "ezopen://open.ys7.com/E53844575/4.hd.live",
          template: "standard", // simple - 极简版;standard-标准版;security - 安防版(预览回放);voice-语音版；
        });
      })
      .catch((error) => {
        // 请求失败，
        console.log(error);
      });
  },
};
</script>
<style>
.videos {
  width: 480px;
  height: 270px;
  margin-top: 10px;
  margin-left: 20px;
  float: left;
}
.button {
  width: 90px;
  height: 36px;
  font-size: 12px;
  background-color: #0087ff;
  border: cornsilk;
  font-family: SourceHanSansSC-Regular, SourceHanSansSC;
  font-weight: 400;
  color: #ffffff;
  line-height: 36px;
  margin-left: 20px;
}
</style>