import request from '../common/request.js'

// 首页--告警滚动展示
export const getAlarmScroll = (_this) => {
	request({
			method: 'GET',
			url: '/business/warn/app/undealDatas',
			data: {} //参数
		}).then(responses => {
			_this.msgData = responses.data;
			_this.timer = setInterval(_this.scrollAnimate, 3000);
			if(_this.msgData.length<3){
				clearInterval(_this.timer);
				_this.animateUp = false;
			}
		}).catch(res => {
	　　console.log(err,'errrr')
	})	
}

// 首页-我的工单-工单数量
export const getMylistnum = (_this) => {
	request({
		method: 'GET',
		url: '/worklist/myWorkList/app/mylistnum',
		data: {} //参数
	}).then(res=>{
		if(res.code*1 === 200){
			_this.mylistnum = res.data;
		}
	}).catch(err=>{
		console.log(err,'errrr')
	})
}

// 首页-我的工单-工单数量
export const getRole = (_this, callBack) => {
	request({
		method: 'GET',
		url: '/sys/role/app/getRole',
		data: {} //参数
	}).then(res=>{
		if(res.code*1 === 200){
			callBack(res.result)
		} else {
			callBack({ roles: '' })
		}
	}).catch(err=>{
		callBack({ roles: '' })
		console.log(err,'errrr')
	})
}


// 获取设备类型--A03
export const getEquiType = (data,_this) => {
	request({
			method: 'GET',
			url: '/sys/category/loadTreeRootCascade',
			data: data //参数
		}).then(res=>{
	  if(res.code*1 === 200){
		_this.equiTypeList = [];
		_this.equiTypeList = res.result;
	  }
  }).catch(err=>{
	  console.log(err,'errrr')
  })
}

// 报警页面列表
export const getAlarmList = (data,_this) => {
  request({
	  method:'GET',
	  url:'/business/warn/app/items',
	  data:data//参数
  }).then(res=>{

	  if(res.code*1 === 200){
		let result = res.result;
		var dataList = [];
		dataList = result.records;
		var length = dataList.length;
		if ( _this.pageNo == 1 ) {
			_this.list = dataList;                                                                                                                                                                                                                                                                                                                                                                        
			if(length == 0){
				_this.is_page = false;
				console.log( '暂无数据' );
				_this.status = 'noMore';
				uni.hideNavigationBarLoading();//关闭加载动画
				return false; 
			} else if (length < 8) {
				_this.is_page = false;
				console.log( '没有更多数据' );
				_this.status = 'noMore';
				uni.hideNavigationBarLoading();//关闭加载动画
				return false;
			}
		} else {
			_this.status = 'more';
			uni.hideNavigationBarLoading()
			if(length == 0){
				_this.is_page = false;
				console.log( '没有更多数据' );
				_this.status = 'noMore';
				uni.hideNavigationBarLoading();//关闭加载动画
				return false;
			}   
			_this.list = _this.list.concat( dataList );
		}




		// if(_this.pageNo == 1 && result.total == 0){
		// 	_this.status = '';
		// }else if(_this.pageNo == 1 && result.total<10){
		// 	_this.list = [];
		// 	_this.list = result.records;
		// 	_this.total = result.total;
		// 	// _this.list = _this.list.concat(result.records)
		// 	_this.status = 'more';
		// 	uni.hideNavigationBarLoading()
		// }else{
		// 	let result = res.result;
		
		// 	_this.list = _this.list.concat(result ? result.records : [])
		// 	_this.status = 'more';
		// 	uni.hideNavigationBarLoading()
		// 	if (_this.list.length ==_this.total) {
		// 		_this.status = 'noMore';
		// 		uni.hideNavigationBarLoading();//关闭加载动画
		// 		return false;
		// 	}
		// }
	  }	  
  }).catch(err=>{
	  console.log(err,'errrr')
  })
}

// 报警详情信息
export const alarmDetail = (data,_this) => {

  request({
	  method:'GET',
	  url:`/business/warn/app/item/${data.bid}`
  }).then  (res=>{
		if(res.code*1===200){
			_this.detailObj = Object.assign({},res.result);
			request({
				  method:'GET',
				  url:`/business/warn/app/item/iot/${res.result.iotId}`,
			}).then(res=>{
			
				_this.gaugeList= [];
				_this.gaugeList = res.result;
				_this.getServerData()
			}).catch(err=>{
				  console.log(err,'errrr')
			})
		}
  }).catch(err=>{
	  console.log(err,'errrr')
  })
}

export const postAction = (url,params) => {
	return request({
		method: 'POST',
		url: url,
		data: params //参数
	});
}