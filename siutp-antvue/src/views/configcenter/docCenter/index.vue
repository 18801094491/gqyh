<template>
  <!-- 配置中心-文档中心 -->
  <div class="documentCenter margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="8" :sm="8">
              <a-form-item label="文档名称">
                <a-input
                  placeholder="请输入文档名称"
                  v-model="queryParam.docName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8" :offset="10">
              <span
                style="float: right; overflow: hidden"
                class="table-page-search-submitButtons"
              >
                <a-button
                  type="primary"
                  @click="searchQuery('min')"
                  icon="search"
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
      <div class="table-operator">
        <a-button @click="handleAdd('add')" type="primary" icon="plus"
          >新增</a-button
        >
      </div>

      <!-- 列表区域-开始 -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          @change="handleTableChange"
        >
          <span slot="status" slot-scope="text, record">
            <a-switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              defaultChecked
              v-model="record.status"
              @change="changeStatus(record, $event)"
            />
          </span>
          <span slot="action" slot-scope="text, record">
            <a @click="handleAdd('change', record)">修改</a>
          </span>
        </a-table>
      </div>
      <!-- 列表区域-end -->
    </a-card>
    <!-- 新增、修改弹窗 - 开始 -->
    <a-modal
      :title="documentCenterTitle"
      :width="500"
      :visible="documentCentervisible"
      @ok="documentCenteronOk"
      @cancel="documentCenteronCancel"
      cancelText="关闭"
    >
      <a-form>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="文档名称"
        >
          <a-input placeholder="输入文档名称" v-model="docName"></a-input>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="文档类型"
        >
          <a-select v-model="docType" placeholder="请选择文档类型">
            <a-select-option
              v-for="(item, index) in inequipmentTypeList"
              :key="index"
              :value="item.code"
              >{{ item.title }}</a-select-option
            >
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="文档排序"
        >
          <a-input placeholder="输入文档排序" v-model="displayOrder"></a-input>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="文档说明"
        >
          <a-textarea
            v-model="docDescription"
            placeholder="请输入文档说明"
            :autoSize="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="文档上传"
        >
          <a-button class="uploadBtn">
            <a-icon type="upload" /> 上传
            <input
              type="file"
              name="file"
              id="uploadBtn"
              @change="upfileClick"
            />
          </a-button>
        </a-form-item>
        <div v-if="fileList.length" class="documentCenter">
          <div class="flieBox" v-for="(item, index) in fileList" :key="index">
            <p>{{ item.fileName }}</p>
          </div>
        </div>
      </a-form>
    </a-modal>
    <!-- 新增、修改弹窗 - end -->
  </div>
</template>
<script>
import index from "@api/configcenter-t/docCenter.js";
import "@assets/less/dataCenter/docCenter.less";
export default {
  ...index,
};
</script>