import axiosRequst from "axios";
import {axios} from '@/utils/request';

/**
 * 运营中心-监控中心-所属接口
 * 
 */

//获取标段标注信息
export const getbiaoduanorbiaozhuData = (_this) => {
    axios.get('/gis/dashboard/data').then(res => {
        if (res.code * 1 == 200) {
            // console.log("获取标段标注信息==============")
            // console.log(res)
            let list = res.result;
            window.oMarker = [];
            list.filter((item) => {
                let icon;
                let width;
                let height;
                if (item.modelOnImg) {
                    icon = item.modelOnImg.imgUrl;
                    width = item.modelOnImg.width;
                    height = item.modelOnImg.height;
                    // console.log(window.oMarker)
                    window.oMarker.push({
                        modelZcImg:new AMap.Marker({
                            position: new AMap.LngLat(item.longitude,item.latitude),
                            offset: new AMap.Pixel(0, Number(item.modelOffset)),
                            // offset: new AMap.Pixel(-((width||26)*1/2), -((height*1)||30)),
                            icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                        }),
                        modelImg: item.modelImg,
                        list: [],
                        modelType: _this.zoonLevel,
                        id: item.id,
                        icon:icon,
                        latitude: item.latitude,
                        longitude: item.longitude,
                        modelLevel: item.modelLevel,
                        modelOffImg: item.modelOffImg,
                        modelOffset: item.modelOffset,
                        modelOnImg: item.modelOnImg,
                        modelPosition: item.modelPosition,
                        modelTypeCode: item.modelTypeCode,
                        modelWaringImg: item.modelWaringImg,
                        equipmentSn: item.hkMonitorCode,
                        equipLocation: item.equipLocation,
                        hkMonitorKey: item.hkMonitorKey
                    })
                } else if (item.modelImg) {
                    // console.log(item.modelImg,'item.modelImg11111')
                    icon = item.modelImg.imgUrl;
                    width = item.modelImg.width;
                    height = item.modelImg.height;
                    window.oMarker.push({
                        // modelZcImg: new AMap.Icon(icon, new AMap.Size(width, height), {
                        //     imageSize: new AMap.Size(width, height)
                        // }),
                        modelZcImg:new AMap.Marker({
                            position: new AMap.LngLat(item.longitude,item.latitude),
                            offset: new AMap.Pixel(0, Number(item.modelOffset)),
                            // offset: new AMap.Pixel(-((width||26)*1/2), -((height*1)||30)),
                            icon: new AMap.Icon({size: new AMap.Size(width, height), image: icon}), // 添加 Icon 图标 URL
                        }),
                        
                        modelImg: item.modelImg,
                        list: [],
                        modelType: _this.zoonLevel,
                        id: item.id,
                        icon:icon,
                        latitude: item.latitude,
                        longitude: item.longitude,
                        modelLevel: item.modelLevel,
                        modelOffImg: item.modelOffImg,
                        modelOffset: item.modelOffset,
                        modelOnImg: item.modelOnImg,
                        modelPosition: item.modelPosition,
                        modelTypeCode: item.modelTypeCode,
                        modelWaringImg: item.modelWaringImg,
                        equipmentSn: item.hkMonitorCode,
                        equipLocation: item.equipLocation,
                    })
                }
            })
            _this.updataMap();
        }
    })
}
//获取河流线路数据
export const getRiversData = (_this) => {
    axios.get('rdp/info/rivers')
        .then(res => {
                if (res.code * 1 == 200) {
                    _this.resultRivers = res.data;
                } else {
                    this.$message.info(res.message);
                }
            }
        )
}
//获取区域轮廓数据
export const getRegionsData = (_this) => {
    axios.get('rdp/info/regions')
        .then(res => {
                // console.log(res.code,res.message,'','状态码')
                if (res.code * 1 == 200) {
                    _this.resultRegions = res.data;
                } else {
                    this.$message.info(res.message);
                }
            }
        )
}
//获取地图设备资产详情
export const getGisModelDetailData = (data, _this) => {
    axios.get('/equipment/gisModel/getDetail', {
        params: data
    }).then(res => {
        if (res.code * 1 == 200) {
            if (res.result.status == 0) {
                _this.attributeList = [];
                let obj = res.result.equipmentDetailModel;
                // console.log(obj, 'objobjobj')
                _this.usageState = obj.usageState ? '打开' : '关闭'; //获取详情-使用状态
                _this.deviceStatus = obj.deviceState ? '正常' : '故障'; //获取详情-设备状态
                _this.assetCoding = obj.optEquipmentModel.equipmentSn; //获取详情-资产编号
                _this.assetName = obj.optEquipmentModel.equipmentName; //获取详情-资产名称
                _this.manufacturer = obj.optEquipmentModel.equipmentSupplier; //获取详情-生产厂家
                _this.informationEquipmentType = obj.optEquipmentModel.equipmentModeName; //获取详情-设备型号
                _this.assetLocation = obj.optEquipmentModel.equipmentLocation; //获取详情-资产位置
                _this.assetImg = obj.optEquipmentModel.equipmentImg; //获取详情-设备图片
                setTimeout(() => { _this.detailsVisible = true; });
                if (obj.list.length) {
                    obj.list.map(index => {
                        _this.attributeList.push({
                            attributeCn: index.attributeCn,
                            attributeVal: index.attributeVal
                        })
                    })
                }
                if (obj.equipData.length) {
                    obj.equipData.map(index => {
                        _this.attributeList.push({
                            attributeCn: index.variableName,
                            attributeVal: index.varibleValue
                        })
                    })
                }
            } else if (res.result.status == 1) {
                let obj = res.result.businessWarnVo;
                _this.warnDetails2(obj);
            } else if (res.result.status == 2) {
                _this.$message.info('该告警事件已处理!');
            }
        } else if (res.code * 1 == 210) {

        } else {
            _this.$message.info(res.message);
        }
    })
}
//获取地图设备资产详情
export const getGisModelDetailData2 = (data, _this, obj2, opts) => {
    axios.get('/equipment/gisModel/getLocation', {
        params: data
    }).then(res => {
        if (res.code * 1 == 200) {
            let obj = res.result;
            var sContent =
                '<div class="introduceBox">'
                + '<div class="introduceDivBox">'
                + '<label>所属标段：</label>'
                + '<span>' + res.result.equipmentSection + '</span>'
                + '</div>'
                + '<div class="introduceDivBox">'
                + '<label>所属位置：</label>'
                + '<span>' + res.result.equipmentLocation + '</span>'
                + '</div>'
            var infoWindow = new BMap.InfoWindow(sContent, opts);
            obj2.infoWindow = infoWindow;
            obj2.str.openInfoWindow(obj2.infoWindow);
        } else if (res.code * 1 !== 210) {
            _this.$message.info(res.message);
        }
    })
}
//websocket启动前调用接口
export const getUrl = () => {
    axios.get('/gis/dashboard/init')
}
//websocket注销前调用接口
export const getUrl2 = () => {
    axios.get('/gis/dashboard/destroy')
}
//信息中心-轮询获取token
export const getToken = () => {
    axios.get('/sys/ping')
}
//监控中心-查询未处理告警数据
export const warnUndealData = (_this) => {
    axios.get('/business/warn/undealData').then(res => {
        _this.tabScrollList = [];
        clearInterval(_this.warnUndealDataTimer);
        if (res.code * 1 == 200) {
            let list = res.result;
            list.map(index => {
                _this.tabScrollList.push({
                    warnType: index.warnType,
                    warnTypeCode: index.warnTypeCode,
                    warnWay: index.warnWay,
                    warnWayCode: index.warnWayCode,
                    id: index.id,
                    confirmStatus: index.confirmStatus,
                    description: index.description,
                    duration: index.duration,
                    equipmentType: index.equipmentType,
                    operateTime: index.operateTime,
                    operator: index.operator,
                    operatorId: index.operatorId,
                    ruleContent: index.ruleContent,
                    warnContent: index.warnContent,
                    warnLevel: index.warnLevel,
                    warnLevelCode: index.warnLevelCode,
                    warnName: index.warnName,
                    warnSn: index.warnSn,
                    warnStatus: index.warnStatus,
                    warnStatusCode: index.warnStatusCode,
                    warnTime: index.warnTime,
                    warnContent2: index.warnContent.length > 14 ? (index.warnContent.substring(0, 14) + '...') : index.warnContent

                })
            })
            setTimeout(() => {
                $('.tab-scroll tbody').html('');
                $('.tab-scroll2 tbody').html('');
                $('.scroll-box').css("top", 0);
                _this.roll2(_this.tabScrollList);
            }, 1000)


        } else {

            _this.$message.info(res.message);
        }
    })
}
//地图界面查询所有图标
export const getWarnQueryAllIcon = (_this) => {
    axios.get('/business/warn/queryAllIcon').then(res => {
        if (res.code * 1 == 200) {
            let list = res.result;
            _this.warnQueryAllIconList = list;
        } else {
            _this.$message.info(res.message);

        }
    })
}
//gis地图-告警事件-解除
export const gisAlarmRelieve = (data, _this) => {
    axios.post('/business/warn/close', data).then(res => {
        if (res.code * 1 == 200) {
            _this.turnOffAlarmvisible = false;
            _this.alarmEventVisible = false;
            _this.warnUndealData();
        } else {
            _this.$message.info(res.message);
        }
    })
}

//gis地图-告警事件-知悉
export const gisAlarmKnow = (data, _this) => {
    axios.get('/business/warn/confirm', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.alarmEventVisible = false;
                _this.warnUndealData();
            } else {
                _this.$message.info(res.message);
            }
        })
}

//gis地图-告警事件-处理
export const gisAlarmHandle = (data, _this) => {
    axios.post('/business/warn/deal', data)
        .then(res => {
            if (res.code * 1 == 200) {
                _this.warnUndealData();
                _this.alarmHandlingvisible = false;
                _this.alarmEventVisible = false;
            } else {
                _this.$message.info(res.message);

            }
        })
}

//设备报警规则-停用启用规则
export const equipmentAlarmStartOrStop = (data, _this) => {
    axios.get('/equipment/alarm/startOrStop', {
        params: data
    })
        .then(res => {
            if (res.code * 1 == 200) {
                _this.alarmRulesUpdata();
            } else {
                _this.$message.info(res.message);

            }
        })
}
//gis地图导航栏
export const gisDashboardGisModelNav = (_this) => {
    axios.get('/gis/dashboard/gisModelNav')
        .then(res => {
            window.noNameArr = [];
            if (res.code * 1 == 200) {
                let list = res.result;
                _this.nameArr = [];
                _this.gisDashboardGisModelNavList = list;
                list.map(index => {
                    if (index.dataShow == 1) {
                        _this.nameArr.push(index.modelType)
                    } else {
                        window.noNameArr.push(index.modelType);
                    }
                    _this.allArrName.push(index.modelType);
                });

                // console.log(window.noNameArr)
                // console.log(_this.allArrName)
                // console.log(_this.gisDashboardGisModelNavList)
            } else {
                _this.$message.info(res.message);

            }
        })
}
// 流量统计  --昨日
export const getGisFlowYestoday = (_this) => {
    // axios.get('/gis/dashboard/flowYestoday').then(res => {
    //     if (res.code * 1 == 200) {
    //         let list = res.result;
    //         _this.flowCountYesList = [];
    //         list.map(item => {
    //             _this.flowCountYesList.push({
    //                 flowCountYes: item.flowCount,
    //                 flowCountFormatYes: item.flowCountFormat,
    //                 flowName: item.flowName,
    //             })
    //         })
    //     } else {
    //         _this.$message.info(res.message);
    //     }
    // })
}
// 流量统计  --今日
export const getGisFlowToday = (_this) => {
    // axios.get('/gis/dashboard/flowToday').then(res => {
    //     if (res.code * 1 == 200) {
    //         let list = res.result;
    //         _this.flowCountList = [];
    //         list.map(item => {
    //             _this.flowCountList.push({
    //                 flowCount: item.flowCount,
    //                 flowCountFormat: item.flowCountFormat,
    //                 flowName: item.flowName,
    //             })
    //         })
    //     } else {
    //         _this.$message.info(res.message);
    //     }
    // })
}
//获取综合安防平台配置(获取海康密钥)
export const getArtemisConfig = (data, _this, callBack) => {
    
    var url = window._CONFIG['monitorURL'] + '/camera/configs';
    axiosRequst.get(url,{
        params: data 
    })
        .then(res => {
            console.log(res.data.code,res.data.message,'=====================================')

            if (res.data.code * 1 == 200) {
                _this.initparam = res.data.data;
                callBack && callBack();
            } else {
                _this.$message.info(res.data.message);
            }
        })
}