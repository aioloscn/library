package com.aiolos.library.service;

import com.aiolos.common.config.IdGeneratorSnowflake;
import com.aiolos.common.utils.PagedResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Aiolos
 * @date 2021/3/20 5:43 下午
 */
public class BaseService extends ServiceImpl {

    @Autowired
    public IdGeneratorSnowflake snowflake;

    @Autowired
    public StringRedisTemplate redis;

    public PagedResult setterPagedResult(IPage<?> page) {
        PagedResult pagedResult = new PagedResult();
        pagedResult.setCurrent(page.getCurrent());
        pagedResult.setPages(page.getPages());
        pagedResult.setTotal(page.getTotal());
        pagedResult.setRecords(page.getRecords());
        return pagedResult;
    }
}
