import { axios } from '@/utils/request'

export const getAction =  function (url,parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}

// 获取字典信息
export const getDictValue =  function (codeValue,parameter) {
    return axios({
        url: `/sys/dict/getDictValue/${codeValue}`,
        method: 'get',
        params: parameter
    })
}