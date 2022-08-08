import {
    axios
} from '@/utils/request';
//获取结算结果
export const getJSResultList = (_this) => {
    axios.get('/sys/dict/getDictValue/settle_status', {}).then(res => {
        if (res.code === 200) {
            _this.resultList = res.result;

        } else {
            _this.$message.info(res.message);
        }
    })
}