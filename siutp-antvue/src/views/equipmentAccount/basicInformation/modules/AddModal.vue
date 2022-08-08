<template>
<!-- 资产中心-资产管理-资产录入-新增、编辑右侧弹框 -->
  <a-drawer
    :title="typeof model.id === 'undefined' ? title + '基本信息' : title"
    :width="500"
    @close="handleCancel"
    :visible="visible"
    :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <div class="table-page-search-wrapper">
      <a-form :form="form">
        <a-row :gutter="24">
           <a-col :span="24">
              <a-form-item
                label="资产名称"> 
                <a-input v-if="!isShow" placeholder="请输入资产名称" v-model="model.baseName" />
              </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item
              label="供应商">
              <a-select
                showSearch
                v-if="!isShow"
                placeholder="请选择供应商"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.equipmentSupplier"
              >
                <a-select-option :value="item.id" v-for="(item,index) in supplierClassificationList" :key="index">{{item.supplierName}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备类型">
              <a-select
                showSearch
                placeholder="请选择设备类型"
                v-if="!isShow"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.equipmentType"
                @change="changeType"
              >
                <a-select-option :value="item.code" v-for="(item,index) in equipmentTypeList" :key="index">{{item.title}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备型号">
              <a-select
                showSearch
                placeholder="请选择设备型号"
                v-if="!isShow"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.equipmentModel"
                @change="changeMondel"
              >
                <a-select-option v-for="(item,index) in add_equipmentModelList" :key="index" :value="item.code">{{item.title}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备规格">
              <a-select
                showSearch
                placeholder="请选择设备规格"
                v-if="!isShow"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.equipmentSpecs"
                @change="changeSpecs"
              >
                <a-select-option v-for="(item,index) in add_equipmentSpecsList" :key="index" :value="item.code">{{item.title}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
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
      <a-button :style="{marginRight: '8px'}" @click="handleCancel">
        关闭
      </a-button>
      <a-button v-if="!isShow" @click="handleOk" type="primary">提交</a-button>
    </div>
  </a-drawer>

</template>

<script>
	import addModal from '@/assets/js/equipmentAccount/basicInformation/modules/AddModal.js';
	export default{
	    ...addModal
	}
    
</script>

<style  scoped>
	    @import '~@/assets/less/equipmentAccount/basicInformation/modules/AddModal.less'

</style>