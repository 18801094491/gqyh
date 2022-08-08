import {
    axios
} from '@/utils/request';
import moment from 'moment'

export default {
    name: 'dayAccountFlow',
    data() {
        return {
            queryParam: {
                region: '',
                dataTime: new Date(new Date().getTime() - 7 * 60 * 60 * 24 * 1000),
                timeLength: '7',
                share: 'd',
                spaceLength: '1',
                space: 'd',  // 查询间隔
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
                    title: '日期',
                    align: "center",
                    dataIndex: 'countDate'
                },
                {
                    title: '河东(供)',
                    align: "center",
                    dataIndex: 'flowHd'
                },
                {
                    title: '行政区',
                    align: "center",
                    dataIndex: 'flowXzq'
                },
                {
                    title: '行政区入楼',
                    align: "center",
                    dataIndex: 'flowXzrl'
                },
                {
                    title: '行政区绿化',
                    align: "center",
                    dataIndex: 'flowXzlh'
                },
                {
                    title: '镜河补水',
                    align: "center",
                    dataIndex: 'flowFzg'
                },
                {
                    title: '华电北燃',
                    align: "center",
                    dataIndex: 'flowHdbr'
                },
            ],
            dataSource: [],
            loading: false,
            ipagination: {
                current: 1, // pageNo
                pageSize: 10,
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                total: 0
            },
            jumpPagNum: '',
            regionList: [{ value: '0', title: '河东' }, { value: '1', title: '行政区' }, { value: '2', title: '行政区入楼' }],
            shareList: [{ value: 'M', title: '月' }, { value: 'd', title: '日' }],
            spaceList: [],
        }
    },
    mounted() {
        this.updata();
        this.changeSpaceList(this.queryParam.share)
    },
    methods: {
        moment,
        // 页码
        toPage() {
            this.ipagination.current = this.jumpPagNum * 1;
            this.updata();
        },
        // 左侧数据
        disabledDateLeft(current) {
            if (!this.queryParam.right) return false;
            const endValue = new Date(this.queryParam.right.format("YYYY-MM-DD")).getTime();
            const startValue = new Date(current.format("YYYY-MM-DD")).getTime();
            return startValue > endValue;
        },
        // 事件数据
        disabledTimeLeft(current) {
            if (!this.queryParam.right) return false;
            if (!current) current = this.queryParam.right;
            const endValue = new Date(this.queryParam.right.format("YYYY-MM-DD")).getTime();
            const startValue = new Date(current.format("YYYY-MM-DD")).getTime();
            if (endValue !== startValue) return false;
            let rh = new Date(this.queryParam.right).getHours();
            let rm = new Date(this.queryParam.right).getMinutes();
            let rs = new Date(this.queryParam.right).getSeconds();
            let lh = new Date(current).getHours();
            let lm = new Date(current).getMinutes();

            return {
                disabledHours: () => {
                    const result = [];
                    for (let i = 23; i > rh && i >= 0; i--) {
                        result.push(i);
                    }
                    return result;
                },
                disabledMinutes: () => {
                    const result = [];
                    if (rh !== lh) return result;
                    for (let i = 59; i < rm && i >= 0; i--) {
                        result.push(i);
                    }
                    return result;
                },
                disabledSeconds: () => {
                    const result = [];
                    if (rh !== lh || rm !== lm)
                        for (let i = 59; i < rs && i >= 0; i--) {
                            result.push(i);
                        }
                    return result;
                }
            }
        },
        // 右侧数据
        disabledDateRight(current) {
            if (!this.queryParam.left) return false;
            const startValue = new Date(this.queryParam.left.format("YYYY-MM-DD")).getTime();
            const endValue = new Date(current.format("YYYY-MM-DD")).getTime();
            return startValue > endValue;
        },
        // 右侧事件数据
        disabledTimeRight(current) {
            if (!this.queryParam.left) return false;
            if (!current) current = this.queryParam.left;
            const startValue = new Date(this.queryParam.left.format("YYYY-MM-DD")).getTime();
            const endValue = new Date(current.format("YYYY-MM-DD")).getTime();
            if (endValue !== startValue) return false;
            let lh = new Date(this.queryParam.left).getHours();
            let lm = new Date(this.queryParam.left).getMinutes();
            let ls = new Date(this.queryParam.left).getSeconds();
            let rh = new Date(current).getHours();
            let rm = new Date(current).getMinutes();

            return {
                disabledHours: () => {
                    const result = [];
                    for (let i = 0; i < lh; i++) {
                        result.push(i);
                    }
                    return result;
                },
                disabledMinutes: () => {
                    const result = [];
                    if (rh !== lh) return result;
                    for (let i = 0; i < lm; i++) {
                        result.push(i);
                    }
                    return result;
                },
                disabledSeconds: () => {
                    const result = [];
                    if (rh !== lh || rm !== lm) return result;
                    for (let i = 0; i < ls; i++) {
                        result.push(i);
                    }
                    return result;
                }
            }
        },
        // 改变事件
        changeSpaceList(val) {
            this.spaceList = [];
            if (val == 'M') {
                this.queryParam.space = 'M';
                this.spaceList.push(
                    { value: 'M', title: '月' }, { value: 'd', title: '日' }
                )
            }
            if (val == 'd') {
                this.queryParam.space = 'd'
                this.spaceList.push({ value: 'd', title: '日' })
            }
        },
        //数据初始化
        updata() {
            let data = {
                left: this.queryParam.dataTime ? this.moment(this.queryParam.dataTime).format('YYYY-MM-DD') : '',
                gap: this.queryParam.timeLength, // 时间长度
                timeUnit: this.queryParam.share, // 时间长度单位
                particle: this.queryParam.spaceLength, // 查询间隔
                particleUnit: this.queryParam.space, // 查询间隔单位
            };
            (function (data, _this) {
                axios({
                    url: "/settle/flowDayRecord",
                    methods: "get",
                    params: data
                }).then(res => {
                    if (res.code * 1 == 200) {
                        _this.dataSource = [];
                        let result = res.result;
                        _this.dataSource = result
                    } else {
                        _this.$message.info(res.message)
                    }
                })
                axios({
                    url: "/settle/flowDayRecord/chart",
                    methods: "get",
                    params: data
                }).then(res => {
                    if (res.code * 1 == 200) {
                        let result = res.result;
                        _this.dataChart(result)
                    } else {
                        _this.$message.info(res.message)
                    }
                })
            }(data, this))
        },
        // 渲染chart
        dataChart(obj) {
            let _this = this;
            let arr = [];
            let colors = ['#1E90FF', '#00CDCD', '#0099CC', '#76EEC6', '#FFB90F', '#EE6363', '#FF00FF',
                '#DDA0DD', '#9370DB', '#8A2BE2', '#97FFFF'
            ];
            let maxLength = 10
            if (obj.xcategories && obj.xcategories.length < maxLength) {
                maxLength = obj.xcategories.length
            }
            let flag = false;
            if (maxLength >= 10) {
                flag = true;
            }
            var chart = Highcharts.chart('lineCon', {
                colors: ['rgb(124, 181, 236)', 'rgb(67, 67, 72)', 'rgb(144, 237, 125)', 'rgb(247, 163, 92)', 'rgb(128, 133, 233)', 'rgb(15, 72, 127)'],
                chart: {
                    type: 'column'
                },
                title: {
                    text: obj.mainTitle
                },
                xAxis: {
                    categories: obj.xcategories,
                    crosshair: true,
                    min: 0,
                    max: maxLength - 1,
                    scrollbar: {
                        enabled: flag
                    },
                },
                yAxis: {
                    title: {
                        text: obj.ytitle
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        borderWidth: 0
                    }
                },
                series: obj.series
            });
        },
        //查询
        searchQuery() {
            this.updata();
        },
        //重置
        searchReset() {
            this.queryParam = {
                region: '',
                dataTime: new Date(new Date().getTime() - 7 * 60 * 60 * 24 * 1000),
                timeLength: '7',
                share: 'd',
                spaceLength: '1',
                space: 'd',
            };
        }
    }
}
//在dayAccountFlow 组件混入dayAccountFlow.js。