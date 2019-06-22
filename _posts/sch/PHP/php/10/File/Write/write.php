
<?php


	//如何写文件

	//1.传统的方法

	$file_path="test.txt";

	/* if(file_exists($file_path)){
		//如果是追加内容，则使用a+ append 
		//如果是全新的写入到文件 ，则使用 w+ write
		$fp=fopen($file_path,"a+");
		$con="\r\n你好a!";
		for($i=0;$i<10;$i++){
			fwrite($fp,$con);
		}

	}else{
	
	}
	echo "添加ok";
	fclose($fp); */

	//2.第二种方式写入文件
 	$file_path="test.txt";
	$con="北京你好!\r\n";
	for($i=0;$i<10;$i++){
		$con.="北京你好!\r\n";
	}
	
	//下面函数比较但是,请不要循环的使用该函数 。
	file_put_contents($file_path,$con,FILE_APPEND); //fopen fwrite fclose
	
	echo "ok";
?>
