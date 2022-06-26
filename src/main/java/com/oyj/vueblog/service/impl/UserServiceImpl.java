package com.oyj.vueblog.service.impl;

import com.oyj.vueblog.entity.User;
import com.oyj.vueblog.mapper.UserMapper;
import com.oyj.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 蓝脚鲣鸟
 * @since 2022-06-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
