<template>
  <a-modal
    :title="title"
    :width="1000"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
          <a-row :gutter="24">
            <a-col :span="20" class="disFlex textleft-label">
                <a-form-item label="选择时间" >
                    <a-date-picker type="date" v-model="subTimeStart" placeholder="选择开始时间" @change="onSetStartDateChange"/>
                    ~
                    <a-date-picker type="date" v-model="subTimeEnd" placeholder="选择结束时间" @change="onSetOverDateChange"/>
                </a-form-item>
            </a-col>
            <a-col class="float-r mt4">
              <a-form-item>
                <a-button class="float-r" type="primary" @click="onSearchQueryChange" icon="search">查询</a-button>
              </a-form-item>
            </a-col>
        </a-row>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :row-selection="rowSelection"
          :columns="columns"
          :dataSource="workListMatter"
          :scroll="{ y: 400 }"
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
  import { } from '@/api/workOrderManagement/modules/workListModal.js'
  export default {
    name: "WorkListModal",
    props: ['workListMatter', 'matterList'],
    data () {
      return {
        title: "选择问题",
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
        url: {
          list: '/worklist/workListMatter/addlist'
        },
        name: '', //巡检点名称
        code: '', //巡检点编号
        loading: false,
        storeStatusList:[],
        columns: [{
          title: '问题标题',
          align: "center",
          dataIndex: 'title',
          width: 210
        },{
          title: '提交人',
          align: "center",
          dataIndex: 'subName',
          width: 100,
        },{
          title: '问题类型',
          align: "center",
          dataIndex: 'matterTypeDes',
          width: 100
        },{
          title: '状态',
          align: "center",
          dataIndex: 'statusDes',
          width: 100
        },{
          title: '提交时间',
          align: "center",
          dataIndex: 'subTime',
          width: 160
        },{
          title: '问题描述',
          align: "center",
          dataIndex: 'description',
          width: 210,
          ellipsis: 'true',
          scopedSlots: {
            customRender: 'description'
          },
        }],
        dataSource: [],
        selectedRows: [],
        subTimeStart: '',
        subTimeEnd: '',
        selectedRowKeys: [], //选中项
      }
    },
    beforeMount() {
    },
    created () {
    },
    mounted(){
    },
    computed: {
      rowSelection() {
        console.log('workListMatter rowSelection： matterList ----- : ', this.matterList)
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
        console.log('workListMatter showModal ： matterList ----- : ', this.matterList)
        this.selectedRowKeys = []
        this.matterList.forEach(item => {
          this.selectedRowKeys.push(item.id);
        })
        this.selectedRows = this.matterList;
        console.log(this.selectedRowKeys)
        this.visible = true;
      },

      handleOk () {
        if (!this.selectedRows.length) {
          return;
        }
        this.handleCancel()
        this.$emit('onSelectedPointChange', this.selectedRows)
      },

      onSetStartDateChange(data, dateString) { //选择开始时间
        console.log(dateString)
          this.subTimeStart = dateString;
      },
      onSetOverDateChange(data, dateString) { //选择结束时间
        console.log(dateString)
          this.subTimeEnd = dateString;
      },

      onSearchQueryChange() {
        if (new Date(this.subTimeEnd).getTime() < new Date(this.subTimeStart).getTime()) {
            this.$message.info('结束时间不能小于开始时间！');
            return;
        }
        this.$emit('onSearchQueryChange', {
          subTimeStart: this.subTimeStart,
          subTimeEnd: this.subTimeEnd,
        })
      },

      handleCancel () {
        this.$emit('close');
        this.subTimeStart = '';
        this.subTimeEnd = '';
        this.visible = false;
      }
    }
  }
</script>