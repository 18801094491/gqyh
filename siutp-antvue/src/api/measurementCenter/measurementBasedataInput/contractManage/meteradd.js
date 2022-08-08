import {
    axios
} from '@/utils/request';
// 合同管理--水表绑定
export const customerWaterbind = (data, _this) => {
    axios.post("/settle/contractWater/bind", data).then(res => {
        if (res.code == 200) {
            _this.$message.info(res.message);
            _this.visible = false;
            _this.$emit('closeModal', 'openAddVisible')
            _this.$parent.upData();
        } else {
            _this.$message.info(res.message);
        }
    })
}
// 合同管理--获取水表下拉框
export const getWaterbindList = (data, _this) => {
    axios({
        url:"/settle/contractWater/queryCustomerBindedWater",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            let list = res.result;
            _this.waterbindList=[];
            list.map(item=>{
                _this.waterbindList.push({
                    equipmentId:item.equipmentId,
                    equipmentName:item.equipmentName,
                })
            })
        }else{
            _this.$message.info(res.message);
        }
    })
}
// 合同管理--获取计价规则下拉框
export const getContractRule = (data, _this) => {
    axios({
        url:"/settle/contractWater/queryContractRule",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            let list = res.result;
            _this.contractRuleList=[];
            list.map(item=>{
                _this.contractRuleList.push({
                    id:item.id,
                    ruleName:item.ruleName,
                })
            })
        }else{
            _this.$message.info(res.message);
        }
    })
}