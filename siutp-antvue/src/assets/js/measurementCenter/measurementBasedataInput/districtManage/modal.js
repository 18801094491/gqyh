
import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import JTreeRegionSelect from '@/components/jeecg/JTreeRegionSelect'

export default {
    name: "regionListModal",
    components: {
        JTreeRegionSelect
    },
    data() {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
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
            validatorRules: {
                code: {
                    rules: [{
                        required: true, message: '请输入类型编码!'
                    }, {
                        validator: this.validateMyCode
                    }]
                },
                parentId: {},
                districtName: {}
            },
            url: {
                add: "/settle/district/add", // 新增节点
                edit: "/settle/district/edit",  // 编辑节点
                checkCode: "/sys/category/checkCode"
            },
            expandedRowKeys: [],
            pidField: "parent_id", //parentId
            disTree: false,
        }
    },
    created() {
    },
    methods: {
        add() {
            this.edit({});
        },
        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, 'parentId', 'districtName', 'id'))
                let pid = this.form.getFieldsValue().parentId
                if (this.title == '编辑') {

                    this.disTree = true;
                } else {
                    this.disTree = false;
                }
            })
        },
        close() {
            this.$emit('close');
            this.visible = false;
        },
        handleOk() {
            const that = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
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
                    //          console.log("表单提交数据",formData)
                    httpAction(httpurl, formData, method).then((res) => {
                        if (res.success) {
                            that.$message.success(res.message);
                            that.submitSuccess(formData)
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
        popupCallback(row) {
            this.form.setFieldsValue(pick(row, 'pid', 'name', 'code'))
        },
        submitSuccess(formData) {
            if (!formData.id) {
                let treeData = this.$refs.treeSelect.getCurrTreeData()
                this.expandedRowKeys = []
                this.getExpandKeysByPid(formData[this.pidField], treeData, treeData)
                this.$emit('ok', formData, this.expandedRowKeys.reverse());
            } else {
                this.$emit('ok', formData);
            }
        },
        getExpandKeysByPid(pid, arr, all) {
            if (pid && arr && arr.length > 0) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].key == pid) {
                        this.expandedRowKeys.push(arr[i].key)
                        this.getExpandKeysByPid(arr[i]['parentId'], all, all)
                    } else {
                        this.getExpandKeysByPid(pid, arr[i].children, all)
                    }
                }
            }
        },
        validateMyCode(rule, value, callback) {
            let params = {
                pid: this.form.getFieldValue('pid'),
                code: value
            }
            getAction(this.url.checkCode, params).then((res) => {
                if (res.success) {
                    callback()
                } else {
                    callback(res.message)
                }
            })
        },


    }
}