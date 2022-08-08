import OptStoreModal from '@/views/operationCenter/warehouseManagement/modules/OptStoreModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import {
  URL,
  getUserData,
  storeTreeStore,
  storeGetStore,
  storeSetManager
} from '@/api/operationCenter-t/warehouseManagement.js'
export default {
  name: "OptStoreList",
  mixins:[JeecgListMixin],
  components: {
    OptStoreModal
  },
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      description: '仓库管理',
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
          title: '仓库名称',
          align:"center",
          dataIndex: 'storeName'
         },
         {
          title: '仓库编码',
          align:"center",
          dataIndex: 'storeSn'
         },
         {
          title: '仓库地址',
          align:"center",
          dataIndex: 'storePosition'
         },
          {
            title: '管理员',
            align:"center",
            dataIndex: 'manager'
         },
         {
          title: '启停状态',
          align:"center",
          dataIndex: 'storeStatus'
         },
        {
          title: '操作',
          dataIndex: 'action',
          align:"center",
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: URL,
      setUpLibraryvisible:false,
      setUpLibraryconfirmLoading:false,
      managerList:[],
      manager:'',
      treeData:[

      ],
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      replaceFields:{
        title:'slotTitle'
      }

  }
  },
  watch: {
    checkedKeys(val) {
      console.log('onCheck', val);
    },
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  mounted(){
    //设置库管-获取管理员下拉列表
    let data={
      name:''
    }
    getUserData(data,this);
    
  },
  methods: {
    //设置库管按钮
    setUpLibrary(){
      this.setUpLibraryvisible=true;
      this.checkedKeys=[];
      this.manager='';
      storeTreeStore(this);
    },
    //设置库管确定按钮
    setUpLibraryhandleOk(){
      if(!this.manager){
        this.$message.info('管理员不能为空!');
        return;
      }
      if(!this.checkedKeys.length){
        this.$message.info('仓库名称不能为空!');
        return;
      }
      let arr=[];
      for(let i=0;i<this.checkedKeys.length;i++){
        if(this.checkedKeys[i]!=0){
          arr.push(this.checkedKeys[i]);
        }
      }
      

      this.setUpLibraryvisible=false;
      let data={
        storeIds:arr.join(','),
        userId:this.manager
      }
      console.log(data);
      storeSetManager(data,this);
    },
    //设置库管取消按钮
    setUpLibraryhandleCancel(){
      this.setUpLibraryvisible=false;
    },
    //管理员改变获取管理员权限仓库
    managerhandleChange(data){
      console.log(data);
      let res={
        manager:data
      }
      storeGetStore(res,this);
    }, 
    //所有仓库树结构是否展开
    onExpand(expandedKeys) {
      console.log('onExpand', expandedKeys);
      this.expandedKeys = expandedKeys;
      this.autoExpandParent = true;
    },
    //所有仓库树结构是否展开
    onCheck(checkedKeys) {
      console.log('onCheck', checkedKeys);
      this.checkedKeys = checkedKeys;
    },
    //所有仓库树结构下拉值改变
    onSelect(selectedKeys, info) {
      console.log('onSelect', info);
      this.selectedKeys = selectedKeys;
    },
  }
}
 //operationCenter/warehouseManagement/optStoreList.vue 
 //页面组件混入了operationCenter/arehouseManagement/optStoreList.js