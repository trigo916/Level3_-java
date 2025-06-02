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
 * 管理学生个人信息的Servlet
 * 此类用于处理获取和更新学生个人信息的HTTP请求。
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private StudentDao studentDao = new StudentDao(); // 学生数据访问对象
    private UserDao userDao = new UserDao(); // 用户数据访问对象

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取会话属性
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, 401, "用户未登录");
            return;
        }

        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            sendError(resp, 401, "用户未登录");
            return;
        }

        // 获取学生信息
        Student student = studentDao.findByUsername(username);
        if (student == null) {
            sendError(resp, 404, "找不到学生信息");
            return;
        }

        // 返回学生信息
        Map<String, Object> data = new HashMap<>();
        data.put("username", student.getUsername());
        data.put("studentId", student.getStudentId());
        data.put("name", student.getName());
        data.put("age", student.getAge());
        data.put("major", student.getMajor());

        Result result = Result.success("获取个人信息成功", data);
        sendResponse(resp, result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取会话属性
        HttpSession session = req.getSession(false);
        if (session == null) {
            sendError(resp, 401, "用户未登录");
            return;
        }

        String username = (String) session.getAttribute("USERNAME");
        if (username == null) {
            sendError(resp, 401, "用户未登录");
            return;
        }

        // 获取请求参数
        String name = req.getParameter("name");
        String ageStr = req.getParameter("age");
        String major = req.getParameter("major");
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");

        // 验证输入
        if (name == null || name.trim().isEmpty() ||
                ageStr == null || ageStr.trim().isEmpty()) {
            sendError(resp, 400, "姓名和年龄不能为空");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 16 || age > 100) {
                sendError(resp, 400, "年龄必须在16-100之间");
                return;
            }
        } catch (NumberFormatException e) {
            sendError(resp, 400, "年龄必须是数字");
            return;
        }

        // 更新学生信息
        Student student = studentDao.findByUsername(username);
        if (student == null) {
            sendError(resp, 404, "找不到学生信息");
            return;
        }

        student.setName(name);
        student.setAge(age);
        student.setMajor(major);

        // 如果请求更改密码
        if (currentPassword != null && !currentPassword.trim().isEmpty()) {
            // 验证当前密码
            User user = userDao.authenticate(username, currentPassword);
            if (user == null) {
                sendError(resp, 401, "当前密码不正确");
                return;
            }

            // 更新密码
            student.setPassword(newPassword);

            boolean success = studentDao.updateWithPassword(student);
            if (!success) {
                sendError(resp, 500, "更新信息失败");
                return;
            }
        } else {
            boolean success = studentDao.update(student);
            if (!success) {
                sendError(resp, 500, "更新信息失败");
                return;
            }
        }

        // 返回成功
        Result result = Result.success("个人信息更新成功", null);
        sendResponse(resp, result);
    }

    /**
     * 发送错误信息
     * @param resp 响应对象
     * @param code 错误代码
     * @param message 错误信息
     * @throws IOException 如果写入响应时发生I/O错误
     */
    private void sendError(HttpServletResponse resp, int code, String message) throws IOException {
        Result result = Result.error(code, message);
        sendResponse(resp, result);
    }

    /**
     * 发送响应
     * @param resp 响应对象
     * @param result 结果对象，包含要发送的数据和状态
     * @throws IOException 如果写入响应时发生I/O错误
     */
    private void sendResponse(HttpServletResponse resp, Result result) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
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
                // 正确处理嵌套的Map对象
                json.append(toJsonString((Map<String, Object>)value));
            } else {
                json.append("\"").append(value).append("\"");
            }
        }

        json.append("}");
        return json.toString();
    }
}