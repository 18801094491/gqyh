import {
    axios
} from '@/utils/request';
// 根据pcode获取分类字典
export const getCategory = (data,_this,list) => {
    axios.get('/sys/category/loadTreeRootCascade',{params:data})
        .then(res => {
            if (res.code * 1 == 200) {
                _this[list] = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 设备台账--备品备件新增、修改
export const updateSpare = (data, _this) => {
	axios.post('/equipment/optSpareparts/one',data)
    .then(res => {
        if (res.code * 1 == 200) {
        	_this.visible = false;
        	_this.loadData();
            _this.$message.info(res.message);
        } else {
            _this.$message.info(res.message);
        }
    })
}