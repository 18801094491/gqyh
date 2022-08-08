import { getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { getTableHead } from '@assets/js/measurementCenter/customerInfo/tableHead'
import { getJSResultList } from '@api/measurementCenter-t/customerInfo.js'
import ContractDetail from "@views/measurementCenter/customerInfo/Detail";
import moment from "moment";

export default {
    name: "CustomerListRight",
    mixins: [JeecgListMixin],
    components: { ContractDetail },
    props: ["value"],
    data() {
        return {
            pageLoading: false,
            description: "客户信息",
            resultList: [],
            cardLoading: true,
            positionInfo: {},
            columns: [
                {
                    title: "客户名称",
                    align: "left",
                    dataIndex: "customerName",
                },
                {
                    title: "总用水量",
                    align: "left",
                    dataIndex: "totalMeter",
                },
                {
                    title: "总费用",
                    align: "center",
                    dataIndex: "totalCost"
                }
            ],
            url: {
                list: "/settle/customerInfo/"
            },
            monthData: []
        };
    },
    watch: {
        value: {
            immediate: true,
            handler(id) {
                this.dataSource = [];
                this.monthData = [];
                this.loadData(1, id);
            }
        }
    },
    created() {
        getJSResultList(this)
    },
    mounted() {
        this.pageLoading = true
    },
    destroyed() {
        console.log('我正在销毁')
        this.monthData = []
        this.pageLoading = false
    },
    methods: {
        loadData(pageNum, id) {
            if (!id) {
                return;
            }
            //加载数据 若传入参数1则加载第一页的内容
            if (pageNum === 1) {
                this.ipagination.current = 1;
            }
            this.loading = true;
            let params = { ...this.getQueryParams() }

            let sendData = {
                startTime: params.startTime ? moment(params.startTime).format('YYYY-MM') : undefined,
                endTime: params.endTime ? moment(params.endTime).format('YYYY-MM') : undefined,
                customerName: params.customerName,
                status: params.status,
            }
            if (id * 1 !== 0) {
                sendData.districtId = id
            }
            getAction(this.url.list, sendData).then(res => {
                if (res.success) {
                    this.dataSource = res.result.customerCounts;
                    let list = [...res.result.customerMonthInfos]
                    // 循环遍历月份数据
                    this.monthData = list.map((one, index) => {
                        let two = {
                            title: one.date,
                            tableDataList: one.listDetail
                        }
                        two.tableHeadList = getTableHead(two.tableDataList, this)
                        return two
                    })
                }
            })
                .finally(() => {
                    this.loading = false;
                    this.cardLoading = false;
                });
        },
        signingTimes(record, index) { // 签约次数点击
            console.log(index, JSON.stringify(record, null, 2))

            this.$router.push({
                name: "settlementCenter-contractManagement-ContractList",
            })
        },
        contractDetail(record, index) {
            console.log(index, JSON.stringify(record, null, 2))
            // 合同名称点击
            this.$refs.conDetail.edit(record)
            this.$refs.conDetail.modalVisible = true
        },
        searchQuery() {
            this.loadData(1, this.value);
        },
        searchReset() {
            this.queryParam = {};
            this.loadData(1, this.value);
        }
    }
};
//right 页面组件混入了right.js