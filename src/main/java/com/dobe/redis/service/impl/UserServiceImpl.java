package com.dobe.redis.service.impl;

import com.dobe.redis.dao.UserDao;
import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.User;
import com.dobe.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zc.ding
 * @since 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Override
    public ResponseEntity<?> addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public ResponseEntity<?> delUser(String name) {
        return userDao.delUser(name);
    }

    @Override
    public ResponseEntity<?> updateUserState(String name, String state) {
        return this.userDao.updateUserState(name, state);
    }

    @Override
    public List<User> findUserList(User user) {
        return this.userDao.findUserList(user);
    }

    @Override
    public User findUserByName(String name) {
        return this.userDao.findUserByName(name);
    }

    @Override
    public ResponseEntity<?> validUser(String name, String pwd) {
        User user = this.userDao.findUserByName(name);
        if (user != null && pwd.equals(user.getPwd())) {
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.error("用户名或密码错误");
    }
}
