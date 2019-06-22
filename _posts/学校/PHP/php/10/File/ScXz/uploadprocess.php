
<?php


	//1.接收提交文件的用户
	$username=$_POST['username'];
	$fileintro=$_POST['fileintro'];

	echo $username.$fileintro;

	//我们这里需要使用到 $_FILE
	echo "<pre>";
	print_r($_FILES);
	echo "</pre>";
	
 	//获取文件的大小
	$file_size=$_FILES['myfile']['size'];

	if($file_size>2*1024*1024){
		echo "文件过大,不能上传大于2m文件";
		exit();
	}

	//获取文件的类型
	$file_type=$_FILES['myfile']['type'];
	if($file_type!='image/jpg' && $file_type!='image/pjpeg'){
		echo "文件类型只能是 jpg的";
		exit();
	}

	//判断是否上传ok
	if(is_uploaded_file($_FILES['myfile']['tmp_name'])){
		//把文件转存到你希望的目录
		$uploaded_file=$_FILES['myfile']['tmp_name'];
	//	$move_to_file=$_SERVER['DOCUMENT_ROOT']."/File/ScXz/file/".$_FILES['myfile']['name'];
	
	//echo $move_to_file;
		//开始上传文件
	//我们给每个用户动态的创建一个文件夹
		$user_path=$_SERVER['DOCUMENT_ROOT']."/File/ScXz/file/".$username;
	//解决中文乱码
		$user_path=iconv("utf-8","gb2312",$user_path);
		//判断该用户是否已经有文件夹 
		if(!file_exists($user_path)){
				
			mkdir($user_path);
		} 
		
		//下面的这行不能解决 同一个用户上传相同的文件名
	//	$move_to_file=$user_path."/".$_FILES['myfile']['name'];
				//.time().rand(1,1000).substr($file_true_name,strrpos($file_true_name,"."));
		$file_true_name=$_FILES['myfile']['name'];
		
		// 取得后缀名   substr($file_true_name,strrpos($file_true_name,".")
		$move_to_file=$user_path."/".time().rand(1,1000).substr($file_true_name,strrpos($file_true_name,"."));
		
		
		echo $move_to_file;
		if(move_uploaded_file($uploaded_file,iconv("utf-8","gb2312",$move_to_file))){
			echo $_FILES['myfile']['name']."上传ok";
		}else{
			echo "上传失败";
		}
		
	
	}else{
		echo "上传失败";
	} 
	

?>
