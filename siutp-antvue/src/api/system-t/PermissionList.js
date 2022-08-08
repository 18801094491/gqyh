import { getAction, putAction, postAction } from '@/api/manage'
/**
 * 菜单管理
 * @param {*} params 
 */
//权限管理
export const getPermissionRuleList = (params) => getAction("/sys/permission/getPermRuleListByPermId", params);
export const queryPermissionRule = (params) => getAction("/sys/permission/queryPermissionRule", params);
export const addPermission= (params)=>postAction("/sys/permission/add",params);
export const editPermission= (params)=>putAction("/sys/permission/edit",params);
export const queryTreeList = (params)=>getAction("/sys/permission/queryTreeList",params);