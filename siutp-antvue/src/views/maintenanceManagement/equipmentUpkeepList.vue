<!-- 运营中心-维保管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="12" :sm="8">
              <a-form-item label="资产名称">
                <a-input placeholder="请输入资产名称" v-model="queryParam.equipmentName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
              <a-form-item label="维保类型">
                <a-select v-model="queryParam.type" placeholder="请选择维保类型">
                  <a-select-option value="" >全部</a-select-option>
                  <a-select-option :value="item.code" v-for="(item,index) in upkeepTypeList" :key="index">{{item.title}}</a-select-option>

                </a-select>
              </a-form-item>
            </a-col>
            
            

          </a-row>
          <a-row :gutter="24">
            <a-col :md="16" :sm="8">
              <a-form-item label="上报日期">
                <a-date-picker v-model="queryParam.startTime"  /> ~ <a-date-picker v-model="queryParam.endTime" />
              </a-form-item>
            </a-col>
            
           
            
            
            <a-col :md="6" :sm="8" :offset="2">
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
        <a-button @click="equipmentUpkeep('xz')" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('维保记录')">导出</a-button>
        
      </div>

      <!-- table区域-begin -->
      <div>
        

        <a-table
          ref="table"
          size="middle"
          bordered

          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          
          @change="handleTableChange">
          
          <span slot="action" slot-scope="text, record">
            <a @click="details(record)">详情</a>
            <a-divider type="vertical"/>
            <a @click="equipmentUpkeep('xg',record)">修改</a>
          </span>

        </a-table>
      </div>
      <!-- table区域-end -->

      
    </a-card>
    <a-modal
      title="详情"
      :width="500"
      :visible="visible"
      :confirmLoading="confirmLoading"
      class="equipmentUpkeepList"
      @cancel="handleCancel"
      cancelText="关闭">
      
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
        
          <a-form-item

            label="参与人">
            <div class="equipmentUpkeepListDiv">
              {{upkeepUsers}}
            </div>
          </a-form-item>
          <a-form-item
            label="附件">
            
            <ul class="equipmentUpkeepImgBox clearfix">
              <li v-for="(item,index) in upkeepAttachList" :key="index">
                <img :src="item.upkeepThumb" alt="" @click="showImg(item.upkeepImage)">
              </li>
            </ul>
          </a-form-item>
          
      
        </a-form>
      </a-spin>
      <template slot="footer">
        <a-button type="primary" @click.stop="()=>{visible=false}">关闭</a-button>
      </template>
    </a-modal>
    <a-modal
      title="图片预览"
      :width="600"
      :visible="visible2"
      :confirmLoading="confirmLoading2"
      @cancel="handleCancel2"
      cancelText="关闭">
      <div class="showImgSrc">
        <img :src="showImgSrc" alt="">
      </div>
      
      <template slot="footer">
        <a-button type="primary" @click.stop="()=>{visible2=false}">关闭</a-button>
      </template>
    </a-modal>
    <a-drawer
      :title="equipmentUpkeepTitle"
      :width="720"
      @close="equipmentUpkeeponClose"
      :visible="equipmentUpkeepvisible"
      :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
      <a-form layout="vertical" hideRequiredMark>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="资产名称">
              <a-select
                placeholder="选择资产名称"
                optionFilterProp="children"
                style="width: 100%"
                v-model="equipmentId"
                showSearch
              >
                <a-select-option :value="item.id"  v-for="(item,index) in equipmentList" :key="index">
                  {{item.equipmentSn}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="维保类型">
              <a-select placeholder="请选择维保类型" v-model="upkeepType">
                <a-select-option :value="item.code" v-for="(item,index) in upkeepTypeList" :key="index">{{item.title}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="维保原因">
              <a-textarea placeholder="请填写维保原因" :rows="4" v-model="upkeepReason" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="维保结果">
              <a-textarea placeholder="请填写维保结果" :rows="4" v-model="upkeepResult"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="维保内容">
              <a-textarea placeholder="请填写维保内容" :rows="4" v-model="upkeepContent"/>
            </a-form-item>
          </a-col>
          
          
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="上报人">
              <a-select
                placeholder="选择上报人"
                optionFilterProp="children"
                style="width: 100%"
                v-model="upkeepCreator"
                showSearch
              >
                <a-select-option :value="item.id"  v-for="(item,index) in managerList" :key="index">
                  {{item.name}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="参与人">
              <a-select
                placeholder="选择参与人"
                optionFilterProp="children"
                style="width: 100%"
                v-model="upkeepUsers"
                mode="multiple"
              >
                <a-select-option :value="item.id"  v-for="(item,index) in managerList2" :key="index">
                  {{item.name}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16" style="padding-bottom:40px;">
            <a-col :span="24">
                <a-form-item label="附件">
                  <a-button class="uploadBtn" >
                    <a-icon type="upload" /> 上传
                    <input type="file" name="file" id="uploadBtn" multiple="multiple" @change="upfileClick">
                  </a-button>
                </a-form-item>
                <div v-if="fileList.length">
                  <div class="flieBox flieImgBox" v-for="(item,index) in fileList" :key="index">
                    <p><img :src="item.fileThumbPath" alt="" srcset="" @click="showImg(item.filePath)"></p>
                    <a-icon type="close-circle" class="close" @click="removeFile(index)"/>
                  </div>
                </div>
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
        <a-button :style="{marginRight: '8px'}" @click="equipmentUpkeeponClose">
          关闭
        </a-button>
        <a-button @click="addSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
  </div>
  
</template>

<script>
  import '@/assets/less/maintenanceManagement/style.less'

  import index from '@/assets/js/maintenanceManagement/equipmentUpkeepList.js'
  export default{
    ...index
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>