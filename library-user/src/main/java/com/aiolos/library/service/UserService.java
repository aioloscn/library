package com.aiolos.library.service;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.pojo.User;

import java.util.List;
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

    /**
     * 创建用户信息
     * @param phone
     * @return
     * @throws CustomizeException
     */
    User create(String phone) throws CustomizeException;

    /**
     * 根据主键集合查询相应的数据
     * @param ids
     * @return
     */
    List<User> searchBatchIds(List<String> ids);
}
