import RoleModal from "@views/system/modules/RoleModal.vue";
import UserRoleModal from "@views/system/modules/UserRoleModal";
import {JeecgListMixin} from "@/mixins/JeecgListMixin";
import JDate from "@/components/jeecg/JDate";
import {roleStartOrStop} from '@api/system-t/RoleList.js'

export default {
    name: "RoleList",
    mixins: [JeecgListMixin],
    components: {
        RoleModal,
        UserRoleModal,
        JDate
    },
    data() {
        return {
            description: "角色管理页面",
            // 查询条件
            queryParam: {roleName: ""},
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
                    title: "角色名称",
                    align: "center",
                    dataIndex: "roleName"
                },
                {
                    title: "角色编码",
                    align: "center",
                    dataIndex: "roleCode"
                },
                {
                    title: "备注",
                    align: "center",
                    dataIndex: "description"
                },
                {
                    title: "创建时间",
                    dataIndex: "createTime",
                    align: "center",
                    sorter: true
                },
                {
                    title: "更新时间",
                    dataIndex: "updateTime",
                    align: "center",
                    sorter: true
                },
                {
                    title: "状态",
                    dataIndex: "status",
                    align: "center",
                    scopedSlots: {customRender: "status"}
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: {customRender: "action"}
                }
            ],
            url: {
                list: "/sys/role/list",
                delete: "/sys/role/delete",
                deleteBatch: "/sys/role/deleteBatch",
                exportXlsUrl: "/sys/role/exportXls",
                importExcelUrl: "sys/role/importExcel"
            }
        };
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    methods: {
        handlePerssion: function (roleId) {
            // alert(roleId);
            this.$refs.modalUserRole.show(roleId);
        },
        onChangeDate(date, dateString) {
            console.log(date, dateString);
        },
        changeStatus(res, e) {
            console.log(res);
            console.log(e);
            let data = {
                id: res.id,
                status: e ? '1' : '0'
            }
            roleStartOrStop(data, this);
        }
    }
};
//system/RoleList.vue 页面组件混入了 system/RoleList.js