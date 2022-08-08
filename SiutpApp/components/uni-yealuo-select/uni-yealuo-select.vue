<template>
	<view class="yealuo-select1" >
		<view class="yealuo-background" @tap="isShow=false" v-if="isShow"></view>
		<view class="pick" v-if="!isShow || valueType!=='equ'" @tap='isShow=true'>{{theTitle}}</view>
		<view :class="'yealuo-select2 '+ valueType" v-if="isShow">
			<view class="yealuo-con" @tap='isShow=true' v-if="valueType==='equ'">
				<input class="sele-input" :focus="focus" :disabled="theDisabled" v-model="theTitle" @input="theInput" @focus="theFocus" @blur="theBlur" />
				<slot name='right' v-if="selectIco">
					<image class="icon" src="/static/search.png" mode="widthFix" />
				</slot>
			</view>
			<view class="yealuo-select3" v-show="show">
				<view class="data">
					<radio-group @change="selectCheckbox">
						<view class="select-item" :class="'item-'+overflow" v-for="(item, index) in binData" :key="index" >
							<label class="item-text" :class="{active: theTitle==item.title}" @touchstart="touchStart" @touchend="touchEnd">
								<radio name="name1" style="display: none;" checked v-if="theTitle==item.title" :value="item.title+'|'+item.code"></radio>
								<radio name="name1" style="display: none;" v-else :value="item.title+'|'+item.code"></radio>
								{{item.title}}
							</label>
						</view>
					</radio-group>
				</view>
			</view>

		</view>
	</view>
</template>

<script>
	/**
	 * v1.0.0
	 * 最后修改: 2020.11.2
	 * 创建: 2020.9.30
	 * Author:yealuo.com
	 * contact:470797533@qq.com
	 */
	let fontUnit = 'upx'
	// #ifdef MP-WEIXIN
	fontUnit = 'rpx'
	// #endif
	export default {
		name: 'yealuoInputs',
		props: {
			placeholder: {
				type: String,
				default: ''
			},
			value: {
				type: String,
				default: ''
			},
			defaultData: {
				type: Object,
				default: null
			},
			checkType: {
				type: String,
				default: ''
			},
			valueType: {
				type: String,
				default: ''
			},
			itemKey: {
				type: String,
				default: ''
			},
			width: {
				type: String,
				default: '600'
			},
			disabled: {
				type: Boolean,
				default: false
			},
			selectStyle: {
				type: String,
				default: ''
			},
			overflow: {
				type: String,
				default: 'auto'
			},
			tags: {
				type: String,
				default: ''
			},
			
			binData:{
				type:Array,
				default: []
			},
			selectIco:{
				type: Boolean,
				default: false
			}
		},
		data() {
			return {
				focus: this.valueType==='equ',
				touchLabel: false,
				isShow: false,
				theTitle: this.value,
				theDisabled: this.disabled,
				is_selectCheck: false,
			}
		},
		watch: {
			// value(val){
			// 	this.theTitle = val;
			// },
			
		},
		computed: {
			show(){
				return this.isShow && this.binData.length
			}
			
		},
		created() {
			// console.log(this.defaultData)
			if (this.defaultData) {
				this.selectCheckbox(this.defaultData)
			}
		},
		methods: {
			//获取焦点
			theFocus(e){
				this.touchLabel = false;
			},
			//失去焦点
			theBlur(e){
				// console.log('失焦')
				if (this.is_selectCheck) {
					this.is_selectCheck = false;
					return;
				}
				if (this.touchLabel) {
					this.touchLabel = false;
					return;
				}
				let flag = false;
				this.binData.forEach(item => {
					if (this.theTitle == item.title) flag = true;
				})
				if (!flag) {
					this.theTitle = '';
					setTimeout(() => { this.$emit('equSearch', '') })
				}
			},
			//获取输入值
			theInput(e) {
				// console.log('输入')
				this.is_selectCheck = false;
				this.$emit('equSearch', e.detail.value)
			},

			touchStart() {
				console.log('touchStart - label')
				this.focus = false;
				this.touchLabel = true;
			},
			touchEnd() {
				console.log('touchEnd - label')
				this.focus = true;
				this.touchLabel = false;
			},

			//下拉选中
			selectCheckbox(e){
				this.focus = false;
				var val=e.target.value;
				var str=val;
				var tit='';
				if(typeof(str)!="string"){
					str="";
					for(var i=0;i<val.length;i++){
						var vt=val[i].split("|");
						str+=i>0?","+vt[0]:vt[0];
					}
				}
				else{
					setTimeout(() => { this.isShow = false })
					tit=str.split("|")[0];
					val=str.split("|")[1];
				}
				this.$emit('getBackVal',val, this.valueType)
				this.is_selectCheck = true;
				this.theTitle = tit;
			}
		},
	}
</script>

<style lang="scss" scoped>
.yealuo-select1{
	width: 100%;
	height: 100%;
	position: relative;
	.yealuo-background{position: fixed;top:0;left:0;right:0;bottom:0;z-index: 1;}
	.pick {
		width: 100%;
		height: 100%;
	}
	.yealuo-con{display: flex;align-items: center;justify-content: center;margin: 20rpx;
		.icon{
			position: absolute;
			top: 24rpx;
			right: 42rpx;
			width: 54rpx;
			height: 54rpx;
		}
		input{
			width: 100%;
			height: 60rpx;
			font-size: 24rpx;
			background: #E0E0E0;
			padding-left: 22rpx;
			border-radius: 30rpx;
		}
	}
	.yealuo-select2 {
		position: absolute;
		z-index: 999;
		width: 100%;
		background: #fff;
		border: 1px solid rgba(187, 187, 187, 0.5);
		&.protype {
			top: 100%;
		}
	}
	
	.yealuo-select3 {
		// position: absolute;
		// z-index: 999;
		width: 100%;
		line-height: 60rpx;
		background: #fff;
		border-radius: 10rpx;
		.data{
			max-height: 543upx;
			// padding-bottom: 10rpx;
			overflow: auto;
			.select-item {width: 100%;color:#232323;
				.item-text{ width:100%; display:block; padding: 0 20rpx;box-sizing: border-box;}
				.active{
					
					background: #F0F0F0;
				}
			}
			.item-auto{overflow: auto;
				.item-text{width: max-content;}
			}
			.item-hide .item-text{
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
			}
		}
		.item-close {
			padding: 20upx;
			text-align: center;
			font-size: 32upx;
			border-top: 1px solid #f3f3f4;
			color:#8F8F94;
		}
	}
}

// 
radio-group label, checkbox-group label{
	padding-right: 0 !important;
}
</style>
