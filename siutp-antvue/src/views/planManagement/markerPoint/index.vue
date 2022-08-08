<template>
    <div class="margin12 workOrder-maintenance">
        <!-- 筛选 -->
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="16">
                        <a-col :md="5" :sm="8">
                            <a-form-item label="区域选择">
                                <a-select v-model="queryParam.areaId" placeholder="请选择区域">
                                    <a-select-option value>全部</a-select-option>
                                    <a-select-option v-for="(item,index) in areaList"
                                      :key="index" :value="item.id">
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="巡检点类别">
                                <a-select v-model="queryParam.type" placeholder="请选择巡检点类别">
                                    <a-select-option value>全部</a-select-option>
                                    <a-select-option v-for="(item,index) in pointTypeList"
                                      :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="巡检点名称">
                                <a-input placeholder="请输入巡检点名称" v-model="queryParam.name"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="巡检点编号">
                                <a-input placeholder="请输入巡检点编号" v-model="queryParam.code"></a-input>
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
        <!-- 表格 -->
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
                <span slot="problemDescription" slot-scope="text, record">
                    <a-tooltip placement="top">
                        <template slot="title">
                            <span>{{record.problemDescription}}</span>
                        </template>
                        <div>{{record.problemDescription && record.problemDescription.length>10?record.problemDescription.substring(0,10)+'...':record.problemDescription}}</div>
                    </a-tooltip>
                </span>
                <span slot="action" slot-scope="text, record">
                    <a :data-data="record" @click="handleAdd('change',record)">修改</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title="确定删除吗?" @confirm="() => onDeleteChange(record.id)"
                      v-if="record.problemStatus!='2' && record.problemStatus!='3'">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-card>
        <!-- 弹层 -->
        <a-drawer :title="drawerTitle" :width="1200" @close="onCloseChange" :visible="visible"
          :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="巡检点名称">
                                <a-input v-model="name" placeholder="请输入巡检点名称"></a-input>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="巡检点编号">
                                <a-input v-model="code" placeholder="请输入巡检点编号"></a-input>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="所属区域">
                                <a-select v-model="areaId" placeholder="请选择所属区域">
                                    <a-select-option v-for="(item,index) in areaList"
                                    :key="index" :value="item.id">
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="是否重点">
                                <a-select v-model="important" placeholder="请选择是否重点">
                                    <a-select-option v-for="(item,index) in importantList"
                                    :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="巡检点类型">
                                <a-select v-model="type" placeholder="请确认巡检点类型">
                                    <a-select-option v-for="(item,index) in pointTypeList"
                                    :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="关联设备">
                                <a-select 
                                show-search 
                                v-model="equipmentId"
                                placeholder="请选择关联设备"
                                :filter-option="filterOption">
                                    <a-select-option value="">-- 请选择 --</a-select-option>
                                    <a-select-option v-for="(item,index) in equipmentList"
                                    :key="index" :value="item.id">
                                        {{item.equipmentName}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="9">
                            <a-form-item label="注意事项">
                                <a-textarea :rows="4" v-model="notices" placeholder="请输入注意事项"/>
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
                <div id="markerPoint-allmap"></div>
            </a-spin>
            <div class="btns">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" @click="addSubmit" type="primary">提交</a-button>
            </div>
        </a-drawer>
    </div>
</template>

<script>
    import index from "@assets/js/planManagement/markerPoint/index.js";

    export default {
        ...index
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/planManagement/markerPoint/index.less";
</style>