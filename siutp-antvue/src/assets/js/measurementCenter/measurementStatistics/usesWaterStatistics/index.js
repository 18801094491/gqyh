import { getAction, getDictValue } from "@api/measurementCenter-t/usesWaterStatistics.js";
import moment from "moment"
export default {
    name: "waterUseStatistics",
    data() {
        return {
            queryParam: {
                customerIds: [],
                customerType: undefined,
                endYear: '',
                beginYear: '',
                likeCustomerName: ''
            },
            customerTypeList: [],
            customerNameList: [],
            // 表头
            columns: [
                {
                    title: '客户名称',
                    align: "left",
                    dataIndex: 'customerName'
                },
                {
                    title: '年份',
                    align: "center",
                    dataIndex: 'statisticYear'
                },
                {
                    title: '一月',
                    align: "right",
                    dataIndex: 'month1'
                },
                {
                    title: '二月',
                    align: "right",
                    dataIndex: 'month2'
                },
                {
                    title: '三月',
                    align: "right",
                    dataIndex: 'month3'
                },
                {
                    title: '四月',
                    align: "right",
                    dataIndex: 'month4'
                },
                {
                    title: '五月',
                    align: "right",
                    dataIndex: 'month5'
                },
                {
                    title: '六月',
                    align: "right",
                    dataIndex: 'month6'
                },
                {
                    title: '七月',
                    align: "right",
                    dataIndex: 'month7'
                },
                {
                    title: '八月',
                    align: "right",
                    dataIndex: 'month8'
                },
                {
                    title: '九月',
                    align: "right",
                    dataIndex: 'month9'
                },
                {
                    title: '十月',
                    align: "right",
                    dataIndex: 'month10'
                },
                {
                    title: '十一月',
                    align: "right",
                    dataIndex: 'month11'
                },
                {
                    title: '十二月',
                    align: "right",
                    dataIndex: 'month12'
                },
                {
                    title: '总计',
                    align: "right",
                    dataIndex: 'yearTotle'
                }
            ],
            url: {
                list: "/settle/statistical"
            },
            loading: false,
            dataSource: []
        }
    },
    created() {
        this.getDictByCustomerType()
    },
    mounted() {
        this.loadData()
    },
    methods: {
        searchReset() {
            this.queryParam = {
                customerIds: [],
                customerType: undefined,
                endYear: '',
                beginYear: '',
                likeCustomerName: ''
            }
        },
        changeCustomer(value, option) {
            console.log(value, option)
            let objs = this.customerNameList.filter(one => value.indexOf(one.id) >= 0)
            this.queryParam.likeCustomerName = objs.map(one => one.customerName).join(',')
            console.log(this.likeCustomerName)
        },
        getDictByCustomerType() {
            getDictValue("customer_type", null).then(res => {
                if (res.code * 1 == 200) {
                    this.customerTypeList = res.result;
                }
            })
        },
        getCustomerNameList(type) {
            this.queryParam.customerIds = []
            this.queryParam.likeCustomerName = ''
            getAction('/settle/statistical/customers', {
                customerType: type
            }).then(res => {
                this.customerNameList = res;
            })
        },
        loadData() {
            this.loading = true;
            let params = { ...this.queryParam }
            console.log(params)
            params.endYear = params.endYear ? moment(params.endYear).format('YYYY') : undefined,
                params.beginYear = params.beginYear ? moment(params.beginYear).format('YYYY') : undefined,
                params.customerIds = params.customerIds.join(',')
            // params.customerType = params.customerType.join(',')
            getAction(this.url.list, params).then(res => {
                if (res.success) {
                    this.dataSource = res.result;
                }
            })
                .finally(() => {
                    this.loading = false;
                });
        }
    }
}
//usesWaterStatistics/index.vue 页面组件混入了 usesWaterStatistics/index.js