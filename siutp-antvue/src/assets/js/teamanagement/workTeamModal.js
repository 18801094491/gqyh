import { httpAction } from '@/api/manage'
import pick from 'lodash.pick'
import moment from "moment"
import {
  URL,
  workTeamShiftsList,
  getUserData
} from '@/api/teamanagement/workTeamModal.js'
export default {
  name: "workTeamModal",
  data () {
    return {
      title:"操作",
      visible: false,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },

      confirmLoading: false,
      form: this.$form.createForm(this),
      validatorRules:{
      },
      url: URL,
      managerList:[],
     
      managerList2:[],

      workTeamShiftsList:[],
      userManageIds:[],
      userIds:[]
    }
  },
  created () {
  },
  mounted(){
    let data={
      name:''
    }
    // getStoreUserListData(data,this);
    workTeamShiftsList(this);
    getUserData(data,this);
  },
  methods: {
    add () {
      this.edit({});
    },
    edit (record) {
      console.log(record)
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.userManageIds=record.userManageIds?record.userManageIds.split(','):[];
      this.userIds  =record.userIds  ?record.userIds  .split(','):[];
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model,'teamName','teamDescribe','teamStatus','delFlag','shiftsId','userIds','userManageIds'))
        this.form.setFieldsValue({userIds:this.model.userIds?this.model.userIds.split(','):[]})
        this.form.setFieldsValue({userManageIds:this.model.userManageIds?this.model.userManageIds.split(','):[]})
    //时间格式化
        this.form.setFieldsValue({startTime:this.model.startTime?moment(this.model.startTime):null})
        this.form.setFieldsValue({overTime:this.model.overTime?moment(this.model.overTime):null})
      });

    },
    close () {
      this.$emit('close');
      this.visible = false;
    },
    handleOk () {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          
          if(!values.teamName){
            this.$message.info('班组名称不能为空!');
            return;
          }
          if(!values.shiftsId){
            this.$message.info('选择班次不能为空!');
            return;
          }
          if(!values.startTime){
            this.$message.info('班组有效期不能为空!');
            return;
          }
          if(!values.overTime){
            this.$message.info('班组有效期不能为空!');
            return;
          }
          if(!values.userIds.length){
            this.$message.info('选择人员不能为空!');
            return;
          }
          if(!values.userManageIds.length){
            this.$message.info('选择管理员不能为空!');
            return;
          }
          console.log(values.userIds,values.userManageIds,'================')
          if(values.userIds['0']==values.userManageIds['0']){
            this.$message.info('班组人员跟管理员不能一样');
            return;
          }
          let httpurl = '';
          let method = '';
          if(!this.model.id){
            httpurl+=this.url.add;
            method = 'post';
          }else{
            httpurl+=this.url.edit;
             method = 'post';
          }
          let formData = Object.assign(this.model, values);
          //时间格式化
          formData.startTime = formData.startTime?this.moment(formData.startTime).format('YYYY-MM-DD') :null;
          formData.overTime = formData.overTime?this.moment(formData.overTime).format('YYYY-MM-DD') :null;
          formData.userIds=formData.userIds.join(',');
          formData.userManageIds=formData.userManageIds.join(',');
          if(new Date(formData.startTime).getTime()>new Date(formData.overTime).getTime()){
            this.$message.info('有效期开始时间不得大于结束时间！');
            return;
          }
          that.confirmLoading = true;
          console.log(formData)
          httpAction(httpurl,formData,method).then((res)=>{
            if(res.success){
              that.$message.success(res.message);
              that.$emit('ok');
            }else{
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
            that.close();
          })



        }
      })
    },
    handleCancel () {
      this.close()
    },



  }
}
//teamanagement/workTeamModal.vue 页面组件混入了 teamanagement/workTeamModal.js