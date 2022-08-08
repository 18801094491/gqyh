 package com.zcdy.dsc.modules.system.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.system.service.ISysLogService;

import io.swagger.annotations.Api;

/**
  * 系统首页控制器
 * @author Roberto
 * @date 2020/05/11
 */
@RestController
@RequestMapping("/sys")
@Api(tags="系统首页香瓜")
public class SystemIndexController {

    @Resource
    private ISysLogService sysLogService;
    
    /**
     * 获取访问量
     * @return
     */
    @GetMapping("visitInfo")
    public Result<List<Map<String,Object>>> visitInfo() {
        Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        List<Map<String,Object>> list = this.sysLogService.findVisitCount(dayStart, dayEnd);
        result.setResult(ConvertUtils.toLowerCasePageList(list));
        return result;
    }
}
