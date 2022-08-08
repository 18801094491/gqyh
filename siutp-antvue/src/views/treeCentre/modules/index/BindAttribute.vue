<template>
    <a-drawer
        :title="title"
        :width="800"
        @close="handleCancel"
        :visible="visible"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item label="属性名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input v-model="treeName" :disabled="true"></a-input>
                </a-form-item>
                <a-form-item label="属性排序集合" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select
                        mode="multiple"
                        placeholder="属性排序集合"
                        optionFilterProp="children"
                        style="width: 100%"
                        v-decorator="['attributeIds', {}]"
                        @change="handleChange">
                        <a-select-option :value="item.field" v-for="(item,index) in attributeList" :key="index">
                            {{item.title}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </a-spin>
        <div class="drawer-btns">
            <a-button class="mr08" @click="handleCancel">关闭</a-button>
            <a-button @click="handleOk" type="primary" >提交</a-button>
        </div>
    </a-drawer>
</template>
<script>
import { axios } from '@/utils/request';
export default {
    name: 'BindAttribute',
    data () {
        return {
            title: "绑定属性",
            visible: false,
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
            treeId: '', //属性节点id
            treeName: '', //属性名称
            attributeList:[], //属性列表
            attributeIds:[],
			objId: '',
			id: '',
			attrs: [], //绑定的属性(英)
			attrsName: [], //绑定的属性(汉)
			isOptAttrTreeVo: false,
        }
    },
    created(){
    },
    mounted () {
    },
    methods: {
        getSysObj (treeId) { //通过id获取树与属性-获取要绑定的对象信息
            let data = {treeId: treeId};
            axios.get('/centre/universal/getSysObj/',{
                params: data
            }).then(res => {
                if (res.code * 1 == 200) {
                    this.treeName = res.data.sysObjectVo.name;
                    this.attributeList = res.data.sysObjectVo.fieldList;
                    this.objId = res.data.sysObjectVo.id;
                    if (res.data.optAttrTreeVo) {
                        this.isOptAttrTreeVo = true;
                        this.id = res.data.optAttrTreeVo.id;
                        this.attrs = res.data.optAttrTreeVo.attrs.split(',');
                        this.attrsName = res.data.optAttrTreeVo.attrsName.split(',');
                        this.form.setFieldsValue({'attributeIds':this.attrs });
                    } else {
                        this.attrs = [];
                        this.attrsName = [];
                        this.isOptAttrTreeVo = false;
                        this.form.setFieldsValue({'attributeIds':this.attrs });
                    }
                } else {
                    this.$message.info(res.message);
                }
            })
        },

        handleChange (value, data) { //选中属性变动时触发
			let arr = [];
			value.map(item2 => {
                this.attributeList.map(item => {
                    if (item.field == item2) {
						arr.push(item.title)
					}
				})
			})
			this.attrs = value;
			this.attrsName = arr;
        },

		bindAdd () { //添加绑定属性
            axios.post('/centre/universal/bind/add', {
				treeId: this.treeId,
				objId: this.objId,
				attrs: (this.attrs).join(','),
				attrsName: (this.attrsName).join(',')
            }).then(res => {
                if (res.code * 1 == 200) {
                    this.$message.success(res.message);
                    this.colse()
                } else {
                    this.$message.info(res.message);
                }
            });
        },

		bindEdit () { //修改绑定属性
            axios.post('/centre/universal/bind/edit', {
				id: this.id,
				treeId: this.treeId,
				objId: this.objId,
				attrs: (this.attrs).join(','),
				attrsName: (this.attrsName).join(',')
            }).then(res => {
                if (res.code * 1 == 200) {
                    this.$message.success(res.message);
                    this.colse()
                } else {
                    this.$message.info(res.message);
                }
            })
        },
        
        handleCancel () { //关闭弹层
            this.visible = false;
            this.attrs = [];
            this.attrsName = [];
            this.isOptAttrTreeVo = false;
        },

        handleOk() { //添加/修改 提交
            if (!this.attrsName.length) {
                this.$message.info('请选择要绑定的属性')
                return;
            }
			if (!this.isOptAttrTreeVo) {
				this.bindAdd();
			} else {
				this.bindEdit();
			}
        },
        
		colse() { //提交成功关闭弹层
			this.handleCancel();
			this.$emit("bindOk");
		}
    }
}
</script>