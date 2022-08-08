import {
  getOptStoreBillOut
} from '@/api/operationCenter-t/sparePartsManagement.js'
import searchReset from '@/mixins/searchReset.js'

export default {
  name: "equipmentAccountMaintenance",
  mixins: [searchReset],

  data() {
    return {
      description: '出库记录组件',
      drawerTitle: '新增信息',//新增或修改弹框title
      queryParam: {
        supplier: '0'
      },//搜索属性集合
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
          title: '备件名称',
          align: "center",

          dataIndex: 'sparepartName',

        },
        {
          title: '规格',
          align: "center",

          dataIndex: 'sparepartSpecs',

        },

        {
          title: '批次号',
          align: "center",

          dataIndex: 'batchSn',

        },
        {
          title: '所在仓库',
          align: "center",

          dataIndex: 'storeName',

        },
        {
          title: '出库量',
          align: "center",

          dataIndex: 'amount',

        },
        {
          title: '出库日期',
          align: "center",

          dataIndex: 'createTime',

        },
        {
          title: '经办人',
          align: "center",

          dataIndex: 'createBy',

        },



      ],//表格头部属性信息
      dataSource: [
        {
          supplierCode: '001',
          supplierName: '离心泵',
          supplierCategory: '机械设备',
          supplierBusinessLicenseNo: '泵',
          createdDate: 'xxx',
          supplyEquipment: '1标段',
          contacts: 'xx路泵房',
          contactNumber: 'XX公司'
        },

      ],//表格数据源
      loading: false,
      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: 10,
        // pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return " 共" + total + "条" + '  当前[' + range[0] + "-" + range[1] + ']'
        },
        showQuickJumper: true,
        // showSizeChanger: true,
        total: 0
      },
      form: this.$form.createForm(this),
      visible: false,//新增修改弹框显示隐藏值
      sparePartsWarehouseOutvisible: false,
    }
  },
  computed: {

  },
  mounted() {
    //初始化页面加载列表信息
    // this.updata();

  },
  methods: {
    //获取列表信息
    updata() {
      let data = {
        batchSn: $.trim(this.queryParam.batchSn),
        sparepartName: $.trim(this.queryParam.sparepartName),
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize
      }
      this.loading = true;
      getOptStoreBillOut(data, this);
    },
    //查询
    searchQuery() {
      this.ipagination.current = 1;
      this.updata();
    },

    //表格属性改变
    handleTableChange(pagination, filters, sorter) {
      this.ipagination.current = pagination.current;
      this.updata();
    },
    sparePartsWarehouseOutonClose() {
      this.sparePartsWarehouseOutvisible = false;
    }



  }

}
//operationCenter/sparePartsManagement/sparePartsWarehouseOutModel/index.vue 页面组件混入了operationCenter/sparePartsManagement/sparePartsWarehouseOutModel/index.js