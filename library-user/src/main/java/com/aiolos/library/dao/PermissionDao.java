package com.aiolos.library.dao;

import com.aiolos.library.pojo.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends BaseMapper<Permission> {
}