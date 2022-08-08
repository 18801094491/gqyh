import { axios } from '@/utils/request';
//设备类型
export const getEquipmentType = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A03')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.inequipmentTypeList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}

// 模型设备维护-分页列表查询
export function getAelist(parameter) {
    return axios({
        url: '/equipment/iotEquipment',
        method: 'get',
        params: parameter
    })
}

// 模型设备维护-添加或修改
export function setAeInfo(data) {
    return axios({
        url: '/equipment/iotEquipment/one',
        method: 'post',
        data: data
    })
}

//【采集设备管理】设备绑定
export function setUnbindEquipment(parameter) {
    return axios({
        url: '/equipment/gisModel/getIotInfoList',
        method: 'get',
        params: parameter
    })
}

//模型设备维护-分页列表查询
export function getbindEquipmentList(parameter) {
    return axios({
        url: '/equipment/iotEquipment',
        method: 'get',
        params: parameter
    })
}

export function setEquipmentOne(parameter) {
    return axios({
        url: '/equipment/optEquipment/getGisVoList',
        method: 'get',
        params: parameter
    })
}

export function getloadTreeChildrenList(parameter) {
    return axios({
        url: '/sys/category/loadTreeChildren',
        method: 'get',
        params: parameter
    })
}

//模型设备变量绑定-添加或修改
//equipment/iotEquipmentVariable/one
export function setiotEquipmentVariableOne(data) {
    return axios({
        url: 'equipment/iotEquipmentVariable/one',
        method: 'post',
        data: data
    })
}

//模型设备变量绑定-变量分页列表查询
// /equipment/iotEquipmentVariable/queryPageList
export function getEquipmentVariableList(data) {
    return axios({
        url: '/equipment/iotEquipmentVariable/queryPageList',
        method: 'get',
        params: data
    })
}

//模型设备变量绑定-通过id删除
// /equipment/iotEquipmentVariable/{id}
export function deliotEquipmentVariableOne(data) {
    return axios({
        url: '/equipment/iotEquipmentVariable/many',
        method: 'get',
        params: data
    })
}

//采集设备-按设备类型修改设备周期
export const iotEquipmentUpdateCycleByCate = (data, _this) => {
    axios.get('/equipment/iotEquipment/updateCycleByCate', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.bindEquipmentCycleVisible = false;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警信息模板-添加或修改
export const alarmModleOne = (data, _this) => {
    axios.post('/equipment/alarmModle/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.templateVisible = false;

                _this.handleAlarAmModelUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警信息模板-分页列表查询
export const alarmModle = (data, _this) => {
    axios.get('/equipment/alarmModle', {
        params: data
    })
        .then(res => {
            _this.alarmModelDataSource = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;
                list.map(index => {
                    _this.alarmModelDataSource.push({
                        alarmTitle: index.alarmTitle,
                        alarmValue: index.alarmValue,
                        createTime: index.createTime,
                        id: index.id
                    })
                })
                _this.alarmModelPagination.current = res.result.current;
                _this.alarmModelPagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);

                _this.alarmModelPagination.current = 0;
                _this.alarmModelPagination.total = 1;
            }


        })
}


//采集设备-告警信息模板-通过id删除
export const removealarmModleId = (data, _this) => {
    axios.delete('/equipment/alarmModle/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);

                _this.handleAlarAmModelUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警规则-添加规则-获取变量信息
export const getVarRule = (data, _this) => {
    axios.get('/equipment/iotEquipmentVariable/queryPageList', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result.records;
                _this.varList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警规则-添加规则-获取对比值
export const getContrastVal = (_this) => {
    axios.get('/sys/dict/getDictValue/alarm_rules')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.ContrastValList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警规则-添加规则-与或
export const getAndOr = (_this) => {
    axios.get('/sys/dict/getDictValue/and_or')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.andOrList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警规则-添加规则-告警内容
export const getAlarmContent = (data, _this) => {
    axios.get('/equipment/alarmModle', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result.records;
                _this.alarmContentList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警规则-添加规则-添加
export const alarmOne = (data, _this) => {
    axios.post('/equipment/alarm/one', data)
        .then(res => {
            _this.ruleSubmitOk = true;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ruleVisible = false;
                _this.alarmRulesPagination.current = 1;
                _this.alarmRulesUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//采集设备-告警规则-添加规则-删除
export const deleteRule = (data, _this) => {
    axios.delete('/equipment/alarm/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.alarmRulesPagination.current = 1;
                _this.alarmRulesUpdata();

            } else {
                _this.$message.info(res.message);
            }
        })
}
//采集设备-告警规则-添加规则-获取规则列表
export const getAlarmList = (data, _this) => {
    axios.get('/equipment/alarm', {
        params: data
    })
        .then(res => {
            _this.alarmRulesSource = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;
                list.map(index => {
                    _this.alarmRulesSource.push({
                        alarmName: index.alarmName,
                        alarmRule: index.alarmRule,
                        alarmLevelName: index.alarmLevelName ? index.alarmLevelName : '',
                        alarmModleName: index.alarmModleName ? index.alarmModleName : '',
                        isMassName: index.isMassName,
                        id: index.id,
                        alarmRuleTypeValue: index.alarmRuleTypeValue,
                        alarmStatus: index.alarmStatus
                    })
                })
                _this.alarmRulesPagination.current = res.result.current;
                _this.alarmRulesPagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);

                _this.alarmRulesPagination.current = 0;
                _this.alarmRulesPagination.total = 1;
            }
        })
}


//采集设备-反向控制-反向控制列表
export const getIotControList = (data, _this) => {
    axios.get('/equipment/iotContro', {
        params: data
    })
        .then(res => {
            _this.reverseControlSource = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;
                list.map(index => {
                    _this.reverseControlSource.push({
                        controName: index.controName,
                        variableName: index.variableName,
                        createTime: index.createTime,
                        equipmentName: index.equipmentName,
                        id: index.id,
                        equipmentId: index.equipmentId,
                        variableId: index.variableId,
                        variableValue: index.variableValue,
                        variableDataType: index.variableDataType
                    })
                })
                _this.reverseControlPagination.current = res.result.current;
                _this.reverseControlPagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);

                _this.reverseControlPagination.current = 0;
                _this.reverseControlPagination.total = 1;
            }
        })
}

//采集设备-反向控制-新增控制
export const iotControOne = (data, _this) => {
    axios.post('/equipment/iotContro/one', data)
        .then(res => {

            if (res.code * 1 == 200) {
                _this.controlVisible = false;
                _this.reverseControlPagination.current = 1;
                _this.reverseControlUpdata();
            } else {
                _this.$message.info(res.message);

            }
        })
}

//采集设备-反向控制-删除
export const reomeveIotContro = (data, _this) => {
    axios.delete('/equipment/iotContro/' + data.id)
        .then(res => {

            if (res.code * 1 == 200) {

                _this.reverseControlPagination.current = 1;
                _this.reverseControlUpdata();
            } else {
                _this.$message.info(res.message);

            }
        })
}

//采集设备-告警规则-告警状态
export const sysdictRule_type = (_this) => {
    axios.get('/sys/dict/getDictValue/rule_type')
        .then(res => {

            if (res.code * 1 == 200) {

                _this.rule_typeList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}

//采集设备-全局配置添加
export const equipmentConfigCat = (data, _this) => {
    axios.post('/iot/equipment/config/cat', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.toConfigureVisible = false;
            } else {
                _this.$message.info(res.message);

            }
        })

}

//采集设备-单项配置添加
export const equipmentConfigOne = (data, _this) => {
    axios.post('/iot/equipment/config/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.toConfigureVisible = false;
            } else {
                _this.$message.info(res.message);

            }
        })

}

//采集设备-单项配置详情
export const getEquipmentConfigOne = (data, _this) => {
    axios.get('/iot/equipment/config/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.checkQuality = res.result.checkQuality == 1 ? true : false;
                _this.alarm = res.result.alarm == 1 ? true : false;
                _this.alarmLevel2 = res.result.alarmLevel;
                _this.alarmModel = res.result.alarmModel;
            } else {
                _this.$message.info(res.message);

            }
        })

}

//采集设备-单项配置详情
export const warnOneData = (_this) => {
    axios.get('/business/warn/oneData')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.sourcesource3(res.result);
            } else {
                _this.$message.info(res.message);

            }
        })

}

//采集设备-资产绑定-采集设备绑定资产信息
export const iotEquipmentBindBindData = (data, _this) => {
    axios.post('/iot/iotEquipmentBind/bindData', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.updata();
                _this.bindGisVisible = false;
                _this.gismodelName = '';
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);

            }
        })
}

//采集设备-资产绑定-采集设备解绑资产
export const iotEquipmentBindUnbindData = (data, _this) => {
    axios.get('/iot/iotEquipmentBind/unbindData', {
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


//采集设备-同步功能
export const iotEquipmentSync = (_this) => {
    axios.get('/equipment/iotEquipment/sync')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);


            } else {
                _this.$message.info(res.message);
            }
        })
}
//gis模型-同步缓存
export const synchronousCache = (data, _this) => {
    axios.post(data.url)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);


            } else {
                _this.$message.info(res.message);
            }
        })
}

//设备报警规则-停用启用规则
export const equipmentAlarmStartOrStop = (data, _this) => {
    axios.get('/equipment/alarm/startOrStop', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.alarmRulesUpdata();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//ioServer管理-新增修改代理-代理状态.心跳状态
export const getAgencyStatus = (_this) => {
    axios.get('/sys/dict/getDictValue/working_status')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.workingStatusList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//设备报警规则-通过id查询规则详细信息
export const equipmentAlarmIdDetails = (data, _this) => {
    axios.get('/equipment/alarm/' + data.id)
        .then(res => {
            _this.ruleList = [];
            _this.ruleVisible = true;
            if (res.code * 1 == 200) {
                let obj = res.result;
                obj.alarmRuleList.map((index) => {
                    _this.ruleList.push({
                        andOr: obj.andOr,
                        variableId: index.variableId,
                        alarmRule: index.alarmRule,
                        alarmValue: index.alarmValue
                    })
                });
                _this.alarmName = obj.alarmName;
                _this.rule_type = obj.alarmRuleType;
                _this.alarm2 = obj.alarm == 0 ? false : true;

                _this.alarmLevel = obj.alarmLevel;
                _this.rulesId = obj.id;
                _this.rulesEquipmentId = obj.equipmentId;
                _this.alarmModleId = obj.alarmModleId;
                if (obj.alarmRuleType == 1 || obj.alarmRuleType == 2) {
                    _this.rule_typeVal = false;
                }
                if (obj.alarm == 0 || obj.alarm == null) {
                    _this.rule_typeVal = false;
                }
            } else {
                _this.$message.info(res.message);

            }
        })
}
//设备报警规则-编辑规则内容
export const equipmentAlarmUpdateValue = (data, _this) => {
    axios.post('/equipment/alarm/updateValue', data)
        .then(res => {
            _this.ruleSubmitOk = true;
            if (res.code * 1 == 200) {
                _this.ruleVisible = false;
                _this.alarmRulesUpdata();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//设备报警规则-编辑规则策略
export const equipmentAlarmUpdateAlarm = (data, _this) => {
    axios.post('/equipment/alarm/updateAlarm', data)
        .then(res => {
            _this.ruleSubmitOk = true;
            if (res.code * 1 == 200) {
                _this.ruleVisible = false;
                _this.alarmRulesUpdata();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//查询设备关联gis模型下拉列表
export const getGisVoList2 = (data, _this) => {
    axios.get('/equipment/optEquipment/getGisVoList', {
        params: data
    })
        .then(res => {
            _this.gisloading2 = false;
            _this.gisdataSource2 = [];
            if (res.code == 200) {
                _this.dataSource2 = [];
                let list = res.result.records;
                list.map(index => {
                    _this.gisdataSource2.push({
                        id: index.id,
                        name: index.name,
                        type: index.type,
                    })
                })
                _this.gisipagination2.current = res.result.current;
                _this.gisipagination2.total = res.result.total;

            } else {
                _this.$message.info(res.message);
                _this.gisdataSource2 = [];
                _this.gisipagination2.current = 0;
                _this.gisipagination2.total = 1;

            }
        })
}
//告警管理-告警状态获取-告警等级
export const getWarnLevel = (_this) => {
    axios.get('/sys/dict/getDictValue/warn_level')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.warnLevelList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}