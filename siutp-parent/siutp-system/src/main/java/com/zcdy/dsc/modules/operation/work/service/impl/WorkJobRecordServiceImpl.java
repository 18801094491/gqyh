package com.zcdy.dsc.modules.operation.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecord;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecordItem;
import com.zcdy.dsc.modules.operation.work.mapper.WorkJobRecordMapper;
import com.zcdy.dsc.modules.operation.work.param.RecordParam;
import com.zcdy.dsc.modules.operation.work.service.WorkJobRecordItemService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobRecordService;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 工单录入
 * @author: songguang.jiao
 * @date: 2020-07-16
 */
@Service
public class WorkJobRecordServiceImpl extends ServiceImpl<WorkJobRecordMapper, WorkJobRecord> implements WorkJobRecordService {

    @Resource
    private WorkJobRecordMapper workJobRecordMapper;

    @Resource
    private WorkJobRecordItemService workJobRecordItemService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveRecordItems(RecordParam recordParam) {
        List<WorkJobRecord> tplAndItems = workJobRecordMapper.getItemsFromDataSouece(recordParam.getTplId());
        Map<String, String> mapData = recordParam.getMapData();
        //批量插入主表
        for (WorkJobRecord workJobRecord : tplAndItems) {
            workJobRecord.setUserType(recordParam.getUserType());
            workJobRecord.setPlanId(recordParam.getPlanId());
            workJobRecord.setWorkJobId(workJobRecord.getWorkJobId());
            workJobRecord.setWorkJobId(recordParam.getWorkJobId());
        }
        this.saveBatch(tplAndItems);
        //生成子表数据，批量插入
        List<WorkJobRecordItem> itemList = new ArrayList<>();
        for (WorkJobRecord workJobRecord : tplAndItems) {
            if (workJobRecord.getItems() != null) {
                List<WorkJobRecordItem> items = workJobRecord.getItems();
                items.forEach(item -> {
                    item.setRecordId(workJobRecord.getId());
                    item.setItemName(item.getItemName());
                    item.setItemValue(mapData.get(item.getItemId()));
                });
            }
            itemList.addAll(workJobRecord.getItems());
        }
        workJobRecordItemService.saveBatch(itemList);
    }

    @Override
    public List<WorkJobDatasourceTree> getRecordItems(String workJobId, String planId, String userType) {
        List<WorkJobDatasourceTree> sourceList = workJobRecordMapper.getRecordItems(workJobId, planId, userType);
        List<WorkJobDatasourceTree> treeList = new ArrayList<>();
        getTreeData(treeList,sourceList,null);
        return treeList;
    }


    /**
     * 转为树形结构
     * @param treeList 树形结构
     * @param sourceList 源数据
     * @param temp 中间值
     */
    private void getTreeData(List<WorkJobDatasourceTree> treeList, List<WorkJobDatasourceTree> sourceList, WorkJobDatasourceTree temp) {
        for (WorkJobDatasourceTree workJobDatasource : sourceList) {
            String parentId = workJobDatasource.getParentId();
            WorkJobDatasourceTree tree = new WorkJobDatasourceTree();
            BeanUtil.copyProperties(workJobDatasource, tree);
            if (temp == null && StringUtils.isEmpty(parentId)) {
                treeList.add(tree);
                if (tree.getIsLeaf().equals(StatusConstant.INVALID)) {
                    getTreeData(treeList, sourceList, tree);
                }
            } else if (parentId != null && temp != null && parentId.equals(temp.getKey())) {
                if (temp.getChildren() == null) {
                    temp.setChildren(new ArrayList<WorkJobDatasourceTree>());
                }
                temp.getChildren().add(tree);
                if (tree.getIsLeaf().equals(StatusConstant.INVALID)) {
                    getTreeData(treeList, sourceList, tree);
                }
            }
        }
    }
}
