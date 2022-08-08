<template>
    <div>
        <a-drawer
                :title="title"
                :width="650"
                @close="closeModal"
                :visible="modalVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="pagination"
                    :loading="loading"
                    @change="tableChange"
                    :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            >
		<span slot="action" slot-scope="text, record">
            <!-- <a @click="handleEdit(record)" v-has="'contract:edit'">编辑</a> -->
            <a @click="details(record, 'edit')">编辑</a>
            <!-- <a-divider type="vertical" v-has="'contract:edit'" /> -->
            <a-divider type="vertical"/>
            <a @click="details(record, 'show')">详情</a>
          </span>
            </a-table>
            <div :style="{position: 'absolute',left: 0,bottom: 0,width: '100%',borderTop: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',textAlign: 'right'}">
                <a-button :style="{marginRight: '8px'}" @click="closeModal">关闭</a-button>
                <a-button @click="handleOk" type="primary">提交</a-button>
            </div>
        </a-drawer>
    </div>
</template>
<script>
    import index from '@assets/js/workOrderManagement/workPlan/InspectionLineManagement/SelectPointList.js';

    export default {
        ...index
    }
</script>

<style>
</style>
