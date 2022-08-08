import {
    getCustomerList,
    getWaterPriceMode,
    getPaymentMode,
    addCustomer,
    getChangeCustomerInformation,
    changeCustomerInformation,
    customerInformationUploadFile,
    getBindWatermeterList,
    getWatermeterList,
    bindCustomerEquip,
    unBindCustomerEquip,
    getBidSegmentData,
    getDictValue
} from '@api/measurementCenter-t/customerInfoSetting.js'
import searchReset from '@/mixins/searchReset.js'
import { downFile } from '@/api/manage'
import '@assets/less/measurementCenter/measurementBasedataInput/customerInfoSetting/style.less'
export default {
    name: 'customerManagement',
    mixins: [searchReset],
    data() {
        return {
            description: '客户管理',
            //筛选集合
            queryParam: {},
            //表格头部定义数组
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
                title: '客户编码',
                align: "center",
                dataIndex: 'customerSn',

            },
            {
                title: '客户名称',
                align: "left",
                width: 450,
                dataIndex: 'customerName',

            },
            {
                title: '客户地址',
                align: "center",
                dataIndex: 'customerAddress',

            },
            {
                title: '客户状态',
                align: "center",
                dataIndex: 'customerStatusName',

            },
            {
                title: '客户类型',
                align: "center",
                dataIndex: 'customerTypeName',

            },
            {
                title: '水价模式',
                align: "center",
                dataIndex: 'priceMode',

            },
            {
                title: '付款模式',
                align: "center",
                dataIndex: 'payMode',

            },
            {
                title: '客户税号',
                align: "center",
                dataIndex: 'customeDuty',

            },
            {
                title: '客户银行账户',
                align: "center",
                dataIndex: 'customeBankAccout',

            },
            //              {
            //                  title: '水价(元/吨)',
            //                  align: "center",
            //                  dataIndex: 'price',
            //
            //              },
            {
                title: '创建日期',
                align: "center",
                width: 150,
                dataIndex: 'createTime',

            },
            // {
            //     title: '合同附件',
            //     align: "center",
            //     dataIndex: 'contractName',
            //     scopedSlots: {
            //         customRender: 'contractName'
            //     },
            // },
            {
                title: '操作',
                align: "center",
                dataIndex: 'maintain',
                width: 150,
                fixed: 'right',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            },
            ],
            dataSource: [], //表格数据源
            loading: true,
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
            columns2: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: '10%',
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },
            {
                title: '设备名称',
                align: "center",
                width: '30%',
                dataIndex: 'equipmentName',

            },
            {
                title: '设备信息',
                align: "center",
                width: '45%',
                dataIndex: 'equipmentInfo',

            },
            {
                title: '操作',
                align: "center",
                width: '15%',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            },
            ],
            dataSource2: [], //表格数据源
            loading2: true,
            /* 分页参数 */
            ipagination2: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            columns3: [{
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
                title: '客户水表编号',
                align: "center",
                dataIndex: 'customerWaterSn',

            },
            {
                title: '设备采集数据',
                align: "center",
                dataIndex: 'equipData',
                scopedSlots: {
                    customRender: 'equipData'
                },
            },
            {
                title: '所在位置',
                align: "center",
                dataIndex: 'equipmentLocation',

            },
            {
                title: '设备名称',
                align: "center",
                dataIndex: 'equipmentName',

            },
            {
                title: '设备编号',
                align: "center",
                dataIndex: 'equipmentSn',

            },
            {
                title: '操作时间',
                align: "center",
                dataIndex: 'operationTime',
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
            dataSource3: [], //表格数据源
            loading3: true,
            /* 分页参数 */
            ipagination3: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0,
                hideOnSinglePage: false,
            },
            //新增/修改名称
            drawerTitle: '新增用户管理信息',
            //新增/修改弹框值
            visible: false,
            customerName: '', //客户名称
            customerType: '', //客户类型
            customerStatus: '', // 客户状态
            priceMode: '', //水价模式
            payMode: '', //付款模式
            contacterName: '', //客户联系人
            contacterPhone: '', //客户联系电话
            price: '', //水价
            customerSn: '', //客户编码
            customeDuty: '', //客户税号
            customeBankAccout: '', //客户银行账号
            fileList: [],
            customerTypeList: [], //客户类型列表
            customerStatusList: [], // 客户状态列表
            waterPriceModeList: [], //水价模式列表
            paymentModeList: [], //付款模式列表
            changeId: '', //点击修改存储当前行数据ID
            url: {
                list: "/settle/customer/list",
                delete: "/iot/varinfo/delete",
                deleteBatch: "/iot/varinfo/deleteBatch",
                importExcelUrl: "iot/varinfo/importExcel",
                exportXlsUrl: "settle/customer/exportXls",

            },
            urlId: '', //修改弹框提交时文件附件ID
            waterMeterInformationVisible: false, //水表信息弹框值
            equipmentName: '', //设备名称
            equipmentSn: '', //设备编码
            equipmemtLocation: '', // 放置位置
            equimentSection: '', // 设备标段
            bidSegmentList: [], // 标段下拉
            customerId: '',//客户ID
            customerAddress: '',//客户地址
            jumpPagNum: '', // 跳至第几页
        }
    },
    mounted() {
        //数据初始化
        this.updata();
        //客户类型数据获取
        //      getCustomerType(this);

        //客户类型数据获取
        this.getDictByCustomerType();
        //客户状态数据获取
        this.getDictByCustomerStatus();

        //水价模式数据获取
        getWaterPriceMode(this);
        //付款方式数据获取
        getPaymentMode(this);

        //获取所属标段值
        getBidSegmentData(this);
    },
    methods: {
        getDictByCustomerType() {
            getDictValue("customer_type", null).then(res => {
                if (res.code * 1 == 200) {
                    this.customerTypeList = res.result;
                }
            })
        },
        getDictByCustomerStatus() {
            getDictValue("customer_status", null).then(res => {
                if (res.code * 1 == 200) {
                    this.customerStatusList = res.result;
                }
            })
        },
        //获取数据
        updata() {
            let data = {
                customerName: $.trim(this.queryParam.customerName), //客户名称
                customerSn: $.trim(this.queryParam.customerNumber), //客户编码
                startTime: this.queryParam.startTime ? this.moment(this.queryParam.startTime).format('YYYY-MM-DD') : '', //开始时间
                endTime: this.queryParam.endTime ? this.moment(this.queryParam.endTime).format('YYYY-MM-DD') : '', //结束时间
                pageSize: this.ipagination.pageSize, //当前页显示一页多少条
                pageNo: this.ipagination.current //当前页
            }
            this.loading = true;
            getCustomerList(data, this);
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1; //查询后修改当前页为第一页
            this.updata();
        },
        //新增/修改弹框
        handleAdd(type, data) {
            if (type == 'add') {
                this.drawerTitle = '新增用户管理信息';
                this.visible = true;
                this.reset();
            } else {
                this.drawerTitle = '修改用户管理信息';
                let res = {
                    id: data.id
                }
                this.changeId = data.id;
                this.reset();
                getChangeCustomerInformation(res, this);
            }
        },
        //关闭新增/修改弹框
        onClose() {
            this.visible = false;
        },
        //提交新增/修改信息
        addSubmit() {
            let data;
            if (!this.customerName) {
                this.$message.info('客户名称不能为空!');
                return;
            }
            if (!this.customerStatus) {
                this.$message.info('客户状态不能为空!');
                return;
            }
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    contacterName: $.trim(this.contacterName), //联系人姓名
                    contacterPhone: $.trim(this.contacterPhone), //联系人电话
                    customerName: $.trim(this.customerName), //客户姓名
                    //                  customerSn: $.trim(this.customerSn), //客户编码
                    customerType: $.trim(this.customerType), //客户类型
                    payMode: $.trim(this.payMode), //付款模式
                    //                  price: $.trim(this.price), //水价
                    priceMode: $.trim(this.priceMode), //水价模式
                    customerAddress: this.customerAddress,
                    //                  customerSn: this.customerSn, // 客户编码	
                    customeDuty: this.customeDuty, // 客户税号
                    customeBankAccout: this.customeBankAccout,	// 客户银行账号
                    customerStatus: this.customerStatus, // 客户状态
                    // contractName: this.fileList.length ? this.fileList[0].fileName : '', //合同名称
                    // contractUrl: this.fileList.length ? this.fileList[0].filePath : '', //合同地址
                }
                addCustomer(data, this);
            } else {
                data = {
                    contacterName: $.trim(this.contacterName), //联系人姓名
                    contacterPhone: $.trim(this.contacterPhone), //联系人电话
                    customerName: $.trim(this.customerName), //客户姓名
                    //                  customerSn: $.trim(this.customerSn), //客户编码
                    customerType: $.trim(this.customerType), //客户类型
                    payMode: $.trim(this.payMode), //付款模式
                    //                  price: $.trim(this.price), //水价
                    priceMode: $.trim(this.priceMode), //水价模式
                    // contractName: this.fileList.length ? this.fileList[0].fileName : '', //合同名称
                    // contractUrl: this.fileList.length ? this.fileList[0].filePath : '', //合同地址
                    id: this.changeId, //当前修改父级ID
                    contractId: this.urlId, //当前合同ID
                    customerAddress: this.customerAddress,
                    //                  customerSn: this.customerSn, // 客户编码	
                    customeDuty: this.customeDuty, // 客户税号
                    customeBankAccout: this.customeBankAccout,	// 客户银行账号
                    customerStatus: this.customerStatus, // 客户状态
                }
                changeCustomerInformation(data, this);
            }
        },
        //表格有变动时触发
        handleTableChange(pagination) {
            this.ipagination.current = pagination.current;
            this.updata();
        },
        //新增上传文件
        upfileClick(e) {
            let file = e.target.files[0];
            if (file.type != 'application/pdf') {
                this.$message.error('请上传PDF格式文件!');
                $('#uploadBtn').val('');
                return;
            };
            if (this.fileList.length >= 1) {
                this.$message.error('合同文件只能传一个!');
                $('#uploadBtn').val('');
                return;
            };
            let param = new FormData();
            param.append('file', file);
            console.log(param)
            customerInformationUploadFile(param, this);
        },
        //删除上传文件
        removeFile() {
            this.fileList = [];
            $('#uploadBtn').val('');
        },
        //新增/修改重置页面内属性
        reset() {
            this.customerName = ''; //客户名称
            this.customerType = ''; //客户类型
            this.priceMode = ''; //水价模式
            this.payMode = ''; //付款模式
            this.contacterName = ''; //客户联系人
            this.contacterPhone = ''; //客户联系电话
            //          this.price = ''; //水价
            this.fileList = [];
            this.customerAddress = '';
            this.customerSn = '' // 客户编码	
            this.customeDuty = '' // 客户税号
            this.customeBankAccout = ''	// 客户银行账号
            this.customerStatus = ''; // 客户状态
        },

        //表格有变动时触发
        handleTableChange2(pagination) {
            this.ipagination2.current = pagination.current;
            let data = {
                equipmentName: $.trim(this.equipmentName),
                equipmentSn: $.trim(this.equipmentSn),
                equipmemtLocation: this.equipmemtLocation,
                equimentSection: this.equimentSection,
                pageNo: this.ipagination2.current,
                pageSize: this.ipagination2.pageSize
            }
            getWatermeterList(data, this);
            console.log(this.customerId)
        },
        //水表信息
        waterMeterInformation(res) {
            console.log(res);
            this.ipagination2.current = 1;
            this.waterMeterInformationVisible = true;
            this.equipmentName = '';
            this.equipmentSn = '';
            let data = {
                equipmentName: $.trim(this.equipmentName),
                equipmentSn: $.trim(this.equipmentSn),
                equipmemtLocation: this.equipmemtLocation,
                equimentSection: this.equimentSection,
                pageNo: this.ipagination2.current,
                pageSize: this.ipagination2.pageSize
            }
            let data2 = {
                customerId: res.id

            }
            this.customerId = res.id;

            getBindWatermeterList(data2, this);
            getWatermeterList(data, this);
        },
        //水表信息搜索
        waterMeterInformation2() {
            this.waterMeterInformationVisible = true;
            let data = {
                equipmentName: $.trim(this.equipmentName),
                equipmentSn: $.trim(this.equipmentSn),
                equipmemtLocation: this.equipmemtLocation,
                equimentSection: this.equimentSection,
                pageNo: this.ipagination2.current,
                pageSize: this.ipagination2.pageSize
            }
            let data2 = {
                customerId: this.customerId

            }
            getBindWatermeterList(data2, this);
            getWatermeterList(data, this);
        },
        //关闭水表信息右侧弹框
        waterMeterInformationOnClose() {
            this.waterMeterInformationVisible = false;
        },
        //导出
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let param = {
                ...this.queryParam
            };
            if (param.startTime) {
                param.startTime = this.moment(param.startTime).format('YYYY-MM-DD')
            }
            if (param.endTime) {
                param.endTime = this.moment(param.endTime).format('YYYY-MM-DD')
            }

            if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
                param['selections'] = this.selectedRowKeys.join(",")
            }
            console.log("导出参数", param)
            downFile(this.url.exportXlsUrl, param).then((data) => {
                if (!data) {
                    this.$message.warning("文件下载失败")
                    return
                }
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
                } else {
                    let url = window.URL.createObjectURL(new Blob([data]))
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url
                    link.setAttribute('download', fileName + '.xls')
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link); //下载完成移除元素
                    window.URL.revokeObjectURL(url); //释放掉blob对象
                }
            })
        },
        //水表信息搜索
        waterMeterInformationSearch() {
            this.ipagination2.current = 1;
            this.waterMeterInformation2();
        },
        //绑定水表信息
        bindWaterMeterInformation(res) {
            console.log(this.customerId);
            let _this = this;
            this.$confirm({
                title: '确定要绑定吗?',
                content: '',
                onOk() {
                    let data = {
                        customerId: _this.customerId,
                        equipmentId: res.id,
                        equipmentSn: res.equipmentSn
                    }
                    bindCustomerEquip(data, _this);
                },
                onCancel() {

                },

            });

        },
        //解绑客户水表
        unBindCustomerEquip(res) {
            let _this = this;
            this.$confirm({
                title: '确定要解绑吗?',
                content: '',
                onOk() {
                    let data = {
                        id: res.id
                    }
                    console.log(data, 'data')
                    unBindCustomerEquip(data, _this);
                },
                onCancel() {

                },

            });


        }
    },
}
//customerInfoSetting/index.vue 页面组件混入了 customerInfoSetting/index.js