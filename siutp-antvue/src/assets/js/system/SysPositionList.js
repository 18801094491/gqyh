import SysPositionModal from "@views/system/modules/SysPositionModal.vue";
import {JeecgListMixin} from "@/mixins/JeecgListMixin";
import JDictSelectTag from "@/components/dict/JDictSelectTag";
import {positionStartOrStop} from '@api/system-t/SysPositionList.js'

export default {
    name: "SysPositionList",
    mixins: [JeecgListMixin],
    components: {
        SysPositionModal,
        JDictSelectTag
    },
    data() {
        return {
            description: "职务表管理页面",
            // 表头
            columns: [
                {
                    title: "#",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "职务编码",
                    align: "center",
                    dataIndex: "code"
                },
                {
                    title: "职务名称",
                    align: "center",
                    dataIndex: "name"
                },
                {
                    title: "职级",
                    align: "center",
                    dataIndex: "rank_dictText"
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "statusCode",
                    scopedSlots: {customRender: "statusCode"}
                },
                // {
                //   title: '公司id',
                //   align: 'center',
                //   dataIndex: 'companyId'
                // },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: {customRender: "action"}
                }
            ],
            url: {
                list: "/sys/position/list",
                delete: "/sys/position/delete",
                deleteBatch: "/sys/position/deleteBatch",
                exportXlsUrl: "/sys/position/exportXls",
                importExcelUrl: "/sys/position/importExcel"
            }
        };
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        statusCodeChange(res, e) {
            console.log(res);
            console.log(e);
            let data = {
                id: res.id,
                status: e ? '1' : '0'
            }
            positionStartOrStop(data, this);
        }
    }
};