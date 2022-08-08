import { axios } from '@/utils/request'

export default {
    name: 'publicConfiguration',
    data() {
        return {
            name: '公共配置',
            labelCol: {
                xs: {
                    span: 12
                },
                sm: {
                    span: 5
                },
            },
            wrapperCol: {
                xs: {
                    span: 24
                },
                sm: {
                    span: 16
                },
            },

            uploadLoading: false,
            picUrl: "",
            loginBg: '',
            horizontalLogo: '',
            simpleLogo: '',
            id: '',
            verticalLogo: '',
            systemName: '',
            systemSummary: ''
        }
    },
    computed: {
        uploadAction: function () {
            return this.url.fileUpload;
        }
    },
    mounted() {//数据初始化
        this.updata();
    },
    methods: {
        publicConfigurationImgUpload(data, _this, type) {
            axios.post('/sys/file/uploadGisNav', data)
                .then(res => {
                    if (res.code * 1 == 200) {
                        if (type == 1) {

                            _this.loginBg = res.result.filePath;
                        } else if (type == 2) {
                            _this.horizontalLogo = res.result.filePath;
                        } else if (type == 3) {
                            _this.simpleLogo = res.result.filePath;
                        } else if (type == 4) {
                            _this.verticalLogo = res.result.filePath;
                        }

                    } else {
                        _this.$message.info(res.message);
                    }
                })
        },
        //数据初始化
        updata() {
            axios.get('/sys/web/config/getCurrentConfig')
                .then(res => {
                    if (res.code * 1 == 200) {
                        this.id = res.result.id;
                        this.loginBg = res.result.loginBg;
                        this.horizontalLogo = res.result.horizontalLogo;
                        this.simpleLogo = res.result.simpleLogo;
                        this.verticalLogo = res.result.verticalLogo;
                        this.systemSummary = res.result.systemSummary;
                        this.systemName = res.result.systemName;

                        window.loginBg = res.result.loginBg;
                        window.horizontalLogo = res.result.horizontalLogo;
                        window.simpleLogo = res.result.simpleLogo;
                        window.verticalLogo = res.result.verticalLogo;
                        window.systemSummary = res.result.systemSummary;
                        window.systemName = res.result.systemName;

                    } else {
                        this.$message.info(res.message);
                    }
                })
        },
        //图片上传
        upfileClick(e, type) {
            let file = e.target.files[0];
            let param = new FormData();
            param.append('file', file);
            console.log(param)
            e.target.value = '';
            this.publicConfigurationImgUpload(param, this, type);

        },
        //数据提交
        handleSubmit() {
            let data = {
                id: this.id,
                systemName: $.trim(this.systemName),
                systemSummary: $.trim(this.systemSummary),
                loginBg: this.loginBg.substring(this.loginBg.indexOf('/res')),
                horizontalLogo: this.horizontalLogo.substring(this.horizontalLogo.indexOf('/res')),
                simpleLogo: this.simpleLogo.substring(this.simpleLogo.indexOf('/res')),
                verticalLogo: this.verticalLogo.substring(this.verticalLogo.indexOf('/res')),
            };
            axios.post('/sys/web/config/one', data)
                .then(res => {
                    if (res.code * 1 == 200) {
                        this.$message.info(res.message);
                        this.updata();
                    } else {
                        this.$message.info(res.message);
                    }
                })
        }
    }
}
//website页面组件混入的website.js