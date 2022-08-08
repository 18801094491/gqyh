import { httpAction, getAction } from "@/api/manage";

export default {
    name: "ContractModal",
    props: ['selectRow'],
    data() {
        return {
            title: '添加数据项',
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
                add: "/work/workJobDatasource/one"
            },
            dataTypeList: [
                // 数据类型下拉
                {
                    code: "0",
                    title: "文本"
                },
                {
                    code: "1",
                    title: "多行文本"
                },
                {
                    code: "2",
                    title: "多选"
                },
                {
                    code: "3",
                    title: "单选"
                },
                {
                    code: "4",
                    title: "日期"
                }
            ]
        }
    },
    methods: {
        loadTree() {
            let that = this;
            let url = `/work/workJobDatasource/${this.selectRow.id}`;
            getAction(url, {}).then(res => {
                if (res.code === 200) {
                    that.treeData = [];
                    let treeList = res.result;
                    for (let a = 0; a < treeList.length; a++) {
                        let temp = treeList[a];
                        temp.isLeaf = temp.isLeaf !== 0;
                        that.treeData.push(temp);
                    }
                }
            });
        },
        add (record, type = 'add') {
            this.loadTree(); // 获取上级数据项
            if (type === 'add') {
                record = {
                    parentId: undefined
                }
            }
            this.edit(record, type);
        },
        edit (record, type) {
            if (type !== 'add' && type !== 'addSub') {
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
        addItem() {
            this.recordFormItem.push({
                itemName: '',
                itemOrder: ''
            })
        },
        minusItem(i) {
            this.recordFormItem.splice(i, 1)
        },
        handleParentIdChange (val) {
            this.$set(this.model, 'parentId', val)
            // this.model.parentId = val
        },
        changeDataType (val) {
            if (val === '2' || val === '3') {
                this.recordFormItem = [{
                    itemName: '',
                    itemOrder: ''
                }]
            } else {
                this.recordFormItem = []
            }
        },
        changeDataCategory (val) {
            this.model.parentId = undefined
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