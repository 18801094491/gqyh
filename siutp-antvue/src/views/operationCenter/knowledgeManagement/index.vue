<!-- 资产中心-资产管理-资产知识管理 -->
<template>
  <div id="supplierManagementBox" class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="知识名称">
                <a-input
                  placeholder="请输入知识名称"
                  v-model="queryParam.knowledgeName"
                />
              </a-form-item>
            </a-col>
            <!--<a-col :md="6" :sm="12">
                            <a-form-item label="设备类别">
                                <a-select v-model="queryParam.ownershipType" placeholder="请选择设备类别">
                                    <a-select-option value="0">全部</a-select-option>
                                    <a-select-option :value="item.code" v-for="(item,index) in ownershipTypeList" :key="index">{{item.title}}</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>-->
            <a-col :md="6" :sm="12">
              <a-form-item label="知识类型">
                <a-select
                  v-model="queryParam.knowledgeType"
                  placeholder="请选择类型"
                >
                  <a-select-option value="0">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in knowledgeTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12" :offset="6">
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
        <a-button
          @click="handleAdd('add')"
          type="primary"
          icon="plus"
          v-has="'knowlege:add'"
          >新增</a-button
        >

        <!-- <a-button type="primary" icon="edit" @click="handleChange">修改</a-button> -->
        <!-- <a-button type="primary" @click="searchQuery" icon="download">导出</a-button> -->
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
        @change="handleTableChange"
      >
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a @click="details(record)">详情</a>
          <a-divider type="vertical" v-has="'knowlege:edit'" />
          <a @click="handleAdd('change', record)" v-has="'knowlege:edit'"
            >修改</a
          >
        </span>
      </a-table>
    </a-card>
    <a-drawer
      :title="drawerTitle"
      :width="1000"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24" v-if="isShow">
            <a-col :md="6" :sm="8">
              <a-form-item label="知识名称">
                <a-input placeholder="请输入知识名称" v-model="knowlegeName" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="知识类型">
                <a-select v-model="type" placeholder="请选择类型">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in knowledgeTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <!--<a-col :md="6" :sm="8">
                        <a-form-item label="设备类别">
                            <a-select v-model="category" placeholder="请选择设备类别">
                                <a-select-option :value="item.code" v-for="(item,index) in ownershipTypeList" :key="index">{{item.title}}</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>-->
            <a-col :md="6" :sm="8">
              <a-form-item label="所属资源">
                <a-select v-model="resource" placeholder="请选择所属资源">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in resourceList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="供应商">
                <a-select v-model="supplierId" placeholder="请选择所属资源">
                  <a-select-option
                    :value="item.id"
                    v-for="(item, index) in supplierClassificationList"
                    :key="index"
                    >{{ item.supplierName }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" v-if="isShow">
            <a-col :md="6" :sm="8">
              <a-form-item label="设备类型">
                <a-select
                  v-model="equipmentTypeName"
                  placeholder="请选择设备类型"
                  @change="changeEquipmentTypeName"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in modelTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="设备规格">
                <a-select
                  v-model="equipmentModel"
                  placeholder="请选择类型"
                  @change="changeEquipmentModel"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in equipmentModelList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="设备型号">
                <a-select v-model="equipmentSpecs" placeholder="请选择所属设备">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in equipmentSpecsList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <!--<a-col :md="6" :sm="8">
                        <a-form-item label="供应商">
                            <a-select v-model="supplierId" placeholder="请选择所属资源">
                                <a-select-option :value="item.id" v-for="(item,index) in supplierClassificationList" :key="index">{{item.supplierName}}</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>-->
          </a-row>
          <div
            v-for="(categoryItem, categoryIndex) in knowlegeItemVoList"
            :key="categoryIndex"
          >
            <div style="margin-bottom: 20px; text-align: right">
              <a-button
                @click="addCategory"
                type="primary"
                v-if="categoryIndex == 0 && isShow"
                >新增规程</a-button
              >
              <a-button
                @click="removeCategory(categoryIndex)"
                v-if="categoryIndex > 0 && isShow"
                type="primary"
                >删除规程</a-button
              >
            </div>

            <div class="categoryBox">
              <div class="clearfix" style="margin-bottom: 15px">
                <h3 style="margin-right: 20px; text-align: center">
                  规程{{ categoryIndex + 1 }}
                </h3>
              </div>

              <a-row style="padding-left: 30px">
                <a-col :md="24" :sm="8">
                  <a-form-item :label="'规程名称:'">
                    <a-input
                      v-if="isShow"
                      placeholder="请输入名称"
                      v-model="categoryItem.operationName"
                    />
                    <p v-if="!isShow">{{ categoryItem.operationName }}</p>
                  </a-form-item>
                </a-col>
              </a-row>
              <div class="operationRulesBox">
                <h5>操作规则:</h5>
                <a-row
                  v-for="(
                    operationRulesItem, operationRulesIndex
                  ) in categoryItem.knowlegeOperationList"
                  :key="operationRulesIndex"
                >
                  <a-col :md="18" :sm="8">
                    <a-form-item :label="operationRulesIndex + 1 + '、'">
                      <a-input
                        v-if="isShow"
                        placeholder="请输入操作规则"
                        v-model="operationRulesItem.operationItem"
                      />
                      <p v-if="!isShow">
                        {{ operationRulesItem.operationItem }}
                      </p>
                    </a-form-item>
                  </a-col>
                  <a-col :md="1" :sm="8" style="text-align: center">
                    <a-icon
                      class="add"
                      @click="
                        addOperationRules(categoryIndex, operationRulesIndex)
                      "
                      v-if="operationRulesIndex == 0 && isShow"
                      type="plus-circle"
                    />
                    <a-icon
                      class="remove"
                      @click="
                        removeOperationRules(categoryIndex, operationRulesIndex)
                      "
                      v-if="operationRulesIndex > 0 && isShow"
                      type="minus-circle"
                    />
                  </a-col>
                </a-row>
              </div>
              <div class="safetyPrecautionsBox">
                <h5>安全事项:</h5>
                <a-row
                  v-for="(
                    safetyPrecautionsItem, safetyPrecautionsIndex
                  ) in categoryItem.knowlegeCautionList"
                  :key="safetyPrecautionsIndex"
                >
                  <a-col :md="18" :sm="8">
                    <a-form-item :label="safetyPrecautionsIndex + 1 + '、'">
                      <a-input
                        v-if="isShow"
                        placeholder="请输入安全注意事项"
                        v-model="safetyPrecautionsItem.cautionItem"
                      />
                      <p v-if="!isShow">
                        {{ safetyPrecautionsItem.cautionItem }}
                      </p>
                    </a-form-item>
                  </a-col>
                  <a-col :md="1" :sm="8" style="text-align: center">
                    <a-icon
                      class="add"
                      type="plus-circle"
                      @click="
                        addSafetyPrecautions(
                          categoryIndex,
                          safetyPrecautionsIndex
                        )
                      "
                      v-if="safetyPrecautionsIndex == 0 && isShow"
                    />
                    <a-icon
                      class="remove"
                      type="minus-circle"
                      @click="
                        removeSafetyPrecautions(
                          categoryIndex,
                          safetyPrecautionsIndex
                        )
                      "
                      v-if="safetyPrecautionsIndex > 0 && isShow"
                    />
                  </a-col>
                </a-row>
              </div>
              <div class="scBox">
                <a-button v-if="isShow" class="uploadBtn">
                  <a-icon type="upload" /> 上传
                  <input
                    type="file"
                    value=""
                    class="uploadBtnB"
                    name="file"
                    multiple="multiple"
                    @change="upfileClick($event, categoryIndex)"
                  />
                </a-button>
                <div v-if="categoryItem.knowlegeAttachList.length">
                  <div
                    class="flieBox"
                    v-for="(item, index) in categoryItem.knowlegeAttachList"
                    :key="index"
                  >
                    <p>{{ item.fileName }}</p>
                    <a-icon
                      v-if="isShow"
                      type="close"
                      @click="removeFile($event, categoryIndex, index)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-form>
      </div>
      <div
        v-if="isShow"
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
        <a-button @click="onSubmit" type="primary">提交</a-button>
      </div>
    </a-drawer>

    <a-drawer
      title="修改"
      :width="1000"
      @close="changeonClose"
      :visible="changevisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div class="table-page-search-wrapper" id="change">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24" v-if="isShow">
            <a-col :md="8" :sm="8">
              <a-form-item label="知识名称">
                <a-input placeholder="请输入知识名称" v-model="knowlegeName" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="12">
              <a-form-item label="知识类型">
                <a-select v-model="type" placeholder="请选择类型">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in knowledgeTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <!--<a-col :md="6" :sm="8">
                        <a-form-item label="设备类别">
                            <a-select v-model="category" placeholder="请选择设备类别">
                                <a-select-option :value="item.code" v-for="(item,index) in ownershipTypeList" :key="index">{{item.title}}</a-select-option>
                            </a-select>

                        </a-form-item>
                    </a-col>-->
            <a-col :md="6" :sm="8">
              <a-form-item label="所属资源">
                <a-select v-model="resource" placeholder="请选择所属资源">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in resourceList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" v-if="isShow">
            <a-col :md="8" :sm="8">
              <a-form-item label="设备类型">
                <a-select
                  v-model="equipmentTypeName"
                  placeholder="请选择设备类型"
                  @change="changeEquipmentTypeName"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in modelTypeList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="12">
              <a-form-item label="设备规格">
                <a-select
                  v-model="equipmentModel"
                  placeholder="请选择类型"
                  @change="changeEquipmentModel"
                >
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in equipmentModelList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="设备型号">
                <a-select v-model="equipmentSpecs" placeholder="请选择所属设备">
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in equipmentSpecsList"
                    :key="index"
                    >{{ item.title }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24" v-if="isShow">
            <!--<a-col :md="8" :sm="8">
                        <a-form-item label="所属资源">
                            <a-select v-model="resource" placeholder="请选择所属资源">
                                <a-select-option :value="item.code" v-for="(item,index) in resourceList" :key="index">{{item.title}}</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>  -->
            <a-col :md="8" :sm="8">
              <a-form-item label="供应商">
                <a-select v-model="supplierId" placeholder="请选择所属资源">
                  <a-select-option
                    :value="item.id"
                    v-for="(item, index) in supplierClassificationList"
                    :key="index"
                    >{{ item.supplierName }}</a-select-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="8">
              <a-button @click="onSubmit" type="primary">保存</a-button>
            </a-col>
          </a-row>
          <div
            class="categoryBox"
            v-for="(categoryItem, categoryIndex) in knowlegeItemVoList"
            :key="categoryIndex"
          >
            <div class="clearfix" style="margin-bottom: 15px">
              <h3 class="fl" style="margin-right: 20px">规程</h3>
            </div>

            <a-row>
              <a-col :md="16" :sm="8">
                <a-form-item label="名称">
                  <a-input
                    v-if="isShow"
                    placeholder="请输入名称"
                    v-model="categoryItem.operationName"
                  />
                  <p v-if="!isShow">{{ categoryItem.operationName }}</p>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="8" :offset="2">
                <a-button
                  @click="
                    bcKnowlegeItemVoList(
                      categoryItem.id,
                      categoryItem.operationName
                    )
                  "
                  type="primary"
                  >保存</a-button
                >
              </a-col>
            </a-row>
            <a-tabs defaultActiveKey="1">
              <a-tab-pane tab="操作规程" key="1">
                <a-button
                  @click="
                    addKnowlegeOperationList(
                      categoryItem.id,
                      categoryIndex,
                      'add'
                    )
                  "
                  icon="plus"
                  type="primary"
                  >增加</a-button
                >
                <!-- 表格显示区域 -->
                <a-table
                  class="operationRulesBox operationRulesBox2"
                  ref="table"
                  bordered
                  size="middle"
                  rowKey="id"
                  :columns="knowlegeOperationListcolumns"
                  :dataSource="categoryItem.knowlegeOperationList"
                >
                  <span slot="caozuo" slot-scope="text, record, index">
                    <a
                      @click="
                        addKnowlegeOperationList(
                          record.id,
                          categoryIndex,
                          'change',
                          record,
                          index
                        )
                      "
                      >编辑</a
                    >
                    <a-divider type="vertical" />
                    <a
                      @click="
                        removeKnowlegeOperationList(
                          record,
                          categoryIndex,
                          index
                        )
                      "
                      >删除</a
                    >
                  </span>
                </a-table>
              </a-tab-pane>
              <a-tab-pane tab="安全事项" key="2" forceRender>
                <a-button
                  @click="
                    addknowlegeCautionList(
                      categoryItem.id,
                      categoryIndex,
                      'add'
                    )
                  "
                  icon="plus"
                  type="primary"
                  >增加</a-button
                >
                <!-- 表格显示区域 -->
                <a-table
                  class="safetyPrecautionsBox safetyPrecautionsBox2"
                  ref="table"
                  bordered
                  size="middle"
                  rowKey="id"
                  :columns="knowlegeCautionListcolumns"
                  :dataSource="categoryItem.knowlegeCautionList"
                >
                  <span
                    v-if="categoryItem.knowlegeCautionList.length"
                    slot="caozuo"
                    slot-scope="text, record, index"
                  >
                    <a
                      @click="
                        addknowlegeCautionList(
                          record.id,
                          categoryIndex,
                          'change',
                          record,
                          index
                        )
                      "
                      >编辑</a
                    >
                    <a-divider type="vertical" />
                    <a
                      @click="
                        removeknowlegeCautionList(record, index, categoryIndex)
                      "
                      >删除</a
                    >
                  </span>
                </a-table>
              </a-tab-pane>
              <a-tab-pane tab="手册" key="3" forceRender>
                <a-button
                  class="uploadBtn"
                  type="primary"
                  icon="plus"
                  style="width: 82px"
                >
                  增加
                  <input
                    type="file"
                    value=""
                    class="uploadBtnB"
                    name="file"
                    multiple="multiple"
                    @change="
                      upfileClick2($event, categoryIndex, categoryItem.id)
                    "
                  />
                </a-button>
                <!-- 表格显示区域 -->
                <a-table
                  class="safetyPrecautionsBox safetyPrecautionsBox2"
                  ref="table"
                  bordered
                  size="middle"
                  rowKey="id"
                  :columns="knowlegeAttachListcolumns"
                  :dataSource="categoryItem.knowlegeAttachList"
                >
                  <span
                    v-if="categoryItem.knowlegeAttachList.length"
                    slot="caozuo"
                    slot-scope="text, record, index"
                  >
                    <a
                      @click="
                        knowlegeAttachDelete(record, categoryIndex, index)
                      "
                      >删除</a
                    >
                  </span>
                </a-table>

                <!-- <a-row>
                                <a-col :md="18" :sm="8">
                                    <a-form-item label="手册">
                                        <a-button v-if="isShow" class="uploadBtn">
                                            <a-icon type="upload" /> 上传
                                            <input type="file" value="" class="uploadBtnB" name="file" multiple="multiple" @change="upfileClick($event,categoryIndex)">
                                        </a-button>
                                    </a-form-item>
                                    <div v-if="categoryItem.knowlegeAttachList.length">
                                        <div class="flieBox" v-for="(item,index) in categoryItem.knowlegeAttachList" :key="index">
                                            <p>{{item.fileName}}</p>
                                            <a-icon v-if="isShow" type="close" @click="removeFile($event,categoryIndex,index)"/>
                                        </div>
                                    </div>
                                </a-col> 
                                
                            </a-row> -->
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-form>
      </div>
      <!-- 操作规程列表增加数据 -->
      <a-modal
        :title="knowlegeOperationTitle"
        :width="600"
        :visible="knowlegeOperationvisible"
        @ok="knowlegeOperationhandleOk"
        @cancel="knowlegeOperationhandleCancel"
        cancelText="关闭"
      >
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="操作规程"
        >
          <a-input placeholder="请输入操作规程" v-model="operationItem" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序索引"
        >
          <a-input placeholder="请输入排序索引" v-model="displayOrder" />
        </a-form-item>
      </a-modal>
      <!-- 安全事项列表增加数据 -->
      <a-modal
        :title="knowlegeCautionTitle"
        :width="600"
        :visible="knowlegeCautionvisible"
        @ok="knowlegeCautionhandleOk"
        @cancel="knowlegeCautionhandleCancel"
        cancelText="关闭"
      >
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="安全事项"
        >
          <a-input
            placeholder="请输入安全事项"
            v-model="knowlegeCautionoperationItem"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="排序索引"
        >
          <a-input
            placeholder="请输入排序索引"
            v-model="knowlegeCautiondisplayOrder"
          />
        </a-form-item>
      </a-modal>
    </a-drawer>
    <a-drawer
      :title="detailsTitle + '详情'"
      :width="800"
      @close="detailsClose"
      :visible="detailsvisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <div
        class="rules"
        v-for="(categoryItem, categoryIndex) in knowlegeItemVoList"
        :key="categoryIndex"
      >
        <h3>规程{{ categoryIndex + 1 }}</h3>
        <div class="detailsBox clearfix">
          <div class="detailsleftBox">
            <h4>{{ detailsTitle }}维护章程</h4>
            <div
              v-for="(
                operationRulesItem, operationRulesIndex
              ) in categoryItem.knowlegeOperationList"
              :key="operationRulesIndex"
            >
              <p>
                {{ operationRulesIndex + 1 }}、{{
                  operationRulesItem.operationItem
                }}
              </p>
            </div>
          </div>
          <div class="detailsrightBox">
            <h4>{{ detailsTitle }}维护安全事项</h4>
            <div
              v-for="(
                safetyPrecautionsItem, safetyPrecautionsIndex
              ) in categoryItem.knowlegeCautionList"
              :key="safetyPrecautionsIndex"
            >
              <p>
                {{ safetyPrecautionsIndex + 1 }}、{{
                  safetyPrecautionsItem.cautionItem
                }}
              </p>
            </div>
          </div>
        </div>
        <div class="detailsManualBox">
          <h4>手册：</h4>
          <!-- <p>安全手册（点击可查看），无则显示暂无</p> -->
          <div
            class="detailsManualNo"
            v-if="!categoryItem.knowlegeAttachList.length"
          >
            <p>暂无数据</p>
          </div>
          <div
            class="detailsManual"
            v-if="categoryItem.knowlegeAttachList.length"
            v-for="(fjItem, fjIndex) in categoryItem.knowlegeAttachList"
            :key="fjIndex"
          >
            <a :href="fjItem.attachFile" target="_blank">{{
              fjItem.fileName
            }}</a>
          </div>
        </div>
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
        <a-button :style="{ marginRight: '8px' }" @click="detailsClose">
          关闭
        </a-button>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import index from "@/assets/js/operationCenter/knowledgeManagement/index.js";
import "@/assets/less/operationCenter/knowledgeManagement/style.less";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>