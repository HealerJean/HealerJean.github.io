

<?php



	//读文件信息

	//1.打文件
	//打开文件
	$file_path="test.txt";
	//该函数返回一个指向文件的指针
	//先判断文件是否存在

	//************************第一种读取方法********************
/* if(file_exists($file_path)){
		//打开文件 以读写的方式 ，文件指针指向末尾，如果文件不存在，则尝试创建
		$fp=fopen($file_path,"a+");
		//读内容,并输入
		//**** 第一种读取方法************** 这个是表示读文件的多少个字节
		$con=fread($fp,filesize($file_path));
		echo "文件的内容是:<br/>"; 
		//在默认情况下，我们得到内容输出到网页后，不会换行,因为网页
		//不认\r\n 是换行符, \r\n -><br/>
		$con=str_replace("\r\n","<br/>",$con);
		echo $con;
		
	}else{
		echo "文件不存在!";
	}

	fclose($fp); */


	//*************第二种读取方法,一个函数****************

/* 	$con=file_get_contents($file_path);

	//替换
	$con=str_replace("\r\n","<br/>",$con);
	echo $con;
 */
	//*************第三种读取方法,循环读取(对付打文件)*********
 
	$fp=fopen($file_path,"a+");

	//我们设置一次读取1024个字节
	$buffer=1024;
	$str="";
	//一边读，一边判断是否到达文件末尾
	while(!feof($fp)){
		//读
		$str=fread($fp,$buffer);
		
		
	}
	$str=str_replace("\r\n","<br/>",$str);
		echo $str;
	fclose($fp); 


?>
