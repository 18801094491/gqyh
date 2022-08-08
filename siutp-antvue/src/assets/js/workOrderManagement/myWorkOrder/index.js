import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import taskDetailsModal from '@/views/workOrderManagement/modules/TaskDetailsModal'
import {
    onWorkListChange,
    getDropAreasData,
    onWorkListQueryChange,
    onTaskListMatterQueryChange,
    onCompleteChange,
} from '@/api/workOrderManagement/myWorkOrder/index.js'

export default {
    name: "myWorkOrder",
    mixins: [JeecgListMixin],
    components: {
        taskDetailsModal,
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
                title: '工单类型',
                align: "center",
                dataIndex: 'typeDes'
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
                list: "/worklist/myWorkList/list",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            changeId: '', // 修改id
            matterListColumns: [
                {   title: '问题标题',
                    align: "center",
                    width: 150,
                    dataIndex: 'title',
                    ellipsis: 'true',
                    scopedSlots: {
                        customRender: 'notices'
                    },
                },
                {
                    title: '提交人', 
                    align: "center",
                    width: 100,
                    dataIndex: 'subName',
                },
                {
                    title: '提交时间', 
                    align: "center",
                    width: 160,
                    dataIndex: 'subTime',
                },
                {
                    title: '问题类型', 
                    align: "center",
                    width: 100,
                    dataIndex: 'typeDes',
                },
                {
                    title: '状态', 
                    align: "center",
                    width: 100,
                    dataIndex: 'statusDes',
                },
                {
                    title: '操作',
                    align: "center",
                    width: 125,
                    dataIndex: 'action',
                    scopedSlots: { customRender: 'action' },
                },
            ],
            matterListDataSource: [],
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            name: '',
            code: '',
            memberList: [],
            startDate: '',
            overDate: '',
            teamId: '',
            teamLeaderId: '',
            taskDetailsData: [], //工单查看 - 任务详情
            workStatus: [],
            workItemData: [], //查询单条工单数据
            selectedRowKeys: [], //选中的任务id - 批量标记完成
            labelCol: {
                xs: { span: 24 },
                sm: { span: 6 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 18 },
            },
            spinning: true,
        }
    },
    computed: {
        rowSelection() {
          return {
            selectedRowKeys: this.selectedRowKeys,
            onChange: (selectedRowKeys, selectedRows) => {
                this.selectedRowKeys = selectedRowKeys;
            },
            getCheckboxProps: record => ({
                props: {
                  disabled: record.status == 3, 
                  status: record.name,
                },
              }),
      
          };
        }
    },
    mounted() {
        getDropAreasData('/worklist/workList/status', 'workStatus', this )
    },
    methods: {
        handleAdd(type, data) { //新增/修改弹框
            this.visible = true;
            if (type === 'see') {
                this.drawerTitle = '工单详情';
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId)
            }
        },

        onWorkListQueryChange(changeId) { //单条工单查询
            onWorkListQueryChange(changeId, this, data => {
                this.workItemData = data;
                this.code = data.code;
                this.name = data.name;
                this.startDate = data.startDate;
                this.overDate = data.overDate;
                this.teamId = data.teamId; //班组id
                this.teamLeaderId = data.teamLeaderId; //班组管理员id
                this.matterListDataSource = data.matterList //工单任务列表
                setTimeout(() => { this.spinning = false }, 50);
                this.onSetOverDateChange({}, this.overDate, () => {
                    this.onSetWorkTeamChange(this.teamId)
                })
            })
        },

        onCompleteChange(ids) { //标记完成
            if (!ids && this.selectedRowKeys.length) {
                ids = this.selectedRowKeys.toString();
            }
            onCompleteChange(ids, this, data => {
                this.selectedRowKeys = [];
                this.onWorkListQueryChange(this.changeId)
            })
        },

        reset() { //新增/修改重置页面内属性
            this.spinning = true;
            this.code = '';
            this.name = '';
            this.startDate = '';
            this.overDate = '';
            this.teamId = '';
            this.teamLeaderId = '';
            this.selectedRowKeys = [];
            this.matterListDataSource = [];
        },

        onCloseChange() { //弹层关闭 - 工单详情
            this.visible = false;
            this.reset();
        },

        onSetStartDateChange(data, dateString) { //选择开始时间
            this.startDate = dateString;
        },
        onSetOverDateChange(data, dateString, callBack) { //选择结束时间
            this.overDate = dateString;
        },

        onShowTaskDetailsModal(id) { //查看弹层 - 查看任务详情
            onTaskListMatterQueryChange(id, this, data => {
                this.taskDetailsData = data;
                setTimeout(() => {
                    this.$refs.modalTaskDetails.showModal(id);
                    this.$refs.modalTaskDetails.title = "查看任务详情";
                });
            })
        },
    }
}