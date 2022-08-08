<!-- 登录页面 -->
<template>
  <div class="zcdy-login-page">
    <div class="login-main">
      <div class="login-bg"></div>
      <div class="login-box" id="login">
        <div class="login-top">
          <div class="login-logo-box">
            <div class="login-logo">
              <img :src="verticalLogo" width="100%" height="100%" alt="logo" />
            </div>
          </div>
          <div class="login-top-title">{{ systemName }}</div>
        </div>
        <a-form
          :form="form"
          class="user-layout-login login-from"
          ref="formLogin"
          id="formLogin"
        >
          <a href="javascript:;" id="formBg"></a>
          <a-tabs
            :activeKey="customActiveKey"
            :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
            @change="handleTabClick"
          >
            <a-tab-pane key="tab1" tab="用户名密码登录">
              <div class="loginBodyBox">
                <a-form-item class="rowClass">
                  <a-input
                    size="large"
                    width="80%"
                    v-decorator="[
                      'username',
                      validatorRules.username,
                      { validator: this.handleUsernameOrEmail },
                    ]"
                    type="text"
                    placeholder="请输入用户名/手机号"
                  >
                    <a-icon
                      slot="prefix"
                      type="user"
                      :style="{ color: 'rgba(0,0,0,.25)' }"
                    />
                  </a-input>
                </a-form-item>
                <a-form-item class="rowClass">
                  <a-input
                    v-decorator="['password', validatorRules.password]"
                    size="large"
                    type="password"
                    autocomplete="false"
                    placeholder="密码"
                  >
                    <a-icon
                      slot="prefix"
                      type="lock"
                      :style="{ color: 'rgba(0,0,0,.25)' }"
                    />
                  </a-input>
                </a-form-item>
                <div class="yanzhengmaBox clearfix rowClass">
                  <a-form-item class="fl" style="width: 2.2rem">
                    <a-input
                      v-decorator="['inputCode', validatorRules.inputCode]"
                      size="large"
                      type="text"
                      class="login-from-inputcode"
                      @change="inputCodeChange"
                      placeholder="请输入验证码"
                    >
                      <a-icon
                        slot="prefix"
                        v-if="inputCodeContent == verifiedCode"
                        type="smile"
                        :style="{ color: 'rgba(0,0,0,.25)' }"
                      />
                      <a-icon
                        slot="prefix"
                        v-else
                        type="frown"
                        :style="{ color: 'rgba(0,0,0,.25)' }"
                      />
                    </a-input>
                  </a-form-item>

                  <div
                    class="gc-canvas fr"
                    @click="reloadVcode"
                    style="width: 1rem; float: right"
                  >
                    <img id="gc-canvas" />
                  </div>
                </div>
              </div>

              <a-form-item class="loginBtn">
                <a-button
                  size="large"
                  type="primary"
                  htmlType="submit"
                  class="login-button qdBtn"
                  :loading="loginBtn"
                  @click.stop.prevent="handleSubmit"
                  :disabled="loginBtn"
                  >登录
                </a-button>
              </a-form-item>
            </a-tab-pane>
          </a-tabs>
        </a-form>

        <two-step-captcha
          v-if="requiredTwoStepCaptcha"
          :visible="stepCaptchaVisible"
          @success="stepCaptchaSuccess"
          @cancel="stepCaptchaCancel"
        ></two-step-captcha>

        <a-modal
          title="登录部门选择"
          :width="450"
          :visible="departVisible"
          :closable="false"
          :maskClosable="false"
        >
          <template slot="footer">
            <a-button type="primary" @click="departOk">确认</a-button>
          </template>

          <a-form>
            <a-form-item
              :labelCol="{ span: 4 }"
              :wrapperCol="{ span: 20 }"
              style="margin-bottom: 10px"
              :validate-status="validate_status"
            >
              <a-tooltip placement="topLeft">
                <template slot="title">
                  <span>您隶属于多部门，请选择登录部门</span>
                </template>
                <a-avatar style="backgroundcolor: #87d068" icon="gold" />
              </a-tooltip>
              <a-select
                @change="departChange"
                :class="{ 'valid-error': validate_status == 'error' }"
                placeholder="请选择登录部门"
                style="margin-left: 10px; width: 80%"
              >
                <a-icon slot="suffixIcon" type="gold" />
                <a-select-option
                  v-for="d in departList"
                  :key="d.id"
                  :value="d.orgCode"
                  >{{ d.departName }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-form>
        </a-modal>
      </div>
    </div>
  </div>
</template>

<script>
import index from "@assets/js/system/Login.js";

export default {
  ...index,
};
</script>

<style lang="scss" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;

    .item-icon {
      font-size: 24px;
      color: rgba(0, 0, 0, 0.2);
      margin-left: 16px;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;

      &:hover {
        color: #1890ff;
      }
    }

    .register {
      float: right;
    }
  }
}
</style>
<style>
.login-bg {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(220, 220, 220, 0.3);
}

/* 新增样式 20200229-登录页面背景图片 */
.userLayout {
  width: 100%;
  height: 100%;
  margin: 0 auto;
  overflow: hidden;
  min-height: 100%;
  background: url("~@/assets/bg_04.jpg") no-repeat;
  position: relative;
  background-size: cover;
}

.zcdy-login-page {
  height: 100%;
}

.zcdy-login-page .login-main {
  width: 100%;
  height: 100%;
  margin: 0 auto;
  /* overflow: hidden; */
  min-height: 100%;
  position: relative;
}

.zcdy-login-page .login-box {
  border: 1px solid #6cabe6;
  box-shadow: 1px 1px 4px #999;
  border-radius: 10px;
  overflow: hidden;
  width: 4rem;

  background-color: #007cd3;
  position: fixed;
  top: 10%;
  z-index: 100;
  right: 5%;
}

.zcdy-login-page .login-logo-box {
  min-height: 0.6rem;
  /* border: red 1px dashed; */
}

.zcdy-login-page .login-logo {
  width: 1.6rem;
  height: 1.3rem;
  margin: 0 auto;
  margin-top: 1rem;
}

.zcdy-login-page .login-top-title {
  color: #fff;
  font-size: 0.2rem;
  text-align: center;
  margin: 0 auto;
  margin-top: 0.1rem;
  height: 0.3rem;
}

.zcdy-login-page #login input:-internal-autofill-previewed,
.zcdy-login-page #login input:-internal-autofill-selected {
  -webkit-text-fill-color: #fff !important;
  transition: background-color 5000s ease-in-out 0s !important;
}

.zcdy-login-page #login input:-internal-autofill-previewed,
.zcdy-login-pag #login input:-internal-autofill-selected {
  -webkit-text-fill-color: #fff !important;
  transition: background-color 5000s ease-in-out 0s !important;
}

.zcdy-login-page .has-error .ant-input-affix-wrapper .ant-input,
.zcdy-login-page .has-error .ant-input-affix-wrapper .ant-input:hover {
  border-color: #dadada !important;
  /*修改登录框划过变白色的问题*/
  background-color: #007cd3;
}

.zcdy-login-page .has-error .ant-form-explain,
.zcdy-login-page .has-error .ant-form-split {
  color: #dadada !important;
}

.zcdy-login-page input.text,
.zcdy-login-page input.password {
  color: #fff !important;
}

.zcdy-login-page .ant-input-affix-wrapper .ant-input {
  background-color: transparent;
  color: #fff;
}

/* 结束*/
.valid-error .ant-select-selection__placeholder {
  color: #f5222d;
}

#login {
  /* border: 1px solid #6cabe6;
        box-shadow: 1px 1px 4px #999;
        border-radius: 10px;
        overflow: hidden; */
  /* width: 400px; */
  /* background: -webkit-linear-gradient(top, #005bea, #00c6fb); */
}

#login .ant-tabs-nav-scroll {
  display: none;
}

.user-layout-login button.login-button.qdBtn {
  width: 3.38rem;
  margin: 0 auto;
}

.ant-input-affix-wrapper .ant-input {
  background-color: transparent;
  color: #999;
}

#login .anticon svg {
  color: #ccc;
}

.gc-canvas {
  width: 100%;
  height: 0.38rem;
}

#gc-canvas {
  width: 100%;
  height: 0.38rem;
}

#login .ant-input-lg {
  height: 0.38rem;
  padding: 0.06rem 0.4rem;
  font-size: 0.16rem;
}

#login .rowClass {
  margin-top: 0.1rem;
}

#login .loginBodyBox .ant-row {
  margin-bottom: 0rem;
}

#login .loginBodyBox .ant-form-item-control {
  line-height: 0;
}

#login .loginBtn {
  margin-bottom: 0.6rem;
  margin-top: 0.25rem;
  text-align: center;
}

#login .ant-tabs-bar {
  margin: 0;
}

#login .login-top {
  margin-bottom: 0.6rem;
}

.loginBodyBox {
  padding: 0 0.3rem;
}
</style>
