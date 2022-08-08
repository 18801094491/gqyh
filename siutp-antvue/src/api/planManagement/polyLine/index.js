import {axios} from "../../../utils/request";

// 页面数据更新函数
let onLoadDataChange = (res, _this) => {
    _this.$message.info(res.message);
    if (res.code == 200) {
        _this.loadData();
        _this.onCloseChange();
    }
};

//计划管理-线路管理-新增、修改
export const onInspRouteChange = (type, data, _this) => {
    axios.post('/inspection/inspRoute/' + type, data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-线路管理-删除
export const onDelInspRouteChange = (id, _this) => {
    axios.delete('/inspection/inspRoute/delete?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-线路管理-单条线路数据查询
export const getInspRouteData = (routeId, _this, callBack) => {
    axios.get('/inspection/inspRoute/query?id=' + routeId,).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-线路管理-获取各下拉框数据
export const getDropAreasData = (url, list, _this) => {
    axios.get(url).then(res => {
        if (res.code == 200) {
            _this[list] = res.result || res.data.records || res.data;
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-线路管理-查询选中的所属区域下对应巡检点
export const getInspPointData = (data, _this, callBack) => {
    axios.get(
        '/inspection/inspPoint/list4area?areaId=' + data.areaId + 
        (data.name ? ('&name=' + data.name) : '') + 
        (data.code ? ('&code=' + data.code) : '')
    ).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })
}

