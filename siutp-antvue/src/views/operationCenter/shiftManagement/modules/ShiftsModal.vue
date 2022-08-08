<!-- 运营中心-班组班次-班次管理-新增、详情 -->
<template>
  <a-drawer
    :title="title"
    :width="800"
    @close="handleCancel"
    :visible="visible"
    :wrapStyle="{
      height: 'calc(100%)',
      overflow: 'auto',
      paddingBottom: '108px',
    }"
  >
    <div class="table-page-search-wrapper">
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :span="12">
            <!-- <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="班次名称">
                <a-input v-if="!isShow" placeholder="请输入班次名称" v-decorator="['shiftsName', {}]" />
                <p v-if="isShow">{{model.shiftsName}}</p>
              </a-form-item> -->
            <a-form-item label="班次名称">
              <a-input
                v-if="!isShow"
                placeholder="请输入班次名称"
                v-decorator="['shiftsName', {}]"
              />
              <p v-if="isShow">{{ model.shiftsName }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="班次描述">
              <a-input
                v-if="!isShow"
                placeholder="请输入班次描述"
                v-decorator="['shiftsDescribe', {}]"
              />
              <p v-if="isShow">{{ model.shiftsDescribe }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="上班时间">
              <a-time-picker
                style="width: 100%"
                v-if="!isShow"
                format="HH:mm"
                v-decorator="[
                  'startTime',
                  {
                    initialValue: !model.startTime
                      ? null
                      : moment(model.startTime, dateFormat),
                  },
                ]"
              />
              <p v-if="isShow">{{ model.startTime }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="下班时间">
              <a-time-picker
                style="width: 100%"
                v-if="!isShow"
                format="HH:mm"
                v-decorator="[
                  'overTime',
                  {
                    initialValue: !model.overTime
                      ? null
                      : moment(model.overTime, dateFormat),
                  },
                ]"
              />
              <p v-if="isShow">{{ model.overTime }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="开始休息时间">
              <a-time-picker
                style="width: 100%"
                v-if="!isShow"
                format="HH:mm"
                v-decorator="[
                  'restStartTime',
                  {
                    initialValue: !model.restStartTime
                      ? null
                      : moment(model.restStartTime, dateFormat),
                  },
                ]"
              />
              <p v-if="isShow">{{ model.restStartTime }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="结束休息时间">
              <a-time-picker
                style="width: 100%"
                v-if="!isShow"
                format="HH:mm"
                v-decorator="[
                  'restOverTime',
                  {
                    initialValue: !model.restOverTime
                      ? null
                      : moment(model.restOverTime, dateFormat),
                  },
                ]"
              />
              <p v-if="isShow">{{ model.restOverTime }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="班次状态">
              <a-select
                v-if="!isShow"
                v-decorator="['shiftsStatus', {}]"
                placeholder="请选择班次状态"
              >
                <a-select-option value="1">启用</a-select-option>
                <a-select-option value="0">停用</a-select-option>
              </a-select>
              <p v-if="isShow">
                {{ model.shiftsStatus == 0 ? "停用" : "启用" }}
              </p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="上班时长">
              <a-select
                v-if="!isShow"
                v-decorator="['workHours', {}]"
                placeholder="请选择上班时长"
              >
                <a-select-option value="1">1小时</a-select-option>
                <a-select-option value="2">2小时</a-select-option>
                <a-select-option value="3">3小时</a-select-option>
                <a-select-option value="4">4小时</a-select-option>
                <a-select-option value="5">5小时</a-select-option>
                <a-select-option value="6">6小时</a-select-option>
                <a-select-option value="7">7小时</a-select-option>
                <a-select-option value="8">8小时</a-select-option>
                <a-select-option value="9">9小时</a-select-option>
                <a-select-option value="10">10小时</a-select-option>
                <a-select-option value="11">11小时</a-select-option>
                <a-select-option value="12">12小时</a-select-option>
              </a-select>
              <p v-if="isShow">{{ model.workHours }}小时</p>
            </a-form-item>
          </a-col>
        </a-row>
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
      <a-button :style="{ marginRight: '8px' }" @click="handleCancel">
        关闭
      </a-button>
      <a-button v-if="!isShow" @click="handleOk" type="primary">提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
import index from "@/assets/js/operationCenter/shiftManagement/modules/shiftsModal.js";

export default {
  ...index,
};
</script>