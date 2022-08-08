<template>
  <div class="account-settings-info-view BaseSetting">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          label="用户账号"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <p>{{ model.username }}</p>
        </a-form-item>

        <a-form-item
          label="用户姓名"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <p>{{ model.realname }}</p>
        </a-form-item>
        <a-form-item
          label="角色分配"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          v-show="!roleDisabled"
        >
          <p>{{ model.realname }}</p>
        </a-form-item>
        <!-- 工号设置设置 -->
        <a-form-item label="工号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入工号"
            v-decorator="['workNo', validatorRules.workNo]"
          />
        </a-form-item>
        <!-- 职务设置 -->
        <a-form-item label="职务" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-select-position
            placeholder="请选择职务"
            :multiple="false"
            v-decorator="['post', {}]"
          />
        </a-form-item>

        <!--部门分配-->
        <a-form-item
          label="部门分配"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          v-show="!departDisabled"
        >
          <div>
            <a-input
              placeholder="点击右侧按钮选择部门"
              v-model="checkedDepartNameString"
              style="width: calc(100% - 82px - 8px)"
              disabled
            ></a-input
            ><a-button icon="search" @click="onSearch">选择</a-button>
          </div>
        </a-form-item>
        <!-- 头像设置 -->
        <a-form-item label="头像" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-upload
            listType="picture-card"
            class="avatar-uploader"
            :showUploadList="false"
            :action="uploadAction"
            :data="{ isup: 1 }"
            :headers="headers"
            :beforeUpload="beforeUpload"
            @change="handleChange"
          >
            <img
              v-if="picUrl"
              :src="getAvatarView()"
              alt="头像"
              style="height: 104px; max-width: 300px"
            />
            <div v-else>
              <a-icon :type="uploadLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>
        </a-form-item>
        <!-- 生日设置 -->
        <a-form-item label="生日" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-date-picker
            style="width: 100%"
            placeholder="请选择生日"
            v-decorator="[
              'birthday',
              {
                initialValue: !model.birthday
                  ? null
                  : moment(model.birthday, dateFormat),
              },
            ]"
          />
        </a-form-item>
        <!-- 性别设置 -->
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="['sex', {}]" placeholder="请选择性别">
            <a-select-option :value="1">男</a-select-option>
            <a-select-option :value="2">女</a-select-option>
          </a-select>
        </a-form-item>
        <!-- 邮箱设置 -->
        <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入邮箱"
            v-decorator="['email', validatorRules.email]"
          />
        </a-form-item>
        <!-- 手机设置 -->
        <a-form-item
          label="手机号码"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            placeholder="请输入手机号码"
            v-decorator="['phone', validatorRules.phone]"
          />
        </a-form-item>
        <!-- 座机设置 -->
        <a-form-item label="座机" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input
            placeholder="请输入座机"
            v-decorator="['telephone', validatorRules.telephone]"
          />
        </a-form-item>
      </a-form>
    </a-spin>
    <depart-window ref="departWindow2" @ok="modalFormOk"></depart-window>

    <div
      class="drawer-bootom-button"
      v-show="!disableSubmit"
      style="text-align: right"
    >
      <a-button @click="handleSubmit" type="primary" :loading="confirmLoading"
        >提交</a-button
      >
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import pick from "lodash.pick";
import moment from "moment";
import departWindow from "./DepartWindow"; // 引入搜索部门弹出框的组件
import JSelectPosition from "@/components/jeecgbiz/JSelectPosition"; //引入职务选择的组件
import { ACCESS_TOKEN } from "@/store/mutation-types"; //引入mutation-types 定义的常量 ACCESS_TOKEN可以替代Mutation事件类型
import { getAction } from "@/api/manage";
import {
  addUser,
  editUser,
  queryUserRole,
  queryall,
  duplicateCheck,
  getMyuserData,
} from "@/api/account-t/settings.js";
import { disabledAuthFilter } from "@/utils/authFilter";
export default {
  name: "UserModal",
  components: {
    departWindow,
    JSelectPosition,
  },
  data() {
    return {
      departDisabled: false, //是否是我的部门调用该页面
      roleDisabled: false, //是否是角色维护调用该页面
      modalWidth: 800,
      drawerWidth: 700,
      modaltoggleFlag: true,
      confirmDirty: false,
      selectedDepartKeys: [], //保存用户选择部门id
      checkedDepartKeys: [],
      checkedDepartNames: [], // 保存部门的名称 =>title
      checkedDepartNameString: "", // 保存部门的名称 =>title
      userId: "", //保存用户id
      disableSubmit: false,
      userDepartModel: { userId: "", departIdList: [] }, // 保存SysUserDepart的用户部门中间表数据需要的对象
      dateFormat: "YYYY-MM-DD",
      validatorRules: {
        username: {
          rules: [
            {
              required: true,
              message: "请输入用户账号!",
            },
            {
              validator: this.validateUsername,
            },
          ],
        },
        password: {
          rules: [
            {
              required: true,
              pattern: /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/,
              message: "密码由8位数字、大小写字母和特殊符号组成!",
            },
            {
              validator: this.validateToNextPassword,
            },
          ],
        },
        confirmpassword: {
          rules: [
            {
              required: true,
              message: "请重新输入登录密码!",
            },
            {
              validator: this.compareToFirstPassword,
            },
          ],
        },
        realname: { rules: [{ required: true, message: "请输入用户名称!" }] },
        phone: { rules: [{ validator: this.validatePhone }] },
        email: {
          rules: [
            {
              validator: this.validateEmail,
            },
          ],
        },
        roles: {},
        //  sex:{initialValue:((!this.model.sex)?"": (this.model.sex+""))}
        workNo: {
          rules: [
            { required: true, message: "请输入工号" },
            { validator: this.validateWorkNo },
          ],
        },
        telephone: {
          rules: [
            {
              pattern: /^0\d{2,3}-[1-9]\d{6,7}$/,
              message: "请输入正确的座机号码",
            },
          ],
        },
      },
      title: "操作",
      visible: false,
      model: {},
      roleList: [],
      selectedRole: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      uploadLoading: false,
      confirmLoading: false,
      headers: {},
      form: this.$form.createForm(this),
      picUrl: "",
      url: {
        fileUpload: window._CONFIG["domianURL"] + "/sys/common/upload",
        imgerver: window._CONFIG["domianURL"] + "/sys/common/view",
        userWithDepart: "/sys/user/userDepartList", // 引入为指定用户查看部门信息需要的url
        userId: "/sys/user/generateUserId", // 引入生成添加用户情况下的url
        syncUserByUserName: "/process/extActProcess/doSyncUserByUserName", //同步用户到工作流
      },
    };
  },
  created() {
    // 获取token
    const token = Vue.ls.get(ACCESS_TOKEN);
    // 设置请求头
    this.headers = { "X-Access-Token": token };
  },
  computed: {
    // 文件上传
    uploadAction: function () {
      return this.url.fileUpload;
    },
  },
  mounted() {
    // 获取用户数据
    getMyuserData(this);
  },
  methods: {
    // 不能编辑
    isDisabledAuth(code) {
      return disabledAuthFilter(code);
    },
    //窗口最大化切换
    toggleScreen() {
      if (this.modaltoggleFlag) {
        this.modalWidth = window.innerWidth;
      } else {
        this.modalWidth = 800;
      }
      this.modaltoggleFlag = !this.modaltoggleFlag;
    },
    // 初始化用户列表
    initialRoleList() {
      queryall().then((res) => {
        if (res.success) {
          this.roleList = res.result;
        } else {
          console.log(res.message);
        }
      });
    },
    // 获取用户角色
    loadUserRoles(userid) {
      queryUserRole({ userid: userid }).then((res) => {
        if (res.success) {
          this.selectedRole = res.result;
        } else {
          console.log(res.message);
        }
      });
    },
    // 刷新
    refresh() {
      this.selectedDepartKeys = [];
      this.checkedDepartKeys = [];
      this.checkedDepartNames = [];
      this.checkedDepartNameString = "";
      this.userId = "";
    },
    // 添加
    add() {
      this.picUrl = "";
      this.refresh();
      this.edit({ activitiSync: "1" });
    },
    // 编辑
    edit(record) {
      this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
      let that = this;
      that.initialRoleList();
      that.checkedDepartNameString = "";
      that.form.resetFields();
      if (record.hasOwnProperty("id")) {
        that.loadUserRoles(record.id);
        this.picUrl = "Has no pic url yet";
      }
      that.userId = record.id;
      that.visible = true;
      that.model = Object.assign({}, record);
      that.$nextTick(() => {
        that.form.setFieldsValue(
          pick(
            this.model,
            "username",
            "sex",
            "realname",
            "email",
            "phone",
            "activitiSync",
            "workNo",
            "telephone",
            "post"
          )
        );
      });
      // 调用查询用户对应的部门信息的方法
      that.checkedDepartKeys = [];
      that.loadCheckedDeparts();
    },
    //
    // 加载部门
    loadCheckedDeparts() {
      let that = this;
      if (!that.userId) {
        return;
      }
      getAction(that.url.userWithDepart, { userId: that.userId }).then(
        (res) => {
          that.checkedDepartNames = [];
          if (res.success) {
            for (let i = 0; i < res.result.length; i++) {
              that.checkedDepartNames.push(res.result[i].title);
              this.checkedDepartNameString = this.checkedDepartNames.join(",");
              that.checkedDepartKeys.push(res.result[i].key);
            }
            that.userDepartModel.departIdList = that.checkedDepartKeys;
          } else {
            console.log(res.message);
          }
        }
      );
    },
    // 关闭
    close() {
      this.$emit("close");
      this.visible = false;
      this.disableSubmit = false;
      this.selectedRole = [];
      this.userDepartModel = { userId: "", departIdList: [] };
      this.checkedDepartNames = [];
      this.checkedDepartNameString = "";
      this.checkedDepartKeys = [];
      this.selectedDepartKeys = [];
    },
    moment,
    // 提交
    handleSubmit() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let avatar = that.model.avatar;
          if (!values.birthday) {
            values.birthday = "";
          } else {
            values.birthday = values.birthday.format(this.dateFormat);
          }
          let formData = Object.assign(this.model, values);
          formData.avatar = avatar;
          formData.selectedroles =
            this.selectedRole.length > 0 ? this.selectedRole.join(",") : "";
          formData.selecteddeparts =
            this.userDepartModel.departIdList.length > 0
              ? this.userDepartModel.departIdList.join(",")
              : "";

          // that.addDepartsToUser(that,formData); // 调用根据当前用户添加部门信息的方法
          let obj;
          if (!this.model.id) {
            formData.id = this.userId;
            obj = addUser(formData);
          } else {
            obj = editUser(formData);
          }
          obj
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
              that.checkedDepartNames = [];
              that.userDepartModel.departIdList = {
                userId: "",
                departIdList: [],
              };

              that.close();
            });
        }
      });
    },
    // 取消
    handleCancel() {
      this.close();
    },
    validateToNextPassword(rule, value, callback) {
      const form = this.form;
      const confirmpassword = form.getFieldValue("confirmpassword");

      if (value && confirmpassword && value !== confirmpassword) {
        callback("两次输入的密码不一样！");
      }
      if (value && this.confirmDirty) {
        form.validateFields(["confirm"], { force: true });
      }
      callback();
    },
    // 检验密码
    compareToFirstPassword(rule, value, callback) {
      const form = this.form;
      if (value && value !== form.getFieldValue("password")) {
        callback("两次输入的密码不一样！");
      } else {
        callback();
      }
    },
    // 验证手机号
    validatePhone(rule, value, callback) {
      if (!value) {
        callback();
      } else {
        //update-begin--Author:kangxiaolin  Date:20190826 for：[05] 手机号不支持199号码段--------------------
        if (new RegExp(/^1[3|4|5|6|7|8|9][0-9]\d{8}$/).test(value)) {
          //update-end--Author:kangxiaolin  Date:20190826 for：[05] 手机号不支持199号码段--------------------

          var params = {
            tableName: "sys_user",
            fieldName: "phone",
            fieldVal: value,
            dataId: this.userId,
          };
          duplicateCheck(params).then((res) => {
            if (res.success) {
              callback();
            } else {
              callback("手机号已存在!");
            }
          });
        } else {
          callback("请输入正确格式的手机号码!");
        }
      }
    },
    // 验证邮箱
    validateEmail(rule, value, callback) {
      if (!value) {
        callback();
      } else {
        if (
          new RegExp(
            /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          ).test(value)
        ) {
          var params = {
            tableName: "sys_user",
            fieldName: "email",
            fieldVal: value,
            dataId: this.userId,
          };
          duplicateCheck(params).then((res) => {
            console.log(res);
            if (res.success) {
              callback();
            } else {
              callback("邮箱已存在!");
            }
          });
        } else {
          callback("请输入正确格式的邮箱!");
        }
      }
    },
    // 验证用户名
    validateUsername(rule, value, callback) {
      var params = {
        tableName: "sys_user",
        fieldName: "username",
        fieldVal: value,
        dataId: this.userId,
      };
      duplicateCheck(params).then((res) => {
        if (res.success) {
          callback();
        } else {
          callback("用户名已存在!");
        }
      });
    },
    // 验证work_no工号
    validateWorkNo(rule, value, callback) {
      var params = {
        tableName: "sys_user",
        fieldName: "work_no",
        fieldVal: value,
        dataId: this.userId,
      };
      duplicateCheck(params).then((res) => {
        if (res.success) {
          callback();
        } else {
          callback("工号已存在!");
        }
      });
    },
    // 确认
    handleConfirmBlur(e) {
      const value = e.target.value;
      this.confirmDirty = this.confirmDirty || !!value;
    },
    // 文件
    normFile(e) {
      console.log("Upload event:", e);
      if (Array.isArray(e)) {
        return e;
      }
      return e && e.fileList;
    },
    // 上传文件
    beforeUpload: function (file) {
      var fileType = file.type;
      if (fileType.indexOf("image") < 0) {
        this.$message.warning("请上传图片");
        return false;
      }
      //TODO 验证文件大小
    },
    // 文件改变
    handleChange(info) {
      this.picUrl = "";
      if (info.file.status === "uploading") {
        this.uploadLoading = true;
        return;
      }
      if (info.file.status === "done") {
        var response = info.file.response;
        this.uploadLoading = false;
        console.log(response);
        if (response.success) {
          this.model.avatar = response.message;
          this.picUrl = "Has no pic url yet";
        } else {
          this.$message.warning(response.message);
        }
      }
    },
    // 获取头像
    getAvatarView() {
      return this.url.imgerver + "/" + this.model.avatar;
    },
    // 搜索用户对应的部门API
    onSearch() {
      this.$refs.departWindow2.add(this.checkedDepartKeys, this.userId);
    },

    // 获取用户对应部门弹出框提交给返回的数据
    modalFormOk(formData) {
      this.checkedDepartNames = [];
      this.selectedDepartKeys = [];
      this.checkedDepartNameString = "";
      this.userId = formData.userId;
      this.userDepartModel.userId = formData.userId;
      for (let i = 0; i < formData.departIdList.length; i++) {
        this.selectedDepartKeys.push(formData.departIdList[i].key);
        this.checkedDepartNames.push(formData.departIdList[i].title);
        this.checkedDepartNameString = this.checkedDepartNames.join(",");
      }
      this.userDepartModel.departIdList = this.selectedDepartKeys;
      this.checkedDepartKeys = this.selectedDepartKeys; //更新当前的选择keys
    },
    // 根据屏幕变化,设置抽屉尺寸
    resetScreenSize() {
      let screenWidth = document.body.clientWidth;
      if (screenWidth < 500) {
        this.drawerWidth = screenWidth;
      } else {
        this.drawerWidth = 700;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }
  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img,
  .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
.BaseSetting {
  p {
    margin-bottom: 0;
  }
}
</style>