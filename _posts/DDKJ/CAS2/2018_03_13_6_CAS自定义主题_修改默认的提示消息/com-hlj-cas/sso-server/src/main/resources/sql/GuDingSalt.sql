use casnew;
/*
账号加盐表
*/
CREATE TABLE SYS_USER_ENCODE (
  USERNAME VARCHAR(30) PRIMARY KEY,
  PASSWORD VARCHAR(64) NOT NULL,
  EMAIL    VARCHAR(50),
  ADDRESS  VARCHAR(100),
  AGE      INT,
  EXPIRED INT,
  DISABLED INT
);


---加盐数据
/*123  可以采用PasswordSaltTest输出值*/
INSERT INTO SYS_USER_ENCODE VALUES ('admin_en', 'bfb194d5bd84a5fc77c1d303aefd36c3', 'huang.wenbin@foxmail.com', '江门蓬江', 24, 0, 0);
INSERT INTO SYS_USER_ENCODE VALUES ('zhangsan_en', '68ae075edf004353a0403ee681e45056',  'zhangsan@foxmail.com', '深圳宝安', 21, 0, 0);
INSERT INTO SYS_USER_ENCODE VALUES ('zhaosi_en', 'd66108d0409f68af538301b637f13a18',  'zhaosi@foxmail.com', '清远清新', 20, 0, 1);
INSERT INTO SYS_USER_ENCODE VALUES ('wangwu_en', '44b907d6fee23a552348eabf5fcf1ac7',  'wangwu@foxmail.com', '佛山顺德', 19, 1, 0);


