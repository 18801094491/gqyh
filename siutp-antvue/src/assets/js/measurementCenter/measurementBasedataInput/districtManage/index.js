import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import RegionListModal from "@views/measurementCenter/measurementBasedataInput/districtManage/regionListModal";

export default {
    name: "regionList",
    mixins: [JeecgListMixin],
    components: {
        RegionListModal
    },
    data() {
        return {
            description: "区域管理页面",
            // 表头
            columns: [
                {
                    title: "区域名称",
                    align: "left",
                    dataIndex: "districtName"
                },
                {
                    title: "操作",
                    dataIndex: "action",
                    align: "center",
                    scopedSlots: { customRender: "action" }
                }
            ],
            url: {
                list: "/settle/district/rootList", // 根节点
                childList: "/settle/district/childList", // 点击加号获取子节点
                delete: "/settle/district/delete", // 删除节点
                deleteBatch: "/settle/district/deleteBatch",
                exportXlsUrl: "/sys/category/exportXls",
                importExcelUrl: "sys/category/importExcel"
            },
            expandedRowKeys: [],
            hasChildrenField: "hasChild",
            pidField: "parentId",
            dictOptions: {},
        };
    },
    computed: {
        importExcelUrl() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        },
        tableProps() {
            let _this = this;
            return {
                // 列表项是否可选择
                rowSelection: {
                    selectedRowKeys: _this.selectedRowKeys,
                    onChange: selectedRowKeys => (_this.selectedRowKeys = selectedRowKeys)
                }
            };
        }
    },
    methods: {
        onChange1(value) {
            this.value1 = value;
        },
        onSearch() {
            console.log(...arguments);
        },
        onSelect() {
            console.log(...arguments);
        },
        loadData(arg) {
            if (arg == 1) {
                this.ipagination.current = 1;
            }
            this.loading = true;
            this.expandedRowKeys = [];
            let params = this.getQueryParams();
            return new Promise(resolve => {
                getAction(this.url.list, params).then(res => {
                    if (res.success) {
                        let result = res.result;
                        if (Number(result.total) > 0) {
                            this.ipagination.total = Number(result.total);
                            this.dataSource = this.getDataByResult(res.result.records);
                            resolve();
                        } else {
                            this.ipagination.total = 0;
                            this.dataSource = [];
                        }
                    } else {
                        this.$message.warning(res.message);
                    }
                    this.loading = false;
                });
            });
        },
        getDataByResult(result) {
            return result.map(item => {
                //判断是否标记了带有子节点
                if (item[this.hasChildrenField] == "1") {
                    let loadChild = {
                        id: item.id + "_loadChild",
                        name: "loading...",
                        isLoading: true
                    };
                    item.children = [loadChild];
                }
                return item;
            });
        },
        handleExpand(expanded, record) {
            // 判断是否是展开状态
            if (expanded) {
                this.expandedRowKeys.push(record.id);
                if (
                    record.children.length > 0 &&
                    record.children[0].isLoading === true
                ) {
                    let params = this.getQueryParams(); //查询条件
                    params[this.pidField] = record.id;
                    getAction(this.url.childList, params).then(res => {
                        if (res.success) {
                            if (res.result && res.result.length > 0) {
                                record.children = this.getDataByResult(res.result);
                                this.dataSource = [...this.dataSource];
                            } else {
                                record.children = "";
                                record.hasChildrenField = "0";
                            }
                        } else {
                            this.$message.warning(res.message);
                        }
                    });
                }
            } else {
                let keyIndex = this.expandedRowKeys.indexOf(record.id);
                if (keyIndex >= 0) {
                    this.expandedRowKeys.splice(keyIndex, 1);
                }
            }
        },
        initDictConfig() { },
        modalFormOk(formData, arr) {
            if (!formData.id) {
                this.addOk(formData, arr);
            } else {
                this.editOk(formData, this.dataSource);
                this.dataSource = [...this.dataSource];
            }
        },
        editOk(formData, arr) {
            if (arr && arr.length > 0) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].id == formData.id) {
                        arr[i] = formData;
                        break;
                    } else {
                        this.editOk(formData, arr[i].children);
                    }
                }
            }
        },
        async addOk(formData, arr) {
            if (!formData[this.pidField]) {
                this.loadData();
            } else {
                this.expandedRowKeys = [];
                for (let i of arr) {
                    await this.expandTreeNode(i);
                }
                this.loadData();
            }
        },
        expandTreeNode(nodeId) {
            return new Promise((resolve, reject) => {
                this.getFormDataById(nodeId, this.dataSource);
                let row = this.parentFormData;
                this.expandedRowKeys.push(nodeId);
                let params = this.getQueryParams(); //查询条件
                params[this.pidField] = nodeId;
                getAction(this.url.childList, params).then(res => {
                    if (res.success) {
                        if (res.result && res.result.length > 0) {
                            row.children = this.getDataByResult(res.result);
                            this.dataSource = [...this.dataSource];
                            resolve();
                        } else {
                            reject();
                        }
                    } else {
                        reject();
                    }
                });
            });
        },
        getFormDataById(id, arr) {
            if (arr && arr.length > 0) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].id == id) {
                        this.parentFormData = arr[i];
                    } else {
                        this.getFormDataById(id, arr[i].children);
                    }
                }
            }
        }
    }
};
//districtManage/index.vue 页面组件混入了 districtManage/index.js