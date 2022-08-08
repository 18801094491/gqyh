	import {queryLabelTreeList, searchLabelByKeywords} from '@/api/operationCenter-t/equipmentSearcher.js'
	var timerHandler;
    export default {
        name: 'RequipmentListLeft',
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
                this.commonRequestThen(queryLabelTreeList({
                    labelName: keyword ? keyword : undefined
                }))
            },

            handleSearch(value) {
                if (value) {
                    this.commonRequestThen(searchLabelByKeywords({keyWord: value}))
                } else {
                    this.queryTreeData()
                }
            },
			handleChange(e) {
            	let value = e.target.value;
            	let _this = this;
                if(timerHandler){
                    clearTimeout(timerHandler);
                    timerHandler = null;
                }
                timerHandler = setTimeout(function(){
                    _this.handleSearch(value);
                }, 300)

            },
            handleTreeSelect(selectedKeys, event) {
                if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
                    this.selectedKeys = [selectedKeys[0]]
                    let id = event.node.dataRef.id
                    this.emitInput(id)
                }
            },

            emitInput(id) {
                this.$emit('input', id)
            },
            expanded(arr) {
                let _this = this;
                arr.forEach((item) => {
                    if (item.expanded) {
                        this.expandedKeys.push(item.id)
                    }
                    if (item.children && item.children.length !== 0) {
                        _this.expanded(item.children);
                    }
                })
            },
            commonRequestThen(promise) {
                this.loading = true
                promise.then(res => {

                    if (res.success) {
                        this.treeDataSource = res.result
                        // 默认选中第一条数据、默认展开所有第一级
                        if (res.result.length > 0) {
                            this.expandedKeys = []
                            this.expanded(res.result);
                            if (this.expandedKeys.length === 0) {
                                res.result.forEach((item, index) => {
                                    if (index === 0) {
                                        this.selectedKeys = [item.id]
                                        this.emitInput(item.id)
                                    }
                                    this.expandedKeys.push(item.id)
                                })
                            }

                        }
                    } else {
                        this.$message.warn('标签查询失败：' + res.message)
                        console.error('标签查询失败:', res)
                    }
                }).finally(() => {
                    this.loading = false
                    this.cardLoading = false
                })
            },

        }
    }
   //equipmentSearcher/RequipmentListLeft.vue 页面混入了equipmentSearcher/RequipmentListLeft.js