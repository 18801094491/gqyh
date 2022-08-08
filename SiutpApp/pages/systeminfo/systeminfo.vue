<template>
	<view class="systemInfo">
		<view class="info">
			<image class="logo" src="/static/i-sys-logo.png" mode="aspectFill"></image>
			<view class="fs24 text-a_c color32">版本号 1.1.0</view>
		</view>
		<view class="fixed">
			<button class="logOut" type="default" @click="logOut()">退出系统</button>
			<view class="fs24 text-a_c color9">技术服务提供商：中成大运数字技术中心</view>
			<view class="fs24 text-a_c color9">北京市通州区运河奥体公园</view>
		</view>
	</view>
</template>

<script>
	import { logOut } from '../../api/api_login.js'
	export default {
		data() {
			return {
			markers:[
				{
					longitude: "116.685721",
					latitude: "39.910670",
					iconPath: '../../static/location.png',
					title: '我们公司的位置',
					label:{
						content:'中成大运智慧城市科技有限公司',
					}
				}
			]
			}
		},
		methods: {
			logOut(){
				logOut();
				console.log('logout');
			}
		}
	}
</script>

<style lang="less">
page{ background: #ffffff !important; }
.systemInfo {
	.info {
		text-align: center;
		padding: 58rpx 0 380rpx;
		.logo {
			width: 578rpx;
			height: 298rpx;
			margin: 0 auto;
			box-shadow: 0 0 40rpx #F9FBFC;
		}
	}
	.fixed {
		position: fixed;
		bottom: 80rpx;
		left: 0;
		width: 100%;
	}
	.logOut {
		width: 400rpx;
		height: 88rpx;
		line-height: 90rpx;
		text-align: center;
		color: #FFFFFF !important;
		font-size: 34rpx;
		border-radius: 88rpx;
		background-color: #3D71AD !important;
		margin: 0 auto 50rpx;
	}
	.fs24 {
		font-size: 24rpx;
	}
	.text-a_c {
		text-align: center;
	}
	.color32 {
		color: #323232;
	}
	.color9 {
		color: #999999;
	}
}

</style>
