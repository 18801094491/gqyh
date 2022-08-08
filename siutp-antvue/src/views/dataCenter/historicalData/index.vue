<template>
  <!-- 数据中心 -历史数据查询 -->
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="设备类型">
                <a-select
                  placeholder="请选择告警类型"
                  style="width: 420px"
                  v-model="queryParam.inequipmentType"
                  @change="inequipmentTypeChange"
                >
                  <a-select-option
                    v-for="(item, index) in inequipmentTypeList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
              <a-form-item label="设备信息">
                <a-select
                  placeholder="选择设备信息"
                  optionFilterProp="children"
                  style="width: 100%"
                  v-model="queryParam.deviceInformation"
                  showSearch
                >
                  <a-select-option
                    :value="item.equipmentId"
                    v-for="(item, index) in deviceInformationList"
                    :key="index"
                  >
                    {{ item.equipmentName }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="查询时间">
                <el-date-picker
                  v-model="queryParam.left"
                  placeholder="请选择开始时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm"
                ></el-date-picker>
                至
                <el-date-picker
                  v-model="queryParam.right"
                  placeholder="请选择结束时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm"
                ></el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8" :offset="8">
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
      <a-button
        type="primary"
        style="margin-bottom: 25px; margin-top: 20px"
        v-if="dataSource.length"
        @click="handleExportXls('历史记录')"
        icon="download"
        >导出
      </a-button>
      <p v-if="!dataSource.length" style="margin-top: 20px">暂无数据,请查询!</p>

      <!-- table区域-begin -->
      <div v-if="dataSource.length">
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
          :loading="loading"
        >
        </a-table>

        <a-pagination
          class="pagination"
          size="small"
          :total="total"
          :showTotal="
            (total, range) => `共 ${total} 条 当前[${range[0]} -  ${range[1]} ]`
          "
          :pageSize="pageSize"
          :current="pageNo"
          @change="handleSizeChange"
          @showSizeChange="handleCurrentChange"
        />
        <div class="jumpPagination">
          跳至
          <a-input
            v-model="jumpPagNum"
            style="width: 50px"
            size="small"
          ></a-input>
          页
          <a-button size="small" @click="toPage">跳转</a-button>
        </div>
      </div>
    </a-card>
  </div>
</template>
<script>
import "../../../assets/less/dataCenter/historydata.less";
import index from "../../../api/dataCenter-t/historydata.js";
export default {
  ...index,
};
</script>