<!-- 计量中心-结算管理-编辑 -->
<template>
  <div class="editable-cell">
    <div v-if="editable" class="editable-cell-input-wrapper">
      <a-input
        :value="value"
        @change="handleChange"
        @pressEnter="check"
      /><a-icon type="check" class="editable-cell-icon-check" @click="check" />
    </div>
    <div v-else class="editable-cell-text-wrapper">
      {{ value || " " }}
      <a-icon type="edit" class="editable-cell-icon" @click="edit" />
    </div>
  </div>
</template>

<script>
import edit from "@assets/js/measurementCenter/settlementManage/edit";
export default {
  ...edit,
};
</script>

<style scoped>
@import "~@assets/less/measurementCenter/settlementManage/edit.less";
</style>