import {
    axios
} from '@/utils/request';
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


