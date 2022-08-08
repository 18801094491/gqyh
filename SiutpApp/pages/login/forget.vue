<template>
	<view class="main">
		<view class="wrapper">
			<view class="text">手机号码</view>
			<view class="phoneWrapper">
				<text class="phoneText">+86 </text>
				<input class="phoneInput" type="number" v-model="phoneNum" maxlength="11" placeholder="手机号">
			</view>			
		</view>
		<view class="btn">
			<button type="primary" class="smsBtn" @tap="getSms()" v-if="!isShow">获取验证码</button>
			<button type="primary" class="smsBtn" v-else disabled>{{countDown}}秒后重发验证码</button>
		</view>
	</view>
</template>

<script>
	import { findPsd } from '../../api/api_login.js'
	
	export default{
		data(){
			return {
				phoneNum:'', // 手机号码
				isShow:false, // 是否显示获取验证码按钮
				countDown:59, // 倒计时
				intervalTimer:null, // 定时器
			}
		},
		methods:{
		// 检验手机号
			mobile2(val){
			                val = val.replace(/\s*/g, "");
			                var result = [];
			                for(var i = 0; i < val.length; i++){
			                    if (i==3 || i==7){
			                        result.push(" " + val.charAt(i));
			                    }else{
			                        result.push(val.charAt(i));
			                    }
			                }
							val = result.join("");
			                return val
			          	},
			// 手机验证
			getSms(){
				if(this.phoneNum){
					findPsd({mobile:this.phoneNum},this);
				}else{
					uni.showToast({
						icon:'none',
						title:'请输入手机号'
					})
				}				
			}
		}
	
	}
</script>
<style lang="less">
	@import url('../../common/app.less');
</style>

<style lang="less" scoped>
	.btn /deep/ uni-button{
		line-height: 2.2;
		border-radius: 20px;
	}
	.main{
		background: #FFFFFF;
		padding: 30px 20px;
		.wrapper{
			height: 80px;
			border-bottom: 1px solid #eee;
			.text{
				line-height: 40px;
			}
			.phoneWrapper{
				display: flex;
				justify-content: flex-start;
				padding-bottom: 10px;
				.phoneText{
					padding-right: 20px;
					border-right: 1px solid #ccc;
				}
				.phoneInput{
					padding-left: 10px;
				}
			}
		}		
		.btn{
			margin-top: 30px;			
		}
	}
	.smsBtn {
		background-color: #3D71AD !important;
	}
</style>
