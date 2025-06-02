package com.school.filter;

import com.school.util.Result;
import com.school.util.TokenManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 用于保护API的认证过滤器
 * 此类用于拦截进入API的请求，检查请求是否包含有效的认证令牌，以确保只有经过认证的用户才能访问受保护的资源。
 */
@WebFilter(urlPatterns = {"/api/*"})
public class AuthFilter implements Filter {
    // 不需要认证的路径列表
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login", // 登录接口
            "/register", // 注册接口
            "/logout" // 登出接口
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 如果需要，可以在这里进行初始化操作
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取请求路径
        String path = httpRequest.getServletPath();

        // 检查路径是否为公共路径（不需要认证）
        if (isPublicPath(path)) {
            // 不需要认证，继续执行请求
            chain.doFilter(request, response);
            return;
        }

        // 从请求头中获取令牌
        String token = httpRequest.getHeader("X-Auth-Token");

        // 验证令牌
        String username = TokenManager.validateToken(token);
        if (token == null || username == null) {
            // 令牌无效，返回错误信息
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            PrintWriter out = httpResponse.getWriter();
            out.write("{\"code\":401,\"message\":\"无效令牌\"}");
            return;
        }

        // 令牌有效，用户名已在验证过程中获取

        // 将用户名设置为请求属性，以便在servlet中使用
        httpRequest.setAttribute("username", username);

        // 继续执行请求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 如果需要，可以在这里进行清理操作
    }

    /**
     * 检查请求路径是否为公共路径（不需要认证）
     * @param path 请求路径
     * @return 如果是公共路径返回true，否则返回false
     */
    private boolean isPublicPath(String path) {
        if (path == null) {
            return false;
        }

        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }

        return false;
    }
}