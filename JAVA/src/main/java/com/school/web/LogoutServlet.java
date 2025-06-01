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
 * Servlet for user logout
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        // Get token from header
        String token = req.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // Get username from session
        HttpSession session = req.getSession(false);
        String username = null;
        if (session != null) {
            username = (String) session.getAttribute("USERNAME");
        }
        
        // Invalidate token if exists
        if (token != null) {
            TokenManager.removeToken(token);
        }
        
        // Invalidate session if exists
        if (username != null) {
            TokenManager.removeUserToken(username);
        }
        
        // Invalidate session
        if (session != null) {
            session.invalidate();
        }
        
        // Return success response
        out.write("{\"code\":200,\"message\":\"已成功登出\"}");
    }
} 