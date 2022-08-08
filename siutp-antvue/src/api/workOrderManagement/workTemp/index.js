import {axios} from "../../../utils/request";

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

// 工单模板--数据项分页
export const getDataItemList = (data, _this) => {
    _this.dataSource = [];
    axios.get(`/work/workJobDatasource/${data}`).then(res => {
        if (res.code * 1 == 200) {
            _this.loading = false;
            _this.dataSource = res.result;
        } else {
            _this.$message.info(res.message);
        }
    })
}