<template>
<!-- 资产中心-资产管理-资产录入-添加设备 -->
    <a-drawer
            :title="typeof model.id === 'undefined' ? title  : title"
            :width="500"
            @close="handleCancel"
            :visible="visible"
            :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
        <div class="table-page-search-wrapper">
            <a-form :form="form">
                <a-row :gutter="24">
                    <a-col :span="12">
                        <a-form-item
                                label="供应商">
                            {{model.equipmentSupplierName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item
                                label="设备类型">
                            {{model.equipmentTypeName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item
                                label="设备型号">
                            {{model.equipmentModelName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item
                                label="设备规格">
                            {{model.equipmentSpecsName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备编号">
                            <a-input v-if="!isShow" placeholder="请输入设备编号" v-model="model.equipmentSn"
                                     @keyup="model.equipmentSn = model.equipmentSn.replace(/[\W]/g,'')"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备名称">
                            <a-input
                                    v-model="model.equipmentName"
                                    placeholder="请输入设备名称"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="资产类别">
                            <a-select v-model="model.equipmentCategory" placeholder="请选择资产类别">
                                <a-select-option :value="item.code" v-for="(item,index) in equipmentCategoryList"
                                                 :key="index">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="放置位置">
                            <a-input
                                    v-model="model.equipmentLocation"
                                    placeholder="请输入放置位置"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="所属标段">
                            <a-select v-model="model.equipmentSection" placeholder="请选择所属标段">
                                <a-select-option :value="item.code" v-for="(item,index) in bidSegmentList" :key="index">
                                    {{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :span="12">
                        <a-form-item label="资产状态">
                            <a-select v-model="model.equipmentStatus" placeholder="请选择资产状态">
                                <a-select-option v-for="(item,index) in assetStatusList" :key="index"
                                                 :value="item.code">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="购置时间">
                            <a-date-picker v-model="model.equipmentPurchase" style="width:100%"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="投入运营时间">
                            <a-date-picker v-model="model.equipmentOperating" style="width:100%"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备状态">
                            <a-select v-model="model.equipmentRevstop" placeholder="请选择设备状态">
                                <a-select-option v-for="(item,index) in workingStatusList" :key="index"
                                                 :value="item.code">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="责任人">
                            <a-select v-model="model.personResponsible" placeholder="请选择责任人" mode="multiple"
                                      optionFilterProp="children">
                                <a-select-option v-for="(item,index) in managerList" :key="index" :value="item.id">
                                    {{item.name}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="24">
                        <a-form-item label="选择图片">
                            <a-button class="uploadBtn" style="float: left;">
                                <a-icon type="upload"/>
                                上传
                                <input type="file" value="" class="uploadBtnB" name="file"
                                       @change="upfileClick($event)">
                            </a-button>
                        </a-form-item>
                    </a-col>
                    <div class="fileImgBox clearfix" v-if="this.stateList[0].imgUrl">
                        <div class="fileImgDivBox">
                            <img :src="this.stateList[0].imgUrl" alt="">
                            <a-icon class="fileImgRemove" type="close-circle" @click="fileImgRemove(0)"/>
                        </div>
                    </div>
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
    import assetInformationAddModal from '@/assets/js/equipmentAccount/components/AssetInformationAddModal.js';

    export default {
        ...assetInformationAddModal
    }
</script>

<style scoped>
    @import '~@assets/less/equipmentAccount/components/AssetInformationAddModal.less'
</style>