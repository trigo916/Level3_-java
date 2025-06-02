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
 * 处理学生相关操作的Servlet
 * 此类用于处理与学生注册和管理相关的HTTP请求。
 */
@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao(); // 学生数据访问对象
    private UserDao userDao = new UserDao(); // 用户数据访问对象

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // 获取请求路径信息

        if (pathInfo == null || "/".equals(pathInfo)) {
            // 如果路径为空，显示学生注册表单
            req.getRequestDispatcher("/register.html").forward(req, resp);
            return;
        }

        if ("/register".equals(pathInfo)) {
            // 如果路径为/register，显示注册表单
            req.getRequestDispatcher("/register.html").forward(req, resp);
            return;
        }

        // 对于其他路径，返回404错误
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // 获取请求路径信息

        resp.setContentType("application/json"); // 设置响应内容类型为JSON
        resp.setCharacterEncoding("UTF-8"); // 设置字符编码为UTF-8
        PrintWriter out = resp.getWriter(); // 获取PrintWriter对象

        if ("/register".equals(pathInfo)) {
            // 如果路径为/register，处理学生注册
            handleRegistration(req, resp, out);
        } else {
            // 对于其他路径，返回404错误
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * 处理学生注册
     * @param req 请求对象
     * @param resp 响应对象
     * @param out PrintWriter对象，用于写入响应内容
     */
    private void handleRegistration(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        // 获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String studentId = req.getParameter("studentId");
        String name = req.getParameter("name");
        String ageStr = req.getParameter("age");
        String major = req.getParameter("major");

        // 验证输入
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

        // 验证密码确认
        if (!password.equals(confirmPassword)) {
            out.write(toJsonString(Result.error("两次输入的密码不一致").toMap()));
            return;
        }

        // 验证用户名（检查是否存在）
        if (userDao.usernameExists(username)) {
            out.write(toJsonString(Result.error("用户名已存在").toMap()));
            return;
        }

        // 解析年龄
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

        // 创建学生对象
        Student student = new Student();
        student.setUsername(username);
        student.setPassword(password);
        student.setRole("student");
        student.setStudentId(studentId);
        student.setName(name);
        student.setAge(age);
        student.setMajor(major);

        // 注册学生
        int result = studentDao.register(student);

        if (result > 0) {
            // 成功
            Map<String, Object> data = new HashMap<>();
            data.put("id", result);
            data.put("username", username);
            out.write(toJsonString(Result.success("注册成功", data).toMap()));
        } else {
            // 失败
            out.write(toJsonString(Result.error("注册失败，请稍后再试").toMap()));
        }
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
            } else {
                json.append("\"").append(value).append("\"");
            }
        }

        json.append("}");
        return json.toString();
    }
}