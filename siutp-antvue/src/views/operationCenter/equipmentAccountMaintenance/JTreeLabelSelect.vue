<template>
  <a-tree-select
    allowClear
    labelInValue
    style="width: 100%"
    :disabled="disabled"
    :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
    :placeholder="placeholder"
    :loadData="asyncLoadTreeData"
    :value="treeValue"
    :treeData="treeData"
    :multiple="multiple"
    @change="onChange"
    @search="onSearch">
  </a-tree-select>
</template>
<script>
import jTreeLabelSelect from '@/assets/js/operationCenter/equipmentAccountMaintenance/JTreeLabelSelect.js'
	export default {
        ...jTreeLabelSelect
    }
</script>
