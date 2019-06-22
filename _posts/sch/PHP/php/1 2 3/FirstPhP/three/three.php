<?php

	class Person{
		
		public $name;
		public $age;

		public   function speak(){
			echo "我是一个好人!";
			
		}
		
		function __destruct(){
			
			echo $this->name."销毁资源 关闭数据库..<br/>";
		}
		
		public function __construct(){
			echo "我是构造方法";
	
		}
		
		public    function count1(){
			 
			$res=0;
			for($i=1;$i<=1000;$i++){
				$res+=$i;
			}
			//return 究竟到什么地方去？谁调用，就返回给谁
			return $res;
		}
 
		//修改jisuan 成员方法,该方法可以接收一个数n，计算 1+..+n 的结果
		public function count2($n){
			$res=0;
			for($i=0;$i<=$n;$i++){
				$res+=$i;
			}
			return $res;
		}

		//添加add 成员方法,可以计算两个数的和
		public function add($num1,$num2){
		
			return $num1+$num2;
		}
		
	}

	//如何使用函数

	//1. 创建一个人
	$p1=new Person(); 
	//通过对象去访问，调用成员方法. 
	//这里强调，调用成员方法和调用普通函数的机制，仍然不变
	$p1->speak();
	$p1->age; 
	//让这个人做算术题
	$res=$p1->count1();

	echo '计算结果是='.$res;
	//让这个人可以接收一个数，然后计算
 
	echo "计算结果是=".$p1->count2(100)."<br>";
	echo "计算结果是=".$p1->count2(100)."\n";
	echo "计算结果是=".$p1->count2(100)."\n";

	echo "23+89=".$p1->add(23,89);
	 $p2 = new Person();
	
	
?>
