<!-- 计量中心-计量基础数据录入-合同管理-水表 -->
<template>
  <div>
    <a-drawer
      :title="stateRuleTitle"
      :width="720"
      @close="closeWatermeterModal"
      :visible="modalVisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-button
        type="primary"
        @click="watermeterAdd()"
        icon="plus"
        style="margin-bottom: 20px"
        >新增</a-button
      >
      <a-table
        ref="table"
        bordered
        size="middle"
        row-key="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
      >
        <span slot="action" slot-scope="text, record">
          <a @click="handleBind(record)">解绑</a>
        </span>
      </a-table>
      <!-- 水表解绑弹窗 -->
      <a-modal
        title="解除绑定"
        :visible="unbindVisible"
        @cancel="handleCancelBind"
      >
        <template slot="footer">
          <a-button key="submit" type="primary" @click="handleUnbind"
            >确定</a-button
          >
          <a-button key="back" @click="handleCancelBind">取消</a-button>
        </template>
        <p>解绑水表请去计量中心进行水费结算</p>
      </a-modal>

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
        <a-button :style="{ marginRight: '8px' }" @click="closeWatermeterModal"
          >关闭</a-button
        >
      </div>
    </a-drawer>
    <watermeter-add-modal
      :openAddVisible="openAddVisible"
      :contractId="contractId"
      :customerId="customerId"
      @closeModal="closeAddModal"
      ref="watermeterAddRef"
    ></watermeter-add-modal>
  </div>
</template>
<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/contractManage/meterlist";
export default {
  ...index,
};
</script>

<style>
</style>
