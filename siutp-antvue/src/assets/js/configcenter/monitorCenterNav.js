import searchReset from '@/mixins/searchReset.js'
import {
    getGisGisListData,
    getGisTypeList,
    GISScreeningMaintainImgUpload,
    gisGisOne,
    gisGisEditDataShow,
    gisGisEditDataStatus
} from '@api/configcenter-t/monitorCenterNav.js'

export default {
    name: 'modelManagement',
    mixins: [searchReset],
    data() {
        return {
            description: '筛选维护',
            labelCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 10
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
            queryParam: {},
            drawerTitle: '新增',
            visible: false,
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
                title: '模型类型',
                align: "center",
                dataIndex: 'modelTypeName',

            },
            {
                title: '模型导航图片',
                align: "center",
                dataIndex: 'modelThumb',
                scopedSlots: {
                    customRender: 'modelThumb'
                },
            },
            {
                title: '导航名称',
                align: "center",
                dataIndex: 'navName',

            },
            {
                title: '默认选中',
                align: "center",
                dataIndex: 'dataShow',
                scopedSlots: {
                    customRender: 'dataShow'
                },
            },
            {
                title: '启停用状态',
                align: "center",
                dataIndex: 'navStatus',
                scopedSlots: {
                    customRender: 'navStatus'
                },

            },
            {
                title: '操作',
                align: "center",
                dataIndex: 'cz',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            }
            ],
            dataSource: [


            ], //表格数据源
            loading: false,
            /* 分页参数 */
            ipagination: false,

            getGisTypeList: [],
            modelType: '',
            navName: '',
            dataShow: '',
            navStatus: '',
            changeId: '',
            GISScreeningMaintainImg: ''
        }
    },
    mounted() {
        //数据初始化
        this.updata();
        //模型类型下拉值获取
        getGisTypeList(this);
    },
    methods: {
        //数据初始化
        updata() {
            let data = {
                modelType: this.queryParam.modelTypeCode
            }
            this.loading = true;
            getGisGisListData(data, this);
        },
        //查询
        searchQuery() {

            this.updata();
        },
        //新增、修改按钮
        handleAdd(type, record) {
            this.modelType = '';
            this.GISScreeningMaintainImg = '';
            this.navName = '';
            this.GISScreeningMaintainImg = '';
            this.dataShow = true;
            this.navStatus = true;
            if (type == 'add') {
                this.drawerTitle = '新增';

            } else {
                this.drawerTitle = '修改';
                this.modelType = record.modelType;
                this.GISScreeningMaintainImg = record.modelThumb;
                this.navName = record.navName;
                this.dataShow = record.dataShow;
                this.navStatus = record.navStatus;

                this.changeId = record.id;
            }
            this.visible = true;
        },
        //关闭弹框
        onClose() {
            this.visible = false;
        },
        //新增修改提交
        onSubmit() {
            if (!this.modelType) {
                this.$message.info('模型类型不能为空!');
                return;
            }
            if (!this.GISScreeningMaintainImg) {
                this.$message.info('上传图片不能为空!');
                return;
            }
            if (!this.navName) {
                this.$message.info('导航名称不能为空!');
                return;
            }

            let data;
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    modelType: this.modelType,
                    modelThumb: this.GISScreeningMaintainImg.substring(this.GISScreeningMaintainImg.indexOf('/res')),
                    navName: $.trim(this.navName),
                    dataShow: this.dataShow ? '1' : '0',
                    navStatus: this.navStatus ? '1' : '0',
                    id: ''
                }
            } else {
                data = {
                    modelType: this.modelType,
                    modelThumb: this.GISScreeningMaintainImg.substring(this.GISScreeningMaintainImg.indexOf('/res')),
                    navName: $.trim(this.navName),
                    dataShow: this.dataShow ? '1' : '0',
                    navStatus: this.navStatus ? '1' : '0',
                    id: this.changeId
                }
            }
            gisGisOne(data, this);
        },
        //图片上传
        upfileClick(e) {
            let file = e.target.files[0];
            let param = new FormData();
            param.append('file', file);
            console.log(param)
            $('.uploadBtnB').val('');
            GISScreeningMaintainImgUpload(param, this);

        },
        //图片删除
        fileImgRemove() {

            this.GISScreeningMaintainImg = '';
        },
        //默认选中滑动块
        dataShowChange(record, e) {
            console.log(e)
            let data = {
                id: record.id
            }
            gisGisEditDataShow(data, this, e, record);
        },
        //启停用状态滑动块
        navStatusChange(record, e) {
            let data = {
                id: record.id
            }
            gisGisEditDataStatus(data, this, e, record);
        }
    }
}
//monitorCenterNav页面组件混入的monitorCenterNav.js