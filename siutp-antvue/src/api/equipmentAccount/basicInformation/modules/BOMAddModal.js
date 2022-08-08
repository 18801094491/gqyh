import { axios } from '@/utils/request'
// 设备台账--BOM清单新增、修改
export const updateBOMList = (data, _this,record) => {
	axios.post('/equipment/optBom/one',data)
    .then(res => {
        if (res.code * 1 == 200) {
            _this.$message.info(res.message);
            _this.visible = false;
            _this.$emit('closeModal', 'openAddVisible')
            _this.$parent.updata(record);
        } else {
            _this.$message.info(res.message);
        }
    })
}
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

// 设备台账--BOM清单备品备件下拉
export const getSparepartsList = ( _this) => {
	axios.get('/equipment/optBom/spareparts')
    .then(res => {
        if (res.code * 1 == 200) {
        	_this.sparePartsList = res.result
        } else {
            _this.$message.info(res.message);
        }
    })
}