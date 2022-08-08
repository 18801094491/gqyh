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
export const onWorkListMatterChange = (type, data, _this) => {
    axios.post('/worklist/workListMatter/' + type, data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-巡检计划管理-单条查询
export const onWorkListQueryChange = (id, _this, callBack) => {
    axios.get('/worklist/workListMatter/query?id=' + id).then(res => {
        if (res.code == 200) {
            callBack(res.data);
        } else {
            _this.$message.info(res.message);
        }
    })
}

//计划管理-巡检计划管理-删除
export const onDelWorkListMatterChange = (id, _this) => {
    console.log(id, _this)
    axios.delete('/worklist/workListMatter/delete?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-线路管理-获取各下拉列表数据
export const getDropAreasData = (url, list ,_this) => {
    axios.get(url).then(res => {
        if (res.code == 200) {
            _this[list] = res.result;
        } else {
            _this.$message.info(res.message);
        }
    })
}



// 设备台账页面--设备类型图片s\上传
export const uploadImg = (data, _this) => {
    axios.post('/sys/file/upload',data).then(res => {
            if(res.code == 200) {
                console.log(res.result)
                _this.fileList.push({
                    fileName: res.result.fileName,
                    filePath: res.result.filePath
                });
            }else{
                _this.$message.info(res.message);
            }
        })
}
