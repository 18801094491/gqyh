<!-- 计量中心-计量基础数据录入-合同管理-计价规则-新增、编辑 -->
<template>
  <a-drawer
    :title="title"
    :width="670"
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
          <a-col :span="12">
            <a-form-item label="规则名称">
              <a-input
                v-if="!isShow"
                placeholder="请输入规则名称"
                v-decorator="['ruleName', {}]"
              />
              <p v-if="isShow">{{ model.ruleName }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="计价规则类型">
              <a-radio-group
                v-if="!isShow"
                @change="changeRuleType"
                v-decorator="['ruleType', {}]"
              >
                <a-radio
                  v-for="(item, key) in ruleTypeList"
                  :key="key"
                  :value="item.code"
                  >{{ item.title }}</a-radio
                >
              </a-radio-group>
              <p v-if="isShow">{{ model.ruleTypeName }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 阶梯计价 -->
        <a-row :gutter="24" v-show="form.ruleType === '1'">
          <a-col :span="12">
            <a-form-item label="基础价格">
              <a-input
                v-if="!isShow"
                placeholder="请输入基础价格"
                v-decorator="['benchPrice', {}]"
              />
              <p v-if="isShow">{{ model.benchPrice }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="增幅">
              <a-input
                type="number"
                v-if="!isShow"
                placeholder="请输入增幅"
                v-decorator="['setUp', {}]"
                addonAfter="%"
              />
              <p v-if="isShow">{{ model.setUp }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="阶梯时间">
              <a-input
                type="number"
                v-if="!isShow"
                placeholder="请输入阶梯时间"
                v-decorator="['setTime', {}]"
                addonAfter="月"
              />
              <p v-if="isShow">{{ model.setTime }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 手工录入价格 -->
        <a-row :gutter="24" v-show="form.ruleType === '2'">
          <a-col
            :span="24"
            v-for="(item, i) in recordFormItem"
            :key="i"
            style="padding: 0"
          >
            <a-col :span="5">
              <a-form-item>
                <a-input
                  :disabled="isShow"
                  placeholder="请输入水价"
                  v-decorator="['items_price_' + i, {}]"
                />
              </a-form-item>
            </a-col>
            <a-col :span="7">
              <a-form-item>
                <a-date-picker
                  disabled
                  v-model="form['items_startTime_' + i]"
                  format="YYYY-MM-DD"
                />
              </a-form-item>
            </a-col>
            <a-col :span="1" style="padding-left: 6px">
              <a-form-item><p>至</p></a-form-item>
            </a-col>
            <a-col :span="7">
              <a-form-item>
                <a-date-picker
                  :disabled="isShow || i === recordFormItem.length - 1"
                  v-model="form['items_endTime_' + i]"
                  format="YYYY-MM-DD"
                  @change="
                    (val) => {
                      endDateChange(val, i);
                    }
                  "
                />
              </a-form-item>
            </a-col>

            <a-col :span="4" v-if="!isShow">
              <a-button
                v-if="i === recordFormItem.length - 1"
                @click="addItem"
                icon="plus"
                type="default"
                style="margin-right: 5px;padding;0"
              ></a-button>
              <a-button
                v-if="
                  recordFormItem.length > 1 && i !== recordFormItem.length - 1
                "
                @click="minusItem(i)"
                icon="minus"
                type="default"
                style="padding;0"
              ></a-button>
            </a-col>
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
      <a-button v-if="!isShow" @click="submit" type="primary">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/contractManage/ruleadd";
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