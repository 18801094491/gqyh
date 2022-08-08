<template>
  <!-- Gis模型 -->
  <div id="equipmentAccountMaintenanceBox" class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="模型名称">
                <a-input
                  placeholder="请输入模型名称"
                  v-model="queryParam.modelName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="模型类型">
                <a-select
                  v-model="queryParam.modelType"
                  placeholder="请选择模型类型"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in modelTypeList2"
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
        <a-button type="primary" icon="edit" @click="changeProgramme"
          >模型主题</a-button
        >
        <a-button type="primary" icon="sync" @click="synchronousCache"
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
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="{ x: 1900 }"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        @change="handleTableChange"
      >
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a @click="bindVar(record)">绑定变量</a>
          <a-divider type="vertical" />
          <a :data-data="record" @click="handleAdd('change', record)"
            >修改详情</a
          >
          <a-divider type="vertical" />
          <a @click="variableDetails(record)">变量详情</a>
          <a-divider type="vertical" />
          <a
            :data-data="record"
            v-if="record.bindStatus == 1"
            @click="bindGisShow(record)"
            >资产绑定</a
          >
          <a
            :data-data="record"
            v-if="record.bindStatus == 0"
            @click="unbindGis(record)"
            >资产解绑</a
          >
          <a-divider type="vertical" />
          <a @click="deleteGisModel(record)">删除</a>
        </span>
        <span
          class="modelImg"
          v-if="record.modelOnImg"
          slot="modelOnImg"
          slot-scope="text, record, index"
        >
          <img :src="record.modelOnImg" alt="" />
        </span>
        <span
          class="modelImg"
          v-if="record.modelOffImg"
          slot="modelOffImg"
          slot-scope="text, record, index"
        >
          <img :src="record.modelOffImg" alt="" />
        </span>
        <span
          class="modelImg"
          v-if="record.modelImg"
          slot="modelImg"
          slot-scope="text, record, index"
        >
          <img :src="record.modelImg" alt="" />
        </span>
        <span
          class="modelImg"
          v-if="record.modelWaringImg"
          slot="modelWaringImg"
          slot-scope="text, record, index"
        >
          <img :src="record.modelWaringImg" alt="" />
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
            <a-form-item label="模型名称">
              <a-input
                v-model="modelName"
                @keyup="modelName = modelName.replace(/[\W]/g, '')"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="模型位置">
              <a-select
                v-model="modelPosition"
                placeholder="请选择模型显示位置"
              >
                <a-select-option value="0">不偏移</a-select-option>
                <a-select-option value="1">上</a-select-option>
                <a-select-option value="2">右</a-select-option>
                <a-select-option value="3">下</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="经度">
              <a-input v-model="longitude" placeholder="请输入经度" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="纬度">
              <a-input v-model="latitude" placeholder="请输入纬度" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="模型类型">
              <a-select v-model="modelType" placeholder="请选择模型类型">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in modelTypeList"
                  :key="index"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="层级">
              <a-input
                v-model="modelLevel"
                placeholder="请输入层级"
                @keyup="modelLevel = modelLevel.replace(/[\W]/g, '')"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="偏移量">
              <a-input
                v-model="modelOffset"
                placeholder="请输入模型偏移量"
                @keyup="modelOffset = modelOffset.replace(/[\W]/g, '')"
              />
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
    <!-- 新增、修改右侧滑框结束 -->
    <!-- 绑定变量滑框开始 -->
    <a-drawer
      :title="bindVarTitle"
      :width="850"
      @close="bindCancel"
      :visible="bindVisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <!-- 搜索区域 -->
      <a-form layout="inline" style="margin-bottom: 25px">
        <a-row :gutter="24">
          <a-col :md="9" :sm="8">
            <a-form-item label="变量名称">
              <a-input placeholder="请输入变量名称" v-model="varName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="9" :sm="8">
            <a-form-item label="变量示意">
              <a-input
                placeholder="请输入变量示意"
                v-model="variableTitle"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8" :offset="2" style="margin-top: 4px">
            <a-button type="primary" @click="searchQuery2" icon="search"
              >查询</a-button
            >
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns2"
        :dataSource="dataSource2"
        :pagination="ipagination2"
        :loading="loading2"
        @change="handleTableChange2"
      >
        <span
          class="bindBtnBox"
          slot="maintainBtn2"
          slot-scope="text, record, index"
        >
          <a
            v-if="record.bindStatus == 0"
            :data-data="record"
            @click="bindShow(record)"
            >绑定</a
          >
          <a v-if="record.bindStatus == 1" :data-data="record" class="active"
            >已绑定</a
          >
        </span>
      </a-table>
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
        <a-button :style="{ marginRight: '8px' }" @click="bindCancel">
          关闭
        </a-button>
      </div>
    </a-drawer>
    <!-- 绑定变量滑框结束 -->
    <!-- 变量详情滑框开始 -->
    <a-modal
      title="变量详情"
      :visible="variableDetailsVisible"
      width="800px"
      @ok="variableDetailsOnOk"
      @cancel="variableDetailsCancel"
    >
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns3"
        :dataSource="dataSource3"
        :pagination="ipagination3"
        :loading="loading3"
        @change="handleTableChange3"
      >
        <span
          class="bindBtnBox"
          slot="maintainBtn2"
          slot-scope="text, record, index"
        >
          <a :data-data="record" @click="setupSerialNumber(record)"
            >设置排序序号</a
          >
          <a-divider type="vertical" />
          <a :data-data="record" @click="unbindShow(record)">解绑</a>
        </span>
      </a-table>
    </a-modal>
    <!-- 变量详情滑框结束 -->
    <!-- 绑定设备滑框开始 -->
    <a-modal
      title="绑定设备"
      :visible="bindGisVisible"
      width="800px"
      @ok="bindOnOk"
      @cancel="bindGisCancel"
    >
      <!-- 搜索区域 -->
      <a-form layout="inline" style="margin-bottom: 25px">
        <a-row :gutter="24">
          <a-col :md="9" :sm="8">
            <a-form-item label="资产名称">
              <a-input
                placeholder="请输入资产名称"
                v-model="gismodelName"
              ></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8" style="margin-top: 4px">
            <a-button type="primary" @click="gissearchQuery2" icon="search"
              >查询</a-button
            >
          </a-col>
        </a-row>
      </a-form>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="giscolumns2"
        :dataSource="gisdataSource2"
        :pagination="gisipagination2"
        :loading="gisloading2"
        @change="gishandleTableChange2"
      >
        <span slot="maintainBtn2" slot-scope="text, record, index">
          <a :data-data="record" @click="bindShow2(record)">绑定</a>
        </span>
      </a-table>
    </a-modal>
    <!-- 绑定设备滑框结束 -->
    <!-- 更改方案弹框开始 -->
    <a-modal
      title="模型主题"
      :visible="changeProgrammeVisible"
      width="400px"
      @ok="changeProgrammeOnOk"
      @cancel="changeProgrammeCancel"
    >
      <!-- 搜索区域 -->
      <a-form>
        <a-form-item
          v-if="allOk"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模型类型"
        >
          <a-select
            placeholder="请选择模型类型"
            v-model="modelType"
            @change="modelTypeChange"
          >
            <a-select-option
              v-for="(item, index) in getGisTypeList"
              :key="index"
              :value="item.code"
            >
              {{ item.title }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模型编码"
        >
          <a-select placeholder="请选择模型编码" v-model="resId">
            <a-select-option
              v-for="(item, index) in gisResSnList"
              :key="index"
              :value="item.id"
            >
              {{ item.resSn }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模型宽度"
        >
          <a-input placeholder="请输入模型宽度" v-model="giswidth" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模型高度"
        >
          <a-input placeholder="请输入模型高度" v-model="gisheight" />
        </a-form-item>
      </a-form>
    </a-modal>
    <!-- 更改方案弹框结束 -->
    <!-- 设置序号 开始-->
    <a-modal
      title="设置排序序号"
      :visible="setUpSerialNumberVisible"
      width="370px"
      @ok="setUpSerialNumberOnOk"
      @cancel="setUpSerialNumberCancel"
    >
      <!-- 搜索区域 -->
      <a-form>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序序号"
        >
          <a-input
            placeholder="请输入排序序号"
            v-model="setUpSerialNum"
            @keyup="setUpSerialNum = setUpSerialNum.replace(/[^\d]/g, '')"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <!-- 设置序号 结束-->
  </div>
</template>

<script>
import gisModel from "@assets/js/collection/gisModel.js";
import "@assets/less/operationCenter/gisModel.less";

export default {
  ...gisModel,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
