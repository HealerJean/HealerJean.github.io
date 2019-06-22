<?php

function show_tab_info($table_name){

	$conn=mysql_connect("localhost","root","147094");
	if(!$conn){
		die("连接失败".mysql_error());
	}
	echo "hello";
	mysql_select_db("phpMysql",$conn);
	mysql_query("set names utf8");
	//$sql="select * from $table_name";
	$sql="desc $table_name";
	$res=mysql_query($sql,$conn);

	//我要知道总有多少行，和多少列  
	$rows=mysql_affected_rows($conn);
	$colums=mysql_num_fields($res);
	echo "$rows=$colums";
 
	echo "<table border=1><tr>";
	//表头
	for($i=0;$i<$colums;$i++){
	$field_name=mysql_field_name($res,$i);
	echo "<th>$field_name</th>";
	}
	echo "</tr>";

	while($row=mysql_fetch_row($res)){
	echo "<tr>";
			for($i=0;$i<$colums;$i++){
			echo "<td>$row[$i]</td>";
			}
			echo "</tr>";
		}
		
		echo "</table>";

	/*	while($field_info=mysql_fetch_field($res)){
		echo  "<br/>".$field_info->name;
		}*/
		//var_dump($field_info);
}
		show_tab_info("user1");
?>