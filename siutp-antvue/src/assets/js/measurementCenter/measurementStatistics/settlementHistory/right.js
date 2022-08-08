import {
    getActions,
    downFile,
    getDictValue
}
    from '@api/measurementCenter-t/settlementHistory.js'

export default {
    name: "HisDataRight",
    props: ["value"],
    data() {
        return {
            queryParam: {
                customerName: '', // 客户名称
                status: '',
                left: '',
                right: '',
            },
            pageLoading: false,
            settleResList: [],
            cardLoading: true,
            columns: [{
                title: '区域名称',
                align: "center",
                dataIndex: 'equipmentDistrictName',
                width: '150px',
            },
            {
                title: '结算月份',
                align: "center",
                dataIndex: 'settleDate',
                width: '150px',
            },
            {
                title: '客户名称',
                align: "center",
                dataIndex: 'customerName',
                width: '150px',
            },
            {
                title: '客户类型',
                align: "center",
                dataIndex: 'customerType',
                width: '150px',
            },
            {
                title: '签约次数',
                align: "center",
                dataIndex: 'signCount',
                width: '100px',
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
                width: '120px'
            },
            {
                title: '本次应缴费用',
                align: "center",
                dataIndex: 'currentCost',
                width: '150px'
            },
            {
                title: '结算结果',
                align: "center",
                dataIndex: 'status',
                width: '150px'
            }],
            url: {
                list: "/settle/customerInfo/historyData"
            },
            pageNo: 1,
            pageSize: 20,
            total: 0,
            jumpPagNum: '',
            left_Date: {
                disabledDate: time => {
                    if (this.queryParam.right) {
                        return time.getTime() > Date.now() || time.getTime() > this.queryParam.right
                    }
                }
            },
            right_Date: {
                disabledDate: time => {
                    return time.getTime() > Date.now() || time.getTime() < this.queryParam.left
                }
            },
            loading: false,
        };
    },
    watch: {
        value: {
            immediate: true,
            handler(id) {
                this.dataSource = [];
                this.loadData(1, id);
            }
        }
    },
    created() {
        this.getDictBySettle();
    },
    mounted() {
        this.pageLoading = true
    },
    destroyed() {
        console.log('我正在销毁')
        this.pageLoading = false
    },
    methods: {
        getDictBySettle() {
            getDictValue("settle_status", null).then(res => {
                if (res.code * 1 == 200) {
                    this.settleResList = res.result;
                }
            })
        },
        pageCount(totalnum, limit) {
            return totalnum > 0 ? ((totalnum < limit) ? 1 : ((totalnum % limit) ? (parseInt(totalnum / limit) + 1) : (totalnum / limit))) : 0;
        },
        toPage() {
            let totalPage = this.pageCount(this.total, this.pageSize)
            this.pageNo = this.jumpPagNum * 1 > totalPage ? totalPage : this.jumpPagNum * 1;
            this.loadData(this.pageNo, this.value);
        },
        loadData(pageNum, id) {
            if (!id) {
                return;
            }
            //加载数据 若传入参数1则加载第一页的内容
            if (pageNum === 1) {
                this.pageNo = 1;
            }
            this.loading = true;
            let sendData = {
                startTime: this.queryParam.left ? this.moment(this.queryParam.left).format("YYYY-MM") : '',
                endTime: this.queryParam.right ? this.moment(this.queryParam.right).format("YYYY-MM") : '',
                customerName: this.queryParam.customerName,
                status: this.queryParam.status,
                pageNo: this.pageNo,
                pageSize: this.pageSize
            }
            if (id * 1 !== 0) {
                sendData.districtId = id
            }
            getActions(this.url.list, sendData).then(res => {
                if (res.success) {
                    this.loading = false;
                    this.total = res.result.total;
                    this.dataSource = res.result.records;
                }
            })
        },
        searchQuery() {
            this.loadData(1, this.value);
        },
        searchReset() {
            this.loadData(1, this.value);
            this.queryParam = {
                customerName: '', // 客户名称
                status: '', // 结算结果
                left: '',
                right: ''
            };
        },
        handleSizeChange(val) {
            this.pageNo = val;
            this.loadData(this.pageNo, this.value);
        },
        handleCurrentChange(val) {
            this.pageSize = val;
            this.loadData(this.pageNo, this.value);
        },
        //导出
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let data = {
                startTime: this.queryParam.left ? this.moment(this.queryParam.left).format("YYYY-MM") : '',
                endTime: this.queryParam.right ? this.moment(this.queryParam.right).format("YYYY-MM") : '',
                customerName: this.queryParam.customerName,
                status: this.queryParam.status,
                pageNo: this.pageNo,
                pageSize: this.pageSize
            }
            if (this.value * 1 !== 0) {
                data.districtId = this.value
            }
            console.log("导出参数", data)
            downFile('/settle/customerInfo/export', data).then((data) => {
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
    }
};
//settlementHistory/right.vue 页面组件混入了 settlementHistory/right.js