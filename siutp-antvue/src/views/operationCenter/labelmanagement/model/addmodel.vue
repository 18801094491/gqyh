<!-- 资产中心-资产管理-台账树管理-新增、编辑 -->
<template>
    <a-modal
            :title="title"
            :width="width"
            :visible="visible"
            :confirmLoading="confirmLoading"
            @ok="handleOk"
            @cancel="handleCancel"
            :destroyOnClose="true"
            cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">

                <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <j-tree-region-select
                            :disabled="disTree"
                            ref="treeSelect"
                            placeholder="请选择父级节点"
                            v-decorator="['parentId', validatorRules.parentId]"
                            dict="opt_label,label_name,id"
                            pidField="parent_id"
                            pidValue="0"
                            condition="{&quot;del_flag&quot;:0}">
                    </j-tree-region-select>
                </a-form-item>

                <a-form-item label="区域名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input
                            v-decorator="[ 'labelName', validatorRules.labelName]"
                            placeholder="请输入区域名称"></a-input>
                </a-form-item>
            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>
import addmodel from '@/assets/js/operationCenter/labelmanagement/model/addmodel.js'
export default{
    ...addmodel
}

</script>