import {
    axios
} from '@/utils/request';
//审核申请
export const getOptGoodsApplyMyInfo = (data, _this) => {
    axios.get('/equipment/optGoodsApply/myInfo', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {
                if (res.result) {
                    let list = res.result.records;
                    list.map(index => {
                        _this.dataSource.push({
                            createBy: index.createBy,
                            createTime: index.createTime,
                            emergenText: index.emergenText,
                            emergentValue: index.emergentValue,
                            id: index.id,
                            reason: index.reason,
                            useTool: index.useTool,
                            verifyStatus: index.verifyStatus,
                            verifyStatusValue: index.verifyStatusValue,
                            wantedTime: index.wantedTime
                        })
                    })
                    _this.ipagination.current = res.result.current;
                    _this.ipagination.total = res.result.total;
                }


            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;
            }
        })
}
//审核状态
export const getOptApplyVerifyQueryAuditStatus = (_this) => {
    axios.get('/equipment/optApplyVerify/queryAuditStatus')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.queryAuditStatusList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//审核添加
export const addOptApplyVerifyOne = (data, _this) => {
    axios.post('/equipment/optApplyVerify/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.visible = false;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//审核历史
export const getOptApplyVerify = (data, _this) => {
    axios.get('/equipment/optApplyVerify/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.optApplyVerifyList = res.result;
                if (res.result.length) {
                    _this.ecuvisible2 = true;
                } else {
                    _this.$message.info('暂无审核历史!');
                }
            } else {
                _this.$message.info(res.message);
            }
        })
}

//申请历史-我的
export const getMyOptGoodsApply = (data, _this) => {
    axios.get('/equipment/optGoodsApply', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource.push({
                        createBy: index.createBy,
                        createTime: index.createTime,
                        expectbackTime: index.expectbackTime,
                        id: index.id,
                        reason: index.reason,
                        useTool: index.useTool,
                        verifyStatus: index.verifyStatus,
                        verifyStatusValue: index.verifyStatusValue,
                        wantedTime: index.wantedTime
                    })
                })
                _this.ipagination.current = res.result.current;
                _this.ipagination.total = res.result.total;

            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;
            }
        })
}

//申请历史总
export const getOptGoodsApplyApplyHistoryAll = (data, _this) => {
    axios.get('/equipment/optGoodsApply/applyHistoryAll', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource.push({
                        createBy: index.createBy,
                        createTime: index.createTime,
                        expectbackTime: index.expectbackTime,
                        id: index.id,
                        reason: index.reason,
                        useTool: index.useTool,
                        verifyStatus: index.verifyStatus,
                        verifyStatusValue: index.verifyStatusValue,
                        wantedTime: index.wantedTime
                    })
                })
                _this.ipagination.current = res.result.current;
                _this.ipagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;
            }
        })
}


