import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import {
    getDropAreasData,
    onWorkListMatterChange,
    onDelWorkListMatterChange,
    onWorkListQueryChange,
} from '@/api/workOrderManagement/problemManage/index.js'
function getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }
export default {
    name: "problemManage",
    mixins: [JeecgListMixin],
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
                title: '问题标题',
                align: "center",
                dataIndex: 'title'
            },{
                title: '提交人',
                align: "center",
                dataIndex: 'subName'
            },{
                title: '提交时间',
                align: "center",
                dataIndex: 'subTime'
            },{
                title: '处理人',
                align: "center",
                dataIndex: 'solveName'
            },{
                title: '处理时间',
                align: "center",
                dataIndex: 'solveTime'
            },{
                title: '处理状态',
                align: "center",
                dataIndex: 'statusDes'
            },{
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: {customRender: 'action'},
            }],
            url: {
                list: "/worklist/workListMatter/list",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            changeId: '', // 修改id
            workTeamId: '',
            assignId: '', // 列表行id
            previewVisible: false,
            previewImage: '',
            wrapStyle: {height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'},
            confirmLoading: false,
            upload_fileList: [], //图片列表 - 显示
            domianURl: '',
            statusList: [], //问题状态
            typeList: [], //问题类型
            levelList: [], //问题等级
            matterType: '',
            matterLevel: '',
            visible_line: false,
            title: '', //问题标题
            matterLongitude: '', //任务经度
            matterLatitude: '', //任务纬度
            description: '', //问题描述
            fileList: [], //图片列表
            equipmentId: '', //关联设备id
            equipmentList: [], //关联设备
            see_disabled: false,
            headers: {},
            spinning: true,
            videoType: 'mp4,mov,m4v,3gp,avi,m3u8,webm',
        }
    },
    mounted() {
        getDropAreasData('/worklist/workListMatter/equipments', 'equipmentList', this);
        getDropAreasData('/worklist/workListMatter/status', 'statusList', this);
        getDropAreasData('/worklist/workListMatter/matterType', 'typeList', this);
        getDropAreasData('/worklist/workListMatter/level', 'levelList', this);
        this.$nextTick(() => {
            this.domianURl = window._CONFIG['domianURL'];
        })
        const token = Vue.ls.get(ACCESS_TOKEN);
        this.headers = {"X-Access-Token":token}
    },
    computed: {
        lngLat() {
            return this.matterLongitude ? this.matterLongitude + '，' + this.matterLatitude : '' //发现地点的纬度
        }
    },
    methods: {
        
        handleCancel() {
            this.previewVisible = false;
        },
        async handlePreview(file) {
            if (!file.url && !file.preview) {
              file.preview = await getBase64(file.originFileObj);
            }
            this.previewImage = file.url || file.preview;
            this.previewVisible = true;
        },
        onImgUploadChange({ fileList, event } ) {
            this.upload_fileList = fileList;
            this.fileList = [];
            fileList.forEach(item => {
                if (item && item.url) {
                    let index = item.url.indexOf('/res/file')
                    this.fileList.push({ url: item.url.substr(index, item.url.length-1) })
                }
                if ( item && item.response && item.response.code == 200) {
                    this.fileList.push({ url: item.response.result.filePath })
                }
            })
        },

        //新增/修改弹框
        handleAdd(type, data) {
            this.visible = true;
            this.see_disabled = false;
            if (type == 'add') {
                this.spinning = false;
                this.drawerTitle = '新建问题';
            } else if (type == 'see') {
                this.drawerTitle = '查看问题';
                this.see_disabled = true;
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId);
            } else {
                this.drawerTitle = '修改问题';
                this.changeId = data.id;
                this.onWorkListQueryChange(this.changeId);
            }
        },

        onWorkListQueryChange(changeId) { //单条数据查询
            onWorkListQueryChange(changeId, this, data => {
                let fileList = [];
                let upload_fileList = [];
                this.title = data.title;
                this.matterType = data.matterType;
                this.matterLevel = data.matterLevel;
                this.matterLongitude = data.matterLongitude;
                this.matterLatitude = data.matterLatitude;
                this.description = data.description;
                data.fileList.forEach((item, index) => {
                    let upfile = {};
                    upfile.url = item.url
                    upfile.uid = '-' + (index+1);
                    upfile.name = 'image.png';
                    upfile.status = 'done';
                    upload_fileList.push(upfile);
                    fileList.push({url: item.url})
                })
                this.fileList = fileList;
                this.upload_fileList = upload_fileList;
                this.equipmentId = data.equipmentId;
                setTimeout(() => { this.spinning = false }, 50);
            });
        },

        onFormValidationChange(data, callBack) { //表单提交验证
            let message = '';
            if (!data.title) {
                message = '请输入问题标题!'
            } else if (data.title.length > 50) {
                message = '问题标题长度不能大于50，目前长度' + data.title.length
            } else if (!data.matterLongitude) {
                message = '请选择发现地点!'
            } else if (!data.matterType) {
                message = '请选择问题类型!'
            } else if (!data.matterLevel) {
                message = '请选择问题等级!'
            } else if (!data.description) {
                message = '请输入问题描述!'
            } else if (data.description.length > 10000) {
                message = '问题描述长度不能大于10000，目前长度' + data.description.length
            }
            if (message) {
                this.$message.info(message);
                return
            }
            callBack()
        },

        //提交新增/修改信息
        addSubmit() {
            let data;
            if (this.drawerTitle.indexOf('新建') != -1) {
                data = {
                    title: this.title, // 问题名称
                    matterType: this.matterType, //问题类型
                    matterLevel: this.matterLevel, //问题等级
                    matterLongitude: this.matterLongitude, //任务经度
                    matterLatitude: this.matterLatitude, //任务纬度
                    description: this.description, //问题描述
                    fileList: this.fileList || [], //图片列表
                    equipmentId: this.equipmentId, //关联设备id
                }
                this.onFormValidationChange(data, () => {
                    onWorkListMatterChange('add', data, this);
                });

            } else {
                data = {
                    id: this.changeId, //当前修改父级ID
                    title: this.title, // 问题名称
                    matterType: this.matterType, //问题类型
                    matterLevel: this.matterLevel, //问题等级
                    matterLongitude: this.matterLongitude, //任务经度
                    matterLatitude: this.matterLatitude, //任务纬度
                    description: this.description, //问题描述
                    fileList: [], //图片列表
                    equipmentId: this.equipmentId, //关联设备id
                }
                this.onFormValidationChange(data, () => {
                    let fileList = []
                    this.fileList.forEach(item => {
                        if (item.url.includes('http')) {
                            fileList.push({url: item.url.slice(item.url.indexOf('/res/file'))})
                        } else {
                            fileList.push(item)
                        }
                    })
                    data['fileList'] = fileList
                    onWorkListMatterChange('edit', data, this);
                });
            }
        },

        reset() { //重置弹层展示数据
            this.spinning = true;
            this.title = '';
            this.matterType = '';
            this.matterLevel = '';
            this.matterLongitude = '';
            this.matterLatitude = '';
            this.description = '';
            this.upload_fileList = '';
            this.fileList = '';
            this.equipmentId = '';
        },
        onCloseChange() {
            this.visible = false;
            this.reset();
        },

        onDeleteChange(id) { // 删除区域
            onDelWorkListMatterChange(id, this)
        },

        onConfirmLocationChange() {
            this.visible_line = true;
            this.$nextTick(() => {
                setTimeout(() => { this.newBMap() })
            })
        },

        newBMap() { //设置地图容器
            setTimeout(() => {
                let Map = new BMap.Map('problemManage-allmap', {
                    enableDblclickZoom: false,
                    minZoom:12.4,
                    maxZoom:15,
                    displayOptions: {
                        building: false
                    }
                });
                Map.centerAndZoom(new BMap.Point(104.7232, 31.4564), 11);
                Map.enableScrollWheelZoom(true);

                this.matterLongitude && Map.addOverlay(new BMap.Marker(new BMap.Point(this.matterLongitude, this.matterLatitude)));
                if (this.see_disabled) return //只读禁用地图点击事件
                Map.addEventListener('click', e => {
                    Map.clearOverlays();
                    Map.addOverlay(new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat)));
                    this.matterLatitude = e.point.lat; // 纬度
                    this.matterLongitude = e.point.lng; // 经度
                });
            });
        },

        lineModalCancel() {
            this.visible_line = false;
        },

        lineModalOk() {
            this.visible_line = false;
        },
        
        filterOption(input, option) {
            return (
              option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
            );
        },
    }
}