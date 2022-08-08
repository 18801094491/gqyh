<template>
    <div>
        <a-table rowKey="id" :columns="columns" :data-source="data" bordered :row-selection="rowSelection" >
        </a-table>
    </div>
</template>

<script>
    import {
        getWorkListMatterList,
    } from '@/api/workOrderManagement/maintenance/index.js' 
    export default {
        name: 'questionList',
        props: ['tableDataList','questionList'],
        data () {
            return {
                columns:  [{
                    title: '问题标题',
                    dataIndex: 'title',
                    key: 'title',
                    align: "center",
                    width: 190,
                    scopedSlots: { customRender: 'title' },
                },{
                    title: '提交人',
                    dataIndex: 'subName',
                    key: 'subName',
                    align: "center",
                    width: 100,
                    scopedSlots: { customRender: 'subName' },
                },{
                    title: '提交时间',
                    dataIndex: 'time',
                    key: 'time',
                    align: "center",
                    width: 190,
                    scopedSlots: { customRender: 'time' },
                },{
                    title: '问题类型',
                    dataIndex: 'typeDes',
                    key: 'typeDes',
                    align: "center",
                    width: 80,
                    scopedSlots: { customRender: 'typeDes' },
                }],
                data:[],
                editingKey: '',
                isSave: true, //是否可点击保存
                selectedData: [],
                setSort: '',
                items: [],
                selectedRowKeys: [],
            }
        },
        computed: {
            rowSelection () {
                return {
                    selectedRowKeys: this.selectedRowKeys,
                    onChange: (selectedRowKeys, selectedRows) => {
                        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
                        this.selectedData = selectedRows;
                        this.selectedRowKeys = selectedRowKeys;
                    }
                }
            }
        },
        watch: {
            tableDataList: {   
                immediate: true,
                handler (val) {
                    this.items=val
                }
            },
            questionList: {
               immediate: true,
                handler (val) {
                    this.items=val
                } 
            }
        },
        mounted () {
            let params = {startDate: '',overDate: ''}
            getWorkListMatterList(params, this);
            setTimeout(()=>{
                let data = this.data;
                this.cacheData = data.map(item => ({ ...item }));
            },500)
        },
        methods: {
            showModle () {
                this.selectedRowKeys = [];
                this.selectedData = this.questionList;
                this.questionList.forEach((item) => {
                    this.selectedRowKeys.push(item.id)
                });
            }
        },
        updated () {}
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>