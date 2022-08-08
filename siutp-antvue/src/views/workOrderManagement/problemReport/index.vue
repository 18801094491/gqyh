<!-- 运营中心-工单管理-问题上报 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="5" :sm="8">
              <a-form-item label="上报人">
                <a-input
                  placeholder="请输入上报人"
                  v-model="queryParam.createUser"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="10" :sm="12">
              <a-form-item label="上报日期">
                <el-date-picker
                  v-model="queryParam.startTime"
                  type="date"
                  placeholder="选择开始时间"
                ></el-date-picker>
                ~
                <el-date-picker
                  v-model="queryParam.endTime"
                  type="date"
                  placeholder="选择结束时间"
                ></el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <a-form-item label="状态">
                <a-select
                  v-model="queryParam.problemStatus"
                  placeholder="请选择状态"
                >
                  <a-select-option value>全部</a-select-option>
                  <a-select-option
                    v-for="(item, index) in warnStatusList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery('')" icon="search"
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
      <div class="table-operator" style="border-top: 5px">
        <a-button
          @click="handleAdd('add')"
          type="primary"
          icon="plus"
          style="margin-right: 6px"
          >新增</a-button
        >
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        row-key="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
      >
        <span slot="problemDescription" slot-scope="text, record">
          <a-tooltip placement="top">
            <template slot="title">
              <span>{{ record.problemDescription }}</span>
            </template>
            <div>
              {{
                record.problemDescription &&
                record.problemDescription.length > 10
                  ? record.problemDescription.substring(0, 10) + "..."
                  : record.problemDescription
              }}
            </div>
          </a-tooltip>
        </span>
        <span slot="action" slot-scope="text, record">
          <a
            :data-data="record"
            @click="handleAdd('change', record)"
            v-if="record.problemStatus == '0'"
            >修改</a
          >
          <a-divider type="vertical" v-if="record.problemStatus == '0'" />
          <a-popconfirm
            title="确定关闭吗?"
            @confirm="() => closeList(record)"
            v-if="record.problemStatus != '2' && record.problemStatus != '3'"
          >
            <a>关闭</a>
          </a-popconfirm>
        </span>
      </a-table>
    </a-card>
    <a-drawer
      :title="drawerTitle"
      :width="500"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-form hideRequiredMark>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="问题名称">
              <a-input
                v-model="problemName"
                placeholder="请输入问题名称"
              ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="问题类型">
              <a-select v-model="problemType" placeholder="请选择问题类型">
                <a-select-option
                  v-for="(item, index) in problemTypeList"
                  :key="index"
                  :value="item.code"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="问题设备信息">
              <a-select v-model="equipmentId" placeholder="请选择问题设备信息">
                <a-select-option
                  v-for="(item, index) in equipmentList"
                  :key="index"
                  :value="item.id"
                  >{{ item.equipmentSn }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="问题描述">
              <a-textarea
                :rows="4"
                v-model="problemDescription"
                placeholder="请输入问题描述"
              />
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
        <a-button :style="{ marginRight: '8px' }" @click="onClose"
          >关闭</a-button
        >
        <a-button @click="addSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import index from "@assets/js/workOrderManagement/problemReport/index.js";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
<style scoped>
</style>