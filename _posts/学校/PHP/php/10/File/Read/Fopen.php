<?php



	//1�ļ���Ϣ

	//���ļ�
	$file_path="test.txt";
	//�ú�������һ��ָ���ļ���ָ��
	
	if($fp=fopen($file_path,"r")){
		//fstat ͨ���Ѿ��򿪵��ļ���ָ�����ļ�����Ϣ
		
		$file_info=fstat($fp);
		echo "<pre>";
		print_r($file_info);
		echo "</pre>";

		//ȡ������
		echo "<br/>�ļ���С�� {$file_info['size']}";
		
		echo "<br/>�ļ��ϴ��޸�ʱ�� ".date("Y-m-d H:i:s)",$file_info['mtime']);
		//�ļ������ʵ�ʱ��  fstat 
		echo "<br/>�ļ��ϴη���ʱ�� ".date("Y-m-d H:i:s)",$file_info['atime']);
		echo "<br/>�ļ��ϴ�changeʱ�� ".date("Y-m-d H:i:s)",$file_info['ctime']);
	}else{
		echo "���ļ�ʧ��";
	}

	//�ر��ļ�!!!����Ҫ
//	fclose($fp);


	//***�ڶ��ַ�ʽ��ȡ�ļ���Ϣ

	echo "<br/>".filesize($file_path);
	echo "<br/>".date("Y-m-d H:i:s",fileatime($file_path));
	echo "<br/>".filectime($file_path);
	echo "<br/>".filemtime($file_path);


?>
