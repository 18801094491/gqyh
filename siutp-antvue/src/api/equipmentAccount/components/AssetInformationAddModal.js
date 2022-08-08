import { axios } from '@/utils/request'
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