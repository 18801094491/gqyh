<!-- 资产中心-资产台账-左侧树状图 -->
<template>
  <a-card :loading="cardLoading" :bordered="false" style="height: 100%">
    <a-spin :spinning="loading">
      <a-input-search
        @search="handleSearch"
        @change="handleChange"
        style="width: 100%; margin-top: 10px"
        placeholder="输入标签名称进行查询..."
      />

      <a-tree
        showLine
        checkStrictly
        :expandedKeys.sync="expandedKeys"
        :selectedKeys="selectedKeys"
        :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
        :treeData="treeDataSource"
        @select="handleTreeSelect"
      />
    </a-spin>
  </a-card>
</template>

<script>
import requipmentListLeft from "@/assets/js/operationCenter/equipmentSearcher/RequipmentListLeft.js";
export default {
  ...requipmentListLeft,
};
</script>
<style scoped>
</style>
