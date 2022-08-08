import {axios} from "../../utils/request";
//职务表-停用启用
export const positionStartOrStop = (data, _this) => {
    axios.get('/sys/position/startOrStop', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
            } else {
                _this.$message.info(res.message);

            }
        })
}