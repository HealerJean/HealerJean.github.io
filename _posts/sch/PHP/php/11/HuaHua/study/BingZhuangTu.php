<?php
	$im =imagecreatetruecolor(1000, 1000);
	
	//修改背景颜色
	$white = imagecolorallocate($im, 255, 255, 255);
	//imagefill($image, $x, $y, $color)
	imagefill($im, 0, 0, $white);
	
	
	
	$red = imagecolorallocate($im,254,0,0);
	$darkred =  imagecolorallocate($im,144,0,0);
	
	$blue = imagecolorallocate($im,0,0,128);	
	$darkblue = imagecolorallocate($im,0,0,80);
	
	$gary= imagecolorallocate($im,192,192,192);
	$darkgary= imagecolorallocate($im,144,144,144);
	
	
	
	//画扇形
	//imagefilledarc($image, $cx, $cy, $width, $height, $start, $end, $color, $style)
/**
 	这个是原来的图形
  	imagefilledarc($im, 50, 100, 100, 60, 0, 35, $blue, IMG_ARC_PIE);
	imagefilledarc($im, 50, 100, 100, 60, 35, 75, $gary, IMG_ARC_PIE);
	imagefilledarc($im, 50, 100, 100, 60, 75, 360, $red, IMG_ARC_PIE);
	 */
	
	for ($i = 60 ; $i>=50;$i--){
		
		imagefilledarc($im, 50, $i, 100, 60, 0, 35, $darkblue, IMG_ARC_PIE);
		imagefilledarc($im, 50, $i, 100, 60, 35, 75, $darkgary, IMG_ARC_PIE);
		imagefilledarc($im, 50, $i, 100, 60, 75, 360, $darkred, IMG_ARC_PIE);
	}
	imagefilledarc($im, 50, 50, 100, 60, 0, 35, $blue, IMG_ARC_PIE);
	imagefilledarc($im, 50, 50, 100, 60, 35, 75, $gary, IMG_ARC_PIE);
	imagefilledarc($im, 50, 50, 100, 60, 75, 360, $red, IMG_ARC_PIE);
	
	
	//画出图像到网页
	header("Content-type: image/png");
	imagepng($im);
	
	//销毁图像资源
	imagedestroy($im);