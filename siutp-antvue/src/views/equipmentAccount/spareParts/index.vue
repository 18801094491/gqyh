<!-- 资产中心-备品备件-备品备件基本 -->
<template>
	<div class="margin12">
		<div class="screenCommonBox">
			<!-- 查询区域 -->
			<div class="table-page-search-wrapper">
				<a-form layout="inline" @keyup.enter.native="searchQuery">
					<a-row :gutter="24">
						<a-col :md="6" :sm="8">
							<a-form-item label="备品备件">
								<a-input placeholder="请输入备品备件" v-model="queryParam.sparepartsName"></a-input>
							</a-form-item>
						</a-col>
						<a-col :md="6" :sm="8">
							<a-form-item label="设备型号">
								<a-input placeholder="请输入设备型号" v-model="queryParam.sparepartsSpces"></a-input>
							</a-form-item>
						</a-col>
						<a-col :md="6" :sm="8">
							<a-form-item label="设备规格">
								<a-input placeholder="请输入设备规格" v-model="queryParam.sparepartsModel"></a-input>
							</a-form-item>
						</a-col>
						<a-col :md="6" :sm="8">
							<span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
				                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button> 
				                <a-button class="ant-btn-border" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
				            </span>
						</a-col>
					</a-row>
				</a-form>
			</div>
		</div>
		<a-card :bordered="false">
			<div class="table-operator" style="border-top: 5px">
				<a-button @click="handleAdd('add')" type="primary" icon="plus" style="margin-right:6px;">新增</a-button>
			</div>
			<a-table ref="table" size="middle" bordered row-key="id" :columns="columns" :dataSource="dataSource" :pagination="ipagination" :loading="loading" :scroll="{ x: 'max-content' }" @change="handleTableChange">
				<span slot="action" slot-scope="text, record">
		            <a :data-data="record" @click="handleAdd('change',record)">修改</a>
		        </span>
				
			</a-table>
		</a-card>
		<a-drawer :title="drawerTitle" :width="720" @close="onClose" :visible="visible" :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}">
			<a-form layout="vertical" hideRequiredMark>
				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="名称">
							<a-input v-model="categoryName" placeholder="请输入名称"></a-input>	
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="备品备件">
							<a-input v-model="sparepartsName" placeholder="请输入备品备件"></a-input>	
						</a-form-item>
					</a-col>
				</a-row>
				<a-row :gutter="16">
					<a-col :span="12">
						<a-form-item label="备品备件型号">
							<a-select v-model="sparepartsSpces" placeholder="请选择设备型号">
								<a-select-option :value="item.code" v-for="(item,index) in sparepartsModelList" :key="index">{{item.title}}</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
					<a-col :span="12">
						<a-form-item label="备品备件规格">
							<a-select v-model="sparepartsModel" placeholder="请选择设备规格">
								<a-select-option :value="item.code" v-for="(item,index) in sparepartsSpcesList" :key="index">{{item.title}}</a-select-option>
							</a-select>
						</a-form-item>
					</a-col>
				</a-row>
			</a-form>
			<div :style="{
                    position: 'absolute',
                    left: 0,
                    bottom: 0,
                    width: '100%',
                    borderTop: '1px solid #e9e9e9',
                    padding: '10px 16px',
                    background: '#fff',
                    textAlign: 'right',
                }">

				<a-button :style="{marginRight: '8px'}" @click="onClose">
					关闭
				</a-button>
				<a-button @click="addSubmit" type="primary">提交</a-button>
			</div>
		</a-drawer>
	</div>
</template>

<script>
	import index from '@/assets/js/equipmentAccount/spareParts/index.js'
	export default {
		...index
	}
</script>
<style scoped>
	@import '~@assets/less/common.less'
</style>