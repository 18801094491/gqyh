<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="时间编号"
        >
          <a-input placeholder="请输入时间编号" v-decorator="['warnSn', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警名称"
        >
          <a-input
            placeholder="请输入告警名称"
            v-decorator="['warnName', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警等级（一般，警告，严重，紧急）"
        >
          <a-input
            placeholder="请输入告警等级（一般，警告，严重，紧急）"
            v-decorator="['warnLevel', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警类型(采集告警，工单告警，应急事件告警，计划任务告警)"
        >
          <a-input
            placeholder="请输入告警类型(采集告警，工单告警，应急事件告警，计划任务告警)"
            v-decorator="['warnType', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警时间"
        >
          <el-date-picker
            v-model="form.warnTime"
            placeholder="请选择告警时间"
            type="datetime"
            format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>

          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'warnTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警状态，0-初始化，1-未处理，2-已处理，3-已关闭"
        >
          <a-input
            placeholder="请输入告警状态，0-初始化，1-未处理，2-已处理，3-已关闭"
            v-decorator="['warnStatus', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警产生渠道(系统，手工)"
        >
          <a-input
            placeholder="请输入告警产生渠道(系统，手工)"
            v-decorator="['warnWay', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="操作时间"
        >
          <el-date-picker
            v-model="form.operateTime"
            placeholder="请选择时间"
            type="datetime"
            format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>

          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'operateTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="持续时长(operate_time和warn_time差值)"
        >
          <a-input
            placeholder="请输入持续时长(operate_time和warn_time差值)"
            v-decorator="['duration', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="操作人id"
        >
          <a-input
            placeholder="请输入操作人id"
            v-decorator="['operator', {}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="删除标识0-正常,1-已删除"
        >
          <a-input
            placeholder="请输入删除标识0-正常,1-已删除"
            v-decorator="['delFlag', {}]"
          />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import index from "@/assets/js/operationCenter/alarmManagement/modules/businessWarnModa.js";
export default {
  ...index,
};
</script>
<style lang="less" scoped>
</style>