<!-- 运营中心-工单管理-通知列表 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="所属标段">
                <a-select
                  v-model="queryParam.equipmentSection"
                  placeholder="请选择所属标段"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in bidSegmentList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="放置位置">
                <a-input
                  placeholder="请输入放置位置"
                  v-model="queryParam.equipmentLocation"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8" :offset="8">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery('')" icon="search"
                  >查询</a-button
                >
                <a-button
                  class="ant-btn-border"
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
    </div>
    <a-card :bordered="false">
      <a-table
        ref="table"
        size="middle"
        bordered
        row-key="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
      >
        <span slot="problemDescription" slot-scope="text, record">
          <a-tooltip placement="top">
            <template slot="title">
              <span>{{ record.problemDescription }}</span>
            </template>
            <div>
              {{
                record.problemDescription &&
                record.problemDescription.length > 10
                  ? record.problemDescription.substring(0, 10) + "..."
                  : record.problemDescription
              }}
            </div>
          </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-popconfirm title="确定关闭吗?" @confirm="() => closeList(record)"
            ><a>关闭</a></a-popconfirm
          >
        </span>
      </a-table>
    </a-card>
  </div>
</template>
<script>
import index from "@assets/js/workOrderManagement/notice/index.js";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
<style scoped>
</style>