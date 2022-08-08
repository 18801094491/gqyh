<!-- 运营中心-我的申请 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="4" :sm="8">
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
            <a-col :md="12" :sm="8">
              <a-form-item label="申请日期">
                <a-date-picker v-model="queryParam.startTime" /> ~
                <a-date-picker v-model="queryParam.endTime" />
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8" :offset="2">
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
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="toolApplication('add')" type="primary" icon="plus"
          >新增工具申请</a-button
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
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record, index">
            <a @click="details(record)">详情</a>
            <a-divider slot="maintainBtn" type="vertical" />
            <!-- <router-link v-if="record.verifyStatus==2" :to="{name:'toolApplication-index',query:{id:record.id}}">修改</router-link> -->
            <a
              v-if="record.verifyStatus == 2"
              @click="toolApplication('change', record)"
              >修改</a
            >
            <a-divider
              v-if="record.verifyStatus == 2"
              slot="maintainBtn"
              type="vertical"
            />
            <a @click="examineHistory(record)">审核历史</a>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-card>
    <a-modal
      title="详情"
      width="30%"
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
    <a-modal
      title="审核历史"
      width="30%"
      :visible="ecuvisible2"
      :destroyOnClose="true"
      :keyboard="false"
      :maskClosable="false"
      @cancel="handleCancel2"
      class="deails"
    >
      <a-timeline>
        <a-timeline-item
          v-for="(item, index) in optApplyVerifyList"
          :key="index"
        >
          <p>{{ item.createTime }}</p>
          <p>{{ item.verifyStatus }}</p>
          <p v-if="item.verifyDesc">{{ item.verifyDesc }}</p>
        </a-timeline-item>
      </a-timeline>
      <template slot="footer">
        <a-button
          type="primary"
          @click.stop="
            () => {
              ecuvisible2 = false;
            }
          "
          >关闭</a-button
        >
      </template>
    </a-modal>
    <toolApplicationT ref="modalForm"></toolApplicationT>
  </div>
</template>

<script>
import optGoodsApplyList from "@/assets/js/operationCenter/operationToolAdmin/optGoodsApplyList.js";
export default {
  ...optGoodsApplyList,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
