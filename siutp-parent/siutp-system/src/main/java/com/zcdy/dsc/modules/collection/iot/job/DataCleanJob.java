package com.zcdy.dsc.modules.collection.iot.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntityPool;
import com.zcdy.dsc.common.strategy.compare.CompareContext;
import com.zcdy.dsc.common.strategy.compare.CompareStrategy;
import com.zcdy.dsc.common.strategy.compare.EqualsStrategy;
import com.zcdy.dsc.common.strategy.compare.GreatThanAndEqualsStrategy;
import com.zcdy.dsc.common.strategy.compare.GreatThanStrategy;
import com.zcdy.dsc.common.strategy.compare.LessThanAndEqualsStrategy;
import com.zcdy.dsc.common.strategy.compare.LessThanStrategy;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.collection.iot.constant.ContentVariableConstant;
import com.zcdy.dsc.modules.collection.iot.constant.ModelStatusConstant;
import com.zcdy.dsc.modules.collection.iot.constant.WarnClassificationConstant;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipRuleEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipconfigEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentRuleItemEntity;
import com.zcdy.dsc.modules.collection.iot.entity.ModelStatusEntity;
import com.zcdy.dsc.modules.collection.iot.event.DataAlarmEvent;
import com.zcdy.dsc.modules.collection.iot.event.OfflineAlarmEvent;
import com.zcdy.dsc.modules.collection.iot.event.PressLessNumEvent;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import com.zcdy.dsc.modules.collection.iot.service.IoserverAlarmRuleService;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.constant.AndOrConstant;
import com.zcdy.dsc.modules.constant.EquipmentWorkStatusConstant;
import com.zcdy.dsc.modules.constant.QualityStampConstant;
import com.zcdy.dsc.modules.constant.WarnRuleConstant;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnTypeConstant;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnWayConstant;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessWarnService;
import com.zcdy.dsc.modules.system.bean.CacheSystemParamBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto 创建时间：2020年3月5日 下午6:04:53 描述：
 * <p>
 * 清洗IOServer数据，并将数据整合供监控中心和事件管理使用
 * </p>
 */
@DisallowConcurrentExecution
public class DataCleanJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 注入redis操作模板类
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    ListOperations<String, String> listOperations;

    // 注入告警规则业务逻辑层
    @Resource
    private IoserverAlarmRuleService ioserverAlarmRuleService;

    // 模型信息操作业务逻辑类
    @Resource
    private IOTModelService ioTModelService;

    // 报警信息数据库业务逻辑
    @Resource
    private BusinessWarnService businessWarnService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    // 配置的告警持续时长，ms
    @Value("${com.zcdy.dsc.var-alarm-expire:15000L}")
    private Long varAlarmExpire;

    // 默认的告警持续时长，ms
    @SuppressWarnings("unused")
    private final Long DEFAULT_VARIABLE_ALARM_EXPIRE = 10000L;

    /**
     * valueEntity 对象池
     */
    @Resource
    private ValueEntityPool valueEntityPool;

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     *      <p>
     *      数据清洗，根据采集设备的数据规则，清洗出设备状态、告警事件
     *      </p>
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        long start = System.currentTimeMillis();

        // 获取所有的设备
        List<IotEquipconfigEntity> iotEquipments = this.ioserverAlarmRuleService.getIotQualitys();
        int room = iotEquipments.size();
        Map<String, IotEquipconfigEntity> iotEquipmentsMap = new HashMap<>(room);
        iotEquipments.forEach(item -> {
            iotEquipmentsMap.put(item.getEquipmentId(), item);
        });
        iotEquipments = null;

        // 获取所有采集设备规则信息
        List<IotEquipRuleEntity> iotEquipRules = this.ioserverAlarmRuleService.getAllEquipmentRules();
        // 获取所有的规则
        List<IotEquipmentRuleItemEntity> ruleItems = this.ioserverAlarmRuleService.getAllRuleItems();
        // 按照设备分配规则，所有的告警规则
        HashMap<String, List<IotEquipmentRuleItemEntity>> ruleItemsMap = new HashMap<>(ruleItems.size());
        ruleItems.forEach(item -> {
            if (null != item.getRuleId()) {
                List<IotEquipmentRuleItemEntity> items = ruleItemsMap.get(item.getRuleId());
                if (null == items) {
                    items = new ArrayList<>();
                    ruleItemsMap.put(item.getRuleId(), items);
                }
                items.add(item);
            }
        });
        ruleItems = null;

        // 存储所有采集不好的设备id
        Map<String, IotEquipconfigEntity> badQualityEquipments = new HashMap<>(room);

        // 存储正常状态的设备信息
        Map<String, String> normalEquipments = new HashMap<>(room);
        // 存储所有其它告警的设备信息
        Map<String, List<RuleMatch>> warnEquipments = new HashMap<>(room);
        // 存储处于开状态的设备
        Map<String, String> openEquipments = new HashMap<>(room);
        // 存储处于关状态的设备
        Map<String, String> closedEquipments = new HashMap<>(room);

        // 记录告警的设备的设备和告警信息
        Map<String, IotEquipRuleEntity> warnEquipmentMap = new HashMap<>(room);

        Map<String, List<IotEquipRuleEntity>> iotEquipRulesMap = new HashMap<>(room);
        // 按照设备分组规则
        for (IotEquipRuleEntity entity : iotEquipRules) {
            List<IotEquipRuleEntity> entitys = iotEquipRulesMap.get(entity.getEquipmentId());
            if (null == entitys) {
                entitys = new ArrayList<>();
                iotEquipRulesMap.put(entity.getEquipmentId(), entitys);
            }
            if (null != entity.getRuleId()) {
                if (StringUtils.isEmpty(entity.getAlarmStatus())) {
                    entitys.add(entity);
                } else {
                    if ((StatusConstant.RUN + "").equals(entity.getAlarmStatus())) {
                        entitys.add(entity);
                    }
                }
            }
        }
        iotEquipRules = null;

        Set<Entry<String, List<IotEquipRuleEntity>>> entrySets = iotEquipRulesMap.entrySet();
        for (Entry<String, List<IotEquipRuleEntity>> entry : entrySets) {

            String equipmentId = entry.getKey();
            IotEquipconfigEntity config = iotEquipmentsMap.get(equipmentId);
            List<IotEquipRuleEntity> entitys = entry.getValue();

            /* **************************************************
             * 如果不配置规则需要检测是否使用质量戳
             * 如果使用质量戳，就检测当前采集状态，如果不使用就不处理
             * 全部设备过一遍质量戳
             * *************************************************/
            if (null != config && null != config.getCheckQuality()
                && QualityStampConstant.YES.equals(config.getCheckQuality())) {
                // 获取当前设备的一个变量校验质量戳
                String randomVarName = this.ioTModelService.getOneVarNameRandom(equipmentId);
                // 如果没有绑定变量
                if (StringUtils.isEmpty(randomVarName)) {
                    continue;
                }
                String messageContent = this.stringRedisTemplate.opsForValue()
                    .get(String.format(RedisKeyConstantV2.REDISDATAKEY, randomVarName));
                ValueEntity valueInfo = JSON.parseObject(messageContent, ValueEntity.class);
                if (null == valueInfo) {
                    // 无采集数据
                    badQualityEquipments.put(equipmentId, config);
                } else {
                    // 采集质量不好，业务规则不用校验了
                    if (QualityStampConstant.QUALITY_BAD.equals(valueInfo.getQualityStamp())) {
                        badQualityEquipments.put(equipmentId, config);
                        continue;
                    } else {
                        normalEquipments.put(equipmentId, equipmentId);
                    }
                }
            } else {
                normalEquipments.put(equipmentId, equipmentId);
            }

            // 7标压力
            if ("02bf0fb366ba11eab600685d43cf9012".equals(equipmentId)) {
                String varName = "ZHGL_7_SLL_0010_P";
                IotEquipRuleEntity ruleEntity = this.ioserverAlarmRuleService.findRuleEntity(equipmentId);
                dealYLJ(varName, equipmentId, warnEquipments, normalEquipments, warnEquipmentMap, ruleEntity);
                continue;
            }

            // 2标压力
            if ("02bf0e1066ba11eab600685d43cf9012".equals(equipmentId)) {
                String varName = "ZHGL_2_ZTXL_1590_P";
                IotEquipRuleEntity ruleEntity = this.ioserverAlarmRuleService.findRuleEntity(equipmentId);
                dealYLJ(varName, equipmentId, warnEquipments, normalEquipments, warnEquipmentMap, ruleEntity);
                continue;
            }

            // 如果当前设备采集质量不佳，剩余未校验规则跳过
            if (!badQualityEquipments.containsKey(equipmentId)) {
                // 如果已经判定为告警，那么不需要在适配其他规则
                if (warnEquipments.containsKey(equipmentId)) {
                    continue;
                }

                // 查询所有设备规则信息
                for (IotEquipRuleEntity entity : entitys) {
                    String alarmStatus = entity.getAlarmStatus();
                    // 规则id
                    String ruleId = entity.getRuleId();
                    // 或还是并
                    String useAndOr = entity.getAndOr();
                    if (StringUtils.isEmpty(useAndOr)) {
                        useAndOr = AndOrConstant.LOGIC_AND;
                    }
                    // 状态区分
                    String statusType = entity.getStatusType();
                    // 采集设备id

                    // 告警规则匹配情况列表
                    List<RuleMatch> mataches = new ArrayList<>();

                    // 用于判断开状态的所有匹配结果
                    List<Boolean> openStatusList = new ArrayList<>();
                    // 用于判断关状态的所有匹配结果
                    List<Boolean> closedStatusList = new ArrayList<>();

                    // 判断规则是否启用
                    // 如果规则停用了
                    if (!StringUtils.isEmpty(alarmStatus) && (StatusConstant.STOP + "").equals(alarmStatus)) {
                        continue;
                    }

                    // 获取所有的规则项目
                    List<IotEquipmentRuleItemEntity> items = ruleItemsMap.get(ruleId);
                    if (null != statusType && null != items && items.size() > 0) {
                        // 遍历所有规则项
                        for (IotEquipmentRuleItemEntity item : items) {
                            // 获取当前变量的最新值
                            String messageContent = this.stringRedisTemplate.opsForValue()
                                .get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVariableName()));
                            if (null == messageContent) {
                                continue;
                            }
                            ValueEntity valueInfo = JSON.parseObject(messageContent, ValueEntity.class);
                            String value = valueInfo.getValue();

                            if (!valueInfo.getVariableName().endsWith("#ZT")) {
                                if (null == config.getCheckQuality()
                                    || config.getCheckQuality().equals(QualityStampConstant.YES)) {
                                    // 采集质量不好，业务规则不用校验了
                                    if (QualityStampConstant.QUALITY_BAD.equals(valueInfo.getQualityStamp())) {
                                        badQualityEquipments.put(equipmentId, config);
                                        break;
                                    } else {
                                        // 如果处理过程中质量戳又变好了，移除记录
                                        badQualityEquipments.remove(equipmentId);
                                        if (!warnEquipments.containsKey(equipmentId)
                                            && !openEquipments.containsKey(equipmentId)
                                            && !closedEquipments.containsKey(equipmentId)) {
                                            normalEquipments.put(equipmentId, equipmentId);
                                        }
                                    }
                                }
                            }
                            // 数据类型
                            String dataType = item.getDataType();
                            // 规则内容
                            String alarmRule = item.getAlarmRule();
                            // 规则值
                            String ruleValue = item.getAlarmValue();

                            boolean isMatch = false;
                            // 定义策略
                            CompareStrategy strategy = null;
                            switch (alarmRule) {
                                case WarnRuleConstant.greatThan: {
                                    strategy = new GreatThanStrategy();
                                    break;
                                }
                                case WarnRuleConstant.equals: {
                                    strategy = new EqualsStrategy();
                                    break;
                                }
                                case WarnRuleConstant.greatThanAndEquals: {
                                    strategy = new GreatThanAndEqualsStrategy();
                                    break;
                                }
                                case WarnRuleConstant.lessThan: {
                                    strategy = new LessThanStrategy();
                                    break;
                                }
                                case WarnRuleConstant.lessThanAndEquals: {
                                    strategy = new LessThanAndEqualsStrategy();
                                    break;
                                }
                                default:
                                    break;
                            }

                            if (StringUtils.isEmpty(value)) {
                                value = "0";
                            } else {
                                value = new BigDecimal(value).setScale(item.getScale(), RoundingMode.HALF_UP)
                                    .toEngineeringString();
                            }
                            CompareContext compareContext = new CompareContext(strategy);
                            isMatch = compareContext.matches(dataType, value, ruleValue);

                            // 匹配规则并设置小数位
                            switch (statusType) {
                                case EquipmentWorkStatusConstant.WARN: {
                                    RuleMatch matchItem = new RuleMatch();
                                    int scale = item.getScale();
                                    if (scale == 0) {
                                        scale = 2;
                                    }
                                    if (scale > 0) {
                                        BigDecimal bg = new BigDecimal(value);
                                        bg = bg.setScale(scale, RoundingMode.HALF_UP);
                                        matchItem.setValue(bg.toString());
                                    } else {
                                        matchItem.setValue(value);
                                    }

                                    matchItem.setWarn(isMatch);
                                    matchItem.setAlarmRule(alarmRule);
                                    matchItem.setVariableTitle(item.getVariableTitle());
                                    matchItem.setLimitValue(ruleValue);
                                    mataches.add(matchItem);
                                    break;
                                }
                                case EquipmentWorkStatusConstant.OPEN: {
                                    openStatusList.add(isMatch);
                                    break;
                                }
                                case EquipmentWorkStatusConstant.CLOSED: {
                                    closedStatusList.add(isMatch);
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                        }

                        // 组合所有规则项目
                        // 判断最终是否告警的标识
                        boolean needWarn = true;
                        // 是否匹配开规则
                        boolean isOpenMatch = true;
                        // 是否匹配关规则
                        boolean isClosedMatch = true;

                        // 处理与关系
                        if (useAndOr.equals(AndOrConstant.LOGIC_AND)) {
                            if (mataches.size() > 0) {
                                for (RuleMatch one : mataches) {
                                    if (!one.warn) {
                                        needWarn = false;
                                        break;
                                    }
                                }
                            } else {
                                needWarn = false;
                            }
                            // 打开状态匹配列表不为空
                            if (openStatusList.size() > 0) {
                                for (boolean one : openStatusList) {
                                    if (!one) {
                                        isOpenMatch = false;
                                        break;
                                    }
                                }
                            } else {
                                isOpenMatch = false;
                            }
                            if (closedStatusList.size() > 0) {
                                for (boolean one : closedStatusList) {
                                    if (!one) {
                                        isClosedMatch = false;
                                        break;
                                    }
                                }
                            } else {
                                isClosedMatch = false;
                            }

                        } else {
                            // 处理或关系
                            if (mataches.size() > 0) {
                                for (RuleMatch one : mataches) {
                                    if (one.warn) {
                                        needWarn = true;
                                        break;
                                    } else {
                                        needWarn = false;
                                    }
                                } ;
                            } else {
                                needWarn = false;
                            }

                            if (openStatusList.size() > 0) {
                                for (boolean one : openStatusList) {
                                    if (one) {
                                        isOpenMatch = true;
                                        break;
                                    } else {
                                        isOpenMatch = false;
                                    }
                                } ;
                            } else {
                                isOpenMatch = false;
                            }

                            if (closedStatusList.size() > 0) {
                                for (boolean one : closedStatusList) {
                                    if (one) {
                                        isClosedMatch = true;
                                        break;
                                    } else {
                                        isClosedMatch = false;
                                    }
                                } ;
                            } else {
                                isClosedMatch = false;
                            }
                        }

                        // 赋值匹配规则
                        // 如果当前设备处于告警状态，就存入告警状态的map
                        if (needWarn) {
                            warnEquipments.put(equipmentId, mataches);
                            warnEquipmentMap.put(equipmentId, entity);
                            if (normalEquipments.containsKey(equipmentId)) {
                                normalEquipments.remove(equipmentId);
                            }
                        } else {
                            if (!warnEquipments.containsKey(equipmentId) && !openEquipments.containsKey(equipmentId)
                                && !closedEquipments.containsKey(equipmentId)) {
                                normalEquipments.put(equipmentId, equipmentId);
                            }
                        }
                        if (isOpenMatch) {
                            // 排除处于告警状态的设备
                            if (!warnEquipments.containsKey(equipmentId)
                                && !closedEquipments.containsKey(equipmentId)) {
                                openEquipments.put(equipmentId, equipmentId);
                                normalEquipments.remove(equipmentId);
                                closedEquipments.remove(equipmentId);
                            }
                        }
                        if (isClosedMatch) {
                            // 排除处于告警状态和开状态的设备
                            if (!warnEquipments.containsKey(equipmentId) && !openEquipments.containsKey(equipmentId)) {
                                closedEquipments.put(equipmentId, equipmentId);
                                normalEquipments.remove(equipmentId);
                                openEquipments.remove(equipmentId);
                            }
                        }
                    } else {
                        if (!warnEquipments.containsKey(equipmentId) && !openEquipments.containsKey(equipmentId)
                            && !closedEquipments.containsKey(equipmentId)) {
                            normalEquipments.put(equipmentId, equipmentId);
                        }
                    }

                }
            }
        }

        // 处理所有采集不佳的设备
        badQualityEquipments.forEach((key, value) -> {
            ModelStatusEntity entity = new ModelStatusEntity();
            entity.setStatus(ModelStatusConstant.STATUS_WARNING);
            entity.setDetailStatus(ModelStatusConstant.STATUS_WARNING_OFFONLINE);
            entity.setMessage("设备离线");
            updateRedisEquipmentStatus(key, entity);
            // 是否生成告警事件
            if (null != value.getNeedAlarm() && value.getNeedAlarm().equals(QualityStampConstant.YES)) {
                // 生成告警
                createBadQualityWarnEvent(value);
            }
        });

        // 处理所有数据告警的设备
        warnEquipments.forEach((key, value) -> {
            ModelStatusEntity entity = new ModelStatusEntity();
            entity.setStatus(ModelStatusConstant.STATUS_WARNING);
            entity.setDetailStatus(ModelStatusConstant.STATUS_WARNING_DATAALARM);
            entity.setMessage("数据告警");
            updateRedisEquipmentStatus(key, entity);

            IotEquipRuleEntity iotEquipRuleEntity = warnEquipmentMap.get(key);
            // 生成告警
            createBusinessWarn(value, iotEquipRuleEntity);
        });

        // 处理正常设备
        normalEquipments.forEach((key, value) -> {
            ModelStatusEntity entity = new ModelStatusEntity();
            entity.setStatus(ModelStatusConstant.STATUS_NORMAL);
            entity.setDetailStatus(ModelStatusConstant.STATUS_NORMAL_ONLINE);
            entity.setMessage("设备正常");
            updateRedisEquipmentStatus(key, entity);
        });

        // 使用状态规则
        openEquipments.forEach((key, value) -> {
            // 设置设备的状态为开状态
            ModelStatusEntity entity = new ModelStatusEntity();
            entity.setStatus(ModelStatusConstant.STATUS_NORMAL);
            entity.setDetailStatus(ModelStatusConstant.STATUS_NORMAL_OPEN);
            entity.setMessage("设备开");
            updateRedisEquipmentStatus(key, entity);
        });

        closedEquipments.forEach((key, value) -> {
            // 设置设备的状态为关状态
            ModelStatusEntity entity = new ModelStatusEntity();
            entity.setStatus(ModelStatusConstant.STATUS_NORMAL);
            entity.setDetailStatus(ModelStatusConstant.STATUS_NORMAL_CLOSED);
            entity.setMessage("设备关");
            updateRedisEquipmentStatus(key, entity);
        });

        if (logger.isDebugEnabled()) {
            long end = System.currentTimeMillis();
            logger.debug("采集数据清洗任务执行完成，耗时：" + (end - start) + "ms");
        }
    }

    /**
     * @author：Roberto 创建时间：2020年3月7日 下午4:26:19 描述：
     *                 <p>
     *                 更新redis中设备的状态
     *                 </p>
     */
    private void updateRedisEquipmentStatus(String key, ModelStatusEntity entity) {
        this.stringRedisTemplate.opsForValue().set(String.format(RedisKeyConstantV2.MODEL_LAST_STATUS_KEY, key),
            JSON.toJSONString(entity));
    }

    /**
     * @author：Roberto 创建时间：2020年3月7日 下午8:31:04 描述：
     *                 <p>
     *                 生成设备离线告警
     *                 </p>
     * @param equipmentId
     *            设备id
     * @param tpl
     *            告警事件时间内容模板
     * @param level
     *            告警事件等级
     */
    private void createBadQualityWarnEvent(IotEquipconfigEntity entity) {

        // 渲染告警模板
        String tpl = entity.getQualityAlarmMessage();
        if (null == tpl) {
            tpl = "";
        }
        tpl = tpl.replace(ContentVariableConstant.eqName, entity.getEquipmentName());
        tpl = tpl.replace(ContentVariableConstant.eqAddress, entity.getEquipmentLocation());
        tpl = tpl.replace(ContentVariableConstant.eqType, entity.getEquipmentType());
        tpl = tpl.replace(ContentVariableConstant.sequenceNo, entity.getEquipmentSn());
        tpl = tpl.replace(ContentVariableConstant.section, entity.getEquipmentSection());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        tpl = tpl.replace(ContentVariableConstant.systemTime, sf.format(now));
        // 不支持
        tpl = tpl.replace(ContentVariableConstant.ruleValue, "");
        tpl = tpl.replace(ContentVariableConstant.limitValue, "");
        tpl = tpl.replace(ContentVariableConstant.variableName, "");
        tpl = tpl.replace(ContentVariableConstant.variableValue, "");

        // 生成告警信息
        BusinessWarn event = new BusinessWarn();
        event.setWarnLevel(entity.getQualityAlarmLevel());
        event.setWarnName(entity.getEquipmentName() + "离线");
        event.setWarnSn(DateUtil.getTimeStamp());
        event.setWarnStatus(WarnStatusConstant.INIT);
        event.setWarnType(WarnTypeConstant.COLLECTION);
        event.setWarnWay(WarnWayConstant.SYSTEM);
        event.setWarnTime(now);
        event.setConfirmStatus(WarnStatusConstant.CONFIRM_WILL);
        event.setWarnContent(tpl);
        event.setIotId(entity.getEquipmentId());
        event.setRuleId(null);
        event.setRuleContent("通讯故障");

        // 添加事件的分类
        event.setWarnClassification(WarnClassificationConstant.OFFLINE);

        QueryWrapper<BusinessWarn> queryWrap = new QueryWrapper<>();
        /*
        queryWrap.eq("iot_id", entity.getEquipmentId()).ne("confirm_status", WarnStatusConstant.CLOSED)
        	.notIn("warn_status", WarnStatusConstant.DEAL, WarnStatusConstant.DEAL_CLOSED);
        Page<BusinessWarn> page = new Page<>(1,1);
        IPage<BusinessWarn> bws = this.businessWarnService.page(page, queryWrap);
        if(bws.getTotal()<=0){
            businessWarnService.save(event);
            //通知观察者
            OfflineAlarmEvent alarmEvent = new OfflineAlarmEvent(this, event);
            this.applicationEventPublisher.publishEvent(alarmEvent);
        }
        */
        queryWrap.select(" 1 as ex ");
        queryWrap.eq("iot_id", entity.getEquipmentId()).ne("confirm_status", WarnStatusConstant.CLOSED)
            .notIn("warn_status", WarnStatusConstant.DEAL, WarnStatusConstant.DEAL_CLOSED);
        if (!businessWarnService.checkExists(queryWrap)) {
            businessWarnService.save(event);
            // 通知观察者
            OfflineAlarmEvent alarmEvent = new OfflineAlarmEvent(this, event);
            this.applicationEventPublisher.publishEvent(alarmEvent);
        }
    }

    /**
     * @author：Roberto
     * @param iotEquipRuleEntity
     *            参数体 创建时间：2020年3月7日 上午10:47:04 描述：
     *            <p>
     *            生成告警事件，如果当前设备还有未处理告警事件，那么忽略本次告警
     *            </p>
     */
    private void createBusinessWarn(List<RuleMatch> matches, IotEquipRuleEntity entity) {

        // 如果当前规则不告警，那么不生成事件
        if (null == entity || StringUtils.isEmpty(entity.getRuleAlarm())
            || entity.getRuleAlarm().equals(StatusConstant.STOP + "")) {
            return;
        }

        Set<String> varName = new HashSet<>();
        Set<String> varValue = new HashSet<>();
        Set<String> ruleValue = new HashSet<>();
        List<String> rules = new ArrayList<>();
        matches.forEach(item -> {
            varName.add(item.getVariableTitle());
            varValue.add(item.getValue());
            ruleValue.add(item.getLimitValue());
            String runFlag = "";
            switch (item.alarmRule) {
                case WarnRuleConstant.greatThan: {
                    runFlag = ">";
                    break;
                }
                case WarnRuleConstant.equals: {
                    runFlag = "==";
                    break;
                }
                case WarnRuleConstant.greatThanAndEquals: {
                    runFlag = ">=";
                    break;
                }
                case WarnRuleConstant.lessThan: {
                    runFlag = "<";
                    break;
                }
                case WarnRuleConstant.lessThanAndEquals: {
                    runFlag = "<=";
                    break;
                }
                default:
                    runFlag = "==";
                    break;
            }
            rules.add(item.getVariableTitle() + "[" + item.getValue() + "]" + runFlag + item.getLimitValue());
        });

        // 渲染告警模板
        String tpl = entity.getMessageValue();
        Date now = new Date();
        if (null == tpl) {
            tpl = "设备告警";
        } else {
            tpl = tpl.replace(ContentVariableConstant.eqName, entity.getEquipmentName());
            tpl = tpl.replace(ContentVariableConstant.eqAddress, entity.getEquipmentLocation());
            tpl = tpl.replace(ContentVariableConstant.eqType, entity.getEquipmentType());
            tpl = tpl.replace(ContentVariableConstant.sequenceNo, entity.getEquipmentSn());
            tpl = tpl.replace(ContentVariableConstant.section, entity.getEquipmentSection());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            tpl = tpl.replace(ContentVariableConstant.systemTime, sf.format(now));

            tpl = tpl.replace(ContentVariableConstant.ruleValue, "[" + StringUtils.join(ruleValue, ",") + "]");
            tpl = tpl.replace(ContentVariableConstant.limitValue, "[" + StringUtils.join(ruleValue, ",") + "]");
            tpl = tpl.replace(ContentVariableConstant.variableName, "[" + StringUtils.join(varName, ",") + "]");
            tpl = tpl.replace(ContentVariableConstant.variableValue, "[" + StringUtils.join(varValue, ",") + "]");
        }
        // 生成告警信息
        BusinessWarn event = new BusinessWarn();
        event.setWarnLevel(entity.getAlarmLevel());
        event.setWarnName(entity.getEquipmentType() + "[" + entity.getEquipmentSn() + "]" + entity.getAlarmName());
        event.setWarnSn(DateUtil.getTimeStamp());
        event.setWarnStatus(WarnStatusConstant.INIT);
        event.setWarnType(WarnTypeConstant.COLLECTION);
        event.setWarnWay(WarnWayConstant.SYSTEM);
        event.setWarnTime(now);
        event.setWarnContent(tpl);
        event.setConfirmStatus(WarnStatusConstant.CONFIRM_WILL);
        event.setIotId(entity.getEquipmentId());
        event.setRuleId(entity.getRuleId());

        // 事件分类
        event.setWarnClassification(WarnClassificationConstant.DATAALARM);

        String logic = entity.getAndOr().equals(AndOrConstant.LOGIC_AND) ? "并且" : "或者";
        StringBuilder stringBuilder = new StringBuilder();
        int size = rules.size();
        for (int index = 0; index < size; index++) {
            stringBuilder.append(rules.get(index));
            if (index < (size - 1)) {
                stringBuilder.append(logic);
            }
        }
        event.setRuleContent(stringBuilder.toString());

        QueryWrapper<BusinessWarn> queryWrap = new QueryWrapper<>();
        queryWrap.select(" 1 as ex ");
        queryWrap.eq("iot_id", entity.getEquipmentId()).ne("confirm_status", WarnStatusConstant.CLOSED)
            .notIn("warn_status", WarnStatusConstant.DEAL, WarnStatusConstant.DEAL_CLOSED);
        if (!businessWarnService.checkExists(queryWrap)) {
            businessWarnService.save(event);
            
            if(entity.getEquipmentId().equals("02bf0fb366ba11eab600685d43cf9012")){
              //如果压力小于底线，那么设置小于底线的标识避免清洗数据的过程再次发送短信
                String timeKey = String.format(RedisKeyConstantV2.MODEL_VARIABLE_WARN_TIME, "ZHGL_7_SLL_0010_P");
                long listSize = this.stringRedisTemplate.opsForList().size(timeKey);
                if (listSize < 1) {
                    // 此时无法判断设备的压力趋势
                } else {
                    List<String> tempPressList = this.stringRedisTemplate.opsForList().range(timeKey, 0, 0);
                    String tempPress = tempPressList.get(0);
                    BigDecimal prevPress = new BigDecimal(tempPress);
                    if(prevPress.compareTo(new BigDecimal("0.100"))<=0) {
                        String warnBottomSmsKey = String.format(RedisKeyConstantV2.ALARM_EVENT_SMS_FLAG, event.getId());
                        if(!this.stringRedisTemplate.hasKey(warnBottomSmsKey)) {
                          //设置已发送短信标识，避免再次发送
                            this.stringRedisTemplate.opsForValue().set(warnBottomSmsKey, "1");
                        }
                    }
                }
            }
            
            // 通知观察者
            DataAlarmEvent alarmEvent = new DataAlarmEvent(this, event);
            this.applicationEventPublisher.publishEvent(alarmEvent);
        }
        /*
        queryWrap.eq("iot_id", entity.getEquipmentId()).ne("confirm_status", WarnStatusConstant.CLOSED)
        	.notIn("warn_status", WarnStatusConstant.DEAL, WarnStatusConstant.DEAL_CLOSED);
        Page<BusinessWarn> page = new Page<>(1,1);
        IPage<BusinessWarn> bws = this.businessWarnService.page(page, queryWrap);
        if(bws.getTotal()<=0){
        	businessWarnService.save(event);
        	//通知观察者
        	DataAlarmEvent alarmEvent = new DataAlarmEvent(this, event);
        	this.applicationEventPublisher.publishEvent(alarmEvent);
        }
        */
    }

    /**
     * 处理压力计,针对客户定制
     * 
     * @param varName
     * @param equipmentId
     * @param warnEquipments
     * @param normalEquipments
     * @param warnEquipmentMap
     * @param entity
     *            规则
     *            <p>
     *            LRANGE 获取redis的前两个， 或多个 根据数据变化判定各个状态 rpop,如果llen已经超过10（某个数值）个执行rpop， 保持list长度 lpush，在列表头添加数据
     *            </p>
     */
    private final void dealYLJ(String varName, String equipmentId, Map<String, List<RuleMatch>> warnEquipments,
        Map<String, String> normalEquipments, Map<String, IotEquipRuleEntity> warnEquipmentMap,
        IotEquipRuleEntity entity) {
        // 底线
        String bottomLine = "0.240";
        // 恢复线
        String middleLine = "0.260";
        String valStr = CacheSystemParamBean.get(SystemParamConstant.PRESS_7_RESUME_POINT_VALUE);
        if (!StringUtils.isEmpty(valStr)) {
            middleLine = valStr;
        }
        // 顶线
        String topLine = "0.390";
        // 压力恢复至此触发恢复事件
        String topResumeLine = "0.370";

        // 2020年7月2日，针对0.1下报警
        String mostBottomLine = "0.100";

        String messageContent =
            this.stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.REDISDATAKEY, varName));
        if (null == messageContent) {
            return;
        }
        ValueEntity valueInfo = JSON.parseObject(messageContent, ValueEntity.class);
        String value = valueInfo.getValue();
        if (StringUtils.isEmpty(value)) {
            value = "0.00";
        }
        int scale = 3;
        BigDecimal press = new BigDecimal(value);
        press = press.setScale(scale, RoundingMode.HALF_UP);
        BigDecimal bottom = new BigDecimal(bottomLine);
        BigDecimal middle = new BigDecimal(middleLine);

        // 顶线
        BigDecimal top = new BigDecimal(topLine);
        BigDecimal topResume = new BigDecimal(topResumeLine);

        // 底线
        BigDecimal mostBottom = new BigDecimal(mostBottomLine);

        // 7表数据队列
        String timeKey = String.format(RedisKeyConstantV2.MODEL_VARIABLE_WARN_TIME, varName);
        long listSize = this.listOperations.size(timeKey);

        // 低于底线，高于高线
        if (press.compareTo(bottom) < 0 || press.compareTo(top) > 0) {
            List<RuleMatch> mataches = new ArrayList<>();

            RuleMatch matchItem = new RuleMatch();
            matchItem.setValue(press.toString());
            matchItem.setWarn(press.compareTo(bottom) < 0);
            matchItem.setAlarmRule(WarnRuleConstant.lessThan);
            matchItem.setVariableTitle("压力");
            matchItem.setLimitValue(bottomLine);
            mataches.add(matchItem);

            RuleMatch matchItem2 = new RuleMatch();
            matchItem2.setValue(press.toString());
            matchItem2.setWarn(press.compareTo(top) > 0);
            matchItem2.setAlarmRule(WarnRuleConstant.greatThan);
            matchItem2.setVariableTitle("压力");
            matchItem2.setLimitValue(topLine);
            mataches.add(matchItem2);
            // 告警
            normalEquipments.remove(equipmentId);

            // 处理压力计特殊处理
            warnEquipments.put(equipmentId, mataches);

            // 2020-07-02添加小于0.1时发送短信
            if (press.compareTo(mostBottom) < 0) {
             // 7标数据处理
                BigDecimal prevPress = BigDecimal.ZERO;
                // 初始化的时候listSize为0，之后维持在10
                if (listSize < 1) {
                    // 此时无法判断设备的压力趋势
                } else {
                    List<String> tempPressList = this.listOperations.range(timeKey, 0, 0);
                    String tempPress = tempPressList.get(0);
                    prevPress = new BigDecimal(tempPress);
                }
                if (prevPress.compareTo(bottom) >= 0) {
                    // 直接从低端压力线下降至最底端线，这个时候只发送一个短信
                    // 告警事件要生成，并且发送短信，所以这里不需要处理，走原来的模式
                } else {
                    // 逐渐从低端线到底端线
                    // 这种情况下，告警事件已经有了，所以要查到告警事件同时记录下此告警的二次报警已经发送，
                    // 如果已经发送就不需要再次发送了，避免短信炸弹
                    // 拿到最新一条告警并做处理
                    QueryWrapper<BusinessWarn> queryWrap = new QueryWrapper<>();
                    queryWrap.eq("iot_id", equipmentId).ne("confirm_status", WarnStatusConstant.CLOSED)
                        .notIn("warn_status", WarnStatusConstant.DEAL, WarnStatusConstant.DEAL_CLOSED);
                    Page<BusinessWarn> page = new Page<>(1, 1);
                    IPage<BusinessWarn> bws = this.businessWarnService.page(page, queryWrap);
                    if (bws.getTotal() <= 0) {
                        // 如果查不到记录，说明上游处理有问题
                        // 这里只能忽略
                    } else {
                        BusinessWarn warn = bws.getRecords().get(0);
                        String warnBottomSmsKey = String.format(RedisKeyConstantV2.ALARM_EVENT_SMS_FLAG, warn.getId());
                        if(!this.stringRedisTemplate.hasKey(warnBottomSmsKey)) {
                            try {
                                PressLessNumEvent event = new PressLessNumEvent(this, press.toString(), new Date());
                                this.applicationEventPublisher.publishEvent(event);
                                //设置已发送短信标识，避免再次发送
                                this.stringRedisTemplate.opsForValue().set(warnBottomSmsKey, "1");
                            } catch (Exception e) {
                                // 如果抛出异常说明有多记录，那么不触发事件
                                logger.error(e.getMessage());
                            }
                        }
                    }
                }
            }

            warnEquipmentMap.put(equipmentId, entity);

            // 存储最新的7和2标的压力异常值
            String warnKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, varName);
            if (!this.stringRedisTemplate.hasKey(warnKey)) {
                ValueEntity valueEntity = this.valueEntityPool.get();
                valueEntity.setValue(press.toEngineeringString());
                this.stringRedisTemplate.opsForValue().set(warnKey, JSON.toJSONString(valueEntity));
                this.valueEntityPool.release(valueEntity);
            }
        } else {
            /*
             * 正常范围之内，执行如下代码
             */
            if (press.compareTo(topResume) <= 0 && press.compareTo(middle) >= 0) {
                processNormalValue(varName, equipmentId, normalEquipments, press);
            } else {
                String obj = this.stringRedisTemplate.opsForValue()
                    .get(String.format(RedisKeyConstantV2.MODEL_LAST_STATUS_KEY, equipmentId));
                if (null == obj) {
                    // redis中无设备的最后状态，使用默认状态[正常状态]
                    normalEquipments.put(equipmentId, equipmentId);
                } else {
                    // redis中有最后设备状态，如果这次是正常的，就不更新状态，减少状态的更新频率，节省资源
                    normalEquipments.remove(equipmentId);
                }
            }
        }
        // 保持队列是10个元素，多了暂时用不上
        if (listSize >= 10) {
            this.listOperations.rightPop(timeKey);
            this.listOperations.leftPush(timeKey, press.toString());
        } else {
            this.listOperations.leftPush(timeKey, press.toString());
        }
    }

    /**
     * 处理压力恢复
     * 
     * @param varName
     * @param equipmentId
     * @param normalEquipments
     * @param press
     */
    private void processNormalValue(String varName, String equipmentId, Map<String, String> normalEquipments,
        BigDecimal press) {
        normalEquipments.put(equipmentId, equipmentId);
        /*
         * 将最新的采集到的正常值存储，便于短信使用该值作为恢复时的数据 
         */
        // 存储最新的7和2标的压力恢复值
        String normalKey = String.format(RedisKeyConstantV2.MODEL_PRESS_NORMAL, varName);
        String warnKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, varName);

        if (!this.stringRedisTemplate.hasKey(normalKey) && this.stringRedisTemplate.hasKey(warnKey)) {
            // 从对象池中获取对象，减少new带来的内存消耗
            ValueEntity valueEntity = this.valueEntityPool.get();
            valueEntity.setValue(press.toEngineeringString());
            this.stringRedisTemplate.opsForValue().set(normalKey, JSON.toJSONString(valueEntity));
            // 使用完将对象释放
            this.valueEntityPool.release(valueEntity);
        }
    }

    /**
     * 初始化redis的list指令集
     */
    @PostConstruct
    public void setListOperations() {
        this.listOperations = this.stringRedisTemplate.opsForList();
    }

    /**
     * @author： Roberto 创建时间：2020年3月7日 下午8:43:36 描述：
     * <p>
     * 变量匹配结果信息
     * </p>
     */
    @Setter
    @Getter
    private static final class RuleMatch {

        // 采集量
        private String value;

        // 规则量
        private String limitValue;

        // 变量title，中文释义
        private String variableTitle;

        // 规则
        private String alarmRule;

        // 是否匹配规则的标识
        private boolean warn;
    }
}
