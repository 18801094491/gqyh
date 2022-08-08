<!-- 信息中心-备品备件-库存管理-出库单-->
<template>
  <a-drawer
    title="出库单"
    width="100%"
    @close="sparePartsWarehouseOutonClose"
    :visible="sparePartsWarehouseOutvisible"
    :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <div id="supplierManagementBox">
        <div class="screenCommonBox">
          <div class="table-page-search-wrapper">
            <a-form layout="inline">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item label="备件名称">
                          <a-input placeholder="请输入备件名称" v-model="queryParam.sparepartName"></a-input>
                        </a-form-item>
                    </a-col>    
                    <a-col :md="6" :sm="8">
                        <a-form-item label="批次号">
                          <a-input placeholder="请输入批次号" v-model="queryParam.batchSn"></a-input>
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
            <a-table
                  ref="table"
                  bordered
                  size="middle"
                  rowKey="id"
                  :columns="columns"
                  :dataSource="dataSource"
                  :pagination="ipagination"
                  :loading="loading"
                  :scroll="{x:1400}"
                  @change="handleTableChange">
                  
            </a-table>
        </a-card>
        
        

    </div>
  </a-drawer>
</template>

<script>
  import index from '@/assets/js/operationCenter/sparePartsManagement/sparePartsWarehouseOutModel/index.js'
  export default{
    ...index
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>