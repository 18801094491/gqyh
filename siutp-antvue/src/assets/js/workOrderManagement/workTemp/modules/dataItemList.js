import {deleteAction} from "@/api/manage.js";
import {getDataItemList} from "@api/workOrderManagement/workTemp/index.js";

import {JeecgListMixin} from "@/mixins/JeecgListMixin";
import JEllipsis from "@/components/jeecg/JEllipsis";
import AddDataItemModal from "@views/workOrderManagement/workTemp/modules/AddDataItemModal";

export default {
    name: "ContractModal",
    props: ["jobTypeList", "jobStatusList"],
    mixins: [JeecgListMixin],
    components: {
        AddDataItemModal,
        JEllipsis
    },
    data() {
        return {
            title: "操作",
            visible: false,
            dataTypeList: [
                // 数据类型下拉
                {
                    code: "0",
                    title: "文本"
                },
                {
                    code: "1",
                    title: "多行文本"
                },
                {
                    code: "2",
                    title: "多选"
                },
                {
                    code: "3",
                    title: "单选"
                },
                {
                    code: "4",
                    title: "日期"
                }
            ],
            dataSource: [],
            columns: [
                {
                    title: "数据项名称",
                    dataIndex: "title",
                    key: "title",
                    align: "left"
                },
                {
                    title: "是否录入",
                    dataIndex: "needEnter",
                    align: "center",
                    customRender: function (text) {
                        return text == "0" ? "否" : "是";
                    }
                },
                {
                    title: "数据类型",
                    dataIndex: "dataType",
                    key: "dataType",
                    align: "center",
                    scopedSlots: {customRender: "dataType"}
                },
                {
                    title: "是否必填",
                    dataIndex: "needRequired",
                    align: "center",
                    customRender: function (text) {
                        return text == "0" ? "否" : "是";
                    }
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    scopedSlots: {customRender: "action"},
                    align: "center",
                    width: 150
                }
            ],
            loading: false,
            url: {
                delete: "/work/workJobDatasource"
            },
            selectRow: {}
        };
    },
    methods: {
        getList(record) {
            this.selectRow = record;
            this.loadData(this.selectRow);
            this.visible = true;
        },
        handleEdits(record, type) {
            if (type === "detail") {
                this.$refs.modalForm.title = "查看数据项";
                this.$refs.modalForm.isShow = true;
                this.$refs.modalForm.add(record, type);
            } else if (type === "edit") {
                this.$refs.modalForm.title = "编辑数据项";
                this.$refs.modalForm.isShow = false;
                this.$refs.modalForm.add(record, type);
            } else {
                this.$refs.modalForm.title = "添加子数据项";
                this.$refs.modalForm.add({
                    dataCategory: '1',
                    parentId: record.key
                }, type);
            }
        },
        loadData(record) {
            if (record) {
                getDataItemList(record.id, this);
            }
        },
        // 打开数据规则编辑
        handleDataRule() {
            this.$refs.PermissionDataRuleList.edit();
        },
        handleAddSub(record) {
            this.$refs.modalForm.title = "添加子菜单";
            this.$refs.modalForm.localMenuType = 1;
            this.$refs.modalForm.disableSubmit = false;
            this.$refs.modalForm.edit({
                parentId: record.id
            });
        },
        handleCancel() {
            this.close();
        },
        close() {
            this.$emit("close");
            this.visible = false;
        },
        submitSuccess(selectRow) {
            this.getList(selectRow);
        },
        handleDel(id) {
            let _this = this;
            deleteAction(_this.url.delete + "/" + id, {}).then(res => {
                if (res.code === 200) {
                    _this.$message.success(res.message);
                    _this.loadData(_this.selectRow);
                } else {
                    _this.$message.warning(res.message);
                }
            });
        }
    }
};