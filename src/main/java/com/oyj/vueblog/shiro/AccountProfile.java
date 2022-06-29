package com.oyj.vueblog.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * shiro存放用户身份信息的实体类
 * @author a123
 * @since 2022-06-29
 */
@Data
public class AccountProfile implements Serializable {
    private Long id;

    private String username;

    private String avatar;

    private String email;
}
