<!-- 运营中心-班组班次-我的排班 -->
<template>
    <div class="margin12">
        <a-card :bordered="false">
            <a-button type="primary" icon="download" style="margin-left:30px;" @click="handleExportXls('班组排班')">个人排班导出</a-button>

            <a-calendar @panelChange="onPanelChange" @select="onSelect" @change="dateChange">
                <ul class="events" slot="dateCellRender" slot-scope="value">
                    <li v-if="value.format('YYYY-MM-DD')==item.dutyDay"  v-for="item in listData" :key="item.content">
                        <p v-for="(item2,index2) in item.list" :key="index2">
                            {{item2.startTime}}-{{item2.overTime}}
                        </p>
                    </li>
                </ul>

            </a-calendar>
        </a-card>
    </div>
</template>
<script>
    import index from '@assets/js/operationCenter/personalScheduling'
    export default {
        ...index
    };
</script>
<style scoped>
    .events {
        list-style: none;
        margin: 0;
        padding: 0;
    }
    .events .ant-badge-status {
        overflow: hidden;
        white-space: nowrap;
        width: 100%;
        text-overflow: ellipsis;
        font-size: 12px;
    }
    .notes-month {
        text-align: center;
        font-size: 28px;
    }
    .notes-month section {
        font-size: 28px;
    }

</style>
<style>
    .ant-fullcalendar-fullscreen .ant-fullcalendar-header .ant-radio-group{
        display: none;
    }
</style>