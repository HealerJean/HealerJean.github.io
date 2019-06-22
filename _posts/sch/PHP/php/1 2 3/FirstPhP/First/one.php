<?php

  	echo "zhang";
    echo  "<br>";

    echo "hello" ;
    echo  "<br>";

  $a=890; 
  var_dump($a); //可以显示变量的信息
  $a=1.1; //这样我们的额$a就编程了小小数类型
  echo  "<br>";
  var_dump($a); //可以显示变量的信息

//1. 测试 单引号和双引号
	$abc = '我是赵春宇$a';
	$abc1 = "我是张宇晋$a";
	echo "".$abc;
	echo ''.$abc1;
	
	echo  "<br><br>*******************连接数据库*************************<br><br><br>";
	
//2.  连接数据库
	$con = mysql_connect('localhost','root','147094');
	if ($con) {
		echo "数据库连接了";
	}else {
		echo  "没有数据库";
	}
	echo  "<br><br>********************测试除法************************<br><br><br>";
	
//3.	测试除法

	$chu = 7/2;
	echo  $chu;
	echo  "<br><br>********************instanceof判断是不是一个类************************<br><br><br>";
	
//4.instanceof判断是不是一个类

	class  cat{};
	class  ma{};
	
	$ma = new cat;
	
	if($ma instanceof cat){
		
		echo  "instanceof是测试成功 ";
		echo  "<br><br><br><br>****************require 导入文件****************************<br><br><br>";
		
	}else {
		echo "不是一个猫";
		echo  "<br><br><br><br>*****************require 导入文件***************************<br><br><br>";
		
	}
	
	
//5. require 导入文件

	require "test.php";
	require "test.php";
	require_once 'test.php';
	 
	include 'test.php'; 
	include_once 'test.php';
	
//6. 数组
	echo  "<br><br><br><br>*****************require 导入文件***************************<br><br><br>";

//直接输入
	$str = array(1,'123',456,'123123132132');
	
	echo count($str);
	 
	for ($i=0;$i< count($str);$i++){
		 
		echo "<br>".$str[$i];
	} 
//数组小标自己定义
	echo  "<br><br><br><br>*************foreach *****************<br><br><br>";
	
	
/* 	
 * $arr['login'] = '登录';
	$arr['背景']='beijing';
 */	
	$arr = array("背景"=>"fadf","fas"=>123);
	
	foreach ($arr as $var){
		
		echo "<br>".$var;
	}
	
	
	
//将下表也打出来
	foreach ($arr as $key=>$var){ 
	
		echo "<br>".$key."=".$var;
	}
	
	$arr2 = array("fadf",3=>123); 
	echo  "***********".$arr2[3]."***********8"; 
	foreach ($arr2  as $key=>$var){
	
		echo "<br>".$key."=".$var;
	}
	
	echo  "<br><br><br><br>***********************<br><br><br>";
//显示数组	
	print_r($arr);
//显示数据的详细信息
	var_dump($arr);
	
	
// 6 判断是不是数组	
	
	echo  "<br><br><br><br>************  判断是不是数组	 *****************<br><br><br>";
	
	
	echo  is_array($arr);  //如果是数组 就是 1
	
	
	
// 7 explode(" ", $str3)拆分字符串 得到数组
	echo  "<br><br><br><br>************  explode拆分字符串 得到数组	 *****************<br><br><br>";
	
	
	
	
	$str3 = "zhang yu jin gao tong zhao chun yu ";
	$str4 = explode(" ", $str3);
	echo  print_r($str4);
	
//8. 删除数组中的某一个元素
	echo  "<br><br><br><br>************ 删除数组中的某一个元素 *****************<br><br><br>";
	
	$atrr = array(1,2,3,5,4);
	unset($atrr[2]);	//删除了会报错
	// echo  $atrr[2]."什么也没有就是删除了";
	
	
	
	
	
?>