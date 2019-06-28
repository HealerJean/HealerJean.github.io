<?php





//header("Location: 新的页面”);
// 跳转到另一个界面
// 		header("Location: http://www.sohu.com");
// 		exit();

/* 	
	echo "hello";
			
    echo "<img src=”Sunset.jpg”  width=”100px”>";

 */

	//header(“Refresh: 3 ; url=http://www.sohu.com”);

//三秒后跳转
//		header("Refresh: 3 ; url=http://localhost/FirstPhP/three/three.php");

    		

		
		
//通过header来禁用缓存(ajax )

/*
		header("Expires: -1");
		//为了保证兼容性，上面的那个是时间上的设置缓存，下面的这个就不是完全不缓存了
		header("Cache-Control: no_cache");
		header("Pragma: no-cache");
		
		echo "hello!cache";
		
*/		
		
	
		//对函数的说明
		//参数说明 $file_name 文件名
		//		   $file_sub_dir: 下载文件的子路径	 '"/xxx/xxx/"
		function down_file($file_name,$file_sub_dir){
		
			
		
			//死去活来，演示下载一个图片.
			//如果文件是中文.
				
			//原因 php文件函数，比较古老，需要对中文转码 gb2312 ，将中文转为 gb2312
			$file_name=iconv("utf-8","gb2312",$file_name);
		
			//相对路径
			$path=".Http".$file_name;
			 
			//绝对路径 server 里面有一个信息是表面这个的 DOCUMENT_ROOT 是 www 的的地址
			$file_path=$_SERVER['DOCUMENT_ROOT'].$file_sub_dir.$file_name;
				
		//	$file_path=$_SERVER['DOCUMENT_ROOT']."/Http/Http/Sunset.jpg";
			
			echo $_SERVER; 
			echo $file_path;
			//如果你希望绝对路径
		 
		
			//1.打开文件
		
			if(!file_exists($file_path)){
				echo "文件不存在!";
				return ;
			}
		//获得文件 
			$fp=fopen($file_path,"r");
		
		
			//获取下载文件的大小
			$file_size=filesize($file_path);
		echo $file_size;
		/* 	if($file_size>30){
					
				echo "<script language='javascript'>window.alert('过大')</script>";
				return ;
			} */
		
			//返回的文件   
			header("Content-type: application/octet-stream");
			//按照字节大小返回
			header("Accept-Ranges: bytes");
			//返回文件大小 
			header("Accept-Length: $file_size");
			//这里客户端的弹出对话框，对应的文件名
			header("Content-Disposition: attachment; filename=".$file_name);
		
		
			//向客户端回送数据
		
			$buffer=1024;
			//为了下载的安全，我们最好做一个文件字节读取计数器
			$file_count=0;  
			//这句话用于判断文件是否结束    feof($fp 最后一次读操作的时候的内容
			while(!feof($fp) && ($file_size-$file_count>0) ){
				   
				$file_data=fread($fp,$buffer);
				//统计读了多少个字节 
				$file_count+=$buffer;
				//把部分数据回送给浏览器;
				echo $file_data;
			}
		
			//关闭文件
			fclose($fp);
		
		}
		
		 
		//测试函数是否可用
		down_file("Sunset.jpg","/Http/Http/"); 
			


?>