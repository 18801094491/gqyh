import { axios } from '@/utils/request'
//获取合同信息
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
//右侧菜单获取结算结果
export const getJSResultList = (_this) => {
    axios.get('/sys/dict/getDictValue/settle_status', {}).then(res => {
        if (res.code === 200) {
            _this.resultList = res.result;

        } else {
            _this.$message.info(res.message);
        }
    })
}
//左侧菜单获取
function getAction(url, parameter) {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}
const queryDistrictTreeList = (params) => getAction("/settle/district/queryTreeList", params);
const searchDistrictByKeywords = (params) => getAction("/settle/district/searchBy", params);

export {
    queryDistrictTreeList,
    searchDistrictByKeywords
}
