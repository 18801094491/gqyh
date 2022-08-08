<template>
  <a-popover
    trigger="click"
    placement="bottomRight"
    :autoAdjustOverflow="true"
    :arrowPointAtCenter="true"
    overlayClassName="header-notice-wrapper"
    @visibleChange="handleHoverChange"
    :overlayStyle="{ width: '300px', top: '50px' }">
    <template slot="content">
      <a-spin :spinning="loadding">
        <a-tabs>
          <a-tab-pane :tab="msg1Title" key="1">
            <a-list>
              <a-list-item :key="index" v-for="(record, index) in announcement1">
                <div style="margin-left: 5%;width: 80%">
                  <p><a @click="showAnnouncement(record)">标题：{{ record.titile }}</a></p>
                  <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
                </div>
                <div style="text-align: right">
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
                </div>
              </a-list-item>
              <div style="margin-top: 5px;text-align: center">
                <a-button @click="toMyAnnouncement()" type="dashed" block>查看更多</a-button>
              </div>
            </a-list>
          </a-tab-pane>
          <a-tab-pane :tab="msg2Title" key="2">
            <a-list>
              <a-list-item :key="index" v-for="(record, index) in announcement2">
                <div style="margin-left: 5%;width: 80%">
                  <p><a @click="showAnnouncement(record)">标题：{{ record.titile }}</a></p>
                  <p style="color: rgba(0,0,0,.45);margin-bottom: 0px">{{ record.createTime }} 发布</p>
                </div>
                <div style="text-align: right">
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'L'" color="blue">一般消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'M'" color="orange">重要消息</a-tag>
                  <a-tag @click="showAnnouncement(record)" v-if="record.priority === 'H'" color="red">紧急消息</a-tag>
                </div>
              </a-list-item>
              <div style="margin-top: 5px;text-align: center">
                <a-button @click="toMyAnnouncement()" type="dashed" block>查看更多</a-button>
              </div>
            </a-list>
          </a-tab-pane>
        </a-tabs>
      </a-spin>
    </template>
    <span @click="fetchNotice" class="header-notice" style="height:50px;line-height:50px; font-size:12px;position: relative;top:-1px;">
      <a-badge :count="msgTotal">
        <a-icon style="font-size: 18px; padding: 4px;position: relative; top:2px;" type="bell" />
      </a-badge>
      消息通知
    </span>
    <show-announcement ref="ShowAnnouncement" @ok="modalFormOk"></show-announcement>
  </a-popover>
</template>

<script>
  import { getAction,putAction } from '@/api/manage'
  import ShowAnnouncement from './ShowAnnouncement'
  import store from '@/store/'

  
  export default {
    name: "HeaderNotice",
    components: {
      ShowAnnouncement,
    },
    data () {
      return {
        loadding: false,
        url:{
          listCementByUser:"/sys/annountCement/listByUser",
          editCementSend:"/sys/sysAnnouncementSend/editByAnntIdAndUserId",
          queryById:"/sys/annountCement/queryById",
        },
        hovered: false,
        announcement1:[],
        announcement2:[],
        msg1Count:"0",
        msg2Count:"0",
        msg1Title:"通知(0)",
        msg2Title:"",
        stopTimer:false,
        websock: null,
        lockReconnect:false,
        heartCheck:null
        // audioSrc:require('@/assets/12310.mp3')
      }
    },
    computed:{
      msgTotal () {
        return parseInt(this.msg1Count)+parseInt(this.msg2Count);
      }
    },
    mounted() {
      this.loadData();
      //this.timerFun();
      this.initWebSocket();
      this.heartCheckFun();

      
      
    },
    destroyed: function () { // 离开页面生命周期函数
      this.websocketclose();
      
    },
    methods: {
      timerFun() {
        this.stopTimer = false;
        let myTimer = setInterval(()=>{
          // 停止定时器
          if (this.stopTimer == true) {
            clearInterval(myTimer);
            return;
          }
          this.loadData()
        },6000)
      },
      mapUpdata(data){
        if(this.$route.name!='operationCenter-mapIndex' && !this.$route.name.includes('bigScreenPages')){
          return;
        };
        if (data!=undefined) {
          if(data.status==2){
            let markData = null; 
            window.oMarker.filter((item,i,arr)=>{
              //对比ID
              if(item.id==data.id){
                if(data.opration.switchSatus!='offline'){
                  item.list=data.data;
                  //分层判断
                  // if(item.modelType<=map.getZoom()){
                    if(data.data){
                      markData = {
                        list: data.data,
                        longitude: item.longitude,
                        latitude: item.latitude,
                        modelOffset:item.modelOffset,
                        modelPosition: item.modelPosition,
                        modelTypeCode: item.modelTypeCode,
                        id: item.id
                      }
                      if (this.$route.name === 'operationCenter-mapIndex') {
                        window.oThis1 && window.oThis1.addComplexCustomOverlay1(markData, window.map, i);
                      } else if (this.$route.name.includes('bigScreenPages')) {
                        window.oThis2 && window.oThis2.addComplexCustomOverlay2(markData, window.map, i);
                      }
                    }
                  // }
                }

                if(data.opration){
                  let icon;
                  let width;
                  let height;
                  //workStatus 正常 normal 异常 warn
                  if(data.opration.workStatus=='normal'){
                    //switchSatus open 打开  close 关闭  online在线
                    
                    if(data.opration.switchSatus=='online'){
                      if(item.modelOnImg){
                        icon = item.modelOnImg.imgUrl;
                        width = item.modelOnImg.width;
                        height = item.modelOnImg.height;
                        item.modelZcImg = new AMap.Marker({
                          position: new AMap.LngLat(item.longitude,item.latitude),
                        offset: new AMap.Pixel(0, Number(item.modelOffset)),
                          icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                        })
                        
                        window[data.id].setIcon(new AMap.Icon({size: new AMap.Size(width, height), image: icon}));
                      }else{
                        icon = item.modelImg.imgUrl;
                        width = item.modelImg.width;
                        height = item.modelImg.height;
                        item.modelZcImg = new AMap.Marker({
                          position: new AMap.LngLat(item.longitude,item.latitude),
                        offset: new AMap.Pixel(0, Number(item.modelOffset)),
                          icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                        })

                        window[data.id].setIcon(new AMap.Icon({size: new AMap.Size(width, height), image: icon}));
                      }
                    }else if(data.opration.switchSatus=='open'){
                      icon = item.modelOnImg.imgUrl;
                      width = item.modelOnImg.width;
                      height = item.modelOnImg.height;
                      item.modelZcImg = new AMap.Marker({
                        position: new AMap.LngLat(item.longitude,item.latitude),
                        offset: new AMap.Pixel(0, Number(item.modelOffset)),
                        icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                      })

                      window[data.id].setIcon(new AMap.Icon({size: new AMap.Size(width, height), image: icon}));
                    }else if(data.opration.switchSatus=='closed'){
                      icon = item.modelOffImg.imgUrl;
                      width = item.modelOffImg.width;
                      height = item.modelOffImg.height;
                      item.modelZcImg = new AMap.Marker({
                        position: new AMap.LngLat(item.longitude,item.latitude),
                        offset: new AMap.Pixel(0, Number(item.modelOffset)),
                        icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                      })

                      window[data.id].setIcon(new AMap.Icon({size: new AMap.Size(width, height), image: icon}));
                    }
                  }else{
                    icon = item.modelWaringImg.imgUrl;
                    width = item.modelWaringImg.width;
                    height = item.modelWaringImg.height;
                    item.modelZcImg = new AMap.Marker({
                      position: new AMap.LngLat(item.longitude,item.latitude),
                        offset: new AMap.Pixel(0, Number(item.modelOffset)),
                      icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                    })
                    window[data.id].setIcon(new AMap.Icon({size: new AMap.Size(width, height), image: icon}));
                    window[data.id].show();
                  }
                  
                }
              }
            })
          }
        }
        return;

        // 旧的
        try {
          let allOverlay =window.map.getOverlays();
          if(data!=undefined){

            //新增

            if(data.status==1){
              window.oMarker.push({
                id: data.id,
                latitude: data.latitude,
                list: data.list,
                longitude: data.longitude,
                modelImg: new BMapGL.Icon(require('@/assets/images/mapIndex/'+data.modelOnImg), new BMapGL.Size(16, 16)),
                modelLevel: data.modelLevel,
                modelOffset: data.modelOffset,
                modelOnImg: data.modelOnImg,
                modelPosition: data.modelPosition,
                modelType: data.modelType?data.modelType:18,
                modelTypeCode: data.modelTypeCode,
              })
            }else if(data.status==2){
              //更改
              //定义作用域内变量-存储文本用的
              let markData; 
              window.oMarker.filter((item,i,arr)=>{
                //对比ID
                if(item.id==data.id){
                  // if(item.modelTypeCode!='FM'&&item.modelTypeCode!='SB'){
                    
                  // }

                  if(data.opration.switchSatus!='offline'){
                    item.list=data.data;
                    //分层判断
                    if(item.modelType<=map.getZoom()){

                      //判断icon分类
                      if(window[item.id].isVisible()){
                        
                        //删除标识
                        $('#'+item.id).remove();
                        if(data.data){
                            markData={
                              list:data.data,
                              longitude:item.longitude,
                              latitude:item.latitude,
                              modelPosition:item.modelPosition,
                              id:item.id
                            }
                          
                          //创建新的标识
                          window.oThis.addComplexCustomOverlay(markData,window.map);
                        }
                        
                      }else{
                        console.log(1);
                      }
                      
                    }
                  }
                  
                  //判断有无标注信息
                  if(data.opration){
                    //workStatus 正常 normal 异常 warn
                    if(data.opration.workStatus=='normal'){
                      //switchSatus open 打开  close 关闭  online在线
                      
                      if(data.opration.switchSatus=='online'){
                        if(item.modelOnImg){
                          item.modelZcImg=new BMapGL.Icon(item.modelOnImg.imgUrl, new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height),{
                            imageSize: new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height)
                          });
                          
                          window[data.id].setIcon(new BMapGL.Icon(item.modelOnImg.imgUrl, new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height),{
                            imageSize: new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height)
                          }));
                        }else{
                          item.modelZcImg=new BMapGL.Icon(item.modelImg.imgUrl, new BMapGL.Size(item.modelImg.width, item.modelImg.height),{
                            imageSize: new BMapGL.Size(item.modelImg.width, item.modelImg.height)
                          });
                          
                          window[data.id].setIcon(new BMapGL.Icon(item.modelImg.imgUrl, new BMapGL.Size(item.modelImg.width, item.modelImg.height),{
                            imageSize: new BMapGL.Size(item.modelImg.width, item.modelImg.height)
                          }));
                        }
                        
                        
                        
                      }else if(data.opration.switchSatus=='open'){
                        
                        item.modelZcImg=new BMapGL.Icon(item.modelOnImg.imgUrl, new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height),{
                          imageSize: new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height)
                        });
                        window[data.id].setIcon(new BMapGL.Icon(item.modelOnImg.imgUrl, new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height),{
                          imageSize: new BMapGL.Size(item.modelOnImg.width, item.modelOnImg.height)
                        })); 
                        
                      }else if(data.opration.switchSatus=='closed'){
                        
                        item.modelZcImg=new BMapGL.Icon(item.modelOffImg.imgUrl, new BMapGL.Size(item.modelOffImg.width, item.modelOffImg.height),{
                          imageSize: new BMapGL.Size(item.modelOffImg.width, item.modelOffImg.height)
                        });
                        window[data.id].setIcon(new BMapGL.Icon(item.modelOffImg.imgUrl, new BMapGL.Size(item.modelOffImg.width, item.modelOffImg.height),{
                          imageSize: new BMapGL.Size(item.modelOffImg.width, item.modelOffImg.height)
                        })); 
                        
                      }
                      
                      
                    }else{
                      // if(data.opration.switchSatus=='offline'){
                      //   if(window[item.id].isVisible()){
                      //     //删除标识
                      //     $('#'+item.id).remove();
                      //     item.list=[
                      //       {
                      //         variableName:'错误提示',
                      //         varibleValue:'无采集数据'
                      //       }
                      //     ];
                      //     item.fmType=1;
                      //     markData={
                      //       list:[{
                      //         variableName:'错误提示',
                      //         varibleValue:'无采集数据'
                      //       }],
                      //       longitude:item.longitude,
                      //       latitude:item.latitude,
                      //       modelPosition:item.modelPosition,
                      //       id:item.id,
                      //       fmType:1
                      //     }
                      //     //创建新的标识
                      //     window.oThis.addComplexCustomOverlay(markData,window.map);
                          
                      //   }
                      // }
                      
                      item.modelZcImg=new BMapGL.Icon(item.modelWaringImg.imgUrl, new BMapGL.Size(item.modelWaringImg.width, item.modelWaringImg.height),{
                        imageSize: new BMapGL.Size(item.modelWaringImg.width, item.modelWaringImg.height)
                      });
                      window[data.id].setIcon(new BMapGL.Icon(item.modelWaringImg.imgUrl, new BMapGL.Size(item.modelWaringImg.width, item.modelWaringImg.height),{
                        imageSize: new BMapGL.Size(item.modelWaringImg.width, item.modelWaringImg.height)
                      }));
                      window[data.id].show();
                    }
                    
                  }
                }
              });
              
            }else if(data.status==3){
              window.oMarker.filter((item,i,arr)=>{
                if(item.id==data.id){

                  for(let s=0;s<allOverlay.length;s++){

                    if(allOverlay[s].id!=undefined){
                      if(allOverlay[s].id==item.id){
                        
                        $('#'+item.id).remove();
                        window.map.removeOverlay(map.getOverlays()[s])
                      }
                    }
                    
                  }
                  window.oMarker.splice(i,1);
                }
              })
            }
 
          }

        } catch (err) {
          this.stopTimer = true;
          // console.log("通知异常",err);
        }
      },
      loadData (){
        try {
          // 获取系统消息
          getAction(this.url.listCementByUser).then((res) => {
            if (res.success) {
              this.announcement1 = res.result.anntMsgList;
              this.msg1Count = res.result.anntMsgTotal;
              this.msg1Title = "通知(" + res.result.anntMsgTotal + ")";
              this.announcement2 = res.result.sysMsgList;
              this.msg2Count = res.result.sysMsgTotal;
              this.msg2Title = "系统消息(" + res.result.sysMsgTotal + ")";
            }
          }).catch(error => {
            console.log("系统消息通知异常",error);//这行打印permissionName is undefined
            this.stopTimer = true;
            console.log("清理timer");
          });
        } catch (err) {
          this.stopTimer = true;
          console.log("通知异常",err);
        }
      },
      fetchNotice () {
        if (this.loadding) {
          this.loadding = false
          return
        }
        this.loadding = true
        setTimeout(() => {
          this.loadding = false
        }, 200)
      },
      showAnnouncement(record){
        putAction(this.url.editCementSend,{anntId:record.id}).then((res)=>{
          if(res.success){
            this.loadData();
          }
        });
        this.hovered = false;
        this.$refs.ShowAnnouncement.detail(record);
      },
      toMyAnnouncement(){

        this.$router.push({
          path: '/isps/userAnnouncement',
          name: 'isps-userAnnouncement'
        });
      },
      modalFormOk(){
      },
      handleHoverChange (visible) {
        this.hovered = visible;
      },

      initWebSocket: function () {

        // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
        var userId = store.getters.userInfo.id;
        var url = window._CONFIG['domianURL'].replace("https://","wss://").replace("http://","ws://")+"/websocket/"+userId;
        //console.log(url);
        this.websock = new WebSocket(url);
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
      },
      websocketonopen: function () {

        console.log("WebSocket连接成功");
        //心跳检测重置
        this.heartCheck.reset().start();
      },
      websocketonerror: function (e) {
        console.log("WebSocket连接发生错误");
        this.reconnect();
      },
      websocketonmessage: function (e) {
        //console.log("-----接收消息-------",e.data);
        var data = eval("(" + e.data + ")"); //解析对象
        if(data.cmd == "topic"){
            //系统通知
          this.loadData();
        }else if(data.cmd == "user"){
            //用户消息
          this.loadData();
        }else if(data.cmd=='gis'){
          let data2={
            status:'2',
            id:"1218732244883038209",
            opration:{
              workStatus:'off',
              "switchSatus":"closed"
            },
            data:[
              {
                variableName:'告警',
                varibleValue:'压力过大'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
            ]
          }
          this.mapUpdata(data.body)
          // this.mapUpdata(data2)
        }else if(data.cmd=='heartcheck'){
          let data2={
            status:'2',
            id:"1218732244883038209",
            opration:{
              workStatus:'off',
              "switchSatus":"closed"
            },
            data:[
              {
                variableName:'告警',
                varibleValue:'压力过大'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
              {
                variableName:'负累计流量',
                varibleValue:'0.05454[m3]'
              },
            ]
          }
          // this.mapUpdata(data2);
        }

        //心跳检测重置
        this.heartCheck.reset().start();

      },
      websocketsend(text) { // 数据发送
      
        try {
          this.websock.send(text);
        } catch (err) {
          console.log("send failed (" + err.code + ")");
        }
      },
      websocketclose: function (e) {
        console.log("connection closed (" + e.code + ")");
        this.reconnect();
      },

      openNotification (data) {
        var text = data.msgTxt;
        const key = `open${Date.now()}`;
        this.$notification.open({
          message: '消息提醒',
          placement:'bottomRight',
          description: text,
          key,
          btn: (h)=>{
            return h('a-button', {
              props: {
                type: 'primary',
                size: 'small',
              },
              on: {
                click: () => this.showDetail(key,data)
              }
            }, '查看详情')
          },
        });
      },

      reconnect() {
        var that = this;
        if(that.lockReconnect) return;
        that.lockReconnect = true;
        //没连接上会一直重连，设置延迟避免请求过多
        setTimeout(function () {
          console.info("尝试重连...");
          that.initWebSocket();
          that.lockReconnect = false;

        }, 5000);
      },
      heartCheckFun(){
        var that = this;
        //心跳检测,每20s心跳一次
        that.heartCheck = {
          timeout: 10000,
          timeoutObj: null,
          serverTimeoutObj: null,
          reset: function(){
            clearTimeout(this.timeoutObj);
            //clearTimeout(this.serverTimeoutObj);
            return this;
          },
          start: function(){
            var self = this;
            this.timeoutObj = setTimeout(function(){
              //这里发送一个心跳，后端收到后，返回一个心跳消息，
              //onmessage拿到返回的心跳就说明连接正常
              that.websocketsend("HeartBeat");
              console.info("客户端发送心跳");
              //self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
              //  that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
              //}, self.timeout)
            }, this.timeout)
          }
        }
      },


      showDetail(key,data){
        this.$notification.close(key);
        var id = data.msgId;
        getAction(this.url.queryById,{id:id}).then((res) => {
          if (res.success) {
            var record = res.result;
            this.showAnnouncement(record);
          }
        })

      },
      control(){
        var audio = document.getElementById('audio');
        if(audio!==null){
          if(audio.paused){
            audio.play();// 播放
          }
        }
      }
    }
  }
</script>

<style lang="css">
  .header-notice-wrapper {
    top: 50px !important;
  }
</style>
<style lang="scss" scoped>
  .header-notice{
    display: inline-block;
    transition: all 0.3s;

    span {
      vertical-align: initial;
    }
  }
  #audio{
    
  }
</style>