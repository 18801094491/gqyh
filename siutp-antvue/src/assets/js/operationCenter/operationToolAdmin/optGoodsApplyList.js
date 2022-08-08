import {
    getMyOptGoodsApply,
    getOptApplyVerifyQueryAuditStatus,
    getOptApplyVerify
  } from '@/api/operationCenter-t/operationToolAdmin.js'
  import '@/assets/less/operationCenter/operationToolAdmin/optGoodsApplyList.less'
  import searchReset from '@/mixins/searchReset.js'
  import toolApplicationT from '@/views/toolApply/v1/index.vue'
  export default {
    name: "optGoodsApplyList",
    mixins:[searchReset],
    components: {
      toolApplicationT
    },
    data () {
      return {
        optApplyVerifyList:[],
        description: '运维工具-我的申请',
        queryParam:{},
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
		      {
            title: '申请时间',
            align:"center",
            dataIndex: 'createTime'
          },
		      {
            title: '借用工具',
            align:"center",
            dataIndex: 'useTool'
          },
		      {
            title: '期望领用时间',
            align:"center",
            dataIndex: 'wantedTime'
           },
		   {
            title: '预计归还时间',
            align:"center",
            dataIndex: 'expectbackTime'
           },
            {
            title: '状态',
            align:"center",
            dataIndex: 'verifyStatusValue'
           },
           {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        dataSource:[
         
        ],
        loading: false,
        /* 分页参数 */
        ipagination: {
            current: 1,
            pageSize: 10,
            // pageSizeOptions: ['10', '20', '30'],
            showTotal: (total, range) => {
              return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
            },
            showQuickJumper: true,
            // showSizeChanger: true,
            total: 0
        },
        ecuvisible:false,
        ecuvisible2:false,
        labelCol: {
            xs: {
                span: 24
            },
            sm: {
                span: 6
            },
        },
        wrapperCol: {
            xs: {
                span: 24
            },
            sm: {
                span: 16
            },
        },
        queryAuditStatusList:[],
        queryAuditStatus:'',
        createBy:'',
        createTime:'',
        expectbackTime:'',
        reason:'',
        useToolList:[],
        verifyStatus:'',
        verifyStatusValue:'',
        wantedTime:''
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  mounted(){
    //数据初始化
    this.updata();
    //状态下拉值获取
    getOptApplyVerifyQueryAuditStatus(this);
  },
  methods: {
    //数据初始化
    updata(){
      let data={
        pageNo:this.ipagination.current,
        pageSize:this.ipagination.pageSize,
        verifyStatus:this.queryParam.queryAuditStatus,
        startTime:this.queryParam.startTime?this.moment(this.queryParam.startTime).format('YYYY-MM-DD') : '',
        endTime:this.queryParam.endTime?this.moment(this.queryParam.endTime).format('YYYY-MM-DD') : '',
      }
      this.loading=true;
      getMyOptGoodsApply(data,this);
    },
    //表格属性改变
    handleTableChange(pagination) {

        this.ipagination.current = pagination.current;
        this.updata();
    },
    //查询
    searchQuery(){
      this.ipagination.current=1;
      this.updata();
    },
    //点击详情
    details(data){
      this.ecuvisible=true;
      this.createBy=data.createBy;
      this.createTime=data.createTime;
      this.expectbackTime=data.expectbackTime;
      this.reason=data.reason;
      this.useToolList=data.useTool.split(',');
      this.verifyStatus=data.verifyStatus;
      this.wantedTime=data.wantedTime;
    },
    //关闭详情
    handleCancel(){
      this.ecuvisible=false;
    },
    //审核历史按钮
    examineHistory(data){
      
      getOptApplyVerify({id:data.id},this);
    },
    //审核历史弹框关闭
    handleCancel2(){
      this.ecuvisible2=false;
    },
    //新增修改按钮触发弹框
    toolApplication(type,data){
      if(type!='add'){
        this.$refs.modalForm.updata(data.id);
      }else{
        this.$refs.modalForm.toolApplicativisible=true;
      }
      
    }
  }
  }
 //operationCenter/operationToolAdmin/optGoodsApplyList.vue 页面组件混入了operationCenter/operationToolAdmin/optGoodsApplyList.js