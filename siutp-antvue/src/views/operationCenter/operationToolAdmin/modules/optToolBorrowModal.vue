<template>
  <a-modal
    title="申请"
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
          label="运维工具id">
          <a-input placeholder="请输入运维工具id" v-decorator="['toolId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="申请信息编号">
          <a-input placeholder="请输入申请信息编号" v-decorator="['applySn', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="借用时间">
            <el-date-picker v-model="form.borrowTime" placeholder="请选择时间"  type="datetime" format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>

          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'borrowTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否归还  1-是，0-否">
          <a-input placeholder="请输入是否归还  1-是，0-否" v-decorator="['backStatus', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="归还时间">
            <el-date-picker v-model="form.backTime" placeholder="请选择时间"  type="datetime" format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>

          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'backTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="借用人">
          <a-input placeholder="请输入借用人" v-decorator="['userId', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="备注信息">
          <a-input placeholder="请输入备注信息" v-decorator="['description', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import index from '@/assets/js/operationCenter/operationToolAdmin/modules/optToolBorrowModal.js'
  export default{
    ...index
  }
</script>
