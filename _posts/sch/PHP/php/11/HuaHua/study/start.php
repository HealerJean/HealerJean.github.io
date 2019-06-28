<?php

	//创建画布，默认背景是黑色 宽 和 高
	$im =imagecreatetruecolor(1000, 1000);
	
	//开始绘制各种图形
	//创建一个颜色
	//$img = imagecolorallocate($im, $red, $green, $blue);
	$red = imagecolorallocate($im,255,0,0);
	
	//画一个椭圆   $cx $cy圆心
	//imageellipse($image, $cx, $cy, $width, $height, $color)
//	imageellipse($im, 0, 0, 89, 89, $red);
	
	
	//画直线 $x1, $y1,起点
	//imageline($image, $x1, $y1, $x2, $y2, $color)
//	imageline($im, 10, 10, 100, 100, $red);
	
	//画矩形
//	imagerectangle($im, 15, 80, 122, 300, $red);
	
	//画实心的矩形
//	imagefilledrectangle($im, 10, 90, 300, 40, $red);
	
	
	
	//画一个弧线 从水平方向开始 顺时针开始算起  下面这个是 0 到 180 度
	//imagearc($image, $cx, $cy, $width, $height, $start, $end, $color)
//	imagearc($im, 80, 20, 89, 89, 0, 180, $red);
	
	//画扇形
//	imagefilledarc($im, 80, 20, 100, 100, 0, 180, $red, IMG_ARC_PIE);
	
	//导入一个图片
//	$srcimage =imagecreatefromgif("a.gif");	
	//获得图片的长和宽 $imagesize[0],$imagesize[1]); 就是长和高
//	$imagesize = getimagesize("a.gif");	
	// $src_x, $src_y, 表示从原来的图片开始截图
	//imagecopy($dst_im, $src_im, $dst_x, $dst_y, $src_x, $src_y, $src_w, $src_h)
//	imagecopy($im, $srcimage, 0, 0, 0, 0,$imagesize[0],$imagesize[1]);

	
	//写一个字符串 	如果 font  是 1，2，3，4 或 5，则使用内置字体。  中文会乱码
	// imagestring($image, $font, $x, $y, $string, $color)
	//imagestring($im, 5, 0, 0,"zhangyujin", $red);
	
	//解决中文乱码 中文的字符 需要从电脑中获取 window /font 下
	$en = "张宇晋";  
	//imagettftext($image, $size, $angle, $x, $y, $color, $fontfile, $text)
	imagettftext($im, 30, 0, 50, 50, $red, "STKAITI.TTF", $en);
	
	//画出图像到网页	
	header("Content-type: image/png");
	imagepng($im);
	
	//销毁图像资源
	imagedestroy($im);
	?>