import {
    axios
} from '@/utils/request';
//知识管理-修改回显
export const changeKnowlegeIdData = (data, _this) => {
    axios.get('/equipment/knowlege/getById', {
        params: data
    })
        .then(res => {
            _this.knowlegeItemVoList = [];
            if (res.code * 1 == 200) {
                let obj = res.result;



                setTimeout(() => {

                    obj.knowlegeItemVoList.map(index => {
                        _this.knowlegeItemVoList.push({
                            knowlegeAttachList: index.knowlegeAttachList,
                            knowlegeCautionList: index.knowlegeCautionList,
                            knowlegeOperationList: index.knowlegeOperationList,
                            operationName: index.operationName,
                            id: index.id
                        })
                    });
                }, 1000)


            } else {
                _this.$message.info(res.message);
            }
        })
}

// 设备台账--设备知识
export const getKnowledge = (data, _this) => {
	_this.dataSource = [];
	axios.get(`/equipment/optEquipment/detail/${data}`).then(res => {
        if (res.code * 1 == 200) {
        	_this.loading = false;
        	_this.dataSource = res.result;
        } else {
            _this.$message.info(res.message);
        }
    })
}