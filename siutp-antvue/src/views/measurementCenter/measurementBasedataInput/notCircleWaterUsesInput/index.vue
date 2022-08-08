<!-- 计量中心-计量基础数据录入-非周期表底 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-item label="水表">
                <a-select
                  showSearch
                  placeholder="请选择水表"
                  optionFilterProp="children"
                  style="width: 100%"
                  v-model="queryParam.equipmentId"
                >
                  <a-select-option
                    v-for="(item, index) in waterNumberList"
                    :key="index"
                    :value="item.equipmentId"
                    >{{ item.equipmentName }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="4">
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
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-card>
    <!-- 表单区域 -->
    <add-modal
      ref="modalForm"
      @ok="modalFormOk"
      :waterNumberList="waterNumberList"
    ></add-modal>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/notCircleWaterUsesInput";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>