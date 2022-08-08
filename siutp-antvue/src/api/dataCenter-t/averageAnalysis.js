import { downFile } from '@/api/manage'
import searchReset from '@/mixins/searchReset.js'
import { axios } from '@/utils/request'

export default {
    name: 'dataChart',
    mixins: [searchReset],
    data() {
        return {
            name: 'averageAnalysis',
            description: '平均值分析',
            queryParam: {
                statisticalInterval: [],
                startTime: '',
                endTime: '',
            },
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
                    title: '统计项目',
                    align: "left",

                    dataIndex: 'variableTitle'
                },
                {
                    title: '设备类型',
                    align: "left",

                    dataIndex: 'equipmentType'
                },
                {
                    title: '标段',
                    align: "left",

                    dataIndex: 'equipmentSection'
                },

                {
                    title: '所在位置',
                    align: "left",

                    dataIndex: 'equipmentLocation'
                },
                {
                    title: '平均值',
                    align: "right",

                    dataIndex: 'avgValue'
                },
                {
                    title: '峰值',
                    align: "right",

                    dataIndex: 'maxValue'
                },
                {
                    title: '谷值',
                    align: "right",

                    dataIndex: 'minValue'
                },
            ],
            dataSource: [],
            ipagination: {
                current: 1,
                pageSize: 100,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            loading: false,
            statisticalItemsList: [],
        }
    },
    mounted() {
        this.updata();
        //平均值分析-统计项目下拉值获取
        (function (_this) {
            axios.get('/iot/vardata/analysis/item/ce3992ca69c911ea9fded05099cd3eff')
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.statisticalItemsList = res.result;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
        })(this);
    },
    methods: {
        searchQuery() {
            if ((this.queryParam.startTime == '' || !this.queryParam.startTime) && this.queryParam.endTime) {
                return this.$message.info('请选择开始时间!');
            }
            if (this.queryParam.startTime && (this.queryParam.endTime == '' || !this.queryParam.endTime)) {
                return this.$message.info('请选择结束时间!');
            }
            this.updata();
        },
        handleTableChange() {
        },
        //平均值分析初始化
        updata() {
            if (this.queryParam.startTime && this.queryParam.endTime) {
                let startTime = (this.queryParam.startTime).valueOf();
                let endTime = (this.queryParam.endTime).valueOf();
                if (endTime <= startTime) {
                    this.$message.info('结束时间不能小于开始时间!');
                    return;
                }
            }
            let data = {
                typeCode: this.queryParam.statisticalItems ? this.queryParam.statisticalItems : '',
                startTime: this.queryParam.startTime ? new Date(this.moment(this.queryParam.startTime).format('YYYY-MM-DD HH:mm')).getTime() : '',
                endTime: this.queryParam.endTime ? new Date(this.moment(this.queryParam.endTime).format('YYYY-MM-DD HH:mm')).getTime() : ''
            };
            (function (data, _this) {
                axios.get('/iot/vardata/analysis/ce3992ca69c911ea9fded05099cd3eff', {
                    params: data
                }).then(res => {
                    _this.loading = false;
                    _this.dataSource = [];
                    if (res.code * 1 == 200) {
                        let list = res.result;
                        list.map(index => {
                            _this.dataSource.push({
                                variableTitle: index.variableTitle,
                                equipmentType: index.equipmentType,
                                equipmentSection: index.equipmentSection,
                                equipmentLocation: index.equipmentLocation,
                                avgValue: index.avgValue,
                                maxValue: index.maxValue,
                                minValue: index.minValue
                            })
                        })
                    } else {
                        _this.$message.info(res.message);
                    }
                })
            })(data, this);
        },
        //导出
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let param = {
                ...this.queryParam
            };
            if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
                param['selections'] = this.selectedRowKeys.join(",")
            }
            console.log("导出参数", param)
            param.startTime = this.queryParam.startTime ? new Date(this.moment(this.queryParam.startTime).format('YYYY-MM-DD HH:mm')).getTime() : '';
            param.endTime = this.queryParam.endTime ? new Date(this.moment(this.queryParam.endTime).format('YYYY-MM-DD HH:mm')).getTime() : '';
            param.typeCode = this.queryParam.statisticalItems ? this.queryParam.statisticalItems : '';
            delete param.statisticalItems;
            delete param.statisticalInterval;
            downFile('/iot/vardata/analysis/exprot/ce3992ca69c911ea9fded05099cd3eff', param).then((data) => {
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
}
//在averageAnalysis 组件混入averageAnalysis.js。