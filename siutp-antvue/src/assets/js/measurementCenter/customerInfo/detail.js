import {
    getContractDetail
} from '@api/measurementCenter-t/customerInfo.js'
export default {
    name: "contractDetail",
    data() {
        return {
            title: "合同详情",
            model: {},
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            fileList: [],
            modalVisible: false,
        }
    },
    methods: {
        edit(record, type) {
            getContractDetail({
                id: record.contractId
            }, this)
        },
        closeContractDetail() {
            this.$emit('close');
            this.modalVisible = false;
        }
    }
}
//datail 页面组件混入了detail.js