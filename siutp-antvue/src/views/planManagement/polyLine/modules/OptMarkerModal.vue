<template>
  <a-modal
    :title="title"
    :width="500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="巡检点名称">
          <a-input placeholder="请输入巡检点名称" v-model="name" />
        </a-form-item>
        
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="巡检点编号">
          <a-input placeholder="请输入巡检点编号" v-model="code" />
        </a-form-item>
        
        <a-form-item>
          <a-button class="float-r" type="primary" @click="onSearchQueryChange" icon="search">查询</a-button>
        </a-form-item>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :row-selection="rowSelection"
          :columns="columns"
          :dataSource="pointList"
          :loading="loading" >
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
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"
  import { } from '@/api/planManagement/polyLine/modules/optMarkerModal.js'
  export default {
    name: "OptMarkerModal",
    props: ['areaId', 'pointList', 'selectedPointList'],
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
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
        name: '', //巡检点名称
        code: '', //巡检点编号
        loading: false,
        storeStatusList:[],
        columns: [{
          title: '巡检点名称',
          align: "center",
          dataIndex: 'name',
          width: 195
        },{
          title: '巡检点编号',
          align: "center",
          dataIndex: 'code',
          width: 195
        }],
        dataSource: [],
        ipagination: {
          current: 1,
          pageSize: 10,
          showTotal: (total, range) => {
              return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
          },
          total: 0
        },

        selectedRows: [],
        selectedRowKeys: [],
      }
    },
    beforeMount() {
    },
    created () {
    },
    mounted(){
    },
    watch: {
    },
    computed: {
      rowSelection() {
        return {
          selectedRowKeys: this.selectedRowKeys,
          onChange: (selectedRowKeys, selectedRows) => {
            this.selectedRowKeys = selectedRowKeys;
            this.selectedRows = selectedRows;
          },
        };
      }
    },
    methods: {
      showModal() {
        this.selectedRowKeys = []
        this.selectedPointList.forEach(item => {
          this.selectedRowKeys.push(item.id);
        })
        this.selectedRows = this.selectedPointList;
        this.visible = true;
      },

      handleOk () {
        if (!this.selectedRows.length) {
          return;
        }
        this.handleCancel()
        this.$emit('onSelectedPointChange', this.selectedRows)
      },
      onSearchQueryChange() {
        this.$emit('onSearchQueryChange', {
          areaId: this.areaId,
          name: this.name,
          code: this.code
        })
      },

      handleCancel () {
        this.visible = false;
        this.name = '';
        this.code = '';
      }

    }
  }
</script>

<style lang="less" scoped>

</style>