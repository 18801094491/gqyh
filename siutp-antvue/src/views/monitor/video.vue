<template>
    <div class="margin12">
        <a-card :bordered="false">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="5" :sm="10">
                            <a-form-item label="资源名称">
                                <a-input
                                        placeholder="请输入资源名称"
                                        v-model="resourceName"
                                ></a-input>
                            </a-form-item>
                        </a-col>


                        <a-col :md="6" :sm="6" :offset="13">
                          <span class="fr over_hide table-page-search-submitButtons" >
                            <a-button type="primary" @click="searchQuery" icon="search" >查询</a-button>
                            <a-button
                                    type="primary"
                                    @click="searchReset"
                                    icon="reload"
                                    class="ml08"
                            >重置</a-button>
                          </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
        </a-card>
        <a-card :bordered="false">
            <a-tabs :activeKey="platformTabKey" @change="callback">
                <a-tab-pane :key="item.key" :tab="item.name" v-for="(item) in tabsData"></a-tab-pane>
            </a-tabs>
            <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="false"
                    :loading="loading"
                    @change="handleTableChange">
                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">查看信息</a>
                </span>
            </a-table>
        </a-card>
        <!--监控层-->
        <el-dialog
                title="信息"
                :visible.sync="resourceVisible"
                width="640px"
                :append-to-body="true"
                :before-close="handleClose">
            <div id="playWnd" class="playWnd" :style="{width:swfWidth+'px',height:swfHeight+'px'}" v-if="resourceVisible"></div>
        </el-dialog>
    </div>

</template>

<script>
    import axios from "axios";
    import httpUrl from '@/api/commont.js'
    export default {
        name: "video",
        data(){
            return {
                resourceName: '', //筛选条件
                // 表头
                columns: [{
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },{
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
                    title: '区域',
                    dataIndex: 'regionName',
                    align: "center",
                    ellipsis: true,
                },
                {
                    title: '区域路径',
                    dataIndex: 'regionPathName',
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
                }],
                dataSource: [],
                loading: false,
                ipagination: {
                    current: 1, // pageNo
                    pageSize: 100,
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



                    hkArtemisAppKey: '', //海康综合安防平台分配的合作方AppKey
                    hkArtemisAppSecret: '', //海康综合安防平台分配的合作方AppSecret
                    hkArtemisHost: '', //海康综合安防API网关
                    key: '', //海康综合安防平台唯一标识key
                },
                //监控点编号
                pointCode: "123xxxx",
                pubKey: "",
                oWebControl: null,
                WebControl: null,
                initCount:'',
                httpUrl: httpUrl,
                platformTabKey: '',
                tabsData: []

            }
        },
        computed: {

        },
        methods: {
            getInitParam() {
                this.init()
            },
            //执行每监控点预览的操作 //获取监控点编号
            videoPreview(val, index) {
                this.position = index;
                this.oWebControl.JS_ShowWnd();
                this.pointCode = val.pointCode;
                this.startpreview();
            },
            // 创建播放实例
            initPlugin() {
                this.oWebControl = new WebControl({
                    szPluginContainer: "playWnd", // 指定容器id
                    iServicePortStart: 15900, // 指定起止端口号，建议使用该值
                    iServicePortEnd: 15900,
                    szClassId: "23BF3B0A-2C56-4D97-9C03-0CB103AA8F11", // 用于IE10使用ActiveX的clsid
                    // 创建WebControl实例成功
                    cbConnectSuccess: () => {
                        this.oWebControl
                            .JS_StartService("window", {
                                // WebControl实例创建成功后需要启动服务
                                dllPath: "./VideoPluginConnect.dll" // 值"./VideoPluginConnect.dll"写死
                            })
                            .then(
                                () => {
                                    // 启动插件服务成功
                                    this.oWebControl.JS_SetWindowControlCallback({
                                        // 设置消息回调
                                        cbIntegrationCallBack: this.cbIntegrationCallBack
                                    });

                                    this.oWebControl
                                        .JS_CreateWnd("playWnd", this.swfWidth, this.swfHeight)
                                        .then(() => {
                                            //JS_CreateWnd创建视频播放窗口，宽高可设定
                                            this.getInitParam(); // 创建播放实例成功后初始化
                                        });
                                },
                                () => {
                                    // 启动插件服务失败
                                }
                            );
                    },
                    // 创建WebControl实例失败
                    cbConnectError: () => {
                        this.oWebControl = null;
                        // $("#playWnd").html("插件未启动，正在尝试启动，请稍候...");
                        let htmls = '<div class="videoTips">插件启动失败，请检查插件是否安装!</div>' +
                            '<div class="videoTips">复制下面链接，在浏览器中打开 即可下载安装</div>' +
                            '<div class="videoTipsUrl">'+this.httpUrl.httpUrl+'</div>'

                        this.WebControl.JS_WakeUp("VideoWebPlugin://"); // 程序未启动时执行error函数，采用wakeup来启动程序
                        this.initCount++;
                        if (this.initCount < 3) {
                            setTimeout(() => {
                                this.initPlugin();
                            }, 3000);
                        } else {
                            console.log("插件启动失败，请检查插件是否安装!");
                            $("#playWnd").width(this.swfWidth);
                            $("#playWnd").height(this.swfHeight);
                            $("#playWnd").append(htmls);
                            $(".videoTips").css({"width":"100%","text-align":"center","padding-top":"30px"});
                            $(".videoTipsUrl").css({"width":"100%","text-align":"center","padding-top": "30px"});
                        }
                    },
                    // 异常断开：bNormalClose = false
                    cbConnectClose: bNormalClose => {
                        // JS_Disconnect正常断开：bNormalClose = true
                        console.log("cbConnectClose");
                        this.oWebControl = null;
                    }
                });
            },
            // 设置窗口控制回调
            setCallbacks() {
                this.oWebControl.JS_SetWindowControlCallback({
                    cbIntegrationCallBack: this.cbIntegrationCallBack
                });
            },
            // 推送消息
            cbIntegrationCallBack(oData) {
                console.log(oData.responseMsg);
                /* showCBInfo(JSON.stringify(oData.responseMsg)); */
            },
            //初始化
            init() {
                this.getPubKey(() => {
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

                    this.oWebControl
                        .JS_RequestInterface({
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
                        })
                        .then(oData => {
                            this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight); // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
                            this.startpreview();  //初始化之后开启预览
                        });
                });
            },
            //获取公钥
            getPubKey(callback) {
                this.oWebControl
                    .JS_RequestInterface({
                        funcName: "getRSAPubKey",
                        argument: JSON.stringify({
                            keyLength: 1024
                        })
                    })
                    .then(oData => {
                        console.log(oData);
                        if (oData.responseMsg.data) {
                            this.pubKey = oData.responseMsg.data;
                            callback();
                        }
                    });
            },
            //RSA加密
            setEncrypt(value) {
                let encrypt = new JSEncrypt();
                encrypt.setPublicKey(this.pubKey);
                return encrypt.encrypt(value);
            },
            // 设置窗口裁剪，当因滚动条滚动导致窗口需要被遮住的情况下需要JS_CuttingPartWindow部分窗口
            setWndCover() {
                let iWidth = $(window).width();
                let iHeight = $(window).height();
                let oDivRect = $("#playWnd")
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
                this.oWebControl.JS_RepairPartWindow(0, 0, this.swfWidth+1, this.swfHeight); // 多1个像素点防止还原后边界缺失一个像素条
                if (iCoverLeft != 0) {
                    this.oWebControl.JS_CuttingPartWindow(0, 0, iCoverLeft, this.swfHeight);
                }
                if (iCoverTop != 0) {
                    this.oWebControl.JS_CuttingPartWindow(0, 0, this.swfWidth+1, iCoverTop); // 多剪掉一个像素条，防止出现剪掉一部分窗口后出现一个像素条
                }
                if (iCoverRight != 0) {
                    this.oWebControl.JS_CuttingPartWindow(
                        this.swfWidth - iCoverRight,
                        0,
                        iCoverRight,
                        this.swfHeight
                    );
                }
                if (iCoverBottom != 0) {
                    this.oWebControl.JS_CuttingPartWindow(
                        0,
                        this.swfHeight - iCoverBottom,
                        this.swfWidth,
                        iCoverBottom
                    );
                }
            },
            //视频“预览”功能
            startpreview() {
                let pointCode = this.pointCode;
                let cameraIndexCode = pointCode; //获取输入的监控点编号值，必填
                let streamMode = 0; //主子码流标识：0-主码流，1-子码流
                let transMode = 1; //传输协议：0-UDP，1-TCP
                let gpuMode = 0; //是否启用GPU硬解，0-不启用，1-启用
                let wndId = -1; //播放窗口序号（在2x2以上布局下可指定播放窗口）
                this.oWebControl.JS_RequestInterface({
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

            //停止全部预览功能
            stopAllPreview() {
                this.oWebControl.JS_RequestInterface({
                    funcName: "stopAllPreview"
                });
            },
            //关闭视频窗口
            closeWindow() {
                if (this.oWebControl != null) {
                    this.oWebControl.JS_HideWnd(); // 先让窗口隐藏，规避可能的插件窗口滞后于浏览器消失问题
                    this.oWebControl.JS_Disconnect().then(
                        () => {
                            // 断开与插件服务连接成功
                        },
                        () => {
                            // 断开与插件服务连接失败
                        }
                    );
                }
            },
            //获取tab
            getTabs () {
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
            //获取列表数据
            getList(){
                (function (_this) {
                    _this.ipagination.current = 1;
                    _this.name = '';
                    _this.dataSource = [];
                    let param = {
                        pageNo: _this.ipagination.current,
                        pageSize: _this.ipagination.pageSize,
                        name: _this.resourceName,
                        configKey: _this.configKey
                    }
                    // axios.get('http://172.168.21.84:8089/siutp/monitor/camera/list', {params:param})
                    var url = window._CONFIG['monitorURL'] + '/camera/monitors';
                    axios.get(url, {params:param})
                        .then(res => {
                            if (res.data.code * 1 == 200) {
                                _this.loading = false;
                                let result = res.data.data.list;
                                _this.dataSource = result;
                                _this.ipagination.total = res.data.total
                            } else {
                                _this.$message.info(res.message);
                            }
                        })
                })(this);
            },
            handleTableChange(page, pageSize) {
                this.ipagination.current = page.current;
            },
            //查询
            searchQuery(){
                this.ipagination.current = 1;
                this.name = '';
                this.dataSource = [];
                this.updata();
            },
            //重置
            searchReset(){
                this.ipagination.current = 1;
                this.resourceName = '';
                this.updata();
            },
            // 显示弹层
            handleEdit: function (record) {
                this.resourceVisible = true;
                this.swfWidth = 600;
                this.swfHeight = 400;
                //页面加载时创建播放实例初始化
                // this.pointCode = record.regionIndexCode;
                this.pointCode = record.indexCode;
                this.initPlugin();
                this.getInitParam();//获取初始话所需的参数
                // 监听resize事件，使插件窗口尺寸跟随DIV窗口变化
                $(window).resize(() => {
                    if (this.oWebControl != null) {
                        this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
                        this.setWndCover();
                    }
                });
                // 监听滚动条scroll事件，使插件窗口跟随浏览器滚动而移动
                $(window).scroll(() => {
                    if (this.oWebControl != null) {
                        this.oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
                        this.setWndCover();
                    }
                });
                this.init();
            },
            // 关闭弹层
            handleClose() {
                this.resourceVisible = false;
                this.closeWindow();//关闭插件
            },
            //数据初始化
            updata() {
                this.loading = true;
                this.getList(this);
            },
            //切换tabs
            callback(key) {
                this.platformTabKey = key;
                let tabsData = this.tabsData;
                let newArr = tabsData.filter((value) => value.key === key);
                this.initparam = newArr[0];
                this.configKey = newArr[0].key;
                this.$nextTick ( () => {
                    this.getList();
                })
            },
        },
        created() {
        },
        beforeMount() {this.WebControl = WebControl;},//调用电脑中的插件
        mounted() {
            this.getTabs();
            // this.getArtemisConfig();
            // this.getList();
        },
        beforeDestroy() {
            this.closeWindow();//关闭插件
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
    .playWnd{
        width: 600px;
        height: 400px;
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