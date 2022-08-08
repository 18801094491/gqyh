<!-- 资产中心-资产管理-资产详细信息 -->
<template>
 <div id="equipmentAccountMaintenanceBox" class="margin12">
    <div class="screenCommonBox">
      <!-- 搜索区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="所属标段">
                <a-select
                  v-model="queryParam.optSection"
                  placeholder="请选择所属标段"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.code"
                    v-for="(item, index) in bidSegmentList"
                    :key="index"
                    >{{ item.title }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="设备类型">
                <a-select
                  v-model="queryParam.equipmentType"
                  placeholder="请选择设备类型"
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
            <a-col :md="6" :sm="12">
              <a-form-item label="放置位置">
                <a-input
                  placeholder="请输入放置位置"
                  v-model="queryParam.optLocation"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="设备状态">
                <a-select
                  v-model="queryParam.equipmentRevstop"
                  placeholder="请选择设备状态"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option value="1">启用</a-select-option>
                  <a-select-option value="0">停用</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="设备编号">
                <a-input
                  placeholder="请输入账号查询"
                  v-model="queryParam.equipmentNumber"
                  @keyup="
                    queryParam.equipmentNumber = queryParam.equipmentNumber.replace(
                      /[\W]/g,
                      ''
                    )
                  "
                ></a-input>
              </a-form-item>
            </a-col>
            <!--<template v-if="toggleSearchStatus">-->
            <a-col :md="6" :sm="8">
              <a-form-item label="供应商">
                <a-select
                  v-model="queryParam.supplier"
                  placeholder="请选择供应商"
                >
                  <a-select-option value="">全部</a-select-option>
                  <a-select-option
                    :value="item.id"
                    v-for="(item, index) in supplierClassificationList2"
                    :key="index"
                  >
                    {{ item.supplierName }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <!--</template>-->
            <a-col :md="6" :sm="8" :offset="6">
              <span
                style="float: right; overflow: hidden;"
                class="table-page-search-submitButtons"
              >
                <a-button type="primary" @click="searchQuery" icon="search"
                  >查询</a-button
                >
                <a-button
                  type="primary"
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
          type="primary"
          @click="handleExportXls('设备台账')"
          icon="download"
          v-has="'opt:export'"
          >导出
        </a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import" v-has="'opt:import'"
            >导入</a-button
          >
        </a-upload>
        <a-button
          type="primary"
          @click="handleExportXls2('设备台账导入模板')"
          icon="download"
          v-has="'opt:downloadModel'"
          >下载导入模板
        </a-button>
        <a-button
          type="primary"
          @click="changeImg"
          icon="edit"
          v-has="'opt:uploadImg'"
          >设备图片</a-button
        >
        <a-button type="primary" @click="setDate" icon="plus"
          >养护到期设置天数</a-button
        >
      </div>
      <a-tabs :activeKey="equipmentCategory2" @change="callback">
        <a-tab-pane
          :tab="item.title"
          :key="item.code"
          v-for="(item, index) in equipmentCategory2List"
        ></a-tab-pane>
      </a-tabs>

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
        :scroll="{ x: 1800 }"
        @change="handleTableChange"
      >
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a
            :data-data="record"
            @click="handleAdd('change', record)"
            v-has="'opt:edit'"
            >修改</a
          >
          <a-divider type="vertical" v-has="'opt:edit'" />
          <a href="javasript:;" @click="labelDetail(record)">设备树节点</a>
          <a-divider type="vertical" v-if="record.labelId" />
          <a-popconfirm
            title="确定解除绑定吗?"
            v-if="record.labelId"
            @confirm="() => labelUnbind(record)"
          >
            <a>树节点解绑</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a :data-data="record" @click="updatePlan(record)">修改养护日期</a>
          <a-divider type="vertical" />
          <a @click="deviceDetailsClick(record)">详情</a>
          <a-divider type="vertical" v-if="record.dispatchStatus != '1'" />
          <a @click="assignWoker(record)" v-if="record.dispatchStatus != '1'"
            >分派工单</a
          >
        </span>
        <a-switch
          slot="equipmentRevstopText"
          v-has="'opt:add'"
          checkedChildren="启用"
          slot-scope="text, record, index"
          unCheckedChildren="停用"
          v-model="record.equipmentRevstop == 1 ? true : false"
          defaultChecked
          @change="stateChange($event, record.id)"
        />
      </a-table>
    </a-card>
    <a-drawer
      :title="drawerTitle"
      :width="500"
      @close="onClose"
      :visible="visible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px'
      }"
    >
      <a-form :form="form" layout="vertical" hideRequiredMark>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="供应商">
              {{ equipmentSupplierName }}
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备类型">
              {{ equipmentTypeName }}
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备型号">
              {{ equipmentModelName }}
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备规格">
              {{ equipmentSpecsName }}
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备编号">
              <a-input
                v-model="equipmentNumber"
                placeholder="请输入设备编号"
                @keyup="equipmentNumber = equipmentNumber.replace(/[\W]/g, '')"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设备名称">
              <a-input v-model="equipmentName" placeholder="请输入设备名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="资产类别">
              <a-select
                v-model="equipmentCategory"
                placeholder="请选择资产类别"
              >
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in equipmentCategoryList"
                  :key="index"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="放置位置">
              <a-input v-model="position" placeholder="请输入放置位置" />
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="所属标段">
              <a-select v-model="bidSegment" placeholder="请选择所属标段">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in bidSegmentList"
                  :key="index"
                >
                  {{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="资产状态">
              <a-select v-model="state" placeholder="请选择资产状态">
                <a-select-option
                  v-for="(item, index) in assetStatusList"
                  :key="index"
                  :value="item.code"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          
          <a-col :span="12">
            <a-form-item label="购置时间">
              <a-date-picker
                style="width: 100%"
                v-decorator="['equipmentPurchase', {}]"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="投入运营时间">
              <a-date-picker
                style="width: 100%"
                v-decorator="['equipmentOperating', {}]"
              />
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="设备状态">
              <a-select v-model="equipmentRevstop" placeholder="请选择设备状态">
                <a-select-option
                  v-for="(item, index) in workingStatusList"
                  :key="index"
                  :value="item.code"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="责任人">
              <a-select
                v-model="personResponsible"
                placeholder="请选择责任人"
                mode="multiple"
                optionFilterProp="children"
              >
                <a-select-option
                  v-for="(item, index) in managerList"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <!--<div class="stateListBoxImg" style="display: flex;" v-for="(item,index) in stateList" :key="index">-->
          <a-col :span="12">
            <a-form-item label="选择图片">
              <a-button class="uploadBtn" style="float: left;">
                <a-icon type="upload" />
                上传
                <input
                  type="file"
                  value=""
                  class="uploadBtnB"
                  name="file"
                  @change="upfileClick($event)"
                />
              </a-button>
            </a-form-item>
          </a-col>
          <a-col :span="12">
          	<a-form-item label="监控标识" v-if="isShowkMonitorCode">
            	<a-input
              	v-model="hkMonitorCode"
                placeholder="请输入监控标识"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
			    	<a-form-item label="监控平台" v-if="isShowkMonitorCode">
			      	<a-select v-model="hkMonitorKey">
			        	<a-select-option 
			        		v-for="(item,index) in hkMonitorKeyData" 
			        		:key="index" 
			        		:value="item.key"
			        		>{{item.name}}</a-select-option
			        	>
			        </a-select>
			      </a-form-item>
			    </a-col>
          <div class="fileImgBox clearfix" v-if="this.stateList[0].imgUrl">
            <div class="fileImgDivBox">
              <img :src="this.stateList[0].imgUrl" alt="" />
              <a-icon
                class="fileImgRemove"
                type="close-circle"
                @click="fileImgRemove()"
              />
            </div>
          </div>
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
    <a-modal
      :title="titlelabel"
      :visible="labelVisible"
      @ok="labelSubmit"
      @cancel="handleCancellabel"
    >
      <a-form :form="labelForm">
        <a-form-item
          label="树节点"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <j-tree-label-select
            :disabled="disTree"
            ref="treeSelect"
            placeholder="请选择树节点"
            v-decorator="['labelId', validatorRules.labelId]"
            dict="opt_label,label_name,id"
            pidField="parent_id"
            pidValue="0"
            condition='{"del_flag":0}'
            @change="getChild"
          >
          </j-tree-label-select>
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
      title="设备信息详情"
      :visible="detailsVisible"
      width="500px"
      class="domeBox domeBox2"
      @ok="informationOk"
      @cancel="informationCancel"
    >
      <div class="informationTopBox">
        <!-- <span>关闭</span> -->
        <!-- <a-icon type="close" class="close" @click="closeDeviceDetails"/> -->
      </div>
      <div class="informationBodyBox">
        <div class="informationTopBodyBox">
          <div class="informationTopBodyLeftBox">
            <div class="informationImgBox">
              <img :src="assetImg" alt="" width="100%" height="100%" />
            </div>
          </div>
          <div
            class="informationTopBodyRightBox"
            style="width: calc(100% - 150px)"
          >
            <!-- <div class="informationTopBodyRightDivBox clearfix" v-if="attributeList.length>0" v-for="(item,index) in attributeList" :key="index">
                          <label>{{item.attributeCn}}:</label>
                          <span>{{item.attributeVal}}</span>
                        </div> -->
            <div class="informationTopBodyRightDivBox clearfix">
              <label>设备编号:</label>
              <span>{{ assetCoding }}</span>
            </div>
            <div class="informationTopBodyRightDivBox clearfix">
              <label>设备名称:</label>
              <span>{{ assetName }}</span>
            </div>
            <div class="informationTopBodyRightDivBox clearfix">
              <label>生产厂家:</label>
              <span>{{ manufacturer }}</span>
            </div>
            <div class="informationTopBodyRightDivBox clearfix">
              <label>设备型号:</label>
              <span>{{ informationEquipmentType }}</span>
            </div>
            <div class="informationTopBodyRightDivBox clearfix">
              <label>放置位置:</label>
              <span>{{ assetLocation }}</span>
            </div>
          </div>
        </div>
        <div class="informationListBox">
          <h4>采集数据</h4>
          <a-form>
            <a-row v-for="(item, index) in attributeList" :key="index">
              <a-col :span="24">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  :label="item.attributeCn"
                >
                  <p>{{ item.attributeVal }}</p>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <div class="informationListBox">
          <h4>维修信息</h4>
          <a-row>
            <a-col :span="24">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="维保次数"
              >
                <p>{{ upkeepCount }}</p>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="最近保养时间"
              >
                <p>{{ upkeepTimeBY }}</p>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="最近维修时间"
              >
                <p>{{ upkeepTimeWX }}</p>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <div class="informationListBox zhishi" style="background-color: #fff">
          <h4>知识信息</h4>
          <a-table
            ref="table"
            bordered
            size="middle"
            rowKey="id"
            :columns="columns3"
            :dataSource="dataSource3"
            :pagination="ipagination3"
            :loading="loading3"
          >
            <span slot="maintainBtn" slot-scope="text, record, index">
              <a @click="zsdetails(record)">详情</a>
            </span>
          </a-table>
        </div>
      </div>
    </a-modal>

    <a-modal
      title="设备类型主题"
      :visible="changeImgVisible"
      width="400px"
      @ok="changeImgOnOk"
      @cancel="changeImgCancel"
    >
      <!-- 搜索区域 -->
      <a-form>
        <a-row>
          <a-col>
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="设备类型"
            >
              <a-select
                placeholder="请选择设备类型"
                v-model="equipType"
                @change="equipTypeChange"
              >
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in equipmentTypeList"
                  :key="index"
                  >{{ item.title }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <div
          class="stateListBoxImg"
          style="display: flex;"
          v-for="(item, index) in stateList"
          :key="index"
        >
          <!--<a-form>-->
          <a-row>
            <a-col>
              <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="选择图片"
              >
                <a-button class="uploadBtn" style="margin-left: 20px">
                  <a-icon type="upload" />
                  上传
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
          <!--</a-form>-->
          <div class="fileImgBox clearfix" v-if="item.imgUrl">
            <div class="fileImgDivBox">
              <img :src="item.imgUrl" alt="" />
              <a-icon
                class="fileImgRemove"
                type="close-circle"
                @click="fileImgRemove()"
              />
            </div>
          </div>
        </div>
      </a-form>
    </a-modal>

    <a-modal
      title="知识详情"
      :visible="zsVisible"
      width="630px"
      @cancel="zsCancel"
    >
      <div
        class="rules"
        v-for="(categoryItem, categoryIndex) in knowlegeItemVoList"
        :key="categoryIndex"
      >
        <h3>规程{{ categoryIndex + 1 }}</h3>
        <div class="detailsBox clearfix">
          <div class="detailsleftBox">
            <h4>维护章程</h4>
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
            <h4>维护安全事项</h4>
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
      <template slot="footer">
        <a-button
          type="primary"
          @click.stop="
            () => {
              zsVisible = false;
            }
          "
          >关闭</a-button
        >
      </template>
    </a-modal>

    <a-modal
      title="修改计划日期"
      :visible="planVisible"
      @ok="updatePlanOk"
      @cancel="updatePlanCancel"
    >
    
      <a-form hideRequiredMark>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="计划养护日期">
              <el-date-picker
                v-model="planDate"
                type="date"
                placeholder="选择计划养护日期"
              >
              </el-date-picker>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
    
    <a-modal
      title="养护到期提醒时间设置"
      :visible="setDateVisible"
      @ok="setDateOk"
      @cancel="setDateCancel"
    >
      <a-form-model :rules="rules" :model="setForm" ref="ruleForm">
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-model-item label="养护到期提醒时间设置" prop="days">
              <a-input
                v-model="setForm.days"
                placeholder="请输入养护到期提醒时间"
                @keyup="setForm.days = setForm.days.replace(/[^[1-9]\d]/g, '')"
              ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-model-item label="收件人">
              <a-select
                placeholder="请选择收件人"
                optionFilterProp="children"
                mode="multiple"
                v-model="setForm.usersId"
              >
                <a-select-option
                  v-for="(item, index) in managerList2"
                  :key="index"
                  :value="item.id"
                  >{{ item.name }}</a-select-option
                >
              </a-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-modal>
    <a-modal
      title="分派工单"
      :visible="workerVisible"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="设备信息">
              <a-select
                v-model="equipmentId"
                placeholder="请选择设备信息"
                optionFilterProp="children"
                mode="multiple"
              >
                <a-select-option
                  v-for="(item, index) in equipmentList"
                  :key="index"
                  :value="item.id"
                  >{{ item.equipmentSn }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="20">
            <a-form-item label="选择班组">
              <a-select v-model="workTeamId" placeholder="请选择班组">
                <a-select-option
                  v-for="(item, index) in teamInformationList"
                  :key="index"
                  :value="item.id"
                  >{{ item.teamName }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import index from "@/assets/js/operationCenter/equipmentAccountMaintenance/index.js";
import "@/assets/less/operationCenter/equipmentAccountMaintenance/style.less";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
<style scoped>
.fileImgBox {
  margin-left: 0 !important;
}
</style>
