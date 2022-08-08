import { getSettleMangeList, settleByHand, getSettleMangeList2 } from '@api/measurementCenter-t/settlementManage.js'
import ContractDetail from '@views/measurementCenter/settlementManage/contractDetail'
import EditableCell from '@views/measurementCenter/settlementManage/editTable'
import moment from "moment";
// 合并单元格
var last = {};
const mergeMyCells = (groupBy, data, columns, index) => {
    let conIdColumn = groupBy + columns;
    if (last[conIdColumn] && index === 0) {
        last = {};
    }
    if (last[conIdColumn]) return 0;
    last[conIdColumn] = 1;
    let list = data.filter(item => {
        return item.customerId === groupBy;
    })
    return list.length;
}

export default {
    name: "settleManage",
    components: {
        ContractDetail,
        EditableCell
    },
    data() {
        let _this = this
        return {
            columns: [{
                title: '客户名称',
                align: "center",
                dataIndex: 'customerName',
                width: '150px',
                customRender: (value, row, index) => {
                    const obj = {
                        children: value,
                        attrs: {},
                    }
                    obj.attrs.rowSpan = mergeMyCells(row.customerId, this.dataSource, 'customerName', index)
                    return obj
                },
            },
            {
                title: '客户类型',
                align: "center",
                dataIndex: 'customerType',
                width: '150px',
                customRender: (value, row, index) => {
                    const obj = {
                        children: value,
                        attrs: {},
                    }
                    obj.attrs.rowSpan = mergeMyCells(row.customerId, this.dataSource, 'customerType', index)
                    return obj
                },
            },
            {
                title: '签约次数',
                align: "center",
                dataIndex: 'signCount',
                width: '100px',
                scopedSlots: {
                    customRender: 'signCount'
                },
                customRender: (value, row, index) => {
                    const obj = {
                        children: (<div>
                            <a onClick={() => this.signingTimes(row)}>{value}</a>
                        </div>),
                        attrs: {},
                    }
                    obj.attrs.rowSpan = mergeMyCells(row.customerId, this.dataSource, 'signCount', index)
                    return obj
                },
            },
            {
                title: '合同名称',
                align: "center",
                dataIndex: 'contractName',
                width: '150px',
                scopedSlots: {
                    customRender: 'contractName'
                },
            },
            {
                title: '合同起始时间',
                align: "center",
                dataIndex: 'contractValidateDate',
                width: '150px'
            },
            {
                title: '合同终止时间',
                align: "center",
                dataIndex: 'contractInvalidateDate',
                width: '150px'
            },
            {
                title: '水表信息',
                align: "center",
                dataIndex: 'equipmentInfo',
                width: '150px'
            },
            {
                title: '区域',
                align: "center",
                dataIndex: 'equipmentDistrictName',
                width: '150px'
            },
            {
                title: '本次抄表时间',
                align: "center",
                dataIndex: 'currentFlowDate',
                width: '150px'
            },
            {
                title: '本次表底',
                align: "center",
                dataIndex: 'currentFlow',
                width: '120px'
            },
            {
                title: '上次抄表时间',
                align: "center",
                dataIndex: 'previousFlowDate',
                width: '150px'
            },
            {
                title: '上次表底',
                align: "center",
                dataIndex: 'previousFlow',
                width: '120px'
            },
            {
                title: '本次用水量',
                align: "center",
                dataIndex: 'currentUsedFlow',
                width: '150px'
            },
            {
                title: '本期水价',
                align: "center",
                dataIndex: 'currentWaterPrice',
                width: '120px',
            },
            {
                title: '本次应缴费用',
                align: "center",
                dataIndex: 'currentCost',
                width: '150px'
            },
            {
                title: '操作',
                dataIndex: 'action',
                width: '130px',
                align: 'center',
                // fixed: 'left',
                customRender: (value, row, index) => {
                    const obj = {
                        children: (<div>
                            <a onClick={() => this.handleOkByHand(row)} style={{ display: 'block' }}>{'手工结算'}</a>
                            <a onClick={() => this.settleHis(row)}>{'结算历史查询'}</a>
                        </div>),
                        attrs: {},
                    }
                    obj.attrs.rowSpan = mergeMyCells(row.customerId, this.dataSource, 'action', index)
                    return obj
                },
            },
            ],
            dataSource: [],
            customerList: [],// 客户名称列表
            queryParam: {
                customerName: ''
            },
            settlementVisble: false, // 手工结算
            settle: {
                customerName: ''
            },
            settleCustomerId: '',
            settleDetailTitle: '',
            settleDataSource: [], // 大列表到手工结算带过去的数据
            settleColumns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                customRender: function (t, r, index) {
                    if (_this.settleDataSource.length == index + 1) {
                        return '合计'
                    } else {
                        return parseInt(index) + 1;
                    }
                }
            },
            {
                title: '水表信息',
                align: "center",
                dataIndex: 'equipmentInfo'
            },
            {
                title: '本次表底',
                align: "center",
                dataIndex: 'currentFlow',
            },
            {
                title: '本次抄表时间',
                align: "center",
                dataIndex: 'currentFlowDate'
            },
            {
                title: '上次表底',
                align: "center",
                dataIndex: 'previousFlow'
            },
            {
                title: '上次抄表时间',
                align: "center",
                dataIndex: 'previousFlowDate'
            },
            {
                title: '本期水价',
                align: "center",
                dataIndex: 'currentWaterPrice'
            },
            {
                title: '本期用水量',
                align: "center",
                dataIndex: 'currentUsedFlow',
            },
            {
                title: '应缴水费(元)',
                align: "center",
                dataIndex: 'currentCost',
            },
            ],
            settleLoading: false,
            loading: false,
            settleParam: { // 手工结算截止时间
                endTime: new Date().getTime()
            },
            contractDetailRecord: null,
            contractDetailVisble: false,
        }
    },
    mounted() {
        this.updata();
    },
    methods: {
        updata() {
            let data = {
                likeCustomerName: this.queryParam.customerName,
            }
            console.log(data);
            this.loading = true;
            getSettleMangeList(data, this)
        },
        searchQuery() {
            this.updata();
        },
        searchReset() {
            this.queryParam = {};
        },
        handleOkByHand(record) { // 手工结算
            this.settlementVisble = true;
            this.settleDetailTitle = record.customerName;
            this.settleCustomerId = record.customerId;
            let settleDataArr = this.dataSource.filter(item => {
                if (record.customerName == item.customerName) {
                    return item
                }
            })
            this.settleDataSource = [];
            this.handleSettleData(settleDataArr);
            this.handleTotal();
        },
        handleTotal() { // 手工计算合计
            let flowTotal = 0;
            let costTotal = 0;
            this.settleDataSource.map(item => {
                flowTotal += Number(item.currentUsedFlow);
                costTotal += Number(item.currentCost);
            })
            this.settleDataSource.push({
                equipmentInfo: '',
                currentFlow: '',
                currentFlowDate: '',
                previousFlow: '',
                previousFlowDate: '',
                currentWaterPrice: '',
                currentUsedFlow: flowTotal.toFixed(1),
                currentCost: costTotal.toFixed(2),
            })
        },
        handleSettleData(settleDataArr) {
            settleDataArr.map(item => {
                this.settleDataSource.push({
                    id: item.id,
                    customerId: item.customerId,      // 客户id
                    customerName: item.customerName,  // 客户名称
                    customerType: item.customerType,  // 客户类型
                    signCount: item.signCount,        // 签约次数
                    contractId: item.contractId,
                    contractName: item.contractName,
                    contractValidateDate: item.contractValidateDate,  // 合同起始时间
                    contractInvalidateDate: item.contractInvalidateDate, // 合同终止时间
                    equipmentId: item.equipmentId,
                    equipmentInfo: item.equipmentInfo, // 水表信息
                    equipmentDistrictId: item.equipmentDistrictId,
                    equipmentDistrictName: item.equipmentDistrictName, // 水表区域
                    currentFlowDate: item.currentFlowDate, // 本次抄表时间
                    currentFlowTime: item.currentFlowTime, // 本次抄表时间
                    currentFlow: item.currentFlow, // 本次表底
                    previousFlowDate: item.previousFlowDate, // 上次抄表时间
                    previousFlow: item.previousFlow, // 上次表底
                    previousId: item.previousId, // 上次表底
                    currentUsedFlow: item.currentUsedFlow, // 本次用水量
                    currentWaterPrice: item.currentWaterPrice, // 本期水价
                    currentCost: item.currentCost, // 本期应缴费用
                    status: item.status
                })
            })
        },
        handleCancelByHand() { //手工结算取消
            this.settle.customerName = '';
        },
        onClose() {
            this.settle.customerName = '';
            this.settlementVisble = false;
        },
        searchQuerySettle() { // 手工结算根据截止时间查询

            let data = {
                customerId: this.settleCustomerId,
                queryDate: moment(this.settleParam.endTime).format("YYYY-MM-DD"),
            }
            getSettleMangeList2(data, this)
        },
        handleSubmit() { // 生成结算单
            let settleArr = this.settleDataSource.filter((item, index) => {
                if (this.settleDataSource.length != (index + 1)) {
                    return item
                }
            });
            settleByHand(settleArr, this)
        },
        settleHis(record) { // 结算历史查询
            this.$router.push({
                path: '/settlementCenter/settleStatistics',
            })
        },
        signingTimes(record) { // 签约次数点击
            this.$router.push({
                name: "settlementCenter-contractManagement-ContractList",
            })
        },
        contractDetail(record) { // 合同名称点击
            this.contractDetailRecord = record;
            this.contractDetailVisble = true;
        },
        closeContractDetail(modalVisible) {
            this[modalVisible] = false
        },
        // 表格单个单元格编辑
        onCellChangeFlow(key, dataIndex, value) {
            const dataSource = this.settleDataSource.filter((item, index) => {
                if (this.settleDataSource.length != (index + 1)) {
                    return item
                }
            });
            const target = dataSource.find(item => item.currentUsedFlow === key);
            if (target) {
                target[dataIndex] = value;
                this.settleDataSource = dataSource;
            }
            this.handleTotal();
        },
        // 表格单个单元格编辑
        onCellChangeCost(key, dataIndex, value) {
            const dataSource = this.settleDataSource.filter((item, index) => {
                if (this.settleDataSource.length != (index + 1)) {
                    return item
                }
            });
            const target = dataSource.find(item => item.currentCost === key);
            if (target) {
                target[dataIndex] = value;
                this.settleDataSource = dataSource;
            }
            this.handleTotal();
        },
    }
}
//settlementManage/index.vue 页面组件混入了 settlementManage/index.js