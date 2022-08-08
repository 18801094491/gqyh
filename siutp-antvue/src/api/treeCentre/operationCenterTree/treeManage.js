import {
    axios
} from '@/utils/request';
//获取所属标段值
export const getBidSegmentData = (_this) => {
    axios.get('/centre/universal/labelList', {
        params: {
            pid: '1206872782805680130'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.bidSegmentList = list;
            } else {
                _this.$message.info(res.message);
            }


        })
}
//获取设备类型值
export const getEquipmentTypeData = (_this) => {
    axios.get('/centre/universal/typeList', {
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
                        code: '',
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

//获取供应商值-筛选
export const getQueryNameList2 = (_this) => {
    axios.get('/equipment/optSupplier/queryNameList')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;

                list.map(index => {
                    _this.supplierClassificationList2.push({
                        id: index.id,
                        supplierName: index.supplierName
                    })
                })
                console.log(_this.supplierClassificationList2)
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取设备台账列表
export const getEquipmentLedgerData = (data, _this) => {
    axios.get('/centre/opt/equ/equlist', {
        params: data
    })
        .then(res => {
            _this.dataSource = [];
            _this.loading = false;
            if (res.code * 1 == 200) {


                let list = res.data.records;
                list.map(index => {
                    _this.dataSource.push({
                        number: index.equipmentSn,
                        equipmentName: index.equipmentName,
                        equipmentCategory: index.equipmentCategory,
                        equipmentType: index.equipmentType,
                        scaleModel: index.equipmentMode,
                        bidSegment: index.equipmentSection,
                        position: index.equipmentLocation,
                        supplier: index.equipmentSupplier,
                        state: index.equipmentStatus,
                        id: index.id,
                        bindStatus: index.bindStatus,
                        scale: index.equipmentSpecs,
                        equipmentOperating: index.equipmentOperating,
                        equipmentPurchase: index.equipmentPurchase,
                        equipmentRevstop: index.equipmentRevstop,
                        equipmentRevstopText: index.equipmentRevstopText,
                        equipmentSpecsName: index.equipmentSpecsName,
                        equipmentModelName: index.equipmentModeName,
                        equipmentTypeName: index.equipmentType,
                        equipmentSupplierName: index.equipmentSupplier,
                        labelName: index.labelName,
                        labelId: index.labelId,
                        planDate:index.planDate,
                        countDays:index.countDays,
                        dispatchStatus:index.dispatchStatus,
                        personResponsible:index.personResponsible,
                        personResponsibleName:index.personResponsibleName,
                        
                    })
                })
                _this.ipagination.current = res.data.current;
                _this.ipagination.total = res.data.total;
            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;
            }

        })
}