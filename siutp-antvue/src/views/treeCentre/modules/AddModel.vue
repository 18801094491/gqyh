<template>
    <a-modal
        :title="title"
        :width="width"
        :visible="visible"
        :confirmLoading="confirmLoading"
        @ok="handleOk"
        @cancel="handleCancel"
        :destroyOnClose="true"
        cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form"> 
                <template v-if="title == '新增'">
                <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-tree-region-select
                        :url="parentUrl"
                        ref="treeSelect"
                        :disabled="disTree"
                        placeholder="请选择父级节点"
                        v-decorator="['parentId', validatorRules.parentId]"
                        dict="opt_label,label_name,id"
                        pidField="parent_id"
                        pidValue="0"
                        condition="{&quot;del_flag&quot;:0}">
                    </j-tree-region-select>
                </a-form-item>
                </template>
                <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-model="treeName" placeholder="请输入名称"></a-input>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
import {httpAction, getAction} from '@/api/manage'
import pick from 'lodash.pick'
import JTreeRegionSelect from '@/components/jeecg/JTreeRegionSelectCenter'
export default {
    name: 'AddModel',
    components: {
        JTreeRegionSelect
    },
    props:['modelType'],
    data () {
        return {
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
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
            validatorRules: {
                parentId: {},
                labelName: {}
            },
            url: { },
            parentUrl: '',
            expandedRowKeys: [],
            pidField: "parent_id", //parentId
            disTree: false,
            treeName: '',
            id: '',
            parentId: '',
        }
    },
    mounted () {
    },
    watch: {
        title (newVal,oldVal) {
            if(newVal == "编辑"){
                this.getInfo();
            }
        },
    },
    methods: {
        add() {
            this.edit({});
        },
        edit(record) {
            this.changUrl()
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true;
            this.$nextTick(() => {
                this.disTree = this.title == '编辑';
            })
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.treeName='';
            this.title = '';
        },
        changUrl() {
            if(this.modelType=='inf'){
                // 信息中心
                this.url={
                    add: "/centre/inf/equ/add", // 新增节点
                    edit: "/centre/inf/equ/edit",  // 编辑节点
                    checkCode:"/sys/category/checkCode",
                    infoUrl: '/centre/inf/equ/query', //通过id查询
                };
                this. parentUrl='/centre/inf/equ/getAll/';
            }else if(this.modelType=='mea'){
                // 计量中心
                this.url={
                    add: "/centre/met/cust/add",
                    edit: "/centre/met/cust/edit",
                    checkCode:"/sys/category/checkCode",
                    infoUrl: '/centre/met/cust/query',
                };
                this. parentUrl='/centre/met/cust/getAll/';
            }else if(this.modelType=='ope'){
                // 运营中心
                this.url={
                    add: "/centre/opt/equ/add",
                    edit: "/centre/opt/equ/edit",
                    checkCode:"/sys/category/checkCode",
                    infoUrl: '/centre/opt/equ/query',
                };
                this. parentUrl='/centre/opt/equ/getAll/';
            }else if(this.modelType=='saf'){
                // 安全中心
                this.url={
                    add: "/centre/sec/equ/add",
                    edit: "/centre/sec/equ/edit",
                    checkCode:"/sys/category/checkCode",
                    infoUrl: '/centre/sec/equ/query',
                };
                this. parentUrl='/centre/sec/equ/getAll/';
            }
        },
        handleOk() {
            const that = this;
            // 触发表单验证
            let treeName = this.treeName;
            let parentId = '';
            let formData = {};
            if(this.title == '新增'){
                parentId = this.$refs.treeSelect.parentId;
                formData = {
                    id:'',
                    name: treeName,
                    parentId: parentId
                }
            } else  {
                parentId = this.parentId;
                formData = {
                    id: this.id,
                    name: treeName,
                    parentId: parentId
                }
            }
            this.$nextTick(() => {
                if (!parentId) {
                    that.$message.info("请选择父节点");
                    return;
                }
                if (!treeName) {
                    that.$message.info("请输入名称");
                    return;
                }
                if (treeName.length > 100) {
                    that.$message.info("名称最大长度为100，当前长度"+treeName.length);
                    return;
                }
                if (parentId && treeName) {
                    that.confirmLoading = true;
                    let httpurl = '';
                    let method = '';
                    if (!this.id) {
                        httpurl += this.url.add;
                        method = 'post';
                    } else {
                        httpurl += this.url.edit;
                        method = 'post';
                    }
                    httpAction(httpurl, formData, method).then((res) => {
                        if (res.code * 1 == 200) {
                            that.$message.success(res.message);
                            this.$emit('ok', formData, this.expandedRowKeys.reverse());
                            that.submitSuccess(formData)
                        } else {
                            that.$message.info(res.message);
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
            this.$emit('ok');
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
        getInfo () {
            let that = this;
            let params = { id: this.id}
            let infoUrl = this.url.infoUrl;
            getAction(this.url.infoUrl, params).then((res) => {
                if (res.code * 1 == 200) {
                    that.treeName = res.data.name;
                    that.parentId =  res.data.id
                } else {
                    that.$message.info(res.message);
                }
            })
        }
    }
}

</script>