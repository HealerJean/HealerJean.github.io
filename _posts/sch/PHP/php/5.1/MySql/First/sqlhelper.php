<?php
	class SqlHelper {
		
	 public $conn;
	 public $host = "localhost";
	 public $username="root";
	 public $password ="147094";
	 public $dbname = "phpmysql";
	 public function __construct() {
	 	 
	  $this->conn = mysql_connect ( $this->host, $this->username, $this->password );
	  if (! conn) {
	   die ( "connot connect mysql" . mysql_errno () );
	  } 
	  mysql_select_db ($this->dbname, $this->conn )or die("mysql_errno");
	 }
	 public function execute_dql($sql) {
	 $res = mysql_query ( $sql, $this->conn ) or die ( "conn't connect MYSQL" . mysql_errno () );
	  return $res;
	 }
	 
	 public function execute_dml($sql) {
	  $b = mysql_query ( $sql, $this->conn ) or die ( "conn't connect MYSQL" . mysql_errno () );
	  if (! $b) {
	   return 0;
	  } else {
	   if (mysql_affected_rows ( $this->conn ) > 0) {
	    return 1;
	   } else {
	    return 2;
	   }
	  
	  }
	 }
	 public function close_connect() {
	  if (! empty ( $this->conn )) {
	   mysql_close ( $this->conn );
	  } 
	 }
	}
?>