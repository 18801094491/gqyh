import { httpAction } from '@/api/manage'
import moment from "moment"
import {
  getEquipmentCategoryData,
  getBidSegmentData,
  getAssetStatus,
  getAgencyStatus,
  getUserData,
  equiImgUpload
} from '@/api/equipmentAccount-t/AssetInformationAddModal.js'

export default {
  name: "ContractModal",
  data() {
    return {
      equipmentCategoryList: [],
      bidSegmentList: [],
      assetStatusList: [],
      workingStatusList: [],
      getAgencyStatus: [],
      stateList: [
        {
          height: '',
          imageType: '',
          imgUrl: '',
          width: ''
        }
      ],
      title: "新增资产信息",
      visible: false,
      model: {
        ruleItemId: "",
      },
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
      validatorRules: {},
      isShow: false,
      url: {
        add: "/equipment/optEquipment/add",
        edit: "/equipment/optEquipment/edit"
      },
      managerList: [], // 用户下拉
    }
  },
  created() {
    //获取设备类别值
    getEquipmentCategoryData(this);
    //获取所属标段值
    getBidSegmentData(this);
    //获取资产状态
    getAssetStatus(this);
    //设备台账获取设备状态列表值
    getAgencyStatus(this);
    getUserData({}, this); // 获取用户下拉
  },
  methods: {
    //新增修改图片上传
    upfileClick(e, index) {
      console.log(33)
      let file = e.target.files[0];
      let param = new FormData();
      param.append('file', file);
      console.log(param)
      $('.uploadBtnB').val('');
      equiImgUpload(param, this, 0);
    },
    //新增修改图片删除
    fileImgRemove(index) {
      console.log(this.stateList)
      this.stateList[index].imgUrl = '';
    },
    /**
     * 编辑
     */
    edit(record, type) {
      if (type === 'add') {
        delete record.id
      }
      if (type === 'detail') {
        this.isShow = true;
      }
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
    },
    close() {
      this.$emit('close');
      this.visible = false;
      this.isShow = false;
    },
    handleOk() {
      // 触发表单验证
      if (!this.model.equipmentSn) {
        this.$message.info('设备编号不能为空');
        return;
      }
      if (!this.model.equipmentName) {
        this.$message.info('设备名称不能为空');
        return;
      }
      if (!this.model.equipmentCategory) {
        this.$message.info('资产类别不能为空');
        return;
      }
      if (!this.model.equipmentLocation) {
        this.$message.info('放置位置不能为空');
        return;
      }
      if (!this.model.equipmentSection) {
        this.$message.info('所属标段不能为空');
        return;
      }
      if (!this.model.equipmentStatus) {
        this.$message.info('资产状态不能为空');
        return;
      }
      if (!this.model.equipmentPurchase) {
        this.$message.info('购置时间不能为空');
        return;
      }
      if (!this.model.equipmentOperating) {
        this.$message.info('投入运营时间不能为空');
        return;
      }
      if (!this.model.equipmentRevstop) {
        this.$message.info('设备状态不能为空');
        return;
      }
      let formData = { ...this.model }
      formData.equipmentPurchase = formData.equipmentPurchase ? moment(formData.equipmentPurchase).format() : undefined,
        formData.equipmentOperating = formData.equipmentOperating ? moment(formData.equipmentOperating).format() : undefined,
        formData.equipmentImg = this.stateList[0].imgUrl //设备图片
      formData.equipmentMode = formData.equipmentModel
      formData.personResponsible = formData.personResponsible ? formData.personResponsible.join(',') : ''

      let httpurl = '';
      let method = '';
      if (!this.model.id) {
        httpurl += this.url.add;
        method = 'post';
      } else {
        httpurl += this.url.edit;
        method = 'put';
      }
      this.onSubmit(httpurl, formData, method)
    },
    /**
     * 提交
     */
    onSubmit(httpurl, formData, method) {
      let that = this
      that.confirmLoading = true;
      httpAction(httpurl, formData, method).then((res) => {
        if (res.success) {
          that.$message.success(res.message);
          that.confirmLoading = false;
          that.close();
          that.$emit('assetOk');
        } else {
          that.$message.warning(res.message);
        }
      }).finally(() => {
        // that.confirmLoading = false;
        // that.close();
      })
    },
    handleCancel() {
      this.close()
    }
  }
}
    //components/AssetInformationAddModal.vue 页面组件混入的components/AssetInformationAddModal.js