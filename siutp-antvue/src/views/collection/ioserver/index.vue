<template>
  <!-- 采集代理 -->
  <div id="customerManagement" class="margin12">
    <a-card :bordered="false">
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="border-top: 5px">
        <a-button
          @click="handleAdd('add')"
          type="primary"
          icon="plus"
          style="margin-right: 6px"
          >新增代理</a-button
        >
        <!-- <a-button type="primary" icon="download" @click="handleExportXls('用户信息')">导出</a-button> -->
      </div>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        class="iotProxyTable"
        :loading="loading"
      >
        <span slot="proxyStatus" slot-scope="text, record, index">
          <a-switch
            checkedChildren="启用"
            unCheckedChildren="停用"
            v-model="record.proxyStatus"
            defaultChecked
            @change="proxyStatusChange(record)"
          />
        </span>
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a :data-data="record" @click="handleAdd('change', record)">修改</a>
          <a-divider type="vertical" />
          <a @click="heartbeatAddressClick('wb', record)">测试</a>
          <a-divider type="vertical" />
          <a @click="acquisitionEquipment(record)">采集设备</a>
        </span>
      </a-table>
    </a-card>
    <!-- 新增/修改弹框 开始-->
    <a-drawer
      :title="drawerTitle"
      :width="720"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-form layout="vertical" hideRequiredMark>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="代理名称">
              <a-input v-model="proxyName" placeholder="请输入代理名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="IP地址">
              <a-input v-model="ipAddress" placeholder="请输入IP地址" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="代理类型">
              <a-select placeholder="请选择代理类型" v-model="proxyType">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in proxyTypeList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
                -->
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="心跳周期">
              <a-input
                style="width: 100px"
                v-model="heartbeatCycle"
                @keyup="heartbeatCycle = heartbeatCycle.replace(/[^\d]/g, '')"
                placeholder=""
              />
              <a-select
                style="width: 214px; margin-left: 10px"
                placeholder="请选择"
                v-model="heartbeatCycleTime"
              >
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in timeTypeList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
                <!-- <a-select-option value="60">分</a-select-option>
                                <a-select-option value="3600">时</a-select-option>
                                <a-select-option value="86400">日</a-select-option> -->
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="心跳监测">
              <a-select placeholder="请选择心跳监测" v-model="heartbeatStatus">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in workingStatusList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态">
              <a-select placeholder="请选择状态" v-model="proxyStatus">
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
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="心跳地址">
              <a-input
                placeholder="请输入代理名称"
                style="width: 324px"
                v-model="heartbeatAddress"
              />
              <a-button
                style="margin-left: 15px"
                type="primary"
                @click="heartbeatAddressClick('nb')"
                >测试心跳</a-button
              >
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
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
        <a-button @click="addSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
    <!-- 新增/修改弹框 结束-->
    <!-- 绑定采集设备 开始-->
    <a-drawer
      title="采集设备"
      :width="720"
      @close="onClose2"
      :visible="visible2"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="设备编号">
                <a-input placeholder="请输入设备编号" v-model="iotSn"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
              <a-form-item label="设备名称">
                <a-input
                  placeholder="请输入变量示意"
                  v-model="iotName"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="设备类型">
                <a-select placeholder="请选择设备类型" v-model="iotCategory">
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in inequipmentTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8" :offset="8" style="text-align: right">
              <a-button type="primary" @click="searchQuery2" icon="search"
                >查询</a-button
              >
            </a-col>
          </a-row>
        </a-form>
      </div>
      <a-divider orientation="left">全部设备</a-divider>
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="margin-bottom: 20px">
        <a-select
          style="width: 110px; margin-right: 20px"
          placeholder="请选择设备类型"
          v-model="iotCategory3"
        >
          <a-select-option
            :value="item.code"
            v-for="(item, index) in inequipmentTypeList"
            :key="index"
            >{{ item.title }}</a-select-option
          >
        </a-select>
        <a-button
          type="primary"
          icon="paper-clip"
          style="margin-right: 20px"
          @click="bindSpecifiedType"
          >绑定指定类型</a-button
        >
        <a-button type="primary" icon="paper-clip" @click="iotProxyBindAllClick"
          >绑定全部</a-button
        >
      </div>

      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns2"
        :dataSource="dataSource2"
        :pagination="ipagination2"
        :loading="loading2"
        @change="handleTableChange2"
      >
        <span
          class="bindBtnBox"
          slot="maintainBtn"
          slot-scope="text, record, index"
        >
          <a
            v-if="record.bindStatus == 0"
            :data-data="record"
            @click="bindOne(record)"
            >绑定</a
          >
          <a v-if="record.bindStatus == 1" :data-data="record" disabled
            >已绑定</a
          >
        </span>
      </a-table>
      <a-divider></a-divider>
      <a-divider orientation="left">绑定设备</a-divider>
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="设备编号">
                <a-input
                  placeholder="请输入设备编号"
                  v-model="iotSn2"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="8">
              <a-form-item label="设备名称">
                <a-input
                  placeholder="请输入变量示意"
                  v-model="iotName2"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="12" :sm="8">
              <a-form-item label="设备类型">
                <a-select placeholder="请选择设备类型" v-model="iotCategory2">
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in inequipmentTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8" :offset="8" style="text-align: right">
              <a-button type="primary" @click="searchQuery3" icon="search"
                >查询</a-button
              >
            </a-col>
          </a-row>
        </a-form>
      </div>
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns3"
        :dataSource="dataSource3"
        :pagination="ipagination3"
        :loading="loading3"
        @change="handleTableChange3"
      >
        <span
          class="bindBtnBox"
          slot="maintainBtnvarDetails"
          slot-scope="text, record, index"
        >
          <a :data-data="record" @click="detailsUnbind(record)">解绑</a>
        </span>
      </a-table>
    </a-drawer>
    <!-- 绑定采集设备 结束-->
  </div>
</template>
<script>
import index from "@/assets/js/collection/ioserver.js";
export default {
  ...index,
};
</script>

