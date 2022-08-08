<template>
	<view class="viewWrape">
		<view class="searchbox">
			<view class="search">
				<view class="screen uni-fl-cn" @tap="clickType">{{typeCon?typeCon:'全部'}}</view>
				<input type="text" placeholder="编号/放置位置/标段" @input="onSearchChange" v-model="searchInputCon" @touchstart="touch">
			</view>
		</view>
		<view v-show="typeShow" class="sharePopup">
			<uni-list class="shareList">
				<uni-list-item :show-arrow="false" @tap="selectType">全部</uni-list-item>
				<uni-list-item :show-arrow="false" v-for="(item,index) in equiTypeList" :key="index" @tap="selectType(item)" :value="item.code">{{item.title}}</uni-list-item>
			</uni-list>
		</view>
		<view class="equip-list">
			<checkbox-group class="equip-checkbox-group uni-fs24" @change="checkboxChange($event)">
				<label class="equip-item" v-for="(item,index) in equiqList" :key="index">
					<view class="con">
						<checkbox :value="item.equipId" :checked="item.selectStatus=='1'" color="#3D71AD" />
						{{item.equipName}}
					</view>
				</label>
			</checkbox-group>
		</view>
		<view class="setHeight"><uni-load-more :status="status" :content-text="contentText" color="#007aff" /></view>
		<button class="sureBtn" type="default" @tap="monitorSubmit" :loading="loadingSubmit">确定</button>
	</view>
</template>

<script>
	import uniLoadMore from '../../../components/uni-load-more/uni-load-more.vue'
	import uniNavBar from "../../../components/uni-nav-bar/uni-nav-bar.vue"
	import uniList from "../../../components/uni-list/uni-list.vue"
	import uniListItem from "../../../components/uni-list-item/uni-list-item.vue"
	import uniSearchBar from '../../../components/uni-search-bar/uni-search-bar.vue'

	import {
		getAction,
		getMontiorList,
		saveMontiorList,
		getEquiType
	} from '../../../api/api_working.js'
	var _this
	export default {
		data() {
			return {
				equiqList: [],
				total: null, // 列表总数
				pageNo: 1,
				pageSize: 15,
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
				loadingSubmit: false,
				checkList: [], // 最初已选中的列表id
				isCheck: null, // 是否包含最初选中的
				preSelectedValues: [],
				searchTimer: null,
				currentSelected: [], //选中的数组
			}
		},
		components: {
			uniLoadMore,
			uniNavBar,
			uniList,
			uniListItem,
			uniSearchBar
		},
		methods: {
			onSelectChange(e) {
				console.log(e)
				let itemdata = e.currentTarget.dataset.item;
				let equipid = itemdata.equipId;
				let currentSelected = this.currentSelected;
				if ( currentSelected.includes(equipid) ) {
					item.selectStatus = '0'
					currentSelected = currentSelected.filter( item => item != equipid )
				} else {
					item.selectStatus = '1'
					currentSelected.push(equipid)
				}
				this.equiqList.forEach( item => {
					item.equipId == itemdata.equipId && item.selectStatus
				})
				
				this.currentSelected = currentSelected;
				this.selectChange(this.currentSelected);
				this.typeShow = false;
			},
			touch() {
				this.typeShow = false;
			},
			left() {
				uni.navigateTo({
					url: '../workingMonitor/workingMonitor'
				})
			},
			clickType() { // 导航栏左侧点击事件
				this.typeShow = !this.typeShow;
			},
			selectType(val) { // 下拉选择	
				this.typeShow = false;
				if (val == undefined) {
					this.typeCon = '全部';
					this.typeCode = '';
				} else {
					let code = this.equiTypeList.filter(item => {
						if (item.code == val.code) {
							return item
						}
					})
					this.typeCon = code[0].title;
					this.typeCode = code[0].code;
				}
				this.pageNo = 1;
				this.equipmentAction(this.pageNo, true);
			},
			onSearchChange(e) { // 搜索框搜索
				if (this.searchTimer) {
					clearTimeout(this.searchTimer)
					this.searchTimer = null;
				}
				this.searchTimer = setTimeout(() => {
					this.typeShow = false;
					this.pageNo = 1;
					this.equipmentAction(this.pageNo, true)
				}, 500);
			},
			equipmentAction(pageNo, isFresh) {
				let data = {
					pageNo: pageNo,
					pageSize: this.pageSize,
					equipType: this.typeCode ? this.typeCode : '',
					name: this.searchInputCon
				}
				if (isFresh) {
					_this.equiqList = [];
					this.preSelectedValues = []; //清空前一次选择
				}
				getMontiorList(data, this);
			},
			checkboxChange(e) { // 复选框
				this.selectChange(e.detail.value);
				this.typeShow = false;
			},
			monitorSubmit() { // 点击确定按钮
				this.loadingSubmit = true;
				getAction('/iot/operate/save', {
					ids: this.checkList.join(',')
				}).then(res => {
					if (res.code * 1 === 200) {
						this.loadingSubmit = false;
						uni.navigateTo({
							url: 'index'
						})
					} else {

					}
				})
			},
			selectChange(currentSelected) {
				let delArray = this.diffDel(this.preSelectedValues, currentSelected)
				this.preSelectedValues = currentSelected;
				if (delArray.length) {
					this.delIds(delArray);
				} else {
					this.addSelectedIds(currentSelected);
				}
			},
			delIds(ids) {
				_this = this;
				let idsStr = "," + ids.join(",") + ",";
				this.checkList = this.checkList.filter((item, index, array) => {
					return idsStr.indexOf("," + item + ",") === -1;
				});
			},
			addSelectedIds(ids) {
				_this = this;
				if (ids) {
					ids.forEach((item, index) => {
						_this.addSelectedId(item);
					});
				}
			},
			addSelectedId(id) {
				let isNotHas = true;
				this.checkList.forEach((item, index) => {
					if (item === id) {
						isNotHas = false;
						return;
					}
				});
				if (isNotHas) {
					this.checkList.push(id);
				}
			},
			diffDel(pre, cur) {
				if (pre.length <= cur.length) {
					return [];
				}
				let curIdsStr = "," + cur.join(",") + ",";
				return pre.filter((item, idex, array) => {
					return curIdsStr.indexOf(item) == -1;
				});
			},
		},
		onLoad() {
			_this = this;
			this.equipmentAction(this.pageNo, true);
			getEquiType({
				pcode: 'A03'
			}, _this);
		},
		onPullDownRefresh() { //下拉
			let _this = this
			_this.typeShow = false;
			_this.pageNo = 1;
			_this.equipmentAction(_this.pageNo, true);
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		// 上拉加载
		onReachBottom() {
			_this = this;
			_this.typeShow = false;
			_this.pageNo++;
			if (_this.status != 'more') {
				return false;
			}
			_this.status = 'loading'
			uni.showNavigationBarLoading(); // 显示加载动画
			_this.equipmentAction(_this.pageNo, false)
		}
	}
</script>

<style lang="less" scoped>
.viewWrape {
	padding-bottom: 50px;
}
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

.equip-list { //列表
	display: block;
	font-size: 24rpx;
	background-color: #FFFFFF;
	border-radius: 10rpx;
	margin: 30rpx;
	overflow: hidden;
	checkbox-group {
		width: auto;
		checkbox {
			transform: scale(0.6);
			margin-left: -5px;
		}
	}
	.equip-item {
		display: block;
		position: relative;
		padding: 0 30rpx;
		line-height: 1;
		&.uni-list-hover {
			background-color: #F1F1F1;
		}
		&:not(:last-child) .con {
			border-bottom: 1px solid rgba(224, 224, 224, 0.5);
		}
		.con {
			display: flex;
			align-items: center;
			color: #232323;
			padding: 24rpx 0;
		}
		image {
			position: absolute;
			top: 50%;
			left: 30rpx;
			transform: translateY(-50%);
			width: 30rpx;
			height: 30rpx;
		}
	}
}

.setHeight {
	width: 100%;
	height: 128rpx;
}
.sureBtn {
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
	z-index: 99;
}</style>
