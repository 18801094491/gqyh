import {
    getContractDetail
} from '@api/measurementCenter-t/settlementManage.js'
export default {
    name: "contractDetail",
    props: ['contractDetailVisble', 'contractDetailRecord'],
    data() {
        return {
            title: "详情",
            model: {},
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },

            confirmLoading: false,
            form: this.$form.createForm(this),
            fileList: [],
            copyObj: {},
            modalVisible: false,
        }
    },
    watch: {
        contractDetailVisble(val) {
            this.modalVisible = val;
        },
        contractDetailRecord(val) {
            let data = { id: val.contractId }
            getContractDetail(data, this)
        }
    },
    mounted() {
    },
    methods: {
        closeContractDetail() {
            this.$emit('closeContractDetail', 'contractDetailVisble')
        },
        reset() {

        },
    }
}