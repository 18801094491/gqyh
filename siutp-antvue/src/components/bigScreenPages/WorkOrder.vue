<template>
    <div class="big-workOrder">
        <div class="wO-title">
            <img class="i1" src="./images/i1.png" alt="">
            <div>工单信息</div>
            <img class="line2" src="./images/line2.png" alt="">
        </div>
        <div class="wO-content">
            <img class="bg3" src="./images/bg3.png" alt="">
            <div class="con-poA">
                <div class="poA-t">
                    <div class="t-left">
                        <img class="w-i" src="./images/w-g.png" alt="">
                        <div class="info">
                            <div class="num">{{getCompleteNum}}/{{workList.length}}</div>
                            <div class="des">已处理</div>
                        </div>
                    </div>
                    <div class="t-right">
                        <img class="w-i" src="./images/w-y.png" alt="">
                        <div class="info">
                            <div class="num">{{getAllotNum}}/{{workList.length}}</div>
                            <div class="des">待处理</div>
                        </div>
                    </div>
                </div>
                <div class="poA-b">
                    <div class="tit">
                        <div class="tab1">工单名称</div>
                        <div class="tab2">工单类型</div>
                        <div class="tab3">处理人</div>
                        <div class="tab4">实施时间</div>
                        <div class="tab5">处理状态</div>
                    </div>
                    <div class="list">
                        <div class="item" v-for="(item, index) in workList" :key="index">
                            <div class="tab1 ellipsis">{{item.name}}</div>
                            <div class="tab2 ellipsis">{{item.typeDes}}</div>
                            <div class="tab3 ellipsis">{{item.leaderName}}</div>
                            <div class="tab4 ellipsis">{{item.startDate}}</div>
                            <div class="tab5 ellipsis">{{item.statusDes}}</div>
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
        name: "workOrder",
        data(){
            return {
                getCompleteNum: 0,
                getAllotNum: 0,
                workList: [],
            }
        },
        created() {
            this.getWorkOrderData();
        },
        mounted() {
            
            
        },
        methods: {
            getWorkOrderData() {
                axios.get('worklist/workList/getCompleteNum', {}).then(res => { 
                    this.getCompleteNum = res.data;
                })
                axios.get('worklist/workList/getAllotNum', {}).then(res => {
                    this.getAllotNum = res.data || 0;
                })
                axios.get('worklist/workList/rdp/list', {}).then(res => {
                    this.workList = res.data || 0;
                })
            },
        },

    }
</script>

<style lang="less" scoped>
.big-workOrder {
    .wO-title {
        display: flex;
        align-items: center;
        padding-top: 30px;
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
        .bg3 {
            width: 100%;
            height: 436px;
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
                padding: 43px 113px 30px;
                .t-left {
                    display: flex;
                    .num {
                        color: #11FFAB;
                    }
                }
                .t-right {
                    display: flex;
                    .num {
                        color: #FFF666;
                    }
                }
                .w-i {
                    width: 90px;
                    height: 84px;
                }
                .info {
                    width: 210px;
                    padding-left: 15px;
                }
                .num {
                    line-height: 1.2;
                    font-size: 34px;
                    padding-top: 10px;
                }
                .des {
                    font-size: 18px;
                }
            }
            .poA-b {
                padding: 0 52px 0 27px;
                .tab1 {
                    flex-shrink: 0;
                    width: 300px;
                    padding: 0 18px;
                }
                .tab2 {
                    flex-shrink: 0;
                    width: 120px;
                    padding-right: 10px;
                }
                .tab3 {
                    flex-shrink: 0;
                    width: 120px;
                    padding-right: 10px;
                }
                .tab4 {
                    flex-shrink: 0;
                    width: 130px;
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
                    color: #EDFFEE;
                    background: #021D01;
                }
                .list {
                    height: 200px;
                    overflow: auto;
                    color: #D7FFD9;
                    font-size: 14px;
                    .item {
                        width: 100%;
                        height: 36px;
                        line-height: 36px;
                        background: #001713;
                        margin-top: 14px;
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