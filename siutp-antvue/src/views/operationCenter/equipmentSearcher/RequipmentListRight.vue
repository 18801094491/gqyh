<!-- 资产中心-资产台账-右侧菜单 -->
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
                <a-form-item label="放置位置">
                  <a-input
                    placeholder="请输入放置位置"
                    v-model="queryParam.optLocation"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="设备状态">
                  <a-select
                    showSearch
                    placeholder="请选择设备状态"
                    optionFilterProp="children"
                    style="width: 100%"
                    v-model="queryParam.equipmentRevstop"
                    clearIcon
                  >
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="1">启用</a-select-option>
                    <a-select-option value="0">停用</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="10">
                <a-form-item label="设备编号">
                  <a-input
                    placeholder="设备编号"
                    v-model="queryParam.equipmentSn"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="供应商">
                  <a-select
                    showSearch
                    placeholder="供应商"
                    optionFilterProp="children"
                    style="width: 100%"
                    v-model="queryParam.equipmentSupplier"
                    clearIcon
                  >
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option
                      :value="item.id"
                      v-for="(item, index) in supplierClassificationList2"
                      :key="index"
                      >{{ item.supplierName }}</a-select-option
                    >
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="4">
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
        <h3>设备台账</h3>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          :pagination="ipagination"
          :scroll="{ x: 'max-content' }"
          @change="handleTableChange"
        >
          <span slot="maintainBtn" slot-scope="text, record, index">
            <a @click="equipmentDetail(record)">详情</a>
          </span>
        </a-table>
      </div>
    </a-card>
    <requipment-detail ref="equipDetail"></requipment-detail>
  </div>
</template>

<script>
import requipmentListRight from "@/assets/js/operationCenter/equipmentSearcher/RequipmentListRight.js";

export default {
  ...requipmentListRight,
};
</script>
<style>
.j-address-list-right-card-box .ant-table-placeholder {
  min-height: 46px;
}
</style>
<style scoped>
@import "~@assets/less/operationCenter/equipmentSearcher/RequipmentListRight.less";
</style>