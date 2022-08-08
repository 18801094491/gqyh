import { axios } from "../../utils/request";
import { getAction, putAction, postAction } from '@/api/manage'

export const roleStartOrStop = (data, _this) => {
    axios.get('/sys/role/startOrStop', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//权限管理
export const queryTreeListForRole = (params) => getAction("/sys/role/queryTreeList", params);
export const queryRolePermission = (params) => getAction("/sys/permission/queryRolePermission", params);
export const saveRolePermission = (params) => postAction("/sys/permission/saveRolePermission", params);