import StructureQueryLeft from "@/views/treeCentre/modules/structureQuery/StructureQueryLeft"
import StructureQueryRight from "@/views/treeCentre/modules/structureQuery/StructureQueryRight"
export default {
    name: "EquipmentListVue",
    components: {StructureQueryLeft, StructureQueryRight},
    data() {
        return {
            description: "计量中心设备树结构管理",
            currentLabelCode: {},
            leftUrl: {
                list: '/centre/met/cust/getTreeCustAttrList'
            },
            rightUrl: {
                list: '/centre/met/cust/getCustListByAllSearch'
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
                title: '客户编码',
                align: "center",
                dataIndex: 'customerSn',

            },{
                title: '客户名称',
                align: "left",
                width: 450,
                dataIndex: 'customerName',

            },{
                title: '客户地址',
                align: "center",
                dataIndex: 'customerAddress',

            },{
                title: '客户状态',
                align: "center",
                dataIndex: 'customerStatusName',

            },{
                title: '客户类型',
                align: "center",
                dataIndex: 'customerTypeName',

            },{
                title: '水价模式',
                align: "center",
                dataIndex: 'priceMode',

            },{
                title: '付款模式',
                align: "center",
                dataIndex: 'payMode',

            },{
                title: '客户税号',
                align: "center",
                dataIndex: 'customeDuty',

            },{
                title: '客户银行账户',
                align: "center",
                dataIndex: 'customeBankAccout',

            },{
                title: '创建日期',
                align: "center",
                width: 150,
                dataIndex: 'createTime',

            },{
                title: '操作',
                align: "center",
                dataIndex: 'maintain',
                width: 150,
                fixed: 'right',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            }],
        };
    },

    methods: {} 
}; 