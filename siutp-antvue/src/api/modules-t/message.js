import { getAction,deleteAction,putAction,postAction} from '@/api/manage'
// 重复校验
export const duplicateCheck = (params)=>getAction("/sys/duplicate/check",params);