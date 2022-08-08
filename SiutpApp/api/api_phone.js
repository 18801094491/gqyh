import request from '../common/request.js'
// 通讯录--查询分组信息
export const getPhoneList = (data,_this,type) => {
	request({
			method: 'GET',
			url: '/sys/addressbook/departuser',
			data: data //参数
		}).then(res => {
			_this.mailList = [];
			_this.mailListUser = [];
			_this.mailDetailList = [];
			_this.mailDetailListUser = [];
			let result = res.result;
			if(type == 'mail'){
				_this.mailList = result.departments;
				_this.mailListUser = result.departmentUsers;
			}else{
				_this.mailDetailList = result.departments;
				_this.mailDetailListUser = result.departmentUsers;
			}						
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
}

// 通讯录--查询用户信息
export const getUserList = (data,_this) => {
	request({
			method: 'GET',
			url: `/sys/addressbook/depart/${data.orgCode}/users`,
			data: {realname:data.realname} //参数
		}).then(res => {
			let result = res.result;		
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
}
