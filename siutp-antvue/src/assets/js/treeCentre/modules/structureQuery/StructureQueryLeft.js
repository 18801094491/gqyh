import { getAction } from '@/api/manage'
export default {
    name: 'RequipmentListLeft',
    props: ['value', 'leftUrl'],
    data() {
        return {
            cardLoading: true,
            loading: false,
            treeDataSource: [],
            checkedKeys: [],
            attrTreeVoList: null,
            defaultKeys: [],
        }
    },
    created() {
        this.commonRequestThen(getAction(this.leftUrl.list))
    },
    methods: {
        recursion(data, callBack) { //接口数据处理
            data.forEach(item => {
                item.class = 'attr'
                item.checkable = false
                if (item.childrenNum) {
                    this.recursion(item.children, callBack)
                }
            })
        },

        onLoadData(treeNode) { //展开异步加载
            return new Promise(resolve => {
                console.log(2,treeNode.dataRef.children)
                if (treeNode.dataRef.children) {
                    resolve();
                    return;
                }
                if (treeNode.dataRef.childrenNum) {
                    getAction(this.leftUrl.list, { parentId: treeNode.eventKey }).then(res => {
                        if (res.code == 200) {
                            let treeList = res.data.treeList;
                            treeList.forEach(item => {
                                item.key = item.id;
                                item.value = item.id;
                                item.title = item.name;
                                item.isLeaf = item.leaf;
                            })
                            this.attrTreeVoList = res.data.attrTreeVoList;
                            if (this.attrTreeVoList) {
                                this.recursion(this.attrTreeVoList)
                            }
                            treeNode.dataRef.children = treeList
                            resolve(treeList);
                        }
                    })
                }
            }).then(res => {
                if (res) treeNode.dataRef.children = res.concat(this.attrTreeVoList || [])
            });
        },

        handleTreeCheck(checkedKeys, e) { //多选事件
            this.checkedKeys = checkedKeys.checked
            this.emitInput({
                id: checkedKeys.checked.toString(),
            })
        },
        handleTreeSelect(checkedKeys, e) { //点击标签事件
            if (e.node.dataRef.treeId) {
                this.checkedKeys = []
                this.emitInput({
                    id: e.node.dataRef.treeId,
                    attrNames: e.node.dataRef.parentAttrNames, 
                    attrValues: e.node.dataRef.parentAttrValues
                })
            }
        },

        emitInput(data) { //选项变更
            this.$emit('input', data)
        },

        commonRequestThen(promise) { //根节点加载
            this.loading = true;
            promise.then(res => {
                if (res.code == 200) {
                    let treeList = res.data.treeList;
                    treeList.forEach(item => {
                        item.key = item.id;
                        item.value = item.id;
                        item.title = item.name;
                        item.isLeaf = !item.childrenNum;
                    })
                    this.treeDataSource = treeList;
                    this.defaultKeys = [treeList[0].id]
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