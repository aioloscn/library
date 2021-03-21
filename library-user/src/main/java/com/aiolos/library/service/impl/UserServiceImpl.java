package com.aiolos.library.service.impl;

import com.aiolos.common.config.IdGeneratorSnowflake;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.enums.Sex;
import com.aiolos.common.enums.UserStatus;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.CommonUtils;
import com.aiolos.library.dao.UserDao;
import com.aiolos.library.pojo.User;
import com.aiolos.library.service.BaseService;
import com.aiolos.library.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 因为OAuth2Realm中需要注入这个对象，使其在Spring没有生成代理对象前初始化了，普通对象没法开启事务
 * ScopedProxyMode.TARGET_CLASS: 每次调用生成不同的实例解决这个问题
 * @author Aiolos
 * @date 2021/3/19 4:48 上午
 */
@Slf4j
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserServiceImpl extends BaseService implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User searchById(String userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", userId);
        wrapper.eq("status", UserStatus.ACTIVE.getType());
        return userDao.selectOne(wrapper);
    }

    @Override
    public Set<String> searchUserPermissions(String userId) {
        return userDao.searchUserPermissions(userId);
    }

    @Override
    public int register(User user) {
        return userDao.insert(user);
    }

    @Override
    public User queryPhoneIsExists(String phone) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone", phone);
        return userDao.selectOne(wrapper);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public User create(String phone) throws CustomizeException {

        User user = new User();
        user.setId(snowflake.nextIdStr());
        user.setPhone(phone);
        user.setNickname("用户" + CommonUtils.hidePhoneNo(phone));
        user.setSex(Sex.secret.getValue());
        user.setStatus(UserStatus.ACTIVE.getType());
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());

        int resultCount = userDao.insert(user);
        if (resultCount != 1) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.REGISTER_FAILED);
            }
        }

        return user;
    }

    @Override
    public List<User> searchBatchIds(List<String> ids) {
        return userDao.selectBatchIds(ids);
    }
}
