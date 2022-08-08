<template>
<view class='viewsWraper' >
	<view class='list' v-if='defaultList.length'>
		<view class='item' v-for='(item, index) in defaultList' :key='index'>
			<view class='equip'>
				<image :src="item.equipImg" v-if="item.equipImg" mode="aspectFill" />
				<image v-else src="../../../static/moren.jpg" mode="aspectFill" />
				<text class="equipName uni-fs24">{{item.equipName}}</text>
			</view>
			<view class="null" v-if="!item.datas.length">暂无数据</view>
			<view class="equipInfo uni-fs24" v-else>
				<view class="info" v-for="(items, i) in item.datas" :key="i">
					<text v-if="items.variableName">{{items.variableName}}</text>
					<text v-if="items.varibleValue">{{items.varibleValue}}</text>
				</view>
			</view>
		</view>
	</view>
	<view class='list'  v-if="workingList.length">
		<view class='item' v-for="(item,index) in workingList" :key="'info'+index">
			<view class='equip'>
				<image :src="item.equipImg" v-if="item.equipImg" mode="aspectFill" />
				<image v-else src="../../../static/moren.jpg" mode="aspectFill" />
				<text class="equipName uni-fs24">{{item.equipName}}</text>
			</view>
			<view class="null" v-if="!item.datas.length">暂无数据</view>
			<view class="equipInfo uni-fs24" v-else>
				<view class="info" v-for="(items, i) in item.datas" :key="i">
					<text v-if="items.variableName">{{items.variableName}}</text>
					<text v-if="items.varibleValue">{{items.varibleValue}}</text>
				</view>
			</view>
		</view>
	</view>
	<view class="setHeight"></view>
	<button class="editBtn" type="default" @tap="goUrl('monitorEquip')">编辑监控设备</button>
</view>
</template>

<script>
	import uniLoadMore from '../../../components/uni-load-more/uni-load-more.vue'
	
	import { getAction, getWorkingList, getDefaultList } from '../../../api/api_working.js'	
	var _this
	export default{
		data(){
			return{
				workingList:[],
				defaultList:[], // 默认加载列表
				total:null, // 列表总数
				pageNo:1,
				pageSize:10,
				status: 'more',
				contentText: {
					contentdown: '查看更多',
					contentrefresh: '加载中',
					contentnomore: '没有更多'
				},
				intervalTimer:null, // 计时器
			}
		},
		components: {
			uniLoadMore
		},
		methods:{
			goUrl(url) {
				uni.navigateTo({
					url: url
				});
			},
			back() {
				// uni.navigateBack({
				// 	delta: 2
				// });
				uni.switchTab({
					url:'/pages/home'
				})
			},
			onFloorImageLoad(key, index) {
				// console.log(this[key][index].equipImg)
				// this.$set(this[key][index].equipImg, 'loaded', 'loaded');
			},
			onFloorImageError(key, index) {
				// console.log(this[key][index].equipImg)
				this[key][index].equipImg = '../../static/moren.jpg';
			},
			addSetInterval(){
				this.intervalTimer = setInterval(()=>{
					getWorkingList({},_this)
					// getDefaultList({},_this)
					uni.hideLoading();
				},5000)
			}
		},
		onLoad(){
			_this = this;
			uni.startPullDownRefresh();
			let data = {
				// pageNo: _this.pageNo,
				// pageSize: _this.pageSize
			}
			getWorkingList(data,_this)
			// getDefaultList({},_this)
			_this.addSetInterval();
		},
		// 下拉刷新
		onPullDownRefresh (){
			let _this = this
			setTimeout(()=>{
				uni.stopPullDownRefresh()
			},1000)
		},
		// 上拉加载
		onReachBottom() {
			_this = this;		
		},
		onReady() {
			_this = this
			uni.getSystemInfo({
				success(res) {

				}
			})
		},
		destroyed() {
		    clearInterval(this.intervalTimer)// 清除定时器
		    this.intervalTimer = null
		}
	}
</script>


<style lang="less">
.viewsWraper {
	.list {
		.item {
			line-height: 1;
			background: #FFFFFF;
			border-radius: 10rpx;
			overflow: hidden;
			padding: 30rpx 30rpx 50rpx;
			margin: 30rpx 30rpx 0;
		}
		.equip {
			display: flex;
			align-items: center;
			padding: 30rpx 0;
			border-bottom: 1px solid rgba(224, 224, 224, .5);
			image {
				width: 100rpx;
				height: 100rpx;
				margin-right: 20rpx;
			}
			.equipName {
				color: #232323;
			}
		}
		.equipInfo {
			line-height: 1;
			color: #999999;
			padding-top: 39rpx;
			.info {
				display: flex;
				justify-content: space-between;
				&:not(:last-child) {
					margin-bottom: 46rpx;
				}
			}
		}
		.null {
			color: #999999;
			text-align: center;
			padding-top: 30rpx;
		}
	}
	.setHeight {
		width: 100%;
		height: 188rpx;
	}
	.editBtn {
		position: fixed;
		bottom: 0;
		left: 0;
		width: 100%;
		height: 98rpx;
		line-height: 98rpx;
		text-align: center;
		color: #FFFFFF !important;
		font-size: 34rpx;
		border-radius: 0;
		background-color: #3D71AD !important;
		z-index: 999;
	}
}
</style>
