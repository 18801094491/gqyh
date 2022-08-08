import {axios} from "../../../utils/request";

// 页面数据更新函数
let onLoadDataChange = (res, _this) => {
    _this.$message.info(res.message);
    if (res.code == 200) {
        _this.loadData();
        _this.onCloseChange();
    }
};

//计划管理-区域管理-新增
export const onAddInspAreaChange = (data, _this) => {
    axios.post('/inspection/inspArea/add', data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-区域管理-修改
export const onEditInspAreaChange = (data, _this) => {
    axios.post('/inspection/inspArea/edit', data).then(res => {
        onLoadDataChange(res, _this);
    })
}

//计划管理-区域管理-删除
export const onDelInspAreaChange = (id, _this) => {
    axios.delete('/inspection/inspArea/delete?id=' + id).then(res => {
        onLoadDataChange(res, _this);
    })
}
