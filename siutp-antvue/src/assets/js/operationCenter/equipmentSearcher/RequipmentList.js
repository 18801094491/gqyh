  import RequipmentListLeft from "@/views/operationCenter/equipmentSearcher/RequipmentListLeft"
  import RequipmentListRight from "@/views/operationCenter/equipmentSearcher/RequipmentListRight"
  export default {
        name: "EquipmentListVue",
        components: {RequipmentListLeft, RequipmentListRight},
        data() {
            return {
                description: "设备信息展示页面",
                currentLabelCode: ""
            };
        },

        methods: {}
    };
    //equipmentSearcher/RequipmentList.vue 页面混入了equipmentSearcher/RequipmentList.js