<?php





//header("Location: �µ�ҳ�桱);
// ��ת����һ������
// 		header("Location: http://www.sohu.com");
// 		exit();

/* 	
	echo "hello";
			
    echo "<img src=��Sunset.jpg��  width=��100px��>";

 */

	//header(��Refresh: 3 ; url=http://www.sohu.com��);

//�������ת
//		header("Refresh: 3 ; url=http://localhost/FirstPhP/three/three.php");

    		

		
		
//ͨ��header�����û���(ajax )

/*
		header("Expires: -1");
		//Ϊ�˱�֤�����ԣ�������Ǹ���ʱ���ϵ����û��棬���������Ͳ�����ȫ��������
		header("Cache-Control: no_cache");
		header("Pragma: no-cache");
		
		echo "hello!cache";
		
*/		
		
	
		//�Ժ�����˵��
		//����˵�� $file_name �ļ���
		//		   $file_sub_dir: �����ļ�����·��	 '"/xxx/xxx/"
		function down_file($file_name,$file_sub_dir){
		
			
		
			//��ȥ��������ʾ����һ��ͼƬ.
			//����ļ�������.
				
			//ԭ�� php�ļ��������ȽϹ��ϣ���Ҫ������ת�� gb2312 ��������תΪ gb2312
			$file_name=iconv("utf-8","gb2312",$file_name);
		
			//���·��
			$path=".Http".$file_name;
			 
			//����·�� server ������һ����Ϣ�Ǳ�������� DOCUMENT_ROOT �� www �ĵĵ�ַ
			$file_path=$_SERVER['DOCUMENT_ROOT'].$file_sub_dir.$file_name;
				
		//	$file_path=$_SERVER['DOCUMENT_ROOT']."/Http/Http/Sunset.jpg";
			
			echo $_SERVER; 
			echo $file_path;
			//�����ϣ������·��
		 
		
			//1.���ļ�
		
			if(!file_exists($file_path)){
				echo "�ļ�������!";
				return ;
			}
		//����ļ� 
			$fp=fopen($file_path,"r");
		
		
			//��ȡ�����ļ��Ĵ�С
			$file_size=filesize($file_path);
		echo $file_size;
		/* 	if($file_size>30){
					
				echo "<script language='javascript'>window.alert('����')</script>";
				return ;
			} */
		
			//���ص��ļ�   
			header("Content-type: application/octet-stream");
			//�����ֽڴ�С����
			header("Accept-Ranges: bytes");
			//�����ļ���С 
			header("Accept-Length: $file_size");
			//����ͻ��˵ĵ����Ի��򣬶�Ӧ���ļ���
			header("Content-Disposition: attachment; filename=".$file_name);
		
		
			//��ͻ��˻�������
		
			$buffer=1024;
			//Ϊ�����صİ�ȫ�����������һ���ļ��ֽڶ�ȡ������
			$file_count=0;  
			//��仰�����ж��ļ��Ƿ����    feof($fp ���һ�ζ�������ʱ�������
			while(!feof($fp) && ($file_size-$file_count>0) ){
				   
				$file_data=fread($fp,$buffer);
				//ͳ�ƶ��˶��ٸ��ֽ� 
				$file_count+=$buffer;
				//�Ѳ������ݻ��͸������;
				echo $file_data;
			}
		
			//�ر��ļ�
			fclose($fp);
		
		}
		
		 
		//���Ժ����Ƿ����
		down_file("Sunset.jpg","/Http/Http/"); 
			


?>