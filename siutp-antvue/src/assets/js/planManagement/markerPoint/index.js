import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {
    onInspPointChange,
    onDelInspPointChange,
    getDropAreasData,
} from '@/api/planManagement/markerPoint/index.js'
export default {
    name: "markerPoint",
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
                title: '巡检点名称',
                align: "center",
                dataIndex: 'name'
            },{
                title: '巡检点编号',
                align: "center",
                dataIndex: 'code'
            },{
                title: '所属区域',
                align: "center",
                dataIndex: 'areaName'
            },{
                title: '注意事项',
                align: "center",
                dataIndex: 'notices',
                ellipsis: 'true',
                scopedSlots: { customRender: 'notices' },
            },{
                title: '类型',
                align: "center",
                dataIndex: 'typeDes'
            },{
                title: '是否重点',
                align: "center",
                dataIndex: 'importDes'
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: { customRender: 'action' },
            }],
            url: {
                list: "/inspection/inspPoint/list",
            },
            dataSource: [],
            loading: true,
            visible: false,
            areaList: [], //所属区域列表下拉
            pointTypeList: [], //巡检点类型下拉
            equipmentList: [], //关联设备下拉
            drawerTitle: '',
            editId: '', //修改id
            name: '', //巡检点名称
            code: '', //巡检点编号
            areaId: '', //所属区域
            important: '', //是否重点
            type: '', //巡检点类型
            equipmentId: '', //关联设备id
            notices: '', //注意事项
            latitude: '', //纬度
            longitude: '', //经度
            importantList: [{ //是否重点
                code: 1, title: '是'
            },{
                code: 0, title: '否'
            }],
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            spinning: false,
        }
    },
    mounted() {
        getDropAreasData('/inspection/inspArea/all', 'areaList', this);
        getDropAreasData('/inspection/inspPoint/types', 'pointTypeList', this);
        getDropAreasData('/worklist/workListMatter/equipments', 'equipmentList', this);
    },
    methods: {
        handleAdd(type, data) { //新增/修改弹框
            this.visible = true;
            if (type == 'add') {
                this.drawerTitle = '新建巡检点';
            } else {
                this.drawerTitle = '修改巡检点';
                this.editId = data.id;
                this.name = data.name; 
                this.code = data.code; //巡检点编号
                this.areaId = data.areaId; //所属区域
                this.important = data.important; //是否重点
                this.type = data.type; //巡检点类型
                this.equipmentId = data.equipmentId; //关联设备id
                this.notices = data.notices; //注意事项
                this.latitude = data.latitude; //纬度
                this.longitude = data.longitude; //经度
                this.areaName = data.areaName; //所属区域名称
            }
            setTimeout(() => { this.newBMap() });
        },

        onFormValidationChange(data, callBack) { //数据验证
            let message = '';
            if (!data.name) {
                message = '请输入巡检点名称!'
            } else if (data.name.length > 20) {
                message = '巡检点名称长度不能大于20，目前长度' + data.name.length;
            } else if (!data.code) {
                message = '请输入巡检点编号!'
            } else if (data.code.length > 20) {
                message = '巡检点编号长度不能大于20，目前长度' + data.code.length;
            } else if (!data.areaId) {
                message = '请选择所属区域!'
            } else if (!data.important && data.important !== 0) {
                message = '请选择是否重点!'
            } else if (!data.type) {
                message = '请选择巡检点类型!'
            } else if (!data.notices) {
                message = '请输入注意事项!'
            } else if (!data.latitude) {
                message = '请在地图上选择巡检点坐标！'
            }
            if (message) {
                this.$message.info(message);
                return
            }
            callBack()
        },
        
        addSubmit() { //提交新增/修改信息
            let type = '';
            let data = {
                name: this.name,
                code: this.code,
                areaId: this.areaId,
                important: this.important,
                type: this.type,
                equipmentId: this.equipmentId,
                notices: this.notices,
                latitude: this.latitude,
                longitude: this.longitude
            };
            if ( this.drawerTitle.includes('修改') ) {
                type = 'edit'
                data['id'] = this.editId; //修改时的巡检点ID
            } else {
                type = 'add';
            }
            this.onFormValidationChange(data, () => {
                onInspPointChange(type, data, this);
            });
        },
        //新增/修改重置页面内属性
        reset() {
            this.name = '';
            this.code = '';
            this.areaId = '';
            this.important = '';
            this.type = '';
            this.equipmentId = '';
            this.notices = '';
            this.latitude = '';
            this.longitude = '';
        },
        onCloseChange() {
            this.visible = false;
            this.reset();
        },

        onDeleteChange(id) { // 删除巡检点
            onDelInspPointChange(id, this);
        },

        newBMap() { //设置地图容器
            let Map = new BMap.Map('markerPoint-allmap', {
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
                Map.centerAndZoom(point, 11);
                Map.addOverlay(new BMap.Marker(point));
            } else {
                Map.centerAndZoom(new BMap.Point(104.7232, 31.4564), 11);
            }
            Map.addEventListener('click', e => {
                Map.clearOverlays();
                Map.addOverlay(new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat)));
                this.latitude = e.point.lat; // 纬度
                this.longitude = e.point.lng; // 经度
            });
        },

        filterOption(input, option) {
            return (
              option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
            );
        },
    }
}