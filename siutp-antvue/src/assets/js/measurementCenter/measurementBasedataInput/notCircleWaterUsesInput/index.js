import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import AddModal from '@views/measurementCenter/measurementBasedataInput/notCircleWaterUsesInput/AddModal'
import {
    getWaterNumberList,
    getEquipmentSectionList
} from '@api/measurementCenter-t/notCircleWaterUsesInput.js'
export default {
    name: "nRemoteMeterReadManagement",
    mixins: [JeecgListMixin],
    components: { AddModal },
    data() {
        return {
            waterNumberList: [],
            equipmentSectionList: [],
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
                    title: '水表信息',
                    align: "left",
                    dataIndex: 'equipmentInfo'
                },
                {
                    title: '客户名称',
                    align: "left",
                    dataIndex: 'customerName'
                },
                {
                    title: '合同名称',
                    align: "left",
                    dataIndex: 'contractName'
                },
                {
                    title: '抄表日期',
                    align: "center",
                    dataIndex: 'currentFlowDate'
                },
                {
                    title: '抄表数（吨）',
                    align: "right",
                    dataIndex: 'currentFlow'
                },
                {
                    title: '水价（元）',
                    align: "right",
                    dataIndex: 'currentWaterPrice'
                },
                {
                    title: '状态',
                    align: "center",
                    dataIndex: 'statusTitle'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: { customRender: 'action' },
                }
            ],
            url: {
                list: "/settle/manual"
            },
            watermeterVisible: false,
            watermeterRecord: null,
            expireDateVisible: false,
            confirmLoading: false,
            form: {
                setExpireDate: null,
            }
        }
    },
    mounted() {
        getWaterNumberList(this);
        getEquipmentSectionList(this);
    },
    methods: {
        details(data) {
            this.$refs.modalForm.edit(data, 'details');
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = false;
        }
    }
}
//notCircleWaterUsesInput/index.vue 页面组件混入了 notCircleWaterUsesInput/index.js