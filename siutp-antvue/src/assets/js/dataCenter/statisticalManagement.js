import {
    JeecgListMixin
} from '@/mixins/JeecgListMixin'
import {
    getCycleType,
    getAgencyStatus,
    getChartType,
    getChartOpt,
    chartStsId,
    getChartIdVar,
    chartDelId,
    chartOne,
    getChartIdDetails,
    getChartIdVar2,
    synchronousCache
} from '@/api/dataCenter-t/statisticalManagement.js'
import '@/assets/less/dataCenter/statisticalManagement.less'

import moment from 'moment'

let tableData = [{
    id: '0',
    name: 'John Brown',
    age: 22,
    address: 'New York No. 1 Lake Park',
    tags: ['nice', 'developer'],
},
{
    id: '1',
    name: 'John Brown',
    age: 42,
    address: 'London No. 1 Lake Park',
    tags: ['loser'],
},
{
    id: '2',
    name: 'John Brown',
    age: 22,
    address: 'New York No. 1 Lake Park',
    tags: ['nice', 'developer'],
},
{
    id: '5',
    name: 'Joe Black',
    age: 3,
    address: 'Sidney No. 1 Lake Park',
    tags: ['cool', 'teacher'],
},
{
    id: '6',
    name: 'Joe Black',
    age: 342,
    address: 'Sidney No. 1 Lake Park',
    tags: ['cool', 'teacher'],
},
{
    id: '7',
    name: 'Joe Black',
    age: 62,
    address: 'Sidney No. 1 Lake Park',
    tags: ['cool', 'teacher'],
}]
export default {
    name: "statisticalManagement",
    mixins: [JeecgListMixin],

    data() {
        //合并数组单元格
        let mergeCells = (text, data, key, index) => {
            // 上一行该列数据是否一样
            if (index !== 0 && text === data[index - 1][key]) {
                return 0
            }
            let rowSpan = 1
            // 判断下一行是否相等
            for (let i = index + 1; i < data.length; i++) {
                if (text !== data[i][key]) {
                    break
                }
                rowSpan++
            }
            return rowSpan
        }
        return {
            description: '图表管理',
            // 表头
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
                title: '统计名称',
                align: "center",

                dataIndex: 'statisticName'
            },
            {
                title: '统计图类型',
                align: "center",

                dataIndex: 'chartType'
            },
            {
                title: '统计周期',
                align: "center",

                dataIndex: 'statisticCycle',
                customRender: function (text, record) {
                    if (record.statisticCycleType == '秒') {
                        return record.statisticCycle + record.statisticCycleType;
                    } else if (record.statisticCycleType == '分') {
                        return record.statisticCycle + record.statisticCycleType + '钟';
                    } else if (record.statisticCycleType == '时') {
                        return record.statisticCycle + '小' + record.statisticCycleType;
                    } else if (record.statisticCycleType == '日') {
                        return record.statisticCycle + record.statisticCycleType;
                    } else if (record.statisticCycleType == '月') {
                        return record.statisticCycle + '个' + record.statisticCycleType;
                    } else if (record.statisticCycleType == '年') {
                        return record.statisticCycle + record.statisticCycleType;
                    }

                }
            },
            {
                title: '统计状态',
                align: "center",

                dataIndex: 'statisticStatus',

                scopedSlots: {
                    customRender: 'statisticStatus'
                },
            },
            {
                title: '自定义',
                align: "center",
                dataIndex: "cycleTime",
                customRender: function (text, record) {
                    return text == '1' ? '是' : '否'
                }
            },
            {
                title: '展示顺序',
                align: "center",

                dataIndex: 'displayOrder'
            },
            {
                title: '开始时间',
                align: "center",

                dataIndex: 'startTime'
            },
            {
                title: '结束时间',
                align: "center",

                dataIndex: 'endTime'
            },
            {
                title: '操作',
                dataIndex: 'action',
                align: "center",

                scopedSlots: {
                    customRender: 'action'
                },
            }
            ],
            url: {
                list: "/statistic/chart",
                delete: "/equipment/equipmentUpkeep/delete",
                deleteBatch: "/equipment/equipmentUpkeep/deleteBatch",
                exportXlsUrl: "/equipment/equipmentUpkeep/export",
                importExcelUrl: "equipment/equipmentUpkeep/importExcel",
            },
            dataSource: [],
            statisticalManagementTitle: '',
            statisticalManagementvisible: false,
            cycleTypeList: [],
            workingStatusList: [],
            chartTypeList: [],
            deviceList2: [],
            items: [
                {
                    equipmentId: '',
                    serials: [
                        {
                            serialName: '',
                            variableName: ''
                        },

                    ],
                    varList: []
                },
            ],
            varList: [],
            statisticStatus: '',
            statisticCycle: '',
            statisticCycleType: '',
            chartType: '',
            statisticName: '',
            chartId: '',
            cycleTime: 0, // 自定义单选框
            displayOrder: '', // 展示顺序
            //          endTime:null, // 结束时间
            endTime: '23:59', // 结束时间
            //          startTime:null, // 开始时间
            startTime: '00:00', // 开始时间
            //          startTime:new Date().toLocaleDateString(), // 开始时间
            // 动态合并表格示例
            mergeDataSource: tableData,
            // 合并表格数据
            mergeColumns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                //                  customRender: function (t, r, index) {
                //                      return parseInt(index) + 1;
                //                  },
                customRender: (text, record, index) => {
                    const obj = {
                        children: text !== null ? text : '',
                        attrs: {}
                    }
                    obj.attrs.rowSpan = mergeCells(text, this.mergeDataSource, 'name', index)

                    return parseInt(index) + 1;
                }
            },
            {
                title: '分类名称',
                align: "center",

                dataIndex: 'name',
                customRender: (text, record, index) => {
                    const obj = {
                        children: text !== null ? text : '',
                        attrs: {}
                    }
                    obj.attrs.rowSpan = mergeCells(text, this.mergeDataSource, 'name', index)
                    return obj
                }
            },
            {
                title: '年龄',
                align: "center",

                dataIndex: 'age',
                customRender: (text, record, index) => {
                    const obj = {
                        children: text !== null ? text : '',
                        attrs: {}
                    }
                    obj.attrs.rowSpan = mergeCells(text, this.mergeDataSource, 'age', index)
                    return obj
                }
            },
            {
                title: '地址',
                align: "center",

                dataIndex: 'address',
                customRender: (text, record, index) => {
                    const obj = {
                        children: text !== null ? text : '',
                        attrs: {}
                    }
                    obj.attrs.rowSpan = mergeCells(text, this.mergeDataSource, 'address', index)
                    return obj
                }
            },
            ]
        }
    },
    computed: {
        importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
    },
    mounted() {
        //统计状态下拉值获取
        getAgencyStatus(this);
        //统计图类型下拉值获取
        getChartType(this);
        //新增修改-统计周期下拉值获取
        getCycleType(this);
        //新增修改-设备名称下拉值获取
        getChartOpt(this);
    },
    methods: {
        moment,
        changeTime(val, dateStrings, type) {
            if (type === 'startTime') {
                this.startTime = dateStrings
            } else {
                this.endTime = dateStrings
            }
        },
        getDisabledHours() {
            let hours = []
            let time = this.startTime
            let timeArr = time.split(':')
            for (var i = 0; i < parseInt(timeArr[0]); i++) {
                hours.push(i)
            }
            return hours
        },
        getDisabledMinutes(selectedHour) {
            let time = this.startTime
            let timeArr = time.split(':')
            let minutes = []
            if (selectedHour == parseInt(timeArr[0])) {
                for (var i = 0; i < parseInt(timeArr[1]); i++) {
                    minutes.push(i + 1)
                }
            }
            return minutes
        },
        getDisabledSeconds(selectedHour, selectedMinute) {
            let time = this.startTime
            let timeArr = time.split(':')
            let second = []
            if (selectedHour == parseInt(timeArr[0]) && selectedMinute == parseInt(timeArr[1])) {
                for (var i = 0; i < parseInt(timeArr[2]); i++) {
                    second.push(i)
                }
            }
            return second
        },
        onChange() { // 自定义单选框
            console.log()
        },
        onChangeStart(time, timeString) {
            this.startTime = timeString;
        },
        onChangeEnd(time, timeString) {
            this.endTime = timeString;
        },
        //新增/修改按钮弹框
        statisticalManagementChange(type, record) {
            //      	console.log(record,'recordrecord')
            this.statisticName = '';
            this.chartType = '';
            this.statisticCycle = '';
            this.statisticCycleType = '';
            this.statisticStatus = '';
            this.cycleTime = 0; // 自定义单选框
            this.displayOrder = ''; // 展示顺序
            this.endTime = '23:59'; // 结束时间
            this.startTime = '00:00'; // 开始时间
            this.items = [
                {
                    equipmentId: '',
                    serials: [
                        {
                            serialName: '',
                            variableName: ''
                        },

                    ],
                    varList: []
                },
            ]
            if (type == 'add') {
                this.statisticName = '';
                this.chartType = '';
                this.statisticCycle = '';
                this.statisticCycleType = '';
                this.statisticStatus = '';
                this.cycleTime = 0; // 自定义单选框
                this.displayOrder = ''; // 展示顺序

                this.endTime = '23:59'; // 结束时间
                this.startTime = '00:00'; // 开始时间

                //				this.endTime=null; // 结束时间
                //	            this.startTime=null; // 开始时间

                this.statisticalManagementTitle = '新增统计';
            } else {
                //          	this.statisticName=record.statisticName;
                //	            this.chartType=record.chartType;
                //	            this.statisticCycle=record.statisticCycle;
                //	            this.statisticCycleType=record.statisticCycleType;
                //	            this.statisticStatus=record.statisticStatus;
                this.cycleTime = record.cycleTime; // 自定义单选框
                this.displayOrder = record.displayOrder; // 展示顺序

                this.endTime = record.endTime; // 结束时间
                this.startTime = record.startTime; // 开始时间

                this.statisticalManagementTitle = '修改统计';
                let data = {
                    id: record.id
                }
                getChartIdDetails(data, this);
            }
            this.statisticalManagementvisible = true;
        },
        //统计图新增关闭      
        statisticalManagementonClose() {
            this.statisticalManagementvisible = false;
        },
        //统计图新增确定     
        statisticalManagementSubmit() {

            if (!this.statisticName) {
                this.$message.info('统计名称不能为空!');
                return;
            }
            if (!this.chartType) {
                this.$message.info('统计图类型不能为空!');
                return;
            }
            if (!this.statisticCycle) {
                this.$message.info('统计周期不能为空!');
                return;
            }
            if (!this.statisticCycleType) {
                this.$message.info('统计周期不能为空!');
                return;
            }
            if (!this.statisticStatus) {
                this.$message.info('启停用状态不能为空!');
                return;
            }
            //          if(!this.cycleTime){
            //              this.$message.info('自定义不能为空!');
            //              return;
            //          }
            for (let i = 0; i < this.items.length; i++) {
                if (!this.items[i].equipmentId) {
                    this.$message.info('设备名称不能为空!');
                    return;
                }
                for (let j = 0; j < this.items[i].serials.length; j++) {
                    if (!this.items[i].serials[j].variableName) {
                        this.$message.info('变量信息不能为空!');
                        return;
                    }
                    if (!this.items[i].serials[j].serialName) {
                        this.$message.info('统计量名称不能为空!');
                        return;
                    }
                }
            }
            let data;
            if (this.statisticalManagementTitle.indexOf('新增') != -1) {
                data = {
                    statisticName: $.trim(this.statisticName),
                    chartType: this.chartType,
                    statisticCycle: this.statisticCycle,
                    statisticCycleType: this.statisticCycleType,
                    statisticStatus: this.statisticStatus,
                    items: this.items,
                    cycleTime: this.cycleTime, // 自定义单选框
                    displayOrder: this.displayOrder, // 展示顺序

                    endTime: this.endTime, // 结束时间
                    startTime: this.startTime, // 开始时间

                    //		            endTime:this.endTime==null?'':this.endTime.format('HH:mm'), // 结束时间
                    //		            startTime:this.startTime==null?'':this.startTime.format('HH:mm'), // 开始时间
                }
            } else {
                data = {
                    statisticName: $.trim(this.statisticName),
                    chartType: this.chartType,
                    statisticCycle: this.statisticCycle,
                    statisticCycleType: this.statisticCycleType,
                    statisticStatus: this.statisticStatus,
                    items: this.items,
                    cycleTime: this.cycleTime, // 自定义单选框
                    displayOrder: this.displayOrder, // 展示顺序

                    endTime: this.endTime, // 结束时间
                    startTime: this.startTime, // 开始时间

                    //		            endTime:this.endTime==null?'':this.endTime.format('HH:mm'), // 结束时间
                    //		            startTime:this.startTime==null?'':this.startTime.format('HH:mm'), // 开始时间

                    id: this.chartId
                }
            }
            chartOne(data, this);

        },
        //添加设备按钮
        addDevice() {
            this.items.push({
                equipmentId: '',
                serials: [
                    {
                        serialName: '',
                        variableName: ''
                    },

                ],
                varList: []
            })
        },
        //删除设备按钮
        removeDevice(index) {
            this.items.splice(index, 1);
        },
        //添加变量按钮
        addVarList(index) {
            this.items[index].serials.push({
                serialName: '',
                variableName: ''
            })
        },
        //删除设备按钮
        removeVarList(index, varIndex) {
            this.items[index].serials.splice(varIndex, 1);
        },
        //启停用状态改变
        chartStsChange(res) {
            let data = {
                id: res.id
            }
            chartStsId(data, this);
        },
        //通过设备名称改变获取变量名称
        deviceNameChange(index, data) {
            //          console.log(index)
            //          console.log(data)
            let res = {
                id: data
            }
            getChartIdVar(res, this, index);
        },
        //通过设备名称改变获取变量名称
        deviceNameChange2(index, data) {
            //          console.log(index)
            //          console.log(data)
            let res = {
                id: data
            }
            getChartIdVar2(res, this, index);
        },
        //删除列表中一个统计数据
        removechartDel(record) {
            let _this = this;
            this.$confirm({
                title: '确定要删除吗?',
                content: '',
                onOk() {
                    let data = {
                        id: record.id
                    }
                    chartDelId(data, _this);
                },
                onCancel() {
                },
            });
        },
        //同步缓存
        synchronousCache() {
            let data = {
                url: '/statistic/chart/clearRedis'
            }
            synchronousCache(data, this);
        }
    }
}
//在statisticalManagement组件混入statisticalManagement.js。