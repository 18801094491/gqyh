<template>
	<view class='historyProList' v-if="showPage">
		<template v-if="historyProList.length">
			<view class="item" v-for="(item, index) in historyProList" :key="index">
				<view class="pick" @tap="goUrl(`detail?id=${item.id}`)">
					<view class="nowrap name">{{item.title}}</view>
					<view class="nowrap time">{{item.subTime}}</view>
				</view>
			</view>
			<uni-load-more :status="status" :content-text="contentText" color="#007aff" />
			<view class="setNullHeight"></view>
		</template>
		<view class="nullData" v-else>
			<image src="/static/null.png" mode="widthFix" />
			<view>很抱歉，暂无数据</view>
		</view>
	</view>
</template>

<script>
	import uniLoadMore from '../../../../components/uni-load-more/uni-load-more.vue'
	import { getHistoryProList } from '../../../../api/api_problem.js'
	export default{
		data(){
			return {
				showPage: false,
				historyProList: [],
				total: null, // 列表总数
				pageNo: 1, // 默认第一页
				pageSize: 10, // 每页10条
				status: 'more',
				contentText: {
					contentdown: '查看更多',
					contentrefresh: '加载中',
					contentnomore: '没有更多'
				},
			}
		},
		components: {
			uniLoadMore
		},
		methods:{
			onLoad() { //初始加载
				// uni.startPullDownRefresh();
				this.getHistoryProList();
			},

			getHistoryProList(type) { //获取我的订单列表数据
				getHistoryProList({
					pageNo: this.pageNo,
					pageSize: this.pageSize,
				}, this)
			},

			goUrl(url) {
				uni.navigateTo({
					url: url
				});
			},
			
			// 下拉刷新
			onPullDownRefresh (){
				this.pageNo = 1
				this.historyProList = [];
				this.getHistoryProList();
				setTimeout(()=>{
					uni.stopPullDownRefresh()
				},1000)
			},
			
			// 上拉加载
			onReachBottom() {
				this.pageNo += 1;
				if (this.status != 'more') {
					return false;
				}
				this.status = 'loading'
				// uni.showNavigationBarLoading(); // 显示加载动画
				this.getHistoryProList();
			},
		},
	}
</script>

<style lang="less">
.historyProList {
	width: 690rpx;
	background: #FFF;
	border-radius: 10rpx;
	margin: 30rpx;
	.item {
		padding: 0 30rpx;
		.pick {
			line-height: 1.1;
			font-size: 24rpx;
			padding: 38rpx 0;
			.nowrap {
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
			}
			.name {
				color: #232323;
			}
			.time {
				color: #999999;
				margin-top: 26rpx;
			}
		}
		&:not(:last-child) {
			.pick {
				border-bottom: 1px solid rgba(224, 224, 224, 0.5);
			}
		}
	}
}
.setNullHeight {
	height: 60rpx;
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
