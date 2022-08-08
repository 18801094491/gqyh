<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="模板名称">
                <a-input v-model="queryParam.tplName" placeholder="请输入模板名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="模板类型">
                <a-select v-model="queryParam.tplType" placeholder="请选择模板类型">
                  <a-select-option value>全部</a-select-option>
                  <a-select-option
                    v-for="(item,index) in jobTypeList"
                    :key="index"
                    :value="item.code"
                  >{{item.title}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="状态">
                <a-select v-model="queryParam.tplStatus" placeholder="请选择状态">
                  <a-select-option value>全部</a-select-option>
                  <a-select-option
                    v-for="(item,index) in jobStatusList"
                    :key="index"
                    :value="item.code"
                  >{{item.title}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="操作人">
                <a-input v-model="queryParam.createUser" placeholder="请输入操作人"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="4" :sm="8" :offset="20">
              <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery('')" icon="search">查询</a-button>
                <a-button
                  class="ant-btn-border"
                  @click="searchReset"
                  icon="reload"
                  style="margin-left: 8px"
                >重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </div>
    <a-card :bordered="false">
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="handleAdd" type="primary" icon="plus" style="margin-right:6px;">新增</a-button>
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
        <span slot="action" slot-scope="text, record">
          <a :data-data="record" @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDel(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>

              <a-menu-item>
                <a href="javascript:;" @click="handleDataItem(record)">数据项</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </a-card>
    <!--新增、修改、详情-->
    <add-work-modal
      ref="modalForm"
      @ok="modalFormOk"
      :jobTypeList="jobTypeList"
      :jobStatusList="jobStatusList"
    ></add-work-modal>
    <!--数据项-->
    <data-item-list ref="dataItemForm" @ok="modalFormOk"></data-item-list>
  </div>
</template>

<script>

  import index from '@assets/js/workOrderManagement/workTemp/index.js';
  export default {
    ...index
  }
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>