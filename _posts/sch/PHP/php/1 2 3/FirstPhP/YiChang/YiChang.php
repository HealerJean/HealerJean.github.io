<?php



try{
		addUser("shunping");
		updateUser("xxx");
} 
//catch ����  Exception ���쳣��(��php�����һ����)
catch(Exception $e){
	//echo "ʧ����Ϣ =".$e->getMessage();
	throw $e;
}
  
function addUser($username){

	if($username=="shunping"){
		//���ok
	}else{
		//���error
		//�׳��쳣.
		throw new Exception("���ʧ��");
	}
}

function updateUser($username){
	if($username=="xiaoming"){
		//�����޸�
	}else{
		throw new Exception("�޸�ʧ��");
	}
}




?>