<template>
  <a-modal
    class="montior-modal"
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :footer="null"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" class="textal-left">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="工单名称"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{monitorData.name}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="班组"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{monitorData.teamName}}</span>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24" v-if="fromPage === 'inspection'">
          <a-col :span="12">
            <a-form-item label="区域"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{monitorData.areaName}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="路线"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{monitorData.routeName}}</span>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="状态"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{monitorData.statusDes}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="时间"  :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{monitorData.startDate +'-'+ monitorData.overDate}}</span>
            </a-form-item>
          </a-col>
        </a-row>

        <div v-if="fromPage=== 'maintenance' " class="monitorModal-allmap" :id="'monitorModal-allmap-' + fromPage"></div>
        <div v-else-if="fromPage=== 'problem' " class="monitorModal-allmap" :id="'monitorModal-allmap-' + fromPage"></div>
        <div v-else-if="fromPage=== 'inspection' " class="monitorModal-allmap" :id="'monitorModal-allmap-' + fromPage"></div>
      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>
  import { getLocation } from '@/api/workOrderManagement/modules/monitorModal.js'
  export default {
    name: "MonitorModal",
    props: ['monitorData'],
    data () {
      return {
        fromPage: '',
        title: "",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 19 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {},
        timer: null,
        userLocationList: [],
        matterList: null,
        mapIcon: require('@/assets/images/mapIndex/locationB.png'),
      }
    },
    created () {
    },
    mounted() {
    },
    methods: {
      handleCancel () {
        this.$emit('close');
        this.visible = false;
        clearInterval(this.timer)
        this.timer = null
      },

      showModal(id, fromPage) {
        console.log(id, fromPage)
        this.visible = true;
        this.fromPage = fromPage;
        this.$nextTick(() => {
          setTimeout(() => { 
            this.newBMap();
            this.getLocation(id);
            this.timer = setInterval(() => {
              this.getLocation(id);
            }, 10000);
          })
        })
      },
      
      getLocation(id) { //获取工单成员定位
        getLocation(id, this, (res) => {
          this.userLocationList = res;
          if (this.Map) {
            this.Map.clearOverlays();
            console.log(new Date())
            let opts = {
                width: 0,
                title: '提示',
                enableMessage: true, //设置允许信息窗发送短息
                message: ""
            };
            res.forEach((item, index) => { //遍历标注
              let myIcon = new BMap.Icon(this.mapIcon, new BMap.Size(39, 25)); //标注样式
              let point = new BMap.Point( item.longitude, item.latitude );
              let marker = new BMap.Marker( point,{ icon: myIcon } );
              this.Map.addOverlay(marker)
              var sContent =
                  `<div class="introduceBox">
                    <div class="introduceDivBox setwidth">
                      <label>姓名：</label>
                      <span>${item.realname}</span>
                    </div>
                    <div class="introduceDivBox setwidth">
                      <label>时间：</label>
                      <span>${item.dateTimeFormat}</span>
                    </div>
                  </div>`
              let infoWindow = new BMap.InfoWindow(sContent, opts);  // 创建信息窗口
              marker.addEventListener("click", () => {
                this.Map.openInfoWindow(infoWindow, point); //显示窗口
              });
            });
          }
        });
      },

      newBMap() { //设置地图容器
        let markers = [];
        let result = this.monitorData.matterList || [];
        let mapId = 'monitorModal-allmap-' + this.fromPage;
        let opts = {
            width: 0,
            title: '提示',
            enableMessage: true, //设置允许信息窗发送短息
            message: ""
        };
        let Map = new BMap.Map(mapId, {
            enableDblclickZoom: false,
             minZoom:12.4,
            maxZoom:15,
            displayOptions: {
                building: false
            }
        });
        console.log(result)
        if (!result.length) return;
        Map.centerAndZoom(new BMap.Point(result[0].matterLongitude, result[0].matterLatitude), 12); //创建地图
        Map.enableScrollWheelZoom(true); //开启缩放
        result.forEach((item, index) => { //遍历标注
          let point = new BMap.Point( item.matterLongitude, item.matterLatitude );
          let marker = new BMap.Marker( point, { enableMassClear: false }  );
          var labelStyle = {
              color: "#ffffff",
              backgroundColor: "#00000000",
              border: "0",
              fontSize: "10px",
              left: '50%',
              top: '50%',
              transform: 'translate(-50%, -50%)'
          }
          var label = new BMap.Label(index+1, {
              offset: new BMap.Size('50%', '60%')
          });
          label.setStyle(labelStyle)
          marker.setLabel(label);
          markers.push(point)

          Map.addOverlay(marker)
          var sContent =
                `<div class="introduceBox">
                  <div class="introduceDivBox">
                    <label><span>名</span><span>称：</span></label>
                    <span>${item.title}</span>
                  </div>
                  <div class="introduceDivBox">
                    <label><span>描</span><span>述：</span></label>
                    <span>${item.description}</span>
                  </div>
                  <div class="introduceDivBox">
                    <label><span>类</span><span>型：</span></label>
                    <span>${item.typeDes}</span>
                  </div>
                  <div class="introduceDivBox">
                    <label><span>状</span><span>态：</span></label>
                    <span>${item.statusDes}</span>
                  </div>
                  <div class="introduceDivBox">
                    <label><span>提</span><span>出</span><span>时</span><span>间：</span></label>
                    <span>${item.subTime || ''}</span>
                  </div>
                  <div class="introduceDivBox">
                    <label><span>解</span><span>决</span><span>人：</span></label>
                    <span>${item.solveName || ''}</span>
                  </div>
                  <div class="introduceDivBox">
                    <label><span>解</span><span>决</span><span>时</span><span>间：</span></label>
                    <span>${item.solveTime || ''}</span>
                  </div>
                </div>`
          let infoWindow = new BMap.InfoWindow(sContent, opts);  // 创建信息窗口
          marker.addEventListener("click", () => {
            Map.openInfoWindow(infoWindow, point); //显示窗口
          });
        })
        var polyline = new BMap.Polyline(markers, { //依据标注连线
          strokeColor: 'blue',
          strokeWeight: 2,
          strokeOpacity: 0.5,
          enableMassClear: false
        });
        Map.addOverlay(polyline); // 增加折线
        this.Map = Map;
      },
    }
  }
</script>

<style>
.montior-modal .ant-form-item {
  margin-bottom: 10px;
}
.montior-modal .textal-left .ant-col {
  text-align: left;
}
.montior-modal .monitorModal-allmap {
  width: 752px;
  height: 500px;
  margin: 0 auto;
}
.montior-modal .monitorModal-allmap .anchorBL,
.montior-modal .monitorModal-allmap .BMap_cpyCtrl span {
  display: none !important;
}
.montior-modal .monitorModal-allmap .BMap_Marker:not(.BMap_noprint) {
    width: 20px !important;
    height: auto !important;
    overflow: hidden;
}
.montior-modal .monitorModal-allmap .BMap_Marker div:first-child {
    position: relative !important;
}
.montior-modal .BMap_cpyCtrl {
  display: none;
}
.montior-modal .anchorBL {
  display: none;
}
.montior-modal .BMap_pop{
  text-align: left;
  opacity: 0.8;
}
.montior-modal .BMap_bubble_pop{
  padding: 0px!important;
  position: absolute;
  cursor: default;
  border: 0px solid rgb(221, 221, 221) !important;
  background-color: rgba(255, 255, 255,.5) !important;
  border-radius: 10px;
  width: 200px !important;
}
/*地图标题*/
.montior-modal .BMap_bubble_top{
  height: 30px;
}
.montior-modal .BMap_bubble_title {
  color: white !important;
  font-size: 13px;
  font-weight: bold;
  text-align: left;
  padding-left: 5px;
  padding-top: 5px;
  border-bottom: 1px solid gray;
  background-color: #0066b3;
  width: 300px !important;
}
.montior-modal .BMap_bubble_center{
  width: 200px !important;
  top: 0px !important;
  padding: 0px !important;

}
.montior-modal .BMap_bubble_content{
  width: 100% !important;
  top: 0px!important;
}

/* 消息内容 */
.montior-modal .BMap_bubble_content {
  background-color: white;
  padding: 5px 0 10px 5px;
}

.montior-modal .introduceDivBox {
  display: flex;
}
.montior-modal .introduceDivBox label {
  flex-shrink: 0;
  width: 74px;
}
.montior-modal .introduceDivBox.setwidth label {
  width: 50px;
}
.montior-modal .introduceDivBox label {
  display: flex;
  justify-content: space-between;
}


/* 内容 */
.montior-modal .BMap_shadow img{
  display: none;
}
.montior-modal .BMap_pop>div:nth-child(9){
  width: 300px !important;
  left: 80px !important;
}
.montior-modal .BMap_pop div:nth-child(9) {
  top: 35px !important;
  border-radius: 7px;
}

/* 左上角删除按键 */
.montior-modal .BMap_pop img {
  top: 45px !important;
  left: 363px !important;
}

.montior-modal .BMap_top {
  display: none;
}

.montior-modal .BMap_bottom {
  display: none;
}

.montior-modal .BMap_center {
  display: none;
}

/* 隐藏边角 */
.montior-modal .BMap_pop>div:nth-child(1) div {
  display: none;
}

.montior-modal .BMap_pop>div:nth-child(3) {
  display: none;
}

.montior-modal .BMap_pop>div:nth-child(7) {
  display: none;
}
.montior-modal .BMap_pop>div:nth-child(5) {
  display: none;
}
</style>