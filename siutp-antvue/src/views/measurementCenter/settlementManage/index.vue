<!-- 计量中心-结算管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="客户名称">
                <a-input
                  v-model="queryParam.customerName"
                  placeholder="请输入客户名称"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="4" :offset="10">
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

    <div>
      <!--结算单详情-->
      <a-drawer
        width="1000"
        :title="'结算单详情( ' + settleDetailTitle + ') '"
        :closable="false"
        @close="onClose"
        :visible="settlementVisble"
      >
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="10" :sm="8">
              <a-form-item label="结算截止时间">
                <el-date-picker
                  v-model="settleParam.endTime"
                  placeholder="请选择结算截止时间"
                  type="date"
                  format="yyyy-MM-dd"
                ></el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="4">
              <span
                style="overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button
                  type="primary"
                  @click="searchQuerySettle"
                  icon="search"
                  >查询</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
        <a-table
          style="margin-top: 20px"
          ref="seetleTable"
          size="middle"
          bordered
          :columns="settleColumns"
          :dataSource="settleDataSource"
          :loading="settleLoading"
          :pagination="false"
        >
        </a-table>
        <div
          :style="{
            position: 'absolute',
            left: 0,
            bottom: 0,
            width: '100%',
            borderTop: '1px solid #e9e9e9',
            padding: '10px 16px',
            background: '#fff',
            textAlign: 'right',
          }"
        >
          <a-button @click="handleSubmit" type="primary">生成结算单</a-button>
          <a-button style="margin-left: 10px" @click="onClose">取消</a-button>
        </div>
      </a-drawer>
    </div>
    <a-card :bordered="false">
      <!-- table区域-begin -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
          :loading="loading"
          :scroll="{ x: 1700 }"
        >
          <span slot="contractName" slot-scope="text, record, index">
            <a @click="contractDetail(record)">{{ record.contractName }}</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <contract-detail
      :contractDetailVisble="contractDetailVisble"
      :contractDetailRecord="contractDetailRecord"
      @closeContractDetail="closeContractDetail"
    ></contract-detail>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/settlementManage/index";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>