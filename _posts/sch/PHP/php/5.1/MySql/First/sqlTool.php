<?php



class SqlTool {

	//����
	private $conn;
	private $host="localhost";
	private $user="root";
	private $password="147094";
	private $db="phpmysql";

	function SqlTool(){
			  
		$this->conn=mysql_connect($this->host,$this->user,$this->password);
		if(!$this->conn){
			die("�������ݿ�ʧ��".mysql_error());
		} 
		mysql_select_db($this->db,$this->conn);
		mysql_query("set names utf8");
	}

	//����..

	// ���select dql
	public  function execute_dql($sql){
 
		$res=mysql_query($sql,$this->conn) or die(mysql_error());
			
		return $res;
			
	} 
	//��� update,delete ,insert dml
	public  function execute_dml($sql){
 
		$b=mysql_query($sql,$this->conn);
		//echo "��ӵ�id=".mysql_insert_id($this->conn);
		if(!$b){
			return 0;//ʧ��
		}else{  
			if(mysql_affected_rows($this->conn)>0){
				return 1;//��ʾ�ɹ�
			}else{ 
				return 2;//��ʾû������Ӱ��.
			}
		}
	}
}

?>