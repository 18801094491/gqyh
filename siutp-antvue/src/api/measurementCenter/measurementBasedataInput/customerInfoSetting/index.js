import {
    axios
} from '@/utils/request';
//客户信息管理-分页列表查询
export const getCustomerList = (data, _this) => {
    axios.get('/settle/customer/list', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            if (res.code == 200) {
                _this.dataSource = [];
                let list = res.result.records;
                list.map(index => {
                    _this.dataSource.push({
                        contacterName: index.contacterName,
                        contacterPhone: index.contacterPhone,
                        contractName: index.contractName,
                        contractUrl: index.contractUrl,
                        createTime: index.createTime,
                        customerName: index.customerName,
                        customerSn: index.customerSn,
                        customerType: index.customerType,
                        customerTypeName: index.customerTypeName,
                        customerStatus: index.customerStatus,
                        customerStatusName: index.customerStatusName,
                        id: index.id,
                        payMode: index.payMode,
                        price: index.price,
                        priceMode: index.priceMode,
                        customerAddress: index.customerAddress,
                        customeDuty: index.customeDuty,
                        customeBankAccout: index.customeBankAccout,
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
//获取水价模式
export const getWaterPriceMode = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1212658786275168257'
        }
    })
        .then(res => {
            if (res.code == 200) {
                _this.waterPriceModeList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取付款模式
export const getPaymentMode = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1212659004043431938'
        }
    })
        .then(res => {
            if (res.code == 200) {
                _this.paymentModeList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}
//客户信息管理-添加
export const addCustomer = (data, _this) => {
    axios.post('/settle/customer/add', data)
        .then(res => {
            if (res.code == 200) {
                _this.$message.info(res.message);
                _this.searchQuery();
                _this.visible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//点击修改回显客户信息管理
export const getChangeCustomerInformation = (data, _this) => {
    axios.get('/settle/customer/queryById', {
        params: data
    })
        .then(res => {
            if (res.code == 200) {
                let obj = res.result;
                if (obj.contractName) {
                    _this.fileList.push({
                        fileName: obj.contractName,
                        filePath: obj.contractUrl
                    })
                }

                _this.contacterPhone = obj.contacterPhone;
                _this.contacterName = obj.contacterName;
                _this.customerName = obj.customerName;
                _this.customerType = obj.customerType;
                _this.payMode = obj.payMode;
                _this.priceMode = obj.priceMode;
                _this.price = obj.price;
                _this.urlId = obj.contractId;
                _this.visible = true;
                _this.customerAddress = obj.customerAddress;
                _this.customerSn = obj.customerSn; // 客户编码
                _this.customeDuty = obj.customeDuty; // 客户税号
                _this.customeBankAccout = obj.customeBankAccout;	// 客户银行账号
                _this.customerStatus = obj.customerStatus; // 客户状态
            } else {
                _this.$message.info(res.message);
            }
        })
}

//客户信息管理-编辑
export const changeCustomerInformation = (data, _this) => {
    axios.post('/settle/customer/edit', data)
        .then(res => {
            if (res.code == 200) {
                _this.$message.info(res.message);
                _this.searchQuery();
                _this.visible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//客户信息管理-新增上传
export const customerInformationUploadFile = (data, _this) => {
    axios.post('/sys/file/upload', data)
        .then(res => {

            if (res.code * 1 == 200) {

                _this.fileList.push({
                    fileName: res.result.fileName,
                    filePath: res.result.filePath
                });

                $('#uploadBtn').val('');
            } else {
                _this.$message.info(res.message);
            }
        })
}
//查询已绑定的动态水表信息
export const getBindWatermeterList = (data, _this) => {
    axios.get('/settle/customer/getBindWatermeterList', {
        params: data
    })
        .then(res => {
            _this.loading3 = false;
            _this.dataSource3 = [];
            if (res.code * 1 == 200) {

                let list = res.result;
                list.map(index => {
                    _this.dataSource3.push({
                        customerWaterSn: index.customerWaterSn,
                        equipData: index.equipData,
                        equipmentLocation: index.equipmentLocation,
                        equipmentName: index.equipmentName,
                        equipmentSn: index.equipmentSn,
                        equipmentId: index.equipmentId,
                        id: index.id,
                        bindStatus: index.bindStatus,
                        operationTime: index.unbindTime ? index.unbindTime : index.bindTime,

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
//查询出所有水表信息
export const getWatermeterList = (data, _this) => {
    axios.get('/settle/customer/getWatermeterList', {
        params: data
    })
        .then(res => {
            _this.loading2 = false;
            _this.dataSource2 = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource2.push({
                        equipmentInfo: index.equipmentInfo, // 新修改
                        equipmentName: index.equipmentName,
                        equipmentSn: index.equipmentSn,
                        bindStatus: index.bindStatus,
                        id: index.id
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
//客户绑定水表
export const bindCustomerEquip = (data, _this) => {
    axios.post('/settle/customer/bindCustomerEquip', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.waterMeterInformation2();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//解绑客户水表
export const unBindCustomerEquip = (data, _this) => {
    axios.get('/settle/customer/unBindCustomerEquip', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.waterMeterInformation2();
                // _this.ipagination3.total = 1;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取所属标段值
export const getBidSegmentData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
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

// 获取字典信息
export function getDictValue(codeValue,parameter) {
    return axios({
        url: `/sys/dict/getDictValue/${codeValue}`,
        method: 'get',
        params: parameter
    })
}