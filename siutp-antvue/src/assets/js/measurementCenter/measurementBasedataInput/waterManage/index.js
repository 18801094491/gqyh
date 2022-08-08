import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import {
    getWaterPriceListData,
    waterPriceOne,
    getBidSegmentData,
    getWaterDetail,
    waterBind,
    waterUnbind,
    remoteStatus
} from '@api/measurementCenter-t/waterManage.js'
import JTreeWaterSelect from '@/components/jeecg/JTreeWaterSelect'
import pick from 'lodash.pick'
export default {
    name: "IotVariableInfoList",
    mixins: [JeecgListMixin],
    components: {
        JTreeWaterSelect
    },
    data() {
        return {
            description: '水表管理',
            labelCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 5
                },
            },
            wrapperCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 16
                },
            },

            // 表头
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
                    title: '编号',
                    align: "center",
                    dataIndex: 'waterSn'
                },
                {
                    title: '客户名称',
                    align: "center",
                    dataIndex: 'customerName'
                },
                {
                    title: '所在位置',
                    align: "center",
                    dataIndex: 'waterLocation'
                },
                {
                    title: '型号',
                    align: "center",
                    dataIndex: 'waterMode',
                },
                {
                    title: '水表名称',
                    align: "center",
                    dataIndex: 'waterName',
                },
                {
                    title: '所属标段',
                    align: "center",
                    dataIndex: 'waterSection',
                },
                {
                    title: '规格',
                    align: "center",
                    dataIndex: 'waterSpecs',
                },
                {
                    title: '水价',
                    align: "center",
                    dataIndex: 'waterPrice',
                },
                {
                    title: '区域名称',
                    align: "center",
                    dataIndex: 'districtName',
                },
                {
                    title: '周期月结',
                    align: "center",
                    dataIndex: 'isFarEnd',
                    scopedSlots: {
                        customRender: 'isFarEnd'
                    },
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: { customRender: 'action' },
                }
            ],
            url: {
                list: "/settle/waterPrice",
                delete: "/iot/varinfo/delete",
                deleteBatch: "/iot/varinfo/deleteBatch",
                exportXlsUrl: "iot/varinfo/exportXls",
                importExcelUrl: "iot/varinfo/importExcel",
            },
            dataSource: [],
            visible: false,
            title: '水价管理',
            columns2: [{
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
                title: '水价(元/吨)',
                align: "center",
                dataIndex: 'price',
            },
            {
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: { customRender: 'action' },
            }
            ],
            dataSource2: [
            ],
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
            loading2: false,
            waterPriceManagementId: '',
            waterPricevisible: false,
            waterPriceTitle: '',
            waterPrice: '',
            id: '',
            titleReading: '抄表详情',
            readingVisible: false, // 抄表详情
            columnsReading: [
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
                    title: '抄表日期',
                    align: "center",
                    dataIndex: 'currentFlowDate',
                },
                {
                    title: '抄表数(m³)',
                    dataIndex: 'currentFlow',
                    align: "center",
                }
            ],
            readingDataSource: [],
            ipaginationReading: {
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
            areaVisible: false, // 水表区域
            titleArea: '水表区域',
            disTree: false,
            form: this.$form.createForm(this),
            model: {},
            validatorRules: {
                parentId: {},
            },
            dictId: '', // 区域id
            treeDataSource: [], // 树结构
            selectedKeys: [],
            expandedKeys: [],
            bidSegmentList: [], // 所属标段
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    mounted() {
        //获取所属标段值
        getBidSegmentData(this);
    },
    methods: {
        // 是否非远端
        stateChange(record) {
            let data = {
                equipmentId: record.waterId
            }
            remoteStatus(data, this)
        },
        //点击水价管理按钮效果
        waterPriceManagementClick(record) {
            this.visible = true;
            this.waterPriceManagementId = record.waterId;
            this.ipagination2.current = 1;
            this.waterPriceManagementUpData();
        },
        // 抄表详情
        waterReadingDetail(record) {
            this.readingVisible = true;
            let data = {
                equipmentId: record.waterId,
                pageNo: this.ipaginationReading.current,
                pageSize: this.ipaginationReading.pageSize
            }
            getWaterDetail(data, this)
        },
        handleCancelReading() {
            this.readingVisible = false;
        },
        // 水表区域
        waterAreaDetail(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.$nextTick(() => {
                setTimeout(() => {
                    this.form.setFieldsValue(pick(this.model, 'districtId'))
                })
            })
            this.areaVisible = true;
        },
        handleCancelArea() {
            this.areaVisible = false;
        },
        getChild(data) {
            this.dictId = data;
        },
        areaSubmit() { // 绑定
            let data = {
                districtId: this.dictId, // 地区id
                equipmentId: this.model.waterId, // 设备id
            }
            waterBind(data, this)
        },
        delAreaUnbind(record) { // 解除绑定
            let data = {
                districtId: record.districtId, // 地区id
                equipmentId: record.waterId, // 设备id
            }
            waterUnbind(data, this)
        },
        handleTableChangeReading(pagination, filters, sorter) {
            this.ipaginationReading = pagination;
        },
        //获取水价管理列表
        waterPriceManagementUpData() {

            let data = {
                waterId: this.waterPriceManagementId,
                pageNo: this.ipagination2.current,
                pageSize: this.ipagination2.pageSize
            }
            this.loading2 = true;
            getWaterPriceListData(data, this);
        },
        //水价管理弹框关闭
        handleCancel() {
            this.visible = false;
        },
        //水价管理列表改变效果
        handleTableChange2(pagination, filters, sorter) {
            this.ipagination2 = pagination;
            this.waterPriceManagementUpData();
        },
        //新增或修改水价
        waterPriceClick(type, data) {
            if (type == 'xz') {
                this.waterPrice = '';
                this.waterPriceTitle = '新增';
            } else {
                this.waterPrice = data.price;
                this.waterPriceTitle = '修改';
                this.id = data.id;
            }
            this.waterPricevisible = true;
        },
        //新增或修改水价弹框关闭
        waterPricehandleCancel() {
            this.waterPricevisible = false;
        },
        //新增或修改水价确定
        waterPriceok() {
            if (!this.waterPrice) {
                this.$message.info('水价不能为空!');
                return;
            }
            let data;
            if (this.waterPriceTitle == '新增') {
                data = {
                    waterId: this.waterPriceManagementId,
                    price: $.trim(this.waterPrice)
                }

            } else {
                data = {
                    waterId: this.waterPriceManagementId,
                    price: $.trim(this.waterPrice),
                    id: this.id
                }

            }
            waterPriceOne(data, this);

        }
    }
}
//waterManage/index.vue 页面组件混入了 waterManage/index.js