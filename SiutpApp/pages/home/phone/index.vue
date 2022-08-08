<template>
	<view>
		<view class="mail-list">
			<view class="mail-item" v-for="(item, index) in mailList" :key="index" hover-class="uni-list-hover" @tap="clickMailItem(item)">
				<view class="con uni-fs24">
					{{item.departName}}（{{item.usercount}}）
					<uni-icons :size="14" class="uni-icon-wrapper" color="#ABAEB2" type="arrowright" />
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniIcons from '../../../components/uni-icons/uni-icons.vue'
	import { getPhoneList } from '../../../api/api_phone.js'
	var _this;
	export default{
		data(){
			return {
				mailList:[], // 组织列表
				mailListUser:[],
			}
		},
		components: {
			uniIcons
		},
		methods:{
			clickMailItem(obj){
				uni.navigateTo({
					url: `phoneDetail?item=${encodeURIComponent(JSON.stringify(obj))}`
				})
			}
		},
		onLoad(){
			_this = this;
			let data = { orgCode:'A01'}
			getPhoneList(data,_this,'mail')
		}
	}
</script>

<style lang="less">
.page-title {
	width: 100%;
	height: 132rpx;
	text-align: center;
	line-height: 132rpx;
	color: #FFFFFF;
	font-size: 28rpx;
	font-weight: bold;
	background: linear-gradient(180deg, #3D71AD, #20B8EE);
}


.mail-list {
	display: block;
	font-size: 24rpx;
	background-color: #FFFFFF;
	border-radius: 10rpx;
	margin: 30rpx 30rpx 88rpx;
	overflow: hidden;
	.mail-item {
		padding: 0 30rpx;
		line-height: 1;
		&.uni-list-hover {
			background-color: #F1F1F1;
		}
		&:not(:last-child) .con {
			border-bottom: 1px solid rgba(224, 224, 224, 0.5);
		}
		.con {
			padding: 38rpx 0;
			position: relative;
			color: #232323;
			.uni-icon-wrapper {
				position: absolute;
				top: 50%;
				right: -6rpx;
				transform: translateY(-50%);
			}
		}
	}
}
</style>
