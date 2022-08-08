 import{
    URL
  } from '@/api/maintenanceManagement/modules/equipmentUpkeepModal.js'
  
  import pick from 'lodash.pick'
  import moment from "moment"
  export default {
    name: "EquipmentUpkeepModal",
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 3},
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
        imgList:[],
        visible2:false,
        showImgSrc:'',
        confirmLoading2:false
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'equipmentId','upkeepCreator','upkeepContent','upkeepReason','upkeepResult'))
		  //时间格式化
          this.form.setFieldsValue({upkeepTime:this.model.upkeepTime?moment(this.model.upkeepTime):null})
        });

      },
      close () {
        this.$emit('close');
        this.visible = false;
      },

      handleCancel () {
        this.close()
      },
      showImg(data){
        console.log(data);
        this.visible2 = true;
        this.showImgSrc=data;
      },
      handleCancel2(){
        this.visible2 = false;
      }

    }
}