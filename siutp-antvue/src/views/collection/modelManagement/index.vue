<template>
  <!-- Gis素材 -->
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
        rowkey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <span slot="legendShow" slot-scope="text, record, index">
          <a-switch
            checkedChildren="是"
            unCheckedChildren="否"
            v-model="record.legendShow"
            defaultChecked
            @change="iconShow(record)"
          />
        </span>
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a @click="handleAdd('change', record)">修改</a>
        </span>
        <span class="tdImg" slot="zc" slot-scope="text, record, index">
          <img
            v-for="(item, index) in record.list"
            :key="index"
            v-if="item.imageType == 0"
            :src="item.imgUrl"
            alt=""
            srcset=""
          />
        </span>
        <span class="tdImg" slot="yc" slot-scope="text, record, index">
          <img
            v-for="(item, index) in record.list"
            :key="index"
            v-if="item.imageType == 1"
            :src="item.imgUrl"
            alt=""
            srcset=""
          />
        </span>
        <span class="tdImg" slot="dk" slot-scope="text, record, index">
          <img
            v-for="(item, index) in record.list"
            :key="index"
            v-if="item.imageType == 2"
            :src="item.imgUrl"
            alt=""
            srcset=""
          />
        </span>
        <span class="tdImg" slot="gb" slot-scope="text, record, index">
          <img
            v-for="(item, index) in record.list"
            :key="index"
            v-if="item.imageType == 3"
            :src="item.imgUrl"
            alt=""
            srcset=""
          />
        </span>
      </a-table>
    </a-card>
    <!--新增、 修改右侧弹框 -->
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
      <a-button type="primary" style="margin-bottom: 20px" @click="addState"
        >新增状态</a-button
      >
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-form-item label="模型类别">
            <a-select v-model="modelTypeCode" placeholder="请选择模型类型">
              <a-select-option
                :value="item.code"
                v-for="(item, index) in getGisTypeList"
                :key="index"
                >{{ item.title }}</a-select-option
              >
            </a-select>
          </a-form-item>
          <div
            class="stateListBox"
            v-for="(item, index) in stateList"
            :key="index"
          >
            <h4>状态{{ index + 1 }}：</h4>
            <a-icon
              class="stateListRemove"
              type="close-circle"
              @click="stateListRemove(index)"
            />
            <a-row>
              <a-col>
                <a-form-item label="资源类型">
                  <a-select
                    v-model="item.imageType"
                    placeholder="请选择模型类型"
                  >
                    <a-select-option
                      :value="item.code"
                      v-for="(item, index) in gisImageTypeList"
                      :key="index"
                      >{{ item.title }}</a-select-option
                    >
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col>
                <a-form-item label="选择图片">
                  <a-button class="uploadBtn">
                    <a-icon type="upload" /> 上传
                    <input
                      type="file"
                      value=""
                      class="uploadBtnB"
                      name="file"
                      @change="upfileClick($event, index)"
                    />
                  </a-button>
                </a-form-item>
              </a-col>
            </a-row>
            <div class="fileImgBox clearfix" v-if="item.imgUrl">
              <div class="fileImgDivBox">
                <img :src="item.imgUrl" alt="" />
                <a-icon
                  class="fileImgRemove"
                  type="close-circle"
                  @click="fileImgRemove(index)"
                />
              </div>
            </div>
          </div>
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
  </div>
</template>

<script>
import index from "@/api/collection-t/modelManagement.js";
import "@/assets/less/collection/modelManagement.less";
export default {
  ...index,
};
</script>

<style scoped>
.fileImgBox {
  margin-left: 50%;
}
</style>