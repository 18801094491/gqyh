import {
    axios
} from '@/utils/request';
//告警管理-设备类型
export const getA16 = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A16')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.inequipmentTypeList = list;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//采集变量-启停用
export const editWorkingStatus = (data, _this) => {
    axios.get('/iot/varinfo/editWorkingStatus', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//采集变量-同步变量信息
export const iotVarinfoSync = (_this) => {
    axios.get('/iot/varinfo/sync',)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        })
}


