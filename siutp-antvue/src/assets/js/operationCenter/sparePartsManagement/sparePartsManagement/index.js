import {
    getOptSparepartData,
    addOrChangeOptSparepartOneData,
    getQueryNameList,
    addOptStoreBillOut,
    getQueryStoreList,
    getEquipmentModelData,
    getEquipmentSpecsData,
    getEquipmentTypeData,
    optSparepartEditWarnAmount
  } from '@/api/operationCenter-t/sparePartsManagement.js'
  import searchReset from '@/mixins/searchReset.js'
  //出库单
  import sparePartsWarehouseOut from '@/views/operationCenter/sparePartsManagement/sparePartsWarehouseOutModel/sparePartsWarehouseOut'
  //入库单
  import storageOfSpareParts from '@/views/operationCenter/sparePartsManagement/storageOfSparePartsModel/storageOfSpareParts'
  export default {
      name: "kucunguanli",
      mixins:[searchReset],
      components: {
        sparePartsWarehouseOut,
        storageOfSpareParts
      },
      data() {
        return {
          description: '库存管理',
          labelCol: {
            xs: {
                span: 24
            },
            sm: {
                span: 5
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
          drawerTitle:'新增库存',// 新增或修改弹框title
          drawerTitle2:'出库',// 新增或修改弹框title
          queryParam: {
            supplier:'0'
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
          //  {
          //   title: '编码',
          //   align: "center",
          //   width: 100,
          //   dataIndex: 'sparepartSn',
            
          // },      
            {
              title: '备品备件',
              align: "center",
  
              dataIndex: 'sparepartName',
              
            },
            {
              title: '型号',
              align: "center",
  
              dataIndex: 'sparepartModel',
              
            },
            {
              title: '规格',
              align: "center",
  
              dataIndex: 'sparepartSpecs',
              
            },
  
            
            {
              title: '所在仓库',
              align: "center",
  
              dataIndex: 'storeName',
              
            },
            {
              title: '库存预警阈值',
              align: "center",
  
              dataIndex: 'warnAmount',
              
            },
            {
              title: '库存量',
              align: "center",
  
              dataIndex: 'amount',
              
            },
            {
              title: '供应商',
              align: "center",
  
              dataIndex: 'supplierName',
              
            },
           
            {
              title: '操作',
              align: "center",
              width: 150,
              fixed: 'right',
              dataIndex: 'maintain',
              scopedSlots: { customRender: 'maintainBtn' },
            },
            
           
          ],//表格头部属性信息
          dataSource:[
              
              
          ],//表格数据源
          loading:false,
          /* 分页参数 */
          ipagination:{
              current: 1,
              pageSize: 10,
              pageSizeOptions: ['10', '20', '30'],
              showTotal: (total, range) => {
                return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
              },
              showQuickJumper: true,
              // showSizeChanger: true,
              total: 0
          },
          selectionRows:[],//表格复选框值
          selectedRowKeys:[],//表格复选框值
          form: this.$form.createForm(this),
          visible: false,//新增或修改弹框显示隐藏值
          visible2: false,//新增或修改弹框显示隐藏值
          changeId:'',
          supplierClassificationList:[],
          sparepartName:'',
          sparepartSn:'',
          sparepartSpecs:'',
          supplierId:'',
          warnAmount:'',
          sparePartList:[],
          queryStoreList:[],
          batchList:[],
          batchSn:'',
          amount:'',
          storeId:'',
          sparepartId:'',
          changeId2:'',
          stockOutvisible:false,
          stockOutId:'',
          stockOutCKID:'',
          stockOutPC:'',
          amountR:'',
          productDate:'',
          endDate:'',
          sparePartList:[],
          stockEarlyWarningvisible:false,
          stockEarlyWarningNum:'',
          inequipmentTypeList:[],
          equipmentTypeName:'',
          modelTypeList:[],
          equipmentModel:'',
          equipmentModelList:[],
          equipmentSpecs:'',
          equipmentSpecsList:[],
          equipmentId:''
        }
      },
      computed: {
        
      },
      mounted(){
        //数据初始化
        this.updata();
        //新增修改-获取供应商下拉值
        getQueryNameList(this);
       
        //新增修改-获取仓库名称下拉值
        getQueryStoreList(this);
    
        //获取备品备件下拉值
        getEquipmentTypeData(this);
      },
      methods: {
        //数据初始化
        updata(){
          let data={
            pageNo:this.ipagination.current,
            pageSize:this.ipagination.pageSize,
            sparepartName:$.trim(this.queryParam.sparepartName),
            sparepartModel:$.trim(this.queryParam.sparepartModel),
            sparepartSpecs:$.trim(this.queryParam.sparepartSpecs),
            storeId:this.queryParam.storeId
          }
          this.loading=true;
          getOptSparepartData(data,this);
        },
        //查询
        searchQuery(){
          this.ipagination.current=1;
          this.updata();
        },
        //新增
        handleAdd(type,data){
          this.reset();
          if(type=='add'){
            this.drawerTitle='新增库存';
          }else if(type=='change'){
            this.drawerTitle='修改库存';
            this.changeId=data.id;
            this.changeSpareParts(data)
          }
          this.visible = true;
          
        },
        //点击修改带入数据
        changeSpareParts(data){
          this.sparepartSn=data.sparepartSn;
          this.sparepartName=data.sparepartName;
          this.sparepartSpecs=data.sparepartSpecs;
          this.supplierId=data.supplierId;
          this.warnAmount=data.warnAmount;
        },
        
        //新增或修改提交
        optSparepartOne(){
          
          if(!this.equipmentTypeName){
            this.$message.info('备件名称不能为空!');
            return;
          }
          if(!this.equipmentModel){
            this.$message.info('型号不能为空!');
            return;
          }
          if(!this.equipmentSpecs){
            this.$message.info('规格不能为空!');
            return;
          }
          if(!this.supplierId){
            this.$message.info('供应商不能为空!');
            return;
          }
          if(!this.amountR){
            this.$message.info('入库量不能为空!');
            return;
          }
          if(!this.storeId){
            this.$message.info('仓库名称不能为空!');
            return;
          }
          let data;
          if(this.drawerTitle.indexOf('新增')!=-1){
            data={
              sparepartName:this.equipmentTypeName,
              sparepartModel:this.equipmentModel,
              sparepartSpecs:this.equipmentSpecs,
              supplierId:this.supplierId,
              amount:$.trim(this.amountR),
              storeId:this.storeId,
              productDate:this.productDate?this.moment(this.productDate).format('YYYY-MM-DD') : '',
              endDate:this.endDate?this.moment(this.endDate).format('YYYY-MM-DD') : '',
              batchSn:$.trim(this.batchSn)
            }
          }else{
            data={
              id:this.changeId,
              sparepartName:$.trim(this.sparepartName),
              sparepartSn:$.trim(this.sparepartSn),
              sparepartSpecs:$.trim(this.sparepartSpecs),
              supplierId:$.trim(this.supplierId),
              warnAmount:$.trim(this.warnAmount),
            }
          }
          addOrChangeOptSparepartOneData(data,this);
        },
        //重置-新增或修改信息
        reset(){
          this.equipmentTypeName='';
          this.equipmentModel='';
          this.equipmentSpecs='';
          this.supplierId='';
          this.amountR='';
          this.storeId='';
          this.productDate='';
          this.endDate='';
          this.batchSn='';
        },
        
  
        //表格属性改变
        handleTableChange(pagination, filters, sorter){
          this.ipagination.current = pagination.current;
          this.updata();
        },
        //关闭新增修改弹框
        onClose() {
          this.visible = false;
          
        },
        
        // 出库
        handleAdd2(data){
          this.amount='';
          this.stockOutId = data.id;
          this.stockOutCKID=data.storeId;
          this.stockOutPC=data.batchSn;
          this.stockOutvisible = true;
            
        },
        //出库弹框确定
        stockOutOk(){
          let data={
            amount:$.trim(this.amount),
            sparepartId:this.stockOutId,
            storeId:this.stockOutCKID,
  
          }
          addOptStoreBillOut(data,this);
          
        },
        //出库弹框关闭
        stockOutCancel(){
          this.stockOutvisible = false;
        },
        // 库存预警按钮
        stockEarlyWarning(record){
          this.stockEarlyWarningNum='';
          this.equipmentId=record.id;
          this.stockEarlyWarningvisible=true;
        },
        //库存预警弹框确定
        stockEarlyWarningtOk(){
          let data={
            id:this.equipmentId,
            warnAmount:$.trim(this.stockEarlyWarningNum)
          }
          optSparepartEditWarnAmount(data,this);
        },
        //库存预警弹框取消
        stockEarlyWarningCancel(){
          this.stockEarlyWarningvisible=false;
        },
        //出库单按钮
        outboundOrder(){
          this.$refs.modelOut.sparePartsWarehouseOutvisible=true;
          this.$refs.modelOut.updata();
        },
        //入库单按钮
        warehouseReceipt(){
          this.$refs.modelR.warehouseReceiptvisible=true;
          this.$refs.modelR.updata();
        },
        //资源名称改变时
        changeEquipmentTypeName(data){
          console.log(data);
          let res={
              pcate: data
          }
          getEquipmentModelData(res,this);
        },
        //资源类型改变时
        changeEquipmentModel(data){
          console.log(data)
          let res = {
              pcate: data
          }
          getEquipmentSpecsData(res, this);
        },  
      }
  
    }
 //operationCenter/sparePartsManagement/sparePartsManagement/index.vue 
 //页面组件混入了operationCenter/sparePartsManagement/sparePartsManagement/index.js