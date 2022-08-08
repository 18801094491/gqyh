import {
    iotEquipmentUpdateCycleByCate,
    alarmModleOne,
    alarmModle,
    removealarmModleId,
    getVarRule,
    getContrastVal,
    getAndOr,
    getWarnLevel,
    getAlarmContent,
    alarmOne,
    getAlarmList,
    deleteRule,
    getIotControList,
    iotControOne,
    reomeveIotContro,
    sysdictRule_type,
    equipmentConfigCat,
    equipmentConfigOne,
    getEquipmentConfigOne,
    getGisVoList2,
    iotEquipmentBindBindData,
    iotEquipmentBindUnbindData,

    iotEquipmentSync,
    synchronousCache,
    equipmentAlarmStartOrStop,
    getAgencyStatus,
    equipmentAlarmIdDetails,
    equipmentAlarmUpdateValue,
    equipmentAlarmUpdateAlarm,

    getAelist,
    setAeInfo,
    setUnbindEquipment,
    getbindEquipmentList,
    setEquipmentOne,
    getloadTreeChildrenList,
    setiotEquipmentVariableOne,
    getEquipmentVariableList,
    deliotEquipmentVariableOne,
    getEquipmentType
} from '@/api/collection-t/equipment.js'

// import {getEquipmentType} from '@/api/dict.js'
import searchReset from '@/mixins/searchReset.js'

import '@/assets/less/collection/equipment.less'

export default {
    name: 'acquisitionEquipmentManagement',
    mixins: [searchReset],
    components: {},
    data() {
        return {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            form: this.$form.createForm(this),
            queryParam: {},
            columns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: '5%',
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },
            {
                title: '设备编码',
                align: "center",
                width: '15%',
                dataIndex: 'iotSn',

            },
            {
                title: '设备名称',
                align: "center",
                width: '10%',
                dataIndex: 'iotName',
            },
            {
                title: '采集周期(秒)',
                align: "center",
                width: '10%',
                dataIndex: 'iotCycle',

            },
            {
                title: '设备类型',
                align: "center",
                width: '10%',
                dataIndex: 'iotCategoryName',

            },
            {
                title: '设备状态',
                align: "center",
                width: '10%',
                dataIndex: 'iotStatus',
                customRender: function (text, record, index) {
                    if (text == 0) {
                        return '停用'
                    } else if (text == 1) {
                        return '启用'
                    } else {
                        return text
                    }
                },


            },
            {
                title: '操作',
                align: "center",
                width: '35%',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            },
            ],
            dataSource: [], //表格数据源
            loading: true,
            /* 分页参数 */
            ipagination: {
                current: 1,
                pageSize: 10,
                // pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            bindId: '', //绑定变量当前行数据ID存储

            isDebugConsole: true,
            aeNumber: '',
            aeName: '',
            aeInfo: {
                iotCycle: '1',
                iotStatus: true, //设备状态1-启用，2停用
                iotCategory: '',
                id: '',
                iotName: '',
                iotSn: '',
                pid: '1206838473407348737',
                iotCycleTypeValue: '1',
            },
            iotCategoryList: [],
            iotCategoryListALL: [],
            aeTitle: '新增采集设备信息', //新增/修改名称
            bindStatus: 1, //设备绑定的状态
            visible: false, //新增/修改弹框值
            /////////////////////////////////////////
            bindEquipmentColumns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: '5%',
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },
            {
                title: '设备编码',
                align: "center",
                width: '10%',
                dataIndex: 'iotSn',

            },
            {
                title: '设备名称',
                align: "center",
                width: '10%',
                dataIndex: 'iotName',
            },
            {
                title: '采集周期(秒)',
                align: "center",
                width: '15%',
                dataIndex: 'iotCycle',

            },
            {
                title: '设备类型',
                align: "center",
                width: '10%',
                dataIndex: 'iotCategory',

            },
            {
                title: '操作',
                align: "center",
                width: '20%',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            },


            ],
            bindEquipmentDataSource: [],
            bindEquipmentPagination: {
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
            bindEquipmentloading: false,
            bindEquipmentVisible: false,
            bindEquipmentName: '', //绑定设备搜索名称信息
            bindEquipmentId: '', //绑定设备 Id
            /////////////////////////////////////////
            varColumns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: '10%',
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },
            {
                title: '变量名称',
                align: "center",
                width: '40%',
                dataIndex: 'variableName',

            },
            {
                title: '变量示意',
                align: "center",
                width: '30%',
                dataIndex: 'variableTitle',

            },
            {
                title: '操作',
                align: "center",
                width: '20%',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtnVar'
                },
            },
            ], //表格头部属性信息
            varDataSource: [],
            varPagination: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            varLoading: true,
            varVisible: false,
            /////////////////////////////////////////
            varDetailsColumns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: '10%',
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },
            {
                title: '变量名称',
                align: "center",
                dataIndex: 'variableName',
                width: '40%',
            },
            {
                title: '变量示意',
                align: "center",
                width: '30%',
                dataIndex: 'variableTitle',

            },
            {
                title: '操作',
                align: "center",
                dataIndex: 'maintain',
                width: '20%',
                scopedSlots: {
                    customRender: 'maintainBtnvarDetails'
                },
            },
            ],
            varDetailsDataSource: [],
            varDetailsPagination: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0,
                hideOnSinglePage: false,
            },
            varDetailsLoading: true,
            varDetailsVisible: false,
            varDetailsId: '',
            varQuseryInfo: {
                varQuseryName: '',
                varQuseryStatus: '',
                varQuseryName2: '',
            },
            /////////////////////////////////////////
            bindEquipmentCycleVisible: false,

            /////////////////////////////////////////

            alarmModelColumns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: '10%',
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '告警模板标题',
                    align: "center",
                    width: '15%',
                    dataIndex: 'alarmTitle',

                },
                {
                    title: '告警模板内容',
                    align: "center",
                    width: '40%',
                    dataIndex: 'alarmValue',

                },
                {
                    title: '创建时间',
                    align: "center",
                    width: '15%',
                    dataIndex: 'createTime',

                },
                {
                    title: '操作',
                    align: "center",
                    width: '20%',
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtnvarDetails'
                    },
                },
            ], //表格头部属性信息
            alarmModelDataSource: [],
            alarmModelPagination: {
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
            alarmModelLoading: false,
            alarmModelVisible: false,
            alarmModelInfo: {
                Title: '',
                Id: '',
                Value: '',
                CreateTime: '',
                Icon: "plus",

            },
            ruleVisible: false,
            ruleList: [
                {
                    andVal: '',
                    varVal: '',
                    symbolVal: '',
                    ruleVal: '',

                }
            ],
            templateVisible: false,
            templatetitle: '',
            templateTitleVal: '',
            templateVal: '',
            iotCategory: '',
            iotCycleNumber: '',
            iotCycleType: '',
            rexValArr: ['{{放置位置}}', '{{设备类型}}', '{{设备名称}}', '{{设备编号}}', '{{变量名称}}', '{{变量值}}', '{{日期时间}}', '{{阈值}}', '{{标段}}'],
            templateChangeId: '',
            alarmRulesVisible: false,
            alarmRulesColumns: [
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
                    title: '规则名称',
                    align: "center",
                    width: 100,
                    dataIndex: 'alarmName',

                },
                {
                    title: '规则内容',
                    align: "center",
                    width: 200,
                    dataIndex: 'alarmRule',
                    scopedSlots: {
                        customRender: 'alarmRule'
                    },

                },
                {
                    title: '状态',
                    align: "center",
                    width: 60,
                    dataIndex: 'alarmRuleTypeValue',

                },
                {
                    title: '告警分级',
                    align: "center",
                    width: 70,
                    dataIndex: 'alarmLevelName',

                },
                {
                    title: '告警内容模板',
                    align: "center",
                    width: 200,
                    dataIndex: 'alarmModleName',
                    scopedSlots: {
                        customRender: 'alarmModleName'
                    },
                },
                // {
                //     title: '质量戳',
                //     align: "center",
                //     width: '10%',
                //     dataIndex: 'isMassName',

                // },
                {
                    title: '状态',
                    align: "center",
                    width: 100,
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtnvarDetails'
                    },
                },
                {
                    title: '操作',
                    align: "center",
                    width: 200,
                    dataIndex: 'maintain2',
                    scopedSlots: {
                        customRender: 'maintain2'
                    },
                },
            ],
            alarmRulesSource: [],
            alarmRulesPagination: {
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
            alarmRulesLoading: false,
            binAlarmRulesId: '',
            varList: [],
            ContrastValList: [],
            andOrList: [],
            alarmContentList: [],
            warnLevelList: [],
            isMass: true,
            alarmLevel: '',
            alarmModleId: '',
            alarmName2: '',
            reverseControlVisible: false,
            reverseControlColumns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: '10%',
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '控制名称',
                    align: "center",
                    width: '20%',
                    dataIndex: 'controName',

                },
                {
                    title: '变量名称',
                    align: "center",
                    width: '20%',
                    dataIndex: 'variableName',

                },
                {
                    title: '变量值',
                    align: "center",
                    width: '20%',
                    dataIndex: 'variableValue',

                },
                {
                    title: '操作',
                    align: "center",
                    width: '30%',
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtnvarDetails'
                    },

                },
            ],
            reverseControlSource: [],
            reverseControlPagination: {
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
            reverseControlLoading: false,
            controlTitle: '',
            controlVisible: false,
            reverseControlId: '',
            variableValue: '',
            variableId: '',
            controName: '',
            equipmentId: '',
            controlId: '',
            varType: '',
            variableValue2: '',
            rule_type: '',
            rule_typeList: [],
            rule_typeVal: true,
            toConfigureVisible: false,
            toConfigurebOk: false,
            toConfigurebId: '',
            alarm: false,
            alarmModel: '',
            checkQuality: false,
            typeCode: '',
            alarmLevel2: '',
            giscolumns2: [{
                title: '资产名称',
                width: '33%',
                align: "center",
                dataIndex: 'name',

            },

            {
                title: '资产类型',
                align: "center",
                width: '33%',
                dataIndex: 'type',

            },
            {
                title: '操作',
                align: "center",
                width: '34%',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn2'
                },
            },


            ], //表格头部属性信息
            gisdataSource2: [], //表格数据源
            gisloading2: false,
            /* 分页参数 */
            gisipagination2: {
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
            bindGisVisible: false,
            gismodelName: '',
            gisbindId: '',
            inequipmentTypeList: [],
            alarm2: true,
            selectedRowKeys: [],
            selectionRows: [],
            stateRuleTitle: '',
            ruleSubmitOk: true,
            workingStatusList: [],
            workingStatus: '',
            bindVarTitle: '',
            rulesId: '',
            rulesOk: true,
            rulesOk2: true,
            rulesEquipmentId: ''
        }
    },
    mounted() {
        this.GetAeList();
        this.GetCategoryList();
        sysdictRule_type(this);
        getEquipmentType(this);
        getAgencyStatus(this);
    },
    methods: {
        updata() {
            this.GetAeList();
        },
        //获取数据
        GetAeList() {
            console.log("GetAeList")
            let queryParam = {
                iotSn: $.trim(this.queryParam.iotSn),
                iotName: $.trim(this.queryParam.iotName),
                iotCategory: $.trim(this.queryParam.iotCategory),
                pageSize: this.ipagination.pageSize, //当前页显示一页多少条
                pageNo: this.ipagination.current //当前页
            }
            console.log(queryParam)

            getAelist(queryParam)
                .then((res) => {
                    this.loading = false;
                    console.log(res)
                    if (res.code == 200) {
                        this.dataSource = [];
                        let list = res.result.records;
                        list.map(index => {
                            this.dataSource.push({
                                id: index.id,
                                iotCategory: index.iotCategory,
                                iotCycle: index.iotCycle,
                                iotName: index.iotName,
                                iotSn: index.iotSn,
                                iotStatus: index.iotStatus,
                                iotCategoryName: index.iotCategoryName,
                                bindStatus: index.bindStatus,
                                optCategoryName: index.optCategoryName
                            })
                        })
                        this.ipagination.current = res.result.current;
                        this.ipagination.total = res.result.total;
                    } else {
                        this.$message.info(res.message);
                        this.dataSource = [];
                        this.ipagination.current = 0;
                        this.ipagination.total = 1;
                    }
                })
                .catch((err) => {
                    this.$message.error(err)
                })
        },
        GetCategoryList() {
            console.log("GetCategoryList")
            let params = {
                pid: this.aeInfo.pid,
            };
            let _this = this;
            getloadTreeChildrenList(params)
                .then((res) => {
                    console.log(res)
                    let list = res.result;
                    _this.iotCategoryList = list;

                    for (let i = 0; i < list.length; i++) {
                        _this.iotCategoryListALL.push({
                            code: list[i].code,
                            title: list[i].title
                        })
                    }


                })
                .catch((err) => {
                    this.$message.error(err)
                })

        },
        //查询设备信息
        searchQuery() {
            this.ipagination.current = 1; //查询后修改当前页为第一页
            this.GetAeList();
        },
        //新增/修改弹框
        handleAdd(type, data) {
            console.log("handleAdd")
            console.log(type)
            console.log(data)
            //
            this.aeInfo.id = '';
            this.aeInfo.iotCycle = '1';
            this.aeInfo.iotStatus = true;
            this.aeInfo.iotName = '';
            this.aeInfo.iotSn = '';
            this.aeInfo.iotCategory = '';

            // this.GetCategoryList();
            if (type == 'add') {
                this.aeTitle = '新增采集设备信息';
                this.visible = true;
            } else {
                this.aeTitle = '修改采集设备信息';
                this.aeInfo.id = data.id;
                this.aeInfo.iotName = data.iotName;
                this.aeInfo.iotSn = data.iotSn;
                this.aeInfo.iotStatus = data.iotStatus == 1 ? true : false;

                this.aeInfo.iotCategory = data.iotCategory;
                this.form.setFieldsValue({ iotCategory: data.iotCategoryName });


                if (data.iotCycle > 1 && data.iotCycle <= 60) {
                    console.log("iotCategoryTime 秒")

                    this.aeInfo.iotCycle = data.iotCycle / 1;

                    this.form.setFieldsValue({ iotCategoryTime: '秒' });
                    this.aeInfo.iotCycleTypeValue = 1;
                } else if (data.iotCycle > 60 && data.iotCycle <= 3600) {
                    console.log("iotCategoryTime 分")

                    this.aeInfo.iotCycle = data.iotCycle / 60;
                    this.form.setFieldsValue({ iotCategoryTime: '分' });
                    this.aeInfo.iotCycleTypeValue = 60;
                } else if (data.iotCycle > 3600 && data.iotCycle <= 86400) {
                    console.log("iotCategoryTime 时")

                    this.aeInfo.iotCycle = data.iotCycle / 3600;
                    this.form.setFieldsValue({ iotCategoryTime: '时' });
                    this.aeInfo.iotCycleTypeValue = 3600;
                } else if (data.iotCycle > 86400) {
                    console.log("iotCategoryTime 天")

                    this.aeInfo.iotCycle = data.iotCycle / 86400;
                    this.form.setFieldsValue({ iotCategoryTime: '日' });
                    this.aeInfo.iotCycleTypeValue = 86400;
                }

                // this.form.setFieldsValue({ iotCategoryTime : 60});


                this.visible = true;


            }

        },
        handleNumberChange(value) {

            console.log(`handleNumberChange value: ${value}`);
            var typeValue = this.aeInfo.iotCycleTypeValue;
            let Sum = value * typeValue;

            console.log(`Sum ${Sum}`);
        },
        handleSelectChange(value) {
            console.log(value);
            this.aeInfo.iotCategory = value;
        },

        //关闭新增/修改弹框
        onClose() {
            this.visible = false;
        },
        //提交新增/修改信息
        addSubmit() {
            console.log("提交新增/修改信息")
            let data;

            // 设备状态1-启用，2停用
            if (this.aeTitle.indexOf('新增') != -1) {
                let Sum = this.aeInfo.iotCycle * this.aeInfo.iotCycleTypeValue;
                data = {
                    id: $.trim(this.aeInfo.id),
                    iotCategory: $.trim(this.aeInfo.iotCategory),
                    iotCycle: Sum,
                    iotName: $.trim(this.aeInfo.iotName),
                    iotSn: $.trim(this.aeInfo.iotSn),
                    iotStatus: this.aeInfo.iotStatus ? '1' : '0',
                }
                console.log(data)
            } else {

                let Sum = this.aeInfo.iotCycle * this.aeInfo.iotCycleTypeValue;
                data = {
                    id: $.trim(this.aeInfo.id),
                    iotCategory: $.trim(this.aeInfo.iotCategory),
                    iotCycle: Sum,
                    iotName: $.trim(this.aeInfo.iotName),
                    iotSn: $.trim(this.aeInfo.iotSn),
                    iotStatus: this.aeInfo.iotStatus ? '1' : '0',
                }
                console.log(data)
            }
            if (!this.aeInfo.iotName) {
                this.$message.info('采集设备名称不能为空!');
                return;
            }
            if (!this.aeInfo.iotSn) {
                this.$message.info('采集设备编码不能为空!');
                return;
            }
            if (!this.aeInfo.iotCategory) {
                this.$message.info('请选择设备类型!');
                return;
            }
            setAeInfo(data)
                .then((res) => {
                    console.log(res)
                    let _this = this;
                    if (res.code * 1 == 200) {
                        _this.$message.info(res.message);

                        this.visible = false;
                        _this.updata();
                    } else {
                        _this.$message.info(res.message);
                    }
                })
                .catch((err) => {
                    this.$message.error(err)
                })

        },

        //表格属性改变
        handleTableChange(pagination) {
            this.ipagination.current = pagination.current;
            this.GetAeList();
        },


        //////////////////////////////////////////////////////////////
        //【变量详情】 设备列表页面操作按钮
        varDetailsSearchQuery() {
            console.log("varDetailsSearchQuery")
            this.varPagination.current = 1;
            this.variableDetailsList();
        },
        binVarDetailsClick(data) {
            console.log("binVarDetailsClick")
            this.varDetailsVisible = true;
            this.varDetailsId = data.id;
            this.varDetailsPagination.current = 1;
            this.variableDetailsList();
        },
        variableDetailsList() {
            console.log("variableDetailsList")
            let params = {
                pageNo: this.varDetailsPagination.current,
                pageSize: this.varDetailsPagination.pageSize,
                iotId: this.bindId,
                status: '1', //绑定状态1为绑定，0为未绑定
                variableName: this.varQuseryInfo.varQuseryName2,
            }
            console.log(params);

            let _this = this;
            getEquipmentVariableList(params)
                .then(res => {
                    console.log(res);
                    _this.varDetailsLoading = false;
                    if (res.code * 1 == 200) {
                        _this.varDetailsDataSource = [];
                        let list = res.result.records;
                        list.map(index => {
                            _this.varDetailsDataSource.push({
                                id: index.id,
                                iotId: index.iotId,
                                iotName: index.iotName,
                                status: index.status, //绑定状态1为绑定，0为未绑定
                                variableName: index.variableName,
                                variableTitle: index.variableTitle
                            })
                        })
                        _this.varDetailsPagination.current = res.result.current;
                        _this.varDetailsPagination.total = res.result.total;

                    } else {
                        _this.$message.info(res.message);
                        _this.varDetailsDataSource = [];
                        _this.varDetailsPagination.current = 0;
                        _this.varDetailsPagination.total = 1;
                    }
                })
                .catch((err) => {
                    this.$message.error(err)
                })

        },
        varDetailsOnOk() {
            console.log("varDetailsOnOk")
            this.varDetailsVisible = false;
        },
        varDetailsOnCancel() {
            console.log("varDetailsOnCancel")
            this.varDetailsVisible = false;
        },
        varDetailsTableChange(pagination) {
            console.log("varDetailsTableChange")
            console.log(pagination)
            this.varDetailsPagination.current = pagination.current;
            this.variableDetailsList();
        },
        varDetailsUnbind(data) {
            console.log("varDetailsUnbind")
            console.log(data)
            let _this = this;
            this.$confirm({
                title: '确定要绑定吗?',
                content: '',
                onOk() {
                    let params = {
                        id: data.id
                    }
                    // debugger
                    console.log(params)
                    deliotEquipmentVariableOne(params)
                        .then(res => {
                            console.log(res)
                            if (res.code * 1 == 200) {
                                _this.$message.info(res.message);
                                _this.bindVarInitLoad();

                            } else {
                                _this.$message.info(res.message);
                            }

                        })
                        .catch((err) => {
                            this.$message.error(err)
                        })

                },
                onCancel() {
                    _this.$message.info("取消操作")
                },
            });

        },

        //////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////
        // 【绑定变量】  设备列表页面操作按钮
        bindVarClick(res) {
            this.varVisible = true;
            this.bindId = res.id;
            this.varQuseryInfo.varQuseryStatus = '';
            this.varQuseryInfo.varQuseryName = '';
            this.varQuseryInfo.varQuseryName2 = '';
            console.log(res)
            this.bindVarTitle = '绑定变量:' + res.iotCategoryName + res.iotSn;
            this.bindVarInitLoad();

        },
        bindVarInitLoad() {
            console.log("bindVarInitLoad")

            // this.varQuseryInfo.varQuseryName = '';
            // this.varQuseryInfo.varQuseryName2 = '';
            // this.varQuseryInfo.varQuseryStatus = '2';
            this.varPagination.current = 1;
            this.bindVarList();

            this.varDetailsPagination.current = 1;
            this.variableDetailsList();
        },
        // 【绑定变量】侧边弹出 取消
        bindVarCancel() {
            this.varVisible = false;
        },
        onVarClose() {
            this.varVisible = false;
        },
        // 【绑定变量】侧边弹出 取消
        varSearchQuery() {
            console.log("varSearchQuery")
            this.varPagination.current = 1;
            this.bindVarList();
        },
        // 【绑定变量】侧边弹出 表格变动
        varTableChange(pagination) {
            console.log("varTableChange")
            this.varPagination.current = pagination.current;
            this.bindVarList();
        },
        //【绑定变量】侧边弹出 获取列表
        bindVarList() {
            console.log("bindVarList")

            let queryParam = {
                iotId: '',
                pageNo: this.varPagination.current,
                pageSize: this.varPagination.pageSize,
                status: this.varQuseryInfo.varQuseryStatus ? this.varQuseryInfo.varQuseryStatus : '2',
                variableName: this.varQuseryInfo.varQuseryName,
            }
            console.log(queryParam)

            getEquipmentVariableList(queryParam)
                .then((res) => {
                    console.log(res);
                    let _this = this;
                    _this.varLoading = false;
                    if (res.code * 1 == 200) {
                        _this.varDataSource = [];
                        let list = res.result.records;
                        list.map(index => {
                            _this.varDataSource.push({
                                id: index.id,
                                iotId: index.iotId,
                                iotName: index.iotName,
                                status: index.status, //绑定状态1为绑定，0为未绑定
                                variableName: index.variableName,
                                variableTitle: index.variableTitle
                            })

                        })
                        _this.varPagination.current = res.result.current;
                        _this.varPagination.total = res.result.total;
                    } else {
                        _this.$message.info(res.message);
                        _this.varDataSource = [];
                        _this.varPagination.current = 0;
                        _this.varPagination.total = 1;
                    }
                })
                .catch((err) => {
                    this.$message.error(err)
                })

        },
        //【绑定变量】侧边弹出- 点击绑定
        bindvarOne(data) {
            console.log("bindvarOne")
            let _this = this;
            let params = {
                iotId: _this.bindId,
                id: '',
                variableId: data.id,
            }
            console.log(params);
            setiotEquipmentVariableOne(params)
                .then((res) => {

                    console.log(res)
                    if (res.code == 200) {
                        _this.$message.info(res.message);

                        //刷新列表
                        _this.bindVarInitLoad();
                    } else {
                        _this.$message.info(res.message);
                    }
                })
                .catch((err) => {
                    _this.$message.error(err)
                })


            //bindGisIot(res, _this);
            // this.$confirm({
            //     title: '确定要绑定吗?',
            //     content: '',
            //     onOk() {

            //     },
            //     onCancel() {
            //         _this.$message.info("取消操作")
            //     },
            // });
        },
        varTimeChange(value) {
            console.log(`varTimeChange ${value}`);
            this.aeInfo.iotCycleTypeValue = value;

        },
        //////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////
        //【设备绑定 解除设备】
        unbindEquipment(data) {
            let params = {
                modelId: data.id
            }
            let _this = this;
            this.$confirm({
                title: '确定要解除绑定吗?',
                content: '',
                onOk() {
                    _this.$message.info("测试数据：设备解除绑定")

                    setUnbindEquipment(params)
                        .then((res) => {
                            if (res.code == 200) {
                                _this.$message.info(res.message);
                                //刷新列表
                                _this.GetAeList();
                                _this.bindEquipmentVisible = false;
                                _this.bindEquipmentName = '';
                            } else {
                                _this.$message.info(res.message);
                            }
                        })
                        .catch((err) => {
                            _this.$message.error(err)
                        })

                },
                onCancel() {
                    _this.$message.info("取消操作")
                },
            });

        },
        //【设备绑定 绑定设备】
        bindEquipment(data) {
            console.log("【设备信息列表】设备绑定")
            console.log(data)
            this.bindEquipmentId = data.id;
            this.bindEquipmentVisible = true;
            this.bindEquipmentGetList();
        },
        // 【设备绑定】弹出层取消按钮
        bindEquipmentCancel() {
            this.bindEquipmentVisible = false;
            this.bindEquipmentName = '';
        },
        // 【设备绑定】弹出层OK按钮
        bindEquipmentOk() {
            this.bindEquipmentVisible = false;
            this.bindEquipmentName = '';
        },
        //【设备绑定】弹出层翻页事件
        bindEquipmentTableChange(pagination) {

            this.bindEquipmentPagination.current = pagination.current;
            this.bindEquipmentGetList();
        },
        //设备绑定】弹出层获取表信息
        bindEquipmentGetList() {
            console.log("bindEquipmentGetList")
            this.bindEquipmentloading = true;
            let params = {
                // iotCategory: $.trim(this.bindEquipmentName),
                pageNo: this.bindEquipmentPagination.current,
                pageSize: this.bindEquipmentPagination.pageSize
            }

            console.log(params)
            let _this = this;
            getbindEquipmentList(params)
                .then((res) => {
                    this.bindEquipmentloading = false;
                    console.log(res)
                    if (res.code == 200) {
                        _this.bindEquipmentDataSource = [];
                        let list = res.result.records;
                        list.map(index => {
                            console.log("getbindEquipmentList")
                            console.log(index);
                            _this.bindEquipmentDataSource.push({
                                id: index.id,

                                iotCategory: index.iotCategory,
                                iotCycle: index.iotCycle,
                                iotName: index.iotName,
                                iotSn: index.iotSn,
                                iotStatus: index.iotStatus,
                            })
                        })
                        _this.bindEquipmentPagination.current = res.result.current;
                        _this.bindEquipmentPagination.total = res.result.total;

                    } else {
                        _this.$message.info(res.message);
                        _this.bindEquipmentDataSource = [];
                        _this.bindEquipmentPagination.current = 0;
                        _this.bindEquipmentPagination.total = 1;

                    }

                })
                .catch((err) => {
                    _this.$message.error(err)
                })
        },
        //设备绑定】弹出层 查询获取表信息
        bindEquipmentQuery() {
            console.log("bindEquipmentQuery")
            this.bindEquipmentGetList();
        },
        //设备绑定】弹出层 绑定一条信息
        bindEquipmentOne(data) {
            console.log("bindEquipmentOne")
            let params = {
                modelId: this.bindEquipmentId,
                equipmentId: data.id,
                data: data
            }
            console.log(params)
            let _this = this;
            setEquipmentOne(params).then((res) => {

                console.log(res)
                if (res.code == 200) {
                    //关闭弹出层 刷新父列表 清输入
                    _this.$message.info(res.message);
                    _this.GetAeList();
                    _this.bindEquipmentVisible = false;
                    _this.bindEquipmentName = '';


                } else {
                    _this.$message.info(res.message);
                    _this.bindEquipmentDataSource = [];
                    _this.bindEquipmentPagination.current = 0;
                    _this.bindEquipmentPagination.total = 1;

                }

            }).catch((err) => {
                _this.$message.error(err)
            })

        },
        //////////////////////////////////////////////////////////////// 
        handleEquipmentCycle() {
            this.iotCategory = '';
            this.iotCycleNumber = '';
            this.iotCycleType = '';
            this.bindEquipmentCycleVisible = true;

        },
        bindEquipmentCycleCancel() {
            this.bindEquipmentCycleVisible = false;
        },
        bindEquipmentCycleOk() {
            // this.bindEquipmentCycleVisible = false;
            console.log(this.iotCategory);
            console.log(this.iotCycleNumber);
            console.log(this.iotCycleType);
            if (!this.iotCategory) {
                this.$message.info('设备类型不能为空！');
                return;
            }
            if (!this.iotCycleNumber) {
                this.$message.info('采集周期不能为空！');
                return;
            }
            if (!this.iotCycleType) {
                this.$message.info('采集周期不能为空！');
                return;
            }
            let iotCycle = this.iotCycleNumber * this.iotCycleType;
            let data = {
                iotCategory: this.iotCategory,
                iotCycle: iotCycle
            }
            console.log(data)
            iotEquipmentUpdateCycleByCate(data, this);
        },


        ////////////////////////////////////////////////////////////////
        binAlarmRulesClick(data) {
            this.alarmRulesVisible = true;
            this.stateRuleTitle = '状态规则(当前设备:' + data.iotSn + ')';
            this.binAlarmRulesId = data.id;
            this.alarmName2 = '';
            this.workingStatus = '';
            console.log(this.binAlarmRulesId);
            this.alarmRulesPagination.current = 1;
            this.alarmRulesUpdata();
        },

        ////////////////////////////////////////////////////////////////
        handleAlarAmModel() {
            this.alarmModelVisible = true;
            this.handleAlarAmModelUpdata();
        },
        handleAlarAmModelUpdata() {
            let data = {
                alarmTitle: $.trim(this.alarmModelInfo.Title),
                pageNo: this.alarmModelPagination.current,
                pageSize: this.alarmModelPagination.pageSize
            }
            alarmModle(data, this);
        },
        alarmModelClose() {
            this.alarmModelVisible = false;
        },
        alarmModelSearchQuery() {
            this.handleAlarAmModelUpdata();
        },
        alarmModelTableChange(pagination) {
            this.alarmModelPagination.current = pagination.current;
            this.handleAlarAmModelUpdata();
        },

        ruleClose() {
            this.ruleVisible = false;
        },
        ruleSubmit() {

            if (!this.ruleSubmitOk) return;
            this.ruleSubmitOk = false;
            let andOr = this.ruleList[0].andOr;

            console.log(this.ruleList[0].andOr);
            if (this.ruleList[0].andOr != 0 && this.ruleList[0].andOr != 1) {
                this.$message.info('规则值不能为空！');
                this.ruleSubmitOk = true;
                return;
            }
            for (let i = 0; i < this.ruleList.length; i++) {

                if (!this.ruleList[i].variableId) {
                    this.$message.info('规则值不能为空！');
                    this.ruleSubmitOk = true;
                    return;
                } else if (!this.ruleList[i].alarmRule) {
                    this.$message.info('规则值不能为空！');
                    this.ruleSubmitOk = true;
                    return;
                } else if (!this.ruleList[i].alarmValue) {
                    this.$message.info('规则值不能为空！');
                    this.ruleSubmitOk = true;
                    return;
                }

            }
            if (!this.rule_type) {
                this.$message.info('选择状态不能为空！');
                this.ruleSubmitOk = true;
                return;
            }
            if (this.rule_type == 0) {
                if (!this.alarmName) {
                    this.$message.info('规则名称不能为空！');
                    this.ruleSubmitOk = true;
                    return;
                }
                if (this.alarm2) {
                    if (!this.alarmLevel) {
                        this.$message.info('告警分级不能为空！');
                        this.ruleSubmitOk = true;
                        return;
                    }

                    if (!this.alarmModleId) {
                        this.$message.info('告警内容不能为空！');
                        this.ruleSubmitOk = true;
                        return;
                    }
                }

            }

            this.ruleList.map(index => {
                delete index.andOr;
            })
            let data;
            if (this.rulesId) {
                if (!this.rulesOk && this.rulesOk2) {
                    data = {
                        alarmName: $.trim(this.alarmName),
                        alarmRuleType: this.rule_type,
                        alarm: this.alarm2 ? '1' : '0',
                        alarmLevel: this.alarmLevel,
                        alarmModleId: this.alarmModleId,
                        id: this.rulesId,
                        equipmentId: this.rulesEquipmentId
                    }
                    equipmentAlarmUpdateValue(data, this);
                } else {
                    if (this.rule_type == 0) {
                        if (this.alarm2) {
                            data = {
                                alarmLevel: this.alarmLevel,
                                // isMass:this.isMass?'1':'0',
                                alarmName: this.alarmName,
                                alarmModleId: this.alarmModleId,
                                alarmRuleList: this.ruleList,
                                equipmentId: this.binAlarmRulesId,
                                andOr: andOr,
                                alarm: this.alarm2 ? '1' : '0',
                                alarmRuleType: 0,
                                id: this.rulesId,
                            }
                        } else {
                            data = {

                                // isMass:this.isMass?'1':'0',
                                alarmName: this.alarmName,
                                alarmRuleList: this.ruleList,
                                andOr: andOr,
                                alarm: this.alarm2 ? '1' : '0',
                                alarmRuleType: 0,
                                equipmentId: this.binAlarmRulesId,
                                id: this.rulesId,
                            }
                        }

                    } else {
                        data = {
                            alarmRuleType: this.rule_type,
                            // isMass:this.isMass?'1':'0',
                            alarmName: this.alarmName,

                            alarmRuleList: this.ruleList,
                            equipmentId: this.binAlarmRulesId,
                            andOr: andOr,
                            id: this.rulesId,
                        }
                    }
                    equipmentAlarmUpdateAlarm(data, this);
                }

            } else {
                if (this.rule_type == 0) {
                    if (this.alarm2) {
                        data = {
                            alarmLevel: this.alarmLevel,
                            // isMass:this.isMass?'1':'0',
                            alarmName: this.alarmName,
                            alarmModleId: this.alarmModleId,
                            alarmRuleList: this.ruleList,
                            equipmentId: this.binAlarmRulesId,
                            andOr: andOr,
                            alarm: this.alarm2 ? '1' : '0',
                            alarmRuleType: 0
                        }
                    } else {
                        data = {

                            // isMass:this.isMass?'1':'0',
                            alarmName: this.alarmName,
                            alarmRuleList: this.ruleList,
                            andOr: andOr,
                            alarm: this.alarm2 ? '1' : '0',
                            alarmRuleType: 0,
                            equipmentId: this.binAlarmRulesId,
                        }
                    }

                } else {
                    data = {
                        alarmRuleType: this.rule_type,
                        // isMass:this.isMass?'1':'0',
                        alarmName: this.alarmName,

                        alarmRuleList: this.ruleList,
                        equipmentId: this.binAlarmRulesId,
                        andOr: andOr
                    }
                }
                console.log(data)
                alarmOne(data, this);
            }


        },
        addRule() {
            this.ruleList.push({
                andOr: '0',
                variableId: '',
                alarmRule: '',
                alarmValue: '',
            });
        },
        removeRule(index) {
            this.ruleList.splice(index, 1);
        },
        alarmModelEdit(type, record) {
            this.templateTitleVal = '';
            this.templateVal = '';
            if (type == 'add') {
                this.templatetitle = '新增模板信息';
            } else {
                this.templatetitle = '修改模板信息';
                this.templateChangeId = record.id;
                this.templateTitleVal = record.alarmTitle;
                this.templateVal = record.alarmValue;
            }
            this.templateVisible = true;
        },
        templatehandleOk() {
            if (!this.templateTitleVal) {
                this.$message.info('模板标题不能为空!');
                return;
            }
            if (!this.templateVal) {
                this.$message.info('内容不能为空!');
                return;
            }
            let rgx = /{{[^}}]+}}/g;
            let arr = this.templateVal.match(rgx);
            let arr2 = this.rexValArr;
            let num = 0;
            console.log(arr)
            if (arr) {
                for (let i = 0; i < arr.length; i++) {
                    for (let j = 0; j < arr2.length; j++) {
                        if (arr2[j] == arr[i]) {
                            num++
                        }
                    }
                }
                if (num != arr.length) {
                    this.$message.info('内容中存在未定义变量');
                    return;
                }
            }


            let data;
            if (this.templatetitle.indexOf('新增') != -1) {
                data = {
                    alarmValue: $.trim(this.templateVal),
                    alarmTitle: $.trim(this.templateTitleVal)
                }
            } else {
                data = {
                    alarmValue: $.trim(this.templateVal),
                    alarmTitle: $.trim(this.templateTitleVal),
                    id: this.templateChangeId
                }
            }

            alarmModleOne(data, this);


        },
        templatehandleCancel() {
            this.templateVisible = false;
        },
        addVar(txt) {
            this.templateVal = this.templateVal + txt;
        },
        alarmModelRemove(record) {
            let _this = this;
            this.$confirm({
                title: '确定要删除吗?',
                content: '',
                onOk() {
                    let data = {
                        id: record.id
                    }
                    removealarmModleId(data, _this);
                },
                onCancel() {

                },
            });
        },
        alarmRulesClose() {
            this.alarmRulesVisible = false;
        },
        alarmRulesSearchQuery() {
            this.alarmRulesUpdata();
        },
        alarmRulesAdd() {
            this.ruleVisible = true;
            this.alarmLevel = '';
            // this.isMass=true;
            this.alarmName = '';
            this.alarmModleId = '';
            this.rule_type = '';
            this.rule_typeVal = true;
            this.alarm2 = true;
            this.rulesId = '';
            this.ruleList = [{
                andOr: '0',
                variableId: '',
                alarmRule: '',
                alarmValue: '',
            }];
            this.rulesOk = true;
            this.rulesOk2 = true;
            let data = {
                iotId: this.binAlarmRulesId,
                status: 1
            }
            getVarRule(data, this);
            getContrastVal(this);
            getWarnLevel(this);
            getAndOr(this);
            let data2 = {
                pageSize: 10000
            }
            getAlarmContent(data2, this);

        },
        alarmRulesUpdata() {

            let data = {
                alarmName: $.trim(this.alarmName2),
                pageNo: this.alarmRulesPagination.current,
                pageSize: this.alarmRulesPagination.pageSize,
                equipmentId: this.binAlarmRulesId,
                alarmStatusCode: this.workingStatus
            }
            getAlarmList(data, this);
        },
        alarmRulesTableChange(pagination) {
            this.alarmRulesPagination.current = pagination.current;
            this.alarmRulesUpdata();
        },
        alarmRuleRemove(record) {
            let _this = this;
            this.$confirm({
                title: '确定要删除吗?',
                content: '',
                onOk() {
                    let data = {
                        id: record.id
                    }
                    deleteRule(data, _this);
                },
                onCancel() {

                },
            });

        },
        //反向控制按钮效果
        reverseControl(record) {
            this.reverseControlId = record.id;
            this.reverseControlUpdata();
            this.reverseControlVisible = true;
            let res = {
                iotId: this.reverseControlId,
                status: 1
            }
            getVarRule(res, this);
        },
        reverseControlUpdata() {
            let data = {
                equipmentId: this.reverseControlId,
                pageNo: this.reverseControlPagination.current,
                pageSize: this.reverseControlPagination.pageSize
            }
            getIotControList(data, this);

        },
        //反向控制弹框关闭
        reverseControlCancel() {
            this.reverseControlVisible = false;
        },
        //反向控制列表改变时
        reverseControlTableChange(pagination) {
            this.reverseControlPagination.current = pagination.current;
            this.reverseControlUpdata();
        },
        //反向控制新增或者修改
        reverseControlAddOrChange(type, data) {

            this.controName = '';
            this.variableId = '';
            this.variableValue = '';
            this.varType = '';
            if (type == 'add') {
                this.controlTitle = '新增控制';
            } else {
                console.log(data)
                this.controlTitle = '修改控制';
                this.controName = data.controName;
                this.variableId = data.variableId;
                this.variableValue = data.variableValue;
                this.controlId = data.id;
                this.varType = data.variableDataType;
                if (this.varType == 5) {
                    this.variableValue2 = data.variableValue;
                }
            }
            this.controlVisible = true;
        },
        //新增控制关闭
        controlCancel() {
            this.controlVisible = false;
        },
        //新增控制确定
        controlOk() {
            if (!this.controName) {
                this.$message.info('控制名称不能为空!');
                return;
            }
            if (!this.variableId) {
                this.$message.info('绑定变量不能为空!');
                return;
            }
            if (this.varType == 5) {
                if (!this.variableValue2) {
                    this.$message.info('变量值不能为空!');
                    return;
                }
            } else {
                if (!this.variableValue) {
                    this.$message.info('变量值不能为空!');
                    return;
                }
            }

            let data;
            if (this.controlTitle.indexOf('新增') != -1) {
                data = {
                    controName: $.trim(this.controName),
                    variableId: this.variableId,
                    variableValue: this.varType == 5 ? this.variableValue2 : this.variableValue,
                    equipmentId: this.reverseControlId
                }
            } else {

                data = {
                    controName: $.trim(this.controName),
                    variableId: this.variableId,
                    variableValue: this.varType == 5 ? this.variableValue2 : this.variableValue,
                    equipmentId: this.reverseControlId,
                    id: this.controlId
                }

            }


            iotControOne(data, this);
        },
        //删除反控列表数据
        reomeveIotControId(record) {
            let data = {
                id: record.id
            }
            reomeveIotContro(data, this);
        },
        //绑定变量改变时
        variableIdChange(item) {
            this.variableValue = '';
            let _this = this;
            this.varList.map(index => {
                if (index.id == item) {

                    _this.varType = index.variableDataType;
                }
            })
        },
        clearNoNum(e) {
            let obj = e.target;
            obj.value = obj.value.replace(/[^\d.]/g, "");  //清除“数字”和“.”以外的字符
            obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
            obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
            if (obj.value.indexOf(".") < 0 && obj.value != "") {//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                obj.value = parseFloat(obj.value);
            }
        },
        rule_typeChange(data) {
            this.alarmLevel = '';
            this.alarmModleId = '';
            this.alarm2 = true;
            if (data != 0) {
                this.rule_typeVal = false;
            } else {
                this.rule_typeVal = true;
            }
        },
        toConfigure(record) {
            this.typeCode = '';
            this.checkQuality = false;
            this.alarm = false;
            this.alarmLevel2 = '';
            this.alarmModel = '';
            getWarnLevel(this);
            let res = {
                pageSize: 10000
            }
            getAlarmContent(res, this);
            if (record) {
                this.toConfigurebOk = false;
                this.toConfigurebId = record.id;
                let data = {
                    id: this.toConfigurebId
                }
                getEquipmentConfigOne(data, this);
            } else {
                this.toConfigurebOk = true;
            }
            this.toConfigureVisible = true;
        },
        toConfigureOk() {
            if (this.toConfigurebOk) {
                if (!this.typeCode) {
                    this.$message.info('设备类型不能为空!');
                    return;
                }
            }
            if (this.alarm) {
                if (!this.alarmLevel2) {
                    this.$message.info('告警等级不能为空!');
                    return;
                }
                if (!this.alarmModel) {
                    this.$message.info('告警模板不能为空!');
                    return;
                }
            }

            let data;
            if (this.toConfigurebOk) {
                data = {
                    typeCode: this.typeCode,
                    checkQuality: this.checkQuality ? '1' : '0',
                    alarm: this.alarm ? '1' : '0',
                    alarmLevel: this.alarmLevel2,
                    alarmModel: this.alarmModel,
                }
                equipmentConfigCat(data, this);
            } else {
                data = {
                    typeCode: this.typeCode,
                    checkQuality: this.checkQuality ? '1' : '0',
                    alarm: this.alarm ? '1' : '0',
                    alarmLevel: this.alarmLevel2,
                    alarmModel: this.alarmModel,
                    equipmentId: this.toConfigurebId
                }
                equipmentConfigOne(data, this);
            }
            console.log(data);

        },
        toConfigureCancel() {
            this.toConfigureVisible = false;
        },
        bindAsset(record) {
            this.bindAssetsVisible = true;
        },
        //绑定弹框确定
        bindOnOk() {
            this.bindGisVisible = false;
            this.gismodelName = '';
        },
        //绑定弹框取消
        bindGisCancel() {
            this.bindGisVisible = false;
            this.gismodelName = '';
        },
        //
        gishandleTableChange2(pagination) {
            this.gisipagination2.current = pagination.current;
            this.updata2();
        },
        gissearchQuery2() {
            this.gisipagination2.current = 1;
            this.updata2();
        },
        updata2() {
            let data = {
                name: $.trim(this.gismodelName),
                pageNo: this.gisipagination2.current,
                pageSize: this.gisipagination2.pageSize
            }
            this.gisloading2 = true;

            getGisVoList2(data, this);
        },
        //点击设备绑定触发弹框
        bindGisShow(data) {
            console.log(data)
            this.gisbindId = data.id;
            this.bindGisVisible = true;
            this.updata2();
        },
        //点击绑定触发弹框
        bindShow2(data) {
            let res = {
                iotId: this.gisbindId,
                optId: data.id
            }
            iotEquipmentBindBindData(res, this);
        },
        unbindGis(data) {
            let _this = this;
            this.$confirm({
                title: '确定要解除绑定吗?',
                content: '当前资产：' + data.optCategoryName,
                onOk() {
                    let res = {
                        iotId: data.id
                    }
                    iotEquipmentBindUnbindData(res, _this);
                },
                onCancel() {
                    console.log('Cancel');
                },
                class: 'test',
            });

        },
        //同步按钮
        iotEquipmentSync() {
            iotEquipmentSync(this);
        },
        synchronousCache() {
            let data = {
                url: '/equipment/iotEquipment/clearRedis'
            }
            synchronousCache(data, this);
        },
        alarm2Change(data) {
            console.log(data)
            this.alarmLevel = '';
            this.alarmModleId = '';
            if (data) {
                this.rule_typeVal = true;
            } else {
                this.rule_typeVal = false;
            }
        },
        alarmChange(data) {
            this.alarmLevel2 = '';
            this.alarmModel = '';
        },
        alarmStatusChange(id, data) {
            console.log(data);
            console.log(id);
            let res = {
                alarmStatusCode: data ? '1' : '0',
                id: id
            }
            equipmentAlarmStartOrStop(res, this);
        },
        onSelectChange(selectedRowKeys, selectionRows) {
            console.log(selectedRowKeys);
            console.log(selectionRows);
            this.selectedRowKeys = selectedRowKeys;
            this.selectionRows = selectionRows;
        },
        //状态规则-修改策略
        modificationRules(record) {
            this.alarmRulesAdd();
            this.ruleVisible = false;
            let data = {
                id: record.id
            }
            this.rulesOk = true;
            this.rulesOk2 = true;
            equipmentAlarmIdDetails(data, this);
        },
        //状态规则-修改内容
        modifyInformation(record) {
            this.alarmRulesAdd();
            this.ruleVisible = false;
            let data = {
                id: record.id
            }
            this.rulesOk = false;
            this.rulesOk2 = true;
            equipmentAlarmIdDetails(data, this);
        },
    },
}
//equipment页面组件混入的equipment.js