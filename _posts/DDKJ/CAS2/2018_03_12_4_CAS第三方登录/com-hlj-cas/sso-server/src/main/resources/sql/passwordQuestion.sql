use casnew;
/*
密码重置问题表
*/
CREATE TABLE SYS_USER_QUESTION (
  USERNAME VARCHAR(30) NOT NULL,
  QUESTION VARCHAR(200) NOT NULL,
  ANSWER     VARCHAR(100) NOT NULL
);
---问题数据
INSERT INTO SYS_USER_QUESTION VALUES ('admin', '你的年龄是？',  '24');
INSERT INTO SYS_USER_QUESTION VALUES ('HealerJean', '我的名字是？',  'HealerJean');
INSERT INTO SYS_USER_QUESTION VALUES ('zhangsan', '我在哪里工作？',  'guangzhou');
---多个密码问题，后面测试的过程中进行导入
INSERT INTO SYS_USER_QUESTION VALUES ('HealerJean', '你喜欢的人是？',  'Liuli');



/**
测试2、将登陆的用户名变成邮箱，已经修改密码按照自己填写的邮箱开始验证。
 */
ALTER TABLE SYS_USER_QUESTION ADD email VARCHAR(100) ;
UPDATE SYS_USER_QUESTION SET  email = 'mxzdhealer@gmail.com' WHERE  USERNAME = 'HealerJean';
UPDATE SYS_USER_QUESTION SET email = 'huang.wenbin@foxmail.com' WHERE USERNAME = 'admin';
UPDATE SYS_USER_QUESTION SET email = 'zhangsan@foxmail.com' WHERE USERNAME = 'zhangsan' ;