package com.school.web;

import com.school.util.Result;
import com.school.util.TokenManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户登出的Servlet
 * 此类用于处理用户的登出请求，包括使令牌和会话失效。
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 对于GET请求，转发到doPost方法处理
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的内容类型和字符编码
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        // 从请求头中获取令牌
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            // 如果令牌以"Bearer "开头，则截取令牌部分
            token = token.substring(7);
        }

        // 从会话中获取用户名
        HttpSession session = req.getSession(false);
        String username = null;
        if (session != null) {
            username = (String) session.getAttribute("USERNAME");
        }

        // 如果存在令牌，则使其失效
        if (token != null) {
            TokenManager.removeToken(token);
        }

        // 如果存在用户名，则移除该用户名对应的令牌
        if (username != null) {
            TokenManager.removeUserToken(username);
        }

        // 如果存在会话，则使其失效
        if (session != null) {
            session.invalidate();
        }

        // 返回成功的响应
        out.write("{\"code\":200,\"message\":\"已成功登出\"}");
    }
}