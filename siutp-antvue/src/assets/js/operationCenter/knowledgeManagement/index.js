import {
    getKnowlegeListData,
    customerInformationUploadFile,
    addKnowlege,
    knowlegeAttachUploadFile,
    changeKnowlegeIdData,
    editKnowlegeIdData,
    getOwnershipType,
    getKnowledgeType,
    getEquipmentTypeData,
    getEquipmentModelData,
    getEquipmentSpecsData,
    knowlegeItemEditItemName,
    knowlegeOperationAdd,
    knowlegeCautionAdd,
    knowlegeOperationEdit,
    knowlegeCautionEdit,
    knowlegeOperationModifyknowlegeop,
    knowlegeCautionModifyknowlegeca,
    uploadMoreKnowlege,
    knowlegeAttachDelete,
    getQueryNameList,
    getCategory
} from '@/api/operationCenter-t/knowledgeManagement.js'
import searchReset from '@/mixins/searchReset.js'
export default {
    name: "equipmentAccountMaintenance",
    mixins: [searchReset],
    components: {

    },
    data() {
        return {
            description: '知识管理',
            drawerTitle: '新增知识',
            current: 1,
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
                title: '知识名称',
                align: "center",

                dataIndex: 'knowlegeName',

            },
            {
                title: '知识类型',
                align: "center",
                dataIndex: 'typeName',

            },

            //              {
            //                  title: '设备类别',
            //                  align: "center",
            //                  dataIndex: 'ownershipType',
            //
            //              },
            {
                title: '设备类型',
                align: "center",
                dataIndex: 'equipmentTypeName',

            },
            {
                title: '设备规格',
                align: "center",
                dataIndex: 'equipmentModelName',

            },
            {
                title: '设备型号',
                align: "center",
                dataIndex: 'equipmentSpecsName',

            },
            {
                title: '所属资源',
                align: "center",
                dataIndex: 'resourceName',
            },
            {
                title: '供应商',
                align: "center",
                dataIndex: 'supplierName',
            },
            {
                title: '知识条目',
                align: "center",

                dataIndex: 'itemCount',

            },
            {
                title: '维护人',
                align: "center",

                dataIndex: 'createBy',

            },

            {
                title: '操作',
                width: 100,
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
                    return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                },
                showQuickJumper: true,
                // showSizeChanger: true,
                total: 0
            },
            selectionRows: [],
            selectedRowKeys: [],
            form: this.$form.createForm(this),
            visible: false,
            changeId: '',
            knowlegeItemVoList: [
                {
                    operationName: '',
                    knowlegeOperationList: [
                        {
                            displayOrder: '',
                            operationItem: ''
                        }
                    ],
                    knowlegeCautionList: [
                        {
                            displayOrder: '',
                            cautionItem: ''
                        }
                    ],
                    knowlegeAttachList: [

                    ]
                }
            ],
            fileList: [],//上传后数据回显List
            knowlegeName: '', //知识名称
            category: '', //知识类型
            type: '', //所属类型
            equipmentTypeName: '', //设备类型
            equipmentModel: '', //规格类型
            equipmentSpecs: '', //设备型号
            ownershipTypeList: [],//所属类型List
            knowledgeTypeList: [],//知识类型List
            modelTypeList: [],//所属资源名称
            equipmentModelList: [], //资源类型List
            equipmentSpecsList: [],//规格类型List
            isShow: true,
            changevisible: false,
            knowlegeOperationListcolumns: [
                {
                    title: '操作规程',
                    align: "center",

                    dataIndex: 'operationItem',

                },
                {
                    title: '排序索引',
                    align: "center",

                    dataIndex: 'displayOrder',

                },
                {
                    title: '操作',
                    align: "center",

                    dataIndex: 'caozuo',
                    scopedSlots: {
                        customRender: 'caozuo'
                    },
                },
            ],
            knowlegeCautionListcolumns: [
                {
                    title: '安全事项',
                    align: "center",

                    dataIndex: 'cautionItem',

                }, {
                    title: '排序索引',
                    align: "center",

                    dataIndex: 'displayOrder',

                }, {
                    title: '操作',
                    align: "center",

                    dataIndex: 'caozuo',
                    scopedSlots: {
                        customRender: 'caozuo'
                    },
                },
            ],
            knowlegeAttachListcolumns: [
                {
                    title: '合同附件名称',
                    align: "center",

                    dataIndex: 'fileName',

                }, {
                    title: '操作',
                    align: "center",

                    dataIndex: 'caozuo',
                    scopedSlots: {
                        customRender: 'caozuo'
                    },
                },
            ],
            knowlegeOperationTitle: '新增',
            knowlegeOperationvisible: false,
            operationItem: '',
            displayOrder: '',
            knowlegeCautionTitle: '新增',
            knowlegeCautionvisible: false,
            knowlegeCautionoperationItem: '',
            knowlegeCautiondisplayOrder: '',
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
            categoryId: '',
            categoryIndex: '',
            zsIndex: '',
            activeKey: '1',
            detailsvisible: false,
            detailsTitle: '',
            resource: '',
            resourceList: [], // 所属资源
            supplierId: '', // 供应商
            supplierClassificationList: [],
        }
    },
    computed: {

    },
    mounted() {
        //设备类别下拉获取
        getOwnershipType(this);
        //知识类型下拉获取
        getKnowledgeType(this);
        //新增修改获取设备类型下拉值
        getEquipmentTypeData(this);
        //数据初始化
        this.updata();
        // 所属资源下拉
        getCategory({ pcode: 'A22' }, this, 'resourceList')
        // 供应商下拉
        getQueryNameList(this)
    },
    methods: {
        //获取列表信息
        updata() {
            let data = {
                knowlegeName: $.trim(this.queryParam.knowledgeName),
                category: this.queryParam.ownershipType == '0' ? '' : this.queryParam.ownershipType,
                type: this.queryParam.knowledgeType == '0' ? '' : this.queryParam.knowledgeType,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            this.loading = true;
            getKnowlegeListData(data, this);
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
                this.drawerTitle = '新增信息';
                this.visible = true;
            } else if (type == 'change') {
                this.drawerTitle = '修改信息';
                this.changeId = data.id;
                this.changevisible = true;
                this.defaultActiveKey = '1';
                this.knowlegeName = data.knowlegeName; // 知识名称
                //	            this.category = data.category?data.category:''; // 设备类别
                this.type = data.type; // 知识类型
                this.equipmentTypeName = data.equipmentType; // 设备类型
                this.equipmentModel = data.equipmentModel; // 设备规格
                this.equipmentSpecs = data.equipmentSpecs; // 设备型号

                this.resource = data.resource; // 所属资源
                this.supplierId = data.supplierId; // 供应商
                setTimeout(() => {
                    this.changeEquipmentTypeName(data.equipmentType);
                }, 500)

                setTimeout(() => {
                    this.changeEquipmentModel(data.equipmentModel);
                }, 800)
                this.knowlegeItemVoList = [];
                changeKnowlegeIdData({
                    id: data.id
                }, this);
            }

        },
        //表格属性改变
        handleTableChange(pagination, filters, sorter) {

            this.ipagination.current = pagination.current;
            this.updata();

        },
        //新增修改知识关闭
        onClose() {
            this.visible = false;
            this.isShow = true;
            this.drawerTitle = '新增信息';

        },
        //新增修改知识数据重置
        addReset() {
            this.knowlegeName = '';
            //          this.category = '';
            this.type = '';
            this.equipmentTypeName = '';
            this.equipmentModel = '';
            this.equipmentSpecs = '';
            this.resource = ''; // 所属资源
            this.supplierId = ''; // 供应商
            this.defaultActiveKey = '1';
            this.knowlegeItemVoList = [
                {
                    operationName: '',
                    knowlegeOperationList: [{
                        displayOrder: '',
                        operationItem: ''
                    }],
                    knowlegeCautionList: [{
                        displayOrder: '',
                        cautionItem: ''
                    }],
                    knowlegeAttachList: [

                    ]
                }
            ]
        },

        //新增修改提交
        onSubmit() {
            let data;
            if (!this.knowlegeName) {
                this.$message.info('知识名称不能为空!');
                return;
            }

            if (!this.type) {
                this.$message.info('知识类型不能为空!');
                return;
            }
            //          if (!this.category) {
            //              this.$message.info('设备类别不能为空!');
            //              return;
            //          }
            if (!this.equipmentTypeName) {
                this.$message.info('设备类型不能为空!');
                return;
            }
            if (!this.equipmentModel) {
                this.$message.info('设备型号不能为空!');
                return;
            }
            if (!this.equipmentSpecs) {
                this.$message.info('设备规格不能为空!');
                return;
            }
            for (let i = 0; i < this.knowlegeItemVoList.length; i++) {
                if (!this.knowlegeItemVoList[i].operationName) {
                    this.$message.info('名称不能为空!');
                    return;
                }
                for (let j = 0; j < this.knowlegeItemVoList[i].knowlegeOperationList.length; j++) {
                    if (!this.knowlegeItemVoList[i].knowlegeOperationList[j].operationItem) {
                        this.$message.info('操作规程不能为空!');
                        return;
                    }
                    // if (!this.knowlegeItemVoList[i].knowlegeOperationList[j].displayOrder) {
                    //     this.$message.info('排序索引不能为空!');
                    //     return;
                    // }
                }
            }
            // this.knowlegeItemVoList.map(index=>{
            //     if (!index.operationName){
            //         this.$message.info('名称不能为空!');
            //         return;
            //     }
            //     index.knowlegeOperationList.map(childrenIndex=>{
            //         if (!childrenIndex.operationItem){
            //             this.$message.info('操作规程不能为空!');
            //             return;
            //         }
            //         if (!childrenIndex.displayOrder) {
            //             this.$message.info('排序索引不能为空!');
            //             return;
            //         }
            //     });
            // })

            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    knowlegeName: $.trim(this.knowlegeName),
                    //                  category: this.category,
                    type: this.type,
                    equipmentType: this.equipmentTypeName,
                    equipmentModel: this.equipmentModel,
                    equipmentSpecs: this.equipmentSpecs,
                    resource: this.resource,// 所属资源
                    supplierId: this.supplierId,
                    knowlegeItemVoList: this.knowlegeItemVoList

                }
                addKnowlege(data, this);
            } else {
                data = {
                    knowlegeName: $.trim(this.knowlegeName),
                    //                  category: this.category,
                    type: this.type,
                    equipmentType: this.equipmentTypeName,
                    equipmentModel: this.equipmentModel,
                    equipmentSpecs: this.equipmentSpecs,
                    resource: this.resource,// 所属资源
                    supplierId: this.supplierId,
                    knowlegeItemVoList: this.knowlegeItemVoList,
                    id: this.changeId
                }
                editKnowlegeIdData(data, this);
            }

        },

        //增加类目
        addCategory() {
            this.knowlegeItemVoList.push({
                operationName: '',
                knowlegeOperationList: [{
                    displayOrder: '',
                    operationItem: ''
                }],
                knowlegeCautionList: [{
                    cautionItem: '',
                    displayOrder: ''
                }],
                knowlegeAttachList: []
            });

        },
        //删除类目
        removeCategory(categoryIndex) {
            this.knowlegeItemVoList.splice(categoryIndex, 1);
        },
        //增加规则操作
        addOperationRules(parentIndex, targetIndex) {
            this.knowlegeItemVoList[parentIndex].knowlegeOperationList.push({
                displayOrder: '',
                operationItem: ''
            });
        },
        //删除规则操作
        removeOperationRules(parentIndex, targetIndex) {
            this.knowlegeItemVoList[parentIndex].knowlegeOperationList.splice(targetIndex, 1);
        },
        //增加安全注意事项
        addSafetyPrecautions(parentIndex, targetIndex) {
            this.knowlegeItemVoList[parentIndex].knowlegeCautionList.push({
                cautionItem: '',
                displayOrder: ''
            });
        },
        //删除安全注意事项
        removeSafetyPrecautions(parentIndex, targetIndex) {
            this.knowlegeItemVoList[parentIndex].knowlegeCautionList.splice(targetIndex, 1);
        },
        //新增上传文件
        upfileClick(e, index) {
            let param = new FormData();
            for (let i = 0; i < e.target.files.length; i++) {
                let file = e.target.files[i];
                if (file.type != 'application/pdf') {
                    this.$message.error('请上传PDF格式文件!');
                    $('.uploadBtn').val('');
                    return;
                };
                param.append('files', file);
            }

            $('.uploadBtn').val('');
            knowlegeAttachUploadFile(param, this, index);
        },
        //修改上传文件
        upfileClick2(e, index, id) {
            let param = new FormData();
            for (let i = 0; i < e.target.files.length; i++) {
                let file = e.target.files[i];
                if (file.type != 'application/pdf') {
                    this.$message.error('请上传PDF格式文件!');
                    $('.uploadBtn').val('');
                    return;
                };
                param.append('files', file);
            }
            if (id) {
                param.append('itemId', id);
            }
            $('.uploadBtn').val('');
            uploadMoreKnowlege(param, this, index);
        },
        //删除上传文件
        removeFile(e, parentIndex, index) {
            this.knowlegeItemVoList[parentIndex].knowlegeAttachList.splice(index, 1);
            $('.uploadBtn').eq(parentIndex).val('');
        },
        //资源名称改变时
        changeEquipmentTypeName(data) {
            console.log(data);
            let res = {
                pcate: data
            }
            getEquipmentModelData(res, this);
        },
        //资源类型改变时
        changeEquipmentModel(data) {
            console.log(data, '修改设备规格')
            let res = {
                pcate: data
            }
            getEquipmentSpecsData(res, this);
        },
        //点击详情
        details(data) {
            console.log(data);
            this.detailsTitle = data.equipmentTypeName;
            changeKnowlegeIdData({
                id: data.id
            }, this);
            this.detailsvisible = true;

        },
        //详情关闭
        detailsClose() {
            this.detailsvisible = false;
        },
        //修改知识关闭
        changeonClose() {
            this.changevisible = false;
            this.activeKey = '1';
        },
        //修改知识-操作规程-增加
        addKnowlegeOperationList(data, index, type, record, zsIndex) {
            this.addKnowlegeOperationReast();
            this.knowlegeOperationvisible = true;
            if (type == 'add') {
                this.knowlegeOperationTitle = '新增';

            } else {
                this.knowlegeOperationTitle = '编辑';
                this.operationItem = record.operationItem;
                this.displayOrder = record.displayOrder;
                this.zsIndex = zsIndex;
                console.log(zsIndex)
            }

            this.categoryId = data;
            this.categoryIndex = index;

        },
        //修改知识-操作规程-参数重置
        addKnowlegeOperationReast() {
            this.displayOrder = '';
            this.operationItem = '';
        },
        //修改知识-保存规程名称
        bcKnowlegeItemVoList(id, operationName) {
            let data = {
                id: id,
                operationName: operationName
            }
            knowlegeItemEditItemName(data, this);
            console.log(data)
        },
        //修改-操作规程增加编辑确定
        knowlegeOperationhandleOk() {
            let data;
            if (this.knowlegeOperationTitle == '新增') {
                data = {
                    knowlegeItemId: this.categoryId,
                    displayOrder: this.displayOrder,
                    operationItem: this.operationItem
                }
                knowlegeOperationAdd(data, this, this.categoryIndex)
            } else {
                data = {
                    id: this.categoryId,
                    displayOrder: this.displayOrder,
                    operationItem: this.operationItem
                }
                knowlegeOperationEdit(data, this, this.categoryIndex, this.zsIndex)
            }


        },
        //修改-操作规程增加编辑取消
        knowlegeOperationhandleCancel() {
            this.knowlegeOperationvisible = false;
        },
        //修改-操作规程增加编辑删除
        removeKnowlegeOperationList(data, index, zsIndex) {
            let _this = this;
            this.$confirm({
                title: '确定要删除吗?',
                content: '',
                onOk() {
                    let res = {
                        id: data.id
                    }
                    knowlegeOperationModifyknowlegeop(res, _this, index, zsIndex);
                },
                onCancel() {
                    console.log('Cancel');
                },
                class: 'test',
            });

        },
        //修改-安全事项增加
        addknowlegeCautionList(data, index, type, record, zsIndex) {
            this.addknowlegeCautionReast();
            if (type == 'add') {
                this.knowlegeCautionTitle = '新增';
            } else {
                console.log(record)
                this.knowlegeCautionTitle = '编辑';
                this.knowlegeCautionoperationItem = record.cautionItem;
                this.knowlegeCautiondisplayOrder = record.displayOrder;
                this.zsIndex = zsIndex;
            }

            this.knowlegeCautionvisible = true;
            this.categoryId = data;
            this.categoryIndex = index;

        },
        //修改-安全事项数据重置
        addknowlegeCautionReast() {
            this.knowlegeCautiondisplayOrder = '';
            this.knowlegeCautionoperationItem = '';
        },
        //修改-安全事项数据确定
        knowlegeCautionhandleOk() {
            let data;
            if (this.knowlegeCautionTitle == '新增') {
                data = {
                    knowlegeItemId: this.categoryId,
                    displayOrder: this.knowlegeCautiondisplayOrder,
                    cautionItem: this.knowlegeCautionoperationItem
                }
                knowlegeCautionAdd(data, this, this.categoryIndex)
            } else {
                data = {
                    id: this.categoryId,
                    displayOrder: this.knowlegeCautiondisplayOrder,
                    cautionItem: this.knowlegeCautionoperationItem
                }
                knowlegeCautionEdit(data, this, this.categoryIndex, this.zsIndex)
            }

        },
        //修改-安全事项数据关闭
        knowlegeCautionhandleCancel() {
            this.knowlegeCautionvisible = false;
        },
        //修改-安全事项数据删除
        removeknowlegeCautionList(data, index, zsIndex) {
            let _this = this;
            this.$confirm({
                title: '确定要删除吗?',
                content: '',
                onOk() {
                    let res = {
                        id: data.id
                    }
                    knowlegeCautionModifyknowlegeca(res, _this, index, zsIndex);
                },
                onCancel() {
                    console.log('Cancel');
                },
                class: 'test',
            });
        },
        //修改-规程-手册删除
        knowlegeAttachDelete(record, index, zsIndex) {
            let data = {
                id: record.id
            }
            knowlegeAttachDelete(data, this, index, zsIndex);
        }
    }

}
//operationCenter/knowledgeManagement/index.vue 页面混入了operationCenter/knowledgeManagement/index.js