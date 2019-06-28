<?php

	$conn=mysql_connect("localhost","root","147094");
	if(!$conn){
		die("出错了".mysql_error());
	}
	mysql_select_db("phpMysql",$conn) or die(mysql_error());
	mysql_query("set names utf8");
	//$sql="insert into user1 (name,password,email,age) values('小明',md5('123'),'xiaoming@sohu.com',34)";
	
	//$sql="delete from user1 where id=5";
	$sql="update user1 set age=102 where id=2";
	
	//如果是dml操作，则返回bool 
	$res=mysql_query($sql,$conn);
	  
	if(!$res){
		die("操作失败".mysql_error());
	} 
	//看看有几条数据,取得前一次,mysql 影响的记录行数为 几
	if(mysql_affected_rows($conn)>0){
		echo "操作成功";
		echo mysql_affected_rows($conn);
	}else{
		echo "没有影响到行数,操作没有成功";
	}
	//关闭连接没有得到表，所以直接关闭连接就可以了
	mysql_close($conn);

	?>