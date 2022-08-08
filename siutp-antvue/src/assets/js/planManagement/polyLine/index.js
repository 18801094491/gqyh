import OptMarkerModal from '@/views/planManagement/polyLine/modules/OptMarkerModal'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {
    onInspRouteChange,
    getDropAreasData,
    getInspPointData,
    onDelInspRouteChange,
    getInspRouteData,
} from '@/api/planManagement/polyLine/index.js'

export default {
    name: "polyLine",
    mixins: [JeecgListMixin],
    components: {
      OptMarkerModal
    },
    data() {
        return {
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
                title: '线路名称',
                align: "center",
                dataIndex: 'name'
            },{
                title: '线路编号',
                align: "center",
                dataIndex: 'code'
            },{
                title: '所属区域',
                align: "center",
                dataIndex: 'areaName'
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: {customRender: 'action'},
            }],
            ipagination: {
                current: 1,
                pageSize: 10,
                showTotal: (total, range) => {
                    return   " 共" + total + "条"+'  当前['+range[0] + "-" + range[1]+']'
                },
                showQuickJumper: true,
                total: 0
            },
            pointListColumns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                customRender: function (t, r, index) {
                    return parseInt(index) + 1;
                }
            },{
                title: '巡检点名称',
                align: "center",
                dataIndex: 'name'
            },{
                title: '巡检点编号',
                align: "center",
                dataIndex: 'code'
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: {
                    customRender: 'action'
                },
            }],
            pointListDataSource: [],
            url: {
                list: "/inspection/inspRoute/list",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            see_disabled: false,
            areaList: [], //所属区域列表
            pointList: [], //所属区域下全部的巡检点列表
            selectedPointIdList: [], //选中的巡检点列表 - 请求接口
            selectedPointList: [], //选中的巡检点列表 - 列表展示/绘制地图
            cachePointIdList: [], //缓存列表
            changeId: '', // 修改id
            name: '', //线路名称
            code: '',//线路编号
            areaId: '', //所属区域
            Map: null,
            markers: [],
            visible_line: false,
            confirmLoading: false,
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            mapIcon: require('@/assets/images/mapIndex/iconHDot.png'),
            spinning: true,
        }
    },
    mounted() {
        getDropAreasData('/inspection/inspArea/all', 'areaList', this);
    },
    methods: {
        //新增/修改弹框
        handleAdd(type, data) {
            this.visible = true;
            this.see_disabled = false;
            if (type == 'add') {
                this.spinning = false;
                this.drawerTitle = '新建线路';
            } else if (type === 'see') {
                this.see_disabled = true;
                this.drawerTitle = '查看线路';
                this.changeId = data.id;
                this.getInspRouteData(this.changeId)
            } else {
                this.drawerTitle = '修改线路';
                this.changeId = data.id;
                this.getInspRouteData(this.changeId)
            }
        },
        getInspRouteData(changeId) {//单条数据查询
            getInspRouteData(changeId, this, data => { 
                this.selectedPointList = data.pointList || [];
                this.name = data.name;
                this.code = data.code;
                this.areaId = data.areaId;
                this.onSelectedPointChange(this.selectedPointList);
                setTimeout(() => { this.spinning = false }, 50);
            });
        },

        onFormValidationChange(data, callBack) {
            let message = '';
            if (!data.name) {
                message = '请输入线路名称!'
            } else if (data.name.length > 20) {
                message = '线路名称长度不能大于20，目前长度' + data.name.length;
            } else if (!data.code) {
                message = '请输入线路编号!'
            } else if (data.code.length > 20) {
                message = '线路编号长度不能大于20，目前长度' + data.code.length;
            } else if (!data.areaId) {
                message = '请选择所属区域!'
            } else if (!data.pointList.length) {
                message = '请选择巡检点！'
            } else {
                data.pointList.forEach(item => {
                    if (!item.seq) { message = '请完善线路编辑！' }
                })
            }
            if (message) {
                this.$message.info(message);
                return
            }
            callBack()
        },
        //提交新增/修改信息
        onAddSubmitChange() {
            let data;
            if (this.drawerTitle.indexOf('新建线路') != -1) {
                data = {
                    name: this.name, //线路名称
                    code: this.code, //线路编号
                    areaId: this.areaId, //所属区域id
                    pointList: this.selectedPointIdList, //所选巡检点
                }
                this.onFormValidationChange(data, () => {
                    onInspRouteChange('add', data, this);
                })
            } else {
                data = {
                    id: this.changeId, //当前操作ID
                    name: this.name, //线路名称
                    code: this.code, //线路编号
                    areaId: this.areaId, //所属区域id
                    pointList: this.selectedPointIdList, //所选巡检点
                }
                this.onFormValidationChange(data, () => {
                    onInspRouteChange('edit', data, this);
                })
            }
        },
        //新增/修改重置页面内属性
        reset() {
            this.spinning = true;
            this.changeId = '';
            this.name = '';
            this.code = '';
            this.areaId = '';
            this.pointList = [];
            this.selectedPointList = [];
            this.selectedPointIdList = [];
            this.markers = [];
            // this.areaList = [];
            this.onSelectedPointChange([], 'null')
        },
        onCloseChange() {
            this.visible = false;
            this.reset();
        },

        onDeleteChange(id) { // 删除区域
            onDelInspRouteChange(id, this);
        },

        onSwitchAreaChange() { //选择所属区域
            this.selectedPointList = []; //切换时清空已选巡检点
            this.selectedPointIdList = []; //切换时清空已选巡检点
        },

        onShowMarkerModal() { //选择巡检点 - 弹层展示
            if (!this.areaId) {
                this.$message.info('请先选择所属区域！');
                return;
            }
            getInspPointData({areaId: this.areaId}, this, data => { //获取所选区域下所有巡检点
                this.pointList = data;
                this.$refs.modalForm.showModal();
                this.$refs.modalForm.title = "选择巡检点";
                this.$refs.modalForm.disableSubmit = false;
            });
        },

        onSearchQueryChange(data) { //查询所选区域下所有巡检点 - 子组件
            getInspPointData(data, this, data => {
                this.pointList = data;
            });
        },

        onSelectedPointChange(selectedPointList, type) { //设置已选巡检点列表
            let newRows = selectedPointList || [];
            this.selectedPointIdList = [];
            if (type !== 'null') {
                let map = new Map();
                newRows = this.selectedPointList.concat(newRows);
                newRows = newRows.filter(item => !map.has(item.id) && map.set(item.id, 1)) //已选项变动 - 合并去重
            }
            newRows.forEach((item) => {
                this.selectedPointIdList.push({pointId: item.id, seq: item.seq || '' }) //选中的问题 - 请求接口
            })
            this.selectedPointList = newRows; //选中的问题 - 展示
            console.log('设置已选巡检点列表 id:', this.selectedPointIdList)
            return

            this.selectedPointIdList = [];
            let setSeq = this.drawerTitle.indexOf('修改') != -1; 
            selectedPointList.forEach((item, index) => {
                // this.selectedPointIdList.push({pointId: item.id, seq: index + 1 }) //选中巡检点 - 请求接口
                this.selectedPointIdList.push({pointId: item.id, seq: setSeq ? (index+1) : '' }) //选中巡检点 - 请求接口
            })
            this.selectedPointList = selectedPointList; //选中巡检点 - 展示
        },

        onSelPointDelChange(pointId) { //选择的巡检点列表 - 删除操作
            let selectedPointList = this.selectedPointList;
            selectedPointList = selectedPointList.filter(item => {
                if (item.id != pointId) {
                    return item
                }
            })
            this.selectedPointList = selectedPointList
            // this.onRemoveOverlayChange();
            this.onSelectedPointChange(selectedPointList, 'null')
        },

        newBMap(flag) { //线路编辑
                if (this.markers.length) this.markers = [];
                let markers = [];
                let Map = null;
                this.Map = null;
                let seq = 0;
                let mapData = this.selectedPointList;
                Map = new BMap.Map('polyLine-allmap', {
                    enableDblclickZoom: false,
                    minZoom:12.4,
                        maxZoom:15,
                    displayOptions: {
                        building: false
                    }
                });
                mapData.forEach(item => {
                    if (item.seq) {
                        seq = item.seq;
                    }
                })
                setTimeout(() => {
                    Map.centerAndZoom(new BMap.Point(mapData[0].longitude, mapData[0].latitude), 13); //创建地图
                    Map.enableScrollWheelZoom(true); //开启缩放
                    let myIcon = new BMap.Icon(this.mapIcon, new BMap.Size(26, 26)); //标注样式
                    mapData.forEach((item, index) => { //遍历标注
                        let point = new BMap.Point( item.longitude, item.latitude );
                        let marker = new BMap.Marker( point, { enableMassClear: false } );
                        if (item.seq) {
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
                            var polyline = new BMap.Polyline(markers, { //依据标注连线
                                strokeColor: '#4149c2',
                                strokeWeight: 2,
                                strokeOpacity: 1
                            });
                            Map.clearOverlays();
                            Map.addOverlay(polyline); //创建折线
                            this.markers = markers;
                        }
                        marker.addEventListener('click', (data) => {
                            if ( JSON.stringify(this.markers).indexOf(JSON.stringify(point)) == -1 ) {
                                this.markers.push(point)
                                this.selectedPointIdList.forEach(item2 => {
                                    if (item.id == item2.pointId) {
                                        ++seq
                                        item2.seq = seq;
                                        return;
                                    }
                                })
                            }
                        });
                        Map.addOverlay(marker)
                    });
                })
                this.Map = Map;
        },
        onRemoveOverlayChange() {
            if (this.Map) {
                this.Map.clearOverlays();
                this.selectedPointIdList.forEach(item => {
                    item.seq = ''
                })
                this.selectedPointList.forEach(item => {
                    item.seq = ''
                })
                setTimeout(() => { this.newBMap(true) });
            }
        },

        onShowPolyLineModal() {
            if (!this.selectedPointList.length) {
                this.$message.info('请先选择巡检点！');
                return;
            }
            this.visible_line = true;
            this.$nextTick(() => { this.newBMap(false) })
        },

        lineModalCancel () {
            this.visible_line = false;
        },
        
        lineModalOk() {
            let message = '';
            this.visible_line = false;
            this.selectedPointIdList.forEach(item => {
                if (!item.seq) { 
                message = '请完善线路编辑！';
                }
            })
            if (message) {
                this.visible_line = true;
                this.$message.info(message);
                return;
            }
            let compare = function (obj1, obj2) {
                if (obj1.seq < obj2.seq) { return -1;
                } else if (obj1.seq > obj2.seq) { return 1;
                } else { return 0 }
            } 
            this.selectedPointList.forEach(item => { //用于地图遍历标注和已选表格展示
                this.selectedPointIdList.forEach(item2 => { 
                    if (item.id == item2.pointId) {
                        item.seq = item2.seq;
                    }
                })
            })
            this.selectedPointList.sort(compare);
            this.selectedPointList.forEach((item, index) => {
                item.seq = index +1
            });
            this.selectedPointIdList.sort(compare);
            this.selectedPointIdList.forEach((item, index) => { //用于提交接口
                item.seq = index +1
            });
            console.log('线路编辑提交----： ', this.selectedPointList, this.selectedPointIdList)
        },
    },
    watch: {
        markers() {
            var polyline = new BMap.Polyline(this.markers, { //依据标注连线
                strokeColor: '#4149c2',
                strokeWeight: 2,
                strokeOpacity: 1
            });
            if (this.Map) {
                this.Map.clearOverlays();
                this.Map.addOverlay(polyline); // 增加折线
            }
        }
    }
}