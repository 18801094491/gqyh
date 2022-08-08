export default function getColumns()
{
    return [{
        title: '序号',
        dataIndex: '',
        key: 'rowIndex',
        width: 60,
        align: "center",
        customRender: function (t, r, index) {
            return parseInt(index) + 1;
        }
    }, {
        title: '标签名称',
        align: "center",
        width: 150,
        dataIndex: 'labelName',

    },
        {
            title: '所属标段',
            align: "center",
            width: 100,
            dataIndex: 'equipmentSection',

        },
        {
            title: '设备类型',
            align: "center",
            width: 100,
            dataIndex: 'equipmentType',

        },
        {
            title: '放置位置',
            align: "center",
            width: 200,
            dataIndex: 'equipmentLocation',

        },
        {
            title: '供应商',
            align: "center",
            width: 300,
            dataIndex: 'equipmentSupplier',

        },
        {
            title: '设备型号',
            align: "center",
            width: 220,
            dataIndex: 'equipmentModeName',

        },
        {
            title: '设备规格',
            align: "center",
            width: 100,
            dataIndex: 'equipmentSpecsName',

        },
        {
            title: '设备编号',
            align: "center",
            width: 120,
            dataIndex: 'equipmentSn',

        },
        {
            title: '设备名称',
            align: "center",
            width: 200,
            dataIndex: 'equipmentName',

        },
        {
            title: '设备类别',
            align: "center",
            width: 120,
            dataIndex: 'equipmentCategory',

        },
        {
            title: '资产状态',
            align: "center",
            width: 100,
            dataIndex: 'equipmentStatus',

        },
        {
            title: '投入运营时间',
            align: "center",
            width: 200,
            dataIndex: 'equipmentOperating',

        },
        {
            title: '购置时间',
            align: "center",
            width: 200,
            dataIndex: 'equipmentPurchase',

        },
        {
            title: '设备状态',
            align: "center",
            width: 100,
            dataIndex: 'equipmentRevstopText',
        },
        {
            title: '设备知识',
            align: "center",
            width: 100,
            dataIndex: 'equipmentKnowledge',
        },
        {
            title: '父资产',
            align: "center",
            width: 100,
            dataIndex: 'equipmentHigher',
        },{
            title: '操作',
            align: "center",
            width: 150,
            fixed: 'right',
            dataIndex: 'maintain',
            scopedSlots: {
                customRender: 'maintainBtn'
            },
        },

    ]
}