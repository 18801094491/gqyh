import WatermeterAddModal from '@views/measurementCenter/measurementBasedataInput/contractManage/modules/watermeterAddModal'
import { waterbindList, customerWaterUnbind } from '@api/measurementCenter-t/measurementBasedataInput.js'

export default {
    name: "watermeterModal",
    props: ['watermeterVisible', 'watermeterRecord'],
    components: {
        WatermeterAddModal
    },
    data() {
        return {
            stateRuleTitle: '水表绑定',
            modalVisible: false,
            openAddVisible: false,
            dataSource: [],
            pagination: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
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
                    title: '水表名称',
                    align: "center",
                    width: 200,
                    dataIndex: 'equipmentName',

                },
                {
                    title: '水表编号',
                    align: "center",
                    width: 100,
                    dataIndex: 'equipmentSn',

                },
                {
                    title: '计价规则',
                    align: "center",
                    width: 100,
                    dataIndex: 'ruleName',

                },
                {
                    title: '操作',
                    align: "center",
                    width: 100,
                    dataIndex: 'action',
                    scopedSlots: {
                        customRender: 'action'
                    },

                }
            ],
            loading: false,
            itemRecord: null,
            contractId: '', // 合同id
            customerId: '', // 客户id
            unbindVisible: false, // 解除绑定
            confirmLoading: false,
            bindRecord: {},
        }
    },
    watch: {
        watermeterVisible(val) {
            this.modalVisible = val
        },
        watermeterRecord: {
            handler: function (newVal, oldVal) {
                this.itemRecord = newVal;
                this.upData(this.itemRecord.id);
                this.contractId = newVal.id
                this.customerId = newVal.customerId
            },
            deep: true,
        }
    },
    methods: {
        closeWatermeterModal() {
            this.$emit('closeWatermeterModal', 'watermeterVisible')
        },
        closeAddModal(modalVisible) {
            this[modalVisible] = false
        },
        watermeterAdd() {
            this.openAddVisible = true;
            this.$refs.watermeterAddRef.getList();
        },
        // 获取列表信息
        upData(val) {
            let data = {
                contractId: this.watermeterRecord.id,
                pageNo: this.pagination.current,
                pageSize: this.pagination.pageSize
            };
            waterbindList(data, this)
            this.$forceUpdate()
        },
        handleBind(record) {
            this.unbindVisible = true;
            this.bindRecord = record;
        },
        handleUnbind() { // 解除绑定
            this.loading = true;
            customerWaterUnbind({ id: this.bindRecord.id }, this)
        },
        handleCancelBind() {
            this.unbindVisible = false;
        }
    }
}
//contractManage/watermeterListModal.vue 页面组件混入了 contractManage/meterlist.js