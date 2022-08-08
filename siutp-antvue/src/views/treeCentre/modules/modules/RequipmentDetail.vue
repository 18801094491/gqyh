<!-- 资产中心-资产台账-详情-设备台账详情tabs -->
<template>
    <a-drawer
        :title="title"
        :width="800"
        @close="closeEquipmentDetail"
        :visible="modalVisible"
        :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}">
        <base-information-detail :item="model" :searchType='searchType'></base-information-detail>
        <div  class="setStyle">
            <a-button :style="{marginRight: '8px'}" @click="closeEquipmentDetail">关闭</a-button>
        </div>
    </a-drawer>
</template>

<script>
import requipmentDetail from '@/assets/js/treeCentre/modules/modules/RequipmentDetail.js';
export default{
    ...requipmentDetail
}
</script>

<style scoped>
.setStyle {
	position: absolute;
	left: 0;
	bottom: 0;
	width: 100%;
	border-top: 1px solid #e9e9e9;
	padding: 10px 16px;
	background: #fff;
	text-align: right;
}
</style>
