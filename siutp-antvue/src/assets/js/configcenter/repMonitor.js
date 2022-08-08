import {rdpMonitorList, rdpMonitorEquipmentList, rdpMonitorEdit} from '@/api/configcenter/repMonitor.js'
export default {
    name: "monitor",
    data(){
        return {
            columns:[
                {
                    title: '设备编码',
                    dataIndex: 'indexCode',
                    key: 'indexCode',
                    align: 'center'
                },
                {
                    title: '设备名称',
                    dataIndex: 'monitorName',
                    key: 'monitorName',
                    align: 'center'
                },
                {
                    title: '排序',
                    dataIndex: 'positioNo',
                    key: 'positioNo',
                    align: 'center'
                },
                {
                    title: '备注',
                    key: 'remarks',
                    dataIndex: 'remarks',
                    align: 'center'
                },
                {
                    title: '操作',
                    key: 'action',
                    align: 'center',
                    scopedSlots: { customRender: 'action' },
                },
            ],
            dataSource: [],
            ipagination:{
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showSizeChanger: true,
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },

            bindMonitorVisible: false,
            equipmentSn: '',
            monitorColumns: [
                {
                    title: '监控编号',
                    dataIndex: 'equipmentSn',
                    key: 'equipmentSn',
                    align: 'center'
                },
                {
                    title: '海康监控设备编码',
                    dataIndex: 'hkMonitorCode',
                    key: 'hkMonitorCode',
                    align: 'center'
                },
                {
                    title: '所在位置',
                    dataIndex: 'equipmentLocation',
                    key: 'equipmentLocation',
                    align: 'center'
                },
                {
                    title: '操作',
                    key: 'monitorAction',
                    align: 'center',
                    scopedSlots: { customRender: 'monitorAction' },
                },

            ],
            monitorDataSource: [],
            monitorIpagination:{
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showSizeChanger: true,
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            loading: false,
            monitorLoading: false,
            editId: '',
        }
    },
    mounted() {
        this.rdpMonitorList();
    },
    methods: {
        //表格页码变动触发数据改变
        handleTableChange(pagination) {
            this.ipagination.current = pagination.current;
            this.rdpMonitorList();
        },
        rdpMonitorList() {
            let params = {
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            rdpMonitorList(params,this)
        },
        //绑定监控
        bindMonitor(record) {
            this.bindMonitorVisible = true
            this.rdpMonitorEquipmentList();
            this.editId = record.id;
        },
        cancel () {
            console.log("解绑监控")
        },
        bindMonitorCancel() {
            this.bindMonitorVisible = false;
            this.monitorIpagination.current = 1;
            this.monitorIpagination.total = 0;
        },
        //搜索
        monitorSearchQuery() {
            this.monitorIpagination.current = 1;
            this.rdpMonitorEquipmentList()
        },
        rdpMonitorEquipmentList (){
            let monitorParams = {
                pageNo: this.monitorIpagination.current,
                pageSize: this.ipagination.pageSize,
                equipmentSn: this.equipmentSn
            }
            rdpMonitorEquipmentList(monitorParams,this)
        },
        monitorHandleTableChange(pagination) {
            this.monitorIpagination.current = pagination.current;
            this.rdpMonitorEquipmentList()
        },
        //大屏重点监控配置-绑定
        bindMonitorPoint(id,indexCode,hkMonitorKey){
            let params = {
                id: id?id:this.editId,
                indexCode: indexCode,
                hkMonitorKey: hkMonitorKey,
            }
            rdpMonitorEdit(params,this)
        },
    }
}