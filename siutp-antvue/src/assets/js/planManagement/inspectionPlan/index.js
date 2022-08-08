import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {
    getDropAreasData,
    onInspPlanQueryChange,
    getInspRouteData,
    onInspPlanChange,
    onDelInspPlanChange,
} from '@/api/planManagement/inspectionPlan/index.js'

export default {
    name: "inspectionPlan",
    mixins: [JeecgListMixin],
    data() {
        return {
            columns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },{
                title: '计划名称',
                align: "center",
                dataIndex: 'name'
            },{
                title: '计划编号',
                align: "center",
                dataIndex: 'code'
            },{
                title: '区域',
                align: "center",
                dataIndex: 'areaName'
            },{
                title: '责任班组',
                align: "center",
                dataIndex: 'teamName'
            },{
                title: '巡查线路',
                align: "center",
                dataIndex: 'routeName'
            },{
                title: '时间',
                align: "center",
                dataIndex: 'showDate'
            },{
                title: '计划状态',
                align: "center",
                dataIndex: 'statusDes'
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: {customRender: 'action'},
            }],
            url: {
                list: "/inspection/inspPlan/list",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            changeId: '', // 修改id
            warnStatusList: [], // 状态下拉
            problemName: '', // 问题名称
            problemType: '', // 问题类型
            equipmentId: '', // 问题设备信息
            problemDescription: '', // 问题描述
            problemTypeList: [], // 问题类型下拉
            equipmentList:[],
            workTeamId: '',
            assignId: '', // 列表行id
            planCycle: {
                '1': [
                    '周一','周二','周三',
                    '周四','周五','周六',
                    '周日'
                ],
                '2': [
                    '1号','2号','3号',
                    '4号','5号','6号',
                    '7号','8号','9号',
                    '10号','11号','12号',
                    '13号','14号','15号',
                    '16号','17号','18号',
                    '19号','20号','21号',
                    '22号','23号','24号',
                    '25号','26号','27号',
                    '28号','29号','30号',
                    '31号',
                ]
            },
            planCycleList: [], //被选日期
            planCycleType: '0',
            patrolArea: '', //巡检区域
            patrolLine: '', //巡线
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            name: '',
            code: '',
            areaList: [], //巡检区域
            routeList: [], //巡检线路
            queryRouteList: [], //巡检线路 - 页面搜索
            areaId: '',
            routeId: '',
            teamId: '',
            teamLeaderId: '',
            startDate: '',
            overDate: '',
            workTeamList: [], //班组 - 依赖于开始结束时间
            memberList: [], //班组管理人员
            frequencyType: '0', //频次 - 日周月年
            frequencyDesc: '', //频次详情字符串
            frequencyDescList: [],
            frequencyDescObj: {
                '0': [],
                '1': [],
                '2': [],
                '3': [],
            },//频次详情列表
            radioValue: '0',
            spinning: true,
        }
    },
    mounted() {
        getDropAreasData('/inspection/inspArea/all', 'areaList', this); //区域下拉列表
    },
    methods: {
        handleAdd(type, data) {
            this.visible = true;
            if (type == 'add') {
                this.spinning = false;
                this.drawerTitle = '新建巡检计划';
            } else {
                this.drawerTitle = '修改巡检计划';
                this.changeId = data.id;
                onInspPlanQueryChange(this.changeId, this, data => {
                    this.name = data.name; //计划名称
                    this.code = data.code; //计划编号
                    this.areaId = data.areaId; //区域id
                    this.areaName = data.areaName; //区域名称
                    this.routeId = data.routeId; //线路id
                    this.routeName = data.routeName; //线路名称
                    this.startDate = data.startDate; //开始时间
                    this.overDate = data.overDate; //结束事件
                    this.teamId = data.teamId; //班组id
                    this.teamName = data.teamName; //班组名称
                    this.teamLeaderId = data.teamLeaderId; //管理人员id
                    this.teamLeaderName = data.teamLeaderName; //管理人员名称
                    this.frequencyType = data.frequencyType; //频次 - 日周月年
                    this.frequencyDesc = data.frequencyDesc //频次详情
                    
                    this.radioValue = data.frequencyType;
                    if (data.frequencyDesc) {
                        let frequencyDesc = data.frequencyDesc.split(',');
                        this.planCycleList = this.planCycle[this.frequencyType]
                        this.planCycleList.forEach((a, i) => {
                            frequencyDesc.forEach(b => {
                                if (i == b-1) {
                                    this.frequencyDescList.push(a)
                                }
                            })
                        })
                        this.frequencyDesc = frequencyDesc
                    }
                    this.onSwitchAreaChange(this.areaId);
                    this.onSetOverDateChange({}, this.overDate, () => {
                        setTimeout(() => { this.spinning = false }, 50);
                        this.onSetWorkTeamChange(this.teamId);
                    });
                })
            }
        },

        onFormValidationChange(data, callBack) { //表单信息验证
            let message = '';
            if (!data.name) {
                message = '请输入计划名称！'
            } else if (data.name.length > 32) {
                message = '计划名称长度不能大于32，目前长度' + data.name.length
            } else if (!data.code) {
                message = '请输入计划编号！'
            } else if (data.code.length > 20) {
                message = '计划名称长度不能大于20，目前长度' + data.name.length
            } else if (!data.areaId) {
                message = '请选择巡检区域！'
            } else if (!data.routeId) {
                message = '请选择巡检线路！'
            } else if (!data.startDate) {
                message = '请选择开始时间！'
            } else if (!data.overDate) {
                message = '请选择结束时间！'
            } else if (new Date(data.overDate).getTime() < new Date(data.startDate).getTime()) {
                message = '结束时间不能小于开始时间！'
            } else if (!data.teamId) {
                message = '请选择班组！'
            } else if (!data.teamLeaderId) {
                message = '请选择管理员！'
            } else if (data.frequencyType !== '0' && data.frequencyType !== '3' && !data.frequencyDesc.length) {
                message = '请选择计划的执行日期！';
            }
            if (message) {
                this.$message.info(message);
                return
            }
            callBack()
        },

        addSubmit() { //提交新增/修改信息
            let data;
            if (this.drawerTitle.indexOf('新建') != -1) {
                data = {
                    name: this.name, //计划名称
                    code: this.code, //计划编号
                    areaId: this.areaId, //区域id
                    routeId: this.routeId, //线路id
                    startDate: this.startDate, //开始时间
                    overDate: this.overDate, //结束事件
                    teamId: this.teamId, //班组id
                    teamLeaderId: this.teamLeaderId, //管理人员id
                    frequencyType: this.frequencyType, //频次 - 日周月年
                    frequencyDesc: this.frequencyDesc.toString() //频次详情
                }
                this.onFormValidationChange(data, () => {
                    onInspPlanChange('add', data, this);
                })
            } else {
                data = {
                    id: this.changeId, //当前修改计划ID
                    name: this.name, //计划名称
                    code: this.code, //计划编号
                    areaId: this.areaId, //区域id
                    routeId: this.routeId, //线路id
                    startDate: this.startDate, //开始时间
                    overDate: this.overDate, //结束事件
                    teamId: this.teamId, //班组id
                    teamLeaderId: this.teamLeaderId, //管理人员id
                    frequencyType: this.frequencyType, //频次 - 日周月年
                    frequencyDesc: this.frequencyDesc.toString() //频次详情
                }
                this.onFormValidationChange(data, () => {
                    onInspPlanChange('edit', data, this);
                })
            }
        },

        searchReset() { //重置
            this.queryRouteList = [];
            this.queryParam = {}
            this.loadData(1);
        },

        //新增/修改重置页面内属性
        reset() {
            this.spinning = true;
            this.name = '';
            this.code = '';
            this.areaId = '';
            this.routeId = '';
            this.startDate = '';
            this.overDate = '';
            this.teamId = '';
            this.routeList = '';
            this.teamLeaderId = '';
            this.workTeamList = [];
            this.memberList = [];
            this.frequencyType = '0';
            this.frequencyDesc = '';
            this.radioValue = '0';
            this.planCycleType = '0';
            this.planCycleList = [];
            this.frequencyDescList = [];
        },

        onCloseChange() {
            this.visible = false;
            this.reset();
        },

        onDeleteChange ( id ) { //删除区域
            onDelInspPlanChange(id, this);
        },

        onSwitchAreaChange(areaId, type, list){ //获取选中区域下的线路
            if (type === 'set') { //手动切换时清空
                this.routeList = '';
                this.routeId = '';
            }
            getInspRouteData(areaId, this, list);
        },

        onSetStartDateChange(data, dateString) { //选择开始时间
            this.startDate = dateString;
            if (this.startDate && this.overDate) {
                getDropAreasData(`/inspection/inspPlan/team/list?startTime=${this.startDate}&overTime=${this.overDate}`, 'workTeamList', this); //班组下拉列表
            }
        },
        onSetOverDateChange(data, dateString, callBack) { //选择结束时间
            this.overDate = dateString;
            if (this.startDate && this.overDate) {
                getDropAreasData(`/inspection/inspPlan/team/list?startTime=${this.startDate}&overTime=${this.overDate}`, 'workTeamList', this, () => {
                    callBack && callBack();
                }); //班组下拉列表
            }
        },

        onSetWorkTeamChange(teamId, type) {
            this.teamId = teamId;
            this.memberList = [];
            if (type === 'change') {
                this.teamLeaderId = '';
            }
            this.workTeamList.forEach(item => {
                if (item.id === teamId) {
                    this.memberList = item.memberList;
                }
            })
        },

        bindPlanCycleChange(e) {
            let value = e.target.value
            this.planCycleList = this.planCycle[value] //被选数组
            this.frequencyDescList = [] //已选数组
            this.frequencyDesc = ''
            this.frequencyType = value
        },
        onSelectDateChange(checkedValues) {
            let frequencyDesc = '';
            this.planCycleList.forEach((a, i) => {
                checkedValues.forEach((b, j) => {
                    if (a == b) {
                        frequencyDesc += i+1+','
                    }
                })
            })
            this.frequencyDesc = frequencyDesc.substr(0, frequencyDesc.length-1);
            return
            let planCycleType = this.planCycleType;
            this.frequencyDescList[planCycleType] = checkedValues;
            this.frequencyType = planCycleType === 'week' ? 1 :
                planCycleType === 'month' ? 2 :
                planCycleType === 'year' ? 3 : '';
        },
    }
}