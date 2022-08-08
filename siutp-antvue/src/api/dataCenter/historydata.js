import {
    axios
} from '@/utils/request';
import {ACCESS_TOKEN} from "@/store/mutation-types"
import Vue from 'vue'
import moment from 'moment'

export default {
    name: 'historicalData',
    data() {
        return {
            description: '历史数据',
            queryParam: {
                inequipmentType: '',
                deviceInformation: '',
                time: [],
            },
            columns: [],
            dataSource: [],
            loading: false,
            inequipmentTypeList: [],
            deviceInformation: '',
            deviceInformationList: [],
            pageNo: 1,
            pageSize: 20,
            total: '',
            sum: 1, // 根据total计算总共多少页
            url: {
                exportXlsUrl: "/statistic/export/hisdata",
            },
            bOk: true,
            jumpPagNum: ''
        }
    },
    mounted() {
        //设备类型获取
        axios.get('/sys/category/loadTreeRootCascade?pcode=A03')
            .then(res => {
                    if (res.code * 1 == 200) {
                        let list = res.result;
                        this.inequipmentTypeList = list;
                    } else {
                        this.$message.info(res.message);
                    }
                }
            )
    },
    methods: {
        moment,
        toPage() {
            this.pageNo = this.jumpPagNum * 1;
            this.updata();
        },
        disabledDateLeft(current) {
            if (!this.queryParam.right) return false;
            const endValue = new Date(this.queryParam.right.format("YYYY-MM-DD")).getTime();
            const startValue = new Date(current.format("YYYY-MM-DD")).getTime();
            return startValue > endValue;
        },
        disabledTimeLeft(current) {
            if (!this.queryParam.right) return false;
            if (!current) current = this.queryParam.right;
            const endValue = new Date(this.queryParam.right.format("YYYY-MM-DD")).getTime();
            const startValue = new Date(current.format("YYYY-MM-DD")).getTime();
            if (endValue !== startValue) return false;
            let rh = new Date(this.queryParam.right).getHours();
            let rm = new Date(this.queryParam.right).getMinutes();
            let rs = new Date(this.queryParam.right).getSeconds();
            let lh = new Date(current).getHours();
            let lm = new Date(current).getMinutes();

            return {
                disabledHours: () => {
                    const result = [];
                    for (let i = 23; i > rh && i >= 0; i--) {
                        result.push(i);
                    }
                    return result;
                },
                disabledMinutes: () => {
                    const result = [];
                    if (rh !== lh) return result;
                    for (let i = 59; i < rm && i >= 0; i--) {
                        result.push(i);
                    }
                    return result;
                },
                disabledSeconds: () => {
                    const result = [];
                    if (rh !== lh || rm !== lm)
                        for (let i = 59; i < rs && i >= 0; i--) {
                            result.push(i);
                        }
                    return result;
                }
            }
        },
        disabledDateRight(current) {
            if (!this.queryParam.left) return false;
            const startValue = new Date(this.queryParam.left.format("YYYY-MM-DD")).getTime();
            const endValue = new Date(current.format("YYYY-MM-DD")).getTime();
            return startValue > endValue;
        },
        disabledTimeRight(current) {
            if (!this.queryParam.left) return false;
            if (!current) current = this.queryParam.left;
            const startValue = new Date(this.queryParam.left.format("YYYY-MM-DD")).getTime();
            const endValue = new Date(current.format("YYYY-MM-DD")).getTime();
            if (endValue !== startValue) return false;
            let lh = new Date(this.queryParam.left).getHours();
            let lm = new Date(this.queryParam.left).getMinutes();
            let ls = new Date(this.queryParam.left).getSeconds();
            let rh = new Date(current).getHours();
            let rm = new Date(current).getMinutes();

            return {
                disabledHours: () => {
                    const result = [];
                    for (let i = 0; i < lh; i++) {
                        result.push(i);
                    }
                    return result;
                },
                disabledMinutes: () => {
                    const result = [];
                    if (rh !== lh) return result;
                    for (let i = 0; i < lm; i++) {
                        result.push(i);
                    }
                    return result;
                },
                disabledSeconds: () => {
                    const result = [];
                    if (rh !== lh || rm !== lm) return result;
                    for (let i = 0; i < ls; i++) {
                        result.push(i);
                    }
                    return result;
                }
            }
        },
        //数据初始化
        updata() {
            let data = {
                equipmentId: this.queryParam.deviceInformation,
                left: this.queryParam.left ? this.moment(this.queryParam.left).format('YYYY-MM-DD HH:mm') : '',
                right: this.queryParam.right ? this.moment(this.queryParam.right).format('YYYY-MM-DD HH:mm') : '',
                pageNo: this.pageNo,
                pageSize: this.pageSize,
            };
            (function (data, _this) {
                axios.post('/iot/vardata', data)
                    .then(res => {
                        _this.loading = false;
                        _this.bOk = true;
                        if (res.code * 1 == 200) {
                            let list = res.result.data;
                            let headerList = res.result.header;
                            if (list.length) {
                                _this.dataSource = [];
                                _this.columns = [];
                            } else {
                                _this.$message.info('无更多数据!');
                                return;
                            }
                            _this.pageNo = res.result.pageNo;
                            _this.total = res.result.total
                            _this.columns = [{
                                title: '序号',
                                dataIndex: '',
                                key: 'rowIndex',
                                width: 60,
                                align: "center",
                                customRender: function (t, r, index) {
                                    return parseInt(index) + 1;
                                }
                            },]
                            headerList.map(index => {
                                _this.columns.push({
                                    title: index.title,
                                    align: "center",
                                    dataIndex: index.data
                                })
                            })
                            _this.dataSource = list;

                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            })(data, this);
        },
        //查询
        searchQuery() {
            if (!this.queryParam.inequipmentType) {
                this.$message.info('请选择设备类型!');
                return;
            }
            if (!this.queryParam.deviceInformation || this.queryParam.deviceInformation.length == 0) {
                this.$message.info('请选择设备信息!');
                return;
            }
            if (this.queryParam.left && this.queryParam.right) {
                let left = (this.queryParam.left).valueOf();
                let right = (this.queryParam.right).valueOf();
                if (right <= left) {
                    this.$message.info('结束时间不能小于开始时间!');
                    return;
                }
            }
            this.pageNo = 1;
            this.jumpPagNum = ''

            this.updata();
        },
        //设备类型变更后获取设备信息
        inequipmentTypeChange(data) {
            let res = {
                type: data
            };
            (function (data, _this) {
                axios.get('/iot/vardata/equip/' + data.type)
                    .then(res => {
                        _this.deviceInformationList = '';
                        if (res.code * 1 == 200) {
                            _this.queryParam.deviceInformation = [];
                            _this.deviceInformationList = res.result;


                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            })(res, this);
        },
        handleSizeChange(val) {
            this.pageNo = val;
            this.updata();

        },
        handleCurrentChange(val) {
            this.pageSize = val;
            this.updata();
        },
        //导出
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let param = {...this.queryParam};
            if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
                param['selections'] = this.selectedRowKeys.join(",")
            }
            console.log("导出参数", param)
            let data = {
                equipmentId: param.deviceInformation,
                left: param.left ? this.moment(param.left).format('YYYY-MM-DD HH:mm') : '',
                right: param.right ? this.moment(param.right).format('YYYY-MM-DD HH:mm') : '',
            }
            window.open(window._CONFIG['domianURL'] + "/anon/statistic/export/hisdata?equipmentId=" + data.equipmentId + '&left=' + data.left + '&right=' + data.right + "&timestamp=" + Number(new Date()) + "&token=" + Vue.ls.get(ACCESS_TOKEN));
        },
        //导出
        downFile(url, parameter) {
            return this.axios.post(url, parameter)
        },
        //重置
        searchReset() {
            this.queryParam = {
                inequipmentType: '',
                deviceInformation: '',
                time: []
            };
            this.dataSource = [];
        }
    }
}