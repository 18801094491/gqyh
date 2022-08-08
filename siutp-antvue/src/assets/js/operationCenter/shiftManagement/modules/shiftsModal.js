import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import { URL } from '@/api/operationCenter-t/shiftManagement.js'

export default {
  name: "ShiftsModal",
  data() {
    return {
      title: "操作",
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
      validatorRules: {
      },
      dateFormat: 'HH:mm',
      isShow: false,
      url: URL,
    }
  },
  created() {
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record) {
      console.log(record)
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'shiftsName', 'shiftsDescribe', 'shiftsStatus', 'workHours'))

      });

    },
    details(record) {
      this.isShow = true;
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'shiftsName', 'shiftsDescribe', 'shiftsStatus', 'workHours'))

      });

    },
    close() {
      this.$emit('close');
      this.visible = false;
      this.isShow = false;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          if (!values.shiftsName) {
            this.$message.info('班次名称不能为空!');
            return;
          }
          if (!values.startTime) {
            this.$message.info('上班时间不能为空!');
            return;
          }
          if (!values.overTime) {
            this.$message.info('下班时间不能为空!');
            return;
          }
          if (!values.shiftsStatus) {
            this.$message.info('班次状态不能为空!');
            return;
          }
          if (!values.workHours) {
            this.$message.info('上班时长不能为空!');
            return;
          }
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'post';
          }
          let formData = Object.assign(this.model, values);
          //时间格式化
          formData.restStartTime = formData.restStartTime ? formData.restStartTime.format('HH:mm') : null;
          formData.restOverTime = formData.restOverTime ? formData.restOverTime.format('HH:mm') : null;
          formData.startTime = formData.startTime ? formData.startTime.format('HH:mm') : null;
          formData.overTime = formData.overTime ? formData.overTime.format('HH:mm') : null;
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
            that.close();
          })



        }
      })
    },
    handleCancel() {
      this.close()
    },


  }
}
  //operationCenter/shiftManagement/shiftsModal.vue 页面组件混入了operationCenter/shiftManagement/shiftsModal.js