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
    axios.post('/worklist/workList/'+ type +'/keep', data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检计划管理-删除
export const onDeleteChange = (id, _this) => {
    console.log(id, _this)
    axios.delete('/worklist/workList/delete/keep?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检计划管理-单条查询
export const onWorkListQueryChange = (id, _this, callBack) => {
    axios.get('/worklist/workList/query?id=' + id).then(res => {
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
        if (res.code == 200) {
            _this[list] = res.data ? ( res.data.records || res.data) : res.result || [];
            setTimeout(() => { callBack && callBack() });
        } else {
            _this.$message.info(res.message);
        }
    })
}

//工单管理-维养工单-获取维养设备
export const getWorkListKeepList = (data, _this) => {
    axios.get('/worklist/workList/list/keep-equ', {
        params: data
    })
    .then(res => {
        _this.data = [];
        let dataList = [];
        if (res.code == 200) {
            let list = res.data;
            list.map((item,index) => {
                // _this.notificationStrategydataSource.push(item)
                dataList.push({
                    key: index.toString(),
                    title: item.equipmentName,
                    time: '',
                    sort: null,
                    type: 2,
                    typeDes: '维养',
                    id: item.id,
                    nextKeepDate: '',
                    seq: item.seq,
                    equipmentSn: item.equipmentSn,
                    equipmentType: item.equipmentType,
                    subName: '',
                    description: item.equipmentType + item.equipmentModeName + item.equipmentLocation,
                    equipmentId: item.id,
                    matterLatitude: item.latitude,
                    matterLongitude: item.longitude,
                   
                })
            })
            _this.data = dataList;
        } else {
            _this.$message.info(res.message);
        }
    })
}

//工单管理-问题列表-获取临时问题列表
export const getWorkListMatterList = (data, _this) => {
    axios.get('/worklist/workListMatter/addlist', {
        params: data
    })
    .then(res => {
        _this.data = [];
        let dataList = [];
        if (res.code == 200) {
            console.log("问题列表==========");
            console.log(res)
            let list = res.data;
            list.map((item,index) => {
                // _this.notificationStrategydataSource.push(item)
                dataList.push({
                    key: index.toString(),
                    title: item.title,
                    time: item.subTime,
                    sort: null,
                    type: 3,
                    id: item.id,
                    nextKeepDate: '',
                    typeDes: item.typeDes,
                    subName: item.subName,
                    description: '',
                    equipmentId: '',
                    matterLatitude: '',
                    matterLongitude: '',
                   
                })
            })
            _this.data = dataList;
            console.log("问题列表数据返回===")
            console.log(_this.data)
        } else {
            _this.$message.info(res.message);
        }
    })
}