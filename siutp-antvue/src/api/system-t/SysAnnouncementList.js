import { getAction, deleteAction, putAction, postAction } from '@/api/manage'
//系统通告
export const doReleaseData = (params) => getAction("/sys/annountCement/doReleaseData", params);
export const doReovkeData = (params) => getAction("/sys/annountCement/doReovkeData", params);