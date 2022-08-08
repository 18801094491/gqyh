<!-- 运营中心-工具台账-申请审核 -->
<template>
  <a-drawer
    title="申请审核"
    width="100%"
    @close="optApplyVerifyListonClose"
    :visible="optApplyVerifyListvisible"
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
            <a @click="examine(record)" v-if="record.verifyStatus == 0">审核</a>
            <!-- <a-divider slot="maintainBtn" type="vertical" v-if="record.verifyStatus!=0"/> -->
            <a @click="examineHistory(record)" v-if="record.verifyStatus != 0"
              >审核历史</a
            >
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-card>

    <a-drawer
      title="审核"
      :width="720"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div class="shClass">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="申请人"
        >
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
          <p v-for="(item, index) in useToolList" :key="index">{{ item }}</p>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="申请事由"
        >
          <p>{{ reason }}</p>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          class="findingsaudit"
          label="审核结果"
        >
          <a-select
            v-model="queryAuditStatus"
            style="width: 300px"
            placeholder="请选择审核结果"
          >
            <a-select-option
              v-if="index > 0"
              v-for="(item, index) in queryAuditStatusList"
              :key="index"
              :value="item.value"
              >{{ item.title }}</a-select-option
            >
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="审核意见"
        >
          <a-textarea
            placeholder="请输入审核意见"
            :rows="4"
            v-model="verifyDesc"
          />
        </a-form-item>
        <div
          :style="{
            position: 'absolute',
            left: 0,
            bottom: 0,
            width: '100%',
            borderTop: '1px solid #e9e9e9',
            padding: '10px 16px',
            background: '#fff',
            textAlign: 'right',
          }"
        >
          <a-button :style="{ marginRight: '8px' }" @click="onClose">
            关闭
          </a-button>
          <a-button @click="onSubmit" type="primary">提交</a-button>
        </div>
      </div>
    </a-drawer>

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
  </a-drawer>
</template>

<script>
import optApplyVerifyList from "@/assets/js/operationCenter/operationToolAdmin/optApplyVerifyList.js";
export default {
  ...optApplyVerifyList,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
