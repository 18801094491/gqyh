import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getDataList } from "@api/workOrderManagement/workTemp/index.js";
import { deleteAction } from "@/api/manage.js";

import AddWorkModal from "@views/workOrderManagement/workTemp/modules/AddWorkModal";
import DataItemList from "@views/workOrderManagement/workTemp/modules/DataItemList";

export default {
    name: "workTemp",
    mixins: [JeecgListMixin],
    components: { AddWorkModal, DataItemList },
    data() {
        return {
            columns: [
                {
                    title: "序号",
                    dataIndex: "",
                    key: "rowIndex",
                    width: 60,
                    align: "center",
                    customRender: function(t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: "模板名称",
                    align: "center",
                    dataIndex: "tplName"
                },
                {
                    title: "模板类型",
                    align: "center",
                    dataIndex: "tplTypeName"
                },
                {
                    title: "备注",
                    align: "center",
                    dataIndex: "remark"
                },
                {
                    title: "创建时间",
                    align: "center",
                    dataIndex: "createTime"
                },
                {
                    title: "操作人",
                    dataIndex: "createUser",
                    align: "center"
                },
                {
                    title: "状态",
                    align: "center",
                    dataIndex: "tplStatusName"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "/work/template"
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: "",
            changeId: "", // 修改id
            warnStatusList: [], // 状态下拉
            problemName: "", // 问题名称
            problemType: "", // 问题类型
            equipmentId: "", // 问题设备信息
            problemDescription: "", // 问题描述
            problemTypeList: [], // 问题类型下拉
            equipmentList: [], // 问题设备信息下拉
            workerVisible: false,
            teamInformationList: [], // 班组下拉
            workTeamId: "",
            jobTypeList: [], // 工单类型下拉
            jobStatusList: [], // 工单状态下拉

            supplierClassificationList: [],
            equipmentTypeList: [],
            equipmentModelList: [],
            equipmentSpecsList: [],
            add_equipmentModelList: [],
            add_equipmentSpecsList: [],
            asset_equipmentModelList: [],
            asset_equipmentSpecsList: [],
            delVisible: false,
            delId: "" // 删除id
        };
    },
    mounted() {
        getDataList("job_type", this, "jobTypeList"); // 获取工单类型下拉
        getDataList("working_status", this, "jobStatusList"); // 获取工单状态下拉
    },
    methods: {
        details(data) {
            this.$refs.modalForm.edit(data, "details");
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = false;
        },
        openAasetAddModal(record) {
            this.$refs.assetInfoAddModal.edit(record, "add");
        },
        handleDetail(record, type) {
            this.$refs.modalForm.edit(record, "detail");
            this.$refs.modalForm.title = "详情";
        },
        handleDel(record) {
            let delId = record;
            let _this = this;
            deleteAction(_this.url.list + "/" + delId, {}).then(res => {
                if (res.success) {
                    _this.$message.success(res.message);
                    _this.loadData();
                } else {
                    _this.$message.warning(res.message);
                }
            });
        },
        handleDataItem(record) {
            this.$refs.dataItemForm.getList(record);
        }
    }
};