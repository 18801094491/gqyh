
import {
    getDevOpsToolLedgerListData,
    editDevOpsToolLedger,
    getToolType,
    getQueryStoreList,
    getQueryNameList,
    operatToolInToolStore,
    getOperatToolBorrowDetailData,
    getStoreUserListData,
    getOperatToolGoodsApply,
    getGoodsItemStatus,
    operatToolEditStatus,
    operatToolBack,
    operatToolGoodsOut,
    getOperatToolHistoryData,
    getEquipmentSpecsData
} from '@/api/operationCenter-t/devOpsToolLedger.js'
import {
    downFile
} from '@/api/manage'
import searchReset from '@/mixins/searchReset.js'
import optApplyVerifyList from '@/views/operationCenter/operationToolAdmin/optApplyVerifyList'
import optToolBorrowList from '@/views/operationCenter/operationToolAdmin/optToolBorrowList'
export default {
    name: "equipmentAccountMaintenance",
    mixins:[searchReset],
    components: {
        optApplyVerifyList,
        optToolBorrowList
    },
    data() {
        return {
            description: '运维工具台账',
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
            drawerTitle: '新增信息',
            current: 1,
            queryParam: {
                supplier: '0'
            }, //搜索属性集合
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

                // {
                //     title: '编号',
                //     align: "center",
                //     width: 100,
                //     dataIndex: 'toolSn',

                // },
                {
                    title: '工具名称',
                    align: "center",
                    width: 180,
                    dataIndex: 'toolName',

                },
                // {
                //     title: '批次号',
                //     align: "center",
                //     width: 200,
                //     dataIndex: 'batchSn',

                // },
                // {
                //     title: '类型',
                //     align: "center",

                //     dataIndex: 'toolType',

                // },
                {
                    title: '规格',
                    align: "center",
                    width: 100,
                    dataIndex: 'toolModel',

                },
                {
                    title: '总量',
                    align: "center",
                    width: 100,
                    dataIndex: 'total',

                },
                // {
                //     title: '生产厂家',
                //     align: "center",
                //     width: 150,
                //     dataIndex: 'toolFactory',

                // },
                {
                    title: '外借数量',
                    align: "center",
                    width: 100,
                    dataIndex: 'borrowedNum',

                },
                {
                    title: '损坏|报废',
                    align: "center",
                    width: 100,
                    dataIndex: 'damageNum',

                },
                {
                    title: '所在仓库',
                    align: "center",
                    width: 100,
                    dataIndex: 'storeName',

                },
                {
                    title: '可用数量',
                    align: "center",
                    width: 100,
                    dataIndex: 'usedNum',

                },
                {
                    title: '操作',
                    width: 150,
                    // fixed: 'right',
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },


            ], //表格头部属性信息
            dataSource: [


            ], //表格数据源
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

            visible: false,
            queryStoreList:[],//存放位置List
            supplierSnList: [], //生产厂家List
            toolTypeList: [], //工具类型List
            changeId: '',
            toolName: '', //工具名称
            toolType: '', //工具类型
            toolModel: '', //规格
            toolNumber: '', //数量
            supplierSn: '', //生产厂家
            borrowingQuantity: '', //借用数量
            wasteNumber: '', //报废/损坏数量
            storeSn: '', //存放位置
            availableQuantity: '', //可用数量
            queryStore:'',
            visible2:false,//入库弹框真价值
            visible3:false,//出库弹框真价值
            columns2:[
                {
                    title: '编码',
                    align: "center",

                    dataIndex: 'itemSn',

                },
                {
                    title: '借用次数',
                    align: "center",

                    dataIndex: 'borrowTimes',

                },
                {
                    title: '状态',
                    align: "center",

                    dataIndex: 'itemStatusName',

                },
                {
                    title: '操作',
                    width: 250,
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },
            ],//出库表格
            dataSource2:[
                
            ],//出库表格数据
            /* 出库分页参数 */
            ipagination2: {
                current: 1,
                pageSize: 5,
                // pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            loading2:false,//出库表格是否load加载
            visible4:false,//借用记录弹框真假值
            columns3:[
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
                    title: '借用人',
                    align: "center",
                    width: 100,
                    dataIndex: 'userName',

                },
                {
                    title: '借用时间',
                    align: "center",
                    
                    dataIndex: 'borrowTime',

                },
                {
                    title: '归还时间',
                    align: "center",

                    dataIndex: 'backTime',

                },
                {
                    title: '备注信息',
                    align: "center",

                    dataIndex: 'description',

                },
            ],//借用记录表格
            dataSource3:[
                
            ],//借用记录表格数据
            /* 借用记录分页参数 */
            ipagination3: {
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
            loading3:false,//借用记录表格是否load加载
            warehousingOrReturnTitle:'出库',//出库、归还title
            warehousingOrReturnvisible:false,//出库、归还真假值
            maintainvisible:false,//维护弹框真假值
            returnTitle:'归还',
            returnvisible:false,
            url: {
                list: "/settle/customer/list",
                delete: "/iot/varinfo/delete",
                deleteBatch: "/iot/varinfo/deleteBatch",
                importExcelUrl: "iot/varinfo/importExcel",
                exportXlsUrl: "/equipment/operatTool/export",

            },
            supplierClassificationList:[],
            supplierId:'',
            toolFactory:'',
            amount:'',
            endDate:'',
            productDate:'',
            batchSn:'',
            storeId:'',
            toolId:'',
            selectedRowKeys: [],
            /* table选中records*/
            selectionRows: [],
            goodsId:'',
            itemSn:'',
            managerList:[],
            applicantName:'',
            applicantId:'',
            applicantIdList:[],
            borrowTime:'',
            goodsItemstatus:'',
            goodsItemstatusList:[],
            description:'',
            id:'',
            startTime:'',
            endTime:'',
            userName:'',
            batchSn2:'',
            equipmentSpecs:'',
            equipmentSpecsList:[]
        }
    },
    computed: {

    },
    mounted() {
        //获取工具名称下拉值
        getToolType(this);
        //获取仓库名称下拉值
        getQueryStoreList(this);
        //获取供应商下拉值
        getQueryNameList(this);
        //出库-申请人姓名下拉值
        let data={
            name:$.trim(this.applicantName)
        }
        getStoreUserListData(data,this);
        //数据初始化
        this.updata();
        //出库-维护-维护状态下拉值
        getGoodsItemStatus(this);
    },
    methods: {
        //获取列表信息
        updata() {
            let data={
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                toolName:this.queryParam.toolType,
                toolModel:$.trim(this.queryParam.toolModel),
                storeId:this.queryParam.storeId
            }
            this.loading=true;
            getDevOpsToolLedgerListData(data,this);
        },
        //查询
        searchQuery() {
            this.updata();
            this.ipagination.current = 1;

        },
        //新增
        handleAdd(type, data) {
            this.addReset();
            if (type == 'add') {
                this.drawerTitle = '新增入库信息';
                console.log(this.toolTypeList)
            } else if (type == 'change') {
                this.drawerTitle = '修改信息';
                let res={
                    id: data.id
                }
                this.toolName=data.toolName;
                this.toolType=data.toolTypeCode;
                this.toolModel=data.toolModel;
                this.supplierId=data.supplierId;
                this.toolFactory=data.toolFactory;

                this.changeId=data.id;
            }
            this.visible = true;

        },
        //表格属性改变
        handleTableChange(pagination, filters, sorter) {

            this.ipagination.current = pagination.current;
            this.updata();

        },
        //关闭新增修改入库弹框
        onClose() {
            this.visible = false;
        },
        //新增修改入库-重置
        addReset() {
            this.toolType='';
            this.equipmentSpecs='';
            this.supplierId='';
            this.toolFactory='';
            this.storeId='';
            this.batchSn='';
            this.productDate='';
            this.endDate='';
            this.amount='';
            this.equipmentSpecsList=[];
        },

        //新增修改提交
        onSubmit() {
            let type;
            if (this.drawerTitle.indexOf('新增') != -1) {
                type = 'add';
            } else {
                type = 'modify';
            }
            
            if (!this.toolType) {
                this.$message.info('工具不能为空!');
                return;
            }
            if (!this.equipmentSpecs) {
                this.$message.info('规格不能为空!');
                return;
            }
            if (!this.supplierId) {
                this.$message.info('供应商不能为空!');
                return;
            }
            if (!this.toolFactory) {
                this.$message.info('生产厂家不能为空!');
                return;
            }
            if (!this.storeId) {
                this.$message.info('仓库名称不能为空!');
                return;
            }
            if (!this.amount) {
                this.$message.info('入库量不能为空!');
                return;
            }
            let data;
            if (type == 'add') {
                data = {
                    toolName:this.toolType,
                    toolModel:this.equipmentSpecs,
                    supplierId:this.supplierId,
                    toolFactory:this.toolFactory,
                    storeId:this.storeId,
                    batchSn:$.trim(this.batchSn),
                    endDate:this.endDate?this.moment(this.endDate).format('YYYY-MM-DD') : '',
                    productDate:this.productDate?this.moment(this.productDate).format('YYYY-MM-DD') : '',
                    amount:$.trim(this.amount)

                }
                operatToolInToolStore(data, this);
            } else {
                data = {
                    supplierId:this.supplierId,
                    toolFactory:this.toolFactory,
                    toolModel:this.toolModel,
                    toolName:this.toolName,
                    toolType:this.toolType,
                    id: this.changeId
                }
                editDevOpsToolLedger(data, this);
            }
        },
        //入库参数重置
        warehousingReast(){
            this.storeId='';
            this.batchSn='';
            this.productDate='';
            this.endDate='';
            this.amount='';
        },
        // 入库按钮效果
        warehousing(data){
            this.warehousingReast();
            console.log(data)
            this.toolId=data.id;
            this.visible2=true;
        },
        //入库关闭
        onClose2(){
            this.visible2=false;
        },
        //入库提交
        onSubmit2(){
            if(!this.storeId){
                this.$message.info('仓库名称不能为空！');
                return;
            }
            if(!this.batchSn){
                this.$message.info('批次号不能为空！');
                return;
            }
            if(!this.amount){
                this.$message.info('入库量不能为空！');
                return;
            }
            if(new Date(this.productDate).getTime()>new Date(this.endDate).getTime()){
                this.$message.info('生产日期不能大于有效期！');
                return;
            }
            let data={
                amount:this.amount,
                batchSn:this.batchSn,
                endDate:this.endDate?this.moment(this.endDate).format('YYYY-MM-DD') : '',
                productDate:this.productDate?this.moment(this.productDate).format('YYYY-MM-DD') : '',
                storeId:this.storeId,
                toolId:this.toolId
            }
            operatToolInToolStore(data,this);
        },
        // 出库按钮效果
        takeDeliveryGoods(data){
            this.visible3=true;
            this.selectionRows=[];
            this.selectedRowKeys=[];
            if(data){
                this.goodsId=data.id;
                this.batchSn2=data.batchSn;
            }
            
            let res={
                itemSn:$.trim(this.itemSn),
                toolid:this.goodsId,
                pageNo:this.ipagination2.current,
                pageSize:this.ipagination2.pageSize,
                batchSn:this.batchSn2
            }
            getOperatToolBorrowDetailData(res,this);
        },
        //出库关闭
        onClose3(){
            this.visible3=false;
        },
        //出库提交
        onSubmit3(){
            this.visible3=false;
        },
        //出库表格改变
        handleTableChange2(pagination, filters, sorter) {

            this.ipagination2.current = pagination.current;
            this.takeDeliveryGoods();

        },
        //借用记录
        borrowJil(data){
            if(data){
                this.goodsId=data.goodsId;
            }
            if(this.startTime){
                if(new Date(this.endTime).getTime()<new Date(this.startTime).getTime()){
                    this.$message.info('借用结束时间不能小于借用开始时间！')
                    return;
                }
            }
            
            let res={
                endTime:this.endTime?this.moment(this.endTime).format('YYYY-MM-DD') : '',
                startTime:this.startTime?this.moment(this.startTime).format('YYYY-MM-DD') : '',
                goodsId:this.goodsId,
                pageNo:this.ipagination3.current,
                pageSize:this.ipagination3.pageSize,
                userName:$.trim(this.userName)
            }
            getOperatToolHistoryData(res,this);
            this.visible4=true;
        },
        //借用记录关闭
        onClose4(){
            this.visible4=false;
        },
        //借用记录表格改变
        handleTableChange3(pagination, filters, sorter) {

            this.ipagination3.current = pagination.current;
            this.borrowJil();

        },
        //出库、归还按钮效果
        warehousingOrReturn(type,data){
            if(type=='warehousing'){
                this.warehousingOrReturnTitle='出库';
                this.applicantName='';
                this.applicantId='';
                this.borrowTime='';
                if(!this.selectedRowKeys.length){
                    this.$message.info('请选择要出库的对象！');
                    return;
                }
                for(let i=0;i<this.selectionRows.length;i++){
                    if(this.selectionRows[i].itemStatus!=1){
                        this.$message.info('只有在库才能出库！');
                        return;
                    }
                }
            }else{
                this.warehousingOrReturnTitle='归还';
            }
            this.warehousingOrReturnvisible=true;
        },
        //出库归还确定
        warehousingOrReturnOk(){
            let ids=[];
            this.selectionRows.map(index=>{
                ids.push(index.goodsId)
            })
            
            let data={
                ids:ids.join(','),
                applySn:this.applicantId,
                userId:this.applicantName
            }
            console.log(data)
            operatToolGoodsOut(data,this);
        },
        //出库归还取消
        warehousingOrReturnCancel(){
            this.warehousingOrReturnvisible=false;

        },
        //维护按钮效果
        maintain(data){
            this.goodsItemstatus='';
            this.maintainvisible=true;
            this.id=data.goodsId;
        },
        //维护弹框确定
        maintainOk(){
            let data={
                itemStatus:this.goodsItemstatus,
                id:this.id
            }
            operatToolEditStatus(data,this);
            
        },
        //维护弹框取消
        maintainCancel(){
            
            this.maintainvisible=false;
            
        },
        //归还按钮效果
        returnClick(data){
            this.goodsItemstatus='';
            this.description='';
            this.id=data.goodsId;
            this.returnvisible=true;
        },
        //归还确定
        returnOk(){
            let data={
                // itemStatus:this.goodsItemstatus,
                id:this.id,
                description:$.trim(this.description)
            }
            operatToolBack(data,this);
        },
        //归还确定
        returnCancel(){
            this.returnvisible=false;
        },
        //导出
        handleExportXls(fileName){
            if(!fileName || typeof fileName != "string"){
                fileName = "导出文件"
            }
            let param = {...this.queryParam};
            if(this.selectedRowKeys && this.selectedRowKeys.length>0){
                param['selections'] = this.selectedRowKeys.join(",")
            }
            console.log("导出参数",param)
            downFile(this.url.exportXlsUrl,param).then((data)=>{
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
        //出库-复选框改变效果
        onSelectChange(selectedRowKeys, selectionRows) {
            console.log(selectionRows)
            this.selectedRowKeys = selectedRowKeys;
            this.selectionRows = selectionRows;
        },
        //出库-出库-申请人姓名改变获取申请单号
        applicantNameChange(data){
            console.log(data);
            let res={
                userId:data
            }
            getOperatToolGoodsApply(res,this);
        },
        //生产日期不能大于今天
        disabledDate(current){
            return current && current >this.moment().endOf('day');
        },
        //申请审核按钮触发申请审核组件
        optApplyVerifyList(){
            this.$refs.modalForm.optApplyVerifyListvisible=true;
            this.$refs.modalForm.updata();
        },
        //申请历史按钮触发申请历史组件
        optToolBorrowList(){
            this.$refs.modalForm2.optToolBorrowListvisible=true;
            this.$refs.modalForm2.updata();
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

//equipmentAccountMaintenance/devOpsToolLedger.vue 页面混入了equipmentAccountMaintenance/devOpsToolLedger.js