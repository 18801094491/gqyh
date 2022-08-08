import {
    axios
} from '@/utils/request';
// import Vue from 'vue'
// import {ACCESS_TOKEN} from "@/store/mutation-types"


//树形结构-对象类型-分页列表查询
export const getObjectTypeTable = (data, _this) => {
    axios.get('/objecttype/sysObject/list',{
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.dataSource = res.data.records
            } else {
                _this.$message.info(res.message);
            }
        })
       
}
//树形结构-对象类型-验证类名是否可用
export const checkObjType = (data, _this) => {
    axios.get('/objecttype/sysObject/checkObjType',{
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.isPass = true
            } else {
                _this.isPass = false
                _this.$message.info(res.message);
            }
        }) 
}

//树形结构-对象类型-添加
export const addObjectType = (data, _this) => {
    axios.post('/objecttype/sysObject/add', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.success(res.message);
                _this.close()
            } else {
                _this.$message.info(res.message);
            }
        })   
}

//树形结构-对象类型-通过id查询编辑
export const editObjectType = (data, _this) => {
    axios.post('/objecttype/sysObject/edit', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.success(res.message);
                _this.close()
            } else {
                _this.$message.info(res.message);
            }
        })   
       
}

//树形结构-对象类型-id获取信息
export const getIdQuery = (data, _this, callBack) => {
    axios.get('/objecttype/sysObject/query',{
        params: data
    })
        .then(res => {
            let result = res.data;
            if (res.code * 1 == 200) {
                if(callBack){
                callBack && callBack(result)
 
                }else{
                    this.$nextTick(() => {
                        _this.form.setFieldsValue({name: result.name ? result.name : ''});
                        _this.form.setFieldsValue({objType: result.objType ? result.objType : ''});
                        _this.form.setFieldsValue({tableName: result.tableName ? result.tableName : ''});
                    });
                }
               
            } else {
                _this.$message.info(res.message);
            }
        }) 
}

//树形结构-对象类型-通过id删除
export const singleDelete = (data, _this) => {
    axios.delete('/objecttype/sysObject/delete' ,{
        params: data
    })
        .then(res => {
            let result = res.data;
            if (res.code * 1 == 200) {
                _this.getObjectTypelist();
            } else {
                _this.$message.info(res.message);
            }
        }) 
}

//树形结构-对象类型-通过批量id删除
export const multipleDelete = (data, _this) => {
    axios.delete('/objecttype/sysObject/deletes',{
        params: data
    })
        .then(res => {
            let result = res.data;
            if (res.code * 1 == 200) {
                _this.getObjectTypelist();
                _this.$message.info(res.message);
            } else {
                _this.$message.info(res.message);
            }
        }) 
}






/*------------------树形结构根目录------------------*/
//树形结构-对象类型-获取全部
export const getAllList = ( _this) => {
    axios.get('/objecttype/sysObject/getAllList')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.objIdData = res.data
            } else {
                _this.$message.info(res.message);
            }
        })
       
}

//树形结构-对象类型-获取全部
export const getCentres = ( _this) => {
    axios.get('/centre/root/centres/')
        .then(res => {
            if (res.code * 1 == 200) {
                _this.centreData = res.data
            } else {
                _this.$message.info(res.message);
            }
        })
       
}

//树形结构-树形结构根目录-分页列表查询
export const getTreeTable = (data, _this) => {
    axios.get('/centre/root/list/',{
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.dataSource = res.data
            } else {
                _this.$message.info(res.message);
            }
        })
       
}
//树形结构-树形结构根目录-添加
export const addTree = (data, _this) => {
    // axios.defaults.headers.common['Authorization'] = _this.tokenHeader
    axios.post('/centre/root/add', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.success(res.message);
                _this.close()
            } else {
                _this.$message.info(res.message);
            }
        })
}

//树形结构-树形结构根目录-通过id查询编辑
export const editTree = (data, _this) => {
    axios.post('/centre/root/edit', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.$message.success(res.message);
                _this.close()
            } else {
                _this.$message.info(res.message);
            }
        })   
       
}

//树形结构-树形结构根目录-id获取信息
export const getTreeIdQuery = (data, _this) => {
    axios.get('/centre/root/query',{
        params: data
    })
        .then(res => {
            let result = res.data;
            if (res.code * 1 == 200) {
                _this.form.setFieldsValue({name: result.name ? result.name : ''});
                _this.form.setFieldsValue({objId: result.objId ? result.objId : ''});
                _this.form.setFieldsValue({centre: result.centre ? result.centre : ''});
            } else {
                _this.$message.info(res.message);
            }
        }) 
}

//树形结构-树形结构根目录-通过id删除
export const singleDeleteTree = (data, _this) => {
    axios.delete('/centre/root/delete',{
        params: data
    })
        .then(res => {
            let result = res.data;
            if (res.code * 1 == 200) {
                _this.getTreelist();
            } else {
                _this.$message.info(res.message);
            }
        }) 
}









