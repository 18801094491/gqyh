import {
    optGoodsApplyOne,
    getQueryStoreList,
    operatToolQueryBystoreId,
    optGoodsApplyQueryByStatus,
    optGoodsApplyQueryByapplyId,
    optGoodsApplyQueryModel
} from '@/api/operationCenter-t/toolApply.js'
export default{
    name:'toolApply',
    data(){
        return{
            description: '工具申请组件',
            toolApplicativisible:false,
            name:'工具申请',
            applicationContentList:[
                {
                    amount:'',
                    dataId:'',
                    storeId:'',
                    dataNameCode:'',
                    toolModelCode:'',
                    operatToolList:[],
                    toolModelList:[]
                },
               
            ],
            queryStoreList:[],
            operatToolList:[],
            emergentLevelList:[],
            emergentLevel:'',
            expectbackTime:'',
            wantedTime:'',
            reason:'',
            dateFormat: '',
            form: this.$form.createForm(this),
            toolApplicationId:''
        }
    },
    mounted(){
        //仓库下拉值获取
        getQueryStoreList(this);
        //紧急程度下拉值获取
        optGoodsApplyQueryByStatus(this);

    },
    methods:{
        //数据初始化
        updata(id){
            let data={
                applyId:id
            }
            this.toolApplicationId=id;
            optGoodsApplyQueryByapplyId(data,this);
        },
        //增加工具基本信息
        addApplicationContent(){
            this.applicationContentList.push({
                amount:'',
                dataId:'',
                storeId:'',
                dataNameCode:'',
                toolModelCode:'',
                operatToolList:[],
                toolModelList:[]
            });
        },
        //删除工具基本信息
        removeApplicationContent(index){
            console.log(index);
            this.applicationContentList.splice(index,1);
        },
        //仓库改变
        queryStoreListChange(index,data){
            this.applicationContentList[index].dataNameCode='';
            let res={
                storeId:data
            }
            operatToolQueryBystoreId(res,this,index);
        },
        //通过仓库查询工具信息
        queryStoreListChange2(index,data){
            let res={
                storeId:data
            }
            operatToolQueryBystoreId(res,this,index);
        },
        //通过工具信息查询规格
        operatToolListChange(index,data){
            console.log(index)
            this.applicationContentList[index].toolModelCode='';
            let res={
                code:data,
                storeId:this.applicationContentList[index].storeId
            }
            optGoodsApplyQueryModel(res,this,index);
        },
        //通过工具信息查询规格
        operatToolListChange2(index,data,data2){
            let res={
                code:data,
                storeId:data2
            }
            optGoodsApplyQueryModel(res,this,index);
        },
        //选择规格效果
        changeToolModelCode(index,data){
            this.applicationContentList[index].dataId=data;
        },
        //提交工具申请
        tjOptGoodsApplyOne(){
            let data;
            for(let i=0;i<this.applicationContentList.length;i++){
                if(this.applicationContentList[i].amount==''){
                    this.$message.info('申请数量不能为空!');
                    return;
                }
                if(this.applicationContentList[i].dataNameCode==''){
                    this.$message.info('工具信息不能为空!');
                    return;
                }
                if(this.applicationContentList[i].storeId==''){
                    this.$message.info('仓库不能为空!');
                    return;
                }
                if(this.applicationContentList[i].toolModelCode==''){
                    this.$message.info('规格不能为空!');
                    return;
                }
            }
            this.form.validateFields((err, values) => {
                if (!err) {
                    
                    
                    if(values.wantedTime==undefined){
                        this.$message.info('期望领取时间不能为空!');
                        return;
                    }
                    if(values.expectbackTime==undefined){
                        this.$message.info('预计归还时间不能为空!');
                        return;
                    }
                    let wantedTime=this.moment(values.wantedTime).format('YYYY-MM-DD hh:mm');
                    let expectbackTime=this.moment(values.expectbackTime).format('YYYY-MM-DD hh:mm');
                    if(new Date(expectbackTime).getTime()<new Date(wantedTime).getTime()){
                        this.$message.info('归还时间不能小于领取时间!');
                        return;
                    }
                    if(this.emergentLevel==''){
                        this.$message.info('紧急程度不能为空!');
                        return;
                    }
                    
                    if(this.reason==''){
                        this.$message.info('备注不能为空!');
                        return;
                    }
                    
                    this.applicationContentList.map(index=>{
                        delete index.operatToolList;
                        delete index.toolModelList;
                    })
                    if(this.toolApplicationId){
                        data={
                            applyItem:this.applicationContentList,
                            emergentLevel:this.emergentLevel,
                            expectbackTime:expectbackTime,
                            reason:this.reason,
                            wantedTime:wantedTime,
                            id:this.toolApplicationId
                        }
                    }else{
                        data={
                            applyItem:this.applicationContentList,
                            emergentLevel:this.emergentLevel,
                            expectbackTime:expectbackTime,
                            reason:this.reason,
                            wantedTime:wantedTime,
                        }
                    }
                    console.log(data);
                    optGoodsApplyOne(data,this);
                }
              })
            
        },
        //重置
        react(){
            this.applicationContentList=[{
                amount:'',
                dataId:'',
                storeId:'',
                dataNameCode:'',
                toolModelCode:'',
                operatToolList:[],
                toolModelList:[]
            }];
            this.emergentLevel='';
            this.expectbackTime='';
            this.wantedTime='';
            this.form.setFieldsValue({wantedTime:null});
            this.form.setFieldsValue({expectbackTime:null});
            this.reason='';
        },
        //组件关闭
        toolApplicationonClose(){
            this.toolApplicativisible=false;
            this.react();
        }
    }
}
//operationCenter/operationToolAdmin/optGoodsApplyList.js 里面混入了operationCenter/toolApply.js