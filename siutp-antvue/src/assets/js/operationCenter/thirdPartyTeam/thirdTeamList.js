import ThirdTeamModal from '@/views/operationCenter/thirdPartyTeam/ThirdTeamModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { URL } from '@/api/operationCenter-t/thirdPartyTeam.js'

export default {
  name: "ThirdTeamList",
  mixins: [JeecgListMixin],
  components: {
    ThirdTeamModal
  },
  data() {
    return {
      description: '三方团队',
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
          title: '团队编号',
          align: "center",
          dataIndex: 'teamSn'
        },
        {
          title: '团队名称',
          align: "center",
          dataIndex: 'teamName'
        },
        {
          title: '协议起始日期',
          align: "center",
          dataIndex: 'agreeStart'
        },
        {
          title: '协议截至日期',
          align: "center",
          dataIndex: 'agreeEnd'
        },
        {
          title: '联系人姓名',
          align: "center",
          dataIndex: 'contactName'
        },
        {
          title: '联系人电话',
          align: "center",
          dataIndex: 'contactPhone'
        },
        {
          title: '联系人职位',
          align: "center",
          dataIndex: 'contactPosition'
        },
        {
          title: '团队评级',
          align: "center",
          dataIndex: 'teamRating'
        },
        {
          title: '资质',
          align: "center",
          dataIndex: 'fileName',
          scopedSlots: { customRender: 'fileName' },
        },

        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: URL,
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    handleEdit2: function (record, xq) {

      this.$refs.modalForm.edit(record, xq);
      this.$refs.modalForm.title = "详情";
      this.$refs.modalForm.disableSubmit = false;
    },
  }
}
 //operationCenter/thirdPartyTeam/thirdTeamList.vue 
 //页面组件混入了operationCenter/hirdPartyTeam/thirdTeamList.js