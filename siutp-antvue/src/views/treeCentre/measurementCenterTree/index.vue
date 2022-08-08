<template>
    <div class="margin12">
        <a-card :bordered="false">
            <div class="table-operator">
                <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
            </div>
            <div>
                <a-table
                    ref="table"
                    size="middle"
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="false"
                    :loading="loading"
                    @change="handleTableChange"
                    @expand="handleExpand"
                    :expandedRowKeys="expandedRowKeys">
                    <span slot="name" slot-scope="text, record" :class="record.treeId?'other_style':''">{{text}}</span>
                    <span slot="action" slot-scope="text, record">
                        <template v-if="!record.treeId">
                            <a @click="handleEdit(record)">编辑</a>
                            <a-divider type="vertical" />
                            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)"><a>删除</a></a-popconfirm>
                            <a-divider type="vertical" />
                            <a @click="bindEvent(record.id)">绑定节点</a>
                        </template>
                        <template v-if="record.treeId">
                            <a @click="bindEvent(record.treeId)">编辑</a>
                            <a-divider type="vertical" />
                            <a-popconfirm title="确定删除吗?" @confirm="() => handleDeletes(record.id)"><a>删除</a></a-popconfirm>
                        </template>
                    </span>
                </a-table>
            </div>
            <add-model ref="modalForm" @ok="modalFormOk" modelType='mea'></add-model>
            <bind-attribute ref="bindModalForm" @bindOk="bindModalFormOk"></bind-attribute>
        </a-card>
    </div>
</template>
<script>
import { axios } from '@/utils/request';
import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixinTree";
import AddModel from "../modules/AddModel";
import BindAttribute from "@/views/treeCentre/modules/index/BindAttribute"
export default {
    name: 'measurementCenterTree',
    mixins: [JeecgListMixin],
    components: {
        AddModel,
        BindAttribute
    },
    data () {
        return {
            description: "设备树节点管理页面",
            columns: [{
                title: "树节点名称",
                align: "left",
                dataIndex: "name",
                width: 800,
                scopedSlots: { customRender: "name" }
            },{
                title: "操作",
                dataIndex: "action",
                align: "center",
                width: 200,
                scopedSlots: { customRender: "action" }
            }],
            url: {
                list: "/centre/met/cust/list/", // 根节点(已改)
                childList: "/centre/met/cust/list/", // 点击加号获取子节点
                delete: "/centre/met/cust/delete", // 删除节点
                deleteBatch: "/equipment/optLabel/deleteBatch", //批量删除操作
                exportXlsUrl: "/sys/category/exportXls",
                importExcelUrl: "sys/category/importExcel"
            },
            expandedRowKeys: [],
            hasChildrenField: "childrenNum",
            pidField: "parentId",
            dictOptions: {},
        }
    },
    computed: {
        importExcelUrl() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        },
    },
    methods: {
        onChange1(value) {
            this.value1 = value;
        },
        onSearch() {
            console.log(...arguments);
        },
        onSelect() {
            console.log(...arguments);
        },
        loadData(arg) {
            let _this = this;
            if (arg == 1) {
                this.ipagination.current = 1;
            }
            this.loading = true;
            this.expandedRowKeys = [];
            let params = this.getQueryParams();
            return new Promise(resolve => {
                getAction(this.url.list, params).then(res => {
                    if (res.code * 1 == 200) {
                        let result = res.data;
                        let optAttrTree = [];
                        let treeList = res.data.treeList;
                        let arrTreeList = [];
                        if (res.data.optAttrTree) {
                            optAttrTree.push(res.data.optAttrTree);
                            arrTreeList = [...treeList,...optAttrTree];
                            _this.dataSource = _this.getDataByResult(arrTreeList);
                        } else {
                            arrTreeList = [...treeList]
                            _this.dataSource = _this.getDataByResult(arrTreeList);
                        }
                        this.handleExpand(true, _this.dataSource[0])
                    } else {
                        this.$message.info(res.message);
                    }
                    this.loading = false;
                });
            });
        },
        //判断是否存在子集元素
        getDataByResult(result) {
            return result.map(item => {
                if (item[this.hasChildrenField] > 0) {
                    let loadChild = {
                        id: item.id + "_loadChild",
                        name: "loading...",
                        isLoading: true
                    };
                    item.children = [loadChild];
                }
                return item;
            });
        },
        //点击展开图标时触发
        handleExpand(expanded, record) {
            let _this = this;
            // 判断是否是展开状态
            if (expanded) {
                this.expandedRowKeys.push(record.id);
                if (
                    record.children.length > 0 &&
                    record.children[0].isLoading === true
                ) {
                    let params = this.getQueryParams(); //查询条件
                    params[this.pidField] = record.id;
                    getAction(this.url.childList, params).then(res => {
                        if (res.code * 1 == 200) {
                            let optAttrTree = [];
                            let treeList = res.data.treeList;
                            let arrTreeList = [];
                            if (res.data.optAttrTree) {
                                optAttrTree.push(res.data.optAttrTree);
                                arrTreeList = [...treeList,...optAttrTree];
                                record.children = this.getDataByResult(arrTreeList);
                                _this.dataSource = [...this.dataSource];
                            } else if(!res.data.optAttrTree && res.data.treeList.length > 0){
                                record.children = this.getDataByResult(treeList);
                                _this.dataSource = [...this.dataSource];
                            } else {
                                record.children = "";
                                record.hasChildrenField = "0";
                            }
                        } else {
                            this.$message.info(res.message);
                        }
                    });
                }
            } else {
                let keyIndex = this.expandedRowKeys.indexOf(record.id);
                if (keyIndex >= 0) {
                    this.expandedRowKeys.splice(keyIndex, 1);
                }
            }
        },
        initDictConfig() {},
        modalFormOk(formData, arr) {
            this.loadData();
        },
        editOk(formData, arr) {
            if (arr && arr.length > 0) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].id == formData.id) {
                        arr[i] = formData;
                        break;
                    } else {
                        this.editOk(formData, arr[i].children);
                    }
                }
            }
        },
        async addOk(formData, arr) {
            if (!formData[this.pidField]) {
                this.loadData();
            } else {
                this.expandedRowKeys = [];
                for (let i of arr) {
                    await this.expandTreeNode(i);
                }
                this.loadData();
            }
        },
        //获取子节点
        expandTreeNode(nodeId) {
            return new Promise((resolve, reject) => {
                this.getFormDataById(nodeId, this.dataSource);
                let row = this.parentFormData;
                this.expandedRowKeys.push(nodeId);
                let params = this.getQueryParams(); //查询条件
                params[this.pidField] = nodeId;
                getAction(this.url.childList, params).then(res => {
                    if (res.code * 1 == 200) {
                        if (res.data.treeList && res.data.treeList.length > 0) {
                            row.children = this.getDataByResult(res.data.treeList);
                            this.dataSource = [...this.dataSource];
                            resolve();
                        } else {
                            reject();
                        }
                    } else {
                        reject();
                    }
                });
            });
        },
        getFormDataById(id, arr) {
            if (arr && arr.length > 0) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].id == id) {
                        this.parentFormData = arr[i];
                    } else {
                        this.getFormDataById(id, arr[i].children);
                    }
                }
            }
        },
        //绑定
        bindEvent (treeId) {
            this.$refs.bindModalForm.treeId = treeId;
            this.$refs.bindModalForm.getSysObj(treeId)
            this.$refs.bindModalForm.visible = true;
        },
        //删除
        handleDeletes (treeId) {
            let _this = this;
            let data = {id: treeId};
            axios.delete('/centre/met/cust/delete',{
                params: data
            }).then(res => {
                let result = res.data;
                if (res.code * 1 == 200) {
                    _this.$message.success(res.message);
                    _this.loadData();
                } else {
                    _this.$message.info(res.message);
                }
            }) 
        },
        bindModalFormOk () {
            this.loadData();
        }
    }

}
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style>
    .other_style{
        color:orange
    }
</style>