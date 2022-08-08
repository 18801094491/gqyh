<!-- 计量中心-计量基础数据录入-合同管理-新增、编辑 -->
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
            <a-form-item label="合同编号">
              <a-input
                v-if="!isShow"
                placeholder="请输入合同编号"
                v-decorator="['contractSn', {}]"
              />
              <p v-if="isShow">{{ model.contractSn }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同名称">
              <a-input
                v-if="!isShow"
                placeholder="请输入合同名称"
                v-decorator="['contractName', {}]"
              />
              <p v-if="isShow">{{ model.contractName }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="签订日期">
              <a-date-picker
                style="width: 100%"
                v-if="!isShow"
                v-decorator="['signDate', {}]"
              />
              <p v-if="isShow">{{ model.signDate }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="生效日期">
              <a-date-picker
                style="width: 100%"
                v-if="!isShow"
                v-decorator="['startDate', {}]"
              />
              <p v-if="isShow">{{ model.startDate }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="结束日期">
              <a-date-picker
                style="width: 100%"
                v-if="!isShow"
                v-decorator="['endDate', {}]"
              />
              <p v-if="isShow">{{ model.endDate }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="用途">
              <a-input
                v-if="!isShow"
                placeholder="请输入用途"
                v-decorator="['contractUse', {}]"
              />
              <p v-if="isShow">{{ model.contractUse }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="分类">
              <a-select
                showSearch
                placeholder="请输入分类"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.type"
                @change="handleChange"
                v-if="!isShow"
              >
                <a-select-option
                  v-for="(item, index) in typeList"
                  :key="index"
                  :value="item.code"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
              <p v-if="isShow">{{ model.typeName }}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户名称">
              <a-select
                showSearch
                placeholder="请输入客户名称"
                optionFilterProp="children"
                style="width: 100%"
                v-model="model.customerId"
                @change="handleChange"
                v-if="!isShow"
              >
                <a-select-option
                  v-for="(item, index) in dataSource"
                  :key="index"
                  :value="item.customerId"
                  >{{ item.constomerName }}
                </a-select-option>
              </a-select>
              <p v-if="isShow">{{ model.constomerName }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="24">
            <a-form-item label="合同摘要">
              <a-textarea
                v-if="!isShow"
                width="100%"
                placeholder="请输入备注信息"
                v-decorator="['remarks', {}]"
                :autosize="{ minRows: 2, maxRows: 6 }"
              />
              <p v-if="isShow">{{ model.remarks }}</p>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="合同附件">
              <a-button class="uploadBtn" v-if="!isShow">
                <a-icon type="upload" />
                上传
                <input
                  type="file"
                  name="file"
                  id="uploadBtn"
                  @change="upfileClick"
                />
              </a-button>
            </a-form-item>
            <div v-if="fileList.length">
              <div
                class="flieBox"
                v-for="(item, index) in fileList"
                :key="index"
              >
                <p>{{ item.fileName }}</p>
                <a-icon v-if="!isShow" type="close" @click="removeFile" />
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
      <a-button :style="{ marginRight: '8px' }" @click="handleCancel">
        关闭
      </a-button>
      <a-button v-if="!isShow" @click="handleOk" type="primary">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/contractManage/modal";
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