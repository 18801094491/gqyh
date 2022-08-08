<template>
	<view class="qiun-columns" v-if="showPage">
		<view class="page-top">
			<view class="tit">全局时间查询</view>
			<view class="time">
				<ruiDatePicker
					v-if="showPicker"
					:default="startDate"
					fields="second"
					type="startDate"
					:value="'请选择开始时间'"
					:reset="reset"
					@change="bindDateChange"
				></ruiDatePicker>
				<ruiDatePicker
					v-if="showPicker"
					:default="endDate"
					fields="second"
					type="endDate"
					:value="'请选择结束时间'"
					:reset="reset"
					@change="bindDateChange"
				></ruiDatePicker>
				<view class="btn reset" @tap="onReastChange">重置</view>
				<view class="btn search" @tap="onSearchChange">查询</view>
			</view>
		</view>
		<view class="con-item" v-for="(item, index) in chartList" :key='index'>
			<view class="tit">
				{{item.title}}
			</view>
			<canvas :canvas-id="'canvasChart'+(index+1)" :id="'canvasChart'+(index+1)" class="charts"
				@touchstart="touchStartChart" @touchmove="touchMoveChart" @touchend="touchEndChart"></canvas>
			<!-- <view v-else class="dataNull">暂无数据</view> -->
		</view>
	</view>
</template>

<script>
	import ruiDatePicker from '@/components/rattenking-dtpicker/rattenking-dtpicker.vue';
	import uCharts from '../../components/u-charts/u-charts.js';
	import { getChartsData } from '../../api/api_leader.js'
	
	var canvasObj = {};

	export default {
		components: {ruiDatePicker},
		data() {
			return {
				showPage: false,
				showPicker: false,
				startDate: '',
				endDate: '',
				reset: false,
				chartList: [],
				cWidth: '',
				cHeight: '',
				cWidth2: '', //横屏图表
				cHeight2: '', //横屏图表
				cWidth3: '', //圆弧进度图
				cHeight3: '', //圆弧进度图
				arcbarWidth: '', //圆弧进度图，进度条宽度,此设置可使各端宽度一致
				gaugeWidth: '', //仪表盘宽度,此设置可使各端宽度一致
				pixelRatio: 1,
				serverData: '',
				itemCount: 30, //x轴单屏数据密度
				color:['#1890ff', '#2fc25b', '#facc14', '#f04864', '#8543e0', '#90ed7d','#996633'],
				positionTimer: null, // 数据隔5秒刷新显示
				flag: false,

				idIndex: 0,
				idList: [], //图表接口数组
			}
		},
		onLoad() {
			let myDate = new Date();
			let date = myDate.getFullYear() + '-' + (+myDate.getMonth()+1) + '-' + myDate.getDate();
			this.startDate = date + ' 00:00:00';
			this.endDate = date + ' 23:59:59';
			this.showPicker = true;

			if ( (uni.getStorageSync('role') || {}).roles.includes('监控中心') ) {
				this.cWidth = uni.upx2px(750);
				this.cHeight = uni.upx2px(500);
				this.cWidth2 = uni.upx2px(700);
				this.cHeight2 = uni.upx2px(1100);
				this.cWidth3 = uni.upx2px(250);
				this.cHeight3 = uni.upx2px(250);
				this.arcbarWidth = uni.upx2px(24);
				this.gaugeWidth = uni.upx2px(30);
				this.showPage = true;
				
				canvasObj = {}; //退出后再访问初始化数据
				this.chartList = []; //退出后再访问初始化数据
				getChartsData(this, {left: this.startDate, right: this.endDate}, false);
			} else {
				this.showPage = false;
			}
		},
		onShow() {
			if (!this.positionTimer) {
				this.positionTimer = setInterval(() => {
					getChartsData(this, {left: this.startDate, right: this.endDate}, true);
				}, 10000)
			}
			if ( !(uni.getStorageSync('role') || {}).roles.includes('监控中心') ) {
				if (this.flag) return
				this.flag = true;
				uni.showModal({
					content: '您暂无访问此功能权限，如需开通请联系管理员。感谢配合。',
					showCancel: false,
					confirmText: '回到首页',
					success: res => {
						if (res.confirm) {
							this.flag = false;
							wx.switchTab({ url: '/pages/home/home' })
						}
					}
				});
			}
		},
		onHide(){
			clearInterval(this.positionTimer) // 清除定时器
			this.positionTimer = null;
		},
		methods: {
			bindDateChange(time, type) {
				console.log(type)
				if (type === 'endDate') {
					this.reset = false;
				}
				this[type] = time;
			},
			onReastChange() {
				this.reset = true;
				this.startDate = '';
				this.endDate = '';
				getChartsData(this, {left:'',right:''}, true);
			},
			onSearchChange() {
				let title = ''
				if (!this.startDate) {
					title = '请选择开始时间'
				} else if (!this.endDate) {
					title = '请选择结束时间'
				}
				if (new Date(this.startDate).getTime() > new Date(this.endDate).getTime()) {
					title = '开始时间不能大于结束时间'
				}
				if (title) {
					uni.showToast({
						title: title,
						icon: 'none',
						mask: true,
						duration: 1500
					});
					return;
				}
				getChartsData(this, {left: this.startDate, right: this.endDate}, true);
			},
			// chart
			showChart(canvasId, chartData, markLine, scroll) {
				if (canvasObj[canvasId]) {
					canvasObj[canvasId].updateData(chartData);
					return;
				} 
				canvasObj[canvasId] = new uCharts({
					$this: this,
					canvasId: canvasId,
					type: 'line',
					padding:[15,15,15,15],
					legend:{
						show: true,
						padding: 5,
						lineHeight: 11,
						margin: 10,
					},
					fontSize:11,
					dataLabel: false,
					dataPointShape: true,
					enableScroll: scroll,
					enableMarkLine: true,
					background:'#FFFFFF',
					pixelRatio: this.pixelRatio,
					animation: false,
					categories: chartData.categories,
					series: chartData.series,
					xAxis: {
						disableGrid: true,
						rotateLabel: false,
						scrollShow: scroll,
						itemCount: 5,
					},
					yAxis: {
						gridColor: '#f3f0f0',
						format:(val)=>{return val.toFixed(0)}
					},
					width: this.cWidth*this.pixelRatio,
					height: this.cHeight*this.pixelRatio,
					extra: {
						line:{
							type: 'straight'
						},
						tooltip: {
							bgColor:'#000000',
							bgOpacity: 0.7,
							gridType:'dash',
							dashLength: 2,
							gridColor:'#1890ff',
							fontColor:'#FFFFFF',
						},
						markLine: markLine
					}
				});
			},
			touchStartChart(e){
				let id = (e.currentTarget && e.currentTarget.id) || (e.target && e.target.id)
				canvasObj[id] && canvasObj[id].scrollStart && canvasObj[id].scrollStart(e);
			},
			touchMoveChart(e) {
				let id = (e.currentTarget && e.currentTarget.id) || (e.target && e.target.id)
				canvasObj[id] && canvasObj[id].scroll && canvasObj[id].scroll(e);
			},
			touchEndChart(e) {
				let id = (e.currentTarget && e.currentTarget.id) || (e.target && e.target.id)
				canvasObj[id] && canvasObj[id].scrollEnd && canvasObj[id].scrollEnd(e);
				//下面是toolTip事件，如果滚动后不需要显示，可不填写
				canvasObj[id] && canvasObj[id].showToolTip && canvasObj[id].showToolTip(e, {
					format: function (item, category) {
						return category + ' ' + item.name + ':' + item.data 
					}
				});
			},
		}
}
			
</script>

<style lang="less">


.qiun-columns {
	.page-top {
		font-size: 24rpx;
		padding: 10rpx 30rpx 30rpx;
		background: #3D71AD;
		.tit {
			line-height: 1;
			color: #ffffff;
			padding-bottom: 30rpx;
		}
		.time {
			display: flex;
			picker {
				width: 224rpx !important;
				height: 50rpx !important;
				line-height: 52rpx !important;
				color: #CDDBEB !important;
				font-size: 20rpx !important;
				border-radius: 4rpx !important;
				padding: 0 10rpx !important;
				border: 1px solid rgba(205, 219, 235, 0.5) !important;
				margin-right: 20rpx;
				box-sizing: border-box;
			}
			.btn {
				width: 80rpx;
				height: 50rpx;
				line-height: 52rpx;
				text-align: center;
				color: #FFFFFF;
				border-radius: 4rpx;
				margin-left: 20rpx;
				&.reset {
					border: 1px solid rgba(255, 255, 255, 0.4);

				}
				&.search {
					background: #38C3BE;

				}
			}
		}
		.picker-view {
			width: 750rpx;
			height: 600rpx;
			margin-top: 20rpx;
		}
		.item {
			height: 50px;
			align-items: center;
			justify-content: center;
			text-align: center;
		}
	}

	display: flex;
	flex-direction: column !important;
	.top-linear-gradient {
		flex-direction: column;
		width: 100%;
		height: 200rpx;
		color: #FFFFFF;
		background: linear-gradient(180deg, #3D71AD, #20B8EE);
		.num {
			font-size: 50rpx;
		}
	}
	.con-item {
		background-color: #FFFFFF;
		padding: 30rpx;
		margin-bottom: 14rpx;
		.tit {
			position: relative;
			display: flex;
			justify-content: space-between;
			line-height: 1;
			color: #232323;
			font-weight: bold;
			padding: 30rpx 0 30rpx 16rpx;
			text {
				color: #999999;
				font-weight: normal;
			}
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
	}
	.dataNull {

	}
}
.charts {
	width: 750rpx;
	height: 500rpx;
	background-color: #FFFFFF;
	margin-left: -30rpx;
}
</style>
