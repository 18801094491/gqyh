<template>
	<view>
		<uni-list>
			<uni-list-item :show-arrow="false" title="组织" :note="personInfoObj.orgName"/>
			<uni-list-item :show-arrow="false" title="职务" :note="personInfoObj.post"/>
			<uni-list-item :show-arrow="false" title="姓名" :note="personInfoObj.realname"/>
			<uni-list-item :show-arrow="false" title="工号" :note="personInfoObj.workNo"/>
			<uni-list-item :show-arrow="false" title="电话" :note="personInfoObj.phone" @tap="go(personInfoObj.phone)"/>
			<uni-list-item :show-arrow="false" title="邮箱" :note="personInfoObj.email"/>
		</uni-list>
	</view>
</template>

<script>
	import uniList from "../../../components/uni-list/uni-list.vue"
	import uniListItem from "../../../components/uni-list-item/uni-list-item.vue"
	
	var _this;
	export default{
		data(){
			return {
				personInfoObj:{}
			}
		},
		components:{
			uniList,
			uniListItem
		},
		methods:{
			go(phone){
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
			},
		},
		onLoad(option){
			_this = this;
			_this.personInfoObj = JSON.parse(decodeURIComponent(option.obj));
			uni.setNavigationBarTitle({
				title:_this.personInfoObj.realname
			});
		}
	}
</script>

<style lang="less">
	/deep/ .uni-list-item__content-title{
		color: #999;
		font-size: 11px;
	}
	/deep/ .uni-list-item__content-note{
		color: #3b4144;
		padding-left: 10px;
		font-size: 13px;
	}
</style>
