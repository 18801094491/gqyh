import { getAction } from '@/api/manage'
// 用户用水量统计--客户树结构
export const queryCustomerTreeList = (params) => getAction("/equipment/meterFlow/queryTreeList", params);
//用水量统计-每日累计用水量查询
export const getmeterFlowQueryNetFlowDay = (data, _this) => {
    axios.get('/equipment/meterFlow/queryNetFlowDay', {
        params: data
    })
        .then(res => {

            if (res.code * 1 == 200) {
                let sourceData = [];
                let list = res.result;
                _this.highchartsUpdata(list);
            } else {
                _this.$message.info(res.message);
            }
        })
}

//用水量统计-客户用水累计统计
export const getmeterFlowQueryAllNetFlowDay = (_this) => {
    axios.get('/equipment/meterFlow/queryAllNetFlowDay')
        .then(res => {
            _this.dataSource = [];
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.dataSource.push({
                        customerName: index.customerName,
                        equipmentSn: index.equipmentSn,
                        monthNetFlowDay: index.monthNetFlowDay,
                        netFlowDay: index.netFlowDay,
                        nowDate: index.nowDate,
                        oldMonthNetFlowDay: index.oldMonthNetFlowDay,
                        projectName: index.projectName,
                        sectionName: index.sectionName,
                        yearNetFlowDay: index.yearNetFlowDay
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}