package com.zcdy.dsc.modules.settle.service.impl;

import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.modules.settle.mapper.AnnualStatisticalMapper;
import com.zcdy.dsc.modules.settle.service.AnnualStatisticalService;
import com.zcdy.dsc.modules.settle.vo.AnnualStatisticalQueryParmVo;
import com.zcdy.dsc.modules.settle.vo.AnnualStatisticalRowVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 年度统计
 *
 * @author tangchao
 * @date 2020/5/25
 */
@Service
public class AnnualStatisticalServiceImpl implements AnnualStatisticalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnualStatisticalServiceImpl.class);

    private final AnnualStatisticalMapper baseMapper;

    public AnnualStatisticalServiceImpl(AnnualStatisticalMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public List<Map<String, String>> index(AnnualStatisticalQueryParmVo param) {
        List<Map<String, String>> statisticalList = new ArrayList<>();
        List<AnnualStatisticalRowVo> rowVos = this.baseMapper.queryAnnualStatistical(param);
        Map<String, Map<String, String>> yearTable = new LinkedHashMap<>();
        if (rowVos != null && rowVos.size() != 0) {
            for (AnnualStatisticalRowVo rowVo : rowVos) {
                Map<String, String> yearItem = yearTable.computeIfAbsent(rowVo.getCustomerId() + rowVo.getStatisticYear(), k -> {
                    HashMap<String, String> objectObjectHashMap = new HashMap<>(16);
                    Class<? extends AnnualStatisticalRowVo> clazz = rowVo.getClass();
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method m : methods) {
                        if (!m.getName().startsWith("get")) {
                            continue;
                        }
                        try {
                            String fieldName = getFieldNameForGetMethod(m);
                            clazz.getDeclaredField(fieldName);
                            objectObjectHashMap.put(fieldName, (String) m.invoke(rowVo));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                            LOGGER.error(e.getMessage());
                            throw new BusinessException("系统出错了.");
                        }
                    }
                    statisticalList.add(objectObjectHashMap);
                    return objectObjectHashMap;
                });
                yearItem.put("month" + rowVo.getStatisticMonth(),
                        new BigDecimal(rowVo.getMonthTotalFlow()).setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toEngineeringString() +
                                "," +
                                new BigDecimal(rowVo.getMonthTotalCost()).setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP).toEngineeringString());
                String[] yearTotles = yearItem.getOrDefault("year_totle", "0,0").split(",");
                BigDecimal flow = new BigDecimal(yearTotles[0]).add(new BigDecimal(rowVo.getMonthTotalFlow()));
                BigDecimal price = new BigDecimal(yearTotles[0]).add(new BigDecimal(rowVo.getMonthTotalCost()));
                yearItem.put("yearTotle",
                        flow.setScale(ScaleConstant.LL_SCALE_SHOW, RoundingMode.HALF_UP).toEngineeringString() +
                                "," +
                                price.setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP).toEngineeringString());
            }
        }

        for (Map<String, String> map : statisticalList) {
            Set<String> strings = map.keySet();
            for (int i = 1; i <= 12; i++) {
                if (!strings.contains("month" + i)) {
                    map.put("month" + i, "0.0, 0.00");
                }
            }
        }
        return statisticalList;
    }

    private String getFieldNameForGetMethod(Method method) {
        String fieldName = method.getName().replace("get", "");
        fieldName = fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1));
        return fieldName;
    }

}
