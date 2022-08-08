import { getAction, deleteAction, putAction, postAction } from '@/api/manage'
import { axios } from "../../utils/request";
//用户管理
const addUser = (params) => postAction("/sys/user/add", params);
const editUser = (params) => putAction("/sys/user/edit", params);
const queryUserRole = (params) => getAction("/sys/user/queryUserRole", params);
//角色管理
const queryall = (params) => getAction("/sys/role/queryall", params);
//重复校验
const duplicateCheck = (params) => getAction("/sys/duplicate/check", params);
//部门管理
const queryIdTree = (params) => getAction("/sys/sysDepart/queryIdTree", params);
//获取个人中心数据
export const getMyuserData = (_this) => {
    axios.get('/sys/user/my')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.edit(res.result);
            } else {
                _this.$message.info(res.message);
            }
        })
}
export {
    addUser,
    editUser,
    queryUserRole,
    queryall,
    queryIdTree,
    duplicateCheck

}