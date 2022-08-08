import { getAction,deleteAction,putAction,postAction} from '@/api/manage'
// 部门管理
export const queryIdTree = (params)=>getAction("/sys/sysDepart/queryIdTree",params);