import {axios} from "../../../utils/request";

// 通知列表--关闭操作
export const closeWork = (data, _this) => {
    axios.get(`/opt/upkeep/plan/close/${data.id}`)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        })
}

//获取所属标段值
export const getBidSegmentData = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade', {
        params: {
            pcode: 'A04'
        }
    }).then(res => {
        if (res.code * 1 == 200) {
            let list = res.result;
            _this.bidSegmentList = list;
        } else {
            _this.$message.info(res.message);
        }
    })
}
