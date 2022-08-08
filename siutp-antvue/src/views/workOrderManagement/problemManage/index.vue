<template>
    <div class="margin12 workOrder-maintenance">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="20">
                        <a-col :md="6" :sm="6">
                            <a-form-item label="问题状态">
                                <a-select v-model="queryParam.status" placeholder="请选择问题状态">
                                    <a-select-option value>全部</a-select-option>
                                    <a-select-option v-for="(item,index) in statusList"
                                      :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="12" :sm="14">
                            <a-form-item label="上报日期">
                                <a-date-picker v-model="queryParam.subTimeStart" placeholder="选择开始时间" />
                                ~
                                <a-date-picker v-model="queryParam.subTimeEnd" placeholder="选择结束时间" />
                            </a-form-item>
                        </a-col>
                        <a-col class="float-r" :md="4" :sm="8">
                            <span class="table-page-search-submitButtons float-r">
                                <a-button type="primary" @click="searchQuery('')" icon="search">查询</a-button>
                                <a-button
                                    class="ant-btn-border ml8"
                                    @click="searchReset"
                                    icon="reload"
                                >重置</a-button>
                            </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">
            <div class="table-operator">
                <a-button @click="handleAdd('add')" type="primary" icon="plus">新增</a-button>
            </div>
            <a-table
              ref="table"
              size="middle"
              bordered
              row-key="id"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="ipagination"
              :loading="loading"
              @change="handleTableChange">
                <span slot="action" slot-scope="text, record">
                    <a :data-data="record" @click="handleAdd('see',record)">查看</a>
                    <template v-if="record.status != 3">
                        <a-divider type="vertical" />
                        <a :data-data="record" @click="handleAdd('edit',record)">修改</a>
                        <a-divider type="vertical" />
                        <a-popconfirm title="确定删除吗?" @confirm="() => onDeleteChange(record.id)"
                        v-if="record.problemStatus!='2' && record.problemStatus!='3'">
                            <a>删除</a>
                        </a-popconfirm>
                    </template>
                </span>
            </a-table>
        </a-card>
        <a-drawer :title="drawerTitle" :width="800" @close="onCloseChange" :visible="visible"
          :wrapStyle="wrapStyle">
            <a-spin :spinning="spinning">
                <a-form hideRequiredMark>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="问题标题">
                                <a-input v-model="title" placeholder="请输入问题标题" :disabled="see_disabled" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="问题类型">
                                <a-select v-model="matterType" placeholder="请选择问题类型" :disabled="see_disabled">
                                    <a-select-option v-for="(item,index) in typeList"
                                    :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="问题等级">
                                <a-select v-model="matterLevel" placeholder="请选择问题等级" :disabled="see_disabled">
                                    <a-select-option v-for="(item,index) in levelList"
                                    :key="index" :value="item.code">
                                        {{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="发现地点">
                                <a-button type="primary" @click="onConfirmLocationChange">选择地点</a-button>
                                <span>{{' '+ lngLat}}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="问题描述">
                                <a-textarea :rows="4" v-model="description" placeholder="请输入问题描述" :disabled="see_disabled" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="24" class="clearfix">
                            <a-form-item label="图片">
                                <a-upload
                                    accept="image/*,video/*"
                                    :action="domianURl + 'sys/file/upload'"
                                    list-type="picture-card"
                                    :file-list="upload_fileList"
                                    :headers="headers"
                                    :multiple="true"
                                    :disabled="see_disabled"
                                    @preview="handlePreview"
                                    @change="onImgUploadChange">
                                    <div v-if="upload_fileList.length < 8">
                                        <a-icon type="plus" />
                                    </div>
                                </a-upload>
                                <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                                    <img alt="example" style="width: 100%" :src="previewImage" />
                                </a-modal>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="24">
                        <a-col :span="24">
                            <a-form-item label="关联设备">
                                <a-select 
                                show-search 
                                v-model="equipmentId"
                                placeholder="请选择关联设备"
                                :filter-option="filterOption"
                                :disabled="see_disabled">
                                    <a-select-option value="">-- 请选择 --</a-select-option>
                                    <a-select-option v-for="(item,index) in equipmentList"
                                    :key="index" :value="item.id">
                                        {{item.equipmentName}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
            </a-spin>
            <div class="btns">
                <a-button class="mr08" @click="onCloseChange">关闭</a-button>
                <a-button :loading="spinning" @click="addSubmit" type="primary" :disabled="see_disabled">提交</a-button>
            </div>
        </a-drawer>

        <a-modal title="选择问题发现点" :width="678" :visible="visible_line" @cancel="lineModalCancel" @ok="lineModalOk">
            <a-spin :spinning="confirmLoading">
                <div id="problemManage-allmap"></div>
            </a-spin>
        </a-modal>
    </div>
</template>

<script>
    import index from "@assets/js/workOrderManagement/problemManage/index.js";
    export default {
        ...index
    };
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
<style scoped>
    @import "~@assets/less/workOrderManagement/problemManage/index.less";
</style>