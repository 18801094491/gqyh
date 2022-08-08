<template>
	<view class='workOrderContent'>
		<view class="page-top">
			<view class="item">
				<view class="tit">工单名称：</view>
				<view class="con">{{workOrderData.name}}</view>
			</view>
			<view class="item">
				<view class="tit">工单编号：</view>
				<view class="con">{{workOrderData.code}}</view>
			</view>
			<view class="item">
				<view class="tit">负责人：</view>
				<view class="con">{{workOrderData.leaderName}}</view>
			</view>
			<view class="item">
				<view class="tit">工单线路：</view>
				<view class="con seeLine" v-if="workOrderData.id" @tap="goUrl(`orderMap?id=${workOrderData.id}&type=line`)">查看工单线路</view>
			</view>
		</view>

		<!-- 任务明细 -->
		<view class="page-content">
			<view class="title">任务明细</view>
			<view class="list" v-for="(item, index) in workOrderData.matterList" :key="index">
				<template v-if="order_status==='doing'">
					<view class="changeBtn con-done" v-if="item.editIng" @tap="onDoneChange(item)">完成</view>
					<view class="changeBtn con-edit" v-else @tap="onEditChange(item)">编辑</view>
				</template>
				
				<!-- 默认显示 -->
				<view :class="'tit ' + order_status">
					<view class="l">{{index+1}}、</view>
					<view class="t">标题：{{item.title}}</view>
				</view>
				<view class="progress">
					<image src="/static/progress-dot.png" mode="widthFix" />
					<view class="line"></view>
				</view>
				<view class="item" v-if="item.equipmentId">
					<view class="left">关联设备：</view>
					<view class="right">{{item.equipmentName}}</view>
				</view>
				<view class="item" v-if="item.matterLatitudeTencent || item.matterLatitude">
					<view class="left">所在位置：</view>
					<view class="locStyle" @tap="onSetProLocationChange(item, 'see-matter')">
						<image src="/static/loc-icon.png" mode="" />查看位置
					</view>
				</view>
				<view class="item content" v-if="item.matterLevel">
					<view class="left">问题等级：</view>
					<view class="right">{{item.matterLevelDes}}</view>
				</view>
				<view class="item content" v-if="item.matterType">
					<view class="left">问题类型：</view>
					<view class="right">{{item.matterTypeDes}}</view>
				</view>
				<view class="item mb12 minh50" v-if="item.description">
					<view class="left">注意事项：</view>
					<view class="right">{{item.description}}</view>
				</view>
				<view class="item porimg" v-if="showFileList &&  matterDetailList[index] && matterDetailList[index].fileList.length && !item.editIng">
					<view class="pick" v-for="(item2, i) in matterDetailList[index].fileList" :key="i" @tap="onPreviewMediaChange(index, i, item.editIng)">
						<view v-if="isVideo(item2.url)">
							<video class="pro-video" :src="item2.url" :controls="false" :show-center-play-btn="false"></video>
							<image class="icon-play" src="/static/icon-play.png" mode="widthFix" />
						</view>
						<image v-else :src="item2.url" mode="aspectFill" />
					</view>
				</view>

				<!-- 已编辑过显示 -->
				<view v-if="(item.solveLatitudeTencent || item.solveLatitude) && !item.editIng">
					<view class="item">
						<view class="left">当前位置：</view>
						<view class="locStyle" @tap="onSetProLocationChange(item, 'see-solve')">
							<image src="/static/loc-icon.png" mode="" />查看位置
						</view>
					</view>
					<view class="item mb12 minh50">
						<view class="left">解决描述：</view>
						<view class="right">{{item.solveDesc}}</view>
					</view>
					<view class="item">
						<view class="left">存在问题：</view>
						<view class="right">{{item.hasMatter}}</view>
					</view>
					<view v-if="item.hasMatter==='是'">
						<view class="item">
							<view class="left">问题类型：</view>
							<view class="right">{{item.newMatter.matterTypeDes}}</view>
						</view>
						<view class="item">
							<view class="left">问题等级：</view>
							<view class="right">{{item.newMatter.matterLevelDes}}</view>
						</view>
						<view class="item mb12 minh50">
							<view class="left">问题描述：</view>
							<view class="right">{{item.newMatter.description}}</view>
						</view>
					</view>
				</view>

				<!-- 编辑时显示 -->
				<view v-if="item.editIng">
					<view class="upload">
						<view class="pick" v-for="(item2, i) in fileUrlList" :key="i" @tap="onPreviewMediaChange(index, i, item.editIng)">
							<template v-if="videoType.includes(item2.url.slice(-3))">
								<video class="pro-video" :src="item2.url" :controls="false" :show-center-play-btn="false"></video>
								<image class="icon-play" src="/static/icon-play.png" mode="" />
							</template>
							<image v-else class="pro-img" :src="item2.url" mode="aspectFill" />
							<image class="icon-del" src="/static/filedel.png" mode="widthFix" @tap.stop="onDelImageChange(i)" />
						</view>
						<image class="icon-img" src="/static/upImage.png" mode="widthFix" @tap="chooseMedia" v-if="fileUrlList.length<8"/>
					</view>
					<view class="item">
						<view class="left">当前位置：</view>
						<view class="locStyle" @tap="onSetProLocationChange(item, 'set')">
							<image src="/static/loc-icon.png" mode="" />{{solveLongitudeTencent?'已打卡': '打卡'}}
						</view>
					</view>
					<view class="item mb30">
						<textarea class="desc" v-model="solveDesc"  placeholder="请输入解决描述" maxlength="2000"></textarea>
					</view>
					<view class="item">
						<view class="left">存在问题：</view>
						<checkbox-group @change="onSetIsProblemChange(item)" >
							<label><checkbox value='是' :checked="item.hasMatter === '是'" :disabled="item.hasMatter === '是'" />是</label>
							<label><checkbox value='否' :checked="item.hasMatter !== '是'" :disabled="item.hasMatter !== '是'" />否</label>
						</checkbox-group>
					</view>
					<view v-if="item.hasMatter === '是'">
						<view class="item type">
							<view class="left">问题类型：</view>
							<view class="con">
								<uni-yealuo-select :binData="proTypeList" :defaultData="defaultData(item, 'matterType')" 
									:disabled="true" overflow="hide" valueType="protype" @getBackVal="getBackVal" :selectIco="true" />
							</view>
						</view>
						<view class="item level">
							<view class="left">问题等级：</view>
							<view class="con">
								<uni-yealuo-select :binData="proLevelList" :defaultData="defaultData(item, 'matterLevel')" 
									:disabled="true" overflow="hide" valueType="level" @getBackVal="getBackVal" :selectIco="true" />
							</view>
						</view>
						<view class="item mb30">
							<textarea class="desc" v-model="description" placeholder-calss="textDesc" placeholder="请输入问题描述" maxlength="2000"></textarea>
						</view>
					</view>
				</view>
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
	</view>
</template>

<script>
	import cache from '@/common/cache.js'
	import {baseUrl} from '../../../../common/request.js'
	import uniYealuoSelect from '../../../../components/uni-yealuo-select/uni-yealuo-select.vue'
	import { postAction , getWorkOrderData, getProTypeData, getProLevelData } from '../../../../api/api_works.js'
	export default{
		data(){
			return{
				order_id: '', //当前工单id
				order_status: '', //当前工单id
				workOrderData: [], //问题数据
				showFileList: false, //是否显示附件（确保已有数据）
				matterDetailList: [], //问题附件
				proTypeList: [], //问题类型
				proLevelList: [], //问题等级
				fileUrlIndex: 0, //当前上传中的图片索引
				fileUrlList: [], //微信本地图片数组
				solveLongitudeTencent: '', //打卡经度
				solveLatitudeTencent: '', //打卡纬度
				solveDesc: '', //解决描述
				matterType: '', //问题类型
				matterLevel: '', //问题等级
				description: '', //问题描述
				fileList: [], //上传附件
				editIng: false,
				id_hasMatter: '',
				val_hasMatter: '',
				videoType: 'mp4,mov,m4v,3gp,avi,m3u8,webm',
				mediaFiles: [],
				mediaFileIndex: ''
			}
		},
		components: {
			uniYealuoSelect
		},
		methods:{
			onLoad(option) { //初始加载
				console.log(option)
				this.order_id = option.id;
				this.order_status = option.status;
				getWorkOrderData({id: option.id}, this, 'foreach');
				getProTypeData({}, this);
				getProLevelData({}, this);
			},

			isVideo(url) {
				return this.videoType.includes(url.slice(-3))
			},
			
			onShow(){
				// uni.getStorageSync('orderMap')
			},

			onSetProLocationChange(item, type) { //获取定位
				if (type === 'see-matter') { //所在位置
					this.goUrl(`orderMap?lat=${item.matterLatitudeTencent || item.matterLatitude}&lng=${item.matterLongitudeTencent || item.matterLongitude}&type=${type}`);
				} else if (type === 'see-solve') { //当前位置
					this.goUrl(`orderMap?lat=${item.solveLatitudeTencent || item.solveLatitude}&lng=${item.solveLongitudeTencent || item.solveLongitude}&type=${type}`);
				} else if (type === 'set') { //打卡
					uni.showLoading({
						title: '当前定位获取中',
						mask: true,
						success: res => {},
					})
					// #ifdef MP-WEIXIN
						this.getLocation(type);
					// #endif

					// #ifdef APP-PLUS
						this.appGetLocation(type);
					// #endif
				}
			},

			getLocation(type) {//微信获取用户位置
				uni.getLocation({
					type: 'gcj02',
					isHighAccuracy: true,
					highAccuracyExpireTime: 3100,
					success: res => {
						uni.hideLoading();
						this.goUrl(`orderMap?lat=${res.latitude}&lng=${res.longitude}&type=${type}`);
						setTimeout(() => {
							this.solveLongitudeTencent = res.longitude;
							this.solveLatitudeTencent = res.latitude;
						}, 1000);
					},
					fail: err => {
						uni.hideLoading();
						console.log('未获取到定位授权')
						uni.showModal({
							title: '获取不到您的定位',
							content: '在设置页中开启定位授权，以获取当前所在位置',
							confirmText: '去开启定位',
							confirmColor: '#3D71AD',
							success: res => {
								if (res.confirm) {
									uni.openSetting({success: res => {
										console.log('打开设置页成功', res)
									}})
								}
							}
						})
					}
				})
			},
			
			appGetLocation(type) { //APP获取定位授权
				plus.geolocation.getCurrentPosition( res => {
					uni.hideLoading()
					console.log('用户定位位置信息：', JSON.stringify(res));
					this.solveLongitudeTencent = res.coords.longitude;
					this.solveLatitudeTencent = res.coords.latitude;
					console.log(this.solveLatitudeTencent)
					console.log(`orderMap?lat=${this.solveLatitudeTencent}&lng=${this.solveLongitudeTencent}&type=${type}`)
					this.goUrl(`orderMap?lat=${this.solveLatitudeTencent}&lng=${this.solveLongitudeTencent}&type=${type}`);
					
				},  err => {
					uni.hideLoading()
					// str = JSON.stringify(err);
					console.log( "获取定位位置信息失败：" + JSON.stringify(err) );
					plus.nativeUI.alert( "获取定位位置信息失败, 请手动在应用设置中开启定位");
				});
				return

				// 权限被临时拒绝  待测试

					// plus.android.requestPermissions(['android.permission.ACCESS_FINE_LOCATION'], res => {
					// 	uni.hideLoading();
					// 	if(res.deniedAlways.length>0){
					// 		console.log('权限被永久拒绝 ：');
					// 		// e.deniedAlways.toString()
					// 		plus.nativeUI.alert( "获取定位位置信息失败, 请到应用设置中开启定位");
					// 	}
					// 	if(res.deniedPresent.length>0){
					// 		console.log('权限被临时拒绝 : '+res.deniedPresent.toString());
					// 		// plus.nativeUI.alert( "获取定位位置信息失败, 请手动在应用设置中开启定位");
					// 	}
					// 	if(res.granted.length>0){
					// 		//调用依赖获取定位权限的代码
					// 		console.log('权限被允许 ：'+res.granted.toString());
					// 		console.log(res)
							// plus.geolocation.getCurrentPosition( res => {
							// 	console.log('用户定位位置信息：', JSON.stringify(res));
							// 	this.solveLongitudeTencent = res.coords.longitude;
							// 	this.solveLatitudeTencent = res.coords.latitude;
							// 	this.goUrl(`orderMap?lat=${this.solveLatitudeTencent}&lng=${this.solveLongitudeTencent}&type=${type}`);
								
							// });
					// 		// }, err => {
					// 		// 	console.log('Geolocation error: ' + err.message);
					// 		// } );
					// 	}
					// }, err =>{
					// 	console.log('APP授权功能调用失败 :'+JSON.stringify(err));
					// });
			},

			onSetIsProblemChange(data) { //是否存在问题
				if (!this.id_hasMatter) this.id_hasMatter = data.id;
				if (!this.val_hasMatter) this.val_hasMatter = data.hasMatter;
				data.hasMatter = data.hasMatter === '是' ? '否' : '是'
				// if (data.hasMatter === '否') {
				// 	// this.matterType = '';
				// 	// this.matterLevel = '';
				// 	// this.description = '';
				// }
				console.log('val_hasMatter ： ', this.val_hasMatter)
			},
			// getSelectedValue(data, value) {
			// 	data.hasMatter = value
			// 	console.log(value)
			// },

			defaultData(item, type) { //问题类型/等级回填
				return item.newMatter ? {target: {
					value: item.newMatter[type+'Des']+'|'+item.newMatter[type]
				}} : ''
			},

			getBackVal(val, type) { //问题类型/等级选择
				// console.log(val, type)
				if (type === 'protype') {
					this.matterType = val;
				} else if (type === 'level') {
					this.matterLevel = val;
				}
			},

			chooseMedia() { //选择图片
				// #ifdef MP-WEIXIN
					uni.chooseMedia({
						count: 8 - this.fileUrlList.length, 
						sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
						mediaType: ['image', 'video'], //文件类型
						sourceType: ['album', 'camera'], //图片视频来源
						success: res => {
							res.tempFiles.forEach(item => {
								this.fileUrlList.push({url: item.tempFilePath, type: 'new'}); //通过相册新增的图片
							})
							if ( this.fileUrlList.length >= 8 ) {
								this.fileUrlList.length = 8;
							}
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
								res.files.forEach(item => {
									this.fileUrlList.push({url: item, type: 'new'})
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
									this.fileUrlList.push({url: fullPath, type: 'new'})
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
			
			onDelImageChange(i) { //移除选中图片
				this.fileUrlList = this.fileUrlList.filter((item, index) => index != i)
				this.fileList = this.fileList.filter((item, index) => index != i)
				console.log('移除所选图片后的 余下线上图片数组fileList: ',this.fileList)
			},

			onEditChange(data) { //任务编辑
				// 清空/替换 默认显示数据
				this.edit_id = data.id; //编辑的任务id
				this.fileList = [];
				this.fileUrlList = [];
				this.solveLongitudeTencent = '';
				this.solveLatitudeTencent = '';
				this.solveDesc = data.solveDesc || '';
				this.matterType = data.newMatter && data.newMatter.matterType || '';
				this.matterLevel = data.newMatter && data.newMatter.matterLevel || '';
				this.description = data.newMatter && data.newMatter.description || '';
				
				this.matterDetailList.forEach(item => { //从接口取出要编辑任务的 线上图片数组
					if (item.id === data.id) {
						item.fileList.forEach(item => {
							this.fileList.push({url: item.url});
							this.fileUrlList.push({url: item.url, type: 'old'});
						})
					}
				})
				setTimeout(() => {
					this.workOrderData.matterList.forEach(item => {
						if (item.id === data.id) {
							this.editIng = true; 
							item.editIng = true; //点击的任务可编辑
						} else { item.editIng = false; } //其余任务退出编辑状态
						if (item.id === this.id_hasMatter) {
							item.hasMatter = this.val_hasMatter; //被退出编辑状态的任务状态恢复
							console.log(this.val_hasMatter)
						}
					})
					this.id_hasMatter = '';
					this.val_hasMatter = '';
				});
			},
			
			onDoneChange(data) { //任务完成
				let params = {
					id: data.id,
					title: data.title,
					historyClockIn: !!data.solveLongitudeTencent, //是否有历史打卡记录
					solveLongitudeTencent: this.solveLongitudeTencent, //打卡经度
					solveLatitudeTencent: this.solveLatitudeTencent, //打卡纬度
					solveDesc: this.solveDesc, //解决描述
					hasMatter: data.hasMatter, //存在问题
					matterType: data.hasMatter === '是' ? this.matterType : '', //问题类型
					matterLevel: data.hasMatter === '是' ? this.matterLevel : '', //问题等级
					description: data.hasMatter === '是' ? this.description : '', //问题描述
					fileList: []
				} 
				if ( data.equipmentId ) params['equipmentId'] = data.equipmentId
				this.onFormValidationChange(params, () => {
					this.updateFile(this.fileUrlList, () => {
						let fileList = []
						this.fileList.forEach(item => {
							if (item.url.includes('http')) {
								fileList.push({url: item.url.slice(item.url.indexOf('/res/file'))})
							} else {
								fileList.push(item)
							}
						})
						params['fileList'] = fileList;
						postAction('/worklist/myWorkList/app/completeOne', params).then(res => {
							this.onShowToast('提交成功', 1000, () => {
								// 清空可编辑数据
								this.editIng = false;
								this.fileList = [];
								this.fileUrlList = [];
								this.solveLongitudeTencent = '';
								this.solveLatitudeTencent = '';
								this.solveDesc = '';
								this.matterType = '';
								this.matterLevel = '';
								this.description = '';
								this.showFileList = false;
								this.matterDetailList = [];
								this.id_hasMatter = '';
								this.val_hasMatter = '';
								// this.defaultData({newMatter: ''})
								getWorkOrderData({id: this.order_id}, this); //初始化页面
								setTimeout(() => { data.editIng = false }) //退出编辑状态
							})
						}).catch(err => {
							uni.hideLoading();
							this.onShowToast('提交失败，请稍后尝试')
						})
					})
				})
			},
			
			onFormValidationChange(params, callBack) { //数据验证
				let title = '';
				if (!params.solveLongitudeTencent) {
					title = params.historyClockIn ? '请重新打卡' : '请完成打卡';
				} else if (!params.solveDesc) {
					title = '请填写解决描述';
				} else if (params.hasMatter === '是') {
					if (!params.matterType) {
						title = '请选择问题类型'
					} else if (!params.matterLevel) {
						title = '请选择问题等级'
					} else if (!params.description) {
						title = '请填写问题描述'
					}
				}
				if (title) {
					this.onShowToast(title, 800);
					return;
				}
				callBack();
			},

			updateFile(fileUrlList, callBack) { //上传文件到服务器
				uni.showLoading({title: '上传中', mask: true})
				if (!fileUrlList[this.fileUrlIndex]) { //查无数据 callBack()
					this.fileUrlIndex = 0;
					uni.hideLoading();
					setTimeout(() => { callBack(), 100 });
					return;
				}
				if (fileUrlList[this.fileUrlIndex].type === 'old') { //从接口取的图片跳过上传
					this.fileUrlIndex++;
					this.updateFile(fileUrlList, callBack);
					return;
				}
				uni.uploadFile({
					url: baseUrl + '/sys/file/upload',
					filePath: fileUrlList[this.fileUrlIndex].url,
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
							setTimeout(() => { this.onShowToast(data.message, 2000) })
						}
					},
					fail: res => {
						console.log('uploadFile - fail - 上传失败，请重新提交')
						uni.hideLoading();
						this.fileList = [];
						this.fileUrlIndex = 0;
						this.onShowToast('上传失败，请重新提交', 1000)
					}
				}).onProgressUpdate(res => {
					uni.showLoading({
						title: `第${this.fileUrlIndex+1}张已上传${res.progress}%`,
						mask: true,
						success: () => { res.progress == 100 && uni.hideLoading() }
					})
				})
			},

			onPreviewMediaChange(index, file_i, editIng) { //显示大图
				let mediaFiles = [];
				let files = editIng ? this.fileUrlList : this.matterDetailList[index].fileList;
				files.forEach(item => {
					if (this.videoType.includes(item.url.slice(-3))) {
						mediaFiles.push({url: item.url, type: 'video'})
					} else {
						mediaFiles.push({url: item.url, type: 'image'})
					}
				})
				console.log(mediaFiles);
				
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
						current: file_i
					});
				//#endif

			},
			
			hideSwiper() {
				this.mediaFiles = []
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
			}
		},
		
	}
</script>

<style lang="less">
page {
	background: #FFFFFF;
}
.workOrderContent {
	.page-top {
		width: 100%;
		padding: 50rpx 30rpx 38rpx;
		background: linear-gradient(0deg, #20B8EE, #3D71AD);
		.item {
			display: flex;
			color: #FFFFFF;
			line-height: 1.5;
			min-height: 50rpx;
			font-size: 24rpx;
			margin-bottom: 10rpx;
			.tit {
				flex-shrink: 0;
			}
			.seeLine {
				color: #F0F300;
				text-decoration: underline;
			}
		} 
	}
	.page-content {
		padding: 40rpx 30rpx 0;
		.title {
			line-height: 1;
			color: #232323;
			font-size: 30rpx;
			padding-left: 10rpx;
			border-left: 6rpx solid #3D71AD;
		}
		.list {
			position: relative;
			padding-left: 64rpx;
			.changeBtn {
				position: absolute;
				top: 36rpx;
				right: 0;
				width: 90rpx;
				height: 46rpx;
				line-height: 42rpx;
				text-align: center;
				font-size: 24rpx;
				border-radius: 6rpx;
				box-sizing: border-box;
				&.con-done {
					color: #FFFFFF;
					background: #3D71AD;
				}
				&.con-edit {
					color: #3D71AD;
					border: 1px solid #3D71AD;
	
				}
			}
			.tit {
				display: flex;
				line-height: 1.5;
				padding: 42rpx 0 36rpx;
				&.doing {
					max-width: 530rpx;
				}
			}
			.progress {
				position: absolute;
				left: 0;
				top: 42rpx;
				width: 36rpx;
				height: 100%;
				image {
					position: relative;
					display: block;
					width: 36rpx;
					height: 36rpx;
				}
				.line {
					width: 1px;
					height: 100%;
					background: #E0E0E0;
					margin: -12rpx auto 0;
				}
			}
			&:last-child {
				padding-bottom: 40rpx;
			}
		}
		.item {
			display: flex;
			line-height: 1.5;
			min-height: 62rpx;
			&.minh50 {
				min-height: 50rpx;
			}
			&.porimg {
				display: block;
				.pick {
					display: inline-block;
					position: relative;
					margin-bottom: 16rpx;
					&:not(:nth-child(4n)) {
						margin-right: 22rpx;
					}
				}
				image, video {
					width: 140rpx;
					height: 140rpx;
				}
				.icon-play {
					position: absolute;
					top: 50%;
					left: 50%;
					transform: translate(-50%, -50%);
					width: 60rpx;
					height: 60rpx;	
				}
			}
			&.type, &.level {
				height: 62rpx;
				.con {
					height: 66%;
					width: 100%;
					border-bottom: 1px solid rgba(224, 224, 224, 0.5);
				}
			}
			&.linh1-5 {
				line-height: 1.5;
			}
			.left {
				color: #999999;
				flex-shrink: 0;
			}
			.right{
				
			}
			.locStyle {
				color: #3D71AD;
				text-decoration: underline;
				image {
					width: 24rpx;
					height: 30rpx;
					margin-right: 10rpx;
					transform: translateY(4rpx);
				}
			}
			.desc {
				width: 100%;
				height: 140rpx;
				font-size: 24rpx;
				background: #F5F5F5;
				padding: 20rpx;
			}
			.textDesc {
				color: #999999;
				font-size: 24rpx;
			}
			checkbox-group {
				width: auto;
				checkbox .wx-checkbox-input {
					width: 28rpx;
					height: 28rpx;
				}
				checkbox .checkbox-input {
					width: 28rpx;
					height: 28rpx;
				}
				checkbox .wx-checkbox-input.wx-checkbox-input-checked::before {
					color: #3D71AD;
					font-size: 30rpx;
				}
				checkbox .checkbox-input.checkbox-input-checked::before {
					color: #3D71AD;
					font-size: 30rpx;
				}
			}
			uni-yealuo-select {
				width: 100% !important;
				color: #323232 !important;
			}
		}
		.upload {
			margin: 30rpx 0;
			.pick {
				display: inline-block;
				position: relative;
				&:not(:nth-child(4n)) {
					margin-right: 20rpx;
				}
				margin-bottom: 8rpx;
			}
			image, video {
				width: 140rpx;
				height: 140rpx;
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
.mb30 {
	margin-bottom: 30rpx;
}
.mb12 {
	margin-bottom: 12rpx;
} 
.setNullHeight {
	height: 60rpx;
}
</style>
