import request from '../common/request.js'

// 问题上报-问题类型
export const getProTypeData = (data, _this) => {
	request({
		method: 'GET',
		url: '/worklist/workListMatter/app/matterType',
		data: data
	}).then(res => {
		if (res.code*1 === 200) {
			_this.proTypeList = res.result;
		}
	}).catch(res => {
	　　console.log(err,'errrr');
	})	
}
// 问题上报-关联设备
export const getEquipmentData = (data, _this) => {
	request({
		method: 'GET',
		url: '/worklist/workListMatter/app/equipments',
		data: data
	}).then(res => {
		if (res.code*1 === 200) {
			let result = [];
			res.result.forEach(item => {
				result.push({code: item.id, title: item.equipmentName})
			})
			_this.initialEquipmentList = result;
			_this.equipmentList = result;
		}
	}).catch(res => {
	　　console.log(err,'errrr');
	})	
}
// 问题上报-问题等级
export const getProLevelData = (data, _this) => {
	request({
		method: 'GET',
		url: '/worklist/workListMatter/app/level',
		data: data
	}).then(res => {
		if (res.code*1 === 200) {
			_this.proLevelList = res.result;
		}
	}).catch(res => {
	　　console.log(err,'errrr');
	})	
}

// 问题上报-历史问题-历史问题列表
export const getHistoryProList = (data, _this) => {
	request({
		method: 'GET',
		url: '/worklist/workListMatter/app/list',
		data: data
	}).then(res => {
		if (res.code*1 === 200) {
			let records = res.data.records;
			_this.status = 'more';
			_this.historyProList = _this.historyProList.concat(records);
			if (!records.length) {
				_this.status = 'noMore';
			} else if (res.data.current == res.data.pages) {
				_this.status = 'noMore';
			}
			setTimeout(() => { _this.showPage = true })
		}
	}).catch(err => {
		_this.showPage = true;
		console.log(err,'errrr');
	})	
}

// 问题上报-历史问题-历史问题列表-问题详情
export const getProblemDetail = (data, _this) => {
	request({
		method: 'GET',
		url: '//worklist/workListMatter/app/query',
		data: data
	}).then(res => {
		if (res.code*1 === 200) {
			_this.problemDetail = res.data
		}
	}).catch(err => {
	　　console.log(err,'errrr');
	})	
}

export const postAction = (url,params) => {
	return request({
		method: 'POST',
		url: url,
		data: params //参数
	});
}