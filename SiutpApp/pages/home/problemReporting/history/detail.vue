<template>
	<view class='problemReporting-page'>
		<view class="page-top">
			<view class="item">
				<view class="title">问题标题：</view>
				<view class="con">{{problemDetail.title}}</view>
			</view>
			<view class="item">
				<view class="title">问题类型：</view>
				<view class="con">{{problemDetail.matterTypeDes}}</view>
			</view>
			<view class="item">
				<view class="title">关联设备：</view>
				<view class="con">{{problemDetail.equipmentName}}</view>
			</view>
			<view class="item">
				<view class="title">问题等级：</view>
				<view class="con">{{problemDetail.matterLevelDes}}</view>
			</view>
			<view class="item">
				<view class="title">问题地点：</view>
				<view class="locStyle" @tap="onSetProLocationChange">
					<image src="/static/loc-icon.png" mode="" />
					<view>查看位置</view>
				</view>
			</view>
		</view>
		<view class="page-bottom">
			<view class="title">问题描述</view>
			<view>{{problemDetail.description}}</view>
			<view class="upload">
				<view class="pick" v-for="(item, index) in problemDetail.fileList" :key="index" @tap="onPreviewMediaChange(index)">
					<template v-if="videoType.includes(item.url.slice(-3))">
						<video :src="item.url" :controls="false" :show-center-play-btn="false"></video>
						<image class="icon-play" src="/static/icon-play.png" mode=""/>
					</template>
					<image v-else class="pro-img" :src="item.url" mode="aspectFill" />
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { getProblemDetail } from '../../../../api/api_problem.js'
	export default{
		data(){
			return{
				problemDetail: [],
				videoType: 'mp4,mov,m4v,3gp,avi,m3u8,webm'
			}
		},
		methods:{
			onLoad(option) { //初始加载
				getProblemDetail({id: option.id}, this)
			},

			onSetProLocationChange() { //问题定位
				this.goUrl(`../problemMap?lat=${this.problemDetail.matterLatitude}&lng=${this.problemDetail.matterLongitude}&type=detail`);
			},

			onPreviewMediaChange(index) { //显示大图
				let mediaFiles = [];
				this.problemDetail.fileList.forEach(item => {
					if (this.videoType.includes(item.url.slice(-3))) {
						mediaFiles.push({url: item.url, type: 'video'})
					} else {
						mediaFiles.push({url: item.url, type: 'image'})
					}
				})
				console.log(mediaFiles)
				uni.previewMedia({
					sources: mediaFiles,
					current: index
				});
			},

			goUrl(url) {
				uni.navigateTo({
					url: url
				});
			},
			
		},
		
		// 下拉刷新
		onPullDownRefresh (){
			setTimeout(()=>{
				uni.stopPullDownRefresh()
			},1000)
		},
		// onReady() {
		// 	uni.getSystemInfo({
		// 		success(res) {

		// 		}
		// 	})
		// },
	}
</script>


<style lang="less">
page {
	background-color: #ffffff;
}
.problemReporting-page {
	.page-top {
		padding: 30rpx 30rpx 14rpx;
		border-bottom: 14rpx solid #F2F2F2;
		font-size: 24rpx;
		.item {
			display: flex;
			align-items: baseline;
			min-height: 84rpx;
			.title {
				line-height: 84rpx;
				flex-shrink: 0;
				color: #999999;
			}
			.con {
				display: flex;
				align-items: center;
				height: 100%;
				width: 100%;
			}
			.locStyle {
				display: flex;
				align-items: center;
				height: 70rpx;
				color: #3D71AD;
				padding-right: 50rpx;
				image {
					width: 24rpx;
					height: 30rpx;
					margin-right: 10rpx;
				}
				view {
					text-decoration: underline;
				}
			}
		}
	}
	.page-bottom {
		padding: 0 30rpx;
		.title {
			line-height: 1;
			font-size: 30rpx;
			color: #232323;
			font-weight: bold;
			padding-left: 10rpx;
			margin: 40rpx 0 30rpx;
			border-left: 6rpx solid #3D71AD;
		}
		.upload {
			margin: 30rpx 0;
			.pick {
				display: inline-block;
				position: relative;
				&:not(:nth-child(4n)) {
					margin-right: 22rpx;
				}
			}
			image, video {
				width: 156rpx;
				height: 156rpx;
			}
			.icon-play {
				position: absolute;
				top: 50%;
				left: 50%;
				transform: translate(-50%, -60%);
				width: 60rpx;
				height: 60rpx;	
			}
		}
		// .upload {
		// 	position: relative;
		// 	display: flex;
		// 	flex-wrap: wrap;
		// 	margin-top: 20rpx;
		// 	image, video {
		// 		width: 156rpx;
		// 		height: 156rpx;
		// 		margin-bottom: 20rpx;
		// 		&:not(:nth-child(4n)) {
		// 			margin-right: 22rpx;
		// 		}
		// 	}
		// 	.icon-play {
		// 		position: absolute;
		// 		top: 50%;
		// 		left: 50%;
		// 		transform: translate(-50%, -60%);
		// 		width: 60rpx;
		// 		height: 60rpx;	
		// 	}
		// }

	}
}
.setNullHeight {
	height: 60rpx;
}
</style>
