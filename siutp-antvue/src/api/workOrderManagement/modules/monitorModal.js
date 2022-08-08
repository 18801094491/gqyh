import {
    axios
} from '@/utils/request';

//工单管理-工单监控弹层-根据工单id获取对应班组成员所在位置
export const getLocation = (id, _this, callBack) => {
    axios.get('worklist/workList/getLocation?id=' + id).then(res => {
        if (res.code == 200) {
            callBack(res.data)
        }
        //  else {
        //     _this.$message.info(res.message);
        // }
    })
}