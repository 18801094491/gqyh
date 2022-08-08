<template>
  <!-- 数据中心 -图表管理-->
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="统计名称">
                <a-input
                  placeholder="请输入资产名称"
                  v-model="queryParam.chartName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="统计状态">
                <a-select
                  v-model="queryParam.chartStatus"
                  placeholder="请选择统计状态"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in workingStatusList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="统计图类型">
                <a-select
                  v-model="queryParam.chartType"
                  placeholder="请选择统计类型"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in chartTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
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
      <div class="table-operator">
        <a-button
          @click="statisticalManagementChange('add')"
          type="primary"
          icon="plus"
          >新增</a-button
        >
        <a-button
          type="primary"
          style="margin-left: 6px"
          @click="synchronousCache"
          icon="sync"
          >同步缓存</a-button
        >
        <!-- <a-button type="primary" icon="download" @click="handleExportXls('维保记录')">导出</a-button> -->
      </div>

      <!-- table区域-begin -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          row-key="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          @change="handleTableChange"
        >
          <span slot="statisticStatus" slot-scope="text, record">
            <a-switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              @change="chartStsChange(record)"
              defaultChecked
              v-model="record.statisticStatus"
            />
          </span>
          <span slot="action" slot-scope="text, record">
            <a @click="statisticalManagementChange('change', record)">修改</a>
            <a-divider type="vertical" />
            <a @click="removechartDel(record)">删除</a>
          </span>
        </a-table>

        <!--动态合并单元格示例-->

        <!--<a-table
          ref="table"
          size="middle"
          bordered
					row-key="id"
          :columns="mergeColumns"
          :dataSource="mergeDataSource">

        </a-table>-->
      </div>
      <!-- table区域-end -->
    </a-card>

    <a-drawer
      :title="statisticalManagementTitle"
      :width="720"
      @close="statisticalManagementonClose"
      :visible="statisticalManagementvisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="统计名称">
                <a-input
                  placeholder="请输入资产名称"
                  v-model="statisticName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="统计图类型">
                <a-select v-model="chartType" placeholder="请选择统计图类型">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in chartTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="统计周期">
                <a-input
                  placeholder="请输入"
                  style="width: 30%"
                  v-model="statisticCycle"
                ></a-input>
                <a-select
                  v-model="statisticCycleType"
                  style="width: 65%; margin-left: 5%"
                  placeholder="请选择"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in cycleTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="启停用状态">
                <a-select
                  v-model="statisticStatus"
                  placeholder="请选择启停用状态"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in workingStatusList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="自定义">
                <a-radio-group @change="onChange" v-model="cycleTime">
                  <a-radio :value="1">是</a-radio>
                  <a-radio :value="0">否</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="展示顺序">
                <a-input placeholder="请输入" v-model="displayOrder"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="开始时间">
                <!--<a-time-picker v-model="startTime" format="HH:mm"/>-->
                <a-time-picker
                  :value="moment(startTime, 'HH:mm')"
                  :allowClear="false"
                  :defaultValue="moment(startTime, 'HH:mm')"
                  @change="
                    (val, dateStrings) =>
                      changeTime(val, dateStrings, 'startTime')
                  "
                  format="HH:mm"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="结束时间">
                <a-time-picker
                  :value="moment(endTime, 'HH:mm')"
                  :allowClear="false"
                  :disabledHours="getDisabledHours"
                  :disabledMinutes="getDisabledMinutes"
                  :defaultValue="moment(endTime, 'HH:mm')"
                  @change="
                    (val, dateStrings) =>
                      changeTime(val, dateStrings, 'endTime')
                  "
                  format="HH:mm"
                />
                <!--<a-time-picker v-model="endTime" format="HH:mm" />-->
                <!--<a-time-picker v-model="endTime" format="HH:mm" :disabledHours="getDisabledHours" :disabledMinutes="getDisabledMinutes" />-->
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <div class="addDeviceBox">
        <a-button type="primary" icon="plus" @click="addDevice"
          >添加设备</a-button
        >
        <div
          class="addDeviceDivBox"
          v-for="(item, index) in items"
          :key="index"
        >
          <a-icon
            v-if="index != 0"
            type="close-circle"
            class="close"
            @click="removeDevice(index)"
          />
          <div class="table-page-search-wrapper">
            <a-form layout="inline">
              <a-row :gutter="24">
                <a-col>
                  <a-form-item label="设备名称">
                    <a-select
                      placeholder="选择设备名称"
                      optionFilterProp="children"
                      style="width: 100%"
                      v-model="item.equipmentId"
                      @change="deviceNameChange(index, $event)"
                      showSearch
                    >
                      <a-select-option
                        :value="item.equipmentId"
                        v-for="(item, index) in deviceList2"
                        :key="index"
                        >{{ item.equipmentName }}</a-select-option
                      >
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row
                :gutter="24"
                v-for="(varItem, varIndex) in item.serials"
                :key="varIndex"
              >
                <a-col :span="11">
                  <a-form-item label="变量信息">
                    <a-select
                      v-model="varItem.variableName"
                      placeholder="请选择变量信息"
                    >
                      <a-select-option
                        :value="item2.variableName"
                        v-for="(item2, index2) in item.varList"
                        :key="index2"
                        >{{ item2.variableTitle }}</a-select-option
                      >
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="11">
                  <a-form-item label="统计量名称">
                    <a-input
                      placeholder="请输入资产名称"
                      v-model="varItem.serialName"
                    ></a-input>
                  </a-form-item>
                </a-col>
                <!-- <a-col :span="2" class="icon">
                            <a-icon v-if="varIndex==0" type="plus-circle" @click="addVarList(index)" />
                            <a-icon v-if="varIndex!=0" type="minus-circle" @click="removeVarList(index,varIndex)" />
                        </a-col> -->
              </a-row>
            </a-form>
          </div>
        </div>
      </div>
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
        <a-button
          :style="{ marginRight: '8px' }"
          @click="statisticalManagementonClose"
        >
          关闭
        </a-button>
        <a-button @click="statisticalManagementSubmit" type="primary"
          >提交</a-button
        >
      </div>
    </a-drawer>
  </div>
</template>

<script>
import index from "@/assets/js/dataCenter/statisticalManagement.js";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>