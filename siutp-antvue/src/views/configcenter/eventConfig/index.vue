<template>
  <!-- 配置中心-短信事件 -->
  <div class="margin12">
    <div class="screenCommonBox content">
      <div class="table-page-search-wrapper" id="isScreen">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="事件名称">
                <a-input
                  placeholder="请输入事件名称"
                  v-model="queryParam.eventName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="事件code">
                <a-input
                  placeholder="请输入事件code"
                  v-model="queryParam.eventCode"
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
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="handleAdd('add')" type="primary" icon="plus"
          >新增</a-button
        >
        <a-button
          type="primary"
          @click="handleExportXls('事件与配置绑定')"
          icon="download"
          >导出</a-button
        >
      </div>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="tableDataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 1200 }"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        @change="handleTableChange"
      >
        <span slot="eventStatus" slot-scope="text, record, index">
          <a-switch
            checkedChildren="启用"
            unCheckedChildren="停用"
            v-model="record.eventStatus"
            defaultChecked
            @change="editWorkingStatusChange(record)"
          />
        </span>

        <span slot="maintainBtn" slot-scope="text, record, index">
          <a @click="handleAdd('change', record)">编辑</a>
        </span>
      </a-table>
    </a-card>

    <!-- 新增、编辑右侧滑框开始 -->
    <a-drawer
      :title="drawerTitle"
      :width="720"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100% )',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-form-model ref="ruleForm" :model="form">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="事件名称">
              <!--<a-form-model-item label="事件名称" prop="eventName" ref="eventName">-->
              <a-input v-model="form.eventName" placeholder="请输入事件名称" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-model-item label="事件code">
              <!--<a-form-model-item label="事件code" prop="eventCode" ref="eventCode">-->
              <a-input
                :disabled="drawerTitle == '修改'"
                v-model="form.eventCode"
                placeholder="请输入事件code"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="20">
          <a-col :span="12">
            <a-form-model-item label="短信配置">
              <!--<a-form-model-item label="短信配置" prop="templateId">-->

              <a-select
                placeholder="请选择短信配置"
                style="width: 100%"
                v-model="form.templateId"
              >
                <a-select-option
                  :value="item.id"
                  v-for="(item, index) in SMSDropdownList"
                  :key="index"
                >
                  {{ item.templateName }}
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

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
        <!-- <a-button :style="{marginRight: '8px'}" @click="resetForm">关闭</a-button> -->
        <a-button :style="{ marginRight: '8px' }" @click="onClose"
          >关闭</a-button
        >

        <a-button @click="onSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
     <!-- 新增、编辑右侧滑框End-->
  </div>
</template>

<script>
import index from "@/assets/js/configcenter/eventConfig.js";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
