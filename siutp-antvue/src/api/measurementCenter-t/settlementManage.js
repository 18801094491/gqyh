import {
    axios
} from '@/utils/request';
// 结算管理--分页列表
export const getSettleMangeList = (data, _this) => {
    axios.get('/settle/statistics', {
        params: data
    }).then(res => {
        if (res.code * 1 == 200) {
            let list = res.result;
            _this.dataSource = [];
            _this.dataSource = list
            _this.dataSource.forEach(item => {
                item.action = item.customerName
            })
            _this.loading = false;
        } else {
            _this.$message.info(res.message);
        }
    })
}

// 结算管理--手工结算生成结算单/settle/contract/queryById
export const settleByHand = (data, _this) => {
    axios.post('/settle/statistics/manualSettle', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.settlementVisble = false;
                _this.updata();
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 结算管理--分页列表
export const getSettleMangeList2 = (data, _this) => {
    axios.get('/settle/statistics', {
        params: data
    }).then(res => {
        if (res.code * 1 == 200) {
            let list = res.result;
            _this.settleDataSource = [];
            _this.handleSettleData(list);
            _this.handleTotal()
            _this.settleLoading = false;
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 结算管理--点击合同名称查看详情
export const getContractDetail = (data, _this) => {
    axios.get('/settle/contract/queryDetail', { params: data })
        .then(res => {
            if (res.code * 1 === 200) {
                let result = res.result;
                _this.model = Object.assign({}, result);
                _this.fileList = [];
                if (result && _this.model.fileUrl) {
                    _this.$nextTick(() => {
                        _this.fileList.push({
                            filePath: _this.model.fileUrl,
                            fileName: _this.model.contractName
                        })
                    });
                }
            } else {
                _this.$message.info(res.message);
            }
        })
}