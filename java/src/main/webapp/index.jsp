<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
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
    </style>
</head>
<body>
    <div class="header">
        <h1>学生管理系统</h1>
        <div class="navbar">
            <span class="welcome-message" id="welcomeMessage"></span>
            <button class="logout-btn" id="logoutBtn">退出登录</button>
        </div>
    </div>
    
    <div class="container">
        <div class="content">
            <h2>欢迎使用学生管理系统</h2>
            <p>这里是系统首页，需要登录后才能访问。</p>
            <div id="protectedContent">
                <h3>受保护的内容</h3>
                <div id="apiData">加载中...</div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const token = localStorage.getItem('token');
            const username = localStorage.getItem('username');
            
            // 检查是否已登录
            if (!token || !username) {
                window.location.href = 'login.jsp';
                return;
            }
            
            // 显示欢迎信息
            document.getElementById('welcomeMessage').textContent = `欢迎, ${username}!`;
            
            // 加载受保护的API数据
            fetchProtectedData();
            
            // 登出功能
            document.getElementById('logoutBtn').addEventListener('click', logout);
        });
        
        function fetchProtectedData() {
            const token = localStorage.getItem('token');
            
            fetch('api/data', {
                method: 'GET',
                headers: {
                    'X-Auth-Token': token
                }
            })
            .then(response => {
                if (!response.ok) {
                    if (response.status === 401) {
                        // 令牌无效，重定向到登录页
                        localStorage.removeItem('token');
                        localStorage.removeItem('username');
                        localStorage.removeItem('role');
                        window.location.href = 'login.jsp';
                        return;
                    }
                    throw new Error('网络错误');
                }
                return response.json();
            })
            .then(data => {
                if (data.code === 200) {
                    // 显示API返回的数据
                    const apiDataElement = document.getElementById('apiData');
                    apiDataElement.innerHTML = `
                        <p>\${data.data.message}</p>
                        <p>时间戳: \${new Date(data.data.timestamp).toLocaleString()}</p>
                    `;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('apiData').textContent = '加载数据失败';
            });
        }
        
        function logout() {
            const token = localStorage.getItem('token');
            
            fetch('logout', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(() => {
                // 清除本地存储的令牌和用户信息
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('role');
                
                // 重定向到登录页
                window.location.href = 'login.jsp';
            })
            .catch(error => {
                console.error('Error:', error);
                // 即使注销失败，也清除本地存储并重定向
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                localStorage.removeItem('role');
                window.location.href = 'login.jsp';
            });
        }
    </script>
</body>
</html> 