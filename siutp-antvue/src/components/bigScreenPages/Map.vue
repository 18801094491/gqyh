<template>
    <div class="big-map">
        <header-notice style="display:none"/>
        <div class="map-left">
            <img class="map-lr" src="./images/map-l.png" mode="" />
            <div class="map-l_iBox">
                <div :class="'map-l_i ' + item.selected" v-for="(item, index) in map_liBox" :key="index" @click="onMarkerCutChange(item)">
                    <img :src="item.icon" alt="">
                    <div class="des">{{item.des}}</div>
                </div>
            </div>
        </div>
        <div class="map-pick">
            <div id="isMap"></div>
            <a-modal title="设备信息详情" class="domeBox" :visible="detailsVisible" width="500px" @ok="informationCancel" @cancel="informationCancel">
                <div class="informationTopBox"></div>
                <div class="informationBodyBox">
                    <div class="informationTopBodyBox">
                        <div class="clearfix">
                            <div class="informationTopBodyLeftBox">
                                <div class="informationImgBox" v-if="assetImg">
									<img :src="imgUrl+assetImg" width="100%" height="100%"/>
                                </div>
                            </div>
                            <div class="informationTopBodyRightBox">
                                <template v-if="attributeList.length>0">
                                    <div class="informationTopBodyRightDivBox clearfix" v-for="(item,index) in attributeList" :key="index">
                                        <label>{{item.attributeCn}}:</label>
                                        <span>{{item.attributeVal}}</span>
                                    </div>
                                </template>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>设备编号:</label>
                                    <span>{{assetCoding}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>设备名称:</label>
                                    <span>{{assetName}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>生产厂家:</label>
                                    <span>{{manufacturer}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>设备型号:</label>
                                    <span>{{informationEquipmentType}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>放置位置:</label>
                                    <span>{{assetLocation}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </a-modal>
            <div class="monitorBox" :style="{backgroundImage: 'url('+ monitorBg +')'}" v-if="isShowMonitor">
                <div class="title">
                    <div>{{equipLocation}}</div>
                    <div class="close" @click="closeMonitor">
                        <img src="./images/close.png" />
                    </div>
                </div>
                <div class="monitorBoxCon">
                    <div id="monitorPage">
                        <div id="playWnd" class="playWnd" v-if="isShowMonitor"></div>
                    </div>
                </div>
            </div>
            <!-- <MapIndex type="bigMap" /> -->
        </div>

        <div class="map-right">
            <img class="map-lr" src="./images/map-r.png" mode="" />
        </div>
        <div class="map-bottom">
            <div class="map-b-i">
                <img class="m-i" src="./images/m-i1.png" alt="">
                <div class="des">水文监测</div>
                <div class="num-pick">
                    <img src="./images/num-i.png" alt="" />
                    {{map_bNum1}}
                </div>
            </div>
            <div class="map-b-i b-i1">
                <img class="m-i" src="./images/m-i2.png" alt="">
                <div class="des">水质监测</div>
                <div class="num-pick">
                    <img src="./images/num-i.png" alt="" />
                    {{map_bNum2}}
                </div>
            </div>
            <div class="map-b-i b-i1">
                <img class="m-i" src="./images/m-i3.png" alt="">
                <div class="des">机房设备</div>
                <div class="num-pick">
                    <img src="./images/num-i.png" alt="" />
                    {{map_bNum3}}
                </div>
            </div>
            <div class="map-b-i b-i1">
                <img class="m-i" src="./images/m-i4.png" alt="">
                <div class="des">广播设备</div>
                <div class="num-pick">
                    <img src="./images/num-i.png" alt="" />
                    {{map_bNum4}}
                </div>
            </div>
            <div class="map-b-i b-i1">
                <img class="m-i" src="./images/m-i5.png" alt="">
                <div class="des">视频监控</div>
                <div class="num-pick">
                    <img src="./images/num-i.png" alt="" />
                    {{map_bNum5}}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import '@/assets/less/operationCenter/mapIndex/dome.less'
    import { axios } from '@/utils/request';
    import {
        getbiaoduanorbiaozhuData,
        getRiversData,
        getRegionsData,
        getGisModelDetailData,
        getGisModelDetailData2,
        getUrl,
        getUrl2,
        getToken,
        warnUndealData,
        getWarnQueryAllIcon,
        gisAlarmRelieve,
        gisAlarmKnow,
        gisAlarmHandle,
        gisDashboardGisModelNav,
        getGisFlowYestoday,
        getGisFlowToday,
        getArtemisConfig,
    } from '@/api/operationCenter/mapIndex/index.js'
    // import MapIndex from '@/views/operationCenter/mapIndex/index';
    import HeaderNotice from '@comp/tools/HeaderNotice.vue'

    const styleJson = require('@/assets/js/custom_map_config.json');
    export default {
        name: "map",
        components: {
            HeaderNotice,
            // MapIndex
        },
        data(){
            return {
                token: '',
                areaDataList: '',
                zoonLevel: 12.65,
                clickZoonLevel: 13,
                map_liBox: [{
                    icon: require('./images/m-i6.png'),
                    des: '水质',
                    selected: 'selected',
                    id: "c665d152d98ee3f91c552e73ded21770",
                    modelType: "Z02",
                    navStatus: "1"
                }, {
                    icon: require('./images/m-i7.png'),
                    des: '水文',
                    selected: 'selected',
                    id: "b8a07b49286fd89dcd25a0875c973996",
                    modelType: "Z03",
                    navStatus: "1"
                }, {
                    icon: require('./images/m-i8.png'),
                    des: '监控',
                    selected: 'selected',
                    id: "4df3a8721e9f95ca669285e647a5ebef",
                    modelType: "Z06",
                    navStatus: "1"
                }],
                oMarker: [],
                markers:[], //地图标记点
                nameArr: ['YLB','LLB','FM'],
                resultRivers: [],
                newArr: [],

                detailsVisible: false, //设备详情弹框
                allArrName:[],
                gisDashboardGisModelNavList: [],
                resultRegions: [], //区域数据
                assetImg:'',
                imgUrl:window._CONFIG['domianURL'],
                usageState: '',
                deviceStatus: '',
                assetCoding: '',
                assetName: '',
                manufacturer: '',
                informationEquipmentType: '',
                assetLocation: '',
                attributeList: [],
                monitorBg: require('@/views/operationCenter/mapIndex/images/monitor_bg.png'),

                oWebControl: null,
                WebControl: null,
                isShowMonitor: false, //是否显示监控
                equipLocation: '',
                initparam: {
                    appKey: "xxxxx",
                    secret: "xxxxxxx",
                    apiIp: "xxx.xxx.xxx",
                    apiPort: 8099,
                    layout: "1x1"//这里是来控制你一开始初始化之后的分屏的
                },
                oMarker_index: 0,
                markerContents: [],


                map_bNum1: 0,
                map_bNum2: 0,
                map_bNum3: 0,
                map_bNum4: 0,
                map_bNum5: 0,

              
            }
        },
        created() {
            getRiversData(this);
            getRegionsData(this);
            gisDashboardGisModelNav(this);
            this.getNums();
            this.getData();
        },
        mounted() {
            clearInterval(window.timer);
            window.timer=setInterval(()=>{
                getToken();
            },120000)
            getUrl();
            window.oThis2 = this;
        },
        activated() {
            $('#app').css('overflow-y', 'hidden');
            //$('.stateSelectionUlBox li').removeClass('active');
            getUrl();
            this.getData();
        },
        deactivated(){
            this.closeWindow();//关闭插件
        },
        destroyed(){
            this.closeWindow();//关闭插件
        },
        beforeDestroy(){
        getUrl2();
        // 离开地图页面后停止调用token定时获取
        clearInterval(window.timer);


        /*    以下三个定时任务停用                                                                                                                              */
//      //离开地图后停止调用流量统计单日统计
//      clearInterval(window.getGisDashboardQueryFlowDayTimer);
//      clearInterval(window.getGisHdFlowDayTimer);
//      clearInterval(window.getGisHdbrFlowDayTimer);


        clearInterval(window.getGisFlowTodayTimer);

        //离开地图后删除非选中标识的气泡框
        window.oMarker.filter((item, i, arr) => {
            window.noNameArr.map(index => {
                if (item.modelTypeCode == index) {

                    delete window[item.id]
                }
            })

        })
        $('#app').css('overflow-y', 'initial');
    },
        methods: {
            getNums() { //获取设备数量
                axios.get('rdp/info/hydrology').then(res => {
                    if (res.code) this.map_bNum1 = res.data.num;
                })
                axios.get('rdp/info/water-quality').then(res => {
                    if (res.code) this.map_bNum2 = res.data.num;
                })
                axios.get('rdp/info/engine').then(res => {
                    if (res.code) this.map_bNum3 = res.data.num;
                })
                axios.get('rdp/info/broadcast').then(res => {
                    if (res.code) this.map_bNum4 = res.data.num;
                })
                axios.get('rdp/info/monitor').then(res => {
                    if (res.code) this.map_bNum5 = res.data.num;
                })
            },
            //获取全量标段OR标注信息
            getData() {
                this.oMarker = [];
                getbiaoduanorbiaozhuData(this);
            },
            updataMap(methodType) {
                // console.log('初始化地图')
                var map = new AMap.Map("isMap",{ 
                    pitch: 40,
                    viewMode: '3D',
                    center: [104.665500, 31.380089],
                    zooms: [11.7, 14.7],
                    zoom: this.zoonLevel,
                    mapStyle: 'amap://styles/dark', //设置地图的显示样式
                });
                // 设置光照
                map.AmbientLight = new AMap.Lights.AmbientLight([1, 1, 1], 0.5);
                map.DirectionLight = new AMap.Lights.DirectionLight([0, 0, 1], [1, 1, 1], 1);
            
                var point = map.getCenter();
                window.map = map;
                window.point = point;
                // 创建地图实例
                // 创建点坐标
                map.setZoomAndCenter(this.zoonLevel, point); //同时设置地图层级与中心点

                setTimeout(() => {
                    this.updata(methodType, window.oMarker);
                }, 1000);
            },
            //标注初始化加载
            updata(methodType, oMarker) {
                var map = window.map;
                var _this = this;
                let resultRivers = this.resultRivers;
                for (var i in resultRivers) {
                    var pois = [];
                    let rivers = JSON.parse(resultRivers[i].riverAddressInfo);

                    for (var j = 0; j < rivers.length; j++) {
                        pois.push(new AMap.LngLat(rivers[j].longitude, rivers[j].latitude)); //创建坐标点对象
                    }
                    var object3Dlayer = new AMap.Object3DLayer();//创建3D图层
                    var height = [];
                    pois.forEach((item, index) => {
                        height[index] = 8050;
                    })
                    var meshLine = new AMap.Object3D.MeshLine({ //创建3D线段
                        path: pois,
                        height: height,
                        color: '#FFFC00',
                        width: 7
                    });
                
                    meshLine.transparent = true;
                    object3Dlayer.add(meshLine); //线段绘制到图层
                    meshLine['backOrFront'] = 'both';
                    map.add(object3Dlayer); //图层添加到地图
                
                }

                let resultRegions = this.resultRegions;
                resultRegions.map((item,index) => {
                    var resultRegion=[];
                    var object3Dlayer = new AMap.Object3DLayer();//创建3D图层
                    map.add(object3Dlayer);
                    var regionAddressInfo = resultRegions[index].regionAddressInfo;
                    regionAddressInfo = regionAddressInfo.split(';');
                    regionAddressInfo.map((item,index)=>{
                        let poi = item.split(',')
                        resultRegion.push(new AMap.LngLat(poi[0], poi[1]))
                    })
                    var prism = new AMap.Object3D.Prism({
                        path: resultRegion,
                        height: 8000,
                        color: 'rgba(48, 150, 142, 0.4)'
                    });
                    prism.transparent = true;
                    object3Dlayer.add(prism);//绘制河流
                    AMap.event.addListener(prism,'click', function (e) {
                        point = e.point;
                        map.setCenter([point.lng, point.lat]);
                        map.setZoom(_this.clickZoonLevel); //设置地图层级
                        // map.centerAndZoom(new AMap.Point(point.lng, point.lat), _this.clickZoonLevel);
                    });
                })


                let newArr = [];
                // 创建标注对象并添加到地图
                // var marker = new AMap.Marker(point, {
                //     icon: this.myIcon
                // }); // 创建标注
                map.remove(this.markers);

                // map.remove(str)
                if (this.nameArr.length) {
                    oMarker.filter((item) => {
                        this.nameArr.map(index => {

                            if (item.modelTypeCode == index) {
                                newArr.push({
                                    id: item.id,
                                    latitude: item.latitude,
                                    longitude: item.longitude,
                                    modelZcImg: item.modelZcImg,
                                    modelTypeCode: item.modelTypeCode,
                                    modelOffset: item.modelOffset,
                                    modelPosition: item.modelPosition,
                                    modelType: this.zoonLevel,
                                    list: item.list,
                                    fmType: item.fmType,
                                    equipmentSn: item.equipmentSn,
                                    hkMonitorKey: item.hkMonitorKey,
                                    equipLocation: item.equipLocation,
                                })
                            }
                        })
                    });
                    oMarker = [];
                    newArr.map(index => {
                        oMarker.push({
                            id: index.id,
                            latitude: index.latitude,
                            longitude: index.longitude,
                            modelZcImg: index.modelZcImg,
                            modelTypeCode: index.modelTypeCode,
                            modelOffset: index.modelOffset,
                            modelPosition: index.modelPosition,
                            modelType: this.zoonLevel,
                            list: index.list,
                            fmType: index.fmType,
                            equipmentSn: index.equipmentSn,
                            hkMonitorKey: index.hkMonitorKey,
                            equipLocation: index.equipLocation,
                        })
                    })
                }
                var markArr = [];
                for (var i = 0; i < oMarker.length; i++) {
                    var str = oMarker[i].id;
                    // _this.addComplexCustomOverlay2(oMarker[i], map);
                    // this.addComplexCustomOve                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        rlay(oMarker[i], map);
                    // let modelOffset = Number(oMarker[i].modelOffset);
                    this.markers.push(oMarker[i].modelZcImg);
                    str = oMarker[i].modelZcImg;
                    map.add(str);

                    var id = oMarker[i].id;
                    str.id = oMarker[i].id;
                    if (oMarker[i].type) {
                        str.setAnimation(AMAP_ANIMATION_BOUNCE);
                    }
                    // map.addOverlay(str);
                    var pointCodeoObj =  {
                        equipmentSn: oMarker[i].equipmentSn,
                        hkMonitorKey: oMarker[i].hkMonitorKey,
                        equipLocation: oMarker[i].equipLocation,
                    };
                    if(oMarker[i].modelTypeCode != "Z06"){
                        AMap.event.addListener(str,"click", function (e) {
                            _this.isShowMonitor = false;
                            _this.equipLocation = '';
                            let data = {
                                id: this
                            }
                            _this.closeWindow();
                            getGisModelDetailData(data, _this);
                            $('.BMap_pop').hide();
                        }.bind(id));
                    }else {
                        AMap.event.addListener(str,"click", function (e) {
                            _this.pointCode = this.equipmentSn;
                            _this.hkMonitorKey = this.hkMonitorKey,
                            _this.equipLocation = this.equipLocation;
                            _this.closeWindow();
                            _this.getArtemisConfig();
                            _this.isShowMonitor = true;
                        }.bind(pointCodeoObj));
                    }

                    //
                    if (oMarker[i].type == 1) {
                        str.setAnimation(AMAP_ANIMATION_BOUNCE);

                    }
                    // if (oMarker[i].modelType <= map.getZoom()&&_this.nameArr.length) {
                    //     str.show();

                    // } else {
                    //     str.hide();
                    //     $('#' + str.id).remove();
                    // }
                    window[oMarker[i].id] = str;

                    // markArr.push(str);

                }
            },
            //创建自定义覆盖物
            addComplexCustomOverlay2(markData, map, index) {
                if ( (!this.nameArr.includes('Z03') && markData.modelTypeCode==="Z03") || 
                    (!this.nameArr.includes('Z02') && markData.modelTypeCode==="Z02") ) {
                    this['markerContents'+index] && map.remove(this['markerContents'+index]);
                    this['markerContents'+index] = null;
                    return;
                }

                // 104.64901,31.36334 //单独处理的点
                // 104.700393,31.392204 //单独处理的点
                let markerClass = ''; 
                if(markData.longitude > 104.64901 && markData.latitude < 31.36334) { //右下
                    markerClass = 'marker-rb'
                } else if (markData.longitude > 104.700393 && markData.latitude < 31.392204) { //右下
                    markerClass = 'marker-rb'
                } else if (markData.longitude < 104.699667 && markData.latitude > 31.39291) { //左上
                    markerClass = 'marker-lt'
                } else if (markData.longitude > 104.699667 && markData.latitude > 31.39291) { //右上
                    markerClass = 'marker-rt'
                } else if (markData.longitude > 104.699667 && markData.latitude < 31.39291) { //右下
                    markerClass = 'marker-rb'
                } else if (markData.longitude < 104.699667 && markData.latitude < 31.39291) { //左下
                    markerClass = 'marker-lb'
                }


                if (!markData.list || !markData.list.length) return
                let str = '';
                let img = '<img class="bg" src="' + require('@/components/bigScreenPages/images/infoWin.png') + '"/>'
                markData.list.forEach(item => {
                    str += '<div class="item"><div class="name">'+ item.variableName+'：</div><div class="value">'+ item.varibleValue +'</div></div>'
                })
                if (this['markerContents'+index]) {
                    this['markerContents'+index].setContent(`<div class="infoWin-pick ${ markerClass
                        }"><div class="line1"></div><div class="line2"></div><div class="infoWin">${ img }${ str }<div></div>`
                    );
                    return;
                }
                let marker = null;
                if (markData.list && markData.list.length) {
                    marker = new AMap.Marker({ //穿件坐标点覆盖物
                        map: map,
                        zIndex: 0,
                        offset: new AMap.Pixel(0, Number(markData.modelOffset)),
                        position: new AMap.LngLat(markData.longitude,markData.latitude), //坐标点对象
                        content: `<div style="display:none;" class="infoWin-pick ${markerClass
                            }"><div class="line1"></div><div class="line2"></div><div class="infoWin">${ img }${ str }<div></div>` //显示的文本内容
                    });
                    setTimeout(() => {
                        $(`.${markerClass}`).parent().parent().addClass(`${markerClass}-parent`)
                        this.$nextTick(() => {
                            $(`.${markerClass}`).show();
                        });
                    }, 900);
                    marker.on('mouseover', function (e) {
                        marker.setzIndex(9999) //设置层级
                    })
                    marker.on('mouseout', function (e) {
                        marker.setzIndex(0) //设置层级
                    })
                }
                this['markerContents'+index] = marker;
                return
            },

            onMarkerCutChange(item) { //地图显示标注切换
                let name = item.modelType;
                let idArr = [];
                let newArr = [];
                if (item.selected) {
                    this.nameArr.map((index, i) => {
                        if (index == name) {
                            this.nameArr.splice(i, 1);
                            item.selected = '';
                        }
                    });
                } else {
                    this.nameArr.push(name);
                    item.selected = 'selected';
                }
                window.oMarker.filter((item) => {
                    this.nameArr.map(index => {
                        if (item.modelTypeCode == index) {
                            newArr.push({
                                id: item.id,
                                latitude: item.latitude,
                                longitude: item.longitude,
                                modelZcImg: item.modelZcImg,
                                modelTypeCode: item.modelTypeCode,
                                modelOffset: item.modelOffset,
                                modelPosition: item.modelPosition,
                                modelType: this.zoonLevel,
                                list: item.list,
                                fmType: item.fmType,
                                equipmentSn: item.equipmentSn,
                                hkMonitorKey: item.hkMonitorKey,
                            })
                        }
                    })

                });
                this.updata('',newArr);
            },
            
            resetDetails() { //设备详情-弹框里的参数初始化
                this.usageState = ''; //获取详情-使用状态
                this.deviceStatus = ''; //获取详情-设备状态
                this.assetCoding = ''; //获取详情-资产编号
                this.assetName = ''; //获取详情-资产名称
                this.manufacturer = ''; //获取详情-生产厂家
                this.informationEquipmentType = ''; //获取详情-设备型号
                this.assetLocation = ''; //获取详情-资产位置
                this.attributeList = []; //资产特殊属性列表
            },
            //设备详情-确定按钮效果
            informationOk() {
                this.detailsVisible = false;
                this.resetDetails();
            },
            //设备详情-取消按钮效果
            informationCancel() {
                this.detailsVisible = false;
                this.resetDetails();
            },
            initPlugin() { //创建播放实例 
                this.oWebControl = new WebControl({
                    szPluginContainer: 'playWnd', // 指定容器id
                    iServicePortStart: 15900, // 指定起止端口号，建议使用该值
                    iServicePortEnd: 15900,
                    szClassId: "23BF3B0A-2C56-4D97-9C03-0CB103AA8F11", // 用于IE10使用ActiveX的clsid
                    // 创建WebControl实例成功
                    cbConnectSuccess: () => {
                        this.oWebControl
                            .JS_StartService("window", {
                                // WebControl实例创建成功后需要启动服务
                                dllPath: "./VideoPluginConnect.dll" // 值"./VideoPluginConnect.dll"写死
                            })
                            .then(
                                () => {
                                    // 启动插件服务成功
                                    this.oWebControl.JS_SetWindowControlCallback({
                                        // 设置消息回调
                                        cbIntegrationCallBack: this.cbIntegrationCallBack
                                    });
                                    this.oWebControl
                                        .JS_CreateWnd('playWnd', 375, 200)
                                        .then(() => {
                                            //JS_CreateWnd创建视频播放窗口，宽高可设定
                                            // this.getInitParam(); // 创建播放实例成功后初始化
                                            this.init(); // 创建播放实例成功后初始化
                                        });
                                },
                                () => {
                                    // 启动插件服务失败
                                }
                            );
                    },
                    // 创建WebControl实例失败
                    cbConnectError: () => {
                        this.oWebControl = null;
                        let htmls = '<div class="videoTips">插件启动失败，请检查插件是否安装!</div>' +
                                    '<div class="videoTips_p">复制下面链接，在浏览器中打开 即可下载安装</div>' +
                                    '<div class="videoTipsUrl" ">'+this.httpUrl.httpUrl+'</div>'
                        // $("#playWnd").html("插件未启动，正在尝试启动，请稍候...");
                        this.WebControl.JS_WakeUp("VideoWebPlugin://"); // 程序未启动时执行error函数，采用wakeup来启动程序
                        this.initCount++;
                        if (this.initCount < 3) {
                            setTimeout(() => {
                                this.initPlugin();
                            }, 3000);
                        } else {
                            console.log("插件启动失败，请检查插件是否安装!");
                            $("#playWnd").append(htmls);
                            $(".videoTips").css({"width":"100%","text-align":"center","padding-top":"15%","padding-bottom": "30px"});
                            $(".videoTips_p").css({"width":"100%","text-align":"center","padding-bottom": "30px"});
                            $(".videoTipsUrl").css({"width":"100%","text-align":"center"});

                        }
                    },
                    // 异常断开：bNormalClose = false
                    cbConnectClose: bNormalClose => {
                        // JS_Disconnect正常断开：bNormalClose = true
                        console.log("cbConnectClose");
                        this.oWebControl = null;
                    }
                });
            },
            
            init() { //初始化
                this.getPubKey(() => {
                    // 请自行修改以下变量值
                    let appkey = this.initparam.hkArtemisAppKey; //综合安防管理平台提供的appkey，必填
                    let secret = this.setEncrypt(this.initparam.hkArtemisAppSecret); //综合安防管理平台提供的secret，必填
                    let ip = this.initparam.hkArtemisHost; //综合安防管理平台IP地址，必填
                    var playMode = 0;                                  //初始播放模式：0-预览，1-回放
                    var port = 443;                                    //综合安防管理平台端口，若启用HTTPS协议，默认443
                    var snapDir = "D:\\SnapDir";                       //抓图存储路径
                    var videoDir = "D:\\VideoDir";                     //紧急录像或录像剪辑存储路径
                    var layout = "1x1";                                //playMode指定模式的布局
                    var enableHTTPS = 1;                               //是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
                    var encryptedFields = 'secret';					   //加密字段，默认加密领域为secret
                    var showToolbar = 1;                               //是否显示工具栏，0-不显示，非0-显示
                    var showSmart = 1;                                 //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
                    var buttonIDs = "0,16,256,257,258,259,260,512,513,514,515,516,517,768,769";  //自定义工具条按钮



                    this.oWebControl
                        .JS_RequestInterface({
                            funcName: "init",
                            argument: JSON.stringify({
                                appkey: appkey, //API网关提供的appkey
                                secret: secret, //API网关提供的secret
                                ip: ip, //API网关IP地址
                                playMode: playMode, //播放模式（决定显示预览还是回放界面）
                                port: port, //端口
                                snapDir: snapDir, //抓图存储路径
                                videoDir: videoDir, //紧急录像或录像剪辑存储路径
                                layout: layout, //布局
                                enableHTTPS: enableHTTPS, //是否启用HTTPS协议
                                encryptedFields: encryptedFields, //加密字段
                                showToolbar: showToolbar, //是否显示工具栏
                                showSmart: showSmart, //是否显示智能信息
                                buttonIDs: buttonIDs //自定义工具条按钮
                            })
                        })
                        .then(oData => {
                            this.oWebControl.JS_Resize(375, 200); // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
                            this.startpreview();  //初始化之后开启预览
                        });
                });
            },
            //获取公钥
            getPubKey(callback) {
                this.oWebControl
                    .JS_RequestInterface({
                        funcName: "getRSAPubKey",
                        argument: JSON.stringify({
                            keyLength: 1024
                        })
                    })
                    .then(oData => {
                        console.log(oData);
                        if (oData.responseMsg.data) {
                            this.pubKey = oData.responseMsg.data;
                            callback();
                        }
                    });
            },
            //RSA加密
            setEncrypt(value) {
                let encrypt = new JSEncrypt();
                encrypt.setPublicKey(this.pubKey);
                return encrypt.encrypt(value);
            },
            //获取综合安防平台配置(获取海康密钥)
            getArtemisConfig(){
                let data = this.hkMonitorKey
                getArtemisConfig({configKey: '001'}, this, () => {
                    setTimeout(() => { 
                        this.initPlugin()
                    }, 2000);
                })
            },
            // 推送消息
            cbIntegrationCallBack(oData) {
                console.log(oData.responseMsg);
                /* showCBInfo(JSON.stringify(oData.responseMsg)); */
            },
            //视频“预览”功能
            startpreview() {
                let pointCode = this.pointCode;
                let cameraIndexCode = pointCode; //获取输入的监控点编号值，必填
                let streamMode = 0; //主子码流标识：0-主码流，1-子码流
                let transMode = 1; //传输协议：0-UDP，1-TCP
                let gpuMode = 0; //是否启用GPU硬解，0-不启用，1-启用
                let wndId = 1; //播放窗口序号（在2x2以上布局下可指定播放窗口）
                this.oWebControl.JS_RequestInterface({
                    funcName: "startPreview",
                    argument: JSON.stringify({
                        cameraIndexCode: cameraIndexCode, //监控点编号
                        streamMode: streamMode, //主子码流标识
                        transMode: transMode, //传输协议
                        gpuMode: gpuMode, //是否开启GPU硬解
                        wndId: wndId //可指定播放窗口
                    })
                });
            },
            //停止全部预览功能
            stopAllPreview() {
                this.oWebControl.JS_RequestInterface({
                    funcName: "stopAllPreview"
                });
            },
            //关闭视频窗口
            closeWindow() {
                if (this.oWebControl != null) {
                    this.stopAllPreview();
                    this.oWebControl.JS_HideWnd(); // 先让窗口隐藏，规避可能的插件窗口滞后于浏览器消失问题
                    setTimeout(() => {
                        this.oWebControl.JS_Disconnect().then(
                            () => {
                                // 断开与插件服务连接成功
                            },
                            () => {
                                // 断开与插件服务连接失败
                            }
                        );
                    });
                }
            },
            closeMonitor(){ //关闭监控窗口
                this.closeWindow();
                setTimeout(() => {
                    this.isShowMonitor = false; 
                });
            },
        },
    }
</script>

<style lang="less" scoped>
    body{background: inherit;}
    .big-map,
    .map-pick,
    #isMap{
        position: relative;
        width: 100%;
        height: 100%;
    }
    .amap-logo {
        display: none !important;
    }
    .big-map {
        width: calc(100% + 1000px);
        transform: translateX(-500px);
    }
    .map-left {
        position: absolute;
        left: 500px;
        top: 134px;
        bottom: 176px;
        z-index: 9999999;
    }
    .map-right {
        position: absolute;
        right: 500px;
        top: 134px;
        bottom: 176px;
        z-index: 9999999;
    }
    .map-lr {
        height: 100%;
    }
    .map-l_iBox {
        position: absolute;
        top: 50%;
        left: 70px;
        transform: translateY(-50%);
        .map-l_i {
            width: 65px;
            height: 65px;
            color: #B9B9B9;
            border: 1px solid #00652C;
            border-radius: 50%;
            padding-top: 13px;
            &:not(:nth-child(2)) {
                margin-left: 24px;
            }
            &:not(:last-child) {
                margin-bottom: 50px;
            }
            &.selected {
                border: 2px solid #FFF055;

            }
            img {
                width: 22px;
                height: 22px;
                display: block;
                margin: 0 auto;
            }
            .des {
                line-height: 1.1;
                text-align: center;
                padding-top: 5px;
            }
            .hint {

            }
        }
    }
    .map-bottom {
        display: flex;
        position: absolute;
        left: 50%;
        bottom: 94px;
        transform: translateX(-50%);
        .map-b-i {
            position: relative;
            text-align: center;
            &:not(:last-child) {
                margin-right: 105px;
            }
            .m-i {
                width: 92px;
                height: 94px;
            }
            .des {
                font-size: 18px;
                line-height: 1.1;
            }
            .num-pick {
                position: absolute;
                top: -6px;
                right: 1px;
                width: 26px;
                height: 26px;
                font-size: 12px;
                img {
                    display: block;
                    position: absolute;
                    width: 100%;
                    height: 100%;
                }
            }
        }
    }
    // 地图气泡信息框
    /deep/ .infoWin-pick {
        .line1 {
            position: absolute;
            width: 139px;
            height: 1px;
            border:  1px dashed rgba(255,255,255,.4);
            box-sizing: border-box;
        }
        .line2 {
            position: absolute;
            width: 60px;
            height: 1px;
            border: 1px dashed rgba(255,255,255,.4);
            box-sizing: border-box;
        }
        .infoWin {
            padding: 24px;
        }
    }
    /deep/ .marker-lt-parent {  //左上
        transform: translate(calc(-100% - 122px), calc(-100% - 75px)) !important;
        .line1 {
            bottom: 0;
            right: 0;
            transform: translate(164px, 20px) rotate(240deg);
        }
        .line2 {
            bottom: 0;
            right: -0;
            transform: translate(60px, -41px);
        }
    }
    /deep/ .marker-rt-parent { //右上
        transform: translate(152px, calc(-100% - 68px)) !important;
        .line1 {
            left: 0;
            bottom: 0;
            transform: translate(-164px, 20px) rotate(120deg);
        }
        .line2 {
            bottom: 0;
            left: 0;
            transform: translate(-60px, -44px);
        }
    }
    /deep/ .marker-rb-parent { //右下
        transform: translate(156px, 100px) !important;
        .line1 {
            top: 0;
            left: 0;
            transform: translate(-165px, -18px) rotate(240deg);
        }
        .line2 {
            top: 0;
            left: 0;
            transform: translate(-60px, 44px);
        }
    }
    /deep/ .marker-lb-parent { //左下
        transform: translate(calc(-100% - 122px), 90px) !important;
        .line1 {
            top: 0;
            right: 0;
            transform: translate(165px, -10px) rotate(120deg);
        }
        .line2 {
            top: 0;
            right: 0;
            transform: translate(60px, 51px);
        }
    }
    // 地图气泡信息框
    /deep/ .infoWin-pick {
        position: relative;
        .bg {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
        .item {
            font-size: 14px;
            justify-content: start;
            .name {
                color: #C4D1CC;
                white-space:nowrap
                
            }
            .value {
                color: #0BEC8F;
                white-space:nowrap
            }
        }
    }
</style>
