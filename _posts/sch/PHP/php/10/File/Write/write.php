
<?php


	//���д�ļ�

	//1.��ͳ�ķ���

	$file_path="test.txt";

	/* if(file_exists($file_path)){
		//�����׷�����ݣ���ʹ��a+ append 
		//�����ȫ�µ�д�뵽�ļ� ����ʹ�� w+ write
		$fp=fopen($file_path,"a+");
		$con="\r\n���a!";
		for($i=0;$i<10;$i++){
			fwrite($fp,$con);
		}

	}else{
	
	}
	echo "���ok";
	fclose($fp); */

	//2.�ڶ��ַ�ʽд���ļ�
 	$file_path="test.txt";
	$con="�������!\r\n";
	for($i=0;$i<10;$i++){
		$con.="�������!\r\n";
	}
	
	//���溯���Ƚϵ���,�벻Ҫѭ����ʹ�øú��� ��
	file_put_contents($file_path,$con,FILE_APPEND); //fopen fwrite fclose
	
	echo "ok";
?>
