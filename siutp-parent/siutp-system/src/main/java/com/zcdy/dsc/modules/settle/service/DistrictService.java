package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.entity.District;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.vo.DistrictTreeVo;
import com.zcdy.dsc.modules.settle.vo.DistrictWaterCustomerInfoVo;

import java.util.List;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-20
 * @Version: V1.0
 */
public interface DistrictService extends IService<District> {

    List<DistrictTreeVo> getTreeByParentId(String id);
    List<DistrictTreeVo> getTreeById(String id);

    void addDistrict(District district);

    void removeDistrict(String id);

    void updateDistrict(District district);

    List<DistrictTreeVo> searchBy(String keyWord);

    List<DistrictWaterCustomerInfoVo> queryWaterAndCustomerInfoByDistrictId(String districtId);
}
