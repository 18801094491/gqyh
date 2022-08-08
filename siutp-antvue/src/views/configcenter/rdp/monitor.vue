<template>
    <div class="margin12">
        <!--表格-->
        <div class="p30 pt30 bg_fff">
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
                <span slot="action" slot-scope="text, record">
                    <a @click="bindMonitor(record)"  v-if="!record.indexCode">绑定监控</a>
                    <a-popconfirm
                            title="Are you sure delete this task?"
                            ok-text="Yes"
                            cancel-text="No"
                            @confirm="bindMonitorPoint(record.id, '')"
                            @cancel="cancel"
                            v-else
                    >
                        <a href="javascript:void(0);">解绑监控</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </div>

        <!-- 绑定监控滑框开始 -->
        <a-modal title="绑定监控" :visible="bindMonitorVisible" width="800px" @cancel="bindMonitorCancel" cancelText="关闭">
            <template slot="footer">
                <a-button @click="bindMonitorCancel">关闭</a-button>
            </template>
            <!-- 搜索区域 -->
            <a-form layout="inline" class="mb25">
                <a-row :gutter="24">
                    <a-col :md="9" :sm="8">
                        <a-form-item label="设备编码">
                            <a-input placeholder="请输入设备编码" v-model="equipmentSn"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8" class="mt04">
                        <a-button type="primary" @click="monitorSearchQuery" icon="search">查询</a-button>
                    </a-col>
                </a-row>
            </a-form>
            <!-- 表格显示区域 -->
            <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="id"
                    :columns="monitorColumns"
                    :dataSource="monitorDataSource"
                    :pagination="monitorIpagination"
                    :loading="monitorLoading"
                    @change="monitorHandleTableChange">
              <span slot="monitorAction" slot-scope="text, record, index">
                  <a  @click="bindMonitorPoint('', record.hkMonitorCode, record.hkMonitorKey)">绑定</a>
              </span>
            </a-table>
        </a-modal>
    </div>
</template>

<script>
    import index from '@/assets/js/configcenter/repMonitor.js'
    export default {
        ...index
    }
</script>

<style scoped lang="less">
    @import "~@assets/less/common.less";
    .mb25{
        margin-bottom:25px
    }
    .mt04{
        margin-top:4px;
    }

</style>