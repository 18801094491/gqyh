<!-- 计量中心-客户信息左侧菜单 -->
<template>
  <a-card :loading="cardLoading" :bordered="false" style="height: 100%">
    <a-spin :spinning="loading">
      <a-input-search
        @search="handleSearch"
        @change="handleChange"
        style="width: 100%; margin-top: 10px"
        placeholder="输入区域名称进行查询..."
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
import index from "@/assets/js/measurementCenter/customerInfo/left";
export default {
  ...index,
};
</script>