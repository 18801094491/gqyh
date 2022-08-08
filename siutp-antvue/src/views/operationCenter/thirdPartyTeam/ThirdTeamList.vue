<!-- 运营中心-班组班次-三方团队 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="团队编号">
                <a-input
                  placeholder="请输入团队编号"
                  v-model="queryParam.teamSn"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="团队名称">
                <a-input
                  placeholder="请输入团队名称"
                  v-model="queryParam.teamName"
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
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
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
          :scroll="{ x: 1600 }"
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <!-- <a-divider type="vertical" />
          <a @click="handleEdit2(record,'xq')">详情</a> -->
          </span>
          <a
            slot="fileName"
            slot-scope="text, record"
            :href="record.fileUrl"
            target="_blank"
            >{{ record.fileName }}</a
          >
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <thirdTeam-modal ref="modalForm" @ok="modalFormOk"></thirdTeam-modal>
    </a-card>
  </div>
</template>

<script>
import index from "@/assets/js/operationCenter/thirdPartyTeam/thirdTeamList.js";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>