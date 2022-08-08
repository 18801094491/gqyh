import { axios } from '@/utils/request'


//工具申请
export const optGoodsApplyOne = (data, _this) => {
    axios.post('/equipment/optGoodsApply/one', data)
        .then(res => {
            if (res.code * 1 == 200) {

                _this.$parent.updata();
                _this.react();
                _this.toolApplicativisible = false;
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        })
}

//工具申请-通过仓库查询工具信息
export const operatToolQueryBystoreId = (data, _this, index) => {
    axios.get('/equipment/optGoodsApply/queryTool', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.applicationContentList[index].operatToolList = [];

                _this.applicationContentList[index].operatToolList = res.result;


            } else {
                _this.$message.info(res.message);
            }
        })
}

//工具申请-通过工具类型跟仓库查询所有规格
export const optGoodsApplyQueryModel = (data, _this, index) => {
    axios.get('/equipment/optGoodsApply/queryModel', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.applicationContentList[index].toolModelList = [];

                _this.applicationContentList[index].toolModelList = res.result;


            } else {
                _this.$message.info(res.message);
            }
        })
}

//工具申请-紧急状态
export const optGoodsApplyQueryByStatus = (_this) => {
    axios.get('/sys/dict/getDictValue/exigence_state')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.emergentLevelList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}


//工具申请-规格获取
export const getToolMode = (data, _this, index) => {
    axios.get('/sys/cate/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.applicationContentList[index].toolModelList = [];

                _this.applicationContentList[index].toolModelList = res.result;

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
//修改工具申请回显
export const optGoodsApplyQueryByapplyId = (data, _this) => {
    axios.get('/equipment/optGoodsApply/queryByapplyId', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.toolApplicativisible = true;
                let obj = res.result;
                _this.applicationContentList = [];
                obj.applyItem.map((index, i) => {

                    _this.applicationContentList.push({
                        amount: index.amount,
                        dataNameCode: index.dataNameCode,
                        storeId: index.storeId,
                        operatToolList: [],
                        dataId: index.dataId,
                        toolModelList: [],
                        toolModelCode: index.dataId,
                    })
                    _this.queryStoreListChange2(i, index.storeId);
                    setTimeout(() => {
                        _this.operatToolListChange2(i, index.dataNameCode, index.storeId);
                    }, 800)
                });
                _this.emergentLevel = obj.emergentLevel;
                _this.wantedTime = obj.wantedTime;
                _this.expectbackTime = obj.expectbackTime;
                _this.reason = obj.reason;

                _this.$nextTick(() => {
                    _this.form.setFieldsValue({ wantedTime: obj.wantedTime ? moment(obj.wantedTime) : null });
                    _this.form.setFieldsValue({ expectbackTime: obj.expectbackTime ? moment(obj.expectbackTime) : null });
                })

            } else {
                _this.$message.info(res.message);
            }
        })
}

