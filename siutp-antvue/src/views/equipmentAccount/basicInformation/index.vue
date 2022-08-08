<template>
<!-- 资产中心-资产管理-资产录入 -->
    <div class="margin12">
        <div class="screenCommonBox">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :span="8">
                            <a-form-item label="供应商">
                                 <a-select
                                    showSearch
                                    placeholder="请选择供应商"
                                    optionFilterProp="children"
                                    style="width: 100%"
                                    v-model="queryParam.equipmentSupplier"
                                    >
                                    <a-select-option value="" >全部</a-select-option>
                                    <a-select-option :value="item.id" v-for="(item,index) in supplierClassificationList" :key="index">{{item.supplierName}}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                         <a-col :span="8">
                            <a-form-item label="设备类型">
                                <a-select
                                    showSearch
                                    placeholder="请选择设备类型"
                                    optionFilterProp="children"
                                    style="width: 100%"
                                    v-model="queryParam.equipmentType"
                                    @change="val => changeType(val)"
                                    >
                                    <a-select-option value="" >全部</a-select-option>
                                    <a-select-option :value="item.code" v-for="(item,index) in equipmentTypeList" :key="index">{{item.title}}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                         <a-col :span="8">
                            <a-form-item label="设备型号">
                                 <a-select
                                    showSearch
                                    placeholder="请选择设备型号"
                                    optionFilterProp="children"
                                    style="width: 100%"
                                    v-model="queryParam.equipmentModel"
                                    @change="val => changeMondel(val)"
                                    >
                                    <a-select-option v-for="(item,index) in equipmentModelList" :key="index" :value="item.code">{{item.title}}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                         <a-col :span="8">
                            <a-form-item label="设备规格">
                                 <a-select
                                    showSearch
                                    placeholder="请选择设备规格"
                                    optionFilterProp="children"
                                    style="width: 100%"
                                    v-model="queryParam.equipmentSpecs"
                                    @change="changeSpecs"
                                    >
                                    <a-select-option v-for="(item,index) in equipmentSpecsList" :key="index" :value="item.code">{{item.title}}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :span="16">
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
                <a-button @click="handleAdd" type="primary" icon="plus" >新增</a-button>
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
                    :scroll="{ x: 'max-content' }"
                    :loading="loading"
                    @change="handleTableChange">
                    <span slot="action" slot-scope="text, record">
                        <a @click="handleEdit(record)" >编辑</a>
                        <a-divider type="vertical" />
                        <a @click="deleteRecord(record)">删除</a>
                        <a-divider type="vertical" />
                        <a @click="openAasetAddModal(record)">添加设备</a>
                        <a-divider type="vertical" />
                        <a @click="BOMList(record)">BOM清单</a>

                        <!--<a-divider type="vertical" v-has="'contract:edit'"/>
                        <a>添加养护计划</a>-->
                    </span>
                </a-table>
            </div>
            <!-- table区域-end -->
        </a-card>
        <!-- 添加基本信息 -->
        <add-modal ref="modalForm" 
            @ok="modalFormOk" 
            :supplierClassificationList="supplierClassificationList"
            :equipmentTypeList="equipmentTypeList"
            @changeType="changeType"
            :add_equipmentModelList="add_equipmentModelList"
            @changeMondel="changeMondel"
            :add_equipmentSpecsList="add_equipmentSpecsList"
        ></add-modal>
        <!-- 添加设备 -->
        <asset-info-add-modal ref="assetInfoAddModal" 
            @assetOk="assetOk" 
        ></asset-info-add-modal>
        
    	<bom-list-modal
                    :BOMListVisible="BOMListVisible" :BOMListRecord="BOMListRecord"
                    @closeBOMListModal="closeBOMListModal"
            ></bom-list-modal>
            
        <a-modal title="确认删除？" okText="确定" cancelText="取消" :visible="delVisible" @cancel="delCancel" @ok="delOk">
        	删除基本信息会删除该类型的全部设备!
		    <template slot="footer">
		      	<a-button type="primary" @click="delOk">确定</a-button>
		      	<a-button @click="delCancel">取消</a-button>
		    </template>
  		</a-modal>
    </div>
</template>

<script>
	import index from '@/assets/js/equipmentAccount/basicInformation/index.js';
	export default{
	    ...index
	}

</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>