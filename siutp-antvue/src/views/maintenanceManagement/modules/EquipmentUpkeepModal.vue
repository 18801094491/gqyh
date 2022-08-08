<template>
<div>
  <a-modal
    title="详情"
    :width="600"
    :visible="visible"
    :confirmLoading="confirmLoading"
    class="equipmentUpkeepList"
    @cancel="handleCancel"
    cancelText="关闭">
    
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
      
        <a-form-item

          label="参与人">
          <div class="equipmentUpkeepListDiv">
            叫宋广，刘宏宇
          </div>
        </a-form-item>
        <a-form-item
          label="附件">
          
          <ul class="equipmentUpkeepImgBox clearfix">
            <li v-for="(item,index) in imgList" :key="index">
              <img :src="item.src" alt="" @click="showImg(item.src)">
            </li>
          </ul>
        </a-form-item>
        
		
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click.stop="()=>{visible=false}">关闭</a-button>
    </template>
  </a-modal>
  <a-modal
    title="图片预览"
    :width="600"
    :visible="visible2"
    :confirmLoading="confirmLoading2"
    @cancel="handleCancel2"
    cancelText="关闭">
    <div class="showImgSrc">
      <img :src="showImgSrc" alt="">
    </div>
    
    <template slot="footer">
      <a-button type="primary" @click.stop="()=>{visible2=false}">关闭</a-button>
    </template>
  </a-modal>
</div>
  

  
</template>

<script>
  import '@/assets/less/maintenanceManagement/style.less'
  import index from '@/assets/js/maintenanceManagement/modules/equipmentUpkeepModal.js'
  export default{
    ...index
  }
</script>

<style lang="less" scoped>

</style>