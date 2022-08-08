<!-- 运营中心-班组班次-班组管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="班组名称">
                <a-input
                  placeholder="请输入班组名称"
                  v-model="queryParam.teamName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="班组状态">
                <a-select
                  placeholder="请选择班组状态"
                  v-model="queryParam.teamStatus"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option value="0">停用</a-select-option>
                  <a-select-option value="1">启用</a-select-option>
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
          @click="handleExportXls('班组信息表')"
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
            <a @click="handleEdit(record)">修改</a>

            <!-- <a-divider type="vertical" />
            <a @click="handleEdit(record)">详情</a> -->
          </span>
          <a-switch
            slot="switch"
            checkedChildren="启用"
            slot-scope="text, record, index"
            unCheckedChildren="停用"
            v-model="record.teamStatusCode == 0 ? false : true"
            @change="teamStatusCodeChange(record)"
            defaultChecked
          />
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <workTeam-modal ref="modalForm" @ok="modalFormOk"></workTeam-modal>
    </a-card>
  </div>
</template>

<script>
import index from "@/assets/js/teamanagement/workTeamList.js";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>