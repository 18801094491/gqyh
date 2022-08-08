import { axios } from '@/utils/request'
// left----get
function getAction(url, parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}
export const queryDistrictTreeList = (params) => getAction("/settle/district/queryTreeList", params);
export const searchDistrictByKeywords = (params) => getAction("/settle/district/searchBy", params);
//right

export const getActions = function (url, parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}

/**
 * 下载文件 用于excel导出
 * @param url
 * @param parameter
 * @returns {*}
 */
export const downFile = function (url, parameter) {
    return axios({
        url: url,
        params: parameter,
        method: 'get',
        responseType: 'blob'
    })
}

// 获取字典信息
export const getDictValue = function (codeValue, parameter) {
    return axios({
        url: `/sys/dict/getDictValue/${codeValue}`,
        method: 'get',
        params: parameter
    })
}