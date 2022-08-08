import { getAction } from '@/api/manage'

// 设备台账树
export const queryLabelTreeList = (params)=>getAction("/equipment/optLabel/queryTreeList",params);
export const searchLabelByKeywords = (params)=>getAction("/equipment/optLabel/searchBy",params);