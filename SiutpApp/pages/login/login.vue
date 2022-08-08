<template>
	<view class="login">
		<image src="/static/logo.png" class="logo"></image>
		<view class="des">
			<view class="tit">欢迎登陆</view>
			<view class="con">木龙河流域水环境管理平台</view>
		</view>
		<view class="userInfo">
			<input class="u-input userName" placeholder="请输入用户名" v-model="userName" />
			<input class="u-input passWord" placeholder="请输入密码" v-model="passWord" password type="text" />
			<view class="forget" @tap="goForgetPsd">忘记密码?</view>
		</view>
		<button type="primary"  :disabled="userName && passWord ? false : true" class="loginBtn" @tap="loginIn">登录</button>
	</view>
</template>

<script>
	import {pwdLogin} from '../../api/api_login.js'
	export default {
		data() {
			return {		
				userName:'', // 用户名
				passWord:''// 密码
			}
		},
		methods: {
			//登录事件
			loginIn(){
				if(this.userName == ''){
					uni.showToast({
						title:'用户名不能为空',
						duration: 2000,
						icon: 'none'
					})
					return
				}
				if(this.passWord == ''){
					uni.showToast({
						title:'密码不能为空',
						duration: 2000,
						icon: 'none'
					})
					return
				}
				let data = {
					username: this.userName,
					password: this.passWord
				}
				pwdLogin(data);
			},
			//忘记密码事件
			goForgetPsd(){
				uni.navigateTo({
					url:'./forget'
				})
			}
			
		}
	}
</script>

<style>
.login {
    width: 100%;
    height: 100%;
    overflow: hidden;
}
.logo {
	display: block;
	/* width: 178rpx;
	height: 178rpx; */
	width: 380rpx;
	height: 90rpx;
	border-radius: 50%;
	margin: 124rpx auto 100rpx;
}
.des {
	text-align: center;
	line-height: 50rpx;
}
.tit {
	color: #424252;
	font-size: 34rpx;	
	font-family: Hiragino Sans GB;
	font-weight: bold;
}
.con {
	color: #999;
	font-size: 24rpx;
}
.userInfo {
	padding: 36rpx 76rpx 86rpx;
}
.u-input {
	position: relative;
	/* color: #C5C5C5; */
	font-size: 28rpx;
	padding: 48rpx 30rpx 20rpx 70rpx;
	border-bottom: 1px solid #E0E0E0;
}
.u-input.input-placeholder {
	color: #C5C5C5;
}
.u-input::after {
	content: '';
	position: absolute;
	top: 58rpx;
	left: 20rpx;
	width: 30rpx;
	height: 30rpx;
	background-repeat: no-repeat;
	background-size: 100% 100%;
}
.userName::after {
	background-image: url(/static/icon1.png);
}
.passWord::after {
	background-image: url(/static/icon2.png);
}
.forget {
	line-height: 1;
	text-align: right;
	color: #C5C5C5;
	font-size: 28rpx;
	font-weight: normal;
	font-family: Hiragino Sans GB;
	margin-top: 26rpx;
}
.loginBtn {
	width: 600rpx;
	height: 88rpx;
	color: #FFFFFF;
	font-size: 34rpx;
	background-color: #3D71AD !important;
	border-radius: 44rpx;
}
</style>
