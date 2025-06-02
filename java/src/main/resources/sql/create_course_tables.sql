-- 创建课程表
CREATE TABLE IF NOT EXISTS `courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` varchar(20) NOT NULL COMMENT '课程编号',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `teacher_name` varchar(50) NOT NULL COMMENT '授课教师',
  `credits` int NOT NULL COMMENT '学分',
  `schedule` varchar(100) NOT NULL COMMENT '课程安排',
  `location` varchar(50) NOT NULL COMMENT '上课地点',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 创建学生选课表
CREATE TABLE IF NOT EXISTS `student_courses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL COMMENT '学生ID',
  `course_id` int NOT NULL COMMENT '课程ID',
  `status` varchar(20) NOT NULL DEFAULT '正常' COMMENT '状态: 正常/退课',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_course` (`student_id`,`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课表';

-- 插入示例课程数据
INSERT INTO `courses` (`course_id`, `course_name`, `teacher_name`, `credits`, `schedule`, `location`) VALUES
('CS101', '计算机导论', '张教授', 3, '周一 08:00-09:50', '教学楼A-101'),
('CS102', '程序设计基础', '李教授', 4, '周二 10:00-11:50', '教学楼B-201'),
('CS201', '数据结构', '王教授', 4, '周三 14:00-15:50', '教学楼A-301'),
('CS202', '算法分析', '赵教授', 3, '周四 08:00-09:50', '教学楼B-101'),
('CS301', '数据库原理', '钱教授', 4, '周五 10:00-11:50', '教学楼C-201'),
('CS302', '操作系统', '孙教授', 4, '周二 14:00-15:50', '教学楼A-201'),
('CS401', '软件工程', '周教授', 3, '周三 10:00-11:50', '教学楼B-301'),
('CS402', '计算机网络', '吴教授', 4, '周四 14:00-15:50', '教学楼C-101'),
('MATH101', '高等数学', '郑教授', 5, '周一 10:00-11:50', '教学楼D-101'),
('MATH201', '线性代数', '王教授', 3, '周五 08:00-09:50', '教学楼D-201');

-- 为李四同学添加选课数据（假设李四的ID为1）
INSERT INTO `student_courses` (`student_id`, `course_id`, `status`) VALUES
((SELECT id FROM students WHERE username = 'li456'), (SELECT id FROM courses WHERE course_id = 'CS101'), '正常'),
((SELECT id FROM students WHERE username = 'li456'), (SELECT id FROM courses WHERE course_id = 'CS201'), '正常'),
((SELECT id FROM students WHERE username = 'li456'), (SELECT id FROM courses WHERE course_id = 'CS301'), '正常'),
((SELECT id FROM students WHERE username = 'li456'), (SELECT id FROM courses WHERE course_id = 'MATH101'), '正常'); 