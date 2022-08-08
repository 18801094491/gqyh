import request from '../common/request.js'

// 我的工单-列表
export const getWorkOrderList = (data, _this) => {
	request({
		method: 'GET',
		url: '/worklist/myWorkList/app/list',
		data: data //参数
	}).then(res => {
		if (res.code*1 === 200) {
			let records = res.data.records
			_this.status = 'more';
			_this.workOrderList = _this.workOrderList.concat(records);
			if (!records.length) {
				_this.status = 'noMore';
			} else if (res.data.current == res.data.pages) {
				_this.status = 'noMore';
			}
			if (data.searchStatus === 'doing') {
				_this.tabList[0].num = res.data.total
			}
		}
	}).catch(err => {
	　　console.log(err,'errrr');
	})	
}

// 我的工单-工单填报(工单详情)
export const getWorkOrderData = (data, _this) => {
	request({
		method: 'GET',
		url: '/worklist/myWorkList/app/query',
		data: data //参数
	}).then(res => {
		if (res.code*1 === 200) {
			res.data.matterList.forEach(item => {
				item.editIng = false;
				item.hasMatter = item.hasMatter || '否';
			})
			_this.workOrderData = res.data;
			if(res.data.matterList.length) {
				getmatterDetailData({id: res.data.matterList[0].id}, _this, 0, res.data.matterList.length);
			}
		}
	}).catch(res => {
		　　console.log(err,'errrr');
	})	
}
// 我的工单-工单填报(工单详情)-单条任务查询
export const getmatterDetailData = (data, _this, index, length) => {
	request({
		method: 'GET',
		url: '/worklist/workListMatter/app/query',
		data: data //参数
	}).then(res => {
		if (res.code*1 === 200) {
			_this.matterDetailList[index] = res.data;
			if ( index+1 < length) {
				++index
				getmatterDetailData({id: _this.workOrderData.matterList[index].id}, _this, index, length)
			} else {
				_this.showFileList = true
			}
		}
	}).catch(res => {
	　　console.log(err,'errrr');
	})	
}

// 我的工单-工单填报-问题类型
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
// 我的工单-工单填报-问题等级
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

export const postAction = (url,params) => {
	return request({
		method: 'POST',
		url: url,
		data: params //参数
	});
}

export const getAction = (url,params) => {
	return request({
		method: 'GET',
		url: url,
		data: params //参数
	});
}