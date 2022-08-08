    import BaseInformationDetail from "@/views/operationCenter/equipmentSearcher/modules/BaseInformationDetail";
    import EquiqKnowledge from "@/views/operationCenter/equipmentSearcher/modules/equiqKnowledge";
	export default {
        name: "EquipmentDetail",
        components: {BaseInformationDetail, EquiqKnowledge
        },
        data() {
            return {
                title: "设备台账详情",
                model: {},
                modalVisible: false,
                detailTabsActiveKey: "00",
                detailTabs:[
                    {tab:"基本信息详情", key:"00"},
                    {tab:"设备知识", key:"01"},
                    {tab:"重置记录", key:"02"},
                    {tab:"维修记录", key:"03"},
                    {tab:"养护记录", key:"04"},
                ],
            }
        },
        methods: {
            closeEquipmentDetail() {
                this.$emit('close');
                this.modalVisible = false;
            },
            displayForTypeCallback(key) {
                this.detailTabsActiveKey = key;
            },
            openEquipmentDetail(record){
                this.modalVisible = true;
                this.model = record;
                this.detailTabsActiveKey = "00";
            }
        },
    }
    //equipmentSearcher/modules/RequipmentDetail.vue 页面混入了equipmentSearcher/modules/RequipmentDetail.js