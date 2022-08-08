<!-- 计量中心-计量基础数据录入-客户信息设置 -->
<template>
  <div id="customerManagement" class="margin12">
    <div class="screenCommonBox">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="12" :sm="12">
              <a-form-item label="客户编码">
                <a-input
                  placeholder="请输入用户编码"
                  v-model="queryParam.customerNumber"
                  @keyup="
                    queryParam.userNumber = queryParam.userNumber.replace(
                      /[\W]/g,
                      ''
                    )
                  "
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="12">
              <a-form-item label="客户名称">
                <a-input
                  placeholder="请输入用户名称"
                  v-model="queryParam.customerName"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row :gutter="24">
            <a-col :md="16" :sm="8">
              <a-form-item label="创建日期">
                <a-date-picker v-model="queryParam.startTime" /> ~
                <a-date-picker v-model="queryParam.endTime" />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8" :offset="2">
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
          v-has="'settle:customer:add'"
          type="primary"
          icon="plus"
          style="margin-right: 6px"
          >新增</a-button
        >
        <a-button
          type="primary"
          icon="download"
          @click="handleExportXls('用户信息')"
          >导出</a-button
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
        :scroll="{ x: 1700 }"
        @change="handleTableChange"
      >
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a :data-data="record" @click="handleAdd('change', record)">修改</a>
          <a-divider type="vertical" />
          <a @click="waterMeterInformation(record)">水表信息</a>
        </span>
        <a
          slot="contractName"
          class="preview"
          :href="record.contractUrl"
          target="_blank"
          :data-data="record.contractUrl"
          slot-scope="text, record, index"
          >{{ record.contractName }}</a
        >
      </a-table>
      <!--<div class="jumpPagination">
            	跳至  <a-input v-model="jumpPagNum" style="width:50px" size="small"></a-input> 页  <a-button size="small">点击</a-button>
            </div>-->
    </a-card>
    <!-- 新增/修改弹框 -->
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
          <a-col :span="12">
            <a-form-item label="客户名称">
              <a-input v-model="customerName" placeholder="请输入客户名称" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户状态">
              <a-select v-model="customerStatus" placeholder="请选择客户状态">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in customerStatusList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
                <!-- <a-select-option value="1">1</a-select-option> -->
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户类型">
              <a-select v-model="customerType" placeholder="请选择客户类型">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in customerTypeList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
                <!-- <a-select-option value="1">1</a-select-option> -->
              </a-select>
            </a-form-item>
          </a-col>
          <!--</a-row>
                <a-row :gutter="16">-->
          <a-col :span="12">
            <a-form-item label="水价模式">
              <a-select v-model="priceMode" placeholder="请选择水价模式">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in waterPriceModeList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
                <!-- <a-select-option value="1">1</a-select-option> -->
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="付款方式">
              <a-select v-model="payMode" placeholder="请选择付款方式">
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in paymentModeList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
                <!-- <a-select-option value="1">1</a-select-option> -->
              </a-select>
            </a-form-item>
          </a-col>
          <!--</a-row>
                <a-row :gutter="16">-->
          <a-col :span="12">
            <a-form-item label="客户联系人">
              <a-input v-model="contacterName" placeholder="请输入客户联系人" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户联系电话">
              <a-input
                v-model="contacterPhone"
                :maxLength="11"
                @keyup="contacterPhone = contacterPhone.replace(/\D/g, '')"
                placeholder="请输入客户联系电话"
              />
            </a-form-item>
          </a-col>
          <!--</a-row>
                <a-row :gutter="16">-->
          <!--<a-col :span="12">
                        <a-form-item label="客户编码">
                            <a-input
                                v-model="customerSn"
                                
                                placeholder="请输入客户编码"
                            />
                        </a-form-item>
                    </a-col>    -->
          <!--<a-col :span="12">
                        <a-form-item label="水价">
                            <a-input
                                v-model="price"
                                
                                placeholder="请输入水价"
                            />
                        </a-form-item>
                    </a-col>    -->
          <a-col :span="12">
            <a-form-item label="客户地址">
              <a-input v-model="customerAddress" placeholder="请输入客户地址" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="客户税号">
              <a-input v-model="customeDuty" placeholder="请输入客户税号" />
            </a-form-item>
          </a-col>
          <!--</a-row>
                <a-row :gutter="16">-->
          <!--<a-col :span="12">
                        <a-form-item label="客户税号">
                            <a-input
                                v-model="customeDuty"
                                placeholder="请输入客户税号"
                            />
                        </a-form-item>
                    </a-col>    -->
          <a-col :span="12">
            <a-form-item label="客户银行账号">
              <a-input
                v-model="customeBankAccout"
                placeholder="请输入客户银行账号"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <!-- <a-row :gutter="16">
                    <a-col :span="24">
                        <a-form-item label="合同附件(请上传PDF格式文件)">
                            <a-button class="uploadBtn">
                                <a-icon type="upload" /> 上传
                                <input type="file" name="file" id="uploadBtn" @change="upfileClick">
                            </a-button>
                        </a-form-item>
                        <div v-if="fileList.length">
                            <div class="flieBox" v-for="(item,index) in fileList" :key="index">
                                <p>{{item.fileName}}</p>
                                <a-icon type="close" @click="removeFile"/>
                            </div>
                        </div>
                    </a-col>    
                        
                </a-row> -->
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
    <!-- 水表信息 -->
    <a-drawer
      title="水表信息"
      :width="850"
      @close="waterMeterInformationOnClose"
      :visible="waterMeterInformationVisible"
      :wrapStyle="{
        height: 'calc(100%)',
        overflow: 'auto',
        paddingBottom: '108px',
      }"
    >
      <!-- 搜索区域 -->
      <a-form layout="inline" style="margin-bottom: 25px">
        <a-row :gutter="24">
          <a-col :span="9">
            <a-form-item label="设备编号">
              <a-input
                placeholder="请输入设备编号"
                v-model="equipmentSn"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="9">
            <a-form-item label="设备名称">
              <a-input
                placeholder="请输入设备名称"
                v-model="equipmentName"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="9">
            <a-form-item label="放置位置">
              <a-input
                placeholder="请输入放置位置"
                v-model="equipmemtLocation"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="9">
            <a-form-item label="设备标段">
              <a-select
                v-model="equimentSection"
                placeholder="请选择所属标段"
                style="width: 174px"
              >
                <a-select-option value="">全部</a-select-option>
                <a-select-option
                  :value="item.code"
                  v-for="(item, index) in bidSegmentList"
                  :key="index"
                  >{{ item.title }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="4" :offset="2">
            <a-button
              type="primary"
              icon="search"
              @click="waterMeterInformationSearch"
              >查询</a-button
            >
          </a-col>
        </a-row>
      </a-form>
      <h3>水表信息</h3>
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
          class="waterMeterInformationBtn"
          slot="maintainBtn"
          slot-scope="text, record, index"
        >
          <a
            v-if="record.bindStatus == '0'"
            @click="bindWaterMeterInformation(record)"
            >绑定</a
          >
          <a v-if="record.bindStatus == '1'" class="active">已绑定</a>
        </span>
      </a-table>

      <h3 style="margin-top: 30px">绑定水表信息</h3>
      <!-- 表格显示区域 -->
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        class="bindCustomerEquip"
        :columns="columns3"
        :dataSource="dataSource3"
        :loading="loading3"
      >
        <span slot="maintainBtn" slot-scope="text, record, index">
          <a
            v-if="record.bindStatus === '00'"
            @click="unBindCustomerEquip(record)"
            >解绑</a
          >
          <span v-if="record.bindStatus === '01'" class="active">失效</span>
        </span>
        <div slot="equipData" slot-scope="text, record, index">
          <p v-for="(item, index) in record.equipData" :key="index">
            <span>{{ item.variableName }}:</span>
            <span>{{ item.varibleValue }}</span>
          </p>
        </div>
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
        <a-button
          :style="{ marginRight: '8px' }"
          @click="waterMeterInformationOnClose"
        >
          关闭
        </a-button>
      </div>
    </a-drawer>
  </div>
</template>
<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/customerInfoSetting";
export default {
  ...index,
};
</script>

