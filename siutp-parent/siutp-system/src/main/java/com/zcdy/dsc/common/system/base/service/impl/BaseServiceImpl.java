package com.zcdy.dsc.common.system.base.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.system.base.entity.BaseEntity;
import com.zcdy.dsc.common.system.base.service.BaseService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午10:09:13
 * 描述: <p>ServiceImpl基类</p>
 */
@Slf4j
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {

}
