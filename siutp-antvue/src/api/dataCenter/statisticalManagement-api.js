import {axios} from '@/utils/request'

//采集变量-获数据类型
export const getDataType = (_this) => {
    axios.get('/sys/dict/getDictValue/data_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.dataType_List = res.result;
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

//统计管理-获取统计类型
export const getChartType = (_this) => {
    axios.get('/sys/dict/getDictValue/chart_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.chartTypeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//统计管理-获取设备名称下拉列表
export const getChartOpt = (_this) => {
    axios.get('/statistic/chart/opt')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.deviceList2 = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//统计管理-改变统计状态
export const chartStsId = (data, _this) => {
    axios.get('/statistic/chart/sts/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination.current = 1;

                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//统计管理-根据资产id获取采集变量
export const getChartIdVar = (data, _this, index) => {
    axios.get('/statistic/chart/' + data.id + '/var')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.items[index].varList = [];
                for (let i = 0; i < _this.items[index].serials.length; i++) {
                    _this.items[index].serials[i].variableName = '';
                }
                _this.items[index].varList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//统计管理-统计事项-通过id删除
export const chartDelId = (data, _this) => {
    axios.get('/statistic/chart/del/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination.current = 1;
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//统计管理-保存图表信息
export const chartOne = (data, _this) => {
    axios.post('/statistic/chart/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination.current = 1;
                _this.statisticalManagementvisible = false;
                _this.loadData();

            } else {
                _this.$message.info(res.message);
            }
        })
}

//统计管理-通过id查询
export const getChartIdDetails = (data, _this) => {
    axios.get('/statistic/chart/' + data.id)
        .then(res => {
            if (res.code * 1 == 200) {
                let obj = res.result;
                _this.chartId = obj.id;
                _this.statisticName = obj.statisticName;
                _this.chartType = obj.chartType;
                _this.statisticCycle = obj.statisticCycle;
                _this.statisticCycleType = obj.statisticCycleType;
                _this.statisticStatus = obj.statisticStatus;
                _this.cycleTime = obj.cycleTime == "0" ? 0 : 1; // 自定义单选框
                _this.displayOrder = obj.displayOrder; // 展示顺序
                _this.endTime = obj.endTime; // 结束时间
                _this.startTime = obj.startTime; // 开始时间
                _this.items = [];
                obj.items.map((index, i) => {

                    _this.deviceNameChange2(i, index.equipmentId);

                    _this.items.push({
                        equipmentId: index.equipmentId,
                        serials: index.serials,
                        varList: []
                    });


                })

            } else {
                _this.$message.info(res.message);
            }
        })
}
//统计管理-根据资产id获取采集变量
export const getChartIdVar2 = (data, _this, index) => {
    axios.get('/statistic/chart/' + data.id + '/var')
        .then(res => {
            if (res.code * 1 == 200) {


                _this.items[index].varList = res.result;

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

//统计管理-获取统计周期
export const getCycleType = (_this) => {
    axios.get('/sys/dict/getDictValue/cycle_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.cycleTypeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}