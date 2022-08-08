<template>
    <div class="margin12" style="width:100%;height:100%;">
        <a-card>
            <a-tabs :activeKey="platformTabKey" @change="callback">
                <a-tab-pane :key="item.key" :tab="item.name" v-for="(item) in tabsData2"></a-tab-pane>
            </a-tabs>
            <div class="playWndList" :style="'height:calc('+'100vh - ' + videoBox_top + 'px)'">
                <!-- <template  v-for="(item, index) in dataSource"  >
                    <iframe :id="'iframe' + index" class="iframe" scrolling="no" frameborder="0" width="400" height="300" :key="index">
                        <div :id="'playWnd' + index" :class="'playWnd playWnd' + index"></div>
                    </iframe>
                </template> -->
                <div v-for="(item, index) in dataSource" :id="'playWnd' + index" :class="'playWnd playWnd' + index" :key="index"></div>
            </div>
        </a-card>
    </div>
</template>

<script>
    import axios from "axios";
    import httpUrl from '@/api/commont.js'
    export default {
        name: "video",
        data(){
            return {
                videoBox_top: 269,
                resourceName: '', //筛选条件
                // 表头
                columns: [
                    {
                        title: '编码',
                        dataIndex: 'indexCode',
                        ellipsis: true,
                        align: "center",
                        // width: 80,
                    },
                    {
                        title: '资源名称',
                        dataIndex: 'name',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: '经度',
                        dataIndex: 'longitude',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: '纬度',
                        dataIndex: 'latitude',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: "操作",
                        dataIndex: "action",
                        align: "center",
                        width:180,
                        fixed:"right",
                        scopedSlots: { customRender: "action" }
                    },
                ],
                dataSource: [],
                loading: false,
                ipagination: {
                    current: 1, // pageNo
                    pageSize: 12,
                    showTotal: (total, range) => {
                        return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
                    },
                    total: 0
                },
                resourceVisible: false, //层
                swfHeight: "",
                swfWidth: "",
                //初始化参数
                initparam: {
                    // appKey: "xxxxx",
                    // secret: "xxxxxxx",
                    // apiIp: "xxx.xxx.xxx",
                    // apiPort: 8099,
                    // layout: "1x1",//这里是来控制你一开始初始化之后的分屏的



                    hkArtemisAppKey: '7898654', //海康综合安防平台分配的合作方AppKey
                    hkArtemisAppSecret: 'xs45s4f5547gagfdgd52', //海康综合安防平台分配的合作方AppSecret
                    hkArtemisHost: '192.168.2.123', //海康综合安防API网关
                    key: '001', //海康综合安防平台唯一标识key
                },
                //监控点编号
                pointCode: "123xxxx",
                pubKey: "",
                oWebControl: null,
                WebControl: null,
                initCount:'',
                httpUrl: httpUrl,
                platformTabKey: '',
                tabsData: [],
                tabsData2: [{
                    hkArtemisAppKey: "7898654",
                    hkArtemisAppSecret: "xs45s4f5547gagfdgd52",
                    hkArtemisHost: "192.168.2.123",
                    key: "001",
                    name: "第一页",
                    version: "1.4.1",
                },{
                    hkArtemisAppKey: "7898654",
                    hkArtemisAppSecret: "xs45s4f5547gagfdgd52",
                    hkArtemisHost: "192.168.2.123",
                    key: "002",
                    name: "第二页",
                    version: "1.4.1",
                }],
                oWebControlList: []

            }
        },
        created() {
        },
        beforeMount() {
            this.WebControl = WebControl;
        },//调用电脑中的插件
        mounted() {
            this.getTabs();
            // this.getArtemisConfig();
            // this.getList();
        },
        beforeDestroy() {
            this.closeWindow();//关闭插件
        },
        computed: {

        },
        methods: {
            // updata() { //数据初始化
            //     this.loading = true;
            //     this.getList(this);
            // },
            getTabs () { //获取tab
                (function (_this) {
                    let url = window._CONFIG['monitorURL'] + '/camera/getAllConfigs';
                    axios.get(url).then( (res) => {
                        if (res.data.code * 1 == 200) {
                            if (res.data.list) {
                                _this.platformTabKey =  res.data.list[0].key;
                                _this.initparam = res.data.list[0];
                                _this.tabsData = res.data.list;
                                _this.configKey =  res.data.list[0].key;
                                _this.getList();
                            }
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
                })(this)
            },
            callback(key) { //切换tabs
                this.platformTabKey = key;
                let tabsData2 = this.tabsData2;
                let newArr = tabsData2.filter((value) => value.key === key);
                // this.initparam = newArr[0];
                this.configKey = newArr[0].key;
                this.closeWindow();
                this.oWebControlList = [];
                this.$nextTick ( () => {
                    this.getList(key == '002' ? 2 : 1);
                })
            },
            
            getList(pageNo) { //获取列表数据
                (function (_this) {
                    // _this.ipagination.current = 1;
                    _this.name = '';
                    _this.dataSource = [];
                    let param = {
                        pageNo: pageNo || _this.ipagination.current,
                        pageSize: _this.ipagination.pageSize,
                        name: _this.resourceName,
                        configKey: '001'
                    }
                    // axios.get('http://172.168.21.84:8089/siutp/monitor/camera/list', {params:param})
                    var url = window._CONFIG['monitorURL'] + '/camera/monitors';
                    axios.get(url, {params:param})
                        .then(res => {
                            if (res.data.code * 1 == 200) {
                                _this.loading = false;
                                let result = res.data.data.list;
                                _this.dataSource = result;
                                result.forEach((item, index)=> {
                                        _this.initPlugin(item.indexCode, 'playWnd'+ index)
                                })
                                // this.videoBox_top = document.getElementsByClassName('playWndList')[0].offsetTop;
                            } else {
                                _this.$message.info(res.message);
                            }
                        })
                })(this);
            },
            
            initPlugin(indexCode, playWndIndex) { //创建播放实例
                // playWndIndex = 'playWnd0'
                let oWebControl = new WebControl({
                    szPluginContainer: playWndIndex, // 指定容器id
                    iServicePortStart: 15900, // 指定起止端口号，建议使用该值
                    iServicePortEnd: 15900,
                    szClassId: "23BF3B0A-2C56-4D97-9C03-0CB103AA8F11", // 用于IE10使用ActiveX的clsid
                    // 创建WebControl实例成功
                    cbConnectSuccess: (res) => {
                        oWebControl.JS_StartService("window", { // WebControl实例创建成功后需要启动服务
                            dllPath: "./VideoPluginConnect.dll" // 值"./VideoPluginConnect.dll"写死
                        }).then((res) => {
                            console.log('启动插件服务成功: ' + playWndIndex)
                            // 启动插件服务成功
                            oWebControl.JS_SetWindowControlCallback({ // 设置消息回调
                                cbIntegrationCallBack: this.cbIntegrationCallBack
                            });
                            oWebControl.JS_CreateWnd(playWndIndex).then(() => { //JS_CreateWnd创建视频播放窗口，宽高可设定
                                this.getInitParam(oWebControl, indexCode, playWndIndex); // 创建播放实例成功后初始化
                            });
                        }, () => {
                            console.log('启动插件服务失败: ' + playWndIndex)
                                // 启动插件服务失败
                            }
                        );
                    }, 
                    cbConnectError: () => { // 创建WebControl实例失败
                        console.log('创建WebControl实例失败=>cbConnectError: ' + playWndIndex)
                        oWebControl = null;
                        // $("#playWnd").html("插件未启动，正在尝试启动，请稍候...");
                        let htmls = '<div class="videoTips">插件启动失败，请检查插件是否安装!</div>' +
                            '<div class="videoTips">复制下面链接，在浏览器中打开 即可下载安装</div>' +
                            '<div class="videoTipsUrl">'+this.httpUrl.httpUrl+'</div>'

                        this.WebControl.JS_WakeUp("VideoWebPlugin://"); // 程序未启动时执行error函数，采用wakeup来启动程序
                        this.initCount++;
                        if (this.initCount < 3) {
                            setTimeout(() => {
                                this.initPlugin(indexCode, playWndIndex);
                            }, 3000);
                        } else {
                            console.log("插件启动失败，请检查插件是否安装: " + playWndIndex);
                            // $("#"+playWndIndex).width(this.swfWidth);
                            // $("#"+playWndIndex).height(this.swfHeight);
                            $("#"+playWndIndex).append(htmls);
                            $(".videoTips").css({"width":"100%","text-align":"center","padding-top":"30px"});
                            $(".videoTipsUrl").css({"width":"100%","text-align":"center","padding-top": "30px"});
                        }
                    },
                    // 异常断开：bNormalClose = false
                    cbConnectClose: bNormalClose => {
                        // JS_Disconnect正常断开：bNormalClose = true
                        console.log("异常断开=>cbConnectClose: " + playWndIndex);
                        oWebControl = null;
                    }
                });
                this.oWebControlList.push(oWebControl)
            },
            // 设置窗口控制回调
            setCallbacks() {
                this.oWebControl.JS_SetWindowControlCallback({
                    cbIntegrationCallBack: this.cbIntegrationCallBack
                });
            },
            // 推送消息
            cbIntegrationCallBack(oData) {
                console.log('推送消息：'+ oData.responseMsg);
                /* showCBInfo(JSON.stringify(oData.responseMsg)); */
            },
            getInitParam(oWebControl, indexCode, playWndIndex) {
                this.init(oWebControl, indexCode, playWndIndex)
            },
            //初始化
            init(oWebControl, indexCode, playWndIndex) {
                this.getPubKey(oWebControl, () => {
                    // 请自行修改以下变量值
                    let appkey = this.initparam.hkArtemisAppKey; //综合安防管理平台提供的appkey，必填
                    let secret = this.setEncrypt(this.initparam.hkArtemisAppSecret); //综合安防管理平台提供的secret，必填
                    let ip = this.initparam.hkArtemisHost; //综合安防管理平台IP地址，必填
                    var playMode = 0;                                  //初始播放模式：0-预览，1-回放
                    var port = 443;                                    //综合安防管理平台端口，若启用HTTPS协议，默认443
                    var snapDir = "D:\\SnapDir";                       //抓图存储路径
                    var videoDir = "D:\\VideoDir";                     //紧急录像或录像剪辑存储路径
                    var layout = "1x1";                                //playMode指定模式的布局
                    var enableHTTPS = 1;                               //是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
                    var encryptedFields = 'secret';					   //加密字段，默认加密领域为secret
                    var showToolbar = 0;                               //是否显示工具栏，0-不显示，非0-显示
                    var showSmart = 1;                                 //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
                    var buttonIDs = "0,16,256,257,258,259,260,512,513,514,515,516,517,768,769";  //自定义工具条按钮
                    oWebControl.JS_RequestInterface({
                        funcName: "init",
                        argument: JSON.stringify({
                            appkey: appkey, //API网关提供的appkey
                            secret: secret, //API网关提供的secret
                            ip: ip, //API网关IP地址
                            playMode: playMode, //播放模式（决定显示预览还是回放界面）
                            port: port, //端口
                            snapDir: snapDir, //抓图存储路径
                            videoDir: videoDir, //紧急录像或录像剪辑存储路径
                            layout: layout, //布局
                            enableHTTPS: enableHTTPS, //是否启用HTTPS协议
                            encryptedFields: encryptedFields, //加密字段
                            showToolbar: showToolbar, //是否显示工具栏
                            showSmart: showSmart, //是否显示智能信息
                            buttonIDs: buttonIDs //自定义工具条按钮
                        })
                    }).then(oData => {
                        // oWebControl.JS_Resize(this.swfWidth, this.swfHeight); // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
                        oWebControl.JS_Resize($('#playWnd0').width(), $('#playWnd0').height()); // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
                        console.log('初始化之后开启预览：' + playWndIndex)
                        this.startpreview(oWebControl, indexCode);  //初始化之后开启预览
                    });
                });
            },

            getPubKey(oWebControl, callback) { //获取公钥
                oWebControl.JS_RequestInterface({
                    funcName: "getRSAPubKey",
                    argument: JSON.stringify({
                        keyLength: 1024
                    })
                }).then(oData => {
                    if (oData.responseMsg.data) {
                        this.pubKey = oData.responseMsg.data;
                        callback();
                    }
                });
            },
            
            setEncrypt(value) { //RSA加密
                let encrypt = new JSEncrypt();
                encrypt.setPublicKey(this.pubKey);
                return encrypt.encrypt(value);
            },
            // 设置窗口裁剪，当因滚动条滚动导致窗口需要被遮住的情况下需要JS_CuttingPartWindow部分窗口
            setWndCover(oWebControl) { //貌似是用不到了 ？？？ 
                let iWidth = $(window).width();
                let iHeight = $(window).height();
                let oDivRect = $("#playWnd0")
                    .get(0)
                    .getBoundingClientRect();
                let iCoverLeft = oDivRect.left < 0 ? Math.abs(oDivRect.left) : 0;
                let iCoverTop = oDivRect.top < 0 ? Math.abs(oDivRect.top) : 0;
                let iCoverRight =
                    oDivRect.right - iWidth > 0 ? Math.round(oDivRect.right - iWidth) : 0;
                let iCoverBottom =
                    oDivRect.bottom - iHeight > 0
                        ? Math.round(oDivRect.bottom - iHeight)
                        : 0;

                iCoverLeft = iCoverLeft > this.swfWidth ? this.swfWidth : iCoverLeft;
                iCoverTop = iCoverTop > this.swfHeight ? this.swfHeight : iCoverTop;
                iCoverRight = iCoverRight > this.swfWidth ? this.swfWidth : iCoverRight;
                iCoverBottom =
                    iCoverBottom > this.swfHeight ? this.swfHeight : iCoverBottom;
                oWebControl.JS_RepairPartWindow(0, 0, this.swfWidth+1, this.swfHeight); // 多1个像素点防止还原后边界缺失一个像素条
                if (iCoverLeft != 0) {
                    oWebControl.JS_CuttingPartWindow(0, 0, iCoverLeft, this.swfHeight);
                }
                if (iCoverTop != 0) {
                    oWebControl.JS_CuttingPartWindow(0, 0, this.swfWidth+1, iCoverTop); // 多剪掉一个像素条，防止出现剪掉一部分窗口后出现一个像素条
                }
                if (iCoverRight != 0) {
                    oWebControl.JS_CuttingPartWindow(
                        this.swfWidth - iCoverRight,
                        0,
                        iCoverRight,
                        this.swfHeight
                    );
                }
                if (iCoverBottom != 0) {
                    oWebControl.JS_CuttingPartWindow(
                        0,
                        this.swfHeight - iCoverBottom,
                        this.swfWidth,
                        iCoverBottom
                    );
                }
            },
            //视频“预览”功能
            startpreview(oWebControl, indexCode) {
                // let pointCode = this.pointCode;
                let cameraIndexCode = indexCode; //获取输入的监控点编号值，必填
                let streamMode = 0; //主子码流标识：0-主码流，1-子码流
                let transMode = 1; //传输协议：0-UDP，1-TCP
                let gpuMode = 0; //是否启用GPU硬解，0-不启用，1-启用
                let wndId = -1; //播放窗口序号（在2x2以上布局下可指定播放窗口）
                oWebControl.JS_RequestInterface({
                    funcName: "startPreview",
                    argument: JSON.stringify({
                        cameraIndexCode: cameraIndexCode, //监控点编号
                        streamMode: streamMode, //主子码流标识
                        transMode: transMode, //传输协议
                        gpuMode: gpuMode, //是否开启GPU硬解
                        wndId: wndId //可指定播放窗口
                    })
                });
            },

            //执行每监控点预览的操作 //获取监控点编号
            videoPreview(val, index) { //未执行
                // console.log('--- videoPreview ---')
                // this.position = index;
                // this.oWebControl.JS_ShowWnd();
                // this.pointCode = val.pointCode;
                // this.startpreview();
            },
            //停止全部预览功能
            stopAllPreview() { //未调用
                // this.oWebControl.JS_RequestInterface({
                //     funcName: "stopAllPreview"
                // });
            },

            closeWindow() { //关闭视频窗口
                console.log('关闭视频窗口')
                this.oWebControlList.forEach((item, idnex) => {
                    if (item != null) {
                        item.JS_HideWnd(); // 先让窗口隐藏，规避可能的插件窗口滞后于浏览器消失问题
                        item.JS_Disconnect().then(() => {
                            // 断开与插件服务连接成功
                        },() => {
                            // 断开与插件服务连接失败
                        });
                    }
                })
            },
            
            //获取综合安防平台配置(获取海康密钥)
            getArtemisConfig(){
                // (function (_this) {
                //     // axios.get('http://172.168.21.84:8089/siutp/monitor/camera/artemisConfig')
                //     var url = window._CONFIG['monitorURL'] + '/camera/configs';
                //     axios.get(url)
                //         .then(res => {
                //             if (res.data.code * 1 == 200) {
                //                 _this.initparam = res.data.result;
                //             } else {
                //                 _this.$message.info(res.message);
                //             }
                //         })
                // })(this);
            },

            // handleTableChange(page, pageSize) {
            //     this.ipagination.current = page.current;
            // },
            // searchQuery(){ //查询
            //     this.ipagination.current = 1;
            //     this.name = '';
            //     this.dataSource = [];
            //     this.updata();
            // },
            // searchReset(){ //重置
            //     this.ipagination.current = 1;
            //     this.resourceName = '';
            //     this.updata();
            // },
            // handleClose() { //关闭弹层
            //     this.resourceVisible = false;
            //     this.closeWindow();//关闭插件
            // },

            
            handleEdit(oWebControl, indexCode, playWndIndex) {// 显示弹层
                // this.resourceVisible = true;
                // this.swfHeight = 400;
                // this.swfWidth = 400;
                // // 页面加载时创建播放实例初始化
                // this.pointCode = record.regionIndexCode;
                // this.pointCode = record.indexCode;

                // 有用的
                // this.initPlugin(indexCode, playWndIndex);
                // this.getInitParam(oWebControl, indexCode, playWndIndex);//获取初始话所需的参数


                // // 监听resize事件，使插件窗口尺寸跟随DIV窗口变化
                // $(window).resize(() => {
                //     if (this.oWebControl != null) {
                //         this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
                //         // this.setWndCover(oWebControl);
                //     }
                // });
                // // 监听滚动条scroll事件，使插件窗口跟随浏览器滚动而移动
                // $(window).scroll(() => {
                //     if (this.oWebControl != null) {
                //         this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
                //         // this.setWndCover(oWebControl);
                //     }
                // });
                // this.init(oWebControl, indexCode);
            },
        },
    }
</script>

<style scoped lang="less">
    .fr{float: right;}
    .over_hide{overflow: hidden;}
    .ml08{margin-left: 8px}
    .mr08{margin-right: 8px}
    .plr30{
        padding-left: 30px;
        padding-right: 30px;
        box-sizing: border-box;
    }
    .bg_fff{background: #ffffff}
    .playWndList {
        display: flex;
        flex-wrap: wrap;
        width: 100%;
    }
    .playWnd {
        width: 24%;
        height: 30%;
        border: 1px solid rgba(224, 224, 224, 0.8);
        margin-bottom: 20px
    }
    .playWnd:not(:nth-child(4n)) {
        margin-right: .75%;
    }
    .dataChartBox{
        .chartDivBox{
            background-color: #fff;
            margin-bottom: 10px;
        }
        .container{
            height: 300px;
        }
    }
    .tableBox{
        .ant-table-pagination{
            display: none;
        }
    }
    .ant-divider-inner-text{
        span{
            font-size: 12px;
            color: #999;
            margin-left: 5px;
        }
    }
    .mzindex {
        z-index: 3000 !important;
    }
    /deep/.v-modal{
        z-index: 1200 !important;
    }
    /deep/.el-dialog__wrapper {
        z-index: 99999 !important;
    }
</style>