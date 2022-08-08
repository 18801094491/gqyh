<template>
	<view class="viewsWraper">
		<view class="searchbox">
			<view class="search">
				<view class="screen uni-fl-cn" @tap="clickType">{{typeCon?typeCon:'全部'}}</view>
				<input type="text" placeholder="编号/放置位置/标段" @input="onSearchChange" v-model="searchInputCon" @touchstart="touch">
			</view>
		</view>
		<view v-show="isTrue" class="sharePopup">
			<uni-list class="shareList" v-show="typeShow">
				<uni-list-item :show-arrow="false" @tap="selectType">全部</uni-list-item>
				<uni-list-item :show-arrow="false" v-for="(item,index) in equiTypeList" :key="index" @tap="selectType(item)" :value="item.code">{{item.title}}</uni-list-item>
			</uni-list>
		</view>
		<view class='list'>
			<view class='item uni-fs24' v-for='(item, index) in list' :key='index'  @tap="goDetails('equipmentAlarmDetaile?id='+item.id)">
				<view class='name-level'>
					<text class="name">{{item.optTypeName}}</text>
					<!--alarmLevelCode-- 0:一般 1：警告-alarm 2：严重-serious 3：紧急-urgent  -->
					<text :class="'level'+item.alarmLevelCode">{{item.alarmLevel}}</text>
				</view>
				<view class="alarmInfo">
					<view class="des">{{item.alarmContent}}</view>
					<view class="time">{{item.occurTime}}</view>
				</view>
			</view>
		</view>

		<view class="setHeight"><uni-load-more :status="status" :content-text="contentText" color="#007aff" /></view>
	</view>
</template>

<script>
	import uniLoadMore from '../../../components/uni-load-more/uni-load-more.vue'
	import uniList from "../../../components/uni-list/uni-list.vue"
	import uniListItem from "../../../components/uni-list-item/uni-list-item.vue"
	import {
		getAction,
		getAlarmList,
		getEquiType
	} from '../../../api/api_alarm.js'

	//var pageNo = 1 // 默认加载第一页
	let _this;
	export default {
		data() {
			return {
				list: [], // 列表数据
				total: null, // 列表总数
				pageNo: 1, // 默认第一页
				pageSize: 10, // 每页10条
				status: 'more',
				contentText: {
					contentdown: '查看更多',
					contentrefresh: '加载中',
					contentnomore: '没有更多'
				},
				equiTypeList: [],
				typeCon: '', // 设备类型名称
				typeCode: '', // 设备类型code
				typeShow: false,
				searchInputCon: '', // 搜索框内容
				isTrue: false,
				searchTimer: null,
				is_page:true,
			}
		},
		components: {
			uniLoadMore,
			uniList,
			uniListItem
		},
		methods: {
			touch() {
				this.typeShow = false;
			},
			goDetails(url) {
				console.log(url, "id:");
				uni.navigateTo({
					url: url

				})
				this.typeShow = false;
			},
			clickType() { // 导航栏左侧点击事件
				this.typeShow = !this.typeShow;
				this.isTrue = true;

			},
			selectType(val) { // 下拉选择		
				this.typeShow = false;
				this.pageNo = 1;
				if (val == undefined || val=='') {
					this.typeCon = '全部';
					this.typeCode = '';
					console.log('wu pipei leixing');
				} else {
					let code = this.equiTypeList.filter(item => {
						if (item.code == val.code) {
							return item
						}
					})
					this.typeCon = code[0].title;
					this.typeCode = code[0].code;
					console.log(code[0].title);
					console.log(code[0].code);
					
				}
				let data = {
					pageNo: 1,
					equipType: this.typeCode ? this.typeCode : '',
					param: this.searchInputCon ? this.searchInputCon : ''
				}
				this.list = [];
				getAlarmList(data, this);
				this.isTrue = false;
			},
			onSearchChange(e) { // 搜索框搜索
				console.log(this.searchInputCon, 'this.searchInputCon')
				this.typeShow = false;
				let data = {
					pageNo: 1,
					equipType: this.typeCode ? this.typeCode : '',
					param: this.searchInputCon
				}
				if (this.searchTimer) {
					clearTimeout(this.searchTimer);
					this.searchTimer = null;
				}
				this.list = [];
				this.searchTimer = setTimeout(() => { //节流
					getAlarmList(data, _this);
				}, 500)
			}
		},
		onLoad() {
			_this = this;
			uni.startPullDownRefresh();
			let data = {
				pageNo: _this.pageNo,
				equipType: _this.typeCode ? _this.typeCode : '',
				param: _this.searchInputCon
			}
			getAlarmList(data, _this);
			getEquiType({
				pcode: 'A03'
			}, _this)
		},

		// 下拉刷新
		onPullDownRefresh() {
			let _this = this;
			let data = {
				pageNo: 1,
				equipType: _this.typeCode ? _this.typeCode : '',
				param: _this.searchInputCon
			}
			getAlarmList(data, _this);
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
			_this.typeShow = false;
		},
		// 上拉加载
		onReachBottom() {
			_this = this;
			_this.typeShow = false;
			
			// if (_this.status != 'more') {
			// 	return false;
			// }
			// this.status = 'loading'
			// uni.showNavigationBarLoading(); // 显示加载动画
				
			if(_this.is_page){
				_this.pageNo++;
				let data = {
					pageNo: _this.pageNo,
					equipType: _this.typeCode ? _this.typeCode : '',
					param: _this.searchInputCon ? _this.searchInputCon : ''
				}
				_this.status = 'loading'
				uni.showNavigationBarLoading(); // 显示加载动画
				getAlarmList(data, _this);
				
			}
			if(!_this.is_page &&  _this.pageNo > 1){
				console.log( '没有更多数据' );
			}
			
		}
	}
</script>

<style lang="scss" scoped>
.searchbox { //搜索框
	position: relative;
	width: 100%;
	height: 112rpx;
	z-index: 999;
	.search {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 112rpx;
		padding: 16rpx 0;
		background-color: #FFFFFF;
		.screen {
			position: absolute;
			top: 16rpx;
			left: 30rpx;
			width: 156rpx;
			line-height: 1;
			height: 80rpx;
			z-index: 1;
			&::after {
				content: '';
				position: absolute;
				top: 50%;
				right: 0;
				width: 1px;
				height: 42rpx;
				transform: translateY(-50%);
				background-color: rgba(204, 204, 204, 0.5);
			}
		}
		input {
			position: relative;
			width: 690rpx;
			height: 80rpx;
			font-size: 28rpx;
			background-color: #E0E0E0;
			border-radius: 40rpx;
			padding: 0 86rpx 0 186rpx;
			margin: 0 30rpx;
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
.sharePopup {
	.shareList {
		position: fixed;
		top: 110rpx;
		left: 10rpx;
		width: 160rpx;
		font-size: 24rpx;
		border-radius: 10rpx;
		border: 1px solid #ccc;
		overflow: hidden;
		z-index: 9998;
		/deep/ .uni-list-item {
			padding-left: 10px;
			.uni-list-item__container {
				padding: 8rpx 20rpx;
			}
		}
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
	.alarmInfo {
		line-height: 1;
		color: #999999;
		view {
			&:first-child {
				color: #232323;
				margin: 28rpx 0 20rpx;
			}
		}
	}
}
.setHeight {
	width: 100%;
	height: 88rpx;
}
</style>
