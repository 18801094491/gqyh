<template>
    <div id="equipmentAccountMaintenanceBox" class="margin12">
        <div class="screenCommonBox">
            <div class="table-page-search-wrapper">

                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="6" :sm="8">
                            <a-form-item label="所属标段">
                                <a-select v-model="queryParam.optSection" placeholder="请选择所属标段">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option :value="item.code" v-for="(item,index) in bidSegmentList"
                                                     :key="index">{{item.title}}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="设备类型">
                                <a-select v-model="queryParam.equipmentType" placeholder="请选择设备类型">
                                    <a-select-option :value="item.code" v-for="(item,index) in modelTypeList2"
                                                     :key="index">{{item.title}}
                                    </a-select-option>

                                </a-select>

                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="放置位置">
                                <a-input placeholder="请输入放置位置" v-model="queryParam.optLocation"></a-input>

                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="设备状态">
                                <a-select v-model="queryParam.equipmentRevstop" placeholder="请选择设备状态">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option value="1">启用</a-select-option>
                                    <a-select-option value="0">停用</a-select-option>
                                </a-select>

                            </a-form-item>
                        </a-col>
                        <a-col :md="6" :sm="12">
                            <a-form-item label="设备编号">
                                <a-input placeholder="请输入账号查询" v-model="queryParam.equipmentNumber"
                                         @keyup="queryParam.equipmentNumber=queryParam.equipmentNumber.replace(/[\W]/g,'')"></a-input>

                            </a-form-item>
                        </a-col>
                        <!--<template v-if="toggleSearchStatus">-->
                        <a-col :md="6" :sm="8">
                            <a-form-item label="供应商">
                                <a-select v-model="queryParam.supplier" placeholder="请选择供应商">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option :value="item.id"
                                                     v-for="(item,index) in supplierClassificationList2" :key="index">
                                        {{item.supplierName}}
                                    </a-select-option>

                                </a-select>
                            </a-form-item>
                        </a-col>
                        <!--</template>-->
                        <a-col :md="6" :sm="8" :offset="6">
                            <!--<a-col :md="6" :sm="8" :offset="toggleSearchStatus?'6':'12'" >-->
                            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
		              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
		              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                                <!--<a @click="handleToggleSearch" style="margin-left: 8px">
                                  {{ toggleSearchStatus ? '收起' : '展开' }}
                                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                                </a>-->
		            </span>
                        </a-col>

                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">
            <!-- 操作按钮区域 -->
            <div class="table-operator" style="border-top: 5px">
                <!-- <a-button @click="handleAdd('add')"  type="primary" icon="plus" v-has="'opt:add'">新增</a-button> -->
                <!-- <a-button type="primary" icon="edit" @click="handleChange">修改</a-button> -->
                <a-button type="primary" @click="handleExportXls('设备台账')" icon="download" v-has="'opt:export'">导出
                </a-button>
                <a-upload
                        name="file"
                        :showUploadList="false"
                        :multiple="false"
                        :headers="tokenHeader"
                        :action="importExcelUrl"
                        @change="handleImportExcel"
                >
                    <a-button type="primary" icon="import" v-has="'opt:import'">导入</a-button>
                </a-upload>
                <a-button type="primary" @click="handleExportXls2('设备台账导入模板')" icon="download"
                          v-has="'opt:downloadModel'">下载导入模板
                </a-button>
                <a-button type="primary" @click="changeImg" icon="edit" v-has="'opt:uploadImg'">设备图片</a-button>
            </div>
            <a-tabs :activeKey="equipmentCategory2" @change="callback">
                <a-tab-pane :tab="item.title" :key="item.code"
                            v-for="(item,index) in equipmentCategory2List"></a-tab-pane>

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
                    @change="handleTableChange">
              <span slot="maintainBtn" slot-scope="text, record, index">
                
                <!-- <a :data-data="record" v-if="record.bindStatus==1" @click="bindShow(record)">绑定</a>
                <a :data-data="record" v-if="record.bindStatus==0" @click="unbindGis(record)">解绑</a>
                <a-divider type="vertical"/> -->
                <a :data-data="record" @click="handleAdd('change',record)" v-has="'opt:edit'">修改</a>
                <a-divider type="vertical" v-has="'opt:edit'"/>
                <a @click="deviceDetailsClick(record)">详情</a>
              </span>
                <a-switch slot="equipmentRevstopText" v-has="'opt:add'" checkedChildren="启用"
                          slot-scope="text, record, index" unCheckedChildren="停用"
                          v-model="record.equipmentRevstop==1?true:false" defaultChecked
                          @change="stateChange($event,record.id)"/>
            </a-table>
            <!--<div class="jumpPagination">
                  跳至  <a-input v-model="jumpPagNum" style="width:50px" size="small"></a-input> 页  <a-button size="small" @click="toPage">点击</a-button>
              </div>-->
        </a-card>
        <a-drawer
                :title="drawerTitle"
                :width="500"
                @close="onClose"
                :visible="visible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-form :form="form" layout="vertical" hideRequiredMark>
                <a-row :gutter="16">
                    <a-col :span="12">
                        <a-form-item label="供应商">
                            {{equipmentSupplierName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备类型">
                            {{equipmentTypeName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备型号">
                            {{equipmentModelName}}
                        </a-form-item>
                    </a-col>

                    <a-col :span="12">
                        <a-form-item label="设备规格">
                            {{equipmentSpecsName}}
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备编号">
                            <a-input
                                    v-model="equipmentNumber"
                                    placeholder="请输入设备编号"
                                    @keyup="equipmentNumber=equipmentNumber.replace(/[\W]/g,'')"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备名称">
                            <a-input
                                    v-model="equipmentName"
                                    placeholder="请输入设备名称"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="设备类别">
                            <a-select v-model="equipmentCategory" placeholder="请选择设备类别">
                                <a-select-option :value="item.code" v-for="(item,index) in equipmentCategoryList"
                                                 :key="index">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="放置位置">
                            <a-input
                                    v-model="position"
                                    placeholder="请输入放置位置"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="所属标段">
                            <a-select v-model="bidSegment" placeholder="请选择所属标段">
                                <a-select-option :value="item.code" v-for="(item,index) in bidSegmentList" :key="index">
                                    {{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :span="12">
                        <a-form-item label="资产状态">
                            <a-select v-model="state" placeholder="请选择资产状态">
                                <a-select-option v-for="(item,index) in assetStatusList" :key="index"
                                                 :value="item.code">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :span="12">
                        <a-form-item label="购置时间">
                            <a-date-picker style="width:100%" v-decorator="[ 'equipmentPurchase', {}]"/>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="投入运营时间">
                            <a-date-picker style="width:100%" v-decorator="[ 'equipmentOperating', {}]"/>
                        </a-form-item>
                    </a-col>

                    <a-col :span="12">
                        <a-form-item label="设备状态">
                            <a-select v-model="equipmentRevstop" placeholder="请选择设备状态态">
                                <a-select-option v-for="(item,index) in workingStatusList" :key="index"
                                                 :value="item.code">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <!--<div class="stateListBoxImg" style="display: flex;" v-for="(item,index) in stateList" :key="index">-->
                    <a-col :span="12">
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
                            <a-icon class="fileImgRemove" type="close-circle" @click="fileImgRemove()"/>
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
                <a-button :style="{marginRight: '8px'}" @click="onClose">
                    关闭
                </a-button>
                <a-button @click="addSubmit" type="primary">提交</a-button>
            </div>
        </a-drawer>
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
                            <img :src="assetImg" alt="" width="100%" height="100%"/>
                        </div>
                    </div>
                    <div class="informationTopBodyRightBox" style="width:calc(100% - 150px)">
                        <!-- <div class="informationTopBodyRightDivBox clearfix" v-if="attributeList.length>0" v-for="(item,index) in attributeList" :key="index">
                          <label>{{item.attributeCn}}:</label>
                          <span>{{item.attributeVal}}</span>
                        </div> -->
                        <div class="informationTopBodyRightDivBox clearfix">
                            <label>设备编号:</label>
                            <span>{{assetCoding}}</span>
                        </div>
                        <div class="informationTopBodyRightDivBox clearfix">
                            <label>设备名称:</label>
                            <span>{{assetName}}</span>
                        </div>
                        <div class="informationTopBodyRightDivBox clearfix">
                            <label>生产厂家:</label>
                            <span>{{manufacturer}}</span>
                        </div>
                        <div class="informationTopBodyRightDivBox clearfix">
                            <label>设备型号:</label>
                            <span>{{informationEquipmentType}}</span>
                        </div>
                        <div class="informationTopBodyRightDivBox clearfix">
                            <label>放置位置:</label>
                            <span>{{assetLocation}}</span>
                        </div>
                    </div>

                </div>
                <div class="informationListBox">
                    <h4>采集数据</h4>
                    <a-form>
                        <a-row v-for="(item,index) in attributeList" :key="index">
                            <a-col :span="24">
                                <a-form-item
                                        :labelCol="labelCol"
                                        :wrapperCol="wrapperCol"
                                        :label="item.attributeCn">
                                    <p>{{item.attributeVal}}</p>

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
                                    label="维保次数">
                                <p>{{upkeepCount}}</p>

                            </a-form-item>
                        </a-col>

                    </a-row>
                    <a-row>
                        <a-col :span="24">
                            <a-form-item
                                    :labelCol="labelCol"
                                    :wrapperCol="wrapperCol"
                                    label="最近保养时间">
                                <p>{{upkeepTimeBY}}</p>

                            </a-form-item>
                        </a-col>

                    </a-row>
                    <a-row>
                        <a-col :span="24">
                            <a-form-item
                                    :labelCol="labelCol"
                                    :wrapperCol="wrapperCol"
                                    label="最近维修时间">
                                <p>{{upkeepTimeWX}}</p>

                            </a-form-item>
                        </a-col>

                    </a-row>
                </div>
                <div class="informationListBox zhishi" style="background-color:#fff">
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

        <a-modal title="设备类型主题" :visible="changeImgVisible" width="400px" @ok="changeImgOnOk" @cancel="changeImgCancel">
            <!-- 搜索区域 -->
            <a-form>
                <a-row>
                    <a-col>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="设备类型">
                            <a-select placeholder="请选择设备类型" v-model="equipType" @change="equipTypeChange">
                                <a-select-option :value="item.code" v-for="(item,index) in equipmentTypeList"
                                                 :key="index">{{item.title}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                </a-row>
                <div class="stateListBoxImg" style="display: flex;" v-for="(item,index) in stateList" :key="index">
                    <!--<a-form>-->
                    <a-row>
                        <a-col>
                            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="选择图片">
                                <a-button class="uploadBtn" style="margin-left: 20px;">
                                    <a-icon type="upload"/>
                                    上传
                                    <input type="file" value="" class="uploadBtnB" name="file"
                                           @change="upfileClick($event,index)">
                                </a-button>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <!--</a-form>-->
                    <div class="fileImgBox clearfix" v-if="item.imgUrl">
                        <div class="fileImgDivBox">
                            <img :src="item.imgUrl" alt="">
                            <a-icon class="fileImgRemove" type="close-circle" @click="fileImgRemove()"/>
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
            <div class="rules" v-for="(categoryItem,categoryIndex) in knowlegeItemVoList" :key="categoryIndex">
                <h3>规程{{categoryIndex+1}}</h3>
                <div class="detailsBox clearfix">
                    <div class="detailsleftBox">
                        <h4>维护章程</h4>
                        <div v-for="(operationRulesItem,operationRulesIndex) in categoryItem.knowlegeOperationList"
                             :key="operationRulesIndex">
                            <p>{{operationRulesIndex+1}}、{{operationRulesItem.operationItem}}</p>
                        </div>
                    </div>
                    <div class="detailsrightBox">
                        <h4>维护安全事项</h4>
                        <div v-for="(safetyPrecautionsItem,safetyPrecautionsIndex) in categoryItem.knowlegeCautionList"
                             :key="safetyPrecautionsIndex">
                            <p>{{safetyPrecautionsIndex+1}}、{{safetyPrecautionsItem.cautionItem}}</p>
                        </div>
                    </div>
                </div>
                <div class="detailsManualBox">
                    <h4>手册：</h4>
                    <!-- <p>安全手册（点击可查看），无则显示暂无</p> -->
                    <div class="detailsManualNo" v-if="!categoryItem.knowlegeAttachList.length">
                        <p>暂无数据</p>
                    </div>
                    <div class="detailsManual" v-if="categoryItem.knowlegeAttachList.length"
                         v-for="(fjItem,fjIndex) in categoryItem.knowlegeAttachList" :key="fjIndex">
                        <a :href="fjItem.attachFile" target="_blank">{{fjItem.fileName}}</a>
                    </div>
                </div>
            </div>
            <template slot="footer">
                <a-button type="primary" @click.stop="()=>{zsVisible=false}">关闭</a-button>
            </template>
        </a-modal>
    </div>
</template>

<script>
    import index from '@/assets/js/equipmentAccount/maintenance.js'
    import '@/assets/less/equipmentAccount/maintenance.less'

    export default {
        ...index
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
<style scoped>
    .fileImgBox {
        margin-left: 0 !important;
    }
</style>
