<template>
  <a-drawer
    :title="
      typeof model.id === 'undefined' ? title + '非周期月结水表抄表查询' : title
    "
    :width="500"
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
          <a-col :span="24">
            <a-form-item label="水表信息">
              <a-select
                showSearch
                placeholder="请选择水表"
                optionFilterProp="children"
                style="width: 100%"
                @change="handleEquipment"
                v-model="model.equipmentId"
                v-if="typeof model.id === 'undefined'"
              >
                <a-select-option
                  v-for="(item, index) in waterNumberList"
                  :key="index"
                  :value="item.equipmentId"
                  >{{ item.equipmentName }}
                </a-select-option>
              </a-select>
              <p v-else>
                {{ model.equipmentInfo ? model.equipmentInfo : "暂无" }}
              </p>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="抄表日期">
              <a-date-picker
                style="width: 100%"
                v-if="!isShow"
                @change="handleFlowDate"
                v-decorator="['currentFlowTime', {}]"
              />
              <p v-if="isShow">{{ model.currentFlowTime }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="24" v-if="isWaterAndTime">
            <a-form-item label="合同">
              <a-select
                showSearch
                placeholder="请选择合同"
                v-if="!isShow"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.ruleItemId"
              >
                <a-select-option
                  v-for="(item, index) in contractInfos"
                  :key="index"
                  :value="item.ruleItemId"
                  >{{ item.contractName }}[{{ item.price }}]
                </a-select-option>
              </a-select>
              <p v-if="isShow">
                {{ model.contractName }}[{{ model.currentWaterPrice }}]
              </p>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="表底数">
              <a-input-number
                :min="0"
                v-if="!isShow"
                placeholder="请输入表底数"
                v-decorator="['currentFlow', {}]"
              />
              <p v-if="isShow">{{ model.currentFlow }}</p>
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
import index from "@assets/js/measurementCenter/measurementBasedataInput/notCircleWaterUsesInput/modal";
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

/deep/ .ant-input-number {
  width: 100%;
}
</style>