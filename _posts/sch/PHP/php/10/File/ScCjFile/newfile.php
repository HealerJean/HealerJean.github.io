
<?php


	//�ļ����ļ��еĴ�����ɾ��.

	//1.�����ļ��� d:/shunping

	 //is_dir �ж��ǲ���һ��Ŀ¼
	/* if(!is_dir("d:/shunping2")){
		if(mkdir("d:/shunping2"))
		{
			echo "�����ļ���ok";
		}else{
			echo "�����ļ���err";
		}
	}else{
		echo "���ļ�������";	
	} */


	//2.�ܲ���һ���Զ���ļ�(�㼶),����ok
/* 	$path="d:/shunping3/aaa/bbb/cccc/ddd";
	if(!is_dir($path)){
		// 0777 ��һ��ģʽ ���һ�����־Ϳ���
		if( mkdir($path,0777,true)){
			echo "�����ļ���ok";
		}else{
			echo "�����ļ���err";
		} 
	}else{
		echo "���ļ�������";	
	} */

	//3.ɾ���ļ���
	//����ļ��������ļ�������Ŀ¼��������ɾ��ok
/* 	
	if(rmdir("d:/shunping2")){
		echo "ɾ���ļ���ok";
	}else{
		echo "err";
	} */

	//4.�ļ��Ĵ���
/* 	//��d:/shunping3 Ŀ¼�£�����һ���ļ���д��hello
	$file_path="d:/shunping3/aa.txt";
	$fp=fopen($file_path,"w+");

	fwrite($fp,"hello,world");

	fclose($fp);

	echo "���ļ�ok"; */

	//5.ɾ���ļ�

	$file_path="d:/shunping3/aa.txt";
	
	if(is_file($file_path)){
		
		if(unlink($file_path)){
			echo "ɾ��ok";
		}else{
			echo "ɾ��error";
		}
	}else{
		echo "�ļ�������";
	}

?>
