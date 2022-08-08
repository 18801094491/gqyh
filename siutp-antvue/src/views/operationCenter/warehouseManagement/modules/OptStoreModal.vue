<!-- 运营中心-仓库管理-新增 -->
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
          label="仓库名称">
          <a-input placeholder="请输入仓库名称" v-decorator="['storeName', {}]" />
        </a-form-item>
        
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="仓库地址">
          <a-input placeholder="请输入仓库地址" v-decorator="['storePosition', {}]" />
        </a-form-item>
        
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="启停状态">
          <a-select v-decorator="['storeStatus', {}]" placeholder="请选择启停状态">
            <a-select-option value="0">停用</a-select-option>
            <a-select-option value="1">启用</a-select-option>
          </a-select>
          
        </a-form-item>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"
  import {
    URL1,
    getStoreUserListData,
    getUserData
  } from '@/api/operationCenter-t/warehouseManagement.js'
  export default {
    name: "OptStoreModal",
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
        url: URL1,
        
        storeStatusList:[],

      }
    },
    created () {
    },
    mounted(){
      let data={
        name:''
      }
      getUserData(data,this)
    },
    methods: {
      add () {
        this.edit();
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);

        this.visible = true;
        this.$nextTick(() => {
          if(record){
            this.model.manager=this.model.managerCode;
            this.model.storeStatus=this.model.storeStatusCode;
            console.log(this.model)
          }
          
          this.form.setFieldsValue(pick(this.model,'storeName','storeSn','storePosition','delFlag','managerCode','storeStatus'));
          
          
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
            if(!values.storeName){
              this.$message.info('仓库名称不能为空!');
              return;
            }
            if(!values.storePosition){
              this.$message.info('仓库地址不能为空!');
              return;
            }
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'post';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化
            
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
      
      storeStatushandleChange(data){

      },

    }
  }
</script>

<style lang="less" scoped>

</style>