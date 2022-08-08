import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import{
    getCategory,updateProReport,getEquipmentList,updateAssignWoker,getDataList,getWarnStatusList,getTeamDutyTeamList
} from "@api/workOrderManagement-t/workerList.js";


export default {
    name: "problemReport",
    mixins:[JeecgListMixin],
    data () {
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
            },
            {
                title: '工单编号',
                align:"center",
                dataIndex: 'jobSn'
            },
            {
                title: '类型',
                align:"center",
                dataIndex: 'jobTypeName'
            },
           	{
                title: '创建时间',
                align:"center",
                dataIndex: 'createTime'
           	},
            {
                title: '状态',
                align:"center",
                dataIndex: 'jobStatusName',
            },
		    {
                title: '负责人',
                align:"center",
                dataIndex: 'manager'
           },
            {
                title: '操作',
                dataIndex: 'action',
                align:"center",
                scopedSlots: { customRender: 'action' },
            }
        ],
        url: {
            list: "/work/workJob",
        },
        dataSource:[],
        loading:false,
        visible:false,
        drawerTitle:'',  
        changeId:'', // 修改id
        warnStatusList:[], // 状态下拉
        problemName:'', // 问题名称
        problemType:'', // 问题类型
        equipmentId:'', // 问题设备信息
        problemDescription:'', // 问题描述
        problemTypeList:[], // 问题类型下拉
        equipmentList:[], // 问题设备信息下拉
        workerVisible:false,
        teamInformationList:[], // 班组下拉
        workTeamId:'',
        jobTypeList:[], // 工单类型下拉
        jobStatusList:[], // 工单状态下拉
      }
    },
    mounted(){
        getWarnStatusList(this);
        getCategory({pcode:'A23'},this,'problemTypeList');
        getEquipmentList(this)
        getTeamDutyTeamList(this); // 获取班组下拉
        getDataList('job_type',this,'jobTypeList'); // 获取工单类型下拉
        getDataList('job_status',this,'jobStatusList'); // 获取工单状态下拉
    },
    methods: {  
    	//新增/修改弹框
        handleAdd(type, data) {
        	this.visible = true;
            if (type == 'add') {
                this.drawerTitle = '新增巡检计划';                
                this.reset();
            } 
//             else {
//                 this.drawerTitle = '修改上报问题';
//                 let res = {
//                     id: data.id
//                 }
//                 this.changeId = data.id;
//                 this.problemName = data.problemName ; // 问题名称
// 		        this.problemType = data.problemType ; // 问题类型
// 		        this.equipmentId = data.equipmentId ; // 问题设备信息
// 		        this.problemDescription = data.problemDescription ; // 问题描述
// //              this.reset();
//             }
        },
        //提交新增/修改信息
        addSubmit() {
            let data;
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                	problemName : this.problemName , // 问题名称
			        problemType : this.problemType , // 问题类型
			        equipmentId : this.equipmentId , // 问题设备信息
			        problemDescription : this.problemDescription , // 问题描述
                }
                updateProReport(data, this);
            } else {
                data = {
                    problemName : this.problemName , // 问题名称
			        problemType : this.problemType , // 问题类型
			        equipmentId : this.equipmentId , // 问题设备信息
			        problemDescription : this.problemDescription , // 问题描述
                    id: this.changeId, //当前修改父级ID
                }
                updateProReport(data, this);
            }
        },
        //新增/修改重置页面内属性
        reset() {
        	this.problemName = '' ; // 问题名称
	        this.problemType = '' ; // 问题类型
	        this.equipmentId = '' ; // 问题设备信息
	        this.problemDecription = '' ; // 问题描述
        },
        onClose() {
            this.visible = false;
        },
        assignWoker(record){ // 分派工单
        	this.workerVisible = true;
        	this.equipmentId = record.equipmentId
        },
        handleOk(){
        	let data = {
        		equipmentId:this.equipmentId,
        		jobType:'1',
        		workTeamId:this.workTeamId
        	}
        	updateAssignWoker(data,this)
        },
        handleCancel(){
        	this.workerVisible = false;
        },
    }
}
//workOrderManagement/workerList/index.vue 页面组件混入了 workOrderManagement/workerList/index.js