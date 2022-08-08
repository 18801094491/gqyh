<!-- 运营中心-班组班次-班组排班 -->
<template>
  <div class="margin12">
    <a-card :bordered="false">
      <a-button type="primary" @click="addTeamScheduling" icon="plus"
        >新增班组排班</a-button
      >
      <a-button
        type="primary"
        icon="download"
        style="margin-left: 30px"
        @click="handleExportXls('班组排班')"
        >导出班组排班</a-button
      >
      <a-calendar
        class="rili"
        @panelChange="onPanelChange"
        @select="onSelect"
        @change="dateChange"
      >
        <ul class="events" slot="dateCellRender" slot-scope="value">
          <li
            v-if="value.format('YYYY-MM-DD') == item.dutyDay"
            v-for="item in listData"
            :key="item.content"
          >
            <p v-for="(item2, index2) in item.list" :key="index2">
              {{ item2.teamName }}({{ item2.shiftsName }})
            </p>
          </li>
        </ul>
      </a-calendar>
    </a-card>
    <a-modal
      title="新增班组信息"
      :width="500"
      :visible="visible"
      :confirmLoading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭"
    >
      <a-spin :spinning="confirmLoading">
        <a-form :form="form">
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="班组信息"
          >
            <a-select
              placeholder="请选择班组信息"
              v-model="teamInformation"
              @change="teamInformationChange"
            >
              <a-select-option
                v-for="(item, index) in teamInformationList"
                :key="index"
                :value="item.id"
                >{{ item.teamName }}</a-select-option
              >
            </a-select>
          </a-form-item>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="班次名称"
          >
            <a-input v-model="shiftsName" disabled />
          </a-form-item>
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="排班日期"
          >
            <a-range-picker v-model="dateString" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
    <a-modal
      title="当日班组排班详情"
      width="30%"
      :visible="ecuvisible2"
      :destroyOnClose="true"
      :keyboard="false"
      :maskClosable="false"
      @cancel="handleCancel2"
      class="deails"
    >
      <div
        class="clearfix"
        v-for="(item, index) in schedulingDetailsList"
        :key="index"
      >
        <p class="fl">{{ item.teamName }}({{ item.shiftsName }})</p>
        <a
          class="fr"
          href="javascript:;"
          @click="removeSchedulingDetails(item.id)"
          >删除</a
        >
      </div>

      <template slot="footer">
        <a-button
          type="primary"
          @click.stop="
            () => {
              ecuvisible2 = false;
            }
          "
          >关闭</a-button
        >
      </template>
    </a-modal>
  </div>
</template>
<script>
import index from "@/assets/js/workforceManagement/index.js";
import "@assets/less/workforceManagement/style.less";
export default {
  ...index,
};
</script>
<style scoped>
.events {
  list-style: none;
  margin: 0;
  padding: 0;
}
.events .ant-badge-status {
  overflow: hidden;
  white-space: nowrap;
  width: 100%;
  text-overflow: ellipsis;
  font-size: 12px;
}
.notes-month {
  text-align: center;
  font-size: 28px;
}
.notes-month section {
  font-size: 28px;
}
</style>