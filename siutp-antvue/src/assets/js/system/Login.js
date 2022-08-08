import TwoStepCaptcha from "@/components/tools/TwoStepCaptcha";
import {mapActions} from "vuex";
import {timeFix} from "@/utils/util";
import Vue from "vue";
import {ACCESS_TOKEN, ENCRYPTED_STRING, USER_INFO} from "@/store/mutation-types";
import JGraphicCode from "@/components/jeecg/JGraphicCode";
import {getAction, postAction, putAction} from "@/api/manage";
import {getEncryptedString} from "@/utils/encryption/aesEncrypt";
import store from "@/store/";
import {anonBaseConfig} from '@api/system/system.js'

export default {
    components: {
        TwoStepCaptcha,
        JGraphicCode
    },
    data() {
        return {
            customActiveKey: "tab1",
            loginBtn: false,
            // login type: 0 email, 1 username, 2 telephone
            loginType: 0,
            requiredTwoStepCaptcha: false,
            stepCaptchaVisible: false,
            form: this.$form.createForm(this),
            encryptedString: {
                key: "",
                iv: ""
            },
            state: {
                time: 60,
                smsSendBtn: false
            },
            formLogin: {
                username: "",
                password: "",
                captcha: "",
                mobile: "",
                rememberMe: true
            },
            validatorRules: {
                username: {
                    rules: [
                        {required: true, message: "请输入用户名!", validator: "click"}
                    ]
                },
                password: {
                    rules: [
                        {required: true, message: "请输入密码!", validator: "click"}
                    ]
                },
                password2: {
                    rules: [
                        {required: true, message: "请输入密码!", validator: "click"}
                    ]
                },
                mobile: {rules: [{validator: this.validateMobile}]},
                captcha: {rule: [{required: true, message: "请输入验证码!"}]},
                inputCode: {
                    rules: [
                        {required: true, message: "请输入验证码!"},
                        //{ validator: this.validateInputCode }
                    ]
                }
            },
            verifiedCode: "",
            inputCodeContent: "",
            inputCodeNull: true,

            departList: [],
            departVisible: false,
            departSelected: "",
            currentUsername: "",
            validate_status: "",
            systemName: '',
            verticalLogo: '',
            contentWidth: 136,
            contentHeight: 38,
            vcodeUrl: "/anon/vcode/base64",
            checkKey: ""
        };
    },
    mounted() {
        anonBaseConfig(this);
        this.reloadVcode();
    },
    created() {
        Vue.ls.remove(ACCESS_TOKEN);
        this.getRouterData();
        // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
        //this.getEncrypte();
        // update-end- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
    },
    methods: {
        ...mapActions(["Login", "Logout", "PhoneLogin"]),
        // handler
        handleUsernameOrEmail(rule, value, callback) {
            const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
            if (regex.test(value)) {
                this.loginType = 0;
            } else {
                this.loginType = 1;
            }
            callback();
        },
        handleTabClick(key) {
            this.customActiveKey = key;
            // this.form.resetFields()
        },
        handleSubmit() {
            let that = this;
            let loginParams = {
                remember_me: that.formLogin.rememberMe
            };
            that.loginBtn = true;
            // 使用账户密码登录
            if (that.customActiveKey === "tab1") {
                that.form.validateFields(
                    ["username", "password", "inputCode"],
                    {force: true},
                    (err, values) => {
                        if (!err) {
                            loginParams.username = values.username;
                            // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
                            //loginParams.password = md5(values.password)
                            //loginParams.password = encryption(values.password,that.encryptedString.key,that.encryptedString.iv)
                            loginParams.password = values.password;
                            // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
                            //let checkParams = this.$refs.jgraphicCodeRef.getLoginParam();
                            //loginParams.captcha = checkParams.checkCode;
                            loginParams.captcha = this.inputCodeContent;
                            //loginParams.checkKey = checkParams.checkKey;
                            loginParams.checkKey = this.checkKey;
                            that
                                .Login(loginParams)
                                .then(res => {
                                    this.departConfirm(res);
                                })
                                .catch(err => {
                                    that.requestFailed(err);
                                });
                        } else {
                            that.loginBtn = false;
                        }
                    }
                );
                // 使用手机号登录
            } else {
                that.form.validateFields(
                    ["mobile", "password2"],
                    {force: true},
                    (err, values) => {
                        if (!err) {
                            loginParams.mobile = values.mobile;
                            loginParams.captcha = values.password2;
                            that
                                .PhoneLogin(loginParams)
                                .then(res => {
                                    console.log(res.result);
                                    this.departConfirm(res);
                                })
                                .catch(err => {
                                    that.requestFailed(err);
                                });
                        }
                    }
                );
            }
        },
        getCaptcha(e) {
            e.preventDefault();
            let that = this;
            this.form.validateFields(["mobile"], {force: true}, (err, values) => {
                if (!values.mobile) {
                    that.cmsFailed("请输入手机号");
                } else if (!err) {
                    this.state.smsSendBtn = true;
                    let interval = window.setInterval(() => {
                        if (that.state.time-- <= 0) {
                            that.state.time = 60;
                            that.state.smsSendBtn = false;
                            window.clearInterval(interval);
                        }
                    }, 1000);

                    const hide = this.$message.loading("验证码发送中..", 0);
                    let smsParams = {};
                    smsParams.mobile = values.mobile;
                    smsParams.smsmode = "0";
                    postAction("/sys/sms", smsParams)
                        .then(res => {
                            if (!res.success) {
                                setTimeout(hide, 0);
                                this.cmsFailed(res.message);
                            }
                            console.log(res);
                            setTimeout(hide, 500);
                        })
                        .catch(err => {
                            setTimeout(hide, 1);
                            clearInterval(interval);
                            that.state.time = 60;
                            that.state.smsSendBtn = false;
                            this.requestFailed(err);
                        });
                }
            });
        },
        stepCaptchaSuccess() {
            this.loginSuccess();
        },
        stepCaptchaCancel() {
            this.Logout().then(() => {
                this.loginBtn = false;
                this.stepCaptchaVisible = false;
            });
        },
        loginSuccess() {
            // update-begin- author:sunjianlei --- date:20190812 --- for: 登录成功后不解除禁用按钮，防止多次点击
            // this.loginBtn = false
            // update-end- author:sunjianlei --- date:20190812 --- for: 登录成功后不解除禁用按钮，防止多次点击

            this.$router.push({name: "operationCenter-mapIndex"});
            this.$notification.success({
                message: "欢迎",
                description: `${timeFix()}，欢迎回来`
            });

            // this.$notification.success({
            //   message: '提示',
            //   description: `您的密码已经一个月没换了，建议更换！`,
            // });
        },
        cmsFailed(err) {
            this.$notification["error"]({
                message: "登录失败",
                description: err,
                duration: 4
            });
        },
        requestFailed(err) {
            this.$notification["error"]({
                message: "登录失败",
                description:
                    ((err.response || {}).data || {}).message ||
                    err.message ||
                    "请求出现错误，请稍后再试",
                duration: 4
            });
            this.loginBtn = false;
        },
        validateMobile(rule, value, callback) {
            if (
                !value ||
                new RegExp(
                    /^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/
                ).test(value)
            ) {
                callback();
            } else {
                callback("您的手机号码格式不正确!");
            }
        },
        validateInputCode(rule, value, callback) {
            if (!value || this.verifiedCode == this.inputCodeContent) {
                callback();
            } else {
                callback("您输入的验证码不正确!");
            }
        },
        generateCode(value) {
            this.verifiedCode = value.toLowerCase();
        },
        inputCodeChange(e) {
            this.inputCodeContent = e.target.value;
            if (!e.target.value || 0 == e.target.value) {
                this.inputCodeNull = true;
            } else {
                this.inputCodeContent = this.inputCodeContent.toLowerCase();
                this.inputCodeNull = false;
            }
        },
        departConfirm(res) {
            if (res.success) {
                let multi_depart = res.result.multi_depart;
                //0:无部门 1:一个部门 2:多个部门
                if (multi_depart == 0) {
                    this.loginSuccess();
                    this.$notification.warn({
                        message: "提示",
                        description: `您尚未归属部门,请确认账号信息`,
                        duration: 3
                    });
                } else if (multi_depart == 2) {
                    this.departVisible = true;
                    this.currentUsername = this.form.getFieldValue("username");
                    this.departList = res.result.departs;
                } else {
                    this.loginSuccess();
                }
            } else {
                this.requestFailed(res);
                this.Logout();
            }
        },
        departOk() {
            if (!this.departSelected) {
                this.validate_status = "error";
                return false;
            }
            let obj = {
                orgCode: this.departSelected,
                username: this.form.getFieldValue("username")
            };
            putAction("/sys/selectDepart", obj).then(res => {
                if (res.success) {
                    const userInfo = res.result.userInfo;
                    Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000);
                    store.commit("SET_INFO", userInfo);
                    //console.log("---切换组织机构---userInfo-------",store.getters.userInfo.orgCode);
                    this.departClear();
                    this.loginSuccess();
                } else {
                    this.requestFailed(res);
                    this.Logout().then(() => {
                        this.departClear();
                    });
                }
            });
        },
        departClear() {
            this.departList = [];
            this.departSelected = "";
            this.currentUsername = "";
            this.departVisible = false;
            this.validate_status = "";
        },
        departChange(value) {
            this.validate_status = "success";
            this.departSelected = value;
        },
        getRouterData() {
            this.$nextTick(() => {
                this.form.setFieldsValue({
                    username: this.$route.params.username
                });
            });
        },
        //获取密码加密规则
        getEncrypte() {
            var encryptedString = Vue.ls.get(ENCRYPTED_STRING);
            if (encryptedString == null) {
                getEncryptedString().then(data => {
                    this.encryptedString = data;
                });
            } else {
                this.encryptedString = encryptedString;
            }
        },
        reloadVcode() {
            let img = document.getElementById("gc-canvas");
            getAction(this.vcodeUrl).then(res => {
                if (res.success) {
                    this.checkKey = res.result.key
                    img.src = res.result.url;
                }
            }).catch(() => {
                console.error("验证码获取失败");
            })
        }
    }
};