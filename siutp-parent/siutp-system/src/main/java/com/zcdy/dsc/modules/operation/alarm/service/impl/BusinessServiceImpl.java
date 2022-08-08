package com.zcdy.dsc.modules.operation.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.framework.redis.RedisOperation;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.collection.iot.constant.VariNameConstant;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.constant.ProcessRouteConstant;
import com.zcdy.dsc.modules.message.handle.enums.SendMsgTypeEnum;
import com.zcdy.dsc.modules.message.util.PushMsgUtil;
import com.zcdy.dsc.modules.operation.alarm.constant.UserChooseType;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.alarm.entity.*;
import com.zcdy.dsc.modules.operation.alarm.mapper.BusinessDao;
import com.zcdy.dsc.modules.operation.alarm.mapper.BusinessWarnMapper;
import com.zcdy.dsc.modules.operation.alarm.mapper.BusinessWarnPolicyMapper;
import com.zcdy.dsc.modules.operation.alarm.param.BusinessWarnParam;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessService;
import com.zcdy.dsc.modules.operation.alarm.vo.*;
import com.zcdy.dsc.modules.operation.alarm.vo.PolicyListVo.PolicyUser;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipment;
import com.zcdy.dsc.modules.operation.work.vo.TeamDropdown;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * 描述: 报警相关字段已接口实现
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月18日 上午11:05:47
 * 版本号: V1.0
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessDao, BusinessWarn> implements BusinessService {

    @Resource
    private BusinessWarnMapper warnMapper;
    @Resource
    private BusinessDao businessDao;
    @Resource
    private BusinessWarnPolicyMapper policyMapper;
    @Resource
    private PushMsgUtil pushMsgUtil;
    
    @Resource
    private RedisOperation<String> stringRedisOperation;

    @Override
    public IPage<BusinessWarnVo> queryWarnData(Page<BusinessWarnVo> page, String warnName, String warnLevel,
                                               String warnType, String warnStatus, String equipmentType, String startTime, String endTime) {
        return businessDao.queryWarnData(page, warnName, warnLevel, warnType, warnStatus, equipmentType, startTime, endTime);
    }

    @Override
    public void closeData(BusinessDeal business) {
        Date date = new Date();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //计算持续时长
        long seconds = DateUtil.getSeconds(DateUtil.convertDateToLocalDate(business.getWarnTime()), DateUtil.convertDateToLocalDate(date));

        //系统加入了自动处理逻辑，所以需要考虑重复关闭的问题
        //如果已经被关闭了，就不执行了
        BusinessWarn one = this.warnMapper.selectById(business.getId());
        if (WarnStatusConstant.CONFIRM_WILL.equals(one.getConfirmStatus())) {
            businessDao.closeData(business.getId(), business.getDescription(), user.getId(), date, seconds);
        }
        
        String p7RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, VariNameConstant.YL_7_01);
        String p7RedisKey1 = String.format(RedisKeyConstantV2.MODEL_PRESS_NORMAL, VariNameConstant.YL_7_01);
        //删除已发送最小下线的短信
        String warnBottomSmsKey = String.format(RedisKeyConstantV2.ALARM_EVENT_SMS_FLAG, one.getId());
        //需要删除redis的告警和恢复key
        stringRedisOperation.del(p7RedisKey, p7RedisKey1,warnBottomSmsKey);
    }

    @Override
    public void addWarnInfo(List<BusinessWarn> warnList) {
        businessDao.insertWarnInfo(warnList);
    }

    @Override
    public Map<String, VariInfo> getAllVarinfo() {
        List<VariInfo> infos = this.businessDao.selectAllVar();
        Map<String, VariInfo> map = new HashMap<String, VariInfo>(infos.size());
        infos.forEach(item -> {
            if (!StringUtils.isEmpty(item.getVarName())) {
                map.put(item.getVarName(), item);
            }
        });
        return map;
    }

    @Override
    public boolean checkWarnExist(String warnName) {
        return businessDao.checkWarnExist(warnName);
    }

    @Override
    public String getWarnId(String warnName) {
        return businessDao.getWarnId(warnName);
    }

    @Override
    public void updateWarnInfo(List<BusinessWarn> warnList) {
        businessDao.updateWarnInfo(warnList);
    }

    @Override
    public void confirmData(String id, String warnStatus) {
        businessDao.confirmData(id, warnStatus);
    }

    @Override
    public List<BusinessWarnVo> queryUndealData(String undeal1, String undeal2) {
        return businessDao.queryUndealData(undeal1, undeal2);
    }

    @Override
    public List<BusinessWarnVo> queryWarnNum() {
        return businessDao.queryWarnNum();
    }

    @Override
    public List<BusinessWarnVo> queryUndealDataNew(String undeal1, String undeal2) {
        return businessDao.queryUndealDataNew(undeal1, undeal2);
    }

    @Override
    public List<WeekData> getSevenDayData() {
        return businessDao.getSevenDayData();
    }

    @Override
    public List<MonthYearData> queryMonthYearData(String type) {
        //1-月份,2-年份(默认月)
        String year="2";
        if (year.endsWith(type)) {
            return businessDao.queryByYear();
        }
        return businessDao.queryByMonth();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void addPolicy(PolicyParamVo vo) {
        BusinessWarnPolicy policy = new BusinessWarnPolicy();
        policy.setDelFlag(DelFlagConstant.NORMAL);
        policy.setName(vo.getName());
        policy.setWorkStatus(vo.getWorkStatus());
        policy.setWarnLevel(vo.getWarnLevel());
        policy.setSmsTemplate(vo.getSmsTemplate());
        policyMapper.insert(policy);

        //遍历3种类型,分别存储各个类型id
        List<BusinessWarnPolicyUsers> list = new ArrayList<BusinessWarnPolicyUsers>();
        if (!StringUtils.isEmpty(vo.getUsersId())) {
            String[] usersId = vo.getUsersId().split(",");
            for (int i = 0; i < usersId.length; i++) {
                BusinessWarnPolicyUsers user = new BusinessWarnPolicyUsers();
                user.setDataId(usersId[i]);
                user.setUserChooseType(UserChooseType.USER);
                user.setPolicyId(policy.getId());
                user.setId(UUIDGenerator.generate());
                list.add(user);
            }
        }
        if (!StringUtils.isEmpty(vo.getRolesId())) {
            String[] rolesId = vo.getRolesId().split(",");
            for (int i = 0; i < rolesId.length; i++) {
                BusinessWarnPolicyUsers user = new BusinessWarnPolicyUsers();
                user.setDataId(rolesId[i]);
                user.setUserChooseType(UserChooseType.ROLE);
                user.setPolicyId(policy.getId());
                user.setId(UUIDGenerator.generate());
                list.add(user);
            }
        }
        if (!StringUtils.isEmpty(vo.getWorkTeamsId())) {
            String[] teamsId = vo.getWorkTeamsId().split(",");
            for (int i = 0; i < teamsId.length; i++) {
                BusinessWarnPolicyUsers user = new BusinessWarnPolicyUsers();
                user.setDataId(teamsId[i]);
                user.setUserChooseType(UserChooseType.TEAM);
                user.setPolicyId(policy.getId());
                user.setId(UUIDGenerator.generate());
                list.add(user);
            }
        }
        if (list.size() > 0 && !list.isEmpty()) {
            businessDao.insertBatch(list);
        }
    }

    @Override
    public List<RoleNameVo> getRoleUsers(String roleName) {
        return businessDao.getRoleUsers(roleName);
    }

    @Override
    public List<String> queryUserIds(String policyId) {
        return businessDao.queryUserIds(policyId);
    }

    @Override
    public List<TeamDropdown> getWorkUsers(String name) {
        return businessDao.getWorkUsers(name);
    }

    @Override
    public boolean checkPolicyExist(PolicyParamVo vo) {
        return businessDao.checkPolicyExist(vo);
    }

    @Override
    public void editPolicy(PolicyParamVo vo) {
        BusinessWarnPolicy policy = new BusinessWarnPolicy();
        policy.setId(vo.getId());
        policy.setSmsTemplate(vo.getSmsTemplate());
        policy.setWorkStatus(vo.getWorkStatus());
        policy.setName(vo.getName());
        policy.setWarnLevel(vo.getWarnLevel());
        policyMapper.updateById(policy);

        //删除已存储用户信息
        businessDao.deletePolicyUsers(policy.getId());
        //遍历3种类型,分别存储各个类型id
        List<BusinessWarnPolicyUsers> list = new ArrayList<BusinessWarnPolicyUsers>();
        if (!StringUtils.isEmpty(vo.getUsersId())) {
            String[] usersId = vo.getUsersId().split(",");
            for (int i = 0; i < usersId.length; i++) {
                BusinessWarnPolicyUsers user = new BusinessWarnPolicyUsers();
                user.setDataId(usersId[i]);
                user.setUserChooseType(UserChooseType.USER);
                user.setPolicyId(policy.getId());
                user.setId(UUIDGenerator.generate());
                list.add(user);
            }
        }
        if (!StringUtils.isEmpty(vo.getRolesId())) {
            String[] rolesId = vo.getRolesId().split(",");
            for (int i = 0; i < rolesId.length; i++) {
                BusinessWarnPolicyUsers user = new BusinessWarnPolicyUsers();
                user.setDataId(rolesId[i]);
                user.setUserChooseType(UserChooseType.ROLE);
                user.setPolicyId(policy.getId());
                user.setId(UUIDGenerator.generate());
                list.add(user);
            }
        }
        if (!StringUtils.isEmpty(vo.getWorkTeamsId())) {
            String[] teamsId = vo.getWorkTeamsId().split(",");
            for (int i = 0; i < teamsId.length; i++) {
                BusinessWarnPolicyUsers user = new BusinessWarnPolicyUsers();
                user.setDataId(teamsId[i]);
                user.setUserChooseType(UserChooseType.TEAM);
                user.setPolicyId(policy.getId());
                user.setId(UUIDGenerator.generate());
                list.add(user);
            }
        }
        if (list.size() > 0 && !list.isEmpty()) {
            businessDao.insertBatch(list);
        }
    }

    @Override
    public IPage<PolicyListVo> queryPolicyPageData(Page<PolicyListVo> page, String name) {
        /**
         * 处理数据格式list中3个数组
         * list:[{type:'班组',dataName:'班组1','班组2'},{type:'角色',dataName:'角色1','角色2'},{type:'系统角色',dataName:'系统角色1','系统角色2'}]
         */
        IPage<PolicyListVo> data = businessDao.queryPolicyPageData(page, name);
        List<PolicyListVo> records = data.getRecords();
        if (records.size() > 0 && !records.isEmpty()) {
            for (PolicyListVo policyUsersVo : records) {
                List<PolicyUser> list = policyUsersVo.getList();
                List<PolicyUser> newList = new ArrayList<>();
                if (list.size() > 0 && !list.isEmpty()) {
                    PolicyUser user = new PolicyUser();
                    StringBuffer sbUser = new StringBuffer();
                    PolicyUser role = new PolicyUser();
                    StringBuffer sbRole = new StringBuffer();
                    PolicyUser team = new PolicyUser();
                    StringBuffer sbTeam = new StringBuffer();
                    for (PolicyUser policy : list) {
                        if (policy.getUserChooseTypeCode().endsWith(UserChooseType.USER)) {
                            user.setUserChooseType(policy.getUserChooseType());
                            sbUser.append(policy.getDataName() + ",");
                        }
                        if (policy.getUserChooseTypeCode().endsWith(UserChooseType.ROLE)) {
                            role.setUserChooseType(policy.getUserChooseType());
                            sbRole.append(policy.getDataName() + ",");
                        }
                        if (policy.getUserChooseTypeCode().endsWith(UserChooseType.TEAM)) {
                            team.setUserChooseType(policy.getUserChooseType());
                            sbTeam.append(policy.getDataName() + ",");
                        }
                    }
                    if (!StringUtils.isEmpty(sbUser.toString())) {
                        String sbUsers = sbUser.toString().substring(0, sbUser.toString().length() - 1);
                        user.setDataName(sbUsers);
                    }
                    if (!StringUtils.isEmpty(sbRole.toString())) {
                        String sbRoles = sbRole.toString().substring(0, sbRole.toString().length() - 1);
                        role.setDataName(sbRoles);
                    }
                    if (!StringUtils.isEmpty(sbTeam.toString())) {
                        String sbTeams = sbTeam.toString().substring(0, sbTeam.toString().length() - 1);
                        team.setDataName(sbTeams);
                    }
                    newList.add(user);
                    newList.add(role);
                    newList.add(team);
                    policyUsersVo.setList(newList);
                }
            }
        }
        return data;
    }

    @Override
    public PolicyDetailVo getDetail(String id) {
        PolicyDetailVo policeUsersVo = new PolicyDetailVo();
        PoliceDetailData policeUsersData = businessDao.getDetail(id);
        BeanUtils.copyProperties(policeUsersData, policeUsersVo);
        List<BusinessWarnPolicyUsers> list = policeUsersData.getList();
        if (list.size() > 0 && !list.isEmpty()) {
            for (BusinessWarnPolicyUsers users : list) {
                if (UserChooseType.USER.endsWith(users.getUserChooseType())) {
                    policeUsersVo.setUsersId(users.getDataId());
                }
                if (UserChooseType.ROLE.endsWith(users.getUserChooseType())) {
                    policeUsersVo.setRolesId(users.getDataId());
                }
                if (UserChooseType.TEAM.endsWith(users.getUserChooseType())) {
                    policeUsersVo.setWorkTeamsId(users.getDataId());
                }
            }
        }
        return policeUsersVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        businessDao.deletePolicy(id);
        businessDao.deletePolicyUsers(id);
    }

    @Override
    public void changeStatus(String id) {
        businessDao.changeStatus(id);

    }

    @Override
    public List<SmsTemplateVo> queryTemplate(String templateName) {
        return businessDao.queryTemplate(templateName);
    }

    @Override
    public boolean sendWarnMsg(String businssWarnId) {
        BusinessWarn warn = warnMapper.selectById(businssWarnId);
        BusinessWarnPolicy policy = businessDao.queryPolicyByLevel(warn.getWarnLevel());
        if (null == policy) {
            return false;
        }
        WarnUsersId usersId = businessDao.queryPolicyAllUserId(policy.getId());
        if (null == usersId) {
            return false;
        }
        if (usersId != null && policy != null && warn != null && WorkingStatus.WORKING.equals(policy.getWorkStatus())) {
            boolean sendStatus = pushMsgUtil.sendBatchMessage(SendMsgTypeEnum.SYS.getType(), policy.getSmsTemplate(), this.concatSendData(warn), this.concatUserId(usersId));
            return sendStatus;
        }
        return false;
    }

    //TODO发送消息通知模板内容
    private Map<String, String> concatSendData(BusinessWarn warn) {
        OptEquipment equipment = businessDao.queryEquipment(warn.getIotId());
        Map<String, String> map = new HashMap<>(10);
        if (equipment != null && !StringUtils.isEmpty(equipment.getEquipmentName())) {
            map.put("equipName", equipment.getEquipmentName());
        }
        map.put("warnContent", warn.getDescription());
        map.put("warnTime", warn.getWarnTime().toString());
        return map;
    }


    //将所有用户id转为数组   拼接数据不为空时,用逗号分隔
    private String concatUserId(WarnUsersId usersId) {
        String sentTo = "";
        if (!StringUtils.isEmpty(usersId.getUsersId())) {
            sentTo = sentTo + usersId.getUsersId();
        }
        if (!StringUtils.isEmpty(usersId.getRoleUsersId())) {
            sentTo = sentTo + usersId.getRoleUsersId();
        } else {
            sentTo = sentTo + "," + usersId.getRoleUsersId();
        }
        if (!StringUtils.isEmpty(usersId.getTeamUsersId())) {
            sentTo = sentTo + usersId.getTeamUsersId();
        } else {
            sentTo = sentTo + "," + usersId.getTeamUsersId();
        }
        return sentTo;
    }

    @Override
    public List<PolicyOneDayVo> oneDayData() {
        return businessDao.oneDayData();
    }

    @Override
    public List<IconListVo> queryAllIcon() {
        return businessDao.queryAllIcon();
    }

    /*
     * @see com.zcdy.dsc.modules.operation.alarm.service.BusinessService#dealData(com.zcdy.dsc.modules.operation.alarm.vo.BusinessDeal)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void dealData(@Valid BusinessDeal business) {
        Date date = new Date();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //计算持续时长
        long seconds = DateUtil.getSeconds(DateUtil.convertDateToLocalDate(business.getWarnTime()), DateUtil.convertDateToLocalDate(date));
        BusinessWarn warn = this.warnMapper.selectById(business.getId());
        warn.setDuration(seconds + "");
        warn.setWarnStatus(WarnStatusConstant.DEAL);
        warn.setOperator(user.getId());
        warn.setOperateTime(date);
        warn.setProcessRoute(ProcessRouteConstant.ROUTE_MANUAL);
        
        String p7RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, VariNameConstant.YL_7_01);
        String p7RedisKey1 = String.format(RedisKeyConstantV2.MODEL_PRESS_NORMAL, VariNameConstant.YL_7_01);
        //删除已发送最小下线的短信
        String warnBottomSmsKey = String.format(RedisKeyConstantV2.ALARM_EVENT_SMS_FLAG, warn.getId());
        //需要删除redis的告警和恢复key
        stringRedisOperation.del(p7RedisKey, p7RedisKey1, warnBottomSmsKey);
        
        this.warnMapper.updateById(warn);
    }

    @Override
    public List<BusinessWarnVo> getBusinessWarnVoById(String id) {
        return businessDao.getBusinessWarnVoById(id);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.operation.alarm.service.BusinessService#getCount()
     */
    @Override
    public int getCount() {
        QueryWrapper<BusinessWarn> wrapper = new QueryWrapper<>();
        List<String> status = new ArrayList<>(2);
        status.add(WarnStatusConstant.INIT);
        status.add(WarnStatusConstant.UNDEAL);
        wrapper.lambda().in(BusinessWarn::getWarnStatus, status);
        return this.warnMapper.selectCount(wrapper);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.operation.alarm.service.BusinessService#queryAlarmFullDataList(long, int)
     */
    @Override
    public List<AlarmResultVO> queryAlarmFullDataList(long startIndex, int pageSize) {
        return this.businessDao.selectAlarmFullDataList(startIndex, pageSize);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.operation.alarm.service.BusinessService#queryAlarmFullDataPage(com.baomidou.mybatisplus.extension.plugins.pagination.Page, java.lang.String)
     */
    @Override
    public Page<AlarmResultVO> queryAlarmFullDataPage(Page<AlarmResultVO> pageinfo, String param, String equipTypeStr) {
        String[] equipTypes = null;
        if (!StringUtils.isEmpty(equipTypeStr)) {
            equipTypes = equipTypeStr.split(",");
        }
        return this.businessDao.selectAlarmFullDataPage(pageinfo, param, equipTypes);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.operation.alarm.service.BusinessService#getAlarmDataDetail(java.lang.String)
     */
    @Override
    public AlarmResultDetailVO getAlarmDataDetail(String bid) {
        return this.businessDao.selectAlarmDataDetail(bid);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.operation.alarm.service.BusinessService#getVarsByIotId(java.lang.String)
     */
    @Cacheable(cacheNames = "com:zcdy:dsc:iot:model:vars", key = "#iotId")
    @Override
    public List<VariableInfo> getVarsByIotId(String iotId) {
        return businessDao.selectVarsByIotId(iotId);
    }

    @Override
    public List<PolicyOneDayVo> getWarnLevelInfoToday() {
        return businessDao.selectWarnLevelInfoToday();
    }

    @Override
    public IPage<BusinessWarnVo> getAppBusinessList(Page<BusinessWarnVo> page, BusinessWarnParam param) {
        return businessDao.getAppBusinessPageList(page, param);
    }

    @Override
    public List<BusinessWarnVo> getAppBusinessList(BusinessWarnParam param) {
        return businessDao.getAppBusinessList(param);
    }

    @Override
    public BusinessWarnVo getAppBusinessDetail(String id) {
        return businessDao.getAppBusinessDetail(id);
    }
}
