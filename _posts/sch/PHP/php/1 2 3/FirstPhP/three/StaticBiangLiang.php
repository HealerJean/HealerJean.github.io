<?php


class Child{

	public $name;
	//���ﶨ�岢��ʼ��һ����̬���� $nums
	public static $nums=0;
	function __construct($name){
			
		$this->name=$name;
	}
  
	public function join_game(){
			
		//self::$nums ʹ�þ�̬����
		self::$nums+=1;
		// Child::$nums;
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
		echo "<br/> ����".Child::$nums;



?>