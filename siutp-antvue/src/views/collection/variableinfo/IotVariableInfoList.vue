<template>
  <!-- 采集管理-采集变量 -->
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="变量名称">
                <a-input
                  placeholder="请输入变量名称如：FM00#zt"
                  v-model="queryParam.variableName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="变量标题">
                <a-input
                  placeholder="请输入变量标题"
                  v-model="queryParam.variableTitle"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="变量类型">
                <a-select
                  v-model="queryParam.variableType"
                  placeholder="请选择变量类型"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in inequipmentTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery" icon="search"
                  >查询</a-button
                >
                <a-button
                  type="primary"
                  @click="searchReset"
                  icon="reload"
                  style="margin-left: 8px"
                  >重置</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </div>
    <a-card :bordered="false">
      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button
          type="primary"
          icon="download"
          @click="handleExportXls('采集变量信息')"
          >导出</a-button
        >
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
        <a-button
          type="primary"
          @click="handleExportXls2('采集变量导入模板')"
          icon="download"
          >下载导入模板</a-button
        >
        <a-tooltip placement="top" style="margin-right: 6px">
          <template slot="title">
            <span>从IOserver同步变量</span>
          </template>
          <a-button
            @click="synchronousVariableInformation"
            type="primary"
            icon="sync"
            >同步变量信息</a-button
          >
        </a-tooltip>
      </div>
      <!-- 列表区域 -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ x: 1600 }"
          @change="handleTableChange"
        >
          <a-switch
            slot="workingStatus"
            checkedChildren="启用"
            slot-scope="text, record, index"
            unCheckedChildren="停用"
            v-model="record.workingStatus"
            defaultChecked
            @change="editWorkingStatusChange(record)"
          />

          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">
                更多
                <a-icon type="down" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a-popconfirm
                    title="确定删除吗?"
                    @confirm="() => handleDelete(record.id)"
                  >
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
      <!-- 表单区域 -->
      <iotVariableInfo-modal
        ref="modalForm"
        @ok="modalFormOk"
      ></iotVariableInfo-modal>
    </a-card>
  </div>
</template>

<script>
import IotVariableInfoModal from "./IotVariableInfoModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import {
//   editWorkingStatus,
//   iotVarinfoSync
// } from '@/api/collection/IotVariableInfoList.js'
//import { getA16} from '@/api/dict.js'
import {
  getA16,
  iotVarinfoSync,
  editWorkingStatus,
} from "@/api/collection-t/variableinfo.js";
import { downFile } from "@/api/manage";

export default {
  name: "IotVariableInfoList",
  mixins: [JeecgListMixin],
  components: {
    IotVariableInfoModal,
  },
  data() {
    return {
      workingStatus: false,
      description: "采集变量",
      // 编辑表头
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "变量名称",
          align: "center",
          dataIndex: "variableName",
        },
        {
          title: "变量标题",
          align: "center",
          dataIndex: "variableTitle",
        },
        {
          title: "变量类型",
          align: "center",
          dataIndex: "variableTypeName",
        },
        {
          title: "读写属性",
          align: "center",
          dataIndex: "readTypeValue",
        },
        {
          title: "数据类型",
          align: "center",
          dataIndex: "dataTypeValue",
        },
        {
          title: "单位",
          align: "center",
          dataIndex: "variableUnit",
        },

        {
          title: "备注",
          align: "center",
          dataIndex: "remark",
        },
        {
          title: "启停用",
          align: "center",
          dataIndex: "workingStatus",
          width: 100,
          fixed: "right",
          scopedSlots: { customRender: "workingStatus" },
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          width: 150,
          fixed: "right",
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/iot/varinfo/list",
        delete: "/iot/varinfo/delete",
        deleteBatch: "/iot/varinfo/deleteBatch",
        exportXlsUrl: "iot/varinfo/exportXls",
        importExcelUrl: "/iot/varinfo/import",
        exportXlsUrl2: "/iot/varinfo/downloadModel",
      },
      inequipmentTypeList: [],
    };
  },
  computed: {
    //导入
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}${this.url.importExcelUrl}`;
    },
  },
  mounted() {
    getA16(this);
  },
  methods: {
    //启停用改变
    editWorkingStatusChange(data) {
      let res = {
        id: data.id,
        workStatus: data.workingStatus ? "1" : "0",
      };
      editWorkingStatus(res, this);
    },
    //下载导入模板
    handleExportXls2(fileName) {
      downFile(this.url.exportXlsUrl2).then((data) => {
        if (!data) {
          this.$message.warning("文件下载失败");
          return;
        }
        if (typeof window.navigator.msSaveBlob !== "undefined") {
          window.navigator.msSaveBlob(new Blob([data]), fileName + ".xls");
        } else {
          let url = window.URL.createObjectURL(new Blob([data]));
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", fileName + ".xls");
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        }
      });
    },
    //同步变量信息
    synchronousVariableInformation() {
      iotVarinfoSync(this);
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>