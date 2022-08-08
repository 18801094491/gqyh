import {
    getEquipmentLedgerData,
    getEquipmentCategoryData,
    getEquipmentTypeData,
    getBidSegmentData,
    addEquipmentLedgerData,
    getEquipmentLedgerChangeData,
    getQueryNameList,
    getQueryNameList2,
    changeEquipmentLedgerData,
    getEquipmentAssetsData,
    getAssetStatus,
    optEquipmentQueryRevstopById,
    knowlegeIdData,
    getEquipmentModelData,
    getEquipmentSpecsData,
    getAgencyStatus,
    equiImgUpload,
    addEquiImg
} from '@/api/equipmentAccount/maintenance.js'
import {getEquipmentOptType} from '@/api/dict.js'


import {colAuthFilter} from "@/utils/authFilter"

import searchReset from '@/mixins/searchReset.js'
import {ACCESS_TOKEN} from "@/store/mutation-types"
import {JeecgListMixin} from "@/mixins/JeecgListMixin";
import Vue from 'vue'
import {
    downFile
} from '@/api/manage'

export default {
    name: "equipmentAccountMaintenance",
    mixins: [searchReset, JeecgListMixin],
    components: {},
    data() {
        return {
            equipmentModelName: '',
            equipmentSpecsName: '',
            equipmentSupplierName: '',
            description: '设备台账',
            labelCol: {
                xs: {span: 24},
                sm: {span: 8},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },
            drawerTitle: '新增设备台账', // 新增或修改弹框title
            queryParam: {}, //搜索属性集合
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
                    dataIndex: 'bidSegment',

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
                    dataIndex: 'position',

                },
                {
                    title: '供应商',
                    align: "center",
                    width: 300,
                    dataIndex: 'supplier',

                },
                {
                    title: '设备型号',
                    align: "center",
                    width: 220,
                    dataIndex: 'equipmentModelName',

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
                    dataIndex: 'number',

                },
                {
                    title: '设备名称',
                    align: "center",
                    width: 200,
                    dataIndex: 'equipmentName',

                },
                {
                    title: '设备类别',
                    align: "center",
                    width: 120,
                    dataIndex: 'equipmentCategory',

                },
                {
                    title: '资产状态',
                    align: "center",
                    width: 100,
                    dataIndex: 'state',

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
                    width: 150,
                    fixed: 'right',
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },


            ], //表格头部属性信息
            dataSource: [], //表格数据源
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
            columns2: [{
                title: '模型名称',
                width: '33%',
                align: "center",
                dataIndex: 'name',

            },

                {
                    title: '模型类型',
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
            dataSource2: [], //表格数据源
            loading2: false,
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

                    dataIndex: 'type',

                },
                {
                    title: '操作',
                    align: "center",

                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'maintainBtn'
                    },
                },


            ], //表格头部属性信息
            dataSource3: [
                {}
            ], //表格数据源
            loading3: false,
            /* 分页参数 */
            ipagination3: {
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
            modelTypeList: [],
            selectionRows: [], //表格复选框值
            selectedRowKeys: [], //表格复选框值
            form: this.$form.createForm(this),
            visible: false, //新增或修改弹框显示隐藏值
            equipmentNumber: '', //编号
            equipmentName: '', //设备名称
            equipmentCategory: '', //设备类别
            equipmentType: '', //设备类型

            bidSegment: '', //所属标段
            position: '', //放置位置
            supplier: '', //供应商
            state: '', //状态,
            supplierClassificationList: [], //供应商
            supplierClassificationList2: [],
            equipmentCategoryList: [], //设备类别
            equipmentTypeList: [], //设备类型
            modelTypeList2: [], //设备类型
            bidSegmentList: [], //所属标段
            detailsVisible: false, //详情弹框值
            changeId: '', //修改ID
            usageState: '', //获取详情-使用状态
            deviceStatus: '', //获取详情-设备状态
            assetImg: '', //获取详情-设备图片
            assetCoding: '', //获取详情-资产编号
            assetName: '', //获取详情-资产名称
            manufacturer: '', //获取详情-生产厂家
            informationEquipmentType: '', //获取详情-设备型号
            assetLocation: '', //获取详情-资产位置
            attributeList: [], //资产特殊属性列表
            bindVisible: false, //绑定弹框值
            bindId: '',
            assetStatusList: [],
            scale: '',
            equipmentPurchase: '',
            equipmentOperating: '',
            model: {},
            upkeepCount: '',
            upkeepTimeBY: '',
            upkeepTimeWX: '',
            zsVisible: false,

            knowlegeName: '',
            category: '',
            equipmentMode: '',
            equipmentSpecs: '',
            equipmentTypeName: '',
            type: '',
            knowlegeItemVoList: [],
            equipmentModelList: [],
            equipmentSpecsList: [],
            url: {
                list: "/settle/customer/list",
                delete: "/iot/varinfo/delete",
                deleteBatch: "/iot/varinfo/deleteBatch",
                importExcelUrl: "equipment/optEquipment/import",
                exportXlsUrl: "/equipment/optEquipment/export",
                exportXlsUrl2: "/equipment/optEquipment/downloadModel",
            },
            //token header
            tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
            equipmentCategory2: '',
            equipmentCategory2List: [],
            workingStatusList: [],
            equipmentRevstop: '',

            // 上传设备类型图片
            changeImgVisible: false,
            equipType: '', // 设备类型编码
            stateList: [
                {
                    height: '',
                    imageType: '',
                    imgUrl: '',
                    width: ''
                }

            ],
            radioValue: '',
            disableMixinCreated: false,
            jumpPagNum: ''
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
        }
    },
    created() {
        this.columns = colAuthFilter(this.columns, 'opt:');
//  	this.initDictConfig();
    },
    mounted() {
        //设备台账-获取设备类型
        getEquipmentOptType(this);
        //获取设备类别值
        getEquipmentCategoryData(this);
        //获取设备类型值
        getEquipmentTypeData(this);
        //获取所属标段值
        getBidSegmentData(this);
        //获取供应商值-新增或修改
        getQueryNameList(this);
        //获取供应商值-筛选
        getQueryNameList2(this);
        //获取资产状态
        getAssetStatus(this);
        //设备台账列表初始化获取数据
        setTimeout(() => {
            this.updata();
        }, 500);
        //设备台账获取设备状态列表值
        getAgencyStatus(this);

    },
    methods: {
        toPage() {
            this.ipagination.current = this.jumpPagNum * 1;
            this.updata();
        },
        initDictConfig() {
        },
        // 添加设备类型图片
        changeImg() {  // 设备类型主题按钮
            this.changeImgVisible = true;
            this.stateList = [
                {
                    height: '',
                    imageType: '',
                    imgUrl: '',
                    width: ''
                }

            ]
        },
        changeImgOnOk() { // 设备类型主题确定按钮

            if (this.equipType == '') {
                this.$message.info('设备类型不能为空!');
                return;
            }
            let data;
            data = {
                equipmentType: this.equipType,
                fileImg: this.stateList[0].imgUrl
            }
            addEquiImg(data, this)
        },
        changeImgCancel() { // 设备类型主题取消按钮
            this.changeImgVisible = false;
        },
        equipTypeChange() {

        },
        //新增修改图片上传
        upfileClick(e, index) {
//          console.log(index)
            let file = e.target.files[0];
            let param = new FormData();
            param.append('file', file);
            console.log(param)
            $('.uploadBtnB').val('');
            equiImgUpload(param, this, 0);
        },
        //新增修改图片删除
        fileImgRemove(index) {
//          this.stateList[index].imgUrl='';
        },
        //设备台账列表初始化获取数据
        updata() {
            let data = {
                equipmentSn: $.trim(this.queryParam.equipmentNumber),
                equipmentSupplier: this.queryParam.supplier == 0 ? '' : this.queryParam.supplier,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                equipmentType: this.queryParam.equipmentType == 0 ? '' : this.queryParam.equipmentType,
                equipmentRevstop: this.queryParam.equipmentRevstop ? this.queryParam.equipmentRevstop : '',
                equipmentCategory: this.equipmentCategory2,
                optSection: this.queryParam.optSection, // 标段
                optLocation: this.queryParam.optLocation, // 放置位置
            }
            this.loading = true;
            getEquipmentLedgerData(data, this);
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //新增
        handleAdd(type, data) {
            this.addReset();
            this.form.setFieldsValue({equipmentPurchase: null})
            this.form.setFieldsValue({equipmentOperating: null})
            if (type == 'add') {
                this.drawerTitle = '新增设备台账';
            } else if (type == 'change') {
                this.drawerTitle = '修改设备台账';
                console.log(data)
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
            this.equipmentSupplierName = data.equipmentSupplierName;
            this.equipmentTypeName = data.equipmentTypeName;
            this.equipmentModelName = data.equipmentModelName;
            this.equipmentSpecsName = data.equipmentSpecsName;
            getEquipmentLedgerChangeData(res, this);
        },
        //表格属性改变
        handleTableChange(pagination) {

            this.ipagination.current = pagination.current;
            this.updata();
        },
        //设备详情点击
        deviceDetailsClick(data) {

            let res = {
                id: data.id
            }
            getEquipmentAssetsData(res, this);
        },
        //关闭设备详情弹框
        closeDeviceDetails() {
            $('#markBg').removeClass('on');
            $('.informationBox').hide();
        },
        //新增/修改设备台账关闭
        onClose() {
            this.visible = false;

        },

        //新增设备台账信息
        addSubmit() {
            console.log(this.stateList, 'this.stateListthis.stateList')
            let type;
            if (this.drawerTitle.indexOf('新增') != -1) {
                type = 'add';
            } else {
                type = 'modify';
            }
//          if (!this.equipmentNumber) {
//              this.$message.info('设备编号不能为空!');
//              return;
//          }

            // if(!this.equipmentName){
            //   this.$message.info('设备名称不能为空!');
            //   return;
            // }
            // if(!this.equipmentCategory){
            //   this.$message.info('设备类别不能为空!');
            //   return;
            // }

//          if (!this.equipmentType) {
//              this.$message.info('设备类型不能为空!');
//              return;
//          }

            // if(!this.scaleModel){
            //   this.$message.info('规格型号不能为空!');
            //   return;
            // }
            // if(!this.bidSegment){
            //   this.$message.info('所属标段不能为空!');
            //   return;
            // }
            // if(!this.position){
            //   this.$message.info('放置位置不能为空!');
            //   return;
            // }
            // if(!this.supplier){
            //   this.$message.info('供应商不能为空!');
            //   return;
            // }


//          if (!this.state) {
//              this.$message.info('资产状态不能为空!');
//              return;
//          }
//          if (!this.equipmentRevstop) {
//              this.$message.info('设备状态不能为空!');
//              return;
//          }

            this.form.validateFields((err, values) => {
                if (!err) {
                    let formData = Object.assign(this.model, values);

                    formData.equipmentOperating = formData.equipmentOperating ? formData.equipmentOperating.format() : null;
                    formData.equipmentPurchase = formData.equipmentPurchase ? formData.equipmentPurchase.format() : null;
                    console.log(formData)
                    if (new Date(formData.equipmentPurchase).getTime() > new Date(formData.equipmentOperating).getTime()) {
                        this.$message.info('购置时间不得大于投入运营时间！');
                        return;
                    }
                    let data;
                    if (type == 'add') {
                        data = {
                            equipmentSn: $.trim(this.equipmentNumber),
                            equipmentName: $.trim(this.equipmentName),
                            equipmentCategory: this.equipmentCategory,
                            equipmentType: this.equipmentType,
                            equipmentMode: $.trim(this.equipmentMode),
                            equipmentSection: this.bidSegment,
                            equipmentLocation: $.trim(this.position),
                            equipmentSupplier: this.supplier == 0 ? '' : this.supplier,
                            equipmentStatus: this.state,
                            equipmentSpecs: $.trim(this.equipmentSpecs),
                            equipmentOperating: formData.equipmentOperating ? this.moment(formData.equipmentOperating).format('YYYY-MM-DD') : '',
                            equipmentPurchase: formData.equipmentPurchase ? this.moment(formData.equipmentPurchase).format('YYYY-MM-DD') : '',
                            equipmentRevstop: this.equipmentRevstop,
                            equipmentImg: this.stateList[0].imgUrl //设备图片
                        }
                        addEquipmentLedgerData(data, this);
                    } else {
                        data = {
                            id: this.changeId,
                            equipmentSn: $.trim(this.equipmentNumber),
                            equipmentName: $.trim(this.equipmentName),
                            equipmentCategory: this.equipmentCategory,
                            equipmentType: this.equipmentType,
                            equipmentMode: $.trim(this.equipmentMode),
                            equipmentSection: this.bidSegment,
                            equipmentLocation: $.trim(this.position),
                            equipmentSupplier: this.supplier == 0 ? '' : this.supplier,
                            equipmentStatus: this.state,
                            equipmentSpecs: $.trim(this.equipmentSpecs),
                            equipmentOperating: formData.equipmentOperating ? this.moment(formData.equipmentOperating).format('YYYY-MM-DD') : '',
                            equipmentPurchase: formData.equipmentPurchase ? this.moment(formData.equipmentPurchase).format('YYYY-MM-DD') : '',
                            equipmentRevstop: this.equipmentRevstop,
                            equipmentImg: this.stateList[0].imgUrl
                        }
                        changeEquipmentLedgerData(data, this);
                    }
                    console.log(data, 'datadata')
                }
            })


        },
        //重置新增
        addReset() {
            this.equipmentNumber = '';
            this.equipmentName = '';
            this.equipmentCategory = '';
            this.equipmentType = '';
            this.equipmentSpecs = '';
            this.bidSegment = '';
            this.position = '';
            this.supplier = '';
            this.state = '';
            this.equipmentMode = '';
            this.model = {};
            this.equipmentRevstop = '';
        },
        //详情弹框确定
        informationOk() {
            this.detailsVisible = false;
        },
        //详情弹框关闭
        informationCancel() {
            this.detailsVisible = false;
        },
        //启停用效果
        stateChange(data, id) {
            console.log(data);
            console.log(id);
            let res = {
                id: id,
                state: data ? '1' : '0'
            }
            optEquipmentQueryRevstopById(res, this);
        },
        //点击知识详情
        zsdetails(data) {
            console.log(data);
            // this.detailsTitle=data.ownedResourcesName;
            knowlegeIdData({
                id: data.id
            }, this);


        },
        //知识详情关闭
        zsCancel() {
            this.zsVisible = false;
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
            console.log(data)
            let res = {
                pcate: data
            }
            getEquipmentSpecsData(res, this);
        },
        //导出
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let param = {...this.queryParam};
            param.equipmentSupplier = this.queryParam.supplier;
            if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
                param['selections'] = this.selectedRowKeys.join(",")
            }
            console.log("导出参数", param)
            downFile(this.url.exportXlsUrl, param).then((data) => {
                if (!data) {
                    this.$message.warning("文件下载失败")
                    return
                }
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
                } else {
                    let url = window.URL.createObjectURL(new Blob([data]))
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url
                    link.setAttribute('download', fileName + '.xls')
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link); //下载完成移除元素
                    window.URL.revokeObjectURL(url); //释放掉blob对象
                }
            })
        },
        //下载导入模板
        handleExportXls2(fileName) {

            downFile(this.url.exportXlsUrl2).then((data) => {
                if (!data) {
                    this.$message.warning("文件下载失败")
                    return
                }
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(new Blob([data]), fileName + '.xls')
                } else {
                    let url = window.URL.createObjectURL(new Blob([data]))
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url
                    link.setAttribute('download', fileName + '.xls')
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link); //下载完成移除元素
                    window.URL.revokeObjectURL(url); //释放掉blob对象
                }
            })
        },
        /* 导入 */
        handleImportExcel(info) {
            if (info.file.status !== 'uploading') {
                console.log(info.file, info.fileList);
            }
            if (info.file.status === 'done') {
                if (info.file.response.success) {
                    // this.$message.success(`${info.file.name} 文件上传成功`);
                    if (info.file.response.code === 201) {
                        let {message, result: {msg, fileUrl, fileName}} = info.file.response
                        let href = window._CONFIG['domianURL'] + fileUrl
                        this.$warning({title: message,content: ('<div><span> {msg} </span><br/><span> 具体详情请 <a href = {href} target = "_blank" download = {fileName} > 点击下载 </a> </span></div>')
                    })
                    } else {
                        this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
                    }
                    this.updata()
                } else {
                    this.$message.error(`${info.file.name} ${info.file.response.message}.`);
                }
            } else if (info.file.status === 'error') {
                this.$message.error(`文件上传失败: ${info.file.msg} `);
            }
        },
        //设备类别筛选
        callback(data) {
            console.log(data);
            this.equipmentCategory2 = data;
            this.ipagination.current = 1;
            this.jumpPagNum = '';
            this.updata();
        }
    }
}