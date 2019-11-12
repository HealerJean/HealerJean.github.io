use casnew;
/*
账号表
*/

CREATE TABLE SYS_USER (
  USERNAME VARCHAR(30) PRIMARY KEY,
  PASSWORD VARCHAR(64) NOT NULL,
  EMAIL    VARCHAR(50),
  ADDRESS  VARCHAR(100),
  AGE      INT,
  EXPIRED INT,
  DISABLE INT
);

/*HealerJean123456*/
INSERT INTO SYS_USER VALUES ('HealerJean', '40e5fd27a2f9db33d397d11617c2098b', 'mxzdhealer@gmail.com', '山西忻州', 24, 0, 0);

/*123*/
INSERT INTO SYS_USER VALUES ('admin', '202cb962ac59075b964b07152d234b70', 'huang.wenbin@foxmail.com', '广州天河', 24, 0, 0);
/*12345678*/
INSERT INTO SYS_USER VALUES ('zhangsan', '25d55ad283aa400af464c76d713c07ad', 'zhangsan@foxmail.com', '广州越秀', 26, 0, 0);
/*1234*/
/*锁定用户*/
INSERT INTO SYS_USER VALUES('zhaosi','81dc9bdb52d04dc20036dbd8313ed055', 'zhaosi@foxmail.com', '广州海珠', 25, 0 , 1);
/*12345*/
/*不可用*/
INSERT INTO SYS_USER VALUES('wangwu','827ccb0eea8a706c4c34a16891f84e7b', 'wangwu@foxmail.com', '广州番禺', 27, 1 , 0);

