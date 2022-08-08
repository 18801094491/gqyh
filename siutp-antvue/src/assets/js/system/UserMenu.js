import HeaderNotice from '@comp/tools/HeaderNotice.vue'
import UserPassword from '@comp/tools/UserPassword'
import SettingDrawer from "@comp/setting/SettingDrawer.vue";
import DepartSelect from '@comp/tools/DepartSelect.vue'
import { mapActions, mapGetters } from 'vuex'
import { mixinDevice } from '@/utils/mixin.js'
import {userCheckPassword,getDocListData} from '@api/system/system.js'
import {
    downFile
} from '@/api/manage'
export default {
    name: "UserMenu",
    mixins: [mixinDevice],
    components: {
        HeaderNotice,
        UserPassword,
        DepartSelect,
        SettingDrawer
    },
    data(){
        return{
            docList:[]
        }
    },
    props: {
        theme: {
            type: String,
            required: false,
            default: 'dark'
        }
    },
    mounted(){
        console.log(window.passWordReg);
        if(window.passWordReg){
            userCheckPassword(this);
        }
        getDocListData(this);
    },
    methods: {
        ...mapActions(["Logout"]),
        ...mapGetters(["nickname", "avatar","userInfo"]),
        getAvatar(){
            console.log('url = '+ window._CONFIG['imgDomainURL']+"/"+this.avatar())
            console.log(require('@/assets/images/header.png'))
            if(this.avatar()){
                return window._CONFIG['imgDomainURL']+"/"+this.avatar();
            }else{
                return require('@/assets/images/header.png');
            }

        },
        handleLogout() {
            const that = this

            this.$confirm({
                title: '提示',
                content: '真的要注销登录吗 ?',
                onOk() {
                    return that.Logout({}).then(() => {
                        window.location.href="/";
                        //window.location.reload()
                    }).catch(err => {
                        that.$message.error({
                            title: '错误',
                            description: err.message
                        })
                    })
                },
                onCancel() {
                },
            });
        },
        updatePassword(){
            let username = this.userInfo().username
            this.$refs.userPassword.show(username)
        },
        updateCurrentDepart(){
            this.$refs.departSelect.show()
        },
        systemSetting(){
            this.$refs.settingDrawer.showDrawer()
        },
        handleExportXls2(fileName){
            let url='http://125.35.94.110/docs/%E6%99%BA%E6%85%A7%E7%AE%A1%E7%BD%91%E7%B3%BB%E7%BB%9F%E7%94%A8%E6%88%B7%E5%B8%AE%E5%8A%A9%E6%89%8B%E5%86%8C.pdf';
            let url2='http://192.168.1.8:8081/res/file/20200401/bb6fcc5e-0aa4-4022-813d-15bfd3261d6b.pdf';
            downFile(url2).then((data) => {
                if (!data) {
                    this.$message.warning("文件下载失败")
                    return
                }
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(new Blob([data]), fileName + '.pdf')
                }else{
                    let url = window.URL.createObjectURL(new Blob([data]))
                    let link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url
                    link.setAttribute('download', fileName+'.pdf')
                    document.body.appendChild(link)
                    link.click()
                    document.body.removeChild(link); //下载完成移除元素
                    window.URL.revokeObjectURL(url); //释放掉blob对象
                }
            })
        },
    }
}