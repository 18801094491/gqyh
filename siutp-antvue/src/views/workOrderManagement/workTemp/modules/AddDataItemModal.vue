<template>
    <a-drawer
            :title="title"
            :width="700"
            @close="handleCancel"
            :visible="visible"
            :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
        <div class="table-page-search-wrapper">
            <a-form :form="form">
                <a-row :gutter="24">
                    <a-col :span="12">
                        <a-form-item label="数据项类型">
                            <a-radio-group :disabled="isShow" v-model="model.dataCategory" @change="changeDataCategory">
                                <a-radio value="0">数据项</a-radio>
                                <a-radio value="1">子数据项</a-radio>
                            </a-radio-group>
                            <!-- <span v-else>{{model.dataCategory * 1 === 0 ? '数据项' : '子数据项'}}</span> -->
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="数据项名称">
                            <a-input :disabled="isShow" placeholder="请输入数据项名称" v-model="model.dataName"/>
                            <!-- <span v-else>{{model.dataName}}</span> -->
                        </a-form-item>
                    </a-col>
                    <a-col :span="12" v-if="model.dataCategory * 1 === 1">
                        <a-form-item label="上级数据项">
                            <a-tree-select
                                    class="customer-select"
                                    style="width:100%"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="treeData"
                                    v-model="model.parentId"
                                    placeholder="请选择上级数据项"
                                    :disabled="isShow"
                                    @change="handleParentIdChange"
                            ></a-tree-select>
                            <!-- <span v-else> {{model.parentName}} </span> -->
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="是否需要录入">
                            <a-switch :disabled="isShow" checkedChildren="是" unCheckedChildren="否"
                                      v-model="model.needEnter"/>
                            <!-- <span v-else>{{model.needEnter ? '是' : '否'}}</span> -->
                        </a-form-item>
                    </a-col>
                    <template v-if="model.needEnter">
                        <a-col :span="24" style="padding:0">
                            <a-col :span="12">
                                <a-form-item label="数据类型">
                                    <a-select class="customer-select"
                                              showSearch
                                              placeholder="请选择数据类型"
                                              :disabled="isShow"
                                              optionFilterProp="children"
                                              style="width: 100%"
                                              @change="changeDataType"
                                              v-model="model.dataType"
                                    >
                                        <a-select-option v-for="(item,index) in dataTypeList" :key="index"
                                                         :value="item.code">{{item.title}}
                                        </a-select-option>
                                    </a-select>
                                    <!-- <span v-else>{{model.dataTypeName}}</span> -->
                                </a-form-item>
                            </a-col>
                            <a-col :span="12">
                                <a-form-item label="单位">
                                    <a-input :disabled="isShow" placeholder="请输入单位" v-model="model.dataUnit"/>
                                    <!-- <span v-else> {{model.dataUnit}}</span> -->
                                </a-form-item>
                            </a-col>
                        </a-col>
                        <a-col :span="24" v-for="(item, i) in recordFormItem" :key="i" style="padding:0">
                            <a-col :span="12">
                                <a-form-item label="选项名称">
                                    <a-input :disabled="isShow" v-model="item.itemName" placeholder="请输入选项名称"/>
                                    <!-- <span v-else> {{item.itemName}}</span> -->
                                </a-form-item>
                            </a-col>
                            <a-col :span="isShow ? 12 : 10">
                                <a-form-item label="选项排序">
                                    <a-input-number :disabled="isShow" v-model="item.itemOrder" placeholder="请输入选项名称"/>
                                    <!-- <span v-else> {{item.itemOrder}}</span> -->
                                </a-form-item>
                            </a-col>
                            <a-col :span="2" style="text-align: right" v-if="!isShow">
                                <a-button style="margin-top: 42px;"
                                          v-if="i === recordFormItem.length - 1"
                                          @click="addItem"
                                          icon="plus"
                                          type="primary"
                                ></a-button>
                                <a-button style="margin-top: 42px;"
                                          v-if="recordFormItem.length > 1 && i !== recordFormItem.length - 1"
                                          @click="minusItem(i,item)"
                                          icon="minus"
                                          type="primary"
                                ></a-button>
                            </a-col>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="是否必填">
                                <a-switch :disabled="isShow" checkedChildren="是" unCheckedChildren="否"
                                          v-model="model.needRequired"/>
                                <!-- <span v-else>{{model.needRequired ? '是' : '否'}}</span> -->
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :span="12">
                        <a-form-item label="排序">
                            <a-input-number :disabled="isShow" v-model="model.dataOrder" placeholder="请输入数据项排序"
                                            style="width: 100%"/>
                            <!-- <span v-else>{{model.dataOrder}}</span> -->
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
            <a-button :style="{marginRight: '8px'}" @click="handleCancel">
                关闭
            </a-button>
            <a-button v-if="!isShow" @click="handleOk" type="primary">提交</a-button>
        </div>
    </a-drawer>

</template>

<script>
    import index from '@assets/js/workOrderManagement/workTemp/modules/addDataItemModal.js';

    export default {
        ...index
    }
</script>
<style>
    .customer-select.ant-select-disabled .ant-select-selection {
        background-color: #f5f5f5 !important;
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
</style>