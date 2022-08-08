<template>
    <div class="margin12 workOrder-maintenance">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="18">
                        <a-col :md="5" :sm="8">
                            <a-form-item label="计划名称">
                                <a-input placeholder="请输入计划名称" v-model="queryParam.name"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="计划编号">
                                <a-input placeholder="请输入计划编号" v-model="queryParam.code"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="区域">
                                <a-select v-model="queryParam.areaId" placeholder="请选择区域" @change="onSwitchAreaChange(queryParam.areaId, '', 'queryRouteList')">
                                    <a-select-option value>全部</a-select-option>
                                    <a-select-option v-for="(item,index) in areaList"
                                      :key="index" :value="item.id">
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="线路">
                                <a-select v-model="queryParam.routeId" placeholder="请选择线路">
                                    <a-select-option value>全部</a-select-option>
                                    <a-select-option v-for="(item,index) in queryRouteList"
                                      :key="index" :value="item.id">
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
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
            <div class="table-operator">
                <a-button @click="handleAdd('add')" type="primary" icon="plus">新增</a-button>
            </div>
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
                <a-switch slot="workStatusCode" checkedChildren="启用" slot-scope="text, record"
                    unCheckedChildren="停用" v-model="record.workStatusCode" defaultChecked
                    @change="workStatusCodeChange(record)"/>
                <span slot="action" slot-scope="text, record">
                    <a :data-data="record" @click="handleAdd('change',record)">修改</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title="确定删除吗?" @confirm="() => onDeleteChange(record.id)"
                      v-if="true">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-card>
        <a-drawer :title="drawerTitle" :width="800" @close="onCloseChange" :visible="visible"
          :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="计划名称" len="10">
                                <a-input v-model="name" placeholder="请输入计划名称" len="15"/>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="计划编号">
                                <a-input v-model="code" placeholder="请输入计划编号"></a-input>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="巡检区域">
                                <a-select placeholder="请选择巡检区域" v-model="areaId" @change="onSwitchAreaChange(areaId, 'set')">
                                    <a-select-option v-for="(item,index) in areaList"
                                    :key="index" :value="item.id"  > <!-- @click="areaId=item.id" -->
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="巡检线路">
                                <a-select placeholder="请选择巡检线路" v-model="routeId">
                                    <a-select-option v-for="(item,index) in routeList"
                                    :key="index" :value="item.id">
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="开始时间" >
                                <a-date-picker placeholder="请选择时间" v-model="startDate" @change="onSetStartDateChange" />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="结束时间">
                                <a-date-picker placeholder="请选择时间" v-model="overDate" @change="onSetOverDateChange" />
                            </a-form-item>
                        </a-col>
                    </a-row>

                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="班组选择">
                                <a-select placeholder="请选择班组" v-model="teamId" @change="onSetWorkTeamChange(teamId, 'change')">
                                    <a-select-option v-for="(item,index) in workTeamList"
                                    :key="index" :value="item.id">
                                        {{item.teamName}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="管理人员">
                                <a-select placeholder="请选择管理人员" v-model="teamLeaderId">
                                    <a-select-option v-for="(item,index) in memberList"
                                    :key="index" :value="item.userId">
                                        {{item.realname}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24" class="planCycle textleft-label">
                        <a-col :span="24">
                            <a-form-item label="周期" :labelCol="{xs:{span:24},sm:{span:8}}" :wrapperCol="{xs:{span:24},sm:{span:15}}">
                                <a-radio-group class="planCycle-radio" v-model="radioValue" @change="bindPlanCycleChange">
                                    <a-radio value="0">每天</a-radio>
                                    <a-radio value="1">每周</a-radio>
                                    <a-radio value="2">每月</a-radio>
                                    <!-- <a-radio value="3">每年</a-radio> -->
                                </a-radio-group>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-checkbox-group v-model="frequencyDescList" :options="planCycleList" @change="onSelectDateChange" />
                </a-form>
            </a-spin>
            <div class="btns">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" @click="addSubmit" type="primary">提交</a-button>
            </div>
        </a-drawer>
    </div>
</template>

<script>
    import index from "@assets/js/planManagement/inspectionPlan/index.js";
    export default {
        ...index
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/planManagement/inspectionPlan/index.less";
    /* 周期多选 */
    .cust-checkbox {
        display: block;
        width: 550px;
        margin: 0 auto 24px;
    }
    .cust-checkbox .ant-col-3 {
        display: block;
        width: 14.25%;
        box-sizing: border-box;
        margin-bottom: 5px;
    }
</style>