<template>
  <!-- 数据中心 -数据看板 -->
  <div class="dataChartBox margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="13" :sm="8">
              <a-form-item label="全局时间查询">
                <el-date-picker
                  style="width: 205px"
                  @change="changeDateTotal('left')"
                  v-model="queryParam.startTime"
                  placeholder="请选择开始时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
                至
                <el-date-picker
                  style="width: 205px"
                  @change="changeDateTotal('right')"
                  v-model="queryParam.endTime"
                  placeholder="请选择结束时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8" :offset="6">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQueryTotal" icon="search"
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
    <a-row :gutter="10">
      <template v-for="(item, index) in allChartList">
        <a-col
          :span="
            allChartList.length == index + 1 && allChartList.length % 2 == 1
              ? 24
              : 12
          "
        >
          <div class="chartDivBox">
            <div class="table-page-search-wrapper" style="padding: 10px 12px">
              <a-form layout="inline">
                <a-row :gutter="24">
                  <a-col :md="19" :sm="8">
                    <el-date-picker
                      @change="(val) => changeDate(val, index, 'left')"
                      v-model="hdLLBTime['left_' + index]"
                      placeholder="请选择开始时间"
                      type="datetime"
                      format="yyyy-MM-dd HH:mm:ss"
                    ></el-date-picker>
                    至
                    <el-date-picker
                      @change="(val) => changeDate(val, index, 'right')"
                      v-model="hdLLBTime['right_' + index]"
                      placeholder="请选择结束时间"
                      type="datetime"
                      format="yyyy-MM-dd HH:mm:ss"
                    ></el-date-picker>
                  </a-col>
                  <a-col :md="5" :sm="8">
                    <span
                      style="float: right; overflow: hidden"
                      class="table-page-search-submitButtons"
                    >
                      <a-button
                        type="primary"
                        @click="
                          openEcharts(item.id, `container${index}`, index)
                        "
                        v-if="isOpen"
                        >放大</a-button
                      >
                      <a-button
                        type="primary"
                        style="margin-left: 10px"
                        @click="
                          searchQuery(item.id, `container${index}`, index)
                        "
                        icon="search"
                        >查询</a-button
                      >
                    </span>
                  </a-col>
                </a-row>
              </a-form>
            </div>
            <div :id="'container' + index" class="container"></div>
          </div>
        </a-col>
      </template>
    </a-row>
    <el-dialog
      title=""
      :fullscreen="true"
      :visible.sync="echartsVisible"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <div>
        <div class="chartDivBox">
          <div class="table-page-search-wrapper" style="padding: 10px 12px">
            <a-form layout="inline">
              <a-row :gutter="24">
                <a-col :md="19" :sm="8">
                  <el-date-picker
                    @change="(val) => changeDateEcharts(val, 'left')"
                    v-model="startTimeEcharts"
                    placeholder="请选择开始时间"
                    type="datetime"
                    format="yyyy-MM-dd HH:mm:ss"
                  ></el-date-picker>
                  至
                  <el-date-picker
                    @change="(val) => changeDateEcharts(val, 'right')"
                    v-model="endTimeEcharts"
                    placeholder="请选择结束时间"
                    type="datetime"
                    format="yyyy-MM-dd HH:mm:ss"
                  ></el-date-picker>
                </a-col>
                <a-col :md="5" :sm="8">
                  <span
                    style="float: right; overflow: hidden"
                    class="table-page-search-submitButtons"
                  >
                    <a-button
                      type="primary"
                      style="margin-left: 10px"
                      @click="searchQueryEcharts"
                      icon="search"
                      >查询</a-button
                    >
                  </span>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <div id="echartsCon" style="height: 600px; width: 100%"></div>
        </div>
      </div>
    </el-dialog>
    <a-card :bordered="false"> </a-card>
  </div>
</template>
<script>
import "@assets/less/dataCenter/dataChart.less";
import index from "@api/dataCenter-t/dataChart";

export default {
  ...index,
};
</script>