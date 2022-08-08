<!-- 运营中心-告警事件 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="告警名称">
                <a-input
                  placeholder="请输入告警名称"
                  v-model="queryParam.warnName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="设备类型">
                <a-select
                  v-model="queryParam.equipmentType"
                  placeholder="请选择告警类型"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    v-for="(item, index) in inequipmentTypeList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="8">
              <a-form-item label="告警状态">
                <a-select
                  v-model="queryParam.warnStatus"
                  placeholder="请选择告警状态"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    v-for="(item, index) in warnStatusList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="5" :sm="8">
              <a-form-item label="告警等级">
                <a-select
                  v-model="queryParam.warnLevel"
                  placeholder="请选择告警等级"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    v-for="(item, index) in warnLevelList"
                    :key="index"
                    :value="item.code"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="14" :sm="8">
              <a-form-item label="告警时间">
                <el-date-picker
                  v-model="queryParam.startTime"
                  placeholder="请选择开始时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm"
                ></el-date-picker>
                -
                <el-date-picker
                  v-model="queryParam.endTime"
                  placeholder="请选择结束时间"
                  type="datetime"
                  format="yyyy-MM-dd HH:mm"
                ></el-date-picker>
                <!-- <a-date-picker v-model="queryParam.startTime"  format="YYYY-MM-DD HH:mm" showTime :showTime="{ format: 'HH:mm' }"/> - <a-date-picker format="YYYY-MM-DD HH:mm"  v-model="queryParam.endTime" showTime :showTime="{ format: 'HH:mm' }"/> -->
              </a-form-item>
            </a-col>

            <a-col :md="5" :sm="8">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button
                  type="primary"
                  @click="searchQuery('min')"
                  icon="search"
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
          @click="notificationStrategy"
          type="primary"
          v-has="'business:config'"
          >通知策略</a-button
        >
        <a-tooltip placement="top">
          <template slot="title">
            <span>配置告警事件是否展示</span>
          </template>

          <a-button
            @click="alarmEventConfiguration"
            type="primary"
            v-has="'business:config'"
            >告警事件配置
          </a-button>
        </a-tooltip>
      </div>

      <!-- table区域-begin -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ x: 1400 }"
          @change="handleTableChange"
        >
          <span slot="warnName" slot-scope="text, record">
            <a-tooltip placement="top">
              <template slot="title">
                <span>{{ record.warnName }}</span>
              </template>
              <div>
                {{
                  record.warnName.length > 10
                    ? record.warnName.substring(0, 10) + "..."
                    : record.warnName
                }}
              </div>
            </a-tooltip>
          </span>
          <span
            v-if="record.warnContent"
            slot="warnContent"
            slot-scope="text, record"
          >
            <a-tooltip placement="top">
              <template slot="title">
                <span>{{ record.warnContent }}</span>
              </template>
              <div>
                {{
                  record.warnContent.length > 10
                    ? record.warnContent.substring(0, 10) + "..."
                    : record.warnContent
                }}
              </div>
            </a-tooltip>
          </span>
          <span
            v-if="record.ruleContent"
            slot="ruleContent"
            slot-scope="text, record"
          >
            <a-tooltip placement="top">
              <template slot="title">
                <span>{{ record.ruleContent }}</span>
              </template>
              <div>
                {{
                  record.ruleContent.length > 10
                    ? record.ruleContent.substring(0, 10) + "..."
                    : record.ruleContent
                }}
              </div>
            </a-tooltip>
          </span>
          <span slot="action" slot-scope="text, record">
            <a @click="details(record)">详情</a>
            <a-divider
              v-if="record.confirmStatus == 1 && record.warnStatusCode != 2"
              type="vertical"
            />
            <a
              v-if="record.confirmStatus == 1 && record.warnStatusCode != 2"
              @click="alarmHandling(record, 'cl')"
              >处理</a
            >
            <a-divider
              v-if="record.confirmStatus == 0 || record.confirmStatus == null"
              type="vertical"
              v-has="'business:operate'"
            />
            <a
              v-if="record.confirmStatus == 0 || record.confirmStatus == null"
              @click="warnConfirm(record)"
              v-has="'business:operate'"
              >确认</a
            >
            <a-divider
              v-if="record.confirmStatus == 0 || record.confirmStatus == null"
              type="vertical"
              v-has="'business:operate'"
            />
            <a
              v-if="record.confirmStatus == 0 || record.confirmStatus == null"
              @click="alarmHandling(record, 'gb')"
              v-has="'business:operate'"
              >关闭</a
            >
            <a-divider type="vertical" v-if="record.warnStatusCode == '0'" />
            <a @click="assignWoker(record)" v-if="record.warnStatusCode == '0'"
              >分派工单</a
            >
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->
    </a-card>
    <!-- 详情弹窗 -->
    <a-drawer
      title="详情"
      :width="600"
      @close="onClose2"
      :visible="visible2"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div class="table-page-search-wrapper alarmDetailsBox">
        <a-form layout="inline">
          <div class="alarmDetailsDivBox">
            <h4></h4>
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="事件编号">
                  <p>{{ warnSn }}</p>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="所属资产">
                  <p>{{ equipmentType }}</p>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="告警等级">
                  <p>{{ warnLevel }}</p>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="告警时间">
                  <p>{{ warnTime }}</p>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="告警状态">
                  <p>{{ warnStatus }}</p>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="告警渠道">
                  <p>{{ warnWay }}</p>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="操作时间">
                  <p>{{ operateTime }}</p>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="操作人员">
                  <p>{{ operator }}</p>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="24">
              <a-col :span="12">
                <a-form-item label="持续时长">
                  <p>{{ duration }}</p>
                </a-form-item>
              </a-col>
            </a-row>
          </div>

          <div class="alarmDetailsDivBox">
            <h4></h4>
            <a-row :gutter="24">
              <a-col :span="24">
                <a-form-item label="告警名称">
                  <p>{{ warnName }}</p>
                </a-form-item>
              </a-col>
            </a-row>
          </div>
          <div class="alarmDetailsDivBox">
            <h4></h4>
            <a-row :gutter="24">
              <a-col :span="24">
                <a-form-item label="告警内容">
                  <p>{{ warnContent }}</p>
                </a-form-item>
              </a-col>
            </a-row>
            <!--<div class="clearfix"  style="margin-bottom:1em">
                          <label style="width:77px; color: rgba(0, 0, 0, 0.85); font-size:14px;" class="fl">告警内容:</label>
                          <span style="width:460px; color: rgba(0, 0, 0, 0.65); font-size:14px;" class="fl">{{warnContent}}</span>
                        </div>-->
          </div>
          <div class="alarmDetailsDivBox">
            <h4></h4>
            <a-row :gutter="24">
              <a-col :span="24">
                <a-form-item label="规则详情">
                  <p>{{ ruleContent }}</p>
                </a-form-item>
              </a-col>
            </a-row>
            <!--<div class="clearfix" style="margin-bottom:1em">
                          <label  style="width:77px;color: rgba(0, 0, 0, 0.85); font-size:14px;" class="fl">规则详情:</label>
                          <span style="width:460px; color: rgba(0, 0, 0, 0.65); font-size:14px;" class="fl">{{ruleContent}}</span>
                        </div>-->
          </div>
        </a-form>
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
        <a-button :style="{ marginRight: '8px' }" @click="onClose2">
          关闭
        </a-button>
      </div>
    </a-drawer>
    <!-- 处理 -->
    <a-modal
      title="事件处置"
      :width="450"
      :visible="alarmHandlingvisible"
      @ok="alarmHandlingOk"
      @cancel="alarmHandlingCancel"
      cancelText="关闭"
    >
      <a-form>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
          <a-textarea
            placeholder="请输入备注"
            :rows="4"
            v-model="description"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
      title="事件关闭"
      :width="450"
      :visible="turnOffAlarmvisible"
      @ok="turnOffAlarmOk"
      @cancel="turnOffAlarmCancel"
      cancelText="关闭"
    >
      <a-form>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
          <a-textarea
            placeholder="请输入备注"
            :rows="4"
            v-model="closeDescription"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-drawer
      title="通知策略"
      :width="800"
      @close="notificationStrategyonClose"
      :visible="notificationStrategyVisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-form layout="inline" style="margin-bottom: 25px">
        <a-form-item
          label="策略名称"
          :label-col="{ span: 8 }"
          :wrapper-col="{ span: 16 }"
        >
          <a-input placeholder="输入策略名称" v-model="policyName"></a-input>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 24 }" style="float: right">
          <a-button
            type="primary"
            @click="notificationStrategySearchQuery"
            icon="search"
            >查询</a-button
          >
        </a-form-item>
      </a-form>
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="margin-bottom: 25px">
        <a-button @click="notificationStrategyDefinition('add')" type="primary"
          >新增策略</a-button
        >
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="notificationStrategycolumns"
        :dataSource="notificationStrategydataSource"
        :pagination="notificationStrategyipagination"
        :loading="notificationStrategyloading"
        @change="notificationStrategyhandleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="notificationStrategyDefinition('change', record)">修改</a>
          <a-divider type="vertical" />
          <a @click="warnDeleteClick(record)">删除</a>
        </span>
        <a-switch
          slot="workStatusCode"
          checkedChildren="启用"
          slot-scope="text, record, index"
          unCheckedChildren="停用"
          v-model="record.workStatusCode"
          defaultChecked
          @change="workStatusCodeChange(record)"
        />
        <span slot="list" slot-scope="text, record">
          <p
            v-if="item.userChooseType"
            v-for="(item, index) in record.list"
            :key="index"
          >
            <span>{{ item.userChooseType }}：</span
            ><span>{{ item.dataName }}</span>
          </p>
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
        <a-button
          :style="{ marginRight: '8px' }"
          @click="notificationStrategyonClose"
        >
          关闭
        </a-button>
      </div>
    </a-drawer>
    <a-modal
      :title="notificationStrategyDefinitionTitle"
      :width="500"
      :visible="notificationStrategyDefinitionvisible"
      @ok="notificationStrategyDefinitionOk"
      @cancel="notificationStrategyDefinitionCancel"
      cancelText="关闭"
    >
      <a-form>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="策略名称"
        >
          <a-input placeholder="输入策略名称" v-model="name"></a-input>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="告警等级"
        >
          <a-select
            placeholder="选择告警等级"
            optionFilterProp="children"
            style="width: 100%"
            v-model="warnLevel2"
          >
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
          label="消息模板"
        >
          <a-select
            placeholder="选择消息模板"
            optionFilterProp="children"
            style="width: 100%"
            v-model="messageTemplate"
          >
            <a-select-option
              v-for="(item, index) in messageTemplateList"
              :key="index"
              :value="item.code"
            >
              {{ item.templateName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="通知角色"
        >
          <a-select
            placeholder="选择通知角色"
            optionFilterProp="children"
            style="width: 100%"
            mode="multiple"
            v-model="rolesId"
          >
            <a-select-option
              v-for="(item, index) in rolesIdList"
              :key="index"
              :value="item.id"
            >
              {{ item.roleName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="通知用户"
        >
          <a-select
            placeholder="选择通知用户"
            optionFilterProp="children"
            style="width: 100%"
            mode="multiple"
            v-model="usersId"
          >
            <a-select-option
              v-for="(item, index) in managerList2"
              :key="index"
              :value="item.id"
            >
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="通知班组"
        >
          <a-select
            placeholder="选择通知班组"
            optionFilterProp="children"
            style="width: 100%"
            mode="multiple"
            v-model="workTeamsId"
          >
            <a-select-option
              v-for="(item, index) in workTeamsIdList"
              :key="index"
              :value="item.id"
            >
              {{ item.teamName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="启用状态"
        >
          <a-switch
            checkedChildren="已启用"
            unCheckedChildren="已禁用"
            defaultChecked
            v-model="workStatus"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
      title="告警事件配置"
      :width="510"
      :visible="alarmEventConfigurationvisible"
      @ok="alarmEventConfigurationOk"
      @cancel="alarmEventConfigurationCancel"
      cancelText="关闭"
    >
      <a-alert message="此配置控制监控中心告警事件是否展示" type="success" />
      <div class="alarmEventConfigurationBox">
        <a-form>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="告警等级"
          >
            <a-checkbox-group
              v-model="alarmEventConfigurationList"
              @change="alarmEventConfigurationonChange"
            >
              <a-checkbox
                v-for="(item, index) in warnLevelList"
                :value="item.code"
                :key="index"
              >
                {{ item.title }}
              </a-checkbox>
            </a-checkbox-group>
          </a-form-item>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="设备类型"
          >
            <a-checkbox-group v-model="typeConfigList" class="cust-checkbox">
              <a-checkbox
                v-for="(item, index) in inequipmentTypeList"
                :value="item.code"
                :key="index"
              >
                {{ item.title }}
              </a-checkbox>
            </a-checkbox-group>
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
    <a-modal
      title="分派工单"
      :visible="workerVisible"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="设备信息">
              <a-select
                v-model="equipmentId"
                placeholder="请选择设备信息"
                optionFilterProp="children"
                mode="multiple"
              >
                <a-select-option
                  v-for="(item, index) in equipmentList"
                  :key="index"
                  :value="item.id"
                >
                  {{ item.equipmentSn }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="选择班组">
              <a-select v-model="workTeamId" placeholder="请选择班组">
                <a-select-option
                  v-for="(item, index) in teamInformationList"
                  :key="index"
                  :value="item.id"
                  >{{ item.teamName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import index from "@/assets/js/operationCenter/alarmManagement/businessWarnList.js";
import "@/assets/less/operationCenter/alarmManagement.less";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>