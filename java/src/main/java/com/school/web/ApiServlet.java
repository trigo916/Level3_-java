package com.school.web;

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
 * 需要认证的API端点
 * 此类定义了一个HTTP Servlet，用于处理对受保护数据的GET请求。
 * 它继承自HttpServlet，重写了doGet方法以处理GET请求。
 */
@WebServlet("/api/data")
public class ApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从请求属性中获取用户名，该属性由认证过滤器（AuthFilter）设置
        String username = (String) req.getAttribute("username");

        // 设置响应的内容类型为JSON，并指定字符编码为UTF-8
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 获取PrintWriter对象，用于向客户端写入响应内容
        PrintWriter out = resp.getWriter();

        // 创建一个Map来存储要返回给客户端的数据
        Map<String, Object> data = new HashMap<>();
        // 向Map中添加一条消息，包含用户名，表示用户已认证成功
        data.put("message", "Hello, " + username + "! This is protected data.");
        // 添加当前时间戳，作为数据的一部分
        data.put("timestamp", System.currentTimeMillis());

        // 将Map转换为JSON格式的字符串，并写入响应中
        out.print("{\"code\":200,\"message\":\"成功\",\"data\":");
        out.print(toJsonString(data)); // 调用toJsonString方法将Map转换为JSON字符串
        out.print("}");
        // 完成响应内容的写入
        out.flush();
    }

    /**
     * 将Map转换为JSON字符串
     * 此方法遍历Map中的每个键值对，并将它们格式化为JSON字符串。
     * @param map 要转换的Map对象
     * @return 转换后的JSON字符串
     */
    private String toJsonString(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true; // 用于跟踪是否是第一个键值对，以避免在JSON字符串中添加多余的逗号

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(","); // 如果不是第一个键值对，则在前面添加逗号分隔
            }
            first = false;

            // 添加键，并将键和值用双引号括起来
            json.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();

            // 根据值的类型，以适当的方式添加到JSON字符串中
            if (value == null) {
                json.append("null"); // 如果值为null，则添加"null"
            } else if (value instanceof Number) {
                json.append(value); // 如果值为数字类型，则直接添加
            } else if (value instanceof Boolean) {
                json.append(value); // 如果值为布尔类型，则直接添加
            } else {
                // 如果值为其他类型，则添加双引号
                json.append("\"").append(value).append("\"");
            }
        }

        json.append("}"); // 结束JSON对象
        return json.toString(); // 返回构建的JSON字符串
    }
}