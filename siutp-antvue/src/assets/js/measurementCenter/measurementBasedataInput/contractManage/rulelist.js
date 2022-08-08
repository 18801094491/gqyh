import PricingRulesAddModal from '@views/measurementCenter/measurementBasedataInput/contractManage/modules/PricingRulesAddModal'
import { getPrivingRulesList } from '@api/measurementCenter-t/measurementBasedataInput.js'
export default {
    name: "PricingRulesModal",
    components: {
        PricingRulesAddModal
    },
    data() {
        return {
            selectRow: [],
            stateRuleTitle: '计价规则',
            modalVisible: false,
            openAddVisible: false,
            dataSource: [],
            pagination: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
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
                    title: '合同名称',
                    align: "center",
                    width: 200,
                    dataIndex: 'contractName'
                },
                {
                    title: '计价规则名称',
                    align: "center",
                    width: 200,
                    dataIndex: 'ruleName'
                },
                {
                    title: '规则类型',
                    align: "center",
                    width: 180,
                    dataIndex: 'ruleTypeName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    width: 150,
                    scopedSlots: { customRender: 'action' },
                }
            ],
            loading: false,
            isShow: false,
            rulesRow: null
        }
    },
    methods: {
        getList(record) {
            this.selectRow = record
            this.getPrivingRulesListPage()
            this.modalVisible = true
        },
        getPrivingRulesListPage() {
            getPrivingRulesList(this, {
                contractId: this.selectRow.id,
                pageNo: this.pagination.current,
                pageSize: this.pagination.pageSize
            })
        },
        closeModal() {
            this.modalVisible = false;
        },
        closeAddModal(modalVisible) {
            this[modalVisible] = false
        },
        pricingRulesAdd() {
            this.$refs.rulesAddForm.openModal(this.selectRow, {});
            this.$refs.rulesAddForm.title = "新增计价规则";
            this.$refs.rulesAddForm.isShow = false
        },
        submitSuccess(selectRow) {
            this.getList(selectRow)
        },
        details(data, type) {
            this.$refs.rulesAddForm.openModal(this.selectRow, data, type);
            if (type === 'show') {
                this.$refs.rulesAddForm.title = "查看计价规则";
                this.$refs.rulesAddForm.isShow = true
            } else {
                this.$refs.rulesAddForm.title = "编辑计价规则";
                this.$refs.rulesAddForm.isShow = false
            }
        },
        tableChange(pagination) {
            this.pagination.current = pagination.current;
            this.getPrivingRulesListPage()
        }
    }
}
//contractManage/PricingRulesListModal.vue 页面组件混入了 contractManage/rulelist.js