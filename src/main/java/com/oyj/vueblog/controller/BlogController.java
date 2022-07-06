package com.oyj.vueblog.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oyj.vueblog.Result;
import com.oyj.vueblog.entity.Blog;
import com.oyj.vueblog.service.BlogService;
import com.oyj.vueblog.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 蓝脚鲣鸟
 * @since 2022-06-26
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    /**
     * 查询当前所有博客的方法
     * @param currentPage 当前页码
     * @return 返回查询内容
     */
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {

        // Page对象两个参数:当前页码，每页条数
        Page page = new Page(currentPage, 5);
        // 按传入的参数查询固定的条数，并将查询结果封装在一个IPage对象中
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.succ(pageData);
    }

    /**
     * 根据ID查询博客的方法
     * @param id 博客ID
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.succ(blog);
    }

    /**
     * 编辑博客的方法
     * @RequiresAuthentication 需要用户登录才能操作
     * @param blog
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }
}
