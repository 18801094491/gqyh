<template>
  <div class="screenCommonBox">
    <div class="table-page-search-wrapper" style="padding: 10px 12px">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="19" :sm="8">
            <a-range-picker
              v-model="hdLLBTime"
              format="YYYY-MM-DD"
              @change="hdLLBSearchQuery"
            ></a-range-picker>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div style="margin: 0 0 10px 0">
      <a-card :bordered="false">
        <div class="accumulatedWaterConsumptionBox" style="margin-top: 10px">
          <div class="chartDivBox">
            <div id="container7" class="container"></div>
          </div>
        </div>
      </a-card>
    </div>
    <div style="margin: 0 0 10px 0">
      <a-card :bordered="false">
        <a-divider
          >客户水价统计({{
            this.treeInfo == "0" ? "全部" : this.treeInfo.title
          }})</a-divider
        >
        <div class="accumulatedWaterConsumptionBox">
          <a-card
            class="j-address-list-right-card-box"
            :loading="cardLoading"
            :bordered="false"
          >
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="userId"
              :pagination="ipagination"
              :columns="columns"
              :dataSource="dataSource"
              :loading="loading"
              @change="handleTableChange"
            ></a-table>
          </a-card>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementStatistics/waterQuantityAnalysis/right";
export default {
  ...index,
};
</script>
<style>
.j-address-list-right-card-box .ant-table-placeholder {
  min-height: 46px;
}
</style>
<style scoped>
.j-address-list-right-card-box {
  height: 100%;
  min-height: 300px;
}
</style>