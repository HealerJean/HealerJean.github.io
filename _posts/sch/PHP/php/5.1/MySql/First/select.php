<?php



	$conn=mysql_connect("localhost","root","147094");
	 
	if(!$conn){
		die("����ʧ��".mysql_error());
	}else{
  
	echo "������ݿ�ɹ�"."<br/>";	
	}

	//2. ѡ����ݿ�
	mysql_select_db("phpMysql",$conn);
	//3. ���ò�������(������)!!!
	mysql_query("set names utf8");  
	//��֤���ǵ�php�����ǰ���utf8�����.
	//4. ����ָ��sql (ddl ��ݶ������ , dml(��ݲ������� update insert ,delete) ,dql (select ), dtl ���������� rollback commit... )
	 
	$sql = "select * from user1";
	//����
	//$res ��ʾ�������Լ򵥵������� һ�ű�. 
	$res=mysql_query($sql,$conn); 
	
	//var_dump($res); //mysql result ��Դ����
	
	//5. ���շ��صĽ������.(��ʾ) 
	// mysql_fecth_row ������ȡ��$res������һ�����,��ֵ��$row
	// $row����һ������, ��ʽarray(5) { [0]=> string(1) "1" [1]=> string(2) "zs" [2]=> string(32) "e10adc3949ba59abbe56e057f20f883e" [3]=> string(11) "zs@sohu.com" [4]=> string(2) "30" }
	//mysql_fetch_assoc mysql_fetch_array
	while($row=mysql_fetch_row($res)){
		//��һ��ȡ���� ͬ $row[$i]
		//echo "<br/> $row[0]--$row[1]--$row[2]";
		//echo "<br/>"; 
		//var_dump($row);
		//�ڶ���ȡ�� 
		foreach($row as $key => $val){
			echo "--$val";
		} 
		echo "<br/>";
	}
	
	
	//6. �ͷ���Դ,�ر�����(����)
	mysql_free_result($res);
	//��仰����û�У�������. 
	//	mysql_close($conn);
	









?>