<!-- 设备树-结构管理-左侧树状图 -->
<template>
    <a-card :loading="cardLoading" :bordered="false" style="height: 100%">
        <a-spin :spinning="loading">
            <a-tree
                v-model="checkedKeys"
                checkStrictly
                checkable
                :showLine="false"
                :loadData="onLoadData"
                :default-expanded-keys="defaultKeys"
                :treeData="treeDataSource"
                @check="handleTreeCheck"
                @select="handleTreeSelect"/>
        </a-spin>
    </a-card>
</template>

<script>
import structureQueryLeft from "@/assets/js/treeCentre/modules/structureQuery/StructureQueryLeft.js";
export default {
    ...structureQueryLeft,
};
</script>
<style scoped>
.ant-card {
    max-height: 700px !important;
    overflow: auto !important;
}
</style>
<style>
.attr .ant-tree-title {
    color: orange !important;
}
</style>