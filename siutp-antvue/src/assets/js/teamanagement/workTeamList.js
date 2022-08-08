import WorkTeamModal from '@/views/teamanagement/workTeamModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import{
  URL,
  workTeamEditStatus
} from '@/api/teamanagement/workTeamList.js'
export default {
  name: "WorkTeamList",
  mixins:[JeecgListMixin],
  components: {
    WorkTeamModal
  },
  data () {
    return {
      dataSource:[],
      description: '班组管理',
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
          title: '班组名称',
          align:"center",
          dataIndex: 'teamName'
        },
            {
          title: '班组描述',
          align:"center",
          dataIndex: 'teamDescribe'
        },
            {
          title: '班次名称',
          align:"center",
          dataIndex: 'shiftsName'
        },
            {
          title: '班组人员',
          align:"center",
          dataIndex: 'userNames'
        },
            {
          title: '班组管理员',
          align:"center",
          dataIndex: 'userManageNames'
        },
            {
          title: '班组状态',
          align:"center",
          dataIndex: 'teamStatusName',
          scopedSlots: { customRender: 'switch' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align:"center",
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: URL,
    }
},
computed: {
  importExcelUrl: function(){
    return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
  }
},
methods: {
  //启动停用
  teamStatusCodeChange(data){
    console.log(data);
    let res={
      id:data.id,
      shiftsStatus:data.shiftsStatus?'1':'0'
    }
    workTeamEditStatus(res,this);
  }  
}
}
//teamanagement/workTeamList.vue 页面组件混入了 teamanagement/workTeamList.js