import {
    axios
} from '@/utils/request';
//合同管理-获取计价规则列表
export const getPrivingRulesList = (_this, data) => {
    axios.get('/settle/contractWaterRule', {params: data})
        .then(res => {
            if (res.code * 1 === 200) {
                _this.dataSource = res.result.records;
                _this.pagination.current = res.result.current;
                _this.pagination.total = res.result.total;

            } else {
                _this.pagination.current = 0;
                _this.pagination.total = 1;
                _this.$message.info(res.message);
            }
        })
}