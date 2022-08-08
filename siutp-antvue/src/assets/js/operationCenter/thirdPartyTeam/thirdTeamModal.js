import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"
import {
  URL1,
  customerInformationUploadFile,
  getThirdPartyTeamContactTitle,
  getThirdPartyTeamRating
} from '@/api/operationCenter-t/thirdPartyTeam.js'
export default {
  name: "ThirdTeamModal",
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
      url: URL1,
      fileList: [],
      contactPositionList: [],
      teamRatingList: [],
      isShow: true
    }
  },
  created() {
  },
  mounted() {
    getThirdPartyTeamContactTitle(this);
    getThirdPartyTeamRating(this);
  },
  methods: {
    add() {
      this.edit({});
    },
    edit(record, type) {
      console.log(record);
      if (type) {
        this.isShow = false;
      }
      console.log(this.isShow);
      this.fileList = [];
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'teamSn', 'teamName', 'contactName', 'contactPhone', 'contactPosition', 'teamRating', 'contactPositionCode', 'teamRatingCode', 'fileUrl', 'fileName', 'delFlag'))
        //时间格式化
        this.form.setFieldsValue({ agreeStart: this.model.agreeStart ? moment(this.model.agreeStart) : null })
        this.form.setFieldsValue({ agreeEnd: this.model.agreeEnd ? moment(this.model.agreeEnd) : null })

        if (this.model.fileUrl) {
          this.fileList.push({
            fileName: this.model.fileName,
            filePath: this.model.fileUrl
          })
        }
      });

    },
    close() {
      this.$emit('close');
      this.visible = false;
      this.isShow = true;
    },
    handleOk() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log(values)
          if (!values.teamName) {
            this.$message.info('团队名称不能为空！');
            return;
          }
          if (!values.teamRating) {
            this.$message.info('团队评级不能为空！');
            return;
          }

          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'post';
          }
          var reg = new RegExp("[\u4E00-\u9FA5]+");

          if (reg.test(values.contactPosition)) {

            values.contactPosition = that.model.contactPositionCode;

          }
          if (reg.test(values.teamRating)) {

            values.teamRating = that.model.teamRatingCode;
          }
          let formData = Object.assign(this.model, values);
          //时间格式化
          formData.agreeStart = formData.agreeStart ? formData.agreeStart.format() : null;
          formData.agreeEnd = formData.agreeEnd ? formData.agreeEnd.format() : null;
          if (this.fileList.length) {

            formData.fileName = this.fileList[0].fileName;
            formData.fileUrl = this.fileList[0].filePath.substring(this.fileList[0].filePath.indexOf('/res'));
          } else {
            formData.fileName = '';
            formData.fileUrl = '';
          }
          if (new Date(formData.agreeStart).getTime() > new Date(formData.agreeEnd).getTime()) {
            this.$message.info('协议起始日期不能大于协议截至日期！');
            return;
          }
          that.confirmLoading = true;
          console.log(formData)
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
    //新增上传文件
    upfileClick(e) {
      let file = e.target.files[0];
      if (file.type != 'application/pdf') {
        this.$message.error('请上传PDF格式文件!');
        $('#uploadBtn').val('');
        return;
      };
      if (this.fileList.length >= 1) {
        this.$message.error('合同文件只能传一个!');
        $('#uploadBtn').val('');
        return;
      };
      let param = new FormData();
      param.append('file', file);
      console.log(param)
      customerInformationUploadFile(param, this);
    },
    //删除上传文件
    removeFile() {
      this.fileList = [];
      $('#uploadBtn').val('');
    },

  }
}
 //operationCenter/thirdPartyTeam/thirdTeamModal.vue 
 //页面组件混入了operationCenter/hirdPartyTeam/thirdTeamModal.js