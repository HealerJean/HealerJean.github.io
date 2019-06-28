
<?php


	//文件及文件夹的创建和删除.

	//1.创建文件夹 d:/shunping

	 //is_dir 判断是不是一个目录
	/* if(!is_dir("d:/shunping2")){
		if(mkdir("d:/shunping2"))
		{
			echo "创建文件夹ok";
		}else{
			echo "创建文件夹err";
		}
	}else{
		echo "该文件夹有了";	
	} */


	//2.能不能一次性多个文件(层级),创建ok
/* 	$path="d:/shunping3/aaa/bbb/cccc/ddd";
	if(!is_dir($path)){
		// 0777 是一个模式 随便一个数字就可以
		if( mkdir($path,0777,true)){
			echo "创建文件夹ok";
		}else{
			echo "创建文件夹err";
		} 
	}else{
		echo "该文件夹有了";	
	} */

	//3.删除文件夹
	//如果文件夹下有文件，或者目录，均不能删除ok
/* 	
	if(rmdir("d:/shunping2")){
		echo "删除文件夹ok";
	}else{
		echo "err";
	} */

	//4.文件的创建
/* 	//在d:/shunping3 目录下，创建一个文件并写入hello
	$file_path="d:/shunping3/aa.txt";
	$fp=fopen($file_path,"w+");

	fwrite($fp,"hello,world");

	fclose($fp);

	echo "创文件ok"; */

	//5.删除文件

	$file_path="d:/shunping3/aa.txt";
	
	if(is_file($file_path)){
		
		if(unlink($file_path)){
			echo "删除ok";
		}else{
			echo "删除error";
		}
	}else{
		echo "文件不存在";
	}

?>
