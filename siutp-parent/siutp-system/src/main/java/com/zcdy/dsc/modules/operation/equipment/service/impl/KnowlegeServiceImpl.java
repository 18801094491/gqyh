package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.equipment.entity.*;
import com.zcdy.dsc.modules.operation.equipment.mapper.*;
import com.zcdy.dsc.modules.operation.equipment.param.KnowlegePageParam;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeService;
import com.zcdy.dsc.modules.operation.equipment.vo.knowlege.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述: 知识库管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Service
public class KnowlegeServiceImpl extends ServiceImpl<KnowlegeMapper, Knowlege> implements IKnowlegeService {
    @Autowired
    private KnowlegeMapper knowlegeMapper;
    @Autowired
    private KnowlegeItemMapper knowlegeItemMapper;
    @Autowired
    private KnowlegeAttachMapper knowlegeAttachMapper;
    @Autowired
    private KnowlegeCautionMapper knowlegeCautionMapper;
    @Autowired
    private KnowlegeOperationMapper knowlegeOperationMapper;

    @Value("${com.zcdy.dsc.file.request.domain}")
    private String baseStoragePath;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInfo(KnowlegeVo knowlegeVo) {
        //获取条目信息
        List<KnowlegeItemVo> knowlegeItemVoList = knowlegeVo.getKnowlegeItemVoList();
        //创建知识库对象，保存知识库
        Knowlege knowlege = new Knowlege();
        BeanUtils.copyProperties(knowlegeVo, knowlege);
        knowlege.setItemCount(knowlegeItemVoList.size());
        //保存知识库对象
        save(knowlege);
        //遍历条目集合，保存每一条操作规程和安全事项
        for (KnowlegeItemVo knowlegeItemVo : knowlegeItemVoList) {
            LinkedList<KnowlegeOperation> knowlegeOpList = new LinkedList<KnowlegeOperation>();
            LinkedList<KnowlegeCaution> knowlegeCaList = new LinkedList<KnowlegeCaution>();
            KnowlegeItem knowlegeItem = new KnowlegeItem();
            knowlegeItem.setOperationName(knowlegeItemVo.getOperationName());
            knowlegeItem.setKnowlegeId(knowlege.getId());
            //下面需要获取到条目信息保存后的id. 直接进行保存
            knowlegeItemMapper.insert(knowlegeItem);
            //获取到 操作规程
            LinkedList<KnowlegeOperationVo> knowlegeOperationList = knowlegeItemVo.getKnowlegeOperationList();
            for (KnowlegeOperationVo knowlegeOperationVo : knowlegeOperationList) {
                //保存每一项操作规程
                KnowlegeOperation knowlegeOperationNew = new KnowlegeOperation();
                BeanUtils.copyProperties(knowlegeOperationVo, knowlegeOperationNew);
                knowlegeOperationNew.setKnowlegeItemId(knowlegeItem.getId());
                knowlegeOpList.add(knowlegeOperationNew);
            }
            //保存操作规程
            knowlegeOperationMapper.addKnowlegeOperationInfo(knowlegeOpList);
            //获取到所有的 安全事项
            LinkedList<KnowlegeCautionVo> knowlegeCautionList = knowlegeItemVo.getKnowlegeCautionList();
            if (StringUtils.isNotEmpty(knowlegeCautionList.get(0).getCautionItem())) {
                //当安全事项有数据时候，才进行保存
                for (KnowlegeCautionVo knowlegeCautionVo : knowlegeCautionList) {
                    KnowlegeCaution knowlegeCautionNew = new KnowlegeCaution();
                    BeanUtils.copyProperties(knowlegeCautionVo, knowlegeCautionNew);
                    knowlegeCautionNew.setKnowlegeItemId(knowlegeItem.getId());
                    knowlegeCaList.add(knowlegeCautionNew);
                }
                // 保存注意事项
                knowlegeCautionMapper.addKnowlegeCautionInfo(knowlegeCaList);
            }

            //获取到 手册信息
            LinkedList<KnowlegeAttach> knowlegeAttachList = knowlegeItemVo.getKnowlegeAttachList();
            if (knowlegeAttachList.size() > 0) {
                for (KnowlegeAttach knowlegeAttach : knowlegeAttachList) {
                    knowlegeAttach.setItemId(knowlegeItem.getId());
                }
                knowlegeAttachMapper.addKnowlegeAttachInfo(knowlegeAttachList);
            }
        }
    }


    @Override
    public KnowlegeVo selectInfo(String id) {
        KnowlegeVo knowlege = new KnowlegeVo();
        LinkedList<KnowlegeItemVo> knowlegeItemVoList = new LinkedList<KnowlegeItemVo>();
        //查条目对象 (条目会有多个)
        List<KnowlegeItem> itemList = knowlegeItemMapper.selectList(new LambdaQueryWrapper<KnowlegeItem>().eq(KnowlegeItem::getKnowlegeId, id).eq(KnowlegeItem::getDelFlag, 0));
        for (KnowlegeItem knowlegeItem : itemList) {
            KnowlegeItemVo knowlegeItemVo = new KnowlegeItemVo();
            //根据条目查 手册
            LinkedList<KnowlegeAttach> knowlegeAttaches = knowlegeAttachMapper.getlistInfo(knowlegeItem.getId());
            for (KnowlegeAttach knowlegeAttach : knowlegeAttaches) {
                knowlegeAttach.setAttachFile(baseStoragePath + knowlegeAttach.getAttachFile());
            }
            //根据条目查 安全事项
            LinkedList<KnowlegeCautionVo> cautionListist = knowlegeCautionMapper.getlistInfo(knowlegeItem.getId());
            //根据条目查 操作规程
            LinkedList<KnowlegeOperationVo> operationList = knowlegeOperationMapper.getlistInfo(knowlegeItem.getId());
            knowlegeItemVo.setOperationName(knowlegeItem.getOperationName());
            knowlegeItemVo.setKnowlegeAttachList(knowlegeAttaches);
            knowlegeItemVo.setKnowlegeCautionList(cautionListist);
            knowlegeItemVo.setKnowlegeOperationList(operationList);
            knowlegeItemVo.setId(knowlegeItem.getId());
            knowlegeItemVoList.add(knowlegeItemVo);
        }
        //封装返回值
        knowlege.setKnowlegeItemVoList(knowlegeItemVoList);
        return knowlege;
    }

    @Override
    public IPage<KnowlegeDataVo> getListInfo(Page<KnowlegeDataVo> page, KnowlegePageParam param) {
        return knowlegeMapper.getListInfo(page, param);
    }

    @Override
    public List<KnowlegeDataVo> getData(@Param("param") KnowlegePageParam param) {
        return knowlegeMapper.getListInfo(param);
    }

}
