<!--监控 视频-->
<template>
    <div id="monitorThree">
        <div class="monitor-pick box1">
            <monitor-components :id="idArr.monitorName1" :bodyScale="bodyScale" :monitorIndex="monitorIndex.index1"></monitor-components>
        </div>
        <div class="monitor-pick box2">
            <monitor-components :id="idArr.monitorName2" :bodyScale="bodyScale" :monitorIndex="monitorIndex.index2"></monitor-components>
        </div>
        <div class="monitor-pick box3">
            <monitor-components :id="idArr.monitorName3" :bodyScale="bodyScale" :monitorIndex="monitorIndex.index3"></monitor-components>
        </div>
    </div>
</template>
 
<script>
    import MonitorComponents from "@comp/bigScreenPages/MonitorComponent";
    export default {
        name: "monitorThree",
        components: { MonitorComponents },
        props: ['bodyScale'],
        data(){
            return{
                idArr : {
                    monitorName1: 'monitor1',
                    monitorName2: 'monitor2',
                    monitorName3: 'monitor3',
                },
                monitorIndex: {
                    index1: 0,
                    index2: 1,
                    index3: 2,
                },
            }
        },
        methods: {
        },
        mounted() {
        },
    }
</script>

<style scoped>
    body{background: #000000;}
    #monitorThree{
        position: absolute;
        top: 31px;
        left: 25px;
        width: 760px;
        height: 318px;
    }
    .monitor-pick{
    }
    .monitor-pick.box1 {
        position: absolute;
        left: 0;
        width: 500px;
        height: 316px;
    }
    .monitor-pick.box2, 
    .monitor-pick.box3 {
        position: absolute;
        right: 0;
        width: 241px;
        height: 146px;
    }
    .monitor-pick.box2 {
        top: 0;
    }
    .monitor-pick.box3 {
        bottom: 0;
    }
</style>