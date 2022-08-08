var json={
    current: 1,
    queryParam: {}, //搜索属性集合
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
            title: '供应商编码',
            align: "center",
            width: 100,
            dataIndex: 'supplierCode',
            sorter: true
        }, {
            title: '供应商名称',
            align: "center",
            width: 180,
            dataIndex: 'supplierName',
            sorter: true
        },

        {
            title: '供应商类别',
            align: "center",
            width: 120,
            dataIndex: 'supplierCategory',
            sorter: true
        }, {
            title: '供应商营业执照编号',
            align: "center",
            width: 180,
            dataIndex: 'supplierBusinessLicenseNo',
            sorter: true
        }, {
            title: '建档日期',
            align: "center",
            width: 120,
            dataIndex: 'createdDate',
            sorter: true
        }, {
            title: '供应设备',
            align: "center",
            width: 150,
            dataIndex: 'supplyEquipment',
            sorter: true
        }, {
            title: '联系人',
            align: "center",

            dataIndex: 'contacts',
            sorter: true
        }, {
            title: '联系电话',
            align: "center",

            dataIndex: 'contactNumber',
            sorter: true
        }, {
            title: '供应商状态',
            align: "center",

            dataIndex: 'supplierStatus',
            sorter: true
        }, {
            title: '操作',
            align: "center",
            dataIndex: 'maintain',
            scopedSlots: {
                customRender: 'maintainBtn'
            },
        },

    ], //表格头部属性信息
    dataSource: [{

    }], //表格数据源
    loading: false,
    /* 分页参数 */
    ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
            return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
    },
}
