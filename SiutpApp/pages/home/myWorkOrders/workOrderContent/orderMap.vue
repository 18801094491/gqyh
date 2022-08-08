<template>
	<view class='orderMap-page'>
		<template v-if="type !== 'line'">
			<map class="orderMap" :longitude="lng" :latitude="lat" :markers="markers">
				<cover-view class="goback" @tap="goBack" v-if="type === 'set' && markers.length">完成</cover-view>
			</map>
		</template>
		<template v-else-if="type === 'line' && polyline">
			<map class="orderMap" scale="12" :longitude="lng" :latitude="lat" :markers="markers" enable-3D="false" enable-rotate="false" enable-overlooking='false' :polyline="polyline" ></map>
		</template>
		<!-- <view class="goback" @tap="goBack" v-if="type === 'set' && markers.length">完成</view> -->
	</view>
</template>

<script>
	import { getAction } from '../../../../api/api_works.js'
	export default{
		data(){
			return{
				lat: '',
				lng: '',
				type: '',
				markers: [],
				polyline: null,
			}
		},
		methods:{
			onLoad(option) { //设置初始位置
				console.log('workorder-option ： ', option)
				this.lat = option.lat;
				this.lng = option.lng;
				this.type = option.type;
				if (option.type !== 'line') {
					this.markers = [{
						iconPath: '/static/location.png',
						longitude: option.lng,
						latitude: option.lat,
						title: option.type == 'see-matter' ? '所在位置' : '当前位置',
					}];
				} else if (option.type === 'line') {
					getAction('/worklist/myWorkList/app/query', {
						id: option.id
					}).then(res => {
						if (res.code == 200) {
							let markers = [];
							let polyline = [{
								points: [],
								color: "#0091ff",
								width: 3
							}];
							let matterList = res.data.matterList;
							this.lat = matterList[0].matterLatitude;
							this.lng = matterList[0].matterLongitude;
							matterList.forEach((item, index) => {
								markers.push({
									id: index+1,
									iconPath: '/static/location.png',
									longitude: item.matterLongitude,
									latitude: item.matterLatitude,
									label:{
										content: index+1,
										color: '#FFFFFF',
										anchorX: index+1>=100?-12 : index+1>=10?-7 : -3,
										anchorY: -35
									}
								})
								polyline[0].points.push({latitude: item.matterLatitude, longitude: item.matterLongitude})
							})
							this.markers = markers
							this.polyline = polyline
							console.log(this.markers)
							console.log(this.polyline)
						}
					})
				}
			},
			onMapChange(e) {
				// if (this.type === 'set') return
				// uni.setStorageSync('pro-lnglat', e.detail)
				// this.markers =[{
				// 	id: 1,
				// 	iconPath: '/static/location.png',
				// 	longitude: e.detail.longitude,
				// 	latitude: e.detail.latitude,
				// }]
			},
			goBack() {
				uni.navigateBack();
			},
		},
		
	}
</script>


<style lang="less">
.orderMap-page,
.orderMap {
	position: relative;
	width: 100%;
	height: 100%;
}
.goback {
	position: absolute;
    top: 30rpx;
    right: 30rpx;
    background: #fff;
    width: 112rpx;
    font-size: 24rpx;
	text-align: center;
	height: 60rpx;
	display: flex;
	align-items: center;
	justify-content: center;
    line-height: 56rpx;
    border: 1px solid #ded6d6;
	border-radius: 10rpx;
}
</style>
