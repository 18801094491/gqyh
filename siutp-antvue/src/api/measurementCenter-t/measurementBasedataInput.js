import {
    axios
} from '@/utils/request';
import pick from "lodash.pick";
//contractManage文件下
export const expireDateConfig = (data, _this) => {
    axios.post("/system/systemConfig/one", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.expireDateVisible = false;
        } else {
            _this.$message.info(res.message);
        }
    })
}
export const configByKey = (data, _this) => {
    axios({
        url: `/system/systemConfig/${data}`,
        methods: "get"
    }).then(res => {
        if (res.code == 200) {
            let result = res.result;
            _this.form.id = result.id;
            _this.form.configKey = result.configKey;
            _this.form.setExpireDate = result.configValue;
        } else {
            _this.$message.info(res.message);
        }
    })
}
//meteradd.js
// 合同管理--水表绑定
export const customerWaterbind = (data, _this) => {
    axios.post("/settle/contractWater/bind", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.visible = false;
            _this.$emit('closeModal', 'openAddVisible')
            _this.$parent.upData();
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 合同管理--获取水表下拉框
export const getWaterbindList = (data, _this) => {
    axios({
        url: "/settle/contractWater/queryCustomerBindedWater",
        methods: "get",
        params: data
    }).then(res => {
        if (res.code == 200) {
            let list = res.result;
            _this.waterbindList = [];
            list.map(item => {
                _this.waterbindList.push({
                    equipmentId: item.equipmentId,
                    equipmentName: item.equipmentName,
                })
            })
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 合同管理--获取计价规则下拉框
export const getContractRule = (data, _this) => {
    axios({
        url: "/settle/contractWater/queryContractRule",
        methods: "get",
        params: data
    }).then(res => {
        if (res.code == 200) {
            let list = res.result;
            _this.contractRuleList = [];
            list.map(item => {
                _this.contractRuleList.push({
                    id: item.id,
                    ruleName: item.ruleName,
                })
            })
        } else {
            _this.$message.info(res.message);
        }
    })
}
//meterlist.js
// 合同管理---水表绑定列表分页
export const waterbindList = (data, _this) => {
    axios({
        url: "/settle/contractWater",
        methods: "get",
        params: data
    }).then(res => {
        _this.dataSource = [];
        _this.loading = false;
        if (res.code === 200) {
            _this.dataSource = res.result.records;
            _this.pagination.current = res.result.current;
            _this.pagination.total = res.result.total;
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 合同管理--水表解除绑定
export const customerWaterUnbind = (data, _this) => {
    axios({
        url: "/settle/contractWater/unbind",
        methods: "get",
        params: data
    }).then(res => {
        if (res.code === 200) {
            _this.unbindVisible = false;
            _this.$message.info(res.message);
            setTimeout(() => {
                _this.upData();
            }, 1000)
        } else {
            _this.$message.info(res.message);
        }
    })
}
//modal.js
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

//合同管理-分类下拉列表
export const getContractTypeList = (_this) => {
    axios.get('/sys/dict/getDictValue/contract_type', {})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.typeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//ruleadd.js
export const getRuleTypeList = (_this) => {
    axios.get('/sys/dict/getDictValue/count_price_type', {})
        .then(res => {
            if (res.code * 1 === 200) {
                _this.ruleTypeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//合同管理-计价规则新增-获取规则类型列表
export const getPricingRuleDetailById = (_this, data) => {
    axios.get('/settle/contractWaterRule/detail', {
        params: { ruleId: data.id }
    })
        .then(res => {
            if (res.code * 1 === 200) {
                _this.visible = true
                _this.form.resetFields();
                _this.model = Object.assign({}, res.result);
                _this.model.ruleTypeName = data.ruleTypeName
                _this.$nextTick(() => {
                    _this.form.setFieldsValue(pick(_this.model, 'ruleType', 'ruleName', 'benchPrice', 'setUp', 'setTime'))
                    _this.form.ruleType = _this.model.ruleType
                    if (_this.form.ruleType !== '1') {
                        _this.setInitItems()
                    }
                    _this.form = Object.assign({}, _this.form);
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//rulelist.js
//合同管理-获取计价规则列表
export const getPrivingRulesList = (_this, data) => {
    axios.get('/settle/contractWaterRule', { params: data })
        .then(res => {
            if (res.code * 1 === 200) {
                _this.dataSource = res.result.records;
                _this.pagination.current = res.result.current;
                _this.pagination.total = res.result.total;

            } else {
                _this.pagination.current = 0;
                _this.pagination.total = 1;
                _this.$message.info(res.message);
            }
        })
}
