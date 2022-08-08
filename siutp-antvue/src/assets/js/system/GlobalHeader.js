import UserMenu from '@comp/tools/UserMenu.vue'
import SMenu from '@comp/menu'
import Logo from '@comp/tools/Logo.vue'
import {mixin} from '@/utils/mixin.js'
import {anonBaseConfig} from '@api/system/system.js'

export default {
    name: 'GlobalHeader',
    components: {
        UserMenu,
        SMenu,
        Logo
    },
    mixins: [mixin],
    props: {
        mode: {
            type: String,
            // sidemenu, topmenu
            default: 'sidemenu'
        },
        menus: {
            type: Array,
            required: true
        },
        theme: {
            type: String,
            required: false,
            default: 'dark'
        },
        collapsed: {
            type: Boolean,
            required: false,
            default: false
        },
        device: {
            type: String,
            required: false,
            default: 'desktop'
        }
    },
    data() {
        return {
            headerBarFixed: false,
            //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
            topMenuStyle: {
                headerIndexLeft: {},
                topNavHeader: {},
                headerIndexRight: {},
                topSmenuStyle: {}
            },
            logoImg: false,
            systemName: ''
        }
    },
    watch: {
        /** 监听设备变化 */
        device() {
            if (this.mode === 'topmenu') {
                this.buildTopMenuStyle()
            }
        },
        /** 监听导航栏模式变化 */
        mode(newVal) {
            if (newVal === 'topmenu') {
                this.buildTopMenuStyle()
            }
        }
    },
    //update-end--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
    mounted() {
        window.addEventListener('scroll', this.handleScroll)
        //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
        if (this.mode === 'topmenu') {
            this.buildTopMenuStyle()
        }
        //update-end--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
        anonBaseConfig(this);
    },
    methods: {
        handleScroll() {
            if (this.autoHideHeader) {
                let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
                if (scrollTop > 100) {
                    this.headerBarFixed = true
                } else {
                    this.headerBarFixed = false
                }
            } else {
                this.headerBarFixed = false
            }
        },
        toggle(num) {
            console.log(num)
            window.updataImg(num)(num);
            this.$emit('toggle')

        },
        //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
        //顶部导航
        buildTopMenuStyle() {
            if (this.mode === 'topmenu') {
                if (this.device === 'mobile') {
                    // 手机端需要清空样式，否则显示会错乱
                    this.topMenuStyle.topNavHeader = {}
                    this.topMenuStyle.topSmenuStyle = {}
                    this.topMenuStyle.headerIndexRight = {}
                    this.topMenuStyle.headerIndexLeft = {}
                } else {
                    let rightWidth = '360px'
                    this.topMenuStyle.topNavHeader = {'min-width': '165px'}
                    this.topMenuStyle.topSmenuStyle = {'width': 'calc(100% - 165px)'}
                    this.topMenuStyle.headerIndexRight = {'min-width': rightWidth}
                    this.topMenuStyle.headerIndexLeft = {'width': `calc(100% - ${rightWidth})`}
                }
            }
        }
        //update-begin--author:sunjianlei---date:20190508------for: 顶部导航栏过长时显示更多按钮-----
    }
}