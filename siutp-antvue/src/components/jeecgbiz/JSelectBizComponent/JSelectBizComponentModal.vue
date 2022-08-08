<template>
  <a-modal
    centered
    :title="name + '选择'"
    :width="900"
    :visible="visible"
    @ok="handleOk"
    @cancel="close"
    cancelText="关闭"
  >
    <a-row :gutter="18">
      <a-col :span="16">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
              <a-col :span="14">
                <a-form-item :label="queryParamText || name">
                  <a-input
                    :placeholder="'请输入' + (queryParamText || name)"
                    v-model="queryParam[valueKey]"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <span
                  style="float: left; overflow: hidden"
                  class="table-page-search-submitButtons"
                >
                  <a-button type="primary" @click="searchQuery" icon="search"
                    >查询</a-button
                  >
                  <a-button
                    type="primary"
                    @click="searchReset"
                    icon="reload"
                    style="margin-left: 8px"
                    >重置</a-button
                  >
                </span>
              </a-col>
            </a-row>
          </a-form>
        </div>

        <a-table
          size="small"
          bordered
          rowkey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ y: 240 }"
          :rowSelection="{
            selectedRowKeys,
            onChange: onSelectChange,
            type: multiple ? 'checkbox' : 'radio',
          }"
          :customRow="customRowFn"
          @change="handleTableChange"
        >
        </a-table>
      </a-col>
      <a-col :span="8">
        <a-card
          :title="'已选' + name"
          :bordered="false"
          :head-style="{ padding: 0 }"
          :body-style="{ padding: 0 }"
        >
          <a-table rowKey="id" size="small" bordered v-bind="selectedTable">
            <span slot="action" slot-scope="text, record, index">
              <a @click="handleDeleteSelected(record, index)">删除</a>
            </span>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </a-modal>
</template>

<script>
import { JeecgListMixin } from "@/mixins/RewriteJeecgListMixin";

export default {
  name: "JSelectBizComponentModal",
  mixins: [JeecgListMixin], // 职务选择表格展示
  props: {
    value: {
      type: Array,
      default: () => [],
    },
    visible: {
      type: Boolean,
      default: false,
    },
    valueKey: {
      type: String,
      required: true,
    },
    multiple: {
      type: Boolean,
      default: true,
    },

    name: {
      type: String,
      default: "",
    },
    listUrl: {
      type: String,
      required: true,
      default: "",
    },
    displayKey: {
      type: String,
      default: null,
    },
    propColumns: {
      type: Array,
      default: () => [],
    },
    // 查询条件文字
    queryParamText: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      // 表头
      columns: this.propColumns,
      // 已选择列表
      selectedTable: {
        pagination: false,
        scroll: { y: 240 },
        columns: [
          this.propColumns[0],
          {
            title: "操作",
            dataIndex: "action",
            align: "center",
            width: 60,
            scopedSlots: { customRender: "action" },
          },
        ],
        dataSource: [],
      },
      url: { list: this.listUrl },
      dataSourcePost: [], // 所有职务列表
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.valueWatchHandler(val);
      },
    },
    dataSource: {
      deep: true,
      handler(val) {
        let options = val.map((data) => ({
          label: data[this.displayKey || this.valueKey],
          value: data[this.valueKey],
        }));
        this.$emit("ok", options);
        this.valueWatchHandler(this.value);
      },
    },
    selectionRows: {
      immediate: true,
      deep: true,
      handler(val) {
        this.selectedTable.dataSource = val;
      },
    },
  },
  mounted() {
    // 获取所有职务列表
    //			allPostList(this)
  },
  methods: {
    /** 关闭弹窗 */
    close() {
      this.$emit("update:visible", false);
    },

    valueWatchHandler(val) {
      // sys/position/list
      let dataSource = [];
      let selectedRowKeys = [];
      val.forEach((item) => {
        this.dataSourcePost.forEach((data) => {
          if (data[this.valueKey] == item) {
            dataSource.push(data);
            selectedRowKeys.push(data.id);
          }
        });
      });
      this.selectedTable.dataSource = dataSource;
      this.selectedRowKeys = selectedRowKeys;
    },

    /** 完成选择 */
    handleOk() {
      console.log(this.valueKey, 22222222);
      let value = this.selectedTable.dataSource.map(
        (data) => data[this.valueKey]
      );
      this.$emit("input", value);
      this.close();
    },

    /** 删除已选择的 */
    handleDeleteSelected(record, index) {
      this.selectedRowKeys.splice(this.selectedRowKeys.indexOf(record.id), 1);
      this.selectedTable.dataSource.splice(index, 1);
    },

    customRowFn(record) {
      if (!this.multiple) {
        return {
          on: {
            click: () => {
              this.selectedRowKeys = [record.id];
              this.selectedTable.dataSource = [record];
            },
          },
        };
      }
      return {};
    },
  },
};
</script>
<style lang="less" scoped>
</style>