import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {
    closeWork1,
    getCategory,
    getEquipmentList,
    getWarnStatusList,
    updateProReport
} from "@api/workOrderManagement-t/problemReport.js";

export default {
    name: "problemReport",
    mixins: [JeecgListMixin],
    data() {
        return {
            columns: [{
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
                    title: '问题编号',
                    align: "center",
                    dataIndex: 'problemSn'
                },
                {
                    title: '问题名称',
                    align: "center",
                    dataIndex: 'problemName'
                },
                {
                    title: '问题类型',
                    align: "center",
                    dataIndex: 'problemTypeName'
                },
                {
                    title: '上报人',
                    align: "center",
                    dataIndex: 'createUser',
                },
                {
                    title: '上报日期',
                    align: "center",
                    dataIndex: 'createTime'
                },
                {
                    title: '问题设备信息',
                    align: "center",
                    dataIndex: 'equipmentName'
                },
                {
                    title: '问题描述',
                    align: "center",
                    dataIndex: 'problemDescription',
                    scopedSlots: {
                        customRender: 'problemDescription'
                    },
                },
                {
                    title: '状态',
                    align: "center",
                    dataIndex: 'problemStatusName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                }
            ],
            url: {
                list: "/work/workProblemReport",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            changeId: '', // 修改id
            warnStatusList: [], // 状态下拉
            problemName: '', // 问题名称
            problemType: '', // 问题类型
            equipmentId: '', // 问题设备信息
            problemDescription: '', // 问题描述
            problemTypeList: [], // 问题类型下拉
            equipmentList:[],
            workTeamId: '',
            assignId: '', // 列表行id
        }
    },
    mounted() {
        getWarnStatusList(this);
        getEquipmentList('/equipment/optEquipment/dropdown', this); // 获取设备信息下拉
        getCategory(this, 'problemTypeList');
    },
    methods: {
        //新增/修改弹框
        handleAdd(type, data) {
            this.visible = true;
            if (type == 'add') {
                this.drawerTitle = '新增上报问题';
                this.reset();
            } else {
                this.drawerTitle = '修改上报问题';
                let res = {
                    id: data.id
                }
                this.changeId = data.id;
                this.problemName = data.problemName; // 问题名称
                this.problemType = data.problemType; // 问题类型
                this.equipmentId = data.equipmentId; // 问题设备信息
                this.problemDescription = data.problemDescription; // 问题描述
            }
        },
        //提交新增/修改信息
        addSubmit() {
            let data;
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    problemName: this.problemName, // 问题名称
                    problemType: this.problemType, // 问题类型
                    equipmentId: this.equipmentId, // 问题设备信息
                    problemDescription: this.problemDescription, // 问题描述
                }
                updateProReport(data, this);
            } else {
                data = {
                    problemName: this.problemName, // 问题名称
                    problemType: this.problemType, // 问题类型
                    equipmentId: this.equipmentId, // 问题设备信息
                    problemDescription: this.problemDescription, // 问题描述
                    id: this.changeId, //当前修改父级ID
                }
                updateProReport(data, this);
            }
        },
        //新增/修改重置页面内属性
        reset() {
            this.problemName = ''; // 问题名称
            this.problemType = ''; // 问题类型
            this.equipmentId = ''; // 问题设备信息
            this.problemDescription = ''; // 问题描述
        },
        onClose() {
            this.visible = false;
        },

        closeList(record) { // 关闭
            closeWork1({id: record.id}, this)
        }
    }
}
//workOrderManagement/problemReport/index.vue 页面组件混入了 workOrderManagement/problemReport/index.js