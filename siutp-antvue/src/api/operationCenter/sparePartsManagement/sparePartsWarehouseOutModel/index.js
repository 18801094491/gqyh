import {
    axios
} from '@/utils/request';

//备品备件管理-出库列表获取
export const getOptStoreBillOut = (data, _this) => {
    axios.get('/equipment/optSparepart/outAll', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {
                if (res.result) {
                    let list = res.result.records;
                    list.map(index => {
                        _this.dataSource.push({
                            amount: index.amount,
                            batchSn: index.batchSn,
                            createBy: index.createBy,
                            createTime: index.createTime,
                            id: index.id,
                            sparepartId: index.sparepartId,
                            sparepartName: index.sparepartName,
                            sparepartSpecs: index.sparepartSpecs,
                            storeId: index.storeId,
                            storeName: index.storeName,
                        })
                    })
                    _this.ipagination.current = res.result.current;
                    _this.ipagination.total = res.result.total;
                }


            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;
            }
        })
}