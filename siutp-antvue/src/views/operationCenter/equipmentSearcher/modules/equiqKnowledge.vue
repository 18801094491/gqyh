<!-- 资产中心-资产台账-详情-设备知识-->
<template>
    <div class="table-page-search-wrapper">
    	<!--<a-card :bordered="false">-->
	       	<a-table
	            ref="table"
	            bordered
	            size="middle"
	            rowKey="id"
	            :columns="columns"
	            :dataSource="dataSource"
	            :pagination="false"
	            :loading="loading"
	            :scroll="{ x: 780 }">
		        <span slot="action" slot-scope="text, record, index">
		        <a @click="getDetail(record)">详情</a>
		      </span>
		    </a-table>

	    <a-drawer
	        :title="detailsTitle+'详情'" :width="800" @close="detailsClose" :visible="detailsvisible"
	    >   
	        <div class="rules"  v-for="(categoryItem,categoryIndex) in knowlegeItemVoList" :key="categoryIndex">
	            <h3>规程{{categoryIndex+1}}</h3>
	            <div class="detailsBox clearfix">
	                <div class="detailsleftBox">
	                    <h4>{{detailsTitle}}维护章程</h4>
	                    <div v-for="(operationRulesItem,operationRulesIndex) in categoryItem.knowlegeOperationList" :key="operationRulesIndex">
	                        <p>{{operationRulesIndex+1}}、{{operationRulesItem.operationItem}}</p>
	                    </div>
	                </div>
	                <div class="detailsrightBox">
	                    <h4>{{detailsTitle}}维护安全事项</h4>
	                    <div v-for="(safetyPrecautionsItem,safetyPrecautionsIndex) in categoryItem.knowlegeCautionList" :key="safetyPrecautionsIndex">
	                        <p>{{safetyPrecautionsIndex+1}}、{{safetyPrecautionsItem.cautionItem}}</p>
	                    </div>
	                </div>
	            </div>
	            <div class="detailsManualBox">
	                <h4>手册：</h4>
	                <!-- <p>安全手册（点击可查看），无则显示暂无</p> -->
	                <div class="detailsManualNo" v-if="!categoryItem.knowlegeAttachList.length">
	                    <p>暂无数据</p>  
	                </div>
	                <div class="detailsManual" v-if="categoryItem.knowlegeAttachList.length" v-for="(fjItem,fjIndex) in categoryItem.knowlegeAttachList" :key="fjIndex">
	                    <a :href="fjItem.attachFile" target="_blank">{{fjItem.fileName}}</a>
	                </div>
	            </div>
	        </div>
	        <div
	            :style="{
	                position: 'absolute',
	                left: 0,
	                bottom: 0,
	                width: '100%',
	                borderTop: '1px solid #e9e9e9',
	                padding: '10px 16px',
	                background: '#fff',
	                textAlign: 'right',
	            }"
	        >
	        
	            <a-button :style="{marginRight: '8px'}" @click="detailsClose">
	                关闭
	            </a-button>
	
	        </div>
	    </a-drawer>
    </div>
</template>
<script>
	
    import equiqKnowledge from '@/assets/js/operationCenter/equipmentSearcher/modules/equiqKnowledge.js';
    export default{
        ...equiqKnowledge
    	
    }
</script>

<style scoped>
	@import '~@assets/less/operationCenter/equipmentSearcher/modules/equiqKnowledge.less'
</style>