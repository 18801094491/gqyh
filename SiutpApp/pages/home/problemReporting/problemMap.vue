<template>
	<view class='problemMap-page'>
		<map class="problemMap" :longitude="longitude" :latitude="latitude" :markers="markers" @tap="onMapChange" ></map>
		<view class="goback" @tap="goBack" v-if="type !== 'detail' && lnglat">完成</view>
	</view>
</template>

<script>
// var QQMapWX = require('./qqmap-wx-jssdk.js');
var wxMarkerData = []; 
	export default{
		data(){
			return{
				latitude: '', 
				longitude: '', 
				type: '',
				markers: [],
				lnglat: null,
			}
		},
		methods:{
			onLoad(option) { //初始加载
				console.log('problemMap-option ： ', option)
				this.latitude = option.lat;
				this.longitude = option.lng;
				this.type = option.type;
				if (this.type === 'detail') {
					// this.reverseLocation({
					// 	latitude: option.lat,
					// 	longitude: option.lng
					// }, 5) //获取腾讯地图经纬度
					this.markers = [{
						id: 1,
						iconPath: '/static/location.png',
						longitude: this.longitude,
						latitude: this.latitude,
					}];
				} else if (this.type === 'edit') {
					this.markers = [{
						id: 1,
						iconPath: '/static/location.png',
						longitude: this.longitude,
						latitude: this.latitude,
					}];
				}
			},

			onMapChange(e) {
				console.log(e)
				if (this.type === 'detail') return;
				this.lnglat = e.detail;
				console.log('点击获取经纬度：', this.lnglat)
				// this.reverseLocation(this.lnglat, 3) //获取百度地图经纬度
				this.markers = [{
					id: 1,
					iconPath: '/static/location.png',
					longitude: e.detail.longitude,
					latitude: e.detail.latitude,
				}]
			},

			reverseLocation(lnglat, coord_type) {
				var demo = new QQMapWX({
					key: 'UQWBZ-FNJ33-3HH3P-3VHYD-GQHXQ-35FBA'
				});
				demo.reverseGeocoder({
					location: {
						latitude: lnglat.latitude,
						longitude: lnglat.longitude
					},
					coord_type: coord_type, //经纬度类型
					success: res => {
						var latitude = res.result.location.lat;
						var longitude = res.result.location.lng;
						var markers = [{
							iconPath: '/static/location.png',
							longitude: longitude,
							latitude: latitude,
							id: "map",
						}]
						if (coord_type == 5) {
							this.markers = markers;
							this.latitude = latitude;
							this.longitude = longitude;
						} else if (coord_type == 3) {
							uni.setStorageSync('pro-push_lnglat', lnglat)
						}
						this.lnglat = {
							longitude: longitude,
							latitude: latitude,
						}
						console.log('转换百度地图经纬度：', this.lnglat)
					},
					fail: error => {    
						console.error('fail: ',error);   
					},
					complete: res => {    
						console.log(res); 
					}
				});
			},

			goBack() {
				uni.setStorageSync('pro-lnglat', this.lnglat)
				setTimeout(() => { uni.navigateBack() })
			},
		},
		
	}
</script>


<style lang="less">
.problemMap-page,
.problemMap {
	width: 100%;
	height: 100%;
}
.goback {
	position: fixed;
    top: 30rpx;
    right: 30rpx;
    z-index: 99;
    background: #fff;
    width: 88rpx;
    font-size: 24rpx;
    text-align: center;
    line-height: 44rpx;
    border: 1px solid #ded6d6;
    border-radius: 10rpx;
}
</style>
