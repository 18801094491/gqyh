<template>
<div>
  <a-drawer
    :title="title + '巡检线路'"
    :width="700"
    @close="handleCancel"
    :visible="visible"
    :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
  >

  <div class="table-page-search-wrapper">
    <a-form :form="form">
      <a-row :gutter="24">
        <a-col :span="8">
            <a-form-item label="线路名称"> 
              <a-input :disabled="isShow" placeholder="请输入线路名称" v-model="model.name" />
            </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="注意事项">
            <a-input :disabled="isShow" placeholder="请输入注意事项" v-model="model.attention" />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="备注">
            <a-input :disabled="isShow" placeholder="请输入备注" v-model="model.remark" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <!-- 操作按钮区域 -->
          <div style="display:flex;flex:1;justify-content: space-between;margin-bottom: 10px;align-items: baseline;"> 
            <span style="color:rgba(0, 0, 0, 0.85);font-size:14px">巡检点列表：</span>
            <a-button type="default" icon="plus" @click="openPointDialog" v-if="!isShow">选择巡检点</a-button>
          </div>
          <!-- table区域-begin -->
          <div>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                :columns="columns"
                :dataSource="pointDataList"
                :pagination="false"
                :loading="loading"
              >
              <span slot="action" slot-scope="text, record, index">
                <a-popconfirm title="确定删除吗?" @confirm="() => delSelectPoint(index)" v-if="!isShow">
                  <a>删除</a>
                </a-popconfirm>
                <span v-else style="color: rgba(0, 0, 0, 0.25)">删除</span>
              </span>
            </a-table>
          </div>
          <!-- table区域-end -->
        </a-col>
        <a-col :span="24">
          <div style="display:flex;flex:1;justify-content: space-between;margin: 10px 0;align-items: baseline;"> 
            <span style="color:rgba(0, 0, 0, 0.85);font-size:14px">地图画线：</span>
            <a-button type="default" @click="clearMapLine" v-if="!isShow">重新绘制</a-button>
          </div>
          <div style="width: 100%;height:300px;">
            <div id="lineMap"></div>
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
    <a-button :style="{marginRight: '8px'}" @click="handleCancel">
      关闭
    </a-button>
    <a-button v-if="!isShow" @click="handleOk" type="primary">提交</a-button>
  </div>
</a-drawer>
<select-point-list ref="listModal" @onSelect="selectPoint" ></select-point-list>
</div>
</template>

<script>
import index from '@assets/js/workOrderManagement/workPlan/InspectionLineManagement/AddModal.js';
export default {
  ...index
}

</script>
<style>
.customer-select.ant-select-disabled .ant-select-selection {
  background-color: #f5f5f5!important;
}
  
</style>
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

    /deep/ .ant-input-number {
        width: 100%;
    }
    #lineMap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
</style>