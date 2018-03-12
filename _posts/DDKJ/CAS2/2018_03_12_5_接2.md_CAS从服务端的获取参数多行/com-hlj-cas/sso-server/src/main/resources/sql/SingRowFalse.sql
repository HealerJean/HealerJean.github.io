

-- 这个具体的返回，我还是按照之前rest随机盐查询的数据库开始，
-- 也就是说主用户表依旧是sys_user_rest_salt，根据主表中的邮箱确定本从表sys_user_rest_salt_attrs

/*
* 用户属性表
*/

CREATE TABLE sys_user_rest_salt_attrs (
  EMAIL VARCHAR(100) NOT NULL,
  ATTR_KEY VARCHAR(50) NOT NULL,
  ATTR_VAL     VARCHAR(100) NOT NULL
);



# ---用户属性表
INSERT INTO sys_user_rest_salt_attrs VALUES ('mxzdhealer@gmail.com', 'group', '软件研究所');
INSERT INTO sys_user_rest_salt_attrs VALUES ('mxzdhealer@gmail.com', 'group', '计算机132');
INSERT INTO sys_user_rest_salt_attrs VALUES ('mxzdhealer@gmail.com', 'school', '大连工业大学');
INSERT INTO sys_user_rest_salt_attrs VALUES ('mxzdhealer@gmail.com', 'school', '北京大学');
INSERT INTO sys_user_rest_salt_attrs VALUES ('mxzdhealer@gmail.com', 'school', '清华大学');
INSERT INTO sys_user_rest_salt_attrs VALUES ('zhangsan@gmail.com', 'group', 'DEV_ROLE');