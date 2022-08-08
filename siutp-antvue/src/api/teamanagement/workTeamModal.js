import {
    axios
} from '@/utils/request';

export const URL = {
    add: "/work/workTeam/one",
    edit: "/work/workTeam/one",
  }
//班组管理-班次列表获取
export const workTeamShiftsList = (_this) => {
    axios.get('/work/workTeam/shiftsList')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.workTeamShiftsList = res.result;

            } else {
                _this.$message.info(res.message);
            }
        })
}

//获取用户信息
export const getUserData = (data, _this) => {
    axios.get('/sys/user/idList', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.managerList = [];
                _this.managerList2 = [];
                list.map(index => {
                    _this.managerList.push({
                        id: index.id,
                        name: index.name
                    })
                    _this.managerList2.push({
                        id: index.id,
                        name: index.name
                    })
                })
            } else {
                _this.$message.info(res.message);
            }
        })
}
