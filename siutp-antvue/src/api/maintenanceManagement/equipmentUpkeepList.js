import {
    axios
} from '@/utils/request';

//维保管理-新增维保记录
export const equipmentUpkeepOne = (data, _this) => {
    axios.post('/equipment/equipmentUpkeep/one', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.loadData(1);
                _this.equipmentUpkeepvisible = false;
            } else {
                _this.$message.info(res.message);
            }
        })
}
//维保管理-新增维保记录-上传附件
export const equipmentUpkeepOneUpLoad = (data, _this) => {
    axios.post('/sys/file/upload/more/pic', data)
        .then(res => {
            if (res.code * 1 == 200) {
                res.result.map(i => {
                    _this.fileList.push({
                        fileName: i.fileName,
                        filePath: i.filePath,
                        fileThumbPath: i.fileThumbPath
                    });
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}

//维保管理-新增维保记录-获取设备下拉列表
export const equipmentUpkeepEquipList = (data, _this) => {
    axios.get('/equipment/equipmentUpkeep/equipList', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.equipmentList = res.result;
            } else {
                _this.$message.info(res.message);
            }
        })
}

//维保管理-新增维保记录-获取维保类型
export const sysdictUpkeepType = (_this) => {
    axios.get('/sys/dict/getDictValue/upkeep_type')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.upkeepTypeList = res.result;
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
