<template>
	<view class="viewsWraper">
		<view class="tabs-box">
			<view class="tabs uni-fs28" >
				<view :class="'tab ' + (item.selected&&'selected')" :data-type="item.code" @tap="onSetSelectedChange"
					v-for="(item, index) in tabList" :key="index" >
					{{item.title}}
				</view>
			</view>
		</view>
		<!--alarmLevelCode-- 0:一般 1：警告-alarm 2：严重-serious 3：紧急-urgent  -->
		<view class='list' v-if="warningList.length">
			<view class='item uni-fs24' v-for='(item, index) in warningList' :key='index'  @tap="gotoUrl('warningDetaile?id='+item.id)">
				<view class='name-level'>
					<text class="name">{{'事件编号：202011271512-L1OX'}}</text>
					<text :class="'level'+item.code">{{item.warnLevel}}</text>
				</view>
				<view class="warningInfo">
					<view class="des">告警名称：{{item.warnName}}</view>
					<view class="des">所属资产：{{item.equipmentType}}</view>
					<view class="des">告警时间：{{item.warnTime}}</view>
				</view>
			</view>
		</view>
		<block v-else>
			<view class="nullData">
				<image src="/static/null.png" mode="widthFix"></image>
				<view>很抱歉，暂无数据</view>
			</view>
		</block>
		<view v-if="warningList.length" class="setHeight"><uni-load-more :status="status" :content-text="contentText" color="#007aff" /></view>
	</view>
</template>

<script>
	import { getWarningList, getWarningClassify } from '../../../api/api_warning.js'
	import uniLoadMore from '../../../components/uni-load-more/uni-load-more.vue'

	//var pageNo = 1 // 默认加载第一页
	export default {
		data() {
			return {
				tabList: [], 
				warningList: [], //告警信息列表
				code: '', //0：tab全部
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
			uniLoadMore,
		},
		methods: {
			gotoUrl(url) {
				uni.navigateTo({
					url: url
				})
			},
			onSetSelectedChange(e) {
				let type = e.currentTarget.dataset.type;
				this.tabList.forEach(item => {
					item.selected = 0;
					if (item.code == type) {
						item.selected = 1;
					}
				})
				let data = {
					pageNo: 1,
					pageSize: this.pageSize,
					warnLevel: type
				};
				this.pageNo = 1;
				this.code = type;
				this.warningList = [];
				getWarningList(data, this);
			}
		},
		onLoad() {
			uni.startPullDownRefresh();
			getWarningClassify({code: 'warn_level'}, this)
			let data = {
				pageNo: this.pageNo,
				pageSize: this.pageSize,
				warnLevel: this.code
			}
			getWarningList(data, this);
		},

		// 下拉刷新
		onPullDownRefresh() {
			let data = {
				pageNo: 1,
				pageSize: this.pageSize,
				warnLevel: this.code
			}
			this.warningList = [];
			getWarningList(data, this);
		},
		// 上拉加载
		onReachBottom() {
			this.pageNo++;
			if (this.status != 'more') {
				return false;
			}
			this.status = 'loading'
			let data = {
				pageNo: this.pageNo,
				pageSize: this.pageSize,
				warnLevel: this.code
			}
			getWarningList(data, this);
		}
	}
</script>

<style lang="scss">
.tabs-box {
	width: 100%;
	height: 45px;
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
		background-color: #ffffff;
		.tab {
			padding: 30rpx 30rpx;
			&.selected {
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
.list {
	.item {
		line-height: 1;
		background: #FFFFFF;
		border-radius: 10rpx;
		overflow: hidden;
		padding: 30rpx;
		margin: 30rpx 30rpx 0;
	}
	.name-level {
		display: flex;
		justify-content: space-between;
		color: #232323;
		padding-bottom: 30rpx;
		border-bottom: 1px solid rgba(224, 224, 224, .5);
		.level {
			&0 { color: #39C6BE; }
			&1 { color: #F0A400; }
			&2 { color: #F000A4; }
			&3 { color: #DA0000; }
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

.setHeight {
	width: 100%;
	height: 88rpx;
}
</style>
