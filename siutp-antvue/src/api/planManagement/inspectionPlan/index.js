import {axios} from "../../../utils/request";

// 页面数据更新函数
let onLoadDataChange = (res, _this) => {
    _this.$message.info(res.message);
    if (res.code == 200) {
        _this.loadData();
        _this.onCloseChange();
    }
};

//计划管理-巡检计划管理-新增、修改
export const onInspPlanChange = (type, data, _this) => {
    axios.post('/inspection/inspPlan/' + type, data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检计划管理-删除
export const onDelInspPlanChange = (id, _this) => {
    console.log(id, _this)
    axios.delete('/inspection/inspPlan/delete?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检计划管理-单条查询
export const onInspPlanQueryChange = (id, _this, callBack) => {
    axios.get('/inspection/inspPlan/query?id=' + id).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-线路管理-获取各下拉框数据
export const getDropAreasData = (url, list, _this, callBack) => {
    axios.get(url).then(res => {
        // console.log(res.data.records) 
        if (res.code == 200) {
            _this[list] = res.data && res.data.records || res.data;
            setTimeout(() => {
                callBack && callBack()
            });
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-线路管理-获取全部巡检区域
export const getInspAreaData = ( _this) => {
    axios.get('/inspection/inspArea/all').then(res => {
        if (res.code == 200) {
            _this.areaList = res.data;
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-线路管理-根据巡检区域查询对应巡检线路
export const getInspRouteData = (areaId, _this, list) => {
    axios.get('/inspection/inspRoute/list4area?areaId=' + areaId,).then(res => {
        if (res.code == 200) {
            _this[list || 'routeList'] = res.data;
        } else {
            _this.$message.info(res.message);
        }
    })
}