<template>
    <a-drawer
            title="数据项"
            :width="1000"
            @close="handleCancel"
            :visible="visible"
            :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
    >
        <div class="table-page-search-wrapper">
            <div class="table-operator" style="border-top: 5px">
                <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
            </div>
            <a-table
                    style="margin-top: 20px;"
                    :columns="columns"
                    bordered
                    size="middle"
                    :pagination="false"
                    :dataSource="dataSource"
                    :loading="loading"
            >
                <span slot="dataType" slot-scope="text,record">{{ record.dataType ? dataTypeList[record.dataType].title : '' }}</span>
                <span slot="action" slot-scope="text, record">
          <a @click="handleEdits(record,'edit')">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:" @click="handleEdits(record,'detail')">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:" @click="handleEdits(record,'addSub')">添加子数据项</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDel(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
                <!-- 字符串超长截取省略号显示 -->
                <span slot="url" slot-scope="text">
          <j-ellipsis :value="text" :length="25"/>
        </span>
                <!-- 字符串超长截取省略号显示-->
                <span slot="component" slot-scope="text">
          <j-ellipsis :value="text"/>
        </span>
            </a-table>
            <div
                    :style="{position: 'absolute',left: 0,bottom: 0,width: '100%',borderTop: '1px solid #e9e9e9',padding: '10px 16px',background: '#fff',textAlign: 'right'}"
            >
                <a-button :style="{marginRight: '8px'}" @click="handleCancel">关闭</a-button>
            </div>

            <!--新增数据项-->
            <add-data-item-modal
                    ref="modalForm"
                    @submitSuccess="submitSuccess"
                    :selectRow="selectRow"></add-data-item-modal>
        </div>
    </a-drawer>
</template>

<script>
    import index from '@assets/js/workOrderManagement/workTemp/modules/dataItemList.js';
    export default {
        ...index
    }
</script>
<style scoped>
    @import "~@assets/less/common.less";
</style>
