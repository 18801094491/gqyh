import {
        axios
    } from '@/utils/request';
    //获取供应商值-筛选
    export const getQueryNameList2 = (_this) => {
        axios.get('/equipment/optSupplier/queryNameList')
            .then(res => {
                if (res.code * 1 == 200) {
                    let list = res.result;
    
                    list.map(index => {
                        _this.supplierClassificationList2.push({
                            id: index.id,
                            supplierName: index.supplierName
                        })
                    })
                    console.log(_this.supplierClassificationList2)
                } else {
                    _this.$message.info(res.message);
                }
            })
    }
    
    