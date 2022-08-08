<!-- 信息中心-备品备件-库存管理-->
<template>
<div id="equipmentAccountMaintenanceBox" class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="备品备件">
                <a-select
                  v-model="queryParam.sparepartName"
                  placeholder="请选择备品备件"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in modelTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="规格">
                <a-input
                  placeholder="请输入规格"
                  v-model="queryParam.sparepartSpecs"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="型号">
                <a-input
                  placeholder="请输入规格"
                  v-model="queryParam.sparepartModel"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="所在仓库">
                <a-select
                  v-model="queryParam.storeId"
                  placeholder="请选择仓库名称"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.storeSn"
                    v-for="(item, index) in queryStoreList"
                    :key="index"
                    >{{ item.storePosition }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8" :offset="10">
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
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="handleAdd('add')" type="primary" icon="plus"
          >新增入库</a-button
        >
        <a-button @click="warehouseReceipt" type="primary" icon="file-search"
          >入库单</a-button
        >
        <a-button @click="outboundOrder" type="primary" icon="file-search"
          >出库单</a-button
        >
      </div>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 1600 }"
        :rowClassName="(record, index) => (record.state == 1 ? 'active' : '')"
        @change="handleTableChange"
      >
        <span slot="maintainBtn" slot-scope="text, record, index">
          <!-- <a slot="maintainBtn" @click="handleAdd('change',record)">修改</a> -->
          <a @click="handleAdd2(record)">出库</a>
          <a-divider type="vertical" />
          <a @click="stockEarlyWarning(record)">库存预警</a>
        </span>
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
      <a-form layout="vertical" hideRequiredMark>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="备品备件">
              <a-select
                v-model="equipmentTypeName"
                placeholder="请选择备品备件"
                @change="changeEquipmentTypeName"
              >
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in modelTypeList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="型号">
              <a-select
                v-model="equipmentModel"
                placeholder="请选择型号"
                @change="changeEquipmentModel"
              >
                <a-select-option
                  v-for="(item, index) in equipmentModelList"
                  :key="index"
                  :value="item.code"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="规格">
              <a-select v-model="equipmentSpecs" placeholder="请选择规格">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in equipmentSpecsList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商">
              <a-select v-model="supplierId" placeholder="请选择供应商">
                <a-select-option
                  v-for="(item, index) in supplierClassificationList"
                  :key="index"
                  :value="item.id"
                  >{{ item.supplierName }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="入库量">
              <a-input
                v-model="amountR"
                @keyup="amountR = amountR.replace(/[^\d]/g, '')"
                placeholder="请输入入库量"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="仓库名称">
              <a-select v-model="storeId" placeholder="请选择仓库名称">
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
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="批次号">
              <a-input v-model="batchSn" placeholder="请输入批次号" />
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
        <a-button @click="optSparepartOne" type="primary">提交</a-button>
      </div>
    </a-drawer>

    <a-modal
      title="出库"
      :width="450"
      :visible="stockOutvisible"
      @ok="stockOutOk"
      @cancel="stockOutCancel"
      cancelText="关闭"
    >
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="出库量">
        <a-input
          v-model="amount"
          @keyup="amount = amount.replace(/[^\d]/g, '')"
          placeholder="请输入出库量"
        />
      </a-form-item>
    </a-modal>
    <a-modal
      title="库存预警"
      :width="450"
      :visible="stockEarlyWarningvisible"
      @ok="stockEarlyWarningtOk"
      @cancel="stockEarlyWarningCancel"
      cancelText="关闭"
    >
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="库存预警"
      >
        <a-input
          v-model="stockEarlyWarningNum"
          @keyup="
            stockEarlyWarningNum = stockEarlyWarningNum.replace(/[^\d]/g, '')
          "
          placeholder="请输入库存预警值"
        />
      </a-form-item>
    </a-modal>
    <sparePartsWarehouseOut ref="modelOut"></sparePartsWarehouseOut>
    <storageOfSpareParts ref="modelR"></storageOfSpareParts>
  </div>
</template>

<script>
import index from "@/assets/js/operationCenter/sparePartsManagement/sparePartsManagement/index.js";
import "@/assets/less/operationCenter/sparePartsManagement/sparePartsManagement/style.less";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>