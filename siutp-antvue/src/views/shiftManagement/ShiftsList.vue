<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="shiftsName">
              <a-input placeholder="请输入shiftsName" v-model="queryParam.shiftsName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="班次描述">
              <a-input placeholder="请输入班次描述" v-model="queryParam.shiftsDescribe"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="上班时间">
              <a-input placeholder="请输入上班时间" v-model="queryParam.startTime"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="下班时间">
              <a-input placeholder="请输入下班时间" v-model="queryParam.overTime"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="下班是否需要打卡,0-否，1-是">
              <a-input placeholder="请输入下班是否需要打卡,0-否，1-是" v-model="queryParam.offSign"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('班次管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <shifts-modal ref="modalForm" @ok="modalFormOk"></shifts-modal>
  </a-card>
</template>

<script>
  import ShiftsModal from './modules/ShiftsModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "ShiftsList",
    mixins:[JeecgListMixin],
    components: {
      ShiftsModal
    },
    data () {
      return {
        description: '班次管理管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: 'shiftsName',
            align:"center",
            dataIndex: 'shiftsName'
           },
		   {
            title: '班次描述',
            align:"center",
            dataIndex: 'shiftsDescribe'
           },
		   {
            title: '上班时间',
            align:"center",
            dataIndex: 'startTime'
           },
		   {
            title: '下班时间',
            align:"center",
            dataIndex: 'overTime'
           },
		   {
            title: '下班是否需要打卡,0-否，1-是',
            align:"center",
            dataIndex: 'offSign'
           },
		   {
            title: '上班时长，小时',
            align:"center",
            dataIndex: 'workHours'
           },
		   {
            title: '开始休息时间',
            align:"center",
            dataIndex: 'restStartTime'
           },
		   {
            title: '结束休息时间',
            align:"center",
            dataIndex: 'restOverTime'
           },
		   {
            title: '启停用状态',
            align:"center",
            dataIndex: 'shiftsStatus'
           },
		   {
            title: '删除标识0-正常,1-已删除',
            align:"center",
            dataIndex: 'delFlag'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/settle/shifts/list",
          delete: "/settle/shifts/delete",
          deleteBatch: "/settle/shifts/deleteBatch",
          exportXlsUrl: "settle/shifts/exportXls",
          importExcelUrl: "settle/shifts/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>