<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生管理系统 - 主页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .welcome-message {
            font-weight: bold;
        }
        .navbar {
            display: flex;
            gap: 15px;
        }
        .navbar a {
            color: white;
            text-decoration: none;
        }
        .navbar a:hover {
            text-decoration: underline;
        }
        .content {
            background-color: #ffffff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-top: 20px;
        }
        .logout-btn {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
        }
        .logout-btn:hover {
            background-color: #d32f2f;
        }
        .dashboard {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 20px;
        }
        .card {
            background: white;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            padding: 20px;
            flex: 1;
            min-width: 250px;
        }
        .card h3 {
            margin-top: 0;
            color: #4CAF50;
        }
        .feature-cards {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 30px;
        }
        .feature-card {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            padding: 20px;
            flex: 1;
            min-width: 200px;
            text-align: center;
            transition: transform 0.3s ease;
            cursor: pointer;
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
        .feature-icon {
            font-size: 36px;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>学生管理系统</h1>
        <div class="navbar">
            <a href="main.jsp" class="active">首页</a>
            <a href="profile.jsp">个人信息</a>
            <a href="courses.jsp">我的课表</a>
            <span class="welcome-message" id="welcomeMessage"></span>
            <button class="logout-btn" id="logoutBtn">退出登录</button>
        </div>
    </div>
    
    <div class="container">
        <div class="content">
            <h2>欢迎使用学生管理系统</h2>
            <p>您已成功登录系统。</p>
            
            <div class="dashboard">
                <div class="card">
                    <h3>个人信息</h3>
                    <div id="userInfo">加载中...</div>
                </div>
                
                <div class="card">
                    <h3>系统公告</h3>
                    <p>欢迎使用学生管理系统，请点击相应的功能模块开始使用。</p>
                </div>
            </div>
            
            <div class="feature-cards">
                <div class="feature-card" onclick="window.location.href='profile.jsp'">
                    <div class="feature-icon">👤</div>
                    <h3>个人信息</h3>
                    <p>查看和修改个人资料</p>
                </div>
                
                <div class="feature-card" onclick="window.location.href='courses.jsp'">
                    <div class="feature-icon">📚</div>
                    <h3>我的课表</h3>
                    <p>查看已选课程和课表安排</p>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 在页面加载时检查登录状态
        document.addEventListener('DOMContentLoaded', function() {
            // 从localStorage获取用户信息
            const token = localStorage.getItem('token');
            const username = localStorage.getItem('username');
            const role = localStorage.getItem('role');
            
            // 如果未登录，重定向到登录页
            if (!token || !username) {
                window.location.href = 'login.jsp';
                return;
            }
            
            // 显示欢迎信息
            document.getElementById('welcomeMessage').textContent = '欢迎, ' + username + '!';
            
            // 显示用户信息
            const userInfoHTML = 
                '<p><strong>用户名:</strong> ' + username + '</p>' +
                '<p><strong>角色:</strong> ' + (role || '普通用户') + '</p>';
            document.getElementById('userInfo').innerHTML = userInfoHTML;
            
            // 退出登录功能
            document.getElementById('logoutBtn').addEventListener('click', function() {
                // 发送登出请求
                fetch('logout', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    }
                })
                .finally(() => {
                    // 无论成功与否，都清除本地存储并重定向
                    localStorage.removeItem('token');
                    localStorage.removeItem('username');
                    localStorage.removeItem('role');
                    window.location.href = 'login.jsp';
                });
            });
        });
    </script>
</body>
</html> 