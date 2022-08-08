import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import monitorModal from '@/views/workOrderManagement/modules/MonitorModal'
import equipmentList from '@/views/workOrderManagement/maintenance/modules/equipmentList'
import questionList from '@/views/workOrderManagement/maintenance/modules/questionList'
import {
    onWorkListChange,
    getDropAreasData,
    onDeleteChange,
    onWorkListQueryChange,
} from '@/api/workOrderManagement/maintenance/index.js'
export default {
    name: "maintenance",
    mixins: [JeecgListMixin],
    components: {
        monitorModal,
        equipmentList,
        questionList,
    },
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
                title: '工单名称',
                align: "center",
                dataIndex: 'name'
            },{
                title: '工单编号',
                align: "center",
                dataIndex: 'code'
            },{
                title: '班组',
                align: "center",
                dataIndex: 'teamName'
            },{
                title: '责任人',
                align: "center",
                dataIndex: 'leaderName'
            },{
                title: '实施时间',
                align: "center",
                dataIndex: 'startDate'
            },{
                title: '处理状态',
                align: "center",
                dataIndex: 'statusDes'
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: {customRender: 'action'},
            }],
            url: {
                list: "/worklist/workList/list/keep",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            changeId: '', // 修改id
            equipmentList:[],
            workTeamId: '',
            assignId: '', // 列表行id
            matterListColumns: [{
                title: '标题',
                align: "center",
                width: 190,
                dataIndex: 'title',
            },{
                title: '任务类型', 
                align: "center",
                width: 100,
                dataIndex: 'typeDes',
            },{
                title: '预计保养时间',
                width: 190,
                align: "center",
                dataIndex: 'nextKeepDate',
                scopedSlots: { customRender: 'nextKeepDate' },
            },{
                title: '排序',
                width: 100,
                align: "center",
                dataIndex: 'seq',
                scopedSlots: { customRender: 'seq' },
            },{
                title: '操作',
                width: 100,
                align: "center",
                dataIndex: 'operation',
                scopedSlots: { customRender: 'operation' },
            }],
            matterListDataSource: [],
            matterListDataSourceId: [],
            editingKey: '',
            equipmentListVisible: false, //是否显示设备列表
            questionListVisible: false, //是否临时任务列表
            equipmentList: [], //选中的维养数据
            questionList: [], //选中的临时问题数据
            matterList: [], //选中的设备级临时问题数据
            see_disabled: false,
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            name: '',
            code: '',
            matterList: [],
            workTeamList: [],
            memberList: [],
            startDate: '',
            overDate: '',
            teamId: '',
            teamLeaderId: '',
            monitorData: [], //工单监控数据
            workStatus: [], //工单状态
            spinning: true,
        }
    },
    computed: {
        rowSelection() {
          return {
            onChange: (selectedRowKeys, selectedRows) => {
              console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            },
          };
        }
    },
    mounted() {
        getDropAreasData('/worklist/workList/status', 'workStatus', this )

        let data = this.matterListDataSource;
        this.cacheData = data.map(item => ({ ...item }));
    },
    methods: {
        //新增/修改弹框
        handleAdd(type, data) {
            this.visible = true;
            this.see_disabled = false;
            if (type == 'add') {
                this.spinning = false;
                this.drawerTitle = '新建工单';
            } else if (type === 'see') {
                this.see_disabled = true;
                this.drawerTitle = '查看工单';
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId)
            } else {
                this.drawerTitle = '修改工单';
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId)
            }
        },

        onWorkListQueryChange(changeId) { //单条工单查询
            onWorkListQueryChange(changeId, this, data => {
                this.code = data.code;
                this.name = data.name;
                this.startDate = data.startDate;
                this.overDate = data.overDate;
                this.teamId = data.teamId; //班组id
                this.teamLeaderId = data.teamLeaderId; //班组管理员id
                // this.matterList = data.matterList;
                this.matterListDataSource = data.matterList //工单任务列表

                this.onSetOverDateChange({}, this.overDate, () => {
                    this.onSetWorkTeamChange(this.teamId)
                    setTimeout(() => { this.spinning = false }, 50);
                })
            })
        },

        //提交新增/修改信息
        addSubmit() {
            this.matterList = [];
            let equipmentObject = {};
            let questionObject = {};
            this.matterListDataSource.forEach((item) => {
                if(item.type != 3) {
                    equipmentObject = {
                        title: item.title,
                        description: item.description,
                        equipmentId: item.equipmentId || item.id,
                        matterLatitude: item.matterLatitude,
                        matterLongitude: item.matterLongitude,
                        nextKeepDate: item.nextKeepDate,
                        seq: item.seq,
                        type: item.type,
                    }
                    this.matterList.push(equipmentObject);
                }else {
                    questionObject = {
                        id: item.id,
                        seq: item.seq,
                        type: 3,
                    }
                    this.matterList.push(questionObject)
                }
               
            })
            let data;
            if (this.drawerTitle.indexOf('新建') != -1) {
                data = {
                    code: this.code,
                    name: this.name,
                    startDate: this.startDate,
                    overDate: this.overDate,
                    teamId: this.teamId, //班组id
                    teamLeaderId: this.teamLeaderId, //班组管理员id
                    matterList: this.matterList,
                }
                this.onFormValidationChange(data, () => {
                    onWorkListChange('add', data, this);
                });
            } else { 
                data = {
                    id: this.changeId, //当前修改父级ID
                    code: this.code,
                    name: this.name,
                    startDate: this.startDate,
                    overDate: this.overDate,
                    teamId: this.teamId, //班组id
                    teamLeaderId: this.teamLeaderId, //班组管理员id
                    matterList: this.matterList,
                }
                this.onFormValidationChange(data, () => {
                    onWorkListChange('edit', data, this);
                });
            }
        },
        
        onFormValidationChange(data, callBack) {
            let flag = false;
            let message = '';
            data.matterList.forEach(item => {
                if (!item.nextKeepDate && item.type != 3 ) {
                    message = '请完善预计保养时间！'
                } else if (!+item.seq) { 
                    message = '请完善任务排序，并且排序值不能为0！'
                }
                console.log(data.matterList)
                if (item.type == 2) {
                    flag = true;
                }
            });
            if (!flag) {
                message = '请选择维养记录！'
            } else if (!data.name) {
                message = '请输入工单名称！'
            } else if (data.name.length > 32) {
                message = '工单名称长度不能大于32，目前长度' + data.name.length
            }  else if (!data.code) {
                message = '请输入工单编号！'
            } else if (data.code.length > 32) {
                message = '工单编号长度不能大于32，目前长度'+ data.code.length
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
            } 
            if (message) {
                this.$message.info(message);
                return
            }
            callBack();
        },

        //新增/修改重置页面内属性
        reset() {
            this.spinning = true;
            this.code = '';
            this.name = '';
            this.startDate = '';
            this.overDate = '';
            this.teamId = '';
            this.teamLeaderId = '';
            this.matterList = [];
            this.matterListDataSource = [];
        },
        onCloseChange() { //关闭弹层
            this.visible = false;
            this.reset();
        },

        onDeleteChange(id) { // 删除工单
            console.log("删除=================")
            onDeleteChange(id, this)
        },

        onShowMonitorModal(id) { //显示工单监控弹层
            onWorkListQueryChange(id, this, data => {
                this.monitorData = data;
                this.$refs.modalMonitor.showModal(id, 'maintenance');
                this.$refs.modalMonitor.title = "工单执行情况监控";
                this.$refs.modalMonitor.disableSubmit = false;
            })
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
        onSetWorkTeamChange(teamId) {
            this.teamId = teamId;
            this.memberList = [];
            this.workTeamList.forEach(item => {
                if (item.id === teamId) {
                    this.memberList = item.memberList
                }
            })
        },

        //是否显示维养记录
        clickEquipmentList () {
            this.equipmentList = this.matterListDataSource.filter((item) => item.type != 3 );
            this.equipmentListVisible = true;
            setTimeout(() => {
                this.$refs.equipmentListTable.showModle()
            })
        },

        //确定选择维养记录
        equipmentListOk () {
            let selectEquipmentList = this.$refs.equipmentListTable.selectedData; //勾选中所有数据
            let newRows = this.matterListDataSource.concat(selectEquipmentList);
            let map = new Map();
            this.matterListDataSource = newRows.filter( item => item.type==3 || ( !map.has(item.equipmentId || item.id) && map.set(item.equipmentId || item.id, 1) ) );
            this.equipmentListVisible = false;
        },

        //是否显示问题
        clickQuestionList () {
            this.questionList = this.matterListDataSource.filter((item) => item.type == 3 );
            this.questionListVisible = true;
            setTimeout(() => {
                this.$refs.questionListTable.showModle();
            })
        },

        //确定选择临时问题
        questionListOk () {
            let selectQuestionList = this.$refs.questionListTable.selectedData; //勾选中所有数据
            let newRows = this.matterListDataSource.concat(selectQuestionList);
            let map = new Map();
            this.matterListDataSource = newRows.filter(item => !map.has(item.id) && map.set(item.id, 1));
            this.questionListVisible = false;
        },

        handleChangeInput(value, id, column) {
            const newData = [...this.matterListDataSource];
            const target = newData.filter(item => id === item.id)[0];
            if (target) {
                var re = /^[0-9]*$/;
                　　if (!re.test(value) || +((!!+target.seq)+(+value)) === 0) {
                    this.$message.info('任务排序请输入正整数！')
                    value = target.seq
                } else if (value.length > 9) {
                    value = target.seq;
                }
                target[column] = value;
                this.matterListDataSource = newData;
            }
        },

        //设置时间
        onChangeTime(date,dateString,id,column) {
            const newData = [...this.matterListDataSource];
            const target = newData.filter(item => id === item.id)[0];
            if (target) {
                target[column] = dateString;
                this.matterListDataSource = newData;
            }
        },
        //删除
        onDelete (id) {
            const newData = [...this.matterListDataSource];
            this.matterListDataSource = this.matterListDataSource.filter((item) => item.id != id )
        }          
    }
}