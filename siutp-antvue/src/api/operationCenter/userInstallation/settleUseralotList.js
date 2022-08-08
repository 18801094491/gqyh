import {axios} from "../../../utils/request";
//报装管理-报装安装进度确定
export const installationProgress = (data, _this) => {
    axios.post('/settle/settleUseralot/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.examinevisible = false;
                _this.installationProgressvisible = false;
                _this.$message.info(res.message);
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
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

//合同管理-客户名称下拉列表
export const getQueryCustomerNamesListData = (data, _this) => {
    axios.get('/settle/contract/queryCustomerNames', {
        params: data
    })
        .then(res => {
            _this.dataSource = [];
            if (res.code * 1 == 200) {

                let list = res.result;
                list.map(index => {
                    _this.dataSource.push({
                        constomerName: index.customerName,
                        customerId: index.id
                    });
                })

            } else {
                _this.$message.info(res.message);
            }
        })
}
