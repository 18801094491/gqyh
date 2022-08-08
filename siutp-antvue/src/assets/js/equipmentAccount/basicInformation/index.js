import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import AddModal from '@/views/equipmentAccount/basicInformation/modules/AddModal'
import BomListModal from '@/views/equipmentAccount/basicInformation/modules/BOMListModal'
import AssetInfoAddModal from '@/views/equipmentAccount/components/AssetInformationAddModal'

import {
    deleteAction,
    getQueryNameList,
    getEquipmentTypeData,
    getEquipmentSelect
} from '@/api/equipmentAccount-t/basicInformation.js';

export default {
    name: "basicInformation",
    mixins: [JeecgListMixin],
    components: { AddModal, AssetInfoAddModal, BomListModal },
    data() {
        return {
            supplierClassificationList: [],
            equipmentTypeList: [],
            equipmentModelList: [],
            equipmentSpecsList: [],
            add_equipmentModelList: [],
            add_equipmentSpecsList: [],
            asset_equipmentModelList: [],
            asset_equipmentSpecsList: [],
            // 表头
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
                    title: '资产名称',
                    align: "left",
                    dataIndex: 'baseName'
                },
                {
                    title: '设备类型',
                    align: "center",
                    dataIndex: 'equipmentTypeName'
                },
                {
                    title: '供应商',
                    align: "left",
                    dataIndex: 'equipmentSupplierName'
                },
                {
                    title: '设备型号',
                    align: "center",
                    dataIndex: 'equipmentModelName'
                },
                {
                    title: '设备规格',
                    align: "center",
                    dataIndex: 'equipmentSpecsName'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: "center",
                    scopedSlots: { customRender: 'action' },
                }
            ],
            url: {
                list: "/equipment/baseInfo"
            },
            expireDateVisible: false,
            confirmLoading: false,
            form: {
                setExpireDate: null,
            },
            BOMListVisible: false,
            BOMListRecord: {},
            delVisible: false,
            delId: '', // 删除id
        }
    },
    mounted() {
        getQueryNameList(this);
        getEquipmentTypeData(this);
    },
    methods: {
        // ok
        assetOk() {
            //           this.$router.push({
            //              name: "operationCenter-equipmentAccountMaintenance-index",
            //          })
        },
        /**
         * 改变类型
         */
        changeType(val, type) {
            let listName = type ? `${type}equipmentModelList` : 'equipmentModelList'
            if (!type) {
                this.equipmentModelList = []
                this.equipmentSpecsList = []
                this.queryParam.equipmentSpecs = undefined
                this.queryParam.equipmentModel = undefined
            } else {
                this[`${type}equipmentModelList`] = []
                this[`${type}equipmentSpecsList`] = []
            }
            getEquipmentSelect(val, this, listName)
        },
        /**
         * 改变模型
         */
        changeMondel(val, type) {
            let listName = type ? `${type}equipmentSpecsList` : 'equipmentSpecsList'
            if (!type) {
                this.equipmentSpecsList = []
                this.queryParam.equipmentSpecs = undefined
            } else {
                this[`${type}equipmentSpecsList`] = []
            }
            getEquipmentSelect(val, this, listName)
        },
        changeSpecs(val) {
            this.queryParam = Object.assign({}, this.queryParam)
            console.log(val, this.queryParam.equipmentSpecs)
        },
        /**
         * 打开想起个对画框 
         * */
        details(data) {
            this.$refs.modalForm.edit(data, 'details');
            this.$refs.modalForm.title = "详情";
            this.$refs.modalForm.disableSubmit = false;
        },
        /**
         * 删除
         */
        deleteRecord(item) {
            this.delVisible = true;
            this.delId = item.id;
            //          let that = this
            //          this.$confirm({
            //              title: "确认删除",
            //              content: "删除基本信息会删除该类型的全部设备!",
            //              onOk: function () {
            //                  deleteAction(that.url.list + '/' + item.id, {}).then((res) => {
            //                      if (res.success) {
            //                          that.$message.success(res.message);
            //                          that.loadData();
            //                          that.onClearSelected();
            //                      } else {
            //                          that.$message.warning(res.message);
            //                      }
            //                  });
            //              }
            //          });
        },
        /**
         * 删除
         */
        delOk() {
            deleteAction(this.url.list + '/' + this.delId, {}).then((res) => {
                if (res.success) {
                    this.delVisible = false;
                    this.$message.success(res.message);
                    this.loadData();
                    //                  this.onClearSelected();
                } else {
                    this.$message.warning(res.message);
                }
            });
        },
        /**
         * 取消删除
         */
        delCancel() {
            this.delVisible = false;
        },
        /**
         * 打开添加对话框
         */
        openAasetAddModal(record) {
            record.baseId = record.id
            this.$refs.assetInfoAddModal.edit(record, 'add')
        },
        /**
         * bom清单
         */
        BOMList(record) { // BOM清单
            this.BOMListVisible = true;
            this.BOMListRecord = Object.assign({}, this.BOMListRecord, record)
        },
        closeBOMListModal(modalVisible) {
            this[modalVisible] = false;
        },
    }
}
//basicInformation/index.vue 页面组件混入的basicInformation/index.js