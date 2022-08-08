<!-- 系统维护-人员权限-角色管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <!-- 搜索区域 -->
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :span="6">
              <a-form-item
                label="名称"
                :labelCol="{ span: 5 }"
                :wrapperCol="{ span: 18, offset: 1 }"
              >
                <a-input
                  placeholder="请输入名称查询"
                  v-model="queryParam.roleName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="创建时间"
                :labelCol="{ span: 5 }"
                :wrapperCol="{ span: 18, offset: 1 }"
              >
                <!-- <j-date
                                  v-model="queryParam.createTime_begin"
                                  :showTime="true"
                                  date-format="YYYY-MM-DD HH:mm:ss"
                                  style="width:45%"
                                  placeholder="请选择开始时间"
                                ></j-date> -->
                <el-date-picker
                  style="width: 45%"
                  v-model="queryParam.createTime_begin"
                  placeholder="请选择开始时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
                <span style="width: 10px">~</span>
                <el-date-picker
                  style="width: 45%"
                  v-model="queryParam.createTime_end"
                  placeholder="请选择结束时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
                <!-- <j-date
                                  v-model="queryParam.createTime_end"
                                  :showTime="true"
                                  date-format="YYYY-MM-DD HH:mm:ss"
                                  style="width:45%"
                                  placeholder="请选择结束时间"
                                ></j-date> -->
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <span
                style="float: left; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery">查询</a-button>
                <a-button style="margin-left: 8px" @click="searchReset"
                  >重置</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </div>
    <a-card :bordered="false" class="card-area">
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="margin-top: 5px">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button
          type="primary"
          icon="download"
          @click="handleExportXls('角色信息')"
          >导出</a-button
        >
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>

        <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
                  <a-menu slot="overlay">
                    <a-menu-item key="1" @click="batchDel">
                      <a-icon type="delete" />删除
                    </a-menu-item>
                  </a-menu>
                  <a-button style="margin-left: 8px">
                    批量操作
                    <a-icon type="down" />
                  </a-button>
                </a-dropdown> -->
      </div>

      <!-- table区域-begin -->
      <div class="tableDiv">
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"></i>
          已选择&nbsp;
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
          >项&nbsp;&nbsp;
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
          }"
          @change="handleTableChange"
        >
          <span slot="status" slot-scope="text, record">
            <a-switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              defaultChecked
              v-model="record.status"
              @change="changeStatus(record, $event)"
            />
          </span>
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" v-hasRole="'admin'" />
            <a-dropdown v-hasRole="'admin'">
              <a class="ant-dropdown-link">
                更多
                <a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="handlePerssion(record.id)">授权</a>
                </a-menu-item>
                <!-- <a-menu-item>
                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                      <a>删除</a>
                    </a-popconfirm>
                  </a-menu-item> -->
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <role-modal ref="modalForm" @ok="modalFormOk"></role-modal>
      <user-role-modal ref="modalUserRole"></user-role-modal>
    </a-card>
  </div>
</template>

<script>
import index from "@assets/js/system/RoleList.js";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";

.tableDiv
  .ant-table-thead
  > tr
  > th
  .ant-table-header-column
  .ant-table-column-sorters::before {
  background: red !important;
}
</style>