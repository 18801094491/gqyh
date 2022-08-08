  export default {
        name: "BaseInformationDetail",
        props: {
            item: Object,
        },
        data() {
            return {
                title: "基本信息详情",
                form: this.$form.createForm(this),
            }
        },
    }
    //equipmentSearcher/modules/BaseInformationDetail.vue 页面混入了equipmentSearcher/modules/BaseInformationDetail.js