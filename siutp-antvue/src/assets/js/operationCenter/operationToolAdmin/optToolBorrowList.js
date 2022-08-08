  
  import {
    getOptGoodsApplyApplyHistoryAll,
    getOptApplyVerifyQueryAuditStatus
  } from '@/api/operationCenter-t/operationToolAdmin.js'
  import '@/assets/less/operationCenter/operationToolAdmin/optToolBorrowList.less'
  import searchReset from '@/mixins/searchReset.js'
  export default {
    name: "optToolBorrowList",
    mixins:[searchReset],

    data () {
      return {
        description: '审核历史组件',
        optToolBorrowListvisible:false,
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
        description: '申请历史',
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
          {
            toolcode:'',
            toolname:'',
            toolgg:'',
            toolzk:'',
            toolszck:'',
            toolnum:'',
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
        queryParam:{},
        ecuvisible:false,
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
      //获取状态下拉值
      getOptApplyVerifyQueryAuditStatus(this);
    },
    methods: {
      //获取数据
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
        getOptGoodsApplyApplyHistoryAll(data,this);
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
      //关闭审核历史组件
      optToolBorrowListonClose(){
        this.optToolBorrowListvisible=false;
        this.searchReset();
      }  
    }
  }
  //operationCenter/operationToolAdmin/optToolBorrowList.vue 页面组件混入了operationCenter/operationToolAdmin/optToolBorrowList.js