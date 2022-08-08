import {
  getOptStoreBillIn,
  getSparePartList,
  getQueryStoreList,
  addOptStoreBill
} from '@/api/operationCenter-t/sparePartsManagement.js'
import searchReset from '@/mixins/searchReset.js'
export default {
    name: "equipmentAccountMaintenance",
    mixins:[searchReset],
    components: {

    },
    data() {
      return {
        description: '入库记录组件',
        drawerTitle:'新增信息',//新增或修改弹框title
        queryParam: {
        },//搜索属性集合
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
                title: '备件名称',
                align: "center",

                dataIndex: 'sparepartName',
               
            },
            {
                title: '规格',
                align: "center",

                dataIndex: 'sparepartSpecs',
               
            },

            {
                title: '批次号',
                align: "center",
                dataIndex: 'batchSn',
                
            },
            {
                title: '所在仓库',
                align: "center",
                dataIndex: 'storeName',
                
            },
            {
                title: '入库量',
                align: "center",
                dataIndex: 'amount',
                
            },
            {
                title: '入库日期',
                align: "center",
                width: 150,
                dataIndex: 'createTime',
                
            },
            {
              title: '生产日期',
              align: "center",
              width: 150,
              dataIndex: 'productDate',
              
            },
            {
              title: '有效期',
              align: "center",
              width: 150,
              dataIndex: 'endDate',
              
            },
            {
                title: '经办人',
                align: "center",
                
                dataIndex: 'createBy',
                
            },
               
            // {
            //     title: '操作',
            //     align: "center",
            //     dataIndex: 'maintain',
            //     scopedSlots: { customRender: 'maintainBtn' },
            // },
          
         
        ],//表格头部属性信息
        dataSource:[
            {
                supplierCode:'001',
                supplierName:'离心泵',
                supplierCategory:'机械设备',
                supplierBusinessLicenseNo:'泵',
                createdDate:'xxx',
                supplyEquipment:'1标段',
                contacts:'xx路泵房',
                contactNumber:'XX公司'
            },
            
        ],//表格数据源
        loading:false,
        /* 分页参数 */
        ipagination:{
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
        form: this.$form.createForm(this),
        visible: false,//新增修改弹框显示隐藏值
        sparePartList:[],
        queryStoreList:[],
        storeId:'',
        sparepartId:'',
        batchSn:'',
        amount:'',
        endDate:'',
        productDate:'',
        warehouseReceiptvisible:false
      }
    },
    computed: {
      
    },
    mounted(){
      //初始化页面加载列表信息
      // this.updata();
      getSparePartList(this);
      getQueryStoreList(this);
    },
    methods: {
        //获取列表信息
        updata(){

            let data={
              batchSn:$.trim(this.queryParam.batchSn),
              sparepartName:$.trim(this.queryParam.sparepartName),
              pageNo:this.ipagination.current,
              pageSize:this.ipagination.pageSize
            }
            this.loading = true;
            getOptStoreBillIn(data,this)
        },
        //查询
        searchQuery(){
            this.ipagination.current=1;
            this.updata();
        },
        //新增or修改
        handleAdd(){
          this.addReset();
          this.visible = true;
          
        },
        //表格属性改变
        handleTableChange(pagination, filters, sorter){
            
          this.ipagination.current = pagination.current;
          this.updata();  
        },
        //关闭新增或修改右侧弹框
        onClose() {
          this.visible = false;
          //重置弹框里的属性
          
        },
        //重置弹框里的属性
        addReset(){
          this.amount='';
          this.batchSn='';
          this.sparepartId='';
          this.storeId='';
        },
        //新增修改提交
        onSubmit(){
          if(!this.sparepartId){
            this.$message.info('备件信息不能为空!');
            return;
          }
          if(!this.batchSn){
            this.$message.info('批次号不能为空!');
            return;
          }
          if(!this.amount){
            this.$message.info('入库量不能为空!');
            return;
          }
          if(!this.storeId){
            this.$message.info('仓库名称不能为空!');
            return;
          }
          if(new Date(this.productDate).getTime()>new Date(this.endDate).getTime()){
            this.$message.info('生产日期不能大于有效期');
            return;
          }
          let data={
            amount:$.trim(this.amount),
            batchSn:$.trim(this.batchSn),
            sparepartId:this.sparepartId,
            storeId:this.storeId,
            endDate:this.endDate?this.moment(this.endDate).format('YYYY-MM-DD') : '',
            productDate:this.productDate?this.moment(this.productDate).format('YYYY-MM-DD') : '',
          }
          addOptStoreBill(data,this);
        },
        changeID(data){
          console.log(data);
        },
        warehouseReceiptonClose(){
          this.warehouseReceiptvisible=false;
        }
        
    }

  }
  //operationCenter/sparePartsManagement/storageOfSparePartsModel/index.vue 
  //页面组件混入了operationCenter/sparePartsManagement/storageOfSparePartsModel/index.js