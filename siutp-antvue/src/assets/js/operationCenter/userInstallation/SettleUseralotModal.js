import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"
import {
    getQueryCustomerNamesListData
} from '@api/operationCenter/userInstallation/settleUseralotList.js'
export default {
    name: "SettleUseralotModal",
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
                add: "/settle/settleUseralot/one",
                edit: "/settle/settleUseralot/one",
            },
            dataSource:[],
        }
    },
    created () {
    },
    mounted(){
        let data={
            customerName:''
        }
        getQueryCustomerNamesListData(data,this);
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
                this.form.setFieldsValue(pick(this.model,'lotSn','customerId','lotContent','taskName','telephone','installState','purpose','accepidea','delFlag'))
                //时间格式化
                this.form.setFieldsValue({lotTime:this.model.lotTime?moment(this.model.lotTime):null})
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

                    if(!values.customerId){
                        this.$message.info('客户名称不能为空!');
                        return;
                    }
                    if(!values.lotTime){
                        this.$message.info('报装时间不能为空!');
                        return;
                    }
                    that.confirmLoading = true;
                    let httpurl = '';
                    let method = '';
                    if(!this.model.id){
                        httpurl+=this.url.add;
                        method = 'post';
                    }else{
                        httpurl+=this.url.edit;
                        method = 'post';
                    }
                    let formData = Object.assign(this.model, values);
                    //时间格式化
                    formData.lotTime = formData.lotTime?formData.lotTime.format('YYYY-MM-DD HH:mm'):null;
                    let data;
                    if(!this.model.id){
                        data={
                            customerId:formData.customerId,
                            lotTime:formData.lotTime,
                            lotContent:formData.lotContent,
                            purpose:formData.purpose,

                        }
                    }else{
                        data={
                            customerId:formData.customerId,
                            lotTime:formData.lotTime,
                            lotContent:formData.lotContent,
                            purpose:formData.purpose,
                            id:formData.id
                        }
                    }
                    console.log(formData)
                    httpAction(httpurl,data,method).then((res)=>{
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
        handleChange (value) {
            console.log(`selected ${value}`);
        },

    }
}