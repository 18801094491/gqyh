import request from '../common/request.js'
// 获取所有gis设备
export const getGisEquipment = (params = {}) => {
	return request({
			method: 'GET',
			url: '/gis/dashboard/data',
			data: params //参数
		});
}
// 获取所有gis设备得状态
export const getGisEquipmentState = (params = {}) => {
	return request({
			method: 'GET',
			url: '/gis/iot/iots/state',
			data: params //参数
		});
}

// 根据流量/压力/水质获取子数据
export const getGisTypeList = (_this, id, selectedList, gisId, flag, ) => {
	request({
			method: 'GET',
			url: `/gis/iot/list/${id}`,
			data: {} //参数
		}).then(res => {
			_this.typeList = [];
			_this.showPage = true;
			if (flag) {
				res.result.forEach(item => {
					_this.typeList.push(item)
				})
			} else {
				res.result.forEach(item => {
					_this.typeList.push(item)
				})
			}
		}).catch(res => {
	　　console.log(err,'errrr')
	})
	
	
}
// 根据流量/压力/水质获取子数据中的详情
export const getGisTypeDetailList = (_this, id, i) => {
	request({
			method: 'GET',
			url: `/gis/iot/getIotData/${id}`,
			data: {} //参数
		}).then(res => {
			let obj = _this.typeList[i]
			// obj.open = true
			obj.showData = res.result.showData || '暂无'
			_this.$set(_this.typeList, i, obj)
			_this.typeChilds['item' + i] = res.result.data
		}).catch(res => {
	　　console.log(err,'errrr')
	})	
}
// 获取监控中心gis地图导航栏列表
export const getGisModelNav = (_this) => {
	request({
			method: 'GET',
			url: '/gis/dashboard/app/gisModelNav',
			data: {} //参数
		}).then(res => {
			let list = res.data;
			_this.nameArr = [];
			_this.navList = list;
			list.map(index => {
				if (index.dataShow == 1) {
					_this.nameArr.push(index.modelType)
				}
			});
		}).catch(res => {
	　　console.log(res,'errrr')
	})	
}
// 获取监控中心gis所有点数据
export const getGisoMarkerData = (_this) => {
	request({
			method: 'GET',
			url: '/gis/dashboard/app/data',
			data: {} //参数
		}).then(res => {
			let list = res.data;
			_this.markers = [];
			list.filter((item) => {
				let icon;
				let width;
				let height;
				if (item.modelOnImg && (item.modelTypeCode == "Z06" || item.modelTypeCode == "Z03" || item.modelTypeCode == "Z02")) {
					icon = item.modelOnImg.imgUrl;
					width = item.modelOnImg.width;
					height = item.modelOnImg.height;
					_this.markers.push({
						id: item.id,
						latitude: item.latitude,
						longitude: item.longitude,
						iconPath: item.modelImg.imgUrl,
						width: item.width,
						height: item.height,
						title: '点位：'+item.equipmentSection+'  区域：'+ item.equipmentLocation ,
						// modelZcImg: new BMap.Icon(icon, new BMap.Size(width, height), {
						// 	imageSize: new BMap.Size(width, height)
						// }),
						modelImg: item.modelImg,
						list: [],
						modelType: _this.zoonLevel,
						// // id: item.id,
						// // latitude: item.latitude,
						// // longitude: item.longitude,
						modelLevel: item.modelLevel,
						modelOffImg: item.modelOffImg,
						modelOffset: item.modelOffset,
						modelOnImg: item.modelOnImg,
						modelPosition: item.modelPosition,
						modelTypeCode: item.modelTypeCode,
						modelWaringImg: item.modelWaringImg,
					})
					_this.markersOld.push({
						id: item.id,
						latitude: item.latitude,
						longitude: item.longitude,
						iconPath: item.modelImg.imgUrl,
						width: item.width,
						height: item.height,
						title: '点位：'+item.equipmentSection+'  区域：'+ item.equipmentLocation ,
						modelImg: item.modelImg,
						list: [],
						modelType: _this.zoonLevel,
						modelLevel: item.modelLevel,
						modelOffImg: item.modelOffImg,
						modelOffset: item.modelOffset,
						modelOnImg: item.modelOnImg,
						modelPosition: item.modelPosition,
						modelTypeCode: item.modelTypeCode,
						modelWaringImg: item.modelWaringImg,
					})
				} else if (item.modelImg && (item.modelTypeCode == "Z06" || item.modelTypeCode == "Z03" || item.modelTypeCode == "Z02")) {
					icon = item.modelImg.imgUrl;
					width = item.modelImg.width;
					height = item.modelImg.height;
					_this.markers.push({
						id: item.id,
						latitude: item.latitude,
						longitude: item.longitude,
						iconPath: item.modelImg.imgUrl,
						width: item.width,
						height: item.height,
						title: '点位：'+item.equipmentSection+'  区域：'+ item.equipmentLocation ,
						
						// modelZcImg: new BMap.Icon(icon, new BMap.Size(width, height), {
						// 	imageSize: new BMap.Size(width, height)
						// }),
						modelImg: item.modelImg,
						modelType: _this.zoonLevel,
						// // id: item.id,
						// // latitude: item.latitude,
						// // longitude: item.longitude,
						modelLevel: item.modelLevel,
						modelOffImg: item.modelOffImg,
						modelOffset: item.modelOffset,
						modelOnImg: item.modelOnImg,
						modelPosition: item.modelPosition,
						modelTypeCode: item.modelTypeCode,
						modelWaringImg: item.modelWaringImg,
					})
					_this.markersOld.push({
						id: item.id,
						latitude: item.latitude,
						longitude: item.longitude,
						iconPath: item.modelImg.imgUrl,
						width: item.width,
						height: item.height,
						title: '点位：'+item.equipmentSection+'  区域：'+ item.equipmentLocation ,
						modelImg: item.modelImg,
						modelType: _this.zoonLevel,
						modelLevel: item.modelLevel,
						modelOffImg: item.modelOffImg,
						modelOffset: item.modelOffset,
						modelOnImg: item.modelOnImg,
						modelPosition: item.modelPosition,
						modelTypeCode: item.modelTypeCode,
						modelWaringImg: item.modelWaringImg,
					})
				}
			})
		}).catch(res => {
	　　console.log(res,'errrr')
	})
	
}

//获取地图设备资产详情
export const getGisModelDetailData = (_this, id) => {
	request({
			method: 'GET',
			url: '/equipment/gisModel/app/getLocation',
			data: {
				modelId: id
			} //参数
		}).then(res => {
			_this.markerTip = '点位：'+ res.data.equipmentSection+'  区域：'+ res.data.equipmentLocation;
			console.log(_this.markerTip)
			setTimeout(function(){
				for (var i = 0; i < _this.markers.length; i++) {
					if (id == _this.markers[i].id) {
						_this.markers[i].title = _this.markerTip
					}
				}
			},300)
		}).catch(res => {
	　　console.log(res,'errrr')
	})	
}

// points.push({
// 	longitude: parseFloat(poLen[j].split(',')[0]),
// 	latitude: parseFloat(poLen[j].split(',')[1])
//   })
//获取地图河流信息列表
export const getGisRiversData = (_this) => {
	request({
			method: 'GET',
			url: '/map/river/app/rivers',
			data: {} //参数
		}).then(res => {
			let resultRivers = res.data;
			console.log("获取地图河流信息列表===========")
			let resultRiversArray = []
			for (var i in resultRivers) {
				var points = [];
				let obj = {
					longitude: "",
					latitude: ""
				}
				let riversPoints = JSON.parse(resultRivers[i].riverAddressInfo);
				// console.log(riversPoints)
				let polylines = {
				        points: riversPoints,
				        color: "#0091ff",
				        width: 6
				    }
				resultRiversArray.push(polylines)
			}
			
			console.log(resultRiversArray)
			_this.polylines = resultRiversArray;
			
		}).catch(res => {
	　　console.log(res,'errrr')
	})	
}

//获取区域信息列表
export const getGisRegionsData = (_this, callBack) => {
	request({
			method: 'GET',
			url: '/map/region/app/regions',
			data: {} //参数
		}).then(res => {
			let resultRegions = res.data;
			console.log("获取地图区域信息列表===========")
			let resultRegionsArray = [];
			for (var i in resultRegions) {
				var childArr = [];
				var points = [];
				let regionsPoints = (resultRegions[i].regionAddressInfo).split(";");
				// console.log(regionsPoints)
				for(var x in regionsPoints){
					let obj = {
						longitude: "",
						latitude: ""
					}
					let child = regionsPoints[x].split(",");
					obj.longitude = child[0];
					obj.latitude = child[1];
					childArr.push(obj);
				}
				// console.log(childArr)
				let polygons = {
				        points: childArr,
						fillColor: '#3D71AD33',
						// fillColor: 'rgba(162,51,51,0.5)',
						// fillOpacity: 0.2,
				        // fillColor: '#FFCCFF',
				        strokeWidth: 3,
						strokeColor: '#3D71AD',
				        // strokeColor: '#CC99CC',
				        zIndex: 11
				    }
				resultRegionsArray.push(polygons)
				// console.log(resultRegionsArray)
			}
			_this.polygons = resultRegionsArray;
			callBack && callBack();
			
		}).catch(res => {
	　　console.log(res,'errrr')
	})	
}