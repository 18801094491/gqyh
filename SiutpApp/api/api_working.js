import request from '../common/request.js'
// 工况导览 -- 默认加载列表
export const getDefaultList = (data,_this) => {
	request({
			method: 'GET',
			url: '/iot/operate/defaultMode',
			data: data //参数
		}).then(res => {
			_this.defaultList = res.result;
			uni.hideLoading();			
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
}
// 工况导览 -- 列表
export const getWorkingList = (data,_this) => {
	request({
			method: 'GET',
			url: '/iot/operate/mode',
			data: data //参数
		}).then(res => {
			_this.workingList = res.result;		
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
}


// 工况导览 -- 设备列表
export const getMontiorList = (data,_this) => {	
	request({
			method: 'GET',
			url: '/iot/operate/equip',
			data: data //参数
		}).then(res => {
			let result = res.result;
			let results = result.operateEquipInfoVo;
			_this.status = 'more';
			_this.total = results.total;
			if(result.ids){
				_this.addSelectedIds(result.ids);
			}
			if(results.records){
				let idsStr = "," + _this.checkList.join(",") + ","
				results.records.forEach((item, index) => {
					if(idsStr.indexOf("," + item.equipId + ",") != -1){
						item.selectStatus = '1';
						_this.preSelectedValues.push(item.equipId);
					}
				});
			}
			if(_this.pageNo > results.pages || !results.records ||  results.records.length === 0){
				_this.status = 'noMore';
			}else if(_this.pageNo === results.pages){
				_this.equiqList = _this.equiqList.concat(results.records)
				_this.status = 'noMore';
			}else{
				_this.equiqList = _this.equiqList.concat(results.records)
				_this.status = 'more';
			}
			uni.hideNavigationBarLoading();//关闭加载动画	
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
}

// 获取设备类型--A03
export const getEquiType = (data,_this) => {
  request({
  		method: 'GET',
  		url: '/sys/category/loadTreeRootCascade',
  		data: data //参数
  	}).then(res => {
  		_this.equiTypeList = [];
  		_this.equiTypeList = res.result;	
  	}).catch(res => {
  　　console.log(err,'errrr');
  })	
}

export const getAction = (url,params) => {
	return request({
			method: 'GET',
			url: url,
			data: params //参数
		});
}
// 工况导览 -- 保存信息
export const saveMontiorList = (data,_this) => {
	request({
			method: 'GET',
			url: '/iot/operate/save',
			data: data //参数
		}).then(res => {
			let result = res.result;	
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
	
	
}