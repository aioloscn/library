package com.aiolos.library.service;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.library.pojo.User;

import java.util.Set;

/**
 * @author Aiolos
 * @date 2021/3/19 4:48 上午
 */
public interface UserService {

    /**
     * 根据用户主键查询用户信息
     * @param userId
     * @return
     */
    User searchById(String userId);

    /**
     * 根据用户主键查询相关操作权限
     * @param userId
     * @return
     */
    Set<String> searchUserPermissions(String userId);

    /**
     * 注册用户
     * @param user
     * @return
     */
    int register(User user);

    /**
     * 查看手机号是否已存在
     * @param phone
     * @return
     */
    User queryPhoneIsExists(String phone);

    User create(String phone) throws CustomizeException;
}
