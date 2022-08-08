<template>
    <div class="page-homeIndex">
        <!--<div class="page-tabs">
            <div class="tab" v-for="(item, index) in page_tabs" :key="index" @click="$router.push({ name: item.url })" >
                <a-icon :type="item.type" class="fs20"/>{{item.name}}
            </div>
        </div>-->
        <div class="page-tabs2">
           <div v-for="(role,index1) in limited " :key="index1">
                <div v-for="(item, index) in page_tabs" :key="index">
                         <div class="index-tabs" v-show="role.name==item.name"  @click="()=>{ $router.push({ name: item.urlHttp });mouseLeave(index)}" @mouseover="mouseOver(index)" @mouseleave="mouseLeave(index)" >
                                <img :src="item.url"  />
                                <!--<img src="./images/information.png"  />-->
                                <div class="index-tabs-text">{{item.name}}</div>
                         </div>
                </div>
           </div>
        </div>
        <div class="page-map">
            <img class="map-back" v-show="show_mapBack" src="./images/back.png" />
            <div class="baidumapy" id="home-allmap"></div>
        </div>

        <div class="page-r-container"> 
            <div class="work-order" :style="{backgroundImage: 'url('+ workOrderBg +')'}">

                <p class="p-r-tit">工单信息</p>
                    <div class="work-order-info">
                    <p class="num num-b">{{workOrderResolvedInfo}}</p>
                    <p class="state">已处理</p>
                </div>
                <div class="work-order-info">
                    <p class="num num-r">{{workOrderUnResolvedInfo}}</p>
                    <p class="state">未处理</p>
                </div>
            </div>
            <div class="give-an-alarm mt10" :style="{backgroundImage: 'url('+ giveAlarmBg +')'}">
                <p class="p-r-tit">实时预警告警</p>
                <div class="alarm-list">
                    <div v-if="realTimeWarningInfo.length == 0" class="empty_icon">
                        <img src="./images/empty.png">
                        <p>暂无数据</p>
                    </div>
                    <div class="alarm-item" v-for="(item, index) in realTimeWarningInfo" :key="index">
                        <p class="alarm-tit">
                            <span class="tit nowrap-ellipsis">类型：{{item.equipmentType}}</span>
                            <span :class="'hint level'+warnLevel(item.warnLevel)">{{item.warnLevel}}</span>
                        </p>
                        <p class="alarm-des nowrap-ellipsis">地址：{{item.warnContent}}</p>
                        <p class="alarm-des nowrap-ellipsis">时间：{{item.warnTime}}</p>
                    </div>
                </div>
            </div>
            <div class="hydrology mt10" :style="{backgroundImage: 'url('+ hydrologyBg +')'}">
                <p class="p-r-tit">水文数据</p>
                <div class="tabs">
                    <div class="tab" :class="item.selected && 'selected'" @click="onSetChartTabChange(index, 'tab')"
                        v-for="(item, index) in hydrology_tabs" :key="index">{{item.name}}</div>
                </div>
                <template v-if="showCanvas">
                    <v-chart class="h-vChart" :forceFit="false" :height="220" :width="290" :series="{size: 0.01}" 
                        :data="dataSource" :padding="padding">
                        <v-coord type="rect" direction="LB" />
                        <v-tooltip />
                        <v-axis dataKey="x" :label="label" />
                        <v-axis dataKey="y" :label="label" />
                        <v-bar position="x*y" />
                    </v-chart>
                </template>
            </div>
        </div>
    </div>
</template>

<script>
    import homeIndex from '@/assets/js/dashboard/Analysis.js';
    export default{
        ...homeIndex
    }
</script>
<style scoped>
    @import '~@assets/less/dashboard/Analysis.less'

</style>


