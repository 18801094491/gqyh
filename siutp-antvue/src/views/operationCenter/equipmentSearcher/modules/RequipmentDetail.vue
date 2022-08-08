<!-- 资产中心-资产台账-详情-设备台账详情tabs -->
<template>
    <a-drawer
            :title="title"
            :width="800"
            @close="closeEquipmentDetail"
            :visible="modalVisible"
            :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
        <a-tabs :activeKey="detailTabsActiveKey" @change="displayForTypeCallback">
            <a-tab-pane :tab="item.tab" :key="item.key" v-for="(item) in detailTabs"></a-tab-pane>
        </a-tabs>
        <base-information-detail :item="model" v-if="detailTabsActiveKey === '00'"></base-information-detail>
        <equiq-knowledge :item="model" v-if="detailTabsActiveKey === '01'"></equiq-knowledge>

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
      }">
            <a-button :style="{marginRight: '8px'}" @click="closeEquipmentDetail">
                关闭
            </a-button>
        </div>
    </a-drawer>

</template>

<script>

	import requipmentDetail from '@/assets/js/operationCenter/equipmentSearcher/modules/RequipmentDetail.js';
	export default{
	    ...requipmentDetail
		
	}

</script>

