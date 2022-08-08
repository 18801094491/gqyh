<!-- 计量中心-计量基础数据录入-水表管理 -->
<template>
  <div class="margin12">
    <div class="screenCommonBox">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="客户名称">
                <a-input
                  placeholder="请输入客户名称"
                  v-model="queryParam.customerName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="水表名称">
                <a-input
                  placeholder="请输入水表名称"
                  v-model="queryParam.waterName"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="所属标段">
                <a-select
                  v-model="queryParam.equimentSection"
                  placeholder="请选择所属标段"
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
            <a-col :md="6" :sm="8">
              <a-form-item label="放置位置">
                <a-input
                  placeholder="请输入放置位置"
                  v-model="queryParam.equimentLocation"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="  编号">
                <a-input
                  placeholder="请输入编号"
                  v-model="queryParam.waterSn"
                ></a-input>
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
      <!-- table区域-begin -->
      <div>
        <a-table
          ref="table"
          size="middle"
          bordered
          row-key="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          @change="handleTableChange"
        >
          <a-switch
            slot="isFarEnd"
            checkedChildren="是"
            slot-scope="text, record, index"
            unCheckedChildren="否"
            v-model="record.remoteStatus == '1' ? false : true"
            defaultChecked
            @change="stateChange(record)"
          />
          <!--<span slot="districtId" slot-scope="text, record">
          <a-popconfirm title="确定解除绑定吗?" v-if="record.districtName" @confirm="() => delAreaUnbind(record)">
	          <a>解除绑定</a>
	        </a-popconfirm>
        </span>-->

          <span slot="action" slot-scope="text, record">
            <!--<a href="javasript:;" @click="waterPriceManagementClick(record)">水价管理</a>
          <a-divider type="vertical"/>-->
            <a href="javasript:;" @click="waterReadingDetail(record)"
              >抄表详情</a
            >
            <a-divider type="vertical" />
            <a href="javasript:;" @click="waterAreaDetail(record)">水表区域</a>
            <a-divider type="vertical" v-if="record.districtName" />
            <a-popconfirm
              title="确定解除绑定吗?"
              v-if="record.districtName"
              @confirm="() => delAreaUnbind(record)"
            >
              <a>区域解绑</a>
            </a-popconfirm>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->
      <a-drawer
        :title="title"
        :width="700"
        @close="handleCancel"
        :visible="visible"
        :wrapStyle="{
          height: 'calc(100%)',
          overflow: 'auto',
          paddingBottom: '108px',
        }"
      >
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="margin-bottom: 20px">
          <a-button @click="waterPriceClick('xz')" type="primary" icon="plus"
            >新增</a-button
          >
        </div>
        <!-- table区域-begin -->
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns2"
            :dataSource="dataSource2"
            :pagination="ipagination2"
            :loading="loading2"
            @change="handleTableChange2"
          >
            <span slot="action" slot-scope="text, record">
              <a href="javasript:;" @click="waterPriceClick('xg', record)"
                >修改</a
              >
            </span>
          </a-table>
        </div>
        <!-- table区域-end -->
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
        </div>
      </a-drawer>
      <!--抄表详情-->
      <a-drawer
        :title="titleReading"
        :width="700"
        row-key="id"
        @close="handleCancelReading"
        :visible="readingVisible"
        :wrapStyle="{
          height: 'calc(100%)',
          overflow: 'auto',
          paddingBottom: '108px',
        }"
      >
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columnsReading"
            :dataSource="readingDataSource"
            :pagination="ipaginationReading"
            :loading="loading2"
            @change="handleTableChangeReading"
          >
          </a-table>
        </div>
        <!-- table区域-end -->
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
            @click="handleCancelReading"
          >
            关闭
          </a-button>
        </div>
      </a-drawer>

      <!--水表区域-->
      <a-modal
        :title="titleArea"
        :visible="areaVisible"
        @ok="areaSubmit"
        @cancel="handleCancelArea"
      >
        <a-form :form="form">
          <a-form-item
            label="区域节点"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
          >
            <j-tree-water-select
              :disabled="disTree"
              ref="treeSelect"
              placeholder="请选择区域"
              v-decorator="['districtId', validatorRules.districtId]"
              dict="settle_district,district_name,id"
              pidField="parent_id"
              pidValue="0"
              condition='{"del_flag":0}'
              @change="getChild"
            >
            </j-tree-water-select>
          </a-form-item>
        </a-form>
      </a-modal>
      <a-modal
        :title="waterPriceTitle"
        width="30%"
        :visible="waterPricevisible"
        :destroyOnClose="true"
        :keyboard="false"
        :maskClosable="false"
        @cancel="waterPricehandleCancel"
        @ok="waterPriceok"
        class="deails"
      >
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="水价">
          <a-input placeholder="请输入水价" v-model="waterPrice"></a-input>
        </a-form-item>
      </a-modal>
    </a-card>
  </div>
</template>

<script>
import index from "@assets/js/measurementCenter/measurementBasedataInput/waterManage";
export default {
  ...index,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>