<template>
    <div class="margin12">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="5" :sm="8">
                            <a-form-item label="区域名称">
                                <a-input placeholder="请输入区域名称" v-model="queryParam.name"></a-input>
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
                    <a :data-data="record" @click="handleAdd('edit', record)">修改</a>
                    <a-divider type="vertical" />
                    <a-popconfirm title="确定删除吗?" @confirm="() => onDeleteChange(record.id)"
                      v-if="record.problemStatus!='2' && record.problemStatus!='3'">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </a-card>
        <a-drawer :title="drawerTitle" :width="1000" @close="onCloseChange" :visible="visible"
          :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="14">
                        <a-col :span="10">
                            <a-form-item label="区域名称">
                                <a-input v-model="name" placeholder="请输入区域名称"></a-input>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="10">
                            <a-form-item label="区域编号">
                                <a-input v-model="code" placeholder="请输入区域编号"></a-input>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="14">
                        <a-col :span="10">
                            <a-form-item label="区域描述">
                                <a-textarea :rows="4" v-model="description" placeholder="请输入区域描述"/>
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
                <div id="polygonArea-allmap"></div>
            </a-spin>
            <div class="btns">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" @click="addSubmit" type="primary">提交</a-button>
            </div>
        </a-drawer>
    </div>
</template>

<script>
    import index from "@assets/js/planManagement/polygonArea/index.js";
    export default {
        ...index
    }
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/planManagement/polygonArea/index.less";
</style>