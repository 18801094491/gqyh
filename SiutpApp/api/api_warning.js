import request from '../common/request.js'

// 告警信息 --分类
export const getWarningClassify = (data, _this) => {
	let obj = [{
		title: '全部',
		code: '',
		selected: 1
	}]
	request({
		method: 'GET',
		url: '/sys/dict/app/getDictValue/warn_level',
		data: data //参数
	}).then(res => {
		if (res.code*1 === 200) {
			let result = res.result;
			result.map((item,index) => {
				item.selected = 0
			})
			_this.tabList = [...obj, ...result];
			uni.hideNavigationBarLoading()
		}	  
	}).catch(err=>{
		console.log(err,'errrr')
	})
}

// 告警信息 -- 列表
export const getWarningList = (data,_this) => {
	request({
		method: 'GET',
		url: '/business/warn/app/businessWarns',
		data: data //参数
	}).then(res => {
		if (res.code*1 === 200) {
		  let records = res.data.records;
		  _this.status = 'more';
		  _this.warningList = _this.warningList.concat(records);
		  if (!records.length) {
			  _this.status = 'noMore';
		  } else if (res.data.current == res.data.pages) {
			  _this.status = 'noMore';
		  }
		}
	}).catch(err=>{
		console.log(err,'errrr')
	})
}

// 告警信息 -- 单条详情
export const getWarningDetail = (id, _this) => {
	request({
		method: 'GET',
		url: '/business/warn/app/undealDatas/' + id,
	}).then(res => {
		if (res.code*1 === 200) {
			_this.warningDetail = res.data;
		}	  
	}).catch(err=>{
		console.log(err,'errrr')
	})
}