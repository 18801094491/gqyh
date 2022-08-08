import { axios } from '@/utils/request'

//deleteAction
export function deleteAction(url, parameter) {
    return axios({
        url: url,
        method: 'delete',
        params: parameter
    })
}

//获取供应商值-新增或修改
export const getQueryNameList = (_this) => {
    axios.get('/equipment/optSupplier/queryNameList')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.supplierClassificationList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//获取设备类型值
export const getEquipmentTypeData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206872024991416321'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.equipmentTypeList = list;
                if (_this.modelTypeList) {
                    _this.modelTypeList = list;
                }
                if (_this.modelTypeList2) {
                    _this.modelTypeList2.push({
                        code: '0',
                        title: '全部'
                    });
                    for (let i = 0; i < list.length; i++) {
                        _this.modelTypeList2.push({
                            code: list[i].code,
                            title: list[i].title
                        })
                    }
                }
            } else {
                _this.$message.info(res.message);
            }

        })
}

//设备类型/设备型号/设备规格得级联关系
export const getEquipmentSelect = (id, _this, listName) => {
    console.log(listName, 'listNamelistNamelistName')
    axios.get('/sys/cate/' + id).then(res => {
        if (res.code * 1 == 200) {
            _this[listName] = res.result
        } else {
            _this.$message.info(res.message);
        }
    })
}
