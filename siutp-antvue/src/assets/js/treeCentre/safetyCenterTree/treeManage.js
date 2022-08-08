import { 
    getBidSegmentData,
    getEquipmentTypeData,
    getEquipmentLedgerData,
    getQueryNameList2,
} from '@/api/treeCentre/safetyCenterTree/treeManage.js'
import { colAuthFilter } from "@/utils/authFilter"
import JTreeRegionSelect from '@/components/jeecg/JTreeRegionSelectCenter'
import searchReset from '@/mixins/searchReset.js'
import { JeecgListMixin } from "@/mixins/JeecgListMixin";

import axios from "axios";
import {httpAction, getAction} from '@/api/manage'
export default {
    name: "equipmentAccountMaintenance",
    mixins:[searchReset,JeecgListMixin],
    components: {JTreeRegionSelect},
    data() {
        return {
            equipmentModelName: '', 
            equipmentSpecsName: '',
            equipmentTypeName: '',
            equipmentSupplierName: '',
            description: '设备台账',
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            queryParam: {
            }, //搜索属性集合
            columns: [{
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '所属标段',
                    align: "center",
					width: 100,
                    dataIndex: 'equipmentSection',

                },
                {
                    title: '设备类型',
                    align: "center",
					width: 100,
                    dataIndex: 'equipmentType',

                },
                {
                    title: '放置位置',
                    align: "center",
					width: 200,
                    dataIndex: 'equipmentLocation',

                },
                {
                    title: '供应商',
                    align: "center",
					width: 300,
                    dataIndex: 'equipmentSupplier',

                },
                {
                    title: '设备型号',
                    align: "center",
					width: 220,
                    dataIndex: 'equipmentModeName',

                },
                {
                    title: '设备规格',
                    align: "center",
                    width: 100,
                    dataIndex: 'equipmentSpecsName',

                },
                {
                    title: '设备编号',
                    align: "center",
					width: 120,
                    dataIndex: 'equipmentSn',

                },
                {
                    title: '设备名称',
                    align: "center",
					width: 200,
                    dataIndex: 'equipmentName',

                },
                {
                    title: '资产类别',
                    align: "center",
					width: 120,
                    dataIndex: 'equipmentCategory',

                },
                {
                    title: '资产状态',
                    align: "center",
					width: 100,
                    dataIndex: 'equipmentStatus',

                },
                {
                    title: '投入运营时间',
                    align: "center",
                    width: 200,
                    dataIndex: 'equipmentOperating',

                },
                {
                    title: '购置时间',
                    align: "center",
                    width: 200,
                    dataIndex: 'equipmentPurchase',

                },
                {
                    title: '树节点名称',
                    align: "center",
                    width: 200,
                    dataIndex: 'labelName',
                },
                {
                    title: '提前提醒时间(天)',
                    align: "center",
                    width: 200,
                    dataIndex: 'countDays',
                },
                {
                    title: '计划养护日期',
                    align: "center",
                    width: 200,
                    dataIndex: 'planDate',
                },
                {
                    title: '责任人',
                    align: "center",
                    width: 120,
                    dataIndex: 'personResponsibleName',
                },
                {
                    title: '设备状态',
                    align: "center",
                    width: 100,
                    fixed: 'right',
                    dataIndex: 'equipmentRevstopText',
                    scopedSlots: {
                        customRender: 'equipmentRevstopText'
                    },
                },
                {
                    title: '操作',
                    align: "center",
                    width: 200,
                    fixed: 'right',
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },


            ], //表格头部属性信息
            dataSource: [
            ], //表格数据源
            loading: true,
            /* 分页参数 */
            ipagination: {
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
           
        
            bidSegmentList: [], //所属标段
            modelTypeList: [],
            modelTypeList2: [],
            supplierClassificationList2: [],


            selectionRows: [], //表格复选框值
            selectedRowKeys: [], //表格复选框值
            form: this.$form.createForm(this),
            title: "操作",
            width: 800,
            visible: false,
            model: {},
            labelCol: {
                xs: {span: 24},
                sm: {span: 5},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },

            confirmLoading: false,
            validatorRules: {
                parentId: {},
                labelName: {}
            },
            url: {
                list: "/centre/sec/equ/equlist",
                add: "/centre/sec/equ/add", // 新增节点
                edit: "/centre/sec/equ/edit",  // 编辑节点
                checkCode:"/sys/category/checkCode",
                infoUrl: '/centre/sec/equ/query', //通过id查询
            },
            parentUrl: '/centre/sec/equ/getAll/',
            expandedRowKeys: [],
            pidField: "parent_id", //parentId
            disTree: false,
            id: '',
        }
    },
    computed: {
        importExcelUrl: function() {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    created(){
    	this.columns = colAuthFilter(this.columns,'opt:');
//  	this.initDictConfig();
    },
    mounted() {
        //获取所属标段值
        getBidSegmentData(this);
        //获取设备类型值
        getEquipmentTypeData(this);
        //获取供应商值-筛选
        getQueryNameList2(this);

        // //设备台账列表初始化获取数据
        // setTimeout(()=>{
        //     this.updata();
        // },500);
    },
    methods: {
        //设备台账列表初始化获取数据
        updata() {
            let data = {
                equipmentSn: $.trim(this.queryParam.equipmentNumber),
                equipmentSupplier: this.queryParam.supplier == 0 ? '' : this.queryParam.supplier,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                equipmentType: this.queryParam.equipmentType == 0 ? '' : this.queryParam.equipmentType,
                equipmentRevstop:this.queryParam.equipmentRevstop?this.queryParam.equipmentRevstop:'',
                equipmentCategory: this.equipmentCategory2,
                optSection: this.queryParam.optSection, // 标段
                optLocation: this.queryParam.optLocation, // 放置位置
            }
            this.loading = true;
            getEquipmentLedgerData(data, this);
        },
        //查询
        // searchQuery() {
        //     this.ipagination.current = 1;
        //     this.updata();
        // },
        // //表格属性改变
        // handleTableChange(pagination) {
        //     this.ipagination.current = pagination.current;
        //     this.updata();
        // },
        //资产类别筛选
        callback(data){
            console.log(data);
            this.equipmentCategory2 = data;
            this.ipagination.current=1;
            this.updata();
        },


        edit(record) {
            this.form.resetFields();
            this.model = Object.assign({}, record);
            this.visible = true; 
            this.$nextTick(() => {
                if (this.title == '编辑') {
                    this.disTree = true;
                } else {
                    this.disTree = false;
                }
            })
        },
        close() {
            this.visible = false;
            this.title = '';
            this.loadData();
        },
        handleOk() {
            const _this = this;
            // 触发表单验证
            let parentId = this.$refs.treeSelect.parentId;
            let data = {
                dataId: this.id,
                treeId: parentId
            }
            this.$nextTick(() => {
                if(!parentId){
                    _this.$message.error("请选择父节点");
                    return false
                }
                httpAction('/centre/sec/equ/treeobj/bind/', data, 'post')
                    .then(res => {
                        let result = res.data;
                        if (res.code * 1 == 200) {
                            _this.$message.success(res.message);
                            _this.close()
                        } else {
                            _this.$message.info(res.message);
                        }
                    }) 
               
            })
        },
        handleCancel() {
            this.close()
        },
        //绑定
        bindEvent (id) {
            this.visible = true;
            this.id = id;
        },
        //删除
        handleDeletes (id) {
            let _this = this;
            let parentId = '';
            let data = {dataId: id};
            httpAction('/centre/sec/equ/treeobj/unbind/', data, 'post')
                .then(res => {
                    let result = res.data;
                    if (res.code * 1 == 200) {
                        _this.$message.success(res.message);
                        _this.loadData();
                    } else {
                        _this.$message.info(res.message);
                    }
                }) 
        },


    }

}