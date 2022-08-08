<template>
    <div id="domeBox" class="margin12 content over_hide" ref="replayModal">
        
        <div class="allmapBox">
            <div class="hierarchyBox">
                <a-dropdown class="mr30" :getPopupContainer="getPopupContainer">
                    <a class="ant-dropdown-link ant-dropdown-open" @click="e => e.preventDefault()">图例 <a-icon type="down" /></a>
                    <a-menu slot="overlay">
                        <!-- <a-menu-item class="allSelect" @click="allSelect">全选</a-menu-item> -->
                        <a-menu-item class="stateSelectionBox">
                            <div class="allSelect" @click="allSelect">全选</div>
                            <ul class="stateSelectionUlBox">
                                <li :class="{active:item.dataShow==1}"  data-name="YLB" v-for="(item,index) in gisDashboardGisModelNavList" :key="index">
                                    <a :data-name="item.modelType" href="javascript:;" class="zhezhao"  @click="selectLi"></a>
                                    <img alt="" :src="item.modelThumb" data-name="YLB">
                                    <p data-name="YLB">{{item.navName}}</p>
                                </li>
                            </ul>
                        </a-menu-item>
                    </a-menu>
                </a-dropdown>
                <a class="mr30" @click="openBig" v-if="!isScreen" @keyup.122="disableESC($event)">全屏</a>
                <a class="mr30" @click="closeBig" v-else>退出全屏</a>
                <a class="mr30" @click="enlarge">放大</a>
                <a  @click="narrow">缩小</a>
            </div>
            <div class="baidumapy" id="allmap"></div>
            <a-modal title="设备信息详情" class="domeBox" :getContainer="()=>$refs.replayModal" :visible="detailsVisible" width="500px" @ok="informationOk" @cancel="informationCancel">
                <div class="informationTopBox"></div>
                <div class="informationBodyBox">
                    <div class="informationTopBodyBox">
                        <div class="clearfix">
                            <div class="informationTopBodyLeftBox">
                                <div class="informationImgBox" v-if="assetImg">
									<img :src="imgUrl+assetImg" width="100%" height="100%"/>
                                </div>
                            </div>
                            <div class="informationTopBodyRightBox">
                                <div v-if="attributeList.length>0" class="informationTopBodyRightDivBox clearfix" v-for="(item,index) in attributeList" :key="index">
                                    <label>{{item.attributeCn}}:</label>
                                    <span>{{item.attributeVal}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>设备编号:</label>
                                    <span>{{assetCoding}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>设备名称:</label>
                                    <span>{{assetName}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>生产厂家:</label>
                                    <span>{{manufacturer}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>设备型号:</label>
                                    <span>{{informationEquipmentType}}</span>
                                </div>
                                <div class="informationTopBodyRightDivBox clearfix">
                                    <label>放置位置:</label>
                                    <span>{{assetLocation}}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </a-modal>
            <!-- 原来的 -->
            <!-- <div class="stateSelectionBox">
                <div class="openOrclose active" @click="openOrclose">收起</div>
                <div class="allSelect" @click="allSelect">全选</div>
                <ul class="stateSelectionUlBox">
                    <li :class="{active:item.dataShow==1}"  data-name="YLB" v-for="(item,index) in gisDashboardGisModelNavList" :key="index">
                        <a :data-name="item.modelType" href="javascript:;" class="zhezhao"  @click="selectLi"></a>
                        <img alt="" :src="item.modelThumb" data-name="YLB">
                        <p data-name="YLB">{{item.navName}}</p>
                    </li>
                </ul>
            </div> -->
        </div>
        <!--监控播放器-->
        <div class="monitorBox" :style="{backgroundImage: 'url('+ monitorBg +')'}" v-if="isShowMonitor">
            <div class="title">
                <div>{{equipLocation}}</div>
                <div class="close" @click="closeMonitor">
                    <img src="./images/close.png" />
                </div>
            </div>
            <div class="monitorBoxCon">
                <div id="monitorPage">
                    <div id="playWnd" class="playWnd" v-if="isShowMonitor"></div>
                </div>
            </div>
        </div>
        <div class="mapRight_box">
            <div @click="onShowAlarmChange">
                <img src='./images/warn1.png'/>
                <div>设备告警情况</div>
            </div>
            <div @click="onShowIconsChange">
                <img src='./images/warn1.png'/>
                <div>设备图例</div>
            </div>
        </div>
        <div class="mapRightBox1 fr" v-show="isShowAlarm">
        	<!--右侧弹框切换按钮-->
        	
            <!--<div class="toggle">
                <div class="toggleBg"></div>-->
                
                <!-- <a-icon v-if="!toggle" type="left" />
                <a-icon v-if="toggle" type="right" /> -->
                
                <!--<i v-if="!toggle" class="left"></i>
                <i v-if="toggle" class="right"></i>
            </div>-->
             
            
            <div class="mapRightTableBox mapRightTableBox1 alarmCon">
                <div class="mapRightTableBG"></div>
                    
                <div class="mapRightTableBodyBox">
                     <div class="h4" ><p class="title">告警事件</p> 
                        <div class="moreCon" @click="onHideAlarmChange">
                            <img class="showIcon" src="./images/delete.png" alt="">
                        </div>
                	</div>
                    <div class="h41 warm-style" >
                        <div class="title1">告警事件</div> 
                        <div class="moreCon1">
                            <router-link :to="{name:'alarmManagement-businessWarnList'}">  更多</router-link>
                        </div>
                	</div>
                    <div class="scrollBox" id="warnEvent">
						<div class="scroll-box">
                            <table class="tab-scroll">
                                <tbody></tbody>
                            </table>
                            <table class="tab-scroll2">
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>                
            </div>
            <!--监控窗口-->
            <!--<div class="mapRightZXBox">
                <div class="mapRightTableBG"></div>
                <div class="mapRightZXBodyBox">
                    <div class="h4"><p class="title">监控窗口</p><p class="upDownStyle"></p></div>
                    
                </div>
            </div>-->
        </div>
         <div class="mapRightBox2 fr" v-show="isShowIcons">
            <div class="mapRightZXBox">
                <div class="mapRightTableBG"></div>
                
                <div class="mapRightZXBodyBox">
                    <div class="h4"><p class="title">图例</p>
                        <div class="moreCon mr15" @click="onHideIconsChange">
                            <img class="upDownClass" src="./images/delete.png"/>
                        </div>
                    </div>
                    <div class="tuli clearfix" id="legendId">
                        <div class="legendBox" v-for="(item,index) in warnQueryAllIconList" :key="index">
                            <img :src="item.url" alt="">
                            <p>{{item.name}}</p>
                        </div>
                    </div>
                </div>
                
            </div>

        </div>
        <a-modal
            title="告警事件"
            :visible="alarmEventVisible"
            width="700px"
            @cancel="alarmEventonCancel"
            class="alarmBox"
            :getContainer="()=>$refs.replayModal"
        >

            <div class="table-page-search-wrapper">
                <a-form layout="inline">
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="事件编号">
                                <p>{{warnSn}}</p>

                            </a-form-item>
                        </a-col> 
                    
                    </a-row>
                    <a-row :gutter="24">                        
                        <a-col :span="24">
                            <a-form-item label="告警名称">
                                <p>{{warnName}}</p>

                            </a-form-item>
                        </a-col>     
                    </a-row>
                    
                    <a-row :gutter="24">                        
                        <a-col :span="24">
                            <a-form-item label="告警内容">
                                <p>{{warnContent}}</p>

                            </a-form-item>
                        </a-col>     
                    </a-row>
                    <!--<div class="clearfix"  style="margin-bottom:1em">
                        <label style="width:77px; color: rgba(0, 0, 0, 0.85); font-size:14px;" class="fl">告警内容 :</label>
                        <span style="width:460px; color: rgba(0, 0, 0, 0.65); font-size:14px;" class="fl">{{warnContent}}</span>
                    </div>-->
                    
                    
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="所属资产">
                                <p>{{equipmentType}}</p>

                            </a-form-item>
                        </a-col>                        
                    </a-row>
                    <a-row>
                        <a-col :span="12">
                            <a-form-item label="告警等级">
                                <p>{{warnLevel}}</p>

                            </a-form-item>
                        </a-col>
                    </a-row>
                    
                    <a-row class="formHeight">	
                        <a-col :span="24">
                            <a-form-item label="规则详情">
                                <p class="ruleHeight">{{ruleContent}}</p>

                            </a-form-item>
                        </a-col>
                    </a-row>
                    <!--<div class="clearfix" style="margin-bottom:1em">
                        <label  style="width:77px;color: rgba(0, 0, 0, 0.85); font-size:14px;" class="fl">规则详情 :</label>
                        <span style="width:460px; color: rgba(0, 0, 0, 0.65); font-size:14px;" class="fl">{{ruleContent}}</span>
                    </div>-->
                    
                    <a-row :gutter="24">
                        <a-col :span="12">
                            <a-form-item label="告警时间">
                                <p>{{warnTime}}</p>

                            </a-form-item>
                        </a-col> 
                        
                    </a-row>

                </a-form>
            </div>

            <template slot="footer">
                <a-button v-if="this.alarmEventType==0||this.alarmEventType==null" type="primary" @click="Know">知悉</a-button>
                <a-button v-if="this.alarmEventType==0||this.alarmEventType==null" type="primary" @click="relieve" class="ml20">解除</a-button>
                <a-button v-if="this.alarmEventType==1&&this.alarmEventType2!=2" type="primary" @click="handle">处理</a-button>
            </template>
        </a-modal>
        <a-modal
            title="事件处置"
            :width="450"
            :visible="alarmHandlingvisible"
            :getContainer="()=>$refs.replayModal"
            @ok="alarmHandlingOk"
            @cancel="alarmHandlingCancel"
            cancelText="关闭">
            <a-form>
            
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="备注">
                    <a-textarea placeholder="请输入备注" :rows="4" v-model="description" />
                
                </a-form-item>
            </a-form>
        </a-modal>
        <a-modal
            title="事件关闭"
            :width="450"
            :visible="turnOffAlarmvisible"
            :getContainer="()=>$refs.replayModal"
            @ok="turnOffAlarmOk"
            @cancel="turnOffAlarmCancel"
            cancelText="关闭">
            <a-form>
            
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="备注">
                <a-textarea placeholder="请输入备注" :rows="4" v-model="closeDescription" />
            
            </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>
<script>
import index from '@/assets/js/operationCenter/mapIndex/index.js';
export default{
    ...index
}
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
<style lang="less" scoped>
    .warm-style{
        display: flex;
        justify-content: space-between;
        padding: 0 16px 0 10px;
    }
    // 地图气泡信息框
    /deep/ .infoWin-pick {
        .line1 {
            position: absolute;
            width: 139px;
            height: 1px;
            border:  1px dashed rgb(45,146,236);
            box-sizing: border-box;
        }
        .line2 {
            position: absolute;
            width: 60px;
            height: 1px;
            border: 1px dashed rgb(45,146,236);
            box-sizing: border-box;
        }
        .infoWin {
            padding: 10px;
            background: rgba(255,255,255,0.75);
            border-radius: 10px;
            border: 1px solid #2D92EC;
        }
    }
    /deep/ .marker-lt-parent {  //左上
        transform: translate(calc(-100% - 124px), calc(-100% - 75px)) !important;
        .line1 {
            bottom: 0;
            right: 0;
            transform: translate(164px, 20px) rotate(240deg);
        }
        .line2 {
            bottom: 0;
            right: -0;
            transform: translate(60px, -41px);
        }
    }
    /deep/ .marker-rt-parent { //右上
        transform: translate(156px, calc(-100% - 70px)) !important;
        .line1 {
            left: 0;
            bottom: 0;
            transform: translate(-164px, 20px) rotate(120deg);
        }
        .line2 {
            bottom: 0;
            left: 0;
            transform: translate(-60px, -44px);
        }
    }
    /deep/ .marker-rb-parent { //右下
        transform: translate(156px, 100px) !important;
        .line1 {
            top: 0;
            left: 0;
            transform: translate(-165px, -18px) rotate(240deg);
        }
        .line2 {
            top: 0;
            left: 0;
            transform: translate(-60px, 44px);
        }
    }
    /deep/ .marker-lb-parent { //左下
        transform: translate(calc(-100% - 125px), 90px) !important;
        .line1 {
            top: 0;
            right: 0;
            transform: translate(165px, -10px) rotate(120deg);
        }
        .line2 {
            top: 0;
            right: 0;
            transform: translate(60px, 51px);
        }
    }
    /deep/ .infoWin-pick {
        position: relative;
        .bg {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
        .item {
            font-size: 14px;
            justify-content: start;
            &:not(:last-child) {
                padding-bottom: 4px;
            }
            .name {
                color: #2D92EC;
                white-space:nowrap
                
            }
            .value {
                color: #2D92EC;
                white-space:nowrap
            }
        }
    }
</style>
