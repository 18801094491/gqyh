<template>
	<view class="page-safe">
		<image src="/static/null.png" mode="" />
		<view>很抱歉，暂无数据</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="less">
.page-safe {
	position: absolute;
	top: 40%;
	left: 50%;
	transform: translate(-50%, -60%);
	image {
		width: 200rpx;
		height: 200rpx;
		margin: 0 auto;
		display: block;
	}
	view {
		line-height: 1;
		color: #999999;
		font-size: 28rpx;
		margin-top: 40rpx;
	}
}
</style>
