package com.zcdy.dsc.modules.settle.service.impl;

import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.mapper.CustomerMapper;
import com.zcdy.dsc.modules.settle.service.ISettleCustomerService;
import com.zcdy.dsc.modules.settle.vo.TreeVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 客户信息管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-02
 * 版本号: V1.0
 */
@Service
public class SettleCustomerServiceImpl extends ServiceImpl<CustomerMapper, SettleCustomer> implements ISettleCustomerService {

    @Override
    public List<TreeVo> queryCustomerTreeList(String keyword) {
        List<TreeVo> children = this.baseMapper.queryCustomerTreeList(keyword);
        List<TreeVo> list = new ArrayList<>();
        TreeVo root = new TreeVo();
        root.setId("0");
        root.setIsLeaf(false);
        root.setKey("0");
        root.setChildren(children);
        root.setValue("");
        root.setTitle("全部");
        list.add(root);
        return list;
    }
}
