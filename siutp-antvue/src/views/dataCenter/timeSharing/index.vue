<template>
  <!-- 数据中心 -数据分时统计-->
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24" class="formHeight" ref="formHeight">
            <a-col :md="6" :sm="8">
              <a-form-item label="设备类型">
                <a-select
                  placeholder="请选择告警类型"
                  v-model="queryParam.inequipmentType"
                  @change="inequipmentTypeChange"
                  style="width: 200px"
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
            <a-col :md="18" :sm="8">
              <a-form-item label="设备信息">
                <a-select
                  class="deviceInfoClass"
                  placeholder="选择设备信息"
                  optionFilterProp="children"
                  mode="multiple"
                  style="width: 100%"
                  @change="changeDeviceInfo"
                  v-model="queryParam.deviceInformation"
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
            <a-col :md="6" :sm="8">
              <a-form-item label="开始时间">
                <!--<a-date-picker v-model="queryParam.startTime" :defaultValue="moment(dateValue)"
							      format="YYYY-MM-DD HH:mm:ss"
							      showTime
							    />  -->
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
                  style="width: 80px"
                  v-model="queryParam.timeLength"
                  @keyup="
                    queryParam.timeLength = queryParam.timeLength.replace(
                      /[^\d]/g,
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
            <a-col :md="6" :sm="8">
              <a-form-item label="查询间隔">
                <a-input
                  style="width: 80px"
                  v-model="queryParam.spaceLength"
                  @keyup="
                    queryParam.spaceLength = queryParam.spaceLength.replace(
                      /[^\d]/g,
                      ''
                    )
                  "
                ></a-input>
                <a-select v-model="queryParam.space" style="width: 80px">
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
            <!--<a-col :md="4" :sm="8" >-->
            <a-col :md="4" :sm="8" :offset="2">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <!--<span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">-->
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
      <p v-if="!dataSourceList.length" style="margin-top: 20px">
        暂无数据,请查询!
      </p>

      <div v-for="(item, index) in dataSourceList">
        <a-divider style="margin-top: 40px">{{ item.tableName }}</a-divider>
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
import index from "@/assets/js/dataCenter/timeSharing.js";
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

/*/deep/ .formHeight{
        height: 100px!important;
    }*/
</style>