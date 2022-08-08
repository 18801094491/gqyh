<template>
    <div id="equipmentAccountMaintenanceBox" class="margin12">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="所属标段">
                                <a-select v-model="queryParam.optSection" placeholder="请选择所属标段">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option :value="item.code" v-for="(item,index) in bidSegmentList"
                                        :key="index">{{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="设备类型">
                                <a-select v-model="queryParam.equipmentType" placeholder="请选择设备类型">
                                    <a-select-option :value="item.code" v-for="(item,index) in modelTypeList2"
                                        :key="index">{{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="放置位置">
                                <a-input placeholder="请输入放置位置" v-model="queryParam.optLocation"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="设备状态">
                                <a-select v-model="queryParam.equipmentRevstop" placeholder="请选择设备状态">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option value="1">启用</a-select-option>
                                    <a-select-option value="0">停用</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="设备编号">
                                <a-input placeholder="请输入设备编号" v-model="queryParam.equipmentSn"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8">
                            <a-form-item label="供应商">
                                <a-select v-model="queryParam.equipmentSupplier" placeholder="请选择供应商">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option :value="item.id"
                                        v-for="(item,index) in supplierClassificationList2" :key="index">
                                        {{item.supplierName}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="8" :offset="6">
                            <span class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                <a-button class="ant-btn-border ml8" @click="searchReset" icon="reload">重置</a-button>
                            </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">
            <!-- 表格 -->
            <a-table
                ref="table"
                bordered
                size="middle"
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :scroll="{ x: 1800 }"
                @change="handleTableChange">
                <span slot="maintainBtn" slot-scope="text, record">
                    <a v-if="record.bindStatus == 1" @click="bindEvent(record.id)">绑定属性</a>
                    <a-popconfirm v-else title="确定解除绑定吗?" @confirm="() => handleDeletes(record.id)"><a>解绑</a></a-popconfirm>
                </span>
            </a-table>
        </a-card>
        <a-modal
            :title="title"
            :width="width"
            :visible="visible"
            :confirmLoading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            :destroyOnClose="true"
            cancelText="关闭">
            <a-spin :spinning="confirmLoading">
                <a-form :form="form">
                    <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                        <j-tree-region-select
                            :url="parentUrl"
                            ref="treeSelect"
                            :disabled="disTree"
                            placeholder="请选择父级节点"
                            v-decorator="['parentId', validatorRules.parentId]"
                            dict="opt_label,label_name,id"
                            pidField="parent_id"
                            pidValue="0"
                            condition="{&quot;del_flag&quot;:0}">
                        </j-tree-region-select>
                    </a-form-item>
                </a-form>
            </a-spin>
        </a-modal>
    </div>
</template>
<script>
import index from '@/assets/js/treeCentre/operationCenterTree/treeManage.js'
import '@/assets/less/operationCenter/equipmentAccountMaintenance/style.less'
export default {
    ...index
}
</script>
<style scoped>
    @import '~@assets/less/common.less';
</style>
<style scoped>
    .fileImgBox {
        margin-left: 0 !important;
    }
</style>