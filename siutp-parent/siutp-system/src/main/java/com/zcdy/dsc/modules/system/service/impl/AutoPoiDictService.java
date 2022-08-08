package com.zcdy.dsc.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jeecgframework.dict.service.AutoPoiDictServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.system.mapper.SysDictMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：AutoPoi Excel注解支持字典参数设置 举例： @Excel(name = "性别", width = 15, dicCode = "sex") 
 * 1、导出的时候会根据字典配置，把值1,2翻译成：男、女;
 * 2、导入的时候，会把男、女翻译成1,2存进数据库;
 * @author Roberto
 * @date 2020/05/11
 */
@Slf4j
@Service
public class AutoPoiDictService implements AutoPoiDictServiceI {
    
    @Autowired
    private SysDictMapper sysDictMapper;

    /**
     * 通过字典查询easypoi，所需字典文本
     * @return 字典数组
     */
    @Override
    public String[] queryDict(String dicTable, String dicCode, String dicText) {
        List<String> dictReplaces = new ArrayList<String>();
        List<DictModel> dictList = null;
        // step.1 如果没有字典表则使用系统字典表
        if (ConvertUtils.isEmpty(dicTable)) {
            dictList = sysDictMapper.queryDictItemsByCode(dicCode);
        } else {
            try {
                dicText = ConvertUtils.getString(dicText, dicCode);
                dictList = sysDictMapper.queryTableDictItemsByCode(dicTable, dicText, dicCode);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        for (DictModel t : dictList) {
            dictReplaces.add(t.getText() + "_" + t.getValue());
        }
        if (dictReplaces != null && dictReplaces.size() != 0) {
            log.info("---AutoPoi--Get_DB_Dict------" + dictReplaces.toString());
            return dictReplaces.toArray(new String[dictReplaces.size()]);
        }
        return null;
    }
}
