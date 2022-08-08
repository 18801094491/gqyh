import { axios } from '@/utils/request'

// 数据中心 -- 流量统计分页
export const getFlowList = (data, _this) => {
    axios.get('/iot/cumulativeStatistics', { params: data })
        .then(res => {
            _this.dataSourceList = []
            if (res.code * 1 == 200) {
                let result = res.result;
                if (result.length == 0 || !result[0].data || !result[0].data.length) {
                    return _this.$message.info('暂无数据');
                }
                let list = []
                let headerList = [];
                result.forEach((item, index) => {
                    item.header.map(item => {
                        headerList.push({
                            title: item.title,
                            align: "center",
                            dataIndex: item.dataIndex
                        })
                    })
                    list[index] = { dataSource: item.data, columns: headerList, tableName: item.title }
                })
                _this.dataSourceList = list;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//数据中心-分时统计-设备信息查询
export const getDeviceInformation = (data, _this) => {
    axios.get('/iot/vardata/equip/' + data.type)
        .then(res => {
            _this.deviceInformationList = [];
            if (res.code * 1 == 200) {
                _this.queryParam.deviceInformation = undefined;
                _this.deviceInformationList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}