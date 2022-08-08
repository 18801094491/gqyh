package com.zcdy.dsc.modules.operation.equipment.vo.check.storebill;

import javax.validation.GroupSequence;

/**
 * 描述: 校验入库参数
 * @author：  songguang.jiao
 * 创建时间： 2020年2月6日 下午12:05:40 
 * 版本号: V1.0
 */
@GroupSequence({CheckAmount.class,CheckSparepartId.class,CheckStoreId.class})
public interface CheckStoreBill {
}
