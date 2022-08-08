import {
    axios
} from '@/utils/request';
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

//班组管理-班组排班管理-根据班组信息ID获取班次信息
export const getTeamDutyQueryShifts = (data, _this) => {
    axios.get('/work/teamDuty/queryShifts', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                // _this.teamInformationList=res.result;
                _this.shiftsName = res.result.shiftsName;
                _this.shiftsNameId = res.result.id;
            } else {
                _this.$message.info(res.message);
                // _this.teamInformationList=res.result;
            }
        })
}

//增加班组排班信息
export const teamDutyOne = (data, _this) => {
    axios.post('/work/teamDuty/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.updata(_this.moment(new Date()).format('YYYY-MM'), '1');
            } else {
                _this.$message.info(res.message);

            }
        })
}

//班组排班列表-分页列表查询
export const workTeamDutyList = (data, _this) => {
    axios.get('/work/teamDuty', {
        params: data
    })
        .then(res => {
            _this.listData = [];
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.listData.push({
                        dutyDay: index.dutyDay,
                        list: index.list
                    })
                })

            } else {
                _this.$message.info(res.message);

            }
        })
}

//当日详细排班情况
export const teamDutyOneDay = (data, _this) => {
    axios.get('/work/teamDuty/oneDay', {
        params: data
    })
        .then(res => {
            _this.schedulingDetailsList = [];
            if (res.code * 1 == 200) {
                if (res.result.length) {
                    let list = res.result;
                    list.map(index => {
                        _this.schedulingDetailsList.push({
                            id: index.id,
                            shiftsName: index.shiftsName,
                            teamName: index.teamName
                        })
                    });
                    _this.ecuvisible2 = true;
                } else {
                    _this.$message.info('当日没有班组排班！');
                }
            } else {
                _this.$message.info(res.message);

            }
        })
}


//当日详细排班情况-删除
export const teamDutyDel = (data, _this) => {
    axios.post('/work/teamDuty/del', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ecuvisible2 = false;
                if (_this.selectDate) {
                    _this.updata(_this.selectDate, '1');
                } else {
                    _this.updata(_this.moment(new Date()).format('YYYY-MM'), '1');
                }

            } else {
                _this.$message.info(res.message);

            }
        })
}