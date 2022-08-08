import {
    axios
} from '@/utils/request';


//备品备件管理-入库列表获取
export const getOptStoreBillIn = (data, _this) => {
    axios.get('/equipment/optSparepart/inAll', {
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
                            amount: index.amount,
                            batchSn: index.batchSn,
                            createBy: index.createBy,
                            createTime: index.createTime,
                            id: index.id,
                            sparepartId: index.sparepartId,
                            sparepartName: index.sparepartName,
                            sparepartSpecs: index.sparepartSpecs,
                            storeId: index.storeId,
                            storeName: index.storeName,
                            productDate: index.productDate,
                            endDate: index.endDate
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

//备品备件管理-入库备品信息
export const getSparePartList = (_this) => {
    axios.get('/equipment/optSparepart/dropdown')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.sparePartList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//查询仓库下拉选
export const getQueryStoreList = (_this) => {
    axios.get('/equipment/operatTool/queryStoreList')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.queryStoreList = [];
                let list = res.result;
                list.map(index => {
                    _this.queryStoreList.push({
                        storeSn: index.storeId,
                        storePosition: index.storePosition
                    })
                })

            } else {
                _this.$message.info(res.message);
            }
        })
}

//备品备件管理-入库添加
export const addOptStoreBill = (data, _this) => {
    axios.post('/equipment/optSparepart/inBill', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.ipagination.current = 1;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
