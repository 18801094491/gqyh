import { getFlowList, getDeviceInformation } from '@/api/dataCenter-t/flowStatistic.js'
import { downFile } from '@/api/manage'
import moment from 'moment'
export default {
    name: 'flowStatistic',
    data() {
        return {
            description: '累计流量统计',
            queryParam: {
                deviceInformation: undefined,
                timeLength: '1',
                share: 'DAYS',
                spaceLength: '1',
                space: 'HOURS',  // 查询间隔
                startTime: new Date(new Date().getTime() - 1 * 60 * 60 * 24 * 1000),
            },
            dateValue: new Date(new Date().getTime() - 1 * 60 * 60 * 24 * 1000),
            columns: [],
            dataSource: [],
            loading: false,
            deviceInformation: '',
            deviceInformationList: [],
            pageNo: 1,
            pageSize: 20,
            total: '',
            sum: 1, // 根据total计算总共多少页
            jumpPagNum: '', // YEARS-年 MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
            shareList: [{ value: 'YEARS', title: '年' }, { value: 'MONTHS', title: '月' }, { value: 'DAYS', title: '日' }, { value: 'HOURS', title: '时' }, { value: 'MINUTES', title: '分' },],
            spaceList: [],
            dataSourceList: [],
            tableName: '',
            tableColumns: [
                {
                    title: '序号',
                    align: 'center',
                    dataIndex: 'serialNo'
                },
                {
                    title: '时间',
                    align: 'center',
                    dataIndex: 'statisticsTime'
                },
                {
                    title: '累计流量[m³]',
                    dataIndex: 'totalFlow',
                    align: 'center',
                },
                {
                    title: '正累计流量[m³]',
                    dataIndex: 'positiveTotalFlow',
                    align: 'center',
                },
                {
                    title: '流量计瞬时流量[m³/h]',
                    dataIndex: 'instantaneousFlow',
                    align: 'center',
                },
                {
                    title: '流量计流速[m/s]',
                    dataIndex: 'flowRate',
                    align: 'center',
                },
                {
                    title: '负累计流量[m³]',
                    dataIndex: 'negativeTotalFlow',
                    align: 'center',
                },
            ],
        }
    },
    mounted() {
        //设备类型信息
        this.inequipmentTypeChange('A03A06'); // 流量计
        this.changeSpaceList(this.queryParam.share)
    },
    methods: {
        moment,
        changeTimeLength(val) {
            var obj = event.target;
            var t = obj.value.charAt(0);
            obj.value = obj.value.replace(/^0|[^\d]/g, "")//只能输入数字、
            if (t == '-') {
                obj.value = '-' + obj.value;
            }
        },
        changeSpaceLength(val) {
            var obj = event.target;
            var t = obj.value.charAt(0);
            obj.value = obj.value.replace(/[^\d]/g, "")//只能输入数字、
            if (t == '-') {
                obj.value = '-' + obj.value;
            }
        },
        changeDeviceInfo() {
            $('.formHeight').height($('.deviceInfoClass').height() + 35)
        },
        toPage() {
            this.pageNo = this.jumpPagNum * 1;
            this.updata();
        },
        changeSpaceList(val) { //MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分 SECONDS-秒
            this.spaceList = [];
            if (val == 'YEARS') {
                this.queryParam.space = 'MONTHS';
                this.spaceList.push({ value: 'YEARS', title: '年' }, { value: 'MONTHS', title: '月' }, { value: 'DAYS', title: '日' });
            }
            if (val == 'MONTHS') {
                this.queryParam.space = 'DAYS';
                this.spaceList.push(
                    { value: 'MONTHS', title: '月' },
                    { value: 'DAYS', title: '日' }
                )
            }
            if (val == 'DAYS') {
                this.queryParam.space = 'HOURS'
                this.spaceList.push(
                    { value: 'DAYS', title: '日' },
                    { value: 'HOURS', title: '时' }
                )
            }
            if (val == 'HOURS') {
                this.queryParam.space = 'MINUTES';
                this.spaceList.push(
                    { value: 'HOURS', title: '时' },
                    { value: 'MINUTES', title: '分' }
                )
            }
            if (val == 'MINUTES') {
                this.queryParam.space = 'SECONDS';
                this.spaceList.push(
                    { value: 'SECONDS', title: '秒' }
                )
            }
        },
        //数据初始化
        updata() {
            let data = {
                equipmentIds: this.queryParam.deviceInformation ? this.queryParam.deviceInformation.join(',') : '', // 设备id
                beginTime: this.queryParam.startTime ? this.moment(this.queryParam.startTime).format('YYYY-MM-DD HH:mm:ss') : '', // 开始时间
                cycle: this.queryParam.timeLength, // 时间长度
                cycleUnit: this.queryParam.share, // 时间长度单位
                interval: this.queryParam.spaceLength, // 查询间隔数
                intervalUnit: this.queryParam.space, // 查询间隔单位
            }
            getFlowList(data, this);
        },
        //查询
        searchQuery() {
            this.jumpPagNum = ''
            this.updata();
        },
        //设备类型变更后获取设备信息
        inequipmentTypeChange(data) {
            let res = {
                type: data
            }
            getDeviceInformation(res, this);
        },
        //重置
        searchReset() {
            this.queryParam = {
                inequipmentType: '',
                deviceInformation: undefined,
                timeLength: '1',
                share: 'DAYS',
                spaceLength: '1',
                space: 'HOURS',  // 查询间隔
                startTime: new Date(new Date().getTime() - 1 * 60 * 60 * 24 * 1000),
            };
            this.dataSourceList = [];
            this.changeSpaceList(this.queryParam.share)
        },
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let param = {
                equipmentIds: this.queryParam.deviceInformation ? this.queryParam.deviceInformation.join(',') : '', // 设备id
                beginTime: this.queryParam.startTime ? this.moment(this.queryParam.startTime).format('YYYY-MM-DD HH:mm:ss') : '', // 开始时间
                cycle: this.queryParam.timeLength, // 时间周期
                cycleUnit: this.queryParam.share, // 时间周期单位
                interval: this.queryParam.spaceLength, // 查询间隔数
                intervalUnit: this.queryParam.space, // 查询间隔单位
            }
            downFile('/iot/cumulativeStatistics/exprot', param).then((data) => {
                if (!data) {
                    this.$message.warning("文件下载失败")
                    return
                }
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
                } else {
                    let url = window.URL.createObjectURL(new Blob([data]))
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url
                    link.setAttribute('download', fileName + '.xls')
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link); //下载完成移除元素
                    window.URL.revokeObjectURL(url); //释放掉blob对象
                }
            })
        }
    }
}
//flowStatistic页面组件混入的flowStatistic.js