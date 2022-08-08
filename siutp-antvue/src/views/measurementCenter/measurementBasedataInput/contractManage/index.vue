<!-- 计量中心-计量基础数据录入-合同管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="客户名称">
                <a-input
                  placeholder="请输入客户名称"
                  v-model="queryParam.customerName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="合同名称">
                <a-input
                  placeholder="请输入合同名称"
                  v-model="queryParam.contractName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="合同编号">
                <a-input
                  placeholder="请输入合同编号"
                  v-model="queryParam.contractSn"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="4">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery" icon="search"
                  >查询</a-button
                >
                <a-button
                  class="ant-btn-border"
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
        <a-button
          @click="handleAdd"
          type="primary"
          icon="plus"
          v-has="'contract:add'"
          >新增</a-button
        >
        <a-button @click="expireDate" type="primary" icon="plus"
          >合同到期设置天数</a-button
        >
        <!-- <a-button type="primary" icon="download" @click="handleExportXls('合同管理')">导出</a-button> -->
      </div>

      <!-- table区域-begin -->
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
          :rowClassName="
            (record, index) => (record.alarmStatus * 1 === 1 ? 'warnStyle' : '')
          "
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)" v-has="'contract:edit'">编辑</a>

            <a-divider type="vertical" v-has="'contract:edit'" />
            <a @click="details(record)">详情</a>
            <a-divider type="vertical" />
            <!-- <a-divider type="vertical" v-has="'contract:rule'" /> -->
            <a @click="pricingRules(record)">计价规则</a>
            <!-- <a-divider type="vertical" v-has="'contract:watermeter'" /> -->
            <a-divider type="vertical" />
            <a @click="watermeter(record)">水表</a>
            <a-divider type="vertical" v-has="'contract:delete'" />
            <a-dropdown v-has="'contract:delete'">
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
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
          <!--<a :href="record.fileUrl" target="_blank" slot="contractName" slot-scope="text, record">{{record.contractName}}</a>-->
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <contract-modal ref="modalForm" @ok="modalFormOk"></contract-modal>

      <!--计价规则-->
      <pricing-rules-list-modal
        ref="rulesListModal"
        @closeModal="closeModal"
      ></pricing-rules-list-modal>

      <!--水表绑定-->
      <watermeter-list-modal
        :watermeterVisible="watermeterVisible"
        :watermeterRecord="watermeterRecord"
        @closeWatermeterModal="closeWatermeterModal"
      ></watermeter-list-modal>
      <!-- 合同到期提醒 -->
      <div>
        <a-modal
          title="合同到期提醒时间设置"
          :visible="expireDateVisible"
          @ok="submitExpireDate"
          :confirmLoading="confirmLoading"
          @cancel="closeExpireDate"
        >
          <div>
            <a-form-model :form="form">
              <a-row :gutter="24">
                <a-col :span="12">
                  <a-form-model-item label="合同到期提醒时间设置">
                    <a-input
                      v-model="form.setExpireDate"
                      @keyup="
                        form.setExpireDate = form.setExpireDate.replace(
                          /[^\d]/g,
                          ''
                        )
                      "
                    ></a-input>
                    <!--<a-input type="number" v-model="form.setExpireDate" :min="0"></a-input>-->
                  </a-form-model-item>
                </a-col>
              </a-row>
            </a-form-model>
          </div>
        </a-modal>
      </div>
    </a-card>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/contractManage";
export default {
  ...index,
};
</script>
<style>
.margin12 .ant-table-bordered .ant-table-tbody tr.warnStyle td {
  /*background: #f74e4e*/
  color: #f74e4e;
}

.warnStyle {
  /*background: #f74e4e;*/
  color: #f74e4e;
}
</style>
<style scoped>
@import "~@assets/less/common.less";
</style>