
<template>
  <div v-if="pageLoading">
    <a-card
      class="j-address-list-right-card-box"
      :loading="cardLoading"
      :bordered="false"
    >
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
                    showSearch
                    placeholder="请选择结算结果"
                    optionFilterProp="children"
                    style="width: 100%"
                    v-model="queryParam.status"
                    clearIcon
                  >
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option
                      v-for="(item, index) in resultList"
                      :key="index"
                      :value="item.code"
                      >{{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="16">
                <a-form-item label="结算时间">
                  <a-month-picker v-model="queryParam.startTime" />
                  ~
                  <a-month-picker v-model="queryParam.endTime" />
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
        <h3>合计</h3>
        <a-table
          size="middle"
          bordered
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          :pagination="false"
        ></a-table>
      </div>
      <div
        class="table-row table-month"
        v-for="(item, i) in monthData"
        :key="i"
      >
        <h3>{{ item.title }}</h3>
        <a-table
          size="middle"
          bordered
          :columns="item.tableHeadList"
          :dataSource="item.tableDataList"
          :loading="loading"
          :scroll="{ x: 'max-content' }"
          :pagination="false"
        >
          <span slot="contractName" slot-scope="text, record, index">
            <a @click="contractDetail(record, index)">{{
              record.contractName
            }}</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <contract-detail ref="conDetail"></contract-detail>
  </div>
</template>

<script>
import index from "@/assets/js/measurementCenter/customerInfo/right";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
@import "~@assets/less/measurementCenter/customerInfo/right.less";
</style>