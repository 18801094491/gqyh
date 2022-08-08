import {
    axios
} from '@/utils/request';
//水表管理-水价管理列表获取
export const getWaterPriceListData = (data, _this) => {
    axios.get('/settle/waterPrice/price', {
        params: data
    })
        .then(res => {
            _this.loading2 = false;
            _this.dataSource2 = [];
            if (res.code * 1 == 200) {

                let list = res.result.records;
                list.map(index => {
                    _this.dataSource2.push({
                        id: index.id,
                        price: index.price
                    })
                })
                _this.ipagination2.current = res.result.current;
                _this.ipagination2.total = res.result.total;

            } else {
                _this.$message.info(res.message);
                _this.dataSource2 = [];
                _this.ipagination2.current = 0;
                _this.ipagination2.total = 1;
            }
        })
}
//水表管理-水价管理新增or修改
export const waterPriceOne = (data, _this) => {
    axios.post('/settle/waterPrice/one', data)
        .then(res => {

            if (res.code * 1 == 200) {
                _this.$message.info(res.message);
                _this.waterPriceManagementUpData();
                _this.waterPricevisible = false;
            } else {
                _this.$message.info(res.message);

            }
        })
}
//获取所属标段值
export const getBidSegmentData = (_this) => {
    axios.get('/sys/category/loadTreeChildren', {
        params: {
            pid: '1206872782805680130'
        }
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.bidSegmentList = list;
            } else {
                _this.$message.info(res.message);
            }


        })
}
// 水表管理---抄表详情表格数据
export const getWaterDetail = (data,_this)=>{
    axios({
        url:"/settle/waterPrice/queryMeterMonth",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            _this.readingDataSource = res.result.records;
        }
    })
}
// 水表管理---水表区域--绑定
export const waterBind = (data,_this)=>{
    axios({
        url:"/settle/waterPrice/bindDistrict",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            _this.$message.info(res.message);
            _this.areaVisible=false;
            _this.loadData();
        }
    })
}
// 水表管理---水表区域--解除绑定
export const waterUnbind = (data,_this)=>{
    axios({
        url:"/settle/waterPrice/unBindDistrict",
        methods:"get",
        params:data
    }).then(res=>{
        if(res.code == 200){
            _this.$message.info(res.message);
            _this.loadData();
        }else{
            _this.$message.info(res.message);
        }
    })
}
// 水表管理--是否非远端
export const remoteStatus = (data,_this)=>{
    axios.get('/settle/waterPrice/remoteStatus',{
        params:data
    })
        .then(res=>{
            if(res.code*1==200){
//          _this.ipagination.current=1;
                _this.loadData();
                _this.$message.info(res.message);
            }else{
                _this.$message.info(res.message);

            }
        })
}