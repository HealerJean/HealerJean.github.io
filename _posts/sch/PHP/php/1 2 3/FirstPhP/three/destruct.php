<?php

class Person{

	public $name;
	public $age;
	public $conn;

	//���췽��
	 function __construct($name,$age){
		$this->name=$name;
		$this->age=$age;
		//��һ��$conn�������ݿ����Դ
	}

	//дһ���������� __ �������»���
	function __destruct(){
		echo $this->name."������Դ �ر����ݿ�..<br/>";
	}

}
//�ȴ����ĺ�����
		$p1=new Person("�ֱ���",16);
		$p2=new Person("������",14);
		$p3 = $p1;

?>