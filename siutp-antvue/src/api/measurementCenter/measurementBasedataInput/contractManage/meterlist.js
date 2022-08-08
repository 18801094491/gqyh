import {
    axios
} from '@/utils/request';

// 合同管理---水表绑定列表分页
export const waterbindList = (data,_this)=>{
    axios({
        url:"/settle/contractWater",
        methods:"get",
        params:data
    }).then(res=>{
        _this.dataSource=[];
        _this.loading = false;
        if(res.code === 200){
            _this.dataSource = res.result.records;
            _this.pagination.current = res.result.current;
            _this.pagination.total = res.result.total;
        }else{
            _this.$message.info(res.message);
        }
    })
}
// 合同管理--水表解除绑定
export const customerWaterUnbind = (data, _this) => {
    axios({
        url:"/settle/contractWater/unbind",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code === 200){
            _this.unbindVisible=false;
            _this.$message.info(res.message);
            setTimeout(()=>{
                _this.upData();
            },1000)
        }else{
            _this.$message.info(res.message);
        }
    })
}