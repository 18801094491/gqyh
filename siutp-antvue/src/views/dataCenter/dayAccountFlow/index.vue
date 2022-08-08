<template>
  <!-- 数据中心-日累计流量统计 -->
  <div class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="开始日期">
                <el-date-picker
                  v-model="queryParam.dataTime"
                  type="date"
                  placeholder="选择日期"
                >
                </el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="7" :sm="8">
              <a-form-item label="时间长度">
                <a-input
                  style="width: 80px"
                  v-model="queryParam.timeLength"
                  @keyup="
                    queryParam.timeLength = queryParam.timeLength.replace(
                      /^0|[^\d]/g,
                      ''
                    )
                  "
                ></a-input>
                <a-select
                  v-model="queryParam.share"
                  style="width: 80px"
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
            <a-col :md="4" :sm="8" :offset="5">
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
    <div style="position: relative; overflow-x: auto">
      <div id="lineCon" class="container" style="height: 400px"></div>
    </div>
    <a-card :bordered="false" style="margin: 10px 0">
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          row-key="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
          :loading="loading"
        >
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script>
import index from "@api/dataCenter-t/dayAccountFlow";

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
  bottom: 39px;
  right: 25px;
}
</style>