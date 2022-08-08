import {axios} from "../../utils/request";
//职务表-停用启用
export const positionStartOrStop = (data, _this) => {
    axios.get('/sys/position/startOrStop', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//校验用户密码是否超过30天
export const userCheckPassword = (_this) => {
    axios.get('/system/userCheck/password')
        .then(res => {
            if (res.code * 1 == 200) {
                if (res.result) {
                    _this.$confirm({
                        title: '提示',
                        content: '您的密码等级较低，请及时更换密码！',
                        onOk() {
                            _this.updatePassword();
                        },
                        onCancel() {
                        },
                    });
                }
            } else {
                _this.$message.info(res.message);
            }
        })
}
//获取文档列表
export const getDocListData = (_this) => {
    axios.get('/oa/docs/data/A17A01')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.docList = res.result;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//公共配置-获取系统的界面配置信息
export const anonBaseConfig = (_this) => {
    axios.get('/anon/baseConfig/')
        .then(res => {
            if (res.code * 1 == 200) {
                $('.userLayout').css({
                    'background': 'url(' + res.result.loginBg + ') no-repeat',
                    'background-size': 'cover'

                });
                _this.systemName = res.result.systemName;
                _this.verticalLogo = res.result.verticalLogo;
                _this.simpleLogo = res.result.simpleLogo;
                _this.horizontalLogo = res.result.horizontalLogo;
            } else {
                _this.$message.info(res.message);
            }
        })
}
export const roleStartOrStop = (data, _this) => {
    axios.get('/sys/role/startOrStop', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.loadData();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//获取个人中心数据
export const getMyuserData = (_this) => {
    axios.get('/sys/user/my')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.edit(res.result);
            } else {
                _this.$message.info(res.message);
            }
        })
}