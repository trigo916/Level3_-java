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
 * 用户登录的Servlet
 * 此类用于处理用户的登录请求，包括验证用户身份和生成认证令牌。
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDao(); // 用户数据访问对象
    private StudentDao studentDao = new StudentDao(); // 学生数据访问对象

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到登录页面
        req.getRequestDispatcher("/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 设置响应的内容类型和字符编码
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        // 验证输入
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            out.write("{\"code\":400,\"message\":\"用户名和密码不能为空\"}");
            return;
        }

        // 验证用户身份
        User user = userDao.authenticate(username, password);
        Result result;

        if (user == null) {
            result = Result.error(401, "用户名或密码错误");
        } else {
            // 为用户生成令牌
            String token = TokenManager.generateToken(username);

            // 创建会话
            HttpSession session = req.getSession(true);
            session.setAttribute("USER_ID", user.getId());
            session.setAttribute("USERNAME", user.getUsername());
            session.setAttribute("ROLE", user.getRole());

            // 准备返回给客户端的数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("role", user.getRole());

            // 如果用户是学生，获取学生详细信息
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

        // 将结果转换为JSON字符串并写入响应
        out.write(toJsonString(result.toMap()));
    }

    /**
     * 将Map转换为JSON字符串
     * @param map 要转换的Map对象
     * @return 转换后的JSON字符串
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
            } else if (value instanceof Map) {
                json.append(toJsonString((Map<String, Object>)value));
            } else {
                json.append("\"").append(value).append("\"");
            }
        }

        json.append("}");
        return json.toString();
    }
}