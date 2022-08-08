<template>
  <a-drawer
      :title="title"
      :width="800"
      placement="right"
      :closable="false"
      @close="close"
      :visible="visible"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="lotSn">
          <a-input placeholder="请输入lotSn" v-decorator="['lotSn', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="客户名称">
          <a-input placeholder="请输入客户名称" v-decorator="['userName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="报装时间">
          <el-date-picker 
              style="width:100%"
              v-model="form.lotTime"
              placeholder="请选择时间"  
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'lotTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="报装内容">
          <a-input placeholder="请输入报装内容" v-decorator="['lotContent', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="任务分配人员">
          <a-input placeholder="请输入任务分配人员" v-decorator="['taskName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="实施人联系电话">
          <a-input placeholder="请输入实施人联系电话" v-decorator="['telephone', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="安装状态，数据字典">
          <a-input placeholder="请输入安装状态，数据字典" v-decorator="['installState', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="安装时间">
            <el-date-picker 
              style="width:100%"
              v-model="form.installTime"
              placeholder="请选择时间"  
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
            ></el-date-picker>
          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'installTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用途">
          <a-input placeholder="请输入用途" v-decorator="['purpose', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="审核验收意见,数据字典">
          <a-input placeholder="请输入审核验收意见,数据字典" v-decorator="['accepidea', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="删除标识0-正常,1-已删除">
          <a-input placeholder="请输入删除标识0-正常,1-已删除" v-decorator="['delFlag', {}]" />
        </a-form-item>
		
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
import index from '@assets/js/operationCenter/userInstallation/SettleUseralotModal__Style#Drawer.js';
export default {
  ...index
}
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>