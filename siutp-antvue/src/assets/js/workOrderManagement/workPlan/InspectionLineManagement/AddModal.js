import { httpAction } from "@/api/manage";
import {byRouteIdgetSelectPointList} from '@api/workOrderManagement/workPlan/InspectionLineManagement/index.js'
import SelectPointList from '@views/workOrderManagement/workPlan/InspectionLineManagement/modules/SelectPointList.vue'
var overlays = []
export default {
    name: "addModal",
    components: { SelectPointList },
    data() {
        return {
            title: '添加巡检路线',
            pointDataList: [],
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: "center",
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1;
                    }
                },
                {
                    title: '巡检点名称',
                    align: "left",
                    dataIndex: 'name'
                },
                {
                    title: '设备',
                    align: "left",
                    dataIndex: 'equipmentName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: {customRender: 'action'},
                }
            ],
            loading: false,
            visible: false,
            model: {},
            confirmLoading: false,
            form: this.$form.createForm(this),
            isShow: false,
            url: {
                add: "/work/route/one"
            },
            map: null
        }
    },
    methods: {
        openPointDialog () {
            this.$refs.listModal.getList();
        },
        delSelectPoint (index) {
            this.pointDataList.splice(index,1)
        },
        selectPoint (list) {
            this.pointDataList = this.pointDataList.concat(list)
            this.addMaker(this.pointDataList)
        },
        // 创建标注
        addMaker (list) {
            list.map(one => {
                if (one.longitude && one.latitude) {
                    let marker = new BMap.Marker(new BMap.Point(one.longitude, one.latitude));
                    this.map.addOverlay(marker)
                }
            })
        },
        initMap (lng = 116.73060349115107, lat = 39.91123564554238, lines) {
            this.map = new BMap.Map("lineMap");
            var point = new BMap.Point(lng, lat);
            this.map.centerAndZoom(point, 16);
            this.map.enableScrollWheelZoom();
            // 在编辑和详情的时候添加线
            if (typeof lines !== 'undefined') {
                let pointArr = []
                lines.map(one => {
                    pointArr.push(new BMap.Point(one.longitude, one.latitude) )
                })
                var polyline = new BMap.Polyline(pointArr);
                overlays[0] = polyline
                this.map.addOverlay(polyline); // 将标注添加到地图中
            }
            // 非详情下可以编辑
            if (!this.isShow && typeof lines === 'undefined') {
                this.drawLine()
            }
        },
        // 重新绘制
        clearMapLine () {
            this.map.removeOverlay(overlays[0]);
            this.drawLine()
            overlays = []
        },
        drawLine () {
            function polylinecomplete(e) {
                overlays[0] = e
            }
            //实例化鼠标绘制工具
            var drawingManager = new BMapLib.DrawingManager(this.map, {
                isOpen: true, //是否开启绘制模式
                enableDrawingTool: false, //是否显示工具栏
                drawingType: BMAP_DRAWING_POLYLINE,
                drawingToolOptions: {
                    anchor: BMAP_DRAWING_CIRCLE, //位置
                    offset: new BMap.Size(5, 5), //偏离值
                }
            });
            drawingManager.setDrawingMode(BMAP_DRAWING_POLYLINE)
            //添加鼠标绘制工具监听事件，用于获取绘制结果
            drawingManager.addEventListener('polylinecomplete', polylinecomplete);
        },
        add (record, type = 'add') {
            this.edit(record, type);
        },
        edit (record, type) {
            if (type !== 'add') {
                if(type === 'details') {
                    this.isShow = true
                }
                this.model = Object.assign({}, record);
                // 详情画线
                if (record.mapLines) {
                    let mapLines = JSON.parse(record.mapLines)
                    this.$nextTick(() => {
                        this.initMap(mapLines.centerPoint.longitude, mapLines.centerPoint.latitude, mapLines.lines)
                    })
                } else {
                    this.$nextTick(() => {
                        this.initMap()
                    })
                }
                // 巡检点列表
                byRouteIdgetSelectPointList(this, record.id)
            } else {
                this.model = Object.assign({}, record);
                this.$nextTick(() => {
                    this.initMap()
                })
            }
            this.form.resetFields();
            this.visible = true;
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.isShow = false;
        },
        handleOk() {
            if (!this.model.name) {
                this.$message.info('线路名称不能为空');
                return;
            }
            if (!this.pointDataList.length) {
                this.$message.info('请选择巡检点');
                return;
            }
            if (!overlays.length) {
                this.$message.info('请在地图绘制路线');
                return;
            }
            let formData ={...this.model}
            let lines =  overlays[0].getPath().map(one => ({
                longitude: one.lng,
                latitude: one.lat
            }))
            let maplines = {
                centerPoint: [lines[lines.length - 1].longitude, lines[lines.length - 1].latitude],
                lines: lines
            }
            formData.mapLines = JSON.stringify(maplines)
            formData.pointIds = this.pointDataList.map(one => one.id).join(',')
            this.onSubmit(this.url.add, formData, 'post')
        },
        onSubmit(httpurl, formData, method) {
            let that = this
            that.confirmLoading = true;
            httpAction(httpurl, formData, method).then((res) => {
                if (res.success) {
                    that.$message.success(res.message);
                    that.confirmLoading = false;
                    that.close();
                    this.pointDataList = []
                    overlays = []
                    this.$emit("ok");
                } else {
                    that.$message.warning(res.message);
                }
            }).finally(() => {
                // that.confirmLoading = false;
                // that.close();
            })
        },
        handleCancel() {
            this.close()
        }
    }
}