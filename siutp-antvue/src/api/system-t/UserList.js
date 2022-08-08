import { getAction,deleteAction,putAction,postAction} from '@/api/manage'

//用户管理

export const frozenBatch = (params)=>putAction("/sys/user/frozenBatch",params);