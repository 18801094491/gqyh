import {axios} from '@/utils/request';

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
//ioServer管理-新增修改代理-代理类型
export const getProxyType = (_this) => {
    axios.get('/sys/dict/getDictValue/proxy_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.proxyTypeList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}


//ioServer管理-连接测试心跳地址
export const iotProxyLinkTest = (data, _this) => {
    axios.get('/iot/iotProxy/linkTest', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                if (res.result.result.linkStatus == 200 && res.result.result.linkStatus == 200) {
                    _this.$message.info('测试连接成功!');
                } else {
                    _this.$message.info('测试连接失败!');
                }


            } else {
                _this.$message.info('测试连接失败!');
            }
        })
}

//ioServer管理-新增修改代理提交
export const iotProxyOne = (data, _this) => {
    axios.post('/iot/iotProxy/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//ioServer管理-新增修改代理提交
export const getIotProxyList = (_this) => {
    axios.get('/iot/iotProxy')
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {
                let list = res.result;
                let num = 0;
                list.map(index => {
                    if (index.heartbeatUnitCode == 0) {
                        num = 1;
                    } else if (index.heartbeatUnitCode == 1) {
                        num = 60;
                    } else if (index.heartbeatUnitCode == 2) {
                        num = 3600;
                    } else if (index.heartbeatUnitCode == 3) {
                        num = 86400;
                    }
                    _this.dataSource.push({
                        heartbeatAddress: index.heartbeatAddress,
                        heartbeatCycle: index.heartbeatCycle * num,
                        heartbeatStatus: index.heartbeatStatus,
                        heartbeatStatusCode: index.heartbeatStatusCode,
                        id: index.id,
                        ipAddress: index.ipAddress,
                        proxyName: index.proxyName,
                        proxyStatus: index.proxyStatus == '停用' ? false : true,
                        proxyStatusCode: index.proxyStatusCode,
                        proxyType: index.proxyType,
                        proxyTypeCode: index.proxyTypeCode,
                        heartbeatUnit: index.heartbeatUnit,
                        heartbeatUnitCode: index.heartbeatUnitCode + ''

                    })
                })

            } else {
                _this.$message.info(res.message);


            }
        })
}
//ioServer管理-更改代理状态
export const iotProxyEditStatus = (data, _this) => {
    axios.get('/iot/iotProxy/editStatus', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//ioServer管理-时间存储方式
export const getTimeType = (_this) => {
    axios.get('/sys/dict/getDictValue/time_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.timeTypeList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}
//ioServer管理-采集设备-分页查询所有采集设备列表
export const iotProxyAllEquip = (data, _this) => {
    axios.get('/iot/iotProxy/allEquip', {
        params: data
    })
        .then(res => {
            _this.loading2 = false;
            _this.dataSource2 = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;

                list.map(index => {

                    _this.dataSource2.push({
                        bindStatus: index.bindStatus,
                        id: index.id,
                        iotName: index.iotName,
                        iotSn: index.iotSn

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
//ioServer管理-采集设备-单个代理已绑定的采集设备列表
export const iotProxyGetBindEquip = (data, _this) => {
    axios.get('/iot/iotProxy/getBindEquip', {
        params: data
    })
        .then(res => {
            _this.loading3 = false;
            _this.dataSource3 = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;

                list.map(index => {

                    _this.dataSource3.push({
                        bindStatus: index.bindStatus,
                        id: index.id,
                        iotName: index.iotName,
                        iotSn: index.iotSn

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
//ioServer管理-单个绑定采集设备
export const iotProxyBindOne = (data, _this) => {
    axios.get('/iot/iotProxy/bindOne', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination2.current = 1;
                _this.iotProxyAllEquipUpdata();
                _this.ipagination3.current = 1;
                _this.iotProxyGetBindEquipUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//ioServer管理-单个绑定采集设备
export const iotProxyUnbindOne = (data, _this) => {
    axios.get('/iot/iotProxy/unbindOne', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination2.current = 1;
                _this.iotProxyAllEquipUpdata();
                _this.ipagination3.current = 1;
                _this.iotProxyGetBindEquipUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//ioServer管理-按照类型绑定采集设备
export const iotProxyBindCate = (data, _this) => {
    axios.get('/iot/iotProxy/bindCate', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination2.current = 1;
                _this.iotProxyAllEquipUpdata();
                _this.ipagination3.current = 1;
                _this.iotProxyGetBindEquipUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//ioServer管理-绑定全部采集设备
export const iotProxyBindAll = (data, _this) => {
    axios.get('/iot/iotProxy/bindAll', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination2.current = 1;
                _this.iotProxyAllEquipUpdata();
                _this.ipagination3.current = 1;
                _this.iotProxyGetBindEquipUpdata();
            } else {
                _this.$message.info(res.message);
            }
        })
}