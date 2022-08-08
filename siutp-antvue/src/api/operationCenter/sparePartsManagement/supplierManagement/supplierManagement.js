import {
    axios
} from '@/utils/request';
//供应商管理-新增供应商信息
export const addSupplierInformationData = (data, _this) => {
    axios.post('/equipment/optSupplier/add', data)
        .then(res => {

            if (res.code * 1 == 200) {
                _this.visible = false;
                _this.$message.info(res.message);
                _this.updata();
                _this.addReset();
            } else {
                _this.visible = false;
                _this.$message.info(res.message);
                _this.addReset();
            }
        })
}

//供应商管理-获取供应商信息
export const getSupplierInformationData = (data, _this) => {
    axios.get('/equipment/optSupplier/queryById', {
        params: data
    })
        .then(res => {


            let obj = res.result;
            if (res.code * 1 == 200) {
                _this.supplierCode = obj.supplierSn;
                _this.supplierName = obj.supplierName;
                _this.supplierCategory = obj.supplierCategory;
                _this.supplierBusinessLicenseNo = obj.supplierCert;
                _this.supplyEquipment = obj.supplierGoods;
                _this.contactNumber = obj.contactsPhone;
                _this.supplierStatus = obj.supplierStatus;
                _this.contacts = obj.contacts;
                _this.changeId = obj.id;

            } else {
                _this.$message.info(res.message);
            }

        })
}
//供应商管理-获取供应商信息列表
export const getSupplierInformationListData = (data, _this) => {
    axios.get('/equipment/optSupplier/list', {
        params: data
    })
        .then(res => {
            _this.dataSource = [];
            _this.loading = false;
            if (res.code * 1 == 200) {
                if (res.result.records.length) {
                    let list = res.result.records;
                    list.map(index => {
                        _this.dataSource.push({
                            supplierCode: index.supplierSn,
                            supplierName: index.supplierName,
                            supplierCategory: index.supplierCategory,
                            supplierBusinessLicenseNo: index.supplierCert,
                            createdDate: index.createTime,
                            supplyEquipment: index.supplierGoods,
                            contacts: index.contacts,
                            contactNumber: index.contactsPhone,
                            id: index.id,
                            status: index.supplierStatus == 0 ? false : true
                        });
                    });
                    _this.ipagination.current = res.result.current;
                    _this.ipagination.total = res.result.total;

                } else {

                    _this.ipagination.current = 1;
                    _this.ipagination.total = 0;
                }

            } else {

                _this.$message.info(res.message);
            }
        })
}
//供应商管理-修改供应商信息
export const changeSupplierInformationData = (data, _this) => {
    axios.post('/equipment/optSupplier/edit', data)
        .then(res => {

            if (res.code * 1 == 200) {
                _this.visible = false;
                _this.$message.info(res.message);
                _this.updata();
                _this.addReset();
            } else {
                _this.visible = false;
                _this.$message.info(res.message);
                _this.addReset();
            }
        })
}
//获取设备类别值
export const getEquipmentCategoryData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206838473407348737'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.equipmentCategoryList = list;
            } else {
                _this.$message.info(res.message);
            }

        })
}
//获取供应商分类值
export const getSupplierClassificationData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206871578809745410'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.supplierClassificationList = list;
                if (_this.supplierClassificationList2 != undefined) {
                    _this.supplierClassificationList2.push({
                        code: '0',
                        title: '全部'
                    });
                    for (let i = 0; i < list.length; i++) {
                        _this.supplierClassificationList2.push({
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
//供应商状态更改
export const supplierStatusChange = (data, _this) => {
    axios.get('/equipment/optSupplier/startupAndShutdown', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.updata();
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        })
}
