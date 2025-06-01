package com.school.web;

import com.school.dao.StudentDao;
import com.school.dao.UserDao;
import com.school.entity.Student;
import com.school.util.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for student operations
 */
@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || "/".equals(pathInfo)) {
            // Show student form
            req.getRequestDispatcher("/register.html").forward(req, resp);
            return;
        }
        
        if ("/register".equals(pathInfo)) {
            // Show registration form
            req.getRequestDispatcher("/register.html").forward(req, resp);
            return;
        }
        
        // 404 for any other paths
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        
        if ("/register".equals(pathInfo)) {
            // Handle student registration
            handleRegistration(req, resp, out);
        } else {
            // 404 for any other paths
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * Handle student registration
     */
    private void handleRegistration(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        // Get parameters
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String studentId = req.getParameter("studentId");
        String name = req.getParameter("name");
        String ageStr = req.getParameter("age");
        String major = req.getParameter("major");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            confirmPassword == null || confirmPassword.trim().isEmpty() || 
            studentId == null || studentId.trim().isEmpty() || 
            name == null || name.trim().isEmpty() || 
            ageStr == null || ageStr.trim().isEmpty() || 
            major == null || major.trim().isEmpty()) {
            
            out.write(toJsonString(Result.error("所有字段都是必填的").toMap()));
            return;
        }
        
        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            out.write(toJsonString(Result.error("两次输入的密码不一致").toMap()));
            return;
        }
        
        // Validate username (check if exists)
        if (userDao.usernameExists(username)) {
            out.write(toJsonString(Result.error("用户名已存在").toMap()));
            return;
        }
        
        // Parse age
        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) {
                out.write(toJsonString(Result.error("年龄必须是正数").toMap()));
                return;
            }
        } catch (NumberFormatException e) {
            out.write(toJsonString(Result.error("年龄必须是数字").toMap()));
            return;
        }
        
        // Create student object
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(password);
        student.setRole("student");
        student.setStudentId(studentId);
        student.setName(name);
        student.setAge(age);
        student.setMajor(major);
        
        // Register student
        int result = studentDao.register(student);
        
        if (result > 0) {
            // Success
            Map<String, Object> data = new HashMap<>();
            data.put("id", result);
            data.put("username", username);
            out.write(toJsonString(Result.success("注册成功", data).toMap()));
        } else {
            // Failure
            out.write(toJsonString(Result.error("注册失败，请稍后再试").toMap()));
        }
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