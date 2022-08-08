import request from '../common/request.js'


//携带token
function getList(type) {
	var ty = type
	return request({
		method: 'GET',
		url: '/gis/iot/listNew/' + ty
	})
}

export {
	getList
}
