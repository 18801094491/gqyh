<!-- 计量中心-计量统计-用水统计 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :span="11">
              <a-form-item label="客户类型">
                <a-select
                  showSearch
                  placeholder="请选择客户类型"
                  optionFilterProp="children"
                  style="width: 92%; max-width: 387px"
                  v-model="queryParam.customerType"
                  @change="getCustomerNameList"
                >
                  <a-select-option
                    v-for="(item, index) in customerTypeList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="13">
              <a-form-item label="客户名称">
                <a-select
                  showSearch
                  placeholder="请选择客户名称"
                  optionFilterProp="children"
                  style="width: 100%"
                  mode="multiple"
                  v-model="queryParam.customerIds"
                  @change="changeCustomer"
                >
                  <a-select-option
                    v-for="(item, index) in customerNameList"
                    :key="index"
                    :value="item.id"
                    >{{ item.customerName }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="16">
              <a-form-item label="年份">
                <el-date-picker
                  v-model="queryParam.beginYear"
                  type="year"
                  placeholder="选择年"
                >
                </el-date-picker>
                ~
                <el-date-picker
                  v-model="queryParam.endYear"
                  type="year"
                  placeholder="选择年"
                >
                </el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="loadData" icon="search"
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
      <!-- table区域-begin -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="customerName"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
          :loading="loading"
          :scroll="{ x: 'max-content' }"
        >
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-card>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementStatistics/usesWaterStatistics";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>