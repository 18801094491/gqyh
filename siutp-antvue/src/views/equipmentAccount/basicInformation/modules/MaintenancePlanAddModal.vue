<template>
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
                label="计划名称"> 
                <a-input v-if="!isShow" placeholder="请输入计划名称" v-model="model.baseName" />
              </a-form-item>
          </a-col>
          <a-col :span="24">
              <a-form-item
                label="提前提醒时间"> 
                <a-input v-if="!isShow" placeholder="请输入提前提醒时间" v-model="model.baseName" />
              </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item
              label="养护时间">
                <a-input v-if="!isShow" placeholder="请输入养护时间" v-model="model.baseName" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item
              label="养护描述">
                <a-input v-if="!isShow" placeholder="请输入养护描述" v-model="model.baseName" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item
              label="上传附件">
                <a-input v-if="!isShow" placeholder="请输入上传附件" v-model="model.baseName" />
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
	import index from '@/assets/js/equipmentAccount/basicInformation/modules/MaintenancePlanAddModal.js';
	export default{
	    ...index
	}
</script>

<style  scoped>
	@import '~@assets/less/equipmentAccount/basicInformation/modules/MaintenancePlanAddModal.less'
</style>