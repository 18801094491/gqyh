import {axios} from "../../../utils/request";


// 页面数据更新函数
let onLoadDataChange = (res, _this) => {
    _this.$message.info(res.message);
    if (res.code == 200) {
        _this.loadData();
        _this.onCloseChange();
    }
};

//工单管理-巡检工单-新增、修改
export const onWorkListChange = (type, data, _this) => {
    axios.post('/worklist/workList/'+ type +'/insp', data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//工单管理-巡检工单-单条查询
export const onWorkListQueryChange = (id, _this, callBack) => {
    axios.get('/worklist/workList/query?id=' + id).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })
}

//工单管理-巡检工单-工单查看-任务查看
export const onTaskListMatterQueryChange = (id, _this, callBack) => {
    axios.get('/worklist/workListMatter/query?id=' + id).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })   
}

//工单管理-巡检工单-删除
export const onDelworkListChange = (id, _this) => {
    console.log(id, _this)
    axios.delete('/worklist/workList/delete/insp?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//工单管理-巡检工单-获取各下拉框数据
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



//工单管理-线路管理-获取各下拉框数据
export const getWorkTeamItemData = (id, _this) => {
    axios.get('/worklist/workList/query?id=' + id).then(res => {
        if (res.code == 200) {
            _this[list] = res.data && res.data.records || res.data;
        } else {
            _this.$message.info(res.message);
        }
    })
}


// -----------------------------------------------------------------------------------------------------




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