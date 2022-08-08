<!-- 信息中心-备品备件-库存管理-入库单-->
<template>
  <a-drawer
    title="入库单"
    width="100%"
    @close="warehouseReceiptonClose"
    :visible="warehouseReceiptvisible"
    :wrapStyle="{
      height: 'calc(100%)',
      overflow: 'auto',
      paddingBottom: '108px',
    }"
  >
    <div id="supplierManagementBox">
      <div class="screenCommonBox">
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="备件名称">
                  <a-input
                    placeholder="请输入备件名称"
                    v-model="queryParam.sparepartName"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="批次号">
                  <a-input
                    placeholder="请输入批次号"
                    v-model="queryParam.batchSn"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8" :offset="6">
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
      <a-card :bordered="false">
        <!-- 表格显示区域 -->
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ x: 1400 }"
          @change="handleTableChange"
        >
          <a
            slot="maintainBtn"
            slot-scope="text, record, index"
            @click="handleAdd('change', record)"
            >修改</a
          >
        </a-table>
      </a-card>
      <a-drawer
        :title="drawerTitle"
        :width="720"
        @close="onClose"
        :visible="visible"
        :wrapStyle="{
          height: 'calc(100%)',
          overflow: 'auto',
          paddingBottom: '108px',
        }"
      >
        <a-form :form="form" layout="vertical" hideRequiredMark>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="备件信息">
                <a-select v-model="sparepartId" placeholder="请选择备件信息">
                  <a-select-option
                    :value="item.id"
                    v-for="(item, index) in sparePartList"
                    :key="index"
                    >{{ item.sparepartName }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="批次号">
                <a-input v-model="batchSn" placeholder="请输入批次号" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="入库量">
                <a-input
                  v-model="amount"
                  @keyup="amount = amount.replace(/[^\d]/g, '')"
                  placeholder="请输入入库量"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="仓库名称">
                <a-select
                  v-model="storeId"
                  placeholder="请选择仓库名称"
                  @change="changeID"
                >
                  <a-select-option
                    :value="item.storeSn"
                    v-for="(item, index) in queryStoreList"
                    :key="index"
                    >{{ item.storePosition }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="生产日期">
                <a-date-picker v-model="productDate" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="有效期">
                <a-date-picker v-model="endDate" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
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
          <a-button :style="{ marginRight: '8px' }" @click="onClose">
            关闭
          </a-button>
          <a-button @click="onSubmit" type="primary">提交</a-button>
        </div>
      </a-drawer>
    </div>
  </a-drawer>
</template>

<script>
import index from "@/assets/js/operationCenter/sparePartsManagement/storageOfSparePartsModel/index.js";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>