import {
    addSupplierInformationData,
    getSupplierInformationListData,
    changeSupplierInformationData,
    getEquipmentCategoryData,
    getSupplierClassificationData,
    getEquipmentTypeData,
    supplierStatusChange,
    getSupplierInformationData
  } from '@/api/operationCenter-t/supplierManagement.js'
  import searchReset from '@/mixins/searchReset.js'
  export default {
    name: "equipmentAccountMaintenance",
    mixins:[searchReset],
    components: {
    },
    data() {
      return {
        description: '供应商管理',
        drawerTitle:'新增信息',//新增或修改弹框title
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
            
            {
                title: '供应商编码',
                align: "center",

                dataIndex: 'supplierCode',
               
            },
            {
                title: '供应商名称',
                align: "center",

                dataIndex: 'supplierName',
               
            },

            {
                title: '供应商类别',
                align: "center",

                dataIndex: 'supplierCategory',
                
            },
            {
                title: '供应商营业执照编号',
                align: "center",

                dataIndex: 'supplierBusinessLicenseNo',
                
            },
            {
                title: '建档日期',
                align: "center",

                dataIndex: 'createdDate',
                
            },
            {
                title: '供应设备类别',
                align: "center",

                dataIndex: 'supplyEquipment',
                
            },
            {
                title: '联系人',
                align: "center",

                dataIndex: 'contacts',
                
            },
            {
                title: '联系电话',
                align: "center",

                dataIndex: 'contactNumber',
                
            },
            {
                title: '供应商状态',
                align: "center",

                dataIndex: 'supplierStatus',
                scopedSlots: { customRender: 'supplierStatus' },
            },    
            {
                title: '操作',
                align: "center",
                fixed: 'right',
                width: 100,
                dataIndex: 'maintain',
                scopedSlots: { customRender: 'maintainBtn' },
            },
          
         
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
        equipmentCategoryList:[],//供应设备数组集合
        supplierClassificationList:[],//供应商类别数组集合
        supplierCode:'',//新增修改弹框供应商编码
        supplierName:'',//新增修改弹框供应商名称
        supplierCategory:'',//新增修改弹框供应商类别
        supplierBusinessLicenseNo:'',//新增修改弹框供应商营业执照编号
        supplyEquipment:'',//新增修改弹框供应设备
        contactNumber:'',//新增修改弹框联系电话
        supplierStatus:'',//新增修改弹框供应商状态
        contacts:'',//新增修改弹框联系人
        changeId:'',//修改ID
      }
    },
    computed: {
      
    },
    mounted(){
      //初始化页面加载列表信息
      this.updata();
      //获取设备类别值
      getEquipmentCategoryData(this);
      //获取供应商分类值
      getSupplierClassificationData(this);
      //获取设备类型值
      getEquipmentTypeData(this);
    },
    methods: {
        //获取列表信息
        updata(){
            let data={
                supplierSn:this.queryParam.code?this.queryParam.code:'',
                supplierName:this.queryParam.name?this.queryParam.name:'',
                startTime:this.queryParam.startTime?this.moment(this.queryParam.startTime).format('YYYY-MM-DD'):'',
                endTime:this.queryParam.endTime?this.moment(this.queryParam.endTime).format('YYYY-MM-DD'):'',
                pageSize:this.ipagination.pageSize,
                pageNo:this.ipagination.current
            }
            
            this.loading=true;
            getSupplierInformationListData(data,this);
            console.log(this.ipagination)
        },
        //查询
        searchQuery(){
            this.ipagination.current=1;
            this.updata();
        },
        //新增or修改
        handleAdd(type,data){
          console.log(type);
          console.log(data);

          if(type=='add'){
            this.drawerTitle='新增信息';
          }else if(type=='change'){
            this.drawerTitle='修改信息';
            this.getChangeData(data);
            this.changeId=data.id;
          }
          this.visible = true;
          
        },
        //修改时获取信息
        getChangeData(data){
          let res={
            id:data.id
          }
          getSupplierInformationData(res,this);  
        },
        //表格属性改变
        handleTableChange(pagination){
            this.ipagination.current=pagination.current;
            this.updata();   
        },
        //关闭新增或修改右侧弹框
        onClose() {
          this.visible = false;
          //重置弹框里的属性
          this.addReset();
        },
        //重置弹框里的属性
        addReset(){
          this.supplierCode='';
          this.supplierName='';
          this.supplierCategory='';
          this.supplierBusinessLicenseNo='';
          this.supplyEquipment='';
          this.contacts='';
          this.contactNumber='';
          this.supplierStatus='';
        },
        //新增修改提交
        onSubmit(){
            let type;
            if(this.drawerTitle.indexOf('新增')!=-1){
                type='add';
            }else{
                type='modify';
            }
            if(!this.supplierCode){
              this.$message.info('供应商编码不能为空!');
              return;
            }
            if(!this.supplierName){
              this.$message.info('供应商名称不能为空!');
              return;
            }
            if(!this.supplierCategory){
              this.$message.info('供应商类别不能为空!');
              return;
            }
            if(!this.supplierStatus){
              this.$message.info('供应商状态不能为空!');
              return;
            }
            this.ipagination.current=1;
            let data;
            if(type=='add'){
              data={
                supplierSn:this.supplierCode,
                supplierName:this.supplierName,
                supplierGoods:this.supplyEquipment,
                supplierCert:this.supplierBusinessLicenseNo,
                supplierCategory:this.supplierCategory,
                contactsPhone:this.contactNumber,
                contacts:this.contacts,
                supplierStatus:this.supplierStatus,
              }
              //新增提交
              addSupplierInformationData(data,this);
            }else{
              data={
                supplierSn:this.supplierCode,
                supplierName:this.supplierName,
                supplierGoods:this.supplyEquipment,
                supplierCert:this.supplierBusinessLicenseNo,
                supplierCategory:this.supplierCategory,
                contactsPhone:this.contactNumber,
                contacts:this.contacts,
                supplierStatus:this.supplierStatus,
                id:this.changeId
              }
              //修改提交
              changeSupplierInformationData(data,this);
            }
        },
        //启停按钮改变
        supplierStatusChange(data){
          console.log(data);
          let res={
            id:data.id
          }
          //启停提交
          supplierStatusChange(res,this);
        },
    }

  }
   //operationCenter/sparePartsManagement/supplierManagement/supplierManagement.vue 
  //页面组件混入了operationCenter/sparePartsManagement/supplierManagement/supplierManagement.js
