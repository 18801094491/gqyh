<template>
  <a-drawer
    title="工具申请"
    width="100%"
    @close="toolApplicationonClose"
    :visible="toolApplicativisible"
    :wrapStyle="{
      height: 'calc(100%)',
      overflow: 'auto',
      paddingBottom: '108px',
    }"
  >
    <a-card :bordered="false">
      <div class="applicationContentBox">
        <div class="applicationContentDivBox">
          <div
            class="applicationContent"
            v-if="applicationContentList.length > 0"
            v-for="(item, index) in applicationContentList"
            :key="index"
          >
            <a-form :form="form">
              <a-row>
                <a-col :md="3" :sm="8">
                  <h3>工具基本信息</h3>
                </a-col>
                <a-col :md="1" :sm="8" style="text-align: center">
                  <a-button
                    type="primary"
                    @click="addApplicationContent"
                    v-if="index == 0"
                    icon="plus"
                    >增加</a-button
                  >
                  <a-button
                    type="primary"
                    @click="removeApplicationContent(index)"
                    v-if="index > 0"
                    icon="minus"
                    >删除</a-button
                  >
                </a-col>
              </a-row>
              <a-row :gutter="16">
                <a-col :md="10" :sm="8">
                  <a-form-item label="仓库">
                    <a-select
                      placeholder="请选择仓库"
                      v-model="item.storeId"
                      @change="queryStoreListChange(index, $event)"
                    >
                      <a-select-option
                        v-for="(item2, index2) in queryStoreList"
                        :key="index2"
                        :value="item2.storeSn"
                        >{{ item2.storePosition }}</a-select-option
                      >
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="10" :sm="8">
                  <a-form-item label="工具信息">
                    <a-select
                      placeholder="请选择工具信息"
                      v-model="item.dataNameCode"
                      @change="operatToolListChange(index, $event)"
                    >
                      <a-select-option
                        v-for="(item3, index3) in item.operatToolList"
                        :key="index3"
                        :value="item3.code"
                        >{{ item3.title }}</a-select-option
                      >
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-row>

              <a-row :gutter="16">
                <a-col :md="10" :sm="8">
                  <a-form-item label="规格">
                    <a-select
                      placeholder="请选择规格"
                      v-model="item.toolModelCode"
                      @change="changeToolModelCode(index, $event)"
                    >
                      <a-select-option
                        v-for="(item4, index4) in item.toolModelList"
                        :key="index4"
                        :value="item4.id"
                        >{{ item4.toolModel }}</a-select-option
                      >
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="10" :sm="8">
                  <a-form-item label="申请数量">
                    <a-input
                      placeholder="请输入申请数量"
                      v-model="item.amount"
                      @keyup="item.amount = item.amount.replace(/\D/g, '')"
                    ></a-input>
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <div>
            <a-form :form="form">
              <a-row :gutter="16">
                <a-col :md="10" :sm="8">
                  <a-form-item label="期望领取时间">
                    <el-date-picker
                      style="width: 100%"
                      v-model="form.wantedTime"
                      placeholder="请选择时间"
                      type="datetime"
                      format="yyyy-MM-dd HH:mm"
                    ></el-date-picker>
                    <!-- <a-date-picker showTime :showTime="{ format: 'HH:mm' }" format="YYYY-MM-DD HH:mm" style="width:100%" v-decorator="['wantedTime', {}]"/> -->
                  </a-form-item>
                </a-col>
                <a-col :md="10" :sm="8">
                  <a-form-item label="预计归还时间">
                    <el-date-picker
                      style="width: 100%"
                      v-model="form.expectbackTime"
                      placeholder="请选择时间"
                      type="datetime"
                      format="yyyy-MM-dd HH:mm"
                    ></el-date-picker>
                    <!-- <a-date-picker showTime :showTime="{ format: 'HH:mm' }" format="YYYY-MM-DD HH:mm" style="width:100%"  v-decorator="['expectbackTime', {}]"/> -->
                  </a-form-item>
                </a-col>
              </a-row>

              <a-row :gutter="16">
                <a-col :md="10" :sm="8">
                  <a-form-item label="紧急程度">
                    <a-select
                      placeholder="请选择紧急程度"
                      v-model="emergentLevel"
                    >
                      <a-select-option
                        v-for="(item, index) in emergentLevelList"
                        :key="index"
                        :value="item.code"
                        >{{ item.title }}</a-select-option
                      >
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-row>
              <a-row :gutter="16">
                <a-col :md="18" :sm="8">
                  <a-form-item label="备注">
                    <a-textarea
                      placeholder="请输入备注"
                      :rows="4"
                      v-model="reason"
                    />
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <div class="applicationContentBtn">
            <a-button type="primary" @click="tjOptGoodsApplyOne" class="tj"
              >提交申请</a-button
            >
          </div>
        </div>
      </div>
    </a-card>
  </a-drawer>
</template>
<script>
import index from "@/assets/js/operationCenter/toolApply.js";
import "@/assets/less/operationCenter/toolApply.less";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>