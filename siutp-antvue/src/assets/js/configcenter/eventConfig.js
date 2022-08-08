import { updateEventStatus, getEventList, addEvent, SMSDropdown } from '@/api/configcenter-t/eventConfig.js'
import { downFile } from '@/api/manage'
import searchReset from '@/mixins/searchReset.js'

export default {
    name: "eventConfig",
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
            tableDataSource: [], //表格数据源
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
                title: '事件名称',
                align: "center",
                width: 200,
                dataIndex: 'eventName',
                ellipsis: true,
            },

            {
                title: '事件code',
                align: "center",
                width: 200,
                dataIndex: 'eventCode',
                ellipsis: true,
            },
            {
                title: '配置名称',
                align: "center",
                width: 200,
                dataIndex: 'templateName',
                className: "myTd",
                ellipsis: true,
            },
            {
                title: '状态',
                align: "center",
                width: 200,
                dataIndex: 'eventStatus',
                scopedSlots: {
                    customRender: 'eventStatus'
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
            form: {
                eventName: '', // 事件名称
                eventCode: "", // 事件code
                templateId: undefined, // 短信模板id
                eventStatus: '', // 启停用状态
            },
            rules: {
                eventName: [{
                    required: true,
                    message: '请输入事件名称',
                    trigger: 'blur'
                },
                    // {
                    //     min: 3,
                    //     max: 5,
                    //     message: 'Length should be 3 to 5',
                    //     trigger: 'blur'
                    // },
                ],
                templateId: [{
                    required: true,
                    message: '请选择短信配置',
                    trigger: 'change'
                }],
                eventCode: [{
                    required: true,
                    message: '请输入事件code',
                    trigger: 'blur'
                },
                ],
            },
            SMSDropdownList: [], // 短信模板配置下拉
            updataId: '', // 列表修改时的每行id
            url: {
                exportXlsUrl: '/message/smsEvent/export'
            },
            selectedRowKeys: [], // 表格复选框
            /* table选中records*/
            selectionRows: [],
        }
    },
    mounted() {
        //数据初始化
        this.updata(); // 测试表格数据
        let data = {
            templateName: ''
        }
        SMSDropdown(data, this);
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
                    if (type == 'add') {
                        data = {
                            eventName: $.trim(this.form.eventName),
                            eventCode: $.trim(this.form.eventCode),
                            eventStatus: this.form.eventStatus == true ? 1 : 0,
                            templateId: this.form.templateId,
                        }
                        console.log(data, "2434335")
                        addEvent(data, this);
                    } else {
                        data = {
                            eventName: $.trim(this.form.eventName),
                            eventCode: $.trim(this.form.eventCode),
                            eventStatus: this.form.eventStatus == true ? 1 : 0,
                            templateId: this.form.templateId,
                            id: this.updataId
                        }
                        console.log(data, "fjdsfhdjshfds")
                        addEvent(data, this);
                    }
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        // resetForm() {
        //     this.$refs.ruleForm.resetFields();
        //     this.visible = false;
        //     this.updata();
        // },


        updata() { // 测试表格数据
            let data = {
                eventName: $.trim(this.queryParam.eventName), // 名称
                eventCode: this.queryParam.eventCode, // code
                eventStatus: this.queryParam.eventStatus, // 启停用状态
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            this.loading = true;
            getEventList(data, this);
        },
        //启停用改变
        editWorkingStatusChange(data) {
            let res = {
                id: data.id,
            }
            updateEventStatus(res, this);
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
            } else if (type == 'change') {
                this.drawerTitle = '修改';
                this.handleChange(data);
                this.updataId = data.id;
            }
            this.visible = true;
        },
        //修改
        handleChange(data) {
            this.form.eventName = data.eventName;
            this.form.eventCode = data.eventCode;
            //          this.form.templateId = data.usersId?data.usersId.split(','):[];
            this.form.eventStatus = data.eventStatus;
            this.form.templateId = data.templateId;
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
            this.form.eventName = '';
            // this.form.region = data.configDescription;
            this.form.eventCode = '';
            this.form.templateId = undefined;
            this.form.eventStatus = '';
        },
        onSelectChange(selectedRowKeys, selectionRows) {
            // console.log(selectionRows)
            this.selectedRowKeys = selectedRowKeys;
            this.selectionRows = selectionRows;
        },
        //导出
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
    }

}
//eventConfig页面组件混入的eventConfig.js