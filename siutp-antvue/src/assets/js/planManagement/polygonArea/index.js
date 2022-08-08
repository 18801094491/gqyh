import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {
    onAddInspAreaChange,
    onEditInspAreaChange,
    onDelInspAreaChange,
} from '@/api/planManagement/polygonArea/index.js'
export default {
    name: "polygonArea",
    mixins: [JeecgListMixin],
    data() {
        return {
            columns: [{
                title: '序号',
                dataIndex: '',
                key: 'rowIndex',
                width: 60,
                align: "center",
                customRender: (t, r, index) => {
                    return parseInt(index) + 1;
                }
            },{
                title: '区域名称',
                align: "center",
                dataIndex: 'name'
            },{
                title: '区域编号',
                align: "center",
                dataIndex: 'code'
            },{
                title: '区域描述',
                align: "center",
                dataIndex: 'description',
                ellipsis: 'true',
                scopedSlots: { customRender: 'description' },
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: { customRender: 'action' },
            }],
            url: {
                list: "/inspection/inspArea/list",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            editId: '', // 修改id
            warnStatusList: [], // 状态下拉
            name: '', // 区域名称
            code: '', // 区域编号
            description: '', // 区域描述
            latitude: '', //中心点坐标纬度
            longitude: '', //中心点坐标经度
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            spinning: false
        }
    },
    mounted() {
    },
    methods: {
        handleAdd(type, data) { //新增/修改弹框
            this.visible = true;
            if (type == 'add') {
                this.drawerTitle = '新建区域';
            } else {
                this.drawerTitle = '修改区域';
                this.editId = data.id;
                this.name = data.name;
                this.code = data.code;
                this.latitude = data.latitude;
                this.longitude = data.longitude;
                this.description = data.description;
            }
            this.newBMap();
        },

        onFormValidationChange(data, callBack) { //数据验证
            let message = '';
            if (!data.name) {
                message = '请输入区域名称!'
            } else if (data.name.length > 20) {
                message = '区域名称长度不能大于20，目前长度' + data.name.length;
            } else if (!data.code) {
                message = '请输入区域编号!'
            } else if (data.code.length > 20) {
                message = '区域编号长度不能大于20，目前长度' + data.code.length
            } else if (!data.description) {
                message = '请输入区域描述!'
            } else if (data.description.length > 50) {
                message = '区域描述长度不能大于50，目前长度' + data.description.length
            } else if (!data.latitude) {
                message = '请在地图上选择区域坐标！'
            }
            if (message) {
                this.$message.info(message);
                return
            }
            callBack()
        },

        addSubmit() { //提交新增/修改信息
            let data;
            if (this.drawerTitle.indexOf('新建') != -1) {
                data = {
                    name: this.name,
                    code: this.code,
                    description: this.description,
                    latitude: this.latitude,
                    longitude: this.longitude,
                }
                this.onFormValidationChange(data, () => {
                    onAddInspAreaChange(data, this);
                })
            } else {
                data = {
                    name: this.name,
                    code: this.code,
                    description: this.description,
                    latitude: this.latitude,
                    longitude: this.longitude,
                    id: this.editId, //修改时的区域ID
                }
                this.onFormValidationChange(data, () => {
                    onEditInspAreaChange(data, this);
                })
            }
        },

        reset() { //新增/修改重置页面内属性
            this.name = '';
            this.code = '';
            this.description = '';
            this.latitude = '';
            this.longitude = '';
        },

        onCloseChange() { //关闭弹窗
            this.visible = false;
            this.reset();
        },

        onDeleteChange(id) { //删除区域
            onDelInspAreaChange(id, this)
        },

        newBMap() { //设置地图容器
            setTimeout(() => {
                let Map = new BMap.Map('polygonArea-allmap', {
                    enableDblclickZoom: false,
                    minZoom:12.4,
                    maxZoom:15,
                    displayOptions: {
                        building: false
                    }
                });
                Map.enableScrollWheelZoom(true);
                if (this.longitude) {
                    let point = new BMap.Point(this.longitude, this.latitude);
                    Map.centerAndZoom(point, 12.4);
                    Map.addOverlay(new BMap.Marker(point));
                } else {
                    Map.centerAndZoom(new BMap.Point(104.708198, 31.395773), 12.4);
                }

                Map.addEventListener('click', e => {
                    Map.clearOverlays();
                    Map.addOverlay(new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat)));
                    this.latitude = e.point.lat;
                    this.longitude = e.point.lng;
                });
            });
        },
    }
}