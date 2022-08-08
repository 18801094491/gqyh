<template>
<!-- 资产中心-资产管理-资产录入-bom清单 -->
	<div>
		<a-drawer
		:title="stateRuleTitle"
		:width="900"
		@close="closeBOMListModal"
		:visible="modalVisible"
		:wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
		>
		<div class="table-page-search-wrapper">
			<a-form layout="inline">
				<a-row :gutter="24">
					<a-col :span="10">
						<a-form-item label="备品备件">
							<a-input placeholder="请输入备品备件" v-model="queryParam.sparepartsName"></a-input>
						</a-form-item>
					</a-col>
					<a-col :span="10">
						<a-form-item label="备品备件型号">
							<a-input placeholder="请输入备品备件型号" v-model="queryParam.sparepartsSpcesName"></a-input>
						</a-form-item>
					</a-col>
				</a-row>
				<a-row :gutter="24">
					<a-col :span="8">
						<a-form-item label="类型">
							<a-select v-model="queryParam.bomType" placeholder="请选择类型">
								<a-select-option value="">全部</a-select-option>
								<a-select-option :value="item.code" v-for="(item,index) in bidSegmentList" :key="index">{{item.title}}</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="10">
						<a-form-item label="备品备件规格">
							<a-input placeholder="请输入备品备件规格" v-model="queryParam.sparepartsModelName"></a-input>
						</a-form-item>
					</a-col>
					<a-col :span="6">
						<span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
			                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button> 
			                <a-button class="ant-btn-border" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
			            </span>
					</a-col>
				</a-row>
			</a-form>
		</div>
			<a-card :bordered="false">
				<div class="table-operator" style="border-top: 5px">
					<a-button @click="handleAdd('add')" type="primary" icon="plus" style="margin-right:6px;">新增</a-button>
				</div>
				
				<a-table ref="table" size="middle" bordered row-key="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" :scroll="{ x: 'max-content' }">
					<span slot="action" slot-scope="text, record">
	            <a :data-data="record" @click="handleAdd('change',record)">修改</a>
	          </span>
				</a-table>
			</a-card>
		</a-drawer>
		<bom-add-modal
			:openAddVisible="openAddVisible" :drawerAddTitle="drawerAddTitle" :basicId="basicId" :BOMAddId="BOMAddId" :BOMAddRecord="BOMAddRecord"
			@closeModal="closeAddModal" ref="BOMAddRef"
		></bom-add-modal>
	</div>
</template>
<script>
	import bomListModal from '@/assets/js/equipmentAccount/basicInformation/modules/BOMListModal.js';
	export default{
	    ...bomListModal
	}
</script>

