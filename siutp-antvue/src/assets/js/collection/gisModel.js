import {
    getGisModelData,
    getEquipmentTypeData2,
    addGisModelData,
    deleteGisModelData,
    getGisModelIdData,
    editGisModelData,
    getBindVarList,
    bindGisIot,
    getBindGisIotList,
    unbindGisIot,
    bindGisData,
    unbindGisData,
    getGisVoList2,
    gisModelEditImage,
    gisModelGetResSn,
    getGisTypeList,
    synchronousCache,
    gisModelSetOrder
} from '@/api/collection-t/gisModel.js'
import searchReset from '@/mixins/searchReset.js'
export default {
    name: "gisModel",
    mixins: [searchReset],
    components: {

    },
    data() {
        return {
            description: 'GIS模型',
            scrollX: {},
            labelCol: {
                xs: { span: 24 },
                sm: { span: 5 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 }
            },
            drawerTitle: '新增GIS模型', // 新增或修改弹框title
            queryParam: {
                supplier: '0'
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
                title: '模型名称',
                align: "center",

                dataIndex: 'modelName',

            },

            {
                title: '模型类型',
                align: "center",

                dataIndex: 'modelType',

            },

            {
                title: '经度',
                align: "center",
                width: 200,

                dataIndex: 'longitude',

            },
            {
                title: '纬度',
                align: "center",
                width: 200,
                dataIndex: 'latitude',

            },
            {
                title: '层级',
                align: "center",

                dataIndex: 'modelLevel',

            },
            {
                title: '模型位置',
                align: "center",

                dataIndex: 'modelPosition',

            },
            {
                title: '打开状态',
                align: "center",

                dataIndex: 'modelOnImg',
                scopedSlots: {
                    customRender: 'modelOnImg'
                },
            },
            {
                title: '关闭状态',
                align: "center",

                dataIndex: 'modelOffImg',
                scopedSlots: {
                    customRender: 'modelOffImg'
                },
            },

            {
                title: '正常状态',
                align: "center",

                dataIndex: 'modelImg',
                scopedSlots: {
                    customRender: 'modelImg'
                },
            },
            {
                title: '告警状态',
                align: "center",

                dataIndex: 'modelWaringImg',
                scopedSlots: {
                    customRender: 'modelWaringImg'
                },
            },
            {
                title: '图片宽度',
                align: "center",

                dataIndex: 'width',

            },
            {
                title: '图片高度',
                align: "center",

                dataIndex: 'height',

            },
            {
                title: '偏移量',
                align: "center",

                dataIndex: 'modelOffset',

            },
            {
                title: '操作',
                align: "center",
                width: 340,
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
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            columns2: [

                {
                    title: '变量名称',
                    align: "center",
                    dataIndex: 'varName',

                },

                {
                    title: '变量示意',
                    align: "center",
                    dataIndex: 'varHint',

                },


                {
                    title: '操作',
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn2'
                    },
                },


            ], //表格头部属性信息
            dataSource2: [


            ], //表格数据源
            loading2: true,
            /* 分页参数 */
            ipagination2: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            columns3: [

                {
                    title: '变量名称',
                    align: "center",
                    dataIndex: 'varName',

                },

                {
                    title: '变量示意',
                    align: "center",
                    dataIndex: 'varHint',

                },
                {
                    title: '排序序号',
                    align: "center",
                    dataIndex: 'displayOrder',

                },

                {
                    title: '操作',
                    align: "center",
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn2'
                    },
                },


            ], //表格头部属性信息
            dataSource3: [


            ], //表格数据源
            loading3: true,
            /* 分页参数 */
            ipagination3: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            visible: false,
            modelTypeCode: '', //模型类型编码
            modelName: '', //模型名称
            modelType: '', //模型类型
            latitude: '', //纬度
            longitude: '', //经度
            modelLevel: '', //层级
            modelPosition: '', //模型显示位置
            modelOnImg: '', //打开图片地址
            modelOffImg: '', //关闭图片地址
            modelWaringImg: '', //警报图片地址
            modelOffset: '', //模型偏移量
            modelTypeCodeList: [], //模型类型编码list
            modelTypeList: [], //模型类型list
            modelTypeList2: [],
            modelPositionList: [], //模型显示位置list
            bindVisible: false, //绑定变量弹框值
            variableDetailsVisible: false, //变量详情弹框值
            bindId: '', //绑定变量当前行数据ID存储
            variableDetailsId: '', //变量详情当前行ID存储
            varName: '', //绑定变量搜索值-变量名称
            variableTitle: '', //绑定变量搜索值-变量示意
            giscolumns2: [{
                title: '资产名称',
                width: '33%',
                align: "center",
                dataIndex: 'name',

            },

            {
                title: '资产类型',
                align: "center",
                width: '33%',
                dataIndex: 'type',

            },
            {
                title: '操作',
                align: "center",
                width: '34%',
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn2'
                },
            },


            ], //表格头部属性信息
            gisdataSource2: [

            ], //表格数据源
            gisloading2: false,
            /* 分页参数 */
            gisipagination2: {
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            bindGisVisible: false,
            gismodelName: '',
            gisbindId: '',
            selectedRowKeys: [],
            changeProgrammeVisible: false,
            allOk: true,
            idModelType: '',
            gisResSnList: [],
            getGisTypeList: [],
            resId: '',
            giswidth: '',
            gisheight: '',
            setUpSerialNumberVisible: false,
            setUpSerialNum: '',
            setupSerialNumberId: '',
            bindVarTitle: ''
        }
    },
    computed: {

    },
    mounted() {
        //获取模型类型
        getEquipmentTypeData2(this);
        //数据初始化
        this.updata();
        //模型主题-模型类型
        getGisTypeList(this);
    },
    methods: {
        //获取GIS模型列表信息
        updata() {
            let data = {
                modelName: $.trim(this.queryParam.modelName),
                modelType: this.queryParam.modelType == 0 ? '' : this.queryParam.modelType,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            this.loading = true;
            getGisModelData(data, this);
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //新增
        handleAdd(type, data) {
            if (type == 'add') {
                this.drawerTitle = '新增GIS模型信息';
            } else if (type == 'change') {
                this.drawerTitle = '修改GIS模型信息';
                this.handleChange(data);
                this.changeId = data.id;
            }
            this.visible = true;

        },
        //修改
        handleChange(data) {
            let res = {
                id: data.id
            }
            getGisModelIdData(res, this);
        },
        //表格属性改变
        handleTableChange(pagination) {

            this.ipagination.current = pagination.current;
            this.updata();
        },
        //绑定变量触发弹框显示
        showDrawer() {
            this.visible = true;

        },
        //绑定变量弹框关闭
        onClose() {
            this.visible = false;
            this.addReset();
        },

        //新增设备台账信息
        addSubmit() {
            let type;
            if (this.drawerTitle.indexOf('新增') != -1) {
                type = 'add';
            } else {
                type = 'modify';
            }
            if (!this.modelName) {
                this.$message.info('模型名称不能为空!');
                return;
            }
            let data;
            if (type == 'add') {
                data = {
                    latitude: $.trim(this.latitude),
                    longitude: $.trim(this.longitude),
                    modelName: $.trim(this.modelName),
                    modelOffImg: $.trim(this.modelOffImg),
                    modelOffset: $.trim(this.modelOffset),
                    modelOnImg: $.trim(this.modelOnImg),
                    modelPosition: $.trim(this.modelPosition),
                    modelType: $.trim(this.modelType),
                    modelWaringImg: $.trim(this.modelWaringImg),
                    modelLevel: $.trim(this.modelLevel),
                }
                addGisModelData(data, this);
                this.updata();
            } else {
                data = {
                    latitude: $.trim(this.latitude),
                    longitude: $.trim(this.longitude),
                    modelName: $.trim(this.modelName),
                    modelOffImg: $.trim(this.modelOffImg),
                    modelOffset: $.trim(this.modelOffset),
                    modelOnImg: $.trim(this.modelOnImg),
                    modelPosition: $.trim(this.modelPosition),
                    modelType: $.trim(this.modelType),
                    modelWaringImg: $.trim(this.modelWaringImg),
                    modelLevel: $.trim(this.modelLevel),
                    id: this.changeId
                }
                editGisModelData(data, this);
            }

        },
        //重置新增
        addReset() {
            this.modelName = '';
            this.modelType = '';
            this.latitude = '';
            this.longitude = '';
            this.modelLevel = '';
            this.modelPosition = '';
            this.modelOnImg = '';
            this.modelOffImg = '';
            this.modelWaringImg = '';
            this.modelOffset = '';
        },
        //绑定变量调用
        bindVar(res) {
            console.log(res)
            this.bindVisible = true;
            this.bindId = res.id;
            this.bindVarTitle = '绑定变量:' + (res.modelType + res.modelName);
            this.bindVarReset();
            this.bindVarList();
        },
        //绑定变量列表获取
        bindVarList() {
            let data = {
                variableName: $.trim(this.varName),
                variableTitle: $.trim(this.variableTitle),
                pageNo: this.ipagination2.current,
                pageSize: this.ipagination2.pageSize
            }
            getBindVarList(data, this);
        },
        //绑定变量表格改变
        handleTableChange2(pagination) {
            this.ipagination2.current = pagination.current;
            this.bindVarList();
        },
        //绑定变量取消
        bindCancel() {
            this.bindVisible = false;
        },
        //绑定变量查询
        searchQuery2() {
            this.ipagination2.current = 1;
            this.bindVarList();

        },
        //变量详情调用
        variableDetails(data) {
            this.variableDetailsVisible = true;
            this.variableDetailsId = data.id;
            this.ipagination3.current = 1;
            this.variableDetailsList();
        },
        //变量详情数据列表获取
        variableDetailsList() {
            let data = {
                modelId: this.variableDetailsId,
                pageNo: this.ipagination3.current,
                pageSize: this.ipagination3.pageSize
            }
            getBindGisIotList(data, this)
        },
        //变量详情数据表格分页触发
        handleTableChange3(pagination) {
            this.ipagination3.current = pagination.current;
            this.variableDetailsList();
        },
        //变量详情确定
        variableDetailsCancel() {
            this.variableDetailsVisible = false;
        },
        //变量详情取消
        variableDetailsOnOk() {
            this.variableDetailsVisible = false;
        },
        //点击绑定变量-绑定效果
        bindShow(data) {
            let _this = this;
            this.$confirm({
                title: '确定要绑定吗?',
                content: '',
                onOk() {
                    let res = {
                        modelId: _this.bindId,
                        variableId: data.id
                    }
                    bindGisIot(res, _this);
                },
                onCancel() { },
            });
            // let res={
            //   modelId:_this.bindId,
            //   variableId:data.id
            // }
            // bindGisIot(res,_this);
        },
        //解除绑定
        unbindShow(data) {
            let _this = this;
            this.$confirm({
                title: '确定要解除绑定吗?',
                content: '',
                onOk() {
                    let res = {
                        variableId: data.id
                    }
                    unbindGisIot(res, _this);
                },
                onCancel() { },
            });

        },
        //删除模型
        deleteGisModel(data) {
            let _this = this;
            this.$confirm({
                title: '确定要删除吗?',
                content: '',
                onOk() {
                    let res = {
                        id: data.id
                    }
                    deleteGisModelData(res, _this);
                    _this.updata();
                },
                onCancel() { },
            });


        },
        //绑定变量重置
        bindVarReset() {
            this.varName = '';
            this.variableTitle = '';
            this.ipagination2.current = 1;
        },
        //获取资产绑定列表
        updata2() {
            let data = {
                name: $.trim(this.gismodelName),
                pageNo: this.gisipagination2.current,
                pageSize: this.gisipagination2.pageSize
            }
            this.gisloading2 = true;

            getGisVoList2(data, this);
        },
        //点击资产绑定触发弹框
        bindGisShow(data) {
            console.log(data)
            this.gisbindId = data.id;
            this.bindGisVisible = true;
            this.updata2();
        },
        //点击绑定触发弹框
        bindShow2(data) {
            let res = {
                modelId: this.gisbindId,
                equipmentId: data.id
            }
            bindGisData(res, this);
        },
        //解除设备绑定
        unbindGis(data) {
            let res = {
                modelId: data.id
            }
            console.log(data)
            let _this = this;
            this.$confirm({
                title: '确定要解除绑定吗?',
                content: '当前资产：' + data.typeSn,
                onOk() {
                    unbindGisData(res, _this);
                },
                onCancel() { },
            });

        },
        //绑定弹框确定
        bindOnOk() {
            this.bindGisVisible = false;
            this.gismodelName = '';
        },
        //绑定弹框取消
        bindGisCancel() {
            this.bindGisVisible = false;
            this.gismodelName = '';
        },
        //资产绑定页码改变
        gishandleTableChange2(pagination) {
            this.gisipagination2.current = pagination.current;
            this.updata2();
        },
        //资产绑定查询
        gissearchQuery2() {
            this.gisipagination2.current = 1;
            this.updata2();
        },
        //gis模型复选框效果
        onSelectChange(selectedRowKeys, selectionRows) {
            console.log(selectedRowKeys);
            console.log(selectionRows);
            this.selectedRowKeys = selectedRowKeys;
            this.selectionRows = selectionRows;
        },
        //模型主题按钮效果
        changeProgramme() {
            this.giswidth = '';
            this.gisheight = '';
            this.resId = '';
            this.modelType = '';
            if (this.selectedRowKeys.length) {
                for (let i = 0; i < this.selectionRows.length; i++) {
                    if (this.selectionRows[0].modelTypeCode != this.selectionRows[i].modelTypeCode) {
                        this.$message.info('模型类型必须相同！');
                        return;
                    }
                }
                this.allOk = false;
                this.idModelType = this.selectionRows[0].modelTypeCode;
                let res = {
                    modelType: this.idModelType
                }
                gisModelGetResSn(res, this);
            } else {
                this.allOk = true;
            }

            this.changeProgrammeVisible = true;
        },
        //模型主题确定
        changeProgrammeOnOk() {
            if (this.allOk) {
                if (!this.modelType) {
                    this.$message.info('模型类型不能为空!');
                    return;
                }
            }

            if (!this.resId) {
                this.$message.info('模型编码不能为空!');
                return;
            }
            if (!this.giswidth) {
                this.$message.info('模型宽度不能为空!');
                return;
            }
            if (!this.gisheight) {
                this.$message.info('模型高度不能为空!');
                return;
            }
            let data;
            if (this.allOk) {
                data = {
                    height: $.trim(this.gisheight),
                    width: $.trim(this.giswidth),
                    type: '2',
                    resId: this.resId
                }
            } else {
                data = {
                    height: $.trim(this.gisheight),
                    width: $.trim(this.giswidth),
                    type: '1',
                    resId: this.resId,
                    ids: this.selectedRowKeys.join(',')
                }
            }
            console.log(data);
            gisModelEditImage(data, this);

        },
        //模型主题取消
        changeProgrammeCancel() {
            this.changeProgrammeVisible = false;
        },
        //模型主题-模型类型改变获取模型编码
        modelTypeChange(data) {

            let res = {
                modelType: data
            }
            gisModelGetResSn(res, this);
        },
        //同步缓存
        synchronousCache() {
            let data = {
                url: '/equipment/gisModel/clearRedis'
            }
            synchronousCache(data, this);
        },
        //设置序号按钮
        setupSerialNumber(record) {
            this.setUpSerialNumberVisible = true;
            this.setUpSerialNum = record.displayOrder ? record.displayOrder : '';
            this.setupSerialNumberId = record.id
        },
        //设置序号确认
        setUpSerialNumberOnOk() {
            let data = {
                displayOrder: this.setUpSerialNum,
                id: this.setupSerialNumberId
            }
            gisModelSetOrder(data, this);

        },
        //设置序号取消
        setUpSerialNumberCancel() {
            this.setUpSerialNumberVisible = false;
        }
    }

}
//gisModel页面组件混入的gisModel.js