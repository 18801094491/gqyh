<template>
    <a-drawer
            :title="typeof model.id === 'undefined' ? title + '工单模板' : title"
            :width="500"
            @close="handleCancel"
            :visible="visible"
            :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
        <div class="table-page-search-wrapper">
            <a-form :form="form">
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="模板名称">
                            <a-input v-if="!isShow" placeholder="请输入模板名称" v-model="model.tplName"/>
                            <span v-else>{{this.model.tplName}}</span>
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="模板类型">
                            <a-select
                                    v-if="!isShow"
                                    placeholder="请选择模板类型"
                                    style="width: 100%"
                                    v-model="model.tplType"
                            >
                                <a-select-option
                                        :value="item.code"
                                        v-for="(item,index) in jobTypeList"
                                        :key="index"
                                >{{item.title}}
                                </a-select-option>
                            </a-select>
                            <span v-else>{{this.model.tplTypeName}}</span>
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="状态">
                            <a-select
                                    v-if="!isShow"
                                    placeholder="请选择状态"
                                    style="width: 100%"
                                    v-model="model.tplStatus"
                            >
                                <a-select-option
                                        :value="item.code"
                                        v-for="(item,index) in jobStatusList"
                                        :key="index"
                                >{{item.title}}
                                </a-select-option>
                            </a-select>
                            <span v-else>{{this.model.tplStatusName}}</span>
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="备注">
                            <a-textarea v-if="!isShow" placeholder="请输入备注" :rows="4" v-model="model.remark"/>
                            <span v-else>{{this.model.remark}}</span>
                        </a-form-item>
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
            <a-button :style="{marginRight: '8px'}" @click="handleCancel">关闭</a-button>
            <a-button v-if="!isShow" @click="handleOk" type="primary">提交</a-button>
        </div>
    </a-drawer>
</template>

<script>
    import index from '@assets/js/workOrderManagement/workTemp/modules/addWorkModal.js'
    export default {
        ...index
    }
</script>

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
</style>