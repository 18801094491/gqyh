<!-- 运营中心-班组班次-三方团队-新增 -->
<template>
  
  <a-drawer
    :title="title"
    :width="800"
    @close="handleCancel"
    :visible="visible"
    :wrapStyle="{height: 'calc(100%)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <div class="table-page-search-wrapper">
      <a-form :form="form">
        <a-row :gutter="24">
         
          <a-col :span="12">
            <a-form-item
              label="团队名称">
              <a-input v-if="isShow" placeholder="请输入团队名称" v-decorator="['teamName', {}]" />
              <p v-if="!isShow">{{model.teamName}}</p>
            </a-form-item>    
              
          </a-col>  
          <a-col :span="12">
            <a-form-item
              label="团队评级">
              <a-select v-if="isShow" v-decorator="['teamRating', {}]" placeholder="请输入团队评级">
                  <a-select-option :value="item.code" v-for="(item,index) in teamRatingList" :key="index">{{item.title}}</a-select-option>
              </a-select>
              <p v-if="!isShow">{{model.teamRating}}</p>
            </a-form-item>              
          </a-col>  
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item
              label="协议起始日期">
              <a-date-picker v-if="isShow" style="width:100%" v-decorator="['agreeStart', {}]" />
              <p v-if="!isShow">{{model.agreeStart}}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              label="协议截至日期">
              <a-date-picker v-if="isShow" style="width:100%" v-decorator="[ 'agreeEnd', {}]" />
              <p v-if="!isShow">{{model.agreeEnd}}</p>
            </a-form-item>               
          </a-col>  
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item
              label="联系人姓名">
              <a-input v-if="isShow" placeholder="请输入联系人姓名" v-decorator="['contactName', {}]" />
              <p v-if="!isShow">{{model.contactName}}</p>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item
              label="联系人电话">
              <a-input v-if="isShow" placeholder="请输入联系人电话" maxLength="11" v-decorator="['contactPhone', {}]" />
              <p v-if="!isShow">{{model.contactPhone}}</p>
            </a-form-item>
              
          </a-col>  
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item
              label="联系人职位">
              <a-select v-if="isShow" v-decorator="['contactPosition', {}]" placeholder="请选择联系人职位">
                  <a-select-option :value="item.code" v-for="(item,index) in contactPositionList" :key="index">{{item.title}}</a-select-option>
              </a-select>
              <p v-if="!isShow">{{model.contactPosition}}</p>
            </a-form-item>
          </a-col>
          
        </a-row>
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item
              label="资质附件(请上传PDF格式文件)">
              <a-button v-if="isShow" class="uploadBtn">
                  <a-icon type="upload" /> 上传
                  <input type="file" name="file" id="uploadBtn" @change="upfileClick">
              </a-button>
            </a-form-item>
            <div v-if="fileList.length">
                <div class="flieBox" v-for="(item,index) in fileList" :key="index">
                    <p>{{item.fileName}}</p>
                    <a-icon v-if="isShow" type="close" @click="removeFile"/>
                </div>
            </div>
          </a-col>         
        </a-row>
      </a-form>
    </div>
    <div
      :style="{
        position: 'absolute',
        left: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
      }"
    >
      <a-button :style="{marginRight: '8px'}" @click="handleCancel">
        关闭
      </a-button>
      <a-button  @click="handleOk" type="primary">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import index from "@/assets/js/operationCenter/thirdPartyTeam/thirdTeamModal.js";
export default {
  ...index
};
</script>