<!-- 资产中心-供应商管理 -->
<template>
  <div id="supplierManagementBox" class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="供应商编码">
                <a-input
                  placeholder="请输入供应商编码"
                  v-model="queryParam.code"
                  @keyup="
                    queryParam.code = queryParam.code.replace(/[^\w\.\/]/gi, '')
                  "
                />
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
              <a-form-item label="供应商名称">
                <a-input
                  placeholder="请输入供应商名称"
                  v-model="queryParam.name"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="建档日期">
                <a-date-picker v-model="queryParam.startTime" /> ~
                <a-date-picker v-model="queryParam.endTime" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6" :offset="6">
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
          >新增</a-button
        >
      </div>
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
        :scroll="{ x: 1600 }"
        @change="handleTableChange"
      >
        <a
          slot="maintainBtn"
          slot-scope="text, record, index"
          @click="handleAdd('change', record)"
          >修改</a
        >
        <a-switch
          slot="supplierStatus"
          checkedChildren="启用"
          slot-scope="text, record, index"
          unCheckedChildren="停用"
          v-model="record.status"
          defaultChecked
          @change="supplierStatusChange(record)"
        />
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
            <a-form-item label="供应商编码">
              <a-input
                v-model="supplierCode"
                @keyup="supplierCode = supplierCode.replace(/\D/g, '')"
                placeholder="请输入供应商编码"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商名称">
              <a-input v-model="supplierName" placeholder="请选择供应商名称" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="供应商类别">
              <a-select
                v-model="supplierCategory"
                placeholder="请选择供应商类别"
              >
                <a-select-option
                  v-for="(item, index) in supplierClassificationList"
                  :key="index"
                  :value="item.code"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商营业执照编号">
              <a-input
                v-model="supplierBusinessLicenseNo"
                @keyup="
                  supplierBusinessLicenseNo = supplierBusinessLicenseNo.replace(
                    /[^\d|chun]/g,
                    ''
                  )
                "
                placeholder="请输入供应商营业执照编号"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="联系人">
              <a-input v-model="contacts" placeholder="请输入联系人" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="联系电话">
              <a-input
                v-model="contactNumber"
                :maxLength="11"
                @keyup="contactNumber = contactNumber.replace(/\D/g, '')"
                placeholder="请输入联系电话"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="供应设备类别">
              <a-select
                v-model="supplyEquipment"
                placeholder="请选择供应设备类别"
              >
                <a-select-option
                  v-for="(item, index) in equipmentCategoryList"
                  :key="index"
                  :value="item.code"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商状态">
              <a-select v-model="supplierStatus" placeholder="请选择供应商状态">
                <a-select-option value="0">停用</a-select-option>
                <a-select-option value="1">启用</a-select-option>
              </a-select>
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
</template>

<script>
import index from "@/assets/js/operationCenter/sparePartsManagement/supplierManagement/supplierManagement.js";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>