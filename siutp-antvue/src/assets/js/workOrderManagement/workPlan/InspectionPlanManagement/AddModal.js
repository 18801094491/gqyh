import { httpAction } from "@/api/manage";

export default {
    name: "addModal",
    data() {
        return {
            title: '添加巡检计划',
            tplList: [],
            lineList: [],
            recordFormItem: [],
            visible: false,
            treeData: [],
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
            isShow: false,
            url: {
                add: "/work/workInspectionPoint/one"
            },
            childList: []
        }
    },
    methods: {
        add (record, type = 'add') {
            this.edit(record, type);
        },
        edit (record, type) {
            if (type !== 'add') {
                if(type === 'detail') {
                    this.title = '查看巡检点'
                } else {
                    this.title = '编辑巡检点'
                }
                let info = {}
                info.id = record.id
                info.dataCategory = record.dataCategory
                info.dataName = record.dataName
                info.parentId = record.parentId
                info.dataUnit = record.dataUnit
                info.dataOrder = record.dataOrder
                info.dataType = record.dataType
                info.needEnter = record.needEnter === '0' ? false : true
                info.needRequired = record.needRequired === '0' ? false : true
                info.dataName = record.title
                this.recordFormItem = record.items
                this.model = Object.assign({}, info);
            } else {
                this.recordFormItem = []
                this.model = Object.assign({}, record);
            }
            this.form.resetFields();
            this.visible = true;
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.isShow = false;
        },
        handleOk() {
            console.log(this.model)
            // 触发表单验证
            if (typeof this.model.dataCategory === 'undefined') {
                this.$message.info('数据类型不能为空');
                return;
            }
            if (!this.model.dataName) {
                this.$message.info('数据项名称不能为空');
                return;
            }
            if (this.model.dataCategory * 1 === 1 && !this.model.parentId) {
                this.$message.info('上级数据项不能为空');
                return;
            }

            let formData ={...this.model}
            formData.tplId = this.selectRow.id
            formData.needEnter = this.model.needEnter ? 1 : 0
            formData.needRequired = this.model.needRequired ? 1 : 0
            formData.items = this.recordFormItem
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
                    this.$emit("submitSuccess", this.selectRow);
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