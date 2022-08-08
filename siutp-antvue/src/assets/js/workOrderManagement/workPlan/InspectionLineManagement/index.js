import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import AddModal from '@views/workOrderManagement/workPlan/InspectionLineManagement/modules/AddModal.vue'
import {delLineById} from '@api/workOrderManagement/workPlan/InspectionLineManagement/index.js'

export default {
    name: "list",
    mixins: [JeecgListMixin],
    components: {AddModal},
    data() {
        return {
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
                    title: '线路名称',
                    align: "left",
                    dataIndex: 'name'
                },
                {
                    title: '注意事项',
                    align: "left",
                    dataIndex: 'attention'
                },
                {
                    title: '备注',
                    align: "left",
                    dataIndex: 'remark'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                }
            ],
            url: {
                list: "/work/route"
            }
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
        deleteLine(id) {
            delLineById(id, this)
        }
    }
}