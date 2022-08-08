import ShiftsModal from '@/views/operationCenter/shiftManagement/modules/ShiftsModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { shiftsStatusChange } from '@/api/operationCenter-t/shiftManagement.js'

export default {
  name: "ShiftsList",
  mixins: [JeecgListMixin],
  components: {
    ShiftsModal
  },
  data() {
    return {
      description: '班次管理',
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
          title: '班次名称',
          align: "center",
          dataIndex: 'shiftsName'
        },
        {
          title: '班次描述',
          align: "center",
          dataIndex: 'shiftsDescribe'
        },
        {
          title: '起止时间',
          align: "center",
          dataIndex: 'startTimeOrendTime',
          scopedSlots: { customRender: 'startTimeOrendTime' },
        },
        {
          title: '班次状态',
          align: "center",
          dataIndex: 'shiftsStatus',
          scopedSlots: { customRender: 'shiftsStatus' },
        },

        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: {
        list: "/work/shifts/list",
        delete: "/settle/shifts/delete",
        deleteBatch: "/settle/shifts/deleteBatch",
        exportXlsUrl: "/work/shifts/export",
        importExcelUrl: "settle/shifts/importExcel",
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    //启停按钮改变
    shiftsStatusChange(data) {
      console.log(data);
      let res = {
        id: data.id,
        shiftsStatus: data.shiftsStatus ? '1' : '0'
      }
      //启停提交
      shiftsStatusChange(res, this);
    },
    //查看详情
    details(data) {
      this.$refs.modalForm.details(data);
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.disableSubmit = false;
    }
  }
}
//operationCenter/shiftManagement/shiftsList.vue 页面组件混入了operationCenter/shiftManagement/shiftsLis.js