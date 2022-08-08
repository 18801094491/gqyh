<template>
  <a-card :loading="cardLoading" :bordered="false" style="height: 100%">
    <a-spin :spinning="loading">
      <a-input-search
        @search="handleSearch"
        style="width: 100%; margin-top: 10px"
        placeholder="输入客户名称进行查询..."
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
import index from "@assets/js/measurementCenter/measurementStatistics/waterQuantityAnalysis/left";
export default {
  ...index,
};
</script>

<style scoped>
</style>