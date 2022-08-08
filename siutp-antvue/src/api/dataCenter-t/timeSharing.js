import { axios } from '@/utils/request'
//告警管理-设备类型
export const getAlarmEquipmentType = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A03')
        .then(res => {
            if (res.code * 1 == 200) { // A03A01--阀门,A03A07--绿化支管
                let list = res.result;
                list.map(item => {
                    if (item.code != 'A03A01' && item.code != 'A03A07') {
                        _this.inequipmentTypeList.push({
                            code: item.code,
                            title: item.title,
                        })
                    }
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
// 分时统计--分页
export const vardataList = (data, _this) => {
    axios.post("/iot/vardata/mean", data).then(res => {
        _this.loading = false;
        _this.bOk = true;
        _this.dataSourceList = [];
        if (res.code * 1 == 200) {
            let tableResult = res.result;
            let list = []
            let headerList = []
            tableResult.forEach((item, index) => {
                list[index] = { dataSource: [], columns: [], tableName: '' };
                list[index].tableName = item.title;
                list[index].dataSource = item.data;
                headerList[index] = { columns: [] }
                headerList[index].columns = item.header;
                list[index].columns = [{
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },]
                headerList[index].columns.map(i => {
                    list[index].columns.push({
                        title: i.title,
                        align: "center",
                        dataIndex: i.data
                    })
                })
            })
            _this.dataSourceList = list;
            if (res.message != '') {
                _this.$message.info(res.message);
            }
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