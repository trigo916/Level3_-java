<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的课表</title>
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
        .course-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .course-table th, .course-table td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        .course-table th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        .course-table tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .course-table tr:hover {
            background-color: #f1f1f1;
        }
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .no-courses {
            text-align: center;
            padding: 30px;
            color: #666;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>学生管理系统</h1>
        <div class="navbar">
            <a href="main.jsp">首页</a>
            <a href="profile.jsp">个人信息</a>
            <a href="courses.jsp" class="active">我的课表</a>
            <span id="welcomeMessage"></span>
            <button id="logoutBtn" style="background-color: #f44336; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer;">退出登录</button>
        </div>
    </div>
    
    <div class="container">
        <div class="content">
            <h2>我的课表</h2>
            <div id="errorMessage" class="message error" style="display: none;"></div>
            
            <div id="courseTableContainer">
                <!-- 课表将通过JavaScript加载 -->
            </div>
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
            
            // 获取课表数据
            fetch('courses', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    displayCourses(data.data);
                } else {
                    showError(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('获取课表失败，请稍后再试');
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
        
        function displayCourses(courses) {
            const container = document.getElementById('courseTableContainer');
            
            if (!courses || courses.length === 0) {
                container.innerHTML = '<div class="no-courses">您当前没有任何课程</div>';
                return;
            }
            
            // 创建课表
            const table = document.createElement('table');
            table.className = 'course-table';
            
            // 创建表头
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            
            const headers = ['课程编号', '课程名称', '教师', '学分', '上课时间', '上课地点', '状态'];
            headers.forEach(headerText => {
                const th = document.createElement('th');
                th.textContent = headerText;
                headerRow.appendChild(th);
            });
            
            thead.appendChild(headerRow);
            table.appendChild(thead);
            
            // 创建表体
            const tbody = document.createElement('tbody');
            
            courses.forEach(course => {
                const row = document.createElement('tr');
                
                // 添加课程信息
                addCell(row, course.courseId);
                addCell(row, course.courseName);
                addCell(row, course.teacherName);
                addCell(row, course.credits);
                addCell(row, course.schedule);
                addCell(row, course.location);
                addCell(row, course.status);
                
                tbody.appendChild(row);
            });
            
            table.appendChild(tbody);
            container.innerHTML = '';
            container.appendChild(table);
        }
        
        function addCell(row, text) {
            const cell = document.createElement('td');
            cell.textContent = text || '-';
            row.appendChild(cell);
        }
        
        function showError(message) {
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.textContent = message;
            errorMessage.style.display = 'block';
        }
    </script>
</body>
</html> 