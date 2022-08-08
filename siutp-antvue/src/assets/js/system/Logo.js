import {mixin} from '@/utils/mixin.js'
import {anonBaseConfig} from '@api/system/system.js'

export default {
    name: 'Logo',
    mixins: [mixin],
    props: {
        title: {
            type: String,
            default: 'DSC',
            required: false
        },
        showTitle: {
            type: Boolean,
            default: true,
            required: false
        },
        showLogoImg: {
            type: Boolean,
            default: true,
            required: false
        }
    },
    data() {
        return {
            logoBok: true,
            horizontalLogo: '',
            simpleLogo: ''
        }
    },
    mounted() {
        anonBaseConfig(this);
        let _this = this;
        window.updataImg = function () {
            return _this.updataImg;
        }
    },
    methods: {
        updataImg(num) {
            console.log(num)
            if (num == 2) {
                if (this.logoBok) {
                    this.logoBok = false;
                } else {
                    this.logoBok = true;
                }
            } else {
                this.logoBok = true;
            }


        }
    }
}