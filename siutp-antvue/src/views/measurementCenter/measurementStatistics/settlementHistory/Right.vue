<!-- 计量中心-计量统计-结算历史右侧菜单 -->
<template>
  <div v-if="pageLoading">
    <a-card class="j-address-list-right-card-box" :bordered="false">
      <div class="screenCommonBox" style="padding: 0">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :span="10">
                <a-form-item label="客户名称">
                  <a-input
                    placeholder="请输入客户名称"
                    v-model="queryParam.customerName"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="结算结果">
                  <a-select
                    placeholder="请选择结算结果"
                    style="width: 100%"
                    v-model="queryParam.status"
                  >
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option
                      v-for="(item, index) in settleResList"
                      :key="index"
                      :value="item.code"
                      >{{ item.title }}</a-select-option
                    >
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="16">
                <a-form-item label="查询时间">
                  <el-date-picker
                    v-model="queryParam.left"
                    placeholder="请选择开始时间"
                    type="month"
                    format="yyyy-MM"
                    :picker-options="left_Date"
                  ></el-date-picker>
                  至
                  <el-date-picker
                    v-model="queryParam.right"
                    placeholder="请选择结束时间"
                    type="month"
                    format="yyyy-MM"
                    :picker-options="right_Date"
                  ></el-date-picker>
                </a-form-item>
              </a-col>
              <a-col :span="8">
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
      </div>
      <a-divider style="margin: 0"></a-divider>
      <div class="table-row table-total">
        <a-button
          type="primary"
          style="margin-bottom: 25px; margin-top: 20px"
          v-if="dataSource.length"
          @click="handleExportXls('历史查询')"
          icon="download"
          >导出</a-button
        >
        <a-table
          size="middle"
          bordered
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          :pagination="false"
          :scroll="{ x: 1700 }"
        ></a-table>
        <div v-if="dataSource.length != 0" style="margin-top: 20px">
          <a-pagination
            class="pagination"
            size="small"
            :total="total"
            :showTotal="
              (total, range) =>
                `共 ${total} 条 当前[${range[0]} -  ${range[1]} ]`
            "
            :pageSize="pageSize"
            :current="pageNo"
            @change="handleSizeChange"
            @showSizeChange="handleCurrentChange"
          />
          <div class="jumpPagination">
            跳至
            <a-input
              v-model="jumpPagNum"
              style="width: 50px"
              size="small"
            ></a-input>
            页 <a-button size="small" @click="toPage">跳转</a-button>
          </div>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementStatistics/settlementHistory/right";
export default {
  ...index,
};
</script>
<style>
.j-address-list-right-card-box .ant-table-placeholder {
  min-height: 46px;
}
</style>
<style scoped>
.j-address-list-right-card-box {
  height: 100%;
  min-height: 300px;
}
/*#f5babf   #f74e4e*/
/deep/ .upClassName {
  background: #f74e4e !important;
}
.table-row h3 {
  font-size: 15px;
  margin: 10px 0;
}

/deep/ .ant-pagination {
  margin-right: 160px !important;
  float: right;
}
.jumpPagination {
  position: absolute;
  bottom: 25px;
  right: 25px;
}
</style>