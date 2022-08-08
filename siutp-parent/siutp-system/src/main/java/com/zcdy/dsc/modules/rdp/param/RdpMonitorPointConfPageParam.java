package com.zcdy.dsc.modules.rdp.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 分页查询大屏重点监控配置列表参数
 * @Author: 在信汇通
 * @Date: 2020-12-11 9:30:06
 * @Version: V1.0
 */
@ApiModel(value = "RdpMonitorPointConfPageParam", description = "大屏重点监控配置列表参数")
@Getter
@Setter
public class RdpMonitorPointConfPageParam extends AbstractPageParam {

}
