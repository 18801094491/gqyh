import {
    workTeamDutyTeamMyDutyData, getTeamDutyQueryShifts, teamDutyOne
} from '@/api/operationCenter-t/personalScheduling.js'
import {downFile} from '@/api/manage'

export default {
    name: 'workforceManagement',
    data() {
        return {
            description: '我的排班',
            name: '班组排班信息',
            form: this.$form.createForm(this),
            labelCol: {
                xs: {span: 24},
                sm: {span: 5},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },

            listData: [
                {
                    list: [
                        {
                            teamName: '班组名称1',
                            shiftsName: '班次名称1',
                        },
                        {
                            teamName: '班组名称1',
                            shiftsName: '班次名称1',
                        },
                        {
                            teamName: '班组名称1',
                            shiftsName: '班次名称1',
                        },
                    ],
                    dutyDay: 8
                },
                {
                    list: [
                        {
                            teamName: '班组名称1',
                            shiftsName: '班次名称1',
                        },
                        {
                            teamName: '班组名称1',
                            shiftsName: '班次名称1',
                        },
                        {
                            teamName: '班组名称1',
                            shiftsName: '班次名称1',
                        },
                    ],
                    dutyDay: 15
                },
            ],
            selectDate: ''
        }
    },
    mounted() {

        this.updata(this.moment(new Date()).format('YYYY-MM'), '1');
    },
    methods: {
        //获取列表数据
        updata(dutyDay) {
            let data = {
                dutyDay: dutyDay
            }
            workTeamDutyTeamMyDutyData(data, this);

        },
        //日历日期改变
        onPanelChange(date, mode) {
            console.log(this.moment(date).format('YYYY-MM'));
            this.selectDate = this.moment(date).format('YYYY-MM');
            this.updata(this.selectDate, '1');
        },
        //日历日期改变
        onSelect(date, mode) {
            console.log(this.moment(date).format('YYYY-MM-DD'));

            this.updata(this.moment(date).format('YYYY-MM'), '1');

        },
        //日历日期改变
        dateChange(date, mode) {
            console.log(date);
        },
        //新增班组排班信息
        addTeamScheduling() {
            this.visible = true;
            this.teamInformation = '';
            this.dateString = [];
            this.shiftsNameId = '';
            this.shiftsName = '';
        },
        //班组排班信息新增确定
        handleOk() {
            if (!this.teamInformation) {
                this.$message.info('班组信息不能为空!');
                return;
            }
            if (!this.shiftsNameId) {
                this.$message.info('班次名称不能为空!');
                return;
            }
            if (!this.dateString.length) {
                this.$message.info('排班日期不能为空!');
                return;
            }
            let startTime = this.moment(this.dateString[0]).format('YYYY-MM-DD');
            let endTime = this.moment(this.dateString[1]).format('YYYY-MM-DD');
            let data = {
                dateString: new Date(startTime).getTime() == new Date(endTime).getTime() ? startTime : startTime + ',' + endTime,
                shfitsId: this.shiftsNameId,
                teamId: this.teamInformation
            }
            console.log(data)
            teamDutyOne(data, this);
        },
        //班组排班信息新增取消
        handleCancel() {
            this.visible = false;
        },
        //班组信息选择获取班次信息
        teamInformationChange(data) {
            let res = {
                teamId: data
            }
            getTeamDutyQueryShifts(res, this);
        },
        //导出
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let res = {
                dutyDay: this.selectDate ? this.selectDate : this.moment(new Date()).format('YYYY-MM')
            }
            downFile('/work/teamDuty/myexport', res).then((data) => {
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
        },
    },
}
//operationCenter/operationToolAdmin/optApplyVerifyList.vue 页面组件混入了operationCenter/personalScheduling.js