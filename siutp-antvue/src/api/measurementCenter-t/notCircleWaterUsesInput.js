import {
    axios
} from '@/utils/request';
//非远端抄表--水表编号
export const getWaterNumberList = (_this) => {
    axios.get('/iot/vardata/equip/A03A03', { params: { monthBalance: "1" } })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.waterNumberList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//非远端抄表--所属标段list
export const getEquipmentSectionList = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A04', {})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.equipmentSectionList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//非远端抄表--所属标段list
export const settleManualContracts = (_this, param) => {
    axios.get('/settle/manual/contracts', { params: param })
        .then(res => {
            if (res.code * 1 === 200) {
                _this.contractInfos = res.result;
                if (_this.contractInfos.length > 0) {
                    _this.model.ruleItemId = _this.contractInfos[0].ruleItemId;
                    _this.model.price = _this.contractInfos[0].price;
                } else {
                    _this.model.ruleItemId = undefined;
                    _this.model.price = undefined;
                }
            } else {
                _this.$message.info(res.message);

            }
        })
}

//post method= {post | put}
export const httpAction = function (url, parameter, method) {
    return axios({
        url: url,
        method: method,
        data: parameter
    })
}