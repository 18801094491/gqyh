import {httpAction} from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"
import {
    customerInformationUploadFile,
    getQueryCustomerNamesListData,
    getContractTypeList
} from '@api/measurementCenter-t/measurementBasedataInput.js'

export default {
    name: "ContractModal",
    data() {
        return {
            title: "操作",
            visible: false,
            model: {},
            labelCol: {
                xs: {span: 24},
                sm: {span: 5},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },

            confirmLoading: false,
            form: this.$form.createForm(this),
            validatorRules: {},
            dataSource: [],
            fileList: [],
            isShow: false,
            url: {
                add: "/settle/contract/add",
                edit: "/settle/contract/edit",
            },
            typeList: [],
            copyObj: {}
        }
    },
    created() {
    },
    mounted() {
        let data = {
            customerName: ''
        }
        getQueryCustomerNamesListData(data, this);
        getContractTypeList(this);
    },
    methods: {
        add() {
            this.edit();
        },
        edit(record, type) {
            console.log(type, 'type')
            if (type) {
                this.isShow = true;
            }
            this.visible = true;
            this.form.resetFields();
            this.fileList = [];
            this.model = Object.assign({}, record);
            this.copyObj = Object.assign({}, record);
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, 'constomerId', 'contractSn', 'contractName', 'fileUrl', 'contractUse', 'remarks', 'delFlag', 'type')
                )
                //时间格式化
                this.form.setFieldsValue({signDate: this.model.signDate ? moment(this.model.signDate) : null})
                this.form.setFieldsValue({startDate: this.model.startDate ? moment(this.model.startDate) : null})
                this.form.setFieldsValue({endDate: this.model.endDate ? moment(this.model.endDate) : null})

            });
            console.log(record, 'record')
            if (record && record.fileUrl) {
                this.$nextTick(() => {
                    this.fileList.push({
                        filePath: this.model.fileUrl,
                        fileName: this.model.contractName
                    })
                });
            }


        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.isShow = false;
        },
        reset() {

        },
        handleOk() {
            const that = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {

                    let httpurl = '';
                    let method = '';
                    if (!this.model.id) {
                        httpurl += this.url.add;
                        method = 'post';
                    } else {
                        httpurl += this.url.edit;
                        method = 'put';
                    }
                    let formData = Object.assign(this.model, values);
                    //时间格式化
                    let startDate = formData.startDate ? formData.startDate.format('YYYY-MM-DD') : ''
                    let endDate = formData.endDate ? formData.endDate.format('YYYY-MM-DD') : ''
                    formData.signDate = formData.signDate ? formData.signDate.format() : null;
                    formData.startDate = formData.startDate ? formData.startDate.format() : null;
                    formData.endDate = formData.endDate ? formData.endDate.format() : null;

                    if (!this.model.contractSn) {
                        this.$message.info('合同编号不能为空');
                        return;
                    }
                    if (!this.model.contractName) {
                        this.$message.info('合同名称不能为空');
                        return;
                    }
                    if (!this.model.signDate) {
                        this.$message.info('签约日期不能为空');
                        return;
                    }
                    if (!this.model.startDate) {
                        this.$message.info('生效日期不能为空');
                        return;
                    }

                    if (!this.model.endDate) {
                        this.$message.info('结束日期不能为空');
                        return;
                    }
                    if (!this.model.type) {
                        this.$message.info('分类不能为空');
                        return;
                    }
                    if (!this.model.customerId) {
                        this.$message.info('客户名称不能为空');
                        return;
                    }
                    if (that.fileList.length > 0) {
                        that.fileList.map(index => {
                            formData.fileUrl = index.filePath.substring(that.fileList[0].filePath.indexOf('/res'));
                        })
                    } else {
                        formData.fileUrl = '';
                    }
                    if (new Date(formData.endDate).getTime() < new Date(formData.startDate).getTime()) {
                        that.$message.info('结束日期不能小于生效日期');
                        return;
                    }
                    if (new Date(formData.endDate).getTime() < new Date(formData.signDate).getTime()) {
                        that.$message.info('结束日期不能小于签订日期');
                        return;
                    }
                    let dateWarnFlag = null
                    if (startDate !== this.copyObj.startDate || endDate !== this.copyObj.endDate) {
                        dateWarnFlag = 'date'
                    }
                    let customerFlag = null
                    if (this.model.customerId !== this.copyObj.customerId) {
                        customerFlag = 'cut'
                    }

                    if (dateWarnFlag && this.model.id) {
                        let msg = '修改合同起止日期或者客户后,原有水价规则将全部失效,并且需要重新绑定水表,'
                        this.$confirm({
                            title: msg + '确定要继续提交吗？',
                            content: '',
                            onOk() {
                                that.onSubmit(httpurl, formData, method)
                            },
                            onCancel() {
                                return
                            },
                        });
                    } else if (customerFlag && this.model.id) {
                        let msg = '合同客户发生变动，需重新绑定水表,'
                        this.$confirm({
                            title: msg + '确定要继续提交吗？',
                            content: '',
                            onOk() {
                                that.onSubmit(httpurl, formData, method)
                            },
                            onCancel() {
                                return
                            },
                        });
                    } else {
                        this.onSubmit(httpurl, formData, method)
                    }
                }
            })
        },
        onSubmit(httpurl, formData, method) {
            let that = this
            that.confirmLoading = true;
            httpAction(httpurl, formData, method).then((res) => {
                that.$confirm({
                    title: res.message,
                    content: '',
                    onOk() {

                    },
                    onCancel() {

                    },
                });
                if (res.success) {

                    that.$emit('ok');
                } else {
                }
            }).finally(() => {
                that.confirmLoading = false;
                that.close();
            })
        },
        handleCancel() {
            this.close()
        },
        //新增上传文件
        upfileClick(e) {
            let file = e.target.files[0];
            if (this.fileList.length >= 1) {
                this.$message.error('合同文件只能传一个!');
                $('#uploadBtn').val('');
                return;
            }
            ;
            let param = new FormData();
            param.append('file', file);
            customerInformationUploadFile(param, this);
        },
        //删除上传文件
        removeFile() {
            this.fileList = [];
            $('#uploadBtn').val('');
        },
        handleChange(value) {
        }
    }
}
//contractManage/ContractModal.vue 页面组件混入了 contractManage/modal.js