<template>
  <a-modal
    :title="title"
    :width="500"
    :visible="visible"
    :footer="null"
    @cancel="handleCancel"
    :confirmLoading="confirmLoading" >
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="巡检点名称">
          <a-input placeholder="请输入巡检点名称" v-model="name" />
        </a-form-item>
        
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="巡检点编号">
          <a-input placeholder="请输入巡检点编号" v-model="code" />
        </a-form-item>
        
        <a-form-item>
          <a-button class="float-r" type="primary" @click="searchQuery('')" icon="search">查询</a-button>
        </a-form-item>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="markerData"
          :pagination="false"
          :loading="loading"
          @change="handleCancel" >
            <span slot="list" slot-scope="text, record">
                <template v-if="item.userChooseType">
                    <p v-for="(item,index) in record.list"
                    :key="index"><span>{{item.userChooseType}}：</span><span>{{item.dataName}}</span></p>
                </template>
            </span>
        </a-table>
		
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  export default {
    name: "MarkerModal",
    props: ['markerData'],
    data () {
      return {
        title:"操作",
        visible: false,
        storeName: '',
        storePosition: '',
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 19 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        },
        storeStatusList:[],
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
          title: '巡检点名称',
          align: "center",
          dataIndex: 'name'
        },
        {
          title: '巡检点编号',
          align: "center",
          dataIndex: 'warnLevel'
        }],
        dataSource: [],
        loading: false,
      }
    },
    mounted(){
    },
    created () {
    },
    methods: {
      showModal () {
        this.visible = true;
      },

      close () {
        this.$emit('close');
        this.visible = false;
      },

      handleCancel () {
        this.close()
      },

      onSearchQueryChange() {
        this.$emit('onSearchQueryChange', {
          name: this.name,
          code: this.code
        })
      },
    }
  }
</script>