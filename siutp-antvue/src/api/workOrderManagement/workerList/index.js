import {axios} from "../../../utils/request";
// 根据pcode获取分类字典
export const getCategory = (data,_this,list) => {
    axios.get('/sys/category/loadTreeRootCascade',{params:data})
        .then(res => {
            if (res.code * 1 == 200) {
                _this[list] = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 设备台账--问题上报新增、修改
export const updateProReport = (data,_this) => {
    axios.post('/work/workProblemReport/one',data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 设备台账--问题上报--问题设备下拉(分派工单设备信息下拉)
export const getEquipmentList = (urlStr,_this) => {
    _this.equipmentList = [];
    axios.get(urlStr).then(res => {
        if (res.code * 1 == 200) {
            _this.equipmentList = res.result
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 分派工单
export const updateAssignWoker = (data, _this,urlStr,type) => {
    axios.post(urlStr,data).then(res => {
        if (res.code * 1 == 200) {
            _this.workerVisible = false;
            if(type=='load'){
                _this.loadData();
            }else{
                _this.updata();
            }
            _this.$message.info(res.message);
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 获取数据字典
export const getDataList = (code,_this,list) => {
    axios.get(`/sys/dict/getDictValue/${code}`).then(res => {
        if (res.code == 200) {
            _this[list] = res.result;

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