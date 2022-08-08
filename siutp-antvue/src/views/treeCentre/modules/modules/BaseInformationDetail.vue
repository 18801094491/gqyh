<!-- 资产中心-资产台账-详情-基本信息详情 -->
<template>
    <div class="table-page-search-wrapper" v-if="searchType=='met'">
        <a-form layout="inline">
            <a-row :gutter="16">
                <a-col :span="12">
                    <a-form-item label="客户编码">
                        {{item.customerSn}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户名称">
                        {{item.customerName}}
                    </a-form-item>
                </a-col> 
                <a-col :span="12">
                    <a-form-item label="客户地址">
                        {{item.customerAddress}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户状态">
                        {{item.customerStatusName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户类型">
                        {{item.customerTypeName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="水价模式">
                        {{item.priceMode}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="付款模式">
                        {{item.payMode}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户税号">
                        {{item.customeDuty}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户银行账户">
                        {{item.customeBankAccout}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="创建日期">
                        {{item.createTime}}
                    </a-form-item>
                </a-col>
                <!-- <a-col :span="12">
                    <a-form-item label="资产状态">
                        {{item.equipmentStatus}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备状态">
                        {{item.equipmentRevstopText}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="投入运营时间">
                        {{item.equipmentOperating}}
                    </a-form-item>
                </a-col> -->
            </a-row>
        </a-form>
    </div>
     <div class="table-page-search-wrapper" v-else>
        <a-form layout="inline">
            <a-row :gutter="16">
                <a-col :span="12">
                    <a-form-item label="所属标段">
                        {{item.equipmentSection}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备类型">
                        {{item.equipmentType}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="客户地址">
                        {{item.customerAddress}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="放置位置">
                        {{item.equipmentLocation}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="供应商">
                        {{item.equipmentSupplier}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备型号">
                        {{item.equipmentModeName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备规格">
                        {{item.equipmentSpecsName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备编号">
                        {{item.equipmentSn}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备名称">
                        {{item.equipmentName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="资产类别">
                        {{item.equipmentCategory}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="资产状态">
                        {{item.equipmentStatus}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备状态">
                        {{item.equipmentRevstopText}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="投入运营时间">
                        {{item.equipmentOperating}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="购置时间">
                        {{item.equipmentPurchase}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="提前提醒时间(天)">
                        {{item.countDays}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="计划养护日期">
                        {{item.planDate}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="责任人">
                        {{item.personResponsibleName}}
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
    </div>
</template>
<script>
  import baseInformationDetail from '@/assets/js/treeCentre/modules/modules/BaseInformationDetail.js';
  export default{
      ...baseInformationDetail
  }
</script>