import { URL } from '@/api/operationCenter/operationToolAdmin/modules/optToolBorrowModal.js'
import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"

export default {
  name: "optToolBorrowModal",
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
    add () {
      this.edit({});
    },
    edit (record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model,'toolId','applySn','backStatus','userId','description'))
        //时间格式化
        this.form.setFieldsValue({borrowTime:this.model.borrowTime?moment(this.model.borrowTime):null})
        this.form.setFieldsValue({backTime:this.model.backTime?moment(this.model.backTime):null})
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
          formData.borrowTime = formData.borrowTime?formData.borrowTime.format('YYYY-MM-DD HH:mm:ss'):null;
          formData.backTime = formData.backTime?formData.backTime.format('YYYY-MM-DD HH:mm:ss'):null;
          
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