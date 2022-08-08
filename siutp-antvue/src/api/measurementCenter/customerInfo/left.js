import { axios } from '@/utils/request'
//get
function getAction(url,parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}
const queryDistrictTreeList = (params)=>getAction("/settle/district/queryTreeList",params);
const searchDistrictByKeywords = (params)=>getAction("/settle/district/searchBy",params);
export {
    queryDistrictTreeList,
    searchDistrictByKeywords
}