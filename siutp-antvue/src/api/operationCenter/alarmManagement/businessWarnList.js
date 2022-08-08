import {
    axios
} from '@/utils/request';
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

//告警管理-告警状态获取-告警类型
export const getWarnType = (_this) => {
    axios.get('/sys/dict/getDictValue/warn_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.warnTypeList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}

//告警管理-告警状态获取-告警状态
export const getWarnStatusList = (_this) => {
    axios.get('/sys/dict/getDictValue/warn_status')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.warnStatusList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}

//告警管理-处理告警信息
export const businessWarnDeal = (data, _this) => {
    axios.post('/business/warn/deal', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
                _this.alarmHandlingvisible = false;
                _this.$router.push({
                    name: _this.$route.name
                })
            } else {
                _this.$message.info(res.message);

            }
        })
}
//告警管理-列表操作-确认
export const warnConfirm = (data, _this) => {
    axios.get('/business/warn/confirm', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.loadData(1);
            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-列表操作-关闭
export const warnClose = (data, _this) => {
    axios.post('/business/warn/close', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.turnOffAlarmvisible = false;
                _this.loadData(_this.ipagination.current);
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取用户信息
export const getUserData = (data, _this) => {
    axios.get('/sys/user/idList', {
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

//告警管理-通知策略-新增/修改通知策略-通知角色获取
export const getWarnRoleUsers = (data, _this) => {
    axios.get('/business/warn/roleUsers', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.rolesIdList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-通知策略-新增/修改通知策略-通知班组获取
export const getWarnWorkUsers = (data, _this) => {
    axios.get('/business/warn/workUsers', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.workTeamsIdList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-通知策略-新增/修改通知策略-提交
export const warnOne = (data, _this) => {
    axios.post('/business/warn/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.notificationStrategyDefinitionvisible = false;
                _this.notificationStrategyipagination.current = 1;
                _this.notificationStrategyUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-通知策略-通知策略列表获取
export const getWarnPilicyList = (data, _this) => {
    axios.get('/business/warn/pilicyList', {
        params: data
    })
        .then(res => {
            _this.notificationStrategydataSource = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;
                list.map(index => {
                    _this.notificationStrategydataSource.push({
                        warnLevel: index.warnLevel,
                        name: index.name,
                        id: index.id,
                        list: index.list,
                        workStatusCode: index.workStatusCode == 1 ? true : false
                    })
                })
                _this.notificationStrategyipagination.current = res.result.current;
                _this.notificationStrategyipagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);

                _this.notificationStrategyipagination.current = 0;
                _this.notificationStrategyipagination.total = 1;
            }
        })
}

//告警管理-通知策略-更改启停用状态
export const warnChangeStatus = (data, _this) => {
    axios.get('/business/warn/changeStatus', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.notificationStrategyipagination.current = 1;
                _this.notificationStrategyUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-通知策略-删除通知策略
export const warnDelete = (data, _this) => {
    axios.get('/business/warn/delete', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.notificationStrategyipagination.current = 1;
                _this.notificationStrategyUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-通知策略-通知策略详情
export const warnGetDetail = (data, _this) => {
    axios.get('/business/warn/getDetail', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.notificationStrategyDefinitionvisible = true;
                let obj = res.result;
                _this.name = obj.name;
                _this.rolesId = obj.rolesId ? obj.rolesId.split(',') : [];
                _this.usersId = obj.usersId ? obj.usersId.split(',') : [];
                _this.warnLevel2 = obj.warnLevelCode;
                _this.workStatus = obj.workStatus == '0' ? false : true;
                _this.workTeamsId = obj.workTeamsId ? obj.workTeamsId.split(',') : [];
                _this.messageTemplate = obj.smsTemplateCode;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//告警管理-通知策略-新增通知策略-消息模板下拉选
export const warnSmstemplate = (data, _this) => {
    axios.get('/business/warn/smstemplate', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {

                _this.messageTemplateList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//告警管理-设备类型
export const getA03 = (_this) => {
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
//告警信息配置-添加或修改
export const alarmIotEquipmentRuleWarnOne = (data, _this) => {
    axios.post('/alarm/iotEquipmentRuleWarn/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.alarmEventConfigurationvisible = false;
            } else {
                _this.$message.info(res.message);

            }
        })
}

//告警信息配置-分页列表查询
export const alarmIotEquipmentRuleWarn = (_this) => {
    axios.get('/alarm/iotEquipmentRuleWarn')
        .then(res => {
            if (res.code * 1 == 200) {
                let obj = res.result.records;
                _this.alarmEventConfiguratiId = obj.length ? obj[0].id : '';
                _this.alarmEventConfigurationList = obj.length ? (obj[0].warnLevelCode.length ? obj[0].warnLevelCode.split(',') : []) : []
                _this.typeConfigList = obj.length ? (obj[0].type.length ? obj[0].type.split(',') : []) : []
            } else {
                _this.$message.info(res.message);

            }
        })
}

//班组管理-班组排班管理-班组信息获取
export const getTeamDutyTeamList = (_this) => {
    axios.get('/work/teamDuty/teamList')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.teamInformationList = res.result;
            } else {
                _this.$message.info(res.message);
                _this.teamInformationList = res.result;
            }
        })
}
// 分派工单
export const updateAssignWoker = (data, _this, urlStr, type) => {
    axios.post(urlStr, data).then(res => {
        if (res.code * 1 == 200) {
            _this.workerVisible = false;
            if (type == 'load') {
                _this.loadData();
            } else {
                _this.updata();
            }
            _this.$message.info(res.message);
        } else {
            _this.$message.info(res.message);
        }
    })
}

// 设备台账--问题上报--问题设备下拉(分派工单设备信息下拉)
export const getEquipmentList = (urlStr, _this) => {
    _this.equipmentList = [];
    axios.get(urlStr).then(res => {
        if (res.code * 1 == 200) {
            _this.equipmentList = res.result
        } else {
            _this.$message.info(res.message);
        }
    })
}

