import { getAction } from '@/api/manage'
//获取系统访问量
const getLoginfo = (params) => getAction("/sys/loginfo", params);
const getVisitInfo = (params) => getAction("/sys/visitInfo", params);

export {
    getLoginfo,
    getVisitInfo
}