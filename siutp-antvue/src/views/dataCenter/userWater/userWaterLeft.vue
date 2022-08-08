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
import { queryCustomerTreeList } from "@/api/api";

export default {
  name: "userWaterLeft",
  props: ["value"],
  data() {
    return {
      cardLoading: true,
      loading: false,
      treeDataSource: [],
      selectedKeys: [],
      expandedKeys: [],
    };
  },
  created() {
    this.queryTreeData();
  },
  methods: {
    queryTreeData(keyword) {
      this.commonRequestThen(
        queryCustomerTreeList({
          departName: keyword ? keyword : undefined,
        })
      );
    },

    handleSearch(value) {
      if (value) {
        this.commonRequestThen(queryCustomerTreeList({ keyword: value }));
      } else {
        this.queryTreeData();
      }
    },

    handleTreeSelect(selectedKeys, event) {
      if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]];
        let nodeInfo = event.node.dataRef;
        this.emitInput(nodeInfo);
      }
    },

    emitInput(nodeInfo) {
      this.$emit("input", nodeInfo);
    },

    commonRequestThen(promise) {
      this.loading = true;
      promise
        .then((res) => {
          if (res.success) {
            this.treeDataSource = res.result;

            // 默认选中第一条数据、默认展开所有第一级
            if (res.result.length > 0) {
              this.expandedKeys = [];
              res.result.forEach((item, index) => {
                if (index === 0) {
                  this.selectedKeys = [item.id];
                  this.emitInput(item.id);
                }
                this.expandedKeys.push(item.id);
              });
            }
          } else {
            this.$message.warn("客户名称查询失败：" + res.message);
          }
        })
        .finally(() => {
          this.loading = false;
          this.cardLoading = false;
        });
    },
  },
};
</script>