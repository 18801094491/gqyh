import {
    JeecgListMixin
} from '@/mixins/JeecgListMixin'
import {
    getWarnLevel,
    getWarnType,
    getWarnStatusList,
    businessWarnDeal,
    warnConfirm,
    warnClose,
    getUserData,
    getWarnRoleUsers,
    getWarnWorkUsers,
    warnOne,
    getWarnPilicyList,
    warnChangeStatus,
    warnDelete,
    warnGetDetail,
    warnSmstemplate,
    getA03,
    alarmIotEquipmentRuleWarnOne,
    alarmIotEquipmentRuleWarn,
    getTeamDutyTeamList,
    updateAssignWoker,
    getEquipmentList
} from '@/api/operationCenter-t/alarmManagement.js'

export default {
    name: "BusinessWarnList",
    mixins: [JeecgListMixin],
    data() {
        return {
            labelCol: {
                xs: {span: 24},
                sm: {span: 5},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },
            description: '告警管理',
            // 表头
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '事件编号',
                    align: "center",
                    width: 200,
                    dataIndex: 'warnSn'
                },
                {
                    title: '告警名称',
                    align: "center",

                    dataIndex: 'warnName',
                    scopedSlots: {
                        customRender: 'warnName'
                    },
                },
                {
                    title: '告警等级',
                    align: "center",

                    dataIndex: 'warnLevel'
                },
                {
                    title: '告警内容',
                    align: "center",
                    width: 200,
                    dataIndex: 'warnContent',
                    scopedSlots: {
                        customRender: 'warnContent'
                    },
                },
                {
                    title: '所属资产',
                    align: "center",

                    dataIndex: 'equipmentType'
                },
                // {
                //     title: '告警类型',
                //     align: "center",

                //     dataIndex: 'warnType'
                // },
                {
                    title: '规则详情',
                    align: "center",
                    width: 200,
                    dataIndex: 'ruleContent',
                    scopedSlots: {
                        customRender: 'ruleContent'
                    },
                },
                {
                    title: '告警时间',
                    align: "center",
                    width: 200,
                    dataIndex: 'warnTime'
                },
                {
                    title: '告警状态',
                    align: "center",

                    dataIndex: 'warnStatus'
                },

                {
                    title: '操作',
                    width: 230,
                    dataIndex: 'action',
                    align: "center",
                    fixed: 'right',
                    scopedSlots: {
                        customRender: 'action'
                    },
                }
            ],
            url: {
                list: "/business/warn",
                delete: "/business/businessWarn/delete",
                deleteBatch: "/business/businessWarn/deleteBatch",
                exportXlsUrl: "business/businessWarn/exportXls",
                importExcelUrl: "business/businessWarn/importExcel",
            },
            dataSource: [],
            visible2: false,
            alarmHandlingvisible: false,
            alarmHandlingId: '',
            warnSn: '',
            warnName: '',
            warnLevel: '',
            warnType: '',
            duration: '',
            operateTime: '',
            operator: '',
            warnStatus: '',
            warnTime: '',
            warnWay: '',
            warnWayCode: '',
            warnTimeCode: '',
            alarmHandlingVal: '',
            alarmHandlingValList: [],
            warnLevelList: [],
            warnTypeList: [],
            warnStatusList: [],
            warnStatusCode: '',
            closeDescription: '',
            turnOffAlarmvisible: false,
            notificationStrategyVisible: false,
            notificationStrategycolumns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '策略名称',
                    align: "center",
                    dataIndex: 'name'
                },
                {
                    title: '告警等级',
                    align: "center",
                    dataIndex: 'warnLevel'
                },
                {
                    title: '策略内容',
                    align: "left",
                    dataIndex: 'list',
                    scopedSlots: {
                        customRender: 'list'
                    },
                },
                {
                    title: '启用状态',
                    align: "center",
                    dataIndex: 'workStatusCode',
                    scopedSlots: {
                        customRender: 'workStatusCode'
                    },
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {
                        customRender: 'action'
                    },
                }
            ],
            notificationStrategydataSource: [],
            notificationStrategyipagination: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            notificationStrategyloading: false,
            notificationStrategyDefinitionTitle: '',
            notificationStrategyDefinitionvisible: false,
            warnLevel2: '',
            rolesId: [],
            rolesIdList: [],
            usersId: [],
            managerList2: [],
            workTeamsId: [],
            workTeamsIdList: [],
            workStatus: true,
            name: '',
            notificationStrategyDefinitionId: '',
            policyName: '',
            messageTemplateList: [],
            messageTemplate: '',
            inequipmentTypeList: [],
            ruleContent: '',
            equipmentType: '',
            warnContent: '',
            alarmEventConfigurationvisible: false,
            alarmEventConfigurationList: [],
            typeConfigList: [],
            alarmEventConfiguratiId: '',
            workerVisible: false,
            workTeamId: '', // 班组
            teamInformationList: [], // 班组下拉
            equipmentId: [], // 设备信息
            equipmentList: [], // 设备信息下拉
            assinId: '',
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    mounted() {
        getWarnLevel(this);
        getWarnType(this);
        getWarnStatusList(this);
        let data = {
            roleName: ''
        }
        getWarnRoleUsers(data, this);
        let data2 = {
            name: ''
        }
        getWarnWorkUsers(data2, this);
        let data3 = {
            name: ''
        }
        getUserData(data3, this);
        let data4 = {
            name: ''
        }
        warnSmstemplate(data4, this);
        getA03(this);
        if (this.$route.query.id) {
            this.warnTimeCode = this.$route.query.time;
            this.alarmHandlingId = this.$route.query.id;
            this.alarmHandlingvisible = true;
        }
        console.log(this.$route.query.id);
        getTeamDutyTeamList(this); // 获取班组人员下拉
        getEquipmentList('/equipment/optEquipment/dropdown', this); // 获取设备信息下拉
    },
    methods: {
        //详情查看
        details(record) {
            this.visible2 = true;
            this.warnSn = record.warnSn;
            this.warnName = record.warnName;
            this.warnLevel = record.warnLevel;
            this.warnType = record.warnType;
            this.warnTime = record.warnTime;
            this.warnStatus = record.warnStatus;
            this.warnWay = record.warnWay;
            this.operateTime = record.operateTime;
            this.duration = record.duration;
            this.operator = record.operator;
            this.ruleContent = record.ruleContent;
            this.warnContent = record.warnContent;
            this.equipmentType = record.equipmentType;
            this.warnLevel = record.warnLevel;
        },
        //详情关闭
        onClose2() {
            this.visible2 = false;
        },
        //处理按钮弹框
        alarmHandling(record, type) {
            this.alarmHandlingId = record.id;
            // this.description=record.description;
            this.warnStatusCode = record.warnStatusCode;
            this.warnTimeCode = record.warnTime;
            if (type == 'cl') {
                this.alarmHandlingvisible = true;
                this.description = '';

            } else {
                this.turnOffAlarmvisible = true;
            }


        },
        //告警处理弹框确定
        alarmHandlingOk() {
            if (!this.description) {
                this.$message.info('备注不能为空!');
                return;
            }
            let data = {
                description: $.trim(this.description),
                id: this.alarmHandlingId,
                // warnStatusCode:this.warnStatusCode,
                warnTime: this.warnTimeCode
            }
            console.log(data);
            console.log(this.$route.name);
            businessWarnDeal(data, this);
        },
        //告警处理弹框取消
        alarmHandlingCancel() {
            this.alarmHandlingvisible = false;
            this.$router.push({
                name: this.$route.name
            })
        },
        //关闭告警弹框
        turnOffAlarm() {
            this.turnOffAlarmvisible = true;
        },
        //关闭告警确定
        turnOffAlarmOk() {
            let data = {
                description: $.trim(this.closeDescription),
                id: this.alarmHandlingId,
                warnTime: this.warnTimeCode
            }
            warnClose(data, this);
        },
        //关闭告警取消
        turnOffAlarmCancel() {
            this.turnOffAlarmvisible = false;
        },
        //确认按钮效果
        warnConfirm(record) {
            let data = {
                id: record.id
            }
            warnConfirm(data, this);
        },
        //通知策略按钮弹框
        notificationStrategy() {

            this.notificationStrategyVisible = true;
            this.notificationStrategyUpdata();
        },
        //通知策略按钮弹框
        notificationStrategyUpdata() {
            let data = {
                name: $.trim(this.policyName),
                pageNo: this.notificationStrategyipagination.current,
                pageSize: this.notificationStrategyipagination.pageSize
            }
            getWarnPilicyList(data, this);
        },
        //通知策略弹框关闭
        notificationStrategyonClose() {
            this.notificationStrategyVisible = false;
        },
        //通知策略列表改变时
        notificationStrategyhandleTableChange(pagination) {
            this.notificationStrategyipagination.current = pagination.current;
            this.notificationStrategyUpdata();
        },
        //新增/修改通知策略
        notificationStrategyDefinition(type, data) {
            this.name = '';
            this.warnLevel2 = '';
            this.rolesId = [];
            this.usersId = [];
            this.workTeamsId = [];
            this.workStatus = true;
            this.messageTemplate = '';
            if (type == 'add') {
                this.notificationStrategyDefinitionTitle = '新增通知策略';
                this.notificationStrategyDefinitionvisible = true;
            } else {
                this.notificationStrategyDefinitionTitle = '修改通知策略';
                this.notificationStrategyDefinitionId = data.id;
                let res = {
                    id: data.id
                }
                warnGetDetail(res, this);
            }

        },
        //新增/修改通知策略确定
        notificationStrategyDefinitionOk() {
            if (!this.name) {
                this.$message.info('策略名称不能为空!');
                return;
            }
            if (!this.warnLevel2) {
                this.$message.info('告警等级不能为空!');
                return;
            }
            if (!this.messageTemplate) {
                this.$message.info('消息模板不能为空!');
                return;
            }

            let data;
            if (this.notificationStrategyDefinitionTitle.indexOf('新增') != -1) {
                data = {
                    name: $.trim(this.name),
                    warnLevel: this.warnLevel2,
                    rolesId: this.rolesId.length ? this.rolesId.join(',') : '',
                    usersId: this.usersId.length ? this.usersId.join(',') : '',
                    workTeamsId: this.workTeamsId.length ? this.workTeamsId.join(',') : '',
                    workStatus: this.workStatus ? '1' : '0',
                    smsTemplate: this.messageTemplate
                }
            } else {
                data = {
                    name: $.trim(this.name),
                    warnLevel: this.warnLevel2,
                    rolesId: this.rolesId.length ? this.rolesId.join(',') : '',
                    usersId: this.usersId.length ? this.usersId.join(',') : '',
                    workTeamsId: this.workTeamsId.length ? this.workTeamsId.join(',') : '',
                    workStatus: this.workStatus ? '1' : '0',
                    id: this.notificationStrategyDefinitionId,
                    smsTemplate: this.messageTemplate
                }
            }
            warnOne(data, this);

        },
        //新增/修改通知策略取消
        notificationStrategyDefinitionCancel() {
            this.notificationStrategyDefinitionvisible = false;
        },
        //更改启停状态
        workStatusCodeChange(record) {
            console.log(record.workStatusCode)
            let data = {
                id: record.id
            }
            warnChangeStatus(data, this);
        },
        //删除通知策略
        warnDeleteClick(record) {
            let _this = this;
            this.$confirm({
                title: '确定要删除通知策略吗?',
                content: '',
                onOk() {
                    let data = {
                        id: record.id
                    }
                    warnDelete(data, _this);
                },
                onCancel() {
                    console.log('Cancel');
                },
                class: 'test',
            });

        },
        //通知策略查询
        notificationStrategySearchQuery() {
            this.notificationStrategyUpdata();
        },
        alarmEventConfiguration() {
            this.alarmEventConfigurationvisible = true;
            alarmIotEquipmentRuleWarn(this);
        },
        alarmEventConfigurationOk() {
            console.log(this.alarmEventConfigurationList);
            let data = {
                id: this.alarmEventConfiguratiId,
                warnLevelCode: this.alarmEventConfigurationList.join(','),
                type: this.typeConfigList.join(',')
            }
            alarmIotEquipmentRuleWarnOne(data, this);
        },
        alarmEventConfigurationCancel() {
            this.alarmEventConfigurationvisible = false;
        },
        alarmEventConfigurationonChange(data) {
            console.log(this.alarmEventConfigurationList)
        },
        assignWoker(record) { // 分派工单
            this.equipmentId = [];
            this.workerVisible = true;
            this.equipmentId.push(record.optId);
            this.assinId = record.id;
        },
        handleOk() {
            let data = {
                equipmentId: this.equipmentId ? this.equipmentId.join(',') : '',
                jobType: '1', // 0-养护，1-维修，2-检修
                workTeamId: this.workTeamId,
                id: this.assinId
            }
            updateAssignWoker(data, this, '/business/warn/confirm', 'load')
        },
        handleCancel() {
            this.workerVisible = false;
        },
    }
}
//operationCenter/alarmManagement/businessWarnList.vue 页面组件混入了operationCenter/alarmManagement/businessWarnList.js