<template>
	<view>
		<view class="searchbox">
			<view class="search">
				<input type="text" placeholder="请输入搜索内容" @input="onSearchChange" v-model="value">
				<image v-if="is_share" src="/static/cancel.png" mode="" @tap="searchCancel"/>
			</view>
		</view>
		<view class="mail-list">
			<view class="mail-item" v-for="(item, index) in is_share ? searchList : mailDetailListUser" 
				:key="index" hover-class="uni-list-hover" :data-item="item" @tap="showDetailsPopup">
				<view class="strCharAt">{{ strCharAt(item.realname)}}</view>
				<view class="con uni-fs24">
					<view >{{item.realname}}<block v-if="item.post">（{{item.post}}）</block></view>
					<uni-icons :size="14" class="uni-icon-wrapper" color="#ABAEB2" type="arrowright" />
				</view>
			</view>
		</view>
		<view class="details-popup" v-if="personInfo">
			<view class="pop-bgcolor"></view>
			<view class="details">
				<view class="strCharAt uni-fl-cn">{{ strCharAt(personInfo.realname)}}</view>
				<view class="realname uni-fs28">{{personInfo.realname}}</view>
				<view class="person-info uni-fs24">
					<view class="item"><text>组织</text><text>{{personInfo.orgName}}</text></view>
					<view class="item"><text>职务</text><text>{{personInfo.post || ''}}</text></view>
					<view class="item"><text>姓名</text><text>{{personInfo.realname}}</text></view>
					<view class="item"><text>工号</text><text>{{personInfo.workNo}}</text></view>
					<view class="item"><text>电话</text><text  @tap="go(personInfo.phone)">{{personInfo.phone}}</text></view>
					<view class="item"><text>邮箱</text><text>{{personInfo.email || ''}}</text></view>
				</view>
				<view class="close-view">
					<image class="line" src="/static/line-y.png" mode="scaleToFill" />
					<image class="close" src="/static/close.png" mode="scaleToFill" @tap="personInfo = null"/>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniIcons from '../../../components/uni-icons/uni-icons.vue'
	import { getPhoneList } from '../../../api/api_phone.js'
	export default{
		data(){
			return {
				value: '', //搜索词
				searchList: [], //搜索结果列表
				is_share: false, //当前是否在搜索进行中
				personInfo: null, //弹层：个人信息
				mailDetailList: [],
				mailDetailListUser: [],
				searchTimer: null,
				mailObj:{},
			}
		},
		components:{
			uniIcons
		},
		methods:{
			strCharAt(value) {
				return value ? value.charAt(0) : ''
			},
			onSearchChange() {
				let searchList = [];
				this.mailDetailListUser.forEach(item => { //取出list中满足条件的item
					if (item.realname.includes(this.value)) {
						searchList.push(item);
					}
					this.is_share = true
				})
				if (this.searchTimer) {
					clearTimeout(this.searchTimer);
					this.searchTimer = null;
				}
				if (!this.value) {
					this.searchCancel();
					return;
				}
				this.searchTimer = setTimeout(() => { //节流
					this.searchList = searchList
				}, 300);
			},
			searchCancel() { //退出搜索
				this.value = '';
				this.searchList = this.mailDetailListUser;
				this.is_share = false;
			},
			showDetailsPopup(e) {
				this.personInfo = e.currentTarget.dataset.item
			},
			go(phone) {
			 	uni.makePhoneCall({		
					phoneNumber: phone, 				
					// 成功回调
					success: (res) => {
						console.log('调用成功!')
					},				
					// 失败回调
					fail: (res) => {
						console.log('调用失败!')
					}					
				});
			}
		},
		onLoad(option) {
			this.mailObj = JSON.parse(decodeURIComponent(option.item));
			console.log(this.mailObj,'_this.mailObj')
			uni.setNavigationBarTitle({
				title: this.mailObj.departName
			});
			let data = { orgCode: this.mailObj.orgCode };
			getPhoneList(data, this,'phoneDetail');
		}
	}
</script>

<style lang="less" scoped>
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
		input {
			position: relative;
			width: 690rpx;
			height: 80rpx;
			font-size: 28rpx;
			background-color: #E0E0E0;
			border-radius: 40rpx;
			padding: 0 86rpx;
			margin: 0 30rpx;
			box-sizing: border-box;
			&::after {
				content: '';
				position: absolute;
				top: 50%;
				left: 20rpx;
				width: 54rpx;
				height: 54rpx;
				transform: translateY(-46%);
				background-size: 100% 100%;
				background-repeat: no-repeat;
				background-image: url(/static/search.png);
			}
		}
		image {
			position: absolute;
			top: 50%;
			right: 60rpx;
			width: 28rpx;
			height: 28rpx;
			transform: translateY(-50%);
		}
	}
}

.mail-list { //列表
	display: block;
	font-size: 24rpx;
	background-color: #FFFFFF;
	border-radius: 10rpx;
	margin: 30rpx 30rpx 88rpx;
	overflow: hidden;
	.mail-item {
		position: relative;
		padding: 0 30rpx 0 106rpx;
		line-height: 1;
		&.uni-list-hover {
			background-color: #F1F1F1;
		}
		&:not(:last-child) .con {
			border-bottom: 1px solid rgba(224, 224, 224, 0.5);
		}
		.con {
			position: relative;
			color: #232323;
			padding: 36rpx 0;
			.uni-icon-wrapper {
				position: absolute;
				top: 50%;
				right: -6rpx;
				transform: translateY(-50%);
			}
		}
		.strCharAt {
			display: flex;
			align-items: center;
			justify-content: center;
			position: absolute;
			top: 50%;
			left: 30rpx;
			width: 58rpx;
			height: 58rpx;
			transform: translateY(-50%);
			color: #FFFFFF;
			background-color: #3D71AD;
			border-radius: 50%;
		}
	}
}

.details-popup { //个人信息弹层
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, .3);
	.details {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%,-55%);
		width: 630rpx;
		height: 628rpx;
		background: #FFFFFF;
		border-radius: 20rpx;
	}
	.strCharAt {
		width: 132rpx;
		height: 132rpx;
		color: #FFFFFF;
		font-size: 64rpx;
		background: #3D71AD;
		box-shadow: 0 1rpx 24rpx 0 rgba(61, 113, 173, 0.31);
		border-radius: 50%;
		margin: -66rpx auto 0;
	}
	.realname {
		line-height: 1;
		text-align: center;
		margin-top: 20rpx;
	}
	.person-info {
		padding: 100rpx 50rpx 60rpx;
		.item {
			display: flex;
			justify-content: space-between;
			line-height: 1;
			&:not(:last-child) {
				margin-bottom: 42rpx;
			}
			text:first-child {
				color: #999999;
			}
		}
	}
	.close-view {
		position: relative;
		left: 50%;
		transform: translateX(-50%);
		image {
			display: block;
			margin: 0 auto;
		}
		.line {
			width: 1px;
			height: 50rpx;
		}
		.close {
			width: 52rpx;
			height: 52rpx;
		}
	}
}
</style>
