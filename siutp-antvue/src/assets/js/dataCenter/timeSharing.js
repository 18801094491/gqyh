import { vardataList, getDeviceInformation, getAlarmEquipmentType } from '@/api/dataCenter-t/timeSharing.js'
import moment from 'moment'

export default {
    name: 'timeSharing',
    data() {
        return {
            description: '分时统计',
            queryParam: {
                inequipmentType: '',
                deviceInformation: undefined,
                timeLength: '1',
                share: 'd',
                spaceLength: '1',
                space: 'h',  // 查询间隔
                startTime: new Date(new Date().getTime() - 1 * 60 * 60 * 24 * 1000),
            },
            dateValue: new Date(new Date().getTime() - 1 * 60 * 60 * 24 * 1000),
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
            jumpPagNum: '',
            shareList: [{ value: 'y', title: '年' }, { value: 'M', title: '月' }, { value: 'd', title: '日' }, {
                value: 'h',
                title: '时'
            }, { value: 'm', title: '分' },],
            spaceList: [],
            dataSourceList: [],
            tableName: '',
        }
    },
    mounted() {
        //设备类型获取
        getAlarmEquipmentType(this);
        this.changeSpaceList(this.queryParam.share)
    },
    methods: {
        moment,
        changeDeviceInfo() {
            $('.formHeight').height($('.deviceInfoClass').height() + 15)
        },
        toPage() {
            this.pageNo = this.jumpPagNum * 1;
            this.updata();
        },
        changeSpaceList(val) {
            this.spaceList = [];
            if (val == 'y') {
                this.queryParam.space = 'M';
                this.spaceList.push({ value: 'y', title: '年' }, { value: 'M', title: '月' });
            }
            if (val == 'M') {
                this.queryParam.space = 'd';
                this.spaceList.push(
                    { value: 'M', title: '月' }, { value: 'd', title: '日' }
                )

            }
            if (val == 'd') {
                this.queryParam.space = 'h'
                this.spaceList.push(
                    { value: 'd', title: '日' }, { value: 'h', title: '时' }
                )
            }
            if (val == 'h') {
                this.queryParam.space = 'm';
                this.spaceList.push(
                    { value: 'h', title: '时' }, { value: 'm', title: '分' }
                )
            }
            if (val == 'm') {
                this.queryParam.space = 's';
                this.spaceList.push(
                    { value: 'm', title: '分' }, { value: 's', title: '秒' }
                )
            }
        },
        //数据初始化
        updata() {
            let data = {
                ids: this.queryParam.deviceInformation, // 设备id
                left: this.queryParam.startTime ? this.moment(this.queryParam.startTime).format('YYYY-MM-DD HH:mm:ss') : '', // 开始时间
                gap: this.queryParam.timeLength, // 时间长度
                timeUnit: this.queryParam.share, // 时间长度单位
                particle: this.queryParam.spaceLength, // 查询间隔数
                particleUnit: this.queryParam.space, // 查询间隔单位
            }
            vardataList(data, this);
        },
        //查询
        searchQuery() {
            if (!this.queryParam.inequipmentType) {
                this.$message.info('请选择设备类型!');
                return;
            }
            if (!this.queryParam.deviceInformation) {
                this.$message.info('请选择设备信息!');
                return;
            }
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
                share: 'd',
                spaceLength: '1',
                space: 'h',  // 查询间隔
                startTime: new Date(new Date().getTime() - 1 * 60 * 60 * 24 * 1000),
            };
            this.dataSourceList = [];
            this.changeSpaceList(this.queryParam.share)
        }
    }
}