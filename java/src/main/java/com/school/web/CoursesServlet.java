package com.school.web;

import com.school.dao.CourseDao;
import com.school.dao.StudentDao;
import com.school.entity.Course;
import com.school.entity.Student;
import com.school.util.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 获取学生课程信息的Servlet
 * 此类用于处理获取学生课程信息的HTTP GET请求。
 */
@WebServlet("/courses")
public class CoursesServlet extends HttpServlet {
    private CourseDao courseDao = new CourseDao(); // 课程数据访问对象
    private StudentDao studentDao = new StudentDao(); // 学生数据访问对象

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

        // 根据用户名获取课程信息
        List<Course> courses = courseDao.findCoursesByUsername(username);

        // 返回课程信息
        Result result = Result.success("获取课表成功", courses);
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
                json.append(toJsonString((Map<String, Object>)value));
            } else if (value instanceof List) {
                json.append(listToJsonString((List<?>) value));
            } else {
                json.append("\"").append(value).append("\"");
            }
        }

        json.append("}");
        return json.toString();
    }

    /**
     * 将List转换为JSON字符串
     * @param list 要转换的List对象
     * @return 转换后的JSON字符串
     */
    private String listToJsonString(List<?> list) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Object item : list) {
            if (!first) {
                json.append(",");
            }
            first = false;

            if (item == null) {
                json.append("null");
            } else if (item instanceof Map) {
                json.append(toJsonString((Map<String, Object>) item));
            } else if (item instanceof List) {
                json.append(listToJsonString((List<?>) item));
            } else if (item instanceof Number) {
                json.append(item);
            } else if (item instanceof Boolean) {
                json.append(item);
            } else if (item instanceof Course) {
                json.append(courseToJsonString((Course) item));
            } else {
                json.append("\"").append(item).append("\"");
            }
        }

        json.append("]");
        return json.toString();
    }

    /**
     * 将Course对象转换为JSON字符串
     * @param course 要转换的Course对象
     * @return 转换后的JSON字符串
     */
    private String courseToJsonString(Course course) {
        StringBuilder json = new StringBuilder("{");

        json.append("\"id\":").append(course.getId()).append(",");
        json.append("\"courseId\":\"").append(course.getCourseId()).append("\",");
        json.append("\"courseName\":\"").append(course.getCourseName()).append("\",");
        json.append("\"teacherName\":\"").append(course.getTeacherName()).append("\",");
        json.append("\"credits\":").append(course.getCredits()).append(",");
        json.append("\"schedule\":\"").append(course.getSchedule()).append("\",");
        json.append("\"location\":\"").append(course.getLocation()).append("\",");
        json.append("\"status\":\"").append(course.getStatus()).append("\"");

        json.append("}");
        return json.toString();
    }
}