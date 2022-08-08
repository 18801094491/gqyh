/**  
 * 缓存数据优化  
 * var cache = require('utils/cache.js');  
 * import cache from '../cache'  
 * 使用方法 【  
 *     一、设置缓存  
 *         string    cache.put('k', 'string你好啊');  
 *         json      cache.put('k', { "b": "3" }, 2);  
 *         array     cache.put('k', [1, 2, 3]);  
 *         boolean   cache.put('k', true);  
 *     二、读取缓存  
 *         默认值    cache.get('k')  
 *         string    cache.get('k', '你好')  
 *         json      cache.get('k', { "a": "1" })  
 *     三、移除/清理    
 *         移除: cache.remove('k');  
 *         清理：cache.clear();   
 * 】  
 * @type {String}  
 */  
var postfix = '_aszapp'; // 缓存前缀   
/**  
 * 设置缓存   
 * @param  {[type]} k [键名]  
 * @param  {[type]} v [键值]  
 * @param  {[type]} t [时间、单位秒]  
 */  
function put (key, value, seconds) {
	var timestamp = Date.parse(new Date()) / 1000
    if (key && value) {
		uni.setStorageSync(key, value)
		let expire = timestamp + (3600 * 24 * 30)
		//设置缓存
		if (seconds) {
		   expire = timestamp + seconds
		}
		uni.setStorageSync(key + postfix, expire + "")
	} else {
		console.log("key不能空")
	}
}
/**  
 * 获取缓存   
 * @param  {[type]} k   [键名]  
 * @param  {[type]} def [获取为空时默认]  
 */  
function get(k, def) {  
    var deadtime = parseInt(uni.getStorageSync(k + postfix))   
    if (deadtime) {  
        if (parseInt(deadtime) < Date.parse(new Date()) / 1000) {  
            if (def) {  
                return def;  
            } else {  
                return false;  
            }  
        }  
    }  
    var res = uni.getStorageSync(k);  
    if (res) {  
        return res;  
    } else {  
        if (def == undefined  || def == "") {  
            def = false;   
        }  
        return def;  
    }  
}  

function remove(k) {  
    uni.removeStorageSync(k);  
    uni.removeStorageSync(k + postfix);  
}  

/**  
 * 清理所有缓存  
 * @return {[type]} [description]  
 */  
function clear() {  
    uni.clearStorageSync();  
}  

module.exports = {  
    put: put,  
    get: get,  
    remove: remove,  
    clear: clear,  
}