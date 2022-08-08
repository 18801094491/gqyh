import { axios } from '@/utils/request'
//get
function getAction(url,parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}
export const queryDistrictTreeList = (params)=>getAction("/settle/district/queryTreeList",params);
export const searchDistrictByKeywords = (params)=>getAction("/settle/district/searchBy",params);