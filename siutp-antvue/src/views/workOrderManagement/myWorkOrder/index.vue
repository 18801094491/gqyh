<template>
    <div class="margin12 workOrder-maintenance">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="5" :sm="8">
                            <a-form-item label="工单名称">
                                <a-input placeholder="请输入工单名称" v-model="queryParam.name"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="工单状态">
                                <a-select v-model="queryParam.status" placeholder="请选择工单状态">
                                    <a-select-option value>全部</a-select-option>
                                    <a-select-option v-for="(item,index) in workStatus"
                                      :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="10" :sm="8">
                            <a-form-item label="时间">
                                <a-date-picker v-model="queryParam.startDate" type="date" placeholder="选择开始时间" />
                                ~
                                <a-date-picker v-model="queryParam.overDate" type="date" placeholder="选择结束时间" />
                            </a-form-item>
                        </a-col>
                        <a-col class="float-r" :md="4" :sm="8">
                            <span class="table-page-search-submitButtons float-r">
                                <a-button type="primary" @click="searchQuery('')" icon="search">查询</a-button>
                                <a-button
                                    class="ant-btn-border ml8"
                                    @click="searchReset"
                                    icon="reload"
                                >重置</a-button>
                            </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">
            <a-table
              ref="table"
              size="middle"
              bordered
              row-key="id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              @change="handleTableChange">
                <span slot="action" slot-scope="text, record">
                    <a @click="handleAdd('see', record)">查看</a>
                </span>
            </a-table>
        </a-card>
        <a-drawer :title="drawerTitle" :width="900" @close="onCloseChange" :visible="visible" :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="工单名称" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                            <span>{{workItemData.name}}</span>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="工单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                            <span>{{workItemData.code}}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="工单类型" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                            <span>{{workItemData.typeDes}}</span>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="工单状态" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                            <span>{{workItemData.statusDes}}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                            <span>{{workItemData.startDate}}</span>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                            <span>{{workItemData.overDate}}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>

                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="负责班组" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                                <span>{{workItemData.teamName}}</span>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="班组责任人" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                                <span>{{workItemData.leaderName}}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="巡检区域" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                                <span>{{workItemData.areaName}}</span>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="巡检路线" :labelCol="labelCol" :wrapperCol="wrapperCol" >
                                <span>{{workItemData.routeName}}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>

                    <a-table
                        ref="table"
                        size="middle"
                        bordered
                        rowKey="id"
                        :row-selection="rowSelection"
                        :columns="matterListColumns"
                        :data-source="matterListDataSource"
                        :pagination="false"
                        :scroll="{ y: 456 }" >
                        <span slot="action" slot-scope="text, record">
                            <a :disabled="record.status == 3" @click="onCompleteChange(record.id)">标记完成</a>
                            <a-divider type="vertical" />
                            <a @click="onShowTaskDetailsModal(record.id)">详情</a>
                        </span>
                    </a-table>

                </a-form>
            </a-spin>
            <div class="btns">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" :disabled="!selectedRowKeys.length" @click="onCompleteChange(selectedRowKeys)" type="primary">批量标记完成</a-button>
            </div>
        </a-drawer>
        <taskDetails-modal ref="modalTaskDetails" :taskDetailsData="taskDetailsData"  @ok="modalFormOk" />
    </div>
</template>

<script>
    import index from "@assets/js/workOrderManagement/myWorkOrder/index.js";
    export default {
        ...index
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/workOrderManagement/myWorkOrder/index.less";
</style>