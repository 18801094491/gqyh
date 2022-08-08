import {
    getAgencyStatus,
    getProxyType,
    iotProxyLinkTest,
    iotProxyOne,
    getIotProxyList,
    iotProxyEditStatus,
    getTimeType,
    iotProxyAllEquip,
    iotProxyGetBindEquip,
    iotProxyBindOne,
    iotProxyUnbindOne,
    iotProxyBindCate,
    iotProxyBindAll,
    getEquipmentType
} from '@/api/collection-t/ioserver.js'
// import {getEquipmentType} from '@/api/dict.js'
import searchReset from '@/mixins/searchReset.js'
import '@/assets/less/collection/ioserver.less'
export default {
    name: 'ioServerAdministration',
    mixins: [searchReset],
    data() {
        return {
            description: 'ioserver管理',
            //筛选集合
            queryParam: {},
            //表格头部定义数组
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
                    title: '代理名称',
                    align: "center",
                    dataIndex: 'proxyName',

                },
                {
                    title: 'IP地址',
                    align: "center",
                    dataIndex: 'ipAddress',

                },
                {
                    title: '代理类型',
                    align: "center",
                    dataIndex: 'proxyType',

                },
                {
                    title: '心跳地址',
                    align: "center",
                    dataIndex: 'heartbeatAddress',

                },
                {
                    title: '心跳周期(秒)',
                    align: "center",
                    dataIndex: 'heartbeatCycle',

                },
                {
                    title: '状态',
                    align: "center",
                    dataIndex: 'proxyStatus',

                    scopedSlots: {
                        customRender: 'proxyStatus'
                    },
                },

                {
                    title: '操作',
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },
            ],
            dataSource: [
                {
                    customerSn: '111',
                    customerName: '222',
                    customerAddress: '333',
                    customerType: '444',
                    priceMode: '555',
                    payMode: '1',
                    payModeOk: false
                },
                {
                    customerSn: '111',
                    customerName: '222',
                    customerAddress: '333',
                    customerType: '444',
                    priceMode: '555',
                    payMode: '1',
                    payModeOk: true
                }
            ], //表格数据源
            loading: false,
            /* 分页参数 */
            ipagination: {
                current: 1,
                pageSize: 10,
                // pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            drawerTitle: '',
            visible: false,
            visible2: false,
            //表格头部定义数组
            columns2: [
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
                    title: '设备编号',
                    align: "center",
                    dataIndex: 'iotSn',

                },
                {
                    title: '设备名称',
                    align: "center",
                    dataIndex: 'iotName',

                },


                {
                    title: '操作',
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },
            ],
            dataSource2: [

            ], //表格数据源
            loading2: false,
            /* 分页参数 */
            ipagination2: {
                current: 1,
                pageSize: 5,
                // pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            //表格头部定义数组
            columns3: [
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
                    title: '设备编号',
                    align: "center",
                    dataIndex: 'iotSn',

                },
                {
                    title: '设备名称',
                    align: "center",
                    dataIndex: 'iotName',

                },

                {
                    title: '操作',
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtnvarDetails'
                    },
                },
            ],
            dataSource3: [

            ], //表格数据源
            loading3: false,
            /* 分页参数 */
            ipagination3: {
                current: 1,
                pageSize: 5,
                // pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            selectedRowKeys: [],
            workingStatusList: [],
            proxyTypeList: [],
            proxyName: '',
            ipAddress: '',
            proxyType: '',
            heartbeatCycle: '',
            heartbeatCycleTime: '',
            heartbeatStatus: '',
            proxyStatus: '',
            heartbeatAddress: '',
            iotProxyId: '',
            timeTypeList: [],
            inequipmentTypeList: [],
            iotCategory: '',
            iotName: '',
            iotSn: '',
            iotCategory2: '',
            iotName2: '',
            iotSn2: '',
            iotCategory3: ''
        }
    },
    mounted() {
        //新增修改代理-状态下拉获取
        getAgencyStatus(this);
        //新增修改代理-代理类型下拉获取
        getProxyType(this);
        //新增修改代理-心跳周期下拉获取
        getTimeType(this);
        //页面初始化
        this.updata();
        //采集设备-设备类型下拉获取
        getEquipmentType(this);
    },
    methods: {
        //页面初始化
        updata() {
            this.loading = true;
            getIotProxyList(this);
        },
        //新增修改代理按钮
        handleAdd(type, data) {
            this.proxyName = '';
            this.ipAddress = '';
            this.proxyType = '';
            this.heartbeatCycle = '';
            this.heartbeatCycleTime = '';
            this.heartbeatStatus = '';
            this.proxyStatus = '';
            this.heartbeatAddress = '';
            if (type == 'add') {
                this.drawerTitle = '新增代理';
            } else {
                this.drawerTitle = '修改代理';
                this.iotProxyId = data.id;
                this.proxyName = data.proxyName;
                this.ipAddress = data.ipAddress;
                this.proxyType = data.proxyTypeCode;

                this.heartbeatStatus = data.heartbeatStatusCode;
                this.proxyStatus = data.proxyStatusCode;
                this.heartbeatAddress = data.heartbeatAddress;
                if (data.heartbeatUnitCode == 0) {
                    this.heartbeatCycle = data.heartbeatCycle / 1;
                } else if (data.heartbeatUnitCode == 1) {
                    this.heartbeatCycle = data.heartbeatCycle / 60;
                } else if (data.heartbeatUnitCode == 2) {
                    this.heartbeatCycle = data.heartbeatCycle / 3600;
                } else if (data.heartbeatUnitCode == 3) {
                    this.heartbeatCycle = data.heartbeatCycle / 86400;
                }

                this.heartbeatCycleTime = data.heartbeatUnitCode;
            }
            this.visible = true;
        },
        //关闭新增修改代理弹框
        onClose() {
            this.visible = false;
        },
        //新增修改代理提交
        addSubmit() {
            if (!this.proxyName) {
                this.$message.info('代理名称不能为空!');
                return;
            }
            if (!this.ipAddress) {
                this.$message.info('IP地址不能为空!');
                return;
            }
            if (!this.proxyType) {
                this.$message.info('代理类型不能为空!');
                return;
            }
            if (!this.heartbeatCycle) {
                this.$message.info('心跳周期不能为空!');
                return;
            }
            if (!this.heartbeatCycleTime) {
                this.$message.info('心跳周期不能为空!');
                return;
            }
            if (!this.heartbeatStatus) {
                this.$message.info('心跳监测不能为空!');
                return;
            }
            if (!this.proxyStatus) {
                this.$message.info('状态不能为空!');
                return;
            }
            if (!this.heartbeatAddress) {
                this.$message.info('心跳地址不能为空!');
                return;
            }
            let data;
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    proxyName: $.trim(this.proxyName),
                    ipAddress: $.trim(this.ipAddress),
                    proxyType: this.proxyType,
                    heartbeatCycle: this.heartbeatCycle,
                    heartbeatStatus: this.heartbeatStatus,
                    proxyStatus: this.proxyStatus,
                    heartbeatAddress: $.trim(this.heartbeatAddress),
                    heartbeatUnit: this.heartbeatCycleTime
                }
            } else {
                data = {
                    proxyName: $.trim(this.proxyName),
                    ipAddress: $.trim(this.ipAddress),
                    proxyType: this.proxyType,
                    heartbeatCycle: this.heartbeatCycle,
                    heartbeatStatus: this.heartbeatStatus,
                    proxyStatus: this.proxyStatus,
                    heartbeatAddress: $.trim(this.heartbeatAddress),
                    heartbeatUnit: this.heartbeatCycleTime,
                    id: this.iotProxyId
                }
            }
            iotProxyOne(data, this);
        },

        //采集设备按钮
        acquisitionEquipment(record) {
            this.visible2 = true;
            this.iotProxyId = record.id;
            this.ipagination2.current = 1;
            this.ipagination3.current = 1;
            this.iotProxyAllEquipUpdata();
            this.iotProxyGetBindEquipUpdata();
        },
        //采集设备列表获取
        iotProxyAllEquipUpdata() {
            let data = {
                iotCategory: this.iotCategory,
                iotName: $.trim(this.iotName),
                iotSn: $.trim(this.iotSn),
                pageNo: this.ipagination2.current,
                pageSize: this.ipagination2.pageSize
            }
            iotProxyAllEquip(data, this);
        },
        //采集设备绑定列表获取
        iotProxyGetBindEquipUpdata() {
            let data = {
                iotCategory: this.iotCategory2,
                iotName: $.trim(this.iotName2),
                iotSn: $.trim(this.iotSn2),
                pageNo: this.ipagination3.current,
                pageSize: this.ipagination3.pageSize,
                proxyId: this.iotProxyId
            }
            iotProxyGetBindEquip(data, this);
        },
        //采集设备关闭
        onClose2() {
            this.visible2 = false;
            this.iotCategory = '';
            this.iotName = '';
            this.iotSn = '';
            this.iotCategory2 = '';
            this.iotName2 = '';
            this.iotSn2 = '';
            this.iotCategory3 = '';
        },
        //采集设备查询
        searchQuery2() {
            this.ipagination2.current = 1;
            this.iotProxyAllEquipUpdata();
        },
        //采集设备绑定设备查询
        searchQuery3() {
            this.ipagination3.current = 1;
            this.iotProxyGetBindEquipUpdata();
        },
        //采集设备列表页码触发数据刷新
        handleTableChange2(pagination) {
            this.ipagination2.current = pagination.current;
            this.iotProxyAllEquipUpdata();
        },
        //采集设备绑定设备列表页码触发数据刷新
        handleTableChange3(pagination) {
            this.ipagination3.current = pagination.current;
            this.iotProxyGetBindEquipUpdata();
        },
        //测试心跳
        heartbeatAddressClick(type, record) {
            let data;
            if (type == 'wb') {
                data = {
                    ipAddress: record.heartbeatAddress
                }
            } else {
                data = {
                    ipAddress: $.trim(this.heartbeatAddress)
                }
            }

            iotProxyLinkTest(data, this);
        },
        //ioserver起停用
        proxyStatusChange(record) {
            let res = {
                id: record.id
            }
            iotProxyEditStatus(res, this);
        },
        //采集设备-绑定
        bindOne(record) {
            let data = {
                equipId: record.id,
                proxyId: this.iotProxyId
            }
            iotProxyBindOne(data, this);
        },
        //采集设备-解绑
        detailsUnbind(record) {
            let data = {
                equipId: record.id,
                proxyId: this.iotProxyId
            }
            iotProxyUnbindOne(data, this);
        },
        //采集设备-绑定指定类型
        bindSpecifiedType() {
            if (!this.iotCategory3) {
                this.$message.info('请选择设备类型!');
                return;
            }
            let data = {
                category: this.iotCategory3,
                proxyId: this.iotProxyId
            }
            iotProxyBindCate(data, this);
        },
        //采集设备-绑定全部
        iotProxyBindAllClick() {
            let data = {
                proxyId: this.iotProxyId
            }
            iotProxyBindAll(data, this);
        }
    },
}
//在ioserver.vue页面组件里面进行混入ioserver.js