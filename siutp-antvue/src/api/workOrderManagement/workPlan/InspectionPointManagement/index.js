import {axios} from "../../../../utils/request";
import {console} from "vuedraggable/src/util/helper";
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

// 根据巡检点类别显示
export const byIPointCategoryShowList = (_this, params, record) => {
    axios.get('/work/workInspectionPoint/categoryDropdown', {params})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.childList = res.result;
                if (record) {
                    _this.model = Object.assign({}, record);
                }
            } else {
                _this.$message.info(res.message);

            }
        })
}
// 所有巡检点类别下拉选
export const getIPointCategoryList = (_this) => {
    axios.get('/sys/dict/getDictValue/inspection_type', {params: {}})
        .then(res => {
            if (res.code * 1 == 200) {
                _this.inPointCategoryList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}

// 所有数据模板下拉选
export const getAllTplList = (_this) => {
    axios.get('/work/template/dropdown', {})
        .then(res => {
            if (res.code * 1 == 200) {
                console.log(res.result)
                _this.tplList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}

//巡检点-通过id删除
export const delPointById = (id, _this) => {
    axios.delete('/work/workInspectionPoint/' + id)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}