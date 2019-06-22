<?php
/* 
 * //自定义 处理器
	//定义了一个函数（我用于处理错误的函数）
	function my_error($errno,$errmes){
		echo "<font size='5' color='red'>$errno</font><br/>";
		echo "错误信息是:";
		exit();
	}

	//改写set_error_handler处理器
	//下面这句话的含义是 ： 如果出现了 E_WARNING这个级别的错误,就去调用my_error函数.
	set_error_handler("my_error",E_WARNING);

	$fp=fopen("aa.txt","r");
	 */

//自定义触发器,时候同时指定错误级别. 

		function  my_error3($errno,$errmes){
			echo "错误号是:".$errno;
			}

	/* 	function my_error4($errno,$errmes){
			echo "出大事了".$errno;
			echo "错误的信息为".$errmes;
			exit();
		}
		  */
		//指定E_USER_WARNING 错误级别的函数 
		set_error_handler("my_error3",E_USER_WARNING);
		set_error_handler("my_error4",E_USER_ERROR);
		
		
		$age=700; 
		if($age>120){
			//自定义触发器,时候同时指定错误级别. 
			trigger_error("输入年龄过大1",E_USER_ERROR);
			//exit(); 
		}
		 
		echo "ok";
	

//写一个真实的文件
	function  my_error4($errno,$errmes){
		$err_info="错误号是:".$errno."--".$errmes;
		echo $err_info; 
		//把这个错误信息保存
		//\r\n 表示向文件输入一个回车换行
		//<br/> 表示向网页输出一个回车换行 
		
		error_log($err_info."\r\n",3,"d:/myerr.txt");
	}
 
?>
