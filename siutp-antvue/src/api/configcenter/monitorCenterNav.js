import {axios} from '@/utils/request'

//监控地图导航栏管理列表
export const getGisGisListData = (data, _this) => {
    axios.get('/gis/gis/', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {

                let list = res.result;
                list.map(index => {
                    _this.dataSource.push({
                        id: index.id,
                        modelType: index.modelType,
                        modelTypeName: index.modelTypeName,
                        modelThumb: index.modelThumb,
                        navName: index.navName,
                        dataShow: index.dataShow == 1 ? true : false,
                        dataShowName: index.dataShowName,
                        navStatus: index.navStatus == 1 ? true : false,
                        navStatusName: index.navStatusName
                    })
                })
                console.log(_this.dataSource)

            } else {
                _this.$message.info(res.message);

            }
        })
}
//素材管理-获取模型分类列表
export const getGisTypeList = (_this) => {
    axios.get('/sys/category/loadTreeRootCascade?pcode=A15')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.getGisTypeList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//筛选维护-上传图片
export const GISScreeningMaintainImgUpload = (data, _this) => {
    axios.post('/sys/file/uploadGisNav', data)
        .then(res => {
            if (res.code * 1 == 200) {

                _this.GISScreeningMaintainImg = res.result.filePath;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//筛选维护-新增或修改
export const gisGisOne = (data, _this) => {
    axios.post('/gis/gis/one/', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.visible = false;
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//筛选维护-更改是否展示状态
export const gisGisEditDataShow = (data, _this, state, data2) => {
    axios.get('/gis/gis/editDataShow', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                if (!state) {
                    window.oMarker.filter((item, i, arr) => {
                        if (item.modelTypeCode == data2.modelType) {
                            console.log(item.id);
                            delete window[item.id]
                        }

                    })
                }
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//筛选维护-更改启停用状态
export const gisGisEditDataStatus = (data, _this, state, data2) => {
    axios.get('/gis/gis/editStatus', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                window.oMarker = [];
                if (!state) {
                    window.oMarker.filter((item, i, arr) => {
                        if (item.modelTypeCode == data2.modelType) {
                            console.log(item.id);
                            delete window[item.id]
                        }

                    })
                }
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}