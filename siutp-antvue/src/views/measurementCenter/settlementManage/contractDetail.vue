<!-- 计量中心-结算管理-合同详情 -->
<template>
  <a-drawer
    :title="title"
    :width="800"
    @close="closeContractDetail"
    :visible="modalVisible"
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
            <a-form-item label="合同编号">
              <p>{{ model.contractSn }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同名称">
              <p>{{ model.contractName }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="签订日期">
              <p>{{ model.signDate }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="生效日期">
              <p>{{ model.startDate }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="结束日期">
              <p>{{ model.endDate }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="用途">
              <p>{{ model.contractUse }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="分类">
              <p>{{ model.typeName }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户名称">
              <p>{{ model.constomerName }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="备注信息">
              <p>{{ model.remarks }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="合同附件(请上传PDF格式文件)"> </a-form-item>
            <div v-if="fileList.length">
              <div
                class="flieBox"
                v-for="(item, index) in fileList"
                :key="index"
              >
                <p>{{ item.fileName }}</p>
              </div>
            </div>
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
      <a-button :style="{ marginRight: '8px' }" @click="closeContractDetail">
        关闭
      </a-button>
    </div>
  </a-drawer>
</template>

<script>
import detail from "@assets/js/measurementCenter/settlementManage/detail";
export default {
  ...detail,
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