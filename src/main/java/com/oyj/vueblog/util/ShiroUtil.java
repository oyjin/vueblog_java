package com.oyj.vueblog.util;

import com.oyj.vueblog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * 查询当前用户信息的类
 * @author a123
 * @since 2022-07-02
 */
public class ShiroUtil {
    /**
     * 通过调用shiro的SecurityUtils.getSubject().getPrincipal()方法获取当前登录用户的信息实体类
     * @return
     */
    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
