<template>
  <!-- 采集管理-采集设备 -->
  <div>
    <div id="acquisitionEquipmentManagement" class="margin12">
      <div class="screenCommonBox">
        <!-- 搜索按钮区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="24">
              <a-col :md="6" :sm="12">
                <a-form-item label="设备编码">
                  <a-input
                    placeholder="请输入设备编码"
                    v-model="queryParam.iotSn"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="12">
                <a-form-item label="设备名称">
                  <a-input
                    placeholder="请输入设备名称"
                    v-model="queryParam.iotName"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="12">
                <a-form-item label="设备类型">
                  <a-select
                    v-model="queryParam.iotCategory"
                    placeholder="请选择设备类型"
                  >
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option
                      :value="item.code"
                      v-for="(item, index) in inequipmentTypeList"
                      :key="index"
                      >{{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="12">
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
        <div class="table-operator" style="border-top: 5px">
          <a-button
            @click="handleAdd('add')"
            v-has="'user:add'"
            type="primary"
            icon="plus"
            style="margin-right: 6px"
            >新增
          </a-button>
          <a-tooltip placement="top" style="margin-right: 6px">
            <template slot="title">
              <span>批量设置设置类型的采集周期</span>
            </template>
            <a-button
              type="primary"
              icon="setting"
              @click="handleEquipmentCycle()"
              >采集周期</a-button
            >
          </a-tooltip>
          <a-tooltip placement="top">
            <template slot="title">
              <span>维护采集设备状态规则模板信息</span>
            </template>

            <a-button
              type="primary"
              icon="snippets"
              @click="handleAlarAmModel()"
              >告警模板</a-button
            >
          </a-tooltip>
          <a-button
            type="primary"
            style="margin-left: 6px"
            @click="toConfigure()"
            icon="tool"
            >其他配置</a-button
          >
          <a-button
            type="primary"
            style="margin-left: 6px"
            @click="iotEquipmentSync"
            icon="sync"
            >同步策略
          </a-button>
          <a-button
            type="primary"
            style="margin-left: 6px"
            @click="synchronousCache"
            icon="sync"
            >同步缓存
          </a-button>
        </div>
        <!-- 表格显示区域 -->
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
          }"
          @change="handleTableChange"
        >
          <!-- 操作按钮 -->
          <span slot="maintainBtn" slot-scope="text, record, index">
            <a :data-data="record" @click="handleAdd('change', record)">修改</a>
            <a-divider type="vertical" />
            <a @click="bindVarClick(record)">绑定变量</a>
            <a-divider type="vertical" />
            <a @click="binAlarmRulesClick(record)">状态规则</a>
            <a-divider type="vertical" />
            <a @click="reverseControl(record)">反向控制</a>
            <a-divider type="vertical" />
            <a @click="toConfigure(record)">其他配置</a>
            <a-divider type="vertical" />
            <a v-if="record.bindStatus == 0" @click="bindGisShow(record)"
              >资产绑定</a
            >
            <a v-if="record.bindStatus == 1" @click="unbindGis(record)"
              >资产解绑</a
            >
            <!-- <a @click="binVarDetailsClick(record)">变量详情</a>
                        <a-divider type="vertical" />-->
            <!-- <a :data-data="record" v-show="false" @click="bindEquipment(record)">绑定资产</a> -->
            <!-- <a :data-data="record" v-if="record.iotStatus==0" @click="unbindEquipment(record)">设备解绑</a> -->
          </span>
        </a-table>
      </a-card>
      <!-- 新增/修改弹框 start -->
      <a-drawer
        :title="aeTitle"
        :width="720"
        @close="onClose"
        :visible="visible"
        :wrapStyle="{
          height: 'calc(100%)',
          overflow: 'auto',
          paddingBottom: '108px',
        }"
      >
        <a-form layout="vertical" :form="form" hideRequiredMark>
          <a-row :gutter="16">
            <a-col :span="16">
              <a-form-item label="设备编码">
                <a-input v-model="aeInfo.iotSn" placeholder="请输入设备编码" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="16">
              <a-form-item label="设备名称">
                <a-input
                  v-model="aeInfo.iotName"
                  placeholder="请输入设备名称"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="16">
              <a-form-item label=" 采集周期">
                <a-input-number
                  :min="1"
                  v-model="aeInfo.iotCycle"
                  :value="aeInfo.iotCycle"
                  @change="handleNumberChange"
                />
                <a-select
                  v-decorator="[
                    'iotCategoryTime',
                    {
                      initialValue: '1',
                    },
                  ]"
                  style="width: 120px"
                  @change="varTimeChange"
                >
                  <a-select-option value="1">秒</a-select-option>
                  <a-select-option value="60">分</a-select-option>
                  <a-select-option value="3600">时</a-select-option>
                  <a-select-option value="86400">日</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="16">
              <a-form-item label="设备类型">
                <a-select
                  v-decorator="[
                    'iotCategory',
                    { rules: [{ required: true, message: '请选择设备类型!' }] },
                  ]"
                  @change="handleSelectChange"
                  placeholder="请选择设备类型"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in inequipmentTypeList"
                    :key="index"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="16">
              <a-form-item label="设备状态">
                <a-switch
                  checkedChildren="启用"
                  unCheckedChildren="停用"
                  v-model="aeInfo.iotStatus"
                  defaultChecked
                />
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
          <a-button :style="{ marginRight: '8px' }" @click="onClose"
            >关闭</a-button
          >
          <a-button @click="addSubmit" type="primary">提交</a-button>
        </div>
      </a-drawer>
      <!-- 新增/修改弹框 end -->
      <!-- 绑定变量弹框 start -->
      <a-drawer
        :title="bindVarTitle"
        :width="720"
        @close="bindVarCancel"
        :visible="varVisible"
        :wrapStyle="{
          height: 'calc(100%)',
          overflow: 'auto',
          paddingBottom: '108px',
        }"
      >
        <a-form :form="form" layout="inline">
          <a-form-item
            label="名称"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 16 }"
          >
            <a-input
              placeholder="输入变量名称"
              v-model="varQuseryInfo.varQuseryName"
            ></a-input>
          </a-form-item>
          <a-form-item
            style="min-width: 270px"
            label="状态"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 16 }"
          >
            <a-select
              placeholder="选择状态"
              v-model="varQuseryInfo.varQuseryStatus"
            >
              <a-select-option value="2">全部</a-select-option>
              <a-select-option value="1">已绑定</a-select-option>
              <a-select-option value="0">未绑定</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button type="primary" @click="varSearchQuery" icon="search"
              >查询</a-button
            >
          </a-form-item>
        </a-form>
        <a-divider orientation="left">全部变量</a-divider>
        <!-- 表格显示区域 -->
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="varColumns"
          :dataSource="varDataSource"
          :pagination="varPagination"
          :loading="varLoading"
          @change="varTableChange"
        >
          <span
            class="bindBtnBox"
            slot="maintainBtnVar"
            slot-scope="text, record, index"
          >
            <a
              v-if="record.status == 0"
              :data-data="record"
              @click="bindvarOne(record)"
              >绑定</a
            >
            <a v-if="record.status == 1" :data-data="record" disabled>已绑定</a>
          </span>
        </a-table>
        <a-divider></a-divider>

        <a-form :form="form" layout="inline">
          <a-form-item
            label="名称"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 16 }"
          >
            <a-input
              placeholder="输入变量名称"
              v-model="varQuseryInfo.varQuseryName2"
            ></a-input>
          </a-form-item>
          <a-form-item style="min-width: 270px"></a-form-item>
          <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
            <a-button
              type="primary"
              @click="varDetailsSearchQuery()"
              icon="search"
              >查询</a-button
            >
          </a-form-item>
        </a-form>
        <a-divider orientation="left">当前设备绑定变量</a-divider>
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="varDetailsColumns"
          :dataSource="varDetailsDataSource"
          :pagination="varDetailsPagination"
          :loading="varDetailsLoading"
          @change="varDetailsTableChange"
        >
          <span
            class="bindBtnBox"
            slot="maintainBtnvarDetails"
            slot-scope="text, record, index"
          >
            <a :data-data="record" @click="varDetailsUnbind(record)">解绑</a>
          </span>
        </a-table>
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
          <a-button :style="{ marginRight: '8px' }" @click="onVarClose"
            >关闭</a-button
          >
        </div>
      </a-drawer>
      <!-- 绑定变量弹框 end -->

      <!-- 绑定设备 s -->
      <a-modal
        title="绑定设备"
        :visible="bindEquipmentVisible"
        width="800px"
        @ok="bindEquipmentOk"
        @cancel="bindEquipmentCancel"
        :bodyStyle="{ 'max-height': '600px' }"
      >
        <!-- 搜索区域 -->
        <a-form layout="inline" style="margin-bottom: 25px">
          <a-row :gutter="24">
            <a-col :md="20" :sm="8">
              <a-form-item label="模型名称">
                <a-input
                  placeholder="请输入模型名称"
                  v-model="bindEquipmentName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8" style>
              <a-button type="primary" @click="bindEquipmentQuery" icon="search"
                >查询</a-button
              >
            </a-col>
          </a-row>
        </a-form>
        <!-- 表格显示区域 -->
        <a-table
          ref="table"
          bordered
          size="middle"
          rowKey="id"
          :columns="bindEquipmentColumns"
          :dataSource="bindEquipmentDataSource"
          :pagination="bindEquipmentPagination"
          :loading="bindEquipmentloading"
          @change="bindEquipmentTableChange"
        >
          <span slot="maintainBtn2" slot-scope="text, record, index">
            <a :data-data="record" @click="bindEquipmentOne(record)">绑定</a>
          </span>
        </a-table>
      </a-modal>
      <!-- 绑定设备 end -->

      <!-- 批量设置采集周期 -->
      <a-modal
        title="批量设置采集周期"
        :visible="bindEquipmentCycleVisible"
        width="400px"
        @ok="bindEquipmentCycleOk"
        @cancel="bindEquipmentCycleCancel"
      >
        <a-form :form="form">
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="设备类型"
          >
            <a-select v-model="iotCategory" placeholder="请选择设备类型">
              <a-select-option
                :value="item.code"
                v-for="(item, index) in inequipmentTypeList"
                :key="index"
                >{{ item.title }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="采集周期"
          >
            <a-input-number
              :min="1"
              v-model="iotCycleNumber"
              @change="handleNumberChange"
              style="margin-right: 10px"
            />
            <a-select
              v-model="iotCycleType"
              style="width: 120px"
              @change="varTimeChange"
            >
              <a-select-option value="1">秒</a-select-option>
              <a-select-option value="60">分</a-select-option>
              <a-select-option value="3600">时</a-select-option>
              <a-select-option value="86400">日</a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-modal>
      <!-- 批量设置采集周期  end -->

      <div id="alarmModel">
        <!-- 告警模板弹框 start -->
        <a-drawer
          title="模板信息"
          :width="750"
          @close="alarmModelClose"
          :visible="alarmModelVisible"
          :wrapStyle="{
            height: 'calc(100%)',
            overflow: 'auto',
            paddingBottom: '108px',
          }"
        >
          <a-form :form="form" layout="inline">
            <a-form-item
              label="标题"
              :label-col="{ span: 8 }"
              :wrapper-col="{ span: 16 }"
            >
              <a-input
                placeholder="输入模板标题"
                v-model="alarmModelInfo.Title"
              ></a-input>
            </a-form-item>
            <a-form-item style="min-width: 270px"></a-form-item>
            <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
              <a-button
                type="primary"
                @click="alarmModelSearchQuery()"
                icon="search"
                >查询</a-button
              >
            </a-form-item>
          </a-form>

          <a-divider orientation="left">模板信息列表</a-divider>
          <a-button
            type="primary"
            @click="alarmModelEdit('add')"
            :icon="alarmModelInfo.Icon"
            style="margin-bottom: 20px"
            >新增
          </a-button>
          <a-table
            ref="table"
            bordered
            size="middle"
            rowKey="id"
            :columns="alarmModelColumns"
            :dataSource="alarmModelDataSource"
            :pagination="alarmModelPagination"
            :loading="alarmModelLoading"
            @change="alarmModelTableChange"
          >
            <span
              class="bindBtnBox"
              slot="maintainBtnvarDetails"
              slot-scope="text, record, index"
            >
              <a :data-data="record" @click="alarmModelEdit('change', record)"
                >修改</a
              >
              <a-divider type="vertical" />
              <a :data-data="record" @click="alarmModelRemove(record)">删除</a>
            </span>
          </a-table>

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
            <a-button :style="{ marginRight: '8px' }" @click="alarmModelClose"
              >关闭</a-button
            >
          </div>
        </a-drawer>
        <!-- 告警模板弹框 end -->
        <!-- 添加规则 开始 -->
        <a-drawer
          title="添加规则"
          :width="720"
          @close="ruleClose"
          :visible="ruleVisible"
          class="ruleDrawer"
          :wrapStyle="{
            height: 'calc(100%)',
            overflow: 'auto',
            paddingBottom: '108px',
          }"
        >
          <a-divider orientation="left">规则信息</a-divider>
          <a-form>
            <a-row
              :gutter="16"
              v-for="(item, index) in ruleList"
              :key="index"
              class="ruleRow"
              v-if="rulesOk"
            >
              <a-col :span="4">
                <a-select
                  placeholder="请选择"
                  v-show="index == 0"
                  v-model="item.andOr"
                >
                  <a-select-option
                    v-for="(item, index) in andOrList"
                    :key="index"
                    :value="item.code"
                  >
                    {{ item.title }}
                  </a-select-option>
                </a-select>
              </a-col>
              <a-col :span="6">
                <a-select placeholder="请选择变量" v-model="item.variableId">
                  <a-select-option
                    v-for="(item, index) in varList"
                    :key="index"
                    :value="item.id"
                  >
                    {{ item.variableTitle }}
                  </a-select-option>
                </a-select>
              </a-col>
              <a-col :span="4">
                <a-select placeholder="请选择" v-model="item.alarmRule">
                  <a-select-option
                    v-for="(item, index) in ContrastValList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-col>
              <a-col :span="6">
                <a-input
                  placeholder="请输入值"
                  v-model="item.alarmValue"
                ></a-input>
              </a-col>
              <a-col :span="4">
                <a-icon
                  type="plus-circle"
                  class="ruleDrawerIcon"
                  @click="addRule"
                  v-if="index == 0"
                />
                <a-icon
                  type="minus-circle"
                  class="ruleDrawerIcon"
                  @click="removeRule(index)"
                  v-if="index != 0"
                />
              </a-col>
            </a-row>
            <!-- <a-form-item
                          style="min-width: 270px; text-align: left;"
                          label="质量戳"
                          :label-col="{ span: 3 }"
                          :wrapper-col="{ span: 16 }"
                        >
                          <a-switch checkedChildren="是" unCheckedChildren="否" defaultChecked v-model="isMass" />
                        </a-form-item> -->
            <!-- 状态规则+添加规则 开始 -->
            <div v-if="rulesOk2">
              <a-form-item
                style="min-width: 270px; text-align: left"
                label="规则名称"
                :label-col="{ span: 3 }"
                :wrapper-col="{ span: 16 }"
              >
                <a-input
                  placeholder="请输入规则名称"
                  v-model="alarmName"
                ></a-input>
              </a-form-item>
              <a-form-item
                style="min-width: 270px; text-align: left"
                label="选择状态"
                :label-col="{ span: 3 }"
                :wrapper-col="{ span: 16 }"
              >
                <a-select
                  placeholder="选择状态"
                  v-model="rule_type"
                  @change="rule_typeChange"
                >
                  <a-select-option
                    v-for="(item, index) in rule_typeList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item
                style="min-width: 270px; text-align: left"
                label="告警事件"
                :label-col="{ span: 3 }"
                :wrapper-col="{ span: 16 }"
                v-if="rule_typeVal"
              >
                <a-switch
                  checkedChildren="是"
                  unCheckedChildren="否"
                  defaultChecked
                  v-model="alarm2"
                  @change="alarm2Change"
                />
              </a-form-item>
              <a-form-item
                style="min-width: 270px; text-align: left"
                label="告警分级"
                :label-col="{ span: 3 }"
                :wrapper-col="{ span: 16 }"
                v-if="rule_typeVal"
              >
                <a-select placeholder="选择告警分级" v-model="alarmLevel">
                  <a-select-option
                    v-for="(item, index) in warnLevelList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item
                style="min-width: 270px; text-align: left"
                label="告警内容"
                :label-col="{ span: 3 }"
                :wrapper-col="{ span: 16 }"
                v-if="rule_typeVal"
              >
                <a-select placeholder="选择告警内容" v-model="alarmModleId">
                  <a-select-option
                    v-for="(item, index) in alarmContentList"
                    :key="index"
                    :value="item.id"
                    >{{ item.alarmTitle }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </div>
            <!-- 状态规则+添加规则 开始 -->
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
            <a-button :style="{ marginRight: '8px' }" @click="ruleClose"
              >关闭</a-button
            >
            <a-button @click="ruleSubmit" type="primary">提交</a-button>
          </div>
        </a-drawer>
        <!-- 添加规则 结束 -->
        <!-- 新增模板信息弹框 开始 -->
        <a-modal
          :title="templatetitle"
          :width="600"
          :visible="templateVisible"
          @ok="templatehandleOk"
          @cancel="templatehandleCancel"
          cancelText="关闭"
        >
          <a-form :form="form" layout="horizontal">
            <a-form-item
              label="模板标题"
              :label-col="{ span: 4 }"
              :wrapper-col="{ span: 16 }"
            >
              <a-input
                placeholder="输入模板标题"
                v-model="templateTitleVal"
              ></a-input>
            </a-form-item>
            <a-form-item
              label="变量"
              :label-col="{ span: 4 }"
              :wrapper-col="{ span: 16 }"
            >
              <div class="clearfix">
                <p class="varClass" @click="addVar('{{放置位置}}')">
                  <span>放置位置</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{设备类型}}')">
                  <span>设备类型</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{设备名称}}')">
                  <span>设备名称</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{设备编号}}')">
                  <span>设备编号</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{变量名称}}')">
                  <span>变量名称</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{变量值}}')">
                  <span>变量值</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{日期时间}}')">
                  <span>日期时间</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{阈值}}')">
                  <span>阈值</span>
                  <a-icon type="plus-circle" />
                </p>
                <p class="varClass" @click="addVar('{{标段}}')">
                  <span>标段</span>
                  <a-icon type="plus-circle" />
                </p>
              </div>
            </a-form-item>
            <a-form-item
              label="内容"
              :label-col="{ span: 4 }"
              :wrapper-col="{ span: 16 }"
            >
              <a-textarea
                placeholder="输入内容信息"
                v-model="templateVal"
                :rows="4"
              />
            </a-form-item>
            <a-form-item :wrapper-col="{ span: 4, offset: 4 }"> </a-form-item>
          </a-form>
        </a-modal>
        <!-- 新增模板信息弹框 结束 -->
        <!-- 状态规则右侧滑框 开始 -->
        <a-drawer
          :title="stateRuleTitle"
          :width="990"
          @close="alarmRulesClose"
          :visible="alarmRulesVisible"
          :wrapStyle="{
            height: 'calc(100%)',
            overflow: 'auto',
            paddingBottom: '108px',
          }"
        >
          <a-form :form="form" layout="inline">
            <a-row :gutter="24">
              <a-col :span="8">
                <a-form-item label="规则名称">
                  <a-input
                    placeholder="输入规则名称"
                    v-model="alarmName2"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="启停用状态">
                  <a-select
                    v-model="workingStatus"
                    style="width: 170px"
                    placeholder="请选择启停用状态"
                  >
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option
                      :value="item.code"
                      v-for="(item, index) in workingStatusList"
                      :key="index"
                      >{{ item.title }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item>
                  <a-button
                    type="primary"
                    @click="alarmRulesSearchQuery()"
                    icon="search"
                    >查询
                  </a-button>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <a-divider orientation="left">状态规则列表</a-divider>
          <a-button
            type="primary"
            @click="alarmRulesAdd()"
            :icon="alarmModelInfo.Icon"
            style="margin-bottom: 20px"
            >添加规则
          </a-button>
          <a-table
            ref="table"
            bordered
            size="middle"
            rowKey="id"
            :columns="alarmRulesColumns"
            :dataSource="alarmRulesSource"
            :pagination="alarmRulesPagination"
            :loading="alarmRulesLoading"
            @change="alarmRulesTableChange"
          >
            <span slot="alarmRule" slot-scope="text, record, index">
              <div style="text-align: left">{{ record.alarmRule }}</div>
              <!-- <a-tooltip placement="top">
                  <template slot="title">
                    <span>{{record.alarmRule}}</span>
                  </template>
                  <div style="text-align: left;">{{record.alarmRule}}</div>
                </a-tooltip>   -->
            </span>
            <span slot="alarmModleName" slot-scope="text, record, index">
              <div v-if="record.alarmModleName" style="text-align: left">
                {{ record.alarmModleName }}
              </div>
              <!-- <a-tooltip placement="top" v-if="record.alarmModleName">
                              <template slot="title">
                                <span>{{record.alarmModleName}}</span>
                              </template>
                              <div v-if="record.alarmModleName" style="text-align: left;">{{record.alarmModleName}}</div>
                            </a-tooltip>   -->
            </span>
            <span
              class="bindBtnBox"
              slot="maintainBtnvarDetails"
              slot-scope="text, record, index"
            >
              <!-- <a :data-data="record" @click="alarmRuleRemove(record)">删除</a> -->

              <a-switch
                checkedChildren="启用"
                unCheckedChildren="停用"
                defaultChecked
                v-model="record.alarmStatus"
                @change="alarmStatusChange(record.id, $event)"
              />
            </span>
            <span slot="maintain2" slot-scope="text, record, index">
              <a @click="modificationRules(record)">修改策略</a>
              <a-divider type="vertical" />
              <a @click="modifyInformation(record)">修改内容</a>
            </span>
          </a-table>
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
            <a-button :style="{ marginRight: '8px' }" @click="alarmRulesClose"
              >关闭</a-button
            >
          </div>
        </a-drawer>
        <!-- 状态规则右侧滑框 结束 -->
        <!-- 反向控制弹框 开始 -->
        <a-modal
          title="控制中心"
          :width="600"
          :visible="reverseControlVisible"
          @cancel="reverseControlCancel"
          cancelText="关闭"
        >
          <a-button
            @click="reverseControlAddOrChange('add')"
            type="primary"
            icon="plus"
            style="margin-right: 6px; margin-bottom: 25px"
            >新增
          </a-button>
          <a-table
            ref="table"
            bordered
            size="middle"
            rowKey="id"
            :columns="reverseControlColumns"
            :dataSource="reverseControlSource"
            :pagination="reverseControlPagination"
            :loading="reverseControlLoading"
            @change="reverseControlTableChange"
          >
            <span
              class="bindBtnBox"
              slot="maintainBtnvarDetails"
              slot-scope="text, record, index"
            >
              <a
                :data-data="record"
                @click="reverseControlAddOrChange('change', record)"
                >修改</a
              >
              <a-divider type="vertical" />
              <a :data-data="record" @click="reomeveIotControId(record)"
                >删除</a
              >
              <!-- <a :data-data="record" @click="alarmRuleRemove(record)">执行</a> -->
            </span>
          </a-table>
          <template slot="footer">
            <a-button
              type="primary"
              @click.stop="
                () => {
                  reverseControlVisible = false;
                }
              "
              >关闭</a-button
            >
          </template>
        </a-modal>
        <!-- 反向控制弹框 结束 -->
        <!-- 反向控制新增弹框 开始 -->
        <a-modal
          :title="controlTitle"
          :width="600"
          :visible="controlVisible"
          @cancel="controlCancel"
          @ok="controlOk"
          cancelText="关闭"
        >
          <a-form :form="form">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="控制名称"
            >
              <a-input
                placeholder="输入控制名称"
                v-model="controName"
              ></a-input>
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="绑定变量"
            >
              <a-select
                v-model="variableId"
                placeholder="请选择绑定变量"
                @change="variableIdChange"
              >
                <a-select-option
                  :value="item.id"
                  v-for="(item, index) in varList"
                  :key="index"
                  >{{ item.variableName }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="变量值"
            >
              <a-input
                v-if="varType != 5 && varType != 2"
                maxLength="8"
                placeholder="输入变量值"
                @keyup="clearNoNum($event)"
                v-model="variableValue"
              ></a-input>
              <a-input
                v-if="varType == 2"
                maxLength="8"
                placeholder="输入变量值"
                v-model="variableValue"
              ></a-input>
              <a-select
                v-if="varType == 5"
                v-model="variableValue2"
                placeholder="请选择变量值"
              >
                <a-select-option value="true">true</a-select-option>
                <a-select-option value="false">false</a-select-option>
              </a-select>
            </a-form-item>
          </a-form>
        </a-modal>
        <!-- 反向控制新增弹框 结束 -->
        <!-- 其他配置 开始 -->
        <a-modal
          title="其他配置"
          :visible="toConfigureVisible"
          width="500px"
          @ok="toConfigureOk"
          @cancel="toConfigureCancel"
        >
          <a-form>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="设备类型"
              v-if="toConfigurebOk"
            >
              <a-select v-model="typeCode" placeholder="请选择设备类型">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in inequipmentTypeList"
                  :key="index"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="启用质量戳"
            >
              <a-switch
                checkedChildren="是"
                unCheckedChildren="否"
                defaultChecked
                v-model="checkQuality"
              />
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="是否告警"
            >
              <a-switch
                checkedChildren="是"
                unCheckedChildren="否"
                defaultChecked
                v-model="alarm"
                @change="alarmChange"
              />
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="告警等级"
            >
              <a-select v-model="alarmLevel2" placeholder="请选择告警等级">
                <a-select-option
                  v-for="(item, index) in warnLevelList"
                  :key="index"
                  :value="item.code"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="告警模板"
            >
              <a-select v-model="alarmModel" placeholder="请选择告警模板">
                <a-select-option
                  v-for="(item, index) in alarmContentList"
                  :key="index"
                  :value="item.id"
                >
                  {{ item.alarmTitle }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-form>
        </a-modal>
        <!-- 其他配置 end -->
        <!-- 绑定设备滑框开始 -->
        <a-modal
          title="绑定设备"
          :visible="bindGisVisible"
          width="800px"
          @ok="bindOnOk"
          @cancel="bindGisCancel"
        >
          <!-- 搜索区域 -->
          <a-form layout="inline" style="margin-bottom: 25px">
            <a-row :gutter="24">
              <a-col :md="9" :sm="8">
                <a-form-item label="资产名称">
                  <a-input
                    placeholder="请输入资产名称"
                    v-model="gismodelName"
                  ></a-input>
                </a-form-item>
              </a-col>

              <a-col :md="6" :sm="8" style="margin-top: 4px">
                <a-button type="primary" @click="gissearchQuery2" icon="search"
                  >查询</a-button
                >
              </a-col>
            </a-row>
          </a-form>
          <!-- 表格显示区域 -->
          <a-table
            ref="table"
            bordered
            size="middle"
            rowKey="id"
            :columns="giscolumns2"
            :dataSource="gisdataSource2"
            :pagination="gisipagination2"
            :loading="gisloading2"
            @change="gishandleTableChange2"
          >
            <span slot="maintainBtn2" slot-scope="text, record, index">
              <a :data-data="record" @click="bindShow2(record)">绑定</a>
            </span>
          </a-table>
          <template slot="footer">
            <a-button type="primary" @click="bindGisCancel">关闭</a-button>
          </template>
        </a-modal>
        <!-- 绑定设备滑框结束 -->
      </div>
    </div>
  </div>
</template>
    
<script>
import index from "@/assets/js/collection/equipment.js";

export default {
  ...index,
};
</script>

