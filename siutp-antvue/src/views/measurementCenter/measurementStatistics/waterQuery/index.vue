<!-- 计量中心-计量统计-水表查询 -->
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
              <a-form-item label="水表名称">
                <a-input
                  placeholder="请输入水表名称"
                  v-model="queryParam.waterName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="所属标段">
                <a-select
                  v-model="queryParam.equimentSection"
                  placeholder="请选择所属标段"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in bidSegmentList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="放置位置">
                <a-input
                  placeholder="请输入放置位置"
                  v-model="queryParam.equimentLocation"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="水表编号">
                <a-input
                  placeholder="请输入水表编号"
                  v-model="queryParam.waterSn"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8" :offset="12">
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
      <a-table
        ref="table"
        size="middle"
        bordered
        row-key="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 'max-content' }"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a
            v-if="record.longitude && record.latitude"
            href="javasript:void(0);"
            @click="openGisDialog(record)"
            >地图点位</a
          >
          <span v-else href="javasript:void(0);">地图点位</span>
        </span>
      </a-table>
    </a-card>
    <a-modal
      :title="gisTitle"
      :width="600"
      :visible="gisImgVisible"
      :footer="null"
      @cancel="handleClose"
    >
      <div>
        <img :src="gisImgUrl" alt="" width="100%" height="400" />
      </div>
    </a-modal>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementStatistics/waterQuery";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>