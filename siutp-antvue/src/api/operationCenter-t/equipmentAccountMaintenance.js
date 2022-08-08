import {
    axios
} from '@/utils/request';

//获取设备类型值
export const getEquipmentTypeData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206872024991416321'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
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
            } else {
                _this.$message.info(res.message);
            }

        })
}

//获取所属标段值
export const getBidSegmentData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206872782805680130'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.bidSegmentList = list;
            } else {
                _this.$message.info(res.message);
            }


        })
}

//获取设备台账列表
export const getEquipmentLedgerData = (data, _this) => {
    axios.get('/equipment/optEquipment/list', {
        params: data
    })
        .then(res => {
            _this.dataSource = [];
            _this.loading = false;
            if (res.code * 1 == 200) {


                let list = res.result.records;
                list.map(index => {
                    _this.dataSource.push({
                        number: index.equipmentSn,
                        equipmentName: index.equipmentName,
                        equipmentCategory: index.equipmentCategory,
                        equipmentType: index.equipmentType,
                        scaleModel: index.equipmentMode,
                        bidSegment: index.equipmentSection,
                        position: index.equipmentLocation,
                        supplier: index.equipmentSupplier,
                        state: index.equipmentStatus,
                        id: index.id,
                        bindStatus: index.bindStatus,
                        scale: index.equipmentSpecs,
                        equipmentOperating: index.equipmentOperating,
                        equipmentPurchase: index.equipmentPurchase,
                        equipmentRevstop: index.equipmentRevstop,
                        equipmentRevstopText: index.equipmentRevstopText,
                        equipmentSpecsName: index.equipmentSpecsName,
                        equipmentModelName: index.equipmentModeName,
                        equipmentTypeName: index.equipmentType,
                        equipmentSupplierName: index.equipmentSupplier,
                        labelName: index.labelName,
                        labelId: index.labelId,
                        planDate:index.planDate,
                        countDays:index.countDays,
                        dispatchStatus:index.dispatchStatus,
                        personResponsible:index.personResponsible,
                        personResponsibleName:index.personResponsibleName,
                        
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


//设备台账新增
export const addEquipmentLedgerData = (data, _this) => {
    axios.post('/equipment/optEquipment/add', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.addReset();
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
}
//设备台账修改
export const changeEquipmentLedgerData = (data, _this) => {
    axios.post('/equipment/optEquipment/edit', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.visible = false;
                _this.addReset();
                _this.updata();
            } else {
                _this.$message.info(res.message);
            }
        })
} 
//设备台账修改回显参数获取
export const getEquipmentLedgerChangeData = (data, _this) => {
    axios.get('/equipment/optEquipment/queryById', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.visible = true;
                if (res.result.equipmentType) {
                    setTimeout(() => {
                        _this.changeEquipmentTypeName(res.result.equipmentType);
                    }, 500)
                }
                if (res.result.equipmentMode) {
                    setTimeout(() => {
                        _this.changeEquipmentModel(res.result.equipmentMode);
                    }, 800)
                }


                setTimeout(() => {
                    _this.equipmentNumber = res.result.equipmentSn;
                    _this.equipmentName = res.result.equipmentName;
                    _this.equipmentCategory = res.result.equipmentCategory;
                    _this.equipmentType = res.result.equipmentType;
                    _this.equipmentMode = res.result.equipmentMode;
                    _this.bidSegment = res.result.equipmentSection;
                    _this.position = res.result.equipmentLocation;
                    _this.supplier = res.result.equipmentSupplier;
                    _this.state = res.result.equipmentStatus;
                    _this.equipmentSpecs = res.result.equipmentSpecs;
                    _this.equipmentRevstop = res.result.equipmentRevstop;
                    // _this.equipmentSupplierName = res.result.equipmentSupplierName;
                    // _this.equipmentTypeName = res.result.equipmentTypeName;
                    // _this.equipmentModelName = res.result.equipmentModelName;
                    // _this.equipmentSpecsName = res.result.equipmentSpecsName;
                    // _this.form.setFieldsValue(pick(_this.model,'equipmentPurchase','equipmentOperating'));
                    _this.form.setFieldsValue({equipmentPurchase: res.result.equipmentPurchase ? moment(res.result.equipmentPurchase) : null})
                    _this.form.setFieldsValue({equipmentOperating: res.result.equipmentOperating ? moment(res.result.equipmentOperating) : null})
                    // _this.equipmentPurchase=res.result.equipmentPurchase;
                    // _this.equipmentOperating=res.result.equipmentOperating;
                }, 1000)


            } else {
                _this.$message.info(res.message);
            }
        })
}

//获取设备类别值
export const getEquipmentCategoryData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206838473407348737'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.equipmentCategoryList = list;
            } else {
                _this.$message.info(res.message);
            }

        })
}

//获取供应商值-新增或修改
export const getQueryNameList = (_this) => {
    axios.get('/equipment/optSupplier/queryNameList')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.supplierClassificationList = list;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取供应商值-筛选
export const getQueryNameList2 = (_this) => {
    axios.get('/equipment/optSupplier/queryNameList')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;

                list.map(index => {
                    _this.supplierClassificationList2.push({
                        id: index.id,
                        supplierName: index.supplierName
                    })
                })
                console.log(_this.supplierClassificationList2)
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取设备资产详情
export const getEquipmentAssetsData = (data, _this) => {
    axios.get('/equipment/optEquipment/getDetail', {
        params: data
    })
        .then(res => {
            _this.attributeList = [];
            if (res.code * 1 == 200) {
                _this.detailsVisible = true;
                let obj = res.result;
                _this.usageState = obj.usageState ? '打开' : '关闭'; //获取详情-使用状态
                _this.deviceStatus = obj.deviceState ? '正常' : '故障'; //获取详情-设备状态
                _this.assetCoding = obj.optEquipmentModel.equipmentSn; //获取详情-资产编号
                _this.assetImg = obj.optEquipmentModel.equipmentImg; //获取详情-设备图片
                _this.assetName = obj.optEquipmentModel.equipmentName; //获取详情-资产名称
                _this.manufacturer = obj.optEquipmentModel.equipmentSupplier; //获取详情-生产厂家
                _this.informationEquipmentType = obj.optEquipmentModel.equipmentModeName; //获取详情-设备型号
                _this.assetLocation = obj.optEquipmentModel.equipmentLocation; //获取详情-资产位置
                // _this.attributeList = obj.list.concat(obj.equipData);
                _this.upkeepCount = obj.optEquipmentModel.upkeepCount;
                _this.upkeepTimeBY = obj.optEquipmentModel.upkeepTimeBY;
                _this.upkeepTimeWX = obj.optEquipmentModel.upkeepTimeWX;
                if (obj.list.length) {
                    obj.list.map(index => {
                        _this.attributeList.push({
                            attributeCn: index.attributeCn,
                            attributeVal: index.attributeVal
                        })
                    })
                }
                if (obj.equipData) {
                    if (obj.equipData.length) {
                        obj.equipData.map(index => {
                            _this.attributeList.push({
                                attributeCn: index.variableName,
                                attributeVal: index.varibleValue
                            })
                        })
                    }
                }
                _this.dataSource3 = [];
                if (obj.knowlegeEquipData) {
                    if (obj.knowlegeEquipData.length) {
                        obj.knowlegeEquipData.map(index => {
                            _this.dataSource3.push({
                                knowlegeName: index.knowlegeName,
                                id: index.id,
                                type: index.type
                            })
                        })
                    }
                }

            } else if (res.code * 1 == 210) {

            } else {
                _this.$message.info(res.message);
            }

        })
}

//获取资产状态
export const getAssetStatus = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1207196946951360514'
        }
    })
        .then(res => {

            if (res.code == 200) {
                _this.assetStatusList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}
//设备台账维护-设备台账-启停用状态更改
export const optEquipmentQueryRevstopById = (data, _this) => {
    axios.get('/equipment/optEquipment/queryRevstopById', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.ipagination.current = 1;
                _this.updata();
            } else {
                _this.$message.info(res.message);

            }
        })
}

//设备管理-知识回显
export const knowlegeIdData = (data, _this) => {
    axios.get('/equipment/optEquipment/getKnowlege', {
        params: data
    })
        .then(res => {
            _this.knowlegeItemVoList = [];
            if (res.code * 1 == 200) {
                let obj = res.result;


                setTimeout(() => {
                    _this.knowlegeName = obj.knowlegeName;
                    _this.category = obj.category;
                    _this.equipmentModel = obj.equipmentModel;
                    _this.equipmentSpecs = obj.equipmentSpecs;
                    _this.equipmentTypeName = obj.equipmentTypeName;
                    _this.type = obj.type;

                    obj.knowlegeItemVoList.map(index => {
                        _this.knowlegeItemVoList.push({
                            knowlegeAttachList: index.knowlegeAttachList,
                            knowlegeCautionList: index.knowlegeCautionList,
                            knowlegeOperationList: index.knowlegeOperationList,
                            operationName: index.operationName,
                            id: index.id
                        })
                    });
                    _this.zsVisible = true;
                }, 1000)


            } else {
                _this.$message.info(res.message);
            }
        })
}

//根据资源名称获取资源类型
export const getEquipmentModelData = (data, _this) => {
    axios.get('/sys/cate/' + data.pcate)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.equipmentModelList = [];
                _this.equipmentMode = '';
//              _this.equipmentSpecs = '';
                _this.equipmentModelList = [];
                _this.equipmentSpecsList = [];

                let list = res.result;
                list.map(index => {
                    _this.equipmentModelList.push({
                        code: index.code,
                        title: index.title
                    })
                });

            } else {
                _this.$message.info(res.message);
            }
        })
}

//根据资源类型获取规格类型
export const getEquipmentSpecsData = (data, _this) => {
//	_this.equipmentSpecs = '';
    _this.equipmentSpecsList = [];
    axios.get('/sys/cate/' + data.pcate)
        .then(res => {
            if (res.code * 1 == 200) {               
                let list = res.result;
                list.map(index => {
                    _this.equipmentSpecsList.push({
                        code: index.code,
                        title: index.title
                    })
                });
            } else {
                _this.$message.info(res.message);
            }
        })

}


//设备台账-获取设备类型
export const getA01 = (_this) => {
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

//ioServer管理-新增修改代理-代理状态.心跳状态
export const getAgencyStatus = (_this) => {
    axios.get('/sys/dict/getDictValue/working_status')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.workingStatusList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//获取用户信息
export const getUserData = (data, _this) => {
    axios.get('/sys/user/idList', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.managerList = [];
                _this.managerList2 = [];
                list.map(index => {
                    _this.managerList.push({
                        id: index.id,
                        name: index.name
                    })
                    _this.managerList2.push({
                        id: index.id,
                        name: index.name
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}

//班组管理-班组排班管理-班组信息获取
export const getTeamDutyTeamList = (_this) => {
    axios.get('/work/teamDuty/teamList')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.teamInformationList = res.result;
            } else {
                _this.$message.info(res.message);
                _this.teamInformationList = res.result;
            }
        })
}
 /******************************************************* */
// 设备台账页面--设备类型图片新增
export const addEquiImg = (data, _this) => {
    axios.post("/equipment/optEquipment/uploadEqTypeImg", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.changeImgVisible = false;
            _this.addReset();
            _this.updata();
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 设备台账页面--设备类型图片s\上传
export const equiImgUpload=(data,_this,index)=>{
    axios.post('/sys/file/uploadGisNav',data)
    .then(res=>{
        if(res.code*1==200){
            _this.stateList[index].imgUrl=res.result.filePath;
            _this.stateList[index].fileName=res.result.fileName;
            _this.stateList[index].fileThumbPath=res.result.fileThumbPath;
            _this.stateList[index].width=res.result.width;
            _this.stateList[index].height=res.result.height;
        }else{
            _this.$message.info(res.message);
        }
    })
}
// 设备信息管理---设备标签--绑定
export const labelBind = (data,_this)=>{
    axios({
        url:"/equipment/optEquipment/bindLabel",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            _this.$message.info(res.message);
            _this.labelVisible=false;
            _this.updata();
        }
    })
}

// 设备信息管理---设备标签--解除绑定
export const labelUnbind = (data,_this)=>{
    axios({
        url:"equipment/optEquipment/unbindLabel",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            _this.$message.info(res.message);
            _this.updata();
        }else{
            _this.$message.info(res.message);
        }
    })
}
// 设备基本信息--修改计划日期
export const updatePlan = (data, _this) => {
	axios.post('/opt/upkeep/plan/createPlan',data)
    .then(res => {
        if (res.code * 1 == 200) {
        	_this.updata();
        	_this.planVisible = false;
        	_this.$message.info(res.message);
        } else {
            _this.$message.info(res.message);
        }
    })
}

// 设备基本信息--设置养护到期日期
export const setPlanDate = (data, _this) => {
	axios.get('/opt/upkeep/plan/change/userAndTime',{params:data})
    .then(res => {
        if (res.code * 1 == 200) {
        	_this.setDateVisible = false;
        	_this.updata();
        	_this.$message.info(res.message);
        } else {
            _this.$message.info(res.message);
        }
    })
}

// 分派工单
export const updateAssignWoker = (data, _this,urlStr,type) => {
	axios.post(urlStr,data).then(res => {
        if (res.code * 1 == 200) {
        	_this.workerVisible = false;
        	if(type=='load'){        		
        		_this.loadData();
        	}else{
        		_this.updata();
        	}
        	_this.$message.info(res.message);
        } else {
            _this.$message.info(res.message);
        }
    })
}

// 设备台账--问题上报--问题设备下拉(分派工单设备信息下拉)
export const getEquipmentList = (urlStr,_this) => {
	_this.equipmentList = [];
	axios.get(urlStr).then(res => {
        if (res.code * 1 == 200) {
        	_this.equipmentList = res.result
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 设备基本信息--获取养护倒计时时间
export const getPlanDate = (_this) => {
	axios.get('/opt/upkeep/plan/getUserAndTime')
    .then(res => {
        if (res.code * 1 == 200) {
        	_this.setForm.days = res.result.time;
		    _this.setForm.usersId = res.result.userId;
        } else {
            _this.$message.info(res.message);
        }
    })
}