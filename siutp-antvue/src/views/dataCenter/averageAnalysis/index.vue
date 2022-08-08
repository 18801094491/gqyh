<template>
  <!-- 数据中心 -平均值分析 -->
  <div class="margin12">
    <a-card :bordered="false">
      <a-divider>平均值分析<span>(默认1h)</span></a-divider>
      <div class="table-page-search-wrapper" style="padding: 10px 12px">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="统计项目">
                <a-select
                  v-model="queryParam.statisticalItems"
                  placeholder="请选择统计项目"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    v-for="(item, index) in statisticalItemsList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="11" :sm="8">
              <a-form-item label="统计区间">
                <el-date-picker
                  v-model="queryParam.startTime"
                  placeholder="请选择开始时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
                至
                <el-date-picker
                  v-model="queryParam.endTime"
                  placeholder="请选择结束时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery" icon="search"
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
      <a-button
        type="primary"
        @click="handleExportXls('平均值分析')"
        icon="download"
        style="margin-bottom: 20px"
        >导出</a-button
      >
      <!-- 列表区域-开始 -->
      <div class="tableBox">
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          @change="handleTableChange"
        >
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import index from "@api/dataCenter-t/averageAnalysis.js";
export default {
  ...index,
};
</script>