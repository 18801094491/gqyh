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
export const onWorkListChange = (type, data, _this) => {
    axios.post('/worklist/workList/'+ type +'/matter', data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检计划管理-单条查询
export const onWorkListQueryChange = (id, _this, callBack) => {
    axios.get('worklist/workList/query?id=' + id).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-巡检计划管理-删除
export const onDelworkListChange = (id, _this) => {
    console.log(id, _this)
    axios.delete('/worklist/workList/delete/matter?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-线路管理-获取各下拉框数据
export const getDropAreasData = (url, list, _this, callBack) => {
    axios.get(url).then(res => {
        // console.log(res.data.records) 
        if (res.code == 200) {
            _this[list] = res.data ? ( res.data.records || res.data) : res.result;
            setTimeout(() => { callBack && callBack() });
        } else {
            _this.$message.info(res.message);
        }
    })
}

//工单管理-问题工单-问题列表
export const getWorkListMatterList = (data, _this) => {
    axios.get('/worklist/workListMatter/addlist?subTimeStart='+ data.subTimeStart + '&subTimeEnd='+ data.subTimeEnd)
    .then(res => {
        if (res.code == 200) {
            _this.workListMatter = res.data;
        } else {
            _this.$message.info(res.message);
        }
    })
}
