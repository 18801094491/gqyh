import {
    axios
} from '@/utils/request';

export const URL = {
    list: "/work/workTeam",
    delete: "/work/workTeam/delete",
    deleteBatch: "/work/workTeam/deleteBatch",
    exportXlsUrl: "/work/workTeam/export",
    importExcelUrl: "work/workTeam/importExcel",
}
//班组管理-班组启用停用
export const workTeamEditStatus = (data, _this) => {
    axios.get('/work/workTeam/editStatus', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                // _this.$message.info(res.message);
                _this.loadData();
            } else {
                _this.$message.info(res.message);
            }
        })
}