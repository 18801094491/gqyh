<template>
	<view class="warningDetail">
		<view class="list">
			<view class="item">
				<view class="info"><view class="tit">事件编号：</view><view class="con">{{warningDetail.warnSn}}</view></view>
				<view class="info"><view class="tit">告警名称：</view><view class="con">{{warningDetail.warnName}}</view></view>
				<view class="info"><view class="tit">所属资产：</view><view class="con">{{warningDetail.equipmentType}}</view></view>
				<view class="info"><view class="tit">告警时间：</view><view class="con">{{warningDetail.warnTime}}</view></view>
			</view>
			<view class="item">
				<view class="info">
					<view class="tit">告警等级：</view><view :class="'level'+ '1'">{{warningDetail.warnLevel}}</view></view>
			</view>
			<view class="item">
				<view class="info"><view class="tit">告警状态：</view><view class="con">{{warningDetail.warnStatus}}</view></view>
				<view class="info"><view class="tit">告警渠道：</view><view class="con">{{warningDetail.warnWay}}</view></view>
				<view class="info"><view class="tit">告警内容：</view><view class="con">{{warningDetail.warnContent}}</view></view>
				<view class="info"><view class="tit">规则详情：</view><view class="con">{{warningDetail.ruleContent}}</view></view>
			</view>
			<view class="item">
				<view class="info"><view class="tit">操作时间：</view><view class="con">{{warningDetail.operateTime}}</view></view>
				<view class="info"><view class="tit">操作人员：</view><view class="con">{{warningDetail.operator}}</view></view>
				<view class="info"><view class="tit">操作时长：</view><view class="con">{{warningDetail.duration}}</view></view>
			</view>
		</view>
	</view>
</template>

<script>
	import { getWarningDetail } from '../../../api/api_warning.js'
	var _self;
	var canvaGauge = null;
	export default {
		data() {
			return {
				warningDetail: [],
				gaugeList:[], // 列表数据
				cWidth: '', // 
				cHeight: '',
				pixelRatio: 1,
				gaugeWidth: 15,
				old: {
					scrollTop: 0
				},
				interval: 5000,
				duration: 500,
				gaugeColor:['#1890ff','#2fc25b','#f04864'],
				detailObj:{},
				imgSrc:'', // 没有仪表盘展示图片
			}
		},
		methods: {
			// 暂无数据
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
			getWarningDetail(option.id, this)
		},
	}
</script>

<style>
page {
	background-color: #ffffff;
}
.warningDetail .list {
	line-height: 1.1;
	padding: 0 30rpx;
}
.warningDetail .item {
	padding: 38rpx 0;
}
.warningDetail .item:not(:last-child) {
	border-bottom: 1px solid rgba(224, 224, 224, 0.5);
}
.warningDetail .item .info {
	display: flex;
}
.warningDetail .item .info:not(:last-child) {
	margin-bottom: 42rpx;
}
.warningDetail .item .info .tit {
	color: #999999;
	flex-shrink: 0;
}
.warningDetail .item .info .con {
	color: #323232;
}

.level0 { color: #39C6BE !important; }
.level1 { color: #F0A400 !important; }
.level2 { color: #F000A4 !important; }
.level3 { color: #DA0000 !important; }
</style>
