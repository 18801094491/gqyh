import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import {
  closeWork,
  getBidSegmentData
} from "@api/workOrderManagement-t/notice.js";

export default {
  name: "problemReport",
  mixins: [JeecgListMixin],
  data() {
    return {
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function(t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "设备编号",
          align: "center",
          dataIndex: "equipmentSn",
        },
        {
          title: "所属标段",
          align: "center",
          dataIndex: "equipmentSectionName",
        },
        {
          title: "放置位置",
          align: "center",
          dataIndex: "equipmentLocation",
        },
        {
          title: "设备名称",
          align: "center",
          dataIndex: "equipmentName",
        },
        {
          title: "计划养护日期",
          align: "center",
          dataIndex: "planDate",
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/opt/upkeep/plan/adviseData",
      },
      dataSource: [],
      loading: false,
      visible: false,
      changeId: "", // 修改id
      bidSegmentList: [],
      assignId: "", // 列表行id
    };
  },
  mounted() {
    getBidSegmentData(this); //获取所属标段值
  },
  methods: {

    closeList(record) {
      // 关闭
      closeWork({ id: record.id }, this);
    },
  },
};
//workOrderManagement/notice/index.vue 页面组件混入了 workOrderManagement/notice/index.js