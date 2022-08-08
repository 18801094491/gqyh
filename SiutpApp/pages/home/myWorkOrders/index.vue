<template>
	<view class='myWorkOrders-page'>
		<view :class="'tabs-box ' + searchStatus">
			<view class="tabs uni-fs28" >
				<view :class="'tab ' + (item.active&&'active')" @tap="onSetTabChange(item.code)"
					v-for="(item, index) in tabList" :key="index" >
					{{item.title }}{{ item.num && item.code == 'doing' ? `(${item.num})` : ''}}
				</view>
			</view>
			<view class="order-search" v-if="searchStatus === 'finish'">
				<view class="searchBox">
					<view class="screen" @tap="onShowScreenChange">
						<image class="calendar" src="/static/workOrder-calendar.png" mode="widthFix" />
						<text>{{screenName}}</text>
					</view>
					<view class="search">
						<text class="searchType">工单编号</text>
						<input type="text" placeholder="请输入" @input="onSearchChange" v-model="searchCode" @touchstart="touch">
					</view>
				</view>
				<view class="screenList" v-if="showScreen" >
					<view class="item" v-for="(item, index) in screenList" :key="index" @tap="onSetScreenChange(item.code)">
						{{item.name}}
					</view>
				</view>
			</view>
		</view>
		<view class='workOrder-list'>
			<view class='item uni-fs24' v-for='(item, index) in workOrderList' :key='index' @tap="goUrl(`workOrderContent/index?status=${searchStatus}&id=${item.id}`)">
				<view class='name-level'>
					<image class="orderType" :src="`/static/workOrder-${item.type}.png`" mode="widthFix" />
					<text class="name">{{item.name}}</text>
					<image v-if="searchStatus==='finish' && (item.status==3 || item.status==4)" 
						class="orderOver" :src="`/static/workOrder-over${item.status}.png`" mode="widthFix" />
					<image class="orderNew" v-else-if="item.new" src="/static/workOrder-new.png" mode="widthFix" />
				</view>
				<view class="warningInfo">
					<view class="des">工单编号：{{item.code}}</view>
					<view class="des">工单时间：{{`${item.startDate}至${item.overDate}`}}</view>
				</view>
			</view>
		</view>
		<view class="setHeight"><uni-load-more :status="status" :content-text="contentText" color="#007aff" /></view>
		<view class="setNullHeight"></view>
	</view>
</template>

<script>
	import uniLoadMore from '../../../components/uni-load-more/uni-load-more.vue'
	import { getAction, getWorkOrderList, } from '../../../api/api_works.js'
	export default{
		data(){
			return{
				tabList: [
					{title: "进行中", num: 0, code: "doing", active: true},
					{title: "已结束", num: 0, code: "finish", active: false},
				],
				searchCode: '',
				showScreen: false,
				screenName: '全部',
				searchDate: 'all',
				screenList: [
					{name: '全部', code: 'all', active: true},
					{name: '本周', code: 'week', active: false},
					{name: '本月', code: 'month', active: false},
					{name: '本年', code: 'year', active: false},
				],
				searchStatus: 'doing',
				workOrderList: [],

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
				this.getWorkOrderList();
			},

			getWorkOrderList(type) { //获取我的订单列表数据
				if ( type === 'reset') { 
					this.pageNo = 1
					this.workOrderList = [];
				}
				getWorkOrderList({
					searchStatus: this.searchStatus,
					pageNo: this.pageNo,
					pageSize: this.pageSize,
					searchDate: this.searchDate,
					code: this.searchCode
				}, this)
			},

			onSetTabChange(code) { //切换页面tab
				this.showScreen = false;
				this.tabList.forEach(item => {
					item.active = false;
					if (item.code == code) {
						item.active = true;
						this.searchStatus = code;
					}
				})
				this.getWorkOrderList('reset');
			},

			onShowScreenChange() { //显隐筛选条件弹层
				this.showScreen = !this.showScreen;
			},

			onSetScreenChange(code) { //切换筛选条件
				this.screenList.forEach(item => {
					if (item.code == code) {
						this.screenName = item.name;
						this.searchDate = code;
						this.showScreen = false;
					}
				})
				this.getWorkOrderList('reset');
			},

			touch() { //搜索框点击事件
				this.showScreen = false;
			},

			onSearchChange() { //搜索框输入事件
				this.getWorkOrderList('reset');
			},

			goUrl(url) {
				uni.navigateTo({
					url: url
				});
			},
		
			// 下拉刷新
			onPullDownRefresh (){
				this.getWorkOrderList('reset');
				setTimeout(()=>{
					uni.stopPullDownRefresh() 
				},1000)
			},

			// 上拉加载
			onReachBottom() {
				this.showScreen = false;
				this.pageNo += 1;
				if (this.status != 'more') {
					return false;
				}
				this.status = 'loading'
				this.getWorkOrderList();
			},
		}
	}
</script>

<style lang="less">
.myWorkOrders-page {
	.tabs-box {
		width: 100%;
		height: 90rpx;
		&.finish {
			height: 200rpx;
		}
		.tabs {
			display: flex;
			align-items: center;
			justify-content: space-around;
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 90rpx;
			line-height: 1;
			font-weight: bold;
			background-color: #ffffff;
			border-bottom: 1px solid rgba(204, 204, 204, 0.5);
			z-index: 100;
			.tab {
				padding: 30rpx 30rpx;
				&.active {
					position: relative;
					color: #3D71AD;
					&::after {
						content: '';
						position: absolute;
						left: 50%;
						bottom: 0;
						width: 28px;
						height: 2px;
						transform: translateX(-50%);
						background-color: #3D71AD;
					}
				}
			}
		}
	}
	.order-search {
		position: fixed;
		top: 90rpx;
		display: flex;
		line-height: 1;
		width: 100%;
		height: 110rpx;
		font-size: 28rpx;
		padding: 0 30rpx;
		background-color: #ffffff;
		z-index: 100;
		.searchBox {
			display: flex;
			align-items: center;
			justify-content: space-between;
			width: 100%;
			.screen {
				display: flex;
				align-items: center;
				background-color: #E0E0E0;
				width: 200rpx;
				height: 80rpx;
				border-radius: 40px;
				.calendar {
					width: 54rpx;
					height: 54rpx;
					margin: 1px 10rpx 0 24rpx;
				}
			}
			.search {
				position: relative;
				align-items: center;
				top: 0;
				left: 0;
				width: 470rpx;
				height: 80rpx;
				.searchType {
					position: absolute;
					top: 50%;
					transform: translateY(-50%);
					left: 0;
					line-height: 1;
					z-index: 1;
					padding: 8rpx 22rpx;
					border-right: 1px solid rgba(204, 204, 204, 0.5);
				}
				input {
					position: relative;
					height: 100%;
					font-size: 28rpx;
					background-color: #E0E0E0;
					padding: 0 86rpx 0 174rpx;
					border-radius: 40rpx;
					box-sizing: border-box;
					&::after {
						content: '';
						position: absolute;
						top: 50%;
						right: 20rpx;
						width: 54rpx;
						height: 54rpx;
						transform: translateY(-46%);
						background-size: 100% 100%;
						background-repeat: no-repeat;
						background-image: url(/static/search.png);
					}
				}
			}
		}
		.screenList {
			position: fixed;
			top: 200rpx;
			left: 10rpx;
			width: 156rpx;
			height: 232rpx;
			background-color: #FFFFFF;
			padding-left: 20rpx;
			line-height: 1;
			border: 1px solid #ccc;
			border-radius: 10rpx;
			.item {
				width: 100%;
				height: 58rpx;
				line-height: 58rpx;
				font-size: 24rpx;
				padding-left: 20rpx;
			}
			.item:not(:last-child) {
				border-bottom: 1px solid rgba(204, 204, 204, 0.5);
			}
		}
	}
	.workOrder-list {
		.item {
			position: relative;
			line-height: 1.1;
			background: #FFFFFF;
			border-radius: 10rpx;
			overflow: hidden;
			padding: 0 30rpx 30rpx 30rpx;
			margin: 30rpx 30rpx 0;
		}
		.name-level {
			display: flex;
			align-items: center;
			height: 82rpx;
			color: #232323;
			border-bottom: 1px solid rgba(224, 224, 224, .5);
			.orderType {
				flex-shrink: 0;
				width: 36rpx;
				height: 36rpx;
				margin-right: 20rpx;
			}
			.name {
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
			.orderNew {
				width: 40rpx;
				height: 22rpx;
				margin-left: 10rpx;
			}
			.orderOver {
				position: absolute;
				top: 0;
				right: 0;
				width: 86rpx;
				height: 86rpx;
			}
		}
		.warningInfo {
			line-height: 1;
			color: #999999;
			margin-top: 28rpx;
			view {
				&:not(:last-child) {
					margin-bottom: 26rpx;
				}
			}
		}
	}
}
.setNullHeight {
	height: 60rpx;
}
</style>
