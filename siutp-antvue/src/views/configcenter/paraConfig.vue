<template>
  <!-- 配置中心-系统参数 -->
  <div class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="配置名称">
                <a-input
                  placeholder="请输入配置名称"
                  v-model="queryParam.configName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="启停用状态">
                <a-select
                  v-model="queryParam.configStatus"
                  placeholder="请选择启停用状态"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in workingStatusList"
                    :key="index"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8" :offset="6">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery" icon="search"
                  >查询</a-button
                >
                <a-button
                  class="ant-btn-border"
                  @click="searchReset"
                  icon="reload"
                  style="margin-left: 8px"
                  >重置</a-button
                >
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </div>
    <a-card :bordered="false">
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="border-top: 5px">
        <a-button @click="handleAdd('add')" type="primary" icon="plus"
          >新增</a-button
        >
        <a-button
          type="primary"
          icon="sync"
          @click="synchronousCache"
          v-has="'sys:param:sync'"
          >同步缓存</a-button
        >
      </div>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="tableDataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 1200 }"
        @change="handleTableChange"
      >
        <span slot="configStatus" slot-scope="text, record, index">
          <a-switch
            checkedChildren="启用"
            unCheckedChildren="停用"
            v-model="record.configStatus"
            defaultChecked
            @change="editWorkingStatusChange(record)"
          />
        </span>

        <span slot="maintainBtn" slot-scope="text, record, index">
          <a @click="handleAdd('change', record)">修改</a>
        </span>
      </a-table>
    </a-card>

    <!-- 新增、修改右侧滑框开始 -->
    <a-drawer
      :title="drawerTitle"
      :width="720"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <a-form layout="vertical" hideRequiredMark>
        <a-row :gutter="16">
          <!-- <a-col :span="12">
                      <a-form-item label="模型类型编码">
                        <a-select v-model="modelTypeCode" placeholder="请选择模型类型编码">
                          <a-select-option :value="item.code" v-for="(item,index) in modelTypeCodeList" :key="index">{{item.title}}</a-select-option>
                        </a-select>
                      </a-form-item>
                    </a-col> -->
          <a-col :span="12">
            <a-form-item label="配置名称">
              <a-input v-model="configName" placeholder="请输入配置名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="配置描述">
              <a-input
                v-model="configDescription"
                placeholder="请输入配置描述"
              />
            </a-form-item>
          </a-col>
          <!-- <a-col :span="12">
                      <a-form-item label="模型位置">
                        <a-select v-model="modelPosition" placeholder="请选择模型显示位置">
                          <a-select-option value="0" >不偏移</a-select-option>
                          <a-select-option value="1" >上</a-select-option>
                          <a-select-option value="2" >右</a-select-option>
                          <a-select-option value="3" >下</a-select-option>
                        </a-select>
                      </a-form-item> -->
          <!-- </a-col> -->
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="配置key">
              <a-input
                v-model="configKey"
                :disabled="isKey"
                placeholder="请输入配置key"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="配置值">
              <!--<a-input
                             v-model="configValue"
                             placeholder="请输入配置值"

                           />-->

              <a-select v-model="configValue" placeholder="请选择配置值">
                <a-select-option
                  v-for="(item, index) in configValueList"
                  :key="index"
                  :value="item.value"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- {
                           validator:(rule, val, callback) => {
                                 if (value) {
                                   if (/[\u4E00-\u9FA5]/g.test(value)) {
                                     callback(new Error('编码不能输入汉字!'));
                                   } else {
                                     callback();
                                   }
                                 }
                                 callback();
                             }
                         } -->

        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="配置项">
              <a-textarea v-model="accessValues" placeholder="请输入配置项" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
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
        <a-button :style="{ marginRight: '8px' }" @click="onClose">
          关闭
        </a-button>
        <a-button @click="addSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import index from "@/assets/js/configcenter/sysParam.js";

export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less"; /* .ant-table-tbody .ant-table-row td{
     overflow: hidden;
     white-space: nowrap;
     text-overflow: ellipsis;
   } */
</style>

<style lang="scss" scoped>
td.myTd {
  font-size: 20px !important;
  /* overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis; */
}
</style>
