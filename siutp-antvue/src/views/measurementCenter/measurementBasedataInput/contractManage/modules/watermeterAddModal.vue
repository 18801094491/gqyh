<!-- 计量中心-计量基础数据录入-合同管理-水表-新增 -->
<template>
  <a-drawer
    :title="title"
    :width="590"
    @close="closeModal"
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
          <a-col :span="24">
            <a-form-item label="水表">
              <a-select
                v-decorator="['equipmentId', {}]"
                placeholder="请选择水表"
              >
                <a-select-option
                  :value="item.equipmentId"
                  v-for="(item, index) in waterbindList"
                  :key="index"
                  >{{ item.equipmentName }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="计价规则">
              <a-select
                v-decorator="['ruleId', {}]"
                placeholder="请选择计价规则"
              >
                <a-select-option
                  :value="item.id"
                  v-for="(item, index) in contractRuleList"
                  :key="index"
                  >{{ item.ruleName }}</a-select-option
                >
              </a-select>
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
      <a-button :style="{ marginRight: '8px' }" @click="closeModal">
        关闭
      </a-button>
      <a-button @click="submit" type="primary">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/contractManage/meteradd";
export default {
  ...index,
};
</script>

<style lang="less" scoped>
.uploadBtn {
  width: 100px;
  height: 30px;
  line-height: 30px;
  position: relative;
  overflow: hidden;
  #uploadBtn {
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    padding: 1000px;
    cursor: pointer;
  }
}
</style>