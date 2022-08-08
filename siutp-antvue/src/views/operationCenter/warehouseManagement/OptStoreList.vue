<!-- 运营中心-仓库管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="6" :sm="8">
              <a-form-item label="仓库名称">
                <a-input placeholder="请输入仓库名称" v-model="queryParam.storeName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="仓库编码">
                <a-input placeholder="请输入仓库编码" v-model="queryParam.storeSn"></a-input>
              </a-form-item>
            </a-col>
         
            <a-col :md="6" :sm="8" :offset="6">
              <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button class="ant-btn-border" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                
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
        <a-button type="primary" @click="setUpLibrary">设置库管</a-button>
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
          @change="handleTableChange">

          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
          </span>

        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <optStore-modal ref="modalForm" @ok="modalFormOk"></optStore-modal>
      <a-modal
        title="设置库管"
        :width="400"
        :visible="setUpLibraryvisible"
        :confirmLoading="setUpLibraryconfirmLoading"
        @ok="setUpLibraryhandleOk"
        @cancel="setUpLibraryhandleCancel"
        cancelText="关闭">
        <a-spin :spinning="setUpLibraryconfirmLoading">
          <a-form>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="管理员">
              <a-select
                showSearch
                placeholder="请选择管理员"
                optionFilterProp="children"
                style="width: 100%"
                v-model="manager"
                
                @change="managerhandleChange"
    
              >
                <a-select-option v-for="(item,index) in managerList" :key="index" :value="item.id">{{item.name}}</a-select-option>

              </a-select>
            </a-form-item>
          </a-form>
          <div class="setUpStoreBox">
             <a-tree
              checkable
              @expand="onExpand"
              :expandedKeys="expandedKeys"
              :autoExpandParent="autoExpandParent"
              v-model="checkedKeys"
              @select="onSelect"
              :selectedKeys="selectedKeys"
              :treeData="treeData"
              :replaceFields="replaceFields"
            />
          </div>
         
        </a-spin>
      </a-modal>
    </a-card>
  </div>
  
</template>

<script>
  import '@/assets/less/operationCenter/warehouseManagement/optStoreList.less'
  import index from '@/assets/js/operationCenter/warehouseManagement/optStoreList.js'

  export default {
      ...index
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>