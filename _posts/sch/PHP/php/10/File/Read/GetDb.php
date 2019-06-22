

<?php
	
	$arr1=parse_ini_file("db.ini");
	print_r($arr1);

	foreach ($arr1 as $var){
		
		echo  $var;
		echo "<br/>";
	}
	foreach ($arr1 as $key=>$var){
	
		echo  $key."=".$var;
		echo "<br/>";
	}
?>
