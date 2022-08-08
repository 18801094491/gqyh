<template>
  <!-- 数据中心 -峰值谷值查询 -->
  <div class="margin12">
    <a-card :bordered="false">
      <a-divider>峰值谷值查询<span>(默认1h)</span></a-divider>
      <!-- <h3 style="">平均值分析</h3> -->
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
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="11" :sm="8">
              <a-form-item label="统计区间">
                <!--TODO待确认-->
                <!--<el-date-picker v-model="queryParam.statisticalInterval" placeholder="请选择时间"  type="datetimerange" format="yyyy-MM-dd HH:mm" ></el-date-picker>-->
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

                <!-- <a-range-picker showTime :showTime="{ format: 'HH:mm' }" format="YYYY-MM-DD HH:mm" v-model="queryParam.statisticalInterval"></a-range-picker> -->
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
        @click="handleExportXls('峰值谷值查询')"
        icon="download"
        style="margin-bottom: 20px"
        >导出
      </a-button>
      <!-- table区域-begin -->
      <a-alert type="warning" :showIcon="true">
        <div slot="message">
          该查询数据量较大，建议时间跨度不要太长，建议最多选择1天
        </div>
      </a-alert>
      <div class="tableBox" style="margin-top: 20px">
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
          :loading="loading"
          @change="handleTableChange"
        >
        </a-table>
        <!--<div class="jumpPagination">
                    跳至   <a-input v-model="jumpPagNum" style="width:50px" size="small"></a-input> 页  <a-button size="small" @click="toPage">跳转</a-button>
                </div>-->
      </div>
      <!-- table区域-end -->
    </a-card>
  </div>
</template>

<script>
import index from "@api/dataCenter-t/peakValley.js";
export default {
  ...index,
};
</script>

<style scoped>
/deep/ .ant-pagination {
  margin-right: 160px !important;
}

.jumpPagination {
  position: absolute;
  bottom: 39px;
  right: 25px;
}
</style>