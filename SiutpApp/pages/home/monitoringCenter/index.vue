<template>
	<view class="page-monitor" v-if="showPage">

		<ss-navbar :navArr="navList" :tabCurrentIndex="currentIndex" @navbarTap="goTypeList"></ss-navbar>
		<view class="type-box" v-if="typeList !== null">
			<view class="list" v-if="typeList.length">
				<uni-collapse class="item" accordion="true" @change="changeType">
					<uni-collapse-item class="info" v-for="(item, i) in typeList" :title="`${item.equipmentName}[${item.showData}]`" :key="item.gisId">
						<view class="infoList">
							<template v-if="typeChilds['item' + i] && typeChilds['item' + i].length">
								<view class="infoItem uni-fs24" v-for="(one, j) in typeChilds['item' + i]" :key="j">
									{{one.variableName}}<view>{{one.varibleValue}}</view>
								</view>
							</template>
							<template v-else> 
								<view class="infoItem uni-fs24">暂无数据</view>
							</template>
						</view>
					</uni-collapse-item>
				</uni-collapse>
			</view>
			<view class="nullData" v-else>
				<image src="/static/null.png" mode="widthFix" />
				<view>很抱歉，暂无数据</view>
			</view>
		</view>
	</view>
</template>
<script>
	var amap = require('@/api/amap/amap-wx.130.js');//如：..­/..­/libs/amap-wx.js
	import {
		getGisModelNav,
		getGisEquipment,
		getGisEquipmentState,
		getGisTypeList,
		getGisTypeDetailList,
		getGisoMarkerData,
		getGisModelDetailData,
		getGisRiversData,
		getGisRegionsData
	} from '../../../api/api_gis.js'
	import ssNavbar from '../../../components/ss-navbar/ss-navbar.vue'
	export default {
		components: {
			ssNavbar
		},
		data() {
			return {
				showPage: false,
				height: 420, // 地图高度
				divHeight: 400+'px', // 列表高度
				markerList: [], // 设备列表
				typeList: null, // 类型列表
				typeChilds: {}, // 每个类型对应的子对象
				navList: [], // nav导航
				nameArr: [],
				currentIndex: 0, // 当前导航对应的索引
				positionTimer: null, // 点击list下拉隔5秒刷新也要显示
				detailTimer: null,
				listIndex: 0,
				gisId: '',
				isChange: false,
				isChange1: false,
				center: [104.708198, 31.395773], //地图中心点坐标，高德系经纬度
				markerData: [],
				oMarker: [],
				markersOld: [],
				markers:[{
					id: 1,
					longitude: "116.68",
					latitude: "39.9",
					iconPath: '/static/i-m7.png',
					title: '111',
					width: 14,
					height: 14,
				},{
					id: 2,
					longitude: "116.67",
					latitude: "39.9",
					iconPath: '/static/i-m7.png',
					title: '2222',
					width: 14,
					height: 14,
				},{
					id: 3,
					longitude: "116.66",
					latitude: "39.9",
					iconPath: '/static/i-m7.png',
					title: '22',
					width: 14,
					height: 14,
				}],
				mapBtns: [{
					type: 1,
					name: '水文',
					selected: true,
					img: '/static/i-m1.png',
					selectedImg: '/static/i-m2.png',
				},{
					type: 2,
					name: '水质',
					selected: false,
					img: '/static/i-m3.png',
					selectedImg: '/static/i-m4.png',
				},{
					type: 3,
					name: '监控',
					selected: false,
					img: '/static/i-m5.png',
					selectedImg: '/static/i-m6.png',
				},],
				markerTip: null,
				mapKey: '367a9a37e021c40cbd8c268284844868', //高德key
				amapPlugin: null,
				riversPoints: [], //河流点经纬度集合
				polylines: [], //河流
				polygons: [], //区域
				flag: false
			}
		},
		onLoad() {
			if ( (uni.getStorageSync('role') || {}).roles.includes('监控中心') ) {
				let _this = this;
				getGisoMarkerData(this);
				// 初始化加载数据
				getGisModelNav(this);
				getGisRegionsData(this);
				// this.getMarkerList();
				getGisRiversData(this)
				setTimeout(function(){
					_this.addTypeListInterval();
				},500)
				this.initMap();
			} else {
				this.showPage = false;
			}
		},
		onShow(){
			if ( (uni.getStorageSync('role') || {}).roles.includes('监控中心') ) {
				this.positionTimer = setInterval(() => {
					this.getGisTypeListCurr()
					uni.hideLoading()
				}, 5000)
			} else {
				if (this.flag) return
				this.flag = true;
				uni.showModal({
					content: '您暂无访问此功能权限，如需开通请联系管理员。感谢配合。',
					showCancel: false,
					confirmText: '回到首页',
					success: res => {
						if (res.confirm) {
							this.flag = false;
							wx.switchTab({ url: '/pages/home/home' })
						}
					}
				});
			}
		},
		onHide(){
			clearInterval(this.positionTimer) // 清除定时器
			this.positionTimer = null;
			clearInterval(this.detailTimer) // 清除定时器
			this.detailTimer = null;
		},
		methods: {
			addTypeListInterval() { //添加类型数据更新定时器
				clearInterval(this.positionTimer)
				this.positionTimer = null
				this.getGisTypeListCurr()
				this.positionTimer = setInterval(() => {
					this.getGisTypeListCurr()
					uni.hideLoading()
				}, 10000)
			},
			addDetailInterval(obj, id, index) { //添加详情数据更新定时器
				clearInterval(this.detailTimer)
				this.detailTimer = null
				getGisTypeDetailList(obj, id, index);
				this.detailTimer = setInterval(() => {
					getGisTypeDetailList(obj, id, index);
					uni.hideLoading()
				}, 5000)
			},

			getMarkerList() { //获取设备list
				getGisEquipment().then(res => {
					if (res.code !== 403) {
						let equipmentList = res ? res.result : []
						getGisEquipmentState().then(stateRes => {
							let stateList = stateRes ? stateRes.result : []
							this.markerList = []
							equipmentList.forEach(one => {
								if (one.modelTypeCode === 'LLB' || one.modelTypeCode === 'YLB' || one.modelTypeCode === 'SZJCD') {
									let obj = {
										longitude: one.longitude,
										latitude: one.latitude,
										modelTypeCode: one.modelTypeCode,
										width: one.width,
										height: one.height,
										title: one.equipLocation
									}
									let switchSatus = 'online'
									stateList.forEach(two => {
										if (obj.id === two.id) {
											switchSatus = two.opration.switchSatus
										}
									})
									let iconImg = ''
									// 详细状态,可用值:
									// online-[normal-正常]在线,
									// open-[normal-正常]开,
									// closed-[normal-正常]关,
									// offline-[warn-异常]离线,
									// alarm-[warn-异常]告警
									if (switchSatus === 'online') {
										iconImg = one.modelOnImg ? one.modelOnImg.imgUrl : (one.modelImg ? one.modelImg.imgUrl : '')
									}
									if (switchSatus === 'open') {
										iconImg = one.modelOnImg ? one.modelOnImg.imgUrl : ''
									}
									if (switchSatus === 'closed') {
										iconImg = one.modelOffImg ? one.modelOffImg.imgUrl : ''
									}
									if (switchSatus === 'offline' || switchSatus === 'alarm') {
										iconImg = one.modelWaringImg ? one.modelWaringImg.imgUrl : ''
									}
									obj.iconImg = iconImg
									if (obj.longitude && obj.latitude) {
										this.markerList.push(obj)
									}
								}
							})
							this.url += '?markers=' + encodeURIComponent(JSON.stringify(this.markerList))
							// #ifdef APP-PLUS
						
						
							// #endif
						})
					} else {
						// TODO  这里要给h5传参让其跳转到首页
						this.url += '?token=no'
						// #ifdef APP-PLUS
						
						// #endif
					}
				})
			},

			changeType(index) { //展开详情
				this.isChange = false;
				let i = 0;
				if (!index.length) return
				let item = this.typeList[index[0]];
				this.typeList.forEach((one, index) => {
					if (one.gisId === item.gisId) {
						i = index
						this.listIndex = index;
						this.gisId = item.gisId;
					}
				})
				// item.open = item.open ? false : true
				// if (item.open) {
					// console.log(i)
					this.getGisDetail(this, item.gisId, i)
				// }
			},

			getGisDetail(obj, id, index) { //获取gis详情
				this.isChange = false;

				this.addTypeListInterval(); //重置各定时器，统一更新
				this.addDetailInterval(obj, id, index);
			},

			goTypeList(index) { //切换监控类型
				this.isChange = true
				this.currentIndex = index
				clearInterval(this.detailTimer) //切换类型时清除详情定时器
				this.detailTimer = null
				this.typeList = null;
				this.getGisTypeListCurr()
			},

			getGisTypeListCurr() { //当前gis类型
				let selectedList = [];
				getGisTypeList(this, this.navList[this.currentIndex].modelType, selectedList, this.gisId, this.isChange)
				this.isChange = false;
			},
			initMap(){ //初始化地图
				var amapPlugin = new amap.AMapWX({
					key: this.mapKey  //该key 是在高德中申请的微信小程序key
				});
			}, 
			tapInfo(e,dataShow,modelType) {
				let m = null;
				let _this = this;
				let newArr = [];
				if(dataShow==1){
					_this.navList.map((index, i) => {
						 if (index.modelType == modelType) {
							 _this.nameArr.splice(_this.nameArr.findIndex(item => item=== modelType), 1);
							 _this.navList[i].dataShow = "0";
						 }
					 });
					 this.markersOld.filter((item) => {
						 _this.nameArr.map(index => {
							 if (item.modelTypeCode == index) {
								 newArr.push({
									 id: item.id,
									 latitude: item.latitude,
									 longitude: item.longitude,
									 iconPath: item.iconPath,
									 title: item.title,
									 modelTypeCode: item.modelTypeCode,
									 modelOffset: item.modelOffset,
									 modelPosition: item.modelPosition,
									 modelType: _this.zoonLevel,
									 list: item.list,
									 fmType: item.fmType,
								 })
							 }
						})
					 });
					 this.markers = newArr
				 }else{
					 let x = null;
					 if ( modelType == 'Z06'){
						x = 0
					 }else if(modelType == 'Z03'){
						x = 1
					 }else{
						x = 2
					 }
					_this.nameArr.splice(x, 0, modelType);
					_this.nameArr.map((index, i) => {
						 if (index == modelType) {
							 _this.navList[e].dataShow = "1";
						 }
					 });
					 this.markersOld.filter((item) => {
						 _this.nameArr.map(index => {
							 if (item.modelTypeCode == index) {
								 newArr.push({
									 id: item.id,
									 latitude: item.latitude,
									 longitude: item.longitude,
									 iconPath: item.iconPath,
									 title: item.title,
									 // modelZcImg: item.modelZcImg,
									 modelTypeCode: item.modelTypeCode,
									 modelOffset: item.modelOffset,
									 modelPosition: item.modelPosition,
									 modelType: _this.zoonLevel,
									 list: item.list,
									 fmType: item.fmType,
									 equipmentSn: item.equipmentSn,
								 })
							 }
						 })
					 });
					 this.markers = newArr
				 }
			},
		}
	};
</script>
<style lang="less">
.mapView {
	width: 100%;
	height: 460rpx;
	.monitor-map {
		position: relative;
		width: 680rpx;
		height: 460rpx;
		margin-left: 70rpx;
	}
	.map-btn {
		position: absolute;
		width: 70rpx;
		height: 460rpx;
		left: 0;
		top: 0;
		background: #fff;
		font-size: 24rpx;
		border: 1px solid #ded6d6;
		border-radius: 10rpx;
		.btn {
			background-color: #FFF;
			text-align: center;
			.cover-image {
				display: inline-block;
				width: 40rpx;
				height: 40rpx;
				margin-top: 40rpx;
				&.selected {

				}
			}
			.title{
				font-size: 20rpx;
				line-height: 24rpx;
				padding-bottom: 36rpx;
			}
			&.selected {
				.title{
					color: #007aff;
				}
			}
			&:nth-child(1),
			&:nth-child(2){
				border-bottom: 1px solid #ECF0F7;
			}
			
		}
	}
}


.type-box {
	position: fixed;
	left: 0;
	right: 0;
	width: 100%;
	height: 100%;
	padding: 15px 15px 0;
	.list {
		height: calc(100% - 15px);
		overflow-y: auto;
		border-radius: 5px;
	}
	.item {
		&:last-child {
			display: block;
			margin-bottom: 30px;
			border-radius: 0 0 5px 5px;
			overflow: hidden;
		}
	}
	.infoList {
		padding: 25px 15px;
		background: #fff;
	}
	.infoItem {
		display: flex;
		justify-content: space-between;
		color: #999999;
		&:not(:last-child) {
			margin-bottom: 23px;
		}
	}
	.uni-collapse-cell--open {
		background-color: #E0E0E0;
	}
} 

.map-button{ 
/* 	position: absolute; 
	left: 0;  
	right: 0; 
	bottom: 0; */
	z-index: 2;
	background: rgb(55, 66, 77);
	background: #fff;
	padding: 0 15px;
	display: flex;
	justify-content: space-between;
	border-bottom: 1px solid rgba(0,0,0, .2);
	
}
.map-button span {
	display: block;
	width: 33.33%;
	text-decoration: none;
	border-right: 1px solid rgba(0,0,0, .2);
	/* border-right: 1px solid rgba(255,255,255, .2); */
	text-align: center;
	color: rgb(55, 66, 77);
	padding: 10px;
	/* color: #FFFFFF; */
}
.map-button span.curr {
	color: rgb(0, 122, 255)
}
.map-button span:last-child {
	border: none;
}
.map-type-item{
/* 	position: absolute;
	left: 0;
	right: 0;
	bottom: 35px; */
	/* background: #fff; */
	display: none;
}
.map-type-item h3 {
	font-size: 16px;
	font-weight:normal;
	padding: 10px 0;
	margin: 0;
}
.map-type-item ul {
	padding: 0;
	margin: 0;
	list-style: none;
	color: #646566;
}
#list1{
	display: block;
}
.map-type-item > ul{
	/* margin-top: 5px; */
}
.map-type-item > ul > li {
	font-size: 13px;
	position: relative;
	margin-bottom: 5px;
	border-bottom: 1px solid #ccc;
	
}
.map-type-item > ul > li > span{
	display: block;
	font-size: 15px;
	background: #ffffff;
	padding: 8px 10px;
}
.map-type-item > ul > li.arrow-bottom:after,
.map-type-item > ul > li.arrow-top:after{
	position: absolute;
	right: 10px;
	top: 10px;
	content: '';
	display: inline-block;
	width: 8px;
	height: 8px;
}
.map-type-item > ul > li.arrow-bottom:after {
	
	border-bottom: 1px solid #ccc;
	border-left: 1px solid #ccc;
	transform: rotate(-45deg);
	-webkit-transform: rotate(-45deg);
}
.map-type-item > ul > li.arrow-top:after {
	top: 15px;
	border-top: 1px solid #ccc;
	border-right: 1px solid #ccc;
	transform: rotate(-45deg);
	-webkit-transform: rotate(-45deg);
}
.map-type-item > ul ul {
	display: none;
	padding: 10px 10px;
	font-size: 13px;
	line-height: 25px;
	background: #fff;
	border-top: 1px solid #ccc;
}
html, body {
	margin: 0;
	height: 100%;
	width: 100%;
	position: absolute;
}

#container {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	width: 100%;
	height: 100%;
}

.button-group {
	position: absolute;
	bottom: 20px;
	right: 20px;
	font-size: 12px;
	padding: 10px;
}

.button-group .button {
	height: 28px;
	line-height: 28px;
	background-color: #0D9BF2;
	color: #FFF;
	border: 0;
	outline: none;
	padding-left: 5px;
	padding-right: 5px;
	border-radius: 3px;
	margin-bottom: 4px;
	cursor: pointer;
}
.button-group .inputtext {
	height: 26px;
	line-height: 26px;
	border: 1px;
	outline: none;
	padding-left: 5px;
	padding-right: 5px;
	border-radius: 3px;
	margin-bottom: 4px;
	cursor: pointer;
}
 /*
.tip {
	position: absolute;
	bottom: 30px;
	right: 10px;
	background-color: #FFF;
	text-align: center;
	border: 1px solid #ccc;
	line-height: 30px;
	border-radius: 3px;
	padding: 0 5px;
	font-size: 12px;
}
*/
#tip {
	background-color: #fff;
	padding-left: 10px;
	padding-right: 10px;
	position: absolute;
	font-size: 12px;
	right: 10px;
	top: 20px;
	border-radius: 3px;
	border: 1px solid #ccc;
	line-height: 30px;
}

/*
#tip input[type='button'] {
	margin-top: 10px;
	margin-bottom: 10px;
	background-color: #0D9BF2;
	height: 30px;
	text-align: center;
	line-height: 30px;
	color: #fff;
	font-size: 12px;
	border-radius: 3px;
	outline: none;
	border: 0;
}
*/
.amap-info-content {
	font-size: 12px;
}

#myPageTop {
	position: absolute;
	top: 5px;
	right: 10px;
	background: #fff none repeat scroll 0 0;
	border: 1px solid #ccc;
	margin: 10px auto;
	padding:6px;
	font-family: "Microsoft Yahei", "微软雅黑", "Pinghei";
	font-size: 14px;
}
#myPageTop label {
	margin: 0 20px 0 0;
	color: #666666;
	font-weight: normal;
}
#myPageTop input {
	width: 170px;
}
#myPageTop .column2{
	padding-left: 25px;
}

.nullData {
	width: 100%;
	text-align: center;
	color: #999999;
	background: #F2F2F2;
	font-size: 28rpx;
	padding-top: 100rpx;
	image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 20rpx;
	}
}
</style>
