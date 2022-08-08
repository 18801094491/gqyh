<template>
  <!-- 数据中心 -流量计统计 -->
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="32">
            <a-col :md="7" :sm="8">
              <a-form-item label="开始时间">
                <el-date-picker
                  style="width: 205px"
                  v-model="queryParam.startTime"
                  placeholder="请选择开始时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="时间长度">
                <a-input
                  style="width: 65px"
                  v-model="queryParam.timeLength"
                  @keyup="changeTimeLength"
                ></a-input>
                <a-select
                  v-model="queryParam.share"
                  style="width: 65px"
                  @change="changeSpaceList"
                >
                  <a-select-option
                    v-for="(item, index) in shareList"
                    :key="index"
                    :value="item.value"
                  >
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="查询间隔">
                <a-input
                  style="width: 65px"
                  v-model="queryParam.spaceLength"
                  @keyup="
                    queryParam.spaceLength = queryParam.spaceLength.replace(
                      /^0|[^\d]/g,
                      ''
                    )
                  "
                ></a-input>
                <a-select v-model="queryParam.space" style="width: 65px">
                  <a-select-option
                    v-for="(item, index) in spaceList"
                    :key="index"
                    :value="item.value"
                  >
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
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
      <div
        v-if="dataSourceList.length"
        class="table-operator"
        style="border-top: 5px"
      >
        <a-button
          type="primary"
          icon="download"
          @click="handleExportXls('累计流量统计')"
          >导出</a-button
        >
      </div>
      <p v-if="!dataSourceList.length" style="margin-top: 20px">
        暂无数据,请查询!
      </p>
      <div v-for="(item, index) in dataSourceList">
        <!--<a-divider style="margin-top: 40px;">{{item.tableName}}</a-divider>-->
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="index"
          :columns="item.columns"
          :pagination="false"
          :dataSource="item.dataSource"
          style="margin-top: 40px"
        >
        </a-table>
      </div>
    </a-card>
  </div>
</template>
<script>
import index from "@/assets/js/dataCenter/flowStatistic.js";

export default {
  ...index,
};
</script>
<style scoped>
/deep/ .ant-pagination {
  margin-right: 160px !important;
}

.jumpPagination {
  position: absolute;
  bottom: 25px;
  right: 25px;
}

/deep/ .ant-select {
  z-index: 999;
}
</style>