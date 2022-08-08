import { httpAction } from "@/api/manage";

export default {
    name: "ContractModal",
    props: ["jobTypeList", "jobStatusList"],
    data() {
        return {
            title: "操作",
            visible: false,
            model: {
                ruleItemId: ""
            },
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            confirmLoading: false,
            form: this.$form.createForm(this),
            validatorRules: {},
            isShow: false,
            url: {
                add: "/work/template/one"
            }
        };
    },
    methods: {
        changeSpecs(val) {
            this.model = Object.assign({}, this.model);
        },
        add() {
            this.edit();
        },
        edit(record, type) {
            if (type == "detail") {
                this.isShow = true;
            }
            if (record) {
                this.form.resetFields();
                this.model = Object.assign({}, record);
                this.visible = true;
            } else {
                this.form.resetFields();
                this.model = Object.assign({}, record);
                this.visible = true;
            }
        },
        close() {
            this.$emit("close");
            this.visible = false;
            this.isShow = false;
        },
        handleOk() {
            // 触发表单验证
            if (!this.model.tplName) {
                this.$message.info("模板名称不能为空");
                return;
            }
            if (this.model.baseId) {
                this.model.id = this.model.baseId;
                delete this.model.baseId;
            }
            let formData = {
                id: this.model.id,
                remark: this.model.remark,
                tplName: this.model.tplName,
                tplStatus: this.model.tplStatus,
                tplType: this.model.tplType
            };
            this.onSubmit(this.url.add, formData, "post");
        },
        onSubmit(httpurl, formData, method) {
            let that = this;
            that.confirmLoading = true;
            httpAction(httpurl, formData, method)
                .then(res => {
                    if (res.success) {
                        that.$message.success(res.message);
                        that.confirmLoading = false;
                        that.close();
                        that.$emit("ok");
                    } else {
                        that.$message.warning(res.message);
                    }
                })
                .finally(() => {
                });
        },
        handleCancel() {
            this.close();
        }
    }
};