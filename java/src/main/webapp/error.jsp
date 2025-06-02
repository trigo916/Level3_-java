<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>错误页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .error-container {
            background-color: #ffffff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 500px;
            text-align: center;
        }
        .error-icon {
            color: #f44336;
            font-size: 48px;
            margin-bottom: 20px;
        }
        .error-title {
            color: #f44336;
            margin-bottom: 10px;
        }
        .error-message {
            margin-bottom: 20px;
            color: #666;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">⚠️</div>
        <h1 class="error-title">发生错误</h1>
        <p class="error-message">
            <%
                String errorCode = request.getParameter("code");
                String errorMsg = request.getParameter("message");
                
                if (errorCode != null) {
                    out.println("错误代码: " + errorCode);
                }
                
                if (errorMsg != null) {
                    out.println("<br>错误信息: " + errorMsg);
                } else {
                    out.println("<br>未知错误，请稍后再试");
                }
            %>
        </p>
        <a href="index.jsp" class="btn">返回首页</a>
    </div>
</body>
</html> 