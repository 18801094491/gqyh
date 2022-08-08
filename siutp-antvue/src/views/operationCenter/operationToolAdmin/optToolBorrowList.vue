<!-- 运营中心-工具台账-申请历史 -->
<template>
 <a-drawer
    title="申请历史"
    width="100%"
    @close="optToolBorrowListonClose"
    :visible="optToolBorrowListvisible"
    :wrapStyle="{
      height: 'calc(100%)',
      overflow: 'auto',
      paddingBottom: '108px',
    }"
  >
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="姓名">
                <a-input
                  placeholder="请输入姓名"
                  v-model="queryParam.name"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="申请时间开始">
                <a-date-picker
                  style="width: 100%"
                  v-model="queryParam.startTime"
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="申请时间结束">
                <a-date-picker
                  style="width: 100%"
                  v-model="queryParam.endTime"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="状态">
                <a-select
                  placeholder="请选择状态"
                  v-model="queryParam.queryAuditStatus"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    v-for="(item, index) in queryAuditStatusList"
                    :key="index"
                    :value="item.value"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8" :offset="10">
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
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="details(record)">详情</a>

            <!-- <a-divider type="vertical" /> -->
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-card>

    <a-modal
      title="详情"
      width="35%"
      :visible="ecuvisible"
      :destroyOnClose="true"
      :keyboard="false"
      :maskClosable="false"
      @cancel="handleCancel"
      class="deails"
    >
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="申请人">
        <p>{{ createBy }}</p>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="申请时间"
      >
        <p>{{ createTime }}</p>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="工具详情"
      >
        <p v-for="(item, index) in useToolList" :key="index">
          {{ item }}
        </p>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="期望领用时间"
      >
        <p>{{ wantedTime }}</p>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="预计归还时间"
      >
        <p>{{ expectbackTime }}</p>
      </a-form-item>
      <a-form-item
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        label="申请事由"
      >
        <p>{{ reason }}</p>
      </a-form-item>
      <template slot="footer">
        <a-button
          type="primary"
          @click.stop="
            () => {
              ecuvisible = false;
            }
          "
          >关闭</a-button
        >
      </template>
    </a-modal>
  </a-drawer>
</template>

<script>
import optToolBorrowList from "@/assets/js/operationCenter/operationToolAdmin/optToolBorrowList.js";
export default {
  ...optToolBorrowList,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
