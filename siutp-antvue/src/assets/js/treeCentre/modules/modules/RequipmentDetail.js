import BaseInformationDetail from "@/views/treeCentre/modules/modules/BaseInformationDetail";
export default {
    components: {BaseInformationDetail},
    props:['searchType'],
    data() {
        return {
            title: "基本信息详情",
            model: {},
            modalVisible: false,
            detailTabsActiveKey: "00",
            detailTabs:[
                {tab:"基本信息详情", key:"00"},
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