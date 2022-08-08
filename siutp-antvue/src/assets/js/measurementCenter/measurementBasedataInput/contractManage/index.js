import ContractModal from '@views/measurementCenter/measurementBasedataInput/contractManage/modules/ContractModal'
import PricingRulesListModal from '@views/measurementCenter/measurementBasedataInput/contractManage/modules/PricingRulesListModal'
import WatermeterListModal from '@views/measurementCenter/measurementBasedataInput/contractManage/modules/watermeterListModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { expireDateConfig, configByKey } from '@api/measurementCenter-t/measurementBasedataInput.js'

export default {
    name: "ContractList",
    mixins: [JeecgListMixin],
    components: {
        ContractModal,
        PricingRulesListModal,
        WatermeterListModal,
    },
    data() {
        return {
            selectRow: {},
            pricingRulesVisible: false,
            description: '合同管理',
            // 表头
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },

                {
                    title: '合同编号',
                    align: "center",
                    dataIndex: 'contractSn'
                },
                {
                    title: '合同名称',
                    align: "left",
                    dataIndex: 'contractName',
                },
                {
                    title: '客户名称',
                    align: "left",
                    dataIndex: 'constomerName',
                },
                {
                    title: '生效日期',
                    align: "center",
                    dataIndex: 'startDate'
                },
                {
                    title: '结束日期',
                    align: "center",
                    dataIndex: 'endDate'
                },
                {
                    title: '用途',
                    align: "center",
                    dataIndex: 'contractUse'
                },
                {
                    title: '倒计时（天）',
                    align: "center",
                    dataIndex: 'countdownDays'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: { customRender: 'action' },
                }
            ],

            url: {
                list: "/settle/contract/list",
                delete: "/settle/contract/delete",
                deleteBatch: "/settle/contract/deleteBatch",
                exportXlsUrl: "settle/contract/exportXls",
                importExcelUrl: "settle/contract/importExcel",
            },
            watermeterVisible: false,
            watermeterRecord: {},
            expireDateVisible: false,
            confirmLoading: false,
            form: {
                setExpireDate: null,
            }
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    mounted() {
    },
    methods: {
        details(data) {
            this.$refs.modalForm.edit(data, 'details');
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = false;
        },
        pricingRules(record) {
            this.$refs.rulesListModal.getList(record);
            // this.pricingRulesVisible = true
        },
        closeModal(modalVisible) {
            this[modalVisible] = false
        },
        watermeter(record) {
            this.watermeterVisible = true;
            //              this.watermeterRecord = record;
            //              this.$set(this.watermeterRecord,{},record)
            this.watermeterRecord = Object.assign({}, this.watermeterRecord, record)
        },
        closeWatermeterModal(modalVisible) {
            this[modalVisible] = false
        },
        expireDate() { // 合同到期天数设置
            this.expireDateVisible = true;
            let data = 'contract.remind.remaining.days';
            configByKey(data, this);
        },
        closeExpireDate() {
            this.expireDateVisible = false;
        },
        submitExpireDate() {
            let data = {
                configValue: this.form.setExpireDate,
                configKey: this.form.configKey ? this.form.configKey : 'contract.remind.remaining.days',
                id: this.form.id ? this.form.id : '',
            }
            expireDateConfig(data, this)
        },
        handleInput(e) {
            let a = e.key.replace(/[^\d]/g, "");
            if (!a) {
                e.preventDefault();
            }
        }
    }
}
//contractManage/index.vue 页面组件混入了 contractManage/index.js