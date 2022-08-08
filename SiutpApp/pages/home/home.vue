<template>
	<view class='homePage'>
		<view class='page-top uni-fs24'>
			<view class='t-tit'>告警信息</view>
			<view class="deslist" >
				<view class="des uni-fs24" :class="{'animate-up': animateUp}" :key="index"
					v-for="(item,index) in msgData" @tap="goUrl('warningInfo/warningDetaile?id='+item.id, '告警信息')">
					{{item.warnContent}}
				</view>
			</view>
			<view class='more' @tap="goUrl('warningInfo/index', '告警信息')">查看更多>></view>
		</view>
		<view class='page-content'>
			<view class='con-item'>
				<view class='con-tit uni-fs30'>监控管理</view>
				<view class='list uni-flex'>
					<view class='item' @tap="goUrlaMonitor('monitoringCenter/index', '监控中心')">
						<image src='/static/i-h-chart.png' mode='scaleToFill' />
						监控中心
					</view>
					<view class='item' @tap="goUrl('equipmentAlarm/index', '设备报警')">
						<image src='/static/i-h-gj.png' mode='scaleToFill' />
						设备报警
					</view>
					<view class='item' @tap="goUrl('workingMonitor/index', '工况导览')">
						<image src='/static/i-h-dk.png' mode='scaleToFill' />
						工况导览
					</view>
				</view>
			</view>
			<view class='con-item con-item1'>
				<view class='con-tit uni-fs30'>实用工具</view>
				<view class='con-item-box box1 uni-fs24'  @tap="goUrl('phone/index', '通讯录')">
					<uni-icons :size="14" class="uni-icon-wrapper" color="#fff" type="arrowright" />
					<text class="uni-fs30">通讯录</text> / mail list
				</view>
				<view class='con-item-box box2 uni-fs24'  @tap="goUrl('problemReporting/index', '问题填报')">
					<uni-icons :size="14" class="uni-icon-wrapper" color="#fff" type="arrowright" />
					<text class="uni-fs30">问题填报</text> / problem reporting
				</view>
				<view class='con-item-box box3 uni-fs24'  @tap="goUrl('myWorkOrders/index', '我的工单')">
					<uni-icons :size="14" class="uni-icon-wrapper" color="#fff" type="arrowright" />
					<text class="uni-fs30">我的工单</text> / my works
					<text class="num">{{mylistnum && mylistnum}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniIcons from '../../components/uni-icons/uni-icons.vue'
	import { postAction, getAlarmScroll, getMylistnum, getRole } from '../../api/api_alarm.js'
	export default {
		components: {
			uniIcons
		},
		data() {
			return {
				role: {
					roles: '',
				},
				msgData : [],
				mylistnum: '',
				animateUp: false,
				timer: null,
				isLocation: false,
			}
		},
		onShow() {		
			getAlarmScroll(this);
			getMylistnum(this);
			// #ifdef MP-WEIXIN
				this.getLocation();
			// #endif
		},
		methods: {
			onLoad() {
				getRole(this, (role) => {
					this.role = role;
					uni.setStorageSync('role', role);
				});
				// #ifdef MP-WEIXIN
					this.setLocationInterval();
				// #endif

				// #ifdef APP-PLUS
					this.appGetLocation();
				// #endif
			},
			getLocation() { //微信获取定位授权
				if (this.isLocation) return;
				uni.getLocation({
					type: 'gcj02',
					isHighAccuracy: true,
					success: res => {
						this.isLocation = true;
						this.latitude = res.latitude
						this.longitude = res.longitude
					}, 
					fail: res => {
						console.log('未开启定位授权-fail', res);
						this.isLocation = false;
						uni.showModal({
							title: '获取不到您的定位',
							content: '在设置页中开启定位授权，以获取当前所在位置',
							confirmText: '开启定位',
							confirmColor: '#3D71AD',
							success: res => {
								console.log(res)
								if (res.confirm) {
									uni.openSetting({success: res => {
										console.log(res)
									}})
								}
							}
						})
					}
				})
				
				
				// wx.startLocationUpdate({
				// 	success: res => {
				// 		console.log('startLocationUpdate-success: ', res)
				// 	},
				// 	fail: res => {
				// 		console.log('未开启定位授权-fail', res)
				// 		if (res.errMsg.includes('开发者工具暂时不支持')) return
				// 		uni.showModal({
				// 			title: '获取不到您的定位',
				// 			content: '在设置页中开启定位授权，以获取当前所在位置',
				// 			confirmText: '开启定位',
				// 			confirmColor: '#3D71AD',
				// 			success: res => {
				// 				console.log(res)
				// 				if (res.confirm) {
				// 					uni.openSetting({success: res => {
				// 						console.log(res)
				// 					}})
				// 				}
				// 			}
				// 		})
				// 	}
				// })
			},
			appGetLocation() { //APP获取定位授权
					plus.android.requestPermissions(['android.permission.ACCESS_FINE_LOCATION'], res => {
						if(res.deniedAlways.length>0){
							console.log('权限被永久拒绝 ：');
							// e.deniedAlways.toString()
							plus.nativeUI.alert( "获取定位位置信息失败, 请到应用设置中开启定位");
						}
						if(res.deniedPresent.length>0){
							console.log('权限被临时拒绝 : '+res.deniedPresent.toString());
							// plus.nativeUI.alert( "获取定位位置信息失败, 请手动在应用设置中开启定位");
						}
						if(res.granted.length>0){
							//调用依赖获取定位权限的代码
							console.log('权限被允许 ：'+res.granted.toString());
							// plus.geolocation.watchPosition(res =>{ //实时获取定位上报
							// 	console.log(res)
							// 	postAction('/worklist/myWorkList/app/subLocation', {
							// 		"dateTime": new Date().getTime(),
							// 		"latitude": res.coords.latitude,
							// 		"longitude": res.coords.longitude
							// 	})
							// }, err => {
							// 	console.log('Geolocation error: ' + err.message);
							// } );
						}
					}, err =>{
						console.log('APP授权功能调用失败 :'+JSON.stringify(err));
					});
			},
			
			setLocationInterval() { //实时上报位置
				// let onLocationChange = () => {
				// 	let timer = setInterval(() => {
				// 		uni.onLocationChange(res => {
				// 			console.log('onLocationChange-success: ', res);
				// 			if (uni.getStorageSync('Token')) {
				// 				postAction('/worklist/myWorkList/app/subLocation', {
				// 					"dateTime": new Date().getTime(),
				// 					"latitude": res.latitude,
				// 					"longitude": res.longitude
				// 				});
				// 			} else {

				// 			}
				// 			uni.offLocationChange();
				// 			clearInterval(timer);
				// 			onLocationChange()
				// 		});
				// 	}, 3000)
				// };
				// onLocationChange()
				
				let timer = setInterval(() => {
					if (!uni.getStorageSync('Token')) {
						clearInterval(timer);
						timer = null;
					}
					if (this.isLocation) {
						uni.getLocation({
							type: 'gcj02',
							isHighAccuracy: true,
							success: res => {
								this.isLocation = true;
								this.latitude = res.latitude
								this.longitude = res.longitude
								if (uni.getStorageSync('Token')) {
									postAction('/worklist/myWorkList/app/subLocation', {
										"dateTime": new Date().getTime(),
										"latitude": res.latitude,
										"longitude": res.longitude
									})
								}
							}, 
							fail: res => {
								console.log('未开启定位授权-fail', res);
								this.isLocation = false;
							}
						})
					}
				}, 10000);
			},

			scrollAnimate() {
			  this.animateUp = true
			  let timer1 = setTimeout(() => {
			      this.msgData.push(this.msgData[0])
			      this.msgData.shift()
			      this.animateUp = false;
			  }, 2000)
			  if(this.msgData.length<=5){
			  	clearTimeout(timer1);
			  	this.animateUp = false;
			  	return ;
			  }
			},
				
			goUrl(url, role) {
				if (this.role.roles && this.role.roles.includes(role)) {
					uni.navigateTo({ url: url })
				} else {
					uni.showToast({
						title: '暂无权限',
						icon: 'none',
						duration: 1000,
						mask: true,
					});
				}
			},
			
			goUrlaMonitor(url, role) {
				if (this.role.roles && this.role.roles.includes(role)) {
					uni.switchTab({ url: url })
				} else {
					uni.showToast({
						title: '暂无权限',
						icon: 'none',
						duration: 1000,
						mask: true,
					});
				}
			},
		}
	}
</script>

<style lang='less'>
.homePage {
	width: 100%;
	overflow: hidden;
	.page-top {
		position: relative;
		width: calc(100% - 60rpx);
		height: 330rpx;
		line-height: 1;
		color: #F0F300;
		font-size: 24rpx;
		background-size: 100% 100%;
		background-repeat: no-repeat;
		background-image: url(/static/h-bg1.png);
		margin: 30rpx 30rpx 0;
		.t-tit {
			position: relative;
			text-align: center;
			font-size: 28rpx;
			padding-top: 40rpx;
			&::after, &::before {
				content: '';
				position: absolute;
				top: 4rpx;
				width: 180rpx;
				height: 20rpx;
				margin-top: 44rpx;
				background-size: 100% 100%;
				background-repeat: no-repeat;
			}
			&::after {
				left: 68rpx;
				background-image: url(/static/line-l.png);
			}
			&::before {
				right: 68rpx;
				background-image: url(/static/line-r.png);
			}
		}
		.deslist {
			height: 150rpx;
			overflow: hidden;
			list-style: none;
			padding: 0  10rpx 0 30rpx;
			margin-top: 20rpx;
		}
		.des {
			width: 100%;
			line-height: 50rpx;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			&.animate-up {
				transition: all 0.5s ease-in-out;
				transform: translateY(-50rpx);
			}
		}
		.more {
			position: absolute;
			left: 30rpx;
			bottom: 30rpx;
			padding: 10rpx 0;
		}
	}
	.page-content {
		line-height: 1;
		.con-item {
			padding: 60rpx 30rpx 0;
			.con-tit {
				position: relative;
				font-size: 30rpx;
				font-weight: bold;
				color: #232323;
				padding: 0 0 30rpx 16rpx;
				&::after {
					content: '';
					position: absolute;
					left: 0;
					top: 1rpx;
					width: 6rpx;
					height: 28rpx;
					background-color: #3D71AD;
				}
			}
			.list {
				justify-content: space-between;
			}
			.item {
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				width: 210rpx;
				height: 220rpx;
				color: #FFF;
				font-size: 28rpx;
				font-weight: bold;
				border-radius: 10rpx;
				&:nth-child(1) {
					background: linear-gradient(0deg, #4EACD9, #68C5F0);
				}
				&:nth-child(2) {
					background: linear-gradient(0deg, #CBB138, #F0DE68);
				}
				&:nth-child(3) {
					background: linear-gradient(0deg, #2EAB8F, #59D5E4);
				}
				image {
					width: 70rpx;
					height: 70rpx;
					margin-bottom: 28rpx;
				}
			}
			.con-item-box {
				position: relative;
				width: 100%;
				height: 120rpx;
				line-height: 1;
				color: #FFFFFF;
				background-size: 100% 100%;
				background-repeat: no-repeat;
				padding: 46rpx 30rpx;
				margin: 10rpx 0 20rpx 0;
				&.box1 {
					background-image: url(/static/h-bg2.png);
				}
				&.box2 {
					background-image: url(/static/h-bg3.png);
				}
				&.box3 {
					background-image: url(/static/h-bg4.png);
				}
				.uni-icon-wrapper {
					position: absolute;
					top: 50%;
					right: 22rpx;
					transform: translateY(-50%);
				}
				text {
					margin-right: 8rpx;
				}
				.num {
					position: absolute;
					right: 56rpx;
					font-weight: bold;
					transform: translateY(-55%);
					top: 50%;
				}
			}
		}
	}
}

</style>