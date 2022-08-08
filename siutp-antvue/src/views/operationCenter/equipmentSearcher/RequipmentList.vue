<!-- 资产中心-资产台账 -->
<template>
  <div class="margin12">
    <a-row type="flex" :gutter="16">
      <a-col :md="5" :sm="24">
        <requipment-list-left v-model="currentLabelCode" />
      </a-col>
      <a-col :md="24 - 5" :sm="24">
        <requipment-list-right v-model="currentLabelCode" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import requipmentList from "@/assets/js/operationCenter/equipmentSearcher/RequipmentList.js";
export default {
  ...requipmentList,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>