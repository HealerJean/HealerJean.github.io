<?php

	class  A{

		public function test1($p){
			echo "����һ������";
			echo "<br/>���յ�������";
			var_dump($p);

		}
		public function test2($p){
			echo "������������<br/>";
			var_dump($p);
		}
		

		//��Щ�ṩһ��__call 
		//__call ����һ���������ĳ�����������÷��������ڣ���
		//ϵͳ���Զ�����__call 
		function __call($method,$p){
			var_dump($p);
			if($method=="test"){
				if(count($p)==1){
					$this->test1($p);
				}else if(count($p)==2){
					$this->test2($p);
				}
			}
		}

	}

	$a=new A();
	$a->test(1);
//�����������������
	$a->fa(56,90);
	
?>
