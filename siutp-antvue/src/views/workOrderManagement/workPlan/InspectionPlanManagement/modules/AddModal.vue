<template>
  <a-drawer
    :title="title"
    :width="700"
    @close="handleCancel"
    :visible="visible"
    :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <div class="table-page-search-wrapper">
      <a-form :form="form">
        <a-row :gutter="24">
           <a-col :span="12">
              <a-form-item label="计划名称"> 
                <a-input :disabled="isShow" placeholder="请输入计划名称" v-model="model.planName" />
              </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="数据模板">
              <a-select class="customer-select"
                showSearch
                placeholder="请选择数据模板"
                :disabled="isShow"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.tplId"
              >
                <a-select-option v-for="(item,index) in tplList" :key="index" :value="item.id">{{item.tplName}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="选择线路">
              <a-select class="customer-select"
                showSearch
                placeholder="请选择线路"
                :disabled="isShow"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.routeId"
              >
                <a-select-option v-for="(item,index) in lineList" :key="index" :value="item.id">{{item.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="开始日期">
              <el-date-picker
                v-model="model.startDate"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="截止日期">
              <el-date-picker
                v-model="model.endDate"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="巡检员">
              <a-input readonly placeholder="请输入巡检员" v-model="model.usersId" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="周期">
              <a-radio-group :disabled="isShow" v-model="model.period">
                <a-radio value="1">每天</a-radio>
                <a-radio value="2">每周</a-radio>
                <a-radio value="3">每月</a-radio>
                <a-radio value="4">单次</a-radio>
              </a-radio-group>
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
    import index from '@assets/js/workOrderManagement/workPlan/InspectionPlanManagement/AddModal.js';
    export default {
      ...index
    }
</script>
<style>
.customer-select.ant-select-disabled .ant-select-selection {
  background-color: #f5f5f5!important;
}
  
</style>
<style lang="less" scoped>
  .uploadBtn {
        width: 100px;
        height: 30px;
        line-height: 30px;
        position: relative;
        overflow: hidden;

        #uploadBtn {
            position: absolute;
            width: 100%;
            height: 100%;
            left: 0;
            top: 0;
            padding: 1000px;
            cursor: pointer;
        }
    }

    /deep/ .ant-input-number {
        width: 100%;
    }
</style>