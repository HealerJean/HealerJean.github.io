<?php



try{
		addUser("shunping");
		updateUser("xxx");
} 
//catch 捕获  Exception 是异常类(是php定义好一个类)
catch(Exception $e){
	//echo "失败信息 =".$e->getMessage();
	throw $e;
}
  
function addUser($username){

	if($username=="shunping"){
		//添加ok
	}else{
		//添加error
		//抛出异常.
		throw new Exception("添加失败");
	}
}

function updateUser($username){
	if($username=="xiaoming"){
		//正常修改
	}else{
		throw new Exception("修改失败");
	}
}




?>