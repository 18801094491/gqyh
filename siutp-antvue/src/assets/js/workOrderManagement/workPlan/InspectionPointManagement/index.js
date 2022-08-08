import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import AddModal from '@views/workOrderManagement/workPlan/InspectionPointManagement/modules/AddModal.vue'
import {
    getIPointCategoryList,
    getAllTplList,
    delPointById
} from '@api/workOrderManagement/workPlan/InspectionPointManagement/index.js'
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
                    title: '巡检点名称',
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
                    title: '巡检点类别',
                    align: "left",
                    dataIndex: 'categoryName'
                },
                {
                    title: '数据模板',
                    align: "left",
                    dataIndex: 'tplName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                }
            ],
            url: {
                list: "/work/workInspectionPoint"
            }
        }
    },
    mounted(){
        getIPointCategoryList(this);
        getAllTplList(this);
    },
    methods: {
        details(data){
            this.$refs.modalForm.edit(data,'details');
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = false;
        },
        deletePoint (id) {
            delPointById(id, this)
        }
    }
}