import {getAction} from "@/api/manage";
import {JeecgListMixin} from "@/mixins/JeecgListMixin";
import { 
    getBidSegmentData,
    getEquipmentTypeData,
    getEquipmentLedgerData,
    getQueryNameList2,
} from '@/api/treeCentre/measurementCenterTree/treeManage.js'
import RequipmentDetail from '@/views/treeCentre/modules/modules/RequipmentDetail'
export default {
        name: "StructureQueryRight",
        mixins: [JeecgListMixin],
        components: { RequipmentDetail },
        props: ["value", 'rightUrl', 'comments', 'searchType'],
        data() {
            return {
                columns: this.comments,
                pageLoading: false,
                description: "结构管理设备信息",
                resultList: [],
                cardLoading: true,
                positionInfo: {},
                queryParam: {},
                supplierClassificationList2: [],
                url: this.rightUrl,
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
                bidSegmentList: [],
                modelTypeList: [],
                modelTypeList2: [],
                supplierClassificationList2: []
            };
        },
        watch: {
            value: {
                immediate: true,
                handler(data) {
                    this.dataSource = [];
                    this.monthData = [];
                    this.loadData(1, data);
                }
            }
        },
        created() {
            //获取所属标段值
            getBidSegmentData(this);
            //获取设备类型值
            getEquipmentTypeData(this);
            //获取供应商值-筛选
            getQueryNameList2(this);
        },
        mounted () {
            this.pageLoading = true
        },
        destroyed () {
            this.monthData = []
            this.pageLoading = false
        },
        methods: {
            loadData(pageNum, data) {
                if (data && !data.id) {
                    this.loading = false;
                    this.cardLoading = false;
                    this.ipagination.current = 1;
                    this.ipagination.total = 0;
                    return;
                }
                //加载数据 若传入参数1则加载第一页的内容
                if (pageNum === 1) {
                    this.ipagination.current = 1;
                }
                this.loading = true;
                let params =  {...this.getQueryParams()}
                let sendData = {
                    pageNo: this.ipagination.current,
                    pageSize: this.ipagination.pageSize,
                }
                this.queryParam.pageNo = this.ipagination.current
                this.queryParam.pageSize = this.ipagination.pageSize
                if (data && data.id * 1 !== 0) {
                    this.queryParam.parentIds = data.id
                    this.queryParam.attrNames = data.attrNames || ''
                    this.queryParam.attrValues = data.attrValues || ''
                }
                if (!data) {
                    this.loading = false;
                    this.cardLoading = false;
                    this.ipagination.current = 0;
                    this.ipagination.total = 0;
                    return;
                }
                getAction(this.rightUrl.list, this.queryParam).then(res => {
                    if (res.code == 200) {
                        this.dataSource = res.data.records;
                        this.ipagination.current = res.data.current;
                        this.ipagination.total = res.data.total;
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