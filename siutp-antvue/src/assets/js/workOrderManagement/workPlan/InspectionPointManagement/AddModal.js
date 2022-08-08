import { httpAction} from "@/api/manage";
import { byIPointCategoryShowList } from '@api/workOrderManagement/workPlan/InspectionPointManagement/index.js'
import {
    BaiduMap
} from '@/assets/map.js';
var overlays = []
export default {
    name: "addModal",
    props: ['tplList', 'inPointCategoryList'],
    data() {
        return {
            title: '新增巡检点',
            recordFormItem: [],
            visible: false,
            treeData: [],
            model: {},
            labelCol: {
                xs: {span: 24},
                sm: {span: 5},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 16},
            },
            confirmLoading: false,
            form: this.$form.createForm(this),
            validatorRules: {},
            isShow: false,
            url: {
                add: "/work/workInspectionPoint/one"
            },
            childList: [],
            overlays: []
        }
    },
    methods: {
        add (record, type = 'add') {
            this.edit(record, type);
        },
        edit (record, type) {
            if (type !== 'add') {
                if(type === 'details') {
                    this.isShow = true
                }
                if (record.category * 1 === 4) {
                    this.model = Object.assign({}, record);
                    this.$nextTick(() => {
                        this.initMap(record.longitude, record.latitude, 'upd')
                    })
                } else {
                    byIPointCategoryShowList(this, {category: record.category}, record)
                }
            } else {
                this.model = Object.assign({}, record);
            }
            this.form.resetFields();
            this.visible = true;
        },
        close() {
            this.$emit('close');
            this.visible = false;
            this.isShow = false;
        },
        initMap (lng = 116.73060349115107, lat = 39.91123564554238, type) {
            var map = new BMap.Map("pointMap");
            var point = new BMap.Point(lng, lat);
            map.centerAndZoom(point, 16);
            map.enableScrollWheelZoom();
            var overlaycomplete = function(e){
                map.removeOverlay(overlays[0]);
                overlays[0] = e.overlay
            };
            // 在编辑和详情的时候添加选取点
            if (typeof type !== 'undefined') {
                var marker = new BMap.Marker(new BMap.Point(lng, lat));  // 创建标注
                overlays[0] = marker
                map.addOverlay(marker); // 将标注添加到地图中
            }
            // 非详情下可以编辑
            if (!this.isShow) {
                //实例化鼠标绘制工具
                var drawingManager = new BMapLib.DrawingManager(map, {
                    isOpen: true, //是否开启绘制模式
                    enableDrawingTool: false, //是否显示工具栏
                    drawingType: BMAP_DRAWING_MARKER
                });
                //添加鼠标绘制工具监听事件，用于获取绘制结果
                drawingManager.addEventListener('overlaycomplete', overlaycomplete);
            }
        },
        changeCategory (val) {
            this.model.dataId = undefined
            this.model.equipmentId = undefined
            this.model.latitude = undefined
            this.model.longitude = undefined
            overlays = []
            if (val * 1 === 4) {
                this.initMap()
            } else {
                byIPointCategoryShowList(this, {category: val})
            }
        },
        changeShow (val, option) {
            let data = this.childList[option.key]
            this.model.dataId = data.id
            this.model.equipmentId = data.equipmentId
            this.model.latitude = data.latitude
            this.model.longitude = data.longitude
            this.model = Object.assign({}, this.model)
        },
        handleOk() {
            if (!this.model.name) {
                this.$message.info('巡检点名称不能为空');
                return;
            }
            if (!this.model.category) {
                this.$message.info('巡检点类别不能为空');
                return;
            }
            if (this.model.category * 1 !== 4 && !this.model.dataId) {
                this.$message.info('根据类别显示不能为空');
                return;
            }
            if (this.model.category * 1 === 4 && !overlays.length) {
                this.$message.info('请在地图选择巡检点');
                return;
            }
            if (!this.model.tplId) {
                this.$message.info('数据模板不能为空');
                return;
            }
            let formData ={...this.model}
            if (this.model.category * 1 === 4) {
                let point = overlays[0].point
                formData.latitude = point.lat
                formData.longitude = point.lng
            }
            this.onSubmit(this.url.add, formData, 'post')
        },
        onSubmit(httpurl, formData, method) {
            let that = this
            that.confirmLoading = true;
            httpAction(httpurl, formData, method).then((res) => {
                if (res.success) {
                    that.$message.success(res.message);
                    that.confirmLoading = false;
                    overlays = []
                    that.close();
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