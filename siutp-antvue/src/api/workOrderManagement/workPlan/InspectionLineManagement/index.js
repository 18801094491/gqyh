import {axios} from "../../../../utils/request";
//巡检路线-通过id删除
export const delLineById = (id, _this) => {
    axios.delete('/work/route/' + id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}

// 巡检路线获取选中的巡检列表
export const byRouteIdgetSelectPointList = (_this, id) => {
    axios.get('/work/route/pointList', {params:{routeId: id}})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.pointDataList = []
                _this.selectPoint(res.result)
            } else {
                _this.$message.info(res.message);

            }
        })
}
//巡检线路-获取巡检点列表
export const getRoutePointList = (_this, data) => {
    axios.get('/work/workInspectionPoint', {params: data})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.dataSource = res.result.records;
                _this.pagination.current = res.result.current;
                _this.pagination.total = res.result.total;

            } else {
                _this.pagination.current = 0;
                _this.pagination.total = 1;
                _this.$message.info(res.message);
            }
        })
}