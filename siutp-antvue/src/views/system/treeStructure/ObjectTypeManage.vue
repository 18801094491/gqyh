<template>
    <div>
        <div class="margin12">
            <div class="screenCommonBox">
                <div class="table-page-search-wrapper">
                    <a-form layout="inline" @keyup.enter.native="searchQuery">
                        <a-row :gutter="24">
                            <a-col :md="8" :sm="8">
                                <a-form-item label="名称">
                                    <a-input placeholder="请输入对象名称" v-model="queryParam.name" :maxLength="32"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="8" :sm="8">
                                <a-form-item label="对象类型">
                                    <a-input placeholder="请输入对象名称" v-model="queryParam.objectType" :maxLength="200"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col :md="5" :sm="8" :offset="3">
                                <span class="table-page-search-submitButtons">
                                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                    <a-button class="ml8" @click="searchReset" icon="reload" >重置</a-button>
                                </span>
                            </a-col>
                        </a-row>
                    </a-form>
                </div>
            </div>
            <a-card :bordered="false">
                <div class="table-operator">
                    <a-button @click="handleAdd" v-has="'user:add'" type="primary" icon="plus" class="mb20">添加对象</a-button>
                    <!-- <a-button type="primary" @click="multipleDelete('用户信息')">批量删除</a-button> -->
                    <a-popconfirm title="你确认要删除吗" cancelText='取消' okText='确认' @confirm="multipleDelete('用户信息')"> 
                        <a-button  type="primary">批量删除</a-button>
                    </a-popconfirm>
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
                    @change="handleTableChange"
                    :row-selection="rowSelection">
                <!-- :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" -->
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
        <object-type-modal ref="objectModel" @closeModal="closeModal"></object-type-modal>
    </div>
</template> 
<script>
import { getObjectTypeTable, singleDelete, multipleDelete} from '@/api/system/treeStructure.js'
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import ObjectTypeModal from './modules/ObjectTypeModal'
export default {
    name: 'ObjectTypeManage',
    mixins: [JeecgListMixin],
    components: {
        ObjectTypeModal
    },
    data () {
        return {
            queryParam: {},
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
                    title: '对象类型',
                    align:"center",
                    dataIndex: 'objType'
                },
                {
                    title: '表名',
                    align:"center",
                    dataIndex: 'tableName'
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
            selectedRowKeys: [],
        }
    },
    computed: {
        rowSelection() {
            let _this = this;
            return  {
                selectedRowKeys: _this.selectedRowKeys,
                onChange: (selectedRowKeys, selectedRows) => {
                    console.log(`selectedRowKeys--: ${selectedRowKeys}`, 'selectedRows--: ', selectedRows);
                    _this.selectedRowKeys = selectedRowKeys
                }, 
            }
        }
    },
    mounted () {
        this.getObjectTypelist();
    },
    methods: {
        //获取表格数据
        getObjectTypelist () {
            let _this = this;
            let params = {
                name: this.queryParam.name,
                objType: this.queryParam.objectType,
                pageSize: this.ipagination.pageSize,
                pageNo: this.ipagination.current
            }
            getObjectTypeTable(params, this)
        },
        //数据初始化
        updata() {
            let params = {
                name: this.queryParam.name,
                objType: this.queryParam.objectType,
                pageSize: this.ipagination.pageSize,
                pageNo: this.ipagination.current
            }
            getObjectTypeTable(params, this)
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //重置
        searchReset () {
            this.queryParam.name = '';
            this.queryParam.objectType = '';
            this.ipagination.current = 1;
            this.updata();
        },
        //分页
        handleTableChange (pagination) {
            this.ipagination.current = pagination.current;
            this.updata();
        },
        //添加对象
        handleAdd () {  
            this.$refs.objectModel.visible = true;
            this.$refs.objectModel.title = '添加对象';
        },
        //编辑对象
        editEvent (id) {
            this.$refs.objectModel.id = id;
            this.$refs.objectModel.visible = true;
            this.$refs.objectModel.title = '编辑对象';

        },
        //查看对象
        viewEvent (id) {
            this.$refs.objectModel.id = id;
            this.$refs.objectModel.visible = true;
            this.$refs.objectModel.title = '查看对象';
        },
        //单个删除对象
        deleteEvent (id) {
            let data = {id: id}
            singleDelete(data, this);
        },
        //批量删除
        multipleDelete () {
            let ids = this.selectedRowKeys.join(',')
            console.log(typeof ids)
            let data = {ids:ids}
            console.log(data)
            console.log(typeof data)
            if (!ids) {
                this.$message.info('请选择要删除的对象！')
                return;
            }
            multipleDelete(data, this);
            this.selectedRowKeys = [];
        },
        //弹层关闭
        closeModal () {
            this.ipagination.current = 1;
            this.queryParam.name = '';
            this.queryParam.objectType = '';
            this.getObjectTypelist();
        }
    } 
}
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>