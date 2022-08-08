import { axios } from '@/utils/request.js'

//大屏重点监控配置-分页列表查询
export const rdpMonitorList = (data, _this) =>{
    axios.get('/rdp/monitor-point/confs', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {
                let list = res.data.records;
                list.map(item => {
                    _this.dataSource.push({
                        id: item.id,
                        indexCode: item.indexCode,
                        monitorName: item.monitorName,
                        positioNo: item.positioNo,
                        remarks: item.remarks,
                    })
                })
                _this.ipagination.current = res.data.current;
                _this.ipagination.total = res.data.total;

            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;

            }
        })
}

//大屏重点监控配置-分页列表查询
export const rdpMonitorEquipmentList = (data, _this) =>{
    axios.get('/equipment/optEquipment/monitor/list', {
        params: data
    })
        .then(res => {
            _this.monitorLoading = false;
            _this.monitorDataSource = [];
            if (res.code * 1 == 200) {
                let list = res.result.records;
                list.map(item => {
                    _this.monitorDataSource.push({
                        id: item.id,
                        equipmentSn: item.equipmentSn,
                        hkMonitorCode: item.hkMonitorCode ? item.hkMonitorCode : '暂无',
                        hkMonitorKey: item.hkMonitorKey,
                        equipmentLocation: item.equipmentLocation,
                    })
                })
                _this.monitorIpagination.current = res.result.current;
                _this.monitorIpagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.monitorDataSource = [];
                _this.monitorIpagination.current = 0;
                _this.monitorIpagination.total = 1;
            }
        })

}
//大屏重点监控配置-编辑
export const rdpMonitorEdit = (data, _this) =>{
    let code = data.indexCode;
    axios.patch('/rdp/monitor-point/confs',data)
        .then(res => {
            if (res.code * 1 == 200) {
                setTimeout(function () {
                    _this.rdpMonitorList();
                    if(code) {
                        _this.bindMonitorVisible = false;
                    }
                    _this.$message.success(res.message)
                },200)

            } else {
                _this.$message.info(res.message);
            }
        })
}



