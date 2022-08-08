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
          label="shiftsName">
          <a-input placeholder="请输入shiftsName" v-decorator="['shiftsName', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="班次描述">
          <a-input placeholder="请输入班次描述" v-decorator="['shiftsDescribe', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="上班时间">
          <a-input placeholder="请输入上班时间" v-decorator="['startTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="下班时间">
          <a-input placeholder="请输入下班时间" v-decorator="['overTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="下班是否需要打卡,0-否，1-是">
          <a-input placeholder="请输入下班是否需要打卡,0-否，1-是" v-decorator="['offSign', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="上班时长，小时">
          <a-input-number v-decorator="[ 'workHours', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="开始休息时间">
            <el-date-picker v-model="form.restStartTime" placeholder="请选择时间"  type="datetime" format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>
          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'restStartTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="结束休息时间">
            <el-date-picker v-model="form.restOverTime" placeholder="请选择时间"  type="datetime" format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>
          <!-- <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'restOverTime', {}]" /> -->
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="启停用状态">
          <a-input placeholder="请输入启停用状态" v-decorator="['shiftsStatus', {}]" />
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
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "ShiftsModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        },
        url: {
          add: "/settle/shifts/add",
          edit: "/settle/shifts/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'shiftsName','shiftsDescribe','startTime','overTime','offSign','workHours','shiftsStatus','delFlag'))
		  //时间格式化
          this.form.setFieldsValue({restStartTime:this.model.restStartTime?moment(this.model.restStartTime):null})
          this.form.setFieldsValue({restOverTime:this.model.restOverTime?moment(this.model.restOverTime):null})
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            formData.restStartTime = formData.restStartTime?formData.restStartTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.restOverTime = formData.restOverTime?formData.restOverTime.format('YYYY-MM-DD HH:mm:ss'):null;
            
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>

<style lang="less" scoped>

</style>