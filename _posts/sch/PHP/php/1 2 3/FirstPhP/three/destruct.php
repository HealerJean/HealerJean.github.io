<?php

class Person{

	public $name;
	public $age;
	public $conn;

	//构造方法
	 function __construct($name,$age){
		$this->name=$name;
		$this->age=$age;
		//打开一个$conn链接数据库的资源
	}

	//写一个析构方法 __ 是两个下划线
	function __destruct(){
		echo $this->name."销毁资源 关闭数据库..<br/>";
	}

}
//先创建的后销毁
		$p1=new Person("贾宝玉",16);
		$p2=new Person("林黛玉",14);
		$p3 = $p1;

?>