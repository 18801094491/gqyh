<template>
    <div class="big-warnEvent">
        <img class="line1" src="./images/line1.png" alt="">
        <div class="wO-title">
            <img class="i1" src="./images/i1.png" alt="">
            <div>实时预警告警</div>
            <img class="line2" src="./images/line2.png" alt="">
        </div>
        <div class="wO-content">
            <img class="bg2" src="./images/bg2.png" alt="">
            <div class="con-poA">
                <div class="poA-t">
                    <div class="t-left">
                        <div class="img-pick">
                            <img class="e-i" src="./images/e-i1.png" alt="">
                            <div>{{level3}}</div>
                        </div>
                        <div class="des">未处理紧急告警</div>
                    </div>
                    <div class="t-center">
                        <div class="img-pick">
                            <img class="e-i" src="./images/e-i2.png" alt="">
                            <div>{{level2}}</div>
                        </div>
                        <div class="des">未处理严重告警</div>
                    </div>
                    <div class="t-right">
                        <div class="img-pick">
                            <img class="e-i" src="./images/e-i3.png" alt="">
                            <div>{{level1}}</div>
                        </div>
                        <div class="des">未处理一般告警</div>
                    </div>
                </div>
                <div class="poA-b">
                    <div class="tit">
                        <div class="tab1">警告名称</div>
                        <div class="tab2">所属资产</div>
                        <div class="tab3">告警时间</div>
                        <div class="tab4">告警状态</div>
                        <div class="tab5">告警等级</div>
                    </div>
                    <div class="list">
                        <div class="item" v-for="(item, index) in warnList" :key="index">
                            <div class="tab1 ellipsis">{{item.warnName}}</div>
                            <div class="tab2 ellipsis">{{item.equ}}</div>
                            <div class="tab3 ellipsis">{{item.warnTime}}</div>
                            <div class="tab4 ellipsis">{{item.warnStatus}}</div>
                            <div :class="'tab5 ellipsis level' + item.warnLevelCode">{{item.warnLevel === '警告' ? '一般' : item.warnLevel}}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { axios } from '@/utils/request';
    import {ACCESS_TOKEN} from "@/store/mutation-types"
    export default {
        name: "warnEvent",
        data(){
            return {
                getCompleteNum: '',
                getAllotNum: '',
                warnList: [],
                level3: 0,
                level2: 0,
                level1: 0,
            }
        },
        created() {
            this.getWarnEventData();
        },
        mounted() {
            
            
        },
        methods: {
            getWarnEventData() {
                // axios.get('business/warn/warnNum', {params: {pageNo: 1, pageSize: 10}}).then(res => {
                // })
                axios.get('business/warn/undealData', {params: {pageNo: 1, pageSize: 100}}).then(res => {
                    let warnList = res.result || [];
                    warnList.forEach(item => {
                        if (item.warnLevelCode == 3) {
                            if (item.num) this.level3 = item.num || 0
                            else this.level3 += 1
                        } else if (item.warnLevelCode == 2) {
                            if (item.num) this.level2 = item.num || 0
                            else this.level2 += 1
                        } else if (item.warnLevelCode == 1) {
                            if (item.num) this.level1 = item.num || 0
                            else this.level1 += 1
                        }
                    })
                    this.warnList = warnList;
                })
            },
        },

    }
</script>

<style lang="less" scoped>
    .big-warnEvent {
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
        .wO-content {
            position: relative;
            .bg2 {
                width: 848px;
                height: 380px;
            }
            .con-poA {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                .poA-t {
                    display: flex;
                    justify-content: space-between;
                    padding: 33px 92px 30px 125px;
                    .img-pick {
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        position: relative;
                        width: 91px;
                        height: 99px;
                        font-size: 30px;
                        margin: 0 auto;
                        .e-i {
                            position: absolute;
                            z-index: 0;
                            width: 100%;
                            height: 100%;
                        }
                        div {
                            position: relative;
                            z-index: 1;
                        }
                    }
                    .des {
                        line-height: 1.1;
                        font-size: 18px;
                    }
                }
                .poA-b {
                    padding: 0 30px 0 58px;
                    .tab1 {
                        flex-shrink: 0;
                        width: 186px;
                        padding: 0 18px;
                    }
                    .tab2 {
                        flex-shrink: 0;
                        width: 138px;
                        padding-right: 10px;
                    }
                    .tab3 {
                        flex-shrink: 0;
                        width: 208px;
                        padding-right: 10px;
                    }
                    .tab4 {
                        flex-shrink: 0;
                        width: 128px;
                        padding-right: 10px;
                    }
                    .tab5 {
                        flex-grow: 1;
                        text-align: center;
                        padding-right: 4px;
                    }
                    .tit {
                        display: flex;
                        width: 100%;
                        height: 43px;
                        line-height: 43px;
                        font-size: 16px;
                        color: #CDE9C8;
                        background: #021D01;
                    }
                    .list {
                        height: 131px;
                        overflow: auto;
                        color: #CDE9C8;
                        font-size: 14px;
                        .item {
                            width: 100%;
                            height: 32px;
                            line-height: 36px;
                            background: rgba(138, 174, 136, 0.1);
                            margin-top: 12px;
                        }
                        .level1 {
                            color: #28D9BE;
                        }
                        .level2 {
                            color: #FFD619;
                        }
                        .level3 {
                            color: #FF9F2D;
                        }
                    }
                }
            }
        }
    }
.ellipsis {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.wO-content .list::-webkit-scrollbar{
  width: 3px;
}
.wO-content .list::-webkit-scrollbar-thumb{
      border-radius: 4px;
      background: #293d42;
}
</style>