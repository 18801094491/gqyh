/* *******
* 数据字典，相关api接口
* ******/
import {
    axios
} from '@/utils/request';

export const getDocType = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A17')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.inequipmentTypeList = list;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//设备类型
export const getEquipmentType = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A03')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.inequipmentTypeList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}
// //告警管理-设备类型
// export const getAlarmEquipmentType = (_this) => {
//     axios.get('/sys/category/loadTreeRootCascade?pcode=A03')
//         .then(res => {
//             if (res.code * 1 == 200) { // A03A01--阀门,A03A07--绿化支管
//                 let list = res.result;
//                 list.map(item => {
//                     if (item.code != 'A03A01' && item.code != 'A03A07') {
//                         _this.inequipmentTypeList.push({
//                             code: item.code,
//                             title: item.title,
//                         })
//                     }
//                 })
//             } else {
//                 _this.$message.info(res.message);
//             }
//         })
// }

//设备台账-获取设备类型
export const getEquipmentOptType = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A01')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.equipmentCategory2List = list;
                _this.equipmentCategory2 = list[0].code;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//告警管理-设备类型
export const getA16 = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A16')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.inequipmentTypeList = list;
            } else {
                _this.$message.info(res.message);

            }
        })
}