<template>
    <div class="big-dataMonitor">
        <div class="wO-title">
            <img class="i1" src="./images/i1.png" alt="">
            <div>实时数据监测</div>
            <img class="line2" src="./images/line2.png" alt="">
        </div>
        <div class="wO-content">
            <img class="bg4" src="./images/bg4.png" alt="">
            <div class="con-poA">
                <div class="poA-l">
                    <div class="t-top">
                        <img class="v-line" src="./images/v-line.png" alt="">
                        <div class="tit">水质分类</div>
                    </div>
                    <div class="t-center">
                        <div id="sunburst"></div>
                    </div>
                    <div class="t-bottom">
                        <div class="des">依据地表水水域环境功能和保护目标，按功能高低依次划分为五类；</div>
                        <div class="list">
                            <div class="item mr14">
                                <div class="bg11FFAB mr7"></div>
                                <div class="I">Ⅰ 类水质</div>
                            </div>
                            <div class="item mr14">
                                <div class="bg058BFF mr7"></div>
                                <div class="I">Ⅱ 类水质</div>
                            </div>
                            <div class="item mr40">
                                <div class="bg0C10FF mr7"></div>
                                <div class="I">Ⅲ 类水质</div>
                            </div>
                            <div class="item mr14">
                                <div class="bgFFD501 mr7"></div>
                                <div class="I">Ⅳ 类水质</div>
                            </div>
                            <div class="item mr14">
                                <div class="bgFF35C8 mr7"></div>
                                <div class="I">Ⅴ 类水质</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="poA-r">
                    <div class="t-top">
                        <img class="v-line" src="./images/v-line.png" alt="">
                        <div class="tit">水文数据</div>
                        <div class="tabs">
                            <div class="tab" :class="item.selected && 'selected'" @click="onSetChartTabChange2(index, 'tab')"
                                v-for="(item, index) in hydrology_tabs" :key="index">{{item.name}}</div>
                        </div>
                    </div>
                    <div class="bigChartBar-pick">
                        <div id="bigChartBar" v-if="showCanvas"></div>
                    </div>
                    
                    <!-- <template v-if="showCanvas">
                        <v-chart class="h-vChart" :forceFit="false" :height="190" :width="380" :series="{size: 0.01}" 
                            :data="dataSource" :padding="padding">
                            <v-tooltip />
                            <v-axis dataKey="x" :label="label" />
                            <v-axis dataKey="y" :label="label" />
                            <v-bar position="x*y" />
                        </v-chart>
                    </template> -->
                    <div class="des">
                        水文监测是指通过科学方法对自然界水的时空分布、变化规律进行监控、测量、分析以及预警等的一个复杂而全面的系统工程。水文监测由监测中心、通信网络、前端监测设备、测量设备四部分组成
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { axios } from '@/utils/request';
    import { getHydrologyList } from "@/api/api";
    import {ACCESS_TOKEN} from "@/store/mutation-types";
    const DataSet = require('@antv/data-set');
    const label = { 
        offset: 10 ,
        textStyle: {
            fill: '#ffffff',
        }
    };
    export default {
        name: "dataMonitor",
        data(){
            return {
                sunburstData: [],
                columnData: [],

                showCanvas: true,
                dataSource: [],
                padding: { top: 20, right: 20, bottom: 10, left: 20 },

                hydrologyIndex: 0, //当前水文数据选中项
                hydrologyList: [], //水文数据
                hydrology_tabs: [{
                    selected: true,
                    class: 'selected',
                    type: 'detectionOfLiquidLevel',
                    name: '监测液位'
                },{
                    selected: false,
                    class: 'selected',
                    type: 'currentSpeed',
                    name: '流速'
                },{
                    selected: false,
                    class: 'selected',
                    type: 'rainfall',
                    name: '雨量'
                },{
                    selected: false,
                    class: 'selected',
                    type: 'sedimentThickness',
                    name: '底泥厚度'
                }],
            }
        },
        created() {
            this.getDataMonitorData();
            this.getHydrologyList();
        },
        mounted() {
            // this.getDataBase()
            
            },
        methods: {
            getDataBase(){
                var chartDom = document.getElementById('bigChartBar');
                var myChart = echarts.init(chartDom);
                var option;
                option = {
                    width: 300,
                    height: 160,
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    color: ['#01926E'],
                    padding: [20,10,20,10],
                    xAxis: {
                        type: 'category',
                        data: this.xAxis
                    },
                    yAxis: {
                        type: 'value'   
                    },
                    series: [{
                        data: this.seriesData,
                        type: 'bar'
                    }]
                };
                option && myChart.setOption(option);

            },
            getDataMonitorData() {
                
                axios.get('rdp/info/water-quality/list', {}).then(res => {
                    if (res.code === 200) {
                        res.data.forEach((item, index) => {
                            if (item.name.includes('Ⅰ')) {
                                item.itemStyle = { color: '#11FFAB' };
                            } else if (item.name.includes('Ⅱ')) {
                                item.itemStyle = { color: '#058BFF' };
                            } else if (item.name.includes('Ⅲ')) {
                                item.itemStyle = { color: '#0C10FF' };
                            } else if (item.name.includes('Ⅳ')) {
                                item.itemStyle = { color: '#FFD501' };
                            } else if (item.name.includes('Ⅴ')) {
                                item.itemStyle = { color: '#FF35C8' };
                            }
                        })
                        this.sunburstData = res.data;
                        this.setSunburstChart();
                    }
                });
                axios.get('rdp/info/hydrology/list', {}).then(res => {
                    this.columnData = res.data;
                });
            },
            
            
            getHydrologyList() { //水文数据
                getHydrologyList().then(res => {
                    if (res.code == 200) {
                        this.hydrologyList = res.data;
                        this.onSetChartTabChange2( this.hydrologyIndex  )
                    } else {
                        this.$message.info(res.message);
                    }
                })
            },
            setSunburstChart() {
                console.log(this.sunburstData)
                var chartDom = document.getElementById('sunburst');
                var myChart = echarts.init(chartDom);
                var option;
                option = {
                    tooltip: {
                        trigger: 'item'
                    },
                    series: [
                        {
                            type: 'pie',
                            left: 'center',
                            radius: ['50%', '70%'],
                            width: 345,
                            avoidLabelOverlap: true,
                            label: {
                                show: true,
                                position: 'outside',
                                formatter: '{value|{c} %}',
                                minMargin: 5,
                                edgeDistance: 10,
                                lineHeight: 15,
                                rich: {
                                    value: {
                                        fontSize: 15,
                                        color: '#fff'
                                    }
                                }
                            },
                            labelLine: {
                                show: true,
                                length: 15,
                                length2: 35,
                                maxSurfaceAngle: 80,
                                lineStyle: {
                                    type: 'dashed',
                                },
                            },
                            data: this.sunburstData
                        }
                    ]
                };

                option && myChart.setOption(option);
            },
            
            onSetChartTabChange(index, is_tab) { //chart Tab切换
                let that = this;
                this.hydrologyIndex = index;
                this.hydrology_tabs.forEach((item, i) => {
                    item.selected = ( i == index );
                })
                if (is_tab === 'tab') {
                    this.showCanvas = false
                }
                clearTimeout(timer)
                var array = [];
                let timer = setTimeout(() => {
                    this.showCanvas = true;
                    this.dataSource = [];
                    this.hydrologyList.length && this.hydrologyList.forEach((item, i) => {
                        // 取出指定类型水文数据做为chart的data数据
                        array.push({ x: item.no, y: +item[this.hydrology_tabs[index].type] })
                    })
                    // const dv = new DataSet.View().source(array);
                    // dv.transform({
                    // type: 'sort',
                    // callback(a, b) {
                    //     return a.y - b.y > 0;
                    // },
                    // });
                    var i = 100
                    array.forEach(item => {
                        item.x = '001'
                        item.y = 100+i
                        i += 100
                    })
                    console.log(array)
                    this.dataSource = array;
                    // this.canvasScroll();
                })
            },
            onSetChartTabChange2(index, is_tab) { //chart Tab切换
                let that = this;
                let xAxis = [];
                let seriesData = [];
                this.hydrologyIndex = index;
                this.hydrology_tabs.forEach((item, i) => {
                    item.selected = ( i == index );
                })
                if (is_tab === 'tab') {
                    this.showCanvas = false
                }
                var array = [];
                setTimeout(() => {
                    this.showCanvas = true;
                    this.hydrologyList.length && this.hydrologyList.forEach((item, i) => {
                        // 取出指定类型水文数据做为chart的data数据
                        xAxis.push(item.no)
                        seriesData.push(+item[this.hydrology_tabs[index].type])
                        // array.push({ x: item.no, y: +item[this.hydrology_tabs[index].type] })
                    })
                    this.xAxis = xAxis;
                    this.seriesData = seriesData;
                    setTimeout(() => { this.getDataBase() });
                })
            },
        },

    }
</script>

<style lang="less" scoped>
    .big-dataMonitor {
        .wO-title {
            display: flex;
            align-items: center;
            padding-top: 30px;
            .i1 {
                width: 54px;
                height: 60px;
                margin-left: 35px;
            }
            div {
                flex-shrink: 0;
                font-size: 28px;
                color: #11FFAB;
                margin-right: 18px;
            }
            .line2 {
                width: 592px;
                height: 8px;
            }
        }
        .wO-content {
            position: relative;
            .bg4 {
                width: 848px;
                height: 436px;
            }
            .con-poA {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                display: flex;
                .poA-l {
                    padding: 0 33px 0 64px;
                    .t-center {
                        #sunburst {
                            width:  345px;
                            height: 200px;
                            margin: 10px auto;
                        }
                    }
                    .t-bottom {
                        padding-left: 12px;
                        .des {
                            width: 200px;
                            color: #CDE9C8;
                            font-size: 12px;
                            line-height: 24px;
                        }
                        .list {
                            display: flex;
                            flex-wrap: wrap;
                            padding-top: 30px;
                            .item {
                                display: flex;
                                align-items: center;
                                margin-bottom: 14px;
                            }
                        }
                    }
                }
                .poA-r {
                    .t-top {
                        width: 398px;
                        .tit {
                            margin-right: 46px;
                        }
                    }
                    .des {
                        width: 384px;
                        line-height: 24px;
                        background: #06170C;
                        border: 1px solid #71A377;
                        padding: 10px 14px;
                        margin-top: -10px;
                    }
                    .bigChartBar-pick {
                        width: 384px;
                        height: 273px;
                        margin-top: -10px;
                        #bigChartBar {
                            width: 100%;
                            height: 100%;
                        }
                    }
                }
                .t-top {
                    display: flex;
                    width: 328px;
                    height: 61px;
                    line-height: 1;
                    padding: 29px 0 14px 0;
                    border-bottom: 1px solid #194729;
                    .v-line {
                        flex-shrink: 0;
                        width: 10px;
                        height: 17px;
                        margin-right: 9px;
                    }
                    .tit {
                        color: #CDE9C8;
                        font-size: 18px;
                    }
                    .tabs {
                        display: flex;
                        line-height: 1.1;
                        .tab {
                            color: #CDE9C8;
                            padding: 3px 6px;
                            border: 1px dashed #194729;
                            margin-right: 9px;
                            &.selected {
                                color: #0AE88B;
                            }
                        }
                    }
                }
            }
        }

        .h-vChart {
            height: 240px;
            overflow: hidden;
        }
    }
.ellipsis {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}


.bg11FFAB {
    width: 12px;
    height: 12px;
    background: #11FFAB;
}
.bg058BFF {
    width: 12px;
    height: 12px;
    background: #058BFF;
}
.bg0C10FF {
    width: 12px;
    height: 12px;
    background: #0C10FF;
}
.bgFFD501 {
    width: 12px;
    height: 12px;
    background: #FFD501;
}
.bgFF35C8 {
    width: 12px;
    height: 12px;
    background: #FF35C8;
}
.mr7 {margin-right: 7px;}
.mr14 {margin-right: 14px;}
.mr20 {margin-right: 20px;}
.mr40 {margin-right: 40px;}
</style>