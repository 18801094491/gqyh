import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import AddModal from '@views/workOrderManagement/workPlan/InspectionPlanManagement/modules/AddModal.vue'
import {
    getWaterNumberList,
    getEquipmentSectionList
} from '@api/workOrderManagement/workPlan/InspectionPlanManagement/index.js'
export default {
    name: "list",
    mixins: [JeecgListMixin],
    components: { AddModal },
    data() {
        return {
            tplList: [],
            inPointCategoryList: [],
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
                    title: '计划名称',
                    align: "left",
                    dataIndex: 'planName'
                },
                {
                    title: '线路名称',
                    align: "left",
                    dataIndex: 'routeName'
                },
                {
                    title: '开始日期',
                    align: "left",
                    dataIndex: 'startDate'
                },
                {
                    title: '截止日期',
                    align: "left",
                    dataIndex: 'endDate'
                },
                {
                    title: '计划状态',
                    align: "left",
                    dataIndex: 'planStatusName'
                },
                {
                    title: '数据模板',
                    align: "left",
                    dataIndex: 'tplName'
                },
                {
                    title: '创建人',
                    align: "left",
                    dataIndex: 'usersName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                }
            ],
            url: {
                list: "/work/workInspectionPlan"
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
    mounted(){
        getWaterNumberList(this);
        getEquipmentSectionList(this);
    },
    methods: {
        details(data){
            this.$refs.modalForm.edit(data,'details');
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = false;
        }
    }
}