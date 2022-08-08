<template>
    <div class="margin12">
        <div class="screenCommonBox">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :span="8">
                            <a-form-item label="线路名称">
                                <a-input placeholder="请输入线路名称" v-model="queryParam.planName"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :span="4">
                            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                <a-button class="ant-btn-border" @click="searchReset" icon="reload"
                                          style="margin-left: 8px">重置</a-button>
                            </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">
            <!-- 操作按钮区域 -->
            <div class="table-operator">
                <a-button @click="handleAdd" type="primary" icon="plus" v-has="'contract:add'">新增</a-button>
            </div>
            <!-- table区域-begin -->
            <div>
                <a-table
                        ref="table"
                        size="middle"
                        bordered
                        rowKey="id"
                        :columns="columns"
                        :dataSource="dataSource"
                        :pagination="ipagination"
                        :loading="loading"
                        :rowClassName="(record, index) => record.alarmStatus * 1 === 1 ? 'warnStyle' : ''"
                        @change="handleTableChange">

                    <span slot="action" slot-scope="text, record">
                        <a @click="handleEdit(record)" v-has="'contract:edit'">编辑</a>

                        <a-divider type="vertical" v-has="'contract:edit'"/>
                        <a @click="details(record)">详情</a>
                    </span>
                </a-table>
            </div>
            <!-- table区域-end -->
        </a-card>
        <!-- 表单区域 -->
        <add-modal ref="modalForm" @ok="modalFormOk" :tplList="tplList"
                   :inPointCategoryList="inPointCategoryList"></add-modal>
    </div>
</template>

<script>
    import index from '@assets/js/workOrderManagement/workPlan/InspectionPlanManagement/index.js';

    export default {
        ...index
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>