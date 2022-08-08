import {
    axios
} from '@/utils/request';
// table关联的api
export const URL = {
    list: "/equipment/store/list",
    delete: "/settle/optStore/delete",
    deleteBatch: "/settle/optStore/deleteBatch",
    exportXlsUrl: "settle/optStore/exportXls",
    importExcelUrl: "settle/optStore/importExcel",
}
//获取用户信息
export const getUserData = (data, _this) => {
    axios.get('/sys/user/idList', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.managerList = [];
                _this.managerList2 = [];
                list.map(index => {
                    _this.managerList.push({
                        id: index.id,
                        name: index.name
                    })
                    _this.managerList2.push({
                        id: index.id,
                        name: index.name
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//仓库管理-设置库管-查询树状仓库列表
export const storeTreeStore = (_this) => {
    axios.get('/equipment/store/treeStore')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result.treeList;
                _this.treeData = list;
                _this.expandedKeys = ['0'];
            } else {
                _this.$message.info(res.message);

            }
        })
}
//仓库管理-设置库管-查询管理员管理的仓库
export const storeGetStore = (data, _this) => {
    axios.get('/equipment/store/getStore', {
        params: data
    })
        .then(res => {
            _this.checkedKeys = [];
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.checkedKeys.push(index);
                })

            } else {
                _this.$message.info(res.message);

            }
        })
}
//仓库管理-设置库管-设置仓库管理员
export const storeSetManager = (data, _this) => {
    axios.post('/equipment/store/setManager', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.loadData();
            } else {
                _this.$message.info(res.message);

            }
        })
}

export const URL1 = {
    add: "/equipment/store/add",
    edit: "/equipment/store/edit",
}
//仓库管理-管理员下拉获取
export const getStoreUserListData = (data, _this) => {
    axios.get('/equipment/store/userList', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.managerList = [];
                _this.managerList2 = [];
                list.map(index => {
                    _this.managerList.push({
                        id: index.id,
                        name: index.name
                    })
                    _this.managerList2.push({
                        id: index.id,
                        name: index.name
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}


