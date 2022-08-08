import { getAction, putAction, postAction } from '@/api/manage'
/**
 * 部门人员-用户信息-用户录入
 * @param {*} params 
 */
//用户管理
export const addUser = (params) => postAction("/sys/user/add", params);
export const editUser = (params) => putAction("/sys/user/edit", params);
export const queryUserRole = (params) => getAction("/sys/user/queryUserRole", params);
//移动端权限
export const getAppRoleList = (params) => getAction("/sys/role/getAppRoleList", params);
//角色管理
export const queryall = (params) => getAction("/sys/role/queryall", params);
// 重复校验
export const duplicateCheck = (params) => getAction("/sys/duplicate/check", params);