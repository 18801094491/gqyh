<template>
	
	<view class="zaiui-login-content">
		<view class="wrapper">
			<view class="text">验证码已发送至手机：</view>
			<view class="phone">+86 {{phoneNum}}</view>			
		</view>
		<view style="padding: 0 20px;">请输入验证码</view>
		<view class="content">
			<view class="zaiui-flex-view checking-view" @tap="showNumLockTap">
				<view class="flex-sub text-center">{{codeKey[0]}}</view>
				<view class="flex-sub text-center">{{codeKey[1]}}</view>
				<view class="flex-sub text-center">{{codeKey[2]}}</view>
				<view class="flex-sub text-center">{{codeKey[3]}}</view>
			</view>
			<view class="introduce">
				<text class="tap" @tap="checking" v-if="!state">重新获取验证码</text>
				<text v-else>{{currentTime}}s 后可重新获取验证码</text>
			</view>
		</view>

		
		<!--数字键盘-->
		<view class="num-lock-view" :class="NumLock?'show':''">
			<view class="num-lock-head">
				<view class="title" v-if="codeBak == ''">安全数字键盘</view>
				<view class="code-bak-view" v-else>
					<!-- <text class="code-text" @tap="codeBakTap">使用: {{codeBak}} </text> -->
					<text class="cuIcon-close close-icon" @tap="codeBakCloseTap"/>
				</view>
				<text class="close" @tap="closeNumLockTap">关闭</text>
			</view>
			<view class="key-grid-list">
				<block v-for="(item,index) in 9" :key="index">
					<view class="key-item" @tap="codeKeyTap(item)">
						<text class="num">{{item}}</text>
					</view>
				</block>
				<view class="key-item">
					<text class="num"/>
				</view>
				<view class="key-item" @tap="codeKeyTap(0)">
					<text class="num">0</text>
				</view>
				<view class="key-item" @tap="codeKeyDelTap">
					<text class="cuIcon-close1 close"/>
				</view>
			</view>
		</view>
		
	</view>
</template>

<script>
	import { checkCode,findPsd } from '../../api/api_login.js'
	
	var _self;
	export default{
		data(){
			return {
				phoneNum:'', // 电话号码
				smsNum:'',
				isFocus:false, // 是否获取焦点 
				count:59, // 倒计时
				interval:null, // 定时器
				
				state: false,		//是否开启倒计时
				totalTime: 60,		//总时间，单位秒
				recordingTime: 0, 	//记录时间变量
				currentTime: 0, 	//显示时间变量
				codeKey: [], // 验证码
				NumLock: false, // 数字键盘
				codeBak: '1234', // 数字键盘code
			}
		},
		onShow() {
			// #ifdef APP-PLUS
				uni.getClipboardData({
					success: res => {
						console.log(res,'res')
						this.codeBak = res.data;
					}
				});
			// #endif
		},
		methods:{
			checking() {
				//把显示时间设为总时间
				this.currentTime = this.totalTime;
				//开始倒计时
				this.state = true;
				//执行倒计时
				this.checkingTime();
				findPsd({mobile:this.phoneNum},this)
			},
			checkingTime(){
				let that = this;
				//判断是否开启
				if(this.state) {
					//判断显示时间是否已到0，判断记录时间是否已到总时间
					if(this.currentTime > 0 && this.recordingTime <= this.totalTime){
						//记录时间增加 1
						this.recordingTime = this.recordingTime + 1;
						//显示时间，用总时间 - 记录时间
						this.currentTime = this.totalTime - this.recordingTime;
						//1秒钟后，再次执行本方法
						setTimeout(() => { 	
							that.checkingTime();
						}, 1000)
					} else {
						//时间已完成，还原相关变量
						this.state = false;		//关闭倒计时
						this.recordingTime = 0;	//记录时间为0
						this.currentTime = this.totalTime;	//显示时间为总时间
					}
				} else {
					//倒计时未开启，初始化默认变量
					this.state = false;
					this.recordingTime = 0;
					this.currentTime = this.totalTime;
				}
			},
			showNumLockTap() {
				this.NumLock = true;
			},
			codeKeyTap (index) {
				if(this.codeKey.length < 4) {
					this.codeKey.push(index);
				}
				if(this.codeKey.length == 4){
					checkCode({code:this.codeKey.join(''),mobile:this.phoneNum},this);
				}
			},
			codeKeyDelTap() {
				this.codeKey.pop();
			},
			closeNumLockTap() {
				this.NumLock = false;
			},
			codeBakTap() {
				let str = this.codeBak;
				let arr = [];
				for (let i = 0; i < str.length; i++) { 
					let nstr = str.slice(i,i+1);
					arr.push(nstr);
				}
				this.codeKey = arr;
				this.codeBak = '';
			},
			codeBakCloseTap() {
				this.codeBak = '';
			},
			
			
			onFocus(e) {
				this.isFocus=true;
			},
			setValue(event) {//文本框输入事件
				var self = this;
				var value = event.target.value;
				setTimeout(function(){self.smsNum = value;},5);
				if(value.length==4){
					checkCode({code:value,mobile:self.phoneNum},this);
				}
			},
		},
		onLoad(option) {
			_self = this;
			_self.phoneNum = option.phone;
			_self.currentTime = _self.totalTime;
			_self.state = true;
			_self.checkingTime();
		}
	}
</script>
<style lang="less">
	@import url('../../common/app.less');
	@import url('../../common/icon.css');
</style>
<style lang="less" scoped>		
	.zaiui-login-content{
		height: 100%;
		.wrapper{
			padding: 30px 20px;
			// height: 80px;
			.text{
				line-height: 50px;
				font-size: 22px;
				font-weight: 700;
			}
			.phone{
				font-weight: 700;
				font-size: 16px;
			}
		}
		.phoneWrapper{
			display: flex;
			justify-content: flex-start;
			padding-bottom: 10px;
			margin-top: 20px;
			.phoneText{
				padding-right: 20px;
				border-right: 1px solid #ccc;
			}
			.phoneInput{
				// margin-left: 20px;
				// text-align: center;
				border-bottom: 1px solid #ccc;
				letter-spacing: 5px;
				padding: 5px 10px;
			}
		}	
		.smsAgain{
			color: #ccc;
			font-size: 12px;
		}
		.hidden_ipt{
			width: 0;
			height: 0;
		}
	}	
</style>
