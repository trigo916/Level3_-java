<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            padding: 20px;
        }
        .register-container {
            background-color: #ffffff;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
            width: 100%;
            max-width: 500px;
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
            width: 100%;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .error-message {
            color: #ff0000;
            margin-bottom: 15px;
            display: none;
        }
        .success-message {
            color: #4CAF50;
            margin-bottom: 15px;
            display: none;
        }
        .button-row {
            display: flex;
            gap: 10px;
        }
        .back-btn {
            background-color: #f44336;
            flex: 1;
        }
        .back-btn:hover {
            background-color: #d32f2f;
        }
        .register-btn {
            flex: 2;
        }
    </style>
</head>
<body>
    <div class="register-container">
        <h2 style="text-align: center; margin-bottom: 20px;">学生注册</h2>
        <div class="error-message" id="errorMessage"></div>
        <div class="success-message" id="successMessage"></div>
        
        <form id="registerForm">
            <div class="form-group">
                <label for="username">用户名:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">密码:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">确认密码:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <div class="form-group">
                <label for="studentId">学号:</label>
                <input type="text" id="studentId" name="studentId" required>
            </div>
            <div class="form-group">
                <label for="name">姓名:</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="age">年龄:</label>
                <input type="number" id="age" name="age" required min="1">
            </div>
            <div class="form-group">
                <label for="major">专业:</label>
                <input type="text" id="major" name="major" required>
            </div>
            <div class="form-group button-row">
                <button type="button" class="btn back-btn" onclick="window.location.href='login.jsp'">返回登录</button>
                <button type="submit" class="btn register-btn">注册</button>
            </div>
        </form>
    </div>

    <script>
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const studentId = document.getElementById('studentId').value;
            const name = document.getElementById('name').value;
            const age = document.getElementById('age').value;
            const major = document.getElementById('major').value;
            
            const errorMessage = document.getElementById('errorMessage');
            const successMessage = document.getElementById('successMessage');
            
            // 隐藏之前的消息
            errorMessage.style.display = 'none';
            successMessage.style.display = 'none';
            
            // 验证密码是否一致
            if (password !== confirmPassword) {
                errorMessage.textContent = '两次输入的密码不一致';
                errorMessage.style.display = 'block';
                return;
            }
            
            // 发送注册请求
            fetch('student/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `username=\${encodeURIComponent(username)}&password=\${encodeURIComponent(password)}` +
                      `&confirmPassword=\${encodeURIComponent(confirmPassword)}&studentId=\${encodeURIComponent(studentId)}` +
                      `&name=\${encodeURIComponent(name)}&age=\${encodeURIComponent(age)}&major=\${encodeURIComponent(major)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    // 注册成功
                    successMessage.textContent = '注册成功！3秒后跳转到登录页...';
                    successMessage.style.display = 'block';
                    
                    // 清空表单
                    document.getElementById('registerForm').reset();
                    
                    // 3秒后跳转到登录页
                    setTimeout(() => {
                        window.location.href = 'login.jsp';
                    }, 3000);
                } else {
                    // 注册失败，显示错误消息
                    errorMessage.textContent = data.message;
                    errorMessage.style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                errorMessage.textContent = '服务器错误，请稍后再试';
                errorMessage.style.display = 'block';
            });
        });
    </script>
</body>
</html> 