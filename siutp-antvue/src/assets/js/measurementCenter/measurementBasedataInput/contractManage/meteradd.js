import pick from 'lodash.pick'
import { customerWaterbind, getWaterbindList, getContractRule } from '@api/measurementCenter-t/measurementBasedataInput.js'
export default {
    name: "watermeterAddModal",
    props: ['openAddVisible', 'contractId', 'customerId'],
    data() {
        return {
            title: "水表绑定",
            visible: false,
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            form: this.$form.createForm(this),
            validatorRules: {
            },
            model: {},
            waterbindList: [], // 水表下拉框
            contractRuleList: [], // 计价规则下拉框
        }
    },
    watch: {
        openAddVisible(val) {
            if (val) {
                this.openModal()
            } else {
                this.visible = false
            }
        },
        contractId(val) {

        }
    },
    methods: {
        getList() {
            getWaterbindList({ customerId: this.customerId }, this);
            getContractRule({ contractId: this.contractId }, this);
        },
        closeModal() {
            this.$emit('closeModal', 'openAddVisible')
        },
        openModal(record = {}) {
            this.visible = true;
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.$nextTick(() => {
                this.form.setFieldsValue(pick(this.model, 'equipmentId', 'ruleId'))
            });
        },
        submit() {
            let data = {
                contractId: this.contractId, // 合同id
                equipmentId: this.form.getFieldsValue().equipmentId,  // 水表id
                ruleId: this.form.getFieldsValue().ruleId,  // 计价规则id
            }
            customerWaterbind(data, this)
        }
    }
}
//contractManage/watermeterAddModal.vue 页面组件混入了 contractManage/meteradd.js