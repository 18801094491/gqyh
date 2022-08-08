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
import CustomerListLeft from "./userWaterLeft";
import CustomerListRight from "./userWaterRight";

export default {
  name: "CustomerListVue",
  components: { CustomerListLeft, CustomerListRight },
  data() {
    return {
      description: "客户水量统计",
      currentDistrictCode: "",
    };
  },

  methods: {},
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>