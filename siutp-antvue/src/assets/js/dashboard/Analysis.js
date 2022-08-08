import { 
    getWorkOrderResolved, 
    getWorkOrderUnResolved,  
    getRealTimeWarningInfo, 
    getHydrologyList, 
    getRegionsList, 
    getRiversList, 
    getProjectList,
    limitRole
} from "@/api/api";

const styleJson = require('@/assets/js/custom_map_config.json');
const DataSet = require('@antv/data-set');
 const label = { 
    offset: 10 ,
    textStyle: {
      fill: '#ffffff',
    }
};

export default {
    name: "homeIndexMap",
    data(){
        return {
            limited:[],
            page_tabs: [{
                name: '信息中心',
                type: 'pic-center',
                url: require('@/views/dashboard/images/information.png'),
                urlLeave: require('@/views/dashboard/images/information.png'),
                urlHover: require('@/views/dashboard/images/information_hover.png'),
                urlHttp: 'operationCenter-equipmentSearcher-RequipmentList',
            },{
                name: '运营中心',
                type: 'area-chart',
                url: require('@/views/dashboard/images/operate.png'),
                urlLeave: require('@/views/dashboard/images/operate.png'),
                urlHover: require('@/views/dashboard/images/operate_hover.png'),
                urlHttp: 'operationCenter-mapIndex',
            },{
                name: '安全中心',
                type: 'lock',
                url: require('@/views/dashboard/images/safe.png'),
                urlLeave: require('@/views/dashboard/images/safe.png'),
                urlHover: require('@/views/dashboard/images/safe_hover.png'),
                urlHttp: 'monitor-video',
            },{
                name: '结算中心',
                type: 'account-book',
                urlLeave: require('@/views/dashboard/images/settlement.png'),
                url: require('@/views/dashboard/images/settlement.png'),
                urlHover: require('@/views/dashboard/images/settlement_hover.png'),
                urlHttp: 'measurementCenter-customerInfo',
            },{
                name: '设计中心',
                type: 'edit',
                url: require('@/views/dashboard/images/devise.png'),
                urlLeave: require('@/views/dashboard/images/devise.png'),
                urlHover: require('@/views/dashboard/images/devise_hover.png'),
                urlHttp: '',
            }],
            hydrologyIndex: 0,
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
            },],
            workOrderResolvedInfo: '',
            workOrderUnResolvedInfo:'',
            realTimeWarningInfo: [],
            dataSource: [],
            padding: [],
            showCanvas: true,
            show_mapBack: false,
            mapIcon: require('@/assets/images/mapIndex/iconHDot.png'),
            current_regionId: 0,
            workOrderBg: require('@/views/dashboard/images/workorder_bg.png'),
            giveAlarmBg: require('@/views/dashboard/images/giveAlarm_bg.png'),
            hydrologyBg: require('@/views/dashboard/images/chart_bg.png'),
            label: label,
        }
    },
    methods: {
        getData() {
            getWorkOrderResolved().then(res => { //工单1
                if (res.code == 200) {
                    console.log(res.data,'===========================解决的')
                    this.workOrderResolvedInfo = res.data;
                } else {
                    this.$message.info(res.message);
                }
            });
            getWorkOrderUnResolved().then(res => { //工单2
                if (res.code == 200) {
                    console.log(res.data,'===========================未解决的')

                    this.workOrderUnResolvedInfo = res.data;
                } else {
                    this.$message.info(res.message);
                }
            });
            this.getRealTimeWarningInfo();
            this.getHydrologyList();
            // setInterval(() => { //定时更新接口数据
            //     this.getRealTimeWarningInfo();
            //     this.getHydrologyList();
            // }, 5000);
        },
        getRealTimeWarningInfo() { //告警信息
            getRealTimeWarningInfo().then(res => {
                if (res.code == 200) {
                    this.realTimeWarningInfo = res.data.responseData.data;
                } else {
                    this.$message.info(res.message);
                }
            });
        },
        getHydrologyList() { //水文数据
            getHydrologyList().then(res => {
                if (res.code == 200) {
                    this.hydrologyList = res.data;
                    this.onSetChartTabChange( this.hydrologyIndex,  )
                } else {
                    this.$message.info(res.message);
                }
            })
        },
        getProjectList(params, Map) { //获取区域重点项目
            if (this.current_regionId === params.regionId) return
            this.current_regionId = params.regionId
            getProjectList(params).then(res => {
                if (res.code == 200) {
                    let result = res.data;
                    Map.clearOverlays(); //清除点位重绘
                    // console.log(result[0].modelImg.imgUrl) //接口字段-待更换
                    let myIcon = new BMapGL.Icon(this.mapIcon, new BMapGL.Size(26, 26));
                    var opts = {
                        width: 0,
                        title: '提示',
                        enableMessage: true, //设置允许信息窗发送短息
                        message: ""
                    };
                    result.forEach(item => {
                        let point = new BMapGL.Point( item.longitude, item.latitude );
                        let marker = new BMapGL.Marker( point, { icon: myIcon } );  // 创建标注
                        Map.addOverlay(marker)
                        let infoWindow = new BMapGL.InfoWindow('项目名称：'+item.modelName, opts);  // 创建信息窗口
                        marker.addEventListener("click", () => {
                            Map.openInfoWindow(infoWindow, point);
                        }); 
                    })
                } else {
                    this.$message.info(res.message);
                }
            });
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
                const dv = new DataSet.View().source(array);
                dv.transform({
                  type: 'sort',
                  callback(a, b) {
                    return a.y - b.y > 0;
                  },
                });
                this.dataSource = dv.rows;
                // this.padding = [20,20,50,40];
                this.padding = { top: 20, right: 30, bottom: 20, left: 60 }
                // this.canvasScroll();
            })
        },
        canvasScroll() { //鼠标滚轮控制canvas可左右移动
            // let timer = setInterval(() => {
            let timer = setTimeout(() => {
                $(".h-vChart canvas").each((i, element) => {
                    element.onwheel = eve => {
                        eve.preventDefault(); 
                        $(element).parent().css({'overflow-x':'scroll', 'overflow-y':'hidden', });
                        var table = $(element).parent();
                        var right = $(element).width()-table[0].offsetWidth;
                        if (table.scrollLeft()<right&&eve.deltaY>0) {
                            table.scrollLeft(table.scrollLeft() + 20); 
                        } else if (table.scrollLeft()>0&&eve.deltaY<0) {
                            table.scrollLeft(table.scrollLeft() - 20);
                        }
                    }
                })
                if ($(".h-vChart canvas").length) {
                    clearInterval(timer)
                }
            }, 1000);
        },
        warnLevel(value) { //告警等级
            return value==='一般'?'0' : value==='警告'?'1' : value==='严重'?'2' : value==='紧急'?'3' : ''
        },
        mouseOver(e) {//左侧选项
            this.page_tabs[e].url = this.page_tabs[e].urlHover
        },
        mouseLeave(e) {
            this.page_tabs[e].url = this.page_tabs[e].urlLeave
        }
    },
    created(){
        limitRole().then((res)=>{
           this.limited=res.result;
           console.log(this.limited,'============================')
        })
    },
    mounted() {
        this.getData();
        //设置地图容器
        let Map = null;
        Map = new BMapGL.Map('home-allmap', {
            enableDblclickZoom: false,
            minZoom:12.4,
            maxZoom:15,
            displayOptions: {
                building: false
            }
        });
        Map.centerAndZoom(new BMapGL.Point(104.7232, 31.4564), 12);
        Map.setMapStyleV2({styleJson:styleJson});
        Map.enableScrollWheelZoom(true);

        var resizeOk = true;
        var mapBack = true;

        $(window).resize(() => {
            // Map.checkResize();
            if(!resizeOk || !mapBack)
            {
                //上次没有完全执行完就跳过
                return;
            }
            resizeOk = false;
            mapBack = false;
            var timer=setTimeout(() => {
                Map.destroy();//销毁地图
                Map = null;
                Map = new BMapGL.Map('home-allmap', {
                    enableDblclickZoom: false,
                    minZoom:12.4,
                    maxZoom:15,
                    displayOptions: {
                        building: false
                    }
                });
                Map.centerAndZoom(new BMapGL.Point(104.7232, 31.4564), 12);
                Map.setMapStyleV2({styleJson:styleJson});
                Map.enableScrollWheelZoom(true);

                getRegionsList().then(res => { //设备区域数据
                    if (res.code == 200) {
                        let result = res.data;
                        let latLng = {};
                        let click_polygon = null;
                        // Map.setDisplayOptions({ poiIcon: false }); //隐藏图标
                        result.map((item,index) => {
                            let polygon = new BMapGL.Polygon(res.data[index].regionAddressInfo, {
                                strokeColor: '#5a9de2',
                                strokeWeight: 5,
                                fillColor: '#142950 ',
                                fillOpacity: 0.5,
                                enableMassClear: false
                            }) //绘制区域
                            // polygon.setFillOpacity(0.6)
                            Map.addOverlay(polygon)
                            polygon.addEventListener('click', e => {
                                // 区域绑定事件-查看区域下重点项目
                                latLng = e.latLng;
                                // latLng = e.point;
                                // click_polygon && click_polygon.setFillOpacity(0.2);
                                click_polygon = polygon;
                                this.getProjectList({regionId: res.data[index].id}, Map)
                                this.show_mapBack = true;
                                // polygon.setFillOpacity(0);
                                polygon.setFillOpacity(0.01);
                                Map.centerAndZoom(new BMapGL.Point(latLng.lng, latLng.lat), 14);

                                // Map.setDisplayOptions({ poiText: false });
                            });
                        })
                        // let timer = setInterval(() => {
                        setTimeout(() => {
                            // 绑定事件-从重点项目级别返回区域级别
                            document.querySelector('.map-back').addEventListener('click', () => {
                                Map.clearOverlays();
                                click_polygon.setFillOpacity(0.2);
                                // Map.setDisplayOptions({ poiText: true });
                                Map.centerAndZoom(new BMapGL.Point(latLng.lng, latLng.lat), 11);
                                this.show_mapBack = false;
                                this.current_regionId = 0;
                                mapBack = true;
                            });
                        }, 1000);
                    } else {
                        this.$message.info(res.message);
                    }
                });
        
                getRiversList().then(res => { //设备河流数据
                    if (res.code == 200) {
                        let result  = res.data;
                        for (var i in result) {
                            var pois = [];
                            let rivers = JSON.parse(result[i].riverAddressInfo);
                            for (var j = 0; j < rivers.length; j++) {
                                pois.push(new BMapGL.Point(rivers[j].longitude, rivers[j].latitude));
                            }
                            // 绘制线
                            var polyline;
                            polyline = new BMapGL.Polyline(pois, {
                                strokeColor: '#00E3FF',
                                strokeWeight: 5,
                                strokeOpacity: 1,
                                enableMassClear: false
                            });
                            Map.addOverlay(polyline);
                        }
                    } else {
                        this.$message.info(res.message);
                    }
                });
                resizeOk = true;
                //clearInterval(timer);
            }, 200);
        });

        // let bd = new BMapGL.Boundary();
        Map.addEventListener('mousedown', function (e) {
            // alert('点击位置经纬度：' + e.latlng.lng + ',' + e.latlng.lat);
            var distance=Map.getDistance(new BMapGL.Point(104.7232, 31.4564),new BMapGL.Point(e.latlng.lng, e.latlng.lat));
            if(distance>100000){
                Map.centerAndZoom(new BMapGL.Point(104.7232, 31.4564), 12);
            }
        //    console.log(distance,'==================================================')
        });

        getRegionsList().then(res => { //设备区域数据
            if (res.code == 200) {
                let result = res.data;
                let latLng = {};
                let click_polygon = null;
                // Map.setDisplayOptions({ poiIcon: false }); //隐藏图标
                result.map((item,index) => {
                    let polygon = new BMapGL.Polygon(res.data[index].regionAddressInfo, {
                        strokeColor: '#5a9de2',
                        strokeWeight: 5,
                        fillColor: '#142950 ',
                        fillOpacity: 0.5,
                        enableMassClear: false
                    }) //绘制区域
                    // polygon.setFillOpacity(0.6)
                    Map.addOverlay(polygon)
                    polygon.addEventListener('click', e => {
                        // 区域绑定事件-查看区域下重点项目
                        latLng = e.latLng;
                        // latLng = e.point;
                        // click_polygon && click_polygon.setFillOpacity(0.2);
                        click_polygon = polygon;
                        this.getProjectList({regionId: res.data[index].id}, Map)
                        this.show_mapBack = true;
                        // polygon.setFillOpacity(0);
                        polygon.setFillOpacity(0.01);
                        Map.centerAndZoom(new BMapGL.Point(latLng.lng, latLng.lat), 14);

                        // Map.setDisplayOptions({ poiText: false });
                    });
                })
                // let timer = setInterval(() => {
                setTimeout(() => {
                    // 绑定事件-从重点项目级别返回区域级别
                    if ( document.querySelector('.map-back') ) {
                        document.querySelector('.map-back').addEventListener('click', () => {
                            Map.clearOverlays();
                            click_polygon.setFillOpacity(0.2);
                            // Map.setDisplayOptions({ poiText: true });
                            Map.centerAndZoom(new BMapGL.Point(latLng.lng, latLng.lat), 11);
                            this.show_mapBack = false;
                            this.current_regionId = 0;
                        });
                    }
                }, 1000);
            } else {
                this.$message.info(res.message);
            }
        });
        
        getRiversList().then(res => { //设备河流数据
            if (res.code == 200) {
                let result  = res.data;
                for (var i in result) {
                    var pois = [];
                    let rivers = JSON.parse(result[i].riverAddressInfo);
                    for (var j = 0; j < rivers.length; j++) {
                        pois.push(new BMapGL.Point(rivers[j].longitude, rivers[j].latitude));
                    }
                    // 绘制线
                    var polyline;
                    polyline = new BMapGL.Polyline(pois, {
                        strokeColor: '#00E3FF',
                        strokeWeight: 5,
                        strokeOpacity: 1,
                        enableMassClear: false
                    });
                    Map.addOverlay(polyline);
                }
            } else {
                this.$message.info(res.message);
            }
        });

        
    },
    updated() {

    },
}