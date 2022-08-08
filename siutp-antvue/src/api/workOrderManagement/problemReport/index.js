import {axios} from "../../../utils/request";

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


// 根据pcode获取分类字典
export const getCategory = (_this, list) => {
    axios.get('/sys/category/loadTreeRootCascade', {params: {pcode: 'A23'}})
        .then(res => {
            if (res.code * 1 == 200) {
                _this[list] = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 设备台账--问题上报新增、修改
export const updateProReport = (data, _this) => {
    axios.post('/work/workProblemReport/one', data)
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


// 问题上报--关闭操作
export const closeWork1 = (data, _this) => {
    axios.get(`/work/workProblemReport/close/${data.id}`)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        })
}


