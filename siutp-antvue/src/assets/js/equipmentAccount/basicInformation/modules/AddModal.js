import { httpAction } from '@/api/manage'
// import pick from 'lodash.pick'
// import moment from "moment"
// import {settleManualContracts} from '@/api/equipmentAccount/basicInformation/modules/AddModal.js';
export default {
    name: "ContractModal",
    props: ['supplierClassificationList', 'equipmentTypeList', 'add_equipmentModelList', 'add_equipmentSpecsList'],
    data() {
        return {
            title: "操作",
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
                add: "/equipment/baseInfo/one"
            }
        }
    },
    methods: {
        /**
         * 级联
         */
        changeType(val) {
            this.model.equipmentModel = undefined
            this.model.equipmentSpecs = undefined
            this.$emit('changeType', val, 'add_')
        },
        changeMondel(val) {
            this.model.equipmentSpecs = undefined
            this.$emit('changeMondel', val, 'add_')
        },
        changeSpecs(val) {
            this.model = Object.assign({}, this.model)
        },
        add() {
            this.edit();
        },
        edit(record, type) {
            if (type) {
                this.isShow = true;
            }
            if (record) {
                this.$emit('changeType', record.equipmentType, 'add_')
                this.$emit('changeMondel', record.equipmentModel, 'add_')
                setTimeout(() => {
                    this.form.resetFields();
                    this.model = Object.assign({}, record);
                    this.visible = true;
                }, 1000)
            } else {
                this.form.resetFields();
                this.model = Object.assign({}, record);
                this.visible = true;
            }
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.isShow = false;
        },
        handleOk() {
            // 触发表单验证
            if (!this.model.baseName) {
                this.$message.info('资产名称不能为空');
                return;
            }
            if (!this.model.equipmentSupplier) {
                this.$message.info('供应商不能为空');
                return;
            }
            if (!this.model.equipmentType) {
                this.$message.info('设备类型不能为空');
                return;
            }
            if (!this.model.equipmentModel) {
                this.$message.info('设备型号不能为空');
                return;
            }
            if (!this.model.equipmentSpecs) {
                this.$message.info('设备规格不能为空');
                return;
            }
            if (this.model.baseId) {
                this.model.id = this.model.baseId;
                delete this.model.baseId

            }
            let formData = { ...this.model }
            this.onSubmit(this.url.add, formData, 'post')
        },
        onSubmit(httpurl, formData, method) {
            let that = this
            that.confirmLoading = true;
            httpAction(httpurl, formData, method).then((res) => {
                if (res.success) {
                    that.$message.success(res.message);
                    that.confirmLoading = false;
                    that.close();
                    that.$emit('ok');
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
    //basicInformation/AddModal 页面组件混入的basicInformation/AddModal .js