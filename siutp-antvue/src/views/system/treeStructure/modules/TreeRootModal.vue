<template>
    <div>
        <a-drawer
        :title="title"
        :maskClosable="true"
        width=650
        placement="right"
        :closable="true"
        @close="close"
        :visible="visible"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto'}">
            <a-spin :spinning="confirmLoading">
                <a-form :form="form" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-form-item label="名称">
                        <a-input placeholder="请输入名称" :disabled='!iswacth' :maxLength="32" v-decorator="['name',{}]"/>
                    </a-form-item>
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属中心">
                         <a-select v-decorator="['centre', {}]" placeholder="请输入所属中心"  :disabled='!isable'>
                            <a-select-option
                                v-for="(item,index) in centreData"
                                :value="item.value"
                                :key="index"
                            >{{ item.text }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="对象类型">
                        <a-select v-decorator="['objId', {}]" placeholder="请选择对象类型" :disabled='!isable'>
                            <a-select-option
                                v-for="(item,index) in objIdData"
                                :value="item.id"
                                :key="index"
                            >{{ item.name }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-form>
            </a-spin>
            <div class="drawer-btns">
                <a-button class="mr08" @click="handleCancel">关闭</a-button>
                <a-button @click="handleOk" type="primary" :disabled="!iswacth">提交</a-button>
            </div>
        </a-drawer>
    </div>
</template>
<script>
import { getTreeIdQuery, addTree, editTree } from '@/api/system/treeStructure.js'
export default {
    name: 'TreeRootModal',
    props: ['objIdData','centreData'],
    data () {
        return {
            isable: true,
            iswacth: true,
            object_name: '',
            object_center: '',
            object_type: '',
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
            isView: false,
            id: '',
        }
    },
    computed: {
    },
    watch: {
        title (newVal, oldVal) {
            if (newVal === '添加目录') {
                this.iswacth = true;
                this.isable = true
                this.isView = true;
            } else if (newVal === '编辑目录') {
                this.iswacth = true;
                this.isable = false;
                this.isView = true;
                getTreeIdQuery({ id: this.id }, this);
            } else if (newVal === '查看目录' || (newVal == oldVal && oldVal === "查看目录")) {
                this.iswacth = false;
                this.isable = false;
                this.isView = false;
                getTreeIdQuery({ id: this.id }, this)
            }
        }
    },
    mounted () {
    },
    methods: {
        close() {
            this.$emit('closeModal');
            this.visible = false;
            this.title = '';
            this.object_name = '';
            this.object_center = '';
            this.object_type = '';
            this.form.resetFields();
        },

        handleOk(e) {
            e.preventDefault();
            let all_return = this.form.getFieldsValue(name);
            this.object_name = all_return.name,
            this.object_center = all_return.centre,
            this.object_type = all_return.objId,
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    console.log(`提交数据-----------${values}`,values)
                    if(this.title == '添加目录'){
                        if(!this.object_name){
                            this.$message.info('名称不能为空');
                            return;
                        };
                        if(!this.object_center){
                           this.$message.info('请选择所属中心');
                            return;
                        };
                        if(!this.object_type){
                            this.$message.info('请选择对象类型');
                            return;
                        }
                        addTree({
                            name: this.object_name,
                            centre: this.object_center,
                            objId: this.object_type
                        }, this)
                    } else if (this.title == '编辑目录') {
                         if(!this.object_name){
                            this.$message.info('名称不能为空');
                            return;
                        };
                        editTree({
                            id: this.id,
                            name: this.object_name,
                        }, this)
                    }
                }
            })
        },
        handleCancel() {
            this.close();
        },
    }
}
</script>