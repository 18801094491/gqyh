import { getAction,deleteAction,putAction,postAction} from '@/api/manage'
// 部门管理
export const queryDepartTreeList = (params)=>getAction("/sys/sysDepart/queryTreeList",params);
export const searchByKeywords   = (params)=>getAction("/sys/sysDepart/searchBy",params);