import {getAction} from "@/api/manage";
import {JeecgListMixin} from "@/mixins/JeecgListMixin";
import RequipmentDetail from '@/views/operationCenter/equipmentSearcher/modules/RequipmentDetail'
import {getQueryNameList2} from '@/api/operationCenter-t/equipmentSearcher.js'
import getColumns from '@/assets/js/operationCenter/equipmentSearcher/columns.js'
export default {
        name: "RequipmentListRight",
        mixins: [JeecgListMixin],
        components: { RequipmentDetail },
        props: ["value"],
        comments: [],
        data() {
            return {
                pageLoading: false,
                description: "设备台账树信息",
                resultList: [],
                cardLoading: true,
                positionInfo: {},
                queryParam: {
                    optLocation: "",
                    equipmentRevstop: "",
                    equipmentSn: "",
                    equipmentSupplier: ""
                },
                supplierClassificationList2: [],
                url: {
                    list: "/equipment/optEquipment"
                },
                monthData: [],
                ipagination: {
                    current: 1,
                    pageSize: 10,
                    showTotal: (total, range) => {
                        return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
                    },
                    showQuickJumper: true,
                    total: 0
                },
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

        },
        mounted () {
            this.pageLoading = true
            this.columns = getColumns();
            //获取供应商值-筛选
            getQueryNameList2(this);
        },
        destroyed () {
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
                let params =  {...this.getQueryParams()}
                let sendData = {
                    optLocation: params.optLocation,
                    equipmentRevstop: params.equipmentRevstop,
                    equipmentSn: params.equipmentSn,
                    equipmentSupplier: params.equipmentSupplier,
                    pageNo: this.ipagination.current,
                    pageSize: this.ipagination.pageSize
                }
                if (id * 1 !== 0) {
                    sendData.labelId = id
                }
                getAction(this.url.list, sendData).then(res => {
                    if (res.success) {
                        this.dataSource = res.result.records;
                        this.ipagination.current = res.result.current;
                        this.ipagination.total = res.result.total;
                    }
                }).finally(() => {
                    this.loading = false;
                    this.cardLoading = false;
                }); 
            },
            searchQuery() {
                this.loadData(1, this.value);
            },
            searchReset() {
                this.queryParam = {};
                this.loadData(1, this.value);
            },
            //表格属性改变
            handleTableChange(pagination) {
                this.ipagination.current = pagination.current;
                this.loadData(this.ipagination.current, this.value)
            },
            equipmentDetail(record){
                this.$refs.equipDetail.openEquipmentDetail(record);
            }
        }
    };
//equipmentSearcher/RequipmentListRight.vue 页面混入了equipmentSearcher/RequipmentListRight.js