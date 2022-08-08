import { axios } from '@/utils/request'
export const getAction = function (url,parameter) {
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
export const downFile =  function (url,parameter){
    return axios({
        url: url,
        params: parameter,
        method:'get' ,
        responseType: 'blob'
    })
}

// 获取字典信息
export const getDictValue = function (codeValue,parameter) {
    return axios({
        url: `/sys/dict/getDictValue/${codeValue}`,
        method: 'get',
        params: parameter
    })
}