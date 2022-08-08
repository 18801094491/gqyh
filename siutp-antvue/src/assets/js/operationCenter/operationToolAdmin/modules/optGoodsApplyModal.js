import { URL } from '@/api/operationCenter/operationToolAdmin/modules/optGoodsApplyModal.js'
import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"

export default {
  name: "optGoodsApplyModal",
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
        this.form.setFieldsValue(pick(this.model,'applySn','dataId','dataType','amount','emergentLevel','verifyStatus','applyDesc','delFlag'))
    //时间格式化
        this.form.setFieldsValue({wantedTime:this.model.wantedTime?moment(this.model.wantedTime):null})
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
          formData.wantedTime = formData.wantedTime?formData.wantedTime.format('YYYY-MM-DD HH:mm:ss'):null;
          
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