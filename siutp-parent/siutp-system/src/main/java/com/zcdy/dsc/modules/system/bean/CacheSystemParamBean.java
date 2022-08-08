package com.zcdy.dsc.modules.system.bean;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统参数缓存bean
 * @author Roberto
 * @date 2020/07/02
 */
@Component
public class CacheSystemParamBean implements InitializingBean {

    private static ConcurrentHashMap<String, String> CACHE = new ConcurrentHashMap<>(16);
    
    private static CacheSystemParamBean bean;
    
    @Resource
    private SystemParamService systemParamService;
    
    public static final String get(String key) {
        return CACHE.get(key);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        fillCache();
        bean = this;
    }
    
    /**
     * 执行同步
     */
    public static final void doSync() {
        if(null!=bean) {
            bean.fillCache();
        }else {
            throw new UnsupportedOperationException("不支持的调用方式");
        }
    }
    
    /**
     * 查询数据库数据并缓存
     */
    private synchronized void fillCache() {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<SystemConfig>();
        queryWrapper.eq("config_status", String.valueOf(StatusConstant.WORKING));
        List<SystemConfig> configs = this.systemParamService.list(queryWrapper);
        HashMap<String, String> map = new HashMap<>(configs.size());
        configs.forEach(item->{
            map.put(item.getConfigKey(), item.getConfigValue());
        });
        CACHE.clear();
        CACHE.putAll(map);
    }
}
