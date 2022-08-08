<template>
    <div class="margin12 workOrder-polyLine">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
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
                            <a-form-item label="线路名称">
                                <a-input placeholder="请输入线路名称" v-model="queryParam.name"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="5" :sm="8">
                            <a-form-item label="线路编号">
                                <a-input placeholder="请输入线路编号" v-model="queryParam.code"></a-input>
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
                <span slot="problemDescription" slot-scope="text, record">
                    <a-tooltip placement="top">
                        <template slot="title">
                            <span>{{record.problemDescription}}</span>
                        </template>
                        <div>{{record.problemDescription && record.problemDescription.length>10?record.problemDescription.substring(0,10)+'...':record.problemDescription}}</div>
                    </a-tooltip>
                </span>
                <span slot="action" slot-scope="text, record">
                    <a @click="handleAdd('see', record)">查看</a>
                    <a-divider type="vertical" />
                    <a @click="handleAdd('change', record)">修改</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title="确定删除吗?" @confirm="() => onDeleteChange(record.id)"
                      v-if="true">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-card>
        <a-drawer class="workOrder-polyLine-drawer" :title="drawerTitle" :width="800" @close="onCloseChange" :visible="visible"
          :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="线路名称">
                                <a-input v-model="name" placeholder="请输入线路名称" :disabled="see_disabled" />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="线路编号">
                                <a-input v-model="code" placeholder="请输入线路编号" :disabled="see_disabled" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="16">
                        <a-col :span="12">
                            <a-form-item label="所属区域">
                                <a-select v-model="areaId" placeholder="请确认所属区域" :disabled="see_disabled" @change="onSwitchAreaChange" >
                                    <a-select-option v-for="(item,index) in areaList" 
                                    :key="index" :value="item.id">
                                        {{item.name}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24" class="mb20">
                        <a-col :span="24">
                            <a-form-item label="巡检点列表" :labelCol="{ xs: { span: 24 }, sm: { span: 3 } }" :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
                                <a-button class="float-r" @click="onShowMarkerModal()" type="primary" :disabled="see_disabled">选择巡检点</a-button>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-table
                        ref="table"
                        size="middle"
                        bordered
                        rowKey="id"
                        :columns="pointListColumns"
                        :dataSource="selectedPointList" >
                        <span slot="action" slot-scope="text, record">
                            <a @click="onSelPointDelChange(record.id)" :disabled="see_disabled">删除</a>
                        </span>
                    </a-table>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="地图线路" :labelCol="{ xs: { span: 24 }, sm: { span: 3 } }" :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
                                <a-button class="float-r" @click="onShowPolyLineModal" type="primary" >线路编辑</a-button>
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
            </a-spin>
            <div class="btns float-r">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" :disabled="see_disabled" @click="onAddSubmitChange" type="primary">提交</a-button>
            </div>
        </a-drawer>
        <optMarker-modal 
            ref="modalForm"
            :areaId="areaId"
            :pointList="pointList"
            :selectedPointList="selectedPointList"
            @onSearchQueryChange="onSearchQueryChange"
            @onSelectedPointChange="onSelectedPointChange"
        />
        <a-modal title="线路编辑" :width="678" :visible="visible_line" 
            cancelText="关闭" @cancel="lineModalCancel" @ok="lineModalOk">
            <a-spin :spinning="confirmLoading">
                <a-button @click="onRemoveOverlayChange" type="primary" :disabled="see_disabled">重绘</a-button>
                <span class="map-hint">请依次点击巡检点以确定巡检顺序</span>
                <div id="polyLine-allmap"></div>
            </a-spin>
        </a-modal>

    </div>
</template>

<script>
    import index from "@assets/js/planManagement/polyLine/index.js";
    export default {
        ...index
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/planManagement/polyLine/index.less";
</style>