<template>
    <div class="margin12">
        <div class="screenCommonBox">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="5" :sm="5">
                            <a-form-item label="河流名称">
                                <a-input placeholder="请输入河流名称" v-model="riverName"></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :md="19" :sm="19">
                          <span class="fr over_hide table-page-search-submitButtons">
                            <a-button type="primary" @click="searchQuery('min')" icon="search">查询</a-button>
                            <a-button class="ant-btn-border ml08" @click="searchReset" icon="reload">重置</a-button>
                          </span>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
        </div>
        <a-card :bordered="false">
            <!-- 操作按钮区域 -->
            <div class="table-operator">
                <a-button @click="addRiver" type="primary"><a-icon type="plus" />新增</a-button>
            </div>
            <!-- table区域-begin -->
            <a-table
                    ref="table"
                    size="middle"
                    bordered
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    @change="handleTableChange"
            >
            <span slot="num" slot-scope="text, record, index">{{(ipagination.current-1)*ipagination.pageSize+parseInt(index)+1}} </span>
            <span slot="action" slot-scope="text, record">
                <a @click="openRiverDetails(record)">详情</a>
                <a-divider type="vertical"/>
                <a @click="openEditRiver(record)">修改</a>
                <a-divider type="vertical"/>
                <a-popconfirm title="确定删除吗?" @confirm="() => deleteRiver(record.id)">
                  <a>删除</a>
                </a-popconfirm>
            </span>
            </a-table>
        </a-card>
        <!--详情-->
        <a-drawer
                title="详情"
                :width="600"
                @close="onCloseRiverDetails"
                :visible="riverDetailsVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-row :gutter="24">
                <a-col :span="24">
                    <a-form-item label="河流名称">
                        <p>{{riversDetails.riverName}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="24">
                    <a-form-item label="支流编号">
                        <p>{{riversDetails.riverLevel}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="24">
                    <a-form-item label="河流经纬度">
                        <p>{{riversDetails.riverAddressInfo}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="添加人员">
                        <p>{{riversDetails.createBy}}</p>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="添加时间">
                        <p>{{riversDetails.createTime}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="修改人员">
                        <p>{{riversDetails.updateBy}}</p>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="修改时间">
                        <p>{{riversDetails.updateTime}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <div class="layer_bottem_btn">
                <a-button class="mr08" @click="onCloseRiverDetails">
                    关闭
                </a-button>
            </div>
        </a-drawer>
        <!--新建河流-->
        <a-drawer
                title="新建河流"
                :width="600"
                :confirmLoading="confirmLoading"
                @close="onCloseAddRiver"
                :visible="addRiverVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-form :form="form" layout="vertical" @submit="handleSubmitAdd">
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="河流名称">
                            <a-input
                                    placeholder="请输入河流名称"
                                    v-decorator="['name', { rules: [{ required: true, message: '请输入河流名称!' }] }]"
                            />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="支流编号">
                            <a-input
                                    placeholder="请输入支流编号"
                                    v-decorator="['branchGrade', { rules: [{ required: true, message: '请输入支流编号!' }] }]"
                            />
                        </a-form-item>
                    </a-col>

                </a-row>
                <a-row :gutter="24" class="mb20">
                    <a-col :span="16">
                        河流经纬度信息
                    </a-col>
                    <a-col :span="8">
                        <a-button type="primary" class="fr" @click="showAddMap">
                            经纬度编辑
                        </a-button>
                    </a-col>
                </a-row>
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item  hasFeedback>
                            <a-input disabled="ture" v-model="riverAddressInfo" />
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
            <div class="layer_bottem_btn">
                <a-button class="mr08" @click="onCloseRiverDetails">关闭</a-button>
                <a-button type="primary" @click="handleSubmitAdd">提交</a-button>
            </div>
        </a-drawer>
        <!--添加河流地图层-->
        <a-modal
                title="河流经纬度信息"
                width="80%"
                :visible="visibleMapCon"
                :confirm-loading="confirmLoading"
                @ok="handleAddMap"
                @cancel="handleCancelAddMap"
        >
            <div class="map-body">
                <div id="dbmap"></div>
            </div>
        </a-modal>
        <!--编辑河流-->
        <a-drawer
                title="编辑河流"
                :width="600"
                :confirmLoading="confirmLoading"
                @close="onCloseEditRiver"
                :visible="editRiverVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-form :form="form" layout="vertical" @submit="handleSubmitEdit">
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="河流名称">
                            <a-input
                                    placeholder="请输入河流名称"
                                    v-decorator="['name', { rules: [{ required: true, message: '请输入河流名称!' }],initialValue: initialName }]"
                            />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="支流编号">
                            <a-input
                                    placeholder="请输入支流编号"
                                    v-decorator="['branchGrade', { rules: [{ required: true, message: '请输入支流编号!' }],initialValue: branchGradeLevel }]"
                            />
                        </a-form-item>
                    </a-col>

                </a-row>
                <a-row :gutter="24" class="mb20">
                    <a-col :span="16">
                        河流经纬度信息
                    </a-col>
                    <a-col :span="8">
                        <a-button type="primary" class="fr" @click="showEditMap">
                            经纬度编辑
                        </a-button>
                    </a-col>
                </a-row>
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item  hasFeedback>
                            <a-input :disabled="disabled" v-model="riverAddressInfo" />
                        </a-form-item>
                    </a-col>

                </a-row>
            </a-form>
            <div class="layer_bottem_btn">
                <a-button class="mr08" @click="onCloseEditRiver">关闭</a-button>
                <a-button type="primary" @click="handleSubmitEdit">提交</a-button>
            </div>
        </a-drawer>
        <!--编辑河流地图层-->
        <a-modal
                title="河流经纬度信息"
                width="80%"
                :visible="editVisibleMapCon"
                :confirm-loading="confirmLoading"
                @ok="handleEditMap"
                @cancel="handleCancelEditMap"
        >
            <div class="map-body">
                <div id="db_edit_map"></div>
            </div>
        </a-modal>
    </div>
</template>

<script>
    import {
        axios
    } from '@/utils/request';
    import { httpAction } from '@/api/manage'
    export default {
        name: "riverConfig",
        data(){
            return{
                riverName: '',
                columns: [
                    {
                        title: '序号',
                        dataIndex: 'num',
                        key: 'num',
                        align: "center",
                        width: 80,
                        scopedSlots: { customRender: 'num'}
                    },
                    {
                        title: '河流名称',
                        dataIndex: 'riverName',
                        key: 'riverName',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: '支流编号',
                        dataIndex: 'riverLevel',
                        key: 'riverLevel',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: '添加人员',
                        dataIndex: 'createBy',
                        key: 'createBy',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: '添加时间',
                        dataIndex: 'createTime',
                        key: 'createTime',
                        align: "center",
                        ellipsis: true,
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        key: 'action',
                        align: "center",
                        width:240,
                        scopedSlots: { customRender: "action" }
                    },
                ],
                dataSource: [],
                loading: false,
                /* 分页参数 */
                ipagination: {
                    current: 1,
                    pageSize: 10,
                    showTotal: (total, range) => {
                        return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
                    },
                    showQuickJumper: true,
                    total: 0
                },
                riversDetails: {},
                addRiverVisible: false,
                riverAddressInfo: '',
                disabled: true,
                editRiverId: '',
                form: this.$form.createForm(this),
                confirmLoading: false,
                editRiverVisible: false,
                riverDetailsVisible: false,
                visibleMapCon: false,
                initialName: '',
                branchGradeLevel: '',
                map: null,
                drawingManager: null,
                overlays: [],
                positionValue: '',  //关键字
                editVisibleMapCon: false,
                editOverlays: [],
                polyline:null,
            }
        },
        computed: {
            validatorRules: function() {
                return {
                    name: { rules: [{ required: true, message: "请输入河流名称!" }] },
                    branchGrade: { rules: [{ required: true, message: "请输入支流编号!" }] },
                };
            }
        },
        methods:{
            //查询
            searchQuery(){
                this.ipagination.current = 1;
                this.dataSource = [];
                this.updata();
            },
            //重置
            searchReset(){
                this.riverName = '';
                this.ipagination.current = 1;
                this.updata();
            },
            //数据初始化
            updata() {
                this.getRiversList(this);
            },
            //表格属性改变
            handleTableChange(pagination){
                this.ipagination.current=pagination.current;
                this.updata();
            },
            //新建河流
            addRiver(){
                this.addRiverVisible = true;
            },
            onCloseAddRiver(){
                this.addRiverVisible = false;
                this.form.setFieldsValue({ name: '', branchGrade: '' })
                this.riverAddressInfo = '';
            },
            showAddMap(){
                this.visibleMapCon = true;
                this.$nextTick(() => {
                    this.initMap();
                });
            },
            //提交添加河流
            handleSubmitAdd() {
                const _this = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        _this.confirmLoading = true;
                        values.name = (values.name || '').trim();
                        values.branchGrade = (values.branchGrade || '').trim();
                        let riverAddressInfo = (_this.riverAddressInfo || '').trim();
                        if(!riverAddressInfo){
                            _this.$message.error("请创建河流经纬度!");
                            return false
                        }
                        let params = {
                            riverAddressInfo: _this.riverAddressInfo,
                            riverLevel: values.branchGrade,
                            riverName: values.name,
                        }
                        let formData = params;
                        httpAction('map/river/add', formData, 'post').then((res)=>{
                            if (res.code * 1 == 200) {
                                _this.riverName = '';
                                _this.ipagination.current = 1;
                                _this.updata();
                                _this.addRiverVisible = false;
                                _this.$message.success(res.message);
                            }else{
                                _this.$message.warning(res.message);
                            }
                        }).finally(() => {
                            _this.confirmLoading = false;
                            _this.close();
                        })
                    }
                })
            },
            //详情
            openRiverDetails(record){
                let _this = this;
                this.riverDetailsVisible = true;
                let params = {
                    id: record.id,
                }
                axios.get('map/river/query', { params: params}).then(res => {
                    if (res.code * 1 == 200) {
                        let list = res.data;
                        _this.riversDetails = list;
                        console.log(res.data)
                    } else {
                        _this.$message.info(res.message);
                    }
                })
            },
            onCloseRiverDetails(){
                this.riverDetailsVisible = false;
            },
            //确定河流经纬度
            handleAddMap(){
                //获取多边形的全部经纬度
                for(var i = 0; i < this.overlays.length; i++) {
                    var path = this.overlays[i].getPath();
                    for (var j = 0; j < path.length; j++) {
                        path[j].longitude = path[j].lng + '';
                        path[j].latitude = path[j].lat + '';
                        delete path[j].lng;
                        delete path[j].lat;
                    }
                    // console.log("河流经纬度集合=========")
                    console.log(path)
                    this.riverAddressInfo = JSON.stringify(path);
                    this.overlays[i].disableEditing(); //关闭编辑多边形
                }
                this.visibleMapCon = false;
            },
            handleCancelAddMap(){
                this.visibleMapCon = false;
            },
            overlaycomplete(e) {
                this.overlays.push(e.overlay);
                var path = e.overlay.getPath();//Array<Point> 返回多边型的点数组
                for(var i=0;i<path.length;i++) {
                    console.log( "lng：" + path[i].lng + "， lat：" + path[i].lat );
                }
                for(var i = 0; i < this.overlays.length; i++){
                    this.overlays[i].enableEditing()
                }
            },
            initMap(){
                this.map = new BMap.Map("dbmap", {
                    enableMapClick: false, // 禁止底图点击事件
                    maxZoom: 15
                });
                this.map.centerAndZoom(new BMap.Point(104.708198,31.395773), 14);
                this.map.enableScrollWheelZoom();
                this.map.enableMapClick = false;

                var styleOptions = {
                    strokeColor:"red",    //边线颜色。
                    fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
                    strokeWeight: 3,       //边线的宽度，以像素为单位。
                    strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
                    fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
                    strokeStyle: 'solid' //边线的样式，solid或dashed。
                }

                //实例化鼠标绘制工具
                this.drawingManager = new BMapLib.DrawingManager(this.map, {
                    isOpen: false, //是否开启绘制模式
                    enableDrawingTool: true, //是否显示工具栏
                    drawingToolOptions: {
                        anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
                        offset: new BMap.Size(5, 5), //偏离值
                        drawingModes : [ // 可见的操作选项
                            BMAP_DRAWING_POLYLINE,
                        ]
                    },
                    polylineOptions: styleOptions, //线的样式
                });
                //添加鼠标绘制工具监听事件，用于获取绘制结果
                this.drawingManager.addEventListener('overlaycomplete', this.overlaycomplete);
            },
            initEditMap(){
                var map = new BMap.Map('db_edit_map', {
                    enableMapClick: false, // 禁止底图点击事件
                    maxZoom: 15
                });
                var point = new BMap.Point(104.708198,31.395773);
                map.centerAndZoom(point, 14);
                map.enableScrollWheelZoom(true);
                // 绘制线
                var pois = [];
                let editOverlays = this.editOverlays
                for (var j = 0; j <  this.editOverlays.length; j++) {
                    pois.push(new BMap.Point(editOverlays[j].longitude, editOverlays[j].latitude));
                }
                this.polyline = new BMap.Polyline(pois, {
                    strokeColor: 'blue',
                    strokeWeight: 2,
                    strokeOpacity: 0.5
                });
                map.addOverlay(this.polyline);
                this.polyline.enableEditing();
            },
            //修改河流
            openEditRiver(record){
                this.editRiverVisible = true;
                this.editRiverId = record.id;
                this.editOverlays = JSON.parse(record.riverAddressInfo);
                this.initialName = record.riverName;
                this.branchGradeLevel = record.riverLevel;
                this.riverAddressInfo = record.riverAddressInfo;
                this.form.setFieldsValue({ name: record.riverName, branchGrade: record.riverLevel })
            },
            showEditMap(){
                this.editVisibleMapCon = true;
                this.$nextTick(() => {
                    this.initEditMap();
                });
            },
            onCloseEditRiver(){
                this.editRiverVisible = false;
                this.form.setFieldsValue({ name: '', branchGrade: '' })
                this.riverAddressInfo = '';
            },
            //提交编辑河流
            handleSubmitEdit() {
                const _this = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        _this.confirmLoading = true;
                        values.name = (values.name || '').trim();
                        values.branchGrade = (values.branchGrade || '').trim();
                        let params = {
                            id: _this.editRiverId,
                            riverAddressInfo: _this.riverAddressInfo,
                            riverLevel: values.branchGrade,
                            riverName: values.name,
                        }
                        axios.post('map/river/edit', params).then(res => {
                            if (res.code * 1 == 200) {
                                _this.riverName = '';
                                _this.ipagination.current = 1;
                                _this.updata();
                                _this.editRiverVisible = false;
                                _this.$message.success(res.message);
                            } else {
                                _this.$message.info(res.message);
                            }
                        })
                    }
                })
            },
            //确定修改河流经纬度
            handleEditMap(){
                var path = this.polyline.getPath();
                this.riverAddressInfo = JSON.stringify(path);
                //获取多边形的全部经纬度
                this.polyline.disableEditing();  //关闭编辑多边形
            },
            handleCancelEditMap(){
                this.editVisibleMapCon = false;
            },
            //获取河流列表
            getRiversList(){
                let _this = this;
                let params = {
                    pageNo: _this.ipagination.current,
                    pageSize: _this.ipagination.pageSize,
                    riverName: _this.riverName,
                }
                axios.get('map/river/list', {
                    params: params
                }).then(res => {
                         _this.loading = false;
                        if (res.code * 1 == 200) {
                            let list = res.data.records;
                            _this.dataSource = list;
                            _this.ipagination.total = Number(res.data.total);
                        } else {
                            _this.$message.info(res.message);
                        }

                    })
            },
            //删除河流
            deleteRiver(id){
                let _this = this;
                axios.delete('map/river/delete?id='+id)
                    .then(res => {
                        if (res.code * 1 == 200) {
                            _this.riverName = '';
                            _this.ipagination.current = 1;
                            _this.updata();
                            _this.$message.success(res.message);
                        } else {
                            _this.$message.info(res.message);
                        }
                    })
            }

        },
        mounted() {
            this.getRiversList();
        },
    }
</script>

<style scoped>
    @import "~@assets/less/common.less";
    .ml08{margin-left: 8px}
    .mr08{margin-right: 8px}
    .mb20{margin-bottom: 20px}
    .fr{float: right;}
    .over_hide{overflow: hidden;}
    .layer_bottem_btn{
        position: absolute;
        right: 0;
        bottom: 0;
        width: 100%;
        border-top: 1px solid #e9e9e9;
        padding: 10px 16px;
        background: #fff;
        text-align: right;
        z-index: 1
    }
    p {margin-bottom: 0em;}
    .map-body{
        width: 100%;
        height: 600px;
    }
    #dbmap,#db_edit_map{
        width: 100%;
        height: 90%;
    }
</style>