<!-- 安全中心-设备树-结构管理 -->
<template>
    <div class="margin12">
        <a-row type="flex" :gutter="16">
        <a-col :md="5" :sm="24">
            <structure-query-left v-model="currentLabelCode" :leftUrl="leftUrl" />
        </a-col>
        <a-col :md="24 - 5" :sm="24">
            <structure-query-right v-model="currentLabelCode" :rightUrl="rightUrl" :comments="comments" searchType="saf" />
        </a-col>
        </a-row>
    </div>
</template>

<script>
import requipmentList from "@/assets/js/treeCentre/safetyCenterTree/structureQuery.js";
export default {
    ...requipmentList,
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style> 