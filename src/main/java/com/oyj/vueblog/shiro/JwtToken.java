package com.oyj.vueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token赋值和获取用户信息的类，需实现shiro的AuthenticationToken接口
 * @author a123
 * @since 2022-06-29
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    /**
     * 构造方法，创建JwtToken对象并赋值
     * @param jwt
     */
    public JwtToken(String jwt) {
        this.token = jwt;
    }

    /**
     * 在父类中是获取身份信息的方法
     * @return 此处是返回token
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    /**
     * 覆写父类中获取密码的方法
     * @return 返回token
     */
    @Override
    public Object getCredentials() {
        return token;
    }
}
