import searchReset from '@/mixins/searchReset.js'
import {
    axios
} from '@/utils/request';

export default {
    name: 'modelManagement',
    mixins: [searchReset],
    data() {
        return {
            description: '素材管理',
            labelCol: {
                xs: {span: 24},
                sm: {span: 10},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },
            queryParam: {},
            drawerTitle: '新增',
            visible: false,
            columns: [
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
                    title: '模型编码',
                    align: "center",
                    dataIndex: 'resSn',

                },
                {
                    title: '模型类别',
                    align: "center",
                    dataIndex: 'modelTypeName',

                },
                {
                    title: '正常状态',
                    align: "center",
                    dataIndex: 'zc',
                    scopedSlots: {
                        customRender: 'zc'
                    },
                },
                {
                    title: '告警状态',
                    align: "center",
                    dataIndex: 'yc',
                    scopedSlots: {
                        customRender: 'yc'
                    },
                },
                {
                    title: '打开状态',
                    align: "center",
                    dataIndex: 'dk',
                    scopedSlots: {
                        customRender: 'dk'
                    },
                },
                {
                    title: '关闭状态',
                    align: "center",
                    dataIndex: 'gb',
                    scopedSlots: {
                        customRender: 'gb'
                    },
                },
                {
                    title: '图例展示',
                    align: "center",
                    dataIndex: 'legendShow',
                    scopedSlots: {
                        customRender: 'legendShow'
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
            dataSource: [], //表格数据源
            loading: false,
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

            stateList: [
                {
                    height: '',
                    imageType: '',
                    imgUrl: '',
                    width: ''
                }

            ],
            gisImageTypeList: [],
            getGisTypeList: [],
            modelTypeCode: '',
            changeId: ''
        }
    },
    mounted() {
        //数据初始化
        this.updata();
        //新增,修改里资源类型下拉值
        this.getGisImageType(this);
        //模型类别下拉获取值
        this.getGisTypes(this);
    },
    methods: {
        getGisModelResListData(data, _this){
            //素材管理-GIS模型库类型-分页列表查询
            axios.get('/gis/gisModelRes', {
                params: data
            }).then(res => {
                _this.loading = false;
                _this.dataSource = [];
                if (res.code * 1 == 200) {
                    let list = res.result.records;
                    list.map(index => {
                        _this.dataSource.push({
                            id: index.id,
                            list: index.list,
                            modelTypeCode: index.modelTypeCode,
                            modelTypeName: index.modelTypeName,
                            resSn: index.resSn,
                            legendShow: index.legendShow == "0" ? false : true
                        })
                    })
                    _this.ipagination.current = res.result.current;
                    _this.ipagination.total = res.result.total;
                } else {
                    _this.$message.info(res.message);
                    _this.dataSource = [];
                    _this.ipagination.current = 0;
                    _this.ipagination.total = 1;
                }
            })
        },
        gisModelResOne(data, _this) {
            //素材管理-GIS模型库类型-添加或修改
            axios.post('/gis/gisModelRes/one', data)
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.updata();
                        _this.visible = false;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
        },
        getGisImageType(_this) {
            //素材管理-获取图片类型接口
            axios.get('/sys/dict/getDictValue/gis_image_type')
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.gisImageTypeList = res.result;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
        },
        getGisTypes(_this) {
            //素材管理-获取模型分类列表
            axios.get('/sys/category/loadTreeRootCascade?pcode=A15')
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.getGisTypeList = res.result;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
        },
        gisImgUpload(data, _this, index) {
            //素材管理-模型图片上传
            axios.post('/sys/file/upload/image', data)
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.stateList[index].imgUrl = res.result.filePath;
                        _this.stateList[index].fileName = res.result.fileName;
                        _this.stateList[index].fileThumbPath = res.result.fileThumbPath;
                        _this.stateList[index].width = res.result.width;
                        _this.stateList[index].height = res.result.height;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
        },
        // 是否图例展示
        iconShow(data) {
            let res = {
                id: data.id
            }
            this.updateIconShow(res, this);
        },
        updateIconShow(data, _this) {
            axios({
                url: "/gis/gisModelRes/eidtShow",
                methods: "get",
                params: data
            }).then(res => {
                if (res.code == 200) {
                    _this.$message.info(res.message);
                    _this.ipagination.current = 1;
                    _this.updata();
                } else {
                    _this.$message.info(res.message);
                }
            })
        },
        //数据初始化
        updata() {
            let data = {
                modelTypeCode: this.queryParam.modelTypeCode,
                pageSize: this.ipagination.pageSize,
                pageNo: this.ipagination.current
            }
            this.loading = true;
            this.getGisModelResListData(data, this);
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //新增、修改按钮
        handleAdd(type, record) {
            this.modelTypeCode = '';


            if (type == 'add') {
                this.drawerTitle = '新增素材';
                this.stateList = [
                    {
                        height: '',
                        imageType: '',
                        imgUrl: '',
                        width: ''
                    }
                ];
            } else {
                this.stateList = [];
                this.drawerTitle = '修改素材';
                this.modelTypeCode = record.modelTypeCode;
                record.list.map(index => {
                    this.stateList.push({
                        height: index.height,
                        imageType: index.imageType,
                        imgUrl: index.imgUrl,
                        width: index.width,
                    })
                })
                this.changeId = record.id;
            }
            this.visible = true;
        },
        //关闭弹框
        onClose() {
            this.visible = false;
        },
        //表格页码变动触发数据改变
        handleTableChange(pagination) {
            this.ipagination.current = pagination.current;
            this.updata();
        },
        //新增修改提交
        onSubmit() {
            let data;
            if (!this.modelTypeCode) {
                this.$message.info('模型类别不能为空！');
                return;
            }
            let list = [];
            let arr = [];
            for (let i = 0; i < this.stateList.length; i++) {
                if (!this.stateList[i].imageType) {
                    this.$message.info('资源类型不能为空！');
                    return;
                }
                if (!this.stateList[i].imgUrl) {
                    this.$message.info('模型图片不能为空！');
                    return;
                }
                arr.push(this.stateList[i].imageType)
            }
            let num = 0;
            let arr2 = arr.sort();
            for (let j = 0; j < arr2.length; j++) {
                if (arr2[j] == arr2[j + 1]) {
                    num++;
                }
            }
            if (num > 0) {
                this.$message.info('资源类型不能相同！');
                return;
            }
            this.stateList.map(index => {
                list.push({
                    height: index.height,
                    imageType: index.imageType,
                    imgUrl: index.imgUrl.substring(index.imgUrl.indexOf('/res')),
                    width: index.width
                })
            })
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    list: list,
                    modelTypeCode: this.modelTypeCode
                }
            } else {
                data = {
                    list: list,
                    modelTypeCode: this.modelTypeCode,
                    id: this.changeId
                }
            }
           this.gisModelResOne(data, this);
        },
        //新增状态
        addState() {
            if (this.stateList.length >= 4) {
                this.$message.info('最多只能新增4个状态');
                return;
            }

            this.stateList.push(
                {
                    height: '',
                    imageType: '',
                    imgUrl: '',
                    width: ''

                }
            )

        },
        //新增修改图片上传
        upfileClick(e, index) {
            console.log(index)
            let file = e.target.files[0];
            let param = new FormData();
            param.append('file', file);
            console.log(param)
            $('.uploadBtnB').val('');
            this.gisImgUpload(param, this, index);
        },
        //新增修改图片删除
        fileImgRemove(index) {
            console.log(index)
            this.stateList[index].imgUrl = '';
        },
        //新增状态删除
        stateListRemove(index) {
            this.stateList.splice(index, 1);
        }
    }
}