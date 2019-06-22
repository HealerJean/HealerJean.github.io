<?php


class Child{

	public $name;
	//这里定义并初始化一个静态变量 $nums
	public static $nums=0;
	function __construct($name){
			
		$this->name=$name;
	}
  
	public function join_game(){
			
		//self::$nums 使用静态变量
		self::$nums+=1;
		// Child::$nums;
		echo $this->name."加入堆雪人游戏";
			
	}


}
		
		//创建三个小孩
		
		$child1=new Child("李逵");
		$child1->join_game();
		$child2=new Child("张飞");
		$child2->join_game();
		$child3=new Child("唐僧");
		$child3->join_game();
		
		//看看有多少人玩游戏
		echo "<br/> 有这".Child::$nums;



?>