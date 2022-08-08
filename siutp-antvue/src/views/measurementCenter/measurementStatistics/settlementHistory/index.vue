<!-- 计量中心-计量统计-结算历史 -->
<template>
  <div class="margin12">
    <a-row type="flex" :gutter="16">
      <a-col :md="5" :sm="24">
        <his-data-left v-model="currentDistrictCode" />
      </a-col>
      <a-col :md="24 - 5" :sm="24">
        <his-data-right v-model="currentDistrictCode" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import HisDataLeft from "./Left";
import HisDataRight from "./Right";

export default {
  name: "hisData",
  components: { HisDataLeft, HisDataRight },
  data() {
    return {
      description: "用户信息展示页面",
      currentDistrictCode: "",
    };
  },

  methods: {},
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>