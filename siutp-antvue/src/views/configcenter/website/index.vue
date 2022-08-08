<template>
  <!-- 配置中心-网站配置 -->
  <div class="margin12 publicConfiguration">
    <a-card :bordered="false">
      <a-divider>公共配置</a-divider>
      <a-form>
        <a-form-item
          label="系统名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input placeholder="请输入系统名称" maxLength='30' v-model="systemName" />
        </a-form-item>

        <a-form-item
          label="系统简介"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-textarea
            v-model="systemSummary"
            placeholder="请输入系统简介"
            :autoSize="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item
          label="登录背景图片"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-button class="uploadBtn2">
            <input
              type="file"
              id="loginBgImg"
              value=""
              @change="upfileClick($event, 1)"
            />
            <div v-if="loginBg">
              <img
                :src="loginBg"
                alt="头像"
                style="height: 84px; max-width: 300px"
              />
            </div>

            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-button>
          <div class="hint-size">建议尺寸：1080px × 620px</div>
        </a-form-item>
        <a-form-item
          label="横向logo图片"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-button class="uploadBtn2">
            <input
              type="file"
              id="loginBgImg"
              value=""
              @change="upfileClick($event, 2)"
            />
            <div v-if="horizontalLogo">
              <img
                :src="horizontalLogo"
                alt="头像"
                style="height: 84px; max-width: 300px"
              />
            </div>

            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-button>
          <div class="hint-size">建议尺寸：157px × 47px</div>
        </a-form-item>
        <a-form-item
          label="极简logo图片"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-button class="uploadBtn2">
            <input
              type="file"
              id="loginBgImg"
              value=""
              @change="upfileClick($event, 3)"
            />
            <div v-if="simpleLogo">
              <img
                :src="simpleLogo"
                alt="头像"
                style="height: 84px; max-width: 300px"
              />
            </div>

            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-button>
          <div class="hint-size">建议尺寸：30px × 25px</div>
        </a-form-item>
        <a-form-item
          label="登录框logo图片"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-button class="uploadBtn2">
            <input
              type="file"
              id="loginBgImg"
              value=""
              @change="upfileClick($event, 4)"
            />
            <div v-if="verticalLogo">
              <img
                :src="verticalLogo"
                alt="头像"
                style="height: 84px; max-width: 300px"
              />
            </div>

            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-button>
          <div class="hint-size">建议尺寸：160px × 130px</div>
        </a-form-item>
      </a-form>
      <div style="text-align: center; margin-top: 40px; margin-bottom: 40px">
        <a-button
          @click="handleSubmit"
          type="primary"
          style="width: 200px; height: 30px"
          >提交</a-button
        >
      </div>
    </a-card>
  </div>
</template>
<script>
import index from "@/assets/js/configcenter/website.js";
import "@/assets/less/configcenter/website.less";
export default {
  ...index,
};
</script>