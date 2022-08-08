import { axios } from '@/utils/request'

//gis模型-同步缓存
export const synchronousCacheByGet = (data, _this) => {
    axios.get(data.url)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
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

// 系统参数配置页面---获取表格列表
export const getTestTableList = (data, _this) => {
    axios({
        url: "/system/systemConfig",
        methods: "get",
        params: data
    }).then(res => {
        _this.tableDataSource = [];
        _this.loading = false;
        if (res.code * 1 == 200) {
            let list = res.result.records;
            list.map(index => {
                _this.tableDataSource.push({
                    configName: index.configName,
                    configKey: index.configKey,
                    configDescription: index.configDescription,
                    configStatusName: index.configStatusName,
                    configStatus: index.configStatus == 1 ? true : false,
                    configValue: index.configValue,
                    accessValues: index.accessValues,
                    id: index.id,
                    configValueList: eval('(' + index.accessValues + ')')
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

// 系统参数配置页面---新增/修改
export const addConfig = (data, _this) => {
    axios.post("/system/systemConfig/one", data).then(res => {
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

// 系统参数配置页面---更改启停用状态
export const updateConfigstatus = (data, _this) => {
    axios({
        url: "/system/systemConfig/editStatus",
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