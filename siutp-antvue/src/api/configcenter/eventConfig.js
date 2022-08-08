import {axios} from '@/utils/request';

// 事件与配置绑定--启停用
export const updateEventStatus = (data, _this) => {
    axios({
        url: "/message/smsEvent/editStatus",
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

// 事件与配置绑定--分页
export const getEventList = (data, _this) => {
    axios({
        url: "/message/smsEvent",
        methods: "get",
        params: data
    }).then(res => {
        _this.tableDataSource = [];
        _this.loading = false;
        if (res.code * 1 == 200) {
            let list = res.result.records;
            list.map(index => {
                _this.tableDataSource.push({
                    eventName: index.eventName, // 事件名称
                    eventCode: index.eventCode, // 事件code
                    eventStatus: index.eventStatus == "1" ? true : false, // 启停用状态
                    templateName: index.templateName, // 短信配置
                    templateId: index.templateId, // 短信配置id
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

// 事件与配置绑定--添加/修改
export const addEvent = (data, _this) => {
    axios.post("/message/smsEvent/one", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
//          _this.changeImgVisible = false;
            _this.visible = false;
//          _this.addReset();
            _this.updata();
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 短信模板配置下拉选
export const SMSDropdown = (data, _this) => {
    axios({
        url: "/message/smsTemplateConfig/dropdown",
        method: "get",
        params: data
    }).then(res => {
        if (res.code * 1 == 200) {
            let list = res.result;
            _this.SMSDropdownList = [];
            list.map(index => {
                _this.SMSDropdownList.push({
                    id: index.id,
                    templateName: index.templateName
                })
            })
        } else {
            _this.$message.info(res.message);
        }
    })
}
