package com.aiolos.library.dao;

import com.aiolos.library.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserDao extends BaseMapper<User> {

    /**
     * 检查是否已经存在超级管理员账号
     * @return
     */
    boolean haveRootUser();

    /**
     * 根据用户主键查询权限列表
     * @param userId
     * @return
     */
    Set<String> searchUserPermissions(Long userId);
}