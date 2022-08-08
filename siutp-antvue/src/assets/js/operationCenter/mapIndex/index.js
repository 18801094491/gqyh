import {
    BaiduMap
} from '@/assets/map.js';
const styleJson = require('@/assets/js/custom_map_config.json');
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
import httpUrl from '@/api/commont.js'
import '@/assets/less/operationCenter/mapIndex/dome.less'



export default {
    // mixins:[markData],
    name: 'dome',
    data() {
        return {
            httpUrl: httpUrl,
            description: '监控中心',
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
            data:[],
            data2:[],
            scale:[
                {
                    dataKey: 'temperature',
                    min: 0
                },
                {
                    dataKey: 'year',
                }
            ],
            scale2:[
                {
                    dataKey: 'temperature',
                    min: 0
                },
                {
                    dataKey: 'year',
                }
            ],
            height: 400,
            height2:'',
            name: '', //筛选后存名称
            detailsVisible: false, //设备详情弹框
            usageState: '', //获取详情-使用状态
            deviceStatus: '', //获取详情-设备状态
            assetCoding: '', //获取详情-资产编号
            assetName: '', //获取详情-资产名称
            manufacturer: '', //获取详情-生产厂家
            informationEquipmentType: '', //获取详情-设备型号
            assetLocation: '', //获取详情-资产位置
            attributeList: [], //资产特殊属性列表
            nameArr: ['YLB','LLB','FM'],
            newArr: [],
            hierarchy: '16级',
            barType:'1',
            adjust: [{
                type: 'dodge',
                marginRatio: 1 / 32,
            }],
            tabScrollList:[],
            warnUndealDataTimer:null,
            toggle:true,
            
            warnQueryAllIconList:[],
            IOServerStatusList:[],
            meterFlowTotal:'',
            waterMeterFlowTotal:'',
            subtract:'',
            trafficStatisticsList:[],
            meterFlowTotalFormat:'',
            waterMeterFlowTotalFormat:'',
            subtractFormat:'',
            CarSrc: require('@/assets/images/mapIndex/iconCar.png'),
            YLBSrc: require('@/assets/images/mapIndex/iconYLB.png'),
            GLRKSrc: require('@/assets/images/mapIndex/iconGLRK.png'),
            LLBSrc: require('@/assets/images/mapIndex/iconLLB.png'),
            SBSrc: require('@/assets/images/mapIndex/iconSB.png'),
            FMSrc: require('@/assets/images/mapIndex/iconFM.png'),
            SZJCDSrc: require('@/assets/images/mapIndex/iconSZJCD.png'),
            WDJSrc: require('@/assets/images/mapIndex/iconWDJ.gif'),
            flowCountFormat:'',
            flowCount:'',
            flowCountFormatYes:'',  // 行政区昨日
            flowCountYes:'',
            // 行政区入楼
            flowCountFormatBuild:'',
            flowCountBuild:'',
            flowCountFormatYesBuild:'',  // 行政区昨日
            flowCountYesBuild:'',
            // 行政区绿化
            flowCountFormatGreen:'',
            flowCountGreen:'',
            flowCountFormatYesGreen:'',  // 行政区昨日
            flowCountYesGreen:'',
            
            flowCountFormat1:'', // 河东水量今日浮框展示
            flowCount1:'',       // 河东水量今日
            flowCountFormatYes1:'', // 河东水量昨日浮框展示
            flowCountYes1:'',       // 河东水量昨日
            merterCountFormat:'',
            merterCount:'',
            merterCountFormatYes:'', // 镜河补水昨日
            merterCountYes:'',
            merterCountFormatHb:'', // 华北电燃
            merterCountHb:'',
            merterCountFormatYesHb:'', 
            merterCountYesHb:'',
            alarmEventVisible:false,
            alarmEventType:'',
            warnSn:'',
            warnName:'',
            warnContent:'',
            equipmentType:'',
            warnLevel:'',
            ruleContent:'',
            warnTime:'',
            alarmEventType2:'',
            alarmIndex:'',
            alarmHandlingvisible:false,
            turnOffAlarmvisible:false,
            closeDescription:'',
            gisDashboardGisModelNavList:[],
            alarmEventConfigurationId:'',
            allArrName:[],
            isScreen:false, // 是否全屏展示
            isShowAlarm:false, // 告警事件显示隐藏
            isUpDownQX:false, // 流量统计收起展开
            isShowIcons:false, // 图例收起展开
            imgUrl:window._CONFIG['domianURL'],
            assetImg:'',
            flowCountList:[], // 流量统计今日
            flowCountYesList:[], // 流量统计昨日

            resultRivers: [],  //河流数据
            resultRegions: [], //区域数据
            zoonLevel: 12.7, //地图级别
            clickZoonLevel: 13, //点击区域 设置级别



            //监控
            swfHeight: "",
            swfWidth: "",
            //初始化参数
            initparam: {
                appKey: "xxxxx",
                secret: "xxxxxxx",
                apiIp: "xxx.xxx.xxx",
                apiPort: 8099,
                layout: "1x1"//这里是来控制你一开始初始化之后的分屏的
            },
            //监控点编号
            pointCode: "123xxxx",
            pubKey: "",
            oWebControl: null,
            WebControl: null,
            initCount:'',

            records: [], //多屏监控点数据
            firstScreen: {}, //监控第一屏

            titleBg: require('@/views/operationCenter/mapIndex/images/title_bg.png'),
            monitorBg: require('@/views/operationCenter/mapIndex/images/monitor_bg.png'),
            isShowMonitor: false, //是否显示监控
            equipLocation: '',
            emptyIcon: require('@/views/operationCenter/mapIndex/images/empty.png'),
            markers:[],//地图标记点
            markerContents:[],
            oMarker_index: 0,
        }
    },
    beforeMount() {this.WebControl = WebControl;},//调用电脑中的插件
    mounted() {
        // this.swfHeight = document.getElementById("monitorPage").offsetHeight;
        // this.swfWidth = document.getElementById("monitorPage").offsetWidth;
        // $("#playWnd").width(this.swfWidth);
        // $("#playWnd").height(this.swfHeight);

        this.closeWindow();//关闭视频插件
        //获取GIS地图右下角图例数据
        getWarnQueryAllIcon(this);
        getRiversData(this);
        getRegionsData(this);
        //在地图页面时定时获取TOKEN
        clearInterval(window.timer);
        window.timer=setInterval(()=>{
            getToken();
        },120000)
        
        $(document).on("keydown", function (event) {
            if (event && (event.keyCode == 122 || event.key == 'F11')) {
                let allMap = document.getElementById('allmap')
                allMap.style.height = '100vh';
            }
        })
        
        //地图页面禁止滚动条
        $('#app').css('overflow-y', 'hidden');
        //进入地图页面时,向后台发送启动WS GIS推送接口
        getUrl();
        window.oThis1 = this;

        let _this = this;
        
        /* 以下是 getTrafficStatisticsData禁用接口相关   */
       	
       	getGisFlowYestoday(_this); // 昨日
    	getGisFlowToday(_this); // 今日
      	clearInterval(window.getGisFlowTodayTimer);
        window.getGisFlowTodayTimer = setInterval(() => {
            getGisFlowToday(_this);
        }, 10 * 60 * 1000);
       
        $('.BMap_mask').siblings('div').eq(1).attr('class', 'wpt');
        //获取地图左侧筛选数据
        gisDashboardGisModelNav(this);

        //给GIS地图添加容器高度和宽度
        let oheight = document.body.clientHeight - ($('.tab-layout-tabs').height() * 1 + $('.header').height());
        let oWidth = document.body.clientWidth - $('.ant-layout-sider').width();
        $('#allmap').height(oheight);
        $('#allmap').width(oWidth);
        //获取地图数据
        this.getData();
        //定时查询告警事件
        this.warnUndealData();
        clearInterval(window.warnUndealDataTimer);
        window.warnUndealDataTimer=setInterval(() => {
            this.warnUndealData();
        },120000)

        //计算右侧浮框高度
        $('.mapRightBox').height(document.documentElement.clientHeight-94);

        
        this.height2=$('.mapRightQXBox').height()-($('.mapRightQXBox .h4').height());
        this.height=$('.mapRightZXBox').height()-($('.statisticst').height()+20);
        //计算告警事件滚动容器高度
        // $('.scroll-box').height($('.mapRightTableBox').height()-$('.mapRightTableBox .h4').height()-20);
        $('.scroll-box').height('100%');
        //告警事件滚动移入移出效果
        $(".scrollBox tbody").hover(function (){
            clearInterval(window.warnUndealDataTimer2);
        },function (){
            clearInterval(window.warnUndealDataTimer2);
            // window.warnUndealDataTimer2 = setInterval(_this.scrollTop, 3000);
        })
        //左侧筛选全选效果
        $('.toggle').click(function(e){
            if(!_this.toggle){
                _this.toggle=true;
                $('.mapRightBox').css('width','20%');
                $('.mapRightTableBox').css('width','100%');
                $('.mapRightQXBox').css('width','100%');
                $('.mapRightZXBox').css('width','100%');
                $('.hierarchyBox').css('right','22%');
                $('.toggle').css('left','-11px');
                clearInterval(window.warnUndealDataTimer2);
                // window.warnUndealDataTimer2 = setInterval(_this.scrollTop, 3000);
            }else{
                _this.toggle=false;
                clearInterval(window.warnUndealDataTimer2);
                $('.mapRightBox').css('width','0%');
                $('.mapRightTableBox').css('width','0%');
                $('.mapRightQXBox').css('width','0%');
                $('.mapRightZXBox').css('width','0%');
                $('.hierarchyBox').css('right', '2%');
                $('.toggle').css('left', '9px');
            }
        });
        //计算图例容器高度
        // $('.tuli').height($('.mapRightZXBox').height() - ($('.mapRightZXBodyBox .h4').height()-20));
        // $('.mapRightLLBottomBox').height($('.mapRightQXBox').height() - ($('.mapRightQXBodyBox h4').height() + 10) - ($('.mapRightLLTopUl').height() + 30))
        //计算统计流量高度
//      $('.mapRightLLBox').height($('.mapRightQXBox').height() - ($('.mapRightQXBodyBox .h4').height() + 10)); // 原有

        // 修改 .mapRightLLBox的height
        setTimeout(()=>{ // for 循环后高度塌陷
        	this.$nextTick(()=>{
        		$('.mapRightLLBox').height($('.mapRightQXBox').height() - ($('.mapRightQXBodyBox .h4').height() + 10));
        	})
        },800)
        //告警事件点击某一条告警事件触发弹框
        $('.scrollBox').on('click', 'td.one p',function(e){
            let id=$(e.target).attr('data-id');
            let index = $(e.target).attr('data-index')
            _this.warnDetails(id,index);
        });

        // 针对esc退出解决
        // 获取当前的路由--判断如果是监控中心-继续监听全屏
        let compareName = 'mapIndex'

        document.addEventListener('webkitfullscreenchange', function(e){
            let locationName = window.location.href
            //e.currentTarget.webkitIsFullScreen  可以判断浏览器是否全屏
            if(!(e.currentTarget.webkitIsFullScreen)){
                if (locationName.indexOf(compareName) >= 0){
                    _this.closeBig();
                }
            }else{
        
            }
        });
        document.addEventListener('fullscreenchange', function(e){
            let locationName = window.location.href

            if(!(e.currentTarget.isFullScreen)){
                if (locationName.indexOf(compareName) >= 0){
                    _this.closeBig();
                }
            }else{
        
            }
        });
        /!* 火狐 *!/
        document.addEventListener('mozfullscreenchange', function(e){
            let locationName = window.location.href

            if(!(e.currentTarget.mozIsFullScreen)){
                if (locationName.indexOf(compareName) >= 0){
                    _this.closeBig();
                }
            }else{
        
            }
        });
        /!* IE *!/
        document.addEventListener('MSFullscreenChange', function(e){
            let locationName = window.location.href
            if(!(e.currentTarget.MSIsFullScreen)){
                if (locationName.indexOf(compareName) >= 0){
                    _this.closeBig();
                }
            }else{
        
            }
        });
        
        $('.mapRightZXBox').height('auto')




        //监控
        // this.getArtemisConfig();


        //页面加载时创建播放实例初始化
        // this.initPlugin();

        // 监听resize事件，使插件窗口尺寸跟随DIV窗口变化
        $(window).resize(() => {
            if (this.oWebControl != null) {
                this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
                this.setWndCover();
            }
        });
        // 监听滚动条scroll事件，使插件窗口跟随浏览器滚动而移动
        $(window).scroll(() => {
            if (this.oWebControl != null) {
                this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
                this.setWndCover();
            }
        });
        this.init(); //获取初始话所需的参数


    },
    activated() {
        $('#app').css('overflow-y', 'hidden');
        //$('.stateSelectionUlBox li').removeClass('active');
        getUrl();
    },
    deactivated(){
        this.closeWindow();//关闭插件
    },
    methods: {
        getPopupContainer (triggerNode) {
            return triggerNode
        },

        onHideAlarmChange(){
            this.isShowAlarm=false;
        },
        onHideIconsChange(){
            this.isShowIcons=false;
        },
        onShowAlarmChange(){
            this.isShowAlarm=true;
        },
        onShowIconsChange(){
            this.isShowIcons=true;
        },
            
    	upDownQX(){
    		this.isUpDownQX=!this.isUpDownQX
    	},
    	disKey(e){
    		console.log(e.keyCode,'keycode')
    	},
    	// 计算高度
    	calHeight(vm){
    		//计算右侧浮框高度
	        $('.mapRightBox').height(document.documentElement.clientHeight-94);
	        
	        this.height2=$('.mapRightQXBox').height()-($('.mapRightQXBox .h4').height());
	        this.height=$('.mapRightZXBox').height()-($('.statisticst').height()+20);
	        //计算告警事件滚动容器高度
	        // $('.scroll-box').height($('.mapRightTableBox').height()-$('.mapRightTableBox .h4').height());
	        $('.scroll-box').height('100%');
	        //告警事件滚动移入移出效果
	        $(".scrollBox tbody").hover(function (){
	            clearInterval(window.warnUndealDataTimer2);
	        },function (){
	            clearInterval(window.warnUndealDataTimer2);
	            // window.warnUndealDataTimer2 = setInterval(_this.scrollTop, 3000);
	        })
	        let _this = this;
	        //左侧筛选全选效果
	        $('.toggle').click(function(e){
				
	            if(!_this.toggle){
	                _this.toggle=true;
	                $('.mapRightBox').css('width','20%');
	                $('.mapRightTableBox').css('width','100%');
	                $('.mapRightQXBox').css('width','100%');
	                $('.mapRightZXBox').css('width','100%');
	                $('.hierarchyBox').css('right','22%');
	                $('.toggle').css('left','-11px');
	                clearInterval(window.warnUndealDataTimer2);
	                // window.warnUndealDataTimer2 = setInterval(_this.scrollTop, 3000);
	            }else{
	                _this.toggle=false;
	                clearInterval(window.warnUndealDataTimer2);
	                $('.mapRightBox').css('width','0%');
	                $('.mapRightTableBox').css('width','0%');
	                $('.mapRightQXBox').css('width','0%');
	                $('.mapRightZXBox').css('width','0%');
	                $('.hierarchyBox').css('right', '2%');
	                $('.toggle').css('left', '9px');
	            }
	        });
	        //计算图例容器高度
	        $('.tuli').height($('.mapRightZXBox').height() - ($('.mapRightZXBodyBox .h4').height()+10));
	        // $('.mapRightLLBottomBox').height($('.mapRightQXBox').height() - ($('.mapRightQXBodyBox h4').height() + 10) - ($('.mapRightLLTopUl').height() + 30))
	        //计算统计流量高度
	        $('.mapRightLLBox').height($('.mapRightQXBox').height() - ($('.mapRightQXBodyBox .h4').height() + 10));
	        //告警事件点击某一条告警事件触发弹框
	        $('.scrollBox').on('click', 'td.one p',function(e){
	            let id=$(e.target).attr('data-id');
	            let index = $(e.target).attr('data-index')
	            // console.log(index)
	            _this.warnDetails(id,index);
	        });
    	},
    	openBig (e){
          // 想要全屏展示的模块包含class名content 
          this.closeWindow();
          this.isShowMonitor = false;
		  var elem = document.querySelector('.content');
		  elem.style.padding = 0;
		  let allMap = document.getElementById('allmap');
          allMap.style.height = '100vh';
		  this.requestFullScreen(elem);
		  this.isScreen = true;
		  this.isShowAlarm = false; // 告警事件弹层隐藏
		  $('.mapRightTableBox').height('auto')
            // this.isUpDownQX=true; // 流量统计收起展开
            this.isShowIcons = false; //
            $('.mapRightZXBox').height('auto')
	    	// if(!this.isShowAlarm){ // 隐藏
	    	// 	clearInterval(window.warnUndealDataTimer2);
            //     // window.warnUndealDataTimer2 = setInterval(this.scrollTop, 3000);
	    	// }else{ // 展开
	    	// 	clearInterval(window.warnUndealDataTimer2);
	    	// }
		//   this.updataMap('all');
		},
		disableESC(e){
//			console.log(e,'eeeeeeee')
		},
		requestFullScreen (elem) {
		   // 兼容不同的浏览器
		  var requestMethod = elem.requestFullScreen || elem.webkitRequestFullScreen || elem.mozRequestFullScreen || elem.msRequestFullScreen;
		  if (requestMethod) {
		    requestMethod.call(elem)
		  }
		},
		closeBig(){
            this.closeWindow();
            this.isShowMonitor = false;
			this.$nextTick(()=>{		
				this.exitFullscreen();
			})
			let div = document.querySelector('.content');
            div.style.padding = "10px 0 0 10px";
            let allMap = document.getElementById('allmap');
            allMap.style.height = '100vh';
			// let oheight = document.body.clientHeight - ($('.tab-layout-tabs').height() * 1 + $('.header').height());
			// let oWidth = document.body.clientWidth - $('.ant-layout-sider').width();
	        //     $('#allmap').height(oheight);
	        //     $('#allmap').width(oWidth);
			this.isScreen = false;
			
            // this.isUpDownQX=false; // 流量统计收起展开
			this.isShowAlarm = false; // 告警事件 隐藏
            this.isShowIcons = false;
            $('.mapRightTableBox').height('26%')
            $('.mapRightZXBox').height('auto')
            clearInterval(window.warnUndealDataTimer2);
            // window.warnUndealDataTimer2 = setInterval(this.scrollTop, 3000);
            // this.updataMap();
//			this.calHeight();
        },
        exitFullscreen() { 
            if(document.exitFullscreen) { 
                document.exitFullscreen(); 
            } else if(document.mozExitFullScreen) { 
                document.mozExitFullScreen(); 
            } else if(document.webkitExitFullscreen) { 
                document.webkitExitFullscreen(); 
            } 
        },
        //监控中心-查询未处理告警数据
        warnUndealData(){
            warnUndealData(this)
        },
        //获取全量标段OR标注信息
        getData() {
            this.oMarker = [];
            getbiaoduanorbiaozhuData(this);
        },
        //初始化地图
        updataMap(methodType) {
            // console.log('初始化地图')
            var map = new AMap.Map("allmap",{ 
                pitch:50, 
                viewMode:'3D',
                center:[104.688198,31.395773],
                zoom:this.zoonLevel,
                zooms: [11.2, 15.2],
                scrollWheel:false
            });
            // 设置光照
            map.AmbientLight = new AMap.Lights.AmbientLight([1, 1, 1], 0.5);
            map.DirectionLight = new AMap.Lights.DirectionLight([0, 0, 1], [1, 1, 1], 1);
        
            // var point = new AMap.Point(104.708198,31.395773);
            var point = map.getCenter(); 
            window.map = map;
            window.point = point;
            // 创建地图实例
            // 创建点坐标
            map.setZoomAndCenter(this.zoonLevel, point); //同时设置地图层级与中心点
            // map.centerAndZoom(point, this.zoonLevel);

            // map.setMapStyleV2({styleJson:styleJson});

            // map.enableScrollWheelZoom(); //启用滚轮放大缩小，默认禁用
            // map.enableContinuousZoom(); //启用地图惯性拖拽，默认禁用

            //地址解析
            // var geoc = new AMap.GaoDeMapUtil();
            // var geoc = new AMap.Geocoder();


            // map.addEventListener('mousedown', function (e) {
            //     // alert('点击位置经纬度：' + e.latlng.lng + ',' + e.latlng.lat);
            //     // 计算两点间的距离
            //     var p1=new AMap.LngLat(104.7232, 31.4564);
            //     var p2=new AMap.LngLat(e.latlng.lng, e.latlng.lat);
            //     var distance = Math.round(p1.distance(p2));
            //     // var distance=map.getDistance(new AMap.Point(104.7232, 31.4564),new AMap.Point(e.latlng.lng, e.latlng.lat));
            //     if(distance>100000){
            //         map.setZoomAndCenter(12,new AMap.LngLat(104.7232, 31.4564),);
            //     }
            // //    console.log(distance,'==================================================')
            // });



            // map.addEventListener("click", function (e) {
            //     //通过点击百度地图，可以获取到对应的point, 由point的lng、lat属性就可以获取对应的经度纬度
            //     var pt = e.point;
            //     // console.log(pt);
            //     geoc.getLocation(pt, function (rs) {
            //         // //addressComponents对象可以获取到详细的地址信息
            //         // var addComp = rs.addressComponents;
            //         // var site = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
            //         // //将对应的HTML元素设置值
            //         // $("#site").val(site);
   
            //         // $("#longitude").val(pt.lng);
            //         // $("#latitude").val(pt.lat);
            //     });
            // });
            let _this = this;
            //zoomend 地图更改缩放级别结束时触发触发此事件
            // map.addEventListener('zoomend', function () {
            //     // if(map.getZoom()<15){
            //     //     map.centerAndZoom(point, 14);
            //     //     _this.updata(undefined, window.oMarker,point,map);
            //     //     return;
            //     // }
            //     _this.updata(methodType, window.oMarker);
            // })
            this.updata(methodType, window.oMarker);
            function ZoomControl() {
                // 默认停靠位置和偏移量
                this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
                this.defaultOffset = 0;
            }
        },
        //标注初始化加载
        updata(methodType, oMarker) {
    	    let that = this;
            var map = window.map;
            // var point = window.point;
            // var markerClusterer;
            var _this = this;
            // map.clearOverlays();
            // this.hierarchy = map.getZoom() + '级';
            let resultRivers = this.resultRivers;
            for (var i in resultRivers) {
                var pois = [];
                let rivers = JSON.parse(resultRivers[i].riverAddressInfo);

                for (var j = 0; j < rivers.length; j++) {
                    pois.push(new AMap.LngLat(rivers[j].longitude, rivers[j].latitude));
                }
                // var polyline;
                // polyline = new AMap.Polyline(pois, {
                //     enableEditing: false,   //是否启用线编辑，默认为false
                //     enableClicking: true,   //是否响应点击事件，默认为true
                //     strokeWeight: 5,        //边线的宽度，以像素为单位
                //     storkeOpacity: 0.8,     //边线透明度，取值范围0 - 1
                //     strokeColor: 'blue'    //边线颜色
                // });
                // map.addOverlay(polyline);


                // var points = [
                //     new AMap.LngLat(116.400433, 39.908084),
                //     new AMap.LngLat(83.52412, 34.777709),
                //     new AMap.LngLat(108.821972, 34.270829),
                //     new AMap.LngLat(104.067108, 30.65769)
                // ];
            
                // var map = new AMap.Map('container', {
                //     center: [108.011931, 37.071694],
                //     zoom: 6,
                //     viewMode: '3D',
                //     pitch: 70
                // });
            
                // console.log(pois)
                // console.log(map)
                // console.log('--------------------------------')
                // console.log('--------------------------------')
                // console.log('--------------------------------')
                // console.log('--------------------------------')
                // console.log('--------------------------------')
                var object3Dlayer = new AMap.Object3DLayer();
                var meshLine = new AMap.Object3D.MeshLine({
                    path: pois,
                    height: [8010],
                    color: '#FFFC00',
                    width: 7
                });
            
                meshLine.transparent = true;
                object3Dlayer.add(meshLine);
                meshLine['backOrFront'] = 'both';
                map.add(object3Dlayer);
            
            }

            let resultRegions = this.resultRegions;
            resultRegions.map((item,index) => {
                var resultRegion=[];
                var object3Dlayer = new AMap.Object3DLayer();
                map.add(object3Dlayer);
                var regionAddressInfo=resultRegions[index].regionAddressInfo;
                regionAddressInfo=regionAddressInfo.split(';');
                regionAddressInfo.map((item,index)=>{
                    let poi = item.split(',')
                    resultRegion.push(new AMap.LngLat(poi[0], poi[1]))
                })
                var height = 8000;
                var color = 'rgba(48, 150, 142, 0.4)';
                var prism = new AMap.Object3D.Prism({
                    path:resultRegion,
                    height:height,
                    color:color
                });
                prism.transparent = true;
                object3Dlayer.add(prism);//添加
                AMap.event.addListener(prism,'click', function (e) {
                    point = e.point;
                    map.setCenter([point.lng, point.lat]);
                    map.setZoom(that.clickZoonLevel); //设置地图层级
                    // map.centerAndZoom(new AMap.Point(point.lng, point.lat), that.clickZoonLevel);
                });
            })
            // console.log(resultRegion,'resultRegionresultRegionresultRegionresultRegion')

                // let polygon = new AMap.Polygon(resultRegions[index].regionAddressInfo, {
                //     strokeColor: '#5a9de2',
                //         strokeWeight: 5,
                //         fillColor: '#142950 ',
                //         fillOpacity: 0.5,
                //         enableMassClear: false
                // })
            //     let point = null;
            //     map.addOverlay(polygon)
              

            
              

            let newArr = [];
            // 创建标注对象并添加到地图
            // var marker = new AMap.Marker(point, {
            //     icon: this.myIcon
            // }); // 创建标注
            console.log(this.nameArr,'nameArrnameArrnameArrnameArr');
            map.remove(_this.markers);
            // map.remove(str)
            if (this.nameArr.length) {
                oMarker.filter((item) => {
                    _this.nameArr.map(index => {

                        if (item.modelTypeCode == index) {
                            newArr.push({
                                id: item.id,
                                latitude: item.latitude,
                                longitude: item.longitude,
                                modelZcImg: item.modelZcImg,
                                modelTypeCode: item.modelTypeCode,
                                modelOffset: item.modelOffset,
                                modelPosition: item.modelPosition,
                                modelType: _this.zoonLevel,
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
                        modelType: _this.zoonLevel,
                        list: index.list,
                        fmType: index.fmType,
                        equipmentSn: index.equipmentSn,
                        hkMonitorKey: index.hkMonitorKey,
                        equipLocation: index.equipLocation,
                    })
                })
            }
            console.log(oMarker,'oMarkeroMarkeroMarkeroMarkeroMarker')
            var markArr = [];
            for (var i = 0; i < oMarker.length; i++) {
                var str = oMarker[i].id;

                // _this.addComplexCustomOverlay1(oMarker[i], map);
                // let modelOffset = Number(oMarker[i].modelOffset);
                _this.markers.push(oMarker[i].modelZcImg);
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
                var index = oMarker[i].index;
                var opts = {
                    width: 0,

                    title: '提示',
                    enableMessage: true, //设置允许信息窗发送短息
                    message: ""
                };


                var json = {
                    'str': str,
                    'mark': oMarker[i],
                    'infoWindow': '',
                    id: id,
                    map: map,
                    oMarker: oMarker[i]
                }
                let timer = null;
                // AMap.event.addListener(str,'mouseover', function (e) {
                //     let data = {
                //         modelId: this.id
                //     }

                //     clearTimeout(timer);
                //     timer = setTimeout(() => {
                //         getGisModelDetailData2(data, _this, this, opts);
                //     }, 300);


                // }.bind(json));
                // AMap.event.addListener(str,'mouseout', function (e) {
                //     clearTimeout(timer);
                //     // $('#'+this.id).remove();
                //     // str.closeInfoWindow();
                // }.bind(json));
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
        addComplexCustomOverlay1(markData, map, index) {
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
            markData.list.forEach(item => {
                str += '<div class="item"><div class="name">'+ item.variableName+'：</div><div class="value">'+ item.varibleValue +'</div></div>'
            })
            if (this['markerContents'+index]) {
                this['markerContents'+index].setContent(`<div class="infoWin-pick ${markerClass
                    }"><div class="line1"></div><div class="line2"></div><div class="infoWin">${ str }<div></div>`
                );
                return;
            }
            let marker = null;
            if (markData.list && markData.list.length) {
                marker = new AMap.Marker({
                    map: map,
                    zIndex: 0,
                    offset: new AMap.Pixel(0, Number(markData.modelOffset)),
                    position: new AMap.LngLat(markData.longitude,markData.latitude),
                    content: `<div style="display:none;" class="infoWin-pick ${markerClass
                        }"><div class="line1"></div><div class="line2"></div><div class="infoWin">${ str }<div></div>`
                });
                setTimeout(() => {
                    $(`.${markerClass}`).parent().parent().addClass(`${markerClass}-parent`)
                    this.$nextTick(() => {
                        $(`.${markerClass}`).show();
                    });
                }, 900);
                marker.on('mouseover', function (e) {
                    marker.setzIndex(9999)
                })
                marker.on('mouseout', function (e) {
                    marker.setzIndex(0)
                })
            }
            this['markerContents'+index] = marker;
            return
            
            function ComplexCustomOverlay(point, text, directionType, id,type) {
                this._point = point;
                this._text = text;
                this._directionType = directionType;
                this._id = id;
                this._type=type;
            }
            ComplexCustomOverlay.prototype = new BMapGL.Overlay();
            ComplexCustomOverlay.prototype.initialize = function (map) {

                this._map = map;
                var div = this._div = document.createElement("div");
                // let div2=document.createElement("div");
                // div2.className="biaoshiTop";
                // div2.appendChild(document.createTextNode('提示'));
                
                div.className = "biaoshi";
                if (this._type == 1) {
                    div.className = "biaoshi error";
                }
                div.setAttribute('id', this._id)
                div.style.position = "absolute";
                div.style.zIndex = BMapGL.Overlay.getZIndex(this._point.lat);
                // div.appendChild(div2);
                if (this._text.indexOf('开') != -1) {
                    div.style.backgroundColor = "rgb(107,173,202)";
                    div.style.border = "1px solid rgb(107,173,202)";
                } else if (this._text.indexOf('关') != -1) {
                    div.style.backgroundColor = "#EE5D5B";
                    div.style.border = "1px solid #BC3B3A";
                } else {
                    div.style.backgroundColor = "rgb(107,173,202)";
                    div.style.border = "1px solid rgb(107,173,202)";
                }
                div.style.backgroundColor = "rgb(107,173,202)";
                div.style.border = "1px solid rgb(107,173,202)";
                div.style.color = "white";
                // div.style.height = "23px";
                div.style.padding = "2px";
                div.style.lineHeight = "18px";
                div.style.whiteSpace = "nowrap";
                div.style.MozUserSelect = "none";
                div.style.fontSize = "12px";
                div.style.left = "10px";

                if (this._text.split(',').length > 1) {
                    var textArr = this._text.split(',');

                    for (var i = 0; i < textArr.length; i++) {
                        var span = this._span = document.createElement("span");
                        var strong = this._strong = document.createElement("strong");
                        var oP = this._oP = document.createElement("p");
                        var oI = this._oI = document.createElement("i");
                        
                        div.appendChild(oP);
                        oP.appendChild(strong);

                        oP.appendChild(oI);
                        oP.appendChild(span);
                        oI.appendChild(document.createTextNode('：'));
                        span.appendChild(document.createTextNode(textArr[i].split(':')[1]));
                        strong.appendChild(document.createTextNode(textArr[i].split(':')[0]));
                    }
                } else {
                    var span = this._span = document.createElement("span");
                    var strong = this._strong = document.createElement("strong");
                    var oP = this._oP = document.createElement("p");
                    var oI = this._oI = document.createElement("i");
                    if (this._type == 1) {
                        div.appendChild(oP);
                        oP.appendChild(document.createTextNode(this._text.split(':')[1]));
                    }else{
                        div.appendChild(oP);
                        oP.appendChild(strong);
                        oP.appendChild(oI);
                        oP.appendChild(span);

                        oI.appendChild(document.createTextNode('：'));
                        span.appendChild(document.createTextNode(this._text.split(':')[1]));
                        strong.appendChild(document.createTextNode(this._text.split(':')[0]));
                    }
                    
                }


                var that = this;

                var arrow = this._arrow = document.createElement("div");
                arrow.className = "biaoshijt";
                // arrow.style.background ="url("+require("./images/label.png")+")0 -20px no-repeat";

                arrow.style.position = "absolute";
                arrow.style.width = "11px";
                arrow.style.height = "10px";
                arrow.style.left = "10px";
                if (this._directionType == 1) {
                    arrow.style.top = "23px";
                    arrow.style.left = "60px";
                } else if (this._directionType == 3) {
                    arrow.style.top = "-20px";
                    arrow.style.transform = "rotate(320deg)"
                } else if (this._directionType == 4) {
                    arrow.style.transform = "rotate(264deg)"
                    arrow.style.top = "5px";
                    arrow.style.left = "66px";
                } else if (this._directionType == 2) {
                    arrow.style.transform = "rotate(260deg) skewX(-60deg)"
                    arrow.style.top = "-20px";
                    arrow.style.left = "-20px";
                }else{
                    arrow.style.top = "23px";
                    arrow.style.left = "60px";
                }
                if (this._text.split(',').length > 1) {
                    if (this._directionType == 1) {
                        arrow.style.top = (this._text.split(',').length * 18) * 1 + 10 + 'px';
                    } else if (this._directionType == 3) {

                        arrow.style.top = "-30px";
                        arrow.style.transform = "rotate(260deg) skewX(-60deg)"
                    } else if (this._directionType == 4) {
                        arrow.style.transform = "rotate(264deg)"
                        arrow.style.top = "5px";
                        arrow.style.left = "66px";
                    } else if (this._directionType == 2) {
                        arrow.style.transform = "rotate(260deg) skewX(-40deg)"
                        arrow.style.top = "5px";
                        arrow.style.left = "-20px";
                    }else{
                        arrow.style.top = (this._text.split(',').length * 18) * 1 + 10 + 'px';
                    }

                }

                arrow.style.overflow = "hidden";

                div.appendChild(arrow);
                div.addEventListener('mouseover', function () {
                    this.style.zIndex = '100000';
                }.bind(div))
                div.addEventListener('mouseout', function () {
                    this.style.zIndex = '-100000';
                }.bind(div))

                map.getPanes().labelPane.appendChild(div);

                return div;
            }
            ComplexCustomOverlay.prototype.draw = function () {
                var map = this._map;
                var pixel = map.pointToOverlayPixel(this._point);
                if (this._text.split(',').length > 1) {
                    if (this._directionType == 1) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) -30+ "px";
                        this._div.style.top = pixel.y - (this._text.split(',').length * 18 + 55) + "px";

                    } else if (this._directionType == 3) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) +23 + "px";
                        this._div.style.top = pixel.y + 38 + "px";
                    } else if (this._directionType == 2) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + 25 + "px";
                        this._div.style.top = pixel.y - 17 + "px";
                    }else{
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) - 30 + "px";
                        this._div.style.top = pixel.y - (this._text.split(',').length * 18 + 55) + "px";
                    }
                } else {
                    if (this._directionType == 1) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) -25 + "px";
                        this._div.style.top = pixel.y - 65 + "px";
                    } else if (this._directionType == 3) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + 5 + "px";
                        this._div.style.top = pixel.y + 16 + "px";
                    } else if (this._directionType == 4) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) - 20 + "px";
                        this._div.style.top = pixel.y - 10 + "px";

                    } else if (this._directionType == 2) {
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + 25 + "px";
                        this._div.style.top = pixel.y + 25 + "px";
                    }else{
                        this._div.style.left = pixel.x - parseInt(this._arrow.style.left) - 25 + "px";
                        this._div.style.top = pixel.y - 65 + "px";
                    }
                }


            }
            ComplexCustomOverlay.prototype.setText = function (aaa) {
                this._text = aaa;
            }
            var myCompOverlay;
            if (oMarker.list != undefined) {
                if (oMarker.list.length > 0) {
                    let listStr = '';
                    let listArr = [];
                    for (let s = 0; s < oMarker.list.length; s++) {
                        
                        listArr.push(oMarker.list[s].variableName + ':' + oMarker.list[s].varibleValue);
                        
                        if (oMarker.id == '1218732296129044482') {

                        }
                        myCompOverlay = new ComplexCustomOverlay(new BMapGL.Point(oMarker.longitude, oMarker.latitude),
                            listArr.join(','), oMarker.modelPosition, oMarker.id, oMarker.fmType);
                    }

                }
            }
            // console.log("自定义================")
            // console.log(oMarker)
            // console.log(myCompOverlay)

            map.addOverlay(myCompOverlay);
        },
        //标注里某一项数值变化监听调取函数
        upDataoMarker() {
            this.updata('',window.oMarker);
//          this.updata(window.oMarker);
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
        //设备详情-弹框里的参数初始化
        resetDetails() {
            this.usageState = ''; //获取详情-使用状态
            this.deviceStatus = ''; //获取详情-设备状态
            this.assetCoding = ''; //获取详情-资产编号
            this.assetName = ''; //获取详情-资产名称
            this.manufacturer = ''; //获取详情-生产厂家
            this.informationEquipmentType = ''; //获取详情-设备型号
            this.assetLocation = ''; //获取详情-资产位置
            this.attributeList = []; //资产特殊属性列表
        },
        //地图放大按钮效果
        enlarge(){
            let size = window.map.getZoom() * 1 + 0.5;
            if(size>15)return;
            window.map.setZoom(size);
        },
        //地图缩小按钮效果
        narrow(){
            let size = window.map.getZoom() * 1 - 0.5;
            if (size<11.2) return
            window.map.setZoom(size);
            console.log( window.map.getZoom() * 1 )
        },
        //右侧告警事件滚动JS
        roll2(arr){
            let str='';
            $.each(arr,function (i, item) {
                // if(i%2){
                //     str += '<tr>' +
                //         '<td class="one"><a href="#/alarmManagement/businessWarnList?id=' + item.id + '&time=' + item.warnTime + '">' + item.warnName + '</a></td>' +
                //         '<td class="six">' + item.warnTime + '</td>' +
                //         '<td class="seven">...</td > ' +
                //         '</tr>'
                // }else{
                //     str += '<tr>' +
                //         '<td class="one"><a href="#/alarmManagement/businessWarnList?id=' + item.id + '&time=' + item.warnTime + '">' + item.warnName + '</a></td>' +
                //         '<td class="six">' + item.warnTime + '</td>' +
                //         '<td class="seven">。。。</td > ' +
                //         '</tr>'
                // }
                // if (item.confirmStatus==0){
                //     str += '<tr class="baise" title="' + item.warnContent + '">' +
                //         '<td class="one" data-id="' + item.id + '" data-index="' + i + '"><p data-id="' + item.id + '" data-index="' + i + '">' + item.warnContent + '</p></td>' +

                //         '</tr>'
                // } else if (item.confirmStatus == '警告' && item.warnStatusCode != '严重'){
                    if (item.warnLevel=='紧急'){
                        str += '<tr title="' + item.warnContent + '">' +
                            '<td class="one" data-id="' + item.id + '" data-index="' + i + '"><p data-id="' + item.id + '" data-index="' + i + '">' +'<span class="hongse1">警</span>'+item.warnContent + '</p></td>' +

                                '</tr>'
                    } else if (item.warnLevel == '严重'){
                        str += '<tr title="' + item.warnContent + '">' +
                            '<td class="one" data-id="' + item.id + '" data-index="' + i + '"><p data-id="' + item.id + '" data-index="' + i + '">' + '<span class="hongse1">警</span>'+item.warnContent + '</p></td>' +

                                '</tr>'
                    } else if (item.warnLevel == '警告' || item.warnLevel == '一般') {
                        str += '<tr title="' + item.warnContent + '">' +
                            '<td class="one" data-id="' + item.id + '" data-index="' + i + '"><p data-id="' + item.id + '" data-index="' + i + '">' +'<span class="hongse1">警</span>'+ item.warnContent + '</p></td>' +

                                '</tr>'
                    }
                // }
            });
            if (!str) {
                let str2 = '<div class="empty_icon">' +
                            '<img src="'+ this.emptyIcon +'">' +
                            '<p>暂无数据</p>' +
                            '</div>'
                $('.tab-scroll').append(str2);
            } else {
                $('.tab-scroll tbody').append(str);
                if($('.scroll-box').height()<$('.tab-scroll').height()){
                    $('.tab-scroll2 tbody').append(str);
                }
                
                clearInterval(window.warnUndealDataTimer2);
                
                // window.warnUndealDataTimer2 = setInterval(this.scrollTop, 3000);
            }
        },
        //右侧告警事件滚动JS
        scrollTop(){
            var trs=$(".tab-scroll tbody").find("tr");
            if($('.tab-scroll').height()<$('.scroll-box').height())return;
            $('.scroll-box').animate({
                top: '-=30'
            },500,function(){
                var height=($('.tab-scroll').height()+$('.tab-scroll').height())-$('.scroll-box').height();

                if(Math.abs(parseInt($('.scroll-box').css("top")))>=height){
                    $('.scroll-box').css("top", 0);
                }
                // if(Math.abs(parseInt($('.tab-scroll').css("top")))>= trs.eq(0).height())
                // if(Math.abs(parseInt($('.tab-scroll').css("top")))>= trs.eq(0).height()){
                //     trs.slice(0,1).appendTo($(".tab-scroll tbody"));
                //     $(".tab-scroll").css("top", 0);
                    
                // }
            });
        },
        //左侧筛选全选or反选
        allSelect(e){
            let newArr = [];

            let _this=this;
            if ($(e.target).hasClass('active')) {
                $(e.target).removeClass('active');
                $(e.target).text('全选');
                $('.stateSelectionUlBox li').removeClass('active');
                _this.nameArr = [];

                window.oMarker.filter((item) => {
                    _this.nameArr.map(index => {

                        if (item.modelTypeCode == index) {

                            newArr.push({
                                id: item.id,
                                latitude: item.latitude,
                                longitude: item.longitude,
                                modelZcImg: item.modelZcImg,
                                modelTypeCode: item.modelTypeCode,
                                modelOffset: item.modelOffset,
                                modelPosition: item.modelPosition,
                                modelType: _this.zoonLevel,
                                list: item.list,
                                fmType: item.fmType,
                                equipmentSn: item.equipmentSn,
                                hkMonitorKey: item.hkMonitorKey,
                            })
                        }
                    })

                });
                _this.updata('',newArr);
            } else {
                $(e.target).addClass('active');
                $(e.target).text('反选');
                $('.stateSelectionUlBox li').addClass('active');
                _this.nameArr = _this.allArrName;
                window.oMarker.filter((item) => {
                    _this.nameArr.map(index => {

                        if (item.modelTypeCode == index) {

                            newArr.push({
                                id: item.id,
                                latitude: item.latitude,
                                longitude: item.longitude,
                                modelZcImg: item.modelZcImg,
                                modelTypeCode: item.modelTypeCode,
                                modelOffset: item.modelOffset,
                                modelPosition: item.modelPosition,
                                modelType: _this.zoonLevel,
                                list: item.list,
                                fmType: item.fmType,
                                equipmentSn: item.equipmentSn,
                                hkMonitorKey: item.hkMonitorKey,
                            })


                        }
                    })

                });

                _this.updata('',newArr);
            }
        },
        //左侧筛选单选
        selectLi(e){
            let name = $(e.target).attr('data-name');
            let idArr = [];
            let newArr = [];
            let _this=this;
            if ($(e.target).parent().hasClass('active')) {
            	$(e.target).parent().removeClass('active');
                $('.allSelect').text('全选');
                $('.biaoshi').remove();
                _this.nameArr.map((index, i) => {
                    if (index == name) {
                        _this.nameArr.splice(i, 1);
                    }
                });
                window.oMarker.filter((item) => {
                    _this.nameArr.map(index => {
                        if (item.modelTypeCode == index) {
                            newArr.push({
                                id: item.id,
                                latitude: item.latitude,
                                longitude: item.longitude,
                                modelZcImg: item.modelZcImg,
                                modelTypeCode: item.modelTypeCode,
                                modelOffset: item.modelOffset,
                                modelPosition: item.modelPosition,
                                modelType: _this.zoonLevel,
                                list: item.list,
                                fmType: item.fmType,
                                equipmentSn: item.equipmentSn,
                                hkMonitorKey: item.hkMonitorKey,
                            })
                        }
                    })

                });
                _this.updata('',newArr);
//              _this.updata(newArr);

                $('.allSelect').removeClass('active')
            } else {
                _this.nameArr.push(name);
                
                window.oMarker.filter((item) => {
                    _this.nameArr.map(index => {

                        if (item.modelTypeCode == index) {
                            newArr.push({
                                id: item.id,
                                latitude: item.latitude,
                                longitude: item.longitude,
                                modelZcImg: item.modelZcImg,
                                modelTypeCode: item.modelTypeCode,
                                modelOffset: item.modelOffset,
                                modelPosition: item.modelPosition,
                                modelType: _this.zoonLevel,
                                list: item.list,
                                fmType: item.fmType,
                                equipmentSn: item.equipmentSn,
                                hkMonitorKey: item.hkMonitorKey,
                            })


                        }
                    })

                });
                
                $(e.target).parent().addClass('active');
                _this.updata('',newArr);
//              _this.updata(newArr);
                if ($('.stateSelectionUlBox li.active').length >= _this.gisDashboardGisModelNavList.length) {
                    $('.allSelect').addClass('active')
                    $('.allSelect').text('反选')
                } else {
                    $('.allSelect').removeClass('active')

                }

            }
        },
        //左侧筛选展开或者关闭
        openOrclose(e){
            let height = $('.openOrclose').height() * 1 + $('.allSelect').height() * 1 + $('.stateSelectionUlBox').height()*1;
            if($(e.target).hasClass('active')){
                $(e.target).removeClass('active');
                $(e.target).text('展开');
                $('.stateSelectionBox').height($('.openOrclose').height());
            }else{
                $(e.target).addClass('active');
                $(e.target).text('收起');
                $('.stateSelectionBox').height(height);
            }
        },
        //右侧告警事件详情获取
        warnDetails(id,index){
            // console.log(this.tabScrollList[index]);
            this.alarmIndex=index;
            this.alarmEventType=this.tabScrollList[index].confirmStatus;
            this.alarmEventType2 = this.tabScrollList[index].warnStatusCode;
            this.alarmEventVisible=true;
            setTimeout(()=>{   
            	$('.formHeight').height($('.ruleHeight').height())
            },200)
            this.warnSn='';
            this.warnName = '';
            this.warnContent = '';
            this.equipmentType = '';
            this.warnLevel = '';
            this.ruleContent = '';
            this.warnTime = '';

            this.warnSn = this.tabScrollList[index].warnSn;
            this.warnName = this.tabScrollList[index].warnName;
            this.warnContent = this.tabScrollList[index].warnContent;
            this.equipmentType = this.tabScrollList[index].equipmentType;
            this.warnLevel = this.tabScrollList[index].warnLevel;
            this.ruleContent = this.tabScrollList[index].ruleContent;
            this.warnTime = this.tabScrollList[index].warnTime;
        },
        //地图中报警标注告警事件详情获取
        warnDetails2(businessWarnVo) {
            
            this.alarmEventVisible = true;
            this.warnSn = '';
            this.warnName = '';
            this.warnContent = '';
            this.equipmentType = '';
            this.warnLevel = '';
            this.ruleContent = '';
            this.warnTime = '';
            this.alarmEventType = businessWarnVo.confirmStatus;
            this.alarmEventType2 = businessWarnVo.warnStatusCode;
            this.warnSn = businessWarnVo.warnSn;
            this.warnName = businessWarnVo.warnName;
            this.warnContent = businessWarnVo.warnContent;
            this.equipmentType = businessWarnVo.equipmentType;
            this.warnLevel = businessWarnVo.warnLevel;
            this.ruleContent = businessWarnVo.ruleContent;
            this.warnTime = businessWarnVo.warnTime;
            this.alarmEventConfigurationId = businessWarnVo.id;
        },
        //告警详情关闭
        alarmEventonCancel(){
            this.alarmEventVisible=false;
        },
        //告警详情知悉效果
        Know(){
            let data 
            if (this.alarmEventConfigurationId){
                data = {
                    id: this.alarmEventConfigurationId
                }
            }else{
                data = {
                    id: this.tabScrollList[this.alarmIndex].id
                }
            }
            
            gisAlarmKnow(data, this);
        },
        //告警详情解除效果
        relieve(){
            this.turnOffAlarmvisible=true;
            this.closeDescription='';
        },
        //告警详情处理效果
        handle(){
            this.alarmHandlingvisible=true;
            this.description='';
        },
        //告警处理弹框确定
        alarmHandlingOk() {
            if (!this.description) {
                this.$message.info('备注不能为空!');
                return;
            }
            let data
            if (this.alarmEventConfigurationId) {
                data = {
                    description: $.trim(this.description),
                    id: this.alarmEventConfigurationId,
                    warnTime: this.warnTime
                }
            } else {
                data = {
                    description: $.trim(this.description),
                    id: this.tabScrollList[this.alarmIndex].id,
                    warnTime: this.tabScrollList[this.alarmIndex].warnTime
                }
            }
            gisAlarmHandle(data, this);
        },
        //告警处理弹框关闭
        alarmHandlingCancel(){
            this.alarmHandlingvisible=false;
        },
        //关闭告警确定
        turnOffAlarmOk() {
            let data
            if (this.alarmEventConfigurationId) {
                data = {
                    description: $.trim(this.description),
                    id: this.alarmEventConfigurationId,
                    warnTime: this.warnTime
                }
            } else {
                data = {
                    description: $.trim(this.closeDescription),
                    id: this.tabScrollList[this.alarmIndex].id,
                    warnTime: this.tabScrollList[this.alarmIndex].warnTime
                }
            }
            gisAlarmRelieve(data,this);
        },
        //关闭告警取消
        turnOffAlarmCancel() {
            this.turnOffAlarmvisible = false;
        },
        //监控
        videoPreview(val, index) {
            this.position = index;
            this.oWebControl.JS_ShowWnd();
            this.pointCode = val.pointCode;
            this.startpreview();
        },
        // 创建播放实例
        initPlugin() {
            this.oWebControl = new WebControl({
                szPluginContainer: "playWnd", // 指定容器id
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
                                    .JS_CreateWnd("playWnd", this.swfWidth, this.swfHeight)
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
                    // $("#playWnd").html("插件未启动，正在尝试启动，请稍候...");
                    let htmls = '<div class="videoTips">插件启动失败，请检查插件是否安装!</div>' +
                        '<div class="videoTips">复制下面链接，在浏览器中打开 即可下载安装</div>' +
                        '<div class="videoTipsUrl">'+this.httpUrl.httpUrl+'</div>'
                    this.WebControl.JS_WakeUp("VideoWebPlugin://"); // 程序未启动时执行error函数，采用wakeup来启动程序
                    this.initCount++;
                    if (this.initCount < 3) {
                        setTimeout(() => {
                            this.initPlugin();
                        }, 3000);
                    } else {
                        console.log("插件启动失败，请检查插件是否安装!");
                        $("#playWnd").append(htmls);
                        $("#playWnd").css({"background":"#ffffff"});
                        $(".videoTips").css({"width":"100%","text-align":"center","padding-top":"30px"});
                        $(".videoTipsUrl").css({"width":"100%","text-align":"center","padding-top": "30px"});
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
        // 设置窗口控制回调
        // setCallbacks() {
        //     this.oWebControl.JS_SetWindowControlCallback({
        //         cbIntegrationCallBack: this.cbIntegrationCallBack
        //     });
        // },
        // 推送消息
        cbIntegrationCallBack(oData) {
            console.log(oData.responseMsg);
            /* showCBInfo(JSON.stringify(oData.responseMsg)); */
        },
        //初始化
        init() {
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
        // 设置窗口裁剪，当因滚动条滚动导致窗口需要被遮住的情况下需要JS_CuttingPartWindow部分窗口
        setWndCover() {
            let iWidth = $(window).width();
            let iHeight = $(window).height();
            let oDivRect = $("#playWnd")
                .get(0)
                .getBoundingClientRect();
            let iCoverLeft = oDivRect.left < 0 ? Math.abs(oDivRect.left) : 0;
            let iCoverTop = oDivRect.top < 0 ? Math.abs(oDivRect.top) : 0;
            let iCoverRight =
                oDivRect.right - iWidth > 0 ? Math.round(oDivRect.right - iWidth) : 0;
            let iCoverBottom =
                oDivRect.bottom - iHeight > 0
                    ? Math.round(oDivRect.bottom - iHeight)
                    : 0;

            iCoverLeft = iCoverLeft > this.swfWidth ? this.swfWidth : iCoverLeft;
            iCoverTop = iCoverTop > this.swfHeight ? this.swfHeight : iCoverTop;
            iCoverRight = iCoverRight > this.swfWidth ? this.swfWidth : iCoverRight;
            iCoverBottom =
                iCoverBottom > this.swfHeight ? this.swfHeight : iCoverBottom;
            this.oWebControl.JS_RepairPartWindow(0, 0, this.swfWidth+1, this.swfHeight); // 多1个像素点防止还原后边界缺失一个像素条
            if (iCoverLeft != 0) {
                this.oWebControl.JS_CuttingPartWindow(0, 0, iCoverLeft, this.swfHeight);
            }
            if (iCoverTop != 0) {
                this.oWebControl.JS_CuttingPartWindow(0, 0, this.swfWidth+1, iCoverTop); // 多剪掉一个像素条，防止出现剪掉一部分窗口后出现一个像素条
            }
            if (iCoverRight != 0) {
                this.oWebControl.JS_CuttingPartWindow(
                    this.swfWidth - iCoverRight,
                    0,
                    iCoverRight,
                    this.swfHeight
                );
            }
            if (iCoverBottom != 0) {
                this.oWebControl.JS_CuttingPartWindow(
                    0,
                    this.swfHeight - iCoverBottom,
                    this.swfWidth,
                    iCoverBottom
                );
            }
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
        //获取综合安防平台配置(获取海康密钥)
        getArtemisConfig(){
            let data = this.hkMonitorKey
            getArtemisConfig({configKey: '001'}, this, () => {
                setTimeout(() => { 
                    this.initPlugin()
                }, 2000);
            })
        },
        closeMonitor(){
            this.closeWindow();
            this.isShowMonitor=false;
        },
        //创建自定义覆盖物
        // addComplexCustomOverlay1(oMarker, map) {
        //     function ComplexCustomOverlay(point, text, directionType, id,type) {
        //         this._point = point;
        //         this._text = text;
        //         this._directionType = directionType;
        //         this._id = id;
        //         this._type=type;
        //     }
        //     ComplexCustomOverlay.prototype = new AMap.Overlay();
        //     ComplexCustomOverlay.prototype.initialize = function (map) {

        //         this._map = map;
        //         var div = this._div = document.createElement("div");
        //         // let div2=document.createElement("div");
        //         // div2.className="biaoshiTop";
        //         // div2.appendChild(document.createTextNode('提示'));
                
        //         div.className = "biaoshi";
        //         if (this._type == 1) {
        //             div.className = "biaoshi error";
        //         }
        //         div.setAttribute('id', this._id)
        //         div.style.position = "absolute";
        //         div.style.zIndex = AMap.Overlay.getZIndex(this._point.lat);
        //         // div.appendChild(div2);
        //         if (this._text.indexOf('开') != -1) {
        //             div.style.backgroundColor = "rgb(107,173,202)";
        //             div.style.border = "1px solid rgb(107,173,202)";
        //         } else if (this._text.indexOf('关') != -1) {
        //             div.style.backgroundColor = "#EE5D5B";
        //             div.style.border = "1px solid #BC3B3A";
        //         } else {
        //             div.style.backgroundColor = "rgb(107,173,202)";
        //             div.style.border = "1px solid rgb(107,173,202)";
        //         }
        //         div.style.backgroundColor = "rgb(107,173,202)";
        //         div.style.border = "1px solid rgb(107,173,202)";
        //         div.style.color = "white";
        //         // div.style.height = "23px";
        //         div.style.padding = "2px";
        //         div.style.lineHeight = "18px";
        //         div.style.whiteSpace = "nowrap";
        //         div.style.MozUserSelect = "none";
        //         div.style.fontSize = "12px";
        //         div.style.left = "10px";

        //         if (this._text.split(',').length > 1) {
        //             var textArr = this._text.split(',');

        //             for (var i = 0; i < textArr.length; i++) {
        //                 var span = this._span = document.createElement("span");
        //                 var strong = this._strong = document.createElement("strong");
        //                 var oP = this._oP = document.createElement("p");
        //                 var oI = this._oI = document.createElement("i");
                        
        //                 div.appendChild(oP);
        //                 oP.appendChild(strong);

        //                 oP.appendChild(oI);
        //                 oP.appendChild(span);
        //                 oI.appendChild(document.createTextNode('：'));
        //                 span.appendChild(document.createTextNode(textArr[i].split(':')[1]));
        //                 strong.appendChild(document.createTextNode(textArr[i].split(':')[0]));
        //             }
        //         } else {
        //             var span = this._span = document.createElement("span");
        //             var strong = this._strong = document.createElement("strong");
        //             var oP = this._oP = document.createElement("p");
        //             var oI = this._oI = document.createElement("i");
        //             if (this._type == 1) {
        //                 div.appendChild(oP);
        //                 oP.appendChild(document.createTextNode(this._text.split(':')[1]));
        //             }else{
        //                 div.appendChild(oP);
        //                 oP.appendChild(strong);
        //                 oP.appendChild(oI);
        //                 oP.appendChild(span);

        //                 oI.appendChild(document.createTextNode('：'));
        //                 span.appendChild(document.createTextNode(this._text.split(':')[1]));
        //                 strong.appendChild(document.createTextNode(this._text.split(':')[0]));
        //             }
                    
        //         }


        //         var that = this;

        //         var arrow = this._arrow = document.createElement("div");
        //         arrow.className = "biaoshijt";
        //         // arrow.style.background ="url("+require("./images/label.png")+")0 -20px no-repeat";

        //         arrow.style.position = "absolute";
        //         arrow.style.width = "11px";
        //         arrow.style.height = "10px";
        //         arrow.style.left = "10px";
        //         if (this._directionType == 1) {
        //             arrow.style.top = "23px";
        //             arrow.style.left = "60px";
        //         } else if (this._directionType == 3) {
        //             arrow.style.top = "-20px";
        //             arrow.style.transform = "rotate(320deg)"
        //         } else if (this._directionType == 4) {
        //             arrow.style.transform = "rotate(264deg)"
        //             arrow.style.top = "5px";
        //             arrow.style.left = "66px";
        //         } else if (this._directionType == 2) {
        //             arrow.style.transform = "rotate(260deg) skewX(-60deg)"
        //             arrow.style.top = "-20px";
        //             arrow.style.left = "-20px";
        //         }else{
        //             arrow.style.top = "23px";
        //             arrow.style.left = "60px";
        //         }
        //         if (this._text.split(',').length > 1) {
        //             if (this._directionType == 1) {
        //                 arrow.style.top = (this._text.split(',').length * 18) * 1 + 10 + 'px';
        //             } else if (this._directionType == 3) {

        //                 arrow.style.top = "-30px";
        //                 arrow.style.transform = "rotate(260deg) skewX(-60deg)"
        //             } else if (this._directionType == 4) {
        //                 arrow.style.transform = "rotate(264deg)"
        //                 arrow.style.top = "5px";
        //                 arrow.style.left = "66px";
        //             } else if (this._directionType == 2) {
        //                 arrow.style.transform = "rotate(260deg) skewX(-40deg)"
        //                 arrow.style.top = "5px";
        //                 arrow.style.left = "-20px";
        //             }else{
        //                 arrow.style.top = (this._text.split(',').length * 18) * 1 + 10 + 'px';
        //             }

        //         }

        //         arrow.style.overflow = "hidden";

        //         div.appendChild(arrow);
        //         div.addEventListener('mouseover', function () {
        //             this.style.zIndex = '100000';
        //         }.bind(div))
        //         div.addEventListener('mouseout', function () {
        //             this.style.zIndex = '-100000';
        //         }.bind(div))

        //         map.getPanes().labelPane.appendChild(div);

        //         return div;
        //     }
        //     ComplexCustomOverlay.prototype.draw = function () {
        //         var map = this._map;
        //         var pixel = map.pointToOverlayPixel(this._point);
        //         if (this._text.split(',').length > 1) {
        //             if (this._directionType == 1) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) -30+ "px";
        //                 this._div.style.top = pixel.y - (this._text.split(',').length * 18 + 55) + "px";

        //             } else if (this._directionType == 3) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) +23 + "px";
        //                 this._div.style.top = pixel.y + 38 + "px";
        //             } else if (this._directionType == 2) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + 25 + "px";
        //                 this._div.style.top = pixel.y - 17 + "px";
        //             }else{
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) - 30 + "px";
        //                 this._div.style.top = pixel.y - (this._text.split(',').length * 18 + 55) + "px";
        //             }
        //         } else {
        //             if (this._directionType == 1) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) -25 + "px";
        //                 this._div.style.top = pixel.y - 65 + "px";
        //             } else if (this._directionType == 3) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + 5 + "px";
        //                 this._div.style.top = pixel.y + 16 + "px";
        //             } else if (this._directionType == 4) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) - 20 + "px";
        //                 this._div.style.top = pixel.y - 10 + "px";

        //             } else if (this._directionType == 2) {
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + 25 + "px";
        //                 this._div.style.top = pixel.y + 25 + "px";
        //             }else{
        //                 this._div.style.left = pixel.x - parseInt(this._arrow.style.left) - 25 + "px";
        //                 this._div.style.top = pixel.y - 65 + "px";
        //             }
        //         }


        //     }
        //     ComplexCustomOverlay.prototype.setText = function (aaa) {
        //         this._text = aaa;
        //     }
        //     var myCompOverlay;
        //     if (oMarker.list != undefined) {
        //         if (oMarker.list.length > 0) {
        //             let listStr = '';
        //             let listArr = [];
        //             for (let s = 0; s < oMarker.list.length; s++) {
                        
        //                 listArr.push(oMarker.list[s].variableName + ':' + oMarker.list[s].varibleValue);
                        
        //                 if (oMarker.id == '1218732296129044482') {

        //                 }
        //                 myCompOverlay = new ComplexCustomOverlay(new AMap.Point(oMarker.longitude, oMarker.latitude),
        //                     listArr.join(','), oMarker.modelPosition, oMarker.id, oMarker.fmType);
        //             }

        //         }
        //     }
        //     // console.log("自定义================")
        //     // console.log(oMarker)
        //     // console.log(myCompOverlay)

        //     map.addOverlay(myCompOverlay);
        // },
    },
    beforeUpdate(){
//  	document.onkeydown = function(event){
//  		console.log("beforeUpdate")
//			    var keyValue
//			    keyValue = event.keyCode
//			    if ((keyValue==27)||(keyValue==122)) {
//			        event.cancelBubble = true;
//			        event.returnvalue=false;
//			        return false;
//
//			    }
//
//			}
    },
    beforeDestroy(){
        getUrl2();
        // 离开地图页面后停止调用token定时获取
        clearInterval(window.timer);
        /*  getTrafficStatisticsDataTimer 此接口不用了   */
        //离开地图页面后停止调用流量统计(不包括单日的)
//      clearInterval(window.getTrafficStatisticsDataTimer);


        //离开地图页面后停止调用告警事件
        clearInterval(window.warnUndealDataTimer);

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
    destroyed(){
        this.closeWindow();//关闭插件
    }
}