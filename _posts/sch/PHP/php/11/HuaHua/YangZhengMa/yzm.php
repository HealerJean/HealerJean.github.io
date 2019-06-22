<?php

	//使用验证码
	$checkCode="";
	for($i=0;$i<4;$i++){
		
		// 将从 1 到15的随机数，将10进制转成16进制数
		$checkCode.=dechex(rand(1,15));
	}
		
	session_start();
	$_SESSION['myCheckCode']=$checkCode;
	
	//创建画布
	$img=imagecreatetruecolor(110,30);	
	$bgcolor=imagecolorallocate($img,0,0,0);
	imagefill($img,0,0,$bgcolor);
	
	$white=imagecolorallocate($img,255,255,255);
	for($i=0;$i<20;$i++){
		// 更好的方法是颜色随机 
		imageline($img,rand(0,110),rand(0,30),rand(0,110),rand(0,30),
		imagecolorallocate($img,rand(0,255),rand(0,255),rand(0,255)
		));
	}
	
	imagestring($img, rand(1,5), rand(2,80),rand(2,10), $checkCode, $white);

	//画出图像到网页
	header("Content-type: image/png");
	imagepng($img);
	
	//销毁图像资源
	imagedestroy($img);