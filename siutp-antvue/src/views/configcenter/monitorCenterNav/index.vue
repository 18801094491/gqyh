<template>
  <!-- 配置中心-监控中心导航 -->
  <div class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="模型类别">
                <a-select
                  v-model="queryParam.modelTypeCode"
                  placeholder="请选择模型类型"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in getGisTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8" :offset="12">
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
      </div>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
      >
        <span class="tdImg" slot="modelThumb" slot-scope="text, record, index">
          <img :src="record.modelThumb" alt="" srcset="" />
        </span>
        <span class="tdImg" slot="navStatus" slot-scope="text, record, index">
          <a-switch
            checkedChildren="启用"
            unCheckedChildren="停用"
            defaultChecked
            v-model="record.navStatus"
            @change="navStatusChange(record, $event)"
          />
        </span>
        <span class="tdImg" slot="dataShow" slot-scope="text, record, index">
          <a-switch
            checkedChildren="是"
            unCheckedChildren="否"
            defaultChecked
            v-model="record.dataShow"
            @change="dataShowChange(record, $event)"
          />
        </span>
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a @click="handleAdd('change', record)">修改</a>
        </span>
      </a-table>
    </a-card>
    <!-- 新增、修改右侧弹框 开始 -->
    <a-drawer
      :title="drawerTitle"
      :width="500"
      @close="onClose"
      :visible="visible"
      class="modelManagementAlertBox"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        padding: '0 0 108px 0',
      }"
    >
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-form-item label="模型类别">
            <a-select v-model="modelType" placeholder="请选择模型类型">
              <a-select-option
                :value="item.code"
                v-for="(item, index) in getGisTypeList"
                :key="index"
                >{{ item.title }}</a-select-option
              >
            </a-select>
          </a-form-item>
          <a-form-item label="导航名称">
            <a-input placeholder="请输入导航名称" v-model="navName" />
          </a-form-item>
          <div class="stateListBox">
            <a-form-item label="选择图片">
              <a-button class="uploadBtn">
                <a-icon type="upload" /> 上传
                <input
                  type="file"
                  value=""
                  class="uploadBtnB"
                  name="file"
                  @change="upfileClick($event)"
                />
              </a-button>
            </a-form-item>
            <div class="fileImgBox clearfix" v-if="GISScreeningMaintainImg">
              <div class="fileImgDivBox">
                <img :src="GISScreeningMaintainImg" alt="" />
                <a-icon
                  class="fileImgRemove"
                  type="close-circle"
                  @click="fileImgRemove"
                />
              </div>
            </div>
          </div>
          <a-form-item label="默认选中">
            <a-switch
              checkedChildren="是"
              unCheckedChildren="否"
              defaultChecked
              v-model="dataShow"
            />
          </a-form-item>
          <a-form-item label="启停用状态">
            <a-switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              defaultChecked
              v-model="navStatus"
            />
          </a-form-item>
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
        <a-button :style="{ marginRight: '8px' }" @click="onClose"
          >关闭</a-button
        >
        <a-button @click="onSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>
    <!-- 新增、修改右侧弹框 End -->
  </div>
</template>

<script>
import index from "@assets/js/configcenter/monitorCenterNav.js";
import "@assets/less/configcenter/monitorCenterNav.less";
export default {
  ...index,
};
</script>
