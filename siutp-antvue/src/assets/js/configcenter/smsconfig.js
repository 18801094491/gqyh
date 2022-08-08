import {
    updateSMSstatus,
    getSMSList,
    addSMS,
    getStoreUserListData
} from '@/api/configcenter-t/SMSConfig.js'
import { downFile } from '@/api/manage'
import searchReset from '@/mixins/searchReset.js'

export default {
    name: "SMSConfig",
    mixins: [searchReset],
    components: {

    },
    data() {
        return {
            scrollX: {},
            drawerTitle: '新增', // 新增或修改弹框title
            queryParam: {
                supplier: '0'
            }, //搜索属性集合
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
            /* 分页参数 */

            visible: false,

            // 表格表头数据
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
                title: '配置名称',
                align: "center",
                width: 200,
                dataIndex: 'templateName',
                ellipsis: true,
            },

            {
                title: '发送人员',
                align: "center",
                width: 200,
                dataIndex: 'usersName',
                ellipsis: true,
            },
            {
                title: '短信code',
                align: "center",
                width: 200,
                dataIndex: 'templateCode',
                ellipsis: true,
            },
            {
                title: '短信签名',
                align: "center",
                width: 200,
                dataIndex: 'signName',
                className: "myTd",
                ellipsis: true,
            },
            //              {
            //                  title: '模板字段',
            //                  align: "center",
            //                  width: 200,
            //                  dataIndex: 'templateContent',
            //                  className: "myTd",
            //                  ellipsis: true,
            //              },
            {
                title: '状态',
                align: "center",
                width: 200,
                dataIndex: 'templateStatus',
                scopedSlots: {
                    customRender: 'templateStatus'
                },

            },
            {
                title: '操作',
                align: "center",
                width: 150,
                dataIndex: 'maintain',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            },
            ],
            tableDataSource: [],
            workingStatusList: [],
            isKey: false, // 配置key输入框是否可以修改
            configRules: {
                configName: [{
                    required: true,
                    message: '配置名称不能为空',
                    trigger: 'blur'
                },
                ],
            },

            form: {
                templateName: '', // 配置名称
                usersId: undefined, // 发送人员
                templateCode: "", // 模板code
                signName: "", // 模板标签
                //              templateContent: "", // 模板keys
                moduleId: '', // 模块id,
                templateStatus: '', // 启停用状态
            },
            rules: {
                templateName: [{
                    required: true,
                    message: '配置名称不能为空',
                    trigger: 'blur'
                },
                    // {
                    //     min: 3,
                    //     max: 5,
                    //     message: 'Length should be 3 to 5',
                    //     trigger: 'blur'
                    // },
                ],
                usersId: [{
                    required: true,
                    message: '请选择发送人员',
                    trigger: 'change'
                }],
                templateCode: [{
                    required: true,
                    message: '请输入模板code',
                    trigger: 'blur'
                },
                ],
                signName: [{
                    required: true,
                    message: '请输入模板标签',
                    trigger: 'blur'
                },
                ],
                //              templateContent: [{
                //                      required: true,
                //                      message: '请输入模板keys',
                //                      trigger: 'blur'
                //                 },
                //              ],
            },
            managerList: [],
            updataId: '', // 列表修改时的每行id
            url: {
                exportXlsUrl: '/message/smsTemplateConfig/export'
            },
            // selectedRowKeys:[], // 表格复选框
            selectedRowKeys: [],
            /* table选中records*/
            selectionRows: [],

            fullscreen: false
        }
    },
    mounted() {
        //数据初始化
        this.updata(); // 测试表格数据
        // getAgencyStatus(this);
        let data = {
            name: ''
        }
        getStoreUserListData(data, this);
    },
    methods: {
        onSubmit() {
            let type, data;
            if (this.drawerTitle.indexOf('新增') != -1) {
                type = 'add';
            } else {
                type = 'modify';
            }
            this.$refs.ruleForm.validate(valid => {
                if (valid) {
                    // console.log(this.form.usersId)
                    // if (this.form.usersId == undefined || this.form.usersId.length==0){
                    //     this.$message.info('请选择发送人员');
                    //     return;
                    // }
                    if (type == 'add') {
                        data = {
                            templateName: $.trim(this.form.templateName),
                            templateCode: $.trim(this.form.templateCode),
                            signName: $.trim(this.form.signName),
                            templateStatus: this.form.templateStatus == true ? 1 : 0,
                            moduleId: this.form.moduleId,
                            usersId: this.form.usersId.join(','),
                        }
                        addSMS(data, this);
                    } else {
                        data = {
                            templateName: $.trim(this.form.templateName),
                            templateCode: $.trim(this.form.templateCode),
                            signName: $.trim(this.form.signName),
                            //                          templateContent: $.trim(this.form.templateContent),
                            templateStatus: this.form.templateStatus == true ? 1 : 0,
                            moduleId: this.form.moduleId,
                            usersId: this.form.usersId.join(','),
                            id: this.updataId
                        }
                        addSMS(data, this);
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },

        updata() { // 测试表格数据
            let data = {
                templateName: $.trim(this.queryParam.templateName), // 名称
                templateCode: this.queryParam.templateCode, // code
                templateConfig: this.queryParam.templateConfig, // 启停用状态
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            this.loading = true;
            getSMSList(data, this);
        },
        //启停用改变
        editWorkingStatusChange(data) {
            let res = {
                id: data.id,
                // configStatus: data.configStatus ? '1' : '0'
            }
            updateSMSstatus(res, this);
        },
        //查询
        searchQuery() {
            this.ipagination.current = 1;
            this.updata();
        },
        //新增
        handleAdd(type, data) {
            if (type == 'add') {
                this.drawerTitle = '新增';
                this.isKey = false;
            } else if (type == 'change') {
                this.drawerTitle = '修改';
                this.handleChange(data);
                this.updataId = data.id;
                this.isKey = true;
            }
            this.visible = true;
        },
        //修改
        handleChange(data) {
            this.form.templateName = data.templateName;
            this.form.templateCode = data.templateCode;
            this.form.signName = data.signName;
            //          this.form.templateContent = data.templateContent;
            this.form.usersId = data.usersId ? data.usersId.split(',') : [];
            this.form.templateStatus = data.templateStatus;
            this.form.moduleId = data.moduleId;
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
        //重置新增
        addReset() {
            this.form.templateName = '';
            // this.form.region = data.configDescription;
            this.form.templateCode = '';
            this.form.signName = '';
            //          this.form.templateContent = '';
            this.form.usersId = undefined;
            this.form.templateStatus = '';
        },
        onSelectChange(selectedRowKeys, selectionRows) {
            // console.log(selectionRows)
            this.selectedRowKeys = selectedRowKeys;
            this.selectionRows = selectionRows;
        },
        //导出文件
        handleExportXls(fileName) {
            if (!fileName || typeof fileName != "string") {
                fileName = "导出文件"
            }
            let param = {
                ...this.queryParam
            };
            if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
                param['selections'] = this.selectedRowKeys.join(",")
            }
            // console.log("导出参数", param)
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
    }

}
//smsconfig页面组件混入的smsconfig.js