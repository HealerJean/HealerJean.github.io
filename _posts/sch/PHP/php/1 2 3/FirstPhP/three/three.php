<?php

	class Person{
		
		public $name;
		public $age;

		public   function speak(){
			echo "����һ������!";
			
		}
		
		function __destruct(){
			
			echo $this->name."������Դ �ر����ݿ�..<br/>";
		}
		
		public function __construct(){
			echo "���ǹ��췽��";
	
		}
		
		public    function count1(){
			 
			$res=0;
			for($i=1;$i<=1000;$i++){
				$res+=$i;
			}
			//return ������ʲô�ط�ȥ��˭���ã��ͷ��ظ�˭
			return $res;
		}
 
		//�޸�jisuan ��Ա����,�÷������Խ���һ����n������ 1+..+n �Ľ��
		public function count2($n){
			$res=0;
			for($i=0;$i<=$n;$i++){
				$res+=$i;
			}
			return $res;
		}

		//���add ��Ա����,���Լ����������ĺ�
		public function add($num1,$num2){
		
			return $num1+$num2;
		}
		
	}

	//���ʹ�ú���

	//1. ����һ����
	$p1=new Person(); 
	//ͨ������ȥ���ʣ����ó�Ա����. 
	//����ǿ�������ó�Ա�����͵�����ͨ�����Ļ��ƣ���Ȼ����
	$p1->speak();
	$p1->age; 
	//���������������
	$res=$p1->count1();

	echo '��������='.$res;
	//������˿��Խ���һ������Ȼ�����
 
	echo "��������=".$p1->count2(100)."<br>";
	echo "��������=".$p1->count2(100)."\n";
	echo "��������=".$p1->count2(100)."\n";

	echo "23+89=".$p1->add(23,89);
	 $p2 = new Person();
	
	
?>
