<!-- 计量中心-计量基础数据录入-合同管理-计价规则 -->
<template>
  <div>
    <a-drawer
      :title="stateRuleTitle"
      :width="800"
      @close="closeModal"
      :visible="modalVisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-button
        type="primary"
        @click="pricingRulesAdd()"
        icon="plus"
        style="margin-bottom: 20px"
        >新增计价规则</a-button
      >
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="tableChange"
      >
        <span slot="action" slot-scope="text, record">
          <!-- <a @click="handleEdit(record)" v-has="'contract:edit'">编辑</a> -->
          <a @click="details(record, 'edit')">编辑</a>
          <!-- <a-divider type="vertical" v-has="'contract:edit'" /> -->
          <a-divider type="vertical" />
          <a @click="details(record, 'show')">详情</a>
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
        <a-button :style="{ marginRight: '8px' }" @click="closeModal"
          >关闭</a-button
        >
      </div>
    </a-drawer>
    <!--计价规则新增-->
    <pricing-rules-add-modal
      ref="rulesAddForm"
      @submitSuccess="submitSuccess"
    ></pricing-rules-add-modal>
  </div>
</template>
<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/contractManage/rulelist";
export default {
  ...index,
};
</script>

<style>
</style>
