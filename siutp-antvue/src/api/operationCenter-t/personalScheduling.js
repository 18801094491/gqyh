import {
    axios
} from '@/utils/request';
//个人排班管理-个人班组排班列表-分页列表查询
export const workTeamDutyTeamMyDutyData = (data, _this) => {
    axios.get('/work/teamDuty/teamMyDutyData', {
        params: data
    })
        .then(res => {
            _this.listData = [];
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.listData.push({
                        dutyDay: index.dutyDay,
                        list: [
                            {
                                startTime: index.startTime,
                                overTime: index.overTime
                            }
                        ]
                    })
                })

            } else {
                _this.$message.info(res.message);

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