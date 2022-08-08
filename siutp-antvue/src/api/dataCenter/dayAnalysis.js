import {axios} from '@/utils/request';

export default {
    name: "userWaterConsumptionstatistics",
    data() {
        return {
            description: '用水量统计',
            data: [],
            scale: [
                {
                    dataKey: 'temperature',
                    min: 0
                },
                {
                    dataKey: 'year',
                }
            ],
            height: 400,
            width: 300,
            barType: '7',
            dataSource: [],
            loading: false,
            /* 分页参数 */
            ipagination: {
                current: 1,
                pageSize: 300,
                // pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
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
                    title: '用户名称',
                    align: "center",
                    width: 100,
                    dataIndex: 'customerName',

                },
                {
                    title: '用户水表编号',
                    align: "center",
                    width: 100,
                    dataIndex: 'equipmentSn',

                },
                {
                    title: '标段',
                    align: "center",
                    width: 100,
                    dataIndex: 'sectionName',

                },
                {
                    title: '当前日期',
                    align: "center",
                    width: 100,
                    dataIndex: 'nowDate',

                },
                {
                    title: '当前月累计用水量',
                    align: "center",
                    width: 100,
                    dataIndex: 'monthNetFlowDay',

                },
                {
                    title: '总累计用水量',
                    align: "center",
                    width: 100,
                    dataIndex: 'netFlowDay',

                },

                {
                    title: '上月累计用水量',
                    align: "center",
                    width: 100,
                    dataIndex: 'oldMonthNetFlowDay',

                },
                /*
                {
                  title: '所属项目',
                  align: "center",
                  width: 100,
                  dataIndex: 'projectName',

                },
                */

                {
                    title: '当年累计用水量',
                    align: "center",
                    width: 100,
                    dataIndex: 'yearNetFlowDay',

                },
            ],
            productionSalesBalance: '14',
            productionSalesBalanceTime: [],
            dateType: 0,
            mode2: ['month', 'month'],
        }
    },
    mounted() {
        //执行水表日累计量统计和执行流量计日累计量统计
        this.aaa();
        //每日累计用水量查询获取
        this.updata();
        //客户用水累计统计
        this.updata2();
        let _this = this;
        //产销差
        setTimeout(() => {
            _this.highchartsUpdata2();
        }, 500);

    },
    methods: {
        //每日累计用水量查询获取
        updata() {
            let data = {
                daycount: this.barType
            }
            (function (data, _this) {
                axios.get('/equipment/meterFlow/queryNetFlowDay', {
                    params: data
                })
                    .then(res => {

                        if (res.code * 1 == 200) {
                            let list = res.result;
                            _this.highchartsUpdata(list);
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            })(data, this);
        },

        //每日用水量
        highchartsUpdata(data) {
            console.log(data);
            let arr = [];
            let arr2 = [];
            for (let i = 0; i < data.length; i++) {
                arr.push(data[i].staticsDate);
                arr2.push(Number(data[i].netFlowDay));
            }
            console.log(arr);
            console.log(arr2);
            Highcharts.setOptions({
                colors: ['#1E90FF', '#00CDCD', '#97FFFF', '#76EEC6', '#FFB90F', '#EE6363', '#FF00FF',
                    '#DDA0DD', '#9370DB', '#8A2BE2', '#ADD8E6'
                ]
            });
            var chart = Highcharts.chart('container', {
                title: {
                    text: ''
                },
                subtitle: {
                    text: ''
                },
                xAxis: {
                    categories: arr
                },
                yAxis: {
                    title: {
                        text: ''
                    }
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    enabled: false
                },
                plotOptions: {
                    series: {
                        label: {
                            connectorAllowed: true
                        }
                    }
                },
                series: [
                    {
                        name: '每日累计用水量',
                        data: arr2
                    }
                ],

                responsive: {
                    rules: [{
                        condition: {
                            maxWidth: 500
                        },
                        chartOptions: {
                            legend: {
                                layout: 'horizontal',
                                align: 'center',
                                verticalAlign: 'bottom'
                            }
                        }
                    }]
                },

            });
        },
        //产销差
        highchartsUpdata2() {

            Highcharts.setOptions({
                colors: ['#1E90FF', '#00CDCD', '#97FFFF', '#76EEC6', '#FFB90F', '#EE6363', '#FF00FF',
                    '#DDA0DD', '#9370DB', '#8A2BE2', '#ADD8E6'
                ]
            });
            var chart = Highcharts.chart('container2', {
                chart: {
                    type: 'column'
                },
                title: {
                    text: ''
                },
                subtitle: {
                    text: ''
                },
                xAxis: {
                    categories: [
                        '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'
                    ],
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    }
                },
                tooltip: {
                    // head + 每个 point + footer 拼接成完整的 table
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.05,
                        borderWidth: 0,
                        dataLabels: {
                            enabled: true //设置显示对应y的值
                        }
                    }
                },
                // legend:{
                //   enabled:false
                // },

                series: [{
                    name: '总供水量',
                    data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
                }, {
                    name: '实际用水量',
                    data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]
                }, {
                    name: '产销差',
                    data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]
                }]
            });
        },
        //客户用水累计统计
        updata2() {
            (function (_this) {
                axios.get('/equipment/meterFlow/queryAllNetFlowDay')
                    .then(res => {
                        _this.dataSource = [];
                        if (res.code * 1 == 200) {
                            let list = res.result;
                            list.map(index => {
                                _this.dataSource.push({
                                    customerName: index.customerName,
                                    equipmentSn: index.equipmentSn,
                                    monthNetFlowDay: index.monthNetFlowDay,
                                    netFlowDay: index.netFlowDay,
                                    nowDate: index.nowDate,
                                    oldMonthNetFlowDay: index.oldMonthNetFlowDay,
                                    projectName: index.projectName,
                                    sectionName: index.sectionName,
                                    yearNetFlowDay: index.yearNetFlowDay
                                })
                            })
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            })(this);
        },
        //每日累计用水量分析天数改变数据刷新
        statisticst(e) {
            this.barType = e.target.value;
            this.updata();

        },
        //产销差数据获取
        productionSalesBalanceUpdata() {
            let data;
            if (this.productionSalesBalance) {
                data = {
                    time: this.productionSalesBalance
                }
            } else {
                data = {
                    startTime: this.moment(this.productionSalesBalanceTime[0]).format('YYYY-MM-DD'),
                    endTime: this.moment(this.productionSalesBalanceTime[1]).format('YYYY-MM-DD')
                }
            }
            console.log(data);
        },
        //产销差固定日期选择
        productionSalesBalanceChange(e) {
            this.productionSalesBalance = e.target.value;
            this.productionSalesBalanceTime = [];
            this.dateType = 0;
            this.productionSalesBalanceUpdata();
        },
        //产销差范围日期选择
        productionSalesBalanceTimeChange(e) {

            this.productionSalesBalance = '';
            this.dateType = 1;


        },
        //产销差范围日期选择查询
        productionSalesBalanceSearchQuery(value) {
            console.log(this.dateType);
            if (this.dateType == 1) {
                let startTime = new Date(this.moment(this.productionSalesBalanceTime[0]).format('YYYY-MM-DD')).getTime();
                let endTime = new Date(this.moment(this.productionSalesBalanceTime[1]).format('YYYY-MM-DD')).getTime();
                let num2 = endTime - startTime;
                let num = 86400 * 30 * 1000;
                if (num2 > num) {
                    this.$message.info('按天类型,时间不能超过1个月!');
                    return;
                }
            } else if (this.dateType == 2) {
                let startTime = new Date(this.moment(this.productionSalesBalanceTime[0]).format('YYYY-MM-DD')).getTime();
                let endTime = new Date(this.moment(this.productionSalesBalanceTime[1]).format('YYYY-MM-DD')).getTime();
                let num2 = endTime - startTime;
                let num = 86400 * 30 * 1000 * 24;
                if (num2 > num) {
                    this.$message.info('按月类型,时间不能超过两年!');
                    return;
                }
            }

            this.productionSalesBalanceUpdata();
        },
        //产销差
        dateTypeChange(e) {
            console.log(this.dateType);
            this.dateType = e.target.value;
            this.productionSalesBalance = '';
        },
        //产销差
        productionSalesBalanceTimeChange2(value, mode) {
            this.productionSalesBalanceTime = value;
            this.mode2 = [
                mode[0] === 'date' ? 'month' : mode[0],
                mode[1] === 'date' ? 'month' : mode[1],
            ];
        },
        aaa() {
            this.axios.get('equipment/meterFlow/statistic', {
                params: {
                    moduleId: '2e1b78e76a8811ea9fded05099cd3eff'
                }
            })
            this.axios.get('/statistic/fc/day', {
                params: {
                    moduleId: '7892dbd36ccb11ea9fded05099cd3eff'
                }
            })
        }
    }
}