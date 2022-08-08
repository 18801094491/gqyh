import {
    axios
} from '@/utils/request';
// 设备台账--BOM清单分页
export const getBOMLIst = (data, _this) => {
    _this.dataSource = [];
    axios.get('/equipment/optBom', { params: data })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loading = false;
                _this.dataSource = res.result.records;
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 根据pcode获取分类字典
export const getCategory = (data, _this, list) => {
    axios.get('/sys/category/loadTreeRootCascade', { params: data })
        .then(res => {
            if (res.code * 1 == 200) {
                _this[list] = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}