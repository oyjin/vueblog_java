package com.oyj.vueblog.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oyj.vueblog.Result;
import com.oyj.vueblog.entity.LoginDto;
import com.oyj.vueblog.entity.User;
import com.oyj.vueblog.service.UserService;
import com.oyj.vueblog.util.JwtUtils;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author a123
 * @since 2022-07-01
 */
@RestController
@Api(value="用户controller",tags={"用户操作接口"})
public class AccountController {
    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 登录方法
     * @param loginDto 登录信息实体类，默认账号密码：admin/111111
     * @param response
     * @return
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在！");

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return Result.succ(new HashMap<String, Object>(){{
            put("id", user.getId());
            put("username", user.getUsername());
            put("avatar", user.getAvatar());
            put("email", user.getEmail());
        }});
    }

    /**
     * 退出登录的方法，直接调用shiro提供的logout方法
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

}
