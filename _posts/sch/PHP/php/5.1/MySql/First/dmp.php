<?php

	$conn=mysql_connect("localhost","root","147094");
	if(!$conn){
		die("������".mysql_error());
	}
	mysql_select_db("phpMysql",$conn) or die(mysql_error());
	mysql_query("set names utf8");
	//$sql="insert into user1 (name,password,email,age) values('С��',md5('123'),'xiaoming@sohu.com',34)";
	
	//$sql="delete from user1 where id=5";
	$sql="update user1 set age=102 where id=2";
	
	//�����dml�������򷵻�bool 
	$res=mysql_query($sql,$conn);
	  
	if(!$res){
		die("����ʧ��".mysql_error());
	} 
	//�����м�������,ȡ��ǰһ��,mysql Ӱ��ļ�¼����Ϊ ��
	if(mysql_affected_rows($conn)>0){
		echo "�����ɹ�";
		echo mysql_affected_rows($conn);
	}else{
		echo "û��Ӱ�쵽����,����û�гɹ�";
	}
	//�ر�����û�еõ�������ֱ�ӹر����ӾͿ�����
	mysql_close($conn);

	?>