<template>
	<view class="main">
		<view class="wrapper">
			<view class="text">请设置密码</view>		
		</view>
		<view style="margin-top: 20px;">密码</view>
		<view class="phoneWrapper">
			<form @submit="formSubmit">
				<input class="uni-input userInput" placeholder="密码由8位或以上数字、字母组成" name="passWord1" password  type="text"/>
				<input class="uni-input psdInput" placeholder="请确认密码" name="passWord2" password type="text"/>
				<view class="btn">
					<button type="primary" class="smsBtn" formType="submit">设置</button>
				</view>
			</form>
		</view>
		
	</view>
</template>

<script>
	import { resetPsd } from '../../api/api_login.js'
	
	var _self;
	var  graceChecker = require("../../common/graceChecker.js");
	export default{
		data(){
			return {
				passWord1:'', // 密码
				passWord2:'', // 确认密码
				phone:'' // 电话号码
			}
		},
		methods:{
			// 设置密码
			formSubmit(e){
				var formData = e.detail.value;
				let psd1 = formData.passWord1;
				let psd2 = formData.passWord2;
				let isTrue ,msg2;
				if(psd1 && psd2) {
					if(psd2!==psd1){
						isTrue = true;		
						msg2 = "两次密码不一样";
					}else{
						isTrue = false;
						msg2 = "密码由8位或以上数字、字母组成!";
					}
				}
				// 定义表单的规则
				var rule=[{name:"passWord1",checkType:"reg",checkRule:"^(?=.*[a-zA-Z])(?=.*\\d).{8,}$",errorMsg:!psd1?"密码不能为空":"密码由8位或以上数字、字母组成!"},
					{name:"passWord2", checkType : "reg", checkRule:"^(?=.*[a-zA-Z])(?=.*\\d).{8,}$",  errorMsg:!psd2?"确认密码不能为空":msg2}];
				//进行表单检查
				var checkRes = graceChecker.check(formData,rule);
				if(checkRes){		
					if(psd2 !== psd1){
						uni.showToast({title:"两次密码不一样！",icon:"none"});
					}else{		
						let data = {
							mobile : this.phone,
							password : psd1,
							confirmPassword : psd2							
						}
						resetPsd(data,this)
						uni.showToast({title:"验证通过！",icon:"none"});
					}
				}else{
					uni.showToast({title:graceChecker.error,icon:"none"});
				}
			},
		},
		onLoad(option) {
			_self = this;
			_self.phone = option.phone;
		}
	}
</script>

<style lang="less" scoped>
	.btn /deep/ uni-button{
		line-height: 2.2;
		border-radius: 20px;
	}
	.main{
		background: #FFFFFF;
		padding: 30px 20px;
		.wrapper{
			// height: 80px;
			.text{
				line-height: 50px;
				font-size: 22px;
				font-weight: 700;
			}
		}
		.phoneWrapper{
			padding-bottom: 10px;
			margin-top: 20px;	
			.btn{
				margin-top: 20px;
			}
		}	
	}	
	.userInput{
		background-color: #FFFFFF;
		border-bottom: 1px solid  rgba(0,0,0,.2);
		margin-top: 20px;
	}
	.psdInput{
		background-color: #FFFFFF;
		border-bottom: 1px solid  rgba(0,0,0,.2);
		margin-top: 10px;
	}
	.smsBtn {
		background-color: #3D71AD !important;
	}
</style>
