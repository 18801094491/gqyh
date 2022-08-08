<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="applySn">
          <a-input placeholder="请输入applySn" v-decorator="['applySn', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="外部数据id">
          <a-input placeholder="请输入外部数据id" v-decorator="['dataId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="货物类型">
          <a-input placeholder="请输入货物类型" v-decorator="['dataType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="申请数量">
          <a-input-number v-decorator="[ 'amount', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="期望领用时间">
            <el-date-picker v-model="form.wantedTime" placeholder="请选择时间"  type="datetime" format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>

          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'wantedTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="紧急程度，数据字典">
          <a-input placeholder="请输入紧急程度，数据字典" v-decorator="['emergentLevel', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="审核状态">
          <a-input placeholder="请输入审核状态" v-decorator="['verifyStatus', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注信息">
          <a-input placeholder="请输入备注信息" v-decorator="['applyDesc', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="删除标识0-正常,1-已删除">
          <a-input placeholder="请输入删除标识0-正常,1-已删除" v-decorator="['delFlag', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import index from '@/assets/js/operationCenter/operationToolAdmin/modules/optGoodsApplyModal.js'
  export default{
    ...index
  }
</script>
