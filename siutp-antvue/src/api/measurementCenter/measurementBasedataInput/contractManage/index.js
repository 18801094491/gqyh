import {
    axios
} from '@/utils/request';

export const expireDateConfig = (data, _this) => {
    axios.post("/system/systemConfig/one", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.expireDateVisible = false;
        } else {
            _this.$message.info(res.message);
        }
    })
}
export const configByKey = (data, _this) => {
    axios({
        url:`/system/systemConfig/${data}`,
        methods:"get"
    }).then(res=>{
        if(res.code == 200){
            let result = res.result;
            _this.form.id = result.id;
            _this.form.configKey = result.configKey;
            _this.form.setExpireDate = result.configValue;
        }else{
            _this.$message.info(res.message);
        }
    })
}