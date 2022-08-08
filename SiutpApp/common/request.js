import cache from '@/common/cache.js'
//服务器地址配置
//const baseUrl = "https://cwe.daringsmartcity.com/siutp";
// const baseUrl = "http://172.168.10.49:8081/siutp";
// const baseUrl = "http://172.168.11.60:8080/siutp";
const baseUrl = "http://106.12.34.223:8080/siutp";
// const baseUrl = "http://118.122.130.29:9000/siutp";
//请求白名单,判断是否必须要携带token，在白名单则不需要携带token,否则...
const whiteUrl = ['/sys/app/login', '/auth-redirect'];
const request = (config) => {
	const toke = cache.get("Token");
	const i = whiteUrl.indexOf(config.url);
	//请求地址不在白名单,必须要携带token
	if (whiteUrl.indexOf(config.url) === -1) {
		//如果token过期失效，则调用刷新方法,根据返回值判断
		if (toke) {
			config.header = {
				'Content-Type': 'application/json', //自定义请求头信息
				'X-request-platform': 'app',
				'X-Access-Token': toke
			}
		} else {
			//没有token则去登录，终止break
			uni.showToast({
				title: "去登录"
			});
			uni.reLaunch({
				url:'/pages/login/login'
			});
			console.log(config.url);
		}
	} else {
		//请求地址在白名单,不要携带token
		/* config.url = baseUrl + config.url; */
		config.header = {
			'Content-Type': 'application/json', //自定义请求头信息
			'X-request-platform': 'app'
		}
		// console.log(config.url);
		// console.log(i);
		// console.log(3);
	}
	config.url = baseUrl + config.url;
	if (!config.data) {
		config.data = {};
	}
	// console.log(JSON.stringify(config.data));
	let promise = new Promise(function(resolve, reject) {
		uni.request(config).then(responses => {
			// 异常
			if (responses[0]) {
				reject({
					message: "网络超时"
				});
				uni.reLaunch({
					url:'/pages/login/login.vue'
				});
			}else{
				let response = responses[1].data; // 如果返回的结果是data.data的，嫌麻烦可以用这个，return res,这样只返回一个data
				resolve(response);
			}
			
		}).catch(error => {
			uni.reLaunch({
				url:'/pages/login/login.vue'
			});
			reject(error);
		})
	})
	return promise;
};
export {baseUrl};
export default request;
