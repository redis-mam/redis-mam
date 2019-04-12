package com.dobe.redis.controller;

import com.dobe.redis.model.ResponseEntity;
import com.dobe.redis.model.User;
import com.dobe.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 *
 * @author zc.ding
 * @since 1.0
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @RequestMapping("login")
    public ResponseEntity<?> login(@RequestParam String userName, @RequestParam String pwd) {
        return userService.validUser(userName, pwd);
    }

    @RequestMapping("userList")
    public List<User> userList(User user) {
        return userService.findUserList(user);
    }

    @RequestMapping("chgUserState")
    public ResponseEntity<?> chgUserState(String name, String state) {
        return userService.updateUserState(name, state);
    }

    @RequestMapping("delUser")
    public ResponseEntity<?> delUser(String name) {
        return this.userService.delUser(name);
    }
}
