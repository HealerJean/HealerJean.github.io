<?php

//����ȫ�ֱ���	
	global $global_nums;
	//��ֵ 
	$global_nums=32;
    
class Child{
 
	public $name;

	function __construct($name){
			
		$this->name=$name;
	}

	public function join_game(){
		
		//����һ��ʹ��ȫ�ֱ���
		 global $global_nums;
		
		$global_nums+=1;
		
		echo $this->name."�����ѩ����Ϸ";
			
	}


}


		//��������С��
		
		$child1=new Child("����");
		$child1->join_game();
		$child2=new Child("�ŷ�");
		$child2->join_game();
		$child3=new Child("��ɮ");
		$child3->join_game();
		
		//�����ж���������Ϸ
		echo "<br/> ��".$global_nums;




?>