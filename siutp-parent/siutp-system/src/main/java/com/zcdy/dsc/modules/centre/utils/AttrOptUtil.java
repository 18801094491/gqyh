package com.zcdy.dsc.modules.centre.utils;

import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.common.util.StringUtil;
import com.zcdy.dsc.modules.centre.constant.CentreTreeConstant;
import com.zcdy.dsc.modules.centre.entity.CentreTree;
import com.zcdy.dsc.modules.centre.vo.AttrTreeVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Description: 属性排序集合处理
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
public class AttrOptUtil {

    private static Logger logger = LoggerFactory.getLogger(AttrOptUtil.class);

    /**
     * 根据属性排序集合，将设备列表中的实际属性组织成树形结构
     * @param objlist 对象列表
     * @param attrs 属性排序集合，多个直接以逗号隔开
     * @param parentName 上级属性节点的属性排序集合名称
     * @param parentValue 上级属性节点的属性排序集合值
     * @param treeId 属性排序集合所在节点的id
     * @return
     */
    public static List<AttrTreeVo> attrInputCentreList(List objlist, String attrs, String parentName, String parentValue, String treeId)
    {
        if(objlist == null || attrs == null || objlist.size() == 0)
        {
            return null;
        }
        //要返回的list
        List<AttrTreeVo> attrTreeVoList = new ArrayList<>();
        try
        {
            Map<String, List> attrMap = new HashMap<>();
            String[] attrsArry = attrs.split(",");
            String attr = attrsArry[0];//获得第一个属性
            for(Object object : objlist)
            {
                String fieldTypeName = PropertyUtils.getPropertyType(object, attr).getName();
                if(fieldTypeName.equals("java.lang.String"))
                {
                    //字符串属性
                    String property = (String) PropertyUtils.getProperty(object, attr);//根据属性获取数值
                    if(StringUtils.isBlank(property))
                    {
                        property = CentreTreeConstant.Attr_BLANK_SHOW_TEXT;
                    }
                    List objModelList = attrMap.get(property);
                    if(objModelList == null)
                    {
                        objModelList = new ArrayList<>();
                    }
                    objModelList.add(object);
                    attrMap.put(property, objModelList);//根据不同数值，把结果集分组
                }
                else if(fieldTypeName.equals("java.util.Date"))
                {
                    //日期类型属性
                    Date property = (Date) PropertyUtils.getProperty(object, attr);//根据属性获取数值
                    String strProperty = null;
                    if(property == null)
                    {
                        //空日期处理
                        strProperty = CentreTreeConstant.Attr_BLANK_SHOW_TEXT;
                    }
                    else
                    {
                        strProperty = DateUtil.date2String(property, DateUtil.dateFormatStr);
                    }
                    List objModelList = attrMap.get(strProperty);
                    if(objModelList == null)
                    {
                        objModelList = new ArrayList<>();
                    }
                    objModelList.add(object);
                    attrMap.put(strProperty, objModelList);//根据不同数值，把结果集分组
                }

            }
            Set<String> keySet = attrMap.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext())
            {
                //整理分组后的信息，把key（属性值）放入centreTree，并累积到centreTreeList中
                AttrTreeVo attrTreeVo = new AttrTreeVo();
                String mapKey = iterator.next();
                attrTreeVo.setAttrValue(mapKey);
                attrTreeVo.setAttrName(attr);
                attrTreeVo.setParentAttrNames(parentName + attr + ",");
                attrTreeVo.setParentAttrValues(parentValue + mapKey + ",");
                attrTreeVo.setTreeId(treeId);
                List mapObjList = attrMap.get(mapKey);
                if(attrsArry.length > 1)
                {
                    //如果属性排序集合还有下一级，则去掉当前一级，再递归获取子集
                    String attrTail = attrs.substring(attrs.indexOf(",") + 1, attrs.length());
                    attrTreeVo.setChildren(attrInputCentreList(mapObjList, attrTail, attrTreeVo.getParentAttrNames(), attrTreeVo.getParentAttrValues(), treeId));
                }
                attrTreeVoList.add(attrTreeVo);
            }

        } catch (Exception e) {
            logger.error("解析设备属性时出错", e);
        }
        return attrTreeVoList;
    }
}
