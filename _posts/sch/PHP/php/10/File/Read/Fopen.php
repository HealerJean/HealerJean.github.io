<?php



	//1文件信息

	//打开文件
	$file_path="test.txt";
	//该函数返回一个指向文件的指针
	
	if($fp=fopen($file_path,"r")){
		//fstat 通过已经打开的文件的指针获得文件的信息
		
		$file_info=fstat($fp);
		echo "<pre>";
		print_r($file_info);
		echo "</pre>";

		//取出看看
		echo "<br/>文件大小是 {$file_info['size']}";
		
		echo "<br/>文件上次修改时间 ".date("Y-m-d H:i:s)",$file_info['mtime']);
		//文件被访问的时间  fstat 
		echo "<br/>文件上次访问时间 ".date("Y-m-d H:i:s)",$file_info['atime']);
		echo "<br/>文件上次change时间 ".date("Y-m-d H:i:s)",$file_info['ctime']);
	}else{
		echo "打开文件失败";
	}

	//关闭文件!!!很重要
//	fclose($fp);


	//***第二种方式获取文件信息

	echo "<br/>".filesize($file_path);
	echo "<br/>".date("Y-m-d H:i:s",fileatime($file_path));
	echo "<br/>".filectime($file_path);
	echo "<br/>".filemtime($file_path);


?>
