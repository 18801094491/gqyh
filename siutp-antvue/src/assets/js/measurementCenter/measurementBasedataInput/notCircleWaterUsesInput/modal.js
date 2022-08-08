import pick from 'lodash.pick'
import moment from "moment"
import { settleManualContracts, httpAction } from '@api/measurementCenter-t/notCircleWaterUsesInput.js'

export default {
    name: "ContractModal",
    props: ['waterNumberList'],
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
                add: "/settle/manual/add",
                edit: "/settle/manual/edit",
            },
            isWaterAndTime: true,
            contractInfos: []
        }
    },
    methods: {
        handleEquipment(val) {
            if (val) {
                this.model.equipmentId = val;
                this.handleChange();
            }
        },
        handleFlowDate(val) {
            if (val) {
                this.model.currentFlowTime = val.format('YYYY-MM-DD');
                this.handleChange();
            }
        },
        handleChange() {
            if (this.model.equipmentId && this.model.currentFlowTime) {
                let param = {
                    equipmentId: this.model.equipmentId,
                    date: moment(this.model.currentFlowTime).format('YYYY-MM-DD')
                }
                settleManualContracts(this, param);
            }
        },
        add() {
            this.edit();
        },
        edit(record, type) {
            if (type) {
                this.isShow = true;
            }
            this.visible = true;
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, 'equipmentId', 'currentFlowTime', 'currentFlow'))
                //时间格式化
                this.form.setFieldsValue({ currentFlowTime: this.model.currentFlowTime ? moment(this.model.currentFlowTime) : null })
            });
            this.handleChange();
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.isShow = false;
        },
        handleOk() {
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {

                    let formData = Object.assign(this.model, values);
                    //时间格式化
                    formData.currentFlowTime = formData.currentFlowTime ? formData.currentFlowTime.format('YYYY-MM-DD') : null;

                    if (!this.model.equipmentId) {
                        this.$message.info('水表编号不能为空');
                        return;
                    }
                    if (!this.model.currentFlowTime) {
                        this.$message.info('抄表日期不能为空');
                        return;
                    }
                    if (!this.model.currentFlow) {
                        this.$message.info('表底数不能为空');
                        return;
                    }
                    if (!this.model.ruleItemId) {
                        this.$message.info('合同不能为空');
                        return;
                    }
                    let httpurl = '';
                    let method = '';
                    if (!this.model.id) {
                        httpurl += this.url.add;
                        method = 'post';
                    } else {
                        httpurl += this.url.edit + '/' + this.model.id;
                        method = 'post';
                        formData = {
                            id: formData.id,
                            currentFlowTime: formData.currentFlowTime,
                            currentFlow: formData.currentFlow,
                            ruleItemId: formData.ruleItemId,
                        }
                    }
                    this.onSubmit(httpurl, formData, method)
                }
            })
        },
        onSubmit(httpurl, formData, method) {
            let that = this
            that.confirmLoading = true;
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
        },
        handleCancel() {
            this.contractInfos = [];
            this.close()
        }
    }
}