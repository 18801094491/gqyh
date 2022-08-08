import {
    axios
} from '@/utils/request';
export const URL = {
    add: "/work/thirdTeam/one",
    edit: "/work/thirdTeam/one",
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
//第三方维修团队-联系人职位获取列表
export const getThirdPartyTeamContactTitle = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1217620983487250433'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.contactPositionList.push({
                        "code": index.code,
                        "title": index.title
                    });

                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
//第三方维修团队-团队评级获取列表
export const getThirdPartyTeamRating = (_this) => {
    axios.get('/sys/dict/getDictValue/team_rate')
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                list.map(index => {
                    _this.teamRatingList.push({
                        "code": index.code,
                        "title": index.title
                    });

                });

            } else {
                _this.$message.info(res.message);
            }
        })
}
