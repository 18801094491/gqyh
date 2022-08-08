<!--大屏 时间日期-->
<template>
    <div>
        <span>{{nowDate}}</span> <span>{{nowWeek}}</span>
    </div>
</template>

<script>
    export default {
        name: "DateTime",
        data(){
            return{
                timer: null,
                nowWeek: '',
                nowDate: '',
            }
        },
        methods:{
            setNowTimes () {
                let myDate = new Date();
                let wk = myDate.getDay();
                let yy = String(myDate.getFullYear());
                let mm = myDate.getMonth() + 1;
                let dd = String(myDate.getDate() < 10 ? '0' + myDate.getDate() : myDate.getDate());
                let weeks = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
                let week = weeks[wk];
                this.nowDate = yy + '-' + mm + '-' + dd;
                this.nowWeek = week;
            }
        },
        mounted() {
            this.timer = setInterval(() => {
                this.setNowTimes()
            }, 1000)
        },
        destroyed() {
            clearInterval(this.timer)
        }
    }
</script>

<style scoped>
    body{background: inherit;}
    span{
        display: inline-block;
        color: #56E6FF;
        font-size: .4rem;
        margin-right: .14rem;
    }
</style>