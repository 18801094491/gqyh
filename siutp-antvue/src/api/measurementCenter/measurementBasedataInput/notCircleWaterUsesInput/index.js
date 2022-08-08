import {
    axios
} from '@/utils/request';
//非远端抄表--水表编号
export const getWaterNumberList = (_this) => {
    axios.get('/iot/vardata/equip/A03A03', {params: {monthBalance: "1"}})
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