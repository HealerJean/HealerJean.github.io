
<?php

	//¿½±´Í¼Æ¬
	$file_path=iconv("utf-8","gb2312","C:\\proxy.jpg");
 
	if(!copy($file_path,"d:\\bb.jpg")){
		echo "error";
	}else{
		echo "ok";
	}
?>
