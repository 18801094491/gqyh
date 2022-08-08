import request from '../common/request.js'
import cache from '../common/cache.js'

//用户登录
export const pwdLogin = (data) => {
	return request({
		method: 'POST',
		url: '/sys/app/login',
		data: data //参数
	}).then(responses => {
		cache.put('Token', responses.result.token)
		cache.put('userName', data.userName)
		cache.put('passWord', data.passWord)
		uni.switchTab({
			url:'/pages/home/home'
		});
		return;
	}).catch( error => {
		uni.showToast({
			title: '登录错误，请重新登录！',
			icon: 'none',
			mask: true
		})
		return;
	})
}

//重新登录
export const logOut = () => {
	cache.clear();
	uni.reLaunch({
		url:'/pages/login/login'
	})
}

// 找回密码;发送验证码
export const findPsd = (data,_this) => {
	
	request({
			method: 'GET',
			url: '/sys/app/findPassword',
			data: data //参数
		}).then(res => {
			_this.isShow = true;
			_this.intervalTimer = setInterval(() => {
			_this.countDown;
				if(_this.countDown == 0){
				   _this.isShow = false;
				   clearInterval(_this.intervalTimer)
				}
			}, 1000)
			if(res.code*1 == 200){
				uni.showToast({
					icon:'none',
					title:res.message
				})
				setTimeout(()=>{
					uni.reLaunch({
						url: `/pages/login/sms?phone=${_this.phoneNum}`
					})
				},100)
			}				
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
	
}

// 提交，重置密码
export const resetPsd = (data,_this) => {
	request({
			method: 'GET',
			url: '/sys/app/resetPassword',
			data: data //参数
		}).then(res => {
			uni.showToast({
				icon:'none',
				title:res.message
			});
					setTimeout(()=>{
						uni.reLaunch({
							url: '/pages/login/login'
						})
					},100);
		}).catch(res => {
	　　console.log(err,'errrr');
	})	
}


// 校验验证码
export const checkCode = (data,_this) => {
	
	request({
			method: 'GET',
			url: '/sys/app/checkCode',
			data: data //参数
		}).then(res => {
			uni.showToast({
				icon:'none',
				title:res.message
			})
			if(res.code*1 == 200){	
				setTimeout(()=>{
					uni.reLaunch({
						url:`/pages/login/setPsd?phone=${_this.phoneNum}`
					})
				},100)
			}
		}).catch(res => {
	　　console.log(err,'errrr');
	})	

}





//携带token
function getItems(data) {
	return request({
		method: 'GET',
		url: '/business/warn/app/items',
		data: data //参数
	})
}

