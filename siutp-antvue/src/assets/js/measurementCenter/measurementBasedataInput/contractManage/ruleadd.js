import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"
import {
    getRuleTypeList,
    getPricingRuleDetailById
} from '@api/measurementCenter-t/measurementBasedataInput.js'
export default {
    name: "PricingRulesAddModal",
    data() {
        return {
            title: "新增计价规则",
            selectRow: {},
            isShow: false,
            visible: false,
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
            model: {},
            ruleTypeList: [],
            recordFormItem: [0]
        }
    },
    mounted() {
        getRuleTypeList(this);
    },
    methods: {
        closeModal() {
            this.visible = false
        },
        openModal(selectRow, record) {
            this.selectRow = selectRow
            if (record.id) {
                getPricingRuleDetailById(this, record)
            } else {
                this.visible = true
                this.form.resetFields();
                this.model = Object.assign({}, {});
                this.model.ruleType = '1'
                this.recordFormItem = [0]
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'ruleType', 'ruleName', 'benchPrice', 'setUp', 'setTime'))
                    this.form.ruleType = '1'
                });
            }
        },
        setInitItems() {
            if (this.model.items && this.model.items.length) {
                this.recordFormItem = []
                this.model.items.map((one, i) => {
                    this.recordFormItem.push(i)
                    this.form['items_startTime_' + i] = one.startTime ? moment(one.startTime) : null
                    this.form['items_endTime_' + i] = one.endTime ? moment(one.endTime) : null
                    this.$nextTick(() => {
                        let obj = {}
                        obj['items_price_' + i] = one.price
                        this.form.setFieldsValue(obj)
                    })
                })
            } else {
                this.recordFormItem = [0]
                this.form['items_startTime_0'] = this.selectRow.startDate ? moment(this.selectRow.startDate) : null
                this.form['items_endTime_' + (this.recordFormItem.length - 1)] = this.selectRow.endDate ? moment(this.selectRow.endDate) : null
            }
            this.form = Object.assign({}, this.form);
        },
        changeRuleType(e) {
            this.form.ruleType = e.target.value
            this.form['items_startTime_0'] = this.selectRow.startDate ? moment(this.selectRow.startDate) : null
            this.form['items_endTime_' + (this.recordFormItem.length - 1)] = this.selectRow.endDate ? moment(this.selectRow.endDate) : null
        },
        addItem() {
            this.form['items_endTime_' + (this.recordFormItem.length - 1)] = null
            this.recordFormItem.push(this.recordFormItem.length)
            this.form['items_startTime_' + (this.recordFormItem.length - 1)] = null
            this.form['items_endTime_' + (this.recordFormItem.length - 1)] = this.selectRow.endDate ? moment(this.selectRow.endDate) : null
        },
        minusItem(i) {
            //  当删除的是最后一项
            if (i === this.recordFormItem.length - 1) {
                return
            }
            // 删除-把所有的值往上提
            for (let j = 0; j < this.recordFormItem.length; j++) {
                if (j >= i) {
                    //要把下一项 赋值到当前要删除的项
                    // 删除的开始日期是上一项的结束日期的第二天
                    let sDate = new Date(this.form['items_endTime_' + (j - 1)])
                    sDate.setTime(sDate.getTime() + 24 * 60 * 60 * 1000);
                    this.form['items_startTime_' + j] = this.form['items_endTime_' + (j - 1)] ? moment(sDate) : null
                    // 删除的结束日期是我下一项的结束日期
                    this.form['items_endTime_' + j] = this.form['items_endTime_' + (j + 1)]
                    // 将下一级的开始时间和结束时间置空
                    this.form['items_startTime_' + (j + 1)] = null
                    this.form['items_endTime_' + (j + 1)] = null
                    this.$nextTick(() => {
                        let obj = {}
                        obj['items_price_' + j] = this.form.getFieldValue('items_price_' + (j + 1))
                        this.form.setFieldsValue(obj)
                    })
                }
            }
            // 当删除的是第一项
            if (i === 0) {
                this.form['items_startTime_0'] = this.selectRow.startDate ? moment(this.selectRow.startDate) : null
            }
            this.form = Object.assign({}, this.form);
            this.recordFormItem.splice(i, 1)
        },
        endDateChange(val, i) {
            if (new Date(this.form['items_endTime_' + i]).getTime() < new Date(this.form['items_startTime_' + i]).getTime()) {
                this.$message.info('结束时间不能小于开始时间');
                this.form['items_endTime_' + i] = null
            } else {
                this.form['items_endTime_' + i] = moment(val)
                if (i < this.recordFormItem.length - 1) {
                    let endDate = new Date(this.form['items_endTime_' + i])
                    endDate.setTime(endDate.getTime() + 24 * 60 * 60 * 1000);
                    this.form['items_startTime_' + (i + 1)] = moment(endDate)
                }
            }
            this.form = Object.assign({}, this.form);
        },
        submit() {
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    let httpurl = '/settle/contractWaterRule/one';
                    let method = 'post';
                    let formData = Object.assign(this.model, values);
                    let params = {}
                    if (!this.model.ruleName) {
                        this.$message.info('规则名称不能为空');
                        return;
                    }
                    if (formData.ruleType === '1') {
                        if (!this.model.benchPrice) {
                            this.$message.info('基础价格不能为空');
                            return;
                        }
                        if (!this.model.setUp) {
                            this.$message.info('增幅不能为空');
                            return;
                        }
                        if (!this.model.setTime) {
                            this.$message.info('阶梯时间不能为空');
                            return;
                        }
                        let regstr = /^(?:1[0-2]|[1-9])$/;
                        if (!regstr.test(this.model.setTime)) {
                            this.$message.info('阶梯时间只能输入1-12月份的正整数');
                            return;
                        }
                        params = {

                            benchPrice: formData.benchPrice,
                            setUp: formData.setUp,
                            setTime: formData.setTime
                        }
                    } else {
                        // 转参数 + 错误信息提示
                        let errPriceMsg = []
                        let errDateMsg = []
                        let items = []
                        this.recordFormItem.map((one, i) => {
                            if (!values['items_price_' + i]) {
                                errPriceMsg.push('第' + (i + 1) + '行')
                            }
                            if (!this.form['items_endTime_' + i]) {
                                errDateMsg.push('第' + (i + 1) + '行')
                            }
                            items.push({
                                price: values['items_price_' + i],
                                startTime: this.form['items_startTime_' + i] ? this.form['items_startTime_' + i].format() : null,
                                endTime: this.form['items_endTime_' + i] ? this.form['items_endTime_' + i].format() : null,
                            })
                        })
                        // 水价
                        if (errPriceMsg.length) {
                            this.$message.info(errPriceMsg.join(',') + '水价不能为空');
                            return
                        }
                        // 结束时间
                        if (errDateMsg.length) {
                            this.$message.info(errDateMsg.join(',') + '结束时间不能为空');
                            return
                        }
                        params.items = items
                    }
                    params.ruleType = formData.ruleType
                    params.ruleName = formData.ruleName
                    params.contractId = this.selectRow.id
                    if (this.model.id) {
                        params.id = this.model.id
                    }
                    this.confirmLoading = true;
                    httpAction(httpurl, params, method).then((res) => {
                        if (res.success) {
                            this.$message.success(res.message);
                            this.$emit('submitSuccess', this.selectRow);
                        } else {
                            this.$message.warning(res.message);
                        }
                    }).finally(() => {
                        this.confirmLoading = false;
                        this.closeModal();
                    })
                }
            })
        }
    }
}
//contractManage/PricingRulesAddModal.vue 页面组件混入了 contractManage/ruleadd.js