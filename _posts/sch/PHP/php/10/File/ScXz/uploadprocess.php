
<?php


	//1.�����ύ�ļ����û�
	$username=$_POST['username'];
	$fileintro=$_POST['fileintro'];

	echo $username.$fileintro;

	//����������Ҫʹ�õ� $_FILE
	echo "<pre>";
	print_r($_FILES);
	echo "</pre>";
	
 	//��ȡ�ļ��Ĵ�С
	$file_size=$_FILES['myfile']['size'];

	if($file_size>2*1024*1024){
		echo "�ļ�����,�����ϴ�����2m�ļ�";
		exit();
	}

	//��ȡ�ļ�������
	$file_type=$_FILES['myfile']['type'];
	if($file_type!='image/jpg' && $file_type!='image/pjpeg'){
		echo "�ļ�����ֻ���� jpg��";
		exit();
	}

	//�ж��Ƿ��ϴ�ok
	if(is_uploaded_file($_FILES['myfile']['tmp_name'])){
		//���ļ�ת�浽��ϣ����Ŀ¼
		$uploaded_file=$_FILES['myfile']['tmp_name'];
	//	$move_to_file=$_SERVER['DOCUMENT_ROOT']."/File/ScXz/file/".$_FILES['myfile']['name'];
	
	//echo $move_to_file;
		//��ʼ�ϴ��ļ�
	//���Ǹ�ÿ���û���̬�Ĵ���һ���ļ���
		$user_path=$_SERVER['DOCUMENT_ROOT']."/File/ScXz/file/".$username;
	//�����������
		$user_path=iconv("utf-8","gb2312",$user_path);
		//�жϸ��û��Ƿ��Ѿ����ļ��� 
		if(!file_exists($user_path)){
				
			mkdir($user_path);
		} 
		
		//��������в��ܽ�� ͬһ���û��ϴ���ͬ���ļ���
	//	$move_to_file=$user_path."/".$_FILES['myfile']['name'];
				//.time().rand(1,1000).substr($file_true_name,strrpos($file_true_name,"."));
		$file_true_name=$_FILES['myfile']['name'];
		
		// ȡ�ú�׺��   substr($file_true_name,strrpos($file_true_name,".")
		$move_to_file=$user_path."/".time().rand(1,1000).substr($file_true_name,strrpos($file_true_name,"."));
		
		
		echo $move_to_file;
		if(move_uploaded_file($uploaded_file,iconv("utf-8","gb2312",$move_to_file))){
			echo $_FILES['myfile']['name']."�ϴ�ok";
		}else{
			echo "�ϴ�ʧ��";
		}
		
	
	}else{
		echo "�ϴ�ʧ��";
	} 
	

?>
