import {
    axios
} from '@/utils/request';
//工具台账管理-分页列表查询
export const getDevOpsToolLedgerListData = (data, _this) => {
    axios.get('/equipment/operatTool/list', {
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
                            id: index.id,
                            borrowedNum: index.borrowedNum,
                            damageNum: index.damageNum,
                            storeId: index.storeId,
                            storeName: index.storeName,
                            supplierId: index.supplierId,
                            supplierName: index.supplierName,
                            toolFactory: index.toolFactory,
                            toolModel: index.toolModel,
                            toolName: index.toolName,
                            toolSn: index.toolSn,
                            toolType: index.toolType,
                            toolTypeCode: index.toolTypeCode,
                            total: index.total,
                            usedNum: index.usedNum,
                            batchSn: index.batchSn
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
//工具台账管理-修改
export const editDevOpsToolLedger = (data, _this) => {
    axios.post('/equipment/operatTool/edit', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.visible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取工具类型
export const getToolType = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1215160876508712962'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.toolTypeList = [];
                let list = res.result;
                list.map(index => {
                    _this.toolTypeList.push({
                        code: index.code,
                        title: index.title
                    })
                })

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

//运维工具台账-运维工具管理
export const operatToolInToolStore = (data, _this) => {
    axios.post('/equipment/operatTool/inToolStore', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.batchList = res.result;
                _this.visible = false;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//运维台账维护-运维工具台账-出库
export const getOperatToolBorrowDetailData = (data, _this) => {
    axios.get('/equipment/operatTool/borrowDetail', {
        params: data
    })
        .then(res => {
            _this.loading2 = false;
            _this.dataSource2 = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource2.push({
                        borrowTimes: index.borrowTimes,
                        goodsId: index.goodsId,
                        itemSn: index.itemSn,
                        itemStatus: index.itemStatus,
                        itemStatusName: index.itemStatusName
                    })
                })
                _this.ipagination2.current = res.result.current;
                _this.ipagination2.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.dataSource2 = [];
                _this.ipagination2.current = 0;
                _this.ipagination2.total = 1;
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

//运维台账维护-运维工具台账-出库-出库-获取申请单号
export const getOperatToolGoodsApply = (data, _this) => {
    axios.get('/equipment/operatTool/goodsApply', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.applicantIdList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//运维台账维护-运维工具台账-出库-出库-获取状态
export const getGoodsItemStatus = (_this) => {
    axios.get('/sys/dict/getDictValue/goods_item_status')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.goodsItemstatusList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//运维台账维护-运维工具台账-出库-维护
export const operatToolEditStatus = (data, _this) => {
    axios.get('/equipment/operatTool/editStatus', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.takeDeliveryGoods();
                _this.maintainvisible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//运维台账维护-运维工具台账-出库-归还
export const operatToolBack = (data, _this) => {
    axios.get('/equipment/operatTool/back', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.takeDeliveryGoods();
                _this.returnvisible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//运维台账维护-运维工具台账-出库-出库
export const operatToolGoodsOut = (data, _this) => {
    axios.post('/equipment/operatTool/goodsOut', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.takeDeliveryGoods();
                _this.warehousingOrReturnvisible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//运维台账维护-运维工具台账-出库-借用记录
export const getOperatToolHistoryData = (data, _this) => {
    axios.get('/equipment/operatTool/history', {
        params: data
    })
        .then(res => {
            _this.loading3 = false;
            _this.dataSource3 = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource3.push({
                        userName: index.userName,
                        description: index.description,
                        borrowTime: index.borrowTime,
                        backTime: index.backTime
                    })
                })
                _this.ipagination3.current = res.result.current;
                _this.ipagination3.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.dataSource3 = [];
                _this.ipagination3.current = 0;
                _this.ipagination3.total = 1;
            }
        })
}
//根据资源类型获取规格类型
export const getEquipmentSpecsData = (data, _this) => {
    //	_this.equipmentSpecs = '';
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