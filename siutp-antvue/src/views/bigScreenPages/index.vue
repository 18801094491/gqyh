<template>
    <div class="bigScreenPages" v-show="showPage">
        <div class="page-top">
            <img src="../../components/bigScreenPages/images/top.png" mode="" />
            <div class="top-text">四川绵阳木龙河水质监测平台</div>
        </div>
        <img class="page-back" v-show="back" src="../../components/bigScreenPages/images/back.png" alt="返回" @click="pageBack">
        <img class="page-back" v-show="backHover" src="../../components/bigScreenPages/images/backHover.png" alt="返回" @click="pageBack">
        <div class="page-content">
            <div class="page-left">
                <img class="line1" src="../../components/bigScreenPages/images/line1.png" alt="">
                <div class="wO-title">
                    <img class="i1" src="../../components/bigScreenPages/images/i1.png" alt="">
                    <div>重点位置监控</div>
                    <img class="line2" src="../../components/bigScreenPages/images/line2.png" alt="">
                </div>
                <div class="monitorThree">
                    <img class="bg1" src="../../components/bigScreenPages/images/bg1.png" alt="">
                    <MonitorThree v-if="showPage" class="poa" :bodyScale="bodyScale" />
                </div>
                <WorkOrder />
            </div>
            <div class="page-center">
                <Map />
            </div>
            <div class="page-right">
                <WarnEvent />
                <DataMonitor />
            </div>
        </div>
    </div>
</template>

<script>
    // import MonitorComponents from "@comp/bigScreenPages/MonitorComponent";
    import Map from "@comp/bigScreenPages/Map";
    import MonitorThree from "@comp/bigScreenPages/MonitorThree";
    import WorkOrder from "@comp/bigScreenPages/WorkOrder";
    import WarnEvent from "@comp/bigScreenPages/WarnEvent";
    import DataMonitor from "@comp/bigScreenPages/DataMonitor";
    export default {
        name: "bigScreenPages",
        components: {
            Map,
            MonitorThree,
            WorkOrder,
            WarnEvent,
            DataMonitor,
        },
        data(){
            return{
                showPage: false,
                bodyScale: null,
                back: true,
                backHover: false,
                timer: null,
                nowWeek: '',
                nowDate: '',
            }
        },
        created() {
            this.bodyScale = document.querySelector('body').offsetWidth/3200
        },
        mounted() {
            this.bodyScale = document.querySelector('body').offsetWidth/3200
            document.querySelector('.bigScreenPages').style.transform = `scale(${this.bodyScale})`
            document.querySelector('.bigScreenPages').style.transformOrigin = '0 0';

            setTimeout(() => {
               this.showPage = true;
               this.$nextTick(() => {
                   $(".page-back").hover(() => {
                       this.back = false;
                       this.backHover = true;
                   },() => {
                       this.back = true;
                       this.backHover = false;
                   });
               })
            }, 500);
        },
        methods:{
            pageBack() {
                history.back()
            },
        },
        destroyed() {
            clearInterval(this.timer)
        }
    }
</script>

<style lang="less">
.bigScreenPages {
    position: relative;
    width: 3200px;
    height: 1200px;
    background: #000000;
    color: #ffffff;
    overflow: hidden;
    .page-top {
        position: relative;
        img {
            width: 100%;
            height: 70px;
        }
        div {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-size: 36px;
            font-weight: normal;
            color: #FFFFFF;
        }
    }
    .page-back {
        position: absolute;
        top: 15px;
        right: 30px;
        width: 80px;
        z-index: 1;
    }
    .page-content {
        position: relative;
        display: flex;
        height: calc(100% - 70px);
        .page-left {
            position: relative;
            z-index: 2;
            width: 916px;
            // height: calc(100% - 76px);
            padding: 60px 0 102px 65px;
            height: 100%;
            background-image: linear-gradient(90deg, black 44%, rgba(42, 42, 42, 0));
            .line1 {
                display: block;
                width: 848px;
            }
            .wO-title {
                display: flex;
                align-items: center;
                .i1 {
                    width: 54px;
                    height: 60px;
                    margin-left: -10px;
                }
                div {
                    flex-shrink: 0;
                    font-size: 28px;
                    color: #11FFAB;
                    margin-right: 18px;
                }
                .line2 {
                    width: 592px;
                    height: 8px;
                }
            }
            .monitorThree {
                position: relative;
                width: 848px;
                height: 380px;
                .bg1 {
                    width: 848px;
                    height: 380px;
                }
            }
        }
        .page-center {
            width:  1370px;
        }
        .page-right {
            position: relative;
            z-index: 2;
            width: 916px;
            // height: calc(100% - 76px);
            padding: 60px 70px 0 0;
            height: 100%;
            background-image: linear-gradient(-90deg, black 44%, rgba(42, 42, 42, 0));
        }
    }
}
</style>