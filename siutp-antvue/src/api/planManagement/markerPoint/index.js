import {axios} from "../../../utils/request";

// 页面数据更新函数
let onLoadDataChange = (res, _this) => {
    _this.$message.info(res.message);
    if (res.code == 200) {
        _this.loadData();
        _this.onCloseChange();
    }
};

//计划管理-巡检点管理-新增、修改
export const onInspPointChange = (type, data, _this) => {
    axios.post('/inspection/inspPoint/' + type, data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检点管理-删除
export const onDelInspPointChange = (id, _this) => {
    axios.delete('/inspection/inspPoint/delete?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检点管理-获取各下拉框数据
export const getDropAreasData = (url, list, _this) => {
    axios.get(url).then(res => {
        if (res.code == 200) {
            _this[list] = res.result || res.data.records || res.data;
        } else {
            _this.$message.info(res.message);
        }
    })
}
