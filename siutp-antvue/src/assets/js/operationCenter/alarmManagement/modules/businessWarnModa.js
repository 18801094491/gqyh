import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"
import { URL } from '@/api/operationCenter-t/alarmManagement.js'

export default {
  name: "BusinessWarnModal",
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
      url: URL,
    }
  },
  created () {
  },
  methods: {
    // 弹框新增
    add () {
      this.edit({});
    },
    // 编辑
    edit (record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model,'warnSn','warnName','warnLevel','warnType','warnStatus','warnWay','duration','operator','delFlag'))
        //时间格式化
        this.form.setFieldsValue({warnTime:this.model.warnTime?moment(this.model.warnTime):null})
        this.form.setFieldsValue({operateTime:this.model.operateTime?moment(this.model.operateTime):null})
      });

    },
    // 关闭
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
          formData.warnTime = formData.warnTime?formData.warnTime.format('YYYY-MM-DD HH:mm:ss'):null;
          formData.operateTime = formData.operateTime?formData.operateTime.format('YYYY-MM-DD HH:mm:ss'):null;
          
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