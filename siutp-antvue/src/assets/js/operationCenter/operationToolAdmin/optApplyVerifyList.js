import {
    getOptGoodsApplyMyInfo,
    getOptApplyVerifyQueryAuditStatus,
    addOptApplyVerifyOne,
    getOptApplyVerify
  } from '@/api/operationCenter-t/operationToolAdmin.js'
  import searchReset from '@/mixins/searchReset.js'
  import '@/assets/less/operationCenter/operationToolAdmin/optApplyVerifyList.less'

  export default {
    name: "optApplyVerifyList",
    mixins:[searchReset],

    data () {
      return {
        description: '申请审核组件',
        optApplyVerifyListvisible:false,
        optApplyVerifyList:[],
        labelCol: {
            xs: {
                span: 24
            },
            sm: {
                span: 3
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
        description: '审核申请',
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
            title: '姓名',
            align:"center",
            dataIndex: 'createBy'
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
            title: '紧急程度',
            align:"center",
            dataIndex: 'emergenText'
          },
          {
            title: '期望领用时间',
            align:"center",
            dataIndex: 'wantedTime'
          },
          {
            title: '状态',
            align:"center",
            dataIndex: 'verifyStatusValue'
          },
          {
            title: '操作',
            align:"center",
            dataIndex: 'applyId',
            scopedSlots: { customRender: 'action' },
          },
        ],
        dataSource:[
          {
            

          }
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
        ecuvisible2:false,
        visible:false,
        queryAuditStatusList:[],
        queryAuditStatus:'',
        createBy:'',
        createTime:'',
        useToolList:[],
        reason:'',
        queryAuditStatus:'',
        verifyDesc:'',
        shId:''
    }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    mounted(){
      //状态下拉值
      getOptApplyVerifyQueryAuditStatus(this);
    },
    methods: {
      //数据初始化
      updata(){
        let data={
          name:$.trim(this.queryParam.name),
          pageNo:this.ipagination.current,
          pageSize:this.ipagination.pageSize,
          verifyStatus:this.queryParam.queryAuditStatus,
          startTime:this.queryParam.startTime?this.moment(this.queryParam.startTime).format('YYYY-MM-DD') : '',
          endTime:this.queryParam.endTime?this.moment(this.queryParam.endTime).format('YYYY-MM-DD') : '',
        }
        this.loading=true;
        getOptGoodsApplyMyInfo(data,this);
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
      //审核历史
      examineHistory(data){
        this.ecuvisible2=true;
        getOptApplyVerify({id:data.id},this);
      },
      //审核历史关闭
      handleCancel2(){
        this.ecuvisible2=false;
      },
      //审核关闭
      onClose(){
        this.visible=false;
      },
      //审核按钮触发弹框
      examine(data){
        this.visible=true;
        this.createBy=data.createBy;
        this.createTime=data.createTime;
        this.reason=data.reason;
        this.useToolList=data.useTool.split(',');
        this.shId=data.id;
        this.verifyDesc='';
        this.queryAuditStatus='';

      },
      //审核提交
      onSubmit(){
        let data={
          applyId:this.shId,
          verifyDesc:$.trim(this.verifyDesc),
          verifyStatus:this.queryAuditStatus
        }  
        addOptApplyVerifyOne(data,this);
      },
      //审核组件弹框关闭
      optApplyVerifyListonClose(){
        this.optApplyVerifyListvisible=false;
        this.searchReset();
      }
    }
  }
  //operationCenter/operationToolAdmin/optApplyVerifyList.vue 页面组件混入了operationCenter/operationToolAdmin/optApplyVerifyList.js