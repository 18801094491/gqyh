import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import {
    getCategory, updateSpare
} from '@/api/equipmentAccount-t/spareParts.js'

export default {
    name: "spareParts",
    mixins: [JeecgListMixin],
    data() {
        return {
            labelCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 5
                },
            },
            wrapperCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 16
                },
            },
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
            },
            {
                title: '名称',
                align: "center",
                dataIndex: 'categoryName'
            },
            {
                title: '备品备件',
                align: "center",
                dataIndex: 'sparepartsName'
            },
            {
                title: '备品备件型号',
                align: "center",
                dataIndex: 'sparepartsSpcesName',
            },
            {
                title: '备品备件规格',
                align: "center",
                dataIndex: 'sparepartsModelName'
            },
            {
                title: '操作',
                dataIndex: 'action',
                align: "center",
                scopedSlots: { customRender: 'action' },
            }
            ],
            url: {
                list: "/equipment/optSpareparts",
            },
            dataSource: [],
            loading: false,
            visible: false,
            drawerTitle: '',
            form: this.$form.createForm(this),
            model: {},
            changeId: '', // 修改id
            categoryName: '', // 名称
            sparepartsName: '', // 备品备件名称
            sparepartsModel: '', // 备件型号
            sparepartsSpces: '', // 备件规格
            sparepartsModelList: [], // 备件型号下拉
            sparepartsSpcesList: [], // 备件规格下拉
        }
    },
    mounted() {
        this.getSpcesList();
        this.getModelList();

    },
    methods: {
        getSpcesList() { // 备件规格
            let data = { pcode: 'A20' }
            getCategory(data, this, 'sparepartsSpcesList')
        },
        getModelList() {  // 备件型号
            let data = { pcode: 'A21' }
            getCategory(data, this, 'sparepartsModelList')
        },
        //新增/修改弹框
        handleAdd(type, data) {
            this.visible = true;
            if (type == 'add') {
                this.drawerTitle = '新增备品备件';
                this.reset();
            } else {
                this.drawerTitle = '修改备品备件';
                let res = {
                    id: data.id
                }
                this.changeId = data.id;
                this.categoryName = data.categoryName; // 名称
                this.sparepartsName = data.sparepartsName; // 备品备件名称
                this.sparepartsModel = data.sparepartsModel; // 备件型号
                this.sparepartsSpces = data.sparepartsSpces; // 备件规格
                //              this.reset();
            }
        },
        //提交新增/修改信息
        addSubmit() {
            let data;
            if (this.drawerTitle.indexOf('新增') != -1) {
                data = {
                    categoryName: this.categoryName, // 名称
                    sparepartsName: this.sparepartsName, // 备品备件名称
                    sparepartsModel: this.sparepartsModel, // 备件型号
                    sparepartsSpces: this.sparepartsSpces, // 备件规格
                }
                updateSpare(data, this);
            } else {
                data = {
                    categoryName: this.categoryName, // 名称
                    sparepartsName: this.sparepartsName, // 备品备件名称
                    sparepartsModel: this.sparepartsModel, // 备件型号
                    sparepartsSpces: this.sparepartsSpces, // 备件规格
                    id: this.changeId, //当前修改父级ID
                }
                updateSpare(data, this);
            }
        },
        //新增/修改重置页面内属性
        reset() {
            this.categoryName = ''; // 名称
            this.sparepartsName = ''; // 备品备件名称
            this.sparepartsModel = ''; // 备件型号
            this.sparepartsSpces = ''; // 备件规格
        },
        onClose() {
            this.visible = false;
        }
    }
}
//spareParts 页面组件混入的spareParts.js