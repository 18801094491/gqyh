<template>
    <div>
        <div class="margin12">
            <div class="screenCommonBox">
                <div class="table-page-search-wrapper">
                    <a-form layout="inline" @keyup.enter.native="searchQuery">
                        <a-row :gutter="24">
                            <a-col :md="6" :sm="12">
                                <a-form-item label="名称">
                                    <a-input placeholder="请输入名称" v-model="queryParam.name" :maxLength="100"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="6" :sm="12">
                                <a-form-item label="所属中心">
                                    <a-select v-model="queryParam.centre" placeholder="请输入所属中心">
                                        <a-select-option value>全部</a-select-option>
                                        <a-select-option
                                            v-for="(item,index) in centreData"
                                            :value="item.value"
                                            :key="index"
                                        >{{ item.text }}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="6" :sm="12">
                                <a-form-item label="对象类型">
                                    <a-select v-model="queryParam.objId" placeholder="请选择变量类型">
                                        <a-select-option value>全部</a-select-option>
                                        <a-select-option
                                            v-for="(item,index) in objIdData"
                                            :value="item.id"
                                            :key="index"
                                        >{{ item.name }}
                                        </a-select-option>
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :md="6" :sm="12">
                                <span class="table-page-search-submitButtons">
                                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                    <a-button class="ml8" @click="searchReset" icon="reload">重置</a-button>
                                </span>
                            </a-col>
                        </a-row>
                    </a-form>
                </div>
            </div>
            <a-card :bordered="false">
                <div class="table-operator">
                    <a-button @click="handleAdd" v-has="'user:add'" type="primary" icon="plus" class="mb20">添加结构目录</a-button>
                </div>
                <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    @change="handleTableChange">
                    <span slot="action" slot-scope="text, record">
                        <a @click="editEvent(record.id)">编辑</a>
                        <a-divider type="vertical" />
                        <a-popconfirm title="确定删除吗?" @confirm="() => deleteEvent(record.id)">
                            <a>删除</a>
                        </a-popconfirm>
                        <a-divider type="vertical" />
                        <a @click="viewEvent(record.id)">查看</a>
                    </span>
                </a-table>
            </a-card>
        </div>
        <tree-root-modal ref="objectModel" @closeModal="closeModal" :objIdData="objIdData" :centreData="centreData"></tree-root-modal>
    </div>
</template>
<script>
import { getAllList, getCentres, getTreeTable, singleDeleteTree, multipleDeleteTree} from '@/api/system/treeStructure.js'
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import TreeRootModal from './modules/TreeRootModal'
export default {
    name: 'TreeRoot',
    mixins: [JeecgListMixin],
    components: {
        TreeRootModal
    },
    data () {
        return {
            queryParam: {
                centreName: '',
                centre: '',
                objId: '', 
            },
            // 表头
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key:'rowIndex',
                    width:60,
                    align:"center",
                    customRender:function (t,r,index) {
                    return parseInt(index)+1;
                    }
                },
                {
                    title: '名称',
                    align:"center",
                    dataIndex: 'name'
                },
                {
                    title: '所属中心',
                    align:"center",
                    dataIndex: 'centreName'
                },
                {
                    title: '对象类型',
                    align:"center",
                    dataIndex: 'objName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                },
            ],
            dataSource: [],
            loading: false,
            ipagination: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                total: 0  
            },
            objIdData: [],
            centreData: [],
        }
    },
    mounted () {
        getAllList(this); //获取对象类型
        getCentres(this); //获取中心集合
        this.getTreelist();
    },
    methods: {
        //获取表格数据
        getTreelist () {
            let _this = this;
            let params = {
                name: this.queryParam.name,
                centre: this.queryParam.centre,
                objId: this.queryParam.objId,
                pageSize: this.ipagination.pageSize,
                pageNo: this.ipagination.current
            }
            getTreeTable(params, this)
        },
        //数据初始化
        updata() {
            console.log("查询--------------", this.queryParam.name)
            let params = {
                name: this.queryParam.name,
                centre: this.queryParam.centre,
                objId: this.queryParam.objId,
                pageSize: this.ipagination.pageSize,
                pageNo: this.ipagination.current
            }
            // this.loading = true;
            getTreeTable(params, this)
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //重置
        searchReset () {
            this.queryParam.name = '';
            this.queryParam.objId = '';
            this.queryParam.centre = '';
            this.ipagination.current = 1;
            this.updata();
        },
        //分页
        handleTableChange (pagination) {
            this.ipagination.current = pagination.current;
            this.updata();
        },
        //添加目录
        handleAdd () {  
            this.$refs.objectModel.visible = true;
            this.$refs.objectModel.title = '添加目录';
        },
        //编辑目录
        editEvent (id) {
            console.log(`id--------${id}`)
            this.$refs.objectModel.id = id;
            this.$refs.objectModel.visible = true;
            this.$refs.objectModel.title = '编辑目录';
        },
        //查看目录
        viewEvent (id) {
            console.log(`id--------${id}`)
            this.$refs.objectModel.id = id;
            this.$refs.objectModel.visible = true;
            this.$refs.objectModel.title = '查看目录';
        },
        //单个删除目录
        deleteEvent (id) {
            console.log(`id--------${id}`)
            let data = {id: id}
            singleDeleteTree(data, this);
        },
        //弹层关闭
        closeModal () {
            this.ipagination.current = 1;
            this.queryParam.name = '';
            this.queryParam.objectType = '';
            this.getTreelist();
        }

    } 
}
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>