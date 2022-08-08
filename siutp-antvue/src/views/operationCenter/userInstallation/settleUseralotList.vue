<template>
    <div class="margin12">
        <div class="screenCommonBox">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="客户名称">
                                <a-input placeholder="请输入客户名称" v-model="queryParam.customerName"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="12" :sm="8">
                            <a-form-item label="报装时间">
                                <a-date-picker v-model="queryParam.startTime"/>
                                ~
                                <a-date-picker v-model="queryParam.endTime"/>
                            </a-form-item>
                        </a-col>

                        <a-col :md="6" :sm="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button> 
              <a-button class="ant-btn-border" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              
            </span>
                        </a-col>

                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">


            <!-- 操作按钮区域 -->
            <div class="table-operator">
                <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
                <a-button type="primary" icon="download" @click="handleExportXls('用户报装信息')">导出</a-button>

            </div>

            <!-- table区域-begin -->
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
                        :scroll="{x:1600}"
                        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">修改</a>

          <a-divider type="vertical"/>
          <a @click="handleEdit2(record)">安装进度</a>

          <a-divider type="vertical"/>
          <a @click="handleEdit3(record)">审核</a>
        </span>

                </a-table>
            </div>
            <!-- table区域-end -->

            <!-- 表单区域 -->
            <settleUseralot-modal ref="modalForm" @ok="modalFormOk"></settleUseralot-modal>

            <a-modal
                    title="安装进度"
                    :width="500"
                    :visible="installationProgressvisible"
                    :confirmLoading="confirmLoading2"
                    @ok="installationProgresshandleOk"
                    @cancel="installationProgresshandleCancel"
                    cancelText="关闭">
                <a-spin :spinning="confirmLoading2">
                    <a-form :form="form2">
                        <a-form-item
                                :labelCol="labelCol"
                                :wrapperCol="wrapperCol"
                                label="任务分配人员">
                            <a-select placeholder="请选择班次" v-decorator="['taskId', {}]">
                                <a-select-option :value="item.id" v-for="(item,index) in managerList" :key="index">
                                    {{item.name}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        <a-form-item
                                :labelCol="labelCol"
                                :wrapperCol="wrapperCol"
                                label="实施人联系电话">
                            <a-input placeholder="请输入实施人联系电话" v-decorator="['telephone', {}]"/>
                        </a-form-item>

                        <a-form-item
                                :labelCol="labelCol"
                                :wrapperCol="wrapperCol"
                                label="安装时间">
                            <el-date-picker
                                    style="width:100%"
                                    v-model="form.installTime"
                                    placeholder="请选择时间"
                                    type="datetime"
                                    format="yyyy-MM-dd HH:mm"
                            ></el-date-picker>
                        </a-form-item>
                        <a-form-item
                                :labelCol="labelCol"
                                :wrapperCol="wrapperCol"
                                label="安装状态">
                            <a-select placeholder="请选择安装状态" v-decorator="['installState', {}]">
                                <a-select-option value="0">未安装</a-select-option>
                                <a-select-option value="1">已安装</a-select-option>
                            </a-select>
                        </a-form-item>


                    </a-form>
                </a-spin>
            </a-modal>

            <a-modal
                    title="审核"
                    :width="500"
                    :visible="examinevisible"
                    :confirmLoading="confirmLoading3"
                    @ok="examineOk"
                    @cancel="examineCancel"
                    cancelText="关闭">
                <a-spin :spinning="confirmLoading3">
                    <a-form>
                        <a-form-item
                                label="审核验收意见">
                            <a-textarea placeholder="请输入审核验收意见" :rows="4" v-model="accepidea"/>

                        </a-form-item>


                    </a-form>
                </a-spin>
            </a-modal>
        </a-card>
    </div>

</template>

<script>
    import index from '@assets/js/operationCenter/userInstallation/settleUseralotList.js';

    export default {
        ...index
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
<style lang="less" scoped>
    .ant-col-sm-5 {
        height: 40px;
        line-height: 40px;
    }

    .ant-row {
        margin-bottom: 10px;
    }
</style>