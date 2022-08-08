<template>
  <a-modal
    class="taskDetails-modal"
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :footer="null"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" class="textal-left">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.title}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="关联设备" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.equipmentName}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.typeDes}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.statusDes}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="提出人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.subName}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="解决人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.solveName}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="提出时间" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.subTime}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="解决时间" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.solveTime}}</span>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="提出描述" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.description}}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="解决描述" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <span>{{taskDetailsData.solveDesc}}</span>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="问题经纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <img :src="mapIconR" alt="">
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="解决经纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <img :src="mapIconB" alt="">
            </a-form-item>
          </a-col>
        </a-row>
        <div class="taskDetails-allmap" id="matter-allmap"></div>

        <a-row :gutter="24" v-if="this.taskDetailsData.fileList && this.taskDetailsData.fileList.length">
          <a-col :span="24">
            <a-upload
              accept="image/*"
              list-type="picture-card"
              :file-list="fileList"
              :disabled="true"
              @preview="handlePreview">
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="previewCancel">
                <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  export default {
    name: "modalTaskDetails",
    props: ['taskDetailsData'],
    data () {
      return {
        title: "",
        fileList: [],
        previewImage: '',
        previewVisible: false,
        visible: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        mapIconB: require('@/assets/images/mapIndex/locationB.png'),
        mapIconR: require('@/assets/images/mapIndex/locationR.png'),
      }
    },
    created () {
    },
    mounted() {
    },
    methods: {
      handleCancel () {
        this.$emit('close');
        this.visible = false;
      },

      showModal(id) {
        console.log(id)
        if (this.taskDetailsData.fileList && this.taskDetailsData.fileList.length) {
          let fileList = [];
          this.taskDetailsData.fileList.forEach((item, index) => {
              let upfile = {};
              upfile.url = item.url
              upfile.uid = '-' + (index+1);
              upfile.name = 'image.png';
              upfile.status = 'done';
              fileList.push(upfile);
          })
          this.fileList = fileList;
          console.log(this.fileList)
        }
        this.visible = true;
        this.$nextTick(() => {
          setTimeout(() => { this.newBMap() })
        })
      },

      newBMap() { //设置地图容器
        let markers = [{
          img: this.mapIconR,
          lng: this.taskDetailsData.matterLongitude,
          lat: this.taskDetailsData.matterLatitude,
        },{
          img: this.mapIconB,
          lng: this.taskDetailsData.matterLongitude == this.taskDetailsData.solveLongitude ? (+this.taskDetailsData.matterLongitude+0.0003) : this.taskDetailsData.solveLongitude,
          lat: this.taskDetailsData.matterLatitude == this.taskDetailsData.solveLatitude ? (+this.taskDetailsData.matterLatitude+0.0003) : this.taskDetailsData.solveLatitude,
        }]
        let Map = new BMap.Map('matter-allmap', {
            enableDblclickZoom: false,
             minZoom:12.4,
            maxZoom:15,
            displayOptions: {
                building: false
            }
        });
        Map.centerAndZoom(new BMap.Point(markers[0].lng,markers[0].lat), 15); //创建地图
        Map.enableScrollWheelZoom(true); //开启缩放
        markers.forEach(item => { //遍历标注
          if (item.lng) {
            let myIcon = new BMap.Icon(item.img,  new BMap.Size(39, 25)); //标注样式
            let marker = new BMap.Marker( new BMap.Point( item.lng, item.lat ), { icon: myIcon } );
            Map.addOverlay(marker)
          }
        })
      },

      async handlePreview(file) {
        console.log(file)
        if (!file.url && !file.preview) {
          file.preview = await getBase64(file.originFileObj);
          }
          this.previewImage = file.url || file.preview;
          this.previewVisible = true;
      },

      previewCancel() {
          this.previewVisible = false;
      },
    }
  }
</script>

<style>
.taskDetails-modal .textal-left .ant-col {
  text-align: left;
}
.taskDetails-modal .ant-form-item {
  margin-bottom: 4px;
}

/* 地图样式 */
.taskDetails-modal .taskDetails-allmap {
  width: 699px !important;
  height: 288px !important;
  margin-bottom: 15px;
}
.taskDetails-modal .taskDetails-allmap .anchorBL,
.taskDetails-modal .taskDetails-allmap .BMap_cpyCtrl span {
  display: none !important;
}

.taskDetails-modal .ant-upload-list-picture-card-container {
  width: 80px;
  height: 80px;
}
.taskDetails-modal .ant-upload-list-picture-card .ant-upload-list-item {
  width: 80px;
  height: 80px;
}
</style>