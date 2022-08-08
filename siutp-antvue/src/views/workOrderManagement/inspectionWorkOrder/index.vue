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
                    <a :data-data="record" @click="handleAdd('see', record)">查看</a>
                    <a-divider type="vertical"/>
                    <a :data-data="record" @click="onShowMonitorModal(record.id)">工单监控</a>
                    <a-divider type="vertical"/>
                    <a :data-data="record" @click="handleAdd('change', record)">修改</a>
                    <a-divider type="vertical"/>
                    <a-popconfirm title="确定删除吗?" @confirm="() => onDeleteChange(record.id)"
                      v-if="record.problemStatus!='2' && record.problemStatus!='3'">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-card>
        <a-drawer :title="drawerTitle" :width="700" @close="onCloseChange" :visible="visible" :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="选择临时任务:" />
                        </a-col>
                        <a-col :span="12">
                            <a-button class="float-r" type="primary" :disabled="see_disabled" @click="clickQuestionList">选择临时任务</a-button>
                        </a-col>
                    </a-row>
                    <a-table ref="table" size="middle" bordered rowKey="id"
                    :columns="matterListColumns" :dataSource="matterListDataSource" >
                        <div slot="seq" slot-scope="text, record">
                            <a-input
                                :disabled="see_disabled"
                                style="margin: -5px 0"
                                :value="text"
                                @change="e => handleChangeInput(e.target.value, record.id)"/>
                        </div>
                        <template slot="operation" slot-scope="text, record">
                            <div class="editable-row-operations">
                                <template v-if="see_disabled">
                                    <a @click="onShowTaskDetailsModal(record.id)">查看</a>
                                </template>
                                <template v-else>
                                    <a @click="() => onDelete(record.id)">删除</a>
                                </template>
                            </div>
                        </template>
                    </a-table>
                
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="工单名称">
                                <a-input v-model="name" placeholder="请输入工单名称" :disabled="see_disabled" />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="工单编号">
                                <a-input v-model="code" placeholder="请输入工单编号" :disabled="see_disabled" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="开始时间" >
                                <a-date-picker v-model="startDate" placeholder="请选择时间" :disabled="see_disabled" @change="onSetStartDateChange" />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="结束时间">
                                <a-date-picker v-model="overDate" placeholder="请选择时间" :disabled="see_disabled" @change="onSetOverDateChange" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="选择班组">
                                <a-select v-model="teamId" placeholder="请选择班组" :disabled="see_disabled" @change="onSetWorkTeamChange(teamId, 'change')">
                                    <a-select-option v-for="(item,index) in workTeamList"
                                    :key="index" :value="item.id">
                                        {{item.teamName}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="选择管理员">
                                <a-select v-model="teamLeaderId" placeholder="请选择管理员" :disabled="see_disabled">
                                    <a-select-option v-for="(item,index) in memberList"
                                    :key="index" :value="item.userId">
                                        {{item.realname}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
            </a-spin>
            <div class="btns">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" @click="addSubmit" type="primary" :disabled="see_disabled">提交</a-button>
            </div>
        </a-drawer>

        <monitor-modal ref="modalMonitor" :monitorData="monitorData"  @ok="modalFormOk" />
        <taskDetails-modal ref="modalTaskDetails" :taskDetailsData="taskDetailsData"  @ok="modalFormOk" />

        <a-modal v-model="questionListVisible" title="选择临时任务" :width="800" @ok="questionListOk" >
            <question-list ref="questionListTable" :tableDataList="matterListDataSource" :questionList="questionList"/>
        </a-modal>
    </div>
</template>

<script>
    import index from "@assets/js/workOrderManagement/inspectionWorkOrder/index.js";
    export default {
        ...index
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/workOrderManagement/inspectionWorkOrder/index.less";
</style>