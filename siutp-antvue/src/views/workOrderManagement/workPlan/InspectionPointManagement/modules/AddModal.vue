<template>
  <a-drawer
    :title="title + '巡检点'"
    :width="700"
    @close="handleCancel"
    :visible="visible"
    :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <div class="table-page-search-wrapper">
      <a-form :form="form">
        <a-row :gutter="24">
           <a-col :span="12">
              <a-form-item label="巡检点名称"> 
                <a-input :disabled="isShow" placeholder="请输入巡检点名称" v-model="model.name" />
              </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="注意事项">
              <a-input :disabled="isShow" placeholder="请输入注意事项" v-model="model.attention" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注">
              <a-input :disabled="isShow" placeholder="请输入备注" v-model="model.remark" />
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
            <a-form-item label="巡检点类别">
              <a-select class="customer-select"
                showSearch
                placeholder="请选择巡检点类别"
                :disabled="isShow"
                optionFilterProp="children"
                style="width: 100%"
                @change="changeCategory"
                v-model="model.category"
              >
                <a-select-option v-for="(item,index) in inPointCategoryList" :key="index" :value="item.code">{{item.title}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12" v-if="model.category * 1 !== 4">
            <a-form-item label="根据类别显示">
              <a-select class="customer-select"
                showSearch
                placeholder="请选择根据类别显示"
                :disabled="isShow"
                optionFilterProp="children"
                style="width: 100%"
                @change="changeShow"
                v-model="model.dataId"
              >
                <a-select-option v-for="(item,index) in childList" :key="index" :value="item.id">{{item.name}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24" v-show="model.category * 1 === 4">
            <div style="width: 100%;height:300px">
              <div id="pointMap"></div>
            </div>
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
   import index from '@assets/js/workOrderManagement/workPlan/InspectionPointManagement/AddModal.js';
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
    #pointMap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
</style>