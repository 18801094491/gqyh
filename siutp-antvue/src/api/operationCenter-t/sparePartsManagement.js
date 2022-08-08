import {
    axios
} from '@/utils/request';
//备品备件管理-库存管理-查询
export const getOptSparepartData = (data, _this) => {
    axios.get('/equipment/optSparepart', {
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
                            id: index.id,
                            sparepartName: index.sparepartName,
                            sparepartSpecs: index.sparepartSpecs,
                            storeName: index.storeName,
                            supplierName: index.supplierName,
                            warnAmount: index.warnAmount,
                            sparepartSn: index.sparepartSn,
                            supplierId: index.supplierId,
                            state: index.state,
                            storeId: index.storeId,
                            sparepartModel: index.sparepartModel
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
//备品备件管理-库存管理-添加or修改
export const addOrChangeOptSparepartOneData = (data, _this) => {
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
//获取供应商值-新增或修改
export const getQueryNameList = (_this) => {
    axios.get('/equipment/optSupplier/queryNameList')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.supplierClassificationList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//备品备件管理-出库单新增修改
export const addOptStoreBillOut = (data, _this) => {
    axios.post('/equipment/optSparepart/outBill', data)
        .then(res => {
            _this.loading = false;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.stockOutvisible = false;
                _this.updata();
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
//根据资源名称获取资源类型
export const getEquipmentModelData = (data, _this) => {
    axios.get('/sys/cate/' + data.pcate)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.equipmentModelList = [];
                _this.equipmentMode = '';
                //              _this.equipmentSpecs = '';
                _this.equipmentModelList = [];
                _this.equipmentSpecsList = [];

                let list = res.result;
                list.map(index => {
                    _this.equipmentModelList.push({
                        code: index.code,
                        title: index.title
                    })
                });

            } else {
                _this.$message.info(res.message);
            }
        })
}
//根据资源类型获取规格类型
export const getEquipmentSpecsData = (data, _this) => {
    _this.equipmentSpecsList = [];
    axios.get('/sys/cate/' + data.pcate)
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.equipmentSpecsList.push({
                        code: index.code,
                        title: index.title
                    })
                });
            } else {
                _this.$message.info(res.message);
            }
        })

}
//获取设备类型值
export const getEquipmentTypeData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206872024991416321'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.equipmentTypeList = list;
                if (_this.modelTypeList) {
                    _this.modelTypeList = list;
                }
                if (_this.modelTypeList2) {
                    _this.modelTypeList2.push({
                        code: '0',
                        title: '全部'
                    });
                    for (let i = 0; i < list.length; i++) {
                        _this.modelTypeList2.push({
                            code: list[i].code,
                            title: list[i].title
                        })
                    }
                }
            } else {
                _this.$message.info(res.message);
            }

        })
}
//备品备件管理-调整库存报警值
export const optSparepartEditWarnAmount = (data, _this) => {
    axios.get('/equipment/optSparepart/editWarnAmount', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.stockEarlyWarningvisible = false;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//备品备件管理-出库列表获取
export const getOptStoreBillOut = (data, _this) => {
    axios.get('/equipment/optSparepart/outAll', {
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
