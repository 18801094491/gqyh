import StructureQueryLeft from "@/views/treeCentre/modules/structureQuery/StructureQueryLeft"
import StructureQueryRight from "@/views/treeCentre/modules/structureQuery/StructureQueryRight"
export default {
    name: "EquipmentListVue",
    components: {StructureQueryLeft, StructureQueryRight},
    data() {
        return {
            description: "信息中心设备树结构管理",
            currentLabelCode: {},
            leftUrl: {
                list: '/centre/inf/equ/getTreeEquAttrList'
            },
            rightUrl: {
                list: '/centre/inf/equ/getEquListByAllSearch'
            },
            comments: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },{
                title: '所属标段',
                align: "center",
                width: 100,
                dataIndex: 'equipmentSection',

            },{
                title: '设备类型',
                align: "center",
                width: 100,
                dataIndex: 'equipmentType',

            },{
                title: '放置位置',
                align: "center",
                width: 200,
                dataIndex: 'equipmentLocation',

            },{
                title: '供应商',
                align: "center",
                width: 300,
                dataIndex: 'equipmentSupplier',

            },{
                title: '设备型号',
                align: "center",
                width: 220,
                dataIndex: 'equipmentModeName',

            },{
                title: '设备规格',
                align: "center",
                width: 100,
                dataIndex: 'equipmentSpecsName',

            },{
                title: '设备编号',
                align: "center",
                width: 120,
                dataIndex: 'equipmentSn',

            },{
                title: '设备名称',
                align: "center",
                width: 200,
                dataIndex: 'equipmentName',

            },{
                title: '资产类别',
                align: "center",
                width: 120,
                dataIndex: 'equipmentCategory',

            },{
                title: '资产状态',
                align: "center",
                width: 100,
                dataIndex: 'equipmentStatus',

            },{
                title: '投入运营时间',
                align: "center",
                width: 200,
                dataIndex: 'equipmentOperating',

            },{
                title: '购置时间',
                align: "center",
                width: 200,
                dataIndex: 'equipmentPurchase',

            },{
                title: '树节点名称',
                align: "center",
                width: 200,
                dataIndex: 'labelName',
            },{
                title: '提前提醒时间(天)',
                align: "center",
                width: 200,
                dataIndex: 'countDays',
            },{
                title: '计划养护日期',
                align: "center",
                width: 200,
                dataIndex: 'planDate',
            },{
                title: '责任人',
                align: "center",
                width: 120,
                dataIndex: 'personResponsibleName',
            },{
                title: '设备状态',
                align: "center",
                width: 100,
                fixed: 'right',
                dataIndex: 'equipmentRevstopText',
                scopedSlots: {
                    customRender: 'equipmentRevstopText'
                },
            },{
                title: '操作',
                align: "center",
                width: 200,
                fixed: 'right',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            }]
        };
    },
    methods: {}
};