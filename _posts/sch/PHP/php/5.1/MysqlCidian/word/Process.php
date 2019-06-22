<?php

header("Content-type: text/html; charset=utf-8");


	require_once 'sqlTool.php';


	if(isset($_POST["type"])){
		
		$type=$_POST["type"];
	}else {
		echo "请输入内容";
	}
	       
	    
	if($type=="search"){
	
	//isset 锟斤拷锟斤拷锟街�锟斤拷为true 锟斤拷锟斤拷 锟斤拷false
	if(isset($_REQUEST["enword"])){
		$enword = $_REQUEST["enword"];
		echo $enword;
		echo "成功";
		
	}else {
		
		echo "没有输入";
	}
	$sql = "select id from user1 where  name= '".$enword."'";
	  
			$sqltool = new SqlTool();
		 
	  	 	$res = $sqltool->execute_dql($sql);
	  	 
	  	 		if($row=mysql_fetch_row($res)){
		  	 		  	 	  
		  	 		foreach($row as  $var){
		  	 			
		  	 			echo "".$var;
		  	 		}
		  	 		echo "<br/>";
		  	 	}else {
		  	 		 
		  	 		echo "没有这个单词";
		  	 	}
	  	 mysql_free_result($res);
	  	 
	  	 }elseif ($type=="search2"){
	  	 
	  	 	if(isset($_REQUEST["chword"])){
	  	 		$chword = $_REQUEST["chword"];
	  	 		echo $chword;
	  	 		echo "成功";
	  	 
	  	 	}else {
	  	 
	  	 		echo "没有输入内容";
	  	 	}
	  	 
	
		 
		
	  	 	$sql = "select id from user1 where  name= '".$enword."'";
	  	 	
		$sqltool = new SqlTool();
			
		$res = $sqltool->execute_dql($sql);
		//锟叫断诧拷询锟斤拷锟� 
		if(mysql_num_rows($res)>0){
			while($row=mysql_fetch_row($res)){
					
				foreach($row as  $var){
			
					echo "".$var;
				}
				echo "<br/>";
			}
			mysql_free_result($res);
		
		}else {
					
				echo "没有这个单词";
			}
	}	
?>