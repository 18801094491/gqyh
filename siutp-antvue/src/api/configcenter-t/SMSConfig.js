import { axios } from '@/utils/request';

//仓库管理-管理员下拉获取
export const getStoreUserListData = (data, _this) => {
    axios.get('/equipment/store/userList', {
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


// 短信模板配置页面---更改启停用状态
export const updateSMSstatus = (data, _this) => {
    axios({
        url: "/message/smsTemplateConfig/editStatus",
        methods: "get",
        params: data
    }).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.ipagination.current = 1;
            _this.updata();
        } else {
            _this.$message.info(res.message);
        }
    })
}

// 短信模板配置页面---获取表格分页列表
export const getSMSList = (data, _this) => {
    axios({
        url: "/message/smsTemplateConfig",
        methods: "get",
        params: data
    }).then(res => {
        _this.tableDataSource = [];
        _this.loading = false;
        if (res.code * 1 == 200) {
            let list = res.result.records;
            list.map(index => {
                _this.tableDataSource.push({
                    templateName: index.templateName, // 模板名称
                    templateCode: index.templateCode, // 模板code
                    templateContent: index.templateContent, // 模板keys
                    signName: index.signName, // 模板标签
                    templateStatus: index.templateStatus == "1" ? true : false, // 启停用状态
                    usersName: index.usersName, // 短信配置
                    usersId: index.usersId, // 发送人员id
                    moduleId: index.moduleId, // 模块id
                    id: index.id
                })
            })
            _this.ipagination.current = res.result.current;
            _this.ipagination.total = res.result.total;
        } else {
            _this.$message.info(res.message);
            _this.tableDataSource = [];
            _this.ipagination.current = 0;
            _this.ipagination.total = 1;
        }
    })
}

// 短信模板配置页面---新增/修改
export const addSMS = (data, _this) => {
    axios.post("/message/smsTemplateConfig/one", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.visible = false;
            _this.addReset();
            _this.updata();
        } else {
            _this.$message.info(res.message);
        }
    })
}