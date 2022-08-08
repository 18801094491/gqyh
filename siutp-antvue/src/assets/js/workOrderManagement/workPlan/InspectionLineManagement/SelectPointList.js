import { getRoutePointList } from '@api/workOrderManagement/workPlan/InspectionLineManagement/index.js'
export default {
    name: "addModal",
    data () {
        return {
            selectRow: [],
            title: '选择巡检点',
            modalVisible: false,
            dataSource: [],
            pagination:{
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                total: 0
            },
            columns:[
                {
                    title: '巡检点名称',
                    align: "center",
                    width: 200,
                    dataIndex: 'name'
                },
                {
                    title: '设备',
                    align: "center",
                    width: 180,
                    dataIndex: 'equipmentName'
                }
            ],
            loading:false,
            selectedRows: [],
            selectedRowKeys: []
        }
    },
    methods: {
        getList() {
            this.getRoutePointListPage()
            this.modalVisible = true
        },
        getRoutePointListPage() {
            getRoutePointList(this, {
                pageNo: this.pagination.current,
                pageSize: this.pagination.pageSize
            })
        },
        closeModal() {
            this.modalVisible = false;
        },
        onSelectChange (selectedRowKeys, selectionRows) {
            this.selectedRowKeys = selectedRowKeys
            this.selectedRows = selectionRows
        },
        handleOk () {
            this.$emit('onSelect', this.selectedRows)
            this.selectedRowKeys = []
            this.selectedRows = []
            this.modalVisible = false
        },
        tableChange(pagination) {
            this.pagination.current = pagination.current;
            this.getRoutePointListPage()
        }
    }
}