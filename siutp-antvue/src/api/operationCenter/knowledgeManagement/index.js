import {
    axios
} from '@/utils/request';
//知识库管理-分页列表查询
export const getKnowlegeListData = (data, _this) => {
    axios.get('/equipment/knowlege/list', {
        params: data
    })
        .then(res => {
            _this.loading = false;
            _this.dataSource = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                _this.dataSource = list;
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
//客户信息管理-新增上传
export const customerInformationUploadFile = (data, _this) => {
    axios.post('/sys/file/upload', data)
        .then(res => {

            if (res.code * 1 == 200) {

                _this.fileList.push({
                    fileName: res.result.fileName,
                    filePath: res.result.filePath
                });

                $('#uploadBtn').val('');
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识库管理-添加
export const addKnowlege = (data, _this) => {
    axios.post('/equipment/knowlege/add', data)
        .then(res => {

            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.visible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-新增上传
export const knowlegeAttachUploadFile = (data, _this, index) => {
    axios.post('/sys/file/upload/more', data)
        .then(res => {

            if (res.code * 1 == 200) {
                res.result.map(i => {
                    _this.knowlegeItemVoList[index].knowlegeAttachList.push({
                        fileName: i.fileName,
                        attachFile: i.filePath
                    });
                })

                $('.uploadBtnB').eq(index).val('');
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-修改回显
export const changeKnowlegeIdData = (data, _this) => {
    axios.get('/equipment/knowlege/getById', {
        params: data
    })
        .then(res => {
            _this.knowlegeItemVoList = [];
            if (res.code * 1 == 200) {
                let obj = res.result;
                setTimeout(() => {
                    obj.knowlegeItemVoList.map(index => {
                        _this.knowlegeItemVoList.push({
                            knowlegeAttachList: index.knowlegeAttachList,
                            knowlegeCautionList: index.knowlegeCautionList,
                            knowlegeOperationList: index.knowlegeOperationList,
                            operationName: index.operationName,
                            id: index.id
                        })
                    });
                }, 1000)
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-修改
export const editKnowlegeIdData = (data, _this) => {

    axios.post('/equipment/knowlege/edit', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.updata();
                _this.visible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-所属类型
export const getOwnershipType = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1215455801774047234'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.ownershipTypeList = [];
                list.map(index => {
                    _this.ownershipTypeList.push({
                        code: index.code,
                        title: index.title
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}

//知识管理-知识类型
export const getKnowledgeType = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1215455919336194050'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.knowledgeTypeList = [];
                list.map(index => {
                    _this.knowledgeTypeList.push({
                        code: index.code,
                        title: index.title
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
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
//知识管理-规程-名称保存
export const knowlegeItemEditItemName = (data, _this) => {
    axios.post('/equipment/knowlegeItem/editItemName', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);

            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-操作规程-添加
export const knowlegeOperationAdd = (data, _this, index) => {
    axios.post('/equipment/knowlegeOperation/add', data)
        .then(res => {
            _this.knowlegeOperationvisible = false;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeOperationList.push({
                    displayOrder: res.result.displayOrder,
                    id: res.result.id,
                    operationItem: res.result.operationItem,
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-安全事项-添加
export const knowlegeCautionAdd = (data, _this, index) => {
    axios.post('/equipment/knowlegeCaution/add', data)
        .then(res => {
            _this.knowlegeCautionvisible = false;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeCautionList.push({
                    displayOrder: res.result.displayOrder,
                    id: res.result.id,
                    cautionItem: res.result.cautionItem,
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-操作规程-编辑
export const knowlegeOperationEdit = (data, _this, index, zsIndex) => {
    axios.post('/equipment/knowlegeOperation/edit', data)
        .then(res => {
            _this.knowlegeOperationvisible = false;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeOperationList.splice(zsIndex, 1, {
                    displayOrder: data.displayOrder,
                    id: data.id,
                    operationItem: data.operationItem,
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-安全事项-编辑
export const knowlegeCautionEdit = (data, _this, index, zsIndex) => {
    axios.post('/equipment/knowlegeCaution/edit', data)
        .then(res => {
            _this.knowlegeCautionvisible = false;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeCautionList.splice(zsIndex, 1, {
                    displayOrder: data.displayOrder,
                    id: data.id,
                    cautionItem: data.cautionItem,
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-操作规程-删除
export const knowlegeOperationModifyknowlegeop = (data, _this, index, zsIndex) => {
    axios.get('/equipment/knowlegeOperation/modifyknowlegeop', {
        params: data
    })
        .then(res => {

            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeOperationList.splice(zsIndex, 1);
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-安全事项-删除
export const knowlegeCautionModifyknowlegeca = (data, _this, index, zsIndex) => {
    axios.get('/equipment/knowlegeCaution/modifyknowlegeca', {
        params: data
    })
        .then(res => {
            _this.knowlegeCautionvisible = false;
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeCautionList.splice(zsIndex, 1)
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-上传合同附件
export const uploadMoreKnowlege = (data, _this, index) => {
    axios.post('/sys/file/upload/moreKnowlege', data)
        .then(res => {

            if (res.code * 1 == 200) {
                res.result.map(i => {
                    _this.knowlegeItemVoList[index].knowlegeAttachList.push({
                        fileName: i.fileName,
                        attachFile: i.filePath,
                        itemId: i.itemId,
                        id: i.id
                    });
                })

                $('.uploadBtnB').val('');
            } else {
                _this.$message.info(res.message);
            }
        })
}
//知识管理-规程-删除合同附件
export const knowlegeAttachDelete = (data, _this, index, zsIndex) => {
    axios.get('/equipment/knowlegeAttach/delete', {
        params: data
    })
        .then(res => {

            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.knowlegeItemVoList[index].knowlegeAttachList.splice(zsIndex, 1);
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
// 根据pcode获取分类字典
export const getCategory = (data,_this,list) => {
    axios.get('/sys/category/loadTreeRootCascade',{params:data})
        .then(res => {
            if (res.code * 1 == 200) {
                _this[list] = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}
