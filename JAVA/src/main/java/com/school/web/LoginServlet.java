package com.school.web;

import com.school.dao.StudentDao;
import com.school.dao.UserDao;
import com.school.entity.Student;
import com.school.entity.User;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for user login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    private StudentDao studentDao = new StudentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect to login page
        req.getRequestDispatcher("/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get parameters
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            out.write("{\"code\":400,\"message\":\"用户名和密码不能为空\"}");
            return;
        }
        
        // Authenticate user
        User user = userDao.authenticate(username, password);
        Result result;
        
        if (user == null) {
            result = Result.error(401, "用户名或密码错误");
        } else {
            // Generate token
            String token = TokenManager.generateToken(username);
            
            // Create session
            HttpSession session = req.getSession(true);
            session.setAttribute("USER_ID", user.getId());
            session.setAttribute("USERNAME", user.getUsername());
            session.setAttribute("ROLE", user.getRole());
            
            // Return user info
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
            
            // If user is a student, get student details
            if ("student".equals(user.getRole())) {
                Student student = studentDao.findByUsername(username);
                if (student != null) {
                    data.put("studentId", student.getStudentId());
                    data.put("name", student.getName());
                    data.put("major", student.getMajor());
                }
            }
            
            result = Result.success("登录成功", data);
        }
        
        out.write(toJsonString(result.toMap()));
    }
    
    /**
     * Convert a map to JSON string
     * @param map Map to convert
     * @return JSON string
     */
    private String toJsonString(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;
            
            json.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            
            if (value == null) {
                json.append("null");
            } else if (value instanceof Number) {
                json.append(value);
            } else if (value instanceof Boolean) {
                json.append(value);
            } else {
                json.append("\"").append(value).append("\"");
            }
        }
        
        json.append("}");
        return json.toString();
    }
} 