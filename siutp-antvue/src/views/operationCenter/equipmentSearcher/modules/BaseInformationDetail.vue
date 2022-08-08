<!-- 资产中心-资产台账-详情-基本信息详情 -->
<template>
    <div class="table-page-search-wrapper">
        <a-form layout="inline">
            <a-row :gutter="16">
                <a-col :span="12">
                    <a-form-item label="设备编号">
                        {{item.equipmentSn}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备类别">
                        {{item.equipmentCategory}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备名称">
                        {{item.equipmentName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备型号">
                        {{item.equipmentModeName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备类型">
                        {{item.equipmentType}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="放置位置">
                        {{item.equipmentLocation}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="设备规格">
                        {{item.equipmentSpecsName}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="供应商">
                        {{item.equipmentSupplier}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="所属标段">
                        {{item.equipmentSection}}
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="购置日期">
                        {{item.equipmentPurchase}}
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
            </a-row>
        </a-form>
    </div>
</template>
<script>
  import baseInformationDetail from '@/assets/js/operationCenter/equipmentSearcher/modules/BaseInformationDetail.js';
  export default{
      ...baseInformationDetail
  }
</script>