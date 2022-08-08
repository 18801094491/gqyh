import {
    getAgencyStatus,
    synchronousCacheByGet,
    getTestTableList,
    addConfig,
    updateConfigstatus
} from '@/api/configcenter-t/paraConfig.js'

import searchReset from '@/mixins/searchReset.js'

const columns1 = [{
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: {
        customRender: 'name'
    },
},
{
    title: 'Age',
    dataIndex: 'age',
    key: 'age',
    width: 80,
},
{
    title: 'Address',
    dataIndex: 'address',
    key: 'address 1',
    ellipsis: true,
},
{
    title: 'Long Column Long Column Long Column',
    dataIndex: 'address',
    key: 'address 2',
    ellipsis: true,
},
{
    title: 'Long Column Long Column',
    dataIndex: 'address',
    key: 'address 3',
    ellipsis: true,
},
{
    title: 'Long Column',
    dataIndex: 'address',
    key: 'address 4',
    ellipsis: true,
},
];

export default {
    name: "gisModel",
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
                dataIndex: 'configName',

            },

            {
                title: '配置key',
                align: "center",
                dataIndex: 'configKey',

            },
            {
                title: '配置值',
                align: "center",
                dataIndex: 'configValue',

            },
            {
                title: '配置描述',
                align: "center",
                dataIndex: 'configDescription',
                className: "myTd"
            },
            {
                title: '启停用状态',
                align: "center",
                dataIndex: 'configStatus',
                scopedSlots: {
                    customRender: 'configStatus'
                },

            },
            {
                title: '操作',
                align: "center",
                width: 120,
                dataIndex: 'maintain',
                fixed: 'right',
                scopedSlots: {
                    customRender: 'maintainBtn'
                },
            },
            ],
            tableDataSource: [],
            workingStatusList: [],
            configName: "", // 配置名称
            configDescription: "", // 配置描述
            configStatus: "", // 启停用,1-启用,2-停用
            configKey: "", // 配置key
            configValue: "", // 配置值
            isKey: false, // 配置key输入框是否可以修改
            accessValues: "", // 配置项
            configValueList: [], // 配置值下拉框

            columns1,
            data1: [{
                key: '1',
                name: 'John Brown',
                age: 32,
                address: 'New York No. 1 Lake Park, New York No. 1 Lake Park',
                tags: ['nice', 'developer'],
            },
            {
                key: '2',
                name: 'Jim Green',
                age: 42,
                address: 'London No. 2 Lake Park, London No. 2 Lake Park',
                tags: ['loser'],
            },
            {
                key: '3',
                name: 'Joe Black',
                age: 32,
                address: 'Sidney No. 1 Lake Park, Sidney No. 1 Lake Park',
                tags: ['cool', 'teacher'],
            },
            ]
        }
    },
    computed: {

    },
    mounted() {
        //数据初始化
        this.updata(); // 测试表格数据
        getAgencyStatus(this);
    },
    methods: {
        updata() { // 测试表格数据
            let data = {
                configName: $.trim(this.queryParam.configName), //configName
                configStatus: this.queryParam.configStatus,
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize
            }
            this.loading = true;
            getTestTableList(data, this);
        },
        //启停用改变
        editWorkingStatusChange(data) {
            let res = {
                id: data.id,
                configStatus: data.configStatus ? '1' : '0'
            }
            updateConfigstatus(res, this);
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
                this.changeId = data.id;
                this.isKey = true;
            }
            this.visible = true;
        },
        //同步缓存
        synchronousCache() {
            let data = {
                url: '/system/systemConfig/sync'
            }
            synchronousCacheByGet(data, this);
        },
        //修改
        handleChange(data) {
            console.log(data, 'data')
            this.configName = data.configName;
            this.configDescription = data.configDescription;
            this.configKey = data.configKey;
            this.configValue = data.configValue;
            this.configStatus = data.configStatus;
            this.configValueList = data.configValueList;
            this.accessValues = data.accessValues;
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

        //新增样例测试数据
        addSubmit() {
            let type;
            if (this.drawerTitle.indexOf('新增') != -1) {
                type = 'add';
            } else {
                type = 'modify';
            }
            if (!this.configName) {
                this.$message.info('名称不能为空!');
                return;
            }
            if (!this.configDescription) {
                this.$message.info('描述不能为空!');
                return;
            }
            if (!this.configKey) {
                this.$message.info('配置key不能为空!');
                return;
            }
            if (!this.configValue) {
                this.$message.info('配置值不能为空!');
                return;
            }
            if (!this.accessValues) {
                this.$message.info('配置项不能为空!');
                return;
            }
            let data;
            if (type == 'add') {
                data = {
                    configName: $.trim(this.configName),
                    configDescription: $.trim(this.configDescription),
                    configKey: $.trim(this.configKey),
                    configValue: $.trim(this.configValue),
                    configStatus: this.configStatus,
                    accessValues: this.accessValues,
                }
                addConfig(data, this);
            } else {
                data = {
                    configName: $.trim(this.configName),
                    configDescription: $.trim(this.configDescription),
                    configKey: $.trim(this.configKey),
                    configValue: $.trim(this.configValue),
                    configStatus: this.configStatus == true ? 1 : 0,
                    accessValues: this.accessValues,
                    id: this.changeId
                }
                addConfig(data, this);
            }
        },
        //重置新增
        addReset() {
            this.configName = '';
            this.configDescription = '';
            this.configKey = '';
            this.configValue = '';
            this.accessValues = '';
        },
    }

}
//sysParam页面组件混入的sysParam.js
