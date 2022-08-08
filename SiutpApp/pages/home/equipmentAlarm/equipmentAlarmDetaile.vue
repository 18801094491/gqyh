<template>
	<view class="alarmDetail">
		<view class="equiInfo">
			<view class="tit uni-fs30">设备信息</view>
			<view class="list">
				<view class="item">设备型号: <text>{{detailObj.optTypeName}}</text></view>
				<view class="item">资产编号: <text>{{detailObj.optSn}}</text></view>
				<view class="item">所属标段: <text>{{detailObj.optSection}}</text></view>
				<view class="item">放置位置: <text>{{detailObj.optPosition}}</text></view>
				<view class="item">资产名称: <text>{{detailObj.optName}}</text></view>
				<view class="item">生产厂家: <text>{{detailObj.optSupplier}}</text></view>
			</view>
			<view class="alarmInfo">告警内容: <view>{{detailObj.alarmContent}}</view></view>
		</view>
	</view>
</template>

<script>
	import { alarmDetail,alarmCharts } from '../../../api/api_alarm.js'
	var _self;
	var canvaGauge = null;
	export default {
		data() {
			return {
				gaugeList:[], // 列表数据
				cWidth: '', // 
				cHeight: '',
				pixelRatio: 1,
				gaugeWidth: 15,
				old: {
					scrollTop: 0
				},
				background: ['color1', 'color2', 'color3'],
				indicatorDots: true,
				autoplay: true,
				interval: 5000,
				duration: 500,
				gaugeColor:['#1890ff','#2fc25b','#f04864'],
				detailObj:{},
				imgSrc:'', // 没有仪表盘展示图片
			}
		},
		methods: {
			getServerData(){
				let data = [];
				_self = this;
				if(!this.gaugeList || this.gaugeList.length==0) return
				this.gaugeList.forEach((item,index)=>{
					data[index] = {categories:[],series:[],name:'',minValue:'',maxValue:'',color:''}
					data[index].series.push({
						name:item.title,
						data:((item.realValue*1) - (item.minValue*1)) / ((item.maxValue*1) - (item.minValue*1)),
						dataName:item.realValue*1
					})
					data[index].categories.push({
						value:((item.leftValue*1) - (item.minValue*1)) / ((item.maxValue*1) - (item.minValue*1)),color:_self.gaugeColor[0]
					},{
						value:((item.rightValue*1) - (item.minValue*1)) / ((item.maxValue*1) - (item.minValue*1)),color:_self.gaugeColor[1]
					},{
						value:((item.maxValue*1) - (item.minValue*1)) / ((item.maxValue*1) - (item.minValue*1)),color:_self.gaugeColor[2]
					})
					if(item.realValue*1>=item.minValue*1 && item.realValue*1<item.leftValue*1){
						data[index].color = _self.gaugeColor[0]
					}else if(item.realValue*1>=item.leftValue*1 && item.realValue*1<item.rightValue*1){
						data[index].color = _self.gaugeColor[1]
					}else{
						data[index].color = _self.gaugeColor[2]
					}
					data[index].name = item.title;
					data[index].minValue = item.minValue*1;
					data[index].maxValue = item.maxValue*1;
				
				})
			},
		},
		onLoad(option) {
	
			alarmDetail({bid:option.id},this)
			_self = this;
			uni.getSystemInfo({
				success(res){
					_self.cWidth = res.windowWidth;
				}
			})

			this.cHeight = uni.upx2px(500);
			uni.startPullDownRefresh({
				success:function(res){
					setTimeout(()=>{

					},100)
				}
			});
			setTimeout(()=>{				

			},100)
		},
		// 下拉刷新
		onPullDownRefresh (){
			let _this = this;
			_this.getServerData();
			setTimeout(()=>{				
				uni.stopPullDownRefresh()
			},1000)
		},
	}
</script>

<style lang="less">
.alarmDetail {
	.alarmEquip {
		image {
			display: block;
			width: 100%;
			height: 380rpx;
		}
	}
	.equiInfo {
		line-height: 1;
		background: #FFFFFF;
		padding: 30rpx 30rpx 160rpx;
		.tit {
			position: relative;
			color: #232323;
			font-weight: bold;
			padding-left: 16rpx;
			&::after {
				content: '';
				position: absolute;
				top: 50%;
				left: 0;
				width: 6rpx;
				height: 28rpx;
				transform: translateY(-50%);
				background-color: #3D71AD;
			}
		}
		.list {
			padding: 50rpx 0;
			border-bottom: 1px solid rgba(224, 224, 224, .5);
			.item {
				color: #999999;
				&:not(:last-child) {
					margin-bottom: 46rpx;
				}
				text {
					color: #323232;
					margin-left: 12rpx;
				}
			}
		}
		.alarmInfo {
			font-weight: bold;
			margin-top: 48rpx;
			view {
				margin-top: 46rpx;
				font-weight: normal;
			}
		}
	}
}
</style>
