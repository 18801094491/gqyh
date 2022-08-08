import {
    axios
} from '@/utils/request';

//客户信息管理-新增上传
export const customerInformationUploadFile = (data, _this) => {
    axios.post('/sys/file/upload', data)
        .then(res => {

            if (res.code * 1 == 200) {

                _this.fileList.push({
                    fileName: res.result.fileName,
                    filePath: res.result.filePath
                });

                $('#uploadBtn').val('');
            } else {
                _this.$message.info(res.message);
            }
        })
}
//合同管理-客户名称下拉列表
export const getQueryCustomerNamesListData = (data, _this) => {
    axios.get('/settle/contract/queryCustomerNames', {
        params: data
    })
        .then(res => {
            _this.dataSource = [];
            if (res.code * 1 == 200) {

                let list = res.result;
                list.map(index => {
                    _this.dataSource.push({
                        constomerName: index.customerName,
                        customerId: index.id
                    });
                })

            } else {
                _this.$message.info(res.message);
            }
        })
}

//合同管理-分类下拉列表
export const getContractTypeList = (_this) => {
    axios.get('/sys/dict/getDictValue/contract_type', {})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.typeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
