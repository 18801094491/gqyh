import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"

export default {
    name: "ContractModal",
    data () {
        return {
            title:"操作",
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
            validatorRules:{
            },
            url: {
                add: "/settle/contract/add",
                edit: "/settle/contract/edit",
            },
        }
    },
    created () {
    },
    methods: {
        add () {
            this.edit({});
        },
        edit (record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model,'constomerId','contractSn','contractName','fileUrl','contractUse','remarks','delFlag'))
                //时间格式化
                this.form.setFieldsValue({signDate:this.model.signDate?moment(this.model.signDate):null})
                this.form.setFieldsValue({startDate:this.model.startDate?moment(this.model.startDate):null})
                this.form.setFieldsValue({endDate:this.model.endDate?moment(this.model.endDate):null})
            });

        },
        close () {
            this.$emit('close');
            this.visible = false;
        },
        handleOk () {
            const that = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    that.confirmLoading = true;
                    let httpurl = '';
                    let method = '';
                    if(!this.model.id){
                        httpurl+=this.url.add;
                        method = 'post';
                    }else{
                        httpurl+=this.url.edit;
                        method = 'put';
                    }
                    let formData = Object.assign(this.model, values);
                    //时间格式化
                    formData.signDate = formData.signDate?formData.signDate.format():null;
                    formData.startDate = formData.startDate?formData.startDate.format():null;
                    formData.endDate = formData.endDate?formData.endDate.format():null;

                    console.log(formData)
                    httpAction(httpurl,formData,method).then((res)=>{
                        if(res.success){
                            that.$message.success(res.message);
                            that.$emit('ok');
                        }else{
                            that.$message.warning(res.message);
                        }
                    }).finally(() => {
                        that.confirmLoading = false;
                        that.close();
                    })



                }
            })
        },
        handleCancel () {
            this.close()
        },


    }
}