import { getAction, deleteAction, putAction, postAction } from '@/api/manage'
//数据字典
export const editDict = (params) => putAction("/sys/dict/edit", params);
export const treeList = (params) => getAction("/sys/dict/treeList", params);
export const addDictItem = (params) => postAction("/sys/dictItem/add", params);
export const editDictItem = (params) => putAction("/sys/dictItem/edit", params);