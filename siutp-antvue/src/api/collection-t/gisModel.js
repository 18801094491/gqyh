import { axios } from '@/utils/request';

//gis模型获取列表信息
export const getGisModelData = (data, _this) => {
    axios.get('/equipment/gisModel/list', {
        params: data
    })
        .then(res => {
            _this.dataSource = [];
            _this.loading = false;
            if (res.code == 200) {

                let list = res.result.records;
                list.map(index => {
                    if (index.modelPosition == 0 || index.modelPosition == '0') {
                        index.modelPosition = '不偏移'
                    }
                    if (index.modelPosition == 1 || index.modelPosition == '1') {
                        index.modelPosition = '上'
                    }
                    if (index.modelPosition == 2 || index.modelPosition == '2') {
                        index.modelPosition = '右'
                    }
                    if (index.modelPosition == 3 || index.modelPosition == '3') {
                        index.modelPosition = '下'
                    }
                    _this.dataSource.push({
                        id: index.id,
                        latitude: index.latitude,
                        longitude: index.longitude,
                        modelName: index.modelName,
                        modelOffImg: index.modelOffImg,
                        modelOffset: index.modelOffset,
                        modelOnImg: index.modelOnImg,
                        modelPosition: index.modelPosition,
                        modelType: index.modelType,
                        modelWaringImg: index.modelWaringImg,
                        modelLevel: index.modelLevel,
                        bindStatus: index.bindStatus,
                        modelTypeCode: index.modelTypeCode,
                        modelImg: index.modelImg,
                        width: index.width,
                        height: index.height,
                        typeSn: index.typeSn
                    })
                })
                _this.ipagination.current = res.result.current;
                _this.ipagination.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.dataSource = [];
                _this.ipagination.current = 0;
                _this.ipagination.total = 1;
            }
        })
}

export const getEquipmentTypeData2 = (_this) => {
    axios.get('/sys/cate/A15')
        .then(res => {
            let list = res.result;
            _this.equipmentTypeList = list;
            if (_this.modelTypeList) {
                _this.modelTypeList = list;
            }
            if (_this.modelTypeList2) {
                _this.modelTypeList2.push({
                    code: '0',
                    title: '全部'
                });
                for (let i = 0; i < list.length; i++) {
                    _this.modelTypeList2.push({
                        code: list[i].code,
                        title: list[i].title
                    })
                }
            }
        })
}
//gis模型新增模型信息
export const addGisModelData = (data, _this) => {
    axios.post('/equipment/gisModel/add', data)
        .then(res => {

            if (res.code == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.addReset();
                _this.getTabledata();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//gis模型删除模型信息
export const deleteGisModelData = (data, _this) => {
    axios.post('/equipment/gisModel/delete', data)
        .then(res => {
            if (res.code == 200) {
                _this.$message.info(res.message);

            } else {
                _this.$message.info(res.message);

            }

        })

}


//gis模型获取模型信息
export const getGisModelIdData = (data, _this) => {
    axios.get('/equipment/gisModel/queryById', {
        params: data
    })
        .then(res => {

            if (res.code == 200) {
                _this.modelTypeCode = res.result.modelTypeCode; //模型类型编码
                _this.modelName = res.result.modelName; //模型名称
                _this.modelType = res.result.modelType; //模型类型
                _this.latitude = res.result.latitude; //纬度
                _this.longitude = res.result.longitude; //经度
                _this.modelLevel = res.result.modelLevel; //层级
                _this.modelPosition = res.result.modelPosition; //模型显示位置
                _this.modelOnImg = res.result.modelOnImg; //打开图片地址
                _this.modelOffImg = res.result.modelOffImg; //关闭图片地址
                _this.modelWaringImg = res.result.modelWaringImg; //警报图片地址
                _this.modelOffset = res.result.modelOffset; //模型偏移量
            } else {
                _this.$message.info(res.message);
            }
        })
}
//gis模型修改模型信息
export const editGisModelData = (data, _this) => {
    axios.post('/equipment/gisModel/edit', data)
        .then(res => {
            if (res.code == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.visible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//gis管理-绑定变量列表获取
export const getBindVarList = (data, _this) => {
    axios.get('/equipment/gisModel/getIotInfoList', {
        params: data
    })
        .then(res => {
            _this.loading2 = false;
            if (res.code * 1 == 200) {
                _this.dataSource2 = [];
                let list = res.result.records;
                list.map(index => {
                    _this.dataSource2.push({
                        varName: index.variableName,
                        varHint: index.variableTitle,
                        bindStatus: index.bindStatus,
                        id: index.id
                    })
                })
                _this.ipagination2.current = res.result.current;
                _this.ipagination2.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.dataSource2 = [];
                _this.ipagination2.current = 0;
                _this.ipagination2.total = 1;
            }
        })
}
//gis管理-绑定变量-绑定
export const bindGisIot = (data, _this) => {
    axios.post('/equipment/gisModel/bindGisIot', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.bindVarList();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//查询Gis模型对应的已经绑定的变量
export const getBindGisIotList = (data, _this) => {
    axios.get('/equipment/gisModel/getBindGisIotList', {
        params: data
    })
        .then(res => {
            _this.loading3 = false;
            _this.dataSource3 = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource3.push({
                        varName: index.variableName,
                        varHint: index.variableTitle,
                        bindStatus: index.bindStatus,
                        id: index.id,
                        displayOrder: index.displayOrder
                    })
                })
                _this.ipagination3.current = res.result.current;
                _this.ipagination3.total = res.result.total;
            } else {
                _this.$message.info(res.message);
                _this.ipagination3.current = 0;
                _this.ipagination3.total = 1;

            }
        })
}
//解除Gis模型跟变量绑定
export const unbindGisIot = (data, _this) => {
    axios.get('/equipment/gisModel/unbindGisIot', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.ipagination3.current = 1;
                _this.variableDetailsList();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//绑定Gis模型
export const bindGisData = (data, _this) => {
    axios.post('/equipment/optEquipment/bindGisData', data)
        .then(res => {
            if (res.code == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.bindGisVisible = false;
                _this.gismodelName = '';
            } else {
                _this.$message.info(res.message);
            }
        })
}
//解除Gis模型绑定
export const unbindGisData = (data, _this) => {
    axios.get('/equipment/optEquipment/unbindGisData', {
        params: data
    })
        .then(res => {
            if (res.code == 200) {
                _this.$message.info(res.message);
                _this.updata();

            } else {
                _this.$message.info(res.message);
            }
        })
}
//查询设备关联gis模型下拉列表
export const getGisVoList2 = (data, _this) => {
    axios.get('/equipment/optEquipment/getGisVoList', {
        params: data
    })
        .then(res => {


            _this.gisloading2 = false;
            _this.gisdataSource2 = [];
            if (res.code == 200) {
                _this.dataSource2 = [];
                let list = res.result.records;
                list.map(index => {
                    _this.gisdataSource2.push({
                        id: index.id,

                        name: index.name,

                        type: index.type,

                    })
                })
                _this.gisipagination2.current = res.result.current;
                _this.gisipagination2.total = res.result.total;

            } else {
                _this.$message.info(res.message);
                _this.gisdataSource2 = [];
                _this.gisipagination2.current = 0;
                _this.gisipagination2.total = 1;

            }
        })
}
// GIS模型管理-更新图片信息
export const gisModelEditImage = (data, _this) => {
    axios.post('/equipment/gisModel/editImage', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.updata();
                _this.$message.info(res.message);
                _this.changeProgrammeVisible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}

// GIS模型管理-查询模型库列表
export const gisModelGetResSn = (data, _this) => {
    axios.get('/equipment/gisModel/getResSn', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.gisResSnList = res.result;
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

//gis模型-同步缓存
export const synchronousCache = (data, _this) => {
    axios.post(data.url)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);


            } else {
                _this.$message.info(res.message);
            }
        })
}
//GIS模型-变量详情-设置序号
export const gisModelSetOrder = (data, _this) => {
    axios.get('/equipment/gisModel/setOrder', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.setUpSerialNumberVisible = false;
                _this.ipagination3.current = 1;
                _this.variableDetailsList();


            } else {
                _this.$message.info(res.message);
            }
        })
}