import { queryCustomerTreeList } from '@/api/measurementCenter-t/waterQuantityAnalysis.js'
export default {
    name: 'userWaterLeft',
    props: ['value'],
    data() {
        return {
            cardLoading: true,
            loading: false,
            treeDataSource: [],
            selectedKeys: [],
            expandedKeys: []
        }
    },
    created() {
        this.queryTreeData()
    },
    methods: {

        queryTreeData(keyword) {
            this.commonRequestThen(queryCustomerTreeList({
                departName: keyword ? keyword : undefined
            }))
        },

        handleSearch(value) {
            if (value) {
                this.commonRequestThen(queryCustomerTreeList({ keyword: value }))
            } else {
                this.queryTreeData()
            }
        },

        handleTreeSelect(selectedKeys, event) {
            if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
                this.selectedKeys = [selectedKeys[0]]
                let nodeInfo = event.node.dataRef
                this.emitInput(nodeInfo)
            }
        },

        emitInput(nodeInfo) {
            this.$emit('input', nodeInfo)
        },

        commonRequestThen(promise) {
            this.loading = true
            promise.then(res => {
                if (res.success) {
                    this.treeDataSource = res.result

                    // 默认选中第一条数据、默认展开所有第一级
                    if (res.result.length > 0) {
                        this.expandedKeys = []
                        res.result.forEach((item, index) => {
                            if (index === 0) {
                                this.selectedKeys = [item.id]
                                this.emitInput(item.id)
                            }
                            this.expandedKeys.push(item.id)
                        })
                    }
                } else {
                    this.$message.warn('客户名称查询失败：' + res.message)
                }
            }).finally(() => {
                this.loading = false
                this.cardLoading = false
            })
        },

    }
}
//waterQuantityAnalysis/left.vue 页面组件混入了 aterQuantityAnalysis/left.js