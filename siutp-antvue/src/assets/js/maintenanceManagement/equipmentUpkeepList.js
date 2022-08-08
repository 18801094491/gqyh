import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import {
  equipmentUpkeepOne,
  equipmentUpkeepOneUpLoad,
  equipmentUpkeepEquipList,
  sysdictUpkeepType,
  getUserData
} from '@/api/maintenanceManagement-t/equipmentUpkeepList.js'
export default {
  name: "EquipmentUpkeepList",
  mixins: [JeecgListMixin],

  data() {
    return {
      description: '维保管理',
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
          align: "center",
          dataIndex: 'equipmentName'
        },
        {
          title: '维保类型',
          align: "center",
          dataIndex: 'typeText'
        },
        {
          title: '维保内容',
          align: "center",
          dataIndex: 'upkeepContent'
        },
        {
          title: '维保原因',
          align: "center",
          dataIndex: 'upkeepReason'
        },
        {
          title: '维保结果',
          align: "center",
          dataIndex: 'upkeepResult'
        },
        {
          title: '上报人',
          align: "center",
          dataIndex: 'upkeepCreatorName'
        },
        {
          title: '上报日期',
          align: "center",
          dataIndex: 'upkeepTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          width: 150,

          scopedSlots: { customRender: 'action' },
        }
      ],
      url: {
        list: "/equipment/equipmentUpkeep",
        delete: "/equipment/equipmentUpkeep/delete",
        deleteBatch: "/equipment/equipmentUpkeep/deleteBatch",
        exportXlsUrl: "/equipment/equipmentUpkeep/export",
        importExcelUrl: "equipment/equipmentUpkeep/importExcel",
      },
      dataSource: [],
      title: "操作",
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      confirmLoading: false,
      form: this.$form.createForm(this),
      upkeepAttachList: [],
      upkeepUsers: '',
      visible2: false,
      showImgSrc: '',
      confirmLoading2: false,
      equipmentUpkeepQueryByStatusList: [],
      queryParam: {},
      equipmentUpkeepvisible: false,
      equipmentUpkeepTitle: '',
      fileList: [
        // {
        //   fileName:'111',
        //   filePath:require('@/assets/fmtupian.jpg'),
        //   fileThumbPath:require('@/assets/fmtupian.jpg')
        // },
        // {
        //   fileName:'111',
        //   filePath:require('@/assets/fmtupian.jpg'),
        //   fileThumbPath:require('@/assets/fmtupian.jpg')
        // }
      ],
      equipmentList: [],
      equipmentId: '',
      upkeepType: '',
      upkeepTypeList: [],
      upkeepUsers: [],
      upkeepCreator: '',
      managerList2: [],
      managerList: [],
      upkeepContent: '',
      upkeepReason: '',
      upkeepResult: '',
      changeId: '',
      model2: {}
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  mounted() {
    //新增修改里资产下拉获取
    let data = {
      equipmentSn: ''
    }
    equipmentUpkeepEquipList(data, this);
    //维保管理-新增维保记录-获取维保类型
    sysdictUpkeepType(this);
    //获取上报人,参与人下拉列表信息
    let data2 = {
      name: ''
    }
    getUserData(data2, this);
  },
  methods: {
    //维保详情-获取
    details(res) {
      // let data={
      //   upkeepId:res.id
      // }
      this.visible = true;
      // equipmentUpkeepQueryById(data,this);
      this.upkeepUsers = res.upkeepUsers;
      this.upkeepAttachList = res.list;
    },
    //维保详情-关闭
    handleCancel() {
      this.visible = false;
    },
    //附件上传图片预览
    showImg(data) {

      console.log(data);
      this.visible2 = true;
      this.showImgSrc = data;
    },
    //附件上传图片预览-关闭
    handleCancel2() {
      this.visible2 = false;
    },
    //新增维保信息
    equipmentUpkeep(type, record) {
      console.log(record)
      this.equipmentId = '';
      this.upkeepType = '';
      this.upkeepReason = '';
      this.upkeepResult = '';
      this.upkeepContent = '';
      this.upkeepCreator = '';
      this.upkeepUsers = [];
      this.changeId = '';
      this.fileList = [];
      if (type == 'xz') {
        this.equipmentUpkeepTitle = '新增维保信息';
      } else {
        this.equipmentUpkeepTitle = '修改维保信息';
        // let data={
        //     equipmentId:record.id
        // }
        // this.changeId=record.id;
        // equipmentUpkeepQueryByequId(data,this);
        this.equipmentId = record.equipmentId ? record.equipmentId : '';
        this.upkeepType = record.type ? record.type : '';
        this.upkeepReason = record.upkeepReason ? record.upkeepReason : '';
        this.upkeepResult = record.upkeepResult ? record.upkeepResult : '';
        this.upkeepContent = record.upkeepContent ? record.upkeepContent : '';
        this.upkeepCreator = record.upkeepCreator ? record.upkeepCreator : '';
        this.upkeepUsers = record.upkeepUsersId ? record.upkeepUsersId.split(',') : '';
        this.changeId = record.id;
        if (record.list.length) {
          record.list.map(index => {
            this.fileList.push({
              fileThumbPath: index.upkeepThumb,
              filePath: index.upkeepImage
            })
          })
        }
      }
      this.equipmentUpkeepvisible = true;
    },
    //新增维保信息-关闭
    equipmentUpkeeponClose() {
      this.equipmentUpkeepvisible = false;
    },
    //新增上传文件
    upfileClick(e) {

      let param = new FormData();
      for (let i = 0; i < e.target.files.length; i++) {
        let file = e.target.files[i];
        console.log(file)
        // if (file.type != 'application/pdf') {
        //     this.$message.error('请上传PDF格式文件!');
        //     $('.uploadBtn').val('');
        //     return;
        // };
        param.append('files', file);
      }

      $('#uploadBtn').val('');
      equipmentUpkeepOneUpLoad(param, this);
    },
    //删除上传文件
    removeFile(index) {
      this.fileList.splice(index, 1);
      $('#uploadBtn').val('');
    },
    //提交维保信息
    addSubmit() {
      if (!this.equipmentId) {
        this.$message.info('资产名称不能为空!');
        return;
      }
      if (!this.upkeepType) {
        this.$message.info('维保类型不能为空!');
        return;
      }
      if (!this.upkeepReason) {
        this.$message.info('维保原因不能为空!');
        return;
      }
      if (!this.upkeepResult) {
        this.$message.info('维保结果不能为空!');
        return;
      }
      if (!this.upkeepContent) {
        this.$message.info('维保内容不能为空!');
        return;
      }
      if (!this.upkeepCreator) {
        this.$message.info('上报人不能为空!');
        return;
      }
      if (!this.upkeepUsers.length) {
        this.$message.info('参与人不能为空!');
        return;
      }
      let data;
      let list = [];
      if (this.fileList.length) {
        this.fileList.map(index => {
          list.push({
            upkeepImage: index.filePath.substring(index.filePath.indexOf('/res')),
            upkeepThumb: index.fileThumbPath.substring(index.fileThumbPath.indexOf('/res'))
          })
        })
      }
      if (this.equipmentUpkeepTitle.indexOf('新增') != -1) {

        console.log(this.fileList)

        data = {
          equipmentId: this.equipmentId,
          list: list,
          type: this.upkeepType,
          upkeepContent: $.trim(this.upkeepContent),
          upkeepCreator: this.upkeepCreator,
          upkeepReason: $.trim(this.upkeepReason),
          upkeepResult: $.trim(this.upkeepResult),
          upkeepUsers: this.upkeepUsers.join(',')
        }
      } else {
        data = {
          equipmentId: this.equipmentId,
          list: list,
          type: this.upkeepType,
          upkeepContent: $.trim(this.upkeepContent),
          upkeepCreator: this.upkeepCreator,
          upkeepReason: $.trim(this.upkeepReason),
          upkeepResult: $.trim(this.upkeepResult),
          upkeepUsers: this.upkeepUsers.join(','),
          id: this.changeId
        }
      }
      console.log(data);
      equipmentUpkeepOne(data, this);
    }
  }
}
//equipmentUpkeepList 页面组件混入了equipmentUpkeepList.js