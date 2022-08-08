<!-- 计量中心设备树-设备树管理-客户信息 -->
<template>
    <div id="customerManagement" class="margin12">
        <div class="screenCommonBox">
            <!-- 查询 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline">
                    <a-row :gutter="24">
                        <a-col :md="8" :sm="12">
                            <a-form-item label="客户编码">
                                <a-input placeholder="请输入用户编码" v-model="queryParam.customerSn"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="8" :sm="12">
                            <a-form-item label="客户名称">
                                <a-input placeholder="请输入用户名称" v-model="queryParam.customerName"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="8" :sm="12">
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
        <!-- 绑定 -->
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
    import index from "@assets/js/measurementCenter/measurementBasedataInput/customerInfoSetting";
    import index1 from '@/assets/js/treeCentre/measurementCenterTree/treeManage.js'
    export default {
        ...index,
        ...index1
    };
</script>

