import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import workListModal from '@/views/workOrderManagement/modules/WorkListModal'
import monitorModal from '@/views/workOrderManagement/modules/MonitorModal'
// import markerModal from '@/views/workOrderManagement/modules/MarkerModal'
import {
    getWorkListMatterList,
    onWorkListChange,
    getDropAreasData,
    onDelworkListChange,
    onWorkListQueryChange
} from '@/api/workOrderManagement/problemWorkOrder/index.js'

export default {
    name: "problemWorkOrder",
    mixins: [JeecgListMixin],
    components: {
        workListModal,
        monitorModal,
        // markerModal,
    },
    data() {
        return {
            oldSeq: '', //点击编辑时的seq
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
                title: '开始时间',
                align: "center",
                dataIndex: 'startDate'
            },{
                title: '结束时间',
                align: "center",
                dataIndex: 'overDate'
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
                list: "/worklist/workList/list/matter",
            },
            dataSource: [],
            ipagination: {
                current: 1,
                pageSize: 10,
                showTotal: (total, range) => {
                    return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
                },
                showQuickJumper: true,
                total: 0
            },
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
            workStatus: [], //工单状态下拉
            workListColumns: [{
                title: '问题标题',
                align: "center",
                dataIndex: 'title',
                width: 160
            },{
                title: '提交人',
                align: "center",
                dataIndex: 'subName',
                width: 80
            },{
                title: '提交时间',
                align: "center",
                dataIndex: 'subTime',
                width: 160
            },{
                title: '问题类型',
                align: "center",
                dataIndex: 'matterTypeDes',
                width: 80
            },{
                title: '状态',
                align: "center",
                dataIndex: 'statusDes',
                width: 80
            },{
                title: '排序',
                align: "center",
                dataIndex: 'seq',
                width: 80,
                scopedSlots: {
                    customRender: 'seq'
                },
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                width: 80,
                scopedSlots: {
                    customRender: 'action'
                },
            }],
            selectedRows: [],
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            see_disabled: false, //查看 - 只读
            name: '',
            code: '',
            startDate: '',
            overDate: '',
            workTeamList: [], //班组列表
            memberList: [], //管理员列表
            matterList: [], //选中问题列表
            teamId: '', //选中班组id
            teamLeaderId: '', //选中管理员id
            monitorData: [], //工单监控弹层数据
            workListMatter: [], //问题列表数据
            spinning: true,
        }
    },
    mounted() {
        getDropAreasData('worklist/workList/status', 'workStatus', this )
    },
    methods: {
        handleChange(value, id) {
            const newData = [...this.selectedRows];
            const target = newData.filter(item => id === item.id)[0];
            if (target) {
                var re = /^[0-9]*$/;
            　　if (!re.test(value)) {
                    this.$message.info('任务排序请输入正整数！')
                    value = target.seq
                } else if (value.length > 9) {
                    value = target.seq;
                }
                target.seq = value;
                this.selectedRows = newData;
                this.matterList.forEach(item => { //数组更新 - 用于post
                    if (item.id === id) {
                        item.seq = value;
                    } 
                });
                console.log(this.matterList)
            }
        },

        //新增/修改弹框
        handleAdd(type, data) {
            this.visible = true;
            this.see_disabled = false;
            if (type === 'add') {
                this.spinning = false;
                this.drawerTitle = '新建问题工单';
            } else if (type === 'see') {
                this.drawerTitle = '查看问题工单';
                this.see_disabled = true;
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId);
            } else {
                this.drawerTitle = '修改问题工单';
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId);
            }
        },
        
        onWorkListQueryChange(changeId) {
            onWorkListQueryChange(changeId, this, data => {
                this.name = data.name;
                this.code = data.code;
                this.startDate = data.startDate;
                this.overDate = data.overDate;
                this.teamId = data.teamId;
                this.teamLeaderId = data.teamLeaderId;
                this.matterList = data.matterList;
                // this.selectedRows = data.matterList;
                this.onSelectedPointChange(data.matterList, 'null')
                this.onSetOverDateChange({}, this.overDate, () => {
                    setTimeout(() => { this.spinning = false }, 50);
                    this.onSetWorkTeamChange(this.teamId);
                });

            })
        },

        onFormValidationChange(data, callBack) {
            let message = '';
            if (!data.matterList.length) {
                message = '请选择工单问题！'
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
            data.matterList.forEach(item => {
                if (!item.seq) {
                    message = '请完成问题排序！'
                }
            });
            if (message) {
                this.$message.info(message);
                return
            }
            callBack();
        },

        //提交新增/修改信息
        addSubmit() {
            let data;
            if (this.drawerTitle.indexOf('新建') != -1) {
                data = {
                    matterList: this.matterList,
                    name: this.name,
                    code: this.code,
                    startDate: this.startDate,
                    overDate: this.overDate,
                    teamId: this.teamId,
                    teamLeaderId: this.teamLeaderId,
                }
                this.onFormValidationChange(data, () => {
                    onWorkListChange('add', data, this);
                })
            } else {
                data = {
                    id: this.changeId, //当前修改父级ID
                    matterList: this.matterList,
                    name: this.name,
                    code: this.code,
                    startDate: this.startDate,
                    overDate: this.overDate,
                    teamId: this.teamId,
                    teamLeaderId: this.teamLeaderId,
                }
                this.onFormValidationChange(data, () => {
                    onWorkListChange('edit', data, this);
                })
            }
        },
        //新增/修改重置页面内属性
        reset() {
            this.spinning = true;
            this.name = '';
            this.code = '';
            this.startDate = '';
            this.overDate = '';
            this.teamId = '';
            this.teamLeaderId = '';
            this.matterList = [];
            // this.selectedRows = '';
            this.onSelectedPointChange([], 'null')
        },
        onCloseChange() {
            this.visible = false;
            this.reset();
        },

        onDeleteChange(id) { //删除区域
            onDelworkListChange(id, this)
        },

        disabledDate(current) {
            return current && current < moment().endOf('day');
        },
        
        onShowMonitorModal(id) { //显示工单监控弹层
            onWorkListQueryChange(id, this, data => {
                this.monitorData = data;
                this.$refs.modalMonitor.showModal(id, 'problem');
                this.$refs.modalMonitor.title = "工单执行情况监控";
                this.$refs.modalMonitor.disableSubmit = false;
            });
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

        onShowWorkListModal() { //问题列表- 弹层展示
            getWorkListMatterList({subTimeStart: '',subTimeEnd: ''}, this);
            this.$refs.modalForm.showModal();
            this.$refs.modalForm.title = "选择问题";
            this.$refs.modalForm.disableSubmit = false;
        },

        onSearchQueryChange(data) {
            getWorkListMatterList(data, this); //查询
        },

        onSelectedPointChange(selectedRows, type) { //选择问题弹层 - 确定选择
            let newRows = [];
            this.matterList = [];
            newRows = selectedRows
            if (type !== 'null') {
                let map = new Map();
                newRows = this.selectedRows.concat(newRows);
                newRows = newRows.filter(item => !map.has(item.id) && map.set(item.id, 1)) //已选项变动 - 合并去重
            }
            newRows.forEach((item, index) => {
                this.matterList.push({id: item.id, seq: item.seq || '' }) //选中的问题 - 请求接口
            })
            this.selectedRows = newRows; //选中的问题 - 展示
            console.log(this.matterList)
        },

        onSelPointDelChange(id) { //选中的问题列表 - 删除操作
            console.log(id)
            this.selectedRows = this.selectedRows.filter(item => {
                if (item.id != id) {
                    return item
                }
            })
            this.onSelectedPointChange(this.selectedRows, 'null')
        },

        notificationStrategyDefinition (){},
        drawerSearchReset(){},
        notificationStrategyhandleTableChange(){},
        modalMonitorOk(){},
    }
}