import { axios } from '@/utils/request'
export const getContractDetail = (data, _this) => {
    axios.get('/settle/contract/queryDetail', {
        params: data
    }).then(res => {
        _this.dataSource = [];
        if (res.code * 1 === 200) {
            _this.model = Object.assign({}, res.result);
            _this.fileList.push({
                filePath: _this.model.fileUrl,
                fileName: _this.model.contractName
            })
            _this.modalVisible = true;
        } else {
            _this.$message.info(res.message);
        }
    })
}