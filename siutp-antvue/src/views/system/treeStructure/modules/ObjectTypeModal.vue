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
                    <a-form-item label="对象名称">
                        <a-input placeholder="请输入对象名称" :maxLength="32" :disabled='!isable' v-model="obj_name"/>
                    </a-form-item>
                    <a-form-item label="对象类型"> 
                        <a-input placeholder="请输入对象类型" :disabled='!addobj' :maxLength="200" @blur="objTypeBlur" v-model="obj_type"/>
                    </a-form-item>
                    <a-form-item label="表名">
                        <a-input placeholder="请输入表名" :disabled='!addobj' :maxLength="100" v-model="obj_table"/>
                    </a-form-item>
                </a-form>
            </a-spin>
            <div class="drawer-btns">
                <a-button class="mr08" @click="handleCancel">关闭</a-button>
                <a-button @click="handleOk" :disabled="!isable" type="primary" >提交</a-button>
            </div>
        </a-drawer>
    </div>
</template>
<script>
import { checkObjType, getIdQuery, addObjectType, editObjectType } from '@/api/system/treeStructure.js'
export default {
    name: 'ObjectTypeModal',
    props: ['titleModel','visibleModel'],
    data () {
        return {
            isable:true,
            addobj:false,
            obj_name:'',
            obj_type:'',
            obj_table:'',
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
            isPass : false,
            id: '', //在父组件中赋值
        }
    },
    computed: {
    },
    watch: {
        title (newVal, oldVal) {
            if (newVal === '添加对象') {
                this.isable = true;
                this.addobj = true;
            } else if (newVal === '编辑对象') {
                this.addobj = false;
                this.isable = true;
                 getIdQuery({ id: this.id }, this, res => {
                    this.obj_name = res.name;
                    this.obj_type = res.objType;
                    this.obj_table = res.tableName;
                })
            } else if (newVal === '查看对象') {
                this.isable = false;
                this.addobj = false;
                getIdQuery({ id: this.id }, this, res => {
                    this.obj_name = res.name;
                    this.obj_type = res.objType;
                    this.obj_table = res.tableName;
                })
            }
        }
    },
    mounted () {
    },
    methods: {
        close() {
            this.$emit('closeModal');
            this.obj_name = '';
            this.obj_type = '';
            this.obj_table = '';
            this.visible = false;
            this.title = '';
            this.form.resetFields();
        },

        handleOk(e) {
            e.preventDefault();
            let obj_name = this.obj_name;
            let obj_type = this.obj_type;
            let obj_table = this.obj_table;
            // 表单验证
            if(this.title === '添加对象'){
                if (!obj_name) {
                    this.$message.info('对象名称不能为空');
                    return;
                } else if (!obj_type) {
                    this.$message.info('对象类型不能为空');
                    return;
                } else if (!this.isPass) {
                    this.$message.info('请核对对象类型!');
                    return;
                } else if (!obj_table) {
                    this.$message.info('表名不能为空');
                    return;
                }
                addObjectType({
                    id: this.id,
                    name: obj_name,
                    objType: obj_type,
                    tableName: obj_table,
                }, this);
                this.obj_name = '';
                this.obj_type = '';
                this.obj_table = '';
            } else if (this.title === '编辑对象') {
                if (!obj_name) {
                    this.$message.info('对象名称不能为空');
                    return;
                };
                editObjectType({
                    id: this.id,
                    name: obj_name,
                }, this)
            }
        },
        handleCancel() {
            this.close();
        },
        
        objTypeBlur(e) { //失去焦点
            e.preventDefault();
            this.obj_type && checkObjType({ objType: this.obj_type }, this)
        }
    }
}
</script>