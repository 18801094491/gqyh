<!-- 计量中心-客户信息 -->
<template>
  <div class="margin12">
    <a-row type="flex" :gutter="16">
      <a-col :md="5" :sm="24">
        <customer-list-left v-model="currentDistrictCode" />
      </a-col>
      <a-col :md="24 - 5" :sm="24">
        <customer-list-right v-model="currentDistrictCode" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import CustomerListLeft from "./Left";
import CustomerListRight from "./Right";

export default {
  name: "index",
  components: { CustomerListLeft, CustomerListRight },
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