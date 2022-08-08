import SettleUseralotModal from '@views/operationCenter/userInstallation/modules/SettleUseralotModal.vue'
import pick from 'lodash.pick'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {getStoreUserListData, installationProgress} from '@api/operationCenter/userInstallation/settleUseralotList.js'

export default {
    name: "SettleUseralotList",
    mixins: [JeecgListMixin],
    components: {
        SettleUseralotModal
    },
    data() {
        return {
            dataSource: [],
            description: '用户报装',
            // 表头
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
                    title: '客户名称',
                    align: "center",
                    dataIndex: 'customerName'
                },
                {
                    title: '报装时间',
                    align: "center",
                    dataIndex: 'lotTime'
                },
                {
                    title: '报装内容',
                    align: "center",
                    dataIndex: 'lotContent'
                },
                {
                    title: '任务分配人员',
                    align: "center",
                    dataIndex: 'taskName'
                },
                {
                    title: '实施人联系电话',
                    align: "center",
                    dataIndex: 'telephone'
                },
                {
                    title: '安装状态',
                    align: "center",
                    dataIndex: 'installStateValue'
                },
                {
                    title: '安装时间',
                    align: "center",
                    dataIndex: 'installTime'
                },
                {
                    title: '用途',
                    align: "center",
                    dataIndex: 'purpose'
                },
                {
                    title: '审核验收意见',
                    align: "center",
                    dataIndex: 'accepidea'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    width: 200,
                    fixed: 'right',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                }
            ],
            url: {
                list: "/settle/settleUseralot",
                delete: "/settle/settleUseralot/delete",
                deleteBatch: "/settle/settleUseralot/deleteBatch",
                exportXlsUrl: "settle/settleUseralot/exportXls",
                importExcelUrl: "settle/settleUseralot/importExcel",
            },

            installationProgressvisible: false,
            confirmLoading2: false,
            labelCol: {
                xs: {span: 24},
                sm: {span: 6},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },
            examinevisible: false,
            confirmLoading3: false,
            accepidea: '',
            managerList: [],
            taskNameId: '',
            form2: this.$form.createForm(this),
            form: this.$form.createForm(this),
            model2: {},
            changeId: ''

        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    mounted() {
        //新增客户名称获取下拉值
        let data = {
            name: ''
        }
        getStoreUserListData(data, this);
    },
    methods: {
        //安装进度按钮
        handleEdit2(record) {
            this.installationProgressvisible = true;
            this.form2.resetFields();
            this.model2 = Object.assign({}, record);
            this.$nextTick(() => {
                this.form2.setFieldsValue(pick(this.model2, 'taskId', 'telephone', 'installTime', 'installState'))
                //时间格式化
                this.form2.setFieldsValue({installTime: this.model2.installTime ? moment(this.model2.installTime) : null})

            });
        },
        // 安装进度OK
        installationProgresshandleOk() {

            const that = this;
            // 触发表单验证
            this.form2.validateFields((err, values) => {
                if (!err) {
                    if (!values.taskId) {
                        this.$message.info('任务分配人员不能为空!');
                        return;
                    }
                    if (!values.telephone) {
                        this.$message.info('实施人联系电话不能为空!');
                        return;
                    }
                    if (!values.installTime) {
                        this.$message.info('安装时间不能为空!');
                        return;
                    }
                    that.confirmLoading = true;
                    let formData = Object.assign(this.model2, values);
                    //时间格式化
                    formData.installTime = formData.installTime ? formData.installTime.format('YYYY-MM-DD HH:mm') : null;

                    let data = {
                        taskId: formData.taskId,
                        telephone: formData.telephone,
                        installTime: formData.installTime,
                        installState: formData.installState,
                        id: formData.id
                    }
                    console.log(data)
                    installationProgress(data, this);
                }
            })
        },
        // 安装进度取消
        installationProgresshandleCancel() {
            this.installationProgressvisible = false;
        },
        //审核按钮
        handleEdit3(data) {
            this.examinevisible = true;
            this.accepidea = '';
            this.changeId = data.id
        },
        //审核OK
        examineOk() {

            let data = {
                accepidea: this.accepidea,
                id: this.changeId
            }
            installationProgress(data, this)
        },
        //审核取消
        examineCancel() {
            this.examinevisible = false;
        },

    }
}