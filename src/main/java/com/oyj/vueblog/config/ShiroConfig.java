package com.oyj.vueblog.config;

import com.oyj.vueblog.shiro.AccountRealm;
import com.oyj.vueblog.shiro.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro启用注解拦截控制器,作用如下：
 * 1.引入RedisSessionDAO和RedisCacheManager，为了解决shiro的权限数据和会话信息能保存到redis中，实现会话共享
 * 2.重写了SessionManager和DefaultWebSecurityManager，同时在DefaultWebSecurityManager中为了关闭shiro自带的session方式，
 * 我们需要设置为false，这样用户就不再能通过session方式登录shiro。后面将采用jwt凭证登录
 * 3.在ShiroFilterChainDefinition中，我们不再通过编码形式拦截Controller访问路径，
 * 而是所有的路由都需要经过JwtFilter这个过滤器，然后判断请求头中是否含有jwt的信息，有就登录，没有就跳过。
 * 跳过之后，有Controller中的shiro注解进行再次拦截，比如@RequiresAuthentication，这样控制权限访问
 *
 * @author a123
 * @since 2022-06-30
 */
@Configuration
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(AccountRealm accountRealm,
                                                     SessionManager sessionManager,
                                                     RedisCacheManager redisCacheManager) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        // inject redisCacheManager
        securityManager.setCacheManager(redisCacheManager);

        // 不加这一行配置,引入Druid会报错:No SecurityManager accessible to the calling code
//        ThreadContext.bind(securityManager);
        return securityManager;
    }


    /**
     * 自定义项目过滤器链，定制不需要经过filter过滤的请求
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        Map<String, String> filterMap = new LinkedHashMap<>();
        // 访问/login和/unauthorized 不需要经过filter过滤器
        filterMap.put("/login", "anon");
        filterMap.put("/entry", "anon");
        filterMap.put("/unauthorized/**", "anon");
        //swagger配置放行
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/v2/**", "anon");
        // druid放行
        filterMap.put("/druid/**", "anon");
        // 静态资源放行
        filterMap.put("/**/*.html", "anon");
        filterMap.put("/**/*.jpg", "anon");
        filterMap.put("/**/*.png", "anon");

        // 所有请求通过我们自己的JWT Filter
        filterMap.put("/**", "jwt");
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition, FilterRegistrationBean jwtFilterRegBean) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilterRegBean.getFilter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    /**
     * 配置JwtFilter过滤器,并设置为未注册状态
     */
    @Bean
    public FilterRegistrationBean jwtFilterRegBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //添加JwtFilter  并设置为未注册状态
        filterRegistrationBean.setFilter(jwtFilter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

}
