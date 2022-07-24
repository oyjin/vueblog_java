package com.oyj.vueblog.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 新增异常过滤器Filter，将异常转移到Controller
 * <p> @Title ExceptionFilter
 * <p> @Description 异常过滤器
 *
 * @author a123
 * @date 2022/7/24
 */
@Slf4j
@Component
public class ExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            // 异常捕获，发送到error controller
            request.setAttribute("filter.error", e);
            //将异常分发到/error/exthrow控制器
            request.getRequestDispatcher("/error/exthrow").forward(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }
}

