<template>
    <div>
        <a-form layout="inline"> 
            <a-row :gutter="12">
                <a-col :md="5" :sm="8" class="disFlex" >
                    <a-form-item label="标题" :labelCol="{xs: { span: 24 }, sm: { span: 7 }}" :wrapperCol="{xs: { span: 24 }, sm: { span: 18 }}">
                        <a-input placeholder="请输入标题" v-model="name"></a-input>
                    </a-form-item>
                </a-col>
            <!-- </a-row> 
            <a-row :gutter="24"> -->
                <a-col :md="13"  :sm="8" class="disFlex textleft-label">
                    <a-form-item label="选择时间">
                        <a-date-picker type="date" v-model="startDate" placeholder="选择开始时间" @change="onSetStartDateChange"/>
                        ~
                        <a-date-picker type="date" v-model="overDate" placeholder="选择结束时间" @change="onSetOverDateChange"/>
                    </a-form-item>
                </a-col>    
                <a-col class="float-r mt4" :md="6" :sm="8">
                    <span class="table-page-search-submitButtons">
                        <a-button type="primary" @click="onSearchQueryChange" icon="search">查询</a-button>
                        <a-button
                            class="ant-btn-border ml8"
                            @click="onSearchResetChange"
                            icon="reload"
                        >重置</a-button>
                    </span>
                </a-col>
            </a-row> 
        </a-form> 
        <a-table rowKey="id" :columns="columns" :data-source="data" bordered :row-selection="rowSelection">
        </a-table>
    </div>
</template>

<script>
    import {
        getWorkListKeepList,
    } from '@/api/workOrderManagement/maintenance/index.js'
    export default {
        name: 'equipmentList',
        props: ['tableDataList','equipmentList'],
        data () {
            return {
                columns: [{   
                    title: '标题',
                    align: "center",
                    dataIndex: 'title',
                },{
                    title: '设备编号',
                    align: "center",
                    dataIndex: 'equipmentSn',
                },{
                    title: '设备类型',
                    align: "center",
                    dataIndex: 'equipmentType',
                }],
                data: [],
                selectedData: [],
                items: [],
                selectedRowKeys: [],
                startDate: '',
                overDate: '',
                name: '',
            }
        },
        watch: {
            tableDataList: {   
                immediate: true,    // 这句重要
                handler (val) {
                    this.items=val
                }
            },
            equipmentList: {
                immediate: true,    // 这句重要
                handler (val) {
                    this.items=val
                } 
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
        mounted () {
            setTimeout(()=>{
                let data = this.data;
                getWorkListKeepList({}, this);
            })
        },
        methods: {
            showModle () {
                // this.onSearchResetChange();
                let params = { startDate: this.startDate, overDate: this.overDate, name: this.name}
                this.selectedRowKeys = [];
                this.selectedData = this.equipmentList;
                this.equipmentList.forEach((item) => {
                    this.selectedRowKeys.push(item.equipmentId || item.id)
                });
            },
            onSetStartDateChange(data, dateString) { //选择开始时间
                console.log(dateString)
                this.startDate = dateString;
            },
            onSetOverDateChange(data, dateString) { //选择结束时间
                console.log(dateString)
                this.overDate = dateString;
            },
            onSearchQueryChange() {
                if (new Date(this.overDate).getTime() < new Date(this.startDate).getTime()) {
                    this.$message.info('结束时间不能小于开始时间！');
                    return;
                }
                getWorkListKeepList({ startDate: this.startDate, overDate: this.overDate, name: this.name}, this);
            },
            onSearchResetChange() { //重置
                this.overDate = '';
                this.startDate = '';
                this.name = '';
                this.onSearchQueryChange();
            },
        },
        updated () {}
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
    .ant-form {
        margin-bottom: 20px;
    }
</style>