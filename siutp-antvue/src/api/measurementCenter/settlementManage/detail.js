import {
    axios
} from '@/utils/request';
// 结算管理--点击合同名称查看详情
export const getContractDetail = (data, _this) => {
    axios.get('/settle/contract/queryDetail',{params:data})
        .then(res => {
            if (res.code * 1 === 200) {
                let result = res.result;
                _this.model = Object.assign({}, result);
                _this.fileList=[];
                if(result && _this.model.fileUrl){
                    _this.$nextTick(() => {
                        _this.fileList.push({
                            filePath:_this.model.fileUrl,
                            fileName:_this.model.contractName
                        })
                    });
                }
            } else {
                _this.$message.info(res.message);
            }
        })
}