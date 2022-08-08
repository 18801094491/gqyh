  export default {
        name: "BaseInformationDetail",
        props: {
            item: Object,
            searchType:String,
        },
        data() {
            return {
                title: "基本信息详情",
                form: this.$form.createForm(this),
            }
        },
    }