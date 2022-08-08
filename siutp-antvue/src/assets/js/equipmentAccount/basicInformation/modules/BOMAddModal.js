import { updateBOMList, getCategory, getSparepartsList } from '@/api/equipmentAccount-t/BOMAddModal.js'
export default {
  name: "BOMAddModal",
  props: ['openAddVisible', 'drawerAddTitle', 'BOMAddId', 'basicId', 'BOMAddRecord'],
  data() {
    return {
      visible: false,
      partList: [], // 类型下拉
      bomType: '', // 类型
      sparePartsList: [], // 备品备件下拉
      sparepartsId: '', // 备品备件
      drawerAddTitleNew: this.drawerAddTitle,
      BOMAddRecordNew: null
    }
  },
  watch: {
    openAddVisible(val) {
      if (val) {
        this.openModal()
      } else {
        this.visible = false
      }
    },
    BOMAddId(val) {

    },
    BOMAddRecord: {
      handler: function (newVal, oldVal) {
        this.BOMAddRecordNew = newVal;
        this.bomType = newVal.bomType;
        this.sparepartsId = newVal.sparepartsId;
      },
      deep: true,
    }
  },
  methods: {
    getList() {
      getCategory({ pcode: 'A19' }, this, 'partList')
      getSparepartsList(this)
    },
    closeModal() {
      this.$emit('closeModal', 'openAddVisible')
    },
    openModal(record = {}) {
      this.visible = true;
      this.bomType = '';
      this.sparepartsId = '';
    },
    addSubmit() { // 提交新增/修改信息
      let data;
      if (this.drawerAddTitle.indexOf('新增') != -1) {
        data = {
          bomType: this.bomType, // 类型
          sparepartsId: this.sparepartsId, // 备品备件信息
          basicId: this.basicId, //当前修改父级ID
        }
        updateBOMList(data, this, { id: this.basicId });
      } else {
        data = {
          bomType: this.bomType, // 类型
          id: this.BOMAddId,
          sparepartsId: this.sparepartsId, // 备品备件信息
          basicId: this.basicId, //当前修改父级ID
        }
        updateBOMList(data, this, { id: this.basicId });
      }
    },
    reset() {
      this.bomType = ''; // 类型
      this.sparepartsId = ''; // 备品备件信息
    },
  }
}
  //basicInformation/BOMAddModal页面组件混入的basicInformation/BOMAddModal.js