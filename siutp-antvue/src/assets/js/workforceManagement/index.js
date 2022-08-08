import {
    getTeamDutyTeamList,
    getTeamDutyQueryShifts,
    teamDutyOne,
    workTeamDutyList,
    teamDutyOneDay,
    teamDutyDel
} from '@/api/workforceManagement/index.js'
import { downFile } from '@/api/manage'
export default{
    name:'workforceManagement',
    data(){
        return {
            description: '班组排班',
            name: '班组排班',
            form: this.$form.createForm(this),
            labelCol: {
            xs: { span: 24 },
            sm: { span: 5 },
            },
            wrapperCol: {
            xs: { span: 24 },
            sm: { span: 16 },
            },
            visible:false,
            confirmLoading:false,
            teamInformationList:[],
            teamInformation:'',
            shiftsName:'',
            shiftsNameId:'',
            dateString:[],
            listData:[
                {
                    list:[
                        {
                            teamName:'班组名称1',
                            shiftsName:'班次名称1',
                        },
                        {
                            teamName:'班组名称1',
                            shiftsName:'班次名称1',
                        },
                        {
                            teamName:'班组名称1',
                            shiftsName:'班次名称1',
                        },
                    ],
                    dutyDay:8
                },
                {
                    list:[
                        {
                            teamName:'班组名称1',
                            shiftsName:'班次名称1',
                        },
                        {
                            teamName:'班组名称1',
                            shiftsName:'班次名称1',
                        },
                        {
                            teamName:'班组名称1',
                            shiftsName:'班次名称1',
                        },
                    ],
                    dutyDay:15
                },
            ],
            ecuvisible2:false,
            schedulingDetailsList:[],
            selectDate:''
        }
    },
    mounted(){
        //班组管理-班组排班管理-班组信息获取
        getTeamDutyTeamList(this);
        //数据初始化
        this.updata(this.moment(new Date()).format('YYYY-MM'),'1');
    },
    methods: {
        //获取列表数据
        updata(dutyDay,type){
            let data={
                dutyDay:dutyDay,
                type:type
            }
            workTeamDutyList(data,this);
            
        },
        onPanelChange(date,mode){
            console.log(this.moment(date).format('YYYY-MM'));
            this.selectDate=this.moment(date).format('YYYY-MM');
            this.updata(this.selectDate,'1');
        },
        onSelect(date,mode){
            console.log(this.moment(date).format('YYYY-MM-DD'));
            let data={
                dutyDay:this.moment(date).format('YYYY-MM-DD')
            }
            teamDutyOneDay(data,this);
            this.updata(this.moment(date).format('YYYY-MM'),'1');
            
        },
        dateChange(date,mode){
            console.log(date);
        },
        //新增班组排班信息
        addTeamScheduling(){
            this.visible=true;
            this.teamInformation='';
            this.dateString=[];
            this.shiftsNameId='';
            this.shiftsName ='';
        },
        //班组排班信息新增确定
        handleOk(){
            if(!this.teamInformation){
                this.$message.info('班组信息不能为空!');
                return;
            }
            if(!this.shiftsNameId){
                this.$message.info('班次名称不能为空!');
                return;
            }
            if(!this.dateString.length){
                this.$message.info('排班日期不能为空!');
                return;
            }
            let startTime=this.moment(this.dateString[0]).format('YYYY-MM-DD');
            let endTime=this.moment(this.dateString[1]).format('YYYY-MM-DD');
            let data={
                dateString:new Date(startTime).getTime()==new Date(endTime).getTime()?startTime:startTime+','+endTime,
                shfitsId:this.shiftsNameId,
                teamId:this.teamInformation
            }
            console.log(data)
            teamDutyOne(data,this);
        },
        //班组排班信息新增取消
        handleCancel(){
            this.visible=false;
        },
        //班组信息选择获取班次信息
        teamInformationChange(data){
            let res={
                teamId:data
            }
            getTeamDutyQueryShifts(res,this);
        },
        //当日班组排班详情关闭
        handleCancel2(){
            this.ecuvisible2=false;
        },
        //当日排班详情删除
        removeSchedulingDetails(id){
            console.log(id)
            let that=this;
            this.$confirm({
                title: '确定要删除班组排班吗?',
                content: '',
                onOk() {
                    let data={
                        id:id
                    }
                    teamDutyDel(data,that);
                },
                onCancel() {},
              });
            
        },
        //导出
        handleExportXls(fileName){
            if(!fileName || typeof fileName != "string"){
              fileName = "导出文件"
            }
            let res={
                dutyDay:this.selectDate?this.selectDate:this.moment(new Date()).format('YYYY-MM')
            }
            downFile('/work/teamDuty/export',res).then((data)=>{
              if (!data) {
                this.$message.warning("文件下载失败")
                return
              }
              if (typeof window.navigator.msSaveBlob !== 'undefined') {
                window.navigator.msSaveBlob(new Blob([data]), fileName+'.xls')
              }else{
                let url = window.URL.createObjectURL(new Blob([data]))
                let link = document.createElement('a')
                link.style.display = 'none'
                link.href = url
                link.setAttribute('download', fileName+'.xls')
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link); //下载完成移除元素
                window.URL.revokeObjectURL(url); //释放掉blob对象
              }
            })
        },
    },
}
//workforceManagement/index.vue 页面组件混入了 workforceManagement/index.js