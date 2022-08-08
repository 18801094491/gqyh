<!-- 设备树-结构管理-右侧菜单 -->
<template>
    <div v-if="pageLoading">
        <a-card
            class="j-address-list-right-card-box"
            :loading="cardLoading"
            :bordered="false">
            <div class="screenCommonBox">
                <!-- 查询区域 -->
                <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <template v-if="searchType != 'met'">
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
                            <a-col :md="6" :sm="12" v-if="searchType !== 'saf'">
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
                            <a-col :md="6" :sm="12">
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
                            <a-col :md="12" :sm="12" :offset="searchType === 'saf' ? 6: 0">
                            <span class="table-page-search-submitButtons">
                                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                <a-button class="ant-btn-border ml8" @click="searchReset" icon="reload">重置</a-button>
                            </span>
                            </a-col>
                        </template>
                        <template v-else>
                            <a-row :gutter="24">
                                <a-col :md="9" :sm="12">
                                <a-form-item label="客户编码">
                                    <a-input placeholder="请输入用户编码" v-model="queryParam.customerSn"></a-input>
                                </a-form-item>
                                </a-col>
                                <a-col :md="9" :sm="12">
                                <a-form-item label="客户名称">
                                    <a-input placeholder="请输入用户名称" v-model="queryParam.customerName"></a-input>
                                </a-form-item>
                                </a-col>
                                <a-col :md="6" :sm="12">
                                <span class="table-page-search-submitButtons">
                                    <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                                    <a-button class="ant-btn-border ml8" @click="searchReset" icon="reload">重置</a-button>
                                </span>
                                </a-col>
                            </a-row>
                        </template>
                    </a-row>
                </a-form>
                </div>
            </div>
            <a-divider></a-divider>
            <div class="table-row table-total">
                <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :loading="loading"
                    :pagination="ipagination"
                    :scroll="{ x: 'max-content' }"
                    @change="handleTableChange">
                    <span slot="maintainBtn" slot-scope="text, record">
                        <a @click="equipmentDetail(record)">详情</a>
                    </span>
                </a-table>
            </div>
        </a-card>
        <requipment-detail ref="equipDetail" :searchType='searchType'></requipment-detail>
    </div>
</template>

<script>
import structureQueryRight from "@/assets/js/treeCentre/modules/structureQuery/StructureQueryRight.js";

export default {
    ...structureQueryRight,
};
</script>
<style>
.j-address-list-right-card-box .ant-table-placeholder {
    min-height: 46px;
}
</style>
<style scoped>
@import "~@assets/less/treeCentre/modules/structureQuery/StructureQueryRight.less";
</style>