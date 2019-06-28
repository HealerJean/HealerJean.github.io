<?php

//定义全局变量	
	global $global_nums;
	//赋值 
	$global_nums=32;
    
class Child{
 
	public $name;

	function __construct($name){
			
		$this->name=$name;
	}

	public function join_game(){
		
		//声明一下使用全局变量
		 global $global_nums;
		
		$global_nums+=1;
		
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
		echo "<br/> 有".$global_nums;




?>