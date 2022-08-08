<!-- 运营中心-班组班次-班组管理-新增 -->
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
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="班组名称">
              <a-input
                placeholder="请输入班组名称"
                v-decorator="['teamName', {}]"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="选择班次">
              <a-select placeholder="请选择班次" v-decorator="['shiftsId', {}]">
                <a-select-option
                  :value="item.id"
                  v-for="(item, index) in workTeamShiftsList"
                  :key="index"
                  >{{ item.shiftsName }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="班组有效期">
              <a-date-picker
                style="width: 175px"
                v-decorator="['startTime', {}]"
              />-
              <a-date-picker
                style="width: 175px"
                v-decorator="['overTime', {}]"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选择人员">
              <a-select
                mode="multiple"
                placeholder="选择人员"
                optionFilterProp="children"
                style="width: 100%"
                v-decorator="['userIds', {}]"
              >
                <a-select-option
                  :value="item.id"
                  v-for="(item, index) in managerList"
                  :key="index"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="选择管理员">
              <a-select
                mode="multiple"
                placeholder="选择管理员"
                optionFilterProp="children"
                style="width: 100%"
                v-decorator="['userManageIds', {}]"
              >
                <a-select-option
                  :value="item.id"
                  v-for="(item, index) in managerList2"
                  :key="index"
                >
                  {{ item.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="班组描述">
              <a-textarea
                placeholder="请填写班组描述"
                :rows="4"
                v-decorator="['teamDescribe', {}]"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
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
      <a-button @click="handleOk" type="primary">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import index from "@/assets/js/teamanagement/workTeamModal.js";
export default {
  ...index,
};
</script>