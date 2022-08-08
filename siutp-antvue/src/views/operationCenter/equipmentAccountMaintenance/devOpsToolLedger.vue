<template>
    <div id="supplierManagementBox" class="margin12">
      <div class="screenCommonBox">
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="工具名称">
                  <a-select v-model="queryParam.toolType" placeholder="请选择工具名称">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option v-for="(item,index) in toolTypeList" :key="index" :value="item.code">{{item.title}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>    
              <a-col :md="6" :sm="12">
                  <a-form-item label="规格">
                      <a-input placeholder="请输入规格" v-model="queryParam.toolModel"/>
                  </a-form-item>
              </a-col>
              <a-col :md="6" :sm="12">
                <a-form-item label="仓库名称">
                    <a-select v-model="queryParam.storeId" placeholder="请选择仓库名称">
                      <a-select-option value="">全部</a-select-option>
                      <a-select-option v-for="(item,index) in queryStoreList" :key="index" :value="item.storeSn">{{item.storePosition}}</a-select-option>
                    </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
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
          <div class="table-operator" style="border-top: 5px">
              <a-button @click="handleAdd('add')"  type="primary" icon="plus">新增入库</a-button>
              
              <!-- <a-button type="primary" icon="edit" @click="handleChange">修改</a-button> -->
              <a-button type="primary" @click="handleExportXls('运维工具管理表')" icon="download">导出</a-button>
              <a-button type="primary" @click="optApplyVerifyList" icon="file-done">申请审核</a-button>
              <a-button type="primary" @click="optToolBorrowList" icon="file-done">申请历史</a-button>
          </div>
          <!-- 表格显示区域 -->
          <a-table
                ref="table"
                bordered
                size="middle"

                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                
                @change="handleTableChange">
                <span slot="maintainBtn" slot-scope="text, record, index">
                  <a  @click="takeDeliveryGoods(record)">出库</a>
                </span>
                
          </a-table>
      </a-card>
      <!-- 新增修改右侧弹框 -->
      <a-drawer
        :title="drawerTitle"
        :width="720"
        @close="onClose"
        :visible="visible"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
      >
        <a-form layout="vertical" hideRequiredMark>
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="工具">
                <a-select v-model="toolType" placeholder="请选择工具" @change="changeEquipmentModel">
                  <a-select-option v-for="(item,index) in toolTypeList" :key="index" :value="item.code">{{item.title}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="规格">
                <a-select v-model="equipmentSpecs" placeholder="请选择规格">
                  <a-select-option :value="item.code" v-for="(item,index) in equipmentSpecsList" :key="index">{{item.title}}</a-select-option>
                  
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            
            
            <a-col :span="12">
              <a-form-item label="供应商">
                <a-select v-model="supplierId" placeholder="请选择供应商">
                  <a-select-option v-for="(item,index) in supplierClassificationList" :key="index" :value="item.id">{{item.supplierName}}</a-select-option>
                </a-select>
                
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="生产厂家">
                <a-input
                  v-model="toolFactory"
                  
                  placeholder="请输入生产厂家"
                />
              </a-form-item>
            </a-col>
          </a-row>
          
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="仓库名称">
                <a-select v-model="storeId" placeholder="请选择仓库名称">
                  <a-select-option v-for="(item,index) in queryStoreList" :key="index" :value="item.storeSn">{{item.storePosition}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="批次号">
                
                <a-input
                  v-model="batchSn"
                  
                  placeholder="请选择批次号"
                />
              </a-form-item>
            </a-col>
          </a-row>  
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="生产日期">
                <a-date-picker style="width:100%" v-model="productDate" :disabledDate="disabledDate"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="有效期">
                <a-date-picker style="width:100%" v-model="endDate"/>
              </a-form-item>
            </a-col>
          </a-row> 
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="入库量">
                
                <a-input
                  v-model="amount"
                  @keyup="amount=amount.replace(/\D/g,'')"
                  placeholder="请选择入库量"
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
          <a-button :style="{marginRight: '8px'}" @click="onClose">
            关闭
          </a-button>
          <a-button @click="onSubmit" type="primary">提交</a-button>
        </div>
      </a-drawer>
      <!-- 入库弹框 -->
      <a-drawer
        title="入库"
        :width="720"
        @close="onClose2"
        :visible="visible2"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
      >
        <a-form layout="vertical" hideRequiredMark>
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="仓库名称">
                <a-select v-model="storeId" placeholder="请选择仓库名称">
                  <a-select-option v-for="(item,index) in queryStoreList" :key="index" :value="item.storeSn">{{item.storePosition}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="批次号">
                
                <a-input
                  v-model="batchSn"
                  
                  placeholder="请选择批次号"
                />
              </a-form-item>
            </a-col>
          </a-row>  
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="生产日期">
                <a-date-picker style="width:100%" v-model="productDate" :disabledDate="disabledDate"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="有效期">
                <a-date-picker style="width:100%" v-model="endDate"/>
              </a-form-item>
            </a-col>
          </a-row> 
          <a-row :gutter="16">
            
            <a-col :span="12">
              <a-form-item label="入库量">
                
                <a-input
                  v-model="amount"
                  @keyup="amount=amount.replace(/\D/g,'')"
                  placeholder="请选择入库量"
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
          <a-button :style="{marginRight: '8px'}" @click="onClose2">
            关闭
          </a-button>
          <a-button @click="onSubmit2" type="primary">提交</a-button>
        </div>
      </a-drawer>
      <!-- 出库弹框 -->
      <a-drawer
        title="出库"
        :width="720"
        @close="onClose3"
        :visible="visible3"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
      >
        <!-- 出库筛选区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
              <a-col :md="8" :sm="8">
                  <a-form-item label="工具编码">
                      <a-input placeholder="请输入工具编码" v-model="itemSn"/>

                  </a-form-item>
              </a-col> 
              <a-col :md="6" :sm="8" :offset="10">
                <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="takeDeliveryGoods()">查询</a-button>
                  
                </span>
                  
              </a-col>      
            </a-row>
          </a-form>
          <!-- 操作按钮区域 -->
          <div class="table-operator" style="border-top: 5px;margin-bottom:25px;">
            <a-button @click="warehousingOrReturn('warehousing')" type="primary" icon="plus">出库</a-button>
              
              
          </div>
        </div>
        <!-- 表格显示区域 -->
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns2"
          :dataSource="dataSource2"
          :pagination="ipagination2"
          :loading="loading2"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          @change="handleTableChange2">
          <!-- <a slot="maintainBtn">详情</a>
          <a-divider slot="maintainBtn" type="vertical"/> -->
          <span slot="maintainBtn" slot-scope="text, record, index">
            <a  @click="borrowJil(record)">外借历史</a>
            <!-- <a-divider slot="maintainBtn" type="vertical"/>
            <a  @click="warehousingOrReturn('warehousing',record)">出库</a> -->
            <a-divider slot="maintainBtn" v-if="record.itemStatus==2" type="vertical"/>
            <a  @click="returnClick(record)" v-if="record.itemStatus==2">归还</a>
            <a-divider slot="maintainBtn" type="vertical"/>
            <a  @click="maintain(record)">维护</a>
          </span>
              
        </a-table>
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
          <a-button :style="{marginRight: '8px'}" @click="onClose3">
            关闭
          </a-button>
          
        </div>
      </a-drawer>
      <!-- 借用记录 -->
      <a-drawer
        title="借用记录"
        :width="720"
        @close="onClose4"
        :visible="visible4"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
      >
        
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
  
              <a-col :md="12" :sm="8">
                  <a-form-item label="借用开始时间">
                    <a-date-picker style="width:100%" v-model="startTime"/>

                  </a-form-item>
              </a-col>
              <a-col :md="12" :sm="8">
                  <a-form-item label="借用结束时间">
                    <a-date-picker style="width:100%" v-model="endTime"/>

                  </a-form-item>
              </a-col>
    
            </a-row>
            <a-row :gutter="24">
              <a-col :md="12" :sm="8">
                  <a-form-item label="借用人">
                      <a-input placeholder="请输入借用人" v-model="userName"/>

                  </a-form-item>
              </a-col> 
              
              <a-col :md="6" :sm="8" :offset="6">
                <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button  type="primary" @click="borrowJil()">查询</a-button>
                  
                </span>
                  
              </a-col>      
            </a-row>
          </a-form>
        </div>
        
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns3"
          :dataSource="dataSource3"
          :pagination="ipagination3"
          :loading="loading3"

          @change="handleTableChange3">
          
              
        </a-table>
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
          <a-button :style="{marginRight: '8px'}" @click="onClose4">
            关闭
          </a-button>
          
        </div>
      </a-drawer>
      <!-- 出库弹框 -->
      <a-modal
        :title="warehousingOrReturnTitle"
        :width="450"
        :visible="warehousingOrReturnvisible"
        @ok="warehousingOrReturnOk"
        @cancel="warehousingOrReturnCancel"
        cancelText="关闭">
        <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="申请人姓名">
            <a-select
              showSearch
              placeholder="请输入申请人姓名"
              optionFilterProp="children"
              style="width: 100%"
              v-model="applicantName"
              @change="applicantNameChange"
              
            >
              <a-select-option v-for="(item,index) in managerList" :key="index" :value="item.id">{{item.name}}</a-select-option>

            </a-select>
        </a-form-item>
        <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="申请单号">
            <a-select style="width:100%;" v-model="applicantId" placeholder="请选择申请单号">
              <a-select-option v-for="(item,index) in applicantIdList" :key="index" :value="item.applySnValue">{{item.applySn}}</a-select-option>
            </a-select>
        </a-form-item>
      </a-modal>
      <!-- 归还弹框 -->
      <a-modal
        :title="returnTitle"
        :width="450"
        :visible="returnvisible"
        @ok="returnOk"
        @cancel="returnCancel"
        cancelText="关闭">
        <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="备注">
            <a-textarea placeholder="请输入备注" :rows="4" v-model="description"/>
        </a-form-item>
      </a-modal>
      <!-- 维护弹框 -->
      <a-modal
        title="维护"
        :width="450"
        :visible="maintainvisible"
        @ok="maintainOk"
        @cancel="maintainCancel"
        cancelText="关闭">
        <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="维护状态">
            <a-select style="width:100%;" v-model="goodsItemstatus" placeholder="请选择维护状态">
              <a-select-option v-for="(item,index) in goodsItemstatusList" :key="index" :value="item.code">{{item.title}}</a-select-option>
            </a-select>
        </a-form-item>
      </a-modal>
      <optApplyVerifyList ref="modalForm"></optApplyVerifyList> 
      <optToolBorrowList ref="modalForm2"></optToolBorrowList> 
  </div>
</template>

<script>
import index from '@/assets/js/operationCenter/equipmentAccountMaintenance/devOpsToolLedger.js'
export default{
  ...index
}
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>