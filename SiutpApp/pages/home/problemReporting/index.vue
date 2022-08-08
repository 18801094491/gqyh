<template>
	<view class='problemReporting-page'>
		<view class="page-top">
			<view class="history">
				<view class="pick" @tap="goUrl('history/index')">
					<image src="/static/problemReporting-1.png" mode="widthFix" />
					<text>历史问题</text>
				</view>
			</view>
			<view class="item">
				<view class="title">问题标题：</view>
				<view class="con flex-al-c">
					<input type="text" v-model="proTitle" maxlength="50">
				</view>
			</view>
			<view class="item">
				<view class="title">问题类型：</view>
				<view class="con">
					<uni-yealuo-select :binData="proTypeList" :disabled="true" overflow="hide" valueType="protype" @getBackVal="getBackVal" :selectIco="true" />
				</view>
			</view>
			<view class="item">
				<view class="title">关联设备：</view>
				<view class="con">
					<uni-yealuo-select :binData="equipmentList" overflow="hide" valueType="equ" @getBackVal="getBackVal" @equSearch="onEquSearchChange" :selectIco="true" />
				</view>
			</view>
			<view class="item">
				<view class="title">问题等级：</view>
				<view class="con" >
					<uni-yealuo-select :binData="proLevelList" :disabled="true" overflow="hide" valueType="level" @getBackVal="getBackVal" :selectIco="true" />
				</view>
			</view>
			<view class="item">
				<view class="title">问题地点：</view>
				<view class="locStyle" @tap="onSetProLocationChange">
					<image src="/static/loc-icon.png" mode="widthFix" />
					<view>{{lnglat.longitude ? '修改位置' : '获取位置信息'}}</view>
				</view>
			</view>
		</view>
		<view class="page-bottom">
			<view class="title">问题描述</view>
			<textarea v-model="proDes" maxlength="2000" placeholder="请输入问题描述" placeholder-class="ph-textarea"></textarea>
			<view class="upload">
				<view class="pick" v-for="(item, index) in fileUrlList" :key="index" @tap="onPreviewMediaChange(index, item)">
					<template v-if="videoType.includes(item.slice(-3))">
						<video class="pro-video" :src="item" :controls="false" :show-center-play-btn="false">
							<cover-image class="icon-play" src="/static/icon-play.png" mode="" @tap="onPreviewMediaChange(index)" />
							<cover-image class="icon-del v-del" src="/static/filedel.png" mode="widthFix" @tap="onDelImageChange(index)" />
						</video>
					</template>
					<template v-else>
						<image class="pro-img" :src="item" mode="aspectFill" />
						<image class="icon-del" src="/static/filedel.png" mode="widthFix" @tap="onDelImageChange(index)" />
					</template>
				</view>
				<image class="icon-img" src="/static/upImage.png" mode="widthFix" @tap="chooseMedia" v-if="fileUrlList.length<8"/>
			</view>
		</view>

		<view v-if="mediaFiles.length">
			<swiper class="swiper-file" :current="mediaFileIndex" @tap="hideSwiper">
				<swiper-item class="item" v-for="(item, index) in mediaFiles" :key="index">
					<view class="index">{{index+1}}/{{mediaFiles.length}}</view>
					<image v-if="item.type==='image'" :src="item.url" mode="widthFix"/>
					<video v-else :src="item.url" :enable-progress-gesture="false"></video>
				</swiper-item>
			</swiper>
		</view>

		<view class="setNullHeight"></view>
		<view class="page-btns">
			<view class="btns">
				<view class="btn" @tap="goBack">取消</view>
				<view class="btn" @tap="onWorkListAdd"><text>提交</text></view>
			</view>
		</view>
	</view>
</template>

<script>
	import cache from '@/common/cache.js'
	import {baseUrl} from '../../../common/request.js'
	import uniYealuoSelect from '../../../components/uni-yealuo-select/uni-yealuo-select.vue'
	
	import { 
		postAction,
		getProTypeData, 
		getEquipmentData,
		getProLevelData,
	} from '../../../api/api_problem.js'
	export default{
		data(){
			return {
				is_matterType: false,
				is_equipmentId: false,
				is_matterLevel: false,
				proTypeList: [], //问题类型列表
				initialEquipmentList: [], //关联设备列表
				equipmentList: [], //关联设备列表
				proLevelList: [], //问题等级列表
				fileUrlList: [], //
				lnglat: {},
				proTitle: '', //问题标题
				matterType: '', //问题类型
				equipmentId: '', //关联设备id
				matterLevel: '', //问题等级
				proDes: '', //问题描述
				fileList: [], //上传图片列表
				fileUrlIndex: 0, //当前上传文件索引
				addOk: [], //可以提交
				videoType: 'mp4,mov,m4v,3gp,avi,m3u8,webm',
				mediaFiles: [],
				mediaFileIndex: ''
			}
		},
		components: {
			uniYealuoSelect
		},
		methods:{
			onLoad() { //初始加载
				getProTypeData({}, this);
				getEquipmentData({}, this);
				getProLevelData({}, this);
			},

			onShow() {
				this.lnglat = uni.getStorageSync('pro-lnglat') || this.lnglat;
				console.log('onShow', this.lnglat)
				uni.removeStorageSync('pro-lnglat');
			},
			
			getBackVal(val, type) { //下拉选择
				console.log(val, type)
				if (type === 'protype') {
					this.is_matterType = false;
					this.matterType = val;
				} else if (type === 'equ') {
					this.is_equipmentId = false;
					this.equipmentId = val;
				} else if (type === 'level') {
					this.is_matterLevel = false; 
					this.matterLevel = val;
				}
			},

			onEquSearchChange(val) { //关联设备选择
				let searchList = [];
				this.initialEquipmentList.forEach(item => {
					if (item.title.includes(val)) {
						searchList.push({code: item.code, title: item.title});
					}
				})
				if (!val) {
					searchList = this.initialEquipmentList
				}
				this.equipmentList = searchList
			},
		
			onSetProLocationChange() { //设置问题定位
				uni.showLoading({
					title: '当前定位获取中',
					mask: true,
					success: res => {},
				})
				// #ifdef MP-WEIXIN
					this.getLocation();
				//#endif
				//#ifdef APP-PLUS
					this.appGetLocation();
				//#endif
			},

			getLocation() { //微信获取位置
				uni.getLocation({
					type: 'gcj02',
					isHighAccuracy: true,
					highAccuracyExpireTime: 3100,
					success: res => {
						console.log(res)
						uni.hideLoading()
						this.goUrl( `problemMap?lat=${this.lnglat.latitude || res.latitude}&lng=${this.lnglat.longitude || res.longitude}${this.lnglat.longitude ? '&type=edit' : ''}` );
					},
					fail(err) {
						uni.hideLoading()
						console.log('定位授权被拒绝')
						uni.showModal({
							title: '获取不到您的定位',
							content: '在设置页中开启定位授权，以获取当前所在位置',
							confirmText: '开启定位',
							confirmColor: '#3D71AD',
							success: res => {
								console.log(res)
								if (res.confirm) {
									uni.openSetting({success: res => {
										console.log(res)
									}})
								}
							}
						})
					}
				})
			},

			appGetLocation() { //app获取位置
				plus.geolocation.getCurrentPosition( res => {
					uni.hideLoading()
					// console.log('123123123')
					// plus.nativeUI.alert(JSON.stringify(res));
					this.goUrl( `appProblemMap?lat=${this.lnglat.latitude || res.coords.latitude}&lng=${this.lnglat.longitude || res.coords.longitude}${this.lnglat.longitude ? '&type=edit' : ''}` );
				},  err => {
					uni.hideLoading()
					// str = JSON.stringify(err);
					console.log( "获取定位位置信息失败：" + JSON.stringify(err) );
					// plus.nativeUI.alert( "获取定位位置信息失败, 请手动在应用设置中开启定位");
				});

			},

			chooseMedia() { //选择图片/视频
				// #ifdef MP-WEIXIN
					uni.chooseMedia({
						count: 8 - this.fileUrlList.length, 
						sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
						mediaType: ['image', 'video'], //文件类型
						sourceType: ['album', 'camera'], //图片视频来源
						success: res => {
							res.tempFiles.forEach(item => {
								this.fileUrlList.push(item.tempFilePath)
							})
							if ( this.fileUrlList.length >= 8 ) {
								this.fileUrlList.length = 8;
							}
							console.log(this.fileUrlList);
						}
					})
				//#endif
				//#ifdef APP-PLUS
					this.showActionSheet();
				//#endif
			},

			showActionSheet() { //app选择图片/视频
				uni.showActionSheet({
					itemList: ['相册', '拍照'],
					// itemList: ['相册', '拍照', '录像'],
					success: res => {
						console.log('选中了第' + (res.tapIndex + 1) + '个按钮');
						if (res.tapIndex === 0) { //相册
							plus.gallery.pick(res => {
								console.log('----------------------------')
								console.log(JSON.stringify(res));
								res.files.forEach(item => {
									this.fileUrlList.push(item)
								})
								if ( this.fileUrlList.length >= 8 ) {
									this.fileUrlList.length = 8;
								}
								console.log(this.fileUrlList);
							}, err => {
								console.log('取消图片选择')
							}, {filter: 'image', multiple: true, maximum: 8 - this.fileUrlList.length, permissionAlert: true})
							// }, {filter: 'none', multiple: true, maximum: 8 - this.fileUrlList.length, permissionAlert: true})
						} else if (res.tapIndex === 1) { //拍照
							let camera =plus.camera.getCamera();
							camera.captureImage(file => {
								plus.io.resolveLocalFileSystemURL(file, res => {
									console.log(res.fullPath)
									let fullPath = 'file:///' + res.fullPath;
									this.fileUrlList.push(fullPath)
									console.log(this.fileUrlList);
								}, err => {    
									console.log(e.message);
								});
							}, err => {
								console.log(err)
							}, {})
						} else if  (res.tapIndex === 2) {
							let camera =plus.camera.getCamera();
							camera.startVideoCapture(file => {
								// console.log(camera.supportedImageResolutions)
								plus.io.resolveLocalFileSystemURL(file, res => {
									console.log(res.fullPath)
									let fullPath = 'file:///' + res.fullPath;
									this.fileUrlList.push(fullPath)
									console.log(this.fileUrlList);
								}, err => {    
									console.log(e.message);
								});
							}, err => {
								console.log(err)
							}, {popover: '{top:"25%",left:"0px",bottom:"25%",right:"0px"}'})
						}
						return;
					},
					fail: res => {
						console.log(res.errMsg);
					}
				});
			},

			onPreviewMediaChange(index, item) { //图片/视频预览
				let mediaFiles = [];
				this.fileUrlList.forEach(item => {
					if (this.videoType.includes(item.slice(-3))) {
						mediaFiles.push({url: item, type: 'video'})
					} else {
						mediaFiles.push({url: item, type: 'image'})
					}
				})
				console.log(mediaFiles)
				
				//#ifdef APP-PLUS
					this.mediaFiles = mediaFiles;
					this.mediaFileIndex = index;
				//#endif
				// #ifdef MP-WEIXIN
					// uni.previewImage({
					// 	urls: this.fileUrlList,
					// 	current: item
					// })
					uni.previewMedia({
						sources: mediaFiles,
						current: index
					});
				//#endif
			},

			hideSwiper() {
				this.mediaFiles = []
			},

			onDelImageChange(i) { //删除图片/视频
				this.fileUrlList = this.fileUrlList.filter((item, index) => index != i)
				console.log(this.fileUrlList)
			},

			goBack() { //页面回退
				uni.navigateBack();
			},

			onWorkListAdd() { //提交
				// uni.showLoading({
				//   title: '',
				// })
				let params = {
					title: this.proTitle,
					matterType: this.matterType,
					equipmentId: this.equipmentId,
					matterLevel: this.matterLevel,
					matterLongitudeTencent: this.lnglat.longitude,
					matterLatitudeTencent: this.lnglat.latitude,
					description: this.proDes,
					fileList: [],
				} 
				this.fileList = []
				this.onFormValidationChange(params, () => {
					this.updateFile(this.fileUrlList, () => {
						params.fileList = this.fileList
						postAction('/worklist/workListMatter/app/add', params).then(res => {
							uni.hideLoading()
							this.onShowToast('提交成功', 1000, () => {
								setTimeout(() => { uni.navigateBack() }, 1000);
							} )
						}).catch(err => {
							uni.hideLoading()
						})
					})
				})
			},

			updateFile(fileUrlList, callBack) { //上传文件到服务器
				let flag = false;
				uni.showLoading({title: '上传中', mask: true})
				if (!fileUrlList[this.fileUrlIndex]) {
					this.fileUrlIndex = 0;
					uni.hideLoading();
					console.log('上传结束')
					setTimeout(() => { callBack() }, 100);
					return;
				}
				uni.uploadFile({
					url: baseUrl + '/sys/file/upload',
					filePath: fileUrlList[this.fileUrlIndex],
					name: 'file',
					header: {
						'Content-Type': 'multipart/form-data',
						'X-request-platform': 'app',
						'X-Access-Token': cache.get("Token")
					},
					success: res => {
						console.log(res)
						let data = JSON.parse(res.data)
						if ( data.code == '200' ) {
							this.fileList.push({ url: data.result.filePath })
							this.fileUrlIndex++;
							this.updateFile(fileUrlList, callBack);
						} else {
							uni.hideLoading();
							console.log(data.message)
							setTimeout(() => { this.onShowToast(data.message, 2000) }, 100)
						}
					},
					fail: res => {
						flag = false
						console.log('uploadFile - fail - 上传失败，请重新提交')
						uni.hideLoading();
						this.fileList = [];
						this.fileUrlIndex = 0;
						this.onShowToast('上传失败，请重新提交', 1000)
					}
				}).onProgressUpdate(res => {
					console.log('上传监听', JSON.stringify(res))
					uni.showLoading({
						title: `第${this.fileUrlIndex+1}张已上传${res.progress}%`,
						mask: true,
						success: () => { 
							if (res.progress == 100) {
								if (!flag) {
									flag = true;
									uni.hideLoading() 
								}
							}
						}
					})
				})
			},

			onFormValidationChange(params, callBack) {//字段验证
				let title = '';
				if (!params.title) {
					title = '请填写问题标题'
				} else if (!params.matterType) {
					title = '请选择问题类型'
				} else if (!params.equipmentId) {
					title = '请选择关联设备'
				} else if (!params.matterLevel) {
					title = '请选择问题等级'
				} else if (!params.matterLongitudeTencent) {
					title = '请选择问题地点'
				} else if (!params.description) {
					title = '请填写问题描述'
				}
				if (title) {
					this.onShowToast(title, 1000);
					return;
				}
				callBack();
			},

			onShowToast(title, duration, callBack) {
				uni.showToast({
					title: title,
					icon: 'none',
					mask: true,
					duration: duration || 1500,
					success: res => {
						callBack && callBack(res);
					}
				});
			},

			goUrl(url) {
				uni.navigateTo({
					url: url
				});
			},
			
		}
	}
</script>

<style lang="less">
page {
	background-color: #ffffff;
}
.problemReporting-page {
	.page-top {
		.history {
			.pick {
				display: inline-block;
				position: absolute;
				top: 20rpx;
				right: 30rpx;
				width: 150rpx;
				color: #3D71AD;
				text-align: right;
				image {
					width: 24rpx;
					height: 24rpx;
					margin-right: 10rpx;
					transform: translateY(2rpx);
				}
			}

		}
		padding: 52rpx 30rpx 14rpx;
		border-bottom: 14rpx solid #F2F2F2;
		font-size: 24rpx;
		.item {
			display: flex;
			height: 84rpx;
			line-height: 84rpx;
			.title {
				flex-shrink: 0;
				color: #999999;
			}
			.con {
				display: flex;
				height: 100%;
				width: 100%;
				border-bottom: 1px solid rgba(224, 224, 224, .5);
				&.flex-al-c {
					align-items: center;
					input {
						width: 100%;
						font-size: 24rpx;
					}
				}
			}
			uni-yealuo-select {
				width: 100% !important;
				color: #323232 !important;
			}
			.locStyle {
				display: flex;
				align-items: center;
				color: #3D71AD;
				image {
					width: 24rpx;
					height: 30rpx;
					margin-right: 10rpx;
				}
				view {
					text-decoration: underline;
				}
			}
		}
	}
	.page-bottom {
		padding: 0 30rpx;
		.title {
			line-height: 1;
			font-size: 30rpx;
			color: #232323;
			font-weight: bold;
			padding-left: 10rpx;
			margin: 40rpx 0 30rpx;
			border-left: 6rpx solid #3D71AD;
		}
		textarea {
			width: 100%;
			padding: 20rpx;
			font-size: 24rpx;
			background: #F5F5F5;
			box-sizing: border-box;
		}
		.ph-textarea {
			color: #999999;
			font-size: 24rpx;
			line-height: 24rpx;
		}
		.upload {
			margin: 30rpx 0;
			.pick {
				display: inline-block;
				position: relative;
				&:not(:nth-child(4n)) {
					margin-right: 22rpx;
				}
			}
			image, video {
				width: 156rpx;
				height: 156rpx;
			}
			.icon-play {
				position: absolute;
				top: 50%;
				left: 50%;
				transform: translate(-50%, -60%);
				width: 60rpx;
				height: 60rpx;
				z-index: 2;
			}
			.icon-del {
				position: absolute;
				top: -14rpx;
				right: -14rpx;
				width: 28rpx;
				height: 28rpx;
				z-index: 2;
				&.v-del {
					top: 0;
					right: 0;
				}
			}
		}
	}
	.page-btns {
		height: 100rpx;
		line-height: 1;
		.btns {
			display: flex;
			position: fixed;
			bottom: 0;
			left: 0;
			width: 100%;
			height: 100rpx;
			text-align: center;
			color: #ffffff;
			font-size: 34rpx;
			background: #3D71AD;
			.btn {
				display: flex;
				align-items: center;
				justify-content: center;
				width: 50%;
			}
			text {
				display: block;
				width: 100%;
				border-left: 1px solid rgba(224, 224, 224, .3);
			}
		}
	}
	.swiper-file {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		width: 100%;
		height: 100%;
		background: rgba(0, 0, 0, 1);
		z-index: 99999;
		.item {
			.index {
				position: absolute;
				top: 0;
				left: 0;
				width: 100%; 
				height: 80rpx;
				color: #ffffff;
				text-align: center;
				line-height: 80rpx;
				letter-spacing: 6rpx;
				z-index: 101;
			}
			image,
			video {
				position: absolute;
				top: 50%;
				left: 0;
				right: 0;
				width: 100%;
				transform: translateY(-50%);
				z-index: 100;
			}
			video {
			}
		}
	}
}
.setNullHeight {
	height: 100rpx;
}
</style>
