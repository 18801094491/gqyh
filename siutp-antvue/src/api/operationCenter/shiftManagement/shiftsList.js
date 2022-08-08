import {
    axios
} from '@/utils/request';
//更改班次状态
export const shiftsStatusChange = (data, _this) => {
    axios.get('/work/shifts/editStatus', {
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
