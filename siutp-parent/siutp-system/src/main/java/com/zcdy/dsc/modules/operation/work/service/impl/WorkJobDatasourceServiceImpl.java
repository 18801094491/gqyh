package com.zcdy.dsc.modules.operation.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobDatasource;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobDatasourceItem;
import com.zcdy.dsc.modules.operation.work.mapper.WorkJobDatasourceMapper;
import com.zcdy.dsc.modules.operation.work.param.WorkJobDatasourceParam;
import com.zcdy.dsc.modules.operation.work.service.WorkJobDatasourceItemService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobDatasourceService;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 工单数据项
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24
 * @version: V1.0
 */
@Service
public class WorkJobDatasourceServiceImpl extends ServiceImpl<WorkJobDatasourceMapper, WorkJobDatasource> implements WorkJobDatasourceService {

    @Resource
    private WorkJobDatasourceMapper workJobDatasourceMapper;

    @Resource
    private WorkJobDatasourceItemService workJobDatasourceItemService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveData(WorkJobDatasourceParam param) {
        WorkJobDatasource workJobDatasource = new WorkJobDatasource();
        String parentId = param.getParentId();
        BeanUtil.copyProperties(param, workJobDatasource);
        if (StringUtils.isEmpty(param.getId())) {
            if (StringUtils.isNotEmpty(parentId)) {
                //父id不为空， 设置父节点不为叶子节点
                workJobDatasourceMapper.updateLeaf(parentId, param.getTplId(), StatusConstant.INVALID);
            }
            //默认新增为叶子节点
            workJobDatasource.setIsLeaf(StatusConstant.VALID);
            workJobDatasourceMapper.insert(workJobDatasource);
        } else {
            WorkJobDatasource oldDataSource = this.getById(param.getId());
            //如果由子数据项改为根数据项，父id置为空
            String parent="0";
            if(parent.equals(param.getDataCategory())){
                workJobDatasource.setParentId("");
            }
            //判断下级是否有菜单,没有的设置为叶子节点
            int count = this.count(Wrappers.<WorkJobDatasource>lambdaQuery().eq(WorkJobDatasource::getParentId, parentId).eq(WorkJobDatasource::getTplId, param.getTplId()));
            if (count == 0) {
                workJobDatasource.setIsLeaf(StatusConstant.VALID);
            }
            workJobDatasourceMapper.updateById(workJobDatasource);

            //更改父级菜单情况
            String oldPid = oldDataSource.getParentId();
            //父id不为空,并且父id修改，跟原来不同
            boolean updateFlag = (StringUtils.isNotEmpty(parentId) && !parentId.equals(oldPid));
            //父id不为空,原来不存在父id
            boolean updateFlag2 = (StringUtils.isEmpty(parentId) && StringUtils.isNotEmpty(oldPid));
            if (updateFlag || updateFlag2) {
                //设置新的菜单不是叶子节点
                workJobDatasourceMapper.updateLeaf(parentId, param.getTplId(), StatusConstant.INVALID);
                //判断老的菜单下是否有其它子菜单，没有则设置为叶子节点
                int oldCount = this.count(Wrappers.<WorkJobDatasource>lambdaQuery().eq(WorkJobDatasource::getParentId, oldPid).eq(WorkJobDatasource::getTplId, param.getTplId()));
                if (oldCount == 0) {
                    workJobDatasourceMapper.updateLeaf(oldPid, param.getTplId(), StatusConstant.VALID);
                }
            }

        }

        //删除选项
        workJobDatasourceItemService.remove(Wrappers.<WorkJobDatasourceItem>lambdaQuery().eq(WorkJobDatasourceItem::getDatasourceId,workJobDatasource.getId()));
        //保存选项
        List<WorkJobDatasourceParam.DatasourceItem> items = param.getItems();
        if (items != null && items.size() > 0) {
            List<WorkJobDatasourceItem> saveItem = new ArrayList<>(items.size());
            items.forEach(item -> {
                WorkJobDatasourceItem datasourceItem = new WorkJobDatasourceItem();
                datasourceItem.setId(item.getItemId());
                datasourceItem.setDatasourceId(workJobDatasource.getId());
                datasourceItem.setItemName(item.getItemName());
                datasourceItem.setItemOrder(item.getItemOrder());
                saveItem.add(datasourceItem);
            });
            workJobDatasourceItemService.saveOrUpdateBatch(saveItem);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delete(String id) {
        WorkJobDatasource oldDataSource = this.getById(id);
        workJobDatasourceMapper.deleteById(id);
        workJobDatasourceItemService.remove(Wrappers.<WorkJobDatasourceItem>lambdaQuery().eq(WorkJobDatasourceItem::getDatasourceId, id));
        int count = this.count(Wrappers.<WorkJobDatasource>lambdaQuery().eq(WorkJobDatasource::getParentId, oldDataSource.getParentId()).eq(WorkJobDatasource::getTplId, oldDataSource.getTplId()));
        //更改父节点为叶子节点
        if (count == 0) {
            workJobDatasourceMapper.updateLeaf(oldDataSource.getParentId(), oldDataSource.getTplId(), StatusConstant.VALID);
        }
    }


    @Override
    public List<WorkJobDatasourceTree> tplDataTree(String tplId) {
        List<WorkJobDatasourceTree> datasourceList = workJobDatasourceMapper.tlpDataTree(tplId);
        List<WorkJobDatasourceTree> treeList = new ArrayList<>();
        getTreeData(treeList, datasourceList, null);
        return treeList;
    }

    /**
     * 转为树形结构
     *
     * @param treeList   树形结构
     * @param sourceList 源数据
     * @param temp       中间值
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
