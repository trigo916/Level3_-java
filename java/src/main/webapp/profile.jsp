<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人信息修改</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
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
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], input[type="password"], input[type="number"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .form-row {
            display: flex;
            gap: 15px;
        }
        .form-row .form-group {
            flex: 1;
        }
        .message {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
            display: none;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>学生管理系统</h1>
        <div class="navbar">
            <a href="main.jsp">首页</a>
            <a href="profile.jsp" class="active">个人信息</a>
            <a href="courses.jsp">我的课表</a>
            <span id="welcomeMessage"></span>
            <button id="logoutBtn" style="background-color: #f44336; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer;">退出登录</button>
        </div>
    </div>
    
    <div class="container">
        <div class="content">
            <h2>个人信息修改</h2>
            <div class="message success" id="successMessage">信息更新成功！</div>
            <div class="message error" id="errorMessage"></div>
            
            <form id="profileForm">
                <div class="form-row">
                    <div class="form-group">
                        <label for="username">用户名:</label>
                        <input type="text" id="username" name="username" readonly>
                    </div>
                    <div class="form-group">
                        <label for="studentId">学号:</label>
                        <input type="text" id="studentId" name="studentId" readonly>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="name">姓名:</label>
                        <input type="text" id="name" name="name">
                    </div>
                    <div class="form-group">
                        <label for="age">年龄:</label>
                        <input type="number" id="age" name="age" min="16" max="100">
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="major">专业:</label>
                    <input type="text" id="major" name="major">
                </div>
                
                <hr style="margin: 20px 0;">
                
                <h3>修改密码</h3>
                <div class="form-group">
                    <label for="currentPassword">当前密码:</label>
                    <input type="password" id="currentPassword" name="currentPassword">
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="newPassword">新密码:</label>
                        <input type="password" id="newPassword" name="newPassword">
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">确认新密码:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword">
                    </div>
                </div>
                
                <div style="margin-top: 20px;">
                    <button type="submit" class="btn">保存更改</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // 从localStorage获取用户信息
            const token = localStorage.getItem('token');
            const username = localStorage.getItem('username');
            
            // 如果未登录，重定向到登录页
            if (!token || !username) {
                window.location.href = 'login.jsp';
                return;
            }
            
            // 显示欢迎信息
            document.getElementById('welcomeMessage').textContent = '欢迎, ' + username + '!';
            
            // 获取用户个人信息
            fetch('profile', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    // 填充表单数据
                    document.getElementById('username').value = data.data.username;
                    document.getElementById('studentId').value = data.data.studentId;
                    document.getElementById('name').value = data.data.name;
                    document.getElementById('age').value = data.data.age;
                    document.getElementById('major').value = data.data.major;
                } else {
                    showError(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('获取个人信息失败，请稍后再试');
            });
            
            // 提交表单更新个人信息
            document.getElementById('profileForm').addEventListener('submit', function(e) {
                e.preventDefault();
                
                // 获取表单数据
                const name = document.getElementById('name').value;
                const age = document.getElementById('age').value;
                const major = document.getElementById('major').value;
                const currentPassword = document.getElementById('currentPassword').value;
                const newPassword = document.getElementById('newPassword').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                
                // 验证输入
                if (!name || !age) {
                    showError('姓名和年龄不能为空');
                    return;
                }
                
                // 如果填写了密码字段，验证密码
                if (currentPassword || newPassword || confirmPassword) {
                    if (!currentPassword) {
                        showError('请输入当前密码');
                        return;
                    }
                    if (!newPassword) {
                        showError('请输入新密码');
                        return;
                    }
                    if (!confirmPassword) {
                        showError('请输入确认密码');
                        return;
                    }
                    if (newPassword !== confirmPassword) {
                        showError('两次输入的新密码不一致');
                        return;
                    }
                }
                
                // 准备提交数据
                const formData = new FormData();
                formData.append('name', name);
                formData.append('age', age);
                formData.append('major', major);
                
                if (currentPassword) {
                    formData.append('currentPassword', currentPassword);
                    formData.append('newPassword', newPassword);
                }
                
                // 发送更新请求
                fetch('profile', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    body: new URLSearchParams(formData)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.code === 200) {
                        showSuccess();
                        
                        // 如果修改了密码，清除密码输入框
                        if (currentPassword) {
                            document.getElementById('currentPassword').value = '';
                            document.getElementById('newPassword').value = '';
                            document.getElementById('confirmPassword').value = '';
                        }
                    } else {
                        showError(data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showError('更新个人信息失败，请稍后再试');
                });
            });
            
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
        
        function showError(message) {
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.textContent = message;
            errorMessage.style.display = 'block';
            document.getElementById('successMessage').style.display = 'none';
        }
        
        function showSuccess() {
            document.getElementById('successMessage').style.display = 'block';
            document.getElementById('errorMessage').style.display = 'none';
        }
    </script>
</body>
</html> 