<template>
    <div class="margin12">
        <div class="screenCommonBox">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
                <a-form layout="inline" @keyup.enter.native="searchQuery">
                    <a-row :gutter="24">
                        <a-col :md="5" :sm="5">
                            <a-form-item label="区域名称">
                                <a-input placeholder="请输入区域名称" v-model="regionName"></a-input>
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
                <a-button @click="addArea" type="primary"><a-icon type="plus" />新增</a-button>
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
            <span slot="num" slot-scope="text, record, index">{{(ipagination.current-1)*ipagination.pageSize+parseInt(index)+1}}</span>
                <span slot="action" slot-scope="text, record">
                <a @click="openAreaDetails(record)">详情</a>
                <a-divider type="vertical"/>
                <a @click="openEditArea(record)">修改</a>
                <a-divider type="vertical"/>
                <a-popconfirm title="确定删除吗?" @confirm="() => deleteArea(record.id)">
                  <a>删除</a>
                </a-popconfirm>
            </span>
            </a-table>
        </a-card>
        <!--详情-->
        <a-drawer
                title="详情"
                :width="600"
                @close="onCloseAreaDetails"
                :visible="areaDetailsVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-row :gutter="24">
                <a-col :span="24">
                    <a-form-item label="区域名称">
                        <p>{{areaDetails.regionName}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="24">
                    <a-form-item label="区域经纬度">
                        <p>{{areaDetails.areaAddressInfo}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="添加人员">
                        <p>{{areaDetails.createBy}}</p>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="添加时间">
                        <p>{{areaDetails.createTime}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-row :gutter="24">
                <a-col :span="12">
                    <a-form-item label="修改人员">
                        <p>{{areaDetails.updateBy}}</p>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="修改时间">
                        <p>{{areaDetails.updateTime}}</p>
                    </a-form-item>
                </a-col>
            </a-row>
            <div class="layer_bottem_btn">
                <a-button class="mr08" @click="onCloseAreaDetails">
                    关闭
                </a-button>
            </div>
        </a-drawer>
        <!--新建区域-->
        <a-drawer
                title="新建区域"
                :width="600"
                :confirmLoading="confirmLoading"
                @close="onCloseAddArea"
                :visible="addAreaVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-form :form="form" layout="vertical" @submit="handleSubmitAdd">
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="区域名称">
                            <a-input
                                    placeholder="请输入区域名称"
                                    v-decorator="['name', { rules: [{ required: true, message: '请输入区域名称' }],initialValue: initialName }]"
                            />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row :gutter="24" class="mb20">
                    <a-col :span="16">
                        区域经纬度信息
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
                            <a-input disabled="ture" v-model="areaAddressInfo" />
                        </a-form-item>
                    </a-col>
                </a-row>
            </a-form>
            <div class="layer_bottem_btn">
                <a-button class="mr08" @click="onCloseAddArea">关闭</a-button>
                <a-button type="primary" @click="handleSubmitAdd">提交</a-button>
            </div>
        </a-drawer>
        <!--添加区域地图层-->
        <a-modal
                title="区域经纬度信息"
                width="80%"
                :visible="visibleMapCon"
                :confirm-loading="confirmLoading"
                @ok="handleAddMap"
                @cancel="handleCancelAddMap"
        >
            <div id="r-result" class="mb20">
                <a-input placeholder="请输入区域坐标内容"  @keyup.enter.native="searchAddr" v-model="positionValue" class="input-with-select">
                </a-input>
                <p class="mt10">例如(104.85341931818623, 31.357913458331296;104.83290615254367, 31.361919765100623;104.81903083580599, 31.379588549939218)</p>
            </div>
            <div class="map-body">
                <div id="dbmap"></div>
            </div>
        </a-modal>
        <!--编辑区域-->
        <a-drawer
                title="编辑区域"
                :width="600"
                :confirmLoading="confirmLoading"
                @close="onCloseEditArea"
                :visible="editAreaVisible"
                :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
        >
            <a-form :form="form" layout="vertical" @submit="handleSubmitEdit">
                <a-row :gutter="24">
                    <a-col :span="24">
                        <a-form-item label="区域名称">
                            <a-input
                                    placeholder="请输入区域名称"
                                    v-decorator="['name', { rules: [{ required: true, message: '请输入区域名称' }],initialValue: initialName }]"

                            />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row :gutter="24" class="mb20">
                    <a-col :span="16">
                        区域经纬度信息
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
                            <a-input :disabled="disabled" v-model="areaAddressInfo" />
                        </a-form-item>
                    </a-col>

                </a-row>
            </a-form>
            <div class="layer_bottem_btn">
                <a-button class="mr08" @click="onCloseEditArea">关闭</a-button>
                <a-button type="primary" @click="handleSubmitEdit">提交</a-button>
            </div>
        </a-drawer>
        <!--编辑区域地图层-->
        <a-modal
                title="区域经纬度信息"
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
        name: "areaConfig",
        data(){
            return{
                regionName: '',
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
                        title: '区域名称',
                        dataIndex: 'regionName',
                        key: 'regionName',
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
                areaDetails: {},
                addAreaVisible: false,
                areaAddressInfo: '',
                disabled: true,
                editAreaId: '',
                form: this.$form.createForm(this),
                confirmLoading: false,
                editAreaVisible: false,
                areaDetailsVisible: false,
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
                    name: { rules: [{ required: true, message: "请输入区域名称!" }] },
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
                this.regionName = '';
                this.ipagination.current = 1;
                this.updata();
            },
            //数据初始化
            updata() {
                this.loading = true;
                this.getAreaList(this);
            },
            //表格属性改变
            handleTableChange(pagination){
                this.ipagination.current=pagination.current;
                this.updata();
            },
            //查询地点事件
            searchAddr() {
                let editOverlays = this.positionValue;
                this.Polygon = new BMap.Polygon(editOverlays, {
                    fillColor: 'blue',
                    fillOpacity: 0.2
                })
                this.map.addOverlay(this.Polygon);
                this.Polygon.enableEditing();
            },
            //新建区域
            addArea(){
                this.addAreaVisible = true;
                this.initialName = '';
                this.areaAddressInfo = '';
            },
            onCloseAddArea(){
                this.addAreaVisible = false;
                this.form.setFieldsValue({ name: '' })
                this.areaAddressInfo = '';
            },
            showAddMap(){
                this.visibleMapCon = true;
                this.$nextTick(() => {
                    this.initMap();
                });
            },
            //提交添加区域
            handleSubmitAdd() {
                const _this = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        _this.confirmLoading = true;
                        values.name = (values.name || '').trim();
                        values.branchGrade = (values.branchGrade || '').trim();
                        let areaAddressInfo = (_this.areaAddressInfo || '').trim();
                        if(!areaAddressInfo){
                            _this.$message.error("请创建区域经纬度!");
                            return false
                        }
                        let params = {
                            regionAddressInfo: _this.areaAddressInfo,
                            regionName: values.name,
                        }
                        let formData = params;
                        httpAction('map/region/add', formData, 'post').then((res)=>{
                            if(res.code * 1 == 200){
                                _this.addAreaVisible = false;
                                _this.regionName = '';
                                _this.ipagination.current = 1;
                                _this.updata();
                                _this.initialName = '';
                                _this.areaAddressInfo = '';
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
            //打开详情
            openAreaDetails(record){
                let _this = this;
                this.areaDetailsVisible = true;
                let params = {id: record.id}
                axios.get('map/region/query', { params: params}).then(res => {
                    if (res.code * 1 == 200) {
                        let list = res.data;
                        _this.areaDetails = list;
                    } else {
                        _this.$message.info(res.message);
                    }
                })
            },
            onCloseAreaDetails(){
                this.areaDetailsVisible = false;
            },
            //确定区域经纬度
            handleAddMap(){
                var locationPlanA = "";
                //获取多边形的全部经纬度
                var path = this.overlays[0].getPath();
                // console.log(path)
                for(var j = 0; j < path.length; j++) {
                    locationPlanA += path[j].lng+','+path[j].lat+';'
                }
                this.overlays[0].disableEditing();  //关闭编辑多边形
                locationPlanA = locationPlanA.slice(0,locationPlanA.length-1);
                // console.log(locationPlanA + '全部多边形的经纬度');
                this.areaAddressInfo = locationPlanA;
                this.visibleMapCon = false;
            },
            handleCancelAddMap(){
                this.visibleMapCon = false;
            },
            overlaycomplete(e) {
                this.overlays.push(e.overlay);
                var locationPlanB = "";
                var path = e.overlay.getPath();//Array<Point> 返回多边型的点数组
                for(var j = 0; j < path.length; j++) {
                    locationPlanB += path[j].lng+','+path[j].lat+';'
                }
                locationPlanB = locationPlanB.slice(0,locationPlanB.length-1);
                this.overlays[0].enableEditing()//关闭编辑多边形
                this.areaAddressInfo = locationPlanB;
                this.editVisibleMapCon = false;
            },
            initMap(){
                this.map = new BMap.Map("dbmap", {
                    enableMapClick: false, // 禁止底图点击事件
                    maxZoom: 15
                });
                this.map.centerAndZoom(new BMap.Point(104.708198,31.395773), 12);
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
                            BMAP_DRAWING_POLYGON
                        ]
                    },
                    polygonOptions: styleOptions, //多边形的样式
                });
                //添加鼠标绘制工具监听事件，用于获取绘制结果
                this.drawingManager.addEventListener('overlaycomplete', this.overlaycomplete);
            },
            initEditMap(){
                this.map = new BMap.Map('db_edit_map', {
                    enableMapClick: false, // 禁止底图点击事件
                    maxZoom: 15
                });
                var point = new BMap.Point(104.708198,31.395773);
                this.map.centerAndZoom(point, 12);
                this.map.enableScrollWheelZoom(true);
                let editOverlays = this.editOverlays;
                this.Polygon = new BMap.Polygon(editOverlays, {
                    fillColor: 'blue',
                    fillOpacity: 0.2,
                    strokeOpacity: 0.5
                })
                this.map.addOverlay(this.Polygon);
                this.Polygon.enableEditing();
            },
            //修改区域
            openEditArea(record){
                this.editAreaVisible = true;
                this.editAreaId = record.id;
                this.editOverlays = record.regionAddressInfo;
                this.initialName = record.regionName;
                this.areaAddressInfo = record.regionAddressInfo;
                this.form.setFieldsValue({ name: record.regionName })
            },
            showEditMap(){
                this.editVisibleMapCon = true;
                this.$nextTick(() => {
                    this.initEditMap();
                });
            },
            onCloseEditArea(){
                this.editAreaVisible = false;
                this.form.setFieldsValue({ name: '' })
                this.areaAddressInfo = '';
            },
            //提交编辑区域
            handleSubmitEdit() {
                const _this = this;
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        _this.confirmLoading = true;
                        values.name = (values.name || '').trim();
                        let params = {
                            id: _this.editAreaId,
                            regionAddressInfo: _this.areaAddressInfo,
                            regionName: values.name,
                        }
                        axios.post('map/region/edit', params).then(res => {
                            if (res.code * 1 == 200) {
                                _this.regionName = '';
                                _this.ipagination.current = 1;
                                _this.updata();
                                _this.initialName = '';
                                _this.areaAddressInfo = '';
                                _this.editAreaVisible = false;
                                _this.$message.success(res.message);
                            } else {
                                _this.$message.info(res.message);
                            }
                        })
                    }
                })
            },
            //确定修改区域经纬度
            handleEditMap(){
                var locationPlanA = "";
                //获取多边形的全部经纬度
                var path = this.Polygon.getPath();
                // console.log(path)
                for(var j = 0; j < path.length; j++) {
                    locationPlanA += path[j].lng+','+path[j].lat+';'
                }
                locationPlanA = locationPlanA.slice(0,locationPlanA.length-1);
                this.Polygon.disableEditing();  //关闭编辑多边形
                // console.log(locationPlanA + '全部多边形的经纬度');
                this.areaAddressInfo = locationPlanA;
                this.editVisibleMapCon = false;
            },
            handleCancelEditMap(){
                this.editVisibleMapCon = false;
            },
            //获取区域列表
            getAreaList(){
                let _this = this;
                let params = {
                    pageNo: _this.ipagination.current,
                    pageSize: _this.ipagination.pageSize,
                    regionName: _this.regionName,
                }
                axios.get('map/region/list', {
                    params: params
                }).then(res => {
                    if (res.code * 1 == 200) {
                        let list = res.data.records;
                        _this.dataSource = list;
                        _this.ipagination.total = Number(res.data.total);
                    } else {
                        _this.$message.info(res.message);
                    }
                    _this.loading = false;
                })
            },
            //删除区域
            deleteArea(id){
                let _this = this;
                axios.delete('map/region/delete?id='+id)
                    .then(res => {
                        if (res.code * 1 == 200) {
                            _this.regionName = '';
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
            this.getAreaList();
        },
    }
</script>

<style scoped>
    @import "~@assets/less/common.less";
    .ml08{margin-left: 8px}
    .mr08{margin-right: 8px}
    .mt10{margin-top: 10px}
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
        height: 400px;
    }
    #dbmap,#db_edit_map{
        width: 100%;
        height: 97%;
    }
</style>