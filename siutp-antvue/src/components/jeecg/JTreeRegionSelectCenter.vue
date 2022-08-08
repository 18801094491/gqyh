<template>
    <a-tree-select
    v-model="treeValue"
    allowClear
    labelInValue
    style="width: 100%"
    :disabled="disabled"
    :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
    :placeholder="placeholder"
    :treeData="treeData"
    :replaceFields="{children:'children', key:'id', value: 'id', title: 'name'}"

    :multiple="multiple"
    @change="onChange"
    @search="onSearch">
  </a-tree-select>
  
</template>
<script>

  /*
  * 异步树加载组件 通过传入表名 显示字段 存储字段 加载一个树控件
  * <j-tree-select dict="aa_tree_test,aad,id" pid-field="pid" ></j-tree-select>
  * */
  import { getAction } from '@/api/manage'

  export default {
    name: 'JTreeRegionSelect',
    props: {
      value:{
        type: String,
        required: false
      },
      placeholder:{
        type: String,
        default: '请选择',
        required: false
      },
      dict:{
        type: String,
        default: '',
        required: false
      },
      pidField:{
        type: String,
        default: 'pid',
        required: false
      },
      pidValue:{
        type: String,
        default: '',
        required: false
      },
      disabled:{
        type:Boolean,
        default:false,
        required:false
      },
      hasChildField:{
        type: String,
        default: '',
        required: false
      },
      condition:{
        type:String,
        default:'',
        required:false
      },
      // 是否支持多选
      multiple: {
        type: Boolean,
        default: false,
      },
      loadTriggleChange:{
        type: Boolean,
        default: false,
        required:false
      },
      url:{
        type: String,
        default: '',
        required: false
      },
    },
    data () {
      return {
        treeValue:"",
        treeData:[],
        tableName:"",
        text:"",
        code:"",
        parentId: '',
        id: '',
      }
    },
    created(){
      this.validateProp().then(()=>{
        this.loadRoot()
      })
    },
    methods: {
      loadRoot(){
        let param = {}
        getAction(this.url,param).then(res=>{
          if(res.code * 1 == 200 && res.data){
            for(let i of res.data){
              if(i.leaf==false){
                i.isLeaf=false
              }else if(i.leaf==true){
                i.isLeaf=true
              }
            }
            this.treeData = [...res.data]
          }else{
            console.log("数根节点查询结果-else",res)
          }
        })
      },
      onChange(value){
        this.treeValue = value;
        this.parentId = value.value;
        console.log("选中的节点id-------",value.value)
      },
      onSearch(value){
        console.log(value)
      },
      validateProp(){
        let mycondition = this.condition
        return new Promise((resolve,reject)=>{
          if(!mycondition){
            resolve();
          }else{
            try {
              let test=JSON.parse(mycondition);
              if(typeof test == 'object' && test){
                resolve()
              }else{
                this.$message.error("组件JTreeSelect-condition传值有误，需要一个json字符串!")
                reject()
              }
            } catch(e) {
              this.$message.error("组件JTreeSelect-condition传值有误，需要一个json字符串!")
              reject()
            }
          }
        })
      }
    },
    //2.2新增 在组件内定义 指定父组件调用时候的传值属性和事件类型 这个牛逼
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
