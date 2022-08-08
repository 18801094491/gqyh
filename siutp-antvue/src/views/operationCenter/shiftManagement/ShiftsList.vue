<!-- 运营中心-班组班次-班次管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="班次名称">
                <a-input
                  placeholder="请输入班次名称"
                  v-model="queryParam.shiftsName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="班次状态">
                <!-- <a-input placeholder="请输入班次描述" v-model="queryParam.shiftsDescribe"></a-input> -->
                <a-select
                  v-model="queryParam.shiftsStatus"
                  placeholder="请选择班次状态"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option value="1">启用</a-select-option>
                  <a-select-option value="0">停用</a-select-option>
                </a-select>
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
      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button
          type="primary"
          icon="download"
          @click="handleExportXls('班次管理')"
          >导出</a-button
        >
      </div>

      <!-- table区域-begin -->
      <div>
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
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a @click="details(record)">详情</a>
          </span>
          <span
            slot="shiftsStatus"
            :data-Status="record.shiftsStatus"
            slot-scope="text, record"
          >
            <a-switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              v-model="record.shiftsStatus == 0 ? false : true"
              defaultChecked
              @change="shiftsStatusChange(record)"
            />
          </span>
          <span slot="startTimeOrendTime" slot-scope="text, record">
            {{ record.startTime + "-" + record.overTime }}
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <shifts-modal ref="modalForm" @ok="modalFormOk"></shifts-modal>
      <!-- <a-drawer
        :title="drawerTitle"
        :width="910"
        @close="onClose"
        :visible="visible"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
      >

      </a-drawer> -->
    </a-card>
  </div>
</template>
<script>
import index from "@/assets/js/operationCenter/shiftManagement/shiftsList.js";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>