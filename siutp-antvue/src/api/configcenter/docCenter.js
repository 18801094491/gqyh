import searchReset from '@/mixins/searchReset.js'
import {
    axios
} from '@/utils/request';
import {
    getDocType
} from '@/api/dict.js'

export default {
    name: 'documentCenter',
    mixins: [searchReset],
    data() {
        return {
            name: '文档中心',
            description: '文档中心',
            queryParam: {},
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
                    title: '文档名称',
                    align: "center",

                    dataIndex: 'docName',

                },
                {
                    title: '文档类别',
                    align: "center",

                    dataIndex: 'docTypeTitle',

                },


                {
                    title: '文档说明',
                    align: "center",

                    dataIndex: 'docDescription',

                },
                {
                    title: '文档排序',
                    align: "center",

                    dataIndex: 'displayOrder',

                },
                {
                    title: '创建时间',
                    align: "center",

                    dataIndex: 'createTime',

                },
                {
                    title: '状态',
                    align: "center",

                    dataIndex: 'status',
                    scopedSlots: {
                        customRender: 'status'
                    },
                },
                {
                    title: '操作',
                    align: "center",
                    width: 100,
                    dataIndex: 'maintain',
                    scopedSlots: {
                        customRender: 'action'
                    },
                },
            ],
            dataSource: [],
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
            loading: false,
            documentCenterTitle: '',
            documentCentervisible: false,
            fileList: [],
            inequipmentTypeList: [],
            docDescription: '',
            displayOrder: '',
            docName: '',
            docType: '',
            id: ''
        }
    },
    mounted() {
        //数据初始化
        this.updata();
        //获取文档类型
        getDocType(this);

    },
    methods: {
        docsStartOrStop(data, _this) {
            //停用启用
            axios.get('/oa/docs/startOrStop', {
                params: data
            })
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.updata();
                    } else {
                        _this.$message.info(res.message);

                    }
                })
        },
        oaDocs(data, _this) {
            //分页列表查询
            axios.get('/oa/docs', {
                params: data
            })
                .then(res => {
                    _this.loading = false;
                    _this.dataSource = [];
                    if (res.code * 1 == 200) {

                        let list = res.result.records;
                        list.map(index => {
                            _this.dataSource.push({
                                id: index.id,
                                docName: index.docName,
                                docType: index.docType,
                                displayOrder: index.displayOrder,
                                docDescription: index.docDescription,
                                createTime: index.createTime,
                                docTypeTitle: index.docTypeTitle,
                                status: index.status
                            })
                        })
                        console.log(_this.dataSource)
                        _this.ipagination.current = res.result.current;
                        _this.ipagination.total = res.result.total;
                    } else {
                        _this.$message.info(res.message);

                    }
                })
        },
        fileUploadCusDocs(data, _this) {
            axios.post('/sys/file/upload/cus/docs', data)
                .then(res => {
                    _this.fileList = [];
                    if (res.code * 1 == 200) {
                        _this.fileList.push({
                            fileName: res.result.fileName,
                            filePath: res.result.filePath
                        });

                        $('#uploadBtn').val('');
                    } else {
                        _this.$message.info(res.message);

                    }
                })
        },
        oaDocsOne(data, _this) {
            //文档新增或修改
            axios.post('/oa/docs/one', data)
                .then(res => {
                    if (res.code * 1 == 200) {
                        _this.updata();
                        _this.documentCentervisible = false;
                    } else {
                        _this.$message.info(res.message);

                    }
                })
        },
        //数据初始化
        updata() {
            let data = {
                docName: $.trim(this.queryParam.docName),
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            this.oaDocs(data, this);
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //新增修改
        handleAdd(type, record) {
            this.docName = '';
            this.docType = '';
            this.displayOrder = '';
            this.docDescription = '';
            this.fileList = [];
            if (type == 'add') {
                this.documentCenterTitle = '新增手册';
            } else {
                this.documentCenterTitle = '修改手册';
                this.id = record.id;
                this.docName = record.docName;
                this.docType = record.docType;
                this.displayOrder = record.displayOrder;
                this.docDescription = record.docDescription;
                this.fileList = [{
                    fileName: record.docName,
                    filePath: record.filePath
                }];
            }
            this.documentCentervisible = true;
        },
        handleTableChange(pagination) {
            this.ipagination.current = pagination.current;
            this.updata();
        },
        //新增修改弹框确定
        documentCenteronOk() {
            if (!this.docName) {
                this.$message.info('文档名称不能为空!');
                return;
            }
            if (!this.docType) {
                this.$message.info('文档类型不能为空!');
                return;
            }
            if (!this.displayOrder) {
                this.$message.info('文档排序不能为空!');
                return;
            }
            if (!this.docDescription) {
                this.$message.info('文档说明不能为空!');
                return;
            }
            if (!this.fileList.length) {
                this.$message.info('文档上传不能为空!');
                return;
            }
            let data;
            if (this.documentCenterTitle.indexOf('新增') != -1) {
                data = {
                    docName: $.trim(this.docName),
                    docType: this.docType,
                    displayOrder: $.trim(this.displayOrder),
                    docDescription: $.trim(this.docDescription),
                    docUrl: this.fileList[0].filePath
                }
            } else {
                console.log(this.fileList)
                data = {
                    docName: $.trim(this.docName),
                    docType: this.docType,
                    displayOrder: $.trim(this.displayOrder),
                    docDescription: $.trim(this.docDescription),
                    docUrl: this.fileList.length ? (this.fileList[0].filePath ? this.fileList[0].filePath.substring(this.fileList[0].filePath.indexOf('/res')) : '' ): '',
                    id: this.id
                }
            }
            this.oaDocsOne(data, this);

        },
        //新增修改弹框取消
        documentCenteronCancel() {
            this.documentCentervisible = false;
        },
        //新增上传文件
        upfileClick(e) {
            let file = e.target.files[0];
            if (file.type != 'application/pdf') {
                this.$message.error('请上传PDF格式文件!');
                $('#uploadBtn').val('');
                return;
            }
            ;

            let param = new FormData();
            param.append('file', file);
            console.log(param)
            this.fileUploadCusDocs(param, this);
        },
        //状态启停用
        changeStatus(res, e) {
            console.log(res);
            console.log(e);
            let data = {
                id: res.id,
                status: e ? '1' : '0'
            }
            this.docsStartOrStop(data, this);
        }
    },
}