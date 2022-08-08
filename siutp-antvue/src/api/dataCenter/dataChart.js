import {
    downFile
} from '@/api/manage'
import searchReset from '@/mixins/searchReset.js'
import {axios} from '@/utils/request'

export default {
    name: 'dataChart',
    mixins: [searchReset],
    data() {
        return {
            name: '数据图表',
            description: '数据图表',
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
            wdTime: [],
            ylTime: [],
            zdTime: [],
            phTime: [],
            yulvTime: [],
            lljTime: [],
            statisticalItemsList: [],
            hdLLBTime: {},
            allChartList: [], // 所有图表列表
            isOpen: true,
            echartsVisible: false,
            startTimeEcharts: '',
            endTimeEcharts: '',
            singleId: '', // 单个图表的id
            singleIndex: '', // 单个图表的index
        }
    },
    mounted() {
        //给每个图表添加宽度
        setTimeout(() => {
            for (let i = 0; i < $('.chartDivBox').length; i++) {
                $('.container').eq(i).width($('.chartDivBox').eq(i).width());
            }
            this.allChartList.forEach((item, index) => {
                this.searchQuery(item.id, ('container' + index), index)
            })
        }, 500)

        //平均值分析-统计项目下拉值获取
        this.getStatisticalItemsList(this);
        //图表初始化
        // this.updata();
        // 获取所有图表信息
        this.getAllChart(this);
    },
    methods: {
        getAllChart(_this) {
            axios({
                url: "/statistic/chart/view/charts",
                methods: "get"
            }).then(res => {
                if (res.code == 200) {
                    _this.allChartList = res.result;
                    let data = {
                        left: _this.lljTime.length ? _this.moment(_this.lljTime[0]).format('YYYY-MM-DD HH:mm') : '',
                        right: _this.lljTime.length ? _this.moment(_this.lljTime[1]).format('YYYY-MM-DD HH:mm') : ''
                    }
                }
            })
        },
        getStatisticalItemsTableList(data, _this) {
            axios.get('/iot/vardata/analysis/ce3992ca69c911ea9fded05099cd3eff', {
                params: data
            })
                .then(res => {
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
        },
        getStatisticalItemsList(_this) {
            axios.get('/iot/vardata/analysis/item/ce3992ca69c911ea9fded05099cd3eff')
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.statisticalItemsList = res.result;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
        },
        getChartView(data, _this, id) {
            axios.post('/statistic/chart/view/' + data.id, {
                left: data.left,
                right: data.right
            }).then(res => {
                if (res.code * 1 == 200) {
                    let obj = res.result;
                    let obj2 = res.result.charsValue;
                    _this.dataChart(id, obj, obj2);


                } else {
                    _this.$message.info(res.message);
                }
            })
        },
        // 时间控制
        changeDateTotal(type) {
            let s = this.queryParam.startTime ? new Date(this.queryParam.startTime).getTime() : null
            let e = this.queryParam.endTime ? new Date(this.queryParam.endTime).getTime() : null
            if (e && s) {
                if (type === 'right' && e < s) {
                    this.$message.error('结束时间需要大于开始时间')
                    this.queryParam.endTime = null
                }
                if (type === 'left' && e < s) {
                    this.$message.error('开始时间需要小于结束时间')
                    this.queryParam.startTime = null
                }
            }
        },
        // 改变日期
        changeDate(val, i, type) {
            let s = this.hdLLBTime['left_' + i] ? new Date(this.hdLLBTime['left_' + i]).getTime() : null
            let e = this.hdLLBTime['right_' + i] ? new Date(this.hdLLBTime['right_' + i]).getTime() : null
            if (e && s) {
                if (type === 'right' && e < s) {
                    this.$messageTips('结束时间需要大于开始时间')
                    this.hdLLBTime['right_' + i] = null
                }

                console.log(e, s)
                if (type === 'left' && e < s) {
                    this.$messageTips('开始时间需要小于结束时间')
                    this.hdLLBTime['left_' + i] = null
                }
            }
        },
        // 搜索
        searchQueryTotal() {
            for (let i = 0; i < $('.chartDivBox').length; i++) {
                $('.container').eq(i).width($('.chartDivBox').eq(i).width());
            }
            this.allChartList.forEach((item, index) => {
                let _this = this
                let data = {
                    id: item.id,
                    left: this.queryParam.startTime ? this.moment(this.queryParam.startTime).format('YYYY-MM-DD HH:mm:ss') : '',
                    right: this.queryParam.endTime ? this.moment(this.queryParam.endTime).format('YYYY-MM-DD HH:mm:ss') : ''
                }
                _this.$set(this.hdLLBTime, 'left_' + index, this.queryParam.startTime)
                _this.$set(this.hdLLBTime, 'right_' + index, this.queryParam.endTime)
                this.getChartView(data, this, ('container' + index));
            })
        },
        // 搜索
        searchQuery(id, container, index, searchData) {
            let data = {
                id,
                left: this.hdLLBTime['left_' + index] ? this.moment(this.hdLLBTime['left_' + index]).format('YYYY-MM-DD HH:mm:ss') : '',
                right: this.hdLLBTime['right_' + index] ? this.moment(this.hdLLBTime['right_' + index]).format('YYYY-MM-DD HH:mm:ss') : ''
            }
            console.log(this.hdLLBTime['right_' + index], 'data')
            this.getChartView(data, this, container);
        },
        // 图形初始化
        openEcharts(id, container, index, searchData) {
            let data = {
                id,
                left: '',
                right: '',
            }
            if (this.hdLLBTime['left_' + index] == undefined) {
                data.left = '';
                this.startTimeEcharts = '';
            } else if (this.hdLLBTime['left_' + index]) {
                data.left = this.moment(this.hdLLBTime['left_' + index]).format('YYYY-MM-DD HH:mm:ss');
                this.startTimeEcharts = this.moment(this.hdLLBTime['left_' + index]).format('YYYY-MM-DD HH:mm:ss')
            } else {
                data.left = '';
                this.startTimeEcharts = ''
            }
            if (this.hdLLBTime['right_' + index] == undefined) {
                data.right = '';
                this.endTimeEcharts = '';
            } else if (this.hdLLBTime['right_' + index]) {
                data.right = this.moment(this.hdLLBTime['right_' + index]).format('YYYY-MM-DD HH:mm:ss');
                this.endTimeEcharts = this.moment(this.hdLLBTime['right_' + index]).format('YYYY-MM-DD HH:mm:ss');
            } else {
                data.right = '';
                this.endTimeEcharts = '';
            }
            this.echartsVisible = true;
            this.singleId = id;
            this.singleIndex = index;
            $('#echartsCon').width('100%')
            this.getChartView(data, this, 'echartsCon');
        },
        // 关闭图形
        closeEcharts(id, container, index, searchData) {
            let data = {
                id,
                left: this.hdLLBTime['left_' + index] ? this.moment(this.hdLLBTime['left_' + index]).format('YYYY-MM-DD HH:mm:ss') : '',
                right: this.hdLLBTime['right_' + index] ? this.moment(this.hdLLBTime['right_' + index]).format('YYYY-MM-DD HH:mm:ss') : ''
            }
            this.getChartView(data, this, container);
        },
        // 关闭图形
        handleClose() {
            this.echartsVisible = false;
            this.$set(this.hdLLBTime, 'left_' + this.singleIndex, this.startTimeEcharts)
            this.$set(this.hdLLBTime, 'right_' + this.singleIndex, this.endTimeEcharts)
            let data = {
                id: this.singleId,
                left: this.hdLLBTime['left_' + this.singleIndex] ? this.moment(this.hdLLBTime['left_' + this.singleIndex]).format('YYYY-MM-DD HH:mm:ss') : '',
                right: this.hdLLBTime['right_' + this.singleIndex] ? this.moment(this.hdLLBTime['right_' + this.singleIndex]).format('YYYY-MM-DD HH:mm:ss') : ''
            }
            this.getChartView(data, this, 'container' + this.singleIndex);
        },
        // 图表搜索
        searchQueryEcharts() {
            let data = {
                id: this.singleId,
                left: this.startTimeEcharts ? this.moment(this.startTimeEcharts).format('YYYY-MM-DD HH:mm:ss') : '',
                right: this.endTimeEcharts ? this.moment(this.endTimeEcharts).format('YYYY-MM-DD HH:mm:ss') : ''
            }
            this.getChartView(data, this, 'echartsCon');
        },
        // 改变图表数据
        changeDateEcharts(val, type) {
            let s = this.startTimeEcharts ? new Date(this.startTimeEcharts).getTime() : null
            let e = this.endTimeEcharts ? new Date(this.endTimeEcharts).getTime() : null
            if (e && s) {
                if (type === 'right' && e < s) {
                    this.$messageTips('结束时间需要大于')
                    this.endTimeEcharts = null
                }
                if (type === 'left' && e < s) {
                    this.startTimeEcharts = null
                }
            }
        },
        //图表初始化
        dataChart(id, json, json2) {
            let arr = [];
            let colors = ['#1E90FF', '#00CDCD', '#0099CC', '#76EEC6', '#FFB90F', '#EE6363', '#FF00FF',
                '#DDA0DD', '#9370DB', '#8A2BE2', '#97FFFF'
            ];
            json2.map((index, i) => {
                arr.push({ //一条延伸到整个绘图区的线，标志着轴中一个特定值。
                        color: colors[i],
                        dashStyle: 'Dash', //Dash,Dot,Solid,默认Solid
                        width: 1,
                        value: index.max, //y轴显示位置
                        zIndex: 5,
                        label: {
                            text: index.name + '峰值：' + index.max, //标签的内容
                            align: 'left', //标签的水平位置，水平居左,默认是水平居中center
                            x: 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
                        }
                    },
                    { //一条延伸到整个绘图区的线，标志着轴中一个特定值。
                        color: colors[i],
                        dashStyle: 'Dash', //Dash,Dot,Solid,默认Solid
                        width: 1,
                        value: index.min, //y轴显示位置
                        zIndex: 5,
                        label: {
                            text: index.name + '谷值：' + index.min, //标签的内容
                            align: 'right', //标签的水平位置，水平居左,默认是水平居中center
                            x: 10 //标签相对于被定位的位置水平偏移的像素，重新定位，水平居左10px
                        }
                    })
            });
            Highcharts.setOptions({
                colors: ['#1E90FF', '#00CDCD', '#0099CC', '#76EEC6', '#FFB90F', '#EE6363', '#FF00FF',
                    '#DDA0DD', '#9370DB', '#8A2BE2', '#97FFFF'],
                lang: {
                    resetZoom: '重置',
                    resetZoomTitle: '重置缩放比例'
                },
                global: {
                    useUTC: false
                }
            });
            var chart = Highcharts.chart(id, {
                chart: {
                    type: json.type,
                    zoomType: 'x'
                },
                title: {
                    text: ''
                },
                subtitle: {
                    text: ''
                },
                xAxis: {
                    type: 'datetime',
                    dateTimeLabelFormats: {
                        millisecond: '%H:%M:%S',
                        second: '%H:%M:%S',
                        minute: '%H:%M',
                        hour: '%H:%M',
                        day: '%Y-%m-%d',
                        week: '%Y-%m-%d',
                        month: '%Y-%m',
                        year: '%Y'
                    }
                },
                tooltip: {
                    dateTimeLabelFormats: {
                        millisecond: '%Y-%m-%d %H:%M:%S',
                        second: '%Y-%m-%d %H:%M:%S',
                        minute: '%Y-%m-%d %H:%M:%S',
                        hour: '%Y-%m-%d %H:%M:%S',
                        day: '%m-%d',
                        week: '%m-%d',
                        month: '%Y-%m',
                        year: '%Y'
                    }
                },
                yAxis: {
                    title: {
                        text: json.title
                    },
                    plotLines: arr
                },
                legend: {
                    enabled: true
                },
                plotOptions: {
                    area: {
                        fillColor: {
                            linearGradient: {
                                x1: 0,
                                y1: 0,
                                x2: 0,
                                y2: 1
                            },
                            stops: [
                                [0, new Highcharts.getOptions().colors[0]],
                                [1, new Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                            ]
                        },
                        marker: {
                            radius: 2
                        },
                        lineWidth: 1,
                        states: {
                            hover: {
                                lineWidth: 1
                            }
                        },
                        threshold: null
                    },
                    series: {
                        events: {
                            legendItemClick: function (event) {
                                console.log(this.visible);
                                console.log(this.name);
                                console.log(this.yAxis.userOptions.plotLines);
                                this.yAxis.userOptions.plotLines = [];
                            }
                        }
                    }

                },
                series: json.series
            });
        },
        //平均值分析初始化
        updata() {
            let data = {
                typeCode: this.queryParam.statisticalItems ? this.queryParam.statisticalItems : '',
                startTime: this.queryParam.statisticalInterval.length ? new Date(this.moment(this.queryParam.statisticalInterval[0]).format('YYYY-MM-DD HH:mm:ss')).getTime() : '',
                endTime: this.queryParam.statisticalInterval.length ? new Date(this.moment(this.queryParam.statisticalInterval[1]).format('YYYY-MM-DD HH:mm:ss')).getTime() : ''
            }
            this.getStatisticalItemsTableList(data, this);
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
            param.startTime = this.queryParam.statisticalInterval.length ? new Date(this.moment(this.queryParam.statisticalInterval[0]).format('YYYY-MM-DD HH:mm:ss')).getTime() : '';
            param.endTime = this.queryParam.statisticalInterval.length ? new Date(this.moment(this.queryParam.statisticalInterval[1]).format('YYYY-MM-DD HH:mm:ss')).getTime() : '';
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